package org.luisluch.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.luisluch.bean.Empleado;
import org.luisluch.bean.TipoEmpleado;
import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;


public class EmpleadoController implements Initializable{

    

    
    private enum operaciones{NUEVO,ELIMINAR,CANCELAR,GUARDAR,EDITAR,ACTUALIZAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ObservableList<Empleado> listaEmpleado;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    }

    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,String>("nombresEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado,String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado,String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado,String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoTipoEmpleado"));
        
        
    }
    
    public  ObservableList <Empleado>getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empleado ( resultado.getInt("codigoEmpleado"),
                                         resultado.getInt("numeroEmpleado"),
                                         resultado.getString("apellidosEmpleado"),
                                         resultado.getString("nombresEmpleado"),
                                         resultado.getString("direccionEmpleado"),
                                         resultado.getString("telefonoContacto"),
                                         resultado.getString("gradoCocinero"),
                                         resultado.getInt("codigoTipoEmpleado")));
               
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableList(lista);
        
    }
    public ObservableList<TipoEmpleado>getTipoEmpleado(){
        ArrayList<TipoEmpleado>lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                           resultado.getString("descripcion")));
               
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
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
        
          Empleado registro = new Empleado();
        
          if(txtNumeroEmpleado.getText().isEmpty() || txtApellidosEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty()
              || cmbCodigoTipoEmpleado.getSelectionModel().isEmpty() ){
              JOptionPane.showMessageDialog(null,"Favor de no dejar campos vacios");
          }else{
          registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
          registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
          registro.setNombresEmpleado(txtNombresEmpleado.getText());
          registro.setDireccionEmpleado(txtDireccion.getText());
          registro.setTelefonoContacto(txtTelefono.getText());
          registro.setGradoCocinero(txtGradoCocinero.getText());
          registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
     
          try{
              
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
              procedimiento.setInt(1,registro.getNumeroEmpleado());
              procedimiento.setString(2,registro.getApellidosEmpleado());
              procedimiento.setString(3,registro.getNombresEmpleado());
              procedimiento.setString(4,registro.getDireccionEmpleado());
              procedimiento.setString(5,registro.getTelefonoContacto());
              procedimiento.setString(6,registro.getGradoCocinero());
              procedimiento.setInt(7,registro.getCodigoTipoEmpleado());
              procedimiento.execute();
              listaEmpleado.add(registro);
             }catch(Exception e){
              e.printStackTrace();
            
            }
      
          }
             
    }
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidosEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtNombresEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtDireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtTelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
    }
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1,codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleado( registro.getInt("codigoEmpleado"),
                                          registro.getInt("numeroEmpleado"),
                                          registro.getString("apellidosEmpleado"),
                                          registro.getString("nombresEmpleado"),
                                          registro.getString("telefonoContacto"),
                                          registro.getString("direccionEmpleado"),
                                          registro.getString("gradoCocinero"),
                                          registro.getInt("codigoTipoEmpleado"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado= null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado ( registro.getInt("codigoTipoEmpleado"),
                                               registro.getString("descripcion"));
            }
        }catch(Exception e ){
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
                if(tblEmpleados.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas Seguro de eliminar este registro?","Eliminar registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                        procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                        procedimiento.execute();
                        listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
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
                if(tblEmpleados.getSelectionModel().getSelectedItem()!=null){
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
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?,?,?,?,?,?,?,?)}");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            
            procedimiento.setInt(1,registro.getCodigoEmpleado());
            procedimiento.setInt(2,registro.getNumeroEmpleado());
            procedimiento.setString(3,registro.getApellidosEmpleado());
            procedimiento.setString(4,registro.getNombresEmpleado());
            procedimiento.setString(5,registro.getDireccionEmpleado());
            procedimiento.setString(6,registro.getTelefonoContacto());
            procedimiento.setString(7,registro.getGradoCocinero());
            procedimiento.setInt(8,registro.getCodigoTipoEmpleado());
            procedimiento.execute();

        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    
    
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setEditable(false);
        
    }
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidosEmpleado.setText("");
        txtNombresEmpleado.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtGradoCocinero.setText("");
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoTipoEmpleado.setValue(null);
    }
       public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaTipoEmpleados(){
        escenarioPrincipal.ventanaTipoEmpleados();
    }
}
