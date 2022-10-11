package com.inar.kickercompose.data.models.lobby.messages

import android.os.Parcel
import android.os.Parcelable

class InviteMessage() : Parcelable{
    var senderId: String = ""
    var senderName: String = ""
    var invitedId: String = ""
    var message: String = ""
    var side: Int = 0
    var position: Int = 0
    var isInviteToAll: Boolean = false

    constructor(parcel: Parcel) : this() {
        senderId = parcel.readString()!!
        senderName = parcel.readString()!!
        invitedId = parcel.readString()!!
        message = parcel.readString()!!
        side = parcel.readInt()
        position = parcel.readInt()
        isInviteToAll = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(senderId)
        parcel.writeString(senderName)
        parcel.writeString(invitedId)
        parcel.writeString(message)
        parcel.writeInt(side)
        parcel.writeInt(position)
        parcel.writeByte(if (isInviteToAll) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InviteMessage> {
        override fun createFromParcel(parcel: Parcel): InviteMessage {
            return InviteMessage(parcel)
        }

        override fun newArray(size: Int): Array<InviteMessage?> {
            return arrayOfNulls(size)
        }
    }

}