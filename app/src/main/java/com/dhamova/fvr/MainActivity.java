package com.dhamova.fvr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dhamova.fvr.Adapters.NewsAdapter;
import com.dhamova.fvr.DTO.News;
import com.dhamova.fvr.REST.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private List<News> news = new ArrayList<>();

    public final static int NEWS = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getNews();

        listView = (ListView)findViewById(R.id.listview_News);
            NewsAdapter itemsAdapter = new NewsAdapter(MainActivity.this, news);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, NewsActivity.class);
                i.putExtra("title", (news.get(position)).getTitle());
                i.putExtra("content", (news.get(position)).getText_content());
                i.putExtra("image_url", (news.get(position)).getImage_url());
                startActivityForResult(i,NEWS);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("","Llega resultado de otra actividad");
        if (requestCode==NEWS){
            if(resultCode==111) {

            }
        }
    }


    private void getNews() {

        news = new ArrayList<>();

        news.add(new News("Los Parces", "The team Los Parces won yesterday in the national park", "https://maxcdn.icons8.com/Share/icon/Sports//trophy1600.png"));


    }


    public void getNewsREST() {
        Call<List<News>> call = RestClient.getInstance().getApiService().getNews();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                news = response.body();
                NewsAdapter itemsAdapter = new NewsAdapter(MainActivity.this, news);
                listView.setAdapter(itemsAdapter);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}


