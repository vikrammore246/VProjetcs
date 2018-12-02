package com.example.pollutiondetector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView txtThreshold, txtMainValue, txtLpgValue;
    private FirebaseDatabase firebaseDatabase;
    private int smokeValue, lpgValue;
    private String smokeValueString, lpgValueString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();


        txtThreshold = findViewById(R.id.txtSetThreshold);
        txtMainValue = findViewById(R.id.txtValues);
        txtLpgValue = findViewById(R.id.txtLpgValue);


        //CHECK IF INTERNET IS CONNECTED OR NOT
        if (isOnline()){

            KeepRunningMainProgram();

        }else {

            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Oops!")
                    .setCancelable(false)
                    .setMessage("Internet Connection is not available.\nTurn ON your Mobile Data to continue.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            finish();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


    }

    public boolean isOnline(){
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    private void KeepRunningMainProgram() {

        DatabaseReference smokeRef = firebaseDatabase.getReference("smoke_value");
        DatabaseReference lpgRef = firebaseDatabase.getReference("lpg_value");

        //READ SMOKE
        smokeRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                smokeValue = dataSnapshot.getValue(Integer.class);
                smokeValueString = String.valueOf(smokeValue);
                txtMainValue.setText(smokeValueString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //READ LPG
        lpgRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lpgValue = dataSnapshot.getValue(Integer.class);
                lpgValueString = String.valueOf(lpgValue);
                txtLpgValue.setText(lpgValueString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


   }
