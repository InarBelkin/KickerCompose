package com.inar.kickercompose.data.models.lobby

import android.os.Parcel
import android.os.Parcelable

class LobbyUserShortInfo() : Parcelable {
    var id: String? = null
    var name: String = ""
    var elo: Int = 0

    var role: Int = 0
    var accepted: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()!!
        elo = parcel.readInt()
        role = parcel.readInt()
        accepted = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(elo)
        parcel.writeInt(role)
        parcel.writeInt(accepted)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LobbyUserShortInfo> {
        override fun createFromParcel(parcel: Parcel): LobbyUserShortInfo {
            return LobbyUserShortInfo(parcel)
        }

        override fun newArray(size: Int): Array<LobbyUserShortInfo?> {
            return arrayOfNulls(size)
        }
    }
}