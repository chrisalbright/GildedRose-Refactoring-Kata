package com.gildedrose.pricing

import com.gildedrose.Item

val Sulfuras = Item("Sulfuras, Hand of Ragnaros", 1, 80)
const val MinQuality = 0
const val MaxQuality = 50
private fun limitMinQuality(q: Int): Int = q.coerceAtLeast(MinQuality)
private fun limitMaxQuality(q: Int): Int = q.coerceAtMost(MaxQuality)

interface Pricing<T> {
    fun update(item: T): T
}

private object DefaultPricing : Pricing<Item> {
    override fun update(item: Item): Item =
            if (item.sellIn <= 0) {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 2))
            } else {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 1))
            }
}

private object ConjuredPricing : Pricing<Item> {
    override fun update(item: Item): Item =
            if (item.sellIn <= 0) {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 4))
            } else {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 2))
            }
}

private object BackstagePassPricing : Pricing<Item> {
    override fun update(item: Item): Item {
        return if (item.sellIn <= 0) {
            Item(item.name, sellIn = item.sellIn - 1, quality = 0)
        } else if (item.sellIn <= 5) {
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 3))
        } else if (item.sellIn <= 10) {
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 2))
        } else {
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 1))
        }
    }
}

private object AgedBriePricing : Pricing<Item> {
    override fun update(item: Item): Item =
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 2))
}

private object SulfurasPricing : Pricing<Item> {
    override fun update(item: Item): Item = Sulfuras
}

object GildedRosePricing : Pricing<Item> {

    private fun pricingFor(item: Item): Pricing<Item> =
            when {
                item.name.contains("Backstage passes") -> {
                    BackstagePassPricing
                }
                item.name.contains("Sulfuras") -> {
                    SulfurasPricing
                }
                item.name.contains("Aged Brie") -> {
                    AgedBriePricing
                }
                item.name.contains("Conjured") -> {
                    ConjuredPricing
                }
                else -> {
                    DefaultPricing
                }
            }

    override fun update(item: Item): Item = pricingFor(item).update(item)
}
