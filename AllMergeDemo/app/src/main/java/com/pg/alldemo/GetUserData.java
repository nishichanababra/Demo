package com.pg.alldemo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by test on 27/2/18.
 */
@Dao
public interface GetUserData {

    @Insert
    void insertAllData(List<User> allData);

    @Query("DELETE FROM User")
    void clearAllTableData();


    @Query("SELECT * FROM user")
    List<User> getAllUserData();
}
