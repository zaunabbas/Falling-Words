package com.zacoding.android.fallingwords.presentation.startgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zacoding.android.fallingwords.R
import com.zacoding.android.fallingwords.databinding.FragmentStartGameBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StartGameFragment : Fragment() {

    private var _binding: FragmentStartGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        setupUiEventListeners()
    }

    private fun setupUiEventListeners() {
        binding.btnStartGame.setOnClickListener {
            findNavController().navigate(R.id.action_to_falling_words_game_fragment)
        }
    }
}
