package com.example.ifoodbank;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FBOwnerMainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_owner_main);

        CardView toMyFoodbank = (CardView) findViewById(R.id.to_my_foodbank);
        CardView toTotalApplication = (CardView) findViewById(R.id.to_total_application);
        CardView toMyAppointment = (CardView) findViewById(R.id.to_my_appointment);
        CardView logout = (CardView) findViewById(R.id.logout);


        TextView showName = (TextView) findViewById(R.id.show_full_name);
        TextView showEmail = (TextView) findViewById(R.id.show_email);
        EditText loginEmail = (EditText) findViewById(R.id.login_email);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String email = sharedPreferences.getString("foodbankOwnerEmail", "");
        showEmail.setText(email);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Confirm to Logout As Food Bank Owner");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                Boolean isLogin = sharedPreferences.getBoolean("isLoginAsOwner", false);
                if (isLogin){
                    sharedPreferences.edit().remove("isLoginAsOwner").remove("foodbankOwnerEmail").apply();

                    Toast.makeText(getApplicationContext(), "Logout Successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FBOwnerMainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error! Cannot perform logout operation", Toast.LENGTH_SHORT).show();
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

        // To My Food Bank
        toMyFoodbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Function to be developed in future.", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(FBOwnerMainActivity.this, FBManageFoodbank.class);
//                startActivity(intent);
            }
        });

        // To Total Application
        toTotalApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Function to be developed in future.", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(FBOwnerMainActivity.this, FBTotalApplication.class);
//                startActivity(intent);
            }
        });

        // To Total Appointment
        toMyAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Function to be developed in future.", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        // Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(FBOwnerMainActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(FBOwnerMainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(FBOwnerMainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}