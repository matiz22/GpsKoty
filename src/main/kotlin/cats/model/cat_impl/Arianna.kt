package cats.model.cat_impl

import cats.model.ICat
import cats.model.IHuntingItem
import cats.model.PreferencesItem

class Arianna : ICat {
    override val preferences: Map<PreferencesItem, Double> = mapOf(
            PreferencesItem.FIELD_MOUSE to 0.125,
            PreferencesItem.HOUSE_MOUSE to 0.125,
            PreferencesItem.SNAIL to 0.375,
            PreferencesItem.ROCK to 0.0,
            PreferencesItem.LEAF to 0.375,
    )

    override val storage: MutableList<IHuntingItem> = mutableListOf()

}