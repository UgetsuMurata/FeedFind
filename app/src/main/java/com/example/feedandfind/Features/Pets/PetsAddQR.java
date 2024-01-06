package com.example.feedandfind.Features.Pets;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.ColorHelpers.ThemedColor;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Dashboard;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.Result;

import java.util.Objects;

public class PetsAddQR extends AppCompatActivity {

    CodeScannerView scannerView;
    CodeScanner codeScanner;
    TextView scannerLabel;

    Integer CAMERA_PERMISSION_REQUEST_CODE = 1;
    Boolean scannerState = false;

    FeedAndFind feedAndFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_pets_petsaddqr);

        scannerView = findViewById(R.id.qr_scanner);
        scannerLabel = findViewById(R.id.qr_results);

        feedAndFind = FeedAndFind.getInstance();

        scannerLabel.setText("Scanning...");
        scannerLabel.setTextColor(ThemedColor.getColorStateList(this, R.attr.text));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else setupScanner();
    }

    private void setupScanner(){
        scannerState = true;
        codeScanner = new CodeScanner(this, scannerView);
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseData fd = new FirebaseData();
                        fd.retrieveData(PetsAddQR.this, "PetTracker/", new FirebaseData.FirebaseDataCallback() {
                            @Override
                            public void onDataReceived(DataSnapshot dataSnapshot) {
                                boolean exists = false;
                                String id = "";
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String childKey = childSnapshot.getKey();
                                    if (Objects.equals(childKey, result.toString())) {
                                        exists = true;
                                        id = childKey;
                                        break;
                                    }
                                }
                                Handler handler = new Handler();
                                if (exists){
                                    for (PetInformation petInformation : feedAndFind.getPetInformationList()) {
                                        if (petInformation.getKey().equals(id)){
                                            scannerLabel.setText("Valid Scan: Device already in account!");
                                            scannerLabel.setTextColor(PetsAddQR.this.getResources().getColorStateList(R.color.green, getTheme()));
                                            handler.postDelayed(() -> {
                                                getOnBackPressedDispatcher().onBackPressed();
                                            }, 3000);
                                        }
                                    }
                                    scannerLabel.setText("Valid Scan: Device Added!");
                                    scannerLabel.setTextColor(PetsAddQR.this.getResources().getColorStateList(R.color.green, getTheme()));

                                    String finalId = id;
                                    handler.postDelayed(() -> {
                                        Intent returnIntent = new Intent(PetsAddQR.this, PetsAdd.class);
                                        returnIntent.putExtra("QR_ID", finalId);
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                    }, 3000);
                                } else {
                                    scannerLabel.setText("Invalid Scan: Device Not Found");
                                    scannerLabel.setTextColor(PetsAddQR.this.getResources().getColorStateList(R.color.red, getTheme()));
                                    handler.postDelayed(() -> {
                                        codeScanner.startPreview();
                                        scannerLabel.setText("Try again.");
                                    }, 3000);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE & resultCode == RESULT_OK){
            setupScanner();
        } else {
            Toast.makeText(this, "Camera permission is required to access this feature.", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (scannerState) codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (scannerState) codeScanner.releaseResources();
        super.onPause();
    }
}