package com.game.TamilGuru.data.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.game.TamilGuru.data.database.entities.TgLoginData;


@Dao

public interface TgLoginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    static void insert(TgLoginData tgLoginData) {

    }
}
