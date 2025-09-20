package com.example.controldegastospersonales // Asegúrate de que el paquete sea el correcto

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

    // ViewHolder que contiene las vistas para cada ítem de gasto
    class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconoGasto: ImageView = itemView.findViewById(R.id.ivGastoIcono)
        val nombreGasto: TextView = itemView.findViewById(R.id.tvGastoNombre)
        val fechaGasto: TextView = itemView.findViewById(R.id.tvGastoFecha)
        val montoGasto: TextView = itemView.findViewById(R.id.tvGastoMonto)
    }

    // Crea nuevas vistas (invocado por el layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        // Crea una nueva vista, que define la UI del ítem de la lista
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gasto, parent, false) // Usa tu item_gasto.xml
        return GastoViewHolder(view)
    }

    // Reemplaza el contenido de una vista (invocado por el layout manager)
    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        // Obtiene el elemento de tu dataset en esta posición y reemplaza
        // el contenido de la vista con ese elemento
        val gasto = gastos[position]

        holder.nombreGasto.text = gasto.nombre
        holder.fechaGasto.text = gasto.fecha

        // Formatear el monto como moneda
        val format = NumberFormat.getCurrencyInstance(Locale.US) // Ejemplo: Español de España
        // Para dólares, podrías usar Locale.US o crear un NumberFormat específico
        // val format = NumberFormat.getCurrencyInstance(Locale.US)
        // format.currency = Currency.getInstance("USD") // O la moneda que necesites

        holder.montoGasto.text = format.format(gasto.monto * -1) // Multiplica por -1 para mostrarlo como negativo si es un gasto

        // Asignar el icono de categoría (si existe)
        if (gasto.iconoCategoriaResId != null) {
            holder.iconoGasto.setImageResource(gasto.iconoCategoriaResId)
            holder.iconoGasto.visibility = View.VISIBLE
        } else {
            // Opcional: si no hay icono, puedes ocultar el ImageView o poner un placeholder
            holder.iconoGasto.visibility = View.GONE // OsetImageResource(R.drawable.ic_default_placeholder)
        }

        // Aquí podrías añadir un OnClickListener para el ítem si lo necesitas
        // holder.itemView.setOnClickListener { /* acción al hacer clic */ }
    }

    // Devuelve el tamaño de tu dataset (invocado por el layout manager)
    override fun getItemCount() = gastos.size
}

