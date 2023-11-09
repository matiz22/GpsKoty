package cats.model

interface ICat {
    val preferences: Map<PreferencesItem, Double>
    val storage: MutableList<IHuntingItem>
    fun getPreference(type: PreferencesItem, size: Int): Double {
        return if ( getTakenStorage() + size <= 2000) {
            preferences[type]?: 0.0
        } else {
            0.0
        }
    }

    fun getTakenStorage(): Int {
        return storage.sumOf { it.getSize() }
    }

}