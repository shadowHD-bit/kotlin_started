package com.example.lab56_krivikov_8k93

import android.os.Parcel
import android.os.Parcelable

// Элемент списка продуктов
class Item() : Parcelable {
    var id: Int = 0
    var kind: String = ""
    var title: String = ""
    var price: Double = 0.0
    var weight: Double = 0.0
    var photo: String = ""

    val info: String
        get() = "$kind $title ($price ₽)"

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        kind = parcel.readString()?: ""
        title = parcel.readString()?: ""
        price = parcel.readDouble()
        weight = parcel.readDouble()
        photo = parcel.readString()?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(kind)
        parcel.writeString(title)
        parcel.writeDouble(price)
        parcel.writeDouble(weight)
        parcel.writeString(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}