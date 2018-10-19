package shishkin.cleanarchitecture.mvi.app.net;


import shishkin.cleanarchitecture.mvi.sl.repository.AbsNetProvider;

public class NetProviderImpl extends AbsNetProvider<NetApi> {
    public static final String NAME = NetProviderImpl.class.getName();

    private static final String URL = "https://api.coinmarketcap.com/"; //Базовый адрес

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getApiClass() {
        return NetApi.class;
    }

    @Override
    public String getBaseUrl() {
        return URL;
    }

}
