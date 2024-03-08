package com.example.myspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.myspace.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String FirstName,LastName,Email,Password,UID;


    private DatabaseReference User_ref,curr_user;
    private final FirebaseDatabase DB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --------------------------------------------------- Registering User and saving Details ------------------------------------------------------------------------//

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstName = binding.etFirstName.getText().toString();
                LastName = binding.etLastName.getText().toString();
                Email = binding.etRegisterEmail.getText().toString();
                Password = binding.etRegisterPassword.getText().toString();

                if((!FirstName.isEmpty() || !LastName.isEmpty()) && !Email.isEmpty() && !Password.isEmpty())
                {
                    if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()  && Password.length()>=8)
                    {
                        CreateUser();
                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches() ) binding.etRegisterEmail.setError("Enter a vaild Email");
                    else binding.etRegisterPassword.setError("Password should be atleast of 8 characters");
                }
                else if(FirstName.isEmpty())
                {
                    binding.etFirstName.setError("Empty Feild are not allowed");
                }
                else if(Email.isEmpty()) binding.etRegisterEmail.setError("Empty Feilds are not allowed");
                else if(LastName.isEmpty()) binding.etRegisterPassword.setError("Empty Felids are not allowed");
                else Toast.makeText(SignUp.this, "Fill Details Properly", Toast.LENGTH_SHORT).show();


            }
//----------------------------------------------------------Creating User----------------------------------------------------------------------------------------------------//

            private void CreateUser() {

                auth.createUserWithEmailAndPassword(Email , Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUp.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            User_ref = DB.getReference().child("Users");
                            UID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            curr_user = User_ref.child(UID);
                            StoreData();
                            Intent it = new Intent(SignUp.this , LogIn.class);
                            startActivity(it);

                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

//----------------------------------------------------------------------------Storing User Profile ----------------------------------------------------------------------------//
                    private void StoreData() {

                        String Profile = "";

                        HashMap<String , String> UserMap = new HashMap<>();

                        UserMap.put("UserName" , FirstName+" "+LastName);
                        UserMap.put("Email" , Email);
                        UserMap.put("Password" , Password);
                        UserMap.put("HomeTown" , "");
                        UserMap.put("Profile" , Profile);
                        UserMap.put("Gender" , "");
                        UserMap.put("Bio" , "");
                        UserMap.put("UID" , UID);
                        UserMap.put("Relationship Status" , "");


                        curr_user.setValue(UserMap);
                        binding.etLastName.setText("");
                        binding.etFirstName.setText("");
                        binding.etRegisterEmail.setText("");
                        binding.etRegisterPassword.setText("");


                    }
                });

            }
        });

        //------------------------------------------------Already a User------------------------------------------------------------//

        binding.AlreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });



    }
}