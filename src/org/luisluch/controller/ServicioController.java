package org.luisluch.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.luisluch.bean.Empresa;
import org.luisluch.bean.Servicio;
import org.luisluch.db.Conexion;
import org.luisluch.report.GenerarReporte;
import org.luisluch.system.Principal;


public class ServicioController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
     @FXML private GridPane grpFechaServicio;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoEmpresa.setItems(getServicio());
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/luisluch/resource/DatePicker.css");
        grpFechaServicio.add(fecha, 0 , 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
    }

    
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));
        
        
    }
    
    public void seleccionarElemento(){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
    public ObservableList<Empresa>getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa( resultado.getInt("codigoEmpresa"),
                                       resultado.getString("nombreEmpresa"),
                                       resultado.getString("direccion"),
                                       resultado.getString("telefono")));
                        
            }
        }catch (Exception e){
                e.printStackTrace();         
        }
        
        
        return listaEmpresa =FXCollections.observableArrayList(lista);
        
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
        
          Servicio registro = new Servicio();
          //registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
          if(txtTipoServicio.getText().isEmpty() || txtHoraServicio.getText().isEmpty() || txtLugarServicio.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() 
              || cmbCodigoEmpresa.getSelectionModel().isEmpty() ){
              JOptionPane.showMessageDialog(null,"Favor de no dejar campos vacios");
          }else{
          registro.setTipoServicio(txtTipoServicio.getText());
          registro.setFechaServicio(fecha.getSelectedDate());
          registro.setHoraServicio(txtHoraServicio.getText());
          registro.setLugarServicio(txtLugarServicio.getText());
          registro.setTelefonoContacto(txtTelefonoContacto.getText());
          registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
     
          try{
              
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
              procedimiento.setDate(1,new java.sql.Date(registro.getFechaServicio().getTime()));
              procedimiento.setString(2,registro.getTipoServicio());
              procedimiento.setString(3,registro.getHoraServicio());
              procedimiento.setString(4,registro.getLugarServicio());
              procedimiento.setString(5,registro.getTelefonoContacto());
              procedimiento.setInt(6,registro.getCodigoEmpresa());
              procedimiento.execute();
              listaServicio.add(registro);
             }catch(Exception e){
              e.printStackTrace();
            
            }
      
          }
             
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
    public Empresa buscarEmpresa(int codigoEmpresa){
            Empresa  resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
                procedimiento.setInt(1, codigoEmpresa);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Empresa(
                                        registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
                                  
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
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                
            default:
                if(tblServicios.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta usted seguro de eliminar este registro?","Eliminar registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                        procedimiento.setInt(1,((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        procedimiento.execute();
                        listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                        
                    }                    
                 }else{
                    JOptionPane.showMessageDialog(null,"Tienes que seleccionar un registro");
                }
        }
    }
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem()!=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Tienes que seleccionar un registro");
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
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?,?)}");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setHoraServicio(txtHoraServicio.getText());
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            
            procedimiento.setInt(1,registro.getCodigoServicio());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3,registro.getTipoServicio());
            procedimiento.setString(4,registro.getHoraServicio());
            procedimiento.setString(5,registro.getLugarServicio());
            procedimiento.setString(6,registro.getTelefonoContacto());
            procedimiento.setInt(7,registro.getCodigoEmpresa());
            procedimiento.execute();

        }catch(Exception e ){
            e.printStackTrace();
        }
    }
     public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;
        }
    }
    public void imprimirReporte(){
        if(tblServicios.getSelectionModel().getSelectedItem()!= null){
        Map parametros = new HashMap();
        int codServicio = Integer.valueOf(txtCodigoServicio.getText());
        parametros.put("codServicio",codServicio);
        GenerarReporte.mostrarReporte("ReporteServicioFinal.jasper","Reporte de Servicios",parametros);
     }else{
        JOptionPane.showMessageDialog(null, "Si deseas un reporte, selecciona algún parámetro");
        
        }
    }
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        grpFechaServicio.setDisable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setEditable(false);
        
    }
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        grpFechaServicio.setDisable(false);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        fecha.selectedDateProperty().set(null);
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        cmbCodigoEmpresa.setValue(null);
    }
    
   
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
    
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
     public void ventanaServicios_has_Platos(){
        escenarioPrincipal.ventanaServicios_has_Platos();
    }
}
