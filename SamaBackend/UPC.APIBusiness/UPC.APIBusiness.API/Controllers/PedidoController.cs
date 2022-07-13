using DBContext;
using DBEntity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using UPC.APIBusiness.API.Services;
using UPC.APIBusiness.DBContext.Interface;
using UPC.APIBusiness.DBEntity.Model;

namespace UPC.APIBusiness.API.Controllers
{
    /// <summary>
    /// 
    /// </summary>
    [Produces("application/json")]
    [Route("api/pedido")]
    [ApiController]
    public class PedidoController : Controller
    {
        /// <summary>
        /// 
        /// </summary>
        protected readonly IPedidoRepository _pedidoRepository;
        protected readonly IUsuarioRepository _usuarioRepository;
        protected readonly INotificationService _notificationService;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pedidoRepository"></param>
        public PedidoController(IPedidoRepository pedidoRepository, IUsuarioRepository usuarioRepository,
            INotificationService notificationService)
        {
            _pedidoRepository = pedidoRepository;
            _usuarioRepository = usuarioRepository;
            _notificationService = notificationService;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpGet]
        [Route("getproductos")]
        public ActionResult getProductos()
        {
            var ret = _pedidoRepository.getProductos();

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="IDUSUARIO"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpGet]
        [Route("getpedidos/{IDUSUARIO}")]
        public ActionResult getPedidos(int IDUSUARIO)
        {
            var ret = _pedidoRepository.getPedidosxUsuario(IDUSUARIO);

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="IDPEDIDO"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpGet]
        [Route("getpedido/{IDPEDIDO}")]
        public ActionResult getPedido(int IDPEDIDO)
        {
            var ret = _pedidoRepository.getPedido(IDPEDIDO);

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pedido"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpPost]
        [Route("registrar")]
        public ActionResult Registrar(EntityPedido pedido)
        {
            //Registrar pedido
            var rpta_pedido = _pedidoRepository.Registrar(pedido);

            if(rpta_pedido.data == null)
            {
                return Json(rpta_pedido);
            }

            object data = rpta_pedido.data;
            int id_pedido = (int)data.GetType().GetProperty("id").GetValue(data, null);

            foreach(EntityPedidoOrden x in pedido.orden)
            {
                x.id_pedido = id_pedido;
            }

            pedido.pago.id_pedido = id_pedido;

            //Registrar Orden
            var rpta_orden = _pedidoRepository.RegistrarOrden(pedido.orden);

            //Registrar Pago
            var rpta_pago = _pedidoRepository.RegistrarPago(pedido.pago);

            var ret = _pedidoRepository.getPedido(id_pedido);

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pedido"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpPut]
        [Route("actualizar")]
        public ActionResult Actualizar(EntityPedido pedido)
        {
            var ret = _pedidoRepository.Actualizar(pedido);

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pedido"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpPut]
        [Route("calificar")]
        public ActionResult Calificar(EntityPedido pedido)
        {
            var ret = _pedidoRepository.Calificar(pedido);

            return Json(ret);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pedido"></param>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpPut]
        [Route("finalizar_pedido")]
        public async Task<IActionResult> FinalizarPedido(EntityPedido pedido)
        {
            var result = new object { };
            var ret = _pedidoRepository.FinalizarPedido(pedido.id);

            if (ret.isSuccess)
            {
                pedido = (EntityPedido)ret.data;

                var res = _usuarioRepository.ObtenerUsuario(pedido.id_usuario);
                var usuario = (EntityUsuario)res.data;

                NotificationModel notificationModel = new NotificationModel();
                notificationModel.DeviceId = usuario.id_firebase;
                notificationModel.IsAndroiodDevice = true;
                notificationModel.Title = "Restaurante SAMA";
                notificationModel.Body = "Su pedido fue entregado. ¡Disfrútelo!";

                result = await _notificationService.SendNotification(notificationModel);
            }


            
            return Ok(result);
        }

    }
}
