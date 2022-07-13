using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityProducto
    {
        public int id { get; set; }
        public int id_categoria { get; set; }
        public string descripcion { get; set; }
        public decimal precio { get; set; }
        public int stock { get; set; }
    }
}
