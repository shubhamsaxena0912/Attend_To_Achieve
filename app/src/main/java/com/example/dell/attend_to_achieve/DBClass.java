package com.example.dell.attend_to_achieve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Achyut Saxena and Subhashni Singh
 */

public class DBClass extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "A2A.db";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, 1);
       // SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table CLASSES(Name TEXT PRIMARY KEY);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP table if exists CLASSES");
        onCreate(sqLiteDatabase);

    }

    public boolean insertClass(String name){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);

        long res = sqLiteDatabase.insert("CLASSES",null,contentValues);

        if(res==-1) return false;

        sqLiteDatabase.execSQL("CREATE table "+"s"+name+" (Reg_No TEXT PRIMARY KEY,S_Name TEXT);");
        sqLiteDatabase.execSQL("CREATE table "+"s"+name+"Date"+"(Date TEXT PRIMARY KEY, attend TEXT, absent TEXT, medical TEXT)");

        return true;

    }

    public Cursor getclasses(){

           SQLiteDatabase sqLiteDatabase = getWritableDatabase();
           Cursor cursor = sqLiteDatabase.rawQuery("SELECT *  from CLASSES",null);
           return cursor;

    }

    public boolean deleteClass(String tableName){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+"s"+tableName,null);
        while (cursor.moveToNext()){
            String str = cursor.getColumnName(0);
            dropStudent(str,tableName);
        }
        sqLiteDatabase.execSQL("Drop table if exists "+"s"+tableName);

        sqLiteDatabase.execSQL("Drop table if exists "+"s"+tableName+"Date");

        int j = sqLiteDatabase.delete("Classes","Name=?",new String[] {tableName});
        if(j<=0){
            return false;
        }
        return true;
    }

    public boolean insertStudent(String className,String RegNo,String studentName){

        SQLiteDatabase  sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Reg_No",RegNo);
        contentValues.put("S_Name", studentName);

        long res = sqLiteDatabase.insert("s"+className,null,contentValues);

        if(res == -1)
            return false;

        RegNo = "s" + className+ RegNo;
        sqLiteDatabase.execSQL("CREATE table "+RegNo+" (date TEXT PRIMARY KEY, ATTENDANCE TEXT);");

        return true;

    }


    public Cursor getStudents(String className){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from "+"s"+className+" order by Reg_No",null);
        return cursor;
    }

    public boolean deleteStudent(String className, String regNo){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int res = sqLiteDatabase.delete("s"+className,"Reg_No=?",new String[]{regNo});

        if(res==0){
            return false;
        }

        sqLiteDatabase.execSQL("Drop table if exists "+"s"+className+regNo);
        return true;
    }

    public void dropStudent(String regNo,String className){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("Drop table if exists "+"s"+className+regNo);
    }

    public Cursor getAttendance(String RegNo,String className){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        RegNo = "s"+className+ RegNo;

        Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+RegNo,null);

        return cursor;

    }

    public boolean saveAttendance(String RegNo,String date,String attendance,String className)  {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        RegNo = "s"+ className + RegNo;

        ContentValues contentValues = new ContentValues();

        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

        try{
            date = String.valueOf(simpleDateFormat.parse(date));
        }
        catch (ParseException e){
          e.printStackTrace();
        }
          SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = simpleDateFormat1.format(date);*/

        contentValues.put("date",date);
        contentValues.put("ATTENDANCE",attendance);
        long res = sqLiteDatabase.insert(RegNo,null,contentValues);

        if (res==-1) return false;


        return true;
    }

    public boolean insertDate(String className,String date, String present, String absent, String medical ){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        className = "s"+className+"date";

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("Date",date);
        contentValues1.put("attend",present);
        contentValues1.put("absent",absent);
        contentValues1.put("medical",medical);
        long res1 = sqLiteDatabase.insert(className,null,contentValues1);

        if(res1==-1) return false;

        return true;

    }

    public Cursor getDate(String className){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        className = "s"+className+"Date" ;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from "+className,null);

        return cursor;
    }

    public Cursor getAttendanceForDate(String className, String RegNo, String Date){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        RegNo = "s"+className+RegNo;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+RegNo+" where date=?",new String[]{Date});

        return cursor;
    }


}
