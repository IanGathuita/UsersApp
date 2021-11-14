package com.example.usersapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Declarations
    EditText etUserId;
    EditText etUserName;
    EditText etUserDesignation;
    EditText etUserLocation;
    Button btnAdd;
    Button btnRead;
    Button btnDelete;
    Button btnUpdate;
    TextView tvUsersInfo;
    MyDatabase myDatabaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Give the activity an actionbar with custom title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Users database");

        //assign values to declarations
        etUserName = findViewById(R.id.etUserName);
        etUserDesignation = findViewById(R.id.etUserDesignation);
        etUserLocation = findViewById(R.id.etUserLocation);
        etUserId = findViewById(R.id.etUserId);
        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        tvUsersInfo = findViewById(R.id.tvUsersInfo);
        myDatabaseInstance = MyDatabase.getMyDatabaseInstance(MainActivity.this);

        //Click handlers
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String userDesignation = etUserDesignation.getText().toString().trim();
                String userLocation = etUserLocation.getText().toString().trim();
                if(userName.isEmpty() || userDesignation.isEmpty() || userLocation.isEmpty()){
                    Toast.makeText(MainActivity.this,"Make sure you enter all fields i.e name, designation and location.",Toast.LENGTH_LONG).show();
                    etUserId.requestFocus();
                    return;
                }

                Long userId = myDatabaseInstance.insertUser(userName,userDesignation,userLocation);
                if(userId == -1){
                    Toast.makeText(MainActivity.this,"Could not add user",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Added new user with id of " + userId,Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor allUsersCursor = myDatabaseInstance.queryUsers();
                /*Iterate over the result set (cursor) while appending new values to form a long String
                /to be set in the textview. Java StringBuilder is preferred for making really long strings
                instead of concatenations.*/
                StringBuilder stringBuilder = new StringBuilder("List of users\n");
                while (allUsersCursor.moveToNext()){
                    stringBuilder.append(allUsersCursor.getInt(0))//id column
                            .append(". ")//full stop and space
                            .append(allUsersCursor.getString(1))//name column
                            .append(", ")//comma and space
                            .append(allUsersCursor.getString(2))//designation column
                            .append(", ")//comma and space
                            .append(allUsersCursor.getString(3))//location column
                            .append(".\n");//full stop and new line
                }
                tvUsersInfo.setText(stringBuilder);

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = etUserId.getText().toString().trim();
                String newUserDesignation = etUserDesignation.getText().toString().trim();
                if(userId.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter an id",Toast.LENGTH_SHORT).show();
                    etUserId.requestFocus();
                    return;
                }
                if(newUserDesignation.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter a new designation",Toast.LENGTH_SHORT).show();
                    etUserDesignation.requestFocus();
                    return;
                }
                int id = Integer.parseInt(userId);
                int rowsAffected = myDatabaseInstance.updateUser(id, newUserDesignation);
                    if(rowsAffected > 0) {
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        //'click' read button in code. The textview will update with new values
                        btnRead.performClick();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Could not update.Make sure the id is valid.", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = etUserId.getText().toString().trim();
                if(userId.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter an id",Toast.LENGTH_SHORT).show();
                    etUserId.requestFocus();
                    return;
                }
                int id = Integer.parseInt(userId);
               int rowsAffected =  myDatabaseInstance.deleteUser(id);
               if(rowsAffected>0){
                   Toast.makeText(MainActivity.this,"Deleted one record",Toast.LENGTH_SHORT).show();
                   btnRead.performClick();
               }
               else{
                   Toast.makeText(MainActivity.this,"No record deleted.Check whether the id is valid.",Toast.LENGTH_SHORT).show();               }

            }
        });
    }

    @Override
    protected void onDestroy() {
        //close database connection
        myDatabaseInstance.close();
        super.onDestroy();
    }
}