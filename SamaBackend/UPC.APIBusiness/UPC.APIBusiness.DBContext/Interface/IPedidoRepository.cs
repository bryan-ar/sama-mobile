using DBEntity;
using System;
using System.Collections.Generic;
using System.Text;

namespace UPC.APIBusiness.DBContext.Interface
{
    public interface IPedidoRepository
    {
        ResponseBase getProductos();
        ResponseBase getPedidosxUsuario(int IDUSUARIO);
        ResponseBase getPedido(int IDPEDIDO);
        ResponseBase getPedidoOrden(int IDPEDIDO);
        ResponseBase RegistrarPago(EntityPedidoPago pago);
        ResponseBase RegistrarOrden(List<EntityPedidoOrden> lista_orden);
        ResponseBase Registrar(EntityPedido pedido);
        ResponseBase Actualizar(EntityPedido pedido);
        ResponseBase Calificar(EntityPedido pedido);
        ResponseBase FinalizarPedido(int id);
    }
}
