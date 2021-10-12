package project.group3tztechcorp.chefitupapp.recipeUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.Direction
import project.group3tztechcorp.chefitupapp.Ingredient
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.adapter.IngredientAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeDirectionsBinding
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeIngredientsBinding

class RecipeDirections : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private var directionArrayList: ArrayList<String> = ArrayList()
    private lateinit var binding: ActivityRecipeDirectionsBinding
    private lateinit var directionRecyclerView: RecyclerView
    private lateinit var directionListAdapter: ArrayAdapter<String>
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipe_directions)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_directions)

        var intent = getIntent()
        name = intent.getStringExtra("recipe").toString().trim()

        directionListAdapter = ArrayAdapter<String>(this, R.layout.list_directions2, directionArrayList)

        getAllDirections()
    }

    fun getAllDirections(){
        var recipe = name
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(recipe)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    snapshot.child(recipe).child("Directions").children.forEach {
                        directionArrayList.add(it.getValue().toString())
                    }
                    binding.recipeDirectionsList.adapter = directionListAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}