package com.example.ifoodbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FBOwnerSignupActivity extends AppCompatActivity {
    private static final String TAG = "FBOwnerSignupActivity";

    private FirestoreUtils firestoreUtils;

    private EditText signupEmail;
    private EditText signupPassword;
    private EditText signupName;
    private Button btn_signup_confirm;
    private Button btn_signup_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_owner_signup);

        // Firestore db initialization
        firestoreUtils = new FirestoreUtils();
        FirebaseFirestore db = firestoreUtils.getDb();

        // Sign-up button
        btn_signup_confirm = findViewById(R.id.btn_signup_confirm);
        btn_signup_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupEmail = findViewById(R.id.signup_email);
                signupPassword = findViewById(R.id.signup_password);
                signupName = findViewById(R.id.signup_name);

                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String name = signupName.getText().toString();

                if(email.equals("") || password.equals("") || name.equals("")){
                    Toast.makeText(FBOwnerSignupActivity.this, "Please fill in all information", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Check existing record
                    db.collection("FoodbankOwner")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            // Insert record into db
                                            Map<String, Object> document = new HashMap<>();
                                            document.put("email", email);
                                            document.put("password", password);
                                            document.put("name", name);
                                            document.put("foodbank", "");

                                            firestoreUtils.addDocument("FoodbankOwner", document);

                                            Toast.makeText(FBOwnerSignupActivity.this, "Successful Sign-up! Back to login page...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(FBOwnerSignupActivity.this,FBOwnerLoginActivity.class);
                                            startActivity(intent);
                                            FBOwnerSignupActivity.this.finish();
                                        }
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            Toast.makeText(FBOwnerSignupActivity.this, "Email registered. Please login to your existing account", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        // Login button
        btn_signup_register = findViewById(R.id.btn_signup_register);
        btn_signup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to login page
                Intent intent = new Intent(FBOwnerSignupActivity.this,FBOwnerLoginActivity.class);
                startActivity(intent);
                FBOwnerSignupActivity.this.finish();
            }
        });

        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(FBOwnerSignupActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(FBOwnerSignupActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(FBOwnerSignupActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}