package com.inar.kickercompose.data.models.lobby.item

import android.os.Parcel
import android.os.Parcelable
import com.inar.kickercompose.data.models.lobby.LobbyUserShortInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class LobbyItemModel() : Parcelable {
    val lastTimeStamp: LobbyTimeStamp?
        get() = timeStamps.lastOrNull()

    //TODO create batLtle must add first element here
    var timeStamps: ArrayList<LobbyTimeStamp> = arrayListOf()
    var result: ResultDto = ResultDto()


    // var dateStart: LocalDateTime? = null
    var message: String = ""
    var initiator: LobbyUserShortInfo = LobbyUserShortInfo()

    var sideA: List<LobbyUserShortInfo> = arrayListOf()
    var sideB: List<LobbyUserShortInfo> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        timeStamps = parcel.createTypedArrayList(LobbyTimeStamp)!!
        result = parcel.readParcelable(ResultDto::class.java.classLoader)!!
        message = parcel.readString()!!
        initiator = parcel.readParcelable(LobbyUserShortInfo::class.java.classLoader)!!
        sideA = parcel.createTypedArrayList(LobbyUserShortInfo)!!
        sideB = parcel.createTypedArrayList(LobbyUserShortInfo)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(timeStamps)
        parcel.writeParcelable(result, flags)
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