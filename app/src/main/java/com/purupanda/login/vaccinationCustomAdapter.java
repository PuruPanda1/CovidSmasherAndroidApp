package com.purupanda.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.purupanda.login.models.vaccinationCenters;
import com.purupanda.login.ui.vaccination.vaccinationFragment;

import java.util.ArrayList;

public class vaccinationCustomAdapter extends RecyclerView.Adapter<vaccinationCustomAdapter.ViewHolder> {
    private ArrayList<vaccinationCenters> centersArrayList;
    private vaccinationFragment context;

    public vaccinationCustomAdapter(ArrayList<vaccinationCenters> centersArrayList, vaccinationFragment context){
        this.centersArrayList = centersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public vaccinationCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_recyclerview,parent,false);
        return  new vaccinationCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vaccinationCustomAdapter.ViewHolder holder, int position) {
        vaccinationCenters centers = centersArrayList.get(position);
        holder.centerName.setText(centers.getCenterName());
        holder.centerId.setText(centers.getCenterId());
        holder.age.setText("Age: "+centers.getAge());
        holder.cityState.setText(centers.getDistrictName()+", " + centers.getStateName());
        holder.pincode.setText(centers.getPincode());
        holder.fee.setText(centers.getFee());
        holder.vaccineName.setText(centers.getVaccineName());
        holder.units.setText("Units: "+centers.getCapacity());
        holder.timings.setText(centers.getFrom()+" to "+centers.getTo());
    }

    @Override
    public int getItemCount() {
        return centersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView centerName,cityState,centerId,pincode,fee,age,vaccineName,units,timings;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            centerName = itemView.findViewById(R.id.centerName);
            centerId = itemView.findViewById(R.id.centerID);
            cityState = itemView.findViewById(R.id.cityState);
            pincode = itemView.findViewById(R.id.pincode);
            fee = itemView.findViewById(R.id.fee);
            age = itemView.findViewById(R.id.ageLimit);
            vaccineName = itemView.findViewById(R.id.vaccineName);
            units = itemView.findViewById(R.id.units);
            timings = itemView.findViewById(R.id.timings);
        }
    }
}
