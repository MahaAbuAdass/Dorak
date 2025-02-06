package com.example.dorak.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dorak.MainActivity
import com.example.dorak.databinding.MoreFragmentBinding
import com.example.dorak.util.PreferenceManager

class MoreFragment:Fragment() {

    private lateinit var binding : MoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=MoreFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogout.setOnClickListener {
            PreferenceManager.clearUserDate(requireContext())
            PreferenceManager.setUserData(requireContext(),false)

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()  // Close LoginActivity so user can't go back
        }
    }
}