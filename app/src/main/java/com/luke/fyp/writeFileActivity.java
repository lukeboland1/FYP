package com.luke.fyp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class writeFileActivity extends AppCompatActivity {
    private DatabaseAccess db;
    private ArrayList<MealRecord> mealRecords;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_file);
        email = (EditText)findViewById(R.id.emailAddress);
        db = new DatabaseAccess(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void sendEmail(View v) throws IOException {
        String e = email.getText().toString();
        new sendEmail().execute(e);
        /*
        mealRecords = db.getAllMealRecords();
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Folder");

        boolean var = false;
        if (!folder.exists()) {
            var = folder.mkdir();
        }

        final String filename = folder.toString() + "/" + "Test.csv";

        try {
            FileWriter fw = new FileWriter(filename);
            for(int i = 0; i < mealRecords.size(); i++)
            {
                String s = Long.toString(mealRecords.get(i).getDateTaken());
                fw.append(mealRecords.get(i).getName() + ",");
                fw.append(convertDate(s, "dd/MM/yyyy") + ",");
                fw.append(mealRecords.get(i).getCreonTaken()+ ",");
                fw.append(mealRecords.get(i).getNotes() + "\n");
            }
            fw.close();

        } catch (Exception e) {


        }
        File file = new File(filename);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "CREON Diary");
        intent.putExtra(Intent.EXTRA_TEXT, "File Attached");
        Uri uri = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Send email..."));

        email.setText("");
        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();*/
    }


    private class sendEmail extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            mealRecords = db.getAllMealRecords();
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Folder");

            boolean var = false;
            if (!folder.exists()) {
                var = folder.mkdir();
            }

            final String filename = folder.toString() + "/" + "Test.csv";

            try {
                FileWriter fw = new FileWriter(filename);
                for (int i = 0; i < mealRecords.size(); i++) {

                    fw.append(mealRecords.get(i).getName() + ",");
                    fw.append(convertDate(mealRecords.get(i).getDateTaken(), "dd/MM/yyyy") + ",");
                    fw.append(mealRecords.get(i).getCreonTaken() + ",");
                    fw.append(mealRecords.get(i).getNotes() + "\n");
                }
                fw.close();

            } catch (Exception e) {


            }
            File file = new File(filename);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{params[0]});
            intent.putExtra(Intent.EXTRA_SUBJECT, "CREON Diary");
            intent.putExtra(Intent.EXTRA_TEXT, "File Attached");
            Uri uri = Uri.fromFile(file);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Send email..."));
            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

                email.setText("");
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}
