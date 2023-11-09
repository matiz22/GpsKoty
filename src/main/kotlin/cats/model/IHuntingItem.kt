package cats.model

interface IHuntingItem {
    val x: Int
    val y: Int
    val z: Int
    fun getSize():Int {
        return x * y * z
    }
}