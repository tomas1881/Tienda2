
package Metodos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author coez
 */
public class MainController implements Initializable {
    
 private PilaListaCircular pilaProductos;
    private ObservableList<Producto> productos;
    
    public void setPilaProductos(PilaListaCircular pilaProductos) {
        this.pilaProductos = pilaProductos;
    }
    
    
    @FXML
    private Label label;
    @FXML
    private Button Salir;
    private TextField tfID;
    private TextField tfNombre;
    private DatePicker tfFechaLote;
    private DatePicker tfFechaVence;
    private TextField tfPrecio;
    @FXML
    private TableView<Producto> Tabla;

    @FXML
    private ComboBox<String> BoxBuscarPor;
    @FXML
    private TextField tfBuscarPor;
    
    
    
   // Variable miembro para la lista observable de productos
 
    @FXML
    private TableColumn<Producto, String> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, String> fechaLoteColumn;
    @FXML
    private TableColumn<Producto, String> fechaVenceColumn;
    @FXML
    private TableColumn<Producto, Integer> precioColumn;
    @FXML
    private ComboBox<String> BoxMeses;
    @FXML
    private Button btnMyMPromedio;
    @FXML
    private Button btnMyMPrecio;
    @FXML
    private Button btnPromedio;
    @FXML
    private Button btnListarPorMes;
    @FXML
    private Button btnActualizarLista;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnAnterior;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productos = FXCollections.observableArrayList();
        ObservableList<String> opciones = FXCollections.observableArrayList(
                "idProducto", "nomProducto", "FechaLote", "FechaVence", "precioU"
        );
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nomProducto"));
fechaLoteColumn.setCellValueFactory(new PropertyValueFactory<>("fechaLote"));
fechaVenceColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVence"));
precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioU"));


        BoxBuscarPor.setItems(opciones);
        BoxBuscarPor.setValue("idProducto");

        // Agrega este código en el método initialize
        Tabla.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Producto productoSeleccionado = row.getItem();
               
                }
            });
            return row;
        });

        ObservableList<String> opcionesMeses = FXCollections.observableArrayList(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        );
        BoxMeses.setItems(opcionesMeses);
        BoxMeses.setValue("Enero"); // Puedes establecer el valor predeterminado si lo deseas
    }

    public void cargarProductos(List<Producto> listaProductos) {
         List<Producto> listaDeProductos = listaProductos;
        productos.clear();
        productos.addAll(listaDeProductos);
        actualizarTabla();
    }

    private void actualizarTabla() {
        Tabla.setItems(productos);
    }
    
    
    
    
private float calcularPrecioPromedio() {
    float total = 0;
    for (Producto producto : productos) {
        total += producto.getPrecioU();
    }
    if (productos.isEmpty()) {
        return 0; // Evita la división por cero
    }
    return total / productos.size();
}



    @FXML
    private void handlePromedio (ActionEvent event) {
    float promedio = calcularPrecioPromedio();
    // Muestra el precio promedio en una ventana emergente
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Precio Promedio");
    alert.setHeaderText(null);
    alert.setContentText("El precio promedio de los productos es: " + promedio);
    alert.showAndWait();
}




private List<Producto> productosMayoresAlPromedio() {
    float promedio = calcularPrecioPromedio();
    List<Producto> productosMayores = new ArrayList<>();
    for (Producto producto : productos) {
        if (producto.getPrecioU() > promedio) {
            productosMayores.add(producto);
        }
    }
    return productosMayores;
}

