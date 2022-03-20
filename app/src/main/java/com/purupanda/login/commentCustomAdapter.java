package com.purupanda.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purupanda.login.models.blogModel;
import com.purupanda.login.models.commentModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class commentCustomAdapter extends RecyclerView.Adapter<commentCustomAdapter.ViewHolder> {
    private final ArrayList<commentModel> commentArrayList;
    private final detailedBlogPage context;

    public commentCustomAdapter(ArrayList<commentModel> commentArrayList, detailedBlogPage context) {
        this.commentArrayList=commentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        commentModel cm = commentArrayList.get(position);
        holder.usernameRV.setText(cm.getCommentUsername());
        holder.descriptionRV.setText(cm.getCommentDescription());
        holder.timeRV.setText(cm.getDateTime());
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

//    View holder for the commentRv adapter
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView usernameRV,descriptionRV,timeRV;
        public ViewHolder(View itemView){
            super(itemView);
            usernameRV = itemView.findViewById(R.id.commentUserNameRV);
            descriptionRV = itemView.findViewById(R.id.commentDescriptionRV);
            timeRV = itemView.findViewById(R.id.timeRV);
        }
}
}
