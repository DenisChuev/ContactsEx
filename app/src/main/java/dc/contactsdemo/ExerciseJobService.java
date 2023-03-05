package dc.contactsdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExerciseJobService  extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("TEST", "onStartJob: starting job with id: " + jobParameters.getJobId());
        ExerciseIntentService.startActionWriteExercise(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("TEST", "onStopJob: stopping job with id: " + jobParameters.getJobId());
        return true;
    }
}