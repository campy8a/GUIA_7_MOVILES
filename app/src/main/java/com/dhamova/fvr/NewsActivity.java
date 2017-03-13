package com.dhamova.fvr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhamova.fvr.DTO.News;
import com.dhamova.fvr.REST.ResponseMessage;
import com.dhamova.fvr.REST.RestClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private String newsTitle;
    private String newsContent;
    private String image_url;

    CoordinatorLayout coordinatorLayout;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getIntent() != null && getIntent().getExtras().getString("title") != null)
        {
            newsTitle = getIntent().getExtras().getString("title");
            newsContent = getIntent().getExtras().getString("content");
            image_url = getIntent().getExtras().getString("image_url");
        }

        getSupportActionBar().setTitle(newsTitle);


        TextView textTitle = (TextView) findViewById(R.id.textViewTitleDetailNews);
        textTitle.setText(newsTitle);

        TextView textContent = (TextView) findViewById(R.id.textViewContentDetailNews);
        textContent.setText(newsContent);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewDetailNews);

        Picasso.with(this)
                .load(image_url) .resize(50, 50) .centerCrop() .into(imageView);


        findViewById(R.id.select_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateRest();
                progressDialog.show();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.espera_metodo));
        Log.d("","Metodo on start");
    }


    private void simulateRest() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("","Ocultar diálogo");
                progressDialog.dismiss();
                setResult(111,new Intent().putExtra("method","Agregado a Favoritos"));
                finish();
            }
        }.execute();
    }


    public void anadirNews() {


        News news = new News("asas", "asas", "asas");
        Call<ResponseMessage> call = RestClient.getInstance().getApiService().createNews(news);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage res = response.body();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, res.getMsg(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            //TODO mostrar mensaje de confirmación

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
            }
        });
        }

}
