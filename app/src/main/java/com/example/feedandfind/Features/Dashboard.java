package com.example.feedandfind.Features;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Adapter.RecordAdapter;
import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Feeder.General;
import com.example.feedandfind.Features.Pets.PetsAdd;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {

    FeedAndFind feedAndFind;

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    LinearLayout parentLayout;
    LinearLayoutManager linearLayoutManager;
    LinearLayout noPet;
    FirebaseData firebaseData;
    FloatingActionButton moreOptions, clickFeeder, clickScan;
    LinearLayout groupedFAB, feederFAB, scanFAB;
    Boolean showingMoreOptions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        noPet = findViewById(R.id.no_pets);
        moreOptions = findViewById(R.id.fab);
        feederFAB = findViewById(R.id.feeder);
        scanFAB = findViewById(R.id.scan);
        clickFeeder = findViewById(R.id.clickable_feeder);
        clickScan = findViewById(R.id.clickable_scan);
        parentLayout = findViewById(R.id.dashboard_layout);
        groupedFAB = findViewById(R.id.fab_group);

        firebaseData = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

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
            startActivity(new Intent(Dashboard.this, PetsAdd.class));
        });
        feederFAB.setOnClickListener(view -> {
            closeFABMenu();
            startActivity(new Intent(Dashboard.this, General.class));
        });
        clickScan.setOnClickListener(view -> scanFAB.performClick());
        clickFeeder.setOnClickListener(view -> feederFAB.performClick());

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
        generate_item();
    }

    private void openFABMenu() {
        feederFAB.setVisibility(View.VISIBLE);
        feederFAB.setAlpha(0.0f);
        feederFAB.setTranslationY(moreOptions.getHeight());
        scanFAB.setVisibility(View.VISIBLE);
        scanFAB.setAlpha(0.0f);
        scanFAB.setTranslationY(moreOptions.getHeight());
        feederFAB.animate()
                .translationY(0.0f)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        feederFAB.setAlpha(1.0f);
                        feederFAB.setTranslationY(0.0f);
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
        feederFAB.animate()
                .translationY(moreOptions.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        feederFAB.setVisibility(View.GONE);
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

    private void generate_item() {
        List<PetInformation> petInformationList = feedAndFind.getPetInformationList();
        if (petInformationList.size() > 0) {
            noPet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recordAdapter = new RecordAdapter(this, petInformationList, new RecordAdapter.RecordCallback() {
                @Override
                public void onDeleteCallback() {
                    onResume();
                }
            });
            recyclerView.setAdapter(recordAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            noPet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        generate_item();
    }

    public static boolean isViewOutsideClicked(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        return x < location[0] || x > (location[0] + view.getWidth()) || y < location[1] || y > (location[1] + view.getHeight());
    }
}