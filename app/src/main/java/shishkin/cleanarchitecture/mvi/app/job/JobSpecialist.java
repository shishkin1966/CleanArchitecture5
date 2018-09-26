package shishkin.cleanarchitecture.mvi.app.job;

import com.firebase.jobdispatcher.Job;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface JobSpecialist extends Specialist {
    void schedule(Job job);

    void cancel(String name);

    void cancelAll();

    Job.Builder getJobBuilder();
}
