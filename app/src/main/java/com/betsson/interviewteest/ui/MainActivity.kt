package com.betsson.interviewteest.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betsson.interviewtest.R
import com.betsson.interviewtest.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ResultAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = viewModel

        setupRecyclerView(binding.recyclerView)
        viewModel.betResults.observe(this) { betResults ->
            adapter = ResultAdapter(betResults)
            recyclerView = binding.recyclerView
            recyclerView.adapter = adapter
            binding.progressBar.visibility = View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showErrorDialog(errorMessage)
            }
        }




        binding.fabButton.setOnClickListener {
            viewModel.updateOdds()
        }

        viewModel.fetchData()
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the state of the RecyclerView here
        val layoutManager = recyclerView.layoutManager
        val state = layoutManager?.onSaveInstanceState()
        outState.putParcelable(KEY_RECYCLER_STATE, state)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the state of the RecyclerView here
        val layoutManager = recyclerView.layoutManager
        val state = savedInstanceState.getParcelable<Parcelable>(KEY_RECYCLER_STATE)
        layoutManager?.onRestoreInstanceState(state)
    }

    companion object {
        private const val KEY_RECYCLER_STATE = "recycler_state"
    }
}
