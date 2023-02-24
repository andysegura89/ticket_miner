import java.util.ArrayList;
import java.util.Hashtable;

public abstract class Event {
    private String eventId;
    private String eventName;
    private String eventLocation;
    private String eventDate;
    private String eventTime;
    private String eventType;
    private int numTicketsSold;
    private Double totalTaxed;
    private Double totalDiscounted;
    private Hashtable <String, Integer> ticketsAvailable = new Hashtable<String, Integer>();
    private Hashtable <String, Double> ticketPrices = new Hashtable<String, Double>();
    private Hashtable <String, ArrayList<Ticket>> ticketsPurchased = new Hashtable<String, ArrayList<Ticket>>(); //event: tickets purchased num
    private Venue eventVenue;
    public Event(){
    } 
    public Event(String[] eventInfo, Hashtable <String, Integer> eventTitleId){
        this.totalTaxed = 0.0;
        this.totalDiscounted = 0.0;
        this.eventId = eventInfo[eventTitleId.get("Event ID")];
        this.eventName = eventInfo[eventTitleId.get("Name")];
        this.eventDate = eventInfo[eventTitleId.get("Date")];
        this.eventTime = eventInfo[eventTitleId.get("Time")];
        this.eventType = eventInfo[eventTitleId.get("Event Type")];
        this.ticketPrices.put("vip",Double.parseDouble(eventInfo[eventTitleId.get("VIP Price")]));
        this.ticketPrices.put("gold",Double.parseDouble(eventInfo[eventTitleId.get("Gold Price")]));
        this.ticketPrices.put("silver",Double.parseDouble(eventInfo[eventTitleId.get("Silver Price")]));
        this.ticketPrices.put("bronze",Double.parseDouble(eventInfo[eventTitleId.get("Bronze Price")]));
        this.ticketPrices.put("generalAdmission",Double.parseDouble(eventInfo[eventTitleId.get("General Admission Price")]));
        
        // {typeOfTicketsAvailable: (int)(percentage * capacity) }
        this.ticketsAvailable.put("unavailable",(int)(Integer.parseInt(eventInfo[eventTitleId.get("Seats Unavailable Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));
        this.ticketsAvailable.put("vip",(int)(Integer.parseInt(eventInfo[eventTitleId.get("VIP Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));
        this.ticketsAvailable.put("gold",(int)(Integer.parseInt(eventInfo[eventTitleId.get("Gold Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));
        this.ticketsAvailable.put("silver",(int)(Integer.parseInt(eventInfo[eventTitleId.get("Silver Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));
        this.ticketsAvailable.put("bronze",(int)(Integer.parseInt(eventInfo[eventTitleId.get("Bronze Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));
        this.ticketsAvailable.put("generalAdmission",(int)(Integer.parseInt(eventInfo[eventTitleId.get("General Admission Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));
        this.ticketsAvailable.put("reservedExtra",(int)(Integer.parseInt(eventInfo[eventTitleId.get("Reserved Extra Pct")]) * Integer.parseInt(eventInfo[eventTitleId.get("Capacity")])/100));

        if(eventInfo[eventTitleId.get("Venue Type")].equalsIgnoreCase("Stadium")){
            eventVenue = new Stadium(eventInfo, eventTitleId);
        }else if(eventInfo[eventTitleId.get("Venue Type")].equalsIgnoreCase("Open Air")){
            eventVenue = new OpenAir(eventInfo, eventTitleId);
        }else if(eventInfo[eventTitleId.get("Venue Type")].equalsIgnoreCase("Auditorium")){
            eventVenue = new Auditorium(eventInfo, eventTitleId);
        }else if(eventInfo[eventTitleId.get("Venue Type")].equalsIgnoreCase("Arena")){
            eventVenue = new Arena(eventInfo, eventTitleId);
        }
    }
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    public String getEventTime() {
        return eventTime;
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    public int getNumTicketsSold() {
        return numTicketsSold;
    }
    public void setNumTicketsSold(int numTicketsSold) {
        this.numTicketsSold = numTicketsSold;
    }
    public Hashtable<String, Integer> getTicketsAvailable() {
        return ticketsAvailable;
    }
    public void setTicketsAvailable(Hashtable<String, Integer> ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }
    public Hashtable<String, Double> getTicketPrices() {
        return ticketPrices;
    }
    public void setTicketPrices(Hashtable<String, Double> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public Venue getEventVenue() {
        return eventVenue;
    }
    public void setEventVenue(Venue eventVenue) {
        this.eventVenue = eventVenue;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public Hashtable<String, ArrayList<Ticket>> getTicketsPurchased() {
        return ticketsPurchased;
    }
    public void setTicketsPurchased(Hashtable<String, ArrayList<Ticket>> ticketsPurchased) {
        this.ticketsPurchased = ticketsPurchased;
    }
    public Double getTotalTaxed() {
        return totalTaxed;
    }
    public void setTotalTaxed(Double totalTaxed) {
        this.totalTaxed = totalTaxed;
    }
    public Double getTotalDiscounted() {
        return totalDiscounted;
    }
    public void setTotalDiscounted(Double totalDiscounted) {
        this.totalDiscounted = totalDiscounted;
    }
}
