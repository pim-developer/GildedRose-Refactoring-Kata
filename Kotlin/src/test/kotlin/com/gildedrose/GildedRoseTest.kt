package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    @Test
    fun `Test items can be retrieved, and in the right order`() {
        val expectedItems = listOf(
            Item("foo", 0, 0),
            Item("bar", 0, 0)
        )
        val app = GildedRose(expectedItems)

        val actualItems = app.items
        assertEquals(expectedItems.size, actualItems.size, "The number of items should match")
        expectedItems.forEachIndexed { index, expectedItem ->
            val actualItem = actualItems[index]
            assertEquals(expectedItem.name, actualItem.name, "Item name at index $index should match")
            assertEquals(expectedItem.sellIn, actualItem.sellIn, "Item sellIn at index $index should match")
            assertEquals(expectedItem.quality, actualItem.quality, "Item quality at index $index should match")
        }
    }

}


