package project.group3tztechcorp.chefitupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Achievements : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        val ourList = generateDummyList(5)
        val recycleView = findViewById<RecyclerView>(R.id.recycleView)
        recycleView.adapter = MyRecyclerView(ourList)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        var drawable = R.drawable.trophy
        var text1 = ""
        for (i in 0 until size) {
            if (i % 5 == 0) {
                text1 = "Completed a recipe with milk"
            } else if (i % 5 == 1) {
                text1 = "Completed an easy recipe"
            } else if (i % 5 == 2) {
                text1 = "Completed an intermediate recipe"
            } else if (i % 5 == 3){
                text1 = "Completed an hard recipe"
            } else {
                text1 = "Made a recipe that has eggs"
            }
            val item = ListItem(drawable, text1)
            list += item
        }
        return list
    }
}