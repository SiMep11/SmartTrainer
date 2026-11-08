package com.popoli.smarttrainer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["workoutPlanId"],
            childColumns = ["workoutPlanID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Workout(
    @PrimaryKey (autoGenerate = true) val workoutId: Long = 0,
    @ColumnInfo(name = "exercise") val exercise: String,
    @ColumnInfo(name = "sets") val sets: String,
    @ColumnInfo(name = "reps") val reps: String,
    @ColumnInfo(name = "rest") val rest: String,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "workoutPlanID", index = true) val workoutPlan: Long = 0
)
