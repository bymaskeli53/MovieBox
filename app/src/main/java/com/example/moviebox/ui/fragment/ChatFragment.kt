package com.example.moviebox.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentChatBinding
import com.example.moviebox.ui.adapter.ChatAdapter
import com.example.moviebox.ui.fragment.base.BaseFragment
import com.example.moviebox.viewmodel.ChatUiState
import com.example.moviebox.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatFragment :
    BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate, R.layout.fragment_chat) {

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())

        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            if (message.isNotBlank()) {
                chatViewModel.sendMessage(message)
                binding.editTextMessage.text.clear()
            }
        }

        chatViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChatUiState.Idle -> {
                 binding.progressBar.visibility = View.GONE
                }
                is ChatUiState.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                }
                is ChatUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter = ChatAdapter(state.messages)
                    binding.recyclerViewMessages.adapter = adapter
                    if (state.messages.size >= 4) {
                        binding.recyclerViewMessages.smoothScrollToPosition(state.messages.size - 2)
                    }
                }
                is ChatUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}