package src;

import java.util.ArrayList;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This class is used to schedule the sending remind email jobs.
 * 
 * @author Tam Thai Hoang
 */
public class SchedulerJava {
    /**
     * {@link Scheduler} to schedule the sending remind email jobs.
     */
    private static Scheduler scheduler;

    /**
     * Create a scheduler to schedule the jobs.
     */
    public static void createScheduler() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to shutdown the scheduler.
     */
    public static void closeScheduler() {
        try {
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to schedule remind email using user and event.
     * 
     * @param user  to get {@link User} information to send remind email.
     * @param event has the {@link Event} detail for the email.
     */
    public static void scheduleMail(User user, Event event) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(MailJob.class).withIdentity(event.getID(), event.getUserID())
                    .build();
            jobDetail.getJobDataMap().put("user", user);
            jobDetail.getJobDataMap().put("event", event);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(event.getID(), event.getUserID())
                    .startAt(event.getRemind())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to unschedule remind email using event.
     * 
     * @param event has the {@link Event} detail for the email.
     */
    public static void unscheduleMail(Event event) {
        try {
            org.quartz.TriggerKey triggerKey = new org.quartz.TriggerKey(event.getID(), event.getUserID());
            scheduler.unscheduleJob(triggerKey);
            System.out.println(scheduler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to schedule all the emails of event that will happen in
     * the future.
     * 
     * @param user to get {@link User} information to send remind email.
     */
    public static void setFutureRemind(User user) {
        ArrayList<Event> remindEvents = LoadEvents.getRemindEvents(user);
        try {
            for (Event event : remindEvents) {
                scheduleMail(user, event);
                System.out.println("Mail scheduled for " + event.getTitle() + " at " + event.getRemind());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh connection to the database every 15 minutes.
     */
    public static void refreshConnection() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(RefreshConnection.class).withIdentity("RefreshConnection")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("RefreshConnection").startNow()
                    .withSchedule(org.quartz.CronScheduleBuilder.cronSchedule("0 0/15 * * * ?")).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
