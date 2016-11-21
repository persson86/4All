package dev.lfspersson.a4all.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

import dev.lfspersson.a4all.R;
import dev.lfspersson.a4all.models.ItemListModel;
import dev.lfspersson.a4all.models.ItemModel;
import dev.lfspersson.a4all.network.RestService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_item_detail)
public class ItemDetailActivity extends AppCompatActivity {
    private String itemId;
    private ItemModel item;
    private ProgressDialog progressDialog;

    @AfterViews
    void init(){
        itemId = (String) getIntent().getSerializableExtra("itemId");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startDialog();

        if (isConnectedToInternet() == true)
            callRestService();
        else
            Toast.makeText(this, "SEM CONEXÃO", Toast.LENGTH_SHORT).show();
    }

    @Background
    public void callRestService() {
        RestService service = RestService.retrofit.create(RestService.class);
        final Call<ItemModel> call = service.getItem(itemId);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Response<ItemModel> response, Retrofit retrofit) {
                item = response.body();
                Toast.makeText(getApplicationContext(), item.getTitulo(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(ItemDetailActivity.this);
        progressDialog.setMessage("Buscando");//getResources().getString(R.string.msg_getting_articles));
        progressDialog.setTitle("aguarde");//getResources().getString(R.string.msg_wait));
        progressDialog.show();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) ItemDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(ItemDetailActivity.this, "sem internet"/*R.string.msg_no_internet*/, Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
}
