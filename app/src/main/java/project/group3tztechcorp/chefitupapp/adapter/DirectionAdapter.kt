package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.group3tztechcorp.chefitupapp.Direction
import project.group3tztechcorp.chefitupapp.R

class DirectionAdapter(private val directionList : ArrayList<Direction>): RecyclerView.Adapter<DirectionAdapter.DirectionViewHolder>() {

    lateinit var context: Context

    class DirectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.recipe_direction)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkBoxes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectionViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_directions, parent, false)

        context = parent.context

        return DirectionAdapter.DirectionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DirectionViewHolder, position: Int) {
        val currentItem = directionList[position]
        holder.name.text = currentItem.Name
        holder.checkbox.isSelected == currentItem.isSelected

        if (holder.checkbox.isSelected){
            currentItem.selected = true
        }
    }

    override fun getItemCount(): Int {
        return directionList.size
    }
}