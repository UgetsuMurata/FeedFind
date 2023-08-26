package com.example.feedandfind.LoadingScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.feedandfind.R;

public class MainActivity extends AppCompatActivity {

    /*
    * This MainActivity will be used for the loading screen.
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingscreen_mainactivity);
    }
}