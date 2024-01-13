package com.example.feedandfind.LoadingScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.CustomViews.PetLoadingBar;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Dashboard;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    /*
    * This MainActivity will be used for the loading screen.
    * */

    PetLoadingBar display;
    TextView label;

    FirebaseData firebaseData;
    FeedAndFind feedAndFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingscreen_mainactivity);

        display = findViewById(R.id.loading_display);
        label = findViewById(R.id.loading_label);

        firebaseData = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();

        final String Process1 = "Retrieving All Pets.";
        final String Process2 = "Retrieving Feeder Information";
        final String Process3 = "Merging All Devices.";

        ArrayList<String> loadingProcesses = new ArrayList<>(Arrays.asList(
                Process1,
                Process2,
                Process3
        ));
        display.setMax(loadingProcesses.size());
        new Thread(() -> {
            AtomicInteger process_number = new AtomicInteger(0);
            for (String process : loadingProcesses) {
                process_number.getAndIncrement();
                runOnUiThread(() -> {
                    label.setText(String.format("%s... %.0f%%", process, ((float) process_number.get() / loadingProcesses.size())*100));
                    display.setProgress(process_number.get());
                });
                switch (process){
                    case Process1:
                        Process1();
                        break;
                    case Process2:
                        Process2();
                        break;
                    case Process3:
                        Process3();
                        break;
                }
            }
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }).start();
    }

    private void Process1(){
        long timeStarted = System.currentTimeMillis();
        CompletableFuture<Void> future = new CompletableFuture<>();
        List<PetInformation> petInformationList = new ArrayList<>();
        firebaseData.retrieveData(this, "Users/"+feedAndFind.APP_CODE+"/PetTrackerQrCodes", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    PetInformation petInformation = new PetInformation();
                    petInformation.setKey(ds.getKey());
                    Object value = ds.child("image").getValue();
                    if (value != null){
                        petInformation.setImage((Long) value);
                    }
                    value = ds.child("name").getValue();
                    if (value != null){
                        petInformation.setName(value.toString());
                    }
                    value = ds.child("birthday").getValue();
                    if (value != null){
                        petInformation.setBirthday(value.toString());
                    }
                    value = ds.child("weight").getValue();
                    if (value != null){
                        petInformation.setWeight(value.toString());
                    }
                    value = ds.child("sex").getValue();
                    if (value != null){
                        petInformation.setSex(value.toString());
                    }
                    value = ds.child("allergies").getValue();
                    if (value != null){
                        petInformation.setAllergies(value.toString());
                    }
                    value = ds.child("medication").getValue();
                    if (value != null){
                        petInformation.setMedication(value.toString());
                    }
                    value = ds.child("vetName").getValue();
                    if (value != null){
                        petInformation.setVetName(value.toString());
                    }
                    value = ds.child("vetPhoneNumber").getValue();
                    if (value != null){
                        petInformation.setPhone(value.toString());
                    }
                    petInformationList.add(petInformation);
                }
                future.complete(null);
            }
        });

        try {
            future.get(); // This will block until the callback is done
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        feedAndFind.setPetInformationList(petInformationList);
        while (System.currentTimeMillis()-timeStarted<1000);
    }
    private void Process2(){
        long timeStarted = System.currentTimeMillis();
        firebaseData.retrieveData(this, "Users/" + feedAndFind.APP_CODE + "/PetFeederQrCode", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    feedAndFind.setFEEDER_CODE(dataSnapshot.getValue().toString());
                }
            }
        });
        //PROCESS 2
        while (System.currentTimeMillis()-timeStarted<1000);
    }
    private void Process3(){
        long timeStarted = System.currentTimeMillis();
        //PROCESS 3
        while (System.currentTimeMillis()-timeStarted<500);
    }
}