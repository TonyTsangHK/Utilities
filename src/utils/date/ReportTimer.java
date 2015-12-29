package utils.date;

public class ReportTimer {
    private long startTime, stopTime, intervalTime;
    
    public static final long secondMillis = 1000, minuteMillis = 60000,
            hourMillis = 3600000, dayMillis = 86400000;
    
    public ReportTimer() {
        startTime = -1;
        stopTime = -1;
        intervalTime = -1;
    }
    
    public void resetTime() {
        startTime = -1;
        stopTime = -1;
        intervalTime = -1;
    }
    
    public void start() {
        if (!isStarted()) {
            startTime = currentTimeMillis();
        }
    }
    
    public void stop() {
        if (isStarted()) {
            stopTime = currentTimeMillis();
        }
    }
    
    public boolean isStarted() {
        return startTime != -1;
    }
    
    public boolean isStopped() {
        return stopTime != -1;
    }
    
    public long elapsedTimeSinceStart() {
        intervalTime = currentTimeMillis();
        return (isStarted())? intervalTime - startTime : -1;
    }
    
    public long elapsedTimeSincePreviousRequest() {
        if (intervalTime == -1) {
            return elapsedTimeSinceStart();
        } else {
            long currentMillis = currentTimeMillis();
            long result = (isStarted())? currentMillis - intervalTime : -1;
            intervalTime = currentMillis;
            return result;
        }
    }
    
    public long getElapsedTime() {
        return (isStarted() && isStopped())? (stopTime - startTime) : -1;
    }
    
    public String getReport() {
        if (!isStarted()) {
            return "ReportTimer not started!";
        } else if (!isStopped()) {
            return "ReportTimer not stopped!";
        } else {
            return 
                "ReportTimer started at " + getTimeString(startTime) + 
                "\nTimer stopped at "     + getTimeString(stopTime) + 
                "\nTime elapsed: "        + getTimeString(getElapsedTime());
        }
    }
    
    public String getSimpleReport() {
        return "Elapsed time: " + getElapsedTime();
    }
    
    public String getSimpleReportSinceStart() {
        return "Elapsed time since start: " + elapsedTimeSinceStart();
    }
    
    public String getSimpleReportSincePreviousRequest() {
        return "Elapsed time since previous request: " + elapsedTimeSincePreviousRequest();
    }
    
    public void printSimpleReport() {
        System.out.println(getSimpleReport());
    }
    
    public void printReport() {
        System.out.println(getReport());
    }
    
    public void printSimpleReportSinceStart() {
        System.out.println(getSimpleReportSinceStart());
    }
    
    public void printSimpleReportSincePreviousRequest() {
        System.out.println(getSimpleReportSincePreviousRequest());
    }
    
    public static String getHumanReadableTimeString(long millis) {
        long ms  = millis % secondMillis,
             sec = (millis % minuteMillis) / secondMillis,
             min = (millis % hourMillis) / minuteMillis,
             hr  = (millis % dayMillis) / hourMillis;
        
        if (hr > 0) {
            return hr + " hrs, " + min + " mins, " + sec + " seconds";
        } else if (min > 0) {
            return min + " mins, " + sec + " seconds";
        } else if (sec > 0) {
            return sec + " seconds";
        } else {
            return "0 seconds";
        }
    }
    
    public static String getTimeString(long millis) {
        long ms  = millis % secondMillis,
             sec = (millis % minuteMillis) / secondMillis,
             min = (millis % hourMillis) / minuteMillis,
             hr  = (millis % dayMillis) / hourMillis;
        
        return hr + ":" + min + ":" + sec + "." + ms;
    }
    
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
