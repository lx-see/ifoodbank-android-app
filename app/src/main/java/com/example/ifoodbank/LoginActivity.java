package com.example.ifoodbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.data.entity.Foodbank;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.io.ObjectStreamException;
import java.util.*;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private FirestoreUtils firestoreUtils;

    private ViewFlipper viewFlipper;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button btn_login_confirm;
    private Button btn_login_register;
    private SignInButton btn_google_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firestore db initialization
        firestoreUtils = new FirestoreUtils();
        FirebaseFirestore db = firestoreUtils.getDb();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Slide show
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {
            viewFlipper.startFlipping();//Auto play
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
                    Toast.makeText(LoginActivity.this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Check database
                    db.collection("User")
                            .whereEqualTo("email", email)
                            .whereEqualTo("password", password)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Toast.makeText(LoginActivity.this, "Failed login", Toast.LENGTH_SHORT).show();
                                        }
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();

                                            // Add credit
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                                            SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                                            SharedPreferences.Editor edits = sharedPreferences.edit();
                                            edits.putString("email", email);
                                            edits.putBoolean("isLoginWithGoogle", false);
                                            edits.commit();

                                            startActivity(intent);
                                            LoginActivity.this.finish();
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
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        // Sign in with Google button
        btn_google_login = findViewById(R.id.btn_google_login);
        btn_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Google authentication
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // Testing Add foodbank data
//        FirestoreUtils firestoreUtils = new FirestoreUtils();
//        Map<String, Object> document = new HashMap<>();
//        document.put("storeName","YAYASAN FOOD BANK MALAYSIA (YFBM)");
//        document.put("foodDesc","Hotmeals");
//        document.put("foodQuantity", 500);
//        document.put("location", "Jalan Chow Kit");
//        document.put("contactNum", "03-87360117");
//        document.put("email", "info@yfbm.org");
//        document.put("halal",1);
//        document.put("imgPath","");
//
//        firestoreUtils.addDocument("Foodbank", document);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        if (getIntent().getBooleanExtra("LogoutWithGoogle", false)){
//            SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
//            SharedPreferences.Editor edits = sharedPreferences.edit();
//            edits.putBoolean("LogoutWithGoogle", false).apply();
//
            return;
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            Toast.makeText(LoginActivity.this, "Already signed-in with Google", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);

            SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
            SharedPreferences.Editor edits = sharedPreferences.edit();
            edits.putString("email", account.getEmail());
            edits.putBoolean("isLoginWithGoogle", true);

            edits.apply();

            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            String email = "";

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                email = personEmail;
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                System.out.println("personName: "+personName);
                System.out.println("personGivenName: "+personGivenName);
                System.out.println("personFamilyName: "+personFamilyName);
                System.out.println("personEmail: "+personEmail);
                System.out.println("personId: "+personId);
                System.out.println("personPhoto: "+personPhoto);
            }

            // Signed in successfully, show authenticated UI.
            Toast.makeText(LoginActivity.this, "Successful signed-in with Google", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);

            SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
            SharedPreferences.Editor edits = sharedPreferences.edit();
            edits.putString("email", email);
            edits.putBoolean("isLoginWithGoogle", true);

            startActivity(intent);
            LoginActivity.this.finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed to login with Google! Please retry again", Toast.LENGTH_SHORT).show();
        }
    }
}