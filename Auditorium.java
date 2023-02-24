import java.util.Hashtable;

public class Auditorium extends Venue {
    private Boolean hasTheaterShow;

    public Auditorium(){
    }
    public Auditorium(String []auditoriumInfo, Hashtable <String, Integer> eventTitleId){
        super(auditoriumInfo, eventTitleId);
    }
    public Boolean getHasTheaterShow() {
        return hasTheaterShow;
    }
    public void setHasTheaterShow(Boolean hasTheaterShow) {
        this.hasTheaterShow = hasTheaterShow;
    }
}
