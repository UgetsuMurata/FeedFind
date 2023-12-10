package com.example.feedandfind.Features.Pets;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PetsAdd extends AppCompatActivity {

    CardView refreshImage, scanButton, addPet;
    ImageView dogImage;
    TextView scanStatus;
    TextInputEditText petName, petBirthday, petWeight, petSex, petAllergies, petMedication,
            vetName, vetPhoneNumber;
    String CollarKey;

    FeedAndFind feedAndFind;

    private final ActivityResultLauncher<Intent> petsAddQRCaller = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            updateQR(intent.getStringExtra("QR_ID"));
                        }
                    }
                }
            });

    Integer currentImage = R.drawable.face3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_pets_petsadd);

        refreshImage = findViewById(R.id.refresh_image);
        dogImage = findViewById(R.id.dog_image);
        scanButton = findViewById(R.id.scan_button);
        scanStatus = findViewById(R.id.scan_status);
        petName = findViewById(R.id.pet_name);
        petBirthday = findViewById(R.id.pet_birthday);
        petWeight = findViewById(R.id.pet_weight);
        petSex = findViewById(R.id.pet_sex);
        petAllergies = findViewById(R.id.pet_allergies);
        petMedication = findViewById(R.id.pet_medication);
        vetName = findViewById(R.id.vet_name);
        vetPhoneNumber = findViewById(R.id.vet_phone_number);
        addPet = findViewById(R.id.add_pet);

        feedAndFind = FeedAndFind.getInstance();

        refreshImage.setOnClickListener(view -> refreshImage());
        scanButton.setOnClickListener(view -> scanQR());
        petBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    showDatePickerDialog();
                }
            }
        });
        addPet.setOnClickListener(view -> addPet());
    }

    private void refreshImage(){
        Random random = new Random();
        List<Integer> resources = new ArrayList<>();
        resources.add(R.drawable.face1);
        resources.add(R.drawable.face2);
        resources.add(R.drawable.face3);
        resources.add(R.drawable.face4);
        resources.add(R.drawable.face5);
        resources.add(R.drawable.face6);
        resources.remove(currentImage);
        currentImage = resources.get(random.nextInt(resources.size()));
        dogImage.setImageDrawable(ResourcesCompat.getDrawable(this.getResources(), currentImage, getTheme()));
    }

    private void scanQR(){
        Intent intent = new Intent(this, PetsAddQR.class);
        petsAddQRCaller.launch(intent);
    }

    private void updateQR(String key){
        CollarKey = key;
        scanStatus.setText(String.format("Device ID: %s", key));
    }

    private void showDatePickerDialog() {
        hideKeyboard(petBirthday);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Handle the date selection
                        @SuppressLint("DefaultLocale")
                        String selectedDate = String.format("%02d-%02d-%04d",
                                monthOfYear + 1,
                                dayOfMonth,
                                year);
                        petBirthday.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Set a limit for the date picker (optional)
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        view.clearFocus();
    }

    private void addPet(){
        if (verifyInformation()){
            FirebaseData firebaseData = new FirebaseData();
            Map<String, Object> values = new HashMap<>();
            values.put("image", currentImage);
            values.put("name", Objects.requireNonNull(petName.getText()).toString());
            values.put("birthday", Objects.requireNonNull(petBirthday.getText()).toString());
            values.put("weight", petWeight.getText() != null ? petWeight.getText().toString() : "");
            values.put("sex", petSex.getText() != null ? petSex.getText().toString() : "");
            values.put("allergies", petAllergies.getText() != null ? petAllergies.getText().toString() : "");
            values.put("medication", petMedication.getText() != null ? petMedication.getText().toString() : "");
            values.put("vetName", vetName.getText() != null ? vetName.getText().toString() : "");
            values.put("vetPhoneNumber", vetPhoneNumber.getText() != null ? vetPhoneNumber.getText().toString() : "");
            firebaseData.addValues("Users/"+feedAndFind.APP_CODE+"/PetFeederQrCodes/"+CollarKey, values);
            Toast.makeText(this, "New pet added!", Toast.LENGTH_SHORT).show();

            PetInformation petInformation = new PetInformation();
            petInformation.setKey(CollarKey);
            petInformation.setImage(Long.parseLong(String.valueOf(currentImage)));
            petInformation.setName(Objects.requireNonNull(petName.getText()).toString());
            petInformation.setBirthday(Objects.requireNonNull(petBirthday.getText()).toString());
            petInformation.setWeight(petWeight.getText() != null ? petWeight.getText().toString() : "");
            petInformation.setSex(petSex.getText() != null ? petSex.getText().toString() : "");
            petInformation.setAllergies(petAllergies.getText() != null ? petAllergies.getText().toString() : "");
            petInformation.setMedication(petMedication.getText() != null ? petMedication.getText().toString() : "");
            petInformation.setVetName(vetName.getText() != null ? vetName.getText().toString() : "");
            petInformation.setPhone(vetPhoneNumber.getText() != null ? vetPhoneNumber.getText().toString() : "");
            feedAndFind.addPetInformationList(petInformation);

            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    private Boolean verifyInformation(){
        if (CollarKey == null || CollarKey.equals("")){
            Toast.makeText(this, "Scan the QR Code from the device.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Objects.requireNonNull(petName.getText()).toString().equals("")){
            Toast.makeText(this, "Pet's Name is crucial and should not be left blank.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Objects.requireNonNull(petBirthday.getText()).toString().equals("")){
            Toast.makeText(this, "Pet's Birthday is crucial and should not be left blank.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}