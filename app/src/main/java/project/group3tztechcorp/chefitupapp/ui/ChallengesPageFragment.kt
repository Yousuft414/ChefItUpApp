package project.group3tztechcorp.chefitupapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Recipe
import project.group3tztechcorp.chefitupapp.adapter.ChallengesAdapter
import project.group3tztechcorp.chefitupapp.databinding.FragmentChallengesPageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "MyActivity"

/**
 * A simple [Fragment] subclass.
 * Use the [ChallengesPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChallengesPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    lateinit var binding: FragmentChallengesPageBinding
    private lateinit var challengesList: ArrayList<Recipe>

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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenges_page, container, false)

        recipeRecyclerView = binding.recycleViewChallenges
        recipeRecyclerView.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL, false)
        recipeRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        challengesList = arrayListOf<Recipe>()

        getChallengesRecipies()

        return binding.root
        //return inflater.inflate(R.layout.fragment_challenges_page, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChallengesPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChallengesPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getChallengesRecipies(){
        var username = arguments?.getString("userName").toString().trim()
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        reference2 = FirebaseDatabase.getInstance().getReference("userInformation")

        Log.i(TAG, username)
        var check: Query = reference2.orderByChild("username").equalTo(username)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var levelFromDB = snapshot.child(username).child("level").getValue()

                    Log.i(TAG, "user exists")

                    reference.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                Log.i(TAG, "recipes exist")
                                for (recipeSnapshot in snapshot.children){
                                    if(levelFromDB.toString().toInt() == 1){
                                        if(recipeSnapshot.child("Level").getValue().toString().equals("Easy") or recipeSnapshot.child("Level").getValue().toString().equals("Intermediate")){
                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                            recipeArrayList.add(recipe!!)
                                            Log.i(TAG, "level 1 user")
                                        }
                                    }else if (levelFromDB.toString().toInt() == 2){
                                        if(recipeSnapshot.child("Level").getValue().toString().equals("Easy") or recipeSnapshot.child("Level").getValue().toString().equals("Intermediate")){
                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                            recipeArrayList.add(recipe!!)
                                            Log.i(TAG, "level 2 user")
                                        }
                                    }else if (levelFromDB.toString().toInt() == 3){
                                        if(recipeSnapshot.child("Level").getValue().toString().equals("Intermediate") or recipeSnapshot.child("Level").getValue().toString().equals("Hard")){
                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                            recipeArrayList.add(recipe!!)
                                            Log.i(TAG, "level 3 user")
                                        }
                                    }else if (levelFromDB.toString().toInt() == 4){
                                        if(recipeSnapshot.child("Level").getValue().toString().equals("Intermediate") or recipeSnapshot.child("Level").getValue().toString().equals("Hard")){
                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                            recipeArrayList.add(recipe!!)
                                            Log.i(TAG, "level 4 user")
                                        }
                                    }else if (levelFromDB.toString().toInt() == 5){
                                        if(recipeSnapshot.child("Level").getValue().toString().equals("Hard")){
                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                            recipeArrayList.add(recipe!!)
                                            Log.i(TAG, "level 5 user")
                                        }
                                    }
                                }
                                recipeArrayList.shuffle()
                                if(challengesList.isEmpty()){
                                    challengesList.add(recipeArrayList.get(0))
                                    challengesList.add(recipeArrayList.get(1))
                                }
                                recipeRecyclerView.adapter = ChallengesAdapter(challengesList)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}