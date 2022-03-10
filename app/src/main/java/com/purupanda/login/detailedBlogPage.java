package com.purupanda.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.purupanda.login.databinding.ActivityDetailedBlogPageBinding;
import com.purupanda.login.models.blogModel;

public class detailedBlogPage extends AppCompatActivity {
    private ActivityDetailedBlogPageBinding binding;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBlogPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent in = getIntent();

        db = FirebaseFirestore.getInstance();

        String title = in.getStringExtra("title");
        String description = in.getStringExtra("description");
        String likeCount = in.getStringExtra("likeCount");
        String commentCount = in.getStringExtra("commentCount");
        String hashTags = in.getStringExtra("hashTags");
        String username = in.getStringExtra("username");
        String userId = in.getStringExtra("userId");
        String blogId = in.getStringExtra("blogId");

        binding.blogTitleRV.setText(title);
        binding.blogDescriptionRV.setText(description);
        binding.blogLikeCountRV.setText(likeCount);
        binding.blogCommentCountRV.setText(commentCount);
        binding.blogHashTags.setText(hashTags);
        binding.blogUserNameRV.setText(username);

        binding.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("blogs").document(blogId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Log.d("detailed", "onComplete: "+task.getResult());
                                Log.d("detailed", "onComplete: "+task.getResult().get("title"));
                                int count = Integer.parseInt(task.getResult().get("likeCount").toString())+1;
                                binding.blogLikeCountRV.setText(Integer.toString(count));
                                db.collection("blogs")
                                        .document(blogId)
                                        .update("likeCount",Integer.toString(count))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(detailedBlogPage.this, "Liked", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(detailedBlogPage.this, "Exception: "+e, Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
            }
        });
    }
}