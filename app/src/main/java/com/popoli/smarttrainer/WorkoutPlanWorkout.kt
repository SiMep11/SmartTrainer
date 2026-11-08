package com.popoli.smarttrainer

import androidx.room.Embedded
import androidx.room.Relation


data class WorkoutPlanWorkout(
    @Embedded val workoutPlan : WorkoutPlan,
    @Relation(
        parentColumn = "workoutPlanId",
        entityColumn = "workoutPlan"
    )
    val workout: List<Workout>
)
