package com.example.controldegastospersonales

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CuentaAdapter(private val cuentas: List<Cuenta>) : RecyclerView.Adapter<CuentaAdapter.CuentaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuentaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cuenta, parent, false)
        return CuentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CuentaViewHolder, position: Int) {
        val cuenta = cuentas[position]
        holder.bind(cuenta)
    }

    override fun getItemCount(): Int {
        return cuentas.size
    }

    class CuentaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.tvNombreCuenta)
        private val saldoTextView: TextView = itemView.findViewById(R.id.tvSaldoActual)

        fun bind(cuenta: Cuenta) {
            nombreTextView.text = cuenta.nombre
            saldoTextView.text = String.format("$%,.2f", cuenta.saldoActual)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, AccountDetailActivity::class.java).apply {
                    putExtra("cuenta", cuenta)
                }
                context.startActivity(intent)
            }
        }
    }
}
