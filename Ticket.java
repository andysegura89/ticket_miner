public class Ticket {
    private String ticketID;
    private Double ticketPrice;
    private Event eventInfo;
    private String belongsTo;
    private int numberOfTickets;
    private Double totalPriceOfPurchase;
    private int confirmationNum;
    private String typeOfTicket;

    public Ticket(){
    }
    public Ticket(Event eventInfoIn, int numberOfTicketsIn, Double totalPriceOfPurchaseIn, int confirmationNumIn, String typeOfTicketIn){
        this.numberOfTickets = numberOfTicketsIn;
        this.totalPriceOfPurchase = totalPriceOfPurchaseIn;
        this.confirmationNum = confirmationNumIn;
        this.eventInfo = eventInfoIn;
        this.typeOfTicket = typeOfTicketIn;
    } 

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Event getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(Event eventInfo) {
        this.eventInfo = eventInfo;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Double getTotalPriceOfPurchase() {
        return totalPriceOfPurchase;
    }

    public void setTotalPriceOfPurchase(Double totalPriceOfPurchase) {
        this.totalPriceOfPurchase = totalPriceOfPurchase;
    }

    public int getConfirmationNum() {
        return confirmationNum;
    }

    public void setConfirmationNum(int confirmationNum) {
        this.confirmationNum = confirmationNum;
    }
    public String getTypeOfTicket() {
        return typeOfTicket;
    }
    public void setTypeOfTicket(String typeOfTicket) {
        this.typeOfTicket = typeOfTicket;
    }
    
}
