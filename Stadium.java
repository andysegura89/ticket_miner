import java.util.Hashtable;

public class Stadium extends Venue {
    private Boolean hasRoof;
    private int balconiesAmount;

    public Stadium(){
    }
    public Stadium(String []stadiumInfo, Hashtable <String, Integer> eventTitleId){
        super(stadiumInfo, eventTitleId);
        
    }
    public Boolean getHasRoof() {
        return hasRoof;
    }
    public void setHasRoof(Boolean hasRoof) {
        this.hasRoof = hasRoof;
    }
    public int getBalconiesAmount() {
        return balconiesAmount;
    }

    public void setBalconiesAmount(int balconiesAmount) {
        this.balconiesAmount = balconiesAmount;
    }
}
