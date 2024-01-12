package com.example.feedandfind.Application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.DataManager.SharedPref;
import com.example.feedandfind.Items.PetInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FeedAndFind extends Application {

    public String APP_CODE;

    private static FeedAndFind instance;
    private List<PetInformation> petInformationList;
    public String FEEDER_CODE;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        petInformationList = new ArrayList<>();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        APP_CODE = SharedPref.readString(this, "APP_CODE", "");
        if (APP_CODE.equals("")){
            APP_CODE = getSaltString();
            SharedPref.write(this, "APP_CODE", APP_CODE);
        }
    }

    public static FeedAndFind getInstance() {
        return instance;
    }

    public void setFEEDER_CODE(String FEEDER_CODE) {
        this.FEEDER_CODE = FEEDER_CODE;
    }

    public List<PetInformation> getPetInformationList() {
        return petInformationList;
    }

    public void addPetInformationList(PetInformation petInformation){
        if (petInformationList != null){
            petInformationList.add(petInformation);
        }
    }

    public void removePetInformationList(String collarKey){
        PetInformation to_delete = null;
        for (PetInformation petInformation : petInformationList){
            if (Objects.equals(petInformation.getKey(), collarKey)){
                to_delete = petInformation;
                break;
            }
        }
        petInformationList.remove(to_delete);
    }

    public void setPetInformationList(List<PetInformation> petInformationList) {
        this.petInformationList = petInformationList;
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
