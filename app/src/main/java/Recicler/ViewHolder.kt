package Recicler


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jennifer.teos.crudjenni2a.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txtProductocard)
    val imgEditar : ImageView = view.findViewById(R.id.imageView2)
    val imgBorrar : ImageView = view.findViewById(R.id.imageView)
}