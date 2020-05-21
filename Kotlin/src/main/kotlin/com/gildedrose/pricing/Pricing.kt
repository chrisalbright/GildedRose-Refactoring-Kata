package com.gildedrose.pricing

interface Pricing<T> {
    fun update(item: T): T
}
