package com.example.ifoodbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;

    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("HOME");
        setContentView(R.layout.activity_main);

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // signOut();
        // revokeAccess();

        CardView toAllLocation = (CardView) findViewById(R.id.to_all_location);
        CardView toRequestFood = (CardView) findViewById(R.id.to_request_food);
        CardView toManageFoodbank = (CardView) findViewById(R.id.to_manage_foodbank);
        CardView toAcceptionHistory = (CardView) findViewById(R.id.to_acception_history);
        CardView toNotification = (CardView) findViewById(R.id.to_notification);
        CardView toHotline = (CardView) findViewById(R.id.to_hotline);

        TextView showFullName = (TextView) findViewById(R.id.show_full_name);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        ArrayList<String> content = mySQLiteAdapter.queryRow(email);
        mySQLiteAdapter.close();

        String fullName = "";

        if (content.size() != 0){
            fullName = content.get(0);

            showFullName.setText(fullName);
        }

        // All location
        toAllLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllLocationActivity.class);
                startActivity(intent);
            }
        });

        // Request food
        toRequestFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RequestFoodActivity.class);
                startActivity(intent);
            }
        });

        // Manage foodbank
        toManageFoodbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FBOwnerLoginActivity.class);
                startActivity(intent);
            }
        });

        // Acception History
        toAcceptionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Function to be developed in future", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, AcceptionHistoryActivity.class);
                startActivity(intent);

            }
        });

        // Notification
        toNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        // Hotline
        toHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HotlineActivity.class);
                startActivity(intent);
            }
        });

        // Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}