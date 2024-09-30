package com.gildedrose

class GildedRose(var items: List<Item>) {

    fun updateQuality() {
        items.forEach { item ->

            // skip Sulfuras: it doesn't degrade or expire
            if (item.name == SpecialItemNames.SULFURAS.value) {
                return@forEach
            }

            if (item.name != "Aged Brie" && item.name != "Backstage passes to a TAFKAL80ETC concert") {
                if (item.quality > 0) {
                    item.quality = item.quality - 1
                }
            } else {
                if (item.quality < 50) {
                    item.quality += 1

                    if (item.name == "Backstage passes to a TAFKAL80ETC concert") {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality += 1
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality += 1
                            }
                        }
                    }
                }
            }

            item.sellIn -= 1

            if (item.sellIn < 0) {
                if (item.name != "Aged Brie") {
                    if (item.name != "Backstage passes to a TAFKAL80ETC concert") {
                        if (item.quality > 0) {
                            item.quality -= 1
                        }
                    } else {
                        item.quality -= item.quality
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality += 1
                    }
                }
            }
        }
    }

}

