import java.util.Hashtable;

public class EventFactory{
    public EventFactory(){
    }
    public Event createEvent(String []eventInfo, Hashtable <String, Integer> eventTitleId){
        // returning an Event
        if(eventInfo[eventTitleId.get("Event Type")].equalsIgnoreCase("Sport")){
            return new Sport(eventInfo, eventTitleId);
        }else if(eventInfo[eventTitleId.get("Event Type")].equalsIgnoreCase("Concert")){
            return new Concert(eventInfo, eventTitleId);
        }else if(eventInfo[eventTitleId.get("Event Type")].equalsIgnoreCase("Special")){
            return new Special(eventInfo, eventTitleId);
        }
        return null;
    }
}