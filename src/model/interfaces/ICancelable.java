package model.interfaces;
import model.entities.dealer.Repartidor;

public interface ICancelable {
    Repartidor cancelar(String motivo);
    boolean isCancelado();
    String getMotivoCancelacion();
}
