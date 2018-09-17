package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.sl.delegate.AbsDelegate;
import shishkin.cleanarchitecture.mvi.sl.delegate.DelegatingFactory;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class ProviderDelegate extends AbsDelegate<RepositoryProvider> {

    @Override
    public DelegatingFactory<RepositoryProvider> getDelegateFactory() {
        return new ProviderDelegateFactory();
    }
}
