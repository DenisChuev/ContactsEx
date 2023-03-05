package dc.contactsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        JobScheduler scheduler = getSystemService(JobScheduler.class);
//        if (scheduler.getPendingJob(12345) == null){
//            // Context of the app under test.
//            Context appContext = this;
//            //Init intent with receiver class
//            Intent demoIntentForBroadcast = new Intent(appContext, ExerciseBroadcastReceiver.class);
//            //Add action to proper handle this request by receiver
//            demoIntentForBroadcast.setAction(ExerciseBroadcastReceiver.ACTION_PERFORM_EXERCISE);
//            //Sending broadcast with intent
//            appContext.sendBroadcast(demoIntentForBroadcast);
//        }
//        else Log.d("TEST", "<KZM");

//
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent demoIntentForBroadcast = new Intent(this, ExerciseBroadcastReceiver.class);
//        demoIntentForBroadcast.setAction(ExerciseBroadcastReceiver.ACTION_PERFORM_EXERCISE);
//        PendingIntent pendingIntent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pendingIntent = PendingIntent.getBroadcast(
//                    MainActivity.this, 0, demoIntentForBroadcast, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//        } else {
//            pendingIntent = PendingIntent.getBroadcast(
//                    MainActivity.this, 0, demoIntentForBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        Calendar t = Calendar.getInstance();
//        t.setTimeInMillis(System.currentTimeMillis());
//
//        int interval = 60 * 1000;
//        am.setRepeating(AlarmManager.RTC_WAKEUP, t.getTimeInMillis(), interval, pendingIntent);


        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(PeriodicWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(work);


    }

    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(this, ExerciseJobService.class);
        JobInfo info = new JobInfo.Builder(12345, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(12345);
        Log.d(TAG, "Job cancelled");
    }

    public void addContacts(View view) {
    }
}