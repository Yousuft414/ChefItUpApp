package project.group3tztechcorp.chefitupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.databinding.ActivitySingleRecipePageBinding

class SingleRecipePage : AppCompatActivity() {

    lateinit var name: String
    private lateinit var binding: ActivitySingleRecipePageBinding
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_single_recipe_page)

        binding =DataBindingUtil.setContentView(this, R.layout.activity_single_recipe_page)

        var intent = getIntent()
        name = intent.getStringExtra("Name").toString().trim()

        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(name)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var nameFromDB = snapshot.child(name).child("Name").getValue()
                    var categoryFromDB = snapshot.child(name).child("Category").getValue()
                    var levelFromDB = snapshot.child(name).child("Level").getValue()
                    var imageURLFromDB = snapshot.child(name).child("Image").getValue()
                    var descriptionFromDB = snapshot.child(name).child("Description").getValue()
                    var cookTimeFromDB = snapshot.child(name).child("Cook_Time").getValue()
                    var prepTimeFromDB = snapshot.child(name).child("Prep_Time").getValue()
                    var servingsFromDB = snapshot.child(name).child("Servings").getValue()

                    binding.singleRecipeName.text = nameFromDB.toString()
                    binding.singleRecipeCategory.text = categoryFromDB.toString()
                    binding.singleRecipeLevel.text = levelFromDB.toString()
                    binding.singleRecipeDescription.text = descriptionFromDB.toString()
                    binding.singleRecipeCookTime.text = cookTimeFromDB.toString() + " mins"
                    binding.singleRecipePrepTime.text = prepTimeFromDB.toString() + " mins"
                    binding.singleRecipeServing.text = servingsFromDB.toString() + " people"
                    Picasso.with(this@SingleRecipePage).load(imageURLFromDB.toString()).into(binding.singleRecipeImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}