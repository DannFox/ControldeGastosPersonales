package com.example.controldegastospersonales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class CuentaAdapter(private val accounts: List<Cuenta>) :
    RecyclerView.Adapter<CuentaAdapter.AccountViewHolder>() {

    class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountItemContainer: LinearLayout = itemView.findViewById(R.id.llAccountItemContainer)
        val accountSymbol: ImageView = itemView.findViewById(R.id.ivAccountSymbol)
        val accountName: TextView = itemView.findViewById(R.id.tvAccountName)
        val accountBalance: TextView = itemView.findViewById(R.id.tvAccountBalance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val cuenta = accounts[position]

        holder.accountName.text = cuenta.Nombre

        val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        holder.accountBalance.text = format.format(cuenta.SaldoActual)
        
        // Use a default icon
        holder.accountSymbol.setImageResource(R.drawable.baseline_account_balance_24)

        // Remove color logic since it's not in Cuenta model
        holder.accountItemContainer.background?.let { background ->
            val wrappedDrawable = DrawableCompat.wrap(background.mutate())
            DrawableCompat.setTintList(wrappedDrawable, null)
            holder.accountItemContainer.background = wrappedDrawable
        }

        holder.itemView.setOnClickListener {
            // Handle click if necessary
        }
    }

    override fun getItemCount() = accounts.size
}