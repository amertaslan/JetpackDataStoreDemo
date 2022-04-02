package com.example.jetpackdatastoredemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import com.example.jetpackdatastoredemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), UserRepository {

    private lateinit var binding: ActivityMainBinding
    private var name: String? = null
    private var surName: String? = null
    private var age: Long? = null
    private val USER_DATA_STORE_FILE_NAME = "user_store.pb"
    private lateinit var user: UserModel

    private val Context.userDataStore: DataStore<UserStore> by dataStore(
        fileName = USER_DATA_STORE_FILE_NAME,
        serializer = UserSerializer
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.saveButton.setOnClickListener {
            if (isDataLoaded()) {
                fillData()
                user = UserModel(name, surName, age)
                lifecycleScope.launch {
                    saveUserInfo(user)
                }
            } else {
                storeData()
            }
        }

    }

    private fun storeData() {
        name = binding.nameInput.text.toString()
        surName = binding.surnameInput.text.toString()
        age = binding.ageInput.text.toString().toLong()
    }

    private fun fillData() {
        binding.nameText.text = name
        binding.surnameText.text = surName
        binding.ageText.text = age.toString()
    }

    private fun isDataLoaded(): Boolean = !name.isNullOrBlank() && !surName.isNullOrBlank() && age != null

    override suspend fun saveUserInfo(user: UserModel) {
        userDataStore.updateData {store ->
            store.toBuilder()
                .setName(user.name)
                .setSurname(user.surName)
                .setAge(user.age!!)
                .build()
        }
    }

    override suspend fun getUserInfo(): Flow<UserModel> {
        TODO("Not yet implemented")
    }

}