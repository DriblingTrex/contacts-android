package contacts.core.entities.mapper

import contacts.core.entities.Entity

// Does not need to be sealed.
internal interface EntityMapper<out T : Entity> {
    val value: T

    /**
     * The [value] if it is not blank. Else, returns null.
     */
    val nonBlankValueOrNull: T?
        get() = if (value.isBlank) null else value
}