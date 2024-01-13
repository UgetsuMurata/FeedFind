package com.example.feedandfind.Features.Feeder;

import static com.example.feedandfind.Features.Dashboard.isViewOutsideClicked;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.FunctionHelpers.CollarIDFunctions;
import com.example.feedandfind.FunctionHelpers.TimeCode;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class General extends AppCompatActivity {

    FeedAndFind feedAndFind;
    RecyclerView recyclerView;
    ArrayList<FeedItem> arrayList;  // Change the type to FeedItem
    LinearLayoutManager linearLayoutManager;
    LinearLayout hasFeeder, hasNoFeeder;
    MyAdapter myAdapter;
    ConstraintLayout parentLayout;
    FloatingActionButton moreOptions, clickFeeder, clickScan;
    LinearLayout groupedFAB, feederLogsFAB, scanFAB;
    TextView feederFABText;
    Boolean showingMoreOptions = false;

    TextView feederContent, nextSchedTime;
    HashMap<String, String> nextSchedPet = new HashMap<>();
    String nextSchedTimeString = "12:00AM";
    String feederContentString = "0.0%";
    FirebaseData firebaseData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_feeder_general);

        recyclerView = findViewById(R.id.horizontalView);
        parentLayout = findViewById(R.id.parent_layout);
        hasFeeder = findViewById(R.id.has_feeder);
        hasNoFeeder = findViewById(R.id.has_no_feeder);
        moreOptions = findViewById(R.id.fab);
        groupedFAB = findViewById(R.id.fab_group);
        feederLogsFAB = findViewById(R.id.feeder);
        scanFAB = findViewById(R.id.scan);
        clickFeeder = findViewById(R.id.clickable_feeder);
        clickScan = findViewById(R.id.clickable_scan);
        feederFABText = findViewById(R.id.feeder_fab_text);
        feederContent = findViewById(R.id.feeder_content);
        nextSchedTime = findViewById(R.id.next_sched_time);

        firebaseData = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();

        // fabs
        if (feedAndFind.FEEDER_CODE != null) {
            feederFABText.setText("Change Feeder");
            hasFeeder.setVisibility(View.VISIBLE);
            hasNoFeeder.setVisibility(View.GONE);
        } else {
            hasFeeder.setVisibility(View.GONE);
            hasNoFeeder.setVisibility(View.VISIBLE);
        }
        moreOptions.setOnClickListener(view -> {
            if (!showingMoreOptions) {
                // if going to show more options
                openFABMenu();
            } else {
                // if going to hide more options
                closeFABMenu();
            }
        });
        scanFAB.setOnClickListener(view -> {
            closeFABMenu();
            Intent intent = new Intent(General.this, FeederAdd.class);
            setupFeeder.launch(intent);
        });
        feederLogsFAB.setOnClickListener(view -> {
            closeFABMenu();
            startActivity(new Intent(General.this, Logs.class));
        });
        clickScan.setOnClickListener(view -> scanFAB.performClick());
        clickFeeder.setOnClickListener(view -> feederLogsFAB.performClick());
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isViewOutsideClicked(groupedFAB, event)) {
                    closeFABMenu();
                    return true;  // Consume the event to prevent it from propagating further
                }
                return false; // Allow the event to propagate if the click is inside your grouped views
            }
        });

        final Handler handler = new Handler();
        Calendar calendar = Calendar.getInstance();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // retrieve feeder content data.
                firebaseData.retrieveData(General.this, "PetFeeder/" + feedAndFind.FEEDER_CODE + "/feeder_contents", new FirebaseData.FirebaseDataCallback() {
                    @Override
                    public void onDataReceived(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            feederContentString = String.format(Locale.getDefault(),
                                    "%.1f%%",
                                    Float.valueOf(dataSnapshot.getValue().toString()));
                        }
                    }
                });
                // retrieve next schedule
                Integer findForAfter = (calendar.get(Calendar.HOUR_OF_DAY) * 100) + calendar.get(Calendar.MINUTE);
                firebaseData.retrieveData(General.this, "PetFeeder/"+feedAndFind.FEEDER_CODE+"/const_schedules", new FirebaseData.FirebaseDataCallback() {
                    @Override
                    public void onDataReceived(DataSnapshot dataSnapshot) {
                        boolean found = false;
                        nextSchedPet.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            Object timeObject = ds.getKey();
                            if (timeObject != null) {
                                String time = timeObject.toString();
                                if (Integer.parseInt(time) > findForAfter){
                                    nextSchedTimeString = TimeCode.timeCodeToTime(time);
                                    for (DataSnapshot data : ds.getChildren()){
                                        Object petId = data.getKey();
                                        Object grams = data.getValue();
                                        if (petId != null && grams != null) {
                                            nextSchedPet.put(petId.toString(), grams.toString());
                                        }
                                    }
                                    found = true;
                                    break; // this is the only data to be displayed for now.
                                }
                            }
                        }
                        if (!found){
                            nextSchedTimeString = "Tomorrow";
                        }
                        updateUI();
                    }
                });

                // update data for every second.
                handler.postDelayed(this, 1000);
            }
        }, 200);
    }

    private void openFABMenu() {
        feederLogsFAB.setVisibility(View.VISIBLE);
        feederLogsFAB.setAlpha(0.0f);
        feederLogsFAB.setTranslationY(moreOptions.getHeight());
        scanFAB.setVisibility(View.VISIBLE);
        scanFAB.setAlpha(0.0f);
        scanFAB.setTranslationY(moreOptions.getHeight());
        feederLogsFAB.animate()
                .translationY(0.0f)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        feederLogsFAB.setAlpha(1.0f);
                        feederLogsFAB.setTranslationY(0.0f);
                    }
                });
        scanFAB.animate()
                .translationY(0.0f)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        scanFAB.setAlpha(1.0f);
                        scanFAB.setTranslationY(0.0f);
                    }
                });
        moreOptions.animate().rotation(45);
        showingMoreOptions = true;
    }

    private void closeFABMenu() {
        feederLogsFAB.animate()
                .translationY(moreOptions.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        feederLogsFAB.setVisibility(View.GONE);
                    }
                });
        scanFAB.animate()
                .translationY(moreOptions.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        scanFAB.setVisibility(View.GONE);
                    }
                });
        moreOptions.animate().rotation(0);
        showingMoreOptions = false;
    }
    private void updateUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                feederContent.setText(feederContentString);
                nextSchedTime.setText(nextSchedTimeString);
                setupScheduleDisplays();
            }
        });
    }

    private void setupScheduleDisplays(){
        arrayList = new ArrayList<>();

        for (String id : nextSchedPet.keySet()) {
            PetInformation petInformation = CollarIDFunctions.findPetInformation(id);
            if (petInformation != null) {
                arrayList.add(new FeedItem(Math.toIntExact(petInformation.getImage()), petInformation.getName()));
            }
        }
        linearLayoutManager = new LinearLayoutManager(General.this, LinearLayoutManager.HORIZONTAL, false);
        myAdapter = new MyAdapter(arrayList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private final ActivityResultLauncher<Intent> setupFeeder = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // setup screen for qr.
                        feederFABText.setText("Change Feeder");
                        hasNoFeeder.setVisibility(View.GONE);
                        hasFeeder.setVisibility(View.VISIBLE);
                    }
                }
            });

    private static class FeedItem {
        private final int imageResource;
        private final String petName;

        public FeedItem(int imageResource, String petName) {
            this.imageResource = imageResource;
            this.petName = petName;
        }

        public int getImage() {
            return imageResource;
        }

        public String getName() {
            return petName;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        ArrayList<FeedItem> data;  // Change the type to FeedItem

        public MyAdapter(ArrayList<FeedItem> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(General.this).inflate(R.layout.features_feeder_general_hzn_row, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            FeedItem item = data.get(position);

            // Set image resource
            holder.petFace.setImageResource(item.getImage());

            // Set grams text
            holder.petName.setText(item.getName());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ImageView petFace;
            TextView petName;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                petFace = itemView.findViewById(R.id.pet_face);
                petName = itemView.findViewById(R.id.pet_name);
            }
        }
    }
}
