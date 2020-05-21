package com.gildedrose

import com.gildedrose.pricing.GildedRosePricing
import com.gildedrose.pricing.Pricing
import com.gildedrose.pricing.Sulfuras
import org.junit.Assert.assertEquals
import org.junit.Test

class GildedRoseTest {

    @Test
    fun `Gilded Rose Adjusts Pricing`() {
        val items: Array<Item> = arrayOf(Item("foo", 1, 1))
        val pricedItems = mutableListOf<Item>()
        val testPricing = object : Pricing<Item> {
            override fun update(item: Item): Item {
                pricedItems += item
                return item
            }
        }
        val app = GildedRose(items, testPricing)
        app.updateQuality()
        assertEquals(items.toMutableList(), pricedItems)
    }

    @Test
    fun `At the end of each day our system lowers both SellIn and Quality for every item`() {
        val item = Item("foo", 1, 1)
        val updatedItem: Item = GildedRosePricing.update(item)
        assertEquals("foo", updatedItem.name)
        assertEquals(0, updatedItem.sellIn)
        assertEquals(0, updatedItem.quality)
    }

    @Test
    fun `Once the sell by date has passed, Quality degrades twice as fast`() {
        val item = Item("foo", 1, 9)
        val updatedItem1 = GildedRosePricing.update(item)
        assertEquals(0, updatedItem1.sellIn)
        assertEquals(8, updatedItem1.quality)
        val updatedItem2 = GildedRosePricing.update(updatedItem1)
        assertEquals(6, updatedItem2.quality)
    }

    @Test
    fun `The Quality of an item is never negative`() {
        val item = Item("foo", 0, 0)
        val newItem = GildedRosePricing.update(item)
        assertEquals(0, newItem.quality)
    }

    @Test
    fun `"Aged Brie" actually increases in Quality the older it gets`() {
        val item = Item("Aged Brie", 0, 0)
        val newItem = GildedRosePricing.update(item)
        assertEquals(2, newItem.quality)
    }

    @Test
    fun `The Quality of an item never becomes more than 50`() {
        val items = listOf(
                Item("Aged Brie", 10, 49),
                Item("Backstage passes to a TAFKAL80ETC concert", 10, 50)
        )
        val updatedItems = items.map { GildedRosePricing.update(it) }
        updatedItems.forEach {
            assertEquals(50, it.quality)
        }

    }

    @Test
    fun `"Sulfuras", being a legendary item, never has to be sold or decreases in Quality`() {
        val item = Sulfuras
        val newItem = GildedRosePricing.update(item)
        assertEquals(Sulfuras.quality, newItem.quality)
    }

    @Test
    fun `"Backstage passes", like aged brie, increases in Quality as its SellIn value approaches`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 30, 10)
        val newItem = GildedRosePricing.update(item)
        assertEquals(11, newItem.quality)
    }

    @Test
    fun `"Backstage passes" Quality increases by 2 when there are 10 days or less`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 10, 10)
        val newItem = GildedRosePricing.update(item)
        assertEquals(12, newItem.quality)
    }

    @Test
    fun `"Backstage passes" Quality increases by 3 when there are 5 days or less`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 5, 10)
        val newItem = GildedRosePricing.update(item)
        assertEquals(13, newItem.quality)
    }

    @Test
    fun `"Backstage passes" Quality drops to 0 after the concert`() {
        val item = Item("Backstage passes to a TAFKAL80ETC concert", 0, 10)
        val newItem = GildedRosePricing.update(item)
        assertEquals(0, newItem.quality)
    }

    @Test
    fun `"Conjured" items degrade in Quality twice as fast as normal items`() {
        val item = Item("Conjured Gem", 1, 10)
        val newItem = GildedRosePricing.update(item)
        assertEquals(8, newItem.quality)
        val newItem2 = GildedRosePricing.update(newItem)
        assertEquals(4, newItem2.quality)
    }
}


