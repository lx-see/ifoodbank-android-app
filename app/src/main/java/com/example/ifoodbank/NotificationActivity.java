package com.example.ifoodbank;

import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private NotificationLinearAdapter adapter;
    private FirestoreUtils utils = new FirestoreUtils();

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        RecyclerView mRvMain = findViewById(R.id.re_view);

        SharedPreferences sharedPreferences = getSharedPreferences("applicationInfo",MODE_PRIVATE);
        SharedPreferences.Editor edits = sharedPreferences.edit();

        String foodbankNameList = sharedPreferences.getString("foodbankNameList", "");
        String applicationTimeList = sharedPreferences.getString("applicationTimeList", "");

        for (String foodbankName: foodbankNameList.split(",")
             ) {
            if (!foodbankName.equals("")){
                mNames.add(foodbankName);
            }
        }

        for (String time: applicationTimeList.split(",")
        ) {
            if (!time.equals("")){
                mDesc.add(time);
            }
        }

        adapter = new NotificationLinearAdapter(NotificationActivity.this, new NotificationLinearAdapter.OnItemClickListener() {
            // Click eye-button to display or hide password
            @Override
            public void onMakeAppointmentBtnClick(NotificationLinearViewHolder holder, int pos) {
                // After click the apply food btn
                Toast.makeText(NotificationActivity.this, "Function to be developed in future.", Toast.LENGTH_SHORT).show();
            }
        }, mNames, mDesc);
        mRvMain.setAdapter(adapter);
        mRvMain.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false));


        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(NotificationActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(NotificationActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(NotificationActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}

// View holder
class NotificationLinearViewHolder extends RecyclerView.ViewHolder{
    TextView foodbank_name;
    TextView food_provided;
    Button make_appointment_btn;

    public NotificationLinearViewHolder(@NonNull View itemView){
        super(itemView);
        foodbank_name = itemView.findViewById(R.id.foodbank_name);
        food_provided = itemView.findViewById(R.id.food_provided);
        make_appointment_btn = itemView.findViewById(R.id.make_appointment_btn);
    }
}

// Linear Adapter
class NotificationLinearAdapter extends RecyclerView.Adapter<NotificationLinearViewHolder> {

    private Context mContext;
    private NotificationLinearAdapter.OnItemClickListener mListener;
    private ArrayList<String> mNames;
    private ArrayList<String> mDescs;

    public NotificationLinearAdapter(Context context, NotificationLinearAdapter.OnItemClickListener listener, ArrayList<String> mNames, ArrayList<String> mDescs){
        this.mContext = context;
        this.mListener = listener;
        this.mNames = mNames;
        this.mDescs = mDescs;


    }

    @NonNull
    @Override
    public NotificationLinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationLinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationLinearViewHolder holder, int position) {
        holder.foodbank_name.setText(mNames.get(position));
        holder.food_provided.setText(mDescs.get(position));

        // apply_food_btn direct to application form
        holder.make_appointment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMakeAppointmentBtnClick(holder, position);
            }
        });
    }

    public void setmNames(ArrayList<String> mNames) {
        this.mNames = mNames;
        notifyDataSetChanged();
    }

    public void setmDescs(ArrayList<String> mDescs) {
        this.mDescs = mDescs;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onMakeAppointmentBtnClick(NotificationLinearViewHolder holder, int pos);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }
}