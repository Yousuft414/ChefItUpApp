package project.group3tztechcorp.chefitupapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Recipe
import project.group3tztechcorp.chefitupapp.adapter.MainCategoryAdapter
import project.group3tztechcorp.chefitupapp.adapter.OnCategoryItemClickListener
import project.group3tztechcorp.chefitupapp.adapter.SubCategoryAdapter
import project.group3tztechcorp.chefitupapp.databinding.FragmentRecipeHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeHomeFragment : Fragment(), OnCategoryItemClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    private lateinit var categoryArrayList: ArrayList<String>
    lateinit var binding: FragmentRecipeHomeBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_home, container, false)

        recipeRecyclerView = binding.rvSubCategory
        recipeRecyclerView.layoutManager =
            LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)
        recipeRecyclerView.setHasFixedSize(true)

        categoryRecyclerView = binding.rvMainCategory
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        categoryArrayList = arrayListOf<String>()

        getCategories()
        getAllRecipies()

        if(binding.searchView != null){
            binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    search(newText)
                    return true
                }
            })
        }

        return binding.root
        //return inflater.inflate(R.layout.fragment_recipe_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecipeHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecipeHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getAllRecipies() {
        reference = FirebaseDatabase.getInstance().getReference("recipes")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (recipeSnapshot in snapshot.children) {
                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        recipeArrayList.add(recipe!!)
                    }
                    recipeRecyclerView.adapter = SubCategoryAdapter(recipeArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getCategories() {
        categoryArrayList.add("Dessert")
        categoryArrayList.add("Breakfast")
        categoryArrayList.add("Lunch")
        categoryArrayList.add("Dinner")
        categoryRecyclerView.adapter = MainCategoryAdapter(categoryArrayList, this)
    }

    override fun onItemClick(item: String, position: Int) {
        val category = categoryArrayList!![position]
        var categoryList: ArrayList<Recipe> = ArrayList<Recipe>()
        reference = FirebaseDatabase.getInstance().getReference("recipes")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (recipeSnapshot in snapshot.children) {
                        if (recipeSnapshot.child("Category").getValue().toString().equals(category)) {
                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                            categoryList.add(recipe!!)
                        }
                    }
                    recipeRecyclerView.adapter = SubCategoryAdapter(categoryList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun search(str: String){
        var searchList: ArrayList<Recipe> = ArrayList<Recipe>()
        for(recipe in recipeArrayList){
            if (recipe.Name!!.lowercase().contains(str.lowercase())){
                searchList.add(recipe)
            }
        }
        recipeRecyclerView.adapter = SubCategoryAdapter(searchList)
    }
}