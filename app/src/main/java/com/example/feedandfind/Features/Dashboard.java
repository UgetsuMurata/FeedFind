package com.example.feedandfind.Features;

import android.os.Bundle;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Adapter.RecordAdapter;
import com.example.feedandfind.Model.RecordModel;
import com.example.feedandfind.NavigationMenu.NavigationDrawer;
import com.example.feedandfind.R;
import com.example.feedandfind.databinding.ActivityNavigationDrawerBinding;
import com.example.feedandfind.databinding.FeaturesDashboardBinding;

import java.util.ArrayList;


public class Dashboard extends NavigationDrawer {

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    LinearLayoutManager linearLayoutManager;

    FeaturesDashboardBinding featuresDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featuresDashboardBinding = FeaturesDashboardBinding.inflate(getLayoutInflater());
        setContentView(featuresDashboardBinding.getRoot());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        generate_item();

    }

    //Mock data only
    private void generate_item(){
        ArrayList<RecordModel> recordsList = new ArrayList<>();
        for(int i=0; i<50; i++){
            recordsList.add(new RecordModel("1","Miya" + (++i), "2 years 7 months and 12 days", "null"));
        }

        recordAdapter = new RecordAdapter(this, recordsList);
        recyclerView.setAdapter(recordAdapter);
    }
}