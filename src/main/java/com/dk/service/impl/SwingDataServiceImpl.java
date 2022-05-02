package com.dk.service.impl;

import com.dk.model.AxisReading;
import com.dk.model.MultiContinuityModel;
import com.dk.model.SensorReading;
import com.dk.service.SwingDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SwingDataServiceImpl implements SwingDataService {
    private final Logger logger = LoggerFactory.getLogger(SwingDataServiceImpl.class);
    private List<SensorReading> sensorReadings;

    @PostConstruct
    public void postConstruct() throws Exception {
        logger.info("Post construct called");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/latestSwing.csv"))) {
            this.sensorReadings = reader.lines()
                    .map(line -> {
                        return Arrays.asList(line.split(","));
                    })
                    .filter(list -> list.size() == 7)
                    .map(this::createSensorReading)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new Exception(e);
        }
        logger.info("Got {} readings from csv file", sensorReadings.size());

    }

    private SensorReading createSensorReading(List<String> values) {
        SensorReading reading = null;
        try {
            reading = new SensorReading();
            reading.setRecorderTime(Long.parseLong(values.get(0)));
            reading.setAccelerometerReading(new AxisReading(Float.parseFloat(values.get(1)), Float.parseFloat(values.get(2)), Float.parseFloat(values.get(3))));
            reading.setGyroscopeReading(new AxisReading(Float.parseFloat(values.get(4)), Float.parseFloat(values.get(5)), Float.parseFloat(values.get(6))));
        } catch (Exception ex) {
            logger.error("Failed Parsing line: {} , Exception: {}", values, ex.getMessage());
        }
        return reading;
    }


    @Override
    public List<Float> getSwingData(Function<SensorReading, Float> inputFunction) {
        return this.sensorReadings.stream()
                .map(inputFunction)
                .collect(Collectors.toList());
    }

    public List<SensorReading> getSensorReadings() {
        if (this.sensorReadings == null) {
            this.sensorReadings = new ArrayList<>();
        }
        return this.sensorReadings;
    }

    /**
     * Returns the first index of the continuous block of elements
     * ( of size winLength) that are greater than the threshold.
     * @param inputFunction
     * @param beginIndex
     * @param endIndex
     * @param threshold
     * @param winLength
     * @return
     */
    public int searchContinuityAboveValue(Function<SensorReading, Float> inputFunction, int beginIndex, int endIndex, float threshold, int winLength) throws Exception {
        List<Float> sensorReadingsByAxis = this.getSwingData(inputFunction);

        if (beginIndex > endIndex || sensorReadingsByAxis.size() < endIndex) {
            throw new Exception("Invalid Index values");
        }

        List<Float> subList = sensorReadingsByAxis.subList(beginIndex, endIndex);

        if (subList.size() < winLength) {
            return -1;
        }
        int i = 0, seq = 0, j = -1;
        while (i < subList.size()) {
            if (subList.get(i) > threshold) {
                seq++;
            } else {
                seq = 0;
            }
            i++;
            if (seq == winLength) {
                j = i - winLength;
                break;
            }
        }
        if (j > -1) {
            // adding the begin Index to get the right index , as we are doing search on subList.
            j = j + beginIndex;
            logger.info("Found Continuity index at: {} ", j);
        }
        return j;
    }

    /**
     * Return the first index of continuous block of elements where
     * data1[element] > threshold1 and data2[element] > threshold2
     * @param inputFunction
     * @param inputFunction2
     * @param beginIndex
     * @param endIndex
     * @param threshold1
     * @param threshold2
     * @param winLength
     * @return
     */
    public int searchContinuityAboveValueTwoSignals(Function<SensorReading, Float> inputFunction, Function<SensorReading, Float> inputFunction2,
                                                    int beginIndex, int endIndex, float threshold1, float threshold2, int winLength) throws Exception {

        List<Float> sensorReadingsByAxis = this.getSwingData(inputFunction);
        List<Float> sensorReadingsByAxis2 = this.getSwingData(inputFunction2);
        if (beginIndex > endIndex || sensorReadingsByAxis.size() < endIndex || sensorReadingsByAxis2.size() < endIndex) {
            throw new Exception("Invalid Index values");
        }
        List<Float> subList = sensorReadingsByAxis.subList(beginIndex, endIndex);
        List<Float> subList2 = sensorReadingsByAxis2.subList(beginIndex, endIndex);

        if (subList.size() < winLength || subList2.size() < winLength) {
            return -1;
        }
        int i = 0, k = 0, seq = 0, j = -1;
        while (i < subList.size() && k < subList2.size()) {
            if (subList.get(i) > threshold1 && subList2.get(k) > threshold2) {
                seq++;
            } else {
                seq = 0;
            }
            i++;
            k++;
            if (seq == winLength) {
                j = i - winLength;
                break;
            }
        }
        if (j > -1) {
            // adding the begin Index to get the right index , as we are doing search on subList.
            j = j + beginIndex;
            logger.info("Found Multi Continuity index at: {} ", j);
        }
        return j;
    }

    /**
     * Get first index of continuous block of elements that fall between thresholdLo and thresholdHi
     * Do the processing in reverse direction. And return the first element in reverse order.
     * BeginIndex > endIndex
     * @param inputFunction
     * @param beginIndex
     * @param endIndex
     * @param thresholdLo
     * @param thresholdHi
     * @param winLength
     * @return
     */
    public int backSearchContinuityWithinRange(Function<SensorReading, Float> inputFunction, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) throws Exception {

        List<Float> sensorReadingsByAxis = this.getSwingData(inputFunction);

        if (beginIndex < endIndex || sensorReadingsByAxis.size() < beginIndex) {
            throw new Exception("Invalid Index values");
        }

        List<Float> subList = sensorReadingsByAxis.subList(endIndex, beginIndex);

        int i = subList.size()-1, seq = 0, j = -1;
        while (i >= 0) {
            if (subList.get(i) > thresholdLo && subList.get(i) < thresholdHi) {
                seq++;
            } else {
                seq = 0;
            }
            i--;
            if (seq == winLength) {
                j = i + winLength;
                break;
            }
        }
        if (j > -1) {
            // adding the end Index to get the right index , as we are doing search on subList.
            // As endIndex is smaller than beginIndex.
            j = j + endIndex;
            logger.info("Found Back Continuity index at: {} ", j);
        }
        return j;
    }

    /**
     * Return List<MultiContinuityModel>
     * eg : beginIndex:endIndex ....
     *
     * @param inputFunction
     * @param beginIndex
     * @param endIndex
     * @param thresholdLo
     * @param thresholdHi
     * @param winLength
     * @return
     */
    public List<MultiContinuityModel> searchMultiContinuityWithInRange(Function<SensorReading, Float> inputFunction, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) {
        List<Float> sensorReadingsByAxis = this.getSwingData(inputFunction);
        List<MultiContinuityModel> result = new ArrayList<>();
        List<Float> subList = sensorReadingsByAxis.subList(beginIndex, endIndex);
        if (subList.size() < winLength) {
            return null;
        }
        int i = 0, seq = 0;
        while (i < subList.size()) {
            if (subList.get(i) > thresholdLo && subList.get(i) < thresholdHi) {
                seq++;
            } else {
                seq = 0;
            }
            i++;
            if (seq == winLength) {
                result.add( new MultiContinuityModel(i - winLength + beginIndex, i + beginIndex));
            }
        }
        return result;
    }

}
