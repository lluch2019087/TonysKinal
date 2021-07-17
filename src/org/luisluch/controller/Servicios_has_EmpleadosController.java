package org.luisluch.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.luisluch.bean.Empleado;
import org.luisluch.bean.Servicio;
import org.luisluch.bean.Servicios_has_Empleados;
import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;


public class Servicios_has_EmpleadosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios_has_Empleados> listaServicios_has_Empleados;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private GridPane grpFechaEvento;
    @FXML private TableView tblServiciosE;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoServicio.setItems(getServicios_has_Empleados());
        cmbCodigoEmpleado.setItems(getServicios_has_Empleados());
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/luisluch/resource/DatePicker.css");
        grpFechaEvento.add(fecha, 0 , 0);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        
    }

    
    public void cargarDatos(){
        tblServiciosE.setItems(getServicios_has_Empleados());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("Servicios_codigoServicio"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("lugarEvento"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("codigoEmpleado"));
        
        
    }
    
    public void seleccionarElemento(){
        txtCodigo.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        fecha.selectedDateProperty().set(((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtHoraEvento.setText(((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getHoraEvento());
        txtLugarEvento.setText(((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getLugarEvento());
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));

        

    }
    
    public  ObservableList <Servicios_has_Empleados>getServicios_has_Empleados(){
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Servicios_has_Empleados(  resultado.getInt("Servicios_codigoServicio"),
                                                        resultado.getDate("fechaEvento"),
                                                        resultado.getString("horaEvento"),
                                                        resultado.getString("lugarEvento"),
                                                        resultado.getInt("codigoServicio"),
                                                        resultado.getInt("codigoEmpleado")));
               
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return listaServicios_has_Empleados = FXCollections.observableList(lista);
        
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
        
        Servicios_has_Empleados registro = new Servicios_has_Empleados();
        if(txtHoraEvento.getText().isEmpty() || txtLugarEvento.getText().isEmpty() || cmbCodigoServicio.getSelectionModel().isEmpty()  
            || cmbCodigoEmpleado.getSelectionModel().isEmpty() ){
            JOptionPane.showMessageDialog(null,"Favor de no dejar campos vacios");
        }else{
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setLugarEvento(txtLugarEvento.getText());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
     
          try{
              
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleado(?,?,?,?,?)}");
            procedimiento.setDate(1,new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(2,registro.getHoraEvento());
            procedimiento.setString(3,registro.getLugarEvento());
            procedimiento.setInt(4,registro.getCodigoServicio());
            procedimiento.setInt(5,registro.getCodigoEmpleado());
            procedimiento.execute();
            listaServicios_has_Empleados.add(registro);
             }catch(Exception e){
              e.printStackTrace();
            
            }
      
          }
             
    }
    public Servicios_has_Empleados buscarServicios_has_Empleado(int Servicios_codigoServicio ){
        Servicios_has_Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios_has_Empleado(?)}");
            procedimiento.setInt(1,Servicios_codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicios_has_Empleados( registro.getInt("Servicios_codigoServicio"),
                                          registro.getDate("fechaEvento"),
                                          registro.getString("horaEvento"),
                                          registro.getString("lugarEvento"),
                                          registro.getInt("codigoServicio"),
                                          registro.getInt("codigoEmpleado"));
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
                if(tblServiciosE.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta usted seguro de eliminar este registro?","Eliminar registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleado(?)}");
                        procedimiento.setInt(1,((Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                        procedimiento.execute();
                        listaServicios_has_Empleados.remove(tblServiciosE.getSelectionModel().getSelectedIndex());
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
                if(tblServiciosE.getSelectionModel().getSelectedItem()!=null){
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
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleado(?,?,?,?,?,?)}");
            Servicios_has_Empleados registro = (Servicios_has_Empleados)tblServiciosE.getSelectionModel().getSelectedItem();
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setLugarEvento(txtLugarEvento.getText());
            registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());          
            registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            
            procedimiento.setInt(1,registro.getServicios_codigoServicio());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(3,registro.getHoraEvento());
            procedimiento.setString(4,registro.getLugarEvento());
            procedimiento.setInt(5,registro.getCodigoServicio());
            procedimiento.setInt(6,registro.getCodigoEmpleado());
            procedimiento.execute();

        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtHoraEvento.setEditable(false);
        grpFechaEvento.setDisable(false);
        txtLugarEvento.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoEmpleado.setEditable(false);
        
    }
    public void activarControles(){
        txtCodigo.setEditable(false);
        txtHoraEvento.setEditable(true);
        grpFechaEvento.setDisable(false);
        txtLugarEvento.setEditable(true);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoEmpleado.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigo.setText("");
        txtHoraEvento.setText("");
        fecha.selectedDateProperty().set(null);
        txtLugarEvento.setText("");
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoServicio.setValue(null);
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.setValue(null);
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
}
