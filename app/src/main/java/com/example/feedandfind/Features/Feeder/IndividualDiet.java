package com.example.feedandfind.Features.Feeder;

import static com.example.feedandfind.FunctionHelpers.CollarIDFunctions.findPetInformation;
import static com.example.feedandfind.FunctionHelpers.TimeCode.timeCodeToTime;
import static com.example.feedandfind.FunctionHelpers.TimeCode.timeToTimeCode;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.FunctionHelpers.TimeCode;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class IndividualDiet extends AppCompatActivity {

    private LinearLayout meal1Layout, meal2Layout, meal3Layout;
    private EditText meal1, meal2, meal3;
    private String currentTimePeriod;
    private final String[] items = {"Once a day", "Twice a day", "Thrice a day"};
    private final String[] levelItems = {"1", "2", "3"};

    private Integer delay1 = 300, delay2 = 300, delay3 = 300;
    private final HashMap<Integer, String> schedules = new HashMap<>();
    private final HashMap<Integer, String> newSchedules = new HashMap<>();
    private PetInformation petInformation;
    private Integer mealFrequency;
    private FirebaseData firebaseData;
    private FeedAndFind feedAndFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_feeder_individualdiet);

        TextView headerText = findViewById(R.id.dog_name);
        AutoCompleteTextView consumptionFrequency = findViewById(R.id.consumption_frequency);
        AutoCompleteTextView firstMealLevel = findViewById(R.id.first_meal_level);
        AutoCompleteTextView secondMealLevel = findViewById(R.id.second_meal_level);
        AutoCompleteTextView thirdMealLevel = findViewById(R.id.third_meal_level);
        meal1 = findViewById(R.id.first_meal);
        meal2 = findViewById(R.id.second_meal);
        meal3 = findViewById(R.id.third_meal);
        meal1Layout = findViewById(R.id.first_meal_container);
        meal2Layout = findViewById(R.id.second_meal_container);
        meal3Layout = findViewById(R.id.third_meal_container);
        CardView cancelButton = findViewById(R.id.cancel_btn);
        CardView saveButton = findViewById(R.id.save_btn);
        ImageView petImage = findViewById(R.id.dog_pfp);

        firebaseData = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();

        if (feedAndFind.FEEDER_CODE == null) { // cancel operation if no feeder is available.
            Toast.makeText(this, "Add a feeder first before setting up feeder schedules.", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        }

        String CollarId = getIntent().getStringExtra("COLLAR_ID");
        if (CollarId == null) {
            getOnBackPressedDispatcher().onBackPressed();
            Toast.makeText(this, "No collar ID found.", Toast.LENGTH_SHORT).show();
        } else {
            petInformation = findPetInformation(CollarId);
            if (petInformation == null) {
                getOnBackPressedDispatcher().onBackPressed();
                Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
        headerText.setText(String.format("Feeder - %s", petInformation.getName()));
        petImage.setImageDrawable(ResourcesCompat.getDrawable(this.getResources(),
                Math.toIntExact(petInformation.getImage()),
                this.getTheme()));

        // retrieve meal for dog data in Firebase if there are any
        firebaseData.retrieveData(this, "PetFeeder/" + feedAndFind.FEEDER_CODE + "/const_schedules", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                Integer scheduleNumber = 1;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot sched : ds.getChildren()) {
                        if (petInformation.getKey().equals(sched.getKey())) {
                            schedules.put(scheduleNumber, ds.getKey());
                            newSchedules.put(scheduleNumber, ds.getKey());
                            scheduleNumber++;
                        }
                    }
                }
                mealFrequency = 3;
                schedules.putIfAbsent(1, "N/A");
                schedules.putIfAbsent(2, "N/A");
                schedules.putIfAbsent(3, "N/A");
                newSchedules.putIfAbsent(1, "N/A");
                newSchedules.putIfAbsent(2, "N/A");
                newSchedules.putIfAbsent(3, "N/A");

                // display the retrieved time.
                meal1.setText(timeCodeToTime(schedules.get(1)));
                meal2.setText(timeCodeToTime(schedules.get(2)));
                meal3.setText(timeCodeToTime(schedules.get(3)));
            }
        });

        // setup consumptionFrequency
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(IndividualDiet.this,
                R.layout.diet_list_item,
                items);
        consumptionFrequency.setAdapter(adapterItems);
        consumptionFrequency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "Once a day":
                        // hide meal 2 and 3
                        meal1Layout.setVisibility(View.VISIBLE);
                        meal2Layout.setVisibility(View.GONE);
                        meal3Layout.setVisibility(View.GONE);
                        mealFrequency = 1;
                        break;
                    case "Twice a day":
                        meal1Layout.setVisibility(View.VISIBLE);
                        meal2Layout.setVisibility(View.VISIBLE);
                        meal3Layout.setVisibility(View.GONE);
                        mealFrequency = 2;
                        break;
                    case "Thrice a day":
                        meal1Layout.setVisibility(View.VISIBLE);
                        meal2Layout.setVisibility(View.VISIBLE);
                        meal3Layout.setVisibility(View.VISIBLE);
                        mealFrequency = 3;
                        break;
                }
            }
        });

        // setup meal levels
        ArrayAdapter<String> mealLevelItems = new ArrayAdapter<>(IndividualDiet.this,
                R.layout.diet_list_item,
                levelItems);
        firstMealLevel.setAdapter(mealLevelItems);
        firstMealLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "1":
                        delay1 = 300;
                        break;
                    case "2":
                        delay1 = 500;
                        break;
                    case "3":
                        delay1 = 700;
                        break;
                }
            }
        });

        firstMealLevel.setSelection(0);

        secondMealLevel.setAdapter(mealLevelItems);
        secondMealLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "1":
                        delay2 = 300;
                        break;
                    case "2":
                        delay2 = 500;
                        break;
                    case "3":
                        delay2 = 700;
                        break;
                }
            }
        });
        secondMealLevel.setSelection(0);

        thirdMealLevel.setAdapter(mealLevelItems);
        thirdMealLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "1":
                        delay3 = 300;
                        break;
                    case "2":
                        delay3 = 500;
                        break;
                    case "3":
                        delay3 = 700;
                        break;
                }
            }
        });
        thirdMealLevel.setSelection(0);

        // setup meal inputs
        meal1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ShowTimeDialogue(meal1, 1);
                }
            }
        });
        meal2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ShowTimeDialogue(meal2, 2);
                }
            }
        });
        meal3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ShowTimeDialogue(meal3, 3);
                }
            }
        });

        saveButton.setOnClickListener(view -> saveChanges());
        cancelButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void ShowTimeDialogue(EditText editText, Integer key) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // add or update the new schedule.
        TimePickerDialog timePickerDialog = new TimePickerDialog(IndividualDiet.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                // add or update the new schedule.
                newSchedules.put(key, timeToTimeCode(hourOfDay, minutes));

                if (hourOfDay >= 12) {
                    currentTimePeriod = "PM";
                    hourOfDay = hourOfDay - 12;
                } else {
                    currentTimePeriod = "AM";
                }
                editText.setText(String.format(Locale.getDefault(), "%02d:%02d%s", hourOfDay, minutes, currentTimePeriod));
                editText.clearFocus();
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.setTitle("Choose a schedule:");
        timePickerDialog.show();
    }

    private void saveChanges() {
        switch (mealFrequency) {
            case 1:
                if (!"N/A".equals(newSchedules.get(1))) {
                    deletePrevious();
                    // save new schedule
                    firebaseData.addValue(String.format("PetFeeder/%s/const_schedules/%s/%s",
                                    feedAndFind.FEEDER_CODE,
                                    newSchedules.get(1),
                                    petInformation.getKey()),
                            delay1);
                    getOnBackPressedDispatcher().onBackPressed();
                    Toast.makeText(this, "Schedule saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please input all time schedules", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (!"N/A".equals(newSchedules.get(1)) &&
                        !"N/A".equals(newSchedules.get(2))) {
                    deletePrevious();
                    // save new schedule
                    firebaseData.addValue(String.format("PetFeeder/%s/const_schedules/%s/%s",
                                    feedAndFind.FEEDER_CODE,
                                    newSchedules.get(1),
                                    petInformation.getKey()),
                            delay1);
                    firebaseData.addValue(String.format("PetFeeder/%s/const_schedules/%s/%s",
                                    feedAndFind.FEEDER_CODE,
                                    newSchedules.get(2),
                                    petInformation.getKey()),
                            delay2);
                    getOnBackPressedDispatcher().onBackPressed();
                    Toast.makeText(this, "Schedule saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please input all time schedules", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (!"N/A".equals(newSchedules.get(1)) &&
                        !"N/A".equals(newSchedules.get(2)) &&
                        !"N/A".equals(newSchedules.get(3))) {
                    deletePrevious();
                    // save new schedule
                    firebaseData.addValue(String.format("PetFeeder/%s/const_schedules/%s/%s",
                                    feedAndFind.FEEDER_CODE,
                                    newSchedules.get(1),
                                    petInformation.getKey()),
                            delay1);
                    firebaseData.addValue(String.format("PetFeeder/%s/const_schedules/%s/%s",
                                    feedAndFind.FEEDER_CODE,
                                    newSchedules.get(2),
                                    petInformation.getKey()),
                            delay2);
                    firebaseData.addValue(String.format("PetFeeder/%s/const_schedules/%s/%s",
                                    feedAndFind.FEEDER_CODE,
                                    newSchedules.get(3),
                                    petInformation.getKey()),
                            delay3);
                    getOnBackPressedDispatcher().onBackPressed();
                    Toast.makeText(this, "Schedule saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please input all time schedules", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void deletePrevious() {
        for (String schedule : schedules.values()) {
            firebaseData.removeData("PetFeeder/" + feedAndFind.FEEDER_CODE + "/const_schedules/" + schedule + "/" + petInformation.getKey());
        }
    }
}
