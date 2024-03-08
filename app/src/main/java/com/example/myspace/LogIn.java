package com.example.myspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.myspace.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    private ActivityLogInBinding binding;


    private FirebaseAuth auth;
    private FirebaseUser user;
    private  String Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //----------------------------- -------------------------------------avoid user to ReLogin-------------------------------------------------------------------//
        if(user!=null)
        {
            Intent it = new Intent(LogIn.this,MainActivity.class);
            startActivity(it);
            finish();
        }

        //-------------------------------------------------------------------LogIn button ------------------------------------------------------------------------------------//
        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = binding.etLogInEmail.getText().toString();
                Password = binding.etLogInPassword.getText().toString();

                if(!Email.isEmpty() && !Password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Email).matches() )
                {
                    LogInUser();
                }
                else Toast.makeText(LogIn.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }

            private void LogInUser() {

                auth.signInWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LogIn.this, "LogIn Successfull", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(LogIn.this,MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogIn.this, "LogIn Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        //---------------------------------------------------------------New User------------------------------------------------//
        binding.NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this,SignUp.class));
            }
        });




    }
}