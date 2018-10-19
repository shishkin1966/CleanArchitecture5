package shishkin.cleanarchitecture.mvi.app.net;


import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;

public interface NetApi {
    @GET("v1/ticker/")
    Call<List<Ticker>> getTicker();

}
