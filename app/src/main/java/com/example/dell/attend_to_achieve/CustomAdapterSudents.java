package com.example.dell.attend_to_achieve;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Achyut Saxena and Subhashni Singh
 */

public class CustomAdapterSudents extends ArrayAdapter<String> {

    private ArrayList arrayName,arrayRegNo,arrayattendance;
    private Context context;
    private TextView RegNo,Name,Attendance;
   // private ImageView imageView;


    public CustomAdapterSudents(@NonNull Context context, ArrayList x, ArrayList y, ArrayList z) {

        super(context, R.layout.students_list, x);
        this.arrayName = x;
        this.arrayRegNo = y;
        this.arrayattendance = z;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.students_list,null);

        RegNo = view.findViewById(R.id.Reg_no);
        Name = view.findViewById(R.id.name);
        Attendance = view.findViewById(R.id.attendance);
       // imageView = view.findViewById(R.id.imageView3);

        RegNo.setText((String)arrayRegNo.get(position));
        Name.setText((String)arrayName.get(position));
        Attendance.setText((String)arrayattendance.get(position));
       // imageView.setImageResource(R.drawable.ic_check_black_24dp);

        return view;
    }
}
