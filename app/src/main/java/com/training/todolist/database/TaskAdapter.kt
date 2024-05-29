package com.training.todolist.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.training.todolist.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasksList: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit,
    private val onTaskComplete: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.root.setOnClickListener {
                onTaskClick(task)

            }
            binding.deleteButton.setOnClickListener {
                onDeleteClick(task)
            }
            binding.taskCompletedCheckBox.isChecked = task.completed
            binding.taskCompletedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onTaskComplete(task, isChecked)
            }
        }
    }
}
