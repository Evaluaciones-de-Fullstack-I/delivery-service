package cl.duoc.delivery.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRequestDelivery (//se actualiza un despacho existente




    @NotBlank(message = "El nombre del repartidor es obligatorio")
    @Size(max = 50, message = "El nombre del repartidor no puede exceder los 50 caracteres")
    String nombreRepartidor,

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 150, message = "La dirección de entrega no puede exceder los 150 caracteres")
    String direccionEntrega,

    @NotNull(message = "La fecha de despacho es obligatoria")
    LocalDate fechaDespacho,

    @NotNull(message = "La fecha de entrega es obligatoria")
    LocalDate fechaEntrega,

    @NotNull(message = "El pedido es obligatorio")
    Integer pedidoId
){

}
