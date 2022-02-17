package src;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This class is used as a job to refresh database connection.
 * 
 * @author Tam Thai Hoang
 */
public class RefreshConnection implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("RefreshConnection is executed");
        System.out.println("Time: " + new Date());
        try {
            Database.closeConnection();
            Database.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
