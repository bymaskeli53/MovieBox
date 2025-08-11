package com.example.moviebox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.BuildConfig
import com.example.moviebox.model.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableLiveData<ChatUiState>(ChatUiState.Idle)
    val uiState: LiveData<ChatUiState> = _uiState

    private val messages = mutableListOf<MessageModel>()

    private val generativeModel: GenerativeModel =
        GenerativeModel(modelName = "gemini-2.5-flash", apiKey = BuildConfig.GEMINI_API_KEY)

    fun sendMessage(question: String) {
        viewModelScope.launch {
            _uiState.value = ChatUiState.Loading
            try {
                val chat = generativeModel.startChat(
                    history = messages.map {
                        if (it.isUser) {
                            content("user") { text(it.message) }
                        } else {
                            content("model") { text(it.message) }
                        }
                    }
                )

                messages.add(MessageModel(question, true))

                val response = chat.sendMessage(question)

                messages.add(MessageModel(response.text.orEmpty(), false))

                _uiState.value = ChatUiState.Success(messages.toList())
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.localizedMessage ?: "Bilinmeyen hata")
            }
        }
    }
}


sealed class ChatUiState {
    object Idle : ChatUiState() // Henüz işlem yok
    object Loading : ChatUiState()
    data class Success(val messages: List<MessageModel>) : ChatUiState()
    data class Error(val errorMessage: String) : ChatUiState()
}
