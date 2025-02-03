package com.example.dorak.ui

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class BannerPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val offset = 40f  // Margin to show part of the next and previous image

        // Ensure the image is shifted horizontally as you scroll
        page.translationX = -position * pageWidth

        // Adjust alpha to make images fade in/out smoothly
        page.alpha = 1 - Math.abs(position)

        // Scale the images to provide a depth effect as you scroll
        val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor

        // Apply margins (left for previous, right for next) on the image views
        if (position < -1) { // Off to the left of the screen
            page.translationX = (-pageWidth).toFloat()
        } else if (position <= 1) {
            // Adjust the page position to show the next/previous part
            page.translationX = -position * (pageWidth - offset)
        } else { // Off to the right of the screen
            page.translationX = pageWidth.toFloat()
        }
    }
}
