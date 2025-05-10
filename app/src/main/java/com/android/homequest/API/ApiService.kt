package com.android.homequest.API

import com.android.homequest.model.LoginRequest
import com.android.homequest.model.LoginResponse
import com.android.homequest.model.PointsUpdate
import com.android.homequest.model.Relationship
import com.android.homequest.model.Reward
import com.android.homequest.model.StatusUpdate
import com.android.homequest.model.TaskAssignment
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("users/search")
    fun searchUsers(
        @Query("role") role: String? = null,
        @Query("firstname") firstname: String? = null,
        @Query("lastname") lastname: String? = null
    ): Call<List<User>>


    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @PATCH("/users/{id}/points")
    fun updateChildPoints(@Path("id") userId: String, @Body pointsUpdate: PointsUpdate): Call<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: String): Call<User>

    @GET("tasks")
    fun getTaskAssignments(): Call<List<TaskAssignment>>

    @GET("/tasks/today")
    fun getTodayTasks(): Call<List<TaskAssignment>>

    @GET("tasks/upcoming")
    fun getUpcomingTasks(
        @Query("assignTo") assignTo: String,
        @Query("assignToEmail") assignToEmail: String
    ): Call<List<TaskAssignment>>



    @POST("tasks")
    fun assignTask(@Body taskAssignment: TaskAssignment): Call<TaskAssignment>

    @PATCH("/tasks/{id}/status")
    fun updateTaskStatus(@Path("id") taskId: String, @Body statusUpdate: StatusUpdate): Call<TaskAssignment>

    @DELETE("tasks")
    fun deleteTaskByAssignee(
        @Query("assignTo") assignTo: String,
        @Query("assignToEmail") assignToEmail: String
    ): Call<TaskAssignment>

    @GET("rewards")
    fun getRewards(): Call<List<Reward>>

    @POST("rewards")
    fun createReward(@Body reward: Reward): Call<Reward>

    @PATCH("/rewards/{id}/status")
    fun updateRewardStatus(@Path("id") rewardId: String, @Body statusUpdate: StatusUpdate): Call<Reward>

    @GET("relationships")
    fun getRelationships(): Call<List<Relationship>>

    @POST("relationships")
    fun createRelationship(@Body relationship: Relationship): Call<Relationship>

    @DELETE("relationships/{id}")
    fun deleteRelationshipById(@Path("id") id: String): Call<Relationship>

    @GET("relationships/search")
    fun searchRelationship(
        @Query("parentFirstname") parentFirstname: String? = null,
        @Query("parentEmail") parentEmail: String? = null,
        @Query("childFirstname") childFirstname: String? = null,
        @Query("childEmail") childEmail: String? = null
    ): Call<List<Relationship>>
}
