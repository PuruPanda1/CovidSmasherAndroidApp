package com.purupanda.login.ui.vaccination;

import static java.lang.Math.round;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.purupanda.login.databinding.FragmentVaccinationBinding;
import com.purupanda.login.models.cityStateModel;
import com.purupanda.login.models.vaccinationCenters;
import com.purupanda.login.vaccinationCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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

    private final Calendar c = Calendar.getInstance();
    private String date=null;

    private String apiDistrictId="0";

//    variables needed for state spinner

        private ArrayList<cityStateModel> stateList;
        private ArrayList<String> getStateName;



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

//        variables for vaccination recycler view
        vaccineRV = binding.vaccinationCenterRecyclerView;
        centersArrayList = new ArrayList<>();
        mAdapter = new vaccinationCustomAdapter(centersArrayList,this);
        vaccineRV.setLayoutManager(new LinearLayoutManager(getContext()));
        vaccineRV.setAdapter(mAdapter);
//        function call to get state dropdown list
        getStates();

//        date icon button onclick method
        binding.dateLayout.setEndIconOnClickListener(new View.OnClickListener() {
            int y=c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            @Override
            public void onClick(View view) {
                DatePickerDialog g=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if((i1+1)<10)
                        {
                            date = i2+"-0"+(i1+1)+"-"+i;
                        }
                        else if(i2<10)
                        {
                            date = "0"+i2+"-"+(i1+1)+"-"+i;
                        }
                        else if((i1+1)<10 && i2<10)
                        {
                            date = "0"+i2+"-0"+(i1+1)+"-"+i;
                        }
                        else {
                            date = i2 + "-" + (i1 + 1) + "-" + i;
                        }
                        binding.vaccinationDate.setText(date);
                    }
                },y,m,d);
                g.show();
            }
        });


//         locate function on click action
        binding.locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    date=binding.vaccinationDate.getText().toString();
                    getStatus(apiDistrictId,date);
            }
        });
        return root;
    }


//    getstatus function to fetch data from the api and locate the vaccination centers

    private void getStatus(String districtId, String date){
        centersArrayList.clear();
        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id="+districtId+"&date="+date;
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
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

//    get states function is used to get the name of the states using api and show in the drop down list

    private void getStates(){
        String url = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getStateName = new ArrayList<String>();
                            stateList = new ArrayList<cityStateModel>();
                            JSONArray r = response.getJSONArray("states");
                            for (int i = 0; i < r.length(); i++) {
                                JSONObject obj = r.getJSONObject(i);
                                stateList.add(new cityStateModel(obj.getString("state_id"),obj.getString("state_name")));
                            }
                            for (int i = 0; i < stateList.size(); i++) {
                                getStateName.add(stateList.get(i).getName());
                            }
                            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,getStateName);
                            spinAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            binding.stateSelector.setAdapter(spinAdapter);
                            binding.stateSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String stateName = binding.stateSelector.getText().toString();
                                    String Id = findId(stateName, stateList);
                                    Log.d("getStates", "onItemClick: "+Id);
                                    getCity(Id);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("getStates", "onErrorResponse: error");
                    }
                });
        queue.add(jsonObjectRequest);
    }

//    get city funct6ion is used to get the name of the cities using api on click via states function/spinner and show in drop drown list
    private void getCity(String stateId){
        String url = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+stateId;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> getCityName = new ArrayList<String>();
                            ArrayList<cityStateModel> cityList = new ArrayList<cityStateModel>();
                            JSONArray r = response.getJSONArray("districts");
                            for (int i = 0; i < r.length(); i++) {
                                JSONObject obj = r.getJSONObject(i);
                                cityList.add(new cityStateModel(obj.getString("district_id"),obj.getString("district_name")));
                            }
                            for (int i = 0; i < cityList.size(); i++) {
                                getCityName.add(cityList.get(i).getName());
                            }
                            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,getCityName);
                            spinAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            binding.citySelector.setAdapter(spinAdapter);
                            binding.citySelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String cityName = binding.citySelector.getText().toString();
                                    apiDistrictId = findId(cityName, cityList);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("getStates", "onErrorResponse: error");
                    }
                });
        queue.add(jsonObjectRequest);
    }

//    function to find the id of the state and city via the auytofill text

    public String findId(String name, ArrayList<cityStateModel> a)
    {
        for (int i = 0; i < a.size(); i++) {
            if(name.equals(a.get(i).getName()))
            {
                return a.get(i).getId();
            }
        }
        return ("-1");
    }

}