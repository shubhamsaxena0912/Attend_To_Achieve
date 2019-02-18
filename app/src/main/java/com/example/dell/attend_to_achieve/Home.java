package com.example.dell.attend_to_achieve;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.demo.CSV;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Achyut Saxena and Subhashni Singh
 */

public class Home extends AppCompatActivity {

    private ListView listView;
    private ImageButton imageButton;
    private ArrayList arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private DBClass dbClass;
    private RelativeLayout relativeLayout;
    private EditText editText;
    private EditText ClassName;
    private Button Cancelbtn;
    private Button Savebtn;
    private int j;
    private String str;
    private String[] choices = {"Delete","Upload attendance to Excel sheet","View Student's list","View Datewise attendance"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        listView = findViewById(R.id.classes);
        imageButton = findViewById(R.id.imageButton);

        arrayList = new ArrayList();
        dbClass = new DBClass(Home.this);

        final Cursor cursor = dbClass.getclasses();

        while(cursor.moveToNext()){
            arrayList.add(cursor.getString(0));
        }

        arrayAdapter = new CustomAdapterForClassesList(getApplicationContext(), arrayList);
        listView.setAdapter(arrayAdapter);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertBox = new AlertDialog.Builder(Home.this);

                LayoutInflater layoutInflater = LayoutInflater.from(Home.this);
                View view1 = layoutInflater.inflate(R.layout.alert_box_for_add_class,null);


                ClassName = view1.findViewById(R.id.Name);
                Cancelbtn = view1.findViewById(R.id.Cancelbtn);
                Savebtn = view1.findViewById(R.id.Savebtn);

                alertBox.setView(view1);

                final AlertDialog alertDialog = alertBox.create();
                alertDialog.show();

                Savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                      str = ClassName.getText().toString();

                        boolean check = true;
                        boolean checkLength = true;
                        boolean notEmpty = true;

                        if(str.length()==0) {
                            check = false;
                            notEmpty = false;
                        }

                        if(str.length()>10){
                            check = false;
                            checkLength = false;
                        }

                        for(int i=0; i<str.length(); i++){
                            if(str.charAt(i)==' '){
                                str = str.substring(0,i)+str.substring(i+1);
                                i--;
                                continue;
                            }

                            if(!((str.charAt(i)>='a' && str.charAt(i)<='z')||(str.charAt(i)>='A' && str.charAt(i)<='Z')||(str.charAt(i)>='0' && str.charAt(i)<='9'))){
                                check = false;
                                break;
                            }
                        }

                        if(!notEmpty) Toast.makeText(Home.this, "Please enter a Class Name", Toast.LENGTH_SHORT).show();
                        else if(!check && !checkLength)
                            Toast.makeText(Home.this, "Class Name can not exceed 10 characters", Toast.LENGTH_SHORT).show();

                        else if(!check){
                            Toast.makeText(Home.this, "Invalid class name!", Toast.LENGTH_SHORT).show();
                        }

                        if(check) {

                            boolean result = dbClass.insertClass(str.toUpperCase());
                            if (result) {
                                arrayList.add(str.toUpperCase());
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(Home.this, str.toUpperCase()+" inserted successfully!!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Home.this, "Duplicate Class names are not allowed", Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.cancel();

                        }
                    }
                });

                Cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String className = (String)listView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(),Students.class);
                intent.putExtra("class",className);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                 j = i;

                final String className = (String)listView.getItemAtPosition(i);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);

                alertDialog.setCancelable(true);

                View view1 = getLayoutInflater().inflate(R.layout.click_on_class,null);

                alertDialog.setView(view1);

                final AlertDialog alert = alertDialog.create();

                TextView NameOfClass = view1.findViewById(R.id.NameOfClass);
                ListView Options = view1.findViewById(R.id.options);

                NameOfClass.setText(className.toUpperCase());

                ArrayAdapter choiceAdapter = new ArrayAdapter(Home.this ,R.layout.click_on_class_listview,choices);

                Options.setAdapter(choiceAdapter);

                Options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(i==0){
                          final  boolean res =  dbClass.deleteClass(className);

                            AlertDialog.Builder deleteBox = new AlertDialog.Builder(Home.this);
                            deleteBox.setMessage(className+" will be deleted permanently. Do you want to proceed?");

                            deleteBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(res){
                                        arrayList.remove(j);
                                        arrayAdapter.notifyDataSetChanged();
                                        Toast.makeText(Home.this, className+" has been removed", Toast.LENGTH_SHORT).show();
                                        dialogInterface.cancel();

                                    }


                                    else{
                                        Toast.makeText(Home.this, className+" cannot be removed", Toast.LENGTH_SHORT).show();
                                        dialogInterface.cancel();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            deleteBox.show();

                            alert.cancel();
                        }

                        else if(i==1){

                            Cursor cursorDates = dbClass.getDate(className);

                            File sd = Environment.getExternalStorageDirectory();
                            String csvFile = className+".xls";
                            String csvFile1 = className+"TotalAttendance.xls";

                            File directory = new File(sd.getAbsolutePath()+"/Attend To Achieve/");
                            //create directory if not exist
                            if (!directory.isDirectory()) {
                                directory.mkdirs();
                            }
                            try {

                                //file path
                                File file = new File(directory, csvFile);
                                File file1 = new File(directory, csvFile1);

                                if (file.exists()){

                                    file.delete();
                                    file = new File(directory, csvFile);

                                }

                                if(file1.exists()){

                                    file1.delete();
                                    file1 = new File(directory,csvFile1);

                                }

                                WorkbookSettings wbSettings = new WorkbookSettings();
                                wbSettings.setLocale(new Locale("en", "IN"));

                                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                                WritableWorkbook workbook1 = Workbook.createWorkbook(file1,wbSettings);

                                WritableSheet sheet = workbook.createSheet(className,0);
                                WritableSheet sheet1 = workbook1.createSheet(className, 0);

                                sheet.addCell(new Label(0,0,"RegNo"));
                                sheet.addCell(new Label(1,0,"Name"));

                                sheet1.addCell(new Label(0,0,"RegNo"));
                                sheet1.addCell(new Label(1,0,"Name"));
                                sheet1.addCell(new Label(2,0,"Present"));
                                sheet1.addCell(new Label(3,0,"Medical"));
                                sheet1.addCell(new Label(4,0,"Absent"));

                                while (cursorDates.moveToNext()){
                                    String date = cursorDates.getString(0);
                                    int pos = cursorDates.getPosition()+2;

                                    sheet.addCell(new Label(pos,0,date));
                                }


                                Cursor cursorStudents = dbClass.getStudents(className);

                                while (cursorStudents.moveToNext()){

                                    String StudentName = cursorStudents.getString(1);
                                    String RegNo = cursorStudents.getString(0);
                                    cursorDates = dbClass.getDate(className);


                                    int pos = cursorStudents.getPosition()+1;

                                    sheet.addCell(new Label(0,pos,RegNo));
                                    sheet.addCell(new Label(1,pos,StudentName));

                                    sheet1.addCell(new Label(0,pos,RegNo));
                                    sheet1.addCell(new Label(1,pos,StudentName));

                                    int p = 0;
                                    int a = 0;
                                    int m = 0;

                                    while(cursorDates.moveToNext()){

                                        Cursor cursorAttendance = dbClass.getAttendanceForDate(className,RegNo,cursorDates.getString(0));


                                        if(cursorAttendance.getCount()>0) {
                                            while (cursorAttendance.moveToNext()){
                                                String attend = cursorAttendance.getString(1);

                                                if(attend.equals("0")) {
                                                    sheet.addCell(new Label(cursorDates.getPosition() + 2, pos, "A"));
                                                    a++;
                                                }

                                                else if(attend.equals("1")) {
                                                    sheet.addCell(new Label(cursorDates.getPosition() + 2, pos, "P"));
                                                    p++;
                                                }

                                                else if(attend.equals("2")) {
                                                    sheet.addCell(new Label(cursorDates.getPosition() + 2, pos, "M"));
                                                    m++;
                                                }
                                            }

                                        }

                                    }
                                     sheet1.addCell(new Label(2,pos,String.valueOf(p)));
                                     sheet1.addCell(new Label(3,pos,String.valueOf(m)));
                                     sheet1.addCell(new Label(4,pos,String.valueOf(a)));

                                    }

                                workbook.write();
                                workbook.close();

                                workbook1.write();
                                workbook1.close();


                                Toast.makeText(Home.this, "Attendance uploaded to Excel sheet!", Toast.LENGTH_SHORT).show();

                            } catch(Exception e){
                                e.printStackTrace();
                                Toast.makeText(Home.this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                            alert.cancel();

                        }

                        else if(i==2){

                            Intent intent = new Intent(Home.this,Students.class);
                            intent.putExtra("class",className);
                            alert.cancel();
                            startActivity(intent);


                        }

                        else if(i==3){
                            Intent intent = new Intent(Home.this,DateWiseAttendance.class);
                            intent.putExtra("className",className);
                            alert.cancel();
                            startActivity(intent);
                        }

                    }
                });
                alert.show();
                return true;
                }
        });

        }
    
}
