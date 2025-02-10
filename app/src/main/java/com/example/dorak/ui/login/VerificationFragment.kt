package com.example.dorak.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.VerificationFragmentBinding
import com.google.android.material.card.MaterialCardView

class VerificationFragment : Fragment() {

    private lateinit var binding: VerificationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VerificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextCode1 = binding.editTextCode1
        val editTextCode2 = binding.editTextCode2
        val editTextCode3 = binding.editTextCode3
        val editTextCode4 = binding.editTextCode4

        val cardView1 = binding.cardView1
        val cardView2 = binding.cardView2
        val cardView3 = binding.cardView3
        val cardView4 = binding.cardView4

        val editTexts = arrayOf(editTextCode1, editTextCode2, editTextCode3, editTextCode4)

        editTexts.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && index < editTexts.size - 1) {
                        editTexts[index + 1].requestFocus() // Move to next EditText
                    } else if (s?.isEmpty() == true && index > 0) {
                        editTexts[index - 1].requestFocus() // Move back to previous EditText if deleted
                    }
                    checkFieldsAndUpdateStroke(cardView1, cardView2, cardView3, cardView4, editTexts)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        binding.btnVerify.setOnClickListener {
            // Check if all EditTexts are filled
            if (editTexts.all { it.text.isNotEmpty() }) {
                findNavController().navigate(VerificationFragmentDirections.actionVerificationPasswordScreenToNewPassword())
            } else {
                // Show toast message if any EditText is empty
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFieldsAndUpdateStroke(
        cardView1: MaterialCardView,
        cardView2: MaterialCardView,
        cardView3: MaterialCardView,
        cardView4: MaterialCardView,
        editTexts: Array<EditText>
    ) {
        val allFilled = editTexts.all { it.text.isNotEmpty() }
        val strokeColor = if (allFilled) {
            ContextCompat.getColor(requireContext(), R.color.green)
        } else {
            ContextCompat.getColor(requireContext(), R.color.light_grey)
        }

        cardView1.strokeColor = strokeColor
        cardView2.strokeColor = strokeColor
        cardView3.strokeColor = strokeColor
        cardView4.strokeColor = strokeColor
    }
}
