package com.Rainydev.Renombrador;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Renombrador extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Renombrador.fxml"));
        primaryStage.setTitle("Ez File Renamer");
        primaryStage.setScene(new Scene(root, 600, 494));
        primaryStage.show();

    }

    @FXML
    private Button SeleccionaCarpeta;

    @FXML
    private TextField Locacion;

    @FXML
    private Pane Principal;

    @FXML
    private TableView TablaArchivos;

    @FXML
    private TableColumn ColumnaAnterior;

    @FXML
    private TableColumn ColumnaNueva;

    @FXML
    private Button Previsualizacion;

    @FXML
    private Button Renombrar;

    @FXML
    private TextField Formato;

    public File selectedDirectory;

    public Vector<Archivo> listaArchivos;

    public void AbreSelector(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:\\"));
        directoryChooser.setTitle("Selecciona la carpeta");
        selectedDirectory = directoryChooser.showDialog((Stage) Principal.getScene().getWindow());
        System.out.println("Direccion seleccionada: " + selectedDirectory.getAbsolutePath());
        Locacion.setText(selectedDirectory.getAbsolutePath());

        //Mostramos los archivos

        try {
            File[] files = RegresaArchivos(selectedDirectory);

            ColumnaAnterior.setCellValueFactory(new PropertyValueFactory<>("NombreViejo"));
            ColumnaNueva.setCellValueFactory(new PropertyValueFactory<>("NombreNuevo"));

            TablaArchivos.getItems().clear();

            // Get the names of the files by using the .getName() method
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                TablaArchivos.getItems().add(new Archivo(files[i].getName(), files[i].getName()));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if(TablaArchivos.getItems().size() != 0){
            Formato.setDisable(false);
            Previsualizacion.setDisable(false);
        }else{
            Formato.setDisable(true);
            Previsualizacion.setDisable(true);
            Renombrar.setDisable(true);
        }

    }

    private File[] RegresaArchivos(File selectedDirectory){
        try {
            File f = new File(selectedDirectory.getAbsolutePath());

            FilenameFilter filter = new FilenameFilter() {

                private String[] exts = {".txt", ".doc", ".pdf", ".exe", ".png", ".jpg", ".gif"};

                @Override
                public boolean accept(File f, String name) {
                    for (String ext : exts) {
                        if (name.endsWith(ext)) {
                            return true;
                        }
                    }

                    return false;
                }

            };

            // Note that this time we are using a File class as an array,
            // instead of String
            File[] files = f.listFiles(filter);

            return files;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @FXML
    private void Separador(){
        System.out.println("Texto raw: " + Formato.getText());
        String cadenaOriginal = Formato.getText();

        Vector<String> formato = new Vector<String>();

        for(int a = 0; a < cadenaOriginal.length(); a++){
            String caracter = "" + cadenaOriginal.charAt(a);
            System.out.println("caracter a validar es: " + caracter + " vuelta: " + a);
            if(caracter.equals("*")){
                System.out.println("Cadena original: " + cadenaOriginal);
                for(int b = a + 1; b < cadenaOriginal.length(); b++){
                    String caracterFinal = "" + cadenaOriginal.charAt(b);
                    if(caracterFinal.equals("*")){
                        System.out.println("Texto extraido: " + cadenaOriginal.substring(a + 1, b));
                        String validar = cadenaOriginal.substring(a + 1, b);
                        System.out.println("Cadena original: '" + validar + "'");
                        validar = validar.replaceAll("[^0-9]","");
                        System.out.println("Cadena eliminando letras: '" + validar + "'");
                        if(validar.equals("1") || validar.equals("2") || validar.equals("3")){
                            formato.add(validar);
                        }
                        String cadenaNueva = "";
                        cadenaNueva = cadenaOriginal.substring(0, a);
                        cadenaNueva += cadenaOriginal.substring(b + 1);
                        a -= 1;
                        cadenaOriginal = cadenaNueva;

                        break;

                    }
                }
                System.out.println("-------------");
            }else if(caracter.equals("{")){
                for(int b = a + 1; b < cadenaOriginal.length(); b++){
                    String caracterFinal = "" + cadenaOriginal.charAt(b);
                    if(caracterFinal.equals("}")){
                        System.out.println("Texto extraido: " + cadenaOriginal.substring(a + 1, b));
                        formato.add(cadenaOriginal.substring(a + 1, b));
                        String cadenaNueva = "";
                        cadenaNueva = cadenaOriginal.substring(0, a);
                        cadenaNueva += cadenaOriginal.substring(b + 1);
                        a -= 1;
                        cadenaOriginal = cadenaNueva;

                        break;

                    }
                }
            }
        }
        System.out.println("Formato: ");
        formato.forEach((n) -> System.out.println(n));
        System.out.println("Texto final: " + cadenaOriginal);

        Renombra(formato);
    }

    private void Renombra(Vector<String> formato){

        File[] files = RegresaArchivos(selectedDirectory);

        listaArchivos = new Vector<Archivo>();

        ColumnaAnterior.setCellValueFactory(new PropertyValueFactory<>("NombreViejo"));
        ColumnaNueva.setCellValueFactory(new PropertyValueFactory<>("NombreNuevo"));

        TablaArchivos.getItems().clear();

        // Get the names of the files by using the .getName() method
        for (int i = 0; i < files.length; i++) {
            Archivo archivo;
            TablaArchivos.getItems().add(archivo = new Archivo(files[i].getName(), ObtenNombre(formato, i, files[i].getName())));
            listaArchivos.addElement(archivo);
        }

        Renombrar.setDisable(false);

    }

    private String ObtenNombre(Vector<String> formato, int contador, String nombreOriginal){

        String nombreNuevo = "";

        for(int a = 0; a < formato.size(); a++){
            System.out.println("El elemento en " + a + " es: " + formato.elementAt(a));

            String elemento = formato.elementAt(a);

            if(elemento.equals("1")){
                //Aqui ponemos el texto viejo
                nombreNuevo += nombreOriginal.substring(0, nombreOriginal.length() - 4);
            }else if(elemento.equals("2")){
                //Contador desde 0
                nombreNuevo += contador;
            }else if(elemento.equals("3")){
                //Contador desde 1
                nombreNuevo += contador + 1;
            }else{
                //Aqui introducimos el texto
                nombreNuevo += formato.elementAt(a);
            }
        }

        System.out.println("El nombre nuevo es: " + nombreNuevo);

        return nombreNuevo + nombreOriginal.substring(nombreOriginal.length() - 4);
    }

    @FXML
    private void GuardarCambios(){
        listaArchivos.forEach((n) -> System.out.println("Nombre viejo: " + n.getNombreViejo() + " Nombre nuevo: " + n.getNombreNuevo()));
        for(int a = 0; a < listaArchivos.size(); a++){
            File file = new File(selectedDirectory.getAbsolutePath() + "\\" + listaArchivos.elementAt(a).getNombreViejo());
            File file2 = new File(selectedDirectory.getAbsolutePath() + "\\" + listaArchivos.elementAt(a).getNombreNuevo());
            try{
                boolean success = file.renameTo(file2);

                if (!success) {
                    System.out.println("Error modificando el archivo");
                }
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
