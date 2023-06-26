package com.example.sfcm.src.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContactPreferenceDao
{
    @Query("SELECT * FROM ContactPreference")
    List<ContactPreference> getAll();

    @Query("SELECT * FROM ContactPreference WHERE phone_num IN (:phoneNum)")
    List<ContactPreference> getByPhoneNum(String phoneNum);

    @Query("SELECT * FROM ContactPreference WHERE id IN (:id)")
    ContactPreference getById(int id);

    @Insert(onConflict = REPLACE)
    void insert(ContactPreference mainData);

    @Query("UPDATE ContactPreference SET call_count = :callCount, interval_time = :inverval_time, enabled = :enabled  WHERE phone_num = :phoneNum")
    void update(String phoneNum, int callCount, int inverval_time, boolean enabled);

    @Delete
    void delete(ContactPreference pref);
}