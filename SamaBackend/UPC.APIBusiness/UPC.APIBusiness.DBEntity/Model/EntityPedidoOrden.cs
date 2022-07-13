using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityPedidoOrden
    {
        public int id_pedido { get; set; }
        public int id_producto { get; set; }
        public string nombre_producto { get; set; }
        public int cantidad { get; set; }
        public decimal precio { get; set; }
        public decimal monto_pagar { get; set; }
    }
}
