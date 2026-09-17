package Presentation.Controller;
import Presentation.Controller.ControladorPaneles.ControladorSistemaReservasFuncionario.ControladorSistemaReservaFuncionario;
import Presentation.Controller.ControladorPaneles.ControladorSistemaReservas.ControladorSistemaDeReservas;
import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.Model.Reserva.ListaReservas;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Funcionario.Rol;
import Presentation.View.Login;
import Presentation.View.PanelsDeReservas.SistemaDeReservas.SistemaDeReservas;
import Presentation.View.CambiarClave;
import Presentation.View.PanelsDeReservas.SistemaReservasFuncionario.SistemaReservaFuncionario;

import javax.swing.*;

public class ControladorLogin {
    private ListaFuncionarios modelo;
    private ListaReservas modeloReservas;
    private ListaCategorias modeloCategorias;
    private ListaRecursos modeloRecursos;
    private Login vista;


    public ControladorLogin(ListaFuncionarios modelo,
                            ListaCategorias modeloCategorias,
                            ListaRecursos modeloRecursos,
                            ListaReservas modeloReservas,
                            Login vista) {

        this.modelo = modelo;
        this.modeloCategorias = modeloCategorias;
        this.modeloRecursos = modeloRecursos;
        this.modeloReservas = modeloReservas;
        this.vista = vista;

        vista.getBotonIngresar().addActionListener(e -> {
            String clave = " ";
            String id = " ";
            try{
            clave = String.valueOf(vista.getClave());
            id = String.valueOf(vista.getId());
                if(modelo.existeFuncionario(clave, id)){
                    Funcionario funcionario = modelo.buscarfuncionario(id);
                    if(funcionario.getRol().equals(Rol.ADMIN)) {

                        SistemaDeReservas reserva = new SistemaDeReservas();
                        ControladorSistemaDeReservas controladoraReserva = new ControladorSistemaDeReservas(reserva, modelo, modeloCategorias, modeloRecursos, modeloReservas);
                        reserva.mostrar();
                        vista.dispose();
                    }else {
                        SistemaReservaFuncionario reserva = new SistemaReservaFuncionario();
                        ControladorSistemaReservaFuncionario controladorFuncionario = new ControladorSistemaReservaFuncionario(
                                reserva,
                                modeloReservas,
                                modeloCategorias,
                                modeloRecursos,
                                funcionario
                        );
                        reserva.mostrar();
                        vista.dispose();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Clave o ID incorrecto");
                }
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        });
        vista.getBotonCancelar().addActionListener(e -> {
            System.exit(0);
        });
        vista.getBotonCambiarClave().addActionListener(e -> {
            String clave = " ";
            String id = " ";
                clave = String.valueOf(vista.getClave());
                id = String.valueOf(vista.getId());
                if(modelo.existeFuncionario(clave, id)){
                    CambiarClave nuevaClave = new CambiarClave();
                    ControladorClave controladorClave = new ControladorClave(modelo,nuevaClave);
                    nuevaClave.mostrar();
                }else {
                    JOptionPane.showMessageDialog(null, "Clave o ID incorrecto");
                }
        });
    }
}
