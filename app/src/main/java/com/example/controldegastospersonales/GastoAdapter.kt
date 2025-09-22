package com.example.controldegastospersonales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class GastoAdapter(private val gastos: List<Gasto>) :
    RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconoGasto: ImageView = itemView.findViewById(R.id.ivGastoIcono)
        val nombreGasto: TextView = itemView.findViewById(R.id.tvGastoNombre)
        val fechaGasto: TextView = itemView.findViewById(R.id.tvGastoFecha)
        val montoGasto: TextView = itemView.findViewById(R.id.tvGastoMonto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gasto, parent, false)
        return GastoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gasto = gastos[position]

        holder.nombreGasto.text = gasto.nombre
        holder.fechaGasto.text = gasto.fecha

        val format = NumberFormat.getCurrencyInstance(Locale.US)


        holder.montoGasto.text = format.format(gasto.monto * -1)
        if (gasto.iconoCategoriaResId != null) {
            holder.iconoGasto.setImageResource(gasto.iconoCategoriaResId)
            holder.iconoGasto.visibility = View.VISIBLE
        } else {
            holder.iconoGasto.visibility = View.GONE
        }

    }

    override fun getItemCount() = gastos.size
}

