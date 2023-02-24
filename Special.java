import java.util.Hashtable;

public class Special extends Event {
    private String specialGuest;
    private String specialHoliday;
    private String specialActivity;

    public Special(){
    }
    public Special(String []specialInfo, Hashtable <String, Integer> eventTitleId){
        super(specialInfo, eventTitleId);
    }

    public String getSpecialGuest() {
        return specialGuest;
    }

    public void setSpecialGuest(String specialGuest) {
        this.specialGuest = specialGuest;
    }

    public String getSpecialHoliday() {
        return specialHoliday;
    }

    public void setSpecialHoliday(String specialHoliday) {
        this.specialHoliday = specialHoliday;
    }

    public String getSpecialActivity() {
        return specialActivity;
    }

    public void setSpecialActivity(String specialActivity) {
        this.specialActivity = specialActivity;
    }
    
}
