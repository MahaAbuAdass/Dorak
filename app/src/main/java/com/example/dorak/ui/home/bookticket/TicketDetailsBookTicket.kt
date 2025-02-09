package com.example.dorak.ui.home.bookticket

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dorak.R
import com.example.dorak.databinding.TicketDetailsBookTicketBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.home.TicketDetailsArgs
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.AppointmentInfoViewModel
import com.example.dorak.viewmodels.GenerateTicketViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TicketDetailsBookTicket : Fragment() {

    private lateinit var binding: TicketDetailsBookTicketBinding
    private lateinit var generateTicketViewModel: GenerateTicketViewModel
    private lateinit var appointmentInfoViewModel: AppointmentInfoViewModel

    private val args: TicketDetailsBookTicketArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TicketDetailsBookTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val getTicketAppointmentFactory = GenericViewModelFactory(AppointmentInfoViewModel::class) {
            AppointmentInfoViewModel(requireContext())
        }

        appointmentInfoViewModel =
            ViewModelProvider(this, getTicketAppointmentFactory).get(AppointmentInfoViewModel::class.java)

        callGenerateTicketApi()
        observerGenerateTicketViewModel()

        binding.btnMyTicket.setOnClickListener {
            findNavController().navigate(TicketDetailsBookTicketDirections.actionTicketDetailsBookTicketFragmentToNavMyTicket())
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun observerGenerateTicketViewModel() {

        appointmentInfoViewModel.appointmentResponse.observe(viewLifecycleOwner) { ticket ->
            Log.v("response for API is", ticket.toString())

            binding.tvBranchEn.text = ticket.BranchNameEn
            binding.tvBranchAr.text = ticket.BranchNameAr
            binding.tvTicketNumber.text = ticket.ApptNo
            binding.tvSelectedService.text = ticket.QNameEn
            binding.tvSelectedServiceAr.text = ticket.QNameAr


            val ticket_id = ticket.ApptNo
            //           val ticket_id= "19991"
            val qrBitmap = generateQRCode(ticket_id ?: "000")

            if (qrBitmap != null) {
                binding.imageViewQr.setImageBitmap(qrBitmap)
            }
        }


        appointmentInfoViewModel.errorResponse.observe(viewLifecycleOwner) {
            Log.v("error generating the ticket", it.toString())
        }

        binding.btnDownloadTicket.setOnClickListener {
            val cardView = binding.cardViewTicket1 // Replace with your CardView ID
            saveCardViewAsImage(requireContext(), cardView, "Ticket_Screenshot")
        }

    }

    private fun callGenerateTicketApi() {
        val app_no = args.appno

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            appointmentInfoViewModel.getAppoitmentInfo(app_no?:"")
        }
    }

    fun generateQRCode(id: String): Bitmap? {
        return try {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                id, // ID to be encoded in the QR Code
                BarcodeFormat.QR_CODE,
                500, 500  // Width & Height of QR Code
            )
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun saveCardViewAsImage(context: Context, view: View, fileName: String) {
        try {
            // Create a bitmap from the CardView
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            // Save image to gallery using MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/MyAppImages"
                )
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            uri?.let {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                outputStream?.flush()
                outputStream?.close()
            }

            Toast.makeText(context, "Ticket saved to gallery!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}

