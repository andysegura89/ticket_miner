import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.String;

public class Customer{
    private String myId;
    private String firstName;
    private String lastName;
    private Double moneyAvailable;
    private int concertsPurchased;
    private Boolean ticketMinerMembership;
    private String username;
    private String password;
    private Double totalSaved;
    private ArrayList<Ticket> myTickets = new ArrayList<Ticket>();
    private ArrayList<Event> watchingEvents = new ArrayList<Event>();
    private Hashtable <String, String[]> paymentMethodsOnFiles = new Hashtable<String, String[]>();
    public Customer(){
    }
    public Customer(String[] customerInfo, Hashtable <String, Integer> customerTitleId){
        this.myId = customerInfo[customerTitleId.get("ID")];
        this.firstName = customerInfo[customerTitleId.get("First Name")];
        this.lastName = customerInfo[customerTitleId.get("Last Name")];
        this.moneyAvailable = Double.parseDouble(customerInfo[customerTitleId.get("Money Available")]);
        this.concertsPurchased = Integer.parseInt(customerInfo[customerTitleId.get("Concerts Purchased")]);
        this.username = customerInfo[customerTitleId.get("Username")];
        this.password = customerInfo[customerTitleId.get("Password")];
        if(customerInfo[customerTitleId.get("TicketMiner Membership")].equalsIgnoreCase("TRUE")){
            this.ticketMinerMembership = true;
        }else{
            this.ticketMinerMembership = false;
        }
        this.totalSaved = 0.0;
    }
    /*
    ID                          null         0 
    First Name                  str         1
    Last Name	                str         2
    Money Available	            Double      3
    Concerts Purchased	        int         4
    TicketMiner Membership	    Boolean     5
    Username	                str         6
    password    	            str         7
    */
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Double getMoneyAvailable() {
        return moneyAvailable;
    }
    public void setMoneyAvailable(Double moneyAvailable) {
        this.moneyAvailable = moneyAvailable;
    }
    public int getConcertsPurchased() {
        return concertsPurchased;
    }
    public void setConcertsPurchased(int concertsPurchased) {
        this.concertsPurchased = concertsPurchased;
    }
    public Boolean getTicketMinerMembership() {
        return ticketMinerMembership;
    }
    public void setTicketMinerMembership(Boolean ticketMinerMembership) {
        this.ticketMinerMembership = ticketMinerMembership;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Ticket> getMyTickets() {
        return myTickets;
    }
    public void setMyTickets(ArrayList<Ticket> myTickets) {
        this.myTickets = myTickets;
    }
    public ArrayList<Event> getWatchingEvents() {
        return watchingEvents;
    }
    public void setWatchingEvents(ArrayList<Event> watchingEvents) {
        this.watchingEvents = watchingEvents;
    }
    public Hashtable<String, String[]> getPaymentMethodsOnFiles() {
        return paymentMethodsOnFiles;
    }
    public void setPaymentMethodsOnFiles(Hashtable<String, String[]> paymentMethodsOnFiles) {
        this.paymentMethodsOnFiles = paymentMethodsOnFiles;
    }
    public Double getTotalSaved() {
        return totalSaved;
    }
    public void setTotalSaved(Double totalSaved) {
        this.totalSaved = totalSaved;
    }
    public String getMyId() {
        return myId;
    }
    public void setMyId(String myId) {
        this.myId = myId;
    }
}