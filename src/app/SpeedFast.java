package app;

import data.util.ControladorEnvios;
import data.util.GestorFases;
import data.util.GestorInstancias;
import model.core.Pedido;
import model.entities.dealer.Repartidor;

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

        GestorFases.ejecutarFaseAsignacion(listaPedidos, listaRepartidores, controlador, scanner);
        GestorFases.ejecutarFaseDespacho(listaPedidos);
        GestorFases.ejecutarFaseCancelaciones(listaPedidos, scanner);
        GestorFases.ejecutarFaseRutas(listaPedidos);
        GestorFases.ejecutarFaseReportes(listaPedidos, controlador);

        scanner.close();
    }
}