package com.ghost.eventmanager

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var remoteConfig: FirebaseRemoteConfig
    lateinit var code: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = FirebaseAuth.getInstance()
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        if (auth.currentUser != null) {
            startActivity(Intent(this, EventListActivity::class.java))
        }
        login.setOnClickListener {
            if (auth.currentUser != null) {
                startActivity(Intent(this, EventListActivity::class.java))
            } else {
                progressBar.visibility = View.VISIBLE
                val email = findViewById<EditText>(R.id.email).text.toString()
                val password = findViewById<EditText>(R.id.password).text.toString()
                val codeInput = findViewById<EditText>(R.id.code).text.toString()
                if (email.isValidEmail()) {
                    if (password.isEmpty()) {
                        Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    } else {
                        checkCode()
                    }
                } else {
                    Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }

        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        // updateUI(currentUser)
    }

    private fun checkCode() {
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val code = remoteConfig.getString("admin_code")
            val result = findViewById<EditText>(R.id.code).text.toString()

            if (result == code && !result.isBlank()) {
                signIn()
            } else {
                Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
            //findViewById<EditText>(R.id.code).hint = code
        }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to get Code", Toast.LENGTH_SHORT).show()
            }

    }

    private fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun signIn() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, EventListActivity::class.java))
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

    }


}