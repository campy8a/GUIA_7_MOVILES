package com.dhamova.fvr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhamova.fvr.ContactoActivity;
import com.dhamova.fvr.DTO.News;
import com.dhamova.fvr.MainActivity;
import com.dhamova.fvr.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JuanCruz on 2/19/17.
 */

public class NewsAdapter extends BaseAdapter {

    private List<News> news;
    private Context context;
    private static LayoutInflater inflater=null;

    public NewsAdapter(MainActivity mainActivity, List<News> platosList) { this.news = platosList;
        this.context = mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) { return position;
    }
    @Override
    public long getItemId(int position) { return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        News currentNews = news.get(position);
        if (view == null)
            view = inflater.inflate(R.layout.news_item, null);
        TextView titleTextView = (TextView) view.findViewById(R.id.title_news);
        TextView contentTextView = (TextView) view.findViewById(R.id.content_news);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_news);
        ImageView shareIco = (ImageView) view.findViewById(R.id.share_item);



        shareIco.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context,ContactoActivity.class);
                i.putExtra("title", (news.get(position)).getTitle());
                i.putExtra("content", (news.get(position)).getText_content());
                i.putExtra("image_url", (news.get(position)).getImage_url());
                context.startActivity(i);

            }
        });
        titleTextView.setText(currentNews.getTitle());
        contentTextView.setText(currentNews.getText_content());

        Picasso.with(context)
                .load(currentNews.getImage_url()) .resize(500, 500) .centerCrop() .into(imageView);
        return view; }

}
