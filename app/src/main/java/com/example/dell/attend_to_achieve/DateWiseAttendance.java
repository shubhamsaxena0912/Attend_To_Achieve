package com.example.dell.attend_to_achieve;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DateWiseAttendance extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private String str;
    private TextView className;
    private ListView dateWiseList;
    private ArrayList present, absent,medical,date;
    private DBClass dbClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_attendance);

        dateWiseList = findViewById(R.id.attendanceView);
        className = findViewById(R.id.className);

        present = new ArrayList();
        absent = new ArrayList();
        medical = new ArrayList();
        date = new ArrayList();

        arrayAdapter = new CustomAdapterForDateWiseAttendance(this,present,absent,medical,date);
        dateWiseList.setAdapter(arrayAdapter);

        dbClass = new DBClass(this);

        Bundle bundle = getIntent().getExtras();

        String cls = bundle.get("className").toString();

        className.setText(cls);

        Cursor cursor = dbClass.getDate(cls);

        while(cursor.moveToNext()){
            date.add(cursor.getString(0));
            present.add(cursor.getString(1));
            absent.add(cursor.getString(2));
            medical.add(cursor.getString(3));

            arrayAdapter.notifyDataSetChanged();
        }

    }
}
