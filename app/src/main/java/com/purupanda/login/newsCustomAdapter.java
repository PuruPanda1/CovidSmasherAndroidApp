package com.purupanda.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purupanda.login.models.newsModel;
import com.purupanda.login.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class newsCustomAdapter extends RecyclerView.Adapter<newsCustomAdapter.ViewHolder> {
    private ArrayList<newsModel> articlesArrayList;
    private HomeFragment context;

    public newsCustomAdapter(ArrayList<newsModel> articlesArrayList, HomeFragment context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public newsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_resource_file,parent,false);
        return  new newsCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsCustomAdapter.ViewHolder holder, int position) {
        newsModel articles = articlesArrayList.get(position);
        holder.newsTitle.setText(articles.getNewsTitle());
        holder.newsSource.setText(articles.getNewsSource());
        Picasso.get().load(articles.getNewsImage()).into(holder.newsImage);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTitle,newsSource;
        private ImageView newsImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsImage = itemView.findViewById(R.id.newsImage);
        }
    }
}
