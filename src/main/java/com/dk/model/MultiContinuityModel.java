package com.dk.model;

public class MultiContinuityModel {

    private int beginIndex;
    private int endIndex;

    public MultiContinuityModel() {
    }

    public MultiContinuityModel(int beginIndex, int endIndex) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
