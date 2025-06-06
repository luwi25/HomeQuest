package com.android.homequest.API

import com.android.homequest.model.ApiResponse
import com.android.homequest.model.ChildUpdateRequest
import com.android.homequest.model.ChildUpdateResponse
import com.android.homequest.model.LoginRequest
import com.android.homequest.model.LoginResponse
import com.android.homequest.model.OtpRequest
import com.android.homequest.model.ParentFirstnameUpdateRequest
import com.android.homequest.model.PointsUpdate
import com.android.homequest.model.Relationship
import com.android.homequest.model.Reward
import com.android.homequest.model.StatusUpdate
import com.android.homequest.model.TaskAssignUpdateRequest
import com.android.homequest.model.TaskAssignment
import com.android.homequest.model.TaskAssignmentResponse
import com.android.homequest.model.UpdateParentResponse
import com.android.homequest.model.UpdateUserRequest
import com.android.homequest.model.UpdateUserResponse
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/send-otp")
    fun sendOtp(@Body otpRequest: OtpRequest): Call<ApiResponse>
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(
        @Path("id") id: String
    ): Call<User>


    @PUT("/users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body request: UpdateUserRequest
    ): Call<UpdateUserResponse>

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

    @DELETE("tasks/{id}")
    fun deleteTaskById(@Path("id") taskId: String): Call<TaskAssignment>

    @PUT("tasks/update-assign-to")
    fun updateAssignTo(@Body request: TaskAssignUpdateRequest): Call<TaskAssignmentResponse>

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

    @DELETE("rewards/{id}")
    fun deleteRewardById(@Path("id") rewardId: String): Call<Reward>

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

    @PUT("/relationships/update-child")
    fun updateChild(@Body childUpdate: ChildUpdateRequest): Call<ChildUpdateResponse>

    @PUT("relationships/update-parent-firstname")
    fun updateParentFirstname(
        @Body request: ParentFirstnameUpdateRequest
    ): Call<UpdateParentResponse>

}
