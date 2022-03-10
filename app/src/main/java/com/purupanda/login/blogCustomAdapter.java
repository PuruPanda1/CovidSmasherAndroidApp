package com.purupanda.login;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purupanda.login.models.blogModel;
import com.purupanda.login.ui.blog.blogFragment;

import java.util.ArrayList;

public class blogCustomAdapter extends RecyclerView.Adapter<blogCustomAdapter.ViewHolder> {
    private final ArrayList<blogModel> blogArrayList;
    private final blogFragment context;

    public blogCustomAdapter(ArrayList<blogModel> blogArrayList, blogFragment context){
        this.blogArrayList = blogArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public blogCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull blogCustomAdapter.ViewHolder holder, int position) {
        blogModel blogs = blogArrayList.get(position);
        holder.title.setText(blogs.getTitle());
        if(blogs.getDescription().length()>50)
        {
            String desc = blogs.getDescription().substring(0,50)+"...";
            holder.description.setText(desc);
        }
        else
        {
            holder.description.setText(blogs.getDescription());
        }
        holder.hashTags.setText(blogs.getHashTags());
        holder.username.setText(blogs.getUsername());
        holder.likeCount.setText(blogs.getLikeCount());
        holder.commentCount.setText(blogs.getCommentCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context.getContext(),detailedBlogPage.class);
                in.putExtra("blogId",blogs.getBlogId());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView description;
        private final TextView likeCount;
        private final TextView username;
        private final TextView commentCount;
        private final TextView hashTags;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.blogTitleRV);
            description = itemView.findViewById(R.id.blogDescriptionRV);
            likeCount = itemView.findViewById(R.id.blogLikeCountRV);
            username = itemView.findViewById(R.id.blogUserNameRV);
            commentCount = itemView.findViewById(R.id.blogCommentCountRV);
            hashTags = itemView.findViewById(R.id.blogHashTags);
        }
    }
}
