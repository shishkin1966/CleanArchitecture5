package shishkin.cleanarchitecture.mvi.app.specialist.job;

import com.firebase.jobdispatcher.Job;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface JobSpecialist extends Specialist {

    /**
     * Запланировать задание
     *
     * @param job задание
     */
    void schedule(Job job);

    /**
     * Отменить задание
     *
     * @param name имя задания
     */
    void cancel(String name);

    /**
     * Отменить все задания
     */
    void cancel();

    /**
     * Получить построитель заданий
     *
     * @return построитель заданий
     */
    Job.Builder getJobBuilder();
}