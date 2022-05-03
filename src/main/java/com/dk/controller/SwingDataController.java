package com.dk.controller;

import com.dk.constant.SwingDataFunction;
import com.dk.model.ContinuityResponse;
import com.dk.service.SwingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class SwingDataController {

    @Autowired
    private SwingDataService swingDataService;

    @GetMapping("/searchContinuityAboveValue")
    public long searchContinuityAboveValue(@RequestParam SwingDataFunction dataValue, @RequestParam int beginIndex, @RequestParam int endIndex,
                                           @RequestParam float threshold, @RequestParam int winLength) throws Exception {
        return this.swingDataService.searchContinuityAboveValue(dataValue, beginIndex, endIndex, threshold, winLength);
    }

    @GetMapping("/searchContinuityAboveValueTwoSignals")
    public long searchContinuityAboveValueTwoSignals(@RequestParam SwingDataFunction dataValue, @RequestParam SwingDataFunction dataValue2,
                                     @RequestParam int beginIndex, @RequestParam int endIndex, @RequestParam float threshold, @RequestParam float threshold2,
                                     @RequestParam int winLength) throws Exception {
        return this.swingDataService.searchContinuityAboveValueTwoSignals(dataValue, dataValue2, beginIndex, endIndex, threshold, threshold2, winLength);
    }


    @GetMapping("/backSearchInRange")
    public long backSearchInRange(@RequestParam SwingDataFunction dataValue, @RequestParam int beginIndex, @RequestParam int endIndex,
                                  @RequestParam float thresholdLo, @RequestParam float thresholdHi, @RequestParam int winLength) throws Exception {
        return this.swingDataService.backSearchContinuityWithinRange(dataValue, beginIndex, endIndex, thresholdLo, thresholdHi, winLength);
    }

    @GetMapping("/searchMultiInRange")
    public List<ContinuityResponse> searchMultiContinuityWithInRange(@RequestParam SwingDataFunction dataValue, @RequestParam int beginIndex, @RequestParam int endIndex,
                                                                     @RequestParam float thresholdLo, @RequestParam float thresholdHi, @RequestParam int winLength) throws Exception {
        return this.swingDataService.searchMultiContinuityWithInRange(dataValue, beginIndex, endIndex, thresholdLo, thresholdHi, winLength);
    }

}
