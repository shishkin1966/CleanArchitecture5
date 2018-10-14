package shishkin.cleanarchitecture.mvi.app.specialist.job;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts.AccountsPresenter;
import shishkin.cleanarchitecture.mvi.sl.mail.ShowMessageMail;

public class JobSpecialistService extends JobService {

    public static final String NAME = JobSpecialistService.class.getName();

    @Override
    public boolean onStartJob(JobParameters job) {
        SLUtil.addMail(new ShowMessageMail(AccountsPresenter.NAME, getApplicationContext().getString(R.string.readme)));
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
