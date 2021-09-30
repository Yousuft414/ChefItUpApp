package project.group3tztechcorp.chefitupapp

data class Recipe(var ID : Int, var Category: String, var Name: String, var Description: String,
var Cook_Time: Int, var Image: String, var Level: String, var Prep_Time: Int, var Servings: String,
var Ingredients: ArrayList<String>, var Directions: ArrayList<String>)
