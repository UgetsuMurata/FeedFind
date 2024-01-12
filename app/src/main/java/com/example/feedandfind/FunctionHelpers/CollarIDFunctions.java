package com.example.feedandfind.FunctionHelpers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.Features.Pets.PetsInfo;
import com.example.feedandfind.Items.PetInformation;

public class CollarIDFunctions {
    public static PetInformation findPetInformation(@NonNull String CollarId){
        FeedAndFind feedAndFind = FeedAndFind.getInstance();
        for (PetInformation petInfo : feedAndFind.getPetInformationList()){
            if (CollarId.equals(petInfo.getKey())){
                return petInfo;
            }
        }
        return null;
    }
}
