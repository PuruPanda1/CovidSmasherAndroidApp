package com.purupanda.login.ui.dashboard;

import static android.content.ContentValues.TAG;

import static java.lang.Math.round;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.purupanda.login.databinding.FragmentDashboardBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

public class DashboardFragment extends Fragment {

    //    volley variables


    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        binding.textView.setText("heya");
        fetchResults();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void fetchResults(){
        String url = "https://api.covid19api.com/summary";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray r = response.getJSONArray("Countries");
                            JSONObject obj = r.getJSONObject(76);
                            String totalConfirmed = obj.getString("TotalConfirmed");
                            String totalDeath = obj.getString("TotalDeaths");
                            String totalRecovered = obj.getString("TotalRecovered");
                            String newCases = obj.getString("NewConfirmed");
                            if(newCases == "0")
                            {
                                totalRecovered = "0";
                            }
                            else
                            {
                                int val = Integer.parseInt(newCases);
                                totalRecovered = String.valueOf(round(val*0.7));
                            }
                            binding.totalConfirmed.setText(totalConfirmed);
                            binding.totalDeath.setText(totalDeath);
                            binding.totalRecovered.setText(totalRecovered);
                            binding.newCases.setText(newCases);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.textView.setText("Error");

                    }
                });
        queue.add(jsonObjectRequest);
    }


}