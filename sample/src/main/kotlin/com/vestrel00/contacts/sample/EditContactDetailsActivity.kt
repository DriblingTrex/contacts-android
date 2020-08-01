package com.vestrel00.contacts.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.vestrel00.contacts.Contacts
import com.vestrel00.contacts.Fields
import com.vestrel00.contacts.async.commitWithContext
import com.vestrel00.contacts.async.findWithContext
import com.vestrel00.contacts.entities.MutableContact
import com.vestrel00.contacts.equalTo
import com.vestrel00.contacts.permissions.queryWithPermission
import com.vestrel00.contacts.permissions.updateWithPermission
import com.vestrel00.contacts.util.names
import kotlinx.android.synthetic.main.activity_edit_contact_details.*
import kotlinx.coroutines.launch

class EditContactDetailsActivity : BaseActivity() {

    private lateinit var contact: MutableContact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact_details)

        val activity: Activity = this@EditContactDetailsActivity
        launch {
            if (!fetchContact()) {
                Toast.makeText(activity, R.string.edit_contact_details_fetch_error, LENGTH_SHORT)
                    .show()
                finish()
                return@launch
            }

            // TODO Add linked contacts field
            setupPhotoView()
            setupNameFields()
            setupPhoneFields()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_contact_details, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.save -> {
                launch { save() }
                return true
            }
        }

        return super.onOptionsItemSelected(menuItem)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoView.onActivityResult(requestCode, resultCode, data)
    }

    private suspend fun fetchContact(): Boolean {
        val result = Contacts().queryWithPermission(this)
            .where(Fields.Contact.Id equalTo intent.contactId())
            .findWithContext()
            .firstOrNull()

        if (result != null) {
            contact = result.toMutableContact()
            return true
        }

        return false
    }

    private fun setupPhotoView() {
        photoView.init(this)
        photoView.contact = contact
    }

    private fun setupNameFields() {
        // TODO Move this to a custom view in contacts-ui and handle multiple names the same way the
        // native Contacts app does. For now just pick the first name, if any.
        val name = contact.names().firstOrNull()
        namePrefixField.setText(name?.prefix)
        firstNameField.setText(name?.givenName)
        middleNameField.setText(name?.middleName)
        lastNameField.setText(name?.familyName)
        nameSuffixField.setText(name?.suffix)
    }

    private fun setupPhoneFields() {
        phonesView.contact = contact
    }

    private suspend fun save(): Boolean {
        showProgressDialog()

        // Save photo first so that the Contact does not get deleted if it only has a photo.
        // Blank Contacts are by default deleted in updates.
        val photoSaveSuccess = photoView.saveContactPhoto()

        // Save changes. Delete blanks!
        val contactSaveResult = Contacts().updateWithPermission(this)
            // This is implicitly true by default. We are just being explicitly verbose here.
            .deleteBlanks(true)
            .contacts(contact)
            .commitWithContext()

        val success = contactSaveResult.isSuccessful && photoSaveSuccess

        val resultMessageRes = if (success) {
            R.string.edit_contact_details_save_success
        } else {
            R.string.edit_contact_details_save_error
        }
        Toast.makeText(this, resultMessageRes, LENGTH_SHORT).show()

        dismissProgressDialog()

        return success
    }

    companion object {

        fun editContactDetails(activity: Activity, contactId: Long) {
            val intent = Intent(activity, EditContactDetailsActivity::class.java).apply {
                putExtra(CONTACT_ID, contactId)
            }

            activity.startActivityForResult(intent, REQUEST_EDIT_CONTACT_DETAILS)
        }

        fun onEditContactDetailsResult(requestCode: Int, contactDetailsEdited: () -> Unit) {
            if (requestCode == REQUEST_EDIT_CONTACT_DETAILS) {
                contactDetailsEdited()
            }
        }

        private fun Intent.contactId(): Long = getLongExtra(CONTACT_ID, -1L)

        private const val REQUEST_EDIT_CONTACT_DETAILS = 111
        private const val CONTACT_ID = "contactId"
    }
}