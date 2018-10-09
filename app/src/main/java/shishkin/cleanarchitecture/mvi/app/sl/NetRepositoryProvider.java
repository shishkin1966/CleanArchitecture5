package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.request.GetTickerRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetValCursRequest;

public class NetRepositoryProvider {
    public static void getTicker(String listener) {
        SLUtil.getNetProvider().request(new GetTickerRequest(listener));
    }

    public static void getValCurs(String listener, String date) {
        SLUtil.getNetCbProvider().request(new GetValCursRequest(listener, date));
    }
}
