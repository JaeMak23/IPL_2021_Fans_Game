package com.jaesysz.ipl2021fansgame.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.enums.Teams
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.PlayerAuthenticationDetailsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.utils.FirebaseAuthExceptionUtility
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val teams = Teams.values()
    var team: String = ""
    private var teamNotSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        val database =
            FirebaseDatabase.getInstance("https://ipl-2021-fans-game-default-rtdb.firebaseio.com/")

        val playersNode = database.getReference(DNodes.BASE).child(DNodes.PLAYERS)
        val authDatabase = playersNode.child(DNodes.AUTHENTICATION)
        val profiles = playersNode.child(DNodes.PROFILES)

        //create spinner
        createSpinner()

        //button events
        btLogin.setOnClickListener { onBackPressed() }
        btBack.setOnClickListener { onBackPressed() }

        tbFullName.doOnTextChanged { text, start, before, count ->

            tilFullName.error = if (tbFullName.text.isNullOrEmpty()) "Name is empty."
            else if (tbFullName.text!!.length < 4) "Name must have 4 or more characters."
            else ""
        }

        tbUserName.doOnTextChanged { text, start, before, count ->
            tilUserName.error = if (tbUserName.text.isNullOrEmpty()) "Username is empty."
            else if (tbUserName.text!!.length < 4) "Name must have 4 or more characters."
            else if (checkUserNameInvalidCharacters()) "Invalid Username characters."
            else ""
        }

        tbEmail.doOnTextChanged { text, start, before, count ->
            tilEmail.error = if (tbEmail.text.isNullOrEmpty()) "Email is empty."
            else if (tbEmail.text!!.length < 4) "Email must have 4 or more characters."
            else if (!tbEmail.text!!.contains('@') || !tbEmail.text!!.contains('.')) "Invalid Email."
            else ""
        }

        tbPassword.doOnTextChanged { text, start, before, count ->
            tilPassword.error = if (tbPassword.text.isNullOrEmpty()) "Password is empty."
            else if (tbPassword.text!!.length < 4) "Password must have 4 or more characters."
            else ""
        }

        tbRetypePassword.doOnTextChanged { text, start, before, count ->
            tilRetypePassword.error =
                if (tbRetypePassword.text.isNullOrEmpty()) "Retype Password is empty."
                else if (tbRetypePassword.text.toString() != tbPassword.text.toString()) "Password doesn't match."
                else ""
        }

        btCheckUserName.setOnClickListener {
            msg("Checking UserName...")

            if (!checkUserNameField()) {
                val userName = tbUserName.text.toString()

                authDatabase.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var found = false

                        if (snapshot.exists()) {
                            for (uid in snapshot.children) {
                                val playerAuthenticationDetails =
                                    uid.getValue(PlayerAuthenticationDetailsModel::class.java)
                                val name = playerAuthenticationDetails!!.userName

                                if (userName == name) {
                                    msg("User Name is already taken.")
                                    found = true
                                    break
                                }
                            }
                        }
                        if (!found)
                            msg("User Name is available.")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        msg("User name checking operation canceled.")
                    }
                })
            }
        }

        btRegister.setOnClickListener {
            msg("Checking fields..")

            if (!validateAllFields()) {
                msg("Registering...")
                val userName = tbUserName.text.toString()

                authDatabase.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var found = false
                        if (snapshot.exists()) {
                            for (uid in snapshot.children) {

                                val playerAuthenticationDetails =
                                    uid.getValue(PlayerAuthenticationDetailsModel::class.java)
                                val name = playerAuthenticationDetails!!.userName

                                if (userName == name) {
                                    msg("User Name is already taken.")
                                    found = true
                                    break
                                }
                            }
                        }
                        if (!found) {
                            val email = tbEmail.text.toString().trim()
                            val password = tbPassword.text.toString()
                            val fullName = tbFullName.text.toString()
                            val team = Teams.valueOf(team)

                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val currentUser = auth.currentUser
                                        msg("Registration successful.")

                                        val playerAuthenticationDetails =
                                            PlayerAuthenticationDetailsModel(
                                                uid = currentUser!!.uid,
                                                email = email,
                                                userName = userName,
                                                emailVerified = currentUser.isEmailVerified
                                            )
                                        val profile =
                                            PlayerProfileModel(
                                                currentUser.uid,
                                                email,
                                                userName,
                                                fullName,
                                                team
                                            )

                                        authDatabase.child(currentUser.uid)
                                            .setValue(playerAuthenticationDetails).apply {
                                                addOnCanceledListener { msg("$userName user cancelled.") }
                                                addOnCompleteListener { msg("$userName user created.") }
                                                addOnFailureListener { msg("$userName user failed.") }
                                            }

                                        profiles.child(userName).setValue(profile)
                                            .apply {
                                                addOnCanceledListener { msg("$userName profile cancelled.") }
                                                addOnCompleteListener { msg("$userName profile created.") }
                                                addOnFailureListener { msg("$userName profile failed.") }
                                            }
                                        if (currentUser.sendEmailVerification().isSuccessful)
                                            msg("Email verification code is sent to $email")

                                        if (currentUser.sendEmailVerification().isCanceled)
                                            msg("Failed to send verification code to $email")
                                        onBackPressed()
                                    }

                                    if (task.isCanceled) {
                                        msg("Unable to create $userName.")
                                        msg(FirebaseAuthExceptionUtility.toString(task.exception as FirebaseAuthException))
                                    }

                                }.addOnFailureListener {

                                    val str =
                                        "Failed : Email id  is already registered or network problem."
                                    msg(str)
                                    tvErrorText.text = str
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        msg("Unable to connect.")
                    }
                })
            }
        }
    }

    private fun createSpinner() {
        // Add Teams to spinner : spTeam
        if (spinnerTeam != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teams)
            spinnerTeam.adapter = adapter

            spinnerTeam.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    team = teams[pos].toString()
                    imgTeamLogo.setImageResource(TeamImage.getLogo(pos))
                    teamNotSelected = team == Teams.NA.name
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    private fun validateAllFields(): Boolean {
        var hasError: Boolean = true
        val str = if (tbFullName.text.isNullOrEmpty()) "Name is empty."
        else if (tbFullName.text!!.length < 4) "Name must have 4 or more characters."
        else if (tbUserName.text.isNullOrEmpty()) "Username is empty."
        else if (tbUserName.text!!.length < 4) "Name must have 4 or more characters."
        else if (checkUserNameInvalidCharacters()) "Invalid Username characters."
        else if (tbEmail.text.isNullOrEmpty()) "Email is empty."
        else if (tbEmail.text!!.length < 4) "Email must have 4 or more characters."
        else if (!tbEmail.text!!.contains('@') || !tbEmail.text!!.contains('.')) "Invalid Email."
        else if (tbPassword.text.isNullOrEmpty()) "Password is empty."
        else if (tbPassword.text!!.length < 4) "Password must have 4 or more characters."
        else if (tbRetypePassword.text.isNullOrEmpty()) "Retype Password is empty."
        else if (tbRetypePassword.text.toString() != tbPassword.text.toString()) "Password doesn't match."
        else if (team == Teams.NA.name) "Please select any one Team"
        else {
            hasError = false
            "No errors. Please wait..."
        }

        msg(str)
        tvErrorText.text = str
        return hasError
    }

    private fun checkUserNameField(): Boolean {
        var hasError= true
        val str = if (tbUserName.text.isNullOrEmpty()) "Username is empty."
        else if (tbUserName.text!!.length < 4) "Name must have 4 or more characters."
        else if (checkUserNameInvalidCharacters()) "Invalid Username characters."
        else {
            hasError = false
            ""
        }
        if (str != "")
            msg(str)
        return hasError
    }

    private fun checkUserNameInvalidCharacters(): Boolean {
        var error= false
        for ((index, value) in tbUserName.text!!.withIndex()) {
            if (!((value in 'a'..'z') || (value in 'A'..'Z') || (value in '0'..'9') || (value == '_'))) {
                error = true
                break
            }
        }
        return error
    }
}