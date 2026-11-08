package com.popoli.smarttrainer

import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutAdapter(private val workouts: List<Workout>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private val giorni = workouts.map { it.day }.distinct()

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val layoutTable: TableLayout = itemView.findViewById(R.id.layoutTable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val day = giorni[position]
        holder.tvDay.text = "Day $day"

        holder.layoutTable.removeAllViews()

        val header = TableRow(holder.itemView.context)
        header.addView(makeCell(holder, "Exercise", true))
        header.addView(makeCell(holder, "Sets", true))
        header.addView(makeCell(holder, "Reps", true))
        header.addView(makeCell(holder, "Rest (min)", true))
        holder.layoutTable.addView(header)

        val dayWorkouts = workouts.filter { it.day == day }
        dayWorkouts.forEach { w ->
            val row = TableRow(holder.itemView.context)
            row.addView(makeCell(holder, w.exercise))
            row.addView(makeCell(holder, w.sets))
            row.addView(makeCell(holder, w.reps))
            row.addView(makeCell(holder, w.rest))
            holder.layoutTable.addView(row)
            holder.layoutTable.requestLayout()
        }

    }

    override fun getItemCount(): Int = giorni.size
    private fun makeCell(holder: WorkoutViewHolder, text: String, isHeader: Boolean = false): TextView {
        return TextView(holder.itemView.context).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f) // 🔑 larghezza uguale
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.table_cell_border)
            if (isHeader) {
                setTypeface(null, Typeface.BOLD)
            }
        }
    }
}
