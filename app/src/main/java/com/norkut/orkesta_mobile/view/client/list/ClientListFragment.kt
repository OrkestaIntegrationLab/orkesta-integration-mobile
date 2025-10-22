package com.norkut.orkesta_mobile.view.client.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.common.adapters.EmptyAdapter
import com.norkut.orkesta_mobile.common.fragments.EmptyFragment
import com.norkut.orkesta_mobile.common.utils.UI
import com.norkut.orkesta_mobile.databinding.FragmentClientListBinding
import com.norkut.orkesta_mobile.domain.client.Client
import com.norkut.orkesta_mobile.domain.client.ClientFilter
import com.norkut.orkesta_mobile.view.client.detail.ClientDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientListFragment : Fragment() {

    private lateinit var binding: FragmentClientListBinding
    private val viewModel: ClientListViewModel by viewModels()
    private lateinit var filter: ClientFilter
    private lateinit var errorFragment: EmptyFragment
    private lateinit var emptyAdapter: EmptyAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var adapter: ClientListAdapter



    init {
        lifecycleScope.launchWhenStarted {
            activity?.title = getString(R.string.client_title)
            setHasOptionsMenu(true)
            filter = ClientFilter()
            observe()
            loadClientList(filter)
            setupRecyclerView()
            setupSwipeContainer()
            setOnClickListeners()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observe() {
        observeState()
        observeClientList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?):
            View = FragmentClientListBinding.inflate(inflater, container, false)
        .root.also { binding = FragmentClientListBinding.bind(it) }



    private fun setOnClickListeners() = with(binding) {
        tvFilter.setOnClickListener {
            showFilterDialog()
        }
        fabAdd.setOnClickListener {
            val client = Client()
            clientDetailActivity(client)
        }
    }

    private fun observeClientList() {
        viewModel.clientList.observe(viewLifecycleOwner) {
            hideLoading()
            it?.let {
                setupListResult(it)
            }
        }
    }

    private fun setupListResult(client: List<Client>) {
        binding.apply {
            activity?.closeEmptyFragment()
            adapter.swapData(client.toMutableList())
            val total = client.size
            tvItemsCount.text = context?.resources?.getQuantityString(R.plurals.numberOfItinerary, total, total)
            emptyAdapter.updateItemsCount(total)
        }
    }

    private fun hideLoading() {
        showLoading(false)
        swipeContainer.isRefreshing = false
        binding.pbLoadingNextPage.isVisible = false
    }



    private fun setupSwipeContainer() {
        swipeContainer = binding.swipeContainer
        swipeContainer.setOnRefreshListener { loadClientList(filter,showLoading = false) }
    }


    private fun loadClientList(clientFilter: ClientFilter, showLoading: Boolean = true) {
        showLoading(showLoading)
        filter = clientFilter
        viewModel.getClientList(filter)
    }

    private fun showLoading(show: Boolean) = with(binding) {
        progressbar.root.isVisible = show
    }


    private fun setupRecyclerView() {
        filter= ClientFilter()
        val click = { item: Client -> clientDetailActivity (item)}
        adapter = ClientListAdapter(mutableListOf(), click,context)
        emptyAdapter = EmptyAdapter(getString(R.string.result_not_found))
        binding.rvItems.adapter = ConcatAdapter(adapter, emptyAdapter)
    }

    private fun clientDetailActivity( value : Client) {
        requireActivity().run {
            val intent = Intent(this, ClientDetailActivity::class.java).apply {
                putExtra(ClientDetailActivity.ARG_CLIENT, value)
            }
            resultClientActivity.launch(intent)
        }
    }

    private val resultClientActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result -> if (result.resultCode == Activity.RESULT_OK) loadClientList(filter) }





    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let {
                hideLoading()
                activity?.openEmptyFragment(it.globalInfo)
                UI.showToast(requireContext(), getString(it.toast), Toast.LENGTH_LONG)
                UI.showDialog(activity, it.dialogRes.first, getString(it.dialogRes.second), getString(R.string.dialog_Accept))
            }
        }
    }



    private fun FragmentActivity.openEmptyFragment(resId: Int?) {
        resId?.let {
            binding.clHeader.isGone = true
            binding.rvItems.isGone = true
            errorFragment = if (::errorFragment.isInitialized) errorFragment else EmptyFragment.newInstance(getString(it))
            return@let supportFragmentManager.beginTransaction().replace(R.id.cl_principal_clients, errorFragment).commit()
        }
    }

    private fun FragmentActivity.closeEmptyFragment() {
        if (::errorFragment.isInitialized) {
            binding.clHeader.isVisible = true
            binding.rvItems.isVisible = true
            supportFragmentManager.beginTransaction().remove(errorFragment).commit()
        }
    }




    private fun showFilterDialog() {
        viewModel.loadDocumentTypeIfNeeded()
        var dialog: AlertDialog? = null
        val dialogBuilder = ClientListFilterDialog(requireContext(), layoutInflater)
            .setDocumentNumber()
        viewModel.documentTypeList.observe(viewLifecycleOwner, dialogBuilder::setDocumentTypes)
        dialog = dialogBuilder
            .setTitle(R.string.action_filter)
            .setPositiveButton(R.string.dialog_Accept) { _, _ ->
                filter = dialogBuilder.filter
                viewModel.getClientList(filter)}
            .setNegativeButton(R.string.dialog_cancel, null)
            .create()
        dialog.show()
    }




    private fun observeDocumentTypeList(){
        viewModel.documentTypeList.observe(viewLifecycleOwner){
            Log.i("tipos de documentos:" , it.toString() )
            with(binding){
                val mutableCategories = it.toMutableList()
                val values = mutableCategories.map { "${it.documentTypeName} - ${it.identifier}" }
                //etAvailabilityType.setAdapter(arrayAdapter(values))
                /*etAvailabilityType.setOnItemClickListener { _, _, position, _ ->
                     //filter.idCategory = position.inc()
                }*/

            }

        }
    }







}