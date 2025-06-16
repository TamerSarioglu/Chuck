package com.tamersarioglu.chuck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tamersarioglu.chuck.data.datastore.UserPreferencesDataStore
import com.tamersarioglu.chuck.presentation.navigation.AppNavigation
import com.tamersarioglu.chuck.presentation.ui.theme.ChuckTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var userPreferencesDataStore: UserPreferencesDataStore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChuckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(userPreferencesDataStore = userPreferencesDataStore)
                }
            }
        }
    }
}