package com.inar.kickercompose.data.models.lobby.item

import android.os.Parcel
import android.os.Parcelable

class ResultDto() : Parcelable {
    var isWinnerA: Boolean? = null
    var countOfGoalsLoser: Int = 5


    constructor(parcel: Parcel) : this() {
        isWinnerA = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        countOfGoalsLoser = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(isWinnerA)
        parcel.writeInt(countOfGoalsLoser)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultDto> {
        override fun createFromParcel(parcel: Parcel): ResultDto {
            return ResultDto(parcel)
        }

        override fun newArray(size: Int): Array<ResultDto?> {
            return arrayOfNulls(size)
        }
    }
}