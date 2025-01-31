package contacts.core.entities.operation

import android.content.ContentProviderOperation
import contacts.core.GroupsFields
import contacts.core.`in`
import contacts.core.entities.MutableGroup
import contacts.core.entities.table.Table
import contacts.core.equalTo

private val TABLE = Table.Groups

/**
 * Builds [ContentProviderOperation]s for [Table.Groups].
 */
internal class GroupsOperation {

    fun insert(group: MutableGroup): ContentProviderOperation = newInsert(TABLE)
        .withValue(GroupsFields.Title, group.title)
        .withValue(GroupsFields.AccountName, group.account.name)
        .withValue(GroupsFields.AccountType, group.account.type)
        // Setting favorites and auto add has no effect. The Contacts Provider will routinely set
        // them to false for all user-created groups.
        // .withValue(Fields.Group.Favorites, it.favorites.toSqlValue())
        // .withValue(Fields.Group.AutoAdd, it.autoAdd.toSqlValue())
        .build()

    fun update(group: MutableGroup): ContentProviderOperation? = group.id?.let { groupId ->
        newUpdate(TABLE)
            .withSelection(GroupsFields.Id equalTo groupId)
            .withValue(GroupsFields.Title, group.title)
            .build()
    }

    fun delete(groupId: Long): ContentProviderOperation = newDelete(TABLE)
        .withSelection(GroupsFields.Id equalTo groupId)
        .build()

    fun delete(groupIds: Collection<Long>): ContentProviderOperation = newDelete(TABLE)
        .withSelection(GroupsFields.Id `in` groupIds)
        .build()
}