package com.example.jetpackdatastoredemo

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUserInfo(user:UserModel)
    suspend fun getUserInfo(): Flow<UserModel>
}