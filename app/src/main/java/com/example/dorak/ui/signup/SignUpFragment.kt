package com.example.dorak.ui.signup

import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.SignupFragmentBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.HomeActivity
import com.example.dorak.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment:Fragment() {

    private lateinit var binding : SignupFragmentBinding
    private lateinit var registerUserViewModel : RegisterViewModel
    private lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    private lateinit var etName : EditText
    private var dialog: Dialog? = null
    var mobileNumber: String? = null
    var password: String? = null
    var name : String?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupFragmentBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getRegisterFactory = GenericViewModelFactory(RegisterViewModel::class) {
            RegisterViewModel(requireContext())
        }

        registerUserViewModel = ViewModelProvider(this, getRegisterFactory).get(RegisterViewModel::class.java)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignupScreenToLoginScreen())
        }

        etMobile = binding.etMobile
        etPassword = binding.etPassword
        etName = binding.etName

        setupMobileNumber()
        setupPassword()
        setupName()

        binding.btnSignup.setOnClickListener {
            // Hide previous error messages
            binding.etMobileError.visibility = View.GONE
            binding.etPasswordError.visibility = View.GONE
            binding.etNameError.visibility = View.GONE

            // Reset field background colors
            binding.etMobile.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
            binding.etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
            binding.etName.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)

            var isValid = true

            // Check if name is empty
            if (binding.etName.text.isNullOrEmpty()) {
                binding.etName.background = ContextCompat.getDrawable(requireContext(), R.drawable.red_border)
                binding.etNameError.visibility = View.VISIBLE
                isValid = false
            }

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
                mobileNumber = binding.etMobile.text.toString()
                password = binding.etPassword.text.toString()
                name = binding.etName.text.toString()
          //      isLoginInProgress = true // Set flag to indicate login is in progress

                callSignUpApi()
                observerSignUpViewModel()
            }
        }

    }

    private fun observerSignUpViewModel() {
        registerUserViewModel.registrationResponse.observe(viewLifecycleOwner){response->
            if (response.MsgStr.equals("Registered Successfully", ignoreCase = true)) {
                findNavController().navigate(SignUpFragmentDirections.actionSignupScreenToLoginScreen())
                //   showPopup()
        }
            else{

                Toast.makeText(requireContext(), "User Already exist", Toast.LENGTH_SHORT).show()
            }
        }

        registerUserViewModel.errorResponse.observe(viewLifecycleOwner){
            Log.e("Sign up Observer", "❌ Error response received: ${it.toString()}")

        }

    }

    private fun callSignUpApi() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            registerUserViewModel.registerUser(
                fullNameEn = name ?:"",
                address = "",
                emailID = "",
                contactMobileNum = mobileNumber?:"",
                nationalIDNo = "",
                loginStatus = 0,
                contactUserId = "",
                sex = "",
                dob = "",
                nationalityEn = "",
                nationalityAr = "",
                postalCode = "",
                username = name?:"",
                txtPassword = password?:"",
                mrn = "",
                fullNameAR = ""
            )
        }
    }



    private fun showPopup() {

        dialog = Dialog(requireContext())
        dialog?.setContentView(R.layout.confirmation_signup)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Important for rounded corners!


        dialog?.setCancelable(false)

        val homeBtn: Button? = dialog?.findViewById(R.id.btn_home)

        homeBtn?.setOnClickListener {

            findNavController().navigate(SignUpFragmentDirections.actionSignupScreenToLoginScreen())

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
    private fun setupName() {
        // Load drawable icons
        val defaultStartIcon: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.name_icon) // Grey icon
        val activeStartIcon: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.name_icon_green) // Green active icon
        val endIcon: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.check) // End icon (green check)

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()

                if (etName.background == ContextCompat.getDrawable(requireContext(), R.drawable.red_border)) {
                    etName.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)
                }

                if (text.length >= 3) {
                    // When exactly 3 digits, show active start icon & check mark end icon
                    etName.setCompoundDrawablesWithIntrinsicBounds(
                        activeStartIcon,
                        null,
                        endIcon,
                        null
                    )
                } else {
                    // Otherwise, show default start icon & remove end icon
                    etName.setCompoundDrawablesWithIntrinsicBounds(defaultStartIcon, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}