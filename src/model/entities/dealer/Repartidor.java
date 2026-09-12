package model.entities.dealer;

import data.enumerate.TipoServicio;
import model.core.Pedido;
import model.core.Persona;
import model.interfaces.IRunnable;

import java.util.ArrayList;

// Clase Repartidor hereda de Persona e implementa tanto tu interfaz como la nativa de Java
public class Repartidor extends Persona implements IRunnable, Runnable {

    private TipoServicio tipoServicio;
    private boolean tieneMochilaTermica;
    private double capacidadPesoMax;
    private boolean estaCercaUbicacion;
    private ArrayList<Pedido> pedidosAsignados;

    public Repartidor() {
        super();
        this.tipoServicio = TipoServicio.COMIDA;
        this.tieneMochilaTermica = false;
        this.capacidadPesoMax = 0.0;
        this.estaCercaUbicacion = false;
        this.pedidosAsignados = new ArrayList<>();
    }

    public Repartidor(String nombreCompleto, String telefonoContacto, TipoServicio tipoServicio,
                      boolean tieneMochilaTermica, double capacidadPesoMax, boolean estaCercaUbicacion) {
        super(nombreCompleto, telefonoContacto);
        this.tipoServicio = tipoServicio;
        this.tieneMochilaTermica = tieneMochilaTermica;
        this.capacidadPesoMax = capacidadPesoMax;
        this.estaCercaUbicacion = estaCercaUbicacion;
        this.pedidosAsignados = new ArrayList<>();
    }

    // =========================================================
    // IMPLEMENTACIÓN SECUENCIAL POR REPARTIDOR (UN PEDIDO A LA VEZ),
    // PERO CADA REPARTIDOR CORRE EN SU PROPIO HILO (ver GestorFases,
    // que administra el pool de repartidores vía ExecutorService).
    // =========================================================
    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();
        System.out.println("\n>>> [EN RUTA - " + nombreHilo + "] El repartidor " + this.getNombreCompleto()
                + " inicia su recorrido secuencial.");

        if (this.pedidosAsignados.isEmpty()) {
            System.out.println("    -> No hay pedidos asignados en la mochila.");
            return;
        }

        for (Pedido pedido : this.pedidosAsignados) {
            if (!pedido.isCancelado()) {
                // Cada pedido se procesa en un HILO REAL (HiloEntrega), pero se espera
                // su finalización con join() antes de iniciar el siguiente pedido de la
                // MISMA mochila. Esto preserva la secuencia interna del repartidor sin
                // dejar de cumplir con el requisito de que HiloEntrega corra en su propio hilo.
                Thread hiloEntrega = new Thread(new model.valueobjects.HiloEntrega(pedido),
                        "Entrega-" + pedido.getIdPedido());
                hiloEntrega.start();
                try {
                    hiloEntrega.join();
                } catch (InterruptedException e) {
                    System.err.println("-> Alerta: El recorrido de " + this.getNombreCompleto() + " fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("    -> Omitiendo pedido ID: " + pedido.getIdPedido() + " (Se encuentra CANCELADO).");
            }
        }

        System.out.println("\n>>> [FIN DE RUTA] " + this.getNombreCompleto() + " ha finalizado su recorrido y liberado su carga.\n");
        this.limpiarPedidos();
    }

    // =========================================================
    // GESTIÓN DE LA LISTA DE PEDIDOS
    // =========================================================
    public ArrayList<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void agregarPedido(Pedido pedido) {
        this.pedidosAsignados.add(pedido);
    }

    public void removerPedido(Pedido pedido) {
        this.pedidosAsignados.remove(pedido);
    }

    public void limpiarPedidos() {
        this.pedidosAsignados.clear();
    }

    // Puente para evitar errores de visibilidad en el Main
    public String getTelefono() {
        return super.getTelefonoContacto();
    }

    // Getters
    public TipoServicio getTipoServicio() { return tipoServicio; }
    public boolean isTieneMochilaTermica() { return tieneMochilaTermica; }
    public double getCapacidadPesoMax() { return capacidadPesoMax; }
    public boolean isEstaCercaUbicacion() { return estaCercaUbicacion; }

    // Setters
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }
    public void setTieneMochilaTermica(boolean tieneMochilaTermica) { this.tieneMochilaTermica = tieneMochilaTermica; }
    public void setCapacidadPesoMax(double capacidadPesoMax) { this.capacidadPesoMax = capacidadPesoMax; }
    public void setEstaCercaUbicacion(boolean estaCercaUbicacion) { this.estaCercaUbicacion = estaCercaUbicacion; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
                .append("\n   -> Perfil Operativo:")
                .append("\n      | Tipo de Servicio: ").append(this.tipoServicio)
                .append("\n      | Mochila Térmica: ").append(this.tieneMochilaTermica ? "Sí" : "No")
                .append("\n      | Capacidad Máx: ").append(this.capacidadPesoMax).append(" kg")
                .append("\n      | Cerca de ubicación: ").append(this.estaCercaUbicacion ? "Sí" : "No")
                .append("\n      | Carga actual: ").append(this.pedidosAsignados.size()).append(" pedidos asignados");
        return sb.toString();
    }
}