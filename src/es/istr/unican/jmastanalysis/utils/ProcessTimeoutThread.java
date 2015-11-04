package es.istr.unican.jmastanalysis.utils;

/**
 * Created by JuanCTR on 04/11/2015.
 * From : http://yzaslavs.blogspot.com.es/2010/06/aaa.html
 * Credit Yair Z
 */

public class ProcessTimeoutThread implements Runnable
{
    private Process mProcess;
    private long mTimeoutValueInMiliseconds;
    private long mAfterExecInMiliseconds;

    public ProcessTimeoutThread(Process aProcess,
                                long aAfterExecInMiliseconds,
                                long aTimeoutValueInMiliseconds)
    {
        mProcess = aProcess;
        mTimeoutValueInMiliseconds = aTimeoutValueInMiliseconds;
        mAfterExecInMiliseconds = aAfterExecInMiliseconds;
    }

    @Override
    public void run()
    {
        try
        {
            //Sleeping for the period specified by the timeout
            //value, but also taking into consideration
            //the time that has passed since execution
            long timeSinceExecution = System.currentTimeMillis() -
                    mAfterExecInMiliseconds;

            Thread.sleep(mTimeoutValueInMiliseconds-timeSinceExecution);
            System.out.println("INTERRUPTED");

            mProcess.exitValue();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
