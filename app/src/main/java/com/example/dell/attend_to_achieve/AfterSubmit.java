package com.example.dell.attend_to_achieve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AfterSubmit extends AppCompatActivity {

    private TextView Present,Absent,Medical,Date,ClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_submit);

        Present = findViewById(R.id.Present);
        Absent = findViewById(R.id.Absent);
        Medical = findViewById(R.id.Medical);
        Date = findViewById(R.id.Date);
        ClassName = findViewById(R.id.ClassName);

        Bundle bundle = getIntent().getExtras();

        Present.setText(bundle.get("Present").toString());
        Absent.setText(bundle.get("Absent").toString());
        Medical.setText(bundle.get("Medical").toString());
        Date.setText(bundle.get("Date").toString());
        ClassName.setText(bundle.get("ClassName").toString());

    }
}
