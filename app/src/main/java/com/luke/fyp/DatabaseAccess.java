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
        super(context, DATABASE_NAME, null, 18);
        DatabaseManagement.initializeInstance(this);
        database = DatabaseManagement.getInstance().openDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Meals table

        db.execSQL(
                "create table users" +
                        "(id integer primary key, " +
                        "creonType text, " +
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
                        "creon10000Taken integer, " +
                        "creon25000Taken integer, " +
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

    public boolean addUser(String creonType, double fatPerCreon) {
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

    /*public void loadDb(){
        database.execSQL("INSERT INTO `components` VALUES (1,'Grams','Spaghetti',1.0);");
                database.execSQL("INSERT INTO `components` VALUES (2,'Grams','Beef Mince ',5.0);");
                        "INSERT INTO `components` VALUES (3,'Unit','Tinned tomatoes ',0.0);");
                        "INSERT INTO `components` VALUES (4,'Grams','Parmesan Cheese ',60.0);");
                        "INSERT INTO `components` VALUES (5,'Millilitres','Milk',3.5);");
                        "INSERT INTO `components` VALUES (6,'Slices','Ham',1.0);");
                        "INSERT INTO `components` VALUES (7,'Slices','Bread ',1.0);");
                        "INSERT INTO `components` VALUES (8,'Slices','Cheese Large Slice',8.0);");
                        "INSERT INTO `components` VALUES (9,'Oz','Butter',23.0);");
                        "INSERT INTO `components` VALUES (10,'Grams','Chicken Breast',4.0);");
                        "INSERT INTO `components` VALUES (11,'Unit','Red Pepper',0.0);");
                        "INSERT INTO `components` VALUES (12,'Unit','Onion',0.0);");
                        "INSERT INTO `components` VALUES (13,'Millilitres','Curry Sauce',3.0);");
                        "INSERT INTO `components` VALUES (14,'Grams','Rice',0.0);");
                        "INSERT INTO `components` VALUES (15,'Unit','McDonald''s Quarter Pounder',25.0);");
                        "INSERT INTO `components` VALUES (16,'Unit','McDonald''s Medium Chips',19.0);");
                        "INSERT INTO `components` VALUES (17,'Millilitres','Diet Coke',0.0);");
                        "INSERT INTO `components` VALUES (18,'Grams','Oven Chips',5.0);");
                        "INSERT INTO `components` VALUES (19,'Grams','Sirloin Steak',13.0);");
                        "INSERT INTO `components` VALUES (20,'Grams','Roast Potatos',5.0);");
                        "INSERT INTO `components` VALUES (21,'Grams','Roast Carrots',2.0);");
                        "INSERT INTO `components` VALUES (22,'Millilitres','Gravy',0.0);");
                        "INSERT INTO `components` VALUES (23,'Millilitres','Scandi shake',10.0);");
                        "INSERT INTO `components` VALUES (24,'Unit','Tayto Small Pack',8.0);");
                        "INSERT INTO `components` VALUES (25,'Unit','Nutrigrain chocolate ',4.5);");
                        "INSERT INTO `components` VALUES (26,'Unit','Crispy Chilli Chicken Fried Rice',40.0);");
                        "INSERT INTO `components` VALUES (27,'Unit','Hot Chicken Roll',20.0);");
                        "INSERT INTO `combinations` VALUES (1,'Spaghetti Bolognese',0.0);");
                        "INSERT INTO `combinations` VALUES (2,'Hot Chicken Roll',0.0);");
                        "INSERT INTO `combinations` VALUES (3,'Steak and Potatoes ',0.0);");
                        "INSERT INTO `combinations` VALUES (4,'Chicken Curry',0.0);");
                        "INSERT INTO `combinations` VALUES (5,'Scandi shake and Tayto',0.0);");
                        "INSERT INTO `combinations` VALUES (6,'McDonalds Meal',0.0);");
                        "INSERT INTO `combinations` VALUES (7,'Ham and Cheese Toastie',0.0);");
                        "INSERT INTO `combinations` VALUES (8,'Nutrigrain and milk',0.0);");
                        "INSERT INTO `combinations` VALUES (9,'Crispy Chilli Chicken',0.0);");
                        "INSERT INTO `componentcombinations` VALUES (1,1,200.0);");
                        "INSERT INTO `componentcombinations` VALUES (2,1,400.0);");
                        "INSERT INTO `componentcombinations` VALUES (4,1,40.0);");
                        "INSERT INTO `componentcombinations` VALUES (3,1,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (27,2,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (19,3,300.0);");
                        "INSERT INTO `componentcombinations` VALUES (9,3,0.4);");
                        "INSERT INTO `componentcombinations` VALUES (21,3,100.0);");
                        "INSERT INTO `componentcombinations` VALUES (20,3,200.0);");
                        "INSERT INTO `componentcombinations` VALUES (22,3,150.0);");
                        "INSERT INTO `componentcombinations` VALUES (10,4,400.0);");
                        "INSERT INTO `componentcombinations` VALUES (13,4,400.0);");
                        "INSERT INTO `componentcombinations` VALUES (11,4,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (12,4,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (14,4,400.0);");
                        "INSERT INTO `componentcombinations` VALUES (18,4,300.0);");
                        "INSERT INTO `componentcombinations` VALUES (23,5,300.0);");
                        "INSERT INTO `componentcombinations` VALUES (24,5,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (15,6,2.0);");
                        "INSERT INTO `componentcombinations` VALUES (16,6,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (17,6,200.0);");
                        "INSERT INTO `componentcombinations` VALUES (6,7,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (8,7,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (7,7,2.0);");
                        "INSERT INTO `componentcombinations` VALUES (9,7,0.6);");
                        "INSERT INTO `componentcombinations` VALUES (25,8,1.0);");
                        "INSERT INTO `componentcombinations` VALUES (5,8,300.0);");
                        "INSERT INTO `componentcombinations` VALUES (26,9,1.0);");
                        "INSERT INTO `entries` VALUES (1,1458140434934,1,2,1,NULL,'Pop');");
                        "INSERT INTO `entries` VALUES (2,1458747798727,0,2,0,NULL,'None');");
                        "INSERT INTO `entries` VALUES (3,1458747835009,0,3,0,NULL,'None');");
                        "INSERT INTO `entries` VALUES (4,1458747871894,2,4,0,NULL,'None');");
                        "INSERT INTO `entries` VALUES (5,1458640800000,1,3,0,NULL,' one');");
                        "INSERT INTO `entries` VALUES (6,1458640800000,0,3,0,NULL,'');");
                        "INSERT INTO `entries` VALUES (7,1458640800000,0,1,0,NULL,'');");
                        "INSERT INTO `entries` VALUES (8,1458554400000,1,5,0,NULL,'');");
                        "INSERT INTO `entries` VALUES (9,1458554400000,2,1,0,NULL,'');");
                        "INSERT INTO `entries` VALUES (10,1458554400000,1,2,0,NULL,NULL);");
                        "INSERT INTO `entries` VALUES (11,1458468000000,1,2,0,NULL,NULL);");
                        "INSERT INTO `entries` VALUES (12,1458468000000,0,2,0,NULL,NULL);");
                        "INSERT INTO `entries` VALUES (13,1458468000000,0,3,0,NULL,NULL);");
                        "INSERT INTO `entries` VALUES (14,1458381600000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (15,1458381600000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (16,1458381600000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (17,1458295200000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (18,1458295200000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (19,1458295200000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (20,1458208800000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (21,1458208800000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (22,1458208800000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (23,1458122400000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (24,1458122400000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (25,1458122400000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (26,1458036000000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (27,1458036000000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (28,1458036000000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (29,1457949600000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (30,1457949600000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (31,1457949600000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (32,1457949600000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (33,1457863200000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (34,1457863200000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (35,1457863200000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (36,1457863200000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (37,1457776800000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (38,1457776800000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (39,1457776800000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (40,1457776800000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (41,1457690400000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (42,1457690400000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (43,1457690400000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (44,1457690400000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (45,1457604000000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (46,1457604000000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (47,1457604000000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (48,1457604000000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (49,1457517600000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (50,1457517600000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (51,1457517600000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (52,1457517600000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (53,1457431200000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (54,1457431200000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (55,1457431200000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (56,1457431200000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (57,1457344800000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (58,1457344800000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (59,1457344800000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (60,1457344800000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (61,1457258400000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (62,1457258400000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (63,1457258400000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (64,1457258400000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (65,1457258400000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (66,1457258400000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (67,1457258400000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (68,1457258400000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (69,1457085600000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (70,1457085600000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (71,1457085600000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (72,1457085600000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (73,1456999200000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (74,1456999200000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (75,1456999200000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (76,1456999200000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (77,1456912800000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (78,1456912800000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (79,1456912800000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (80,1456912800000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (81,1456826400000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (82,1456826400000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (83,1456826400000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (84,1456826400000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (85,1458640800000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (86,1458554400000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (87,1458468000000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (88,1458381600000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (89,1456740000000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (90,1456740000000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (91,1456740000000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (92,1456740000000,0,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (93,1456653600000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (94,1456653600000,2,4,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (95,1456653600000,1,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (96,1456653600000,0,3,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (97,1456567200000,0,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (98,1456567200000,1,5,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (99,1456567200000,2,1,0,NULL,NULL);\n" +
                        "INSERT INTO `entries` VALUES (100,1456567200000,1,2,0,NULL,NULL);\n" +
                        "INSERT INTO `entrycombinations` VALUES (1,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (2,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (3,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (4,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (5,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (6,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (7,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (8,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (9,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (10,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (11,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (12,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (13,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (14,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (15,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (16,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (17,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (18,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (19,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (20,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (21,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (22,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (23,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (24,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (25,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (26,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (27,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (28,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (29,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (30,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (31,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (32,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (33,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (34,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (35,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (36,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (37,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (38,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (39,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (40,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (41,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (42,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (43,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (44,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (45,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (46,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (47,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (48,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (49,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (50,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (51,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (52,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (53,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (54,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (55,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (56,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (57,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (58,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (59,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (60,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (61,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (62,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (63,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (64,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (65,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (66,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (67,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (68,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (69,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (70,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (71,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (72,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (73,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (74,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (75,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (76,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (77,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (78,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (79,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (80,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (81,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (82,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (83,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (84,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (85,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (86,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (87,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (88,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (89,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (90,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (91,1,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (92,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (93,7,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (94,3,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (95,9,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (96,5,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (97,8,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (98,6,1.0);\n" +
                        "INSERT INTO `entrycombinations` VALUES (99,4,0.5);\n" +
                        "INSERT INTO `entrycombinations` VALUES (100,1,0.5);\n");
    }*/

    public User getUser() {
        Cursor res = database.rawQuery("select * from users", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            return new User(res.getString(res.getColumnIndex("creonType")), res.getDouble(res.getColumnIndex("fatPerCreon")));
        }
        res.close();
        return null;
    }

    public boolean removeComponentCombination(int componentID, int combinationID)
    {
        return database.delete("componentcombinations","componentID=? and combinationID=?",new String[]{""+componentID,""+combinationID}) > 0;
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

    public boolean insertEntry(int creon10000Taken, int creon25000Taken, String notes, long datetime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("creon10000Taken", creon10000Taken);
        contentValues.put("creon25000Taken", creon25000Taken);
        contentValues.put("notes", notes);
        contentValues.put("dateTaken", datetime);
        contentValues.put("result", 0);
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

    public ArrayList<Entry> getAllEntries() {

        ArrayList<Entry> comps = new ArrayList<>();
        Cursor res = database.rawQuery("select * from entries ", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            int entryID = res.getInt(res.getColumnIndex("id"));
            long date = res.getLong(res.getColumnIndex("dateTaken"));
            int creon10 = res.getInt(res.getColumnIndex("creon10000Taken"));
            int creon25 = res.getInt(res.getColumnIndex("creon25000Taken"));
            int result = res.getInt(res.getColumnIndex("result"));
            String notes = res.getString(res.getColumnIndex("notes"));
            Entry entry = new Entry("", creon10, creon25, notes, date, result);
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

    public void updateResult(int result, int id) {

        ContentValues cv = new ContentValues();
        cv.put("result", result);
        database.update("entries", cv, "id = ? ", new String[]{Integer.toString(id)});
    }

}
