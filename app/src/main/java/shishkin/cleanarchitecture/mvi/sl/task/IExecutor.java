package shishkin.cleanarchitecture.mvi.sl.task;

import shishkin.cleanarchitecture.mvi.sl.delegate.SenderDelegating;

/**
 * Created by Shishkin on 13.03.2018.
 */

public interface IExecutor extends SenderDelegating {

    void cancelRequests(String listener);

    void cancelRequests(String listener, String taskName);

    void shutdown();

    void clear();
}
