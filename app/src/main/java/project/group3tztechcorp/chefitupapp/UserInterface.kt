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

    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_interface)

        bottomNav = findViewById(R.id.bottomNav)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ProfileFragment()).commit()
        bottomNav.setOnItemSelectedListener{
            var selectedFragment : Fragment? = null
            when(it.itemId){
                R.id.userProfile -> selectedFragment == ProfileFragment()
                R.id.recipePage -> selectedFragment == RecipeHomeFragment()
                R.id.rewardsPage -> selectedFragment == RewardsPageFragment()
                R.id.challengesPage -> selectedFragment == ChallengesPageFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit()
            }
            true
        }
    }
}