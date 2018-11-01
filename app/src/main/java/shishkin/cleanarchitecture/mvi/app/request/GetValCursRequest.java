package shishkin.cleanarchitecture.mvi.app.request;

import retrofit2.Call;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;
import shishkin.cleanarchitecture.mvi.sl.request.AbsNetResultMailRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class GetValCursRequest extends AbsNetResultMailRequest<ValCurs> {

    public GetValCursRequest(final String listener, String date) {
        super(listener);

        this.date = date;
    }

    private String date;

    @Override
    public Call<ValCurs> getData() {
        return SLUtil.getNetCbProvider().getApi().getData(date);
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public int getAction(Request oldRequest) {
        return RequestThreadPoolExecutor.ACTION_DELETE;
    }

}
