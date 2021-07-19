package com.example.recycler_view_example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CatalogueFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CatalogueFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}