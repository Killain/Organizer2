package com.killain.organizer.packages.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;

@Entity (tableName = "subtask")
public class SubTask {

    @PrimaryKey (autoGenerate = true)
    private int Id;
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @ColumnInfo(name = "subtask_text")
    private String text;
    @ColumnInfo(name = "subtask_order_id")
    private int OrderId;
    @ColumnInfo(name = "subtask_reference")
    private String reference;
    @ColumnInfo(name = "subtask_is_checked")
    private boolean isChecked;

    public SubTask() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
