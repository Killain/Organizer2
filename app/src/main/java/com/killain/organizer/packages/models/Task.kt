package com.killain.organizer.packages.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "task")
class Task : Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var id: Int = 0

    @ColumnInfo(name = "task_string")
    var task_string: String? = null

    @ColumnInfo(name = "task_group_tag")
    var group_tag: Int? = 0

    @ColumnInfo(name = "task_date")
    var date: Long? = 0

    @ColumnInfo(name = "task_time")
    var time: String? = null

    @ColumnInfo(name = "task_title")
    var title: String? = null

    @ColumnInfo(name = "task_is_completed")
    var isCompleted: Boolean? = false

    @ColumnInfo(name = "is_notification_showed")
    var isNotificationShowed: Boolean? = false

    @ColumnInfo(name = "is_deleted")
    var isDeleted: Boolean? = false

    @ColumnInfo(name = "has_reference")
    var isHasReference: Boolean? = false

    @ColumnInfo(name = "task_list_index")
    var list_index: Int? = 0

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        return super.clone()
    }
}
