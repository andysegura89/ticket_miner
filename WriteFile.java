import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class WriteFile {
    public WriteFile(){
    }
    // EDIT - handle exceptions
    public static void writeLogFile(String filePath){
        
        LogSingleton log = LogSingleton.getInstance();
        // create a file Log .txt file
        try{
            FileWriter writer = new FileWriter(filePath);
            writer.write(log.getLogMessage());
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void writeCustomerReceipts(String filePath, ArrayList<Customer> myCustomers){
        // create a file Customer Receipts.txt file
        //EventType 
        //2. EventName
        //3. EventDate
        //4. TicketType
        //5. NumberofTickets
        //`6. TotalPrice
        //7. ConfirmationNumber
        try{
            FileWriter writer = new FileWriter(filePath);
            writer.write("Receipts.\n");
            for(int i =0; i < myCustomers.size(); i++){
                if(myCustomers.get(i).getMyTickets().size() == 0){
                    writer.write(myCustomers.get(i).getFirstName() + " " + myCustomers.get(i).getLastName() + " has not made any purchases and does not have any receipts.\n");
                    continue;
                }
                writer.write("Receipt for : " + myCustomers.get(i).getFirstName() + " " + myCustomers.get(i).getLastName() + ".\n");
                writer.write("Event type: " + myCustomers.get(i).getMyTickets().get(0).getEventInfo().getEventType() + ".\n");
                writer.write("Event name: " + myCustomers.get(i).getMyTickets().get(0).getEventInfo().getEventName() + ".\n");
                writer.write("Event date: " + myCustomers.get(i).getMyTickets().get(0).getEventInfo().getEventDate() + ".\n");
                writer.write("Type of ticket" + myCustomers.get(i).getMyTickets().get(0).getTypeOfTicket() + ".\n");
                writer.write("Number of tickets: " + myCustomers.get(i).getMyTickets().get(0).getNumberOfTickets() + ".\n");
                writer.write("Total Price: " + String.format("%.2f", myCustomers.get(i).getMyTickets().get(0).getTotalPriceOfPurchase()) + ".\n");
                writer.write("Confirmation number: " + myCustomers.get(i).getMyTickets().get(0).getConfirmationNum()+ ".\n\n");
                writer.write("\n\n");
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // EDIT - handle exceptions
    public static void writeNewEventFile(String eventTitles, Hashtable <Integer, Event> venueEvents, Hashtable <Integer, String> eventTitleIndex){
        // created new path to be in local location
        String pathEventList = "updatedEventListPA4.csv";
        // assume path to my local computer (hardcoded) - needed for Mac
        //String pathEventList = "/Users/angelcoronel/Desktop/updatedEventListPA4.csv";
        // Event ID,Event Type,Name,Date,Time,VIP Price,Gold Price,Silver Price,Bronze Price,General Admission Price,Venue Name,Seats Unavailable Pct,Venue Type,Capacity,Cost,VIP Pct,Gold Pct,Silver Pct,Bronze Pct,General Admission Pct,Reserved Extra Pct,Fireworks Planned,Fireworks Cost,VIP Seats Sold,Gold Seats Sold,Silver Seats Sold,Bronze Seats Sold,General Admission Seats Sold,Total VIP Revenue,Total Gold Revenue,Total Silver Revenue,Total Bronze Revenue,Total General Admission Revenue
        FileWriter writer = null;
        try{
            writer = new FileWriter(pathEventList);
            writer.append(eventTitles);
            writer.append("\n");
            java.util.Enumeration<Integer> e = venueEvents.keys();
            String recordEventInfo = "";
            while (e.hasMoreElements()) {
                int key = e.nextElement();
                
                Hashtable <String, String> eventVariables = collectEventData(venueEvents.get(key));
                for(int i =0; i < eventTitleIndex.size()-1; i++){
                    String myEventInfoString = eventVariables.get(eventTitleIndex.get(i));
                    recordEventInfo = recordEventInfo.concat(myEventInfoString);
                    recordEventInfo = recordEventInfo.concat(",");
                }
                recordEventInfo = recordEventInfo.concat(eventVariables.get(eventTitleIndex.get(eventTitleIndex.size()-1)));
                writer.append(recordEventInfo);
                writer.append("\n");
                recordEventInfo = "";
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    } 

    // EDIT - handle exceptions
    public static void writeNewCustomerFile(String customerTitles, Hashtable <Integer, Customer> customers, Hashtable <Integer, String> customerTitleIndex){
        // created new path to be in local location
        String pathCustomertList = "updatedCustomerListRandomPA4.csv";
        // assume path to my local computer (hardcoded) - needed for Mac
        //String pathCustomertList = "/Users/angelcoronel/Desktop/updatedCustomerListRandomPA4.csv";

        FileWriter writer = null;
        try{
            // assume path to my local computer (hardcoded) - needed for Mac
            writer = new FileWriter(pathCustomertList);
            writer.append(customerTitles);
            writer.append("\n");
            java.util.Enumeration<Integer> e = customers.keys();
            String recordCustomerInfo = "";
            while (e.hasMoreElements()) {
                int key = e.nextElement(); 
                Hashtable <String, String> eventVariables = collectCustomerData(customers.get(key));
                for(int i =0; i < customerTitleIndex.size()-1; i++){
                    recordCustomerInfo = recordCustomerInfo.concat(eventVariables.get(customerTitleIndex.get(i)));
                    recordCustomerInfo = recordCustomerInfo.concat(",");
                }
                recordCustomerInfo = recordCustomerInfo.concat(eventVariables.get(customerTitleIndex.get(customerTitleIndex.size()-1)));
                writer.append(recordCustomerInfo);
                writer.append("\n");
                recordCustomerInfo = "";
            }
            writer.close();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void addExtraEventTitles(Hashtable <Integer, String> eventTitleIndex, String[] extraTitles){
        int size = eventTitleIndex.size();
        for(int i = eventTitleIndex.size(); i < size + extraTitles.length; i++){
            eventTitleIndex.put(i, extraTitles[i-size] );
        }
    }

    private static Hashtable <String, String> collectCustomerData(Customer myCustomer){
        Hashtable <String, String> customerVariables = new Hashtable<String, String>();
        // ID,First Name,Last Name,Money Available,Concerts Purchased,TicketMiner Membership,Username,Password
        String ticketMinerMembership = "FALSE";
        if(myCustomer.getTicketMinerMembership()){
            ticketMinerMembership = "TRUE";
        }
        customerVariables.put("ID", myCustomer.getMyId());
        customerVariables.put("First Name", myCustomer.getFirstName());
        customerVariables.put("Last Name", myCustomer.getLastName());
        customerVariables.put("Money Available", String.format("%.2f",myCustomer.getMoneyAvailable()));
        customerVariables.put("Concerts Purchased", Integer.toString(myCustomer.getConcertsPurchased()));
        customerVariables.put("TicketMiner Membership", ticketMinerMembership);
        customerVariables.put("Username", myCustomer.getUsername());
        customerVariables.put("Password", myCustomer.getPassword());
        return customerVariables;
    }

    private static Hashtable <String, String> collectEventData(Event myEvent){
        Hashtable <String, String> eventVariables = new Hashtable<String, String>();
        String fireWorksPlanned = "No";
        String fireWorksCost = "";
        if (myEvent.getEventVenue().getHasFireworksPlanned()){
            fireWorksPlanned = "Yes";
            fireWorksCost = Integer.toString(myEvent.getEventVenue().getHasFireWorksCost());
        }
        eventVariables.put("Event ID", myEvent.getEventId());
        eventVariables.put("Event Type", myEvent.getEventType());
        eventVariables.put("Name", myEvent.getEventName());
        eventVariables.put("Date", myEvent.getEventDate());
        eventVariables.put("Time", myEvent.getEventTime());
        eventVariables.put("VIP Price", String.format("%.2f",myEvent.getTicketPrices().get("vip")));
        eventVariables.put("Gold Price", String.format("%.2f",myEvent.getTicketPrices().get("gold")));
        eventVariables.put("Silver Price", String.format("%.2f",myEvent.getTicketPrices().get("silver")));
        eventVariables.put("Bronze Price", String.format("%.2f",myEvent.getTicketPrices().get("bronze")));
        eventVariables.put("General Admission Price", String.format("%.2f",myEvent.getTicketPrices().get("generalAdmission")));
        eventVariables.put("Venue Name", myEvent.getEventVenue().getVenueName());
        eventVariables.put("Seats Unavailable Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("unavailable")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("Venue Type", myEvent.getEventVenue().getVenueType());
        eventVariables.put("Capacity", Integer.toString(myEvent.getEventVenue().getVenueCapacity()));
        eventVariables.put("Cost", Integer.toString(myEvent.getEventVenue().getVenueCost()));
        eventVariables.put("VIP Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("vip")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("Gold Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("gold")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("Silver Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("silver")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("Bronze Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("bronze")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("General Admission Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("generalAdmission")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("Reserved Extra Pct", Integer.toString((int)((myEvent.getTicketsAvailable().get("reservedExtra")/myEvent.getEventVenue().getVenueCapacity())* 100)));
        eventVariables.put("Fireworks Planned", fireWorksPlanned);
        eventVariables.put("Fireworks Cost", fireWorksCost);
        
        eventVariables.put("VIP Seats Sold", Integer.toString(totalSeatsSold(myEvent, "vip")));
        eventVariables.put("Gold Seats Sold", Integer.toString(totalSeatsSold(myEvent, "gold")));
        eventVariables.put("Silver Seats Sold", Integer.toString(totalSeatsSold(myEvent, "silver")));
        eventVariables.put("Bronze Seats Sold", Integer.toString(totalSeatsSold(myEvent, "bronze")));
        eventVariables.put("General Admission Seats Sold", Integer.toString(totalSeatsSold(myEvent, "generalAdmission")));
        eventVariables.put("Total VIP Revenue", String.format("%.2f", myEvent.getTicketPrices().get("vip") * totalSeatsSold(myEvent, "vip")));
        eventVariables.put("Total Gold Revenue", String.format("%.2f", myEvent.getTicketPrices().get("gold") * totalSeatsSold(myEvent, "gold")));
        eventVariables.put("Total Silver Revenue", String.format("%.2f", myEvent.getTicketPrices().get("silver") * totalSeatsSold(myEvent, "silver")));
        eventVariables.put("Total Bronze Revenue", String.format("%.2f", myEvent.getTicketPrices().get("bronze") * totalSeatsSold(myEvent, "bronze")));
        eventVariables.put("Total General Admission Revenue", String.format("%.2f", myEvent.getTicketPrices().get("generalAdmission") * totalSeatsSold(myEvent, "generalAdmission")));
        
        return eventVariables;
    }

    private static int totalSeatsSold(Event myEvent, String typeOfTicket){
        int ticketCount = 0;
        if(myEvent.getTicketsPurchased().get(typeOfTicket) == null){
            return 0;
        }
        for(int i=0; i < myEvent.getTicketsPurchased().get(typeOfTicket).size(); i++){
            ticketCount += myEvent.getTicketsPurchased().get(typeOfTicket).get(i).getNumberOfTickets();
        }
        return ticketCount;
    }
}
