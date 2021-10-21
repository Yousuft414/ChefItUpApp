package project.group3tztechcorp.chefitupapp.recipeUI

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.Direction
import project.group3tztechcorp.chefitupapp.Ingredient
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.adapter.IngredientAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeIngredientsBinding
import project.group3tztechcorp.chefitupapp.databinding.ActivitySingleRecipePageBinding

private const val TAG = "MyActivity"

class RecipeIngredients : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private var ingredientArrayList: ArrayList<Ingredient> = ArrayList<Ingredient>()
    private lateinit var binding: ActivityRecipeIngredientsBinding
    private lateinit var adapter: IngredientAdapter
    lateinit var name: String
    private var check: Int = 0
    private var checked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipe_ingredients)
        var intent = getIntent()
        name = intent.getStringExtra("recipe").toString().trim()
        check = intent.getIntExtra("Check", 0)
        if(check == 1){
            this.window.decorView.setBackgroundColor(Color.RED)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_ingredients)


        getAllIngredients()

        binding.recipeIngredientList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val ingredients: Ingredient = ingredientArrayList!![position] as Ingredient
            ingredients.selected = !ingredients.selected
            adapter.notifyDataSetChanged()
        }

        binding.nextButton.setOnClickListener {
            for(i in 0 until adapter.count) {
                var ingredient: Ingredient = adapter.getItem(i)
                if (!ingredient.selected) {
                    checked = false
                    break
                } else {
                    checked = true
                }
            }
            if(checked) {
                var intent: Intent = Intent(this@RecipeIngredients, RecipeDirections::class.java)
                intent.putExtra("recipe", name)
                intent.putExtra("Check", check)
                startActivity(intent)
            } else {
                Toast.makeText(this,"You have not checked off all the ingredients", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getAllIngredients(){
        var recipe = name
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(recipe)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    snapshot.child(recipe).child("Ingredients").children.forEach {
                        ingredientArrayList!!.add(Ingredient(it.getValue().toString(), false))
                    }
                    adapter = IngredientAdapter(ingredientArrayList!!, applicationContext)
                    binding.recipeIngredientList.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}