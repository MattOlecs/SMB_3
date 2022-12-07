package mateusz.oleksik.smb_projekt_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import mateusz.oleksik.smb_projekt_1.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonRegister.setOnClickListener {

            val nicknameTextEdit = binding.editTextNickname
            val emailTextEdit = binding.editTextEmailAddress
            val passwordTextEdit = binding.editTextPassword

            if (TextUtils.isEmpty(nicknameTextEdit.text)){
                nicknameTextEdit.error = "Nickname cannot be empty"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(emailTextEdit.text)){
                emailTextEdit.error = "Email cannot be empty"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(passwordTextEdit.text)){
                passwordTextEdit.error = "Password cannot be empty"
                return@setOnClickListener
            }

            firebaseAuth
                .createUserWithEmailAndPassword(
                    emailTextEdit.text.toString(),
                    passwordTextEdit.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast
                            .makeText(this, "User registered successfully.", Toast.LENGTH_SHORT)
                            .show()
                        val loginIntent = Intent(this, LoginActivity::class.java)

                        it.onSuccessTask { authResult ->

                            val profileUpdates = userProfileChangeRequest {
                                displayName = nicknameTextEdit.text.toString()
                            }

                            authResult.user!!.updateProfile(profileUpdates)
                        }

                        startActivity(loginIntent)
                    }
                    else{
                        Toast
                            .makeText(this, "User registration failed.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}