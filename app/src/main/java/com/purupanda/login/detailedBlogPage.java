package com.purupanda.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.purupanda.login.databinding.ActivityDetailedBlogPageBinding;
import com.purupanda.login.models.blogModel;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.HashMap;
import java.util.Map;

public class detailedBlogPage extends AppCompatActivity {
    private ActivityDetailedBlogPageBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBlogPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent in = getIntent();

//        progress bar variables

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();

        String blogId = in.getStringExtra("blogId");
        dataChange(blogId);
        FirebaseFirestore s = FirebaseFirestore.getInstance();

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
                        }
                        else
                        {
                            binding.grayHeart.setBtnColor(Color.GRAY);
                            binding.grayHeart.setBtnFillColor(Color.RED);
                            binding.grayHeart.setBigShineColor(Color.RED);
                        }
                    }
                });


        binding.grayHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("detailed", "Fireabse User: "+firebaseUser.getUid().toString());
                db.collection("blogs").document(blogId).collection("likedBy").whereEqualTo("userId",firebaseUser.getUid().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Log.d("detailed", "onComplete: "+task.getResult().size());
                                if(task.getResult().size()>0)
                                {
                                    FirebaseFirestore.getInstance().collection("blogs").document(blogId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    int nCount = (Integer.parseInt(task.getResult().get("likeCount").toString())-1);
                                                    updateValues(blogId,nCount,1);
                                                }
                                            });
                                }
                                else
                                {
                                    FirebaseFirestore.getInstance().collection("blogs").document(blogId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    int nCount = (Integer.parseInt(task.getResult().get("likeCount").toString())+1);
                                                    updateValues(blogId,nCount,2);

                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("detailed", "onFailure: "+e.toString());
                            }
                        });
            }
        });

//        binding.redHeart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                disLike();
//                Log.d("detailed", "Fireabse User: "+firebaseUser.getUid().toString());
//                db.collection("blogs").document(blogId).collection("likedBy").whereEqualTo("userId",firebaseUser.getUid().toString())
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                Log.d("detailed", "onComplete: "+task.getResult().size());
//                                if(task.getResult().size()>0)
//                                {
//                                    FirebaseFirestore.getInstance().collection("blogs").document(blogId)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                    int nCount = (Integer.parseInt(task.getResult().get("likeCount").toString())-1);
//                                                    updateValues(blogId,nCount,1);
//                                                }
//                                            });
//                                }
//                                else
//                                {
//                                    FirebaseFirestore.getInstance().collection("blogs").document(blogId)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                    int nCount = (Integer.parseInt(task.getResult().get("likeCount").toString())+1);
//                                                    updateValues(blogId,nCount,2);
//                                                }
//                                            });
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("detailed", "onFailure: "+e.toString());
//                            }
//                        });
//            }
//        });
    }
    public void updateValues(String blogId,int nCount,int option)
    {
        String userId = firebaseUser.getUid().toString();
        FirebaseFirestore.getInstance().collection("blogs").document(blogId)
                .update("likeCount",Integer.toString(nCount));
        switch (option)
        {
            case 1:
                FirebaseFirestore.getInstance().collection("blogs").document(blogId).collection("likedBy").document(userId).delete();
                break;
            case 2:
                Map<String, Object> value = new HashMap<>();
                value.put("userId",userId);
                FirebaseFirestore.getInstance().collection("blogs").document(blogId).collection("likedBy").document(userId).set(value);
                break;
            default:
                Log.d("Error", "updateValues: Error");
        }
        dataChange(blogId);
    }

    public void dataChange(String blogId)
    {

        FirebaseFirestore k = FirebaseFirestore.getInstance();

        k.collection("blogs").document(blogId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
//                        Log.d("detailed", "onComplete: "+task.getResult().get("title").toString());
//                        progress bar
                        binding.blogTitleRV.setText(task.getResult().get("title").toString());
                        binding.blogDescriptionRV.setText(task.getResult().get("description").toString());
                        binding.blogLikeCountRV.setText(task.getResult().get("likeCount").toString());
                        binding.blogCommentCountRV.setText(task.getResult().get("commentCount").toString());
                        binding.blogHashTags.setText(task.getResult().get("hashTags").toString());
                        binding.blogUserNameRV.setText(task.getResult().get("userName").toString());
                        binding.newProgressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detailedBlogPage.this, "Failed to load the data", Toast.LENGTH_LONG).show();
                    }
                });

    }





}