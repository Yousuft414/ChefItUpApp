package project.group3tztechcorp.chefitupapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.*

class UserProfile : AppCompatActivity() {
    lateinit var musername: TextView
    lateinit var mfullName: TextView
    lateinit var mrecipeBtn: MaterialCardView
    lateinit var mrecipeCompletedNum: TextView
    lateinit var mrewardsBtn: MaterialCardView
    lateinit var mrewardsGainedNum: TextView
    lateinit var machievementsBtn: MaterialCardView
    lateinit var machievementsCompletedNum: TextView
    lateinit var msettingsBtn: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        musername = findViewById(R.id.userName)
        mfullName = findViewById(R.id.full_Name)
        mrecipeBtn = findViewById(R.id.recipes_completed_btn)
        mrecipeCompletedNum = findViewById(R.id.recipes_completed_num)
        mrewardsBtn = findViewById(R.id.rewards_gained_btn)
        mrewardsGainedNum = findViewById(R.id.rewards_num)
        machievementsBtn = findViewById(R.id.achievements_btn)
        machievementsCompletedNum = findViewById(R.id.achievements_num)
        msettingsBtn = findViewById(R.id.settings_btn)

        msettingsBtn.setOnClickListener {
            startActivity(Intent(this@UserProfile, UserInterface::class.java))
        }

        getData()
    }

    private fun getData() {

        var intent = getIntent()
        var fullName = intent.getStringExtra("fullName").toString().trim()
        var username = intent.getStringExtra("username").toString().trim()

        var reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("userInformation")

        var checkUser: Query = reference.orderByChild("username").equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var nameFromDB = snapshot.child(username).child("fullName").getValue()
                    var levelFromDB = snapshot.child(username).child("level").getValue()
                    var experienceFromDB = snapshot.child(username).child("experience").getValue()
                    var rewardsNumFromDB = snapshot.child(username).child("rewards").getValue()
                    var recipiesNumFromDB = snapshot.child(username).child("recipesCompleted").getValue()
                    var achivementsNumFromDB = snapshot.child(username).child("achievementsCompleted").getValue()

                    //set the data
                    musername.text = username
                    mfullName.text = fullName
                    mrewardsGainedNum.text = rewardsNumFromDB.toString()
                    mrecipeCompletedNum.text = recipiesNumFromDB.toString()
                    machievementsCompletedNum.text = achivementsNumFromDB.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}