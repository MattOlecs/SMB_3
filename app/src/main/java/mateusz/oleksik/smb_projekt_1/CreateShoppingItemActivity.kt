package mateusz.oleksik.smb_projekt_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mateusz.oleksik.smb_projekt_1.common.Constants
import mateusz.oleksik.smb_projekt_1.common.Extensions.Companion.round
import mateusz.oleksik.smb_projekt_1.common.Utils.Companion.getUUID
import mateusz.oleksik.smb_projekt_1.databinding.ActivityUpsertShoppingItemBinding
import mateusz.oleksik.smb_projekt_1.models.ShoppingItem

class CreateShoppingItemActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityUpsertShoppingItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUpsertShoppingItemBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.confirmAddItemButton.setOnClickListener {
            createShoppingItem()
        }
        _binding.cancelAddItem.setOnClickListener {
            finish()
        }
    }

    private fun createShoppingItem(){
        val name = _binding.itemNameText.text.toString()
        val amount = Integer.parseInt(_binding.amountTextNumber.text.toString())
        val price = _binding.priceTextNumber.text.toString().toDouble().round(2)
        val isBought = _binding.checkBox.isChecked

        val createdItem = ShoppingItem(getUUID(), name, price, amount, isBought)

        val data = Intent()
        data.putExtra(Constants.ItemUUIDExtraID, createdItem.uuid)
        data.putExtra(Constants.ItemNameExtraID, createdItem.name)
        data.putExtra(Constants.ItemPriceExtraID, createdItem.price)
        data.putExtra(Constants.ItemAmountExtraID, createdItem.amount)
        data.putExtra(Constants.ItemIsBoughtExtraID, createdItem.isBought)

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}