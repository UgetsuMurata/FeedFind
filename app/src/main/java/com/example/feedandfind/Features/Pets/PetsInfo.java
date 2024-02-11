package com.example.feedandfind.Features.Pets;

import static com.example.feedandfind.FunctionHelpers.CollarIDFunctions.findPetInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Feeder.IndividualDiet;
import com.example.feedandfind.Features.Finder.FinderDataDisplay;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;

import java.util.Objects;

public class PetsInfo extends AppCompatActivity {

    FeedAndFind feedAndFind;
    PetInformation petInformation;
    FirebaseData firebaseData;

    TextView toolbarTitle, petName, petAge, petBirthday, petWeight, petSex, petAllergies,
            petMedication, vetName, vetPhoneNumber;
    ImageView petImage, openMenu;
    String CollarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_pets_petsinfo);

        openMenu = findViewById(R.id.open_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        petImage = findViewById(R.id.pet_image);
        petName = findViewById(R.id.pet_name);
        petAge = findViewById(R.id.pet_age);
        petBirthday = findViewById(R.id.pet_birthday);
        petWeight = findViewById(R.id.pet_weight);
        petSex = findViewById(R.id.pet_sex);
        petAllergies = findViewById(R.id.pet_allergies);
        petMedication = findViewById(R.id.pet_medication);
        vetName = findViewById(R.id.vet_name);
        vetPhoneNumber = findViewById(R.id.vet_phone_number);

        feedAndFind = FeedAndFind.getInstance();
        firebaseData = new FirebaseData();
        CollarId = getIntent().getStringExtra("COLLAR_ID");

        openMenu.setOnClickListener(this::showPopupMenu);

        petInformation = findPetInformation(CollarId);
        if (petInformation == null) {
            getOnBackPressedDispatcher().onBackPressed();
            Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
        }

        renderInformation();
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.pet_items_menu); // Replace with your menu resource

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.delete_pet):
                        firebaseData.removeData("Users/"+feedAndFind.APP_CODE+"/PetTrackerQrCodes/"+CollarId);
                        feedAndFind.removePetInformationList(CollarId);
                        getOnBackPressedDispatcher().onBackPressed();
                        break;
                    case (R.id.edit_pet):
                        intent = new Intent(PetsInfo.this, PetsEdit.class);
                        intent.putExtra("COLLAR_ID", CollarId);
                        startActivity(intent);
                        break;
                    case (R.id.show_device):
                        intent = new Intent(PetsInfo.this, FinderDataDisplay.class);
                        intent.putExtra("COLLAR_ID", CollarId);
                        startActivity(intent);
                        break;
                    case (R.id.setup_feeder):
                        intent = new Intent(PetsInfo.this, IndividualDiet.class);
                        intent.putExtra("COLLAR_ID", petInformation.getKey());
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }
    private void renderInformation(){
        toolbarTitle.setText(petInformation.getName());
        petImage.setImageDrawable(ResourcesCompat.getDrawable(this.getResources(),
                Math.toIntExact(petInformation.getImage()),
                this.getTheme()));
        petName.setText(petInformation.getName());
        petAge.setText(petInformation.getAge());
        petBirthday.setText(petInformation.getBirthday());
        if (!Objects.equals(petInformation.getWeight(), "")){
            petWeight.setText(String.format("%s kilograms", petInformation.getWeight()));
        } else {
            petWeight.setVisibility(View.GONE);
        }
        if (!Objects.equals(petInformation.getSex(), "")){
            petSex.setText(petInformation.getSex());
        } else {
            petSex.setVisibility(View.GONE);
        }
        petAllergies.setText(!petInformation.getAllergies().equals("") ?petInformation.getAllergies():"No Allergies");
        petMedication.setText(!petInformation.getMedication().equals("")?petInformation.getMedication():"No Medication");
        vetName.setText(!petInformation.getVetName().equals("")?petInformation.getVetName():"No Veterinarian");
        vetPhoneNumber.setText(!petInformation.getPhone().equals("")?petInformation.getPhone():"No Veterinarian");
    }

    @Override
    protected void onResume() {
        super.onResume();
        petInformation = findPetInformation(CollarId);
        if (petInformation == null) {
            getOnBackPressedDispatcher().onBackPressed();
            Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
        }
        renderInformation();
    }
}