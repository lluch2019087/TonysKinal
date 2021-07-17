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
import org.luisluch.bean.Producto;
import org.luisluch.bean.Productos_has_Platos;
import org.luisluch.bean.Servicios_has_Platos;
import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;


public class Productos_has_PlatosController implements Initializable {
    
    private enum operaciones{ELIMINAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos_has_Platos> listaProductos_has_Platos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    private Principal escenarioPrincipal;
    @FXML private TextField txtCodigo;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProductosPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoProducto.setItems(getProductos_has_Platos());
        cmbCodigoPlato.setItems(getProductos_has_Platos());
        cmbCodigoProducto.setItems(getProducto());
        cmbCodigoPlato.setItems(getPlato());
    }
      public void cargarDatos(){
        tblProductosPlatos.setItems(getProductos_has_Platos());
       // colCodigo.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("Productos_codigoProducto"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoPlato"));
       
    }
       public ObservableList<Productos_has_Platos>getProductos_has_Platos(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Productos_has_Platos(//resultado.getInt("Productos_codigoProducto"),
                                           resultado.getInt("codigoProducto"),
                                           resultado.getInt("codigoPlato")));
            }
                
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos_has_Platos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Producto>getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Producto (resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidad")));                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        return listaProducto = FXCollections.observableArrayList(lista);
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
        //txtCodigo.setText(String.valueOf(((Productos_has_Platos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));

    }
     public Productos_has_Platos  buscarProductos_has_Platos(int codigoProducto, int codigoPlato){
            Productos_has_Platos  resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos_has_Plato(?,?)}");
                procedimiento.setInt(1,codigoProducto );
                procedimiento.setInt(2,codigoPlato );
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Productos_has_Platos(
                                        //registro.getInt("Productos_codigoProducto"),
                                        registro.getInt("codigoProducto"),
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
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
                procedimiento.setInt(1,codigoProducto);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Producto( 
                                                  registro.getInt("codigoProducto"),
                                                  registro.getString("nombreProducto"),
                                                  registro.getInt("cantidad"));
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
            if(tblProductosPlatos.getSelectionModel().getSelectedItem()!=null){
                int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar Presupuesto",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                   try{
                       PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos_has_Plato(?)}");
                       procedimiento.setInt(1,((Productos_has_Platos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                       procedimiento.setInt(2,((Productos_has_Platos)tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                       procedimiento.execute();
                       listaProductos_has_Platos.remove(tblProductosPlatos.getSelectionModel().getSelectedIndex());
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
        txtCodigo.setEditable(false);
        cmbCodigoProducto.setEditable(false);
        cmbCodigoPlato.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigo.setText("");
        cmbCodigoProducto.getSelectionModel().clearSelection();
        cmbCodigoProducto.setValue(null);
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
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }

}
     
    

