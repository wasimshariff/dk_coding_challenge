package com.dk.repository;

import com.dk.model.AxisReading;
import com.dk.model.SensorReading;
import com.dk.service.impl.SwingDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class SwingDataRepository {

    private final Logger logger = LoggerFactory.getLogger(SwingDataRepository.class);
    private List<SensorReading> sensorReadings;
    @PostConstruct
    public void postConstruct()  {
        logger.info("Post construct called");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/latestSwing.csv"))) {
            this.sensorReadings = reader.lines()
                    .map(line -> Arrays.asList(line.split(",")))
                    .filter(list -> list.size() == 7)
                    .map(this::createSensorReading)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error occurred while trying to load data from CSV File: {}, Initializing an Empty SensorReadings", ex.getMessage());
            this.sensorReadings = new ArrayList<>();
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
            throw ex;
        }
        return reading;
    }


    /**
     * Returning the same instance for brevity.
     * @return
     */
    public List<SensorReading> getSwingData() {
        return this.sensorReadings;
    }
}
