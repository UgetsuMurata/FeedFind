package com.example.feedandfind.Items;

import android.annotation.SuppressLint;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PetInformation {
    private String key, name, age, birthday, weight, sex, allergies, medication, vetName, phone;
    private Long image;

    public PetInformation() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    private void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        setAge(BirthdayToAge(birthday));
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SuppressLint("DefaultLocale")
    private String  BirthdayToAge(String birthday){
        Calendar inputDate = parseDate(birthday);
        Calendar currentDate = new GregorianCalendar();

        // Calculate the difference in days
        long daysDifference = daysBetween(inputDate, currentDate);

        // Calculate years, months, and remaining days
        long years = daysDifference / 365;
        long remainingDays = daysDifference % 365;
        long months = remainingDays / 30;
        remainingDays %= 30;

        // Display the result (you can format it as per your needs)
        return String.format("%d years, %d months, and %d days", years, months, remainingDays);
    }

    private Calendar parseDate(String dateString) {
        String[] dateParts = dateString.split("-");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        return calendar;
    }

    private long daysBetween(Calendar startDate, Calendar endDate) {
        long startMillis = startDate.getTimeInMillis();
        long endMillis = endDate.getTimeInMillis();
        return (endMillis - startMillis) / (24 * 60 * 60 * 1000);
    }
}
