package com.training.todolist.database

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val completed: Boolean // New property

)
