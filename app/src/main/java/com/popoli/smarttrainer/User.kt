package com.popoli.smarttrainer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false) val username: String,
    @ColumnInfo(name = "password") val password: String?
)
