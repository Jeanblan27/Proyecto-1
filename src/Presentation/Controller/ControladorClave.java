package Presentation.Controller;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.View.CambiarClave;

import javax.swing.*;

public class ControladorClave {
    private ListaFuncionarios modelo;
    private CambiarClave vista;

    public ControladorClave(ListaFuncionarios modelo, CambiarClave vista) {
        this.modelo = modelo;
        this.vista = vista;

        vista.getCambiarClave().addActionListener(e ->{
           String nuevaClave =  String.valueOf(vista.getNuevaClave());
           String claveActual = String.valueOf(vista.getClaveActual());

           if(modelo.cambiarClaveFuncionario(claveActual,nuevaClave)){
               JOptionPane.showMessageDialog(null, "La clave ahora es: " + nuevaClave);
               vista.dispose();
           }else{
               JOptionPane.showMessageDialog(null, "La clave actual es incorrecta: " + claveActual);
           }

        });
        vista.getCancelar().addActionListener(e ->{
            vista.dispose();
        });
    }

}
