package com.example.feedandfind.Features.Feeder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.FunctionHelpers.TimeCode;
import com.example.feedandfind.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Logs extends AppCompatActivity {

    private final ArrayList<LogsTime> logsEntry = new ArrayList<>();
    private RecyclerView logs;
    LinearLayoutManager linearLayoutManager;
    MyAdapter myAdapter;
    FeedAndFind feedAndFind;
    FirebaseData firebaseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_feeder_logs);
        logs = findViewById(R.id.logs);

        firebaseData = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();
        getLogsEntry();
    }

    private void getLogsEntry(){
        firebaseData.retrieveData(this, "PetFeeder/" + feedAndFind.FEEDER_CODE + "/device_logs", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot times : dataSnapshot.getChildren()){
                    Object time = times.getKey();
                    if (time != null) {
                        logsEntry.add(new LogsTime(TimeCode.timeCodeToTime(time.toString())));
                    }
                }
                linearLayoutManager = new LinearLayoutManager(Logs.this, LinearLayoutManager.VERTICAL, true);
                myAdapter = new MyAdapter(logsEntry);
                logs.setAdapter(myAdapter);
                logs.setLayoutManager(linearLayoutManager);
            }
        });
    }

    private static class LogsTime{
        String time;

        public LogsTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<Logs.MyAdapter.MyHolder> {

        ArrayList<Logs.LogsTime> data;  // Change the type to FeedItem

        public MyAdapter(ArrayList<Logs.LogsTime> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public Logs.MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Logs.this).inflate(R.layout.features_feeder_logs_items, parent, false);
            return new Logs.MyAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Logs.MyAdapter.MyHolder holder, int position) {
            Logs.LogsTime item = data.get(position);

            // Set image resource
            holder.time.setText(item.getTime());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView time;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                time = itemView.findViewById(R.id.time);
            }
        }
    }
}