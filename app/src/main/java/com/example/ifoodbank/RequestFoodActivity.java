package com.example.ifoodbank;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

public class RequestFoodActivity extends AppCompatActivity {
    private static final String TAG = "RequestFoodActivity";
    private LinearAdapter adapter;
    private FirestoreUtils utils = new FirestoreUtils();

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mBtnNames = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_food);
        RecyclerView mRvMain = findViewById(R.id.re_view);

        SharedPreferences sharedPreference = getSharedPreferences("applicationInfo",MODE_PRIVATE);
        String foodbankNameList = sharedPreference.getString("foodbankNameList", "");

        for (String foodbankName: foodbankNameList.split(",")
        ) {
            if (!foodbankName.equals("")){
                mBtnNames.add(foodbankName);
            }
        }

        FirebaseFirestore db = utils.getDb();
        db.collection("Foodbank")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String contactNum = (String) document.getData().get("contactNum");
                                String email = (String) document.getData().get("email");
                                String foodDesc = (String) document.getData().get("foodDesc");
                                long foodQuantity = (long) document.getData().get("foodQuantity");
                                Boolean halal = (Boolean) document.getData().get("halal");
                                String imgPath = (String) document.getData().get("imgPath");
                                String location = (String) document.getData().get("location");
                                String foodbankName = (String) document.getData().get("storeName");

                                mNames.add(foodbankName);
                                mDesc.add(foodDesc);

                                if (mNames.size()==12){
                                    adapter = new LinearAdapter(RequestFoodActivity.this, new LinearAdapter.OnItemClickListener() {
                                        @Override
                                        public void onApplyFoodBtnClick(LinearViewHolder holder, int pos) {
                                            // After click the apply food btn
                                            Toast.makeText(RequestFoodActivity.this, "Selected food bank: " + mNames.get(pos), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RequestFoodActivity.this, ApplyFoodActivity.class);
                                            intent.putExtra("foodbankName", mNames.get(pos));
                                            intent.putExtra("foodbankDesc", mDesc.get(pos));

                                            startActivity(intent);
                                        }
                                    }, mNames, mDesc, mBtnNames);
                                    mRvMain.setAdapter(adapter);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(RequestFoodActivity.this, RecyclerView.VERTICAL, false));
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(RequestFoodActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(RequestFoodActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(RequestFoodActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}

// View holder
class LinearViewHolder extends RecyclerView.ViewHolder{
    TextView foodbank_name;
    TextView food_provided;
    Button apply_food_btn;

    public LinearViewHolder(@NonNull View itemView){
        super(itemView);
        foodbank_name = itemView.findViewById(R.id.foodbank_name);
        food_provided = itemView.findViewById(R.id.food_provided);
        apply_food_btn = itemView.findViewById(R.id.apply_food_btn);
    }
}

// Linear Adapter
class LinearAdapter extends RecyclerView.Adapter<LinearViewHolder> {

    private Context mContext;
    private OnItemClickListener mListener;
    private ArrayList<String> mNames;
    private ArrayList<String> mDescs;
    private ArrayList<String> mBtnNames;

    public LinearAdapter(Context context, OnItemClickListener listener, ArrayList<String> mNames, ArrayList<String> mDescs, ArrayList<String> mBtnNames){
        this.mContext = context;
        this.mListener = listener;
        this.mNames = mNames;
        this.mDescs = mDescs;
        this.mBtnNames = mBtnNames;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foodbank, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {

        holder.foodbank_name.setText(mNames.get(position));
        holder.food_provided.setText(mDescs.get(position));

        for (String fbName: mBtnNames
             ) {
            if(mNames.get(position).equals(fbName)){
                holder.apply_food_btn.setBackgroundColor(R.color.btn_disabled);
                holder.apply_food_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "You have applied this food bank.", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
        }

        // apply_food_btn direct to application form
        holder.apply_food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onApplyFoodBtnClick(holder, position);
            }
        });
    }

    public interface OnItemClickListener{
        void onApplyFoodBtnClick(LinearViewHolder holder, int pos);
   }

    @Override
    public int getItemCount() {
        return mNames.size();
    }
}