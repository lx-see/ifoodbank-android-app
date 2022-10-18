package com.example.ifoodbank;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        CardView toMyAppointment = (CardView) findViewById(R.id.to_my_appointment);
        CardView toUpdateInfo = (CardView) findViewById(R.id.to_update_info);
        CardView logout = (CardView) findViewById(R.id.logout);
        TextView showName = (TextView) findViewById(R.id.show_full_name);
        TextView showEmail = (TextView) findViewById(R.id.show_email);
        EditText loginEmail = (EditText) findViewById(R.id.login_email);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        showEmail.setText(email);

        ArrayList<String> content = mySQLiteAdapter.queryRow(email);
        mySQLiteAdapter.close();

        String fullName, emailAdd = "";

        if (content.size() != 0){
            fullName = content.get(0);
            emailAdd = content.get(2);

            showName.setText(fullName);
            showEmail.setText(emailAdd);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Confirm to Logout");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                Boolean isLoginWithGoogle = sharedPreferences.getBoolean("isLoginWithGoogle", false);
                if (isLoginWithGoogle){
                    signOut();
                }
                else{
                    sharedPreferences.edit().clear().commit();

                    Toast.makeText(getApplicationContext(), "Logout Successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        toUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.show();

            }
        });

        toMyAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Function to be developed in future.", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(ProfileActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }

    // Sign out of Google
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        revokeAccess();
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                        SharedPreferences.Editor edits = sharedPreferences.edit();
                        edits.putBoolean("isLoginWithGoogle", false).apply();

                        Toast.makeText(getApplicationContext(), "Logout Successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.putExtra("LogoutWithGoogle", true);
                        startActivity(intent);
                        ProfileActivity.this.finish();
                    }
                });
    }
}