package com.inar.kickercompose.data.models.lobby

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class LobbyItemModel() : Parcelable {
    var dateStart: LocalDateTime? = null
    var message: String = ""
    var initiator: LobbyUserShortInfo = LobbyUserShortInfo()

    var sideA: List<LobbyUserShortInfo> = arrayListOf()
    var sideB: List<LobbyUserShortInfo> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        val dateStartString = parcel.readString()
        dateStart = (if (dateStartString != null) LocalDateTime.parse(dateStartString) else null)

        message = parcel.readString()!!
        initiator = parcel.readParcelable(LobbyUserShortInfo::class.java.classLoader)!!
        sideA = parcel.createTypedArrayList(LobbyUserShortInfo)!!
        sideB = parcel.createTypedArrayList(LobbyUserShortInfo)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateStart?.format(DateTimeFormatter.ISO_DATE_TIME))
        parcel.writeString(message)
        parcel.writeParcelable(initiator, flags)
        parcel.writeTypedList(sideA)
        parcel.writeTypedList(sideB)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LobbyItemModel> {
        override fun createFromParcel(parcel: Parcel): LobbyItemModel {
            return LobbyItemModel(parcel)
        }

        override fun newArray(size: Int): Array<LobbyItemModel?> {
            return arrayOfNulls(size)
        }
    }


}