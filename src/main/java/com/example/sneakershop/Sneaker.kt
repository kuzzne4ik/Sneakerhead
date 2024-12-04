import android.os.Parcel
import android.os.Parcelable

data class Sneaker(
    val model: String,
    val brand: String,
    val price: Double,
    val imageUrl: String,
    val color: String
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    constructor() : this("", "", 0.0, "", "")


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(model)
        parcel.writeString(brand)
        parcel.writeDouble(price)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Sneaker> {
            override fun createFromParcel(parcel: Parcel): Sneaker {
                return Sneaker(parcel)
            }

            override fun newArray(size: Int): Array<Sneaker?> {
                return arrayOfNulls(size)
            }
        }
    }
}
