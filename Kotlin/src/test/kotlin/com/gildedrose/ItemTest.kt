package com.gildedrose

import com.gildedrose.pricing.Sulfuras
import com.gildedrose.pricing.updateQuality
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class ItemTest {

    @Test
    fun `At the end of each day our system lowers both SellIn and Quality for every item`() {
        val item = Item("foo", 1, 1)
        val updatedItem: Item = item.updateQuality()
        assertEquals("foo", updatedItem.name)
        assertEquals(0, updatedItem.sellIn)
        assertEquals(0, updatedItem.quality)
    }

    @Test
    fun `Once the sell by date has passed, Quality degrades twice as fast`() {
        val item = Item("foo", 1, 9)
        val updatedItem1 = item.updateQuality()
        assertEquals(0, updatedItem1.sellIn)
        assertEquals(8, updatedItem1.quality)
        val updatedItem2 = updatedItem1.updateQuality()
        assertEquals(6, updatedItem2.quality)
    }

    @Test
    fun `The Quality of an item is never negative`() {
        val item = Item("foo", 0, 0)
        val newItem = item.updateQuality()
        assertEquals(0, newItem.quality)
    }

    @Test
    fun `"Aged Brie" actually increases in Quality the older it gets`() {
        val item = Item("Aged Brie", 0, 0)
        val newItem = item.updateQuality()
        assertEquals(2, newItem.quality)
    }

    @Test
    fun `The Quality of an item never becomes more than 50`() {
        val items = listOf(
                Item("Aged Brie", 10, 49),
                Item("Backstage passes to a TAFKAL80ETC concert", 10, 50)
        )
        val updatedItems = items.map { it.updateQuality() }
        updatedItems.forEach {
            assertEquals(50, it.quality)
        }

    }

    @Test
    fun `"Sulfuras", being a legendary item, never has to be sold or decreases in Quality`() {
        val item = Sulfuras
        val  newItem = item.updateQuality()
        assertEquals(Sulfuras.quality, newItem.quality)
    }

    @Test
    fun `"Backstage passes", like aged brie, increases in Quality as its SellIn value approaches`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 30, 10)
        val newItem = item.updateQuality()
        assertEquals(11, newItem.quality)
    }

    @Test
    fun `"Backstage passes" Quality increases by 2 when there are 10 days or less`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 10, 10)
        val newItem = item.updateQuality()
        assertEquals(12, newItem.quality)
    }

    @Test
    fun `"Backstage passes" Quality increases by 3 when there are 5 days or less`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 5, 10)
        val newItem = item.updateQuality()
        assertEquals(13, newItem.quality)
    }

    @Test
    fun `"Backstage passes" Quality drops to 0 after the concert`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 0, 10)
        val newItem = item.updateQuality()
        assertEquals(0, newItem.quality)
    }

    @Test
    fun `"Conjured" items degrade in Quality twice as fast as normal items`() {
        val item = Item("Conjured Gem", 1, 10)
        val newItem = item.updateQuality()
        assertEquals(8, newItem.quality)
        val newItem2 = newItem.updateQuality()
        assertEquals(4, newItem2.quality)
    }
}


