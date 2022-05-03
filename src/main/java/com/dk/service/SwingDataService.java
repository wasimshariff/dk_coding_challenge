package com.dk.service;

import com.dk.constant.SwingDataFunction;
import com.dk.model.ContinuityResponse;

import java.util.List;

public interface SwingDataService {

   // List<Float> getSwingData(Function<SensorReading, Float> inputFunction);

    public int searchContinuityAboveValue(SwingDataFunction input, int beginIndex, int endIndex, float threshold, int winLength) throws Exception;

    public int searchContinuityAboveValueTwoSignals(SwingDataFunction input, SwingDataFunction input2,
                                                    int beginIndex, int endIndex, float threshold1, float threshold2, int winLength) throws Exception;

    public int backSearchContinuityWithinRange(SwingDataFunction input, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) throws Exception;

    public List<ContinuityResponse> searchMultiContinuityWithInRange(SwingDataFunction input, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) throws Exception;
    }
