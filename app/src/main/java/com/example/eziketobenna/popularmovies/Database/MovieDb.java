package com.example.eziketobenna.popularmovies.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.eziketobenna.popularmovies.Model.Movie.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDb extends RoomDatabase {
    private static final String DB_NAME = "favorite_db";
    private static MovieDb sInstance;

    static MovieDb getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (MovieDb.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDb.class, DB_NAME).build();
                }

            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
