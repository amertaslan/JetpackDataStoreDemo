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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity(), UserRepository {

    private lateinit var binding: ActivityMainBinding
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

                user = createUser()
                lifecycleScope.launch {
                    saveUserInfo(user)
                }

        }

        lifecycleScope.launch {
            getUserInfo().collect { userModel ->
                fillData(userModel)
            }
        }
    }

    private fun createUser(): UserModel {
        return UserModel(
            binding.nameInput.text.toString(),
            binding.surnameInput.text.toString(),
            binding.ageInput.text.toString().toLong()
        )
    }

    private fun fillData(userModel: UserModel) {
        binding.nameText.text = userModel.name
        binding.surnameText.text = userModel.surName
        binding.ageText.text = userModel.age.toString()
    }

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
        return  userDataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(UserStore.getDefaultInstance())
                } else {
                    throw exception
                }
            }.map { protoBuilder ->
                UserModel(protoBuilder.name, protoBuilder.surname, protoBuilder.age)
            }
    }

}