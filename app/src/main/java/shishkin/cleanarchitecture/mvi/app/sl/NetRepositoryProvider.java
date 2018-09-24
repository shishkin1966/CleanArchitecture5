package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.request.GetTickerRequest;

public class NetRepositoryProvider {
    public static void getTicker(String listener) {
        SLUtil.getNetProvider().request(new GetTickerRequest(listener));
    }
}
