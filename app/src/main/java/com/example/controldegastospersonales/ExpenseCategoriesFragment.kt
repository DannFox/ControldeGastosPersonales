package com.example.controldegastospersonales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldegastospersonales.API.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpenseCategoriesFragment : Fragment() {

    private lateinit var categoriasRecyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        categoriasRecyclerView = view.findViewById(R.id.categorias_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
        categoriasRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadExpenseCategories()
    }

    private fun loadExpenseCategories() {
        APIClient.instance.getCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    val expenseCategories = response.body()?.filter { it.tipo == "Gasto" } ?: listOf()
                    if (expenseCategories.isEmpty()) {
                        categoriasRecyclerView.visibility = View.GONE
                        emptyView.visibility = View.VISIBLE
                    } else {
                        categoriasRecyclerView.visibility = View.VISIBLE
                        emptyView.visibility = View.GONE
                        categoriaAdapter = CategoriaAdapter(expenseCategories)
                        categoriasRecyclerView.adapter = categoriaAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
