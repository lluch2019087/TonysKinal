
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
import org.luisluch.bean.TipoPlato;
import org.luisluch.db.Conexion;
import org.luisluch.system.Principal;

public class TipoPlatoController implements Initializable{
    
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,CANCELAR,EDITAR,ACTUALIZAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    private Principal escenarioPrincipal;
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoPlatos;
    @FXML private TableColumn colCodigoTipoPlato;
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
        tblTipoPlatos.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato,String>("descripcion"));
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
         TipoPlato registro = new TipoPlato();
           if(txtDescripcion.getText().isEmpty()){
           JOptionPane.showMessageDialog(null,"Favor de llenar todos los datos");
      }else{
         registro.setDescripcion(txtDescripcion.getText());
        
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
             procedimiento.setString(1,registro.getDescripcion());
             procedimiento.execute();
             listaTipoPlato.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
           } 
     }
     public void seleccionarElemento(){
         txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
         txtDescripcion.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcion());
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
                 cargarDatos();
                 limpiarControles();
                 break;
              
            default:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas Seguro de eliminar el registro?","Eliminar registro", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            procedimiento.setInt(1,((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
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
                if(tblTipoPlatos.getSelectionModel().getSelectedItem()!=null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
            TipoPlato registro = (TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2,registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
     }
     public void desactivarControles(){
         txtCodigoTipoPlato.setEditable(false);
         txtDescripcion.setEditable(false);
     }
     public void activarControles(){
         txtCodigoTipoPlato.setEditable(false);
         txtDescripcion.setEditable(true);
     }
    
     public void limpiarControles(){
         txtCodigoTipoPlato.setText("");
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
     public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
}
    
 
