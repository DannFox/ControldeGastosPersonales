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

        holder.nombreGasto.text = gasto.Descripcion
        holder.fechaGasto.text = gasto.Fecha

        val format = NumberFormat.getCurrencyInstance(Locale.US)


        holder.montoGasto.text = format.format(gasto.Monto * -1)
        holder.iconoGasto.setImageResource(R.drawable.ic_category_placeholder)

    }

    override fun getItemCount() = gastos.size
}
