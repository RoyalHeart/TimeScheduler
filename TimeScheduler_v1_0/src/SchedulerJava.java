package src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerJava {
    private static Scheduler scheduler;

    public static void createScheduler() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleMail(User user, Event event) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(MailJob.class).withIdentity("job1", "group1").build();
            jobDetail.getJobDataMap().put("user", user);
            jobDetail.getJobDataMap().put("event", event);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startAt(event.getRemind())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Database.createConnection();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateString = "20/01/2022 09:57";
        String dateRemindString = "20/01/2022 10:00";
        Date date = dateFormat.parse(dateString);
        Date remind = dateFormat.parse(dateRemindString);
        Event event = new Event("0", "Test Reminder", new Date(), 3);
        event.setRemind(remind);
        event.setDate(date);
        User user = Database.getUser("admin", Hash.hashPassword("adminadmin").toUpperCase());
        // event.getDate().setMinutes(45);
        // create the scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDetail jobDetail = JobBuilder.newJob(MailJob.class).build();
        jobDetail.getJobDataMap().put("user", user);
        jobDetail.getJobDataMap().put("event", event);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                // .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                .startAt(event.getRemind())
                .build();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // SchedulerJava.main(args);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }).start();

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // MainFrame.main(args);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }).start();
    }

}
