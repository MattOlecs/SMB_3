package mateusz.oleksik.smb_projekt_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mateusz.oleksik.smb_projekt_1.common.Constants
import mateusz.oleksik.smb_projekt_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openShoppingListButton.setOnClickListener {
            val intent = Intent(this, FirebaseShoppingListActivity::class.java)
            startActivity(intent)
        }

        binding.textViewHelloUserNickname.text = intent.getStringExtra(Constants.CurrentUserWelcomeName)
    }
}