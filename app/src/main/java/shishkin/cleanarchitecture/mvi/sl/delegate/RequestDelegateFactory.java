package shishkin.cleanarchitecture.mvi.sl.delegate;

import shishkin.cleanarchitecture.mvi.app.net.NetProviderImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.repository.DbProviderImpl;
import shishkin.cleanarchitecture.mvi.sl.task.CommonExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.DbExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.NetExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.RequestExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.RunnableExecutor;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class RequestDelegateFactory implements DelegatingFactory<RequestExecutor> {
    @Override
    public RequestExecutor create(String sender) {
        if (sender != null) {
            if (DbProviderImpl.class.getName().equals(sender)) {
                return DbExecutor.getInstance();
            } else if (NetProviderImpl.class.getName().equals(sender)) {
                return NetExecutor.getInstance();
            } else if (RequestSpecialistImpl.COMMON.equalsIgnoreCase(sender)) {
                return CommonExecutor.getInstance();
            } else if (RequestSpecialistImpl.NAME.equalsIgnoreCase(sender)) {
                return CommonExecutor.getInstance();
            } else if (RequestSpecialistImpl.DB.equalsIgnoreCase(sender)) {
                return DbExecutor.getInstance();
            } else if (RequestSpecialistImpl.NET.equalsIgnoreCase(sender)) {
                return NetExecutor.getInstance();
            }
        }
        return RunnableExecutor.getInstance();
    }
}
