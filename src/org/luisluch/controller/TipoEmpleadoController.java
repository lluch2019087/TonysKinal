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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.luisluch.bean.TipoEmpleado;
import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;


public class TipoEmpleadoController implements Initializable{
    
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,CANCELAR,EDITAR,ACTUALIZAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private Principal escenarioPrincipal;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleados;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @Override
  
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
       
    }
    
    public void cargarDatos(){
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,String>("descripcion"));
    }
    public ObservableList<TipoEmpleado>getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
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
                 tipoDeOperacion= operaciones.GUARDAR;
                break;
             case GUARDAR:
                 guardarDatos();
                 desactivarControles();
                 limpiarControles();
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEliminar.setDisable(false);
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);
                 tipoDeOperacion = operaciones.NINGUNO;
                 cargarDatos();
                 break;
   
         }
     }
     
     public void guardarDatos(){
         TipoEmpleado registro = new TipoEmpleado();
         registro.setDescripcion(txtDescripcion.getText());
         if(txtDescripcion.getText().isEmpty()){
           JOptionPane.showMessageDialog(null,"Favor de llenar todos los datos");
      }else{
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
             procedimiento.setString(1,registro.getDescripcion());
             procedimiento.execute();
             listaTipoEmpleado.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
         }
     }
     public void seleccionarElemento(){
         txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
         txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
     }
     public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
         TipoEmpleado resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
                procedimiento.setInt(1,codigoTipoEmpleado);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new TipoEmpleado( 
                                                  registro.getInt("codigoTipoEmpleado"),
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
                 cargarDatos();
                 limpiarControles();
                 break;
              
            default:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas Seguro de eliminar el registro?","Eliminar registro", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1,((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
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
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem()!=null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2,registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
     }
     public void desactivarControles(){
         txtCodigoTipoEmpleado.setEditable(false);
         txtDescripcion.setEditable(false);
     }
     public void activarControles(){
         txtCodigoTipoEmpleado.setEditable(false);
         txtDescripcion.setEditable(true);
     }
    
     public void limpiarControles(){
         txtCodigoTipoEmpleado.setText("");
         txtDescripcion.setText("");
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
    
     public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
}

