package com.purupanda.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.purupanda.login.databinding.ActivityMainBinding;
import com.purupanda.login.models.users;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private FirebaseAuth auth;
    FirebaseDatabase database;

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Integer> launcher;

    ProgressDialog progressDialog;
//        stop restrict back button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

// google authentication

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestIdToken("763423662321")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        launcher = registerForActivityResult(new getGoogleSignInDetails(), new ActivityResultCallback<Intent>() {
            @Override
            public void onActivityResult(Intent result) {
                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(result);
                try {
                    GoogleSignInAccount account = googleSignInAccountTask.getResult(ApiException.class);
                    createFirebaseUserFromGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(MainActivity.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });




//        firebase authentication and database

        auth = FirebaseAuth.getInstance();// for authorisation
        database = FirebaseDatabase.getInstance(); // for accessing the database

//        creating a progress dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Signing in");
        progressDialog.setMessage("We are creating your account");

//        intent to sign in page with onclick event
        binding.toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),signInPage.class);
                startActivity(intent);
            }
        });

        binding.googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(12);
            }
        });

//        sign up button onclick event

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            users user = new users(name,email,password);
                            String id = task.getResult().getUser().getUid();

                            database.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(MainActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    private void createFirebaseUserFromGoogle(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    users user = new users("",firebaseUser.getEmail(),"");
                    String id = firebaseUser.getUid();
                    database.getReference().child("Users").child(id).setValue(user);
                    Intent in = new Intent(getApplicationContext(),news.class);
                    Toast.makeText(MainActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                    startActivity(in);
                }
                else
                {
                    Log.d("taskexception", "onComplete: "+task.getException().toString());
                }
            }
        });
    }

    public class getGoogleSignInDetails extends ActivityResultContract<Integer,Intent>{

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer input) {
            return mGoogleSignInClient.getSignInIntent();
        }

        @Override
        public Intent parseResult(int resultCode, @Nullable Intent intent) {
            if(resultCode!=Activity.RESULT_OK || intent == null)
            {
                return null;
            }
            return intent;
        }
    }

}