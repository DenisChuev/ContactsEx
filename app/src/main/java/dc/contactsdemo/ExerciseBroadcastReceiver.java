package dc.contactsdemo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExerciseBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_PERFORM_EXERCISE = "ACTION_PERFORM_EXERCISE";
    private static final int sJobId = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_PERFORM_EXERCISE:
                ExerciseIntentService.startActionWriteExercise(context);
//                scheduleJob(context);
                break;
            default:
                throw new IllegalArgumentException("Unknown action.");
        }
    }

    private void scheduleJob(Context context) {
        ComponentName jobService = new ComponentName(context, ExerciseJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(sJobId, jobService)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setPeriodic(15 * 60 * 1000);


        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo myJobInfo = exerciseJobBuilder.build();
        int resultCode = jobScheduler.schedule(myJobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("ExerciseBroadcastReceiver", "Job scheduled");
        } else {
            Log.d("ExerciseBroadcastReceiver", "Job scheduling failed");
        }
    }

}