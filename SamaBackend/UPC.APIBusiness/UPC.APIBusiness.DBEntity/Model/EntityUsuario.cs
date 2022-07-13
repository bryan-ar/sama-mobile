using System;
using System.Collections.Generic;
using System.Text;

namespace DBEntity
{
    public class EntityUsuario
    {
        public int id { get; set; }
        public string email { get; set; }
        public string password { get; set; }
        public string confirm_password { get; set; }
        public string nombres { get; set; }
        public string apellido_paterno { get; set; }
        public string apellido_materno { get; set; }
        public string documento_identidad { get; set; }
        public string id_firebase { get; set; }
    }
}