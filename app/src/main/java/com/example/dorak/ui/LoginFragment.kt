package com.example.dorak.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etMobile: EditText = binding.etMobile
        val etPassword: EditText = binding.etPassword


        // Load drawable icons
        val defaultStartIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.mobile) // Grey icon
        val activeStartIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.mobile_green) // Green active icon
        val endIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.check) // End icon (green check)

        etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()

                if (text.length >= 10) {
                    // When exactly 10 digits, show active start icon & check mark end icon
                    etMobile.setCompoundDrawablesWithIntrinsicBounds(activeStartIcon, null, endIcon, null)
                } else {
                    // Otherwise, show default start icon & remove end icon
                    etMobile.setCompoundDrawablesWithIntrinsicBounds(defaultStartIcon, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        val eyeOpen: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.eyegreen) // Visible icon
        val eyeClosed: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.eye) // Hidden icon
        val lockIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.lock)

        var isPasswordVisible = false


        etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (etPassword.right - etPassword.compoundDrawables[2].bounds.width())) {

                    isPasswordVisible = !isPasswordVisible // Toggle state

                    // **1. Update the icon immediately**
                    val newIcon = if (isPasswordVisible) eyeOpen else eyeClosed
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, newIcon, null)

                    // **2. Delay the inputType change to prevent UI refresh issues**
                    etPassword.postDelayed({
                        etPassword.inputType = if (isPasswordVisible) {
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        } else {
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        }
                        etPassword.setSelection(etPassword.text.length) // Keep cursor at the end
                    }, 50) // Small delay ensures smooth UI update

                    return@setOnTouchListener true
                }
            }
            false
        }
    }



    }

