package com.example.feedandfind.Features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Adapter.RecordAdapter;
import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Feeder.FeederConfiguration;
import com.example.feedandfind.Features.Finder.GPSMap;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.Model.RecordModel;
import com.example.feedandfind.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {

    FeedAndFind feedAndFind;

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    LinearLayoutManager linearLayoutManager;
    FirebaseData firebaseData;

    // Navigation
    ImageView gpsIcon, feederIcon, reportIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        gpsIcon = findViewById(R.id.gps_icon);
        feederIcon = findViewById(R.id.feeder_icon);
        reportIcon = findViewById(R.id.report_icon);

        firebaseData  = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        generate_item();

        gpsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, GPSMap.class);
            startActivity(intent);
        });
        feederIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, FeederConfiguration.class);
            startActivity(intent);
        });
        reportIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, Reports.class);
            startActivity(intent);
        });
    }

    private void generate_item(){
        List<PetInformation> petInformationList = feedAndFind.getPetInformationList();
        if (petInformationList.size() > 0) {
            recordAdapter = new RecordAdapter(this, petInformationList);
            recyclerView.setAdapter(recordAdapter);
        } else {
            //display an empty message.
        }
    }
}