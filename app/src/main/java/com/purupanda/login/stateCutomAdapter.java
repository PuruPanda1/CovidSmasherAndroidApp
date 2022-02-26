package com.purupanda.login;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purupanda.login.models.cityStateModel;

import java.util.ArrayList;

public class stateCutomAdapter extends RecyclerView.Adapter<stateCutomAdapter.ViewHolder> {
    private ArrayList<cityStateModel> arrayList;
    private stateSelectionActivity context;

    public stateCutomAdapter(ArrayList<cityStateModel> arrayList, stateSelectionActivity context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public stateCutomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_city_recyclerview,parent,false);
        return new stateCutomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stateCutomAdapter.ViewHolder holder, int position) {
        cityStateModel model = arrayList.get(position);
        holder.name.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(),news.class);
                in.putExtra("stateId",model.getId());
                Toast.makeText(context, "hey : "+model.getId(), Toast.LENGTH_SHORT).show();
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cityStateNameTextView);
        }
    }
}
