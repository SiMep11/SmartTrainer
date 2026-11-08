package com.popoli.smarttrainer

import androidx.room.Embedded
import androidx.room.Relation

data class UserWorkoutPlan(
    @Embedded val user : User,
    @Relation(
        parentColumn = "username",
        entityColumn = "userID"
    )
    val workoutPlan: List<WorkoutPlan>
)
