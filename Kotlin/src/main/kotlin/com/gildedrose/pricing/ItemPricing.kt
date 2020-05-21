package com.gildedrose.pricing

import com.gildedrose.Item

fun Item.copy(name: String = this.name, sellIn: Int = this.sellIn, quality: Int = this.quality): Item = Item(name, sellIn, quality)

val Sulfuras = Item("Sulfuras, Hand of Ragnaros", 1, 80)

private interface DecrementingQuality : Pricing<Item> {
    override fun update(item: Item): Item = decrement(item).limitMinQuality()
    fun decrement(item: Item): Item

    companion object {
        const val MinQuality = 0
        private fun Item.limitMinQuality(): Item = copy(quality = quality.coerceAtLeast(MinQuality))
    }
}

private interface IncrementingQuality : Pricing<Item> {
    override fun update(item: Item): Item = increment(item).limitMaxQuality()
    fun increment(item: Item): Item

    companion object {
        const val MaxQuality = 50
        private fun Item.limitMaxQuality(): Item = copy(quality = quality.coerceAtMost(MaxQuality))
    }
}

private object DefaultPricing : DecrementingQuality {
    override fun decrement(item: Item): Item =
            if (item.sellIn <= 0) {
                item.copy(sellIn = item.sellIn - 1, quality = item.quality - 2)
            } else {
                item.copy(sellIn = item.sellIn - 1, quality = item.quality - 1)
            }
}

private object ConjuredPricing : DecrementingQuality {
    override fun decrement(item: Item): Item =
            if (item.sellIn <= 0) {
                item.copy(sellIn = item.sellIn - 1, quality = item.quality - 4)
            } else {
                item.copy(sellIn = item.sellIn - 1, quality = item.quality - 2)
            }
}

private object BackstagePassPricing : IncrementingQuality {
    override fun increment(item: Item): Item {
        return if (item.sellIn <= 0) {
            Item(item.name, sellIn = item.sellIn - 1, quality = 0)
        } else if (item.sellIn <= 5) {
            Item(item.name, sellIn = item.sellIn - 1, quality = item.quality + 3)
        } else if (item.sellIn <= 10) {
            Item(item.name, sellIn = item.sellIn - 1, quality = item.quality + 2)
        } else {
            Item(item.name, sellIn = item.sellIn - 1, quality = item.quality + 1)
        }
    }
}

private object AgedBriePricing : IncrementingQuality {
    override fun increment(item: Item): Item =
            Item(item.name, sellIn = item.sellIn - 1, quality = item.quality + 2)
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