private List<Producto> productosMenoresAlPromedio() {
    float promedio = calcularPrecioPromedio();
    List<Producto> productosMenores = new ArrayList<>();
    for (Producto producto : productos) {
        if (producto.getPrecioU() < promedio) {
            productosMenores.add(producto);
        }
    }
    return productosMenores;
}


    @FXML
    private void handleMyMPromedio(ActionEvent event) {
    List<Producto> productosMayores = productosMayoresAlPromedio();
    List<Producto> productosMenores = productosMenoresAlPromedio();

    // Muestra los productos en una ventana emergente
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Productos con Precio Mayor/Menor al Promedio");
    alert.setHeaderText(null);

    // Personaliza el contenido de la alerta
    String contenido = "Productos con precio mayor al promedio:\n";
    for (Producto producto : productosMayores) {
        contenido += "ID: " + producto.getIdProducto() + "\n";
        contenido += "Nombre: " + producto.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + producto.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + producto.getFechaVence() + "\n";
        contenido += "Precio: " + producto.getPrecioU() + "\n\n";
    }
    if (productosMayores.isEmpty()) {
        contenido += "No se encontraron productos con precio mayor al promedio.\n\n";
    }

    contenido += "Productos con precio menor al promedio:\n";
    for (Producto producto : productosMenores) {
        contenido += "ID: " + producto.getIdProducto() + "\n";
        contenido += "Nombre: " + producto.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + producto.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + producto.getFechaVence() + "\n";
        contenido += "Precio: " + producto.getPrecioU() + "\n\n";
    }
    if (productosMenores.isEmpty()) {
        contenido += "No se encontraron productos con precio menor al promedio.\n";
    }

    alert.setContentText(contenido);
    alert.showAndWait();
}


private Producto productoDeMayorPrecio() {
    if (productos.isEmpty()) {
        return null;
    }

    Producto mayorPrecio = productos.get(0);
    for (Producto producto : productos) {
        if (producto.getPrecioU() > mayorPrecio.getPrecioU()) {
            mayorPrecio = producto;
        }
    }
    return mayorPrecio;
}

private Producto productoDeMenorPrecio() {
    if (productos.isEmpty()) {
        return null;
    }

    Producto menorPrecio = productos.get(0);
    for (Producto producto : productos) {
        if (producto.getPrecioU() < menorPrecio.getPrecioU()) {
            menorPrecio = producto;
        }
    }
    return menorPrecio;
}


    @FXML
    private void handleMyMPrecio(ActionEvent event) {
    Producto mayorPrecio = productoDeMayorPrecio();
    Producto menorPrecio = productoDeMenorPrecio();

    // Muestra los productos en una ventana emergente
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Productos de Mayor y Menor Precio");
    alert.setHeaderText(null);

    // Personaliza el contenido de la alerta
    String contenido = "Producto de mayor precio:\n";
    if (mayorPrecio != null) {
        contenido += "ID: " + mayorPrecio.getIdProducto() + "\n";
        contenido += "Nombre: " + mayorPrecio.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + mayorPrecio.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + mayorPrecio.getFechaVence() + "\n";
        contenido += "Precio: " + mayorPrecio.getPrecioU() + "\n";
    } else {
        contenido += "No se encontró un producto de mayor precio.\n";
    }

    contenido += "\nProducto de menor precio:\n";
    if (menorPrecio != null) {
        contenido += "ID: " + menorPrecio.getIdProducto() + "\n";
        contenido += "Nombre: " + menorPrecio.getNomProducto() + "\n";
        contenido += "Fecha de Lote: " + menorPrecio.getFechaLote() + "\n";
        contenido += "Fecha de Vencimiento: " + menorPrecio.getFechaVence() + "\n";
        contenido += "Precio: " + menorPrecio.getPrecioU() + "\n";
    } else {
        contenido += "No se encontró un producto de menor precio.\n";
    }

    alert.setContentText(contenido);
    alert.showAndWait();
}




    @FXML
    private void handleActualizarLista(ActionEvent event) {
    // Implementación del método
    actualizarTabla();
}


    
    @FXML
     private void handleSalir(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }

    @FXML
    private void handleBuscarPor(ActionEvent event) {
    String campoSeleccionado = (String) BoxBuscarPor.getValue();
    String criterio = tfBuscarPor.getText().toLowerCase(); // Convertir a minúsculas para la comparación

    ObservableList<Producto> productosEncontrados = FXCollections.observableArrayList();

    for (Producto producto : productos) {
        String valorCampo = "";
        switch (campoSeleccionado) {
            case "idProducto":
                valorCampo = producto.getIdProducto();
                break;
            case "nomProducto":
                valorCampo = producto.getNomProducto();
                break;
            case "FechaLote":
                valorCampo = producto.getFechaLote().toString();
                break;
            case "FechaVence":
                valorCampo = producto.getFechaVence().toString();
                break;
            case "precioU":
                valorCampo = String.valueOf(producto.getPrecioU());
                break;
        }

        if (valorCampo.toLowerCase().contains(criterio)) {
            productosEncontrados.add(producto);
        }
    }

    // Actualiza la tabla con los productos encontrados
    Tabla.setItems(productosEncontrados);
}


    @FXML
    private void handleListarPorMes(ActionEvent event) {
    // Obtén el mes seleccionado del ComboBox
    String mesSeleccionado = BoxMeses.getValue();

    // Crea una lista para almacenar los productos que coinciden con el mes
    ObservableList<Producto> productosEncontrados = FXCollections.observableArrayList();

    for (Producto producto : productos) {
        // Obtiene el mes de la fecha de lote y fecha de vencimiento
        int mesLote = producto.getFechaLote().getMonthValue();
        int mesVencimiento = producto.getFechaVence().getMonthValue();

        // Compara si alguno de los meses coincide con el mes seleccionado
        if (mesLote == obtenerNumeroMes(mesSeleccionado) || mesVencimiento == obtenerNumeroMes(mesSeleccionado)) {
            productosEncontrados.add(producto);
        }
    }

    // Actualiza la tabla con los productos encontrados
    Tabla.setItems(productosEncontrados);
}

