package dev.lfspersson.a4all.activities;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import dev.lfspersson.a4all.R;
import dev.lfspersson.a4all.adapters.ItemListAdapter;
import dev.lfspersson.a4all.models.ItemComentarioModel;
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
    private Context context;

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
    @ViewById
    LinearLayout llComentarios;
    @ViewById
    ScrollView svScreen;
    @ViewById
    TextView tvToolbarTitle;

    @AfterViews
    void init() {
        itemId = (String) getIntent().getSerializableExtra("itemId");
        context = getApplicationContext();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadToolbar();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startDialog();

        if (isConnectedToInternet() == true)
            callRestService();
        else
            Toast.makeText(this, "SEM CONEXÃO", Toast.LENGTH_SHORT).show();
    }

    private void loadToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

       /* setSupportActionBar(tb);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void loadFotoComentario(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //pbFoto.setVisibility(View.GONE);
                        //ivFoto.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }

    private void showProgressBarImages() {
        progressDialog.dismiss();
        pbFoto.setVisibility(View.VISIBLE);
        pbLogo.setVisibility(View.VISIBLE);
    }

    private void loadInfoScreen() {
        //getSupportActionBar().setTitle(item.getCidade() + item.getBairro());
        //getSupportActionBar().setTitle(R.layout.custom_actionbar);

        tvToolbarTitle.setText(item.getCidade() + " - " + item.getBairro());

        loadFotoImage(item.getUrlFoto(), ivFoto);
        loadLogoImage(item.getUrlLogo(), ivLogo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTitulo.setText(item.getTitulo());
        tvTexto.setText(item.getTexto());
        tvEndereco.setText(item.getEndereco());

        loadComentarios();
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

    private void initCamera(double lat, double lng) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(lat, lng))
                .zoom(15.7f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(item.getEndereco());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(markerOptions);
    }

    public void loadComentarios() {
        List<ItemComentarioModel> comentarioList = item.getComentariosList();

        for (ItemComentarioModel comentario : comentarioList) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View inflatedLayout = inflater.inflate(R.layout.comentario_row, null, false);

            TextView tvComentarioNome = (TextView) inflatedLayout.findViewById(R.id.tvComentarioNome);
            TextView tvComentarioTitulo = (TextView) inflatedLayout.findViewById(R.id.tvComentarioTitulo);
            TextView tvComentarioComentario = (TextView) inflatedLayout.findViewById(R.id.tvComentarioComentario);
            ImageView ivComentarioFoto = (ImageView) inflatedLayout.findViewById(R.id.ivComentarioFoto);
            ImageView ivComentarioNota = (ImageView) inflatedLayout.findViewById(R.id.ivComentarioNota);

            tvComentarioNome.setText(comentario.getNome());
            tvComentarioTitulo.setText(comentario.getTitulo());
            tvComentarioComentario.setText(comentario.getComentario());
            loadFotoComentario(comentario.getUrlFoto(), ivComentarioFoto);

            switch (comentario.getNota()) {
                case "1":
                    ivComentarioNota.setImageResource(R.drawable.img_star_nota1);
                    break;
                case "2":
                    ivComentarioNota.setImageResource(R.drawable.img_star_nota2);
                    break;
                case "3":
                    ivComentarioNota.setImageResource(R.drawable.img_star_nota3);
                    break;
                case "4":
                    ivComentarioNota.setImageResource(R.drawable.img_star_nota4);
                    break;
                case "5":
                    ivComentarioNota.setImageResource(R.drawable.img_star_nota5);
                    break;
            }

            llComentarios.addView(inflatedLayout);
        }

        progressDialog.dismiss();
    }

    @Click
    void ivServicos() {
        ServicosActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }

    @Click
    void ivLigar() {
        onCall();
    }

    @Click
    void ivEndereco() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ItemDetailActivity.this);
        builder.setTitle(getString(R.string.tit_endereco));
        builder.setMessage(item.getEndereco());
        builder.setPositiveButton("Ok!", null);
        builder.show();
    }

    @Click
    void ivComentarios() {
/*        svScreen.post(new Runnable() {
            public void run() {*/
        svScreen.fullScroll(View.FOCUS_DOWN);
        //}
        //      });
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getTelefone()));
            startActivity(it);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }
}
