package com.example.controldegastospersonales

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransaccionAdapter(private val transacciones: List<Transaccion>) :
    RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
    TransaccionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaccion, parent, false)
        return TransaccionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        val transaccion = transacciones[position]
        holder.bind(transaccion)
    }

    override fun getItemCount(): Int {
        return transacciones.size
    }

    inner class TransaccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val montoTextView: TextView = itemView.findViewById(R.id.montoTextView)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
        private val fechaTextView: TextView = itemView.findViewById(R.id.fechaTextView)

        fun bind(transaccion: Transaccion) {
            montoTextView.text = String.format("$%.2f", transaccion.monto)
            descripcionTextView.text = transaccion.descripcion
            fechaTextView.text = transaccion.fecha

            if (transaccion.tipo == "gasto") {
                montoTextView.setTextColor(Color.RED)
            } else {
                montoTextView.setTextColor(Color.GREEN)
            }
        }
    }
}
