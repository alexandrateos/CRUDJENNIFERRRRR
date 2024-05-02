package jennifer.teos.crudjenni2a

import Modelo.ClaseConnection
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        //2- Programar el boton

        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){


                // Guardar datos

                //1- Creo un objeto de la clase conexion

                val ClaseC = ClaseConnection().cadenaConexion()


                //2- Creo una variable que contenga PreparedStatement

                val addProducto = ClaseC?.prepareStatement("insert into tbProductos(nombreProducto, precio, cantidad) values(?,?,?)")!!
                addProducto.setString(1,txtName.text.toString())
                addProducto.setInt(2,txtPrecio.text.toString().toInt())
                addProducto.setInt(3,txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()
            }
        }

    }
}