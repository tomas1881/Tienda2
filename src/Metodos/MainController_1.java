package Metodos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController_1 {
    private PilaListaCircular pilaProductos;
    private ObservableList<Producto> productos = FXCollections.observableArrayList();

    @FXML
private DatePicker tfFechaLote;

@FXML
private DatePicker tfFechaVence;

@FXML
private TextField tfID;

@FXML
private TextField tfNombre;

@FXML
private TextField tfPrecio;

@FXML
private TableView<Producto> Tabla;


   void initData(PilaListaCircular pila) {
        this.pilaProductos = pila;
        productos.setAll(obtenerListaDeProductos());
        if (Tabla != null) {
            Tabla.setItems(productos);
        } else {
            System.out.println("Tabla es null. Verifica el enlace en el archivo FXML.");
        }
    
}

    @FXML
    private void handleAgregar(ActionEvent event) {
        while (true) {
            if (camposIncompletos()) {
                mostrarAlerta("Agregar Producto", "Por favor, completa todos los campos para guardar el producto.");
                break;
            }

            if (idExistente()) {
                mostrarAlerta("Agregar Producto", "Ya existe un producto con esa ID. Ingresa una ID única.");
                break;
            }

            Producto producto = crearProductoDesdeDatos();
            pilaProductos.apilar(producto);

            limpiarCampos();
            cargarProductosEnTabla();

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Agregar Producto");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("¿Desea agregar otro producto?");

            Optional<ButtonType> result = confirmDialog.showAndWait();

            if (result.isPresent() && result.get() != ButtonType.OK) {
                break; // Salir del bucle si se presiona un botón diferente a "Aceptar"
            }
        }
    }

   // Método para verificar si hay campos incompletos en la información del producto
private boolean camposIncompletos() {
    return tfFechaLote.getValue() == null || tfFechaVence.getValue() == null 
        || tfID.getText().isEmpty() || tfNombre.getText().isEmpty() || tfPrecio.getText().isEmpty();
}


private boolean idExistente() {
    String idProducto = tfID.getText();
    for (Producto producto : productos) {
        if (producto.getIdProducto().equals(idProducto)) {
            mostrarAlerta("ID Existente", "Ya existe un producto con este ID. Ingresa uno diferente.");
            return true; // Si el ID ya existe en la lista de productos
        }
    }
    return false; // Si el ID no existe en la lista de productos
}

  // Método para crear un nuevo objeto Producto a partir de los datos ingresados en los campos
private Producto crearProductoDesdeDatos() {
    LocalDate fechaLote = tfFechaLote.getValue();
    LocalDate fechaVence = tfFechaVence.getValue();
    String idProducto = tfID.getText();
    String nomProducto = tfNombre.getText();
    float precio = Float.parseFloat(tfPrecio.getText());
    
    return new Producto(idProducto, nomProducto, fechaLote, fechaVence, precio);
}



// Método para manejar el flujo de creación de un nuevo producto
private void manejarCreacionProducto() {
    if (camposIncompletos()) {
        mostrarAlerta("Campos Incompletos", "Por favor, completa todos los campos.");
    } else if (idExistente()) {
        mostrarAlerta("ID Existente", "Ya existe un producto con esta ID. Ingresa uno diferente.");
    } else {
        Producto nuevoProducto = crearProductoDesdeDatos();
        productos.add(nuevoProducto);
        limpiarCampos();
        mostrarAlerta("Producto Agregado", "El producto ha sido agregado exitosamente.");
        cargarProductosEnTabla();
    }
}


    private void limpiarCampos() {
    tfFechaLote.setValue(null);
    tfFechaVence.setValue(null);
    tfID.clear(); // Limpia el campo de texto para el ID
    tfNombre.clear(); // Limpia el campo de texto para el nombre
    tfPrecio.clear(); // Limpia el campo de texto para el precio
}


    private void mostrarAlerta(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<Producto> obtenerListaDeProductos() {
        List<Producto> listaDeProductos = new ArrayList<>();
        if (!pilaProductos.estaVacia()) {
            NodoProducto temp = pilaProductos.obtenerCimaNodo();
            do {
                listaDeProductos.add(temp.getProducto());
                temp = temp.getSiguiente();
            } while (temp != pilaProductos.obtenerCimaNodo());
        }
        return listaDeProductos;
    }

    private void cargarProductosEnTabla() {
    if (Tabla != null) {
        productos.setAll(obtenerListaDeProductos());
        Tabla.setItems(productos);
    } else {
        System.out.println("Tabla es null. Verifica el enlace en el archivo FXML.");
    }
}

    @FXML
private void handleSiguiente(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.cargarProductos(obtenerListaDeProductos()); // Pasar la lista de productos

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Manejo de errores durante la carga de la interfaz
    }
    }

    @FXML
    private void handleSalir(ActionEvent event) {
        System.exit(0); // Cierra la aplicación
    }
}


