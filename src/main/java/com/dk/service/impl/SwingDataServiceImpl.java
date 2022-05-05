package com.dk.service.impl;

import com.dk.constant.SwingDataFunction;
import com.dk.model.ContinuityRequest;
import com.dk.model.ContinuityResponse;
import com.dk.repository.SwingDataRepository;
import com.dk.service.SwingDataService;
import com.dk.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

@Service
public class SwingDataServiceImpl implements SwingDataService {
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
        ContinuityRequest request = new ContinuityRequest();
        request.setSensorReadingList(sensorReadingsByAxis);
        request.setThresholdList(Arrays.asList(threshold));
        BiPredicate<Float, List<Float>> continuityPredicate = (inputVal, thresholdList) -> inputVal > thresholdList.get(0);
        request.setContinuityPredicate(continuityPredicate);
        List<ContinuityRequest> continuityRequests = new ArrayList<>();
        continuityRequests.add(request);
        List<ContinuityResponse> continuityResponse = AppUtil.findContinuityResponse(continuityRequests, beginIndex, endIndex, winLength, true, false);
        return !continuityResponse.isEmpty() ? continuityResponse.get(0).getBeginIndex() : -1;
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
        List<ContinuityRequest> continuityRequests = new ArrayList<>();
        ContinuityRequest request = new ContinuityRequest();
        request.setSensorReadingList(sensorReadingsByAxis);
        request.setThresholdList(Arrays.asList(threshold1));
        BiPredicate<Float, List<Float>> continuityPredicate = (inputVal, thresholdList) -> inputVal > thresholdList.get(0);
        request.setContinuityPredicate(continuityPredicate);
        continuityRequests.add(request);
        ContinuityRequest request2 = new ContinuityRequest();
        request2.setSensorReadingList(sensorReadingsByAxis2);
        request2.setThresholdList(Arrays.asList(threshold2));
        BiPredicate<Float, List<Float>> continuityPredicate2 = (inputVal, thresholdList) -> inputVal > thresholdList.get(0);
        request2.setContinuityPredicate(continuityPredicate2);
        continuityRequests.add(request2);
        List<ContinuityResponse> continuityResponse = AppUtil.findContinuityResponse(continuityRequests, beginIndex, endIndex, winLength, true, false);
        return !continuityResponse.isEmpty() ? continuityResponse.get(0).getBeginIndex() : -1;
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
        ContinuityRequest request = new ContinuityRequest();
        request.setSensorReadingList(sensorReadingsByAxis);
        request.setThresholdList(Arrays.asList(thresholdLo, thresholdHi));
        BiPredicate<Float, List<Float>> continuityPredicate = (inputVal, thresholdList) -> inputVal > thresholdList.get(0) && inputVal < thresholdList.get(1);
        request.setContinuityPredicate(continuityPredicate);
        List<ContinuityRequest> continuityRequests = new ArrayList<>();
        continuityRequests.add(request);
        List<ContinuityResponse> continuityResponse = AppUtil.findContinuityResponse(continuityRequests, beginIndex, endIndex, winLength, false, false);
        return continuityResponse.isEmpty() ? -1 : continuityResponse.get(0).getBeginIndex();
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
        ContinuityRequest request = new ContinuityRequest();
        request.setSensorReadingList(sensorReadingsByAxis);
        request.setThresholdList(Arrays.asList(thresholdLo, thresholdHi));
        BiPredicate<Float, List<Float>> continuityPredicate = (inputVal, thresholdList) -> inputVal > thresholdList.get(0) && inputVal < thresholdList.get(1);
        request.setContinuityPredicate(continuityPredicate);
        List<ContinuityRequest> continuityRequests = new ArrayList<>();
        continuityRequests.add(request);
        return AppUtil.findContinuityResponse(continuityRequests, beginIndex, endIndex, winLength, true, true);
    }

}
