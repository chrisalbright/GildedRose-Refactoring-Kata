package com.gildedrose.pricing

import com.gildedrose.Item

fun Item.updateQuality(): Item = pricingStrategyFor(this).updateItem(this)

val Sulfuras = Item("Sulfuras, Hand of Ragnaros", 1, 80)
const val MinQuality = 0
const val MaxQuality = 50
private fun limitMinQuality(q: Int): Int = q.coerceAtLeast(MinQuality)
private fun limitMaxQuality(q: Int): Int = q.coerceAtMost(MaxQuality)

interface PricingStrategy {
    fun updateItem(item: Item): Item
}

private class DefaultPricingStrategy : PricingStrategy {
    override fun updateItem(item: Item): Item =
            if (item.sellIn <= 0) {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 2))
            } else {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 1))
            }
}

private class ConjuredPricingStrategy : PricingStrategy {
    override fun updateItem(item: Item): Item =
            if (item.sellIn <= 0) {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 4))
            } else {
                Item(item.name, sellIn = item.sellIn - 1, quality = limitMinQuality(item.quality - 2))
            }
}

private class BackstagePassPricingStrategy : PricingStrategy {
    override fun updateItem(item: Item): Item {
        return if (item.sellIn <= 0){
            Item(item.name, sellIn = item.sellIn - 1, quality = 0)
        } else if(item.sellIn <= 5) {
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 3))
        } else if (item.sellIn <= 10) {
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 2))
        } else {
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 1))
        }
    }
}

private class AgedBriePricingStrategy : PricingStrategy {
    override fun updateItem(item: Item): Item =
            Item(item.name, sellIn = item.sellIn - 1, quality = limitMaxQuality(item.quality + 2))
}

private class SulfurasPricingStrategy : PricingStrategy {
    override fun updateItem(item: Item): Item = Sulfuras
}

fun pricingStrategyFor(item: Item): PricingStrategy {
    return if (item.name.contains("Backstage passes")) {
        BackstagePassPricingStrategy()
    } else if (item.name.contains("Sulfuras")) {
        SulfurasPricingStrategy()
    } else if (item.name.contains("Aged Brie")) {
        AgedBriePricingStrategy()
    } else if (item.name.contains("Conjured")) {
        ConjuredPricingStrategy()
    } else {
        DefaultPricingStrategy()
    }
}
