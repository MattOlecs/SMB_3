package mateusz.oleksik.smb_projekt_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mateusz.oleksik.smb_projekt_1.adapters.FirebaseShoppingItemsAdapter
import mateusz.oleksik.smb_projekt_1.common.Constants
import mateusz.oleksik.smb_projekt_1.databinding.ActivityShoppingListBinding
import mateusz.oleksik.smb_projekt_1.interfaces.IClickedShoppingItemListener
import mateusz.oleksik.smb_projekt_1.models.ShoppingItem
import kotlin.collections.ArrayList

class FirebaseShoppingListActivity : AppCompatActivity(), IClickedShoppingItemListener {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var itemsList : ArrayList<ShoppingItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shoppingListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL)
        )

        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("shoppingItems/" + firebaseAuth.currentUser?.uid)
        itemsList = arrayListOf<ShoppingItem>()

        binding.shoppingListRecyclerView.adapter = FirebaseShoppingItemsAdapter(this, itemsList, databaseReference, this)

        binding.addShoppingItemButton.setOnClickListener {
            createShoppingItemActivityLauncher
                .launch(Intent(this, CreateShoppingItemActivity::class.java))
        }
    }

    override fun onClickedShoppingItem(item: ShoppingItem) {

        val editActivityIntent = Intent(this, EditShoppingItemActivity::class.java)
        editActivityIntent.putExtra(Constants.ItemUUIDExtraID, item.uuid)
        editActivityIntent.putExtra(Constants.ItemNameExtraID, item.name)
        editActivityIntent.putExtra(Constants.ItemPriceExtraID, item.price)
        editActivityIntent.putExtra(Constants.ItemAmountExtraID, item.amount)
        editActivityIntent.putExtra(Constants.ItemIsBoughtExtraID, item.isBought)

        editShoppingItemActivityLauncher.launch(editActivityIntent)
    }

    private val createShoppingItemActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val newItem = getShoppingItemFromResult(result)

                if (newItem == null) {
                    Toast
                        .makeText(this, "Creating shopping item failed", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    CoroutineScope(Dispatchers.IO).launch {
                        databaseReference.push().setValue(newItem)
                    }
                }
            }
        }

    private val editShoppingItemActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val updatedItem = getShoppingItemFromResult(result)

                if (updatedItem == null) {
                    Toast
                        .makeText(this, "Creating shopping item failed", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    val queryItemToUpdate = databaseReference
                        .orderByChild("uuid")
                        .equalTo(updatedItem.uuid)

                    val item = mapOf<String, Any>(
                        "amount" to updatedItem.amount,
                        "bought" to updatedItem.isBought,
                        "name" to updatedItem.name,
                        "price" to updatedItem.price,
                    )

                    queryItemToUpdate.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            CoroutineScope(Dispatchers.IO).launch {
                                for (child in snapshot.children){
                                    child.ref.updateChildren(item)
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.e(this::class.java.name, "Failed to update shopping item.", error.toException())
                        }
                    })
                }
            }
        }

    private fun getShoppingItemFromResult(result: ActivityResult): ShoppingItem? {

        if (result.data == null) {
            return null
        }

        val uuid = result.data!!.getStringExtra(Constants.ItemUUIDExtraID)
        val name = result.data!!.getStringExtra(Constants.ItemNameExtraID)
        val price = result.data!!.getDoubleExtra(Constants.ItemPriceExtraID, 0.00)
        val amount = result.data!!.getIntExtra(Constants.ItemAmountExtraID, 0)
        val isBought = result.data!!.getBooleanExtra(Constants.ItemIsBoughtExtraID, false)

        return ShoppingItem(uuid!!, name!!, price, amount, isBought)
    }
}