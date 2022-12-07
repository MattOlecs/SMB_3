package mateusz.oleksik.smb_projekt_1.common

import android.provider.ContactsContract
import com.google.firebase.database.DataSnapshot
import mateusz.oleksik.smb_projekt_1.models.ShoppingItem

class Extensions {
    companion object{

        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return kotlin.math.round(this * multiplier) / multiplier
        }

        fun DataSnapshot.toShoppingItem(): ShoppingItem{
            val uuid = this.child("uuid").value as String
            val name = this.child("name").value as String
            val price = this.child("price").value as Double
            val amount = this.child("amount").value as Long
            val isBought = this.child("bought").value as Boolean
            return ShoppingItem(uuid, name, price, amount.toInt(), isBought)
        }
    }
}