package com.example.dorak.ui.login

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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.NewPasswordBinding

class NewPassword : Fragment() {

    private lateinit var binding : NewPasswordBinding

    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    var password: String? = null
    var confirmPassword: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewPasswordBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etConfirmPassword = binding.etConfirmPassword
        etPassword = binding.etPassword


        setupPassword(etPassword)
        setupPassword(etConfirmPassword)

        binding.btnCreatePass.setOnClickListener {
            // Hide previous error messages
            binding.etConfirmPasswordError.visibility = View.GONE
            binding.etPasswordError.visibility = View.GONE

            // Reset field background colors
            binding.etConfirmPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
            binding.etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)

            var isValid = true

            // Check if confirm password is empty
            if (binding.etConfirmPassword.text.isNullOrEmpty()) {
                binding.etConfirmPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.red_border)
                binding.etConfirmPasswordError.visibility = View.VISIBLE
                isValid = false
            }

            // Check if password is empty
            if (binding.etPassword.text.isNullOrEmpty()) {
                binding.etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.red_border)
                binding.etPasswordError.visibility = View.VISIBLE
                isValid = false
            }

            // Check if password and confirm password match
            if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                Toast.makeText(requireContext(), "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                isValid = false
            }

            // If all fields are valid, navigate to the next screen
            if (isValid) {
                confirmPassword = binding.etConfirmPassword.text.toString()
                password = binding.etPassword.text.toString()

                findNavController().navigate(NewPasswordDirections.actionNewPasswordToLoginScreen())
            }
        }



    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPassword(etPassword: EditText) {
        val eyeOpen: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.eyegreen) // Visible icon
        val eyeClosed: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.eye) // Hidden icon
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
        // Remove red border when user starts typing
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if there is a red border (indicating error) and remove it when user starts typing
                if (etPassword.background == ContextCompat.getDrawable(requireContext(), R.drawable.red_border)) {
                    etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}