using DBContext;
using DBEntity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace UPC.Business.API.Controllers
{
    /// <summary>
    /// 
    /// </summary>
    [Produces("application/json")]
    [Route("api/usuario")]
    [ApiController]
    public class UsuarioController : Controller
    {

        /// <summary>
        /// Constructor
        /// </summary>
        protected readonly IUsuarioRepository _UserRepository;


        /// <summary>
        /// 
        /// </summary>
        /// <param name="UserRepository"></param>
        public UsuarioController(IUsuarioRepository UserRepository)
        {
            _UserRepository = UserRepository;

        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="login"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpPost]
        [Route("login")]
        public ActionResult Login(EntityLogin login)
        {
            var ret = _UserRepository.Login(login);

            if (ret.isSuccess)
            {
                var usuario = (EntityUsuario)ret.data;
                usuario.id_firebase = login.FirebaseId;
                var token = _UserRepository.ActualizarToken(usuario);
            }

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="usuario"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpPost]
        [Route("registrar")]
        public ActionResult Registrar(EntityUsuario usuario)
        {
            var ret = _UserRepository.Registrar(usuario);

            return Json(ret);
        }
    }
}