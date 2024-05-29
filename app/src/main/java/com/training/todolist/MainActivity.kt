package com.training.todolist

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.todolist.database.ConsDataBase
import com.training.todolist.database.Task
import com.training.todolist.database.TaskAdapter
import com.training.todolist.database.ToDoListDataBase
import com.training.todolist.databinding.ActivityMainBinding
import com.training.todolist.databinding.DialogAddEditTaskBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dataBase: ToDoListDataBase
    lateinit var taskAdapter: TaskAdapter
    lateinit var taskList: MutableList<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBase = ToDoListDataBase(this)
        taskList = mutableListOf()
        taskAdapter = TaskAdapter(taskList ,{task ->showEditTaskDialog(task)}, {task -> deleteTask(task.id)} ,
            { task, isChecked -> updateTaskCompletion(task, isChecked) })
        binding.tasksRecyclerView.adapter = taskAdapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.fab.setOnClickListener { showAddTaskDialog() }
        viewTaskList()

    }

    private fun updateTaskCompletion(task: Task, checked: Boolean) {
        val db = dataBase.writableDatabase
        val values = ContentValues().apply {
            put(ConsDataBase.COMPLETED, if (task.completed) 0 else 1)
        }
        db.update(ConsDataBase.TABLE_NAME, values, "${ConsDataBase.ID} = ?", arrayOf(task.id.toString()))
        viewTaskList()
    }

    private fun showAddTaskDialog() {
        val dialog = DialogAddEditTaskBinding.inflate(layoutInflater)
        val dialogBinding = AlertDialog.Builder(this)
            .setView(dialog.root)
            .setTitle("Add Task")
            .setPositiveButton("Add") { _, _ ->
                val title = dialog.taskTitleEditText.text.toString()
                val description = dialog.taskDescriptionEditText.text.toString()
                addTask(title, description)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun addTask(title: String, description: String) {
    if (title.isNotEmpty()&&description.isNotEmpty())  {
        val db = dataBase.writableDatabase
        val values = ContentValues().apply {
            put(ConsDataBase.TITLE, title)
            put(ConsDataBase.DESCRIPTION, description)
            put(ConsDataBase.COMPLETED,0)
        }
        db.insert(ConsDataBase.TABLE_NAME, null, values)
        Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
        viewTaskList()
    } else {
        Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show()
    }
    }

    private fun showEditTaskDialog(task: Task) {
        val dialog = DialogAddEditTaskBinding.inflate(layoutInflater)
        dialog.taskTitleEditText.setText(task.title)
        dialog.taskDescriptionEditText.setText(task.description)
    AlertDialog.Builder(this)
        .setView(dialog.root)
        .setTitle("Edit Task")
        .setPositiveButton("Save") { _, _ ->
            val title = dialog.taskTitleEditText.text.toString()
            val description = dialog.taskDescriptionEditText.text.toString()
            updateTask(task.id, title, description)
    }
        .setNegativeButton("Cancel", null)
        .create()
        .show()
}

    private fun updateTask(id: Long, title: String, description: String) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            val db = dataBase.writableDatabase
            val values = ContentValues().apply {
                put(ConsDataBase.TITLE, title)
                put(ConsDataBase.DESCRIPTION, description)
            }
            db.update(ConsDataBase.TABLE_NAME, values, "${ConsDataBase.ID} =?", arrayOf(id.toString()))
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
            viewTaskList()
        } else {
            Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteTask(id: Long) {
        val db = dataBase.writableDatabase
        db.delete(ConsDataBase.TABLE_NAME, "${ConsDataBase.ID} =?", arrayOf(id.toString()))
        Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
        viewTaskList()
    }

    private fun viewTaskList() {
        taskList.clear()
        val db = dataBase.readableDatabase
        val projection = arrayOf(
            ConsDataBase.ID,
            ConsDataBase.TITLE,
            ConsDataBase.DESCRIPTION,
            ConsDataBase.COMPLETED
        )
        val cursor = db.query(ConsDataBase.TABLE_NAME,
            projection, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ConsDataBase.ID))
                val title = getString(getColumnIndexOrThrow(ConsDataBase.TITLE))
                val description = getString(getColumnIndexOrThrow(ConsDataBase.DESCRIPTION))
                val completed = getInt(getColumnIndexOrThrow(ConsDataBase.COMPLETED)) ==1
                taskList.add(Task(id, title, description,completed))
            }
        }
        taskAdapter.notifyDataSetChanged()
    }


}