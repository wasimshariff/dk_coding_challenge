## Getting Started
This is a Spring Boot Project with Spring Web Dependency. It has few rest controllers.

## PreReq

Java 8
Gradle 7.+

## Steps to Start the Project

1. Run command "gradle build"
2. Open StartUpApplication.java and run its main method.
3. Tomcat should be running on default Port 8080

## Csv File Loader
 CSV file is placed in resources folder and gets loaded on startup of the app.
 This data is used for executing various operations.
 
## Rest Endpoints
1. **/api/searchContinuityAboveValue**

   _RequestParams : (data, indexBegin, indexEnd, threshold,winLength) 
   Desc : From indexBegin to indexEnd, search data for values that are higher than
   threshold. Return the first index where data has values that meet this criteria for at least
   winLength samples_


2. **/api/searchContinuityAboveValueTwoSignals**

   _RequestParams: (data1, data2, indexBegin, indexEnd, threshold1, threshold2, winLength) 
   Desc:  From indexBegin to indexEnd, search data1 for values that are higher than threshold1 and also search data2 for values
   that are higher than threshold2. Return the first index where both data1 and data2 have
   values that meet these criteria for at least winLength samples._


3. **/api/backSearchInRange**

   _RequestParams: (data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength)
   Desc: From indexBegin to indexEnd (where
   indexBegin is larger than indexEnd), search data for values that are higher than
   thresholdLo and lower than thresholdHi. Return the first index where data has values that
   meet this criteria for at least winLength samples._


4. /api/searchMultiInRange
   _RequestParams: (data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength)
   Desc: From indexBegin to indexEnd, search data for values that are higher than thresholdLo and lower than thresholdHi. Return the the
   starting index and ending index of all continuous samples that meet this criteria for at least
   winLength data points._

## Additional Resources
  Attached PostMan API Collection in the project.