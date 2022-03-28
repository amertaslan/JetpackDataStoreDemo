package com.example.jetpackdatastoredemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.jetpackdatastoredemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var name: String? = null
    private var surName: String? = null
    private var age: String? = null
    private val USER_DATA_STORE_FILE_NAME = "user_store.pb"

    val Context.userDataStore: DataStore<UserStore> by dataStore(
        fileName = USER_DATA_STORE_FILE_NAME,
        serializer = UserSerializer
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.saveButton.setOnClickListener {
            if (isDataLoaded()) {
                fillData()
            } else {
                storeData()
            }
        }

    }

    private fun storeData() {
        name = binding.nameInput.text.toString()
        surName = binding.surnameInput.text.toString()
        age = binding.ageInput.text.toString()
    }

    private fun fillData() {
        binding.nameText.text = name
        binding.surnameText.text = surName
        binding.ageText.text = age
    }

    private fun isDataLoaded(): Boolean = !name.isNullOrBlank() && !surName.isNullOrBlank() && !age.isNullOrBlank()

}