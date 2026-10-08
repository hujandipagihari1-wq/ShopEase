package com.example.shopease.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.data.model.Product
import java.text.NumberFormat
import java.util.Locale
import coil.load
class ProductAdapter(
    private var products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageProduct: ImageView =
            itemView.findViewById(R.id.imageProduct)

        val textProductName: TextView =
            itemView.findViewById(R.id.textProductName)

        val textProductPrice: TextView =
            itemView.findViewById(R.id.textProductPrice)

        val textProductRating: TextView =
            itemView.findViewById(R.id.textProductRating)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = products[position]

        holder.textProductName.text = product.name

        val priceFormat = NumberFormat.getCurrencyInstance(
            Locale("id", "ID")
        )

        priceFormat.maximumFractionDigits = 0

        holder.textProductPrice.text =
            priceFormat.format(product.price)

        holder.textProductRating.text =
            "★ ${product.rating}"

        holder.imageProduct.load(product.image_url) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }
        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}