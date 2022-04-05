package com.example.jetpackdatastoredemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.jetpackdatastoredemo.databinding.ActivityPreferencesDataStoreBinding
import kotlinx.coroutines.launch

class PreferencesDataStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesDataStoreBinding

    private val Context.userInfoPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "userInfo"
    )

    private val USER_JOB = stringPreferencesKey("user_job")
    private val USER_INCOME = intPreferencesKey("user_income")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preferences_data_store)
        binding.bottomNavigation.selectedItemId = R.id.preferences
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.proto -> {
                    startActivity(Intent(this, ProtoDataStoreActivity::class.java))
                }
                R.id.preferences -> {
                    startActivity(Intent(this, PreferencesDataStoreActivity::class.java))
                }
            }
            true
        }

        binding.saveButton.setOnClickListener {
            lifecycleScope.launch {
                val userJob = binding.jobInput.text.toString()
                val userIncome = binding.incomeInput.text.toString().toInt()
                userInfoPreferencesDataStore.edit { preferences ->
                    preferences[USER_JOB] = userJob
                    preferences[USER_INCOME] = userIncome
                }
            }
        }

        lifecycleScope.launch {
            userInfoPreferencesDataStore.data.collect { preferences ->
                binding.jobText.text = preferences[USER_JOB]
                binding.incomeText.text = preferences[USER_INCOME].toString()
            }

        }
    }
}