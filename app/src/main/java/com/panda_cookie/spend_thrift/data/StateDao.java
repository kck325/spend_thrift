package com.panda_cookie.spend_thrift.data;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StateDao {
    @Query("SELECT * FROM state")
    List<State> getAll();

    @Query("SELECT * FROM state WHERE bucket = :bucket")
    State findByBucket(String bucket);

    @Insert
    void insert(State state);

    @Update
    void update(State state);

    @Query("SELECT bucket, COUNT(*) as goals_met FROM state WHERE tokens_left >= 0 GROUP BY bucket")
    List<GoalsMet> getGoalsMet();

    @Query("SELECT bucket, COUNT(*) as goals_missed FROM state WHERE tokens_left < 0 GROUP BY bucket")
    List<GoalsMissed> getGoalsMissed();

    class GoalsMet {
        @ColumnInfo(name = "bucket")
        public String bucketType;
        @ColumnInfo(name = "goals_met")
        public int goalsMet;

        public String getBucketType() {
            return bucketType;
        }

        public void setBucketType(String bucketType) {
            this.bucketType = bucketType;
        }

        public int getGoalsMet() {
            return goalsMet;
        }

        public void setGoalsMet(int goalsMet) {
            this.goalsMet = goalsMet;
        }
    }

    class GoalsMissed {
        @ColumnInfo(name = "bucket")
        public String bucketType;
        @ColumnInfo(name = "goals_missed")
        public int goalsMissed;

        public String getBucketType() {
            return bucketType;
        }

        public void setBucketType(String bucketType) {
            this.bucketType = bucketType;
        }

        public int getGoalsMissed() {
            return goalsMissed;
        }

        public void setGoalsMissed(int goalsMissed) {
            this.goalsMissed = goalsMissed;
        }
    }
}

