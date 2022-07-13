using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using UPC.APIBusiness.DBContext.Interface;

namespace UPC.APIBusiness.API.Controllers
{
    /// <summary>
    /// 
    /// </summary>
    [Produces("application/json")]
    [Route("api/oferta")]
    [ApiController]
    public class OfertaController : Controller
    {
        /// <summary>
        /// 
        /// </summary>
        protected readonly IOfertaRepository _ofertaRepository;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="ofertaRepository"></param>
        public OfertaController(IOfertaRepository ofertaRepository)
        {
            _ofertaRepository = ofertaRepository;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpGet]
        [Route("getofertas")]
        public ActionResult getOfertas()
        {
            var ret = _ofertaRepository.getOfertas();

            return Json(ret);
        }
    }
}
