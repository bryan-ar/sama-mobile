using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityLocal
    {
        public int id { get; set; }
        public string nombre { get; set; }
        public string direccion { get; set; }
        public decimal gps_lat { get; set; }
        public decimal gps_lon { get; set; }
        public int favorite { get; set; }
    }
}
