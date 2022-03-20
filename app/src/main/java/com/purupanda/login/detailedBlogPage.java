package com.purupanda.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.purupanda.login.databinding.ActivityDetailedBlogPageBinding;
import com.purupanda.login.models.commentModel;
import com.purupanda.login.models.users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class detailedBlogPage extends AppCompatActivity {
    private ActivityDetailedBlogPageBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
//    Variables for recyclerview
    private RecyclerView commentRV;
    private ArrayList<commentModel> commentArrayList;
    private commentCustomAdapter mAdapter;
    private int countsOfLike;
    private boolean liked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBlogPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getting Intent data which is blog Id
        Intent in = getIntent();
        String blogId = in.getStringExtra("blogId");

//        getting the current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

//        commentRV variables
        commentRV = findViewById(R.id.commentRV);
        commentArrayList = new ArrayList<>();
        mAdapter = new commentCustomAdapter(commentArrayList,this);
        commentRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentRV.setAdapter(mAdapter);
        dataChange(blogId);
        getComments(blogId);
        FirebaseFirestore s = FirebaseFirestore.getInstance();

//        setting heart bydefault color when a blog is opened/viewed
        s.collection("blogs").document(blogId).collection("likedBy").whereEqualTo("userId",firebaseUser.getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()>0)
                        {
                            binding.grayHeart.setBtnColor(Color.RED);
                            binding.grayHeart.setBtnFillColor(Color.GRAY);
                            binding.grayHeart.setBigShineColor(Color.RED);
                            liked=true;
                        }
                        else
                        {
                            binding.grayHeart.setBtnColor(Color.GRAY);
                            binding.grayHeart.setBtnFillColor(Color.RED);
                            binding.grayHeart.setBigShineColor(Color.RED);
                            liked=false;
                        }
                    }
                });


        binding.grayHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("detailed", "Fireabse User: "+firebaseUser.getUid().toString());
                if(liked)
                {
                    countsOfLike--;
                    binding.blogLikeCountRV.setText(String.valueOf(countsOfLike));
                    liked=false;
                    updateValues(blogId,countsOfLike,1);
                }
                else
                {
                    countsOfLike++;
                    binding.blogLikeCountRV.setText(String.valueOf(countsOfLike));
                    liked=true;
                    updateValues(blogId,countsOfLike,2);
                }
            }
        });
        final String[] commentUsername = new String[1];
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users profileUser = snapshot.getValue(users.class);
                if(profileUser != null)
                {
                    commentUsername[0] = profileUser.getName();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        comment post on click listener
        binding.commentLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.commentDescription.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "Empty Comment", Toast.LENGTH_SHORT).show();
                }
                else {
                String commentUserId = firebaseUser.getUid().toString();
                String commentDescription = String.valueOf(binding.commentDescription.getText());
                long dateTime = System.currentTimeMillis();
                String commentId = UUID.randomUUID().toString();
                Map<String, Object> comment = new HashMap<>();
                comment.put("commentUserId", commentUserId);
                comment.put("commentDescription", commentDescription);
                comment.put("dateTime", dateTime);
                comment.put("commentId", commentId);
                comment.put("commentUsername", commentUsername[0]);
                FirebaseFirestore.getInstance().collection("blogs").document(blogId).collection("comments")
                        .document(commentId)
                        .set(comment)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getComments(blogId);
                                binding.commentDescription.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(detailedBlogPage.this, "Failed to add the comment", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            }
        });

    }
    public void updateValues(String blogId,int nCount,int option)
    {
        String userId = firebaseUser.getUid().toString();
        FirebaseFirestore.getInstance().collection("blogs").document(blogId)
                .update("likeCount",Integer.toString(nCount));
        switch (option)
        {
            case 1: // if user is present -- unlike
                FirebaseFirestore.getInstance().collection("blogs").document(blogId).collection("likedBy").document(userId).delete();
                break;
            case 2: // if user is not present -- like & adding the user
                Map<String, Object> value = new HashMap<>();
                value.put("userId",userId);
                FirebaseFirestore.getInstance().collection("blogs").document(blogId).collection("likedBy").document(userId).set(value);
                break;
            default:
                Log.d("Error", "updateValues: Error");
        }
    }

    public void dataChange(String blogId)
    {
        FirebaseFirestore k = FirebaseFirestore.getInstance();
        k.collection("blogs").document(blogId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        binding.blogTitleRV.setText(task.getResult().get("title").toString());
                        binding.blogDescriptionRV.setText(task.getResult().get("description").toString());
                        binding.blogLikeCountRV.setText(task.getResult().get("likeCount").toString());
                        countsOfLike = Integer.parseInt(task.getResult().get("likeCount").toString());
                        binding.blogCommentCountRV.setText(task.getResult().get("commentCount").toString());
                        binding.blogHashTags.setText(task.getResult().get("hashTags").toString());
                        binding.blogUserNameRV.setText(task.getResult().get("userName").toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detailedBlogPage.this, "Failed to load the data", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void getComments(String blogId)
    {
        commentArrayList.clear();
        FirebaseFirestore commentDB = FirebaseFirestore.getInstance();
        commentDB.collection("blogs").document(blogId).collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            Toast.makeText(detailedBlogPage.this, "Error"+error, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            for(DocumentSnapshot v : value)
                            {
                                long milli = v.getLong("dateTime");
//                                SimpleDateFormat sdf = new SimpleDateFormat("kk:mm",Locale.getDefault());
                                String time = getTimeAgo(milli);
                                commentArrayList.add(new commentModel(String.valueOf(v.get("commentUsername")),
                                        String.valueOf(v.get("commentDescription")),String.valueOf(v.get("commentId")),
                                        time,String.valueOf(v.get("commentUserId"))
                                        ));
                            }
                            binding.newProgressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public String getTimeAgo(long time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



}