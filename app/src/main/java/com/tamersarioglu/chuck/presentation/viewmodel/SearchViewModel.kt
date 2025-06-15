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
class SearchViewModel @Inject constructor(
    private val repository: ChuckRepository
): ViewModel() {

    private val _searchUiState = MutableStateFlow<UiState<List<JokeUI>>>(UiState.Loading)
    val searchUiState: StateFlow<UiState<List<JokeUI>>> = _searchUiState.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    fun searchJokes(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isSearching.value = true
            _hasSearched.value = true
            _currentQuery.value = query
            _searchUiState.value = UiState.Loading

            repository.searchJokes(query)
                .onSuccess { jokes ->
                    if (jokes.isEmpty()) {
                        _searchUiState.value = UiState.Error(
                            message = "No jokes found for \"$query\"",
                            throwable = Exception("No results")
                        )
                    } else {
                        _searchUiState.value = UiState.Success(jokes)
                    }
                }
                .onFailure { exception ->
                    _searchUiState.value = UiState.Error(
                        message = exception.message ?: "Search failed",
                        throwable = exception
                    )
                }

            _isSearching.value = false
        }
    }

    fun clearSearch() {
        _searchUiState.value = UiState.Loading
        _hasSearched.value = false
        _currentQuery.value = ""
    }

    fun retrySearch() {
        val query = _currentQuery.value
        if (query.isNotBlank()) {
            searchJokes(query)
        }
    }
}