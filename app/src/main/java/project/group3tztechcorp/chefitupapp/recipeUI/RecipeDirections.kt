package project.group3tztechcorp.chefitupapp.recipeUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.Direction
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.UserInterface
import project.group3tztechcorp.chefitupapp.adapter.DirectionAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeDirectionsBinding

class RecipeDirections : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var binding: ActivityRecipeDirectionsBinding
    lateinit var name: String
    private var checked: Boolean = false

    private var directionArray: ArrayList<Direction> = ArrayList<Direction>()
    private lateinit var adapter: DirectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipe_directions)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_directions)

        var intent = getIntent()
        name = intent.getStringExtra("recipe").toString().trim()

        getAllDirections()

        binding.recipeDirectionsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val directions: Direction = directionArray!![position] as Direction
            directions.selected = !directions.selected
            adapter.notifyDataSetChanged()
        }

        binding.completeButton.setOnClickListener {
            /*for(item in 0 until adapter.count){
                var direction: Direction = adapter.getItem(item)
                if(direction.selected){
                    checked = true
                }
            }
            if(checked){
                var intent: Intent = Intent(this@RecipeDirections, UserInterface::class.java)
                intent.putExtra("recipe", name)
                startActivity(intent)
            } else{
                Toast.makeText(this,"You have not completed all the steps", Toast.LENGTH_SHORT).show()
            }*/
            var intent: Intent = Intent(this@RecipeDirections, UserInterface::class.java)
            intent.putExtra("recipe", name)
            startActivity(intent)
        }
    }

    fun getAllDirections(){
        var recipe = name
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(recipe)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
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