package com.example.dell.attend_to_achieve;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterForDateWiseAttendance extends ArrayAdapter<String> {

    private ArrayList present,absent,medical,date;
    private Context context;
    private TextView Date,Present,Absent,Medical;


    public CustomAdapterForDateWiseAttendance(@NonNull Context context,ArrayList x, ArrayList y,ArrayList z, ArrayList w) {

        super(context,R.layout.datewise_attendance,x);

        this.present = x;
        this.absent = y;
        this.medical = z;
        this.date = w;
        this.context = context;
        }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view1 = layoutInflater.inflate(R.layout.datewise_attendance,null);

        Present = view1.findViewById(R.id.Present);
        Absent = view1.findViewById(R.id.Absent);
        Medical = view1.findViewById(R.id.Medical);
        Date =  view1.findViewById(R.id.Date);

        Present.setText(present.get(position).toString());
        Absent.setText(absent.get(position).toString());
        Medical.setText(medical.get(position).toString());
        Date.setText(date.get(position).toString());

        return view1;

    }
}
