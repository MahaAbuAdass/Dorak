package com.example.dorak.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.ForgetPasswordLayoutBinding
import com.example.dorak.ui.HomeActivity

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding : ForgetPasswordLayoutBinding
    private lateinit var etMobile: EditText
    var mobileNumber: String? = null
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ForgetPasswordLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etMobile = binding.etMobile

        setupMobileNumber()

        binding.btnResetPass.setOnClickListener {
            // Hide previous error messages
            binding.etPasswordError.visibility = View.GONE

            // Reset field background colors
            binding.etMobile.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edit_text)

            var isValid = true

            // Check if mobile is empty
            if (binding.etMobile.text.isNullOrEmpty()) {
                binding.etMobile.background = ContextCompat.getDrawable(requireContext(), R.drawable.red_border)
                binding.etPasswordError.visibility = View.VISIBLE
                isValid = false
            }



            // If both fields are valid, show the popup
            if (isValid) {
                mobileNumber = binding.etMobile.text.toString()
               showPopup()

            }
        }
        binding.btnResetPass.setOnClickListener {
            findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordScreenToVerificationPasswordScreen())
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