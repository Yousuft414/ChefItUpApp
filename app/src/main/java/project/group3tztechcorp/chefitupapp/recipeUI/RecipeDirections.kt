package project.group3tztechcorp.chefitupapp.recipeUI

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.adapter.DirectionAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeDirectionsBinding

private const val TAG = "MyActivity"

class RecipeDirections : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var binding: ActivityRecipeDirectionsBinding
    lateinit var name: String
    private var checked: Boolean = false
    lateinit var sharedPreferences: SharedPreferences
    private var user: UserInformation = UserInformation()
    private var username: String = "user"
    private var recipeLevel: String = "Easy"
    private var fullName: String = "user"
    private var completedList: ArrayList<String> = ArrayList<String>()
    private var completed: Boolean = false
    private var check: Int = 0

    private final val myPreferences: String = "MyPref"

    private var directionArray: ArrayList<Direction> = ArrayList<Direction>()
    private lateinit var adapter: DirectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipe_directions)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_directions)

        var intent = getIntent()
        name = intent.getStringExtra("recipe").toString().trim()
        check = intent.getIntExtra("Check", 0)
        if(check == 1){
            this.window.decorView.setBackgroundColor(Color.RED)
        }

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        fullName = sharedPreferences.getString("fullName", "").toString()

        getAllDirections()

        binding.recipeDirectionsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val directions: Direction = directionArray!![position] as Direction
            directions.selected = !directions.selected
            adapter.notifyDataSetChanged()
        }

        binding.completeButton.setOnClickListener {
            for(i in 0 until adapter.count) {
                var direction: Direction = adapter.getItem(i)
                if (!direction.selected) {
                    checked = false
                    break
                } else {
                    checked = true
                }
            }
            if(checked){
                reference = FirebaseDatabase.getInstance().getReference("userInformation")
                reference2 = FirebaseDatabase.getInstance().getReference("CompletedRecipes")
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

                            user = UserInformation(username, nameFromDB.toString(), levelFromDB.toString().toInt(), experienceFromDB.toString().toInt(), rewardsNumFromDB.toString().toInt(), recipiesNumFromDB.toString().toInt(), achivementsNumFromDB.toString().toInt())

                            if(check == 0) {
                                user.increaseExp(recipeLevel)
                            } else if(check == 1){
                                user.increaseExpChallenge(recipeLevel)
                            }
                            user.checkLevel()

                            reference.child(username).child("level").setValue(user.level)
                            reference.child(username).child("experience").setValue(user.experience)

                            var checks: Query = reference2.orderByChild("username").equalTo(username)
                            checks.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(snapshot.exists()) {
                                        var checkRecipe = false
                                        for(recipe in snapshot.child(username).child("completedList").children){
                                            if(recipe.getValue().toString().equals(name)){
                                                Log.i(TAG, "not adding recipe")
                                                checkRecipe = true
                                                break
                                            } else {
                                                Log.i(TAG, "adding recipe")
                                                checkRecipe = false
                                            }
                                        }
                                        if(!checkRecipe){
                                           user.addCompletedRecipes()
                                        }else{

                                        }
                                        reference.child(username).child("recipesCompleted").setValue(user.recipesCompleted)
                                    } else {
                                        user.addCompletedRecipes()
                                        reference.child(username).child("recipesCompleted").setValue(user.recipesCompleted)
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

                var check: Query = reference2.orderByChild("username").equalTo(username)

                check.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            snapshot.child(username).child("completedList").children.forEach {
                                completedList.add(it.getValue().toString())
                            }
                            for(item in completedList){
                                if(item.equals(name)){
                                    completed = true
                                    break
                                } else {
                                    completed = false
                                }
                            }
                            if(!completed){
                                completedList.add(name)
                            } else {

                            }
                            var completedRecipe = UserCompletedRecipe(username, completedList)
                            reference2.child(username).child("completedList").setValue(completedRecipe.completedList)
                        } else {
                            completedList.add(name)
                            var completedRecipe = UserCompletedRecipe(username, completedList)
                            reference2.child(username).setValue(completedRecipe)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

                var intent: Intent = Intent(this@RecipeDirections, UserInterface::class.java)
                intent.putExtra("recipe", name)
                intent.putExtra("username", username)
                intent.putExtra("fullName", fullName)
                startActivity(intent)
            } else{
                Toast.makeText(this,"You have not completed all the steps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getAllDirections(){
        var recipe = name
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(recipe)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    recipeLevel = snapshot.child(recipe).child("Level").getValue().toString()
                    snapshot.child(recipe).child("Directions").children.forEach {
                        //directionArrayList.add(it.getValue().toString())
                        directionArray!!.add(Direction(it.getValue().toString(), false))
                    }
                    adapter = DirectionAdapter(directionArray!!, applicationContext)
                    binding.recipeDirectionsList.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}