package com.example.feedandfind.Items;

public class PedometerData {
    Integer stepsCount, totalMeters;

    public PedometerData(Integer stepsCount, Integer totalMeters) {
        this.stepsCount = stepsCount;
        this.totalMeters = totalMeters;
    }

    public Integer getStepsCount(){
        return stepsCount;
    }
    public Integer getTotalMeters() {
        return totalMeters;
    }
}
