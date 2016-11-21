package dev.lfspersson.a4all.network;

import dev.lfspersson.a4all.BuildConfig;
import dev.lfspersson.a4all.models.ItemListModel;
import dev.lfspersson.a4all.models.ItemModel;
import retrofit.Call;
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
