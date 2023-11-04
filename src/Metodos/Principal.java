package Metodos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal extends Application {
    private PilaListaCircular pilaProductos; // Usar la pila en lugar de una lista

    @Override
    public void start(Stage stage) throws Exception {
        pilaProductos = new PilaListaCircular(); // Inicializar la pila
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_1.fxml"));
        Parent root = loader.load();

        MainController_1 controller = loader.getController();
        controller.initData(pilaProductos); // Pasar la pila

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
