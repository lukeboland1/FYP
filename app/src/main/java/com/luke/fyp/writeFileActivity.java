package com.luke.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class writeFileActivity extends AppCompatActivity {
    private DatabaseAccess db;
    private ArrayList<Entry> mealRecords;
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
        new ExportDatabaseFileTask().execute(e);
    }


    /*private class sendEmail extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            mealRecords = db.getAllEntries();
            File data = Environment.getDataDirectory();
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Folder");

            boolean var = false;
            if (!folder.exists()) {
                var = folder.mkdir();
            }

            final String filename = folder.toString() + "/" + "dump.db";

            try {
                String  currentDBPath= "//data/data/com.luke.fyp/databases/MyDBName.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(folder, filename);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

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
    }*/

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {

        // can use UI thread here
        protected void onPreExecute() {

        }

        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... params) {

            File dbFile =
                    new File(Environment.getDataDirectory() + "/data/com.luke.fyp/databases/MyDBName.db");

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{params[0]});
                intent.putExtra(Intent.EXTRA_SUBJECT, "CREON Diary");
                intent.putExtra(Intent.EXTRA_TEXT, "File Attached");
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Send email..."));
                return true;
            } catch (IOException e) {
                Log.e("mypck", e.getMessage(), e);
                return false;
            }
        }

        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
            email.setText("");
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

        }

        void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        }

    }
}
