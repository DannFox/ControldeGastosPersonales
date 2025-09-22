
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.Account
import com.example.controldegastospersonales.R

class AccountAdapter(private val accounts: List<Account>) :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

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
        val account = accounts[position]

        holder.accountName.text = account.name
        holder.accountBalance.text = "${account.balance} ${account.currency}"
        holder.accountSymbol.setImageResource(account.symbolResId)

        // Aplicar el color de fondo
        try {
            val color = Color.parseColor(account.colorHex)
            holder.accountItemContainer.background?.let { background ->
                val wrappedDrawable = DrawableCompat.wrap(background.mutate())
                DrawableCompat.setTint(wrappedDrawable, color)
                holder.accountItemContainer.background = wrappedDrawable
            }
        } catch (e: IllegalArgumentException) {
        }

        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount() = accounts.size
}
