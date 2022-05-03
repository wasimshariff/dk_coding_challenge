package com.dk.service.impl;

import com.dk.constant.SwingDataFunction;
import com.dk.model.ContinuityResponse;
import com.dk.repository.SwingDataRepository;
import com.dk.service.SwingDataService;
import com.dk.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;

@Service
public class SwingDataServiceImpl implements SwingDataService {
    private final Logger logger = LoggerFactory.getLogger(SwingDataServiceImpl.class);
    @Autowired
    private SwingDataRepository repository;

    /**
     * Returns the first index of the continuous block of elements
     * ( of size winLength) that are greater than the threshold.
     * @param input
     * @param beginIndex
     * @param endIndex
     * @param threshold
     * @param winLength
     * @return
     */
    public int searchContinuityAboveValue(SwingDataFunction input, int beginIndex, int endIndex, float threshold, int winLength) throws Exception {
        List<Float> sensorReadingsByAxis = input.getReading(this.repository.getSwingData());
        float[] thresholdArray = new float[]{threshold};
        BiFunction<Float, float[], Boolean> continuityChecker = (readingVal, thresholdVal) -> readingVal > thresholdVal[0];
        List<ContinuityResponse> continuityResponse = AppUtil.findContinuityResponse(sensorReadingsByAxis, beginIndex, endIndex, thresholdArray, winLength, continuityChecker, false);

        if (!continuityResponse.isEmpty()) {
            return continuityResponse.get(0).getBeginIndex();
        }
        return -1;
    }

    /**
     * Return the first index of continuous block of elements where
     * data1[element] > threshold1 and data2[element] > threshold2
     * @param input
     * @param input2
     * @param beginIndex
     * @param endIndex
     * @param threshold1
     * @param threshold2
     * @param winLength
     * @return
     */
    public int searchContinuityAboveValueTwoSignals(SwingDataFunction input, SwingDataFunction input2,
                                                    int beginIndex, int endIndex, float threshold1, float threshold2, int winLength) throws Exception {

        List<Float> sensorReadingsByAxis = input.getReading(this.repository.getSwingData());
        List<Float> sensorReadingsByAxis2 = input2.getReading(this.repository.getSwingData());

        AppUtil.validateInput(sensorReadingsByAxis, beginIndex, endIndex);
        AppUtil.validateInput(sensorReadingsByAxis2, beginIndex, endIndex);

        List<Float> subList = sensorReadingsByAxis.subList(beginIndex, endIndex);
        List<Float> subList2 = sensorReadingsByAxis2.subList(beginIndex, endIndex);

        if (subList.size() < winLength || subList2.size() < winLength) {
            return -1;
        }
        int i = 0, seq = 0, foundIndex = -1;
        while (i < subList.size() && i < subList2.size()) {
            if (subList.get(i) > threshold1 && subList2.get(i) > threshold2) {
                seq++;
            } else {
                seq = 0;
            }
            i++;
            if (seq == winLength) {
                foundIndex = i - winLength;
                break;
            }
        }
        if (foundIndex > -1) {
            // adding the begin Index to get the right index , as we are doing search on subList.
            foundIndex = foundIndex + beginIndex;
            logger.info("Found Multi Continuity index at: {} ", foundIndex);
        }
        return foundIndex;
    }

    /**
     * Get first index of continuous block of elements that fall between thresholdLo and thresholdHi
     * Do the processing in reverse direction. And return the first element in reverse order.
     * BeginIndex > endIndex
     * @param input
     * @param beginIndex
     * @param endIndex
     * @param thresholdLo
     * @param thresholdHi
     * @param winLength
     * @return
     */
    public int backSearchContinuityWithinRange(SwingDataFunction input, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) throws Exception {

        List<Float> sensorReadingsByAxis = input.getReading(this.repository.getSwingData());

        if (beginIndex < endIndex || sensorReadingsByAxis.size() < beginIndex) {
            throw new Exception("Invalid Index values");
        }
        AppUtil.validateInput(sensorReadingsByAxis, endIndex, beginIndex);

        List<Float> subList = sensorReadingsByAxis.subList(endIndex, beginIndex);

        int i = subList.size()-1, seq = 0, foundIndex = -1;
        while (i >= 0) {
            if (subList.get(i) > thresholdLo && subList.get(i) < thresholdHi) {
                seq++;
            } else {
                seq = 0;
            }
            i--;
            if (seq == winLength) {
                foundIndex = i + winLength;
                break;
            }
        }
        if (foundIndex > -1) {
            // adding the end Index to get the right index , as we are doing search on subList.
            // As endIndex is smaller than beginIndex.
            foundIndex = foundIndex + endIndex;
            logger.info("Found Back Continuity index at: {} ", foundIndex);
        }
        return foundIndex;
    }

    /**
     * Return List<MultiContinuityModel>
     * eg : beginIndex:endIndex ....
     *
     * @param input
     * @param beginIndex
     * @param endIndex
     * @param thresholdLo
     * @param thresholdHi
     * @param winLength
     * @return
     */
    public List<ContinuityResponse> searchMultiContinuityWithInRange(SwingDataFunction input, int beginIndex, int endIndex, float thresholdLo, float thresholdHi, int winLength) throws Exception {
        List<Float> sensorReadingsByAxis = input.getReading(this.repository.getSwingData());
        float[] thresholdArray = new float[]{thresholdLo, thresholdHi};
        BiFunction<Float, float[], Boolean> continuityChecker = (readingVal, thresholdVal) -> readingVal > thresholdVal[0] && readingVal < thresholdVal[1];
        return AppUtil.findContinuityResponse(sensorReadingsByAxis, beginIndex, endIndex, thresholdArray, winLength, continuityChecker, true);
    }

}
