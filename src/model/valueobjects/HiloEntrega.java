package model.valueobjects;

import model.core.Pedido;

import java.util.concurrent.ThreadLocalRandom;

public class HiloEntrega implements Runnable {

    // Rango (en milisegundos) dentro del cual se sortea la duración de cada etapa
    private static final int ESPERA_MINIMA_MS = 1000;
    private static final int ESPERA_MAXIMA_MS = 3000;

    private Pedido pedido;

    public HiloEntrega(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + pedido.getIdPedido() + "] Tu repartidor está en el punto de recogida.");
            Thread.sleep(tiempoAleatorio());

            System.out.println("[" + pedido.getIdPedido() + "] Tu pedido está en ruta.");
            Thread.sleep(tiempoAleatorio());

            System.out.println("[" + pedido.getIdPedido() + "] Ya casi está en tus manos.");
            Thread.sleep(tiempoAleatorio());

            System.out.println("[" + pedido.getIdPedido() + "] Tu pedido ha sido entregado con éxito.");
            pedido.marcarEntregado();

        } catch (InterruptedException e) {
            System.err.println("-> Alerta: La simulación del pedido " + pedido.getIdPedido() + " fue interrumpida.");
            Thread.currentThread().interrupt();
        }
    }

    // Genera una espera aleatoria distinta para cada etapa del recorrido,
    // simulando tiempos de traslado variables entre pedidos y repartidores.
    private int tiempoAleatorio() {
        return ThreadLocalRandom.current().nextInt(ESPERA_MINIMA_MS, ESPERA_MAXIMA_MS + 1);
    }
}