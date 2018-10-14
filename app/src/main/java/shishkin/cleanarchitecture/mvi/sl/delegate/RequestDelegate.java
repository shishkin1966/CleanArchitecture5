package shishkin.cleanarchitecture.mvi.sl.delegate;

import shishkin.cleanarchitecture.mvi.sl.task.RequestExecutor;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class RequestDelegate extends AbsSenderDelegate<RequestExecutor> {

    @Override
    public DelegatingFactory<RequestExecutor> getDelegateFactory() {
        return new RequestDelegateFactory();
    }
}
