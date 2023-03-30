package com.panda_cookie.spend_thrift.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Bucket.class, State.class}, version = 2, exportSchema = false)
 public abstract class AppDatabase extends RoomDatabase {

     public abstract BucketDao bucketDao();
     public abstract StateDao stateDao();
     private static volatile AppDatabase INSTANCE;

     private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
         @Override
         public void migrate(SupportSQLiteDatabase database) {
             // Create the new State table with NOT NULL constraints
             database.execSQL("CREATE TABLE state (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, bucket TEXT NOT NULL, start_date INTEGER NOT NULL, end_date INTEGER NOT NULL, tokens_used INTEGER NOT NULL, tokens_left INTEGER NOT NULL)");
         }
     };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
 }

