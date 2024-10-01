package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


internal class GildedRoseTest {

    @Test
    fun `Test items can be retrieved, and in the right order`() {
        val expectedItems = listOf(
            Item("foo", 0, 0),
            Item("bar", 0, 0),
            Item("baz", 0, 0)
        )
        val app = GildedRose(expectedItems)

        val actualItems = app.items
        assertEquals(expectedItems.size, actualItems.size, "The number of items should match")
        expectedItems.forEachIndexed { index, expectedItem ->
            val actualItem = actualItems[index]
            assertEquals(
                expectedItem.name,
                actualItem.name,
                "Item name at index $index should match"
            )
            assertEquals(
                expectedItem.sellIn,
                actualItem.sellIn,
                "Item sellIn at index $index should match"
            )
            assertEquals(
                expectedItem.quality,
                actualItem.quality,
                "Item quality at index $index should match"
            )
            assertTrue(
                expectedItem === actualItem,
                "Item reference at index $index should match"
            )
        }
    }


    @Test
    fun `Test normal item has lowered SellIn and Quality at end of day`() {
        val items = listOf(
            Item("foo", 1, 2),
            Item("bar", 3, 4),
        )

        val app = GildedRose(items)

        // simulate end of day
        app.updateQuality()

        assertEquals(
            0,
            items[0].sellIn,
            "Item sellIn for `foo` should be lowered by 1"
        )
        assertEquals(
            1,
            items[0].quality,
            "Item quality for `foo` should be lowered by 1"
        )

        assertEquals(
            2,
            items[1].sellIn,
            "Item sellIn for `bar` should be lowered by 1"
        )
        assertEquals(
            3,
            items[1].quality,
            "Item sellIn for `bar` should be lowered by 1"
        )
    }

    @Test
    fun `Test normal item has lowered SellIn and Quality after multiple days (2)`() {
        val items = listOf(
            Item("foo", 2, 3),
            Item("bar", 4, 5),
        )

        val app = GildedRose(items)

        // simulate end of day 0
        app.updateQuality()
        // simulate end of day 1
        app.updateQuality()

        assertEquals(
            0,
            items[0].sellIn,
            "Item sellIn for `foo` should be lowered by 2"
        )
        assertEquals(
            1,
            items[0].quality,
            "Item quality for `foo` should be lowered by 2"
        )

        assertEquals(
            2,
            items[1].sellIn,
            "Item sellIn for `bar` should be lowered by 2"
        )
        assertEquals(
            3,
            items[1].quality,
            "Item sellIn for `bar` should be lowered by 2"
        )
    }

    /* WIP: (BOOKMARK) - use subclasses for items*/
    @Test
    fun `Test item degrades Quality twice as fast after SellIn has passed`() {
        val items = listOf(
            Item("foo", 0, 10), // normal item at sell-by date
            Item("bar", -1, 8), // normal item past sell-by date
            Item("Conjured Mana Cake", -1, 8)  // conjured item past sell-by date
        )

        val app = GildedRose(items)

        // simulate end of day
        app.updateQuality()

        assertEquals(
            -1,
            items[0].sellIn,
            "Item sellIn for `foo` should be lowered by 1"
        )
        assertEquals(
            8,
            items[0].quality,
            "Item quality for `foo` should be lowered by 2 (twice as fast after SellIn passed)"
        )

        assertEquals(
            -2,
            items[1].sellIn,
            "Item sellIn for `bar` should be lowered by 1"
        )
        assertEquals(
            6,
            items[1].quality,
            "Item quality for `bar` should be lowered by 2 (already past SellIn)"
        )

        assertEquals(
            -2,
            items[2].sellIn,
            "Item sellIn for conjured should be lowered by 1"
        )
//        assertEquals( /* WIP: (BOOKMARK) - REENABLE */
//            4,
//            items[2].quality,
//            "Item quality for conjured should be lowered by 4 (past SellIn so degrades twice as fast, also degrades twice as fast for being conjured)"
//        )
    }

    /* WIP: (BOOKMARK) - use suclasses for items */
    @Test
    fun `Test the Quality of a normal item is never negative`() {
        val items = listOf(
            Item("foo", 1, 0),  // normal item with Quality already at 0
            Item("Conjured Mana Cake", 0, 1),  // conjured past sell-by date with low Quality
            Item("bar", -1, 1)  // normal item past sell-by date with low Quality
        )

        val app = GildedRose(items)

        // simulate end of day
        app.updateQuality()

        assertEquals(
            0,
            items[0].quality,
            "Item quality for `foo` should remain at 0 and never be negative"
        )

        assertEquals(
            0,
            items[1].quality,
            "Item quality for `bar` should not drop below 0"
        )

        assertEquals(
            0,
            items[2].quality,
            "Item quality for conjured should not drop below 0"
        )
    }

