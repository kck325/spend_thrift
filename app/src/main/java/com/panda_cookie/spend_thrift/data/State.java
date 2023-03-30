package com.panda_cookie.spend_thrift.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "state")
public class State {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    public String bucket;

    @ColumnInfo(name = "start_date")
    public long startDate;
    @ColumnInfo(name = "end_date")
    public long endDate;
    @ColumnInfo(name = "tokens_used")
    public int tokensUsed;
    @ColumnInfo(name = "tokens_left")
    public int tokensLeft;

    public State(@NonNull String bucket, long startDate, long endDate, int tokensUsed, int tokensLeft) {
        this.bucket = bucket;

        this.startDate = startDate;
        this.endDate = endDate;
        this.tokensUsed = tokensUsed;
        this.tokensLeft = tokensLeft;
    }

    @NonNull
    public String getBucket() {
        return bucket;
    }

    public void setBucket(@NonNull String bucket) {
        this.bucket = bucket;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(int tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public int getTokensLeft() {
        return tokensLeft;
    }

    public void setTokensLeft(int tokensLeft) {
        this.tokensLeft = tokensLeft;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

