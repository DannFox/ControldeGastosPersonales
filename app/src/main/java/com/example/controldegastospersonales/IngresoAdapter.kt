package com.example.controldegastospersonales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngresoAdapter(private val ingresos: List<Ingreso>) : RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngresoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingreso, parent, false)
        return IngresoViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngresoViewHolder, position: Int) {
        val ingreso = ingresos[position]
        holder.bind(ingreso)
    }

    override fun getItemCount(): Int {
        return ingresos.size
    }

    class IngresoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvIngresoDescription)
        private val amountTextView: TextView = itemView.findViewById(R.id.tvIngresoAmount)

        fun bind(ingreso: Ingreso) {
            descriptionTextView.text = ingreso.descripcion
            amountTextView.text = String.format("+$%,.2f", ingreso.monto)
        }
    }
}
