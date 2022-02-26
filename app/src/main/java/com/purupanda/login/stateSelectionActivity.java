package com.purupanda.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purupanda.login.databinding.ActivityMainBinding;
import com.purupanda.login.databinding.ActivityStateSelectionBinding;
import com.purupanda.login.models.cityStateModel;
import com.purupanda.login.models.newsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class stateSelectionActivity extends AppCompatActivity {

    private ActivityStateSelectionBinding binding;
    private RecyclerView stateRV;
    private ArrayList<cityStateModel> stateArrayList;
    private stateCutomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_selection);
        binding = ActivityStateSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        stateRV = binding.RVstates;
        stateArrayList = new ArrayList<>();
        mAdapter = new stateCutomAdapter(stateArrayList,this);
        stateRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stateRV.setAdapter(mAdapter);
        getStates();
        mAdapter.notifyDataSetChanged();
    }

    private void getStates(){
        stateArrayList.clear();
        String url = "https://cdn-api.co-vin.in/api/v2/admin/location/states";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        newsModel newsModel;
                        try {
                            JSONArray r = response.getJSONArray("states");
                            for (int i = 0; i < r.length(); i++) {
                                JSONObject obj = r.getJSONObject(i);
                                stateArrayList.add(new cityStateModel(obj.getString("state_id"),
                                        obj.getString("state_name")));
//                                Log.d("description", "onResponse: "+articlesArrayList.get(0).getDesctiption());
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsonObjectRequest);
    }
}