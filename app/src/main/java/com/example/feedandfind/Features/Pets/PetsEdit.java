package com.example.feedandfind.Features.Pets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PetsEdit extends AppCompatActivity {

    CardView refreshImage, editPet;
    ImageView dogImage;
    TextInputEditText petName, petBirthday, petWeight, petAllergies, petMedication,
            vetName, vetPhoneNumber;
    AutoCompleteTextView petSex;
    FeedAndFind feedAndFind;
    String CollarKey;
    PetInformation petInformation;

    Integer currentImage = R.drawable.face3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_pets_petsedit);

        refreshImage = findViewById(R.id.refresh_image);
        dogImage = findViewById(R.id.dog_image);
        petName = findViewById(R.id.pet_name);
        petBirthday = findViewById(R.id.pet_birthday);
        petWeight = findViewById(R.id.pet_weight);
        petSex = findViewById(R.id.pet_sex);
        petAllergies = findViewById(R.id.pet_allergies);
        petMedication = findViewById(R.id.pet_medication);
        vetName = findViewById(R.id.vet_name);
        vetPhoneNumber = findViewById(R.id.vet_phone_number);
        editPet = findViewById(R.id.add_pet);

        feedAndFind = FeedAndFind.getInstance();

        CollarKey = getIntent().getStringExtra("COLLAR_ID");

        boolean found = false;
        for (PetInformation petInfo : feedAndFind.getPetInformationList()){
            if (petInfo.getKey().equals(CollarKey)){
                petInformation = petInfo;
                found = true;
                break;
            }
        }

        if (!found){
            getOnBackPressedDispatcher().onBackPressed();
            Toast.makeText(this, "Collar ID not found! Please try again later.", Toast.LENGTH_SHORT).show();
        }

        dogImage.setImageDrawable(ResourcesCompat.getDrawable(this.getResources(),
                Math.toIntExact(petInformation.getImage()),
                getTheme()));
        currentImage = Math.toIntExact(petInformation.getImage());
        petName.setText(petInformation.getName());
        petBirthday.setText(petInformation.getBirthday());
        petWeight.setText(petInformation.getWeight());
        petSex.setText(petInformation.getSex());
        petAllergies.setText(petInformation.getAllergies());
        petMedication.setText(petInformation.getMedication());
        vetName.setText(petInformation.getVetName());
        vetPhoneNumber.setText(petInformation.getPhone());

        petSex.setAdapter(new ArrayAdapter<>(this,
                R.layout.textinputlayout_dropdown_item,
                this.getResources().getStringArray(R.array.sex)));

        refreshImage.setOnClickListener(view -> refreshImage());
        petBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    showDatePickerDialog();
                }
            }
        });
        editPet.setOnClickListener(view -> editPet());
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

    private void editPet(){
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
            Toast.makeText(this, "Pet information Edited!", Toast.LENGTH_SHORT).show();

            List<PetInformation> petInformationList = feedAndFind.getPetInformationList();
            petInformationList.remove(petInformation);

            PetInformation newPetInformation = new PetInformation();

            newPetInformation.setKey(CollarKey);
            newPetInformation.setImage(Long.parseLong(String.valueOf(currentImage)));
            newPetInformation.setName(Objects.requireNonNull(petName.getText()).toString());
            newPetInformation.setBirthday(Objects.requireNonNull(petBirthday.getText()).toString());
            newPetInformation.setWeight(petWeight.getText() != null ? petWeight.getText().toString() : "");
            newPetInformation.setSex(petSex.getText() != null ? petSex.getText().toString() : "");
            newPetInformation.setAllergies(petAllergies.getText() != null ? petAllergies.getText().toString() : "");
            newPetInformation.setMedication(petMedication.getText() != null ? petMedication.getText().toString() : "");
            newPetInformation.setVetName(vetName.getText() != null ? vetName.getText().toString() : "");
            newPetInformation.setPhone(vetPhoneNumber.getText() != null ? vetPhoneNumber.getText().toString() : "");

            petInformationList.add(newPetInformation);
            feedAndFind.setPetInformationList(petInformationList);

            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    private Boolean verifyInformation(){
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