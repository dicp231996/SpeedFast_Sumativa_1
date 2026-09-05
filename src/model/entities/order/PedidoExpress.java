package model.entities.order;

import data.enumerate.TipoServicio;
import model.core.Pedido;
import model.entities.dealer.Repartidor;

public class PedidoExpress extends Pedido {

    public PedidoExpress() {
        super();
        this.setTipoPedido("Express");
    }

    // Constructor actualizado: Incorpora distanciaKm y la envía a la clase padre
    public PedidoExpress(String idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, "Express", distanciaKm);
    }

    // Implementación del cálculo de tiempo con lógica condicional
    @Override
    public double calcularTiempoEntrega() {
        double tiempoBase = 10.0;
        if (this.getDistanciaKm() > 5.0) {
            tiempoBase += 5.0; // Añade 5 minutos si supera los 5 km
        }
        return tiempoBase;
    }

    @Override
    public boolean validarRequisitos(Repartidor candidato) {
        if (candidato.getTipoServicio() != TipoServicio.COMPRA_EXPRESS) {
            System.out.println(" - Fallo: El repartidor no ofrece el servicio Express.");
            return false;
        }
        if (!candidato.isEstaCercaUbicacion()) {
            System.out.println(" - Fallo: El repartidor no se encuentra cerca de la dirección de entrega.");
            return false;
        }
        System.out.println(" - Estado verificado: El repartidor se encuentra cerca de la dirección de entrega.");
        return true;
    }

    @Override
    public void asignarRepartidor(Repartidor candidato) {
        System.out.println("[Protocolo Express] Iniciando evaluación estándar de compra rápida...");
        super.asignarRepartidor(candidato);
    }

    public void asignarRepartidor(String nombre) {
        System.out.println("[Protocolo Comida] Registrando asignación de emergencia por nombre: " + nombre);
        super.asignarRepartidor(nombre);
    }

    @Override
    public void mostrarResumen() {
        super.mostrarResumen();
        System.out.println("Nota de Despacho (Express): Prioridad máxima en ruta, transporte directo sin desvíos.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        if (this.repartidorAsignado != null) {
            sb.append("\n   -> Repartidor a cargo: ").append(this.repartidorAsignado.getNombreCompleto());
        } else {
            sb.append("\n   -> Repartidor a cargo: Pendiente de asignación");
        }
        return sb.toString();
    }
}