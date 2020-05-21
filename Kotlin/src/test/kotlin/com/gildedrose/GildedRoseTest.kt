package com.gildedrose

import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

class GildedRoseTest {

    @Test
    fun `At the end of each day our system lowers both SellIn and Quality for every item`() {
        val items: Array<Item> = arrayOf(Item("foo", 1, 1))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("foo", app.items[0].name)
        assertEquals(0, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `Once the sell by date has passed, Quality degrades twice as fast`() {
        val items: Array<Item> = arrayOf(Item("foo", 1, 9))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(0, app.items[0].sellIn)
        assertEquals(8, app.items[0].quality)
        app.updateQuality()
        assertEquals(6, app.items[0].quality)
    }

    @Test
    fun `The Quality of an item is never negative`() {
        val items: Array<Item> = arrayOf(Item("foo", 0, 0))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `"Aged Brie" actually increases in Quality the older it gets`() {
        val items: Array<Item> = arrayOf(Item("Aged Brie", 0, 0))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(2, app.items[0].quality)

    }

    @Test
    fun  `The Quality of an item never becomes more than 50`() {
        val items: Array<Item> = arrayOf(Item("Aged Brie", 0, 49))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(50, app.items[0].quality)
    }

    @Test
    fun `"Sulfuras", being a legendary item, never has to be sold or decreases in Quality`(){
        val items: Array<Item> = arrayOf(Item("Sulfuras, Hand of Ragnaros", 0, 20))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(20, app.items[0].quality)
    }

    @Test
    fun `"Backstage passes", like aged brie, increases in Quality as its SellIn value approaches`() {
        val items: Array<Item> = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 30, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(11, app.items[0].quality)
    }

    @Test
    fun `"Backstage passes" Quality increases by 2 when there are 10 days or less`() {
        val items: Array<Item> = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 10, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(12, app.items[0].quality)
    }

    @Test
    fun `"Backstage passes" Quality increases by 3 when there are 5 days or less`() {
        val items: Array<Item> = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(13, app.items[0].quality)
    }

    @Test
    fun `"Backstage passes" Quality drops to 0 after the concert`() {
        val items: Array<Item> = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 0, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(0, app.items[0].quality)
    }

    @Ignore
    @Test
    fun `"Conjured" items degrade in Quality twice as fast as normal items`() {
        val items: Array<Item> = arrayOf(Item("Conjured Gem", 10, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(8, app.items[0].quality)
    }
}


