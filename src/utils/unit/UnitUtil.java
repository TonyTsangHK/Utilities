package utils.unit;

public class UnitUtil {
    public static final double INCH_TO_CM_FACTOR = 2.54;
    
    public static double convertInchToCm(double inch) {
        return inch * INCH_TO_CM_FACTOR;
    }
    
    public static double convertCmToInch(double cm) {
        return cm / INCH_TO_CM_FACTOR;
    }
    
    public static double convertCmToM(double cm) {
        return cm / 100.0;
    }
    
    public static double convertMToCm(double m) {
        return m * 100;
    }
    
    public static double convertMmToCm(double mm) {
        return mm / 10.0;
    }
    
    public static double convertCmToMm(double cm) {
        return cm * 10;
    }
    
    public static double convertMmToM(double mm) {
        return mm / 1000.0;
    }
}
