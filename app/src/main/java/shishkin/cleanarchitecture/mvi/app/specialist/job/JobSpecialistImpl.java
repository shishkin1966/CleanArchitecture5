package shishkin.cleanarchitecture.mvi.app.specialist.job;

import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;

public class JobSpecialistImpl extends AbsSpecialist implements JobSpecialist {

    public static final String NAME = JobSpecialistImpl.class.getName();

    private FirebaseJobDispatcher mDispatcher;

    @Override
    public void onRegister() {
        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ApplicationSpecialistImpl.getInstance()));
        mDispatcher.newJobBuilder();
    }

    @Override
    public void schedule(Job job) {
        mDispatcher.mustSchedule(job);
    }

    @Override
    public void stop(String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            mDispatcher.cancel(name);
        }
    }

    @Override
    public void stop() {
        super.stop();

        mDispatcher.cancelAll();
    }

    @Override
    public Job.Builder getJobBuilder() {
        return mDispatcher.newJobBuilder();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (JobSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
