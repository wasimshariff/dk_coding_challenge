package com.dk;

import com.dk.model.SensorReading;

import java.util.function.Function;

public interface SwingDataConstant {

    enum SwingDataFunction {
        GET_ACC_X_AXIS(sensorReading -> sensorReading.getAccelerometerReading().getxAxis()),
        GET_ACC_Y_AXIS(sensorReading -> sensorReading.getAccelerometerReading().getyAxis()),
        GET_ACC_Z_AXIS(sensorReading -> sensorReading.getAccelerometerReading().getzAxis()),
        GET_GYRO_X_AXIS(sensorReading -> sensorReading.getGyroscopeReading().getxAxis()),
        GET_GYRO_Y_AXIS(sensorReading -> sensorReading.getGyroscopeReading().getyAxis()),
        GET_GYRO_Z_AXIS(sensorReading -> sensorReading.getGyroscopeReading().getzAxis());

        private Function<SensorReading, Float> lambdaFunction;
        SwingDataFunction(Function<SensorReading, Float> input) {
            this.lambdaFunction = input;
        }

        public Function<SensorReading, Float> getLambdaFunction() {
            return lambdaFunction;
        }
    }
}
