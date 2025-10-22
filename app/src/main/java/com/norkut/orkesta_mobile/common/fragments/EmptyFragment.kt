package com.norkut.orkesta_mobile.common.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.norkut.orkesta_mobile.databinding.FragmentEmptyBinding


class EmptyFragment : Fragment() {

    private lateinit var binding: FragmentEmptyBinding
    private var _argMessage: String = String()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEmptyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArguments()
        setupView()
    }

    private fun setArguments() {
        arguments?.let {
            _argMessage = it.getString(ARG_MESSAGE, String())
        }
    }

    private fun setupView() {
        binding.message.text = _argMessage
    }

    companion object {
        private const val ARG_MESSAGE = "_argMessage"

        @JvmStatic
        fun newInstance(message: String) =
            EmptyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
    }



}