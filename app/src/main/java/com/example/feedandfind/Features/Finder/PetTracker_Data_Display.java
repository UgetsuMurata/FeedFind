package com.example.feedandfind.Features.Finder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Pets.PetsEdit;
import com.example.feedandfind.R;

public class PetTracker_Data_Display extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressText;
    int i= 0;
    ImageView openMenu;
    TextView toolbarTitle;
    FirebaseData firebaseData;
    String CollarId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feautres_finder_pet_tracker_data_display);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
        openMenu = findViewById(R.id.open_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        firebaseData = new FirebaseData();
        CollarId = getIntent().getStringExtra("COLLAR_ID");

        openMenu.setOnClickListener(this::showPopupMenu);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){

                if (i <= 100) {

                    progressText.setText(""+i);
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this, 200);
                }
                else{
                    handler.removeCallbacks(this);
                }

            }
        }, 200);

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.pet_tracker_display_menu); // Replace with your menu resource

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_pet:
                        Intent intent = new Intent(PetTracker_Data_Display.this, PetsEdit.class);
                        intent.putExtra("COLLAR_ID", CollarId);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}

