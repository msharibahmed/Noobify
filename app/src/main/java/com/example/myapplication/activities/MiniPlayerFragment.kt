package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMiniPlayerBinding

class MiniPlayerFragment : Fragment(R.layout.fragment_mini_player){
    private  lateinit var binding: FragmentMiniPlayerBinding
    private lateinit var txtViewSongName :TextView
    private lateinit var txtViewCreatorName :TextView
    private lateinit var btnPrev :ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMiniPlayerBinding.inflate(layoutInflater,container,false)
        txtViewSongName =  binding.miniPlayerSongName
        txtViewCreatorName =  binding.miniPlayerCreatorName
        btnPrev = binding.miniPlayerPrevBtn

        getCurrentSongData()
        return binding.root

    }


    private fun getCurrentSongData(){

        val bundle = arguments
        val position = bundle?.getInt("index")
        if (position != null) {
            togglePrevBtn(position)
        }
        txtViewSongName.text = bundle?.getString("songName")
        txtViewCreatorName.text = bundle?.getString("creatorName")
        val audioPath = bundle?.getString("audioPath")
        Log.d("please print me", "$position $audioPath")

    }
    private fun togglePrevBtn(index:Int){
        btnPrev.isEnabled = index != 0

    }



}