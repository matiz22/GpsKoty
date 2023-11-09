package cats.model.cat_impl

import cats.model.ICat
import cats.model.IHuntingItem
import cats.model.PreferencesItem

class Dante : ICat {
    override val preferences: Map<PreferencesItem, Double> = mapOf(
            PreferencesItem.FIELD_MOUSE to 0.2,
            PreferencesItem.HOUSE_MOUSE to 0.2,
            PreferencesItem.SNAIL to 0.05,
            PreferencesItem.ROCK to 0.5,
            PreferencesItem.LEAF to 0.05,
    )

    override val storage: MutableList<IHuntingItem> = mutableListOf()
}