package com.h.pixeldroid.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.h.pixeldroid.objects.Instance

@Entity(tableName = "instances")
data class InstanceDatabaseEntity (
    @PrimaryKey var uri: String,
    var title: String = "",
    var max_toot_chars: Int = Instance.DEFAULT_MAX_TOOT_CHARS,
    var thumbnail: String = ""
)