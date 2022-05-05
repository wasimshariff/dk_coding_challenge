package com.dk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class ContinuityRequest {

    private List<Float> sensorReadingList;

    private BiPredicate continuityPredicate;

    private List<Float> thresholdList;
    public List<Float> getSensorReadingList() {
        if (sensorReadingList == null) {
            sensorReadingList = new ArrayList<>();
        }
        return sensorReadingList;
    }

    public void setSensorReadingList(List<Float> sensorReadingList) {
        this.sensorReadingList = sensorReadingList;
    }

    public BiPredicate getContinuityPredicate() {
        return continuityPredicate;
    }

    public void setContinuityPredicate(BiPredicate continuityPredicate) {
        this.continuityPredicate = continuityPredicate;
    }

    public List<Float> getThresholdList() {
        if (thresholdList == null) {
            thresholdList = new ArrayList<>();
        }
        return thresholdList;
    }

    public void setThresholdList(List<Float> thresholdList) {
        this.thresholdList = thresholdList;
    }
}
