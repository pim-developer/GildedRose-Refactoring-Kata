package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    fun `Test default (not special) item has lowered SellIn and Quality at end of day`() {
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
    fun `Test default (not special) item has lowered SellIn and Quality after multiple days (2)`() {
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

    @Test
    fun `Test default item degrades Quality twice as fast after SellIn has passed`() {
        val items = listOf(
            Item("foo", 0, 10), // Item at sell-by date
            Item("bar", -1, 8)  // Item past sell-by date
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
    }

}


