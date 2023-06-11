package com.betsson.interviewteest.presentation.main

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.betsson.interviewteest.presentation.main.adapters.ResultAdapter
import com.betsson.interviewtest.R
import com.betsson.interviewtest.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),  View.OnClickListener {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ResultAdapter
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        initializeAdapter()
        setListeners()
        subscribeObservers()
        fetchData()
    }
    private fun initializeAdapter() {
        adapter = ResultAdapter(arrayListOf())
        binding.recyclerView.adapter = adapter
    }
    private fun fetchData() {
        viewModel.fetchData()
    }
    private fun setListeners(){
        binding.fabButton.setOnClickListener(this)
    }

    private fun subscribeObservers(){
        viewModel.betResults.observe(this) { betResults ->
            adapter.updateBetsList(betResults)
            binding.progressBar.visibility = View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                binding.progressBar.visibility = View.GONE
                showErrorDialog(errorMessage)
            }
        }

        viewModel.updatedResults.observe(this) { updatedResults ->
            adapter.updateBetsList(updatedResults)
        }
    }
    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.itemAnimator = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the state of the RecyclerView here for landscape orientation
        val layoutManager = binding.recyclerView.layoutManager
        val state = layoutManager?.onSaveInstanceState()
        outState.putParcelable(KEY_RECYCLER_STATE, state)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore the state of the RecyclerView
        val layoutManager = binding.recyclerView.layoutManager
        val state = savedInstanceState.getParcelable<Parcelable>(KEY_RECYCLER_STATE)
        layoutManager?.onRestoreInstanceState(state)
    }

    companion object {
        private const val KEY_RECYCLER_STATE = "recycler_state"
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.fabButton -> {
                viewModel.updateOdds()
            }
        }
    }
}