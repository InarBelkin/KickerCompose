package com.inar.kickercompose.data.models.lobby.item

import android.os.Parcel
import android.os.Parcelable
import com.inar.kickercompose.data.models.lobby.BattleStatus
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class LobbyTimeStamp(
    var battleState: Int = 0,
    var globalTime: LocalDateTime,
    var battleTime: Double = 0.0,
) : Parcelable {

//    val dateStartString = parcel.readString()
//    dateStart = (if (dateStartString != null) LocalDateTime.parse(dateStartString) else null)


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        LocalDateTime.parse(parcel.readString()),
        parcel.readDouble()) {
    }

    fun getCurrentBattleTime(): Double {
        if (battleState == BattleStatus.Started.num) {
            val dif =
                Duration.between(globalTime, LocalDateTime.now(ZoneOffset.UTC))
            return dif.seconds + battleTime
        }
        return battleTime
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(battleState)
        parcel.writeString(globalTime.format(DateTimeFormatter.ISO_DATE_TIME))
        parcel.writeDouble(battleTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LobbyTimeStamp> {
        override fun createFromParcel(parcel: Parcel): LobbyTimeStamp {
            return LobbyTimeStamp(parcel)
        }

        override fun newArray(size: Int): Array<LobbyTimeStamp?> {
            return arrayOfNulls(size)
        }
    }


}