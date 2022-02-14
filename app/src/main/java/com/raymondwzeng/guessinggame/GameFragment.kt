package com.raymondwzeng.guessinggame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.raymondwzeng.guessinggame.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private var _binding : FragmentGameBinding? = null
    private val binding get() = _binding!!

    //Get the ViewModel
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        //Gets the ViewModel, assigns it to the view model property that we have.
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
        }

        //Use new observer pattern
        viewModel.isGameOver.observe(viewLifecycleOwner) { newValue ->
            if (newValue) {
                val action =
                    GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view.findNavController().navigate(action)
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}