package org.luisluch.system;


import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.luisluch.bean.Empresa;
import org.luisluch.bean.Servicios_has_Platos;
import org.luisluch.controller.DatosPersonalesController;
import org.luisluch.controller.EmpleadoController;
import org.luisluch.controller.EmpresaController;
import org.luisluch.controller.MenuPrincipalController;
import org.luisluch.controller.PlatoController;
import org.luisluch.controller.PresupuestoController;
import org.luisluch.controller.ProductoController;
import org.luisluch.controller.Productos_has_PlatosController;
import org.luisluch.controller.ServicioController;
import org.luisluch.controller.Servicios_has_EmpleadosController;
import org.luisluch.controller.Servicios_has_PlatosController;
import org.luisluch.controller.TipoEmpleadoController;
import org.luisluch.controller.TipoPlatoController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/luisluch/view/";
    public Stage escenarioPrincipal;
    public Scene escena;
    
    
    @Override
    public void start(Stage escenarioPrincipal ) throws Exception {
        
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony´s Kinal App");
        escenarioPrincipal.getIcons().add(new Image("/org/luisluch/image/Icono.PNG"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/luisluch/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        menuPrincipal();
        escenarioPrincipal.show();
       
    }
        public void menuPrincipal(){
            try{
                MenuPrincipalController menuPricipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",509,594);
                menuPricipal.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void ventanaProgramador(){
            
            try{
                DatosPersonalesController datosPersonales = (DatosPersonalesController)cambiarEscena("DatosPersonalesView.fxml",841,544);
                datosPersonales.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    
        public class Empresa extends Application {
        @Override
        public void start(Stage stage) throws Exception {
         Parent root = FXMLLoader.load(getClass().getResource("EmpresaView.fxml"));
         Scene scene = new Scene(root);
         String css=Empresa.class.getResource("/org.luisluch.resource/Empresa.css").toExternalForm();
         scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
            }
        }
        
        public void ventanaEmpresas(){
            try{
                EmpresaController empresaController =(EmpresaController)cambiarEscena("EmpresaView.fxml",771,626);
                empresaController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace(); 
        }
    }
        
        public void ventanaPresupuestos(){
            try{
                PresupuestoController presupuesto =(PresupuestoController)cambiarEscena("PresupuestoView.fxml",771,626);
                presupuesto.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        public void ventanaTipoEmpleados(){
            try{
                TipoEmpleadoController tipoempleado =(TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml",765,610);
                tipoempleado.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void ventanaEmpleados(){
            try{
                EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadoView.fxml",1094,687);
                empleado.setEscenarioPrincipal(this);
            }catch(Exception e ){
                e.printStackTrace();
            }
        }
        public void ventanaServicios(){
            try{
                ServicioController servicio=(ServicioController) cambiarEscena("ServiciosView.fxml",1094,715);
                servicio.setEscenarioPrincipal(this);
            }catch(Exception e){
            e.printStackTrace();
            }
        }
        public void ventanaProducto(){
            try{
                ProductoController producto=(ProductoController) cambiarEscena("ProductoView.fxml",765,612);
                producto.setEscenarioPrincipal(this);
            }catch(Exception e){
            e.printStackTrace();
        }
        }
        
         public void ventanaTipoPlato(){
            try{
                TipoPlatoController tipoPlato =(TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",765,610);
                tipoPlato.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void ventanaPlato(){
            try{
                PlatoController Plato =(PlatoController) cambiarEscena("PlatoView.fxml",1199,642);
                Plato.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void ventanaServicios_has_Platos(){
            try{
                Servicios_has_PlatosController serviciosPlato =(Servicios_has_PlatosController) cambiarEscena("Servicios_has_PlatosView.fxml",765,564);
                serviciosPlato.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void ventanaProductos_has_Platos(){
            try{
                Productos_has_PlatosController productosPlato =(Productos_has_PlatosController) cambiarEscena("Productos_has_PlatosView.fxml",765,565);
                productosPlato.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void ventanaServicios_has_Empleados(){
            try{
                Servicios_has_EmpleadosController serviciosEmpleado =(Servicios_has_EmpleadosController) cambiarEscena("Servicios_has_EmpleadosView.fxml",1199,642);
                serviciosEmpleado.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void ventanaSalir(){
           JOptionPane.showMessageDialog(null,"Gracias por haber usado este programa","Saliendo del Programa...", JOptionPane.YES_NO_OPTION);
          System.exit(0);
            
        }
        
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena (String fxml,int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
        
    }
}
