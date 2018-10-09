package shishkin.cleanarchitecture.mvi.app.net;


import retrofit2.Converter;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import shishkin.cleanarchitecture.mvi.sl.repository.AbsNetProvider;

public class NetCbProviderImpl extends AbsNetProvider<NetCbApi> {
    public static final String NAME = NetCbProviderImpl.class.getName();

    private static final String URL = "http://www.cbr.ru/"; //Базовый адрес

    @Override
    public Converter.Factory getConverterFactory() {
        return SimpleXmlConverterFactory.create();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getApiClass() {
        return NetCbApi.class;
    }

    @Override
    public String getBaseUrl() {
        return URL;
    }

}
