package com.vanniktech.emoji.ios

import com.vanniktech.emoji.EmojiProvider
import com.vanniktech.emoji.emoji.EmojiCategory
import com.vanniktech.emoji.ios.category.ActivityCategory
import com.vanniktech.emoji.ios.category.FlagsCategory
import com.vanniktech.emoji.ios.category.FoodCategory
import com.vanniktech.emoji.ios.category.NatureCategory
import com.vanniktech.emoji.ios.category.ObjectsCategory
import com.vanniktech.emoji.ios.category.PeopleCategory
import com.vanniktech.emoji.ios.category.SymbolsCategory
import com.vanniktech.emoji.ios.category.TravelCategory

class IosEmojiProvider : EmojiProvider {
    override fun getCategories(): Array<EmojiCategory> {
        return arrayOf(PeopleCategory(), NatureCategory(), FoodCategory(), ActivityCategory(),
                       TravelCategory(), ObjectsCategory(), SymbolsCategory(), FlagsCategory())
    }
}
