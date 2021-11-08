package com.game.TamilGuru.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.game.TamilGuru.data.database.daos.TgLoginDao;
import com.game.TamilGuru.data.database.entities.TgLoginData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {TgLoginData.class},version = 1)

public abstract class TamilGuruDatabase extends RoomDatabase {
    public abstract TgLoginDao tgLoginDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    @Override
    public void clearAllTables() {

    }
}