    /* WIP: (BOOKMARK) - use subclass for Aged Brie */
    @Test
    fun `Test 'Aged Brie' increases in Quality as it gets older`() {
        val items = listOf(
            Item("Aged Brie", 2, 0),  // Aged Brie with initial Quality of 0
            Item("Aged Brie", 1, 5)   // Aged Brie with initial Quality of 5
        )

        val app = GildedRose(items)

        // simulate end of day 0
        app.updateQuality()

        assertEquals(
            1,
            items[0].sellIn,
            "SellIn for Aged Brie should be decreased by 1 after 1 days"
        )
        assertEquals(
            1,
            items[0].quality,
            "Quality for Aged Brie should increase from 0 to 1 after 1 days"
        )

        assertEquals(
            0,
            items[1].sellIn,
            "SellIn for Aged Brie should be decreased by 1 after 1 days"
        )
        assertEquals(
            6,
            items[1].quality,
            "Quality for Aged Brie should increase by 1 after 1 days"
        )

        // simulate end of day 1
        app.updateQuality()

        assertEquals(
            0,
            items[0].sellIn,
            "SellIn for Aged Brie should be decreased by 2 after 2 days"
        )
        assertEquals(
            2,
            items[0].quality,
            "Quality for Aged Brie should increase from 0 to 2 after 2 days"
        )

        assertEquals(
            -1,
            items[1].sellIn,
            "SellIn for Aged Brie should be decreased by 2 after 2 days"
        )
        assertEquals(
            8,
            items[1].quality,
            "Quality for Aged Brie should increase from 5 to 8 after 2 days, increasing in quality twice as fast after SellIn passed"
        )
    }

    /* WIP: (BOOKMARK) - use subclass for Sulfuras */
    @Test
    fun `Test 'Sulfuras, Hand of Ragnaros' never decreases in Quality or SellIn`() {
        val items = listOf(
            Item("Sulfuras, Hand of Ragnaros", 0, 80), // Sulfuras with initial SellIn and Quality
            Item(
                "Sulfuras, Hand of Ragnaros",
                -1,
                100
            ) // Another Sulfuras with different SellIn and Quality
        )

        val app = GildedRose(items)

        // simulate end of day
        app.updateQuality()

        assertEquals(
            0,
            items[0].sellIn,
            "SellIn for Sulfuras should remain unchanged"
        )
        assertEquals(
            80,
            items[0].quality,
            "Quality for Sulfuras should remain unchanged"
        )

        assertEquals(
            -1,
            items[1].sellIn,
            "SellIn for Sulfuras should remain unchanged"
        )
        assertEquals(
            100,
            items[1].quality,
            "Quality for Sulfuras should remain unchanged"
        )
    }

    /* WIP: (BOOKMARK) - use subclass for Backstage passes */
    @Test
    fun `Test 'Backstage passes to a TAFKAL80ETC concert' increases in Quality as SellIn approaches`() {
        val items = listOf(
            Item("Backstage passes to a TAFKAL80ETC concert", 15, 10), // More than 10 days
            Item("Backstage passes to a TAFKAL80ETC concert", 10, 20), // 10 days or less
            Item("Backstage passes to a TAFKAL80ETC concert", 5, 30),  // 5 days or less
            Item("Backstage passes to a TAFKAL80ETC concert", 0, 40)   // Concert day
        )

        val app = GildedRose(items)

        // simulate end of day 0
        app.updateQuality()

        assertEquals(
            14,
            items[0].sellIn,
            "SellIn for Backstage passes with over 10 days left  should decrease by 1"
        )
        assertEquals(
            11,
            items[0].quality,
            "Quality for Backstage passes with over 10 days left should increase by 1"
        )

        assertEquals(
            9,
            items[1].sellIn,
            "SellIn for Backstage passes should decrease by 1"
        )
        assertEquals(
            22,
            items[1].quality,
            "Quality for Backstage passes should increase by 2 (10 days or less)"
        )

        assertEquals(
            4,
            items[2].sellIn,
            "SellIn for Backstage passes should decrease by 1"
        )
        assertEquals(
            33,
            items[2].quality,
            "Quality for Backstage passes should increase by 3 (5 days or less)"
        )

        assertEquals(
            -1,
            items[3].sellIn,
            "SellIn for Backstage passes should decrease by 1"
        )
        assertEquals(
            0,
            items[3].quality,
            "Quality for Backstage passes should drop to 0 after the concert"
        )
    }

    /* WIP: (BOOKMARK) - use subclass for items*/
    @Disabled
    @Test
    fun `Test Conjured items degrade in Quality twice as fast as normal items`() {
        val items = listOf(
            Item("Conjured Mana Cake", 1, 6), // Conjured item
            Item("Normal Item", 3, 6)         // Normal item
        )

        val app = GildedRose(items)

        // simulate end of day 0
        app.updateQuality()

        assertEquals(
            0,
            items[0].sellIn,
            "SellIn for Conjured item should decrease by 1"
        )
        assertEquals(
            4,
            items[0].quality,
            "Quality for Conjured item should decrease by 2 (degrades twice as fast)"
        )

        assertEquals(
            2,
            items[1].sellIn,
            "SellIn for normal item should decrease by 1"
        )
        assertEquals(
            5,
            items[1].quality,
            "Quality for normal item should decrease by 1"
        )
    }

}


