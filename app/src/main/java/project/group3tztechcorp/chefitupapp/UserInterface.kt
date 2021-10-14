package project.group3tztechcorp.chefitupapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import project.group3tztechcorp.chefitupapp.ui.ChallengesPageFragment
import project.group3tztechcorp.chefitupapp.ui.ProfileFragment
import project.group3tztechcorp.chefitupapp.ui.RecipeHomeFragment
import project.group3tztechcorp.chefitupapp.ui.RewardsPageFragment

class UserInterface : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView
    private var profileFragment: ProfileFragment = ProfileFragment()
    private var recipeHomeFragment: RecipeHomeFragment = RecipeHomeFragment()
    private var rewardsPageFragment: RewardsPageFragment = RewardsPageFragment()
    private var challengesPageFragment: ChallengesPageFragment = ChallengesPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_interface)

        var intent = getIntent()
        var fullName = intent.getStringExtra("fullName").toString().trim()
        var username = intent.getStringExtra("username").toString().trim()

        var bundle = Bundle()
        bundle.putString("fullName", fullName)
        bundle.putString("userName", username)

        profileFragment.arguments = bundle
        recipeHomeFragment.arguments = bundle
        rewardsPageFragment.arguments = bundle
        challengesPageFragment.arguments = bundle

        bottomNav = findViewById(R.id.bottomNav)
        replaceFragment(profileFragment)

        bottomNav.setOnItemSelectedListener {
            //var selectedFragment : Fragment = null
            when (it.itemId) {
                R.id.userProfile -> replaceFragment(profileFragment)
                R.id.recipePage -> replaceFragment(recipeHomeFragment)
                R.id.rewardsPage -> replaceFragment(rewardsPageFragment)
                R.id.challengesPage -> replaceFragment(challengesPageFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
        }
    }
}