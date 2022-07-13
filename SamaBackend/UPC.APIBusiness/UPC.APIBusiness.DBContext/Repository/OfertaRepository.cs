using Dapper;
using DBContext;
using DBEntity;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using UPC.APIBusiness.DBContext.Interface;

namespace UPC.APIBusiness.DBContext.Repository
{
    public class OfertaRepository : BaseRepository, IOfertaRepository
    {
        public ResponseBase getOfertas()
        {
            var returnEntity = new ResponseBase();
            var ofertas = new List<EntityOferta>();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Listar_Ofertas";
                    ofertas = db.Query<EntityOferta>(sql: sql, commandType: CommandType.StoredProcedure).ToList();

                    if (ofertas.Count > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = ofertas;
                    }
                    else
                    {
                        returnEntity.isSuccess = false;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = null;
                    }
                }
            }
            catch (Exception ex)
            {
                returnEntity.isSuccess = false;
                returnEntity.errorCode = "0001";
                returnEntity.errorMessage = ex.Message;
                returnEntity.data = null;
            }

            return returnEntity;
        }

    }
}
