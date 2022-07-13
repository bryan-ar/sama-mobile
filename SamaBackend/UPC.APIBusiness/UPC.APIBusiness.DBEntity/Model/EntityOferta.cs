using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityOferta
    {
        public int id { get; set; }
        public string nombre { get; set; }
        public string descripcion { get; set; }
        public decimal monto_descuento { get; set; }
    }
}
