package contacts.core.entities.operation

import contacts.core.Fields
import contacts.core.NicknameField
import contacts.core.entities.MimeType
import contacts.core.entities.NicknameEntity

internal class NicknameOperation(isProfile: Boolean, includeFields: Set<NicknameField>) :
    AbstractDataOperation<NicknameField, NicknameEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Nickname

    override fun setValuesFromData(
        data: NicknameEntity, setValue: (field: NicknameField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Nickname.Name, data.name)
    }
}