public class LogSingleton {
    //singleton object for logging
    private static LogSingleton obj;
    private String logMessage;
    
    private LogSingleton(){
    }
    public static synchronized LogSingleton getInstance(){
        // if obj does not exist, give a variable obj a reference to that obj
        if(obj == null){
            obj = new LogSingleton();
        }
        return obj;
    }
    public String getLogMessage() {
        return logMessage;
    }
    public void setLogMessage(String logMessageIn) {
        this.logMessage = logMessageIn;
    }
}
