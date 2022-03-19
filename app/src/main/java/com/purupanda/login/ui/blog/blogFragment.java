package com.purupanda.login.ui.blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.purupanda.login.R;
import com.purupanda.login.blogAddPage;
import com.purupanda.login.blogCustomAdapter;
import com.purupanda.login.databinding.FragmentBlogBinding;
import com.purupanda.login.models.blogModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link blogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class blogFragment extends Fragment {
    private FragmentBlogBinding binding;

    //    variables for blog recyclerview
    private RecyclerView blogRv;
    private ArrayList<blogModel> blogsArrayList;
    private blogCustomAdapter mAdapter;

//    firebase firestore database
    FirebaseFirestore db;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public blogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment blogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static blogFragment newInstance(String param1, String param2) {
        blogFragment fragment = new blogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args)    ;
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
//        binding.newProgressBar.setVisibility(View.VISIBLE);
//        pd = new ProgressDialog(getContext());
//        pd.setTitle("Loading");
//        pd.setMessage("loading the forum");
//        pd.show();
        binding = FragmentBlogBinding.inflate(inflater,container,false);

//        code for blog recyclerview

        blogRv = binding.blogRecylerView;
        blogsArrayList = new ArrayList<>();
        mAdapter = new blogCustomAdapter(blogsArrayList,this);
        blogRv.setLayoutManager(new LinearLayoutManager(getContext()));
        blogRv.setAdapter(mAdapter);
        getBlogs();

        final int[] state = new int[1];
        blogRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state[0] = newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 && (state[0]==0 || state[0]==2))
                {
                    binding.slidingBlogLayout.setVisibility(View.GONE);
                }
                else if(dy< (0))
                {
                    binding.slidingBlogLayout.setVisibility(View.VISIBLE);
                }
            }
        });

//        mAdapter.notifyDataSetChanged();
//       mAdapter.notifyDataSetChanged(); use this to show and update blog


        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),blogAddPage.class));
            }
        });


        return binding.getRoot();


    }

//    private void getBlogs() {
//        blogsArrayList.clear();
//
////        fetching data from the firestore database
//        db = FirebaseFirestore.getInstance();
//        db.collection("blogs")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful())
//                        {
////                            pd.dismiss();
//                            binding.newProgressBar.setVisibility(View.GONE);
//                            for(QueryDocumentSnapshot document : task.getResult())
//                            {
//                                Log.d("getBlogs", "onComplete: "+document.getData());
//
//                                blogsArrayList.add(new blogModel(
//                                        document.get("title").toString(),document.get("description").toString(),
//                                        document.get("hashTags").toString(),document.get("userId").toString(),
//                                        document.get("userName").toString(),document.get("likeCount").toString(),
//                                        document.get("commentCount").toString(),
//                                        document.get("blogId").toString()
//                                ));
//                                Log.d("getBlogs", "onComplete: "+document.get("description"));
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        }
//                        else
//                        {
//                            Log.d("getBlogs", "onComplete: "+task.getException());
//                        }
//                    }
//                });
//    }

    private void getBlogs() {
        blogsArrayList.clear();

//        fetching data from the firestore database
        db = FirebaseFirestore.getInstance();
        db.collection("blogs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            Toast.makeText(getContext(), "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            binding.newProgressBar.setVisibility(View.GONE);
                            for(DocumentSnapshot document : value)
                            {
                                Log.d("getBlogs", "onComplete: "+document.getData());

                                blogsArrayList.add(new blogModel(
                                        document.get("title").toString(),document.get("description").toString(),
                                        document.get("hashTags").toString(),document.get("userId").toString(),
                                        document.get("userName").toString(),document.get("likeCount").toString(),
                                        document.get("commentCount").toString(),
                                        document.get("blogId").toString()
                                ));
                                Log.d("getBlogs", "onComplete: "+document.get("description"));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}