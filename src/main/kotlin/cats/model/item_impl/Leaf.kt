package cats.model.item_impl

import cats.model.IHuntingItem

class Leaf: IHuntingItem {
    override val x: Int = 3
    override val y: Int = 2
    override val z: Int = 1
}