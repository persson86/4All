package dev.lfspersson.a4all.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import dev.lfspersson.a4all.database.dao.ItemDAO;
import dev.lfspersson.a4all.database.models.ItemListModel;
import dev.lfspersson.a4all.adapters.ItemListAdapter;
import dev.lfspersson.a4all.R;
import dev.lfspersson.a4all.database.models.ItemModel;
import dev.lfspersson.a4all.network.RestService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private List<String> itemIdList;
    private ItemModel item;
    private List<ItemModel> itemModelList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Context context;
    public Intent intent;
    public boolean waitProcess;

    private ItemListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RestService service;

    @Bean
    ItemDAO itemDAO;

    @ViewById
    Toolbar toolbar;
    @ViewById
    RecyclerView rvList;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startDialog();

        if (isConnectedToInternet() == true)
            callRestService();
        else
            Toast.makeText(this, R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
    }

    @Background
    public void callRestService() {
        service = RestService.retrofit.create(RestService.class);
        final Call<ItemListModel> call = service.getList();
        call.enqueue(new Callback<ItemListModel>() {
            @Override
            public void onResponse(Response<ItemListModel> response, Retrofit retrofit) {
                itemIdList = new ArrayList<>();
                itemIdList = response.body().getLista();

                if (itemIdList.size() != itemDAO.getList().size())
                    getInfoItems();
                else {
                    itemModelList = itemDAO.getList();
                    loadCards();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Background
    public void getInfoItems() {
        for (String id : itemIdList) {
            while (waitProcess)
                continue;
            waitProcess = true;

            final Call<ItemModel> call = service.getItem(id);
            call.enqueue(new Callback<ItemModel>() {
                @Override
                public void onResponse(Response<ItemModel> response, Retrofit retrofit) {
                    item = response.body();

                    ItemModel model = new ItemModel();
                    model = item;
                    model.setId(item.getId());
                    model.setBairro(item.getBairro());
                    model.setCidade(item.getCidade());
                    model.setEndereco(item.getEndereco());
                    model.setLatitude(item.getLatitude());
                    model.setLongitude(item.getLongitude());
                    model.setTelefone(item.getTelefone());
                    model.setTexto(item.getTexto());
                    model.setTitulo(item.getTitulo());
                    model.setUrlFoto(item.getUrlFoto());
                    model.setUrlLogo(item.getUrlLogo());
                    model.setComentariosList(item.getComentariosList());
                    itemModelList.add(model);
                    waitProcess = false;
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }

        while (waitProcess)
            continue;

        itemDAO.SaveList(itemModelList);
        loadCards();
    }

    private void startDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.msg_aguarde));
        progressDialog.setTitle(getResources().getString(R.string.msg_buscando));
        progressDialog.show();
    }

    @UiThread
    public void loadCards() {
        mLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(mLayoutManager);

        mAdapter = new ItemListAdapter(itemModelList, context, this);
        rvList.setAdapter(mAdapter);
        progressDialog.dismiss();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(MainActivity.this, R.string.msg_no_internet, Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
}
