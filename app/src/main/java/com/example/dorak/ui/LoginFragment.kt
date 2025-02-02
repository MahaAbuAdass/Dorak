package com.example.dorak.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dorak.R
import com.example.dorak.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding : LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etMobile: EditText = binding.etMobile

        // Load drawable icons
        val defaultStartIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.mobile) // Grey icon
        val activeStartIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.mobile_green) // Green active icon
        val endIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.check) // End icon (green check)

        etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()

                if (text.length == 10) {
                    // When exactly 10 digits, show active start icon & check mark end icon
                    etMobile.setCompoundDrawablesWithIntrinsicBounds(activeStartIcon, null, endIcon, null)
                } else {
                    // Otherwise, show default start icon & remove end icon
                    etMobile.setCompoundDrawablesWithIntrinsicBounds(defaultStartIcon, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    }
