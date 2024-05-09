package Recicler

import Modelo.ClaseConnection
import Modelo.DataClassProductos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jennifer.teos.crudjenni2a.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Adaptador(private var Datos: List<DataClassProductos>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizar (nuevaLista: List<DataClassProductos>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(vista)    }
    override fun getItemCount() = Datos.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProducto

        holder.imgBorrar.setOnClickListener {
            eliminarRegistro(producto.nombreProducto, position)
        }
    }

    fun eliminarRegistro (nombreProductos: String, position:  Int) {



        val ListaDatos = Datos.toMutableList()
        ListaDatos.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {

            val objConexion = ClaseConnection().cadenaConexion()
            val delProductos =
                objConexion?.prepareStatement("delete tbprodcutos where nombreProducto = ?")!!
            delProductos.setString(1, nombreProductos)
            delProductos.executeUpdate()

            val commit = objConexion?.prepareStatement("commit")!!
            commit.executeUpdate()
        }

        Datos = ListaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    }
