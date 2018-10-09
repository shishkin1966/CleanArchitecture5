package shishkin.cleanarchitecture.mvi.app.net;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;

public interface NetCbApi {
    @GET("scripts/XML_daily_eng.asp")
    Call<ValCurs> getData(@Query("date_req") String dateReg);
}
