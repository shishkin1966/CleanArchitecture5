package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.sl.delegate.AbsSenderDelegate;
import shishkin.cleanarchitecture.mvi.sl.delegate.DelegatingFactory;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class ProviderDelegate extends AbsSenderDelegate<RepositoryProvider> {

    @Override
    public DelegatingFactory<RepositoryProvider> getDelegateFactory() {
        return new ProviderDelegateFactory();
    }
}
