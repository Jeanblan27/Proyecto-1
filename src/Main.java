import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Reserva.ListaReservas;
import Presentation.View.Login;
import javax.swing.SwingUtilities;

import Presentation.Controller.ControladorLogin;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ListaFuncionarios modeloFuncionarios =
                    new ListaFuncionarios();

            ListaCategorias modeloCategorias =
                    new ListaCategorias();

            ListaRecursos modeloRecursos =
                    new ListaRecursos( modeloCategorias.getCategorias());

            ListaReservas modeloReservas =
                    new ListaReservas();

            Login vista = new Login();

            new ControladorLogin(
                    modeloFuncionarios,
                    modeloCategorias,
                    modeloRecursos,
                    modeloReservas,
                    vista
            );

            vista.mostrar();
        });
    }
}