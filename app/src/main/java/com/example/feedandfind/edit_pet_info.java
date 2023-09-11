package com.example.feedandfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class edit_pet_info extends AppCompatActivity {

    Button button;

    //TODO: test this code: Get Image from Gallery and Camera-- nagawa ko na toh nalimutan ko jusq huhu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_info);

        button = findViewById(R.id.nxt_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_PetHealth();
            }
        });

    }

    private void open_PetHealth() {
        Intent intent= new Intent(this,Edit_pet_info_health_information.class);
    }
}