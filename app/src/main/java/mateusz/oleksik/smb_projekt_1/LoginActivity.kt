package mateusz.oleksik.smb_projekt_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import mateusz.oleksik.smb_projekt_1.common.Constants
import mateusz.oleksik.smb_projekt_1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val emailTextEdit = binding.editTextEmailAddress
        val passwordTextEdit = binding.editTextPassword

        binding.buttonCreateAccount.setOnClickListener {
            val registerActivity = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivity)
        }

        binding.buttonLogIn.setOnClickListener {

            if (TextUtils.isEmpty(emailTextEdit.text)){
                emailTextEdit.error = "Email cannot be empty"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(passwordTextEdit.text)){
                passwordTextEdit.error = "password cannot be empty"
                return@setOnClickListener
            }

            val email = emailTextEdit.text.toString().trim()
            val password = passwordTextEdit.text.toString()
            firebaseAuth
                .signInWithEmailAndPassword(
                    email,
                    password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val mainActivity = Intent(this, MainActivity::class.java)
                        mainActivity.putExtra(Constants.CurrentUserWelcomeName, firebaseAuth.currentUser?.displayName ?: firebaseAuth.currentUser?.email)
                        startActivity(mainActivity)
                    } else {
                        Toast
                            .makeText(this, "Log-in failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}