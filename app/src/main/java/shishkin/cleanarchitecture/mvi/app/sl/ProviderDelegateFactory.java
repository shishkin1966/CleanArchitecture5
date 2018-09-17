package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.sl.delegate.DelegatingFactory;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class ProviderDelegateFactory implements DelegatingFactory<RepositoryProvider> {
    @Override
    public RepositoryProvider create(Object sender) {
        return new DbRepositoryProvider();
    }
}
