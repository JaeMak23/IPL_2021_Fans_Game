package com.jaesysz.ipl2021fansgame.appData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.models.PlayerAuthenticationDetailsModel


object AppInfo {
    var loggedOut: Boolean = true
    const val DEVICE_VERSION:Int=5


    //testing
    private lateinit var database: FirebaseDatabase
    var isLoggedIn: Boolean = false
    var userName: String = ""
    var email:String=""
    var isEmailVerified:Boolean=false
    var isDashboardActive=false

    fun load() {
        database = FirebaseDatabase.getInstance()
    }

    fun logIn(user:FirebaseUser) {
        val authNode =
            database.getReference(DNodes.BASE).child(DNodes.PLAYERS).child(DNodes.AUTHENTICATION)

        authNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isLoggedIn = true
                email = user.email!!
                isEmailVerified = user.isEmailVerified

                val auth = snapshot.child(user.uid)
                    .getValue(PlayerAuthenticationDetailsModel::class.java)!!
                userName = auth.userName
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    fun logOut() {
        isLoggedIn = false
        userName = ""
        email=""
        isEmailVerified=false
    }

}