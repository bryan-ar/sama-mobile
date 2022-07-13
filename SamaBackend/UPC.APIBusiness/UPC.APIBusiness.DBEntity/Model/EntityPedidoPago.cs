using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityPedidoPago
    {
        public int id { get; set; }
        public int id_pedido { get; set; }
        public string tarjeta { get; set; }
        public string nombre { get; set; }
        public string fecha_exp { get; set; }
        public string cvv { get; set; }
        public int estado { get; set; }
    }
}
