package com.example.dorak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dorak.databinding.ProfileFragmentBinding
import com.example.dorak.util.PreferenceManager

class ProfileFragment : Fragment() {

    private lateinit var binding : ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = PreferenceManager.getUsername(requireContext())
        val gender = PreferenceManager.getGender(requireContext())
        val mobile = PreferenceManager.getMobile(requireContext())
        binding.tvName.text = username
        binding.tvGender.text = gender
        binding.tvNumber.text = mobile


    }
}