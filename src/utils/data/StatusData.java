package utils.data;

public class StatusData {
    private int statusCode;
    private String statusMessage;
    
    public StatusData(int code) {
        this(code, "");
    }
    
    public StatusData(int code, String msg) {
        setStatusCode(code);
        setStatusMessage(msg);
    }
    
    public void setStatusCode(int code) {
        statusCode = code;
    }
    
    public void setStatusMessage(String msg) {
        statusMessage = msg;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
}
