package com.example.bantalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

class MoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_mood, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize spinners by finding them by ID
        val spinner2: Spinner = view.findViewById(R.id.jawaban2)
        val spinner3: Spinner = view.findViewById(R.id.jawaban3)

        // Create an adapter for the spinner using a string array from resources
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.numbers_array, // This refers to the array of numbers 1-10 from strings.xml
            android.R.layout.simple_spinner_item
        )

        // Set the dropdown layout style for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinners
        spinner2.adapter = adapter
        spinner3.adapter = adapter
    }
}
