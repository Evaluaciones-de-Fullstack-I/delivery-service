package cl.duoc.delivery.mapper;

import cl.duoc.delivery.model.Delivery;
import cl.duoc.delivery.dto.CreateRequestDelivery;
import cl.duoc.delivery.dto.UpdateRequestDelivery;
import java.time.LocalDate;


public class DeliveryMapper {
//create
public static Delivery toDelivery(CreateRequestDelivery request) {
       Delivery delivery = new Delivery();
        delivery.setNombreRepartidor(request.nombreRepartidor());
        delivery.setDireccionEntrega(request.direccionEntrega());
        delivery.setFechaDespacho(request.fechaDespacho());
        delivery.setFechaEntrega(request.fechaEntrega());
        delivery.setPedidoId(request.pedidoId());
        return delivery;
}
//update

    public static void updateDelivery(
        Delivery delivery ,
        UpdateRequestDelivery request
        
    ){
       
        delivery.setNombreRepartidor(request.nombreRepartidor());
        delivery.setDireccionEntrega(request.direccionEntrega());
        delivery.setFechaDespacho(request.fechaDespacho());
        delivery.setFechaEntrega(request.fechaEntrega());
    }


}


