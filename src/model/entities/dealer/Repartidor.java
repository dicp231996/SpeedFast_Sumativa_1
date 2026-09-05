package model.entities.dealer;

import data.enumerate.TipoServicio;
import model.core.Persona;

// Clase Repartidor que hereda de Persona
public class Repartidor extends Persona {

    // Atributos propios del repartidor
    private TipoServicio tipoServicio;
    private boolean tieneMochilaTermica;
    private double capacidadPesoMax;
    private boolean estaCercaUbicacion;

    // Constructor por defecto
    public Repartidor() {
        super(); // Llama al constructor de Persona (asigna "No especificado")
        this.tipoServicio = TipoServicio.COMIDA;
        this.tieneMochilaTermica = false;
        this.capacidadPesoMax = 0.0;
        this.estaCercaUbicacion = false;
    }

    // Constructor con todos los atributos
    public Repartidor(String nombreCompleto, String telefonoContacto, TipoServicio tipoServicio,
                      boolean tieneMochilaTermica, double capacidadPesoMax, boolean estaCercaUbicacion) {
        super(nombreCompleto, telefonoContacto); // Inicializa los datos heredados
        this.tipoServicio = tipoServicio;
        this.tieneMochilaTermica = tieneMochilaTermica;
        this.capacidadPesoMax = capacidadPesoMax;
        this.estaCercaUbicacion = estaCercaUbicacion;
    }

    // =========================================================
    // MÉTODO AÑADIDO PARA COMPATIBILIDAD CON SPEEDFAST (MAIN)
    // =========================================================
    public String getTelefono() {
        // Llama al getter de la clase Persona.
        // Nota: Si en tu clase Persona el getter se llama diferente (ej: getTelefonoContacto()),
        // debes cambiar el nombre de este retorno para que coincida.
        return super.getTelefonoContacto();
    }

    // Getters
    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public boolean isTieneMochilaTermica() {
        return tieneMochilaTermica;
    }

    public double getCapacidadPesoMax() {
        return capacidadPesoMax;
    }

    public boolean isEstaCercaUbicacion() {
        return estaCercaUbicacion;
    }

    // Setters
    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public void setTieneMochilaTermica(boolean tieneMochilaTermica) {
        this.tieneMochilaTermica = tieneMochilaTermica;
    }

    public void setCapacidadPesoMax(double capacidadPesoMax) {
        this.capacidadPesoMax = capacidadPesoMax;
    }

    public void setEstaCercaUbicacion(boolean estaCercaUbicacion) {
        this.estaCercaUbicacion = estaCercaUbicacion;
    }

    // Método toString sobrescrito utilizando StringBuilder
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Se llama al toString() de Persona y se añaden los datos del Repartidor
        sb.append(super.toString())
                .append("\n   -> Perfil Operativo:")
                .append("\n      | Tipo de Servicio: ").append(this.tipoServicio)
                .append("\n      | Mochila Térmica: ").append(this.tieneMochilaTermica ? "Sí" : "No")
                .append("\n      | Capacidad Máx: ").append(this.capacidadPesoMax).append(" kg")
                .append("\n      | Cerca de ubicación: ").append(this.estaCercaUbicacion ? "Sí" : "No");

        return sb.toString();
    }
}