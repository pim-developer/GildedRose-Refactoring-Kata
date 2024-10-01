package com.gildedrose

open class Item(var name: String, var sellIn: Int, var quality: Int) {
    override fun toString(): String {
        return this.name + ", " + this.sellIn + ", " + this.quality
    }
}

enum class SpecialItemNames(val value: String) {
    SULFURAS("Sulfuras, Hand of Ragnaros"),
    AGED_BRIE("Aged Brie"),
    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert"),
    CONJURED("Conjured Mana Cake");
}

const val DEFAULT_MAXIMUM_ITEM_QUALITY = 50
const val MINIMUM_ITEM_QUALITY = 0
const val DEFAULT_QUALITY_DECREASE = -1
const val DEFAULT_QUALITY_INCREASE = DEFAULT_QUALITY_DECREASE * -1 // Inverse to decrease
const val BACKSTAGE_PASS_QUALITY_INCREASE_FINAL_DAYS = 3  // When 5 days or less
const val BACKSTAGE_PASS_QUALITY_INCREASE_SOON = 2       // When 10 days or less
const val DEFAULT_QUALITY_EXPIRED_MULTIPLIER = 2

fun Item.updateQuality() {
    // SULFURAS never changes
    if (name == SpecialItemNames.SULFURAS.value) return

    // Get how much each item should change in quality
    val qualityChange = when (name) {
        SpecialItemNames.AGED_BRIE.value -> getAgedBrieQualityChange()
        SpecialItemNames.BACKSTAGE_PASSES.value -> getBackstagePassesQualityChange()
//        SpecialItemNames.CONJURED.value -> getNormalItemQualityChange() * 2
        else -> getNormalItemQualityChange()
    }

    // Apply change in quality and sellIn
    quality = (quality + qualityChange).coerceIn(MINIMUM_ITEM_QUALITY, DEFAULT_MAXIMUM_ITEM_QUALITY)
    sellIn -= 1
}

private fun Item.getNormalItemQualityChange() = if (sellIn <= 0) {
    DEFAULT_QUALITY_DECREASE * DEFAULT_QUALITY_EXPIRED_MULTIPLIER // Decreases in quality twice as fast when SellIn has passed
} else {
    DEFAULT_QUALITY_DECREASE
}

private fun Item.getAgedBrieQualityChange() = if (sellIn <= 0) {
    DEFAULT_QUALITY_INCREASE * DEFAULT_QUALITY_EXPIRED_MULTIPLIER // Increases in quality twice as fast after SellIn has passed
} else {
    DEFAULT_QUALITY_INCREASE // Always increases in quality (default by 1)
}

private fun Item.getBackstagePassesQualityChange() = when {
    sellIn <= 0 -> -quality // Expired so minus the inverse to make 0
    sellIn <= 5 -> BACKSTAGE_PASS_QUALITY_INCREASE_FINAL_DAYS // Quality increases by 3 when there are 5 days or less
    sellIn <= 10 -> BACKSTAGE_PASS_QUALITY_INCREASE_SOON // Quality increases by 2 when there are 10 days or less
    else -> DEFAULT_QUALITY_INCREASE // Quality always increases unless expired (default by 1)
}
