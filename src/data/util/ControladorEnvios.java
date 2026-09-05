package data.util;

import model.core.Pedido;
import model.entities.dealer.Repartidor;
import java.util.ArrayList;

public class ControladorEnvios {

    private ArrayList<Pedido> entregasExitosas;

    public ControladorEnvios() {
        this.entregasExitosas = new ArrayList<>();
    }

    public ArrayList<Repartidor> filtrarRepartidoresElegibles(Pedido pedido, ArrayList<Repartidor> disponibles) {
        ArrayList<Repartidor> elegibles = new ArrayList<>();
        for (Repartidor candidato : disponibles) {
            if (pedido.validarRequisitos(candidato)) {
                elegibles.add(candidato);
            }
        }
        return elegibles;
    }

    public void registrarEntregaExitosa(Pedido pedido) {
        if (pedido.getRepartidorAsignado() != null && !pedido.isCancelado()) {
            this.entregasExitosas.add(pedido);
        }
    }

    public void borrarDeEntregasExitosas(Pedido pedido) {
        this.entregasExitosas.remove(pedido);
    }

    public void mostrarHistorialEntregas() {
        System.out.println("\n--- HISTORIAL DE ENTREGAS EXITOSAS ---");
        if (entregasExitosas.isEmpty()) {
            System.out.println("No hay entregas registradas.");
        } else {
            for (Pedido p : entregasExitosas) {
                System.out.println("ID: " + p.getIdPedido() + " | Entregado por: " + p.getRepartidorAsignado().getNombreCompleto());
            }
        }
    }
}