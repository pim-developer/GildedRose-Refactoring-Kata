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

fun Item.updateQuality() {
    when (name) {
        SpecialItemNames.SULFURAS.value -> {
            // skip SULFURAS as it doesn't degrade or expire
            return
        }

        SpecialItemNames.AGED_BRIE.value -> {
            quality = if (sellIn <= 0) {
                // Increases in quality twice as fast after SellIn has passed
                (quality + 2).coerceAtMost(DEFAULT_MAXIMUM_ITEM_QUALITY)
            } else {
                // Always increases in quality (default by 1)
                (quality + 1).coerceAtMost(DEFAULT_MAXIMUM_ITEM_QUALITY)
            }
        }

        SpecialItemNames.BACKSTAGE_PASSES.value -> {
            quality = if (sellIn <= 0) {
                // Expired so 0
                MINIMUM_ITEM_QUALITY
            } else if (sellIn <= 5) {
                // Quality increases by 3 when there are 5 days or less
                (quality + 3).coerceAtMost(DEFAULT_MAXIMUM_ITEM_QUALITY)
            } else if (sellIn <= 10) {
                // Quality increases by 2 when there are 10 days or less
                (quality + 2).coerceAtMost(DEFAULT_MAXIMUM_ITEM_QUALITY)
            } else {
                // Always increases in quality unless expired (default by 1)
                (quality + 1).coerceAtMost(DEFAULT_MAXIMUM_ITEM_QUALITY)
            }
        }

        else -> {
            quality = if (sellIn <= 0) {
                (quality - 2).coerceAtLeast(MINIMUM_ITEM_QUALITY)
            } else {
                (quality - 1).coerceAtLeast(MINIMUM_ITEM_QUALITY)
            }
        }
    }
    sellIn -= 1
}