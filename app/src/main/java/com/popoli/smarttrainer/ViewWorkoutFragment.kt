package com.popoli.smarttrainer

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewWorkoutFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_workout, container, false)
    }

    val args: ViewWorkoutFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageButtonDelete = view.findViewById<ImageButton>(R.id.imageButtonDelete)
        val tvWorkout = view.findViewById<TextView>(R.id.tvWorkout)
        tvWorkout.text = args.wpName
        recycler = view.findViewById(R.id.recyclerViewWorkouts)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()
        val myCoroutineScope = CoroutineScope(Dispatchers.IO)

        myCoroutineScope.launch {
            val workouts = dbDAO.getAllWorkouts(args.wpId)
            withContext(Dispatchers.Main) {
                adapter = WorkoutAdapter(workouts)
                recycler.adapter = adapter
            }
        }

        imageButtonDelete.setOnClickListener (object : View.OnClickListener {
            override fun onClick(v: View?) {
                showDeleteConfirmationDialog()
            }
        })
    }

    private fun showDeleteConfirmationDialog() {
        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()
        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
        val navController = view?.findNavController()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Deletion")
        builder.setMessage("All the data will be deleted from the database")

        builder.setPositiveButton("Yes") { dialog, _ ->
            myCoroutineScope.launch {
                dbDAO.deleteWorkoutPlans(args.wpId)
                withContext(Dispatchers .Main){
                    navController?.navigate(R.id.deletedWorkout)
                }
            }
            Toast.makeText(requireContext(), "Workout deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}