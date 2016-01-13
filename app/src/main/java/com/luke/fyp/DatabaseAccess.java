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
    private SQLiteDatabase database;
    private static SQLiteOpenHelper mDatabaseHelper;
    private int mOpenCounter;
    private DatabaseManagement dbManager;



    public DatabaseAccess(Context context)
    {
        super(context, DATABASE_NAME, null, 5);
        DatabaseManagement.initializeInstance(this);
        database = DatabaseManagement.getInstance().openDatabase();

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
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fatContent", fatContent);
        database.insert("meals", null, contentValues);
        return true;
    }

    public boolean addUser(int creonType, int fatPerCreon)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("creonType", creonType);
        contentValues.put("fatPerCreon", fatPerCreon);
        database.insert("users", null, contentValues);
        return true;
    }

    public boolean doesUserExist()
    {
        Cursor res =  database.rawQuery( "select * from users", null );
        res.moveToFirst();
        int count = res.getCount();

        if (count > 0)
        {
            return true;
        }

        return false;
    }


    public ArrayList<Meal> getAllMeals()
    {

        ArrayList<Meal> array_list = new ArrayList<Meal>();

        Cursor res = database.rawQuery("select * from meals", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(MEALS_COLUMN_NAME));
            int fat = res.getInt(res.getColumnIndex(MEALS_COLUMN_FATCONTENT));
            array_list.add(new Meal(name, fat));
            res.moveToNext();
        }
        return array_list;
    }

    public User getUser()
    {
        Cursor res = database.rawQuery("select * from users", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){

            return new User(res.getInt(res.getColumnIndex("creonType")), res.getInt(res.getColumnIndex("fatPerCreon")));
        }
        return null;
    }

    public boolean updateMeal (Integer id, String name, Integer fatContent)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fatContent", fatContent);
        database.update("meals", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean storeMealRecord(String name, int creonTaken, String notes, long datetime)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("creonTaken", creonTaken);
        contentValues.put("notes", notes);
        contentValues.put("dateTaken", datetime);
        database.insert("mealRecords", null, contentValues);
        return true;
    }

    public ArrayList<MealRecord> getMealRecordsFromName(String name)
    {

        Cursor res =  database.rawQuery( "select * from mealRecords where name =?", new String[] {name});
        res.moveToFirst();
        ArrayList<MealRecord> mealRecords = new ArrayList<MealRecord>();
        while(res.isAfterLast() == false){
            int creonTaken = res.getInt(res.getColumnIndex(MEALRECORDS_COLUMN_CREON));
            String notes = res.getString(res.getColumnIndex("notes"));
            long datetaken = res.getLong(res.getColumnIndex("dateTaken"));
            mealRecords.add(new MealRecord(name, creonTaken, notes, datetaken));
            res.moveToNext();
        }
        return mealRecords;
    }

    public ArrayList<MealRecord> getAllMealRecords()
    {

        Cursor res =  database.rawQuery( "select * from mealRecords", null);
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
        return mealRecords;
    }

    public ArrayList<MealRecord> getMealRecordsFromDate(long time1, long time2)
    {
        Cursor res =  database.rawQuery( "select * from mealRecords where dateTaken < " + time2 + " AND dateTaken >= " + time1, null);
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
        return mealRecords;
    }



}
