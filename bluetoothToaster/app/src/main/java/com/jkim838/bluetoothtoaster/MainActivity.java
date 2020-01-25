package com.jkim838.bluetoothtoaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;

public class MainActivity extends AppCompatActivity implements NoticeListener{

    private static BluetoothLeAdvertiser advertiser =
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
    private AdvertiseCallback advertiseCallback = new AdvertiseCallback()
    {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect)
        {
            super.onStartSuccess(settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode)
        {
            Log.e("ADV","Advertisement Failure:" + errorCode);
            super.onStartFailure(errorCode);
        }
    };
    private static BluetoothManager manager;
    private static BluetoothGattServer server;
    private final BluetoothGattServerCallback serverCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.i("CON", "CONNECTION CHANGED");
            switch (newState)
            {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("CON", "Device connected: " + device.getAddress());
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.i("DC", "Device disconnected");
                    break;

            }
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            server.sendResponse(device, requestId, GATT_SUCCESS, offset, characteristic.getValue());
            Log.i("CHR", "Characteristic was read");
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            server.sendResponse(device, requestId, GATT_SUCCESS, offset, value);
            characteristic.setValue(value);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
        }
    };
    TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        checkPermission(this);
        advertise();
        Toast.makeText(this, "Advertising", Toast.LENGTH_SHORT).show();
        initServer();

        NotificationManager noticeManager =
               (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder noticeBuilder =
                new NotificationCompat.Builder(MainActivity.this, "default");
        noticeBuilder.setContentTitle("Notifications");
        noticeBuilder.setContentText("Notification Listener");
        noticeBuilder.setTicker("Notification Listener");
        noticeBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        noticeBuilder.setAutoCancel(true);
        new notificationListener().setListener(this);
        NotificationChannel channel =
                new NotificationChannel("10001",
                        "CHANNEL_NAME",
                       NotificationManager.IMPORTANCE_HIGH);
        noticeBuilder.setChannelId("10001");
        assert noticeManager != null;
        Intent noticeIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(noticeIntent);
        noticeManager.notify((int) System.currentTimeMillis(), noticeBuilder.build());

    }

    private void advertise()
    {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW)
                .setConnectable(true)
                .build();
        ParcelUuid serviceUuid = new ParcelUuid(UUID.fromString(getString(R.string.serviceUuid)));
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(serviceUuid)
                .build();
        advertiser.startAdvertising(settings, data, advertiseCallback);
    }
    private void initServer()
    {
        manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        server = manager.openGattServer(this, serverCallback);
    }
    private void checkPermission(AppCompatActivity thisActivity)
    {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.BLUETOOTH);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        for(String thisPermission:permissions)
        {
            switch(ContextCompat.checkSelfPermission(thisActivity, thisPermission))
            {
                case PackageManager.PERMISSION_GRANTED:
                    Log.w("PERMISSION", thisPermission.toString() + ": permission Granted");
                    break;
                case PackageManager.PERMISSION_DENIED:
                    Log.w("PERMISSION", "Needs Permission: " + thisPermission.toString());
                    // List of permissions to ask
                    String[] permissionRequired = {Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION};
                    // Android will automatically only ask permissions that are not granted
                    ActivityCompat.requestPermissions(thisActivity, permissionRequired, 1); // requires all permissions
                    break;
            }
        }
    }

    @Override
    public void setValue(String packageName) {
        Log.i("RESULT", "CONTENT: " + packageName);
        display.append("\n" + packageName);
    }
}
