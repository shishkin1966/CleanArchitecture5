package shishkin.cleanarchitecture.mvi.sl.delegate;

import shishkin.cleanarchitecture.mvi.app.net.NetProviderImpl;
import shishkin.cleanarchitecture.mvi.sl.repository.DbProvider;
import shishkin.cleanarchitecture.mvi.sl.task.CommonExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.DbExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.IExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.NetExecutor;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class RequestDelegateFactory implements DelegatingFactory<IExecutor> {
    @Override
    public IExecutor create(Object sender) {
        if (DbProvider.class.isInstance(sender)) {
            return DbExecutor.getInstance();
        } else if (NetProviderImpl.class.isInstance(sender)) {
            return NetExecutor.getInstance();
        }
        return CommonExecutor.getInstance();
    }
}
