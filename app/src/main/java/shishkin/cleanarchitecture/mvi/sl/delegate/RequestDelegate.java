package shishkin.cleanarchitecture.mvi.sl.delegate;

import shishkin.cleanarchitecture.mvi.sl.task.IExecutor;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class RequestDelegate extends AbsSenderDelegate<IExecutor> {

    @Override
    public DelegatingFactory<IExecutor> getDelegateFactory() {
        return new RequestDelegateFactory();
    }
}
