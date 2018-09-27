package shishkin.cleanarchitecture.mvi.app.job;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;

public class JobSpecialistService extends JobService {

    public static final String NAME = JobSpecialistService.class.getName();

    @Override
    public boolean onStartJob(JobParameters job) {
        if (SLUtil.getActivityUnion().hasSubscribers()) {
            SLUtil.getActivityUnion().showToast(new ShowMessageEvent("Тестовое сообщение"));
        }
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
