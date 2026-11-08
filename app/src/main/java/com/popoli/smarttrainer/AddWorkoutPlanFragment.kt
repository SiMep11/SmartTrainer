package com.popoli.smarttrainer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kotlin.getValue

const val CHAT_GPT_MODEL = "gpt-5-mini"
class AddWorkoutPlanFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_workout_plan, container, false)
    }

    val args: HomeFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //clearWorkouts()
        val etSport: EditText = view.findViewById(R.id.editTextSport)
        val cgDays: ChipGroup = view.findViewById(R.id.chipGroupDays)
        var chipDays: String? = null
        val etHours: EditText = view.findViewById(R.id.editTextHours)
        val etExtraNote: EditText = view.findViewById(R.id.editTextExtraNote)
        val btGenerate: Button = view.findViewById(R.id.buttonGenerate)
        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
        val apiClient = ApiClient.getInstance()
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        cgDays.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedId = checkedIds[0]
                val chip = group.findViewById<Chip>(checkedId)
                chipDays = chip.text.toString()
            }
        }

        btGenerate.setOnClickListener (object : View.OnClickListener {
            override fun onClick(view: View?){
                val sport: String = etSport.text.toString()
                val hours: String = etHours.text.toString()
                val extraNote: String = etExtraNote.text.toString()
                val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()
                if(etSport.text.toString() != "" && etHours.text.toString() != "" && chipDays != null){
                    progressBar.visibility = View.VISIBLE
                    myCoroutineScope.launch {
                        try {
                            val message = "Generate a workout plan for $sport in a ordered list divided in $chipDays where each workout " +
                                    "must not last more than $hours hours and that takes into account this additional information: " +
                                    "$extraNote, WITHOUT SUMMARY. WRITE THE REST IN EVERY EXERCISE. " +
                                    "Put it in a **EXERCISE_NAME**, SETS, REPS, REST FORMAT. DIVIDE EVERY DAY WITH ---. DON'T ADD ANY NOTE"

                            val request = OpenAIRequest(arrayListOf(Message(message, "user")), CHAT_GPT_MODEL)
                            val response = apiClient.createChatCompletion(request)
                            val workoutPlan = response.choices[0].message.content
                            //Log.i("simone", workoutPlan)
                            addWorkoutPlanToDatabase(sport, chipDays, args.username)
                            val WpId = dbDAO.getLastWpId()
                            addWorkoutsToDatabase(workoutPlan, WpId)
                        }catch(e: HttpException){
                            val errorBody = e.response()?.errorBody()?.string()
                            Log.e("simone", "Errore HTTP: ${e.code()} - $errorBody")
                        }finally {
                            withContext(Dispatchers.Main) {
                                progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Workout Created", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    suspend fun addWorkoutPlanToDatabase(sport: String, days: String?, user: String){
        val newWorkoutPlan = WorkoutPlan(title = "Workout " + sport, days = days.toString(), userID = user)
        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()
        dbDAO.insWorkoutPlan(newWorkoutPlan)
    }

    suspend fun addWorkoutsToDatabase(workouts: String, WpId: Long){
        val days = workouts.split("---").map { it.trim() }.filter { it.isNotEmpty() }
        val regex = Regex("(?i)^\\s*-?\\s*\\*\\*(.+?)\\*\\*\\s*,\\s*([^,]+?)\\s*,\\s*([^,]+?)\\s*,\\s*([^,]+?)\\s*\$")
        val dbDAO = AppDatabase.getInstance(requireContext().applicationContext).getDao()

        var d = 1
        for(day in days){
            val lines = day.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if(lines.isEmpty()) continue

            val workoutList = mutableListOf<Workout>()

            for(line in lines){
                val match = regex.find(line)
                if(match != null){
                    val (name, sets, reps, rest) = match.destructured
                    workoutList.add(Workout(exercise = name, sets = sets, reps = reps, rest = rest,day = d,  workoutPlan = WpId))
                    Log.i("simone", line)
                }
            }
            for(workout in workoutList){
                dbDAO.insWorkout(workout)
            }
            d++
        }
    }
}