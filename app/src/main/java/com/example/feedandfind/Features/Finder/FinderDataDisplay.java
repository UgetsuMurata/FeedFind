package com.example.feedandfind.Features.Finder;

import static com.example.feedandfind.FunctionHelpers.CollarIDFunctions.findPetInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.firebase.database.DataSnapshot;

import java.util.Locale;
import java.util.Objects;

public class FinderDataDisplay extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressText, activeStatus;
    private TextView petMovement, temperature;
    private ImageView petMovementIcon, temperatureIcon;
    private LinearLayout showTempWarning;
    private TextView dismissWarning;
    TextView toolbarTitle;
    FirebaseData firebaseData;
    String CollarId;

    FeedAndFind feedAndFind;
    PetInformation petInformation;

    Integer stepsProgress = 0;
    String commentary;
    String petMovementState;
    Float temperatureValue = 0.0f;
    Integer currentTempMode = 0;
    Integer prevTempMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_finder_pet_tracker_data_display);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
        activeStatus = findViewById(R.id.active_status);
        petMovement = findViewById(R.id.pet_movement);
        petMovementIcon = findViewById(R.id.pet_movement_icon);
        temperature = findViewById(R.id.temperature);
        temperatureIcon = findViewById(R.id.temperature_icon);
        showTempWarning = findViewById(R.id.show_temp_warning);
        dismissWarning = findViewById(R.id.dismiss_warning);

        toolbarTitle = findViewById(R.id.toolbar_title);
        firebaseData = new FirebaseData();
        CollarId = getIntent().getStringExtra("COLLAR_ID");
        feedAndFind = FeedAndFind.getInstance();
        petInformation = new PetInformation();

        commentary = getResources().getString(R.string.steps_commentary_0);
        petMovementState = getResources().getString(R.string.movement_idle);

        petInformation = findPetInformation(CollarId);
        if (petInformation == null) {
            getOnBackPressedDispatcher().onBackPressed();
            Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
        }

        toolbarTitle.setText(petInformation.getName());

        showTempWarning.setVisibility(View.GONE);
        dismissWarning.setOnClickListener(view -> showTempWarning.setVisibility(View.GONE));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                firebaseData.retrieveData(FinderDataDisplay.this, String.format("PetTracker/%s", CollarId), new FirebaseData.FirebaseDataCallback() {
                    @Override
                    public void onDataReceived(DataSnapshot dataSnapshot) {
                        // update progressbar
                        Object progressObject = dataSnapshot.child("/current_pedometer_data/steps_count").getValue();
                        if (progressObject != null){
                            stepsProgress = Integer.valueOf(progressObject.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(stepsProgress!=null?stepsProgress:0);
                                progressText.setText(stepsProgress!=null?String.valueOf(stepsProgress):"0");
                            }
                        });

                        // update commentary
                        commentary = getResources().getString(R.string.steps_commentary_0);
                        Log.d("Progress Object", "Steps Progress: " + (stepsProgress!=null?stepsProgress:"NULL"));
                        if (stepsProgress > 0 && stepsProgress <= 100){
                            commentary = getResources().getString(R.string.steps_commentary_100);
                        } else if (stepsProgress > 100 && stepsProgress <= 500) {
                            commentary = getResources().getString(R.string.steps_commentary_500);
                        } else if (stepsProgress > 500 && stepsProgress <= 750) {
                            commentary = getResources().getString(R.string.steps_commentary_750);
                        } else if (stepsProgress > 750 && stepsProgress <= 1000) {
                            commentary = getResources().getString(R.string.steps_commentary_1000);
                        } else if (stepsProgress > 1000){
                            commentary = getResources().getString(R.string.steps_commentary_more);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activeStatus.setText(commentary);
                            }
                        });

                        // update current movement card
                        Object petState = dataSnapshot.child("/current_pedometer_data/pet_state").getValue();
                        if (petState != null){
                            petMovementState = petState.toString();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                petMovement.setText(petMovementState);
                                if (getResources().getString(R.string.movement_idle).equals(petMovementState)) {
                                    petMovementIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dog_idle, getTheme()));
                                } else if (getResources().getString(R.string.movement_walking).equals(petMovementState)) {
                                    petMovementIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dog_walking, getTheme()));
                                } else if (getResources().getString(R.string.movement_running).equals(petMovementState)) {
                                    petMovementIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dog_running, getTheme()));
                                }
                            }
                        });

                        // update temperature card
                        Object currentTemp = dataSnapshot.child("/current_temperature").getValue();
                        if (currentTemp != null){
                            temperatureValue = Float.valueOf(currentTemp.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                temperature.setText(String.format(Locale.getDefault(), "%.2f°C", temperatureValue));
                                prevTempMode = currentTempMode;
                                if (temperatureValue < 20.0f) {
                                    currentTempMode = 0;
                                    temperatureIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.temp_low, getTheme()));
                                } else if (temperatureValue > 25.0f) {
                                    currentTempMode = 2;
                                    temperatureIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.temp_high, getTheme()));
                                } else {
                                    currentTempMode = 1;
                                    temperatureIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.temp_mid, getTheme()));
                                }
                                if (!Objects.equals(currentTempMode, prevTempMode) && currentTempMode == 2){
                                    // it is hot, so show warning.
                                    showTempWarning.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
                // trigger for next second
                handler.postDelayed(this, 1000);
            }
        }, 200);

    }
}

