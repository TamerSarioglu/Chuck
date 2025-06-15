package com.tamersarioglu.chuck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.chuck.domain.model.JokeUI
import com.tamersarioglu.chuck.domain.repository.ChuckRepository
import com.tamersarioglu.chuck.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokesViewModel @Inject constructor(
    private val repository: ChuckRepository
): ViewModel(){

    private val _jokeUiState = MutableStateFlow<UiState<JokeUI>>(UiState.Loading)
    val jokeUiState: StateFlow<UiState<JokeUI>> = _jokeUiState.asStateFlow()

    private val _categoriesUiState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val categoriesUiState: StateFlow<UiState<List<String>>> = _categoriesUiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadCategories()
        loadRandomJoke()
    }

    fun loadRandomJoke() {
        viewModelScope.launch {
            _jokeUiState.value = UiState.Loading
            repository.getRandomJoke()
                .onSuccess { joke ->
                    _jokeUiState.value = UiState.Success(joke)
                }
                .onFailure { exception ->
                    _jokeUiState.value = UiState.Error(
                        message = exception.message ?: "Unknown error occurred",
                        throwable = exception
                    )
                }
        }
    }

    fun loadRandomJokeByCategory(category: String) {
        viewModelScope.launch {
            _jokeUiState.value = UiState.Loading
            _selectedCategory.value = category
            repository.getRandomJokeByCategory(category)
                .onSuccess { joke ->
                    _jokeUiState.value = UiState.Success(joke)
                }
                .onFailure { exception ->
                    _jokeUiState.value = UiState.Error(
                        message = exception.message ?: "Unknown error occurred",
                        throwable = exception
                    )
                }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categoriesUiState.value = UiState.Loading
            repository.getCategories()
                .onSuccess { categories ->
                    _categoriesUiState.value = UiState.Success(categories)
                }
                .onFailure { exception ->
                    _categoriesUiState.value = UiState.Error(
                        message = exception.message ?: "Failed to load categories",
                        throwable = exception
                    )
                }
        }
    }

    fun refreshJoke() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val category = _selectedCategory.value
            if (category != null) {
                loadRandomJokeByCategory(category)
            } else {
                loadRandomJoke()
            }
            _isRefreshing.value = false
        }
    }

    fun clearCategorySelection() {
        _selectedCategory.value = null
        loadRandomJoke()
    }

    fun retryLastAction() {
        val category = _selectedCategory.value
        if (category != null) {
            loadRandomJokeByCategory(category)
        } else {
            loadRandomJoke()
        }
    }
}