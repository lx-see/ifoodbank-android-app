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

public class FBOwnerLoginActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";

    private FirestoreUtils firestoreUtils;

    private EditText loginEmail;
    private EditText loginPassword;
    private Button btn_login_confirm;
    private Button btn_login_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_owner_login);

        // Firestore db initialization
        firestoreUtils = new FirestoreUtils();
        FirebaseFirestore db = firestoreUtils.getDb();

        // Check already logined as foodbank owner
        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String foodbankOwnerEmail = sharedPreferences.getString("foodbankOwnerEmail", "");
        Boolean isLogin = sharedPreferences.getBoolean("isLoginAsOwner", false);
        if (isLogin){
            Toast.makeText(getApplicationContext(), "Welcome back! "+foodbankOwnerEmail, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FBOwnerLoginActivity.this, FBOwnerMainActivity.class);
            startActivity(intent);
            FBOwnerLoginActivity.this.finish();
        }

        // Login button
        btn_login_confirm = findViewById(R.id.btn_login_confirm);
        btn_login_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmail = findViewById(R.id.login_email);
                loginPassword = findViewById(R.id.login_password);
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if(email.equals("") || password.equals("")){
                    Toast.makeText(FBOwnerLoginActivity.this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Check database
                    db.collection("FoodbankOwner")
                            .whereEqualTo("email", email)
                            .whereEqualTo("password", password)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Toast.makeText(FBOwnerLoginActivity.this, "Failed login", Toast.LENGTH_SHORT).show();
                                        }
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            Toast.makeText(FBOwnerLoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();

                                            // Add credit
                                            Intent intent = new Intent(FBOwnerLoginActivity.this,FBOwnerMainActivity.class);

                                            SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                                            SharedPreferences.Editor edits = sharedPreferences.edit();
                                            edits.putString("foodbankOwnerEmail", email);
                                            edits.putBoolean("isLoginAsOwner", true);
                                            edits.commit();

                                            startActivity(intent);
                                            FBOwnerLoginActivity.this.finish();
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });

                }
            }
        });

        // Sign in button
        btn_login_register = findViewById(R.id.btn_login_register);
        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to sign-up page
                Intent intent = new Intent(FBOwnerLoginActivity.this,FBOwnerSignupActivity.class);
                startActivity(intent);
                FBOwnerLoginActivity.this.finish();
            }
        });

        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(FBOwnerLoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(FBOwnerLoginActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(FBOwnerLoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}