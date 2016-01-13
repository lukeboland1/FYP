package com.luke.fyp;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

/**
 * Created by Luke on 12/01/2016.
 */
public class MyBackupAgent extends BackupAgentHelper {
    @Override
    public void onCreate(){
        FileBackupHelper dbs = new FileBackupHelper(this, "/data/data/com.luke.fyp/databases/MyDBName.db");
        addHelper("MyDBName", dbs);
    }
}
