package shishkin.cleanarchitecture.mvi.sl.task;

import shishkin.cleanarchitecture.mvi.sl.request.Request;

public interface RequestExecutor extends IExecutor {
    void execute(final Request request);

    boolean isShutdown();

}
