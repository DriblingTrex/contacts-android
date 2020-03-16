package com.vestrel00.contacts.entities.mapper

import com.vestrel00.contacts.entities.Entity
import com.vestrel00.contacts.entities.GroupMembership
import com.vestrel00.contacts.entities.cursor.GroupMembershipCursor

internal class GroupMembershipMapper(private val groupMembershipCursor: GroupMembershipCursor) :
    EntityMapper<GroupMembership, Entity> {

    override val toImmutable: GroupMembership
        get() = GroupMembership(
            id = groupMembershipCursor.id,
            rawContactId = groupMembershipCursor.rawContactId,
            contactId = groupMembershipCursor.contactId,

            isPrimary = groupMembershipCursor.isPrimary,
            isSuperPrimary = groupMembershipCursor.isSuperPrimary,

            groupId = groupMembershipCursor.groupId
        )

    override val toMutable: Entity
        get() = throw UnsupportedOperationException("There is no mutable group membership")
}
