package com.popoli.smarttrainer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insUser(vararg u: User)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insWorkoutPlan(vararg wp: WorkoutPlan)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insWorkout(vararg w: Workout)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun findUser(username: String): User?

    @Query("SELECT * FROM workoutPlans WHERE userID =:username")
    fun getUserWorkoutPlans(username: String): Flow<List<WorkoutPlan>>
    
    @Query("SELECT username FROM users")
    fun getAllUsernames(): List<String>

    @Query("SELECT workoutPlanId FROM workoutPlans ORDER BY workoutPlanId DESC LIMIT 1")
    suspend fun getLastWpId(): Long

    @Query("SELECT * FROM workouts WHERE workoutPlanID = :wpId")
    suspend fun getAllWorkouts(wpId: Long): List<Workout>

    @Query("DELETE FROM workoutPlans WHERE workoutPlanId = :wpId")
    suspend fun deleteWorkoutPlans(wpId: Long)
}