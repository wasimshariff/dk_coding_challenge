package com.dk.util;

import com.dk.model.ContinuityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class AppUtil {

    private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

    public static List<ContinuityResponse> findContinuityResponse(List<Float> input, int indexBegin, int indexEnd, float[] threshold, int winLength, BiFunction<Float, float[], Boolean> continuityChecker, boolean multiResult) throws Exception {
        List<ContinuityResponse> result = new ArrayList<>();

        validateInput(input, indexBegin, indexEnd);

        List<Float> subList = input.subList(indexBegin, indexEnd);

        if (subList.size() < winLength) {
            return null;
        }
        int i = 0, seq = 0, j = -1;
        while (i < subList.size()) {
            //if (subList.get(i) > threshold) {
            if (continuityChecker.apply(subList.get(i), threshold)) {
                seq++;
            } else {
                seq = 0;
            }
            i++;
            if (seq == winLength) {
                seq = 0;
                j = i - winLength;
                if (multiResult) {
                    ContinuityResponse response = new ContinuityResponse(j + indexBegin, i + indexBegin);
                    result.add(response);
                } else {
                    break;
                }
            }
        }
        if (j > -1 && !multiResult) {
            // adding the begin Index to get the right index , as we are doing search on subList.
            logger.info("Found Continuity index at: {} ", j);
            ContinuityResponse response = new ContinuityResponse(j + indexBegin, i + indexBegin);
            result.add(response);
        }
        return result;
    }

    public static void validateInput(List<Float> input, int indexBegin, int indexEnd) throws Exception {
        if (input.isEmpty()) {
            throw new Exception("Input is Empty");
        }
        if (indexBegin > indexEnd || input.size() < indexEnd) {
            throw new Exception("Invalid Index values");
        }
    }

}
