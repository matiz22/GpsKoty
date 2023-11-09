package cats

import cats.model.cat_impl.Arianna
import cats.model.cat_impl.Dante
import cats.model.ICat
import cats.model.IHuntingItem
import cats.model.PreferencesItem
import cats.model.cat_impl.Luna
import cats.model.item_impl.*

fun main() {
    val cats = listOf<ICat>(Arianna(), Luna(), Dante())
    val huntingItems = listOf<IHuntingItem>(FieldMouse(), HouseMouse(), Leaf(), Rock(), Snail())
    val k = 1000
    for (i in 1..k) {
        val itemToCheck = huntingItems.random()
        var chosenCat: ICat? = null
        when (itemToCheck) {
            is FieldMouse -> {
                chosenCat = cats.maxBy { it.getPreference(PreferencesItem.FIELD_MOUSE, itemToCheck.getSize()) }
            }

            is HouseMouse -> {
                chosenCat = cats.maxBy { it.getPreference(PreferencesItem.HOUSE_MOUSE, itemToCheck.getSize()) }
            }

            is Leaf -> {
                chosenCat = cats.maxBy { it.getPreference(PreferencesItem.LEAF, itemToCheck.getSize()) }
            }

            is Rock -> {
                chosenCat = cats.maxBy { it.getPreference(PreferencesItem.ROCK, itemToCheck.getSize()) }
            }

            is Snail -> {
                chosenCat = cats.maxBy { it.getPreference(PreferencesItem.SNAIL, itemToCheck.getSize()) }
            }
        }
        if (chosenCat != null && chosenCat.getTakenStorage().plus(itemToCheck.getSize()) <= 2000) chosenCat.storage.add(itemToCheck)

    }
    cats.forEach { iCat ->
        println(iCat)
        println(iCat.storage.sumOf { item -> item.getSize() })
//        iCat.storage.forEach { item ->
//            println(item)
//        }


    }

}