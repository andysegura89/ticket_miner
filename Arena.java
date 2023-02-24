import java.util.Hashtable;

public class Arena extends Venue {
    private Boolean isPaved;
    private String shape;
    private Boolean hasStage;

    public Arena(){
    }
    public Arena(String []arenaInfo, Hashtable <String, Integer> eventTitleId){
        super(arenaInfo, eventTitleId);
    }
    public Boolean getIsPaved() {
        return isPaved;
    }
    public void setIsPaved(Boolean isPaved) {
        this.isPaved = isPaved;
    }
    public String getShape() {
        return shape;
    }
    public void setShape(String shape) {
        this.shape = shape;
    }
    public Boolean getHasStage() {
        return hasStage;
    }
    public void setHasStage(Boolean hasStage) {
        this.hasStage = hasStage;
    }
}
