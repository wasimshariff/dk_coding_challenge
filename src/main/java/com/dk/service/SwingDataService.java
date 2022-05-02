package com.dk.service;

import com.dk.model.MultiContinuityModel;
import com.dk.model.SensorReading;

import java.util.List;
import java.util.function.Function;

public interface SwingDataService {

    List<Float> getSwingData(Function<SensorReading, Float> inputFunction);

    public int searchContinuityAboveValue(Function<SensorReading, Float> inputFunction, int beginIndex, int endIndex, float threshold, int winLength) throws Exception;

    public int searchContinuityAboveValueTwoSignals(Function<SensorReading, Float> inputFunction, Function<SensorReading, Float> inputFunction2,
                                                    int beginIndex, int endIndex, float threshold1, float threshold2, int winLength) throws Exception;

    public int backSearchContinuityWithinRange(Function<SensorReading, Float> inputFunction, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) throws Exception;

    public List<MultiContinuityModel>  searchMultiContinuityWithInRange(Function<SensorReading, Float> inputFunction, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength);
    }
