package com.motodo.todo.presentation.recyclerViews

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.motodo.todo.R
import com.motodo.todo.databinding.CardTodoBinding
import com.motodo.todo.domain.models.Priority
import com.motodo.todo.domain.models.ToDo

class TodosAdapter : RecyclerView.Adapter<TodosAdapter.TodosViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<ToDo>() {
        override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    inner class TodosViewHolder(val binding: CardTodoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        return TodosViewHolder(
            CardTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {
       val todo = differ.currentList[position]
        holder.binding.tvTitle.text = todo.title
        holder.binding.tvTime.text = todo.alarmTime ?: ""

        holder.binding.ivCheck.setImageResource(
            // wrong names that's why i used !
            if(!todo.isChecked) R.drawable.ic_check_circle
            else R.drawable.ic_check
        )

        holder.binding.cardBackground.background = if (todo.isChecked) {
            ContextCompat.getDrawable(holder.binding.cardBackground.context, R.drawable.card_checked)
        }else{
            ContextCompat.getDrawable(holder.binding.cardBackground.context, R.drawable.card_not_checked)
        }

        if (todo.hasAlarm && !todo.isChecked){
            holder.binding.ivAlarm.isGone = false
            holder.binding.tvTime.isGone = false
        }else{
            holder.binding.ivAlarm.isGone = true
            holder.binding.tvTime.isGone = true
        }

        when(todo.priority){
            Priority.LOW -> {
                holder.binding.ivPriority.isGone = false
                holder.binding.ivPriority.setImageResource(R.drawable.ic_circle_red)
            }
            Priority.MEDIUM -> {
                holder.binding.ivPriority.isGone = false
                holder.binding.ivPriority.setImageResource(R.drawable.ic_circle_yellow)
            }
            Priority.HIGH -> {
                holder.binding.ivPriority.isGone = false
                holder.binding.ivPriority.setImageResource(R.drawable.ic_circle_green)
            }
            Priority.NONE -> {
                holder.binding.ivPriority.isGone = true
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

}

