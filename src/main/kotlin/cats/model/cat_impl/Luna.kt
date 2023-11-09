package cats.model.cat_impl

import cats.model.ICat
import cats.model.IHuntingItem
import cats.model.PreferencesItem

class Luna : ICat {

    override val preferences: Map<PreferencesItem, Double> = mapOf(
            PreferencesItem.FIELD_MOUSE to 0.4,
            PreferencesItem.HOUSE_MOUSE to 0.4,
            PreferencesItem.SNAIL to 0.1,
            PreferencesItem.ROCK to 0.1,
            PreferencesItem.LEAF to 0.0,
    )

    override val storage: MutableList<IHuntingItem> = mutableListOf()

}