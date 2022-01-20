package src;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MailJob implements Job {

    private static User user;
    private static Event event;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Context: " + context);
        System.out.println("MailJob is executed");
        System.out.println("Time: " + new Date());
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        user = (User) dataMap.get("user");
        event = (Event) dataMap.get("event");
        try {
            Mail.sendRemindEmail(user, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
