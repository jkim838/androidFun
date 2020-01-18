package com.example.apptoapp_server;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Package manager helps grabbing app packages installed on the device
        PackageManager appPackage = getPackageManager(); // getPackageManager() needs a context
        // Generate an intent to launch an app
        final Intent launchIntent = appPackage.getLaunchIntentForPackage("com.example.apptoapp");
        Button launchApp = findViewById(R.id.button);
        launchApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(launchIntent!=null)
                {
                    startActivity(launchIntent); // start an app
                    finishAndRemoveTask(); // kill current activity
                }
            }
        });
    }
}
