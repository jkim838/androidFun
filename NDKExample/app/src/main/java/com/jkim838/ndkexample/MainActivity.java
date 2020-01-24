package com.jkim838.ndkexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Load the native library
    static
    {
        System.loadLibrary("native-lib");
    }
    // Declare native method (implemented in C++)
    public static native String showCookie();
    public static native int bakeCookie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView cookieCount = findViewById(R.id.cookies);
        Button getCookie = findViewById(R.id.getCookie);
        getCookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bakeCookie();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String cookiesCount = showCookie();
                        Log.i("COOKIE", "COUNT:"+cookiesCount);
                        cookieCount.setText(cookiesCount);
                    }
                });
            }
        });
    }
}
