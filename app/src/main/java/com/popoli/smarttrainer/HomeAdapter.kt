package com.popoli.smarttrainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter(private var wps: List<WorkoutPlan>, private val onItemClick: (WorkoutPlan) -> Unit) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textTitle: TextView = view.findViewById(R.id.textTitle)
        private val textSubtitle: TextView = view.findViewById(R.id.textSubtitle)

        fun bind(wp: WorkoutPlan) {
            textTitle.id = wp.workoutPlanId.toInt()
            textTitle.text = wp.title
            textSubtitle.text = wp.days

            itemView.setOnClickListener { onItemClick(wp) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(wps[position])
    }

    override fun getItemCount(): Int = wps.size

    fun updateWps(newWps: List<WorkoutPlan>) {
        wps = newWps
        notifyDataSetChanged()
    }
}
