package com.luke.fyp;

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
    private ArrayList<Entry> entries;
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

        if (id == android.R.id.home)
        {
            super.onBackPressed();
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
        entries = db.getAllEntries();
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Folder");
        boolean var = false;
        if (!folder.exists()) {
            var = folder.mkdir();
        }
        final String filename = folder.toString() + "/" + "Test.csv";
        try {
            FileWriter fw = new FileWriter(filename);
            for(int i = 0; i < entries.size(); i++)
            {
                fw.append("'" + convertDate(entries.get(i).getDateTaken(), "dd/MM/yyyy") + ",");
                fw.append(entries.get(i).getCreon10000taken() + ",");
                fw.append(entries.get(i).getCreon25000taken() + "");
                if(entries.get(i).getCombination() != null) {
                    fw.append(entries.get(i).getCombination().getName() + ",");
                    ArrayList<Component> components = entries.get(i).getCombination().getComponents();
                    for(int j = 0; j < components.size(); j++)
                    {
                        fw.append(components.get(j).getName() + " " + components.get(j).getQuantity() + " " + components.get(j).getServingType() + ",");
                    }

                }


                fw.append("\n");
            }
            fw.close();
        } catch (Exception ex) {
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
        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
    }


    /*private class sendEmail extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            entries = db.getAllEntries();

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, "Test.csv");
            /*File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Folder");
            boolean var = false;
            if (!folder.exists()) {
                var = folder.mkdir();
            }
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                for (int i = 0; i < entries.size(); i++) {
                    fw.append(convertDate(entries.get(i).getDateTaken(), "dd/MM/yyyy") + ",");
                    fw.append(entries.get(i).getCreon10000taken() + ",");
                    fw.append(entries.get(i).getCreon25000taken() + ",");
                    if(entries.get(i).getCombination() != null)
                    {
                        fw.append(entries.get(i).getCombination().getName() + ",");
                        ArrayList<Component> components = entries.get(i).getCombination().getComponents();
                        for(int j = 0; j < components.size(); j++)
                        {
                            fw.append(components.get(i).getName() + " " + components.get(i).getQuantity() + " " + components.get(i).getServingType() + ",");
                        }
                        if(entries.get(i).getComponents() != null)
                        {
                            for(int k = 0; k < entries.get(i).getComponents().size(); k++)
                            {
                                fw.append(entries.get(i).getComponents().get(i).getName() + " " + entries.get(i).getComponents().get(i).getQuantity() + " " + entries.get(i).getComponents().get(i).getServingType() + ",");
                            }
                        }

                    }
                    else
                    {
                        fw.append(" ,");
                        for(int k = 0; k < entries.get(i).getComponents().size(); k++)
                        {
                            fw.append(entries.get(i).getComponents().get(i).getName() + " " + entries.get(i).getComponents().get(i).getQuantity() + " " + entries.get(i).getComponents().get(i).getServingType() + ",");
                        }
                    }



                    fw.append(entries.get(i).getNotes() + "\n");
                }
                fw.close();
            } catch (Exception e) {
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{params[0]});
            intent.putExtra(Intent.EXTRA_SUBJECT, "CREON Diary");
            intent.putExtra(Intent.EXTRA_TEXT, "File Attached");
            Uri uri = Uri.fromFile(file1);
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
