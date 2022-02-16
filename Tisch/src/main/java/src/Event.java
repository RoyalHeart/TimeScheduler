package src;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * This class is used to create a {@link Event} object.
 * <p>
 * <p>
 * The {@code Event} object contains the following:
 * <p>
 * - A unique ID
 * <p>
 * - A user ID of the user creates object
 * <p>
 * - A title
 * <p>
 * - A description
 * <p>
 * - A date
 * <p>
 * - A remind
 * <p>
 * - A location
 * <p>
 * - A duration
 * <p>
 * - A priority
 * <p>
 * - A list of participants
 * 
 * <p>
 * @author Sang Doan Tan 1370137
 */
public class Event {
    private String ID;
    private String userID;
    private String title;
    private String description;
    private Date date;
    private Date remind;
    private String location;
    private int duration;
    private int priority;
    private ArrayList<String> participants;

    /**
    * Constructor for the {@link Event} with 4 attributes
    * 
    * @param userID    The ID of the user created event
    * @param title     The title of the event
    * @param date      The date of the event
    * @param duration  The duration of the event
    */
    public Event(String userID, String title, Date date, int duration) {
        this.userID = userID;
        this.title = title;
        this.date = date;
        this.duration = duration;
    }

    /**
    * Constructor for the {@link Event} with participants
    * 
    * @param userID        The ID of the user created event
    * @param title         The title of the event
    * @param description   The description of the event    
    * @param participants  The list of participant of the event
    * @param date          The date of the event
    * @param remind        The remind of the event
    * @param location      The location of the event
    * @param duration      The duration of the event   
    * @param priority      The priority of the event
    */
    public Event(String userID, String title, String description, ArrayList<String> participants, Date date,
            Date remind, String location, int duration,
            int priority) {
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.participants = participants;
        this.date = date;
        this.remind = remind;
        this.location = location;
        this.duration = duration;
        this.priority = priority;
    }

    /** 
    * Constructor for the {@link Event} without participants
    * @param userID        The ID of the user created event
    * @param title         The title of the event
    * @param description   The description of the event 
    * @param date          The date of the event
    * @param remind        The remind of the event
    * @param location      The location of the event
    * @param duration      The duration of the event   
    * @param priority      The priority of the event
    */
    public Event(String userID, String title, String description, Date date, Date remind, String location, int duration,
            int priority) {
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.remind = remind;
        this.location = location;
        this.duration = duration;
        this.priority = priority;
    }

    /** 
    * Constructor for the {@link Event} to edit {@code Event} and without participants
    * 
    * @param id            The ID of the event need to be edit
    * @param userID        The ID of the user created event
    * @param title         The title of the event
    * @param description   The description of the event 
    * @param date          The date of the event
    * @param remind        The remind of the event
    * @param location      The location of the event
    * @param duration      The duration of the event   
    * @param priority      The priority of the event
    */
    public Event(String id, String userID, String title, String description, Date date, Date remind, String location,
            int duration, int priority) {
        this.ID = id;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.remind = remind;
        this.location = location;
        this.duration = duration;
        this.priority = priority;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripton) {
        this.description = descripton;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocaction(String location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getRemind() {
        return remind;
    }

    public void setRemind(Date remind) {
        this.remind = remind;
    }

    /*
     * public Date getStartTime() {return startTime;}
     * public void setStartTime(Date startTime) {this.startTime = startTime;}
     */

}
