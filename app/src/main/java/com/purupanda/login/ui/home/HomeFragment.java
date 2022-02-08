package com.purupanda.login.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purupanda.login.databinding.FragmentHomeBinding;
import com.purupanda.login.models.newsModel;
import com.purupanda.login.newsCustomAdapter;
//import com.purupanda.login.models.News;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private RecyclerView newsRV;
    private ArrayList<newsModel> articlesArrayList;
    private newsCustomAdapter newsRVAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        newsRV = binding.RVnews;
        articlesArrayList = new ArrayList<>();
        newsRVAdapter = new newsCustomAdapter(articlesArrayList,this);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRV.setAdapter(newsRVAdapter);
        getNews();
        newsRVAdapter.notifyDataSetChanged();
        return root;
    }

    private void getNews(){
        articlesArrayList.clear();
        String url = "https://gnews.io/api/v4/search?q=covid&token=91b145c4cfebf58974bd497a23bdabd1&lang=en&country=in";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        newsModel newsModel;
                        try {
                            JSONArray r = response.getJSONArray("articles");
                            for (int i = 0; i < r.length(); i++) {
                                JSONObject obj = r.getJSONObject(i);
                                JSONObject source = obj.getJSONObject("source");
                                articlesArrayList.add(new newsModel
                                        (obj.getString("title"),source.getString("name"),
                                                obj.getString("image"),obj.getString("url"))
                                );
                            }
                            newsRVAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
