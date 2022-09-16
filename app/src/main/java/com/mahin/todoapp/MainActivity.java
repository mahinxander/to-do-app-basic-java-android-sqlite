package com.mahin.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private EditText e;
    private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(this);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = helper.getAllAcontacts();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.WHITE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                return view;
            }
        };
        lvItems.setAdapter(itemsAdapter);
        e = (EditText)findViewById(R.id.etNewItem);
//        Log.d("MahinLog","my first logcat");
        Button add = (Button)findViewById(R.id.btnAddItem);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!e.getText().toString().isEmpty()) {
                    if (helper.insert(e.getText().toString())){
                        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    e.setError("Enter To Do List Item");

                }
                items.clear();
                items.addAll(helper.getAllAcontacts());
                itemsAdapter.notifyDataSetChanged();
                lvItems.invalidateViews();
                lvItems.refreshDrawableState();
                e.setText("");
            }
        });

        //items.add("First Item");
        //items.add("Second Item");
        setupListViewListener();

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        dialogBuilder.setTitle("DELETE OR UPDATE");
                        dialogBuilder.setMessage("Write in text edit box and the tap on UPDATE to update value or simply tap on DELETE to delete value.");
                        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(!e.getText().toString().isEmpty()) {
                                    if (helper.update(e.getText().toString(), pos)){
                                        Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    e.setError("Enter To Do List Item");
                                }
                                items.clear();
                                items.addAll(helper.getAllAcontacts());
                                itemsAdapter.notifyDataSetChanged();
                                lvItems.invalidateViews();
                                lvItems.refreshDrawableState();
                                e.setText("");

                            }
                        });

                        dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(pos);

                                helper.deleteItem(pos);

                                itemsAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();

                        return true;

                    }
                });

    }



}