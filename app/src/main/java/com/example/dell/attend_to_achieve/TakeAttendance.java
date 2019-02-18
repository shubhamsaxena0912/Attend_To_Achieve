package com.example.dell.attend_to_achieve;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/**
 * Created by Achyut Saxena and Subhashni Singh
 */

public class TakeAttendance extends AppCompatActivity {

    private ListView student_list;
    private ArrayList arrayName, arrayRegNo;
    private ArrayAdapter<String> arrayAdapter;
    private DBClass dbClass;
    private String className;
    private String RegNo, Name;
    private TextView displayDate;
    private String date;
    private Button Submit;
    private int j=0;
    private int present=0, absent=0, medical=0;
    private String passDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        student_list = findViewById(R.id.attendanceList);
        displayDate = findViewById(R.id.textView2);
        Submit = findViewById(R.id.SubmitBtn);

        Bundle bundle = getIntent().getExtras();
        className = bundle.getString("ClassName");
        date = bundle.getString("Date");

        displayDate.setText(date);

        arrayName = new ArrayList();
        arrayRegNo = new ArrayList();

        dbClass = new DBClass(getApplicationContext());

        Cursor cursor = dbClass.getStudents(className.toUpperCase());

        while (cursor.moveToNext()) {

            String RegNo = cursor.getString(0);
            String Name = cursor.getString(1);

            arrayName.add(Name);
            arrayRegNo.add(RegNo);

        }

        arrayAdapter = new CustomAdapterFortakeAttendance(getApplicationContext(), arrayName, arrayRegNo);
        student_list.setAdapter(arrayAdapter);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean check = true;

                for(int i=0; i<arrayRegNo.size(); i++){

                    if(CustomAdapterFortakeAttendance.array[i]==-1) {
                        j=i;
                        check = false;
                        break;
                    }
                }

                if(check) {

                    boolean res = true;

                    for (int i = 0; i < student_list.getCount(); i++) {

                        String Str = "";

                        if (CustomAdapterFortakeAttendance.array[i]==1) {
                            Str = Integer.toString(1);
                            present++;
                        }
                        else if (CustomAdapterFortakeAttendance.array[i]==0) {
                            Str = Integer.toString(0);
                            absent++;
                        }
                        else if(CustomAdapterFortakeAttendance.array[i]==2){
                            Str = Integer.toString(2);
                            medical++;
                        }

                        boolean result = dbClass.saveAttendance((String) arrayRegNo.get(i), date, Str, className);

                        if (!result) {
                            Toast.makeText(TakeAttendance.this, "Attendance is already saved for "+date, Toast.LENGTH_SHORT).show();
                            res = false;

                        }

                    }

                    if (res)
                    {
                        boolean res1 = dbClass.insertDate(className,date,String.valueOf(present),String.valueOf(absent),String.valueOf(medical));
                       if(res1) {
                           Toast.makeText(TakeAttendance.this, "Attendance is saved", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(TakeAttendance.this, AfterSubmit.class);
                           intent.putExtra("Date", date);
                           intent.putExtra("ClassName", className);
                           intent.putExtra("Present", present);
                           intent.putExtra("Absent", absent);
                           intent.putExtra("Medical", medical);
                           startActivity(intent);

                       }

                    }
               }

                else{
                    Toast.makeText(TakeAttendance.this, "Mark attendance for "+arrayRegNo.get(j), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    
}
