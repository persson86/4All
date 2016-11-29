package dev.lfspersson.a4all.network;

import dev.lfspersson.a4all.BuildConfig;
import dev.lfspersson.a4all.database.models.ItemListModel;
import dev.lfspersson.a4all.database.models.ItemModel;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by LFSPersson on 17/11/16.
 */

public interface RestService {
    @GET("/tarefa/")
    Call<ItemListModel> getList();

    @GET("/tarefa/{itemId}")
    Call<ItemModel> getItem(@Path("itemId") String itemId);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
