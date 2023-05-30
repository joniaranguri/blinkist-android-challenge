package com.blinkslabs.blinkist.android.challenge.ui.decorators

interface StickyHeaderSectionCallback {
    fun isSection(position: Int): Boolean
    fun getSectionHeader(position: Int): CharSequence
}