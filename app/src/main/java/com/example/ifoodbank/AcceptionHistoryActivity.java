package com.example.ifoodbank;

import android.content.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AcceptionHistoryActivity extends AppCompatActivity {
    private AcceptionHistoryLinearAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acception_history);
        RecyclerView mRvMain = findViewById(R.id.re_view);

        adapter = new AcceptionHistoryLinearAdapter(AcceptionHistoryActivity.this);
        mRvMain.setAdapter(adapter);
        mRvMain.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(AcceptionHistoryActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(AcceptionHistoryActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(AcceptionHistoryActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}

// View holder
class AcceptionHistoryLinearViewHolder extends RecyclerView.ViewHolder{
    TextView acception_date;
    TextView foodbank_name;
    TextView food_collected;

    public AcceptionHistoryLinearViewHolder(@NonNull View itemView){
        super(itemView);
        acception_date = itemView.findViewById(R.id.acception_date);
        foodbank_name = itemView.findViewById(R.id.foodbank_name);
        food_collected = itemView.findViewById(R.id.food_collected);
    }
}

// Linear Adapter
class AcceptionHistoryLinearAdapter extends RecyclerView.Adapter<AcceptionHistoryLinearViewHolder> {

    private Context mContext;

    public AcceptionHistoryLinearAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public AcceptionHistoryLinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AcceptionHistoryLinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acception_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptionHistoryLinearViewHolder holder, int position) {
        String acception_date = "20/11/2021";
        String foodbank_name = "Food Bank A";
        String food_collected = "Food Collected: xxx, xxx, xxx...";

        holder.acception_date.setText(acception_date);
        holder.foodbank_name.setText(foodbank_name);
        holder.food_collected.setText(food_collected);

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}