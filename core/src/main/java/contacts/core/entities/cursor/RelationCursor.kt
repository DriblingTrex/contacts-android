package contacts.core.entities.cursor

import android.database.Cursor
import contacts.core.Fields
import contacts.core.RelationField
import contacts.core.entities.RelationEntity

/**
 * Retrieves [Fields.Relation] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class RelationCursor(cursor: Cursor, includeFields: Set<RelationField>) :
    AbstractDataCursor<RelationField>(cursor, includeFields) {

    val type: RelationEntity.Type? by type(
        Fields.Relation.Type,
        typeFromValue = RelationEntity.Type::fromValue
    )

    val label: String? by string(Fields.Relation.Label)

    val name: String? by string(Fields.Relation.Name)
}
