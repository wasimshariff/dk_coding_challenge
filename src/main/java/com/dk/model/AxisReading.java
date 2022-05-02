package com.dk.model;

public class AxisReading {

    private float xAxis;
    private float yAxis;
    private float zAxis;

    public AxisReading() {
    }

    public AxisReading(float xAxis, float yAxis, float zAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.zAxis = zAxis;
    }

    public float getxAxis() {
        return xAxis;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public float getzAxis() {
        return zAxis;
    }

    public void setzAxis(float zAxis) {
        this.zAxis = zAxis;
    }
}
