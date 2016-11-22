package dev.lfspersson.a4all.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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
public class ItemDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String itemId;
    private ItemModel item;
    private ProgressDialog progressDialog;
    private GoogleMap mMap;

    @ViewById
    ImageView ivFoto;
    @ViewById
    ProgressBar pbFoto;
    @ViewById
    ImageView ivLogo;
    @ViewById
    ProgressBar pbLogo;
    @ViewById
    TextView tvTitulo;
    @ViewById
    TextView tvTexto;
    @ViewById
    TextView tvEndereco;

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
                showProgressBarImages();
                loadInfoScreen();
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

    public void loadFotoImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbFoto.setVisibility(View.GONE);
                        ivFoto.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public void loadLogoImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbLogo.setVisibility(View.GONE);
                        ivLogo.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }

    private void showProgressBarImages(){
        progressDialog.dismiss();
        pbFoto.setVisibility(View.VISIBLE);
        pbLogo.setVisibility(View.VISIBLE);
    }

    private void loadInfoScreen(){
        loadFotoImage(item.getUrlFoto(), ivFoto);
        loadLogoImage(item.getUrlLogo(), ivLogo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTitulo.setText(item.getTitulo());
        tvTexto.setText(item.getTexto());
        tvEndereco.setText(item.getEndereco());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (item.getLatitude() != null) {
            double lat = Double.parseDouble(item.getLatitude());
            double lng = Double.parseDouble(item.getLongitude());
            initCamera(lat, lng);
        }
    }

    private void initCamera( double lat, double lng ) {
        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( lat, lng ) )
                .zoom( 15.7f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        mMap.animateCamera( CameraUpdateFactory
                .newCameraPosition( position ), null );
        mMap.setMapType( GoogleMap.MAP_TYPE_TERRAIN );

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(item.getCidade());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(markerOptions);
    }
}
