package com.luke.fyp;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Luke on 21/09/2015.
 */
public class DatabaseAccess extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String MEALS_TABLE_NAME = "meals";
    public static final String MEALS_COLUMN_ID = "id";
    public static final String MEALS_COLUMN_NAME = "name";
    public static final String MEALS_COLUMN_FATCONTENT = "fatContent";
    public static final String MEALRECORDS_TABLE_NAME = "mealRecords";
    public static final String MEALRECORDS_COLUMN_ID = "id";
    public static final String MEALRECORDS_COLUMN_NAME = "name";
    public static final String MEALRECORDS_COLUMN_DATE = "dateTaken";
    public static final String MEALRECORDS_COLUMN_CREON = "creonTaken";



    public DatabaseAccess(Context context)
    {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Meals table
        db.execSQL(
                "create table meals" +
                        "(id integer primary key, " +
                        "name text, " +
                        "fatContent integer)"
        );
        // Create Meal Record table
        db.execSQL(
                "create table mealRecords" +
                        "(id integer primary key, " +
                        "dateTaken date, " +
                        "name text, " +
                        "creonTaken integer, " +
                        "result integer," +
                        "notes text," +
                        "foreign key (name) references meals(name))"
        );

        db.execSQL(
                "create table users" +
                        "(id integer primary key, " +
                        "creonType integer, " +
                        "fatPerCreon integer)"
        );
/*
        db.execSQL(
                "create table mealFood" +
                        "(recordID integer, " +
                        "mealID integer, " +
                        "primary key(recordID, mealID)" +
                        "foreign key (recordID) references mealRecords(id)" +
                        "foreign key (mealID) references meals(id))"
        );*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS meals");
        db.execSQL("DROP TABLE IF EXISTS mealRecords");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertMeal(String name, Integer fatContent)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fatContent", fatContent);
        db.insert("meals", null, contentValues);
        db.close();
        return true;
    }

    public boolean addUser(int creonType, int fatPerCreon)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("creonType", creonType);
        contentValues.put("fatPerCreon", fatPerCreon);
        db.insert("users", null, contentValues);
        db.close();
        return true;
    }

    public boolean doesUserExist()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();
        int count = res.getCount();

        if (count > 0)
        {
            db.close();
            return true;
        }

        db.close();
        return false;
    }


    public ArrayList<Meal> getAllMeals()
    {

        ArrayList<Meal> array_list = new ArrayList<Meal>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from meals", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(MEALS_COLUMN_NAME));
            int fat = res.getInt(res.getColumnIndex(MEALS_COLUMN_FATCONTENT));
            array_list.add(new Meal(name, fat));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    public User getUser()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from users", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){

            db.close();
            return new User(res.getInt(res.getColumnIndex("creonType")), res.getInt(res.getColumnIndex("fatPerCreon")));
        }
        db.close();
        return null;
    }

    public boolean updateMeal (Integer id, String name, Integer fatContent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fatContent", fatContent);
        db.update("meals", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    public boolean storeMealRecord(String name, int creonTaken, String notes, long datetime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("creonTaken", creonTaken);
        contentValues.put("notes", notes);
        contentValues.put("dateTaken", datetime);
        db.insert("mealRecords", null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<MealRecord> getMealRecordsFromName(String name)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from mealRecords where name =?", new String[] {name});
        res.moveToFirst();
        ArrayList<MealRecord> mealRecords = new ArrayList<MealRecord>();
        while(res.isAfterLast() == false){
            int creonTaken = res.getInt(res.getColumnIndex(MEALRECORDS_COLUMN_CREON));
            String notes = res.getString(res.getColumnIndex("notes"));
            long datetaken = res.getLong(res.getColumnIndex("dateTaken"));
            mealRecords.add(new MealRecord(name, creonTaken, notes, datetaken));
            res.moveToNext();
        }
        db.close();
        return mealRecords;
    }

    public ArrayList<MealRecord> getAllMealRecords()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from mealRecords", null);
        res.moveToFirst();
        ArrayList<MealRecord> mealRecords = new ArrayList<MealRecord>();
        while(res.isAfterLast() == false){
            int creonTaken = res.getInt(res.getColumnIndex(MEALRECORDS_COLUMN_CREON));
            String notes = res.getString(res.getColumnIndex("notes"));
            String name = res.getString(res.getColumnIndex("name"));
            long datetaken = res.getLong(res.getColumnIndex("dateTaken"));
            mealRecords.add(new MealRecord(name, creonTaken, notes, datetaken));
            res.moveToNext();
        }
        db.close();
        return mealRecords;
    }

    public ArrayList<MealRecord> getMealRecordsFromDate(long time1, long time2)
    {
        //dateTaken >= " + time1 + " AND
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from mealRecords where dateTaken < " + time2 + " AND dateTaken >= " + time1, null);
        res.moveToFirst();
        ArrayList<MealRecord> mealRecords = new ArrayList<MealRecord>();
        while(res.isAfterLast() == false){
            int creonTaken = res.getInt(res.getColumnIndex(MEALRECORDS_COLUMN_CREON));
            String notes = res.getString(res.getColumnIndex("notes"));
            long datetaken = res.getLong(res.getColumnIndex("dateTaken"));
            String name = res.getString(res.getColumnIndex("name"));
            mealRecords.add(new MealRecord(name, creonTaken, notes, datetaken));
            res.moveToNext();
        }
        db.close();
        return mealRecords;
    }


}
