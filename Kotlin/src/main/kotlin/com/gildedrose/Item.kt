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

// Subclass for Sulfuras Item
class SulfurasItem(
    sellIn: Int,
    quality: Int
) : Item(SpecialItemNames.SULFURAS.value, sellIn, quality)

// Subclass for Aged Brie Item
class AgedBrieItem(
    sellIn: Int,
    quality: Int
) : Item(SpecialItemNames.AGED_BRIE.value, sellIn, quality)

// Subclass for Backstage Passes Item
class BackstagePassesItem(
    sellIn: Int,
    quality: Int
) : Item(SpecialItemNames.BACKSTAGE_PASSES.value, sellIn, quality)

// Subclass for Conjured Item
class ConjuredItem(
    sellIn: Int,
    quality: Int
) : Item(SpecialItemNames.CONJURED.value, sellIn, quality)

// Subclass for Conjured Item
class NormalItem(
    name: String,
    sellIn: Int,
    quality: Int
) : Item(name, sellIn, quality)

