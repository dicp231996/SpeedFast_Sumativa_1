package model.core;

import model.entities.dealer.Repartidor;
import model.interfaces.ICancelable;
import model.interfaces.IDespachable;
import model.interfaces.IRastreable;

public abstract class Pedido implements IDespachable, ICancelable, IRastreable {

    private String idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private double distanciaKm;

    protected Repartidor repartidorAsignado;

    // Nuevas variables internas para encapsular la interfaz ICancelable
    protected boolean estadoCancelado;
    protected String motivoCancelacion;

    public Pedido() {
        this.idPedido = "GEN-0000";
        this.direccionEntrega = "Dirección no especificada";
        this.tipoPedido = "Estándar";
        this.distanciaKm = 0.0;
        this.repartidorAsignado = null;
        this.estadoCancelado = false;
        this.motivoCancelacion = "N/A";
    }

    public Pedido(String idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
        this.repartidorAsignado = null;
        this.estadoCancelado = false;
        this.motivoCancelacion = "N/A";
    }

    public String getIdPedido() { return idPedido; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public String getTipoPedido() { return tipoPedido; }
    public double getDistanciaKm() { return distanciaKm; }
    public Repartidor getRepartidorAsignado() { return repartidorAsignado; }

    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public void setTipoPedido(String tipoPedido) { this.tipoPedido = tipoPedido; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }
    public void setRepartidorAsignado(Repartidor repartidorAsignado) { this.repartidorAsignado = repartidorAsignado; }

    public abstract double calcularTiempoEntrega();
    public abstract boolean validarRequisitos(Repartidor candidato);

    // =========================================================
    // LÓGICA DE ASIGNACIÓN
    // =========================================================
    public void asignarRepartidor(Repartidor candidato) {
        System.out.println("Evaluando al repartidor " + candidato.getNombreCompleto() +
                " para el pedido " + this.idPedido + "...");

        if (validarRequisitos(candidato)) {
            this.repartidorAsignado = candidato;
            System.out.println("-> ÉXITO: Repartidor asignado correctamente.\n");
        } else {
            System.out.println("-> RECHAZADO: El repartidor no cumple con los requisitos del pedido.\n");
        }
    }

    public void asignarRepartidor(String nombre) {
        System.out.println("Forzando asignación nominal para el pedido " + this.idPedido + "...");
        Repartidor comodin = new Repartidor(nombre, "N/A", data.enumerate.TipoServicio.COMIDA, true, 999.0, true);
        this.repartidorAsignado = comodin;
        System.out.println("-> ÉXITO: Asignado directamente al repartidor: " + nombre + "\n");
    }

    // =========================================================
    // IMPLEMENTACIÓN DE INTERFACES FUNCIONALES DISTRIBUIDAS
    // =========================================================
    @Override
    public void despachar() {
        if (this.repartidorAsignado != null && !this.estadoCancelado) {
            System.out.println("-> [ESTADO] Pedido " + this.idPedido + " despachado con éxito.");
        }
    }

    @Override
    public String rastrear() {
        if (this.estadoCancelado) {
            return "CANCELADO (" + this.motivoCancelacion + ")";
        } else if (this.repartidorAsignado != null) {
            return "EN RUTA (A cargo de: " + this.repartidorAsignado.getNombreCompleto() + ")";
        } else {
            return "PENDIENTE (Esperando repartidor)";
        }
    }

    @Override
    public Repartidor cancelar(String motivo) {
        // Regla base: Ningún pedido general se puede cancelar si ya se asignó
        if (this.repartidorAsignado == null) {
            this.estadoCancelado = true;
            this.motivoCancelacion = motivo;
            System.out.println("-> Pedido " + this.idPedido + " cancelado exitosamente antes de despacho.");
            return null;
        } else {
            System.out.println("-> Error: El pedido " + this.idPedido + " ya fue despachado y no admite cancelación tardía.");
            return null;
        }
    }

    @Override
    public boolean isCancelado() {
        return this.estadoCancelado;
    }

    @Override
    public String getMotivoCancelacion() {
        return this.motivoCancelacion;
    }

    public void mostrarResumen() {
        System.out.println("--- RESUMEN DEL PEDIDO ---");
        System.out.println("ID: " + this.idPedido + " | Tipo: " + this.tipoPedido);
        System.out.println("Dirección: " + this.direccionEntrega);
        if (this.repartidorAsignado != null) {
            System.out.println("Tiempo estimado de entrega: " + calcularTiempoEntrega() + " minutos");
        }
    }
}