package shishkin.cleanarchitecture.mvi.app.job;

import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;

public class JobSpecialistService extends JobService {

    public static final String NAME = JobSpecialistService.class.getName();

    @Override
    public boolean onStartJob(JobParameters job) {
        ApplicationUtils.showToast("Старт задания", Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_INFO);
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
