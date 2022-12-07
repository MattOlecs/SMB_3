package mateusz.oleksik.smb_projekt_1.models

data class ShoppingItem(
    val uuid: String,
    val name: String,
    val price: Double,
    val amount: Int,
    val isBought: Boolean
)
