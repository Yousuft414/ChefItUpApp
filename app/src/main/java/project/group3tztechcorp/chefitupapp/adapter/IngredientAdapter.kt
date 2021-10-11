package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.group3tztechcorp.chefitupapp.Ingredient
import project.group3tztechcorp.chefitupapp.R

class IngredientAdapter (private val ingredientList : ArrayList<Ingredient>): RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    lateinit var context: Context

    class IngredientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.ingredientName)
        val checkbox = itemView.findViewById<CheckBox>(R.id.ingredientCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)

        context = parent.context

        return IngredientAdapter.IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = ingredientList[position]
        holder.name.text = currentItem.Name
        holder.checkbox.isSelected == currentItem.isSelected

        if (holder.checkbox.isSelected){
            currentItem.selected = true
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}