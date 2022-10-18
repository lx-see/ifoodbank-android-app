package com.example.ifoodbank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private FirestoreUtils firestoreUtils;
    
    private ViewFlipper viewFlipper;
    private EditText signupEmail;
    private EditText signupPassword;
    private Button btn_signup_confirm;
    private Button btn_signup_register;
    private SignInButton btn_google_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firestore db initialization
        firestoreUtils = new FirestoreUtils();
        FirebaseFirestore db = firestoreUtils.getDb();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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

        // Sign-up button
        btn_signup_confirm = findViewById(R.id.btn_signup_confirm);
        btn_signup_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupEmail = findViewById(R.id.signup_email);
                signupPassword = findViewById(R.id.signup_password);
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();

                if(email.equals("") || password.equals("")){
                    Toast.makeText(SignupActivity.this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Check existing record
                    db.collection("User")
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
                                            firestoreUtils.addDocument("User", document);

                                            Toast.makeText(SignupActivity.this, "Successful Sign-up! Back to login page...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                            SignupActivity.this.finish();
                                        }
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            Toast.makeText(SignupActivity.this, "Email registered. Please login to your existing account", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                SignupActivity.this.finish();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            Toast.makeText(SignupActivity.this, "Already signed-in with Google", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
            SignupActivity.this.finish();
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

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
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
            Toast.makeText(SignupActivity.this, "Successful signed-in with Google", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
            SignupActivity.this.finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(SignupActivity.this, "Failed to login with Google! Please retry again", Toast.LENGTH_SHORT).show();
        }
    }
}