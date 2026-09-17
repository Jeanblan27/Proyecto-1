package Presentation.Controller;

import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.View.CambiarClave;

import javax.swing.JOptionPane;

public class ControladorClave {

    private ListaFuncionarios modelo;
    private CambiarClave vista;
    private Funcionario funcionario;

    public ControladorClave(
            ListaFuncionarios modelo,
            CambiarClave vista,
            Funcionario funcionario) {

        this.modelo = modelo;
        this.vista = vista;
        this.funcionario = funcionario;


        // BOTÓN CAMBIAR CLAVE
        vista.getCambiarClave().addActionListener(e -> cambiarClave());


        // BOTÓN CANCELAR
        vista.getCancelar().addActionListener(e -> {
            vista.dispose();
        });
    }


    private void cambiarClave() {

        String claveActual =
                vista.getClaveActual();

        String claveNueva =
                vista.getNuevaClave();

        String confirmarClave =
                vista.getConfirmarClave();


        // 1. Verificar campos vacíos
        if (claveActual.isBlank()
                || claveNueva.isBlank()
                || confirmarClave.isBlank()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Debe completar todos los campos."
            );

            return;
        }


        // 2. Verificar que la clave actual sea correcta
        if (!claveActual.equals(funcionario.getClave())) {

            JOptionPane.showMessageDialog(
                    null,
                    "La clave actual es incorrecta."
            );

            return;
        }


        // 3. Verificar longitud de la nueva clave
        if (claveNueva.length() < 4) {

            JOptionPane.showMessageDialog(
                    null,
                    "La nueva clave debe tener al menos 4 caracteres."
            );

            return;
        }


        // 4. Verificar que las claves nuevas coincidan
        if (!claveNueva.equals(confirmarClave)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Las nuevas claves no coinciden."
            );

            return;
        }


        // 5. Verificar que sea diferente a la actual
        if (claveNueva.equals(claveActual)) {

            JOptionPane.showMessageDialog(
                    null,
                    "La nueva clave debe ser diferente a la actual."
            );

            return;
        }


        // 6. Cambiar la clave
        boolean cambiado =
                modelo.cambiarClaveFuncionario(
                        funcionario.getId(),
                        claveActual,
                        claveNueva
                );


        // 7. Mostrar resultado
        if (cambiado) {

            JOptionPane.showMessageDialog(
                    null,
                    "Clave cambiada correctamente."
            );

            vista.dispose();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo cambiar la clave."
            );
        }
    }
}
