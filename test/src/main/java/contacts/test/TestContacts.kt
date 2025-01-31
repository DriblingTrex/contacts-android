package contacts.test

import android.content.Context
import contacts.core.*
import contacts.core.accounts.Accounts
import contacts.core.data.Data
import contacts.core.entities.custom.CustomDataRegistry
import contacts.core.groups.Groups
import contacts.core.profile.Profile
import contacts.test.entities.TestDataRegistration

@JvmOverloads
@Suppress("FunctionName")
fun TestContacts(
    context: Context,
    customDataRegistry: CustomDataRegistry = CustomDataRegistry()
): Contacts = TestContacts(Contacts(context, customDataRegistry)).also {
    customDataRegistry.register(TestDataRegistration())
}

object ContactsFactory {

    @JvmStatic
    @JvmOverloads
    fun create(
        context: Context,
        customDataRegistry: CustomDataRegistry = CustomDataRegistry()
    ): Contacts = TestContacts(context, customDataRegistry)
}

// Note that we cannot use "by" to delegate calls to the internal query because function calls will
// return the internal contacts instance instead of this test instance.
// class TestContacts(private val contacts: Contacts): Contacts by contacts

/**
 * TODO document this
 */
private class TestContacts(private val contacts: Contacts) : Contacts {

    override fun query(): Query = TestQuery(contacts.query())

    override fun broadQuery(): BroadQuery {
        TODO("Not yet implemented")
    }

    override fun insert(): Insert {
        TODO("Not yet implemented")
    }

    override fun update(): Update {
        TODO("Not yet implemented")
    }

    override fun delete(): Delete {
        TODO("Not yet implemented")
    }

    override fun data(): Data {
        TODO("Not yet implemented")
    }

    override fun groups(): Groups {
        TODO("Not yet implemented")
    }

    override fun profile(): Profile {
        TODO("Not yet implemented")
    }

    override fun accounts() = accounts(false)

    override fun accounts(isProfile: Boolean): Accounts {
        TODO("Not yet implemented")
    }

    override val permissions: ContactsPermissions = contacts.permissions

    override val applicationContext: Context = contacts.applicationContext

    override val customDataRegistry: CustomDataRegistry = contacts.customDataRegistry
}