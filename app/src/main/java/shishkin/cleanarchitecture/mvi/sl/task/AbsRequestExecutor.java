package shishkin.cleanarchitecture.mvi.sl.task;

import androidx.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.sl.request.Request;

@SuppressWarnings("unused")
public abstract class AbsRequestExecutor implements RequestExecutor {

    protected abstract RequestThreadPoolExecutor getExecutor();

    @Override
    public void execute(final Request request) {
        getExecutor().addRequest(request);
    }

    @Override
    public void shutdown() {
        getExecutor().shutdown();
    }

    @Override
    public void clear() {
        getExecutor().clear();
    }

    @Override
    public void cancelRequests(String listener) {
        getExecutor().cancelRequests(listener);
    }

    @Override
    public void cancelRequests(String listener, String taskName) {
        getExecutor().cancelRequests(listener, taskName);
    }

    @Override
    public boolean isShutdown() {
        return getExecutor().isShutdown();
    }

    @Override
    public void processing(Object sender, Object object) {
        execute((Request) object);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        if (command instanceof Request) {
            execute((Request) command);
        }
    }
}
