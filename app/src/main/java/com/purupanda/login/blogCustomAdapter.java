package com.purupanda.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.description.setText(blogs.getDescription());
        holder.username.setText(blogs.getUsername());
        holder.likeCount.setText(blogs.getLikeCount());
        holder.commentCount.setText(blogs.getCommentCount());
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
                public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.blogTitleRV);
            description = itemView.findViewById(R.id.blogDescriptionRV);
            likeCount = itemView.findViewById(R.id.blogLikeCountRV);
            username = itemView.findViewById(R.id.blogUserNameRV);
            commentCount = itemView.findViewById(R.id.blogCommentCountRV);
        }
    }
}
