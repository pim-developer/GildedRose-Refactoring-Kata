package com.gildedrose

open class Item(var name: String, var sellIn: Int, var quality: Int) {
    override fun toString(): String {
        return this.name + ", " + this.sellIn + ", " + this.quality
    }
}

// a sealed class would be less performant
enum class SpecialItems(val value: String) {
    SULFURAS("Sulfuras, Hand of Ragnaros"),
    AGED_BRIE("Aged Brie"),
    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert"),
    CONJURED("Conjured Mana Cake");
}

private object QualityRules {
    const val MAX = 50
    const val MIN = 0
    const val DEFAULT_DECREASE = -1
    const val DEFAULT_INCREASE = DEFAULT_DECREASE * -1  // Inverse to decrease in quality
    const val BACKSTAGE_INCREASE_SOON = 2               // When 5 days or less
    const val BACKSTAGE_INCREASE_FINAL = 3              // When 10 days or less
    const val EXPIRED_MULTIPLIER = 2
}

fun Item.processEndOfDay() {
    updateQuality()
    updateSellIn()
}

private fun Item.updateQuality() {
    val qualityChangeToBeApplied = when (name) {
        SpecialItems.SULFURAS.value -> return  // "Sulfuras", being a legendary item, never has to be sold or decreases in Quality

        SpecialItems.AGED_BRIE.value -> getItemQualityChange(
            change = QualityRules.DEFAULT_INCREASE, // "Aged Brie" actually increases in Quality the older it gets
        )

        SpecialItems.CONJURED.value -> getItemQualityChange(
            change = QualityRules.DEFAULT_DECREASE * 2, // "Conjured" items degrade in Quality twice as fast as normal items
        )

        SpecialItems.BACKSTAGE_PASSES.value -> {
            when (sellIn) {
                in Int.MIN_VALUE..0 -> {
                    -quality // Quality drops to 0 after the concert (multiply by -1 so the change value will negate the quality to 0)
                }

                in 1..5 -> {
                    QualityRules.BACKSTAGE_INCREASE_FINAL // Quality increases by 3 when there are 5 days or less
                }

                in 6..10 -> {
                    QualityRules.BACKSTAGE_INCREASE_SOON // Quality increases by 2 when there are 10 days or less
                }

                else -> {
                    getItemQualityChange(
                        change = QualityRules.DEFAULT_INCREASE, // "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches
                    )
                }
            }
        }

        else -> getItemQualityChange()
    }
    this.quality = (this.quality + qualityChangeToBeApplied)
        .coerceIn(
            minimumValue = QualityRules.MIN,// The Quality of an item is never negative
            maximumValue = QualityRules.MAX //The Quality of an item is never more than 50
        )
}

/**
 *
 * Multiplies `change` with `isExpiredMultiplier` if this `Item`'s `sellIn` date is `0` or less. Otherwise just returns `change`.
 *
 * @return the change at end of day to be applied to the `Item`'s `quality`.
 * @param change is `-1` by default (decrease in quality).
// * @param isExpiredMultiplier is `2` by default (decreases in quality twice as fast if `sellIn` is `0` or less). TODO: Can be implemented if needed
 * */
private fun Item.getItemQualityChange(
    change: Int = QualityRules.DEFAULT_DECREASE,
//    isExpiredMultiplier: Int = QualityRules.EXPIRED_MULTIPLIER, TODO: Can be implemented if needed
): Int = if (this.sellIn <= 0) {
    change * QualityRules.EXPIRED_MULTIPLIER
} else {
    change
}

private fun Item.updateSellIn() {
    this.sellIn += when (name) {
        SpecialItems.SULFURAS.value -> return  // "Sulfuras", being a legendary item, never has to be sold or decreases in Quality
        else -> -1
    }
}