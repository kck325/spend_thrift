package com.example.spend_thrift;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "buckets")
public class Bucket {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "size")
    private Size size;

    @ColumnInfo(name = "value")
    private int value;

    @ColumnInfo(name = "tokens")
    private int tokens;

    public Bucket(@NonNull Size size, int value, int tokens) {
        this.size = size;
        this.value = value;
        this.tokens = tokens;
    }

    @NonNull
    public Size getSize() {
        return size;
    }

    public void setSize(@NonNull Size size) {
        this.size = size;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
}


