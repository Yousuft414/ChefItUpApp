package project.group3tztechcorp.chefitupapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.UserInterface
import project.group3tztechcorp.chefitupapp.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        getData()

        binding.accountBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_profileFragment_to_accountInformationFragment)
        }


        return binding.root
        //return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun getData(){

        var fullname = arguments?.getString("fullName").toString().trim()
        var username = arguments?.getString("userName").toString().trim()

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
                    binding.userName.text = username
                    binding.fullName.text = fullname
                    binding.rewardsNum.text = rewardsNumFromDB.toString()
                    binding.recipesCompletedNum.text = recipiesNumFromDB.toString()
                    binding.achievementsNum.text = achivementsNumFromDB.toString()
                    binding.level.text = levelFromDB.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}