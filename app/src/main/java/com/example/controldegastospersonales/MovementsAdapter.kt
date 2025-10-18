package com.example.controldegastospersonales

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class MovementsAdapter(private val movements: List<Movement>) : RecyclerView.Adapter<MovementsAdapter.MovementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movement, parent, false)
        return MovementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val movement = movements[position]
        holder.bind(movement)
    }

    override fun getItemCount() = movements.size

    class MovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.ivMovementIcon)
        private val description: TextView = itemView.findViewById(R.id.tvMovementDescription)
        private val date: TextView = itemView.findViewById(R.id.tvMovementDate)
        private val amount: TextView = itemView.findViewById(R.id.tvMovementAmount)

        fun bind(movement: Movement) {
            description.text = movement.description
            val formatter = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "ES"))
            date.text = formatter.format(movement.date)

            if (movement.type == MovementType.INGRESO) {
                amount.text = String.format("+$%,.2f", movement.amount)
                amount.setTextColor(Color.GREEN)
                icon.setImageResource(getIconForCategory(movement.categoryName, true))
            } else {
                amount.text = String.format("-$%,.2f", movement.amount)
                amount.setTextColor(Color.RED)
                icon.setImageResource(getIconForCategory(movement.categoryName, false))
            }
        }

        private fun getIconForCategory(categoryName: String, isIngreso: Boolean): Int {
            return if (isIngreso) {
                when (categoryName) {
                    "Salario" -> R.drawable.ic_salario
                    "Venta" -> R.drawable.ic_venta
                    "Intereses" -> R.drawable.ic_intereses
                    "Regalo" -> R.drawable.ic_regalos
                    "Otros" -> R.drawable.ic_other
                    else -> R.drawable.ic_category_placeholder
                }
            } else {
                when (categoryName) {
                    "Alimentacion" -> R.drawable.ic_alimentacion
                    "Transporte" -> R.drawable.ic_transporte
                    "Entretenimiento" -> R.drawable.ic_entretenimiento
                    "Salud" -> R.drawable.ic_salud
                    "Educación" -> R.drawable.ic_educacion
                    "Otros" -> R.drawable.ic_other
                    else -> R.drawable.ic_category_placeholder
                }
            }
        }
    }
}
