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
    [Route("api/local")]
    [ApiController]
    public class LocalController : Controller
    {
        /// <summary>
        /// 
        /// </summary>
        protected readonly ILocalRepository _localRepository;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="localRepository"></param>
        public LocalController(ILocalRepository localRepository)
        {
            _localRepository = localRepository;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        [Produces("application/json")]
        [AllowAnonymous]
        [HttpGet]
        [Route("getlocales")]
        public ActionResult getLocales()
        {
            var ret = _localRepository.getLocales();

            return Json(ret);
        }
    }
}
