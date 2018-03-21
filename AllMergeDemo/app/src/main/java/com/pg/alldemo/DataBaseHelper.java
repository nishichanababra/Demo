package com.pg.alldemo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

/**
 * Created by test on 27/2/18.
 */
@Database(entities = {User.class}, version = 1)
public abstract class DataBaseHelper extends RoomDatabase {

    public abstract GetUserData getUserData();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //Execute queries of on upgrade methods
        }
    };
}


