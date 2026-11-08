package com.popoli.smarttrainer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    val args: HomeFragmentArgs by navArgs()
    private lateinit var adapter: HomeAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageButtonLogout: ImageButton = view.findViewById(R.id.imageButtonDelete)
        val imageButtonAdd: ImageButton = view.findViewById(R.id.imageButtonAdd)
        val navController = view.findNavController()
        val TvUser: TextView = view.findViewById(R.id.tvUser)
        TvUser.text = "Welcome, " + args.username
        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()
        adapter = HomeAdapter(emptyList()) {wp -> val workoutPlanId = wp.workoutPlanId
            val action = HomeFragmentDirections.viewWorkouts(workoutPlanId, wp.title)
            navController.navigate(action)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            dbDAO.getUserWorkoutPlans(args.username).collect { list -> adapter.updateWps(list)
                if (list.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    tvEmpty.visibility = View.GONE
                    adapter.updateWps(list)
                }
            }
        }

        imageButtonLogout.setOnClickListener (object : View.OnClickListener {
            @SuppressLint("UseKtx")
            override fun onClick(v: View?) {
                val sharedPreferences = requireActivity().applicationContext.getSharedPreferences("sp", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                navController.navigate(R.id.signInFragment)
            }
        })

        imageButtonAdd.setOnClickListener (object: View.OnClickListener{
            override fun onClick(v: View?){
                val action = HomeFragmentDirections.addWorkoutPlan(args.username)
                navController.navigate(action)
            }
        })
    }
}