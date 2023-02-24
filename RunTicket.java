/*
 * Angel Coronel
 * 88743045
 * 10/23/22
 * CS 3331
 * Daniel Mejia
 * Programming Assignment 4
 * Updated a Ticket Master System to create updated .csv files and 
 * allow for admin inquieries
 * 
 * Honesty Statement:
 * This work was done individually and completely on my own. 
 * I did not share, reproduce, or alter any part of this 
 * assignment for any purpose. I did not share code, upload 
 * this assignment online in any form, or view/received/modified 
 * code written from anyone else. All deliverables were 
 * produced entirely on my own. This assignment is part of an 
 * academic course at The University of Texas at El Paso and a 
 * grade will be assigned for the work I produced.
 */
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class RunTicket {
    public static void main(String []args){
        Scanner scnr = new Scanner(System.in);
        
        // created new path to be in local location
        String eventPath = "EventListPA4-A.csv";

        // assume path to my local computer (hardcoded) - needed for Mac
        //String eventPath = "/Users/angelcoronel/Desktop/EventListPA4-A.csv";
        //String eventPath = "/Users/angelcoronel/Desktop/EventListPA4-B.csv";
        ArrayList<String[]> myEvents = new ArrayList<>();
        String eventTitles = ReadCSV.readCSV(eventPath, myEvents);
        Hashtable <String, Integer> eventTitleId = new Hashtable<String, Integer>();
        Hashtable <Integer, String> eventTitleIndex = new Hashtable<Integer, String>();
        ReadCSV.indexTitleOrder(eventTitleId, eventTitles, eventTitleIndex);
        System.out.println("index for Event ID" +eventTitleId.get("Event ID"));
        // Create event objects using a Creational Design Pattern
        Hashtable <String, Integer> eventIds = new Hashtable<String, Integer>();
        Hashtable <Integer, Event> venueEvents = new Hashtable <Integer, Event>();
        createEventObjects(myEvents, venueEvents, eventIds, eventTitleId);

        // created new path to be in local location
        String customerPath = "CustomerListRandomPA4.csv";

        // assume path to my local computer (hardcoded) - needed for Mac
        //String customerPath = "/Users/angelcoronel/Desktop/CustomerListRandomPA4.csv";
        ArrayList<String[]> myCustomers = new ArrayList<>();
        String customerTitles = ReadCSV.readCSV(customerPath, myCustomers);
        Hashtable <String, Integer> customerTitleId = new Hashtable<String, Integer>();
        Hashtable <Integer, String> customerTitleIndex = new Hashtable<Integer, String>();
        ReadCSV.indexTitleOrder(customerTitleId, customerTitles, customerTitleIndex);
        Hashtable<String, Customer> customerUsernames = new Hashtable<String, Customer>();
        Hashtable <Integer, Customer> customers = new Hashtable<Integer, Customer>(); // connects (unique) username to customer object 
        createCustomerObjects(myCustomers, customers, customerTitleId, customerUsernames);
        
        // created new path to be in local location
        String autoPurchasePath = "AutoPurchase10K.csv";

        // assume path to my local computer (hardcoded) - needed for Mac
        //String autoPurchasePath = "/Users/angelcoronel/Desktop/AutoPurchase10K.csv";
        
        LogSingleton log = LogSingleton.getInstance();
        log.setLogMessage("Log of actions taken:\n");
        log.setLogMessage(log.getLogMessage().concat("\nAdded by creating inside a function\n"));
        greetUser();
        log.setLogMessage(log.getLogMessage().concat("User is being asked if they are an administrator.\n"));
        Boolean adminUser = isAdminUser();
        log.setLogMessage(log.getLogMessage().concat("User admin is: " + adminUser+ "\n"));
        // allow admin use
        if(adminUser){
            adminUserUI(venueEvents, eventIds, eventTitleIndex, eventTitleId, customerUsernames, autoPurchasePath);
        }
        // allow for user login(s) & transactions
        else{
            userUI(customers, venueEvents);
        }
        log.setLogMessage(log.getLogMessage().concat("\nThe program was terminated\n"));
        
        //VIP Seats Sold,Gold Seats Sold,Silver Seats Sold,Bronze Seats Sold,General Admission Seats Sold,Total VIP Revenue,Total Gold Revenue,Total Silver Revenue,Total Bronze Revenue,Total General Admission Revenue
        String[] extraTitles = {"VIP Seats Sold","Gold Seats Sold","Silver Seats Sold","Bronze Seats Sold","General Admission Seats Sold","Total VIP Revenue","Total Gold Revenue","Total Silver Revenue","Total Bronze Revenue","Total General Admission Revenue"};
        WriteFile.addExtraEventTitles(eventTitleIndex, extraTitles);
        // update header titles in .cav file
        eventTitles = eventTitles + ",VIP Seats Sold,Gold Seats Sold,Silver Seats Sold,Bronze Seats Sold,General Admission Seats Sold,Total VIP Revenue,Total Gold Revenue,Total Silver Revenue,Total Bronze Revenue,Total General Admission Revenue";
        WriteFile.writeNewEventFile(eventTitles, venueEvents, eventTitleIndex);
        WriteFile.writeNewCustomerFile(customerTitles, customers, customerTitleIndex);

        // created new path to be in local location
        String filePathLog = "log.txt";
        // assume path to my local computer (hardcoded) - needed for Mac
        //String filePathLog = "/Users/angelcoronel/Desktop/log.txt";
        WriteFile.writeLogFile(filePathLog);

        ArrayList<Customer> specialCustomer = new ArrayList<>();
        addSpecialCustomers(specialCustomer, customerUsernames);
        // created new path to be in local location
        String filePathCustomerReceipt = "customerReceipt.txt";
        // assume path to my local computer (hardcoded) - needed for Mac
        //String filePathCustomerReceipt = "/Users/angelcoronel/Desktop/customerReceipt.txt";
        WriteFile.writeCustomerReceipts(filePathCustomerReceipt, specialCustomer);

        disregardUser();
        scnr.close();
    }

    public static void userUI(Hashtable <Integer, Customer> customers, Hashtable <Integer, Event>venueEvents){
        Scanner scnr = new Scanner(System.in);
        Boolean exit = false;
        LogSingleton log = LogSingleton.getInstance();
        int ticketsTransactions = 0;
        int userAccountId = 0;
        Boolean userHasLoggedIn = false;
        Boolean makeAnotherTransaction = true;
        int ticketAmount = 0;
        ArrayList<Customer> myCustomers = new ArrayList<>();
        Boolean individualCustomer = isSingleCustomer();
        log.setLogMessage(log.getLogMessage().concat("User has entered if is single login."));
        log.setLogMessage(log.getLogMessage().concat("Single user login is " + individualCustomer+ "\n"));
        if (individualCustomer){
            userAccountId = getUserAccountId(customers);
            // user wishes to exit
            if (userAccountId == -1){
                return;
            }
            
            Customer myCustomer = customers.get(userAccountId);
            myCustomers.add(myCustomer);
            log.setLogMessage(log.getLogMessage().concat("Recorded user info and saved as: " + myCustomer.getFirstName() + " "+ myCustomer.getLastName()+ "\n"));
            
            userHasLoggedIn = makeUserLogIn(myCustomer);
            // user wishes to exit
            if(!userHasLoggedIn){
                return;
            }

            log.setLogMessage(log.getLogMessage().concat("User has logged in as " + myCustomer.getPassword() +".\n"));
            // allow user to do transactions as many times as they wish
            while(makeAnotherTransaction){
                processTransaction(venueEvents, myCustomer, ticketAmount, ticketsTransactions);

                log.setLogMessage(log.getLogMessage().concat("Asked user if they want to do another transaction.\n"));
                makeAnotherTransaction = askToMakeAnotherTransaction();
                if(makeAnotherTransaction){
                    ticketsTransactions += 1;
                    log.setLogMessage(log.getLogMessage().concat("User wants to do another transaction.\n"));
                    continue;
                }
                else{
                    log.setLogMessage(log.getLogMessage().concat("User does not want to do another transaction.\n"));
                    break;
                }
            }
        }else{
            // not a single customer (multiple customers), repeat customer "log-in's"
            Boolean logInAgain = true;
            while(logInAgain){
                makeAnotherTransaction = true;
                userAccountId = getUserAccountId(customers);
                // user wishes to exit
                if (userAccountId == -1){
                    return;
                }
                Customer myCustomer = customers.get(userAccountId);
                myCustomers.add(myCustomer);
                log.setLogMessage(log.getLogMessage().concat("Recorded user info and saved as: " + myCustomer.getFirstName() + " "+ myCustomer.getLastName()+ "\n"));
                
                userHasLoggedIn = makeUserLogIn(myCustomer);
                // user wishes to exit
                if(!userHasLoggedIn){
                    return;
                }

                log.setLogMessage(log.getLogMessage().concat("User has logged in as " + myCustomer.getPassword() +".\n"));
                // allow user to do transactions as many times as they wish
                while(makeAnotherTransaction){
                    processTransaction(venueEvents, myCustomer, ticketAmount, ticketsTransactions);

                    log.setLogMessage(log.getLogMessage().concat("Asked user if they want to do another transaction.\n"));
                    makeAnotherTransaction = askToMakeAnotherTransaction();
                    if(makeAnotherTransaction){
                        ticketsTransactions += 1;
                        log.setLogMessage(log.getLogMessage().concat("User wants to do another transaction.\n"));
                        continue;
                    }
                    else{
                        log.setLogMessage(log.getLogMessage().concat("User does not want to do another transaction.\n"));
                        log.setLogMessage(log.getLogMessage().concat("Asked user if they wish to exit.\n"));
                        exit = askToExit();
                        if(exit){
                            log.setLogMessage(log.getLogMessage().concat("User has chosen to exit the program.\n"));
                            return;
                        }else{
                            log.setLogMessage(log.getLogMessage().concat("User has chosen to proceed.\n"));
                            break;
                        }
                    }
                }

                log.setLogMessage(log.getLogMessage().concat("Asked User if they want to login again."));
                //ask user if a different acct will login
                logInAgain = askIfLogInAgain();
                log.setLogMessage(log.getLogMessage().concat("User login response is " + logInAgain + "\n"));
                if(logInAgain){
                    userHasLoggedIn = false;
                    exit = false;
                    userAccountId = 0;
                }
            }
        }
        log.setLogMessage(log.getLogMessage().concat("User info was shown to user\n"));
        showCustomersInfo(myCustomers);
    }

    public static void adminUserUI(Hashtable <Integer, Event>venueEvents, Hashtable <String, Integer> eventIds, Hashtable <Integer, String> eventTitleIndex, Hashtable <String, Integer> eventTitleId, Hashtable<String, Customer> customerUsernames, String autoPurchasePath){
        LogSingleton log = LogSingleton.getInstance();
        log.setLogMessage(log.getLogMessage().concat("Admin is being asked if they wish to run automatic purchasing functionality.\n"));
        Boolean automaticPurchase = askAutomaticPurchasing();
        if(automaticPurchase){
            readAutoPurchaseCSV(autoPurchasePath, customerUsernames, venueEvents);
        }
        System.out.println("Please choose an event from the availbale options (A, B or C):");
        Boolean isInquiring = true;
        while(isInquiring){
            log.setLogMessage(log.getLogMessage().concat("Admin is being asked method to inquire for an event.\n"));
            String adminChoice = askAdminChoice();
            if(adminChoice.equalsIgnoreCase("a")){
                log.setLogMessage(log.getLogMessage().concat("Admin chose to inquire by event ID.\n"));
                log.setLogMessage(log.getLogMessage().concat("Admin is being asked for event ID.\n"));
                int eventId = askForEventIdByNumber(venueEvents);
                log.setLogMessage(log.getLogMessage().concat("Admin chose: " + eventId + ".\n"));
                log.setLogMessage(log.getLogMessage().concat("Showing event information of event to admin.\n"));
                showEventInfo(venueEvents.get(eventId), eventId);
            }else if(adminChoice.equalsIgnoreCase("b")){
                log.setLogMessage(log.getLogMessage().concat("Admin chose to inquire by event Name.\n"));
                
                log.setLogMessage(log.getLogMessage().concat("Admin is being asked for event id.\n"));
                int eventId = askForEventIdByName(venueEvents, eventIds);
                log.setLogMessage(log.getLogMessage().concat("Admin chose: " + venueEvents.get(eventId).getEventName() + ".\n"));
                
                log.setLogMessage(log.getLogMessage().concat("Showing information of event to admin.\n"));
                showEventInfo(venueEvents.get(eventId), eventId);
            }else{
                addEvent(venueEvents, eventIds, eventTitleIndex, eventTitleId);
            }
            log.setLogMessage(log.getLogMessage().concat("Asking admin if they wish to continue inquiring.\n"));
            isInquiring = askAdminContinue();
            log.setLogMessage(log.getLogMessage().concat("Admin continue inquiring: " + isInquiring + "\n"));
        }
    }

    public static void addSpecialCustomers(ArrayList<Customer> specialCustomer, Hashtable<String, Customer> customerUsernames){
        specialCustomer.add(customerUsernames.get("angelcoronel"));
        specialCustomer.add(customerUsernames.get("stitchdisney"));
        specialCustomer.add(customerUsernames.get("plutodisney"));
        

    }
    
    public static Boolean askAutomaticPurchasing(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Do you want to automatically add the purchases on file?");
        System.out.println("Please enter y/n:");
        String userResponse = scnr.nextLine();
        while(!(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes") || userResponse.equalsIgnoreCase("n") || userResponse.equalsIgnoreCase("no"))){
            System.out.println("There was an error with your response.");
            System.out.println("Please enter only Y or N as a response: ");
            userResponse = scnr.nextLine();
        }
        if(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes")){
            return true;
        }
        return false;
    }

    public static void readAutoPurchaseCSV(String autoPurchasePath, Hashtable<String, Customer> customerUsernames, Hashtable <Integer, Event>venueEvents){
        String line = "";
        String titlesString = "";
        int ticketsTransactionsNum = 0;
        // read csv lines through a bufferedreader
        try{ 
            BufferedReader br = new BufferedReader(new FileReader(autoPurchasePath));
            titlesString = br.readLine(); // read the title line
            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                // continue if no values
                if(values.length <1)
                    continue;
                String username = values[0].toLowerCase() + values[1].toLowerCase(); 
                Customer myCustomer = customerUsernames.get(username);
                String typeOfTicket = "";
                //['Bronze', 'Silver', 'Gold', 'General Admission', 'VIP']
                if(values[6].equalsIgnoreCase("Bronze")){
                    typeOfTicket = "bronze";
                }else if(values[6].equalsIgnoreCase("Silver")){
                    typeOfTicket = "silver";
                }else if(values[6].equalsIgnoreCase("Gold")){
                    typeOfTicket = "gold";
                }else if(values[6].equalsIgnoreCase("General Admission")){
                    typeOfTicket = "generalAdmission";
                }else if(values[6].equalsIgnoreCase("VIP")){
                    typeOfTicket = "vip";
                }
                int ticketAmount = Integer.parseInt(values[5]);
                int eventId = Integer.parseInt(values[5]);
                // assume buying every transaction
                Event currentEvent = venueEvents.get(eventId);
                processAutomaticTransaction(currentEvent, myCustomer, ticketAmount, ticketsTransactionsNum, typeOfTicket);
                ticketsTransactionsNum += 1;
            }
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void processAutomaticTransaction(Event myEvent, Customer myCustomer, int ticketAmount, int ticketsTransactionsNum, String typeOfTicketForTransaction){
        LogSingleton log = LogSingleton.getInstance();
        if(ticketAmount <0){
            System.out.println("Can't buy a negative amount of tickets.");
            return;
        }
        if(ticketAmount >6){
            System.out.println("Amount of tickets for purchase is too much: " + ticketAmount);
            return;
        }
        if(myEvent.getTicketPrices().get(typeOfTicketForTransaction) > myCustomer.getMoneyAvailable()){
            System.out.println("Customer is broke and can't afford even 1 " + typeOfTicketForTransaction + " ticket...");
            return;
        }
        if(ticketAmount * myEvent.getTicketPrices().get(typeOfTicketForTransaction) * 1.0825 > myCustomer.getMoneyAvailable()){
            System.out.println("Customer is broke and can't afford this transaction...");
            return;
        }
        if(ticketAmount > myEvent.getTicketsAvailable().get(typeOfTicketForTransaction)){
            System.out.println("Venue is past capacity and can't process any more purchases of this event...");
            return;
        } 
        // assume can make purchase at this point
        // get price of purchase, savings, and taxes
        Double totalPriceOfPurchase = ticketAmount * myEvent.getTicketPrices().get(typeOfTicketForTransaction);
        Double totalSavingsOfPurchase = 0.0;
        if(myCustomer.getTicketMinerMembership()){
            totalSavingsOfPurchase = totalPriceOfPurchase * 0.1;
            totalPriceOfPurchase = totalPriceOfPurchase * 0.9;
        }
        Double totalTaxesOfPurchase = totalPriceOfPurchase * 0.0825;
        totalPriceOfPurchase = totalPriceOfPurchase * 1.0825;

        // update tickets available (Event: ticketsAvailable)
        myEvent.getTicketsAvailable().put(typeOfTicketForTransaction, myEvent.getTicketsAvailable().get(typeOfTicketForTransaction) - ticketAmount);
        log.setLogMessage(log.getLogMessage().concat("Updated amount of tickets available.\n"));
        // update tickets purchased (Event: ticketsPurchased)
        if(!myEvent.getTicketsPurchased().contains(typeOfTicketForTransaction)){
            ArrayList<Ticket> newTicketList = new ArrayList<>();
            newTicketList.add(new Ticket(myEvent, ticketAmount, totalPriceOfPurchase, ticketsTransactionsNum, typeOfTicketForTransaction));
            myEvent.getTicketsPurchased().put(typeOfTicketForTransaction, newTicketList);
        }else{
            myEvent.getTicketsPurchased().get(typeOfTicketForTransaction).add(new Ticket(myEvent, ticketAmount, totalPriceOfPurchase, ticketsTransactionsNum, typeOfTicketForTransaction));
        }
        log.setLogMessage(log.getLogMessage().concat("Updated tickets purchased in event.\n"));
        // update tickets sold  (Event: ticketsSoldNum)
        myEvent.setNumTicketsSold(ticketAmount + myEvent.getNumTicketsSold());
        log.setLogMessage(log.getLogMessage().concat("Updated tickets sold in event.\n"));
        // update event taxes (Event: totalTaxed)
        myEvent.setTotalTaxed(myEvent.getTotalTaxed() + totalTaxesOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated event taxes.\n"));
        // update event discounted (Event: totalDiscounted)
        myEvent.setTotalDiscounted(myEvent.getTotalDiscounted() + totalSavingsOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated event total discounted.\n"));
        // update customer money (Customer: moneyAvailable)
        myCustomer.setMoneyAvailable(myCustomer.getMoneyAvailable() - totalPriceOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated customer money available.\n"));
        // update customer concerts purchased (Customer: concertsPurchased +1)
        myCustomer.setConcertsPurchased(myCustomer.getConcertsPurchased() + 1);
        log.setLogMessage(log.getLogMessage().concat("Updated customer concerts purchased.\n"));
        // add transaction to customer tickets (Customer: myTickets)
        myCustomer.getMyTickets().add(new Ticket(myEvent, ticketAmount, totalPriceOfPurchase, ticketsTransactionsNum, typeOfTicketForTransaction));
        log.setLogMessage(log.getLogMessage().concat("Updated customer tickets.\n"));
        // add cutomer events (Customer: watchingEvents)
        myCustomer.getWatchingEvents().add(myEvent);
        log.setLogMessage(log.getLogMessage().concat("Updated customer events.\n"));
        // update customer savings (Customer: totalSaved)
        myCustomer.setTotalSaved(myCustomer.getTotalSaved() + totalSavingsOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated customer savings.\n"));
    }

    public static void addEvent(Hashtable <Integer, Event>venueEvents, Hashtable <String, Integer> eventIds, Hashtable <Integer, String> eventTitleIndex, Hashtable <String, Integer> eventTitleId){
        int newEventId = findLargestEventId(venueEvents) +1;
        String[] eventTypes = {"Concert", "Special", "Sport"};
        String newEventType = askForNewEventInfo(eventTypes, 0);
        String newEventName = askForNewEventInfo(eventTypes, 1);
        String newEventDate = askForNewEventInfo(eventTypes, 2);
        String newEventTime = askForNewEventInfo(eventTypes, 3);

        String[] venues = {"Sun Bowl Stadium", "Don Haskins Center", "Magoffin Auditorium", "San Jacinto Plaza", "Centennial Plaza"};
        String newVenueName = askForNewEventInfo(venues, 0);
        String[] venueTypes = {"Stadium", "Open Air", "Auditorium", "Arena"};
        String newVenueType = askForNewEventInfo(venueTypes, 0);
        int newCost = askForNewVenueCost();

        int newVipPct = 5;
        int newGoldPct = 10;
        int newSilverPct = 15;
        int newBronzePct = 20;
        int newGenAdmitPct = 45;
        int newResExtrPct = 5;
        int newUnvlbPct = 0; // asume all seats are available

        Double newGenAdmitPrice = askForNewGenAdmitPrice();
        Double newVipPrice = newGenAdmitPrice * 5;
        Double newGoldPrice = newGenAdmitPrice * 3;
        Double newSilverPrice = newGenAdmitPrice * 2.5;
        Double newBronzePrice = newGenAdmitPrice * 1.5;

        Boolean hasFireWorksPlanned = false;
        String newFireWorksPlanned = "No";
        String newFireWorksCost = "";
        if(newVenueName.equalsIgnoreCase("Sun Bowl Stadium") || newVenueName.equalsIgnoreCase("San Jacinto Plaza") || newVenueName.equalsIgnoreCase("Centennial Plaza")){
            hasFireWorksPlanned = askForNewEventFireWorks();
            if(hasFireWorksPlanned){
                newFireWorksPlanned = "Yes";
                newFireWorksCost = Integer.toString(askForNewEventFireWorksCost());
            }
        }else{
            newFireWorksPlanned = "";
        }
        int capacity = -1;
        if(newVenueName.equalsIgnoreCase("Sun Bowl Stadium")){
            capacity = 58000;
        }else if(newVenueName.equalsIgnoreCase("Don Haskins Center")){
            capacity = 12800;
        }else if(newVenueName.equalsIgnoreCase("Magoffin Auditorium")){
            capacity = 1152;
        }else if(newVenueName.equalsIgnoreCase("San Jacinto Plaza")){
            capacity = 1500;
        }else if(newVenueName.equalsIgnoreCase("Centennial Plaza")){
            capacity = 5000;
        }

        // use WriteFile "technique" to create new event
        //call createEvent with newinfo

        Hashtable <String, String> eventVariables = new Hashtable<String, String>();
        eventVariables.put("Event ID", Integer.toString(newEventId));
        eventVariables.put("Event Type", newEventType);
        eventVariables.put("Name", newEventName);
        eventVariables.put("Date", newEventDate);
        eventVariables.put("Time", newEventTime);
        eventVariables.put("VIP Price", String.format("%.2f",newVipPrice));
        eventVariables.put("Gold Price", String.format("%.2f",newGoldPrice));
        eventVariables.put("Silver Price", String.format("%.2f",newSilverPrice));
        eventVariables.put("Bronze Price", String.format("%.2f",newBronzePrice));
        eventVariables.put("General Admission Price", String.format("%.2f",newGenAdmitPrice));
        eventVariables.put("Venue Name", newVenueName);
        eventVariables.put("Seats Unavailable Pct", Integer.toString(newUnvlbPct));
        eventVariables.put("Venue Type", newVenueType);
        eventVariables.put("Capacity", Integer.toString(capacity));
        eventVariables.put("Cost", Integer.toString(newCost));
        eventVariables.put("VIP Pct", Integer.toString(newVipPct));
        eventVariables.put("Gold Pct", Integer.toString(newGoldPct));
        eventVariables.put("Silver Pct", Integer.toString(newSilverPct));
        eventVariables.put("Bronze Pct", Integer.toString(newBronzePct));
        eventVariables.put("General Admission Pct", Integer.toString(newGenAdmitPct));
        eventVariables.put("Reserved Extra Pct", Integer.toString(newResExtrPct));
        eventVariables.put("Fireworks Planned", newFireWorksPlanned);
        eventVariables.put("Fireworks Cost", newFireWorksCost);

        String eventInfo[] = new String[eventTitleIndex.size()];
        for(int i =0; i < eventTitleIndex.size(); i++){
            String myEventInfoString = eventVariables.get(eventTitleIndex.get(i));
            eventInfo[i] = myEventInfoString;
        }

        EventFactory myEventFactory = new EventFactory();
        Event newEvent = myEventFactory.createEvent(eventInfo, eventTitleId);
        venueEvents.put(newEventId, newEvent);
        eventIds.put(newEvent.getEventName(), newEventId);
    }

    public static int askForNewEventFireWorksCost(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("What is the cost of the fireworks?");
        int cost = (int)scnr.nextDouble();
        while(cost <1 || cost > 10000){
            System.out.print("Please choose a cost between $1 and $10,000.");
            System.out.println("What is the cost of the fireworks?");
            cost = (int)scnr.nextDouble();
        }
        return cost;
    }

    public static Boolean askForNewEventFireWorks(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Do you want to have fireworks planned for this event?");
        System.out.println("Please enter y/n:");
        String userResponse = scnr.nextLine();
        while(!(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes") || userResponse.equalsIgnoreCase("n") || userResponse.equalsIgnoreCase("no"))){
            System.out.println("There was an error with your response.");
            System.out.println("Please enter only Y or N as a response: ");
            userResponse = scnr.nextLine();
        }
        if(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes")){
            return true;
        }
        return false;
    }

    public static int findLargestEventId(Hashtable <Integer, Event> venueEvents){
        java.util.Enumeration<Integer> e = venueEvents.keys();
        int largestId = 0;
        while (e.hasMoreElements()) {
            int key = e.nextElement();
            if(key  > largestId){
                largestId = key;
            }
        }
        return largestId;
    }

    public static int askForNewVenueCost(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("What is the cost of the venue?");
        int cost = (int)scnr.nextDouble();
        while(cost <1 || cost > 10000000){
            System.out.print("Please choose a cost between $1 and $10,000,000.");
            System.out.println("What is the cost of the venue?");
            cost = (int)scnr.nextDouble();
        }
        return cost;
    }

    public static Double askForNewGenAdmitPrice(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("What is the price of a general admission ticket?");
        Double price = scnr.nextDouble();
        while(price <1.0 || price > 1000.0){
            System.out.print("Please choose a price between $1 and $1,000.");
            System.out.println("What is the price of a general admission ticket?");
            price = scnr.nextDouble();
        }
        return price;
    }
    
    public static Boolean checkValidDate(String date){
        Set<Character> numSet = new HashSet<Character> (); 
        numSet.addAll(Arrays.asList(new Character[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));
        // M/D/YYYY
        if(date.length() <8 ){
            System.out.println("Date format is not valid. Input too short");
            return false;
        }else if (date.length() > 10){
            System.out.println("Date format is not valid. Input too long");
            return false;
        }else if(date.length() ==8){
            if(date.charAt(1) != '/' || date.charAt(3) != '/' ){
                System.out.println("Date format is not valid.");
                return false;
            }
            for(int i =0; i < date.length(); i++){
                if(i == 1 || i ==3 ){
                    continue;
                }
                if(!numSet.contains(date.charAt(i))){
                    System.out.println("Input is not valid. Please enter only numbers.");
                    return false;
                }
            }
            return true;
            
        } // MM/DD/YYYY 
        else if(date.length() ==10){
            if(date.charAt(2) != '/' || date.charAt(4) != '/' ){
                System.out.println("Date format is not valid.");
                return false;
            }
            for(int i =0; i < date.length(); i++){
                if(i == 2 || i == 4 ){
                    continue;
                }
                if(!numSet.contains(date.charAt(i))){
                    System.out.println("Input is not valid. Please enter only numbers.");
                    return false;
                }
            }
            return true;
        }else if(date.length() ==9){
            // M/DD/YYYY 
            if(date.charAt(1) == '/'){
                if( date.charAt(4) != '/' ){
                    System.out.println("Date format is not valid.");
                    return false;
                }
                for(int i =0; i < date.length(); i++){
                    if(i == 1 || i == 4 ){
                        continue;
                    }
                    if(!numSet.contains(date.charAt(i))){
                        System.out.println("Input is not valid. Please enter only numbers.");
                        return false;
                    }
                }
                return true;
            }
            // MM/D/YYYY
            if(date.charAt(2) == '/'){
                if( date.charAt(4) != '/' ){
                    System.out.println("Date format is not valid.");
                    return false;
                }
                for(int i =0; i < date.length(); i++){
                    if(i == 1 || i == 4 ){
                        continue;
                    }
                    if(!numSet.contains(date.charAt(i))){
                        System.out.println("Input is not valid. Please enter only numbers.");
                        return false;
                    }
                }
                return true;
            }
        }
        System.out.println("MAINTENANCE NEEDED - DATE INPUT CHECK FUNCTION MALFUNCTION");
        return true;
    }
    
    public static Boolean checkValidTime(String time){
        // XX:XX AM (or PM)
        Set<Character> numSet = new HashSet<Character> (); 
        numSet.addAll(Arrays.asList(new Character[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));
        if(time.length() <6 ){
            System.out.println("Time format is not valid. Input too short.");
            return false;
        }else if (time.length() > 8){
            System.out.println("Time format is not valid. Input too long.");
            return false;
        }else if(time.charAt(time.length()-1) != 'M'){
            System.out.println("Time format is not valid. Write 'AM' or 'PM'.");
            return false;
        }
        // X:X AM (or PM)
        if(time.length() == 6){
            if(time.charAt(1) != ':'){
                System.out.println("Time format is not valid. Missing ':' in input." );
                return false;
            }
            if(time.charAt(3) != ' '){
                System.out.println("Time format is not valid. Missing space between numbers and PM/AM in input." );
                return false;
            }
            if(!(time.charAt(4) == 'A' || time.charAt(4) == 'P' )){
                System.out.println("Date format is not valid. Write 'AM' or 'PM'.");
                return false;
            }
            for(int i =0; i < 3; i++){
                if(i == 1 ){
                    continue;
                }
                if(!numSet.contains(time.charAt(i))){
                    System.out.println("Input is not valid. Please enter only numbers.");
                    return false;
                }
            }
            return true;
        }
        // XX:XX AM (or PM) 
        if(time.length() == 8){
            if(time.charAt(2) != ':'){
                System.out.println("Time format is not valid. Missing ':' in input." );
                return false;
            }
            if(time.charAt(5) != ' '){
                System.out.println("Time format is not valid. Missing space between numbers and PM/AM in input." );
                return false;
            }
            if(!(time.charAt(6) == 'A' || time.charAt(6) == 'P' )){
                System.out.println("Date format is not valid. Write 'AM' or 'PM'.");
                return false;
            }
            for(int i =0; i < 5; i++){
                if(i == 2 ){
                    continue;
                }
                if(!numSet.contains(time.charAt(i))){
                    System.out.println("Input is not valid. Please enter only numbers.");
                    return false;
                }
            }
            return true;
        }
        // X:XX AM (or PM) || XX:X AM (or PM)
        if(time.length() == 7){
            if(time.charAt(2) == ':'){
                if(time.charAt(4) != ' '){
                    System.out.println("Time format is not valid. Missing space between numbers and PM/AM in input." );
                    return false;
                }
                if(!(time.charAt(5) == 'A' || time.charAt(5) == 'P' )){
                    System.out.println("Date format is not valid. Write 'AM' or 'PM'.");
                    return false;
                }
                for(int i =0; i < 4; i++){
                    if(i == 2 ){
                        continue;
                    }
                    if(!numSet.contains(time.charAt(i))){
                        System.out.println("Input is not valid. Please enter only numbers.");
                        return false;
                    }
                }
                return true;
            }
            else if(time.charAt(1) == ':'){
                if(time.charAt(4) != ' '){
                    System.out.println("Time format is not valid. Missing space between numbers and PM/AM in input." );
                    return false;
                }
                if(!(time.charAt(5) == 'A' || time.charAt(5) == 'P' )){
                    System.out.println("Date format is not valid. Write 'AM' or 'PM'.");
                    return false;
                }
                for(int i =0; i < 4; i++){
                    if(i == 1 ){
                        continue;
                    }
                    if(!numSet.contains(time.charAt(i))){
                        System.out.println("Input is not valid. Please enter only numbers.");
                        return false;
                    }
                }
                return true;
            }else{
                System.out.println("Input is not valid.");
                return false;
            }
        }
        System.out.println("MAINTENANCE NEEDED - DATE INPUT CHECK FUNCTION MALFUNCTION");
        return true;
    }

    private static String askForNewEventInfo(String[] options, int typeOfData){
        Scanner scnr = new Scanner(System.in);
        int choice;
        String typeChoice = "";
        switch(typeOfData){
            // ask for choice from list
            case 0:
                System.out.println("Please choose one of the following options.");
                for(int i =0; i< options.length; i++){
                    System.out.println(i+1 + ": " + options[i]);
                }
                choice = (int)scnr.nextDouble();
                while(choice <1 || choice > options.length){
                    System.out.println(" There are only event types from 1 to " + options.length + ".");
                    System.out.println("What is the # of your choice?");
                    choice = (int)scnr.nextDouble();
                }
                typeChoice =  options[choice];
                return typeChoice;

            // ask for event name
            case 1:
                System.out.println("Please enter the NAME of the new event:");
                typeChoice = scnr.nextLine();
                return typeChoice;

            // ask for event date MM/DD/YYYY
            case 2:
                System.out.println("Please enter the DATE (MM/DD/YYYY) of the new event:");
                typeChoice = scnr.nextLine();
                Boolean isValidDateInput = checkValidDate(typeChoice);
                while(!isValidDateInput){
                    System.out.println("Use the format of MM/DD/YYYY.");
                    System.out.println("Please enter the date:");
                    typeChoice = scnr.nextLine();
                    isValidDateInput = checkValidDate(typeChoice);
                }
                return typeChoice;

            // ask for event time XX:XX AM (or PM)
            case 3:
                System.out.println("Please enter the TIME (XX:XX AM or XX:XX PM) of the new event:");
                typeChoice = scnr.nextLine();
                Boolean isValidTimeInput = checkValidTime(typeChoice);
                while(!isValidTimeInput){
                    System.out.println("Use the format of XX:XX AM or XX:XX PM. Space is included between the numbers.");
                    System.out.println("PM or AM must be capitalized. Space is included between the numbers.");
                    System.out.println("Please enter the time:");
                    typeChoice = scnr.nextLine();
                    isValidTimeInput = checkValidTime(typeChoice);
                }
                return typeChoice;

            default:
                System.out.println("An error in collecting info for adding a new event has occurred.");
        }
        return "";
    }

    public static int askForEventIdByName(Hashtable<Integer, Event> venueEvents, Hashtable<String, Integer> eventIds) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("What is the name of the event?");
        String searchName = scnr.nextLine();
        while(!eventIds.containsKey(searchName)){
            System.out.print("Please choose an event from the availbale events.");
            System.out.println(" Make sure that all spelling is correct and is case sensitive.");
            System.out.println("What is the name of the event?");
            searchName = scnr.nextLine();
        }
        return eventIds.get(searchName);
    }

    public static int askForEventIdByNumber(Hashtable <Integer, Event>venueEvents){
        Scanner scnr = new Scanner(System.in);
        System.out.println("What is the ID of the event?");
        int searchId = (int)scnr.nextDouble();
        while(searchId <1 || searchId > venueEvents.size()){
            System.out.print("Please choose an event from the availbale events.");
            System.out.println(" There are only event ID's from 1 to " + venueEvents.size() + ".");
            System.out.println("What is the ID of the event?");
            searchId = (int)scnr.nextDouble();
        }
        return searchId;
    }

    public static String askAdminChoice(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("A. Inquire event by ID");
        System.out.println();
        System.out.println("B. Inquire event by name");
        System.out.println();
        System.out.println("C. Add a new event");
        System.out.println();
        String userResponse = scnr.nextLine();
        while(!(userResponse.equalsIgnoreCase("a") || userResponse.equalsIgnoreCase("b") || userResponse.equalsIgnoreCase("c"))){
            System.out.println("There was an error with your response.");
            System.out.println("Please enter only A, B or C as a response: ");
            userResponse = scnr.nextLine();
        }
        return userResponse;
    }
    
    public static Boolean askAdminContinue(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Do you want to inquire about another event or create another event?");
        System.out.println("Please enter y/n:");
        String userResponse = scnr.nextLine();
        while(!(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes") || userResponse.equalsIgnoreCase("n") || userResponse.equalsIgnoreCase("no"))){
            System.out.println("There was an error with your response.");
            System.out.println("Please enter only Y or N as a response: ");
            userResponse = scnr.nextLine();
        }
        if(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("yes")){
            return true;
        }
        return false;
    }

    public static Boolean askToMakeAnotherTransaction(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Would you like to make another transaction?");
        System.out.println("Please enter y or n:");
        String userInput = scnr.nextLine();
        if(userInput.equalsIgnoreCase("Y") || userInput.equalsIgnoreCase("Yes"))
            return true;
        return false;
    }

    public static int askUserForTicketAmount(Customer myCustomer, Event myEventVenue, String typeOfTicketForTransaction){
        Scanner scnr = new Scanner(System.in);
        LogSingleton log = LogSingleton.getInstance();
        System.out.println("How many tickets would you like to buy?");
        int ticketAmount = (int)scnr.nextDouble();
        
        // calculate if enough tickets available and if money can buy as many tickets as desired for transaction
        while(ticketAmount > myEventVenue.getTicketsAvailable().get(typeOfTicketForTransaction) || ticketAmount <0 || ticketAmount >6 || (ticketAmount * myEventVenue.getTicketPrices().get(typeOfTicketForTransaction) * 1.0825 > myCustomer.getMoneyAvailable())){
            System.out.println("There aren't enough tickets or you don't have enough money for your purchase.");
            System.out.println("There are at most " + myEventVenue.getTicketsAvailable().get(typeOfTicketForTransaction) + " tickets.");
            System.out.println("You currently have $" +  String.format("%.2f", myCustomer.getMoneyAvailable()));
            if(ticketAmount <0){
                System.out.println("You need to buy at least 1 ticket or 0 for this transaction.");
            }
            if(ticketAmount >6){
                System.out.println("You can only buy up to 6 tickets per transaction.");
            }
            if(myEventVenue.getTicketPrices().get(typeOfTicketForTransaction) > myCustomer.getMoneyAvailable()){
                System.out.println("You are broke and can't afford even 1 " + typeOfTicketForTransaction + " ticket...");
                ticketAmount = 0;
                break;
            }
            log.setLogMessage(log.getLogMessage().concat("Asked user for amount of tickets.\n"));
            System.out.println("How many tickets would you like to buy?");
            ticketAmount = (int)scnr.nextDouble();
        }
        return ticketAmount;
    }

    public static Boolean askIfLogInAgain(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Does another user wish to log in again?");
        System.out.println("Please enter y or n:");
        String userInput = scnr.nextLine();
        if(userInput.equalsIgnoreCase("Y") || userInput.equalsIgnoreCase("Yes"))
            return true;
        return false;
    }

    public static Boolean askToExit(){
        Scanner scnr = new Scanner(System.in);
        System.out.print("You now have the option to leave. ");
        System.out.println("Do you wish to exit?");
        System.out.println("Enter EXIT or YES to leave or enter anyhting else to continue: ");
        String userInput = scnr.nextLine();
        if(userInput.equalsIgnoreCase("EXIT") || userInput.equalsIgnoreCase("Yes") || userInput.equalsIgnoreCase("Y"))
            return true;
        return false;
    }

    public static Boolean isAdminUser(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Are you an administrator?");
        System.out.println("Y if yes or N if there will be multiple users logging in.");
        System.out.println("Please enter y/n");
        String userResponse = scnr.nextLine();
        while(true){
            if(userResponse.equalsIgnoreCase("n") || userResponse.equalsIgnoreCase("No")){
                return false;
            }else if(userResponse.equalsIgnoreCase("y") || userResponse.equalsIgnoreCase("Yes")){
                return true;
            }else{
                System.out.println("There was an error with your response.");
                System.out.println("Are you a single customer?");
                System.out.println("Please enter only Y or N as a response");
                userResponse = scnr.nextLine();
            }
        }
    }

    public static Boolean isSingleCustomer(){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Are you a single customer?");
        System.out.println("Y if yes or N if there will be multiple users logging in.");
        System.out.println("Please enter y/n");
        String individualCustomerResponse = scnr.nextLine();
        while(true){
            if(individualCustomerResponse.equalsIgnoreCase("n") || individualCustomerResponse.equalsIgnoreCase("No")){
                return false;
            }else if(individualCustomerResponse.equalsIgnoreCase("y") || individualCustomerResponse.equalsIgnoreCase("Yes")){
                return true;
            }else{
                System.out.println("There was an error with your response.");
                System.out.println("Are you a single customer?");
                System.out.println("Please enter only Y or N as a response");
                individualCustomerResponse = scnr.nextLine();
            }
        }
    }

    public static int chooseEvent(Hashtable <Integer, Event>venueEvents){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Here is a list of the available events.");
        displayEvents(venueEvents);
        System.out.println("Enter the number of your event:");
        int searchId = (int)scnr.nextDouble();
        while(searchId <1 || searchId > venueEvents.size()){
            System.out.println("Here is a list of the available events.");
            displayEvents(venueEvents);
            System.out.print("Please choose an event from the availbale events.");
            System.out.println(" From 1 to " + venueEvents.size() + ".");
            System.out.println("Enter the number of your event:");
            searchId = (int)scnr.nextDouble();
        }
        return searchId;
    }

    public static void showCustomersInfo(ArrayList<Customer> myCustomers){
        // show tickets purchased from customer to customer user
        for(int i =0; i < myCustomers.size(); i++){
            System.out.println("");
            System.out.println( "");
            System.out.println( "Hello "+ myCustomers.get(i).getFirstName() + "!");
            System.out.println( "Here is your info so far..");
            System.out.println( "Amount of concerts purchased\t-\t\t"+ myCustomers.get(i).getConcertsPurchased());
            System.out.println( "Amount of money to your name\t-\t\t$"+ String.format("%.2f", myCustomers.get(i).getMoneyAvailable()));
            System.out.println( "__________________________________________");
            for(int j =0; j < myCustomers.get(i).getMyTickets().size(); j++){
                System.out.println( "Your event\t-\t\t"+ myCustomers.get(i).getMyTickets().get(j).getEventInfo().getEventName());
                System.out.println( "Your amount of tickets\t-\t\t"+ myCustomers.get(i).getMyTickets().get(j).getNumberOfTickets());
                System.out.println( "The total price of purchse\t-\t\t"+ String.format("%.2f", myCustomers.get(i).getMyTickets().get(j).getTotalPriceOfPurchase()));
                System.out.println( "The ticket confirmation number\t-\t\t" + myCustomers.get(i).getMyTickets().get(j).getConfirmationNum());
            }
        }
    }

    public static void displayEvents(Hashtable <Integer, Event>venueEvents){
        java.util.Enumeration<Integer> e = venueEvents.keys();
        while (e.hasMoreElements()) {
            int key = e.nextElement();
            System.out.println( key+ ".\t\t"+ venueEvents.get(key).getEventName());
        }
    }

    public static void showEventInfo(Event myEvent, int eventId){
        if(eventId ==0){
            System.out.println("The event chosen is " + myEvent.getEventName());
            System.out.print("The event date is " + myEvent.getEventDate());
            System.out.println(" and time is " + myEvent.getEventTime());
        }else{
            System.out.println();
            System.out.println("========================================");
            System.out.println("Event ID: " + eventId);
            System.out.println(myEvent.getEventName());
            System.out.println(myEvent.getEventDate());
            System.out.println(myEvent.getEventTime());
            
            System.out.println("Event Type: " + myEvent.getEventType());
            System.out.println("Event Capacity: " + myEvent.getEventVenue().getVenueCapacity());

            System.out.println("Total Seats Available: " + totalSeatsAvailable(myEvent));
            System.out.println("Total VIP Seats Available: " + myEvent.getTicketsAvailable().get("vip"));
            System.out.println("Total Gold Seats Available: " + myEvent.getTicketsAvailable().get("gold"));
            System.out.println("Total Silver Seats Available: " + myEvent.getTicketsAvailable().get("silver"));
            System.out.println("Total Bronze Seats Available: " + myEvent.getTicketsAvailable().get("bronze"));
            System.out.println("Total General Adm Seats Available: " + myEvent.getTicketsAvailable().get("generalAdmission"));

            System.out.println("Total VIP Seats Sold: " + totalSeatsSold(myEvent, "vip"));
            System.out.println("Total Gold Seats Sold: " + totalSeatsSold(myEvent, "gold"));
            System.out.println("Total Silver Seats Sold: " + totalSeatsSold(myEvent, "silver"));
            System.out.println("Total Bronze Seats Sold: " + totalSeatsSold(myEvent, "bronze"));
            System.out.println("Total General Adm Seats Sold: " + totalSeatsSold(myEvent, "generalAdmission"));
            System.out.println("Total Seats sold: " + myEvent.getNumTicketsSold());

            System.out.println("Total revenue for VIP tickets: $" + String.format("%.2f", myEvent.getTicketPrices().get("vip") * totalSeatsSold(myEvent, "vip")));
            System.out.println("Total revenue for Gold tickets: $" + String.format("%.2f", myEvent.getTicketPrices().get("gold") * totalSeatsSold(myEvent, "gold")));
            System.out.println("Total revenue for Silver tickets: $" + String.format("%.2f", myEvent.getTicketPrices().get("silver") * totalSeatsSold(myEvent, "silver")));
            System.out.println("Total revenue for Bronze tickets: $" + String.format("%.2f", myEvent.getTicketPrices().get("bronze") * totalSeatsSold(myEvent, "bronze")));
            System.out.println("Total revenue for General Admission tickets: $" + String.format("%.2f", myEvent.getTicketPrices().get("generalAdmission") * totalSeatsSold(myEvent, "generalAdmission")));
            double ttlRevenue = totalRevenue(myEvent);
            System.out.println("Total revenue for all tickets: $" + String.format("%.2f", ttlRevenue));
            System.out.println("Expected profit (Sell Out): $" + String.format("%.2f", expectedProfit(myEvent)));
            System.out.println("Actual profit: $" + String.format("%.2f", (ttlRevenue - myEvent.getEventVenue().getVenueCost())));
            System.out.println("========================================");
            System.out.println();
        }
    }

    public static void greetUser(){
        System.out.println("Welcome!");
        System.out.println("This is a Ticket Master System for you to see and get into events.");
        System.out.println();
    }

    public static void disregardUser(){
        System.out.println();
        System.out.println("Thank you for using our Ticket Master System");
        System.out.println("Goodbye!");
    }

    public static void getTicketPricesOptions(Event myEvent, Hashtable <Integer, String>ticketPricesOptions){
        int count = 1;
        // dic[1]: "generalAdmissionPrice"
        // dic[2]: "vipPrice"
        java.util.Enumeration<String> e = myEvent.getTicketPrices().keys();
        while ( e.hasMoreElements()) {
            String key = e.nextElement();
            ticketPricesOptions.put(count, key);
            count++;
        }
    }

    public static void showTicketPrices(Event myEvent, Hashtable <Integer, String>ticketPricesOptions){
        System.out.println("\nHere are your options \t(type of ticket: price)");
        java.util.Enumeration<Integer> e = ticketPricesOptions.keys();
        while (e.hasMoreElements()) {
            int key = e.nextElement();
            System.out.println(key + ". " + ticketPricesOptions.get(key) + ": \t\t\t\t\t\t\t"+ myEvent.getTicketPrices().get(ticketPricesOptions.get(key)));
        }
    }

    public static void processTransaction(Hashtable <Integer, Event>venueEvents, Customer myCustomer, int ticketAmount, int ticketsTransactions){
        LogSingleton log = LogSingleton.getInstance();
        log.setLogMessage(log.getLogMessage().concat("Asked user to choose event.\n"));
        int eventChosenID = chooseEvent(venueEvents);
        Event myEventVenue = venueEvents.get(eventChosenID);
        log.setLogMessage(log.getLogMessage().concat("User has chosen "+ myEventVenue.getEventName() +" as their event.\n"));
        showEventInfo(myEventVenue, 0);
        System.out.println("You currently have $" + myCustomer.getMoneyAvailable());
        Hashtable <Integer, String>ticketPricesOptions = new Hashtable<Integer, String>();
        getTicketPricesOptions(myEventVenue, ticketPricesOptions);
        showTicketPrices(myEventVenue, ticketPricesOptions);
        log.setLogMessage(log.getLogMessage().concat("Asked user to choose the type of ticket they wish to buy.\n"));
        String typeOfTicketForTransaction = askForTicketPrices(myEventVenue, ticketPricesOptions);
        log.setLogMessage(log.getLogMessage().concat("User chose " + typeOfTicketForTransaction + " as the type of ticket they wish to buy.\n"));

        log.setLogMessage(log.getLogMessage().concat("Asked user for amount of tickets.\n"));
        ticketAmount = askUserForTicketAmount(myCustomer, myEventVenue, typeOfTicketForTransaction);
        log.setLogMessage(log.getLogMessage().concat("User is buying " + ticketAmount + " tickets.\n"));
        
        // assume can make purchase at this point
        // get price of purchase, savings, and taxes
        Double totalPriceOfPurchase = ticketAmount * myEventVenue.getTicketPrices().get(typeOfTicketForTransaction);
        Double totalSavingsOfPurchase = 0.0;
        if(myCustomer.getTicketMinerMembership()){
            totalSavingsOfPurchase = totalPriceOfPurchase * 0.1;
            totalPriceOfPurchase = totalPriceOfPurchase * 0.9;
        }
        Double totalTaxesOfPurchase = totalPriceOfPurchase * 0.0825;
        totalPriceOfPurchase = totalPriceOfPurchase * 1.0825;
        System.out.println("You are being taxed $" + String.format("%.2f", totalTaxesOfPurchase));

        // update tickets available (Event: ticketsAvailable)
        myEventVenue.getTicketsAvailable().put(typeOfTicketForTransaction, myEventVenue.getTicketsAvailable().get(typeOfTicketForTransaction) - ticketAmount);
        log.setLogMessage(log.getLogMessage().concat("Updated amount of tickets available.\n"));
        // update tickets purchased (Event: ticketsPurchased)
        if(!myEventVenue.getTicketsPurchased().contains(typeOfTicketForTransaction)){
            ArrayList<Ticket> newTicketList = new ArrayList<>();
            newTicketList.add(new Ticket(myEventVenue, ticketAmount, totalPriceOfPurchase, ticketsTransactions, typeOfTicketForTransaction));
            myEventVenue.getTicketsPurchased().put(typeOfTicketForTransaction, newTicketList);
        }else{
            myEventVenue.getTicketsPurchased().get(typeOfTicketForTransaction).add(new Ticket(myEventVenue, ticketAmount, totalPriceOfPurchase, ticketsTransactions, typeOfTicketForTransaction));
        }
        log.setLogMessage(log.getLogMessage().concat("Updated tickets purchased in event.\n"));
        // update tickets sold  (Event: ticketsSoldNum)
        myEventVenue.setNumTicketsSold(ticketAmount + myEventVenue.getNumTicketsSold());
        log.setLogMessage(log.getLogMessage().concat("Updated tickets sold in event.\n"));
        // update event taxes (Event: totalTaxed)
        myEventVenue.setTotalTaxed(myEventVenue.getTotalTaxed() + totalTaxesOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated event taxes.\n"));
        // update event discounted (Event: totalDiscounted)
        myEventVenue.setTotalDiscounted(myEventVenue.getTotalDiscounted() + totalSavingsOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated event total discounted.\n"));
        // update customer money (Customer: moneyAvailable)
        myCustomer.setMoneyAvailable(myCustomer.getMoneyAvailable() - totalPriceOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated customer money available.\n"));
        // update customer concerts purchased (Customer: concertsPurchased +1)
        myCustomer.setConcertsPurchased(myCustomer.getConcertsPurchased() + 1);
        log.setLogMessage(log.getLogMessage().concat("Updated customer concerts purchased.\n"));
        // add transaction to customer tickets (Customer: myTickets)
        myCustomer.getMyTickets().add(new Ticket(myEventVenue, ticketAmount, totalPriceOfPurchase, ticketsTransactions, typeOfTicketForTransaction));
        log.setLogMessage(log.getLogMessage().concat("Updated customer tickets.\n"));
        // add cutomer events (Customer: watchingEvents)
        myCustomer.getWatchingEvents().add(myEventVenue);
        log.setLogMessage(log.getLogMessage().concat("Updated customer events.\n"));
        // update customer savings (Customer: totalSaved)
        myCustomer.setTotalSaved(myCustomer.getTotalSaved() + totalSavingsOfPurchase);
        log.setLogMessage(log.getLogMessage().concat("Updated customer savings.\n"));
    }

    public static String askForTicketPrices(Event myEvent, Hashtable <Integer, String>ticketPricesOptions){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please choose from the available options (from 1 to " + ticketPricesOptions.size()+ "):");
        int ticketChoice = (int)scnr.nextDouble();
        while (ticketChoice <1 || ticketChoice > ticketPricesOptions.size()) {
            System.out.println("You can only choose options from 1 to " + ticketPricesOptions.size());
            System.out.println("Please choose an option:");
            ticketChoice = (int)scnr.nextDouble();   
        }
        System.out.println("You have chosen "+ ticketPricesOptions.get(ticketChoice) + " tickets.");
        return ticketPricesOptions.get(ticketChoice);
    }
    
    private static int totalSeatsAvailable(Event myEvent) {
        return myEvent.getTicketsAvailable().get("generalAdmission") + myEvent.getTicketsAvailable().get("vip") + myEvent.getTicketsAvailable().get("gold") + myEvent.getTicketsAvailable().get("silver") + myEvent.getTicketsAvailable().get("bronze");
    }

    private static Double expectedProfit(Event myEvent) {
        double expectedProfit = 0.0;
        // sum of all of the tickets already putchased 
        java.util.Enumeration<String> e = myEvent.getTicketsPurchased().keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            // adding all of the type of tickets and all of the tickets purchased per type
            for(int i=0; i < myEvent.getTicketsPurchased().get(key).size(); i++){
                expectedProfit += myEvent.getTicketsPurchased().get(key).get(i).getNumberOfTickets() * myEvent.getTicketPrices().get(key);
            }
        }
        // sum of all tickets still avaialble
        java.util.Enumeration<String> h = myEvent.getTicketsAvailable().keys();
        while (h.hasMoreElements()) {
            String key = h.nextElement();
            // adding all of the type of tickets and all of the tickets purchased per type
            if(myEvent.getTicketsAvailable().get(key) == null){
                // there are no tickets available for given key (which is 0, so don't add anything)
                continue;
            }else if(myEvent.getTicketPrices().get(key) == null){
                // type of ticket doesn't exist for given key (which is reservedExtra && unavailable, so don't add anything)
                continue;
            }else{
                double testNum = myEvent.getTicketsAvailable().get(key);
                double testNum2 = myEvent.getTicketPrices().get(key);
                expectedProfit += testNum * testNum2;
                //expectedProfit += myEvent.getTicketsAvailable().get(key) * myEvent.getTicketPrices().get(key);
            }
        }
        return expectedProfit;
    }

    private static double totalRevenue(Event myEvent) {
        double totalRev = 0.0;
        java.util.Enumeration<String> e = myEvent.getTicketsPurchased().keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            // adding all of the type of tickets and all of the tickets purchased per type - taxes are excluded
            for(int i=0; i < myEvent.getTicketsPurchased().get(key).size(); i++){
                totalRev += myEvent.getTicketsPurchased().get(key).get(i).getNumberOfTickets() * myEvent.getTicketPrices().get(key);
            }
        }
        return totalRev;
    }

    public static int totalSeatsSold(Event myEvent, String typeOfTicket) {
        int ticketCount = 0;
        if(myEvent.getTicketsPurchased().get(typeOfTicket) == null){
            return 0;
        }
        for(int i=0; i < myEvent.getTicketsPurchased().get(typeOfTicket).size(); i++){
            ticketCount += myEvent.getTicketsPurchased().get(typeOfTicket).get(i).getNumberOfTickets();
        }
        return ticketCount;
    }
    
    public static Boolean userLogIn(Customer myCustomer){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please enter your username:");
        String username = scnr.nextLine();
        System.out.println("Please enter your password:");
        String password = scnr.nextLine();
        if (username.equalsIgnoreCase(myCustomer.getUsername()) && password.equals(myCustomer.getPassword()) )
            return true;
        return false;
    }

    public static Boolean makeUserLogIn(Customer myCustomer){
        LogSingleton log = LogSingleton.getInstance();
        log.setLogMessage(log.getLogMessage().concat("Asked user for username and password information.\n"));
        Boolean userHasLoggedIn = userLogIn(myCustomer);
        // check user has logged in
        while(!userHasLoggedIn){
            System.out.println("Incorrect username or password.");
            System.out.println("Make sure you are spelling your username and password correctly.");
            System.out.println("Password is case sensitive.");
            log.setLogMessage(log.getLogMessage().concat("Asked user if they wish to exit.\n"));
            Boolean exit = askToExit();
            if(exit){
                log.setLogMessage(log.getLogMessage().concat("User has chosen to exit the program.\n"));
                return false;
            }else{
                log.setLogMessage(log.getLogMessage().concat("User has chosen to proceed.\n"));
            }
            log.setLogMessage(log.getLogMessage().concat("Asked user for username and password information.\n"));
            userHasLoggedIn = userLogIn(myCustomer);
        }
        return true;
    }

    public static int getUserInfo(Hashtable <Integer, Customer> customers){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please enter your first name:");
        String userFirstName = scnr.nextLine();
        System.out.println("Please enter your last name:");
        String userLastName = scnr.nextLine();
        // traverse through hashtable
        java.util.Enumeration<Integer> eCustomers = customers.keys();
        while (eCustomers.hasMoreElements()) {
            int customerID = eCustomers.nextElement();
            if(userFirstName.equalsIgnoreCase(customers.get(customerID).getFirstName()) && userLastName.equalsIgnoreCase(customers.get(customerID).getLastName())){
                return customerID;
            }
        }
        // if not found return 0, assume ID 0 doesn't exist
        return 0;
    }

    public static int getUserAccountId(Hashtable <Integer, Customer> customers){
        LogSingleton log = LogSingleton.getInstance();
        log.setLogMessage(log.getLogMessage().concat("Asked user for first name and last name information.\n"));
        int userAccountId = getUserInfo(customers);
        // check user exists
        while(userAccountId == 0){
            System.out.println("Your first and last name don't appear to be in our files.");
            System.out.println("Make sure you are spelling your name correctly.");
            log.setLogMessage(log.getLogMessage().concat("Asked user if they wish to exit.\n"));
            Boolean exit = askToExit();
            if(exit){
                log.setLogMessage(log.getLogMessage().concat("User has chosen to exit the program.\n"));
                return -1;
            }else{
                log.setLogMessage(log.getLogMessage().concat("User has chosen to proceed.\n"));
            }
            log.setLogMessage(log.getLogMessage().concat("Asked user for first name and last name information.\n"));
            userAccountId = getUserInfo(customers);
        }
        return userAccountId;
    }

    public static void createCustomerObjects(ArrayList<String[]> myCustomers, Hashtable <Integer, Customer> customers, Hashtable <String, Integer> customerTitleId, Hashtable<String, Customer> customerUsernames){
        for(int i=0; i < myCustomers.size(); i++){
            Customer currentCustomer = new Customer(myCustomers.get(i), customerTitleId);
            customers.put(Integer.parseInt(myCustomers.get(i)[customerTitleId.get("ID")]), currentCustomer);
            customerUsernames.put(myCustomers.get(i)[customerTitleId.get("Username")], currentCustomer);
        }
    }

    // EDIT - get rid of this file by moving it to EventFactory.java
    public static void createEventObjects(ArrayList<String[]>myEvents, Hashtable <Integer, Event> venueEvents, Hashtable <String, Integer>eventIds, Hashtable <String, Integer> eventTitleId){
        EventFactory myEventFactory = new EventFactory();
        for (int i =myEvents.size()-1; i >=0; i--){
            Event newEvent = myEventFactory.createEvent(myEvents.get(i), eventTitleId);
            int currentEventId = Integer.parseInt(myEvents.get(i)[eventTitleId.get("Event ID")]);
            venueEvents.put(currentEventId, newEvent);
            eventIds.put(newEvent.getEventName(), currentEventId);
        }
    }
}
