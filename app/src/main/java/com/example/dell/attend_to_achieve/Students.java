package com.example.dell.attend_to_achieve;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
/**
 * Created by Achyut Saxena and Subhashni Singh
 */

public class Students extends AppCompatActivity {

    private Button mark;
    private ListView student_list;
    private TextView totalClasses;
    private FloatingActionButton addStudent;
    private ArrayList arrayName,arrayRegNo,arrayattendance;
    private ArrayAdapter<String> arrayAdapter;
    private DBClass dbClass;
    private String className;
    private int classCount=0,attendanceSum=0;
    private String RegNo , Name;
    private int year,month,day;
    private int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        mark = findViewById(R.id.mark);
        student_list = findViewById(R.id.student_list);
        totalClasses = findViewById(R.id.totalClasses);
        addStudent = findViewById(R.id.addStudent);

        arrayName = new ArrayList();
        arrayRegNo = new ArrayList();
        arrayattendance = new ArrayList();

        arrayAdapter = new CustomAdapterSudents(getApplicationContext(),arrayName,arrayRegNo,arrayattendance);
        student_list.setAdapter(arrayAdapter);

        Bundle bundle = getIntent().getExtras();
        className = bundle.getString("class");

        dbClass = new DBClass(getApplicationContext());

        Cursor cursor = dbClass.getStudents(className.toUpperCase());

        Cursor cursor1 = dbClass.getDate(className.toUpperCase());

       // classCount = cursor1.getCount();
        classCount=0;
        while (cursor1.moveToNext()){
            classCount++;
        }

        while(cursor.moveToNext()){

            String RegNo = cursor.getString(0);
            String Name = cursor.getString(1);

            attendanceSum = 0;

            Cursor cursor2 = dbClass.getAttendance(RegNo,className);

            while(cursor2.moveToNext()){

                int attend = Integer.parseInt(cursor2.getString(1));
                if(attend ==1) attendanceSum = attendanceSum + attend;

            }

            String att = String.valueOf(attendanceSum);

            arrayName.add(Name);
            arrayRegNo.add(RegNo);
            arrayattendance.add(att);
            arrayAdapter.notifyDataSetChanged();

        }

        totalClasses.setText("Total Classes: "+String.valueOf(classCount));

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View view1 = getLayoutInflater().inflate(R.layout.alert_box_for_add_student,null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Students.this);
                alertDialog.setView(view1);

                Button Cancel = view1.findViewById(R.id.Cancelbtn);
                Button Next = view1.findViewById(R.id.Nextbtn);
                final EditText Reg_No = view1.findViewById(R.id.Name);

                final AlertDialog alert = alertDialog.create();
                alert.show();

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.cancel();
                    }
                });

                Next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RegNo = Reg_No.getText().toString();
                        boolean checkLength = true;

                        boolean check = true;

                        if(RegNo.length()>10){
                            checkLength = false;
                        }

                        if(checkLength) {

                            if (Reg_No.length() == 0) check = false;

                            for (int i = 0; i < RegNo.length(); i++) {
                                if (!(RegNo.charAt(i) >= '0' && RegNo.charAt(i) <= '9')) {
                                    check = false;
                                    break;
                                }
                            }

                            if (!check) {
                                Toast.makeText(Students.this, "Enter a valid Registration Number!", Toast.LENGTH_SHORT).show();
                            }

                            if (check) {

                                alert.cancel();

                                View view2 = getLayoutInflater().inflate(R.layout.alert_box_for_student_name, null);

                                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Students.this);
                                alertDialog2.setView(view2);

                                final AlertDialog alert2 = alertDialog2.create();
                                alert2.show();

                                Button cancel = view2.findViewById(R.id.Cancelbtn);
                                Button Save = view2.findViewById(R.id.Savebtn);
                                final EditText name = view2.findViewById(R.id.Name);

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alert2.cancel();
                                    }
                                });

                                Save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Name = name.getText().toString();

                                        boolean nameCheck = true;

                                        if (Name.length() == 0) {
                                            nameCheck = false;
                                            Toast.makeText(Students.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                                        }

                                        if (nameCheck) {

                                            boolean result = dbClass.insertStudent(className.toUpperCase(), RegNo, Name);

                                            if (result) {

                                                arrayName.add(Name);
                                                arrayRegNo.add(RegNo);
                                                arrayattendance.add("0");
                                                arrayAdapter.notifyDataSetChanged();

                                                Toast.makeText(Students.this, RegNo + " added successfully!!!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Students.this, "Duplicate Registration Number is not allowed", Toast.LENGTH_SHORT).show();
                                            }

                                            alert2.cancel();

                                        }

                                    }
                                });
                            }

                        }

                        else{
                            Toast.makeText(Students.this, "Registration Number cannot contain more than 10 digits", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Students.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        String date = String.valueOf(i2)+"-"+String.valueOf(i1+1)+"-"+String.valueOf(i);
                        Intent intent = new Intent(Students.this,TakeAttendance.class);
                        intent.putExtra("ClassName",className);
                        intent.putExtra("Date",date);
                        //intent.putExtra("passDate",passDate);
                        startActivity(intent);
                    }
                },year,month,day);
                 datePickerDialog.show();
            }
        });


        student_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                j = i;

                View view1 = student_list.getAdapter().getView(i,null,null);
                TextView textView = view1.findViewById(R.id.Reg_no);
                final String reg = textView.getText().toString();

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Students.this);

                alertDialog.setMessage("Are you sure you want to delete "+reg+"?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean res =  dbClass.deleteStudent(className,reg);

                        if(res){
                            arrayName.remove(j);
                            arrayRegNo.remove(j);
                            arrayattendance.remove(j);
                            arrayAdapter.notifyDataSetChanged();
                            Toast.makeText(Students.this,
                                    reg+" has been removed", Toast.LENGTH_SHORT).show();
                        }


                        else{
                            Toast.makeText(Students.this,
                                    reg+" cannot be removed", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertDialog.show();
                return true;

            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        Cursor cursor = dbClass.getStudents(className.toUpperCase());


        Cursor cursor1 = dbClass.getDate(className.toUpperCase());

        classCount = 0;

        //classCount = cursor1.getCount();
        while(cursor1.moveToNext()){
            classCount++;
        }



        arrayName = new ArrayList();
        arrayRegNo = new ArrayList();
        arrayattendance = new ArrayList();

        arrayAdapter = new CustomAdapterSudents(getApplicationContext(),arrayName,arrayRegNo,arrayattendance);
        student_list.setAdapter(arrayAdapter);


        while(cursor.moveToNext()){

            String RegNo = cursor.getString(0);
            String Name = cursor.getString(1);

            attendanceSum = 0;

            Cursor cursor2 = dbClass.getAttendance(RegNo,className);

            while(cursor2.moveToNext()){

                int attend = Integer.parseInt(cursor2.getString(1));
                if(attend == 1)attendanceSum = attendanceSum + attend;
            }

            String att = String.valueOf(attendanceSum);

            arrayName.add(Name);
            arrayRegNo.add(RegNo);
            arrayattendance.add(att);
            arrayAdapter.notifyDataSetChanged();

        }



        totalClasses.setText("Total Classes: "+String.valueOf(classCount));
    }

}
