package contacts.core.util

import android.provider.ContactsContract

internal val Long?.isProfileId: Boolean
    get() = this?.let(ContactsContract::isProfileId) == true