package com.popoli.smarttrainer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workoutPlans")
data class WorkoutPlan(
    @PrimaryKey (autoGenerate = true) val workoutPlanId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "days") val days: String,
    @ColumnInfo(name = "userID") val userID: String
)
