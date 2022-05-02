package com.dk.model;

public class SensorReading {

    private long recorderTime;
    private AxisReading accelerometerReading;
    private AxisReading gyroscopeReading;

    public long getRecorderTime() {
        return recorderTime;
    }

    public void setRecorderTime(long recorderTime) {
        this.recorderTime = recorderTime;
    }

    public AxisReading getAccelerometerReading() {
        return accelerometerReading;
    }

    public void setAccelerometerReading(AxisReading accelerometerReading) {
        this.accelerometerReading = accelerometerReading;
    }

    public AxisReading getGyroscopeReading() {
        return gyroscopeReading;
    }

    public void setGyroscopeReading(AxisReading gyroscopeReading) {
        this.gyroscopeReading = gyroscopeReading;
    }
}
