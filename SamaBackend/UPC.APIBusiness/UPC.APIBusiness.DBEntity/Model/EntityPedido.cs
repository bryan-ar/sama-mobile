using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityPedido
    {
        public int id { get; set; }
        public int id_usuario { get; set; }
        public string fecha_creacion { get; set; }
        public string descripcion { get; set; }
        public decimal monto_total { get; set; }
        public int estado { get; set; }
        public decimal calificacion { get; set; }
        public string hora_llegada { get; set; }
        public List<EntityPedidoOrden> orden { get; set; }
        public EntityPedidoPago pago { get; set; }
    }
}
