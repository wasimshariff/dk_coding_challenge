package com.dk.constant;

import com.dk.model.SensorReading;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SwingDataFunction {
    ACC_X_AXIS(sensorReading -> sensorReading.getAccelerometerReading().getxAxis()),
    ACC_Y_AXIS(sensorReading -> sensorReading.getAccelerometerReading().getyAxis()),
    ACC_Z_AXIS(sensorReading -> sensorReading.getAccelerometerReading().getzAxis()),
    GYRO_X_AXIS(sensorReading -> sensorReading.getGyroscopeReading().getxAxis()),
    GYRO_Y_AXIS(sensorReading -> sensorReading.getGyroscopeReading().getyAxis()),
    GYRO_Z_AXIS(sensorReading -> sensorReading.getGyroscopeReading().getzAxis());

    private Function<SensorReading, Float> lambdaFunction;

    SwingDataFunction(Function<SensorReading, Float> input) {
        this.lambdaFunction = input;
    }

    /*public Function<SensorReading, Float> getLambdaFunction() {
        return lambdaFunction;
    }*/
    public List<Float> getReading(List<SensorReading> sensorReadings) {
        return sensorReadings.stream()
                .map(lambdaFunction)
                .collect(Collectors.toList());
    }
}
