package com.example.controldegastospersonales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoriaAdapter(private val categorias: List<Categoria>) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.bind(categoria)
    }

    override fun getItemCount(): Int {
        return categorias.size
    }

    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.categoria_nombre)
        private val iconImageView: ImageView = itemView.findViewById(R.id.categoria_icon)

        fun bind(categoria: Categoria) {
            nombreTextView.text = categoria.nombre

            val iconRes = if (categoria.tipo == "Gasto") {
                when (categoria.nombre) {
                    "Alimentacion" -> R.drawable.ic_alimentacion
                    "Transporte" -> R.drawable.ic_transporte
                    "Entretenimiento" -> R.drawable.ic_entretenimiento
                    "Salud" -> R.drawable.ic_salud
                    "Educación" -> R.drawable.ic_educacion
                    "Otros" -> R.drawable.ic_other
                    else -> R.drawable.ic_category_placeholder
                }
            } else {
                when (categoria.nombre) {
                    "Salario" -> R.drawable.ic_salario
                    "Venta" -> R.drawable.ic_venta
                    "Intereses" -> R.drawable.ic_intereses
                    "Regalo" -> R.drawable.ic_regalos
                    "Otros" -> R.drawable.ic_other
                    else -> R.drawable.ic_category_placeholder
                }
            }

            iconImageView.setImageResource(iconRes)
        }
    }
}
