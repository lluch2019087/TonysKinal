package org.luisluch.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.luisluch.system.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    
    }
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
    public void ventanaPresupuestos(){
        escenarioPrincipal.ventanaPresupuestos();
    }
    public void ventanaTipoEmpleados(){
        escenarioPrincipal.ventanaTipoEmpleados();
    }
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    public void ventanaServicios_has_Platos(){
        escenarioPrincipal.ventanaServicios_has_Platos();
    }
      
    public void ventanaProductos_has_Platos(){
        escenarioPrincipal.ventanaProductos_has_Platos();
    }
     public void ventanaServicios_has_Empleados(){
        escenarioPrincipal.ventanaServicios_has_Empleados();
    }
    public void ventanaSalir(){
        escenarioPrincipal.ventanaSalir();
    }
}
