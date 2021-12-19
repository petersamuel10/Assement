package com.church.nagwa.ui.view

import com.church.nagwa.data.model.ItemInfo

interface BookClickListener {
    fun bookClickListener(url: ItemInfo, position:Int)
}