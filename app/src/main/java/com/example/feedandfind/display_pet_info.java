package com.example.feedandfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class display_pet_info extends AppCompatActivity {

    Button nav_petProfile,nav_gps,nav_feed,nav_statistics;
    ImageButton imageButton;


    //TODO:check the code --- Retrive the User input and display the Pet Information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pet_info);

        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageButton();
            }
        });

    }

    private void onClickImageButton() {
        Intent intent = new Intent(this,edit_pet_info.class);
        startActivity(intent);
    }
}