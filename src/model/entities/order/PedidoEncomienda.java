package model.entities.order;

import data.enumerate.TipoServicio;
import model.core.Pedido;
import model.entities.dealer.Repartidor;

public class PedidoEncomienda extends Pedido {

    private double pesoPaquete;

    public PedidoEncomienda() {
        super();
        this.setTipoPedido("Encomienda");
        this.pesoPaquete = 0.0;
    }

    // Constructor actualizado: Incorpora distanciaKm y la envía a la clase padre
    public PedidoEncomienda(String idPedido, String direccionEntrega, double distanciaKm, double pesoPaquete) {
        super(idPedido, direccionEntrega, "Encomienda", distanciaKm);
        this.pesoPaquete = pesoPaquete;
    }

    public double getPesoPaquete() {
        return pesoPaquete;
    }

    public void setPesoPaquete(double pesoPaquete) {
        this.pesoPaquete = pesoPaquete;
    }

    // Implementación del cálculo de tiempo
    @Override
    public double calcularTiempoEntrega() {
        // 20 minutos base + 1.5 minutos por km, redondeado hacia arriba
        return Math.ceil(20.0 + (1.5 * this.getDistanciaKm()));
    }

    @Override
    public boolean validarRequisitos(Repartidor candidato) {
        if (candidato.getTipoServicio() != TipoServicio.ENCOMIENDA) {
            System.out.println(" - Fallo: El repartidor no ofrece el servicio de Encomienda.");
            return false;
        }
        if (candidato.getCapacidadPesoMax() < this.pesoPaquete) {
            System.out.println(" - Fallo: El peso del paquete (" + this.pesoPaquete + "kg) excede la capacidad máxima del repartidor (" + candidato.getCapacidadPesoMax() + "kg).");
            return false;
        }
        return true;
    }

    @Override
    public void asignarRepartidor(Repartidor candidato) {
        System.out.println("[Protocolo Encomienda] Iniciando evaluación estándar de transporte de carga...");
        super.asignarRepartidor(candidato);
    }

    public void asignarRepartidor(String nombre) {
        System.out.println("[Protocolo Comida] Registrando asignación de emergencia por nombre: " + nombre);
        super.asignarRepartidor(nombre);
    }

    @Override
    public void mostrarResumen() {
        super.mostrarResumen();
        System.out.println("Nota de Despacho (Encomienda): Verificar el peso en báscula (" + this.pesoPaquete + " kg) antes del traslado.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
                .append("\n   -> Peso del paquete: ").append(this.pesoPaquete).append(" kg");

        if (this.repartidorAsignado != null) {
            sb.append("\n   -> Repartidor a cargo: ").append(this.repartidorAsignado.getNombreCompleto());
        } else {
            sb.append("\n   -> Repartidor a cargo: Pendiente de asignación");
        }
        return sb.toString();
    }
}