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
public class DatabaseAccess extends SQLiteOpenHelper {

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


    public DatabaseAccess(Context context) {
        super(context, DATABASE_NAME, null, 10);
        DatabaseManagement.initializeInstance(this);
        database = DatabaseManagement.getInstance().openDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Meals table

        db.execSQL(
                "create table users" +
                        "(id integer primary key, " +
                        "creonType integer, " +
                        "fatPerCreon double)"
        );

        db.execSQL(
                "create table combinations" +
                        "(id integer primary key, " +
                        "name text, " +
                        "fatContent double)"
        );

        db.execSQL(
                "create table entries" +
                        "(id integer primary key, " +
                        "dateTaken date, " +
                        "creonTaken integer, " +
                        "result integer," +
                        "name text, " +
                        "notes text)"
        );

        db.execSQL(
                "create table components" +
                        "(id integer primary key, " +
                        "servingtype text, " +
                        "name text, " +
                        "fatPerServing double)"
        );

        db.execSQL(
                "create table componentcombinations" +
                        "(componentID integer, " +
                        "combinationID integer, " +
                        "quantity double, " +
                        "primary key(componentID, combinationID)" +
                        "foreign key (componentID) references components(id)" +
                        "foreign key (combinationID) references combinations(id))"
        );


        db.execSQL(
                "create table entrycombinations" +
                        "(entryID integer, " +
                        "combinationID integer, " +
                        "quantity double, " +
                        "primary key(entryID, combinationID)" +
                        "foreign key (entryID) references entries(id)" +
                        "foreign key (combinationID) references combinations(id))"
        );


        db.execSQL(
                "create table entrycomponents" +
                        "(entryID integer, " +
                        "componentID integer, " +
                        "quantity double, " +
                        "primary key(entryID, componentID)" +
                        "foreign key (entryID) references entries(id)" +
                        "foreign key (componentID) references component(id))"
        );


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS meals");
        db.execSQL("DROP TABLE IF EXISTS mealRecords");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS components");
        db.execSQL("DROP TABLE IF EXISTS entrycomponents");
        db.execSQL("DROP TABLE IF EXISTS componentcombinations");
        db.execSQL("DROP TABLE IF EXISTS entrycombinations");
        db.execSQL("DROP TABLE IF EXISTS entries");
        db.execSQL("DROP TABLE IF EXISTS combinations");
        onCreate(db);
    }

    public boolean addUser(int creonType, double fatPerCreon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("creonType", creonType);
        contentValues.put("fatPerCreon", fatPerCreon);
        database.insert("users", null, contentValues);
        return true;
    }

    public boolean doesUserExist() {
        Cursor res = database.rawQuery("select * from users", null);
        res.moveToFirst();
        int count = res.getCount();

        if (count > 0) {
            return true;
        }
        res.close();
        return false;
    }

    public User getUser() {
        Cursor res = database.rawQuery("select * from users", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            return new User(res.getInt(res.getColumnIndex("creonType")), res.getDouble(res.getColumnIndex("fatPerCreon")));
        }
        res.close();
        return null;
    }

