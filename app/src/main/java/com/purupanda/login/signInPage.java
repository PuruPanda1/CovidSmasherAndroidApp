package com.purupanda.login;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import android.widget.TextView;
import android.widget.Toast;

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
import com.purupanda.login.databinding.ActivitySignInPageBinding;
import com.purupanda.login.models.users;

public class signInPage extends AppCompatActivity {
    private ActivitySignInPageBinding binding;

    private FirebaseAuth auth;
    FirebaseDatabase database;

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Integer> launcher;

    ProgressDialog progressdialog;
//        stop restrict back button

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

//        google sign in button functions

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        launcher = registerForActivityResult(new signInPage.getGoogleSignInDetails(), new ActivityResultCallback<Intent>() {
            @Override
            public void onActivityResult(Intent result) {
                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(result);
                try {
                    GoogleSignInAccount account = googleSignInAccountTask.getResult(ApiException.class);
                    createFirebaseUserFromGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(signInPage.this, "erro: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//      creating progress dialog box settings and value

        progressdialog = new ProgressDialog(signInPage.this);
        progressdialog.setTitle("Logging In");
        progressdialog.setMessage("Logging in into your account");





//        creating instance for the firebase authentication system and database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                progressdialog.show();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressdialog.dismiss();
                        if(task.isSuccessful())
                        {
                            Toast.makeText(signInPage.this, "Siggned in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),news.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(signInPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(25);
            }
        });


    }


    private void createFirebaseUserFromGoogle(String idToken) {
        progressdialog.show();
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
                    Toast.makeText(signInPage.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                    startActivity(in);
                }
                else
                {
                    Log.d("taskexception", "onComplete: "+task.getException().toString());
                }
            }
        });
    }

    public class getGoogleSignInDetails extends ActivityResultContract<Integer,Intent> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer input) {
            return mGoogleSignInClient.getSignInIntent();
        }

        @Override
        public Intent parseResult(int resultCode, @Nullable Intent intent) {
            if(resultCode!= Activity.RESULT_OK || intent == null)
            {
                return null;
            }
            return intent;
        }
    }
}