package model.valueobjects;

import model.core.Pedido;

public class HiloEntrega implements Runnable {

    private Pedido pedido;

    public HiloEntrega(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + pedido.getIdPedido() + "] Tu repartidor está en el punto de recogida.");
            // Pausa de 2 segundos (2000 milisegundos) para simular el paso del tiempo
            Thread.sleep(2000);

            System.out.println("[" + pedido.getIdPedido() + "] Tu pedido está en ruta.");
            Thread.sleep(2000);

            System.out.println("[" + pedido.getIdPedido() + "] Ya casi está en tus manos.");
            Thread.sleep(2000);

            System.out.println("[" + pedido.getIdPedido() + "] Tu pedido ha sido entregado con éxito.");

        } catch (InterruptedException e) {
            System.err.println("-> Alerta: La simulación del pedido " + pedido.getIdPedido() + " fue interrumpida.");
        }
    }
}