    public boolean updateMeal(Integer id, String name, double fatContent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fatContent", fatContent);
        database.update("meals", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    // new methods
    public boolean insertComponent(String name, double fatPerServing, String servingType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("servingtype", servingType);
        contentValues.put("fatPerServing", fatPerServing);
        database.insert("components", null, contentValues);
        return true;
    }

    public boolean insertCombination(String name, double fatContent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fatContent", fatContent);
        database.insert("combinations", null, contentValues);
        return true;
    }

    public boolean insertEntry(int creonTaken, String notes, long datetime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("creonTaken", creonTaken);
        contentValues.put("notes", notes);
        contentValues.put("dateTaken", datetime);
        database.insert("entries", null, contentValues);
        return true;

    }

    public boolean addComponentCombination(int compID, int combID, double quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("componentID", compID);
        contentValues.put("combinationID", combID);
        contentValues.put("quantity", quantity);
        database.insert("componentcombinations", null, contentValues);
        return true;
    }

    public boolean addEntryCombination(int entryID, int combID, double quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("entryID", entryID);
        contentValues.put("combinationID", combID);
        contentValues.put("quantity", quantity);
        database.insert("entrycombinations", null, contentValues);
        return true;
    }

    public boolean addEntryComponent(int compID, int entryID, double quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("componentID", compID);
        contentValues.put("entryID", entryID);
        contentValues.put("quantity", quantity);
        database.insert("entrycomponents", null, contentValues);
        return true;
    }

    public ArrayList<Entry> getEntriesFromName(String name) {

        Cursor res = database.rawQuery("select * from entries where id IN (select entryID from entrycomponents where name = ? group by entryID)", new String[]{name});
        res.moveToFirst();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        while (res.isAfterLast() == false) {
            int creonTaken = res.getInt(res.getColumnIndex(MEALRECORDS_COLUMN_CREON));
            String notes = res.getString(res.getColumnIndex("notes"));
            long datetaken = res.getLong(res.getColumnIndex("dateTaken"));
            entries.add(new Entry(name, creonTaken, notes, datetaken));
            res.moveToNext();
        }
        res.close();
        return entries;
    }

    public ArrayList<Entry> getAllEntries() {

        ArrayList<Entry> comps = new ArrayList<>();
        Cursor res = database.rawQuery("select * from entries ", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            int entryID = res.getInt(res.getColumnIndex("id"));
            long date = res.getLong(res.getColumnIndex("dateTaken"));
            int creon = res.getInt(res.getColumnIndex("creonTaken"));
            String notes = res.getString(res.getColumnIndex("notes"));
            Entry entry = new Entry("", creon, notes, date);
            entry.setId(entryID);
            comps.add(entry);
            res.moveToNext();
        }

        for(int i = 0; i < comps.size(); i++)
        {
            ArrayList<Component> comps1 = new ArrayList<>();
            int thisID = comps.get(i).getId();
            res = database.rawQuery("select components.name, components.servingtype, components.fatPerServing, components.id, entrycomponents.quantity from components inner join entrycomponents on components.id = entrycomponents.componentID where entrycomponents.entryID = ?", new String[] {""+thisID});
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String name = res.getString(res.getColumnIndex("name"));
                String type = res.getString(res.getColumnIndex("servingtype"));
                double fat = res.getDouble(res.getColumnIndex("fatPerServing"));
                int myid = res.getInt(res.getColumnIndex("id"));
                double quantity = res.getDouble(res.getColumnIndex("quantity"));
                Component component = new Component(name, fat, quantity, myid, type);
                comps1.add(component);
                res.moveToNext();

            }

            comps.get(i).setComponents(comps1);
        }

        for(int i = 0; i < comps.size(); i++) {
            ArrayList<Combination> combs1 = new ArrayList<>();
            int thisID = comps.get(i).getId();
            res = database.rawQuery("select combinations.name, combinations.id, entrycombinations.quantity from combinations inner join entrycombinations on combinations.id = entrycombinations.combinationID where entryID = ?", new String[]{"" + thisID});
            //res = database.rawQuery("select * from combinations where id = (select combinationID from entrycombinations where entryID = ?)", new String[]{"" + thisID});
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String name = res.getString(res.getColumnIndex(MEALS_COLUMN_NAME));
                int myid = res.getInt(res.getColumnIndex("id"));
                double quantity = res.getDouble(res.getColumnIndex("quantity"));
                Combination combination = new Combination();
                combination.setId(myid);
                combination.setName(name);
                combination.setQuantity(quantity);
                combs1.add(combination);

                res.moveToNext();

            }
            comps.get(i).setCombinations(combs1);
        }
        res.close();
        return comps;
    }

    public ArrayList<Entry> getMealRecordsFromDate1(long time1, long time2) {
        Cursor res = database.rawQuery("select * from entries where dateTaken < " + time2 + " AND dateTaken >= " + time1, null);
        res.moveToFirst();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        while (res.isAfterLast() == false) {
            int creonTaken = res.getInt(res.getColumnIndex(MEALRECORDS_COLUMN_CREON));
            String notes = res.getString(res.getColumnIndex("notes"));
            long datetaken = res.getLong(res.getColumnIndex("dateTaken"));
            String name = res.getString(res.getColumnIndex("name"));
            entries.add(new Entry(name, creonTaken, notes, datetaken));
            res.moveToNext();
        }
        res.close();
        return entries;
    }

