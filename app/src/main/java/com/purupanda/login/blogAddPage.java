package com.purupanda.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.purupanda.login.databinding.ActivityBlogAddPageBinding;
import com.purupanda.login.models.users;
import com.purupanda.login.ui.blog.blogFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class blogAddPage extends AppCompatActivity {
    private ActivityBlogAddPageBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String userId,userName="default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlogAddPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add a new Blog");


//        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

//        getting the current logged in user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

//        submit button on click method
        binding.blogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Creating new blog");
                progressDialog.setMessage("Your blog is being created");
                progressDialog.show();
                if(binding.blogTitle.getText().toString().equals("") || binding.blogDescription.getText().toString().equals(""))
                {
                    progressDialog.dismiss();
                    if(binding.blogTitle.getText().toString().equals(""))
                        binding.blogTitle.requestFocus();
                    else
                        binding.blogDescription.requestFocus();
                }
                else {
//                    calling upload data function to upload data to the firestore database
                    uploadData(Objects.requireNonNull(binding.blogTitle.getText()).toString(),
                            Objects.requireNonNull(binding.blogDescription.getText()).toString(),
                            Objects.requireNonNull(binding.blogHashTags.getText()).toString()
                    );
                }
            }
        });


//        go back on click listener

        binding.goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    upload data function definition, this function is used to upload the data from the xml file to the firebase firestore database

    private void uploadData(String title, String description, String hashTags) {

        FirebaseDatabase.getInstance().getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users profileUser = snapshot.getValue(users.class);
                if(profileUser==null)
                {
                    Log.d("check", "on null: "+profileUser.getName().toString());
                }
                else
                {
                    userName = profileUser.getName().toString();
                    String id = UUID.randomUUID().toString();
                    Map<String, Object> blogPost = new HashMap<>();
                    blogPost.put("blogId",id);
                    blogPost.put("title",title);
                    blogPost.put("description",description);
                    blogPost.put("hashTags",hashTags);
                    blogPost.put("userId",userId);
                    blogPost.put("userName",userName);
                    blogPost.put("likeCount",0);
                    blogPost.put("commentCount",0);
                    db.collection("blogs")
                            .document(id)
                            .set(blogPost)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(blogAddPage.this, "Post Created", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to create the Post", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}