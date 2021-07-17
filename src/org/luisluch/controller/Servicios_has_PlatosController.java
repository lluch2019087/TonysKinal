package org.luisluch.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.luisluch.bean.Plato;
import org.luisluch.bean.Servicio;
import org.luisluch.bean.Servicios_has_Platos;
import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;


public class Servicios_has_PlatosController implements Initializable {
    
    private enum operaciones{ELIMINAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios_has_Platos> listaServicios_has_Platos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Servicio> listaServicio;
    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblServiciosPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoServicio.setItems(getServicios_has_Platos());
        cmbCodigoPlato.setItems(getServicios_has_Platos());
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoPlato.setItems(getPlato());
   
    }
      public void cargarDatos(){
        tblServiciosPlatos.setItems(getServicios_has_Platos());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoPlato"));
       
    }
       public ObservableList<Servicios_has_Platos>getServicios_has_Platos(){
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Servicios_has_Platos(
                                           resultado.getInt("codigoServicio"),
                                           resultado.getInt("codigoPlato")));
            }
                
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicios_has_Platos = FXCollections.observableArrayList(lista);
    }
    
  public  ObservableList <Servicio>getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Servicio ( resultado.getInt("codigoServicio"),
                                         resultado.getDate("fechaServicio"),
                                         resultado.getString("tipoServicio"),
                                         resultado.getString("horaServicio"),
                                         resultado.getString("lugarServicio"),
                                         resultado.getString("telefonoContacto"),
                                         resultado.getInt("codigoEmpresa")));
               
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableList(lista);
        
    }
    public  ObservableList <Plato>getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Plato ( resultado.getInt("codigoPlato"),
                                         resultado.getInt("cantidad"),
                                         resultado.getString("nombrePlato"),
                                         resultado.getString("descripcionPlato"),
                                         resultado.getDouble("precioPlato"),
                                         resultado.getInt("codigoTipoPlato")));
               
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableList(lista);
        
    }
     public void seleccionarElemento(){
        //txtCodigo.setText(String.valueOf(((Servicios_has_Platos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));

    }
     public Servicios_has_Platos buscarServicios_has_Platos(int codigoServicio, int codigoPlato ){
            Servicios_has_Platos  resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios_has_Plato(?,?)}");
                procedimiento.setInt(1, codigoServicio);
                procedimiento.setInt(2, codigoPlato);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Servicios_has_Platos(
                                       // registro.getInt("Servicios_codigoServicio"),
                                        registro.getInt("codigoServicio"),
                                        registro.getInt("codigoPlato"));
                                  
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return resultado;
        }
     public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1,codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato( registro.getInt("codigoPlato"),
                                          registro.getInt("cantidad"),
                                          registro.getString("nombrePlato"),
                                          registro.getString("descripcionPlato"),
                                          registro.getDouble("precioPlato"),
                                          registro.getInt("codigoTipoPlato"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public Servicio buscarServicio(int codigoServicio ){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1,codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio( registro.getInt("codigoServicio"),
                                          registro.getDate("fechaServicio"),
                                          registro.getString("tipoServicio"),
                                          registro.getString("horaServicio"),
                                          registro.getString("lugarServicio"),
                                          registro.getString("telefonoContacto"),
                                          registro.getInt("codigoEmpresa"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public void eliminar(){
        switch (tipoDeOperacion){
            case ELIMINAR:
                activarControles();
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
               
            default:
            if(tblServiciosPlatos.getSelectionModel().getSelectedItem()!=null){
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar Presupuesto",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                   try{
                       PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Plato(?,?)}");
                       procedimiento.setInt(1,((Servicios_has_Platos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio());
                       procedimiento.setInt(2,((Servicios_has_Platos)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                       procedimiento.execute();
                       listaServicios_has_Platos.remove(tblServiciosPlatos.getSelectionModel().getSelectedIndex());
                       limpiarControles();
                   }catch(Exception e){
                       e.printStackTrace();
                   } 
                }
            }else{
                JOptionPane.showMessageDialog(null, "Selecciona un elemento");
            }
             
        }
    }
    /* public void desactivarControles(){
        txtCodigo.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoPlato.setEditable(false);
        
    }
    */
    public void activarControles(){
        //txtCodigo.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoPlato.setEditable(false);
    }
    
    public void limpiarControles(){
       // txtCodigo.setText("");
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoServicio.setValue(null);
        cmbCodigoPlato.getSelectionModel().clearSelection();
        cmbCodigoPlato.setValue(null);
        
        
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
     public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }}
     
