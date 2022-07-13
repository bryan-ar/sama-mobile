using DBEntity;
using System;
using System.Collections.Generic;
using System.Text;

namespace DBContext
{
    public interface IUsuarioRepository
    {
        ResponseBase ObtenerUsuario(int IdUsuario);
        ResponseBase Login(EntityLogin login);
        ResponseBase Registrar(EntityUsuario user);
        ResponseBase ActualizarToken(EntityUsuario user);
    }
}
