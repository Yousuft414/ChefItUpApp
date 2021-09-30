package project.group3tztechcorp.chefitupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Recipe

class SubCategoryAdapter(private val recipeList : ArrayList<Recipe>): RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.recipeName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sub_category, parent, false)

        return SubCategoryAdapter.RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = recipeList[position]

        holder.name.text = currentItem.Name
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}