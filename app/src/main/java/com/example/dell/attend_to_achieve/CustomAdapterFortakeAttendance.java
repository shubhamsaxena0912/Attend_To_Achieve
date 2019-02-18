package com.example.dell.attend_to_achieve;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Achyut Saxena and Subhashni Singh
 */

public class CustomAdapterFortakeAttendance extends ArrayAdapter<String> {


    private ArrayList arrayName,arrayRegNo;
    private Context context;
    private TextView RegNo,Name;
    private RadioButton Present, Absent,Medical;
    public static int[] array = new int[100];


    public CustomAdapterFortakeAttendance(@NonNull Context context, ArrayList x, ArrayList y ){

        super(context, R.layout.listview_for_take_attendance, x);
        this.arrayName = x;
        this.arrayRegNo = y;

        for(int i= 0 ;i<100 ; i++){
            array[i]=-1;
        }

        this.context = context;

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.listview_for_take_attendance,null);

        RegNo = view.findViewById(R.id.Reg_no);
        Name = view.findViewById(R.id.name);
        Present = view.findViewById(R.id.radioPresent);
        Absent = view.findViewById(R.id.radioAbsent);
        Medical = view.findViewById(R.id.Medical);

        Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array[position]=1;
            }
        });

        Absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array[position]=0;
            }
        });

        Medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array[position]=2;
            }
        });

        RegNo.setText((String)arrayRegNo.get(position));
        Name.setText((String)arrayName.get(position));

        if(array[position]==1) Present.setChecked(true);
        else if(array[position]==0) Absent.setChecked(true);
        else if(array[position]==2)  Medical.setChecked(true);

        return view;
    }
}
