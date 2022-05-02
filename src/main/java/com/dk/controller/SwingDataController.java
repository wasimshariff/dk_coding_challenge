package com.dk.controller;

import com.dk.SwingDataConstant;
import com.dk.model.MultiContinuityModel;
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
    public long searchContinuityAboveValue(@RequestParam SwingDataConstant.SwingDataFunction dataValue, @RequestParam int beginIndex, @RequestParam int endIndex,
                                     @RequestParam float threshold, @RequestParam int winLength) throws Exception {
        return this.swingDataService.searchContinuityAboveValue(dataValue.getLambdaFunction(), beginIndex, endIndex, threshold, winLength);
    }

    @GetMapping("/searchContinuityAboveValueTwoSignals")
    public long searchContinuityAboveValueTwoSignals(@RequestParam SwingDataConstant.SwingDataFunction dataValue, @RequestParam SwingDataConstant.SwingDataFunction dataValue2,
                                     @RequestParam int beginIndex, @RequestParam int endIndex, @RequestParam float threshold, @RequestParam float threshold2,
                                     @RequestParam int winLength) throws Exception {
        return this.swingDataService.searchContinuityAboveValueTwoSignals(dataValue.getLambdaFunction(), dataValue2.getLambdaFunction(), beginIndex, endIndex, threshold, threshold2, winLength);
    }


    @GetMapping("/backSearchInRange")
    public long backSearchInRange(@RequestParam SwingDataConstant.SwingDataFunction dataValue, @RequestParam int beginIndex, @RequestParam int endIndex,
                                  @RequestParam float thresholdLo, @RequestParam float thresholdHi, @RequestParam int winLength) throws Exception {
        return this.swingDataService.backSearchContinuityWithinRange(dataValue.getLambdaFunction(), beginIndex, endIndex, thresholdLo, thresholdHi, winLength);
    }

    @GetMapping("/searchMultiInRange")
    public List<MultiContinuityModel> searchMultiContinuityWithInRange(@RequestParam SwingDataConstant.SwingDataFunction dataValue, @RequestParam int beginIndex, @RequestParam int endIndex,
                                                                       @RequestParam float thresholdLo, @RequestParam float thresholdHi, @RequestParam int winLength) {
        return this.swingDataService.searchMultiContinuityWithInRange(dataValue.getLambdaFunction(), beginIndex, endIndex, thresholdLo, thresholdHi, winLength);
    }

}
