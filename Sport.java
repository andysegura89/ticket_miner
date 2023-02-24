import java.util.Hashtable;

public class Sport extends Event {
    private String sportType;
    private String homeTeam;
    private String visitingTeam;

    public Sport(){
    }
    public Sport(String []sportInfo, Hashtable <String, Integer> eventTitleId){
        super(sportInfo, eventTitleId);
    }

    public String getSportType() {
        return sportType;
    }
    public void setSportType(String sportType) {
        this.sportType = sportType;
    }
    public String getHomeTeam() {
        return homeTeam;
    }
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }
    public String getVisitingTeam() {
        return visitingTeam;
    }
    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }
}
