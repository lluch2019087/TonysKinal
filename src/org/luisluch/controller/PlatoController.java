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
import org.luisluch.bean.TipoPlato;

import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;


public class PlatoController implements Initializable{

     private enum operaciones{NUEVO,ELIMINAR,CANCELAR,GUARDAR,EDITAR,ACTUALIZAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    private ObservableList<Plato> listaPlato;
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
    }

    
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato,String>("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato,String>("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato,Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoTipoPlato"));
        
        
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
        public ObservableList<TipoPlato>getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                           resultado.getString("descripcion")));                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
            
                guardardatos();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                cargarDatos();
                break;
        }
    }
   
    public void guardardatos(){
        
          Plato registro = new Plato();
        
          if( txtCantidad.getText().isEmpty() || txtNombrePlato.getText().isEmpty() || txtDescripcion.getText().isEmpty() || txtPrecio.getText().isEmpty()
              || cmbCodigoTipoPlato.getSelectionModel().isEmpty() ){
              JOptionPane.showMessageDialog(null,"Favor de no dejar campos vacios");
          }else{
          registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
          registro.setNombrePlato(txtNombrePlato.getText());
          registro.setDescripcionPlato(txtDescripcion.getText());
          registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
          registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
     
          try{
              
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
              procedimiento.setInt(1,registro.getCantidad());
              procedimiento.setString(2,registro.getNombrePlato());
              procedimiento.setString(3,registro.getDescripcionPlato());
              procedimiento.setDouble(4,registro.getPrecioPlato());
              procedimiento.setInt(5,registro.getCodigoTipoPlato());
              procedimiento.execute();
              listaPlato.add(registro);
             }catch(Exception e){
              e.printStackTrace();
            
            }
      
          }      
    }
    public void seleccionarElemento(){
        txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcion.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        txtPrecio.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
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
    
       public TipoPlato buscarTipoPlato(int codigoTipoPlato){
         TipoPlato resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
                procedimiento.setInt(1,codigoTipoPlato);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new TipoPlato( 
                                                  registro.getInt("codigoTipoPlato"),
                                                  registro.getString("descripcion"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
         return resultado;
     }
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                cargarDatos();
                break;
                
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas Seguro de eliminar este registro?","Eliminar registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                        procedimiento.setInt(1,((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                        procedimiento.execute();
                        listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                        
                    }                    
                 }else{
                    JOptionPane.showMessageDialog(null,"Selecciona un elemento");
                }
        }
    }
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblPlatos.getSelectionModel().getSelectedItem()!=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Selecciona un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
            
        }     
       
    }
    public void actualizar(){
        
        try{
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?,?)}");
            Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcion.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
            registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            
            procedimiento.setInt(1,registro.getCodigoPlato());
            procedimiento.setInt(2,registro.getCantidad());
              procedimiento.setString(3,registro.getNombrePlato());
              procedimiento.setString(4,registro.getDescripcionPlato());
              procedimiento.setDouble(5,registro.getPrecioPlato());
              procedimiento.setInt(6,registro.getCodigoTipoPlato());
            procedimiento.execute();

        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    
    
    
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecio.setEditable(false);
        cmbCodigoTipoPlato.setEditable(false);
        
    }
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecio.setEditable(true);
        cmbCodigoTipoPlato.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
        cmbCodigoTipoPlato.setValue(null);
    }
       public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
     public void ventanaServicios_has_Platos(){
        escenarioPrincipal.ventanaServicios_has_Platos();
    }
}

    

