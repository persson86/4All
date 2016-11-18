package dev.lfspersson.a4all;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * Created by LFSPersson on 17/11/16.
 */

public interface RestService {
    @GET("/tarefa/")
    Call<ItemListModel> getList();

    @GET("/tarefa/1")
    Call<ItemModel> getTasks();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
