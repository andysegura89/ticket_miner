import java.util.Hashtable;

public class Concert extends Event {
    private String concertType;
    private boolean isPremier;
    
    public Concert(){
    }
    public Concert(String []concertInfo, Hashtable <String, Integer> eventTitleId){
        super(concertInfo, eventTitleId);
    }

    public String getConcertType() {
        return concertType;
    }

    public void setConcertType(String concertType) {
        this.concertType = concertType;
    }

    public boolean isPremier() {
        return isPremier;
    }

    public void setPremier(boolean isPremier) {
        this.isPremier = isPremier;
    }
    
}
