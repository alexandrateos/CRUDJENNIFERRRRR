package jennifer.teos.crudjenni2a

import Modelo.ClaseConnection
import Modelo.DataClassProductos
import Recicler.Adaptador
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mandar a llamar a todos los elementos de la pantalla
        val txtName = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)


        fun limpiar() {
            txtName.setText("")
            txtPrecio.setText("")
            txtCantidad.setText("")
        }

        //////////////////////////////////////////////////////////////Mostrar//////////////////////////////////////////////////////////////////////////////////


        val rcvProductos = findViewById<RecyclerView>(R.id.rcvProductos)

        ///1- Asignar un layout al recicleView

        rcvProductos.layoutManager = LinearLayoutManager(this)

        //funcion para obtener datos
        fun obtenerDatops(): List<DataClassProductos>{

            val objConexion= ClaseConnection().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbprodcutos")!!

            val productos= mutableListOf<DataClassProductos>()
            while (resultSet.next()){
                val nombre= resultSet.getString ("nombreProducto")
                val producto= DataClassProductos(nombre)
                productos.add(producto)
            }
            return productos

        }

        CoroutineScope(Dispatchers.IO).launch{
            val productosDB = obtenerDatops()
            withContext(Dispatchers.Main){
                val miAdapter = Adaptador(productosDB)
                rcvProductos.adapter= miAdapter

            }
        }

        //// todo: Guardar productos

        //2- Programar el boton

        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){


                // Guardar datos

                //1- Creo un objeto de la clase conexion

                val ClaseC = ClaseConnection().cadenaConexion()


                //2- Creo una variable que contenga PreparedStatement

                val addProducto = ClaseC?.prepareStatement("insert into tbprodcutos(nombreProducto, precio, cantidad) values(?,?,?)")!!
                addProducto.setString(1,txtName.text.toString())
                addProducto.setInt(2,txtPrecio.text.toString().toInt())
                addProducto.setInt(3,txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()

                val nuevosProductos = obtenerDatops()
                withContext(Dispatchers.Main){
                    (rcvProductos.adapter as? Adaptador)?.actualizar(nuevosProductos)
                }
            }
            //limpiar()
        }

    }
}