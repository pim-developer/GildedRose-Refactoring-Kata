package com.gildedrose

class GildedRose(var items: List<Item>) {

    fun updateQuality() {
        items.forEach { item ->
            item.updateQuality()
            return@forEach

            // unreachable
            // TODO: Remove slightly refactored legacy code
            if (item.name == SpecialItemNames.SULFURAS.value) {
                return@forEach
            }

            if (item.name != SpecialItemNames.AGED_BRIE.value && item.name != SpecialItemNames.BACKSTAGE_PASSES.value) {
                if (item.quality > 0) {
                    item.quality -= 1
                }
            } else {
                if (item.quality < 50) {
                    item.quality += 1

                    if (item.name == SpecialItemNames.BACKSTAGE_PASSES.value) {
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
                if (item.name == SpecialItemNames.AGED_BRIE.value) {
                    item.quality = (item.quality + 1).coerceAtMost(50)
                } else if (item.name == SpecialItemNames.BACKSTAGE_PASSES.value) {
                    item.quality = 0
                } else if (item.quality > 0) {
                    item.quality -= 1
                }
            }
        }
    }
}
