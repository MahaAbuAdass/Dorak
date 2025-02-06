package com.example.dorak.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.SplashFragmentBinding
import com.example.dorak.util.PreferenceManager

class SplashScreen : Fragment() {

    private lateinit var binding : SplashFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SplashFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({

                val isSavedAnyURl = PreferenceManager.isAddedAnyUser(requireContext())

                if (isSavedAnyURl) {
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()  // Close LoginActivity so user can't go back
                }

            else {
                    findNavController().navigate(R.id.action_splashScreen_to_loginScreen)
                }


            // Navigate to the next screen, e.g., HomeFragment
        }, 1000) // 2-second delay
    }

}