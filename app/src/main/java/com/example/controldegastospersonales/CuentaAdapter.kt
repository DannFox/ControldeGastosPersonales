package com.example.controldegastospersonales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CuentaAdapter(private val cuentas: List<Cuenta>) : RecyclerView.Adapter<CuentaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cuenta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuenta = cuentas[position]
        holder.bind(cuenta)
    }

    override fun getItemCount(): Int {
        return cuentas.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCuentaTextView: TextView = itemView.findViewById(R.id.nombre_cuenta_text_view)
        private val saldoCuentaTextView: TextView = itemView.findViewById(R.id.saldo_cuenta_text_view)

        fun bind(cuenta: Cuenta) {
            nombreCuentaTextView.text = cuenta.nombre
            saldoCuentaTextView.text = String.format("$%.2f", cuenta.saldoActual)
        }
    }
}
