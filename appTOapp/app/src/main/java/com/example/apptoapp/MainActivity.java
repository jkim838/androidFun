package com.example.apptoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int cookies = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView cookieCount = findViewById(R.id.mainText);
        Button cookieButton = findViewById(R.id.cookies);
        cookieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookies++;
                cookieCount.setText(String.valueOf(cookies));
            }
        });

    }

    // https://inducesmile.com/android/android-camera2-api-example-tutorial/
}
