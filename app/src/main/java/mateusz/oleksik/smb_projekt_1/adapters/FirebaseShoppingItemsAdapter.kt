package mateusz.oleksik.smb_projekt_1.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mateusz.oleksik.smb_projekt_1.common.Extensions.Companion.toShoppingItem
import mateusz.oleksik.smb_projekt_1.databinding.ShoppingItemsListElementBinding
import mateusz.oleksik.smb_projekt_1.interfaces.IClickedShoppingItemListener
import mateusz.oleksik.smb_projekt_1.models.ShoppingItem

class FirebaseShoppingItemsAdapter(val context: Context, val list: ArrayList<ShoppingItem>, val ref: DatabaseReference, val clickListener: IClickedShoppingItemListener)  : RecyclerView.Adapter<FirebaseShoppingItemsAdapter.ShoppingItemViewHolder>() {

    inner class ShoppingItemViewHolder(val binding: ShoppingItemsListElementBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(IO).launch {
                    list.add(snapshot.toShoppingItem())

                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(IO).launch {
                    val updatedItem = snapshot.toShoppingItem()
                    val index = list.indexOfFirst { a -> a.uuid == updatedItem.uuid }
                    list[index] = updatedItem

                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch {
                    list.remove(snapshot.toShoppingItem())
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Toast.makeText(context, "Moving not supported", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(this::class.java.name, "Failed to modify value.",error.toException())
            }
        })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShoppingItemsListElementBinding.inflate(inflater, parent, false)
        return ShoppingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        holder.binding.shoppingItem = list[position]

        holder.itemView.setOnClickListener {
            clickListener.onClickedShoppingItem(list[position])
        }

        holder.binding.deleteButton.setOnClickListener {
            ref.orderByValue()
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        CoroutineScope(IO).launch {
                            snapshot.children.iterator().next().ref.removeValue()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(this::class.java.name, "Failed to delete value.", error.toException())
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}