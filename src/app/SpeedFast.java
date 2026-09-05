package app;

import data.util.ControladorEnvios;
import data.util.GestorInstancias;
import model.core.Pedido;
import model.entities.dealer.Repartidor;
import model.entities.order.PedidoComida;

import java.util.ArrayList;
import java.util.Scanner;

public class SpeedFast {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String rutaPedidos = "resources/pedidos.txt";
        String rutaRepartidores = "resources/repartidores.txt";

        System.out.println("=========================================");
        System.out.println("       INICIANDO SISTEMA SPEEDFAST       ");
        System.out.println("=========================================\n");

        ArrayList<Pedido> listaPedidos = GestorInstancias.cargarPedidos(rutaPedidos);
        ArrayList<Repartidor> listaRepartidores = GestorInstancias.cargarRepartidores(rutaRepartidores);
        ControladorEnvios controlador = new ControladorEnvios();

        // =========================================================
        // FASE 1: RESERVA Y ASIGNACIÓN (No hay despacho ni éxito aún)
        // =========================================================
        System.out.println("--- FASE 1: RESERVA DE PEDIDOS ---");
        System.out.println("1. Automática (El sistema evalúa y asigna bajo sus reglas)");
        System.out.println("2. Manual (El usuario elige entre los candidatos aptos)");
        System.out.println("3. Nominal (Asignación por nombre de repartidor)");
        System.out.print("Seleccione el método de asignación: ");

        String tipoAsignacion = scanner.nextLine();
        boolean esManual = tipoAsignacion.equals("2");
        boolean esNominal = tipoAsignacion.equals("3");

        for (Pedido pedido : listaPedidos) {
            System.out.println("\n>> Reservando Pedido ID: " + pedido.getIdPedido());

            if (esNominal) {
                System.out.print("Ingrese el nombre exacto del repartidor para este pedido: ");
                pedido.asignarRepartidor(scanner.nextLine());
                // Ya no despachamos ni registramos éxito aquí
                continue;
            }

            if (esManual) {
                ArrayList<Repartidor> candidatosAptos = controlador.filtrarRepartidoresElegibles(pedido, listaRepartidores);
                if (candidatosAptos.isEmpty()) {
                    System.out.println("-> Alerta: No hay repartidores elegibles. Pedido pendiente.");
                    continue;
                }

                System.out.println("Candidatos disponibles:");
                for (int i = 0; i < candidatosAptos.size(); i++) {
                    System.out.println("  [" + i + "] " + candidatosAptos.get(i).getNombreCompleto());
                }
                System.out.print("Ingrese el número del repartidor a reservar: ");

                try {
                    int index = Integer.parseInt(scanner.nextLine());
                    if (index >= 0 && index < candidatosAptos.size()) {
                        Repartidor seleccionado = candidatosAptos.get(index);
                        pedido.asignarRepartidor(seleccionado);
                        listaRepartidores.remove(seleccionado);
                    }
                } catch (Exception e) {
                    System.out.println("-> Entrada inválida. Omitiendo reserva.");
                }
            } else {
                for (int i = 0; i < listaRepartidores.size(); i++) {
                    Repartidor candidato = listaRepartidores.get(i);
                    pedido.asignarRepartidor(candidato);

                    if (pedido.getRepartidorAsignado() != null) {
                        listaRepartidores.remove(i);
                        break;
                    }
                }
            }
        }

        // =========================================================
        // FASE 2: VERIFICACIÓN Y DESPACHO (Invocación exigida en rúbrica)
        // =========================================================
        System.out.println("\n=========================================");
        System.out.println("--- FASE 2: VERIFICACIÓN Y DESPACHO ---");
        System.out.println("=========================================");

        for (Pedido pedido : listaPedidos) {
            // Solo despachamos los que lograron conseguir una reserva en la Fase 1
            if (pedido.getRepartidorAsignado() != null && !pedido.isCancelado()) {
                pedido.mostrarResumen(); // Llama internamente a calcularTiempoEntrega()
                pedido.despachar();      // Cambia el estado/imprime log de despacho
                System.out.println("--------------------------------------------------");
            }
        }

        // =========================================================
        // FASE 3: CONTINGENCIAS (Cancelación en ruta)
        // =========================================================
        System.out.println("\n=========================================");
        System.out.println("--- FASE 3: GESTIÓN DE CANCELACIONES ---");
        System.out.println("=========================================");

        boolean enFaseCancelacion = true;

        while (enFaseCancelacion) {
            int activos = 0;
            System.out.println("\nPedidos despachados (Solo Comida admite cancelación tardía):");

            for (Pedido p : listaPedidos) {
                if (p.getRepartidorAsignado() != null && !p.isCancelado() && p instanceof PedidoComida) {
                    System.out.println("- " + p.getIdPedido() + " | Repartidor: " + p.getRepartidorAsignado().getNombreCompleto());
                    activos++;
                }
            }

            if (activos == 0) {
                System.out.println("-> No quedan pedidos despachados susceptibles a cancelación.");
                break;
            }

            System.out.print("\nIngrese ID a cancelar (o escriba 'FIN' para confirmar las entregas): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("FIN")) break;

            Pedido pedidoACancelar = null;
            for (Pedido p : listaPedidos) {
                if (p.getIdPedido().equalsIgnoreCase(input)) {
                    pedidoACancelar = p;
                    break;
                }
            }

            if (pedidoACancelar != null) {
                System.out.print("Ingrese el motivo: ");
                String motivo = scanner.nextLine();

                Repartidor liberado = pedidoACancelar.cancelar(motivo);

                if (liberado != null && !liberado.getTelefono().equals("N/A")) {
                    listaRepartidores.add(liberado);
                    System.out.println("-> Se reintegró al repartidor " + liberado.getNombreCompleto());
                }
            } else {
                System.out.println("-> Error: ID no encontrado.");
            }
        }

        // =========================================================
        // FASE 4: CONFIRMACIÓN DE ENTREGAS EXITOSAS
        // =========================================================
        // Solo los pedidos que sobrevivieron a la Fase 3 se registran como exitosos.
        for (Pedido p : listaPedidos) {
            if (p.getRepartidorAsignado() != null && !p.isCancelado()) {
                controlador.registrarEntregaExitosa(p);
            }
        }

        // =========================================================
        // FASE 5: RASTREO Y REPORTES
        // =========================================================
        System.out.println("\n=========================================");
        System.out.println("--- FASE 5: ESTADO GLOBAL Y RASTREO ---");
        System.out.println("=========================================");

        for (Pedido p : listaPedidos) {
            System.out.println("Seguimiento ID " + p.getIdPedido() + " -> " + p.rastrear());
        }

        controlador.mostrarHistorialEntregas();
        scanner.close();
    }
}