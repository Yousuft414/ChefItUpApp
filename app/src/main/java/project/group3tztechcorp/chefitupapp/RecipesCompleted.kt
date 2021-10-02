package project.group3tztechcorp.chefitupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipesCompleted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_completed)
        val ourList = generateDummyList(3)
        val recycleView = findViewById<RecyclerView>(R.id.recycleView)
        recycleView.adapter = MyRecyclerView(ourList)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        var drawable = R.drawable.recipe_completed
        var text1 = ""
        for (i in 0 until size) {
            if (i % 3 == 0) {
                text1 = "Chocolate Drip Cake"
            } else if (i % 3 == 1) {
                text1 = "Pineapple Coconut Cake"
            } else {
                text1 = "Simple White Cake"
            }
            val item = ListItem(drawable, text1)
            list += item
        }
        return list
    }
}