package dev.lfspersson.a4all;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private List<String> itemList;
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    RecyclerView rvList;

    @AfterViews
    void init() {
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
        final Call<ItemListModel> call = service.getList();

        call.enqueue(new Callback<ItemListModel>() {
            @Override
            public void onResponse(Response<ItemListModel> response, Retrofit retrofit) {
                itemList = new ArrayList<String>();
                itemList = response.body().getLista();
                loadList();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Buscando");//getResources().getString(R.string.msg_getting_articles));
        progressDialog.setTitle("aguarde");//getResources().getString(R.string.msg_wait));
        progressDialog.show();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(MainActivity.this, "sem internet"/*R.string.msg_no_internet*/, Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private void loadList() {
        mLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(itemList);
        rvList.setAdapter(mAdapter);
        progressDialog.dismiss();
    }
}
