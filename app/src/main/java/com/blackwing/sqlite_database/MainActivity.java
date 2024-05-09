package com.blackwing.sqlite_database;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/** @noinspection Convert2Lambda*/
public class MainActivity extends AppCompatActivity {
    //reference to all buttons
    Button btn_add, viewAll;
    EditText editName, editAge;
    CheckBox activeCustomer;
    ListView listCustomer;
    ArrayAdapter<CustomerModel> customerAdapter;
    DBHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_add = findViewById(R.id.btn_Add);
        viewAll = findViewById(R.id.viewAll);
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        activeCustomer = findViewById(R.id.activeCustomer);
        listCustomer = findViewById(R.id.listView);
        extracted();
        btn_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CustomerModel customerModel;
               try {
                    customerModel = new CustomerModel(-1,
                           editName.getText().toString(),
                           Integer.parseInt(editAge.getText().toString()),
                           activeCustomer.isChecked());

               } catch (Exception e) {
                   Toast.makeText(MainActivity.this,"Error Creating Record",
                           Toast.LENGTH_SHORT).show();
                   customerModel = new CustomerModel(-1,"error",-1,false);
               }
               try {
                   DBHandler dbHandler = new DBHandler(MainActivity.this);
                   boolean success = dbHandler.addOne(customerModel);
                   Toast.makeText(MainActivity.this,"Success: " + success,
                           Toast.LENGTH_SHORT).show();
               } catch (Exception e) {
                   Toast.makeText(MainActivity.this,"Some Error",
                           Toast.LENGTH_SHORT).show();
               }
               extracted();
           }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                if (handler.isDataPresent()) {
                    extracted();
                } else {
                    Toast.makeText(MainActivity.this, "No Data Present", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
           }
        });

        listCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this,"Customer " + clickedCustomer.getName() + " deleted",Toast.LENGTH_SHORT).show();
                handler.deleteOne(clickedCustomer);
                extracted();
            }
        });
    }

    private void extracted() {
        handler = new DBHandler(MainActivity.this);
        customerAdapter = new ArrayAdapter<>
                (MainActivity.this, android.R.layout.simple_list_item_1, handler.getEveryone());
        listCustomer.setAdapter(customerAdapter);
    }
}