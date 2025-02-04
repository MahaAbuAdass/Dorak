package com.example.dorak.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.imageview.ShapeableImageView

class BannerAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ShapeableImageView(parent.context)
        val marginPx = 60 // Adjust this for spacing between images

        // Set layout params to fill the entire ViewPager2 (MATCH_PARENT)
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,  // Width set to MATCH_PARENT
            ViewGroup.LayoutParams.MATCH_PARENT   // Height set to MATCH_PARENT
        ).apply {
            setMargins(marginPx, 0, marginPx, 0) // Add space between images
        }

        imageView.layoutParams = layoutParams

        // Set the corner radius for the image
        val shapeAppearance = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, 30f) // Adjust 30f to change corner radius
            .build()
        imageView.shapeAppearanceModel = shapeAppearance

        return BannerViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size

    class BannerViewHolder(val imageView: ShapeableImageView) : RecyclerView.ViewHolder(imageView)
}
