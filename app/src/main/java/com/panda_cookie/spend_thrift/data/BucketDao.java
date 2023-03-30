package com.panda_cookie.spend_thrift.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BucketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bucket bucket);

    @Query("SELECT * FROM buckets")
    List<Bucket> getAllBuckets();

    @Query("SELECT * FROM buckets")
    List<Bucket> getAll();

    @Query("SELECT * FROM buckets WHERE size = :bucketSize")
    Bucket findByBucket(String bucketSize);

    @Query("SELECT COUNT(*) FROM buckets")
    int getRowCount();

}
