package com.example.dorak.ui.home

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class BannerPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val scaleFactor = 0.85f + (1 - abs(position)) * 0.15f

        // Move pages closer together
        page.translationX = -position * pageWidth * 0.2f

        // Apply scale effect
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor

        // Fade out images when they are further away
        page.alpha = 0.5f + (1 - abs(position)) * 0.5f
    }
}
