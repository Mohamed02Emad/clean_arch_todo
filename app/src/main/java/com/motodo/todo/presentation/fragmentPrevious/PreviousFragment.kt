package com.motodo.todo.presentation.fragmentPrevious

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.motodo.todo.R
import com.motodo.todo.databinding.FragmentPreviousBinding
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.presentation.recyclerViews.TodosAdapter
import com.motodo.todo.utils.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviousFragment : Fragment() {

    private lateinit var binding: FragmentPreviousBinding
    private lateinit var myAdapter : TodosAdapter
    private val viewModel : PreviousViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviousBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
        setUpRecyclerView()
        setObservers()
    }

    private fun setObservers() {
        viewModel.todos.observe(viewLifecycleOwner){todos ->
            myAdapter.differ.submitList(todos)
        }
    }

    private fun setUpRecyclerView() {
        myAdapter = TodosAdapter { todo , position ->
            triggerTodoChecked(todo , position)
            myAdapter.notifyItemChanged(position)
        }
        binding.rvPrevTodos.adapter = myAdapter
        setupSwipeToDelete()
    }

    private fun triggerTodoChecked(todo: ToDo, position: Int) {
        todo.isChecked = !todo.isChecked
        lifecycleScope.launch {
            viewModel.updateTodo(todo , position)
        }
    }

    private fun setOnClicks() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSwipeToDelete() {
        val swipeToDeleteCallback: SwipeToDeleteCallback =
            object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    removeAfterSwiped(viewHolder)
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvPrevTodos)
    }

    private fun removeAfterSwiped(viewHolder: RecyclerView.ViewHolder) {
        val item = myAdapter.differ.currentList[viewHolder.adapterPosition]
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteTodo(item)
            showUndoSnackbar()
        }
    }

    private fun showUndoSnackbar() {
        val snackbar = Snackbar.make(
            binding.rvPrevTodos,
            "Todo deleted",
            Snackbar.LENGTH_LONG
        )
        snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
        snackbar.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary_blue))
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.color_dialog))
        snackbar.setAction("Undo") {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.undoDeleting()
            }
            snackbar.dismiss()
        }
        snackbar.show()
    }
}