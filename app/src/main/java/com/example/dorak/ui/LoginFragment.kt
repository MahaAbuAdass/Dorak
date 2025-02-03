package com.example.dorak.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etMobile = binding.etMobile
        etPassword = binding.etPassword

        setupMobileNumber()
        setupPassword()

        binding.btnLogin.setOnClickListener {
            // Hide previous error messages
            binding.etMobileError.visibility = View.GONE
            binding.etPasswordError.visibility = View.GONE

            // Reset field background colors
            binding.etMobile.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
            binding.etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)

            var isValid = true

            // Check if mobile is empty
            if (binding.etMobile.text.isNullOrEmpty()) {
                binding.etMobile.background = ContextCompat.getDrawable(requireContext(), R.drawable.red_border)
                binding.etMobileError.visibility = View.VISIBLE
                isValid = false
            }

            // Check if password is empty
            if (binding.etPassword.text.isNullOrEmpty()) {
                binding.etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.red_border)
                binding.etPasswordError.visibility = View.VISIBLE
                isValid = false
            }

            // If both fields are valid, show the popup
            if (isValid) {
                showPopup()
            }
        }



    }

    private fun showPopup() {

        dialog = Dialog(requireContext())
        dialog?.setContentView(R.layout.confirmation_layout)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Important for rounded corners!


        dialog?.setCancelable(false)

        val homeBtn: Button? = dialog?.findViewById(R.id.btn_home)

        homeBtn?.setOnClickListener {

           // findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()  // Close LoginActivity so user can't go back



            dialog?.dismiss()
            dialog = null  // Dismiss the popup when "Complete" is clicked
        }

        dialog?.show()
    }


@SuppressLint("ClickableViewAccessibility")
private fun setupPassword() {
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

private fun setupMobileNumber() {
    // Load drawable icons
    val defaultStartIcon: Drawable? =
        ContextCompat.getDrawable(requireContext(), R.drawable.mobile) // Grey icon
    val activeStartIcon: Drawable? =
        ContextCompat.getDrawable(requireContext(), R.drawable.mobile_green) // Green active icon
    val endIcon: Drawable? =
        ContextCompat.getDrawable(requireContext(), R.drawable.check) // End icon (green check)

    etMobile.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()

            if (etMobile.background == ContextCompat.getDrawable(requireContext(), R.drawable.red_border)) {
                etMobile.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
            }

            if (text.length >= 10) {
                // When exactly 10 digits, show active start icon & check mark end icon
                etMobile.setCompoundDrawablesWithIntrinsicBounds(
                    activeStartIcon,
                    null,
                    endIcon,
                    null
                )
            } else {
                // Otherwise, show default start icon & remove end icon
                etMobile.setCompoundDrawablesWithIntrinsicBounds(defaultStartIcon, null, null, null)
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}


}

