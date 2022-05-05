package com.dk.util;

import com.dk.model.ContinuityRequest;
import com.dk.model.ContinuityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AppUtil {

    private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

    public static List<ContinuityResponse> findContinuityResponse(List<ContinuityRequest> requestList, int indexBegin, int indexEnd, int winLength, boolean isForwardFlow, boolean multiResult) throws Exception {
        List<ContinuityResponse> result = new ArrayList<>();
        requestList.forEach(request -> validateInput(request.getSensorReadingList(), indexBegin, indexEnd, winLength, isForwardFlow));
        int i = indexBegin, seq = 0, j = -1;
        while ( isForwardFlow ? i < indexEnd : i > indexEnd) {
            final int currentIndex = i;
            boolean matchFound = requestList.stream()
                    .allMatch(continuityRequest -> continuityRequest.getContinuityPredicate()
                            .test(continuityRequest.getSensorReadingList().get(currentIndex), continuityRequest.getThresholdList()));
            if (matchFound) {
                seq++;
            } else {
                seq = 0;
            }
            if (isForwardFlow) {
                i++;
            } else {
                i--;
            }
            if (seq == winLength) {
                logger.info("Sequence with winLength found");
                seq = 0;
                if (isForwardFlow) {
                    j = i - winLength;
                } else {
                    j = i + winLength;
                }
                addResponse(result, j, i);
                if (!multiResult) {
                    break;
                }
            }
        }
        return result;
    }

    private static void addResponse(List<ContinuityResponse> result, int startIndex, int endIndex) {
        ContinuityResponse response = new ContinuityResponse(startIndex, endIndex);
        result.add(response);
    }

    private static void validateInput(List<Float> input, int indexBegin, int indexEnd, int winLength, boolean isForwardFlow) {
        if (input.isEmpty()) {
            throw new RuntimeException("Input is Empty");
        }
        if (indexBegin < 0 || indexEnd < 0 || (isForwardFlow && (indexBegin > indexEnd || input.size() < indexEnd))
            || (!isForwardFlow && (indexBegin < indexEnd || input.size() < indexBegin))) {
            throw new RuntimeException("Invalid Index values");
        }
        if (input.size() < winLength) {
            throw new RuntimeException("WinLength greater than List size");
        }
    }

}
