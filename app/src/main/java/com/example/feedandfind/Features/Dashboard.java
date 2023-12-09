package com.example.feedandfind.Features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Adapter.RecordAdapter;
import com.example.feedandfind.Features.Finder.GPSMap;
import com.example.feedandfind.Model.RecordModel;
import com.example.feedandfind.R;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    LinearLayoutManager linearLayoutManager;

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

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        generate_item();

        gpsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, GPSMap.class);
                startActivity(intent);
            }
        });
    }

    //Mock data only
    private void generate_item(){
        ArrayList<RecordModel> recordsList = new ArrayList<>();
        for(int i=0; i<3; i++){
            recordsList.add(new RecordModel("1","Miya" + (i), "2 years 7 months and 12 days", "null"));
        }

        recordAdapter = new RecordAdapter(this, recordsList);
        recyclerView.setAdapter(recordAdapter);
    }
}