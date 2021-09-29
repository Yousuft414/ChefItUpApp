package project.group3tztechcorp.chefitupapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.ui.ProfileFragment

class Login : AppCompatActivity() {
    lateinit var mUsername: TextInputLayout;
    lateinit var mUsernameText : TextInputEditText
    lateinit var mEmail: TextInputLayout;
    lateinit var mEmailText : TextInputEditText
    lateinit var mPassword: TextInputLayout;
    lateinit var mPasswordText : TextInputEditText
    lateinit var mLoginBtn: Button;
    lateinit var mRegisterBtn : TextView;
    lateinit var progressBar: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        mUsername = findViewById(R.id.editTextUsernameEmail)
        mUsernameText = findViewById(R.id.editTextUsernameEmailText)
        mEmail = findViewById(R.id.editTextUsernameEmail)
        mEmailText = findViewById(R.id.editTextUsernameEmailText)
        mPassword = findViewById(R.id.editTextPassword2)
        mPasswordText = findViewById(R.id.editTextPassword2Text)
        mLoginBtn = findViewById<Button>(R.id.button_Login)
        mRegisterBtn = findViewById<TextView>(R.id.registerButton)
        progressBar = findViewById<ProgressBar>(R.id.progressBar2)

        mRegisterBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Login, Registration::class.java))
        })
    }

    private fun validateUsername(): Boolean {
        var username = mUsernameText.text.toString()

        return if (username.isEmpty()) {
            mUsername.error = "Field cannot be empty"
            false
        } else {
            mUsername.error = null
            mUsername.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        var password = mPasswordText.text.toString()

        return if (password.isEmpty()) {
            mPassword.error = "Field cannot be empty"
            false
        } else {
            mPassword.error = null
            mPassword.isErrorEnabled = false
            true
        }
    }
    fun loginUser(view: View){
        //Validate login info
        if(!validateUsername() or !validatePassword()){
            return
        } else {
            isUser()
        }
    }

    private fun isUser() {
        var userEnteredUsername = mUsernameText.text.toString().trim()
        var userEnteredPassword = mPasswordText.text.toString().trim()

        var reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("userLogin")
        var reference2 : DatabaseReference = FirebaseDatabase.getInstance().getReference("userRegistration")

        var checkUser : Query = reference.orderByChild("username").equalTo(userEnteredUsername)
        var checkUser2 : Query = reference2.orderByChild("username").equalTo(userEnteredUsername)

            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        mUsername.error = null
                        mUsername.isErrorEnabled = false

                        var passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue()

                        if(passwordFromDB?.equals(userEnteredPassword) == true){
                            mPassword.error = null
                            mPassword.isErrorEnabled = false
                            progressBar.visibility = View.VISIBLE
                            checkUser2.addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onDataChange(snapshot2: DataSnapshot) {
                                    if (snapshot2.exists()){
                                        var nameFromDB = snapshot2.child(userEnteredUsername).child("fullName").getValue()
                                        var usernameFromDB = snapshot2.child(userEnteredUsername).child("username").getValue()
                                        var phoneFromDB = snapshot2.child(userEnteredUsername).child("phone").getValue()
                                        var emailFromDB = snapshot2.child(userEnteredUsername).child("email").getValue()

                                        val intent = Intent(this@Login, UserInterface::class.java)
                                        intent.putExtra("fullName", nameFromDB.toString())
                                        intent.putExtra("username", usernameFromDB.toString())
                                        intent.putExtra("email", emailFromDB.toString())
                                        intent.putExtra("phone", phoneFromDB.toString())
                                        intent.putExtra("password", passwordFromDB.toString())

                                        startActivity(intent)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })

                        } else {
                            mPassword.error = "Incorrect Password"
                            mPassword.requestFocus()
                        }
                    } else {
                        mUsername.error = "User does not exist"
                        mUsername.requestFocus()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}