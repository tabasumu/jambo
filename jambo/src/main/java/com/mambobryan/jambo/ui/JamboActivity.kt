package com.mambobryan.jambo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mambobryan.jambo.R
import com.mambobryan.jambo.data.JamboLog
import com.mambobryan.jambo.data.LogType
import com.mambobryan.jambo.databinding.ActivityJamboBinding
import com.mambobryan.jambo.helpers.onQueryTextChanged
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class JamboActivity : AppCompatActivity() {

    lateinit var binding: ActivityJamboBinding
    private val adapter: JamboLogAdapter = JamboLogAdapter()

    private val viewModel: JamboViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJamboBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupToolbar()

        setupRecycler()

        lifecycleScope.launchWhenResumed {
            viewModel.logs.collectLatest { adapter.submitData(it) }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.apply {

                    stateContent.isVisible = false
                    stateEmpty.isVisible = false
                    stateLoading.isVisible = false

                    when (loadState.source.refresh) {
                        is LoadState.Loading -> {
                            stateLoading.isVisible = true
                        }

                        is LoadState.Error -> {
                            Snackbar.make(root, "Error Getting Logs", Snackbar.LENGTH_SHORT).show()
                        }

                        is LoadState.NotLoading -> {

                            if (loadState.append.endOfPaginationReached) {
                                if (adapter.itemCount < 1)
                                    stateEmpty.isVisible = true
                                else {
                                    stateContent.isVisible = true
                                }
                            } else {
                                stateContent.isVisible = true
                            }


                        }
                    }
                }
            }
        }

    }

    private fun setupToolbar() {

        setSupportActionBar(binding.toolbarJambo)

        binding.apply {
            chipAll.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.ALL, isSelected)
            }
            chipInfo.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.INFO, isSelected)
            }
            chipDebug.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.DEBUG, isSelected)
            }
            chipError.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.ERROR, isSelected)
            }
            chipWarn.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.WARN, isSelected)
            }
            chipVerbose.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.VERBOSE, isSelected)
            }
            chipAssert.setOnCheckedChangeListener { _, isSelected ->
                updateSelectedTag(logType = LogType.ASSERT, isSelected)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            setupSearchView(menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Logs")
                    .setMessage("Are you sure you want to delete all logs?")
                    .setPositiveButton("YES") { _, _ -> viewModel.deleteAll() }
                    .setNegativeButton("NO") { _, _ -> }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isIconified = false
        searchView.onQueryTextChanged {
            viewModel.updateSearchQuery(it)
        }
    }

    private fun setupRecycler() {
        binding.apply {

            adapter.setListener(object : JamboLogAdapter.OnJamboLogClickListener {
                override fun onLogClicked(log: JamboLog) {
                    viewModel.selectLog(log)
                        showMoreBottomSheet()
                }
            })

            recyclerView.adapter = adapter

        }
    }

    private fun showMoreBottomSheet() {
        val dialog = MoreBottomSheet()
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun updateSelectedTag(logType: LogType, isSelected: Boolean) {
        if (isSelected) viewModel.updateTagFilter(logType)
    }

}