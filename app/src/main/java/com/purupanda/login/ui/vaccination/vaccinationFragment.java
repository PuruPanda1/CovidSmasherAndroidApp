package com.purupanda.login.ui.vaccination;

import static java.lang.Math.round;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purupanda.login.R;
import com.purupanda.login.databinding.FragmentDashboardBinding;
import com.purupanda.login.databinding.FragmentVaccinationBinding;
import com.purupanda.login.models.newsModel;
import com.purupanda.login.models.vaccinationCenters;
import com.purupanda.login.vaccinationCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vaccinationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vaccinationFragment extends Fragment {

    private FragmentVaccinationBinding binding;
    private RecyclerView vaccineRV;
    private ArrayList<vaccinationCenters> centersArrayList;
    private vaccinationCustomAdapter mAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public vaccinationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vaccinationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static vaccinationFragment newInstance(String param1, String param2) {
        vaccinationFragment fragment = new vaccinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_vaccination, container, false);


        binding = FragmentVaccinationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vaccineRV = binding.vaccinationCenterRecyclerView;
        centersArrayList = new ArrayList<>();
        mAdapter = new vaccinationCustomAdapter(centersArrayList,this);
        vaccineRV.setLayoutManager(new LinearLayoutManager(getContext()));
        vaccineRV.setAdapter(mAdapter);
//        getStatus("390019","31-03-2021");
//        mAdapter.notifyDataSetChanged();


        binding.locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        String userPincode = binding.vaccinationPinCode.getText().toString();
//        String date = binding.vaccinationDate.getText().toString();
        String date ="31-03-2021";
//        fetchResults(userPincode,date);


        getStatus(userPincode,date);
        mAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }


//    fetch results functions to fetch data from the api and show it in the vaccination page

    private void getStatus(String userPincode, String date){
        centersArrayList.clear();
        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+userPincode+"&date="+date;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        vaccinationCenters vaccinationCenters;
                        try {
                            JSONArray r = response.getJSONArray("sessions");
                            for (int i = 0; i < r.length(); i++) {
                                JSONObject obj = r.getJSONObject(i);
                                centersArrayList.add(new vaccinationCenters(obj.getString("name"),obj.getString("state_name"),
                                        obj.getString("district_name"), obj.getString("fee_type"),obj.getString("min_age_limit"),obj.getString("vaccine"),
                                        obj.getString("from"), obj.getString("to"),
                                        obj.getString("pincode"),obj.getString("center_id"),
                                        obj.getString("available_capacity")));
//                                Log.d("centerName", "onResponse: "+centersArrayList.get(0).getCenterName());
//                                Log.d("centerName", "onResponse: "+centersArrayList.get(0).getVaccineName());
//                                Log.d("centerName", "onResponse: "+centersArrayList.get(0).getAge());
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("centerName", "onResponse: error");
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volleyError", "onErrorResponse: Volley error");
                    }
                });
        queue.add(jsonObjectRequest);
    }



}