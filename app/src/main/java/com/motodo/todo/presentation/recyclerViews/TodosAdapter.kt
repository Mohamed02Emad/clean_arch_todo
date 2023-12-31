package com.motodo.todo.presentation.recyclerViews

import android.util.Log
import android.view.LayoutInflater
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

class TodosAdapter(var list : ArrayList<ToDo>,val onCheckClicked: (ToDo , Int ) -> Unit) : RecyclerView.Adapter<TodosAdapter.TodosViewHolder>() {



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
       val todo = list[position]
        holder.binding.tvTitle.text = todo.title
        holder.binding.tvTime.text = todo.alarmTime ?: ""

        holder.binding.ivCheck.apply {
            setImageResource(
                // wrong names that's why i used !
                if(!todo.isChecked) R.drawable.ic_not_checked_square
                else R.drawable.ic_check
            )
        }


        holder.binding.cardBackground.setOnClickListener{
            onCheckClicked(todo , position)
        }

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

    override fun getItemCount(): Int = list.size

}

