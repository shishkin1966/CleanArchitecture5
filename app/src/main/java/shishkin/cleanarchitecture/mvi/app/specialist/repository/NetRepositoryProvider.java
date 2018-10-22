package shishkin.cleanarchitecture.mvi.app.specialist.repository;

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

    public static void cancelRequests(String listener) {
        SLUtil.getNetProvider().cancelRequests(listener);
        SLUtil.getNetCbProvider().cancelRequests(listener);
    }

}
