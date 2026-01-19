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

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-3-flash-preview",
        apiKey = BuildConfig.GEMINI_API_KEY,
        systemInstruction = content {
            text(
                """
                You are MovieBot, a friendly and knowledgeable movie recommendation assistant for the MovieBox app.

                Your expertise:
                - Recommending movies based on user preferences, mood, or specific criteria
                - Providing information about movies (plot summaries, cast, directors, release years, genres)
                - Suggesting similar movies to ones the user has enjoyed
                - Helping users discover hidden gems and classics
                - Discussing movie trivia and interesting facts

                Guidelines:
                - Always be enthusiastic and passionate about movies
                - Ask clarifying questions to give better recommendations (e.g., preferred genre, mood, actors)
                - Provide 3-5 movie suggestions when recommending, with brief explanations for each
                - Include the release year when mentioning movies
                - If asked about non-movie topics, politely redirect the conversation back to movies
                - Keep responses concise and mobile-friendly
                - Use emojis sparingly to keep the conversation fun 🎬

                Example response format for recommendations:
                "Based on your love for thriller movies, here are my picks:
                1. **Inception (2010)** - Mind-bending thriller by Christopher Nolan
                2. **Gone Girl (2014)** - A gripping psychological thriller
                3. **Prisoners (2013)** - Intense mystery thriller with stellar performances"
                """.trimIndent()
            )
        }
    )

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
