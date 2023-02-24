import java.util.Hashtable;

public class OpenAir extends Venue {
    private Boolean hasTents;

    public OpenAir(){
    }
    public OpenAir(String []openAirInfo, Hashtable <String, Integer> eventTitleId){
        super(openAirInfo, eventTitleId);
    }
    public Boolean getHasTents() {
        return hasTents;
    }

    public void setHasTents(Boolean hasTents) {
        this.hasTents = hasTents;
    }
}