// Método para obtener el número de mes a partir del nombre
private int obtenerNumeroMes(String nombreMes) {
    switch (nombreMes) {
        case "Enero":
            return 1;
        case "Febrero":
            return 2;
        case "Marzo":
            return 3;
        case "Abril":
            return 4;
        case "Mayo":
            return 5;
        case "Junio":
            return 6;
        case "Julio":
            return 7;
        case "Agosto":
            return 8;
        case "Septiembre":
            return 9;
        case "Octubre":
            return 10;
        case "Noviembre":
            return 11;
        case "Diciembre":
            return 12;
        default:
            return 0; // Valor predeterminado si no se encuentra el mes
    }
}

    @FXML
    private void handleEliminarProducto(ActionEvent event) {
    // Obtén el producto seleccionado en la tabla
    Producto productoSeleccionado = Tabla.getSelectionModel().getSelectedItem();

    if (productoSeleccionado != null) {
        // Muestra un diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Producto");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar este producto?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Elimina el producto de la lista observable
            productos.remove(productoSeleccionado);
        }
    } else {
        // Muestra un mensaje si no se selecciona un producto
        mostrarAlerta("Eliminar Producto", "Por favor, selecciona un producto de la tabla.");
    }
}

// Método para mostrar una alerta
private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}






private void handleGuardarProducto(ActionEvent event) {
    // Obtiene el producto seleccionado en la tabla
    Producto productoSeleccionado = Tabla.getSelectionModel().getSelectedItem();

    if (productoSeleccionado != null) {
        // Actualiza los datos del producto seleccionado con los datos de los campos de edición
        productoSeleccionado.setIdProducto(tfID.getText());
        productoSeleccionado.setNomProducto(tfNombre.getText());
        productoSeleccionado.setFechaLote(tfFechaLote.getValue());
        productoSeleccionado.setFechaVence(tfFechaVence.getValue());
        productoSeleccionado.setPrecioU(Float.parseFloat(tfPrecio.getText()));

        // Limpia los campos de edición después de guardar los cambios
        tfID.clear();
        tfNombre.clear();
        tfFechaLote.setValue(null);
        tfFechaVence.setValue(null);
        tfPrecio.clear();

        // Actualiza solo la fila del producto modificado en la tabla
        Tabla.refresh();
    } else {
        mostrarAlerta("Guardar Producto", "Por favor, selecciona un producto de la tabla.");
    }
}



    @FXML
private void handleAnterior(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_1.fxml"));
        Parent root = loader.load();

        MainController_1 controller = loader.getController();
        controller.initData(pilaProductos); // Pasar la pila a MainController_1

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Manejo de errores durante la carga de la interfaz
    }
}

  void initData(PilaListaCircular pila) {
        this.pilaProductos = pila;
        List<Producto> listaDeProductos = new ArrayList<>();
        
    // Llenar listaDeProductos desde la pila
    NodoProducto temp = pila.obtenerCimaNodo();
    if (temp != null) {
        do {
            listaDeProductos.add(temp.getProducto());
            temp = temp.getSiguiente();
        } while (temp != pila.obtenerCimaNodo());
    }

  productos = FXCollections.observableArrayList(listaDeProductos);
        actualizarTabla();
}

}