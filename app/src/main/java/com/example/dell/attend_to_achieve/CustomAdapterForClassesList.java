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

import com.example.dell.attend_to_achieve.R;

import java.util.ArrayList;

public class CustomAdapterForClassesList extends ArrayAdapter<String> {

    private TextView className;
    private Context context;
    private ArrayList arrayClass;

    public CustomAdapterForClassesList(@NonNull Context context, ArrayList x) {
        super(context, R.layout.classes_list,x);
        this.arrayClass = x;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.classes_list,null);

        ImageView imageView = view.findViewById(R.id.imageView6);
        className = view.findViewById(R.id.className);

        className.setText(arrayClass.get(position).toString());
        imageView.setImageResource(R.drawable.ic_class_black_24dp);

        return view;
    }
}