    public ArrayList<Combination> getAllCombinations() {

        ArrayList<Combination> array_list = new ArrayList<>();

        Cursor res = database.rawQuery("select * from combinations", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String name = res.getString(res.getColumnIndex("name"));
            double fat = res.getDouble(res.getColumnIndex("fatContent"));
            int id = res.getInt(res.getColumnIndex("id"));
            Combination combination = new Combination();
            combination.setId(id);
            combination.setName(name);
            combination.setFatContent(fat);
            array_list.add(combination);
            res.moveToNext();
        }


        for (int i = 0; i < array_list.size(); i++) {
            ArrayList<Component> comps = new ArrayList<>();
            res = database.rawQuery("select components.name, components.servingtype, components.fatPerServing, components.id, componentcombinations.quantity from components inner join componentcombinations on components.id = componentcombinations.componentID where combinationID = ?", new String[]{"" + array_list.get(i).getID()});
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String name = res.getString(res.getColumnIndex("name"));
                String type = res.getString(res.getColumnIndex("servingtype"));
                double fat = res.getDouble(res.getColumnIndex("fatPerServing"));
                int id = res.getInt(res.getColumnIndex("id"));
                double quantity = res.getDouble(res.getColumnIndex("quantity"));
                Component component = new Component(name, fat, quantity, id, type);
                comps.add(component);
                res.moveToNext();

            }
            array_list.get(i).setComponents(comps);
        }
        res.close();
        return array_list;
    }

    public ArrayList<Component> getAllComponents() {

        ArrayList<Component> comps = new ArrayList<>();
        Cursor res = database.rawQuery("select * from components", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String name = res.getString(res.getColumnIndex("name"));
            String type = res.getString(res.getColumnIndex("servingtype"));
            double fat = res.getDouble(res.getColumnIndex("fatPerServing"));
            int id = res.getInt(res.getColumnIndex("id"));
            Component component = new Component(name, fat, 0, id, type);
            comps.add(component);
            res.moveToNext();
        }
        res.close();
        return comps;

    }

    public int getIDForRecentEntry() {
        Cursor res = database.rawQuery("select id from entries where dateTaken = (select max(dateTaken) from entries)", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            return (res.getInt(res.getColumnIndex("id")));
        }
        res.close();
        return 0;
    }

    public int getIDFromNameCombination(String name){
        Cursor res = database.rawQuery("select id from combinations where name = ?",new String[] {name});
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            return (res.getInt(res.getColumnIndex("id")));
        }
        res.close();
        return 0;
    }

    public ArrayList<Entry> getEntriesFromComponentID(int id)
    {
        ArrayList<Entry> comps = new ArrayList<>();
        Cursor res = database.rawQuery("select * from entries where id = (select entryID from entrycomponents where componentID = ?", new String[] {""+id});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            int entryID = res.getInt(res.getColumnIndex("id"));
            long date = res.getLong(res.getColumnIndex("dateTaken"));
            int creon = res.getInt(res.getColumnIndex("creaonTaken"));
            String notes = res.getString(res.getColumnIndex("id"));
            Entry entry = new Entry("", creon, notes, date);
            comps.add(entry);
            res.moveToNext();
        }

        for(int i = 0; i < comps.size(); i++)
        {
            ArrayList<Component> comps1 = new ArrayList<>();
            int thisID = comps.get(i).getId();
            res = database.rawQuery("select components.name, components.servingtype, components.fatPerServing, components.id, entrycomponents.quantity from components inner join entrycomponents on components.id = entrycomponents.componentID where entrycomponents.entryID = ?", new String[] {""+thisID});
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String name = res.getString(res.getColumnIndex("name"));
                String type = res.getString(res.getColumnIndex("servingtype"));
                double fat = res.getDouble(res.getColumnIndex("fatPerServing"));
                int myid = res.getInt(res.getColumnIndex("id"));
                double quantity = res.getDouble(res.getColumnIndex("quantity"));
                Component component = new Component(name, fat, quantity, myid, type);
                comps1.add(component);
                res.moveToNext();

            }

            comps.get(i).setComponents(comps1);
        }

        for(int i = 0; i < comps.size(); i++) {
            ArrayList<Combination> combs1 = new ArrayList<>();
            int thisID = comps.get(i).getId();
            res = database.rawQuery("select * from combinations where id = (select combinationID from entrycombinations where entryID = ?", new String[]{"" + thisID});
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String name = res.getString(res.getColumnIndex(MEALS_COLUMN_NAME));
                double fat = res.getDouble(res.getColumnIndex(MEALS_COLUMN_FATCONTENT));
                int myid = res.getInt(res.getColumnIndex("id"));
                Combination combination = new Combination();
                combination.setName(name);
                combination.setFatContent(fat);
                combs1.add(combination);

                res.moveToNext();

            }
            comps.get(i).setCombinations(combs1);
        }
        res.close();
        return comps;
    }

    public ArrayList<Entry> getEntriesFromCombinationID(int id)
    {
        return null;
    }

}
