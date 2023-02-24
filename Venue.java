import java.util.Hashtable;

public abstract class Venue {
    private String venueId;
    private String venueName;
    private String venueLocation;
    private int venueCapacity;
    private String venueCurfew;
    private int venueCost;
    private String venueType;
    private Boolean hasFireworksPlanned;
    private int hasFireWorksCost;
    public Venue(){ 
    }
    public Venue(String[] venueInfo, Hashtable <String, Integer> eventTitleId){
        this.venueName = venueInfo[eventTitleId.get("Venue Name")];
        this.venueType = venueInfo[eventTitleId.get("Venue Type")];
        this.venueCapacity = Integer.parseInt(venueInfo[eventTitleId.get("Capacity")]);
        this.venueCost = Integer.parseInt(venueInfo[eventTitleId.get("Cost")]);
        if(venueInfo.length >21 && venueInfo[eventTitleId.get("Fireworks Planned")] != null){
            if(venueInfo[eventTitleId.get("Fireworks Planned")].equalsIgnoreCase("Yes")){
                this.hasFireworksPlanned = true;
                if (this.hasFireworksPlanned){
                    this.hasFireWorksCost = Integer.parseInt(venueInfo[eventTitleId.get("Fireworks Cost")]);
                }else{
                    this.hasFireWorksCost = 0;
                }
            }else{
                this.hasFireworksPlanned = false;
                this.hasFireWorksCost = 0;
            }
        }else{
            this.hasFireworksPlanned = false;
            this.hasFireWorksCost = 0;
        }
    }
    public String getVenueId() {
        return venueId;
    }
    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }
    public String getVenueName() {
        return venueName;
    }
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    public String getVenueLocation() {
        return venueLocation;
    }
    public void setVenueLocation(String venueLocation) {
        this.venueLocation = venueLocation;
    }
    public int getVenueCapacity() {
        return venueCapacity;
    }
    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }
    public String getVenueCurfew() {
        return venueCurfew;
    }
    public void setVenueCurfew(String venueCurfew) {
        this.venueCurfew = venueCurfew;
    }
    public int getVenueCost() {
        return venueCost;
    }
    public void setVenueCost(int venueCost) {
        this.venueCost = venueCost;
    }
    public String getVenueType() {
        return venueType;
    }
    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }
    public Boolean getHasFireworksPlanned() {
        return hasFireworksPlanned;
    }
    public void setHasFireworksPlanned(Boolean hasFireworksPlanned) {
        this.hasFireworksPlanned = hasFireworksPlanned;
    }
    public Integer getHasFireWorksCost() {
        return hasFireWorksCost;
    }
    public void setHasFireWorksCost(Integer hasFireWorksCost) {
        this.hasFireWorksCost = hasFireWorksCost;
    }    
}
