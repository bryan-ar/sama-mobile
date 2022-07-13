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
    public class PedidoRepository : BaseRepository, IPedidoRepository
    {
        public ResponseBase getProductos()
        {
            var returnEntity = new ResponseBase();
            var entitiesProductos = new List<EntityProducto>();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Listar_Productos";
                    var p = new DynamicParameters();
                    entitiesProductos = db.Query<EntityProducto>(sql: sql, param: p, commandType: CommandType.StoredProcedure).ToList();

                    if (entitiesProductos.Count > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = entitiesProductos;
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

        public ResponseBase getPedidosxUsuario(int IDUSUARIO)
        {
            var returnEntity = new ResponseBase();
            var pedidos = new List<EntityPedido>();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Listar_Pedidos_X_Usuario";
                    var p = new DynamicParameters();
                    p.Add(name: "@IDUSUARIO", value: IDUSUARIO, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    pedidos = db.Query<EntityPedido>(sql: sql, param: p, commandType: CommandType.StoredProcedure).ToList();

                    if (pedidos.Count > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = pedidos;
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

        public ResponseBase getPedido(int IDPEDIDO)
        {
            var returnEntity = new ResponseBase();
            var pedido = new EntityPedido();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Obtener_Pedido";
                    var p = new DynamicParameters();
                    p.Add(name: "@IDPEDIDO", value: IDPEDIDO, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    var multi = db.QueryMultiple(sql: sql, param: p, commandType: CommandType.StoredProcedure);

                    pedido = multi.Read<EntityPedido>().First();
                    pedido.orden = multi.Read<EntityPedidoOrden>().ToList();
                    pedido.pago = multi.Read<EntityPedidoPago>().First();


                    if (pedido != null)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = pedido;
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

        public ResponseBase getPedidoOrden(int IDPEDIDO)
        {
            var returnEntity = new ResponseBase();
            var ordenPedido = new List<EntityPedidoOrden>();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Listar_Orden_X_Pedido";
                    var p = new DynamicParameters();
                    p.Add(name: "@IDPEDIDO", value: IDPEDIDO, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    ordenPedido = db.Query<EntityPedidoOrden>(sql: sql, param: p, commandType: CommandType.StoredProcedure).ToList();

                    if (ordenPedido.Count > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = ordenPedido;
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

        public ResponseBase RegistrarPago(EntityPedidoPago pago)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Registrar_Pago";

                    var p = new DynamicParameters();
                    p.Add(name: "@ID", dbType: DbType.Int32, direction: ParameterDirection.Output);
                    p.Add(name: "@IDPEDIDO", value: pago.id_pedido, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    p.Add(name: "@TARJETA", value: pago.tarjeta, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@NOMBRE", value: pago.nombre, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@FECHAEXP", value: pago.fecha_exp, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@CVV", value: pago.cvv, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@ESTADO", value: pago.estado, dbType: DbType.Int32, direction: ParameterDirection.Input);

                    db.Query<EntityPedidoPago>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    int idPago = p.Get<int>("@ID");

                    if (idPago > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = new
                        {
                            id = idPago
                        };
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

        public ResponseBase RegistrarOrden(List<EntityPedidoOrden> lista_orden)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    int rows= db.Execute("INSERT INTO pedidos_orden(id_pedido, id_producto, cantidad, precio, monto_pagar)"  +
                        " VALUES(@id_pedido, @id_producto, @cantidad, @precio, @monto_pagar)", lista_orden);  


                    if (rows > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = new
                        {
                            id = lista_orden[0].id_pedido
                        };
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

        public ResponseBase Registrar(EntityPedido pedido)
        {
            var returnEntity = new ResponseBase();
            DateTime fecha_creacion = DateTime.Now;
            DateTime hora_llegada = DateTime.Now.AddHours(1);

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Registrar_Pedido";

                    var p = new DynamicParameters();
                    p.Add(name: "@ID", dbType: DbType.Int32, direction: ParameterDirection.Output);
                    p.Add(name: "@IDUSUARIO", value: pedido.id_usuario, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    p.Add(name: "@FECHACREACION", value: fecha_creacion, dbType: DbType.DateTime, direction: ParameterDirection.Input);
                    p.Add(name: "@DESCRIPCION", value: pedido.descripcion, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@MONTOTOTAL", value: pedido.monto_total, dbType: DbType.Decimal, direction: ParameterDirection.Input);
                    p.Add(name: "@ESTADO", value: pedido.estado, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    p.Add(name: "@CALIFICACION", value: pedido.calificacion, dbType: DbType.Decimal, direction: ParameterDirection.Input);
                    p.Add(name: "@HORALLEGADA", value: hora_llegada, dbType: DbType.DateTime, direction: ParameterDirection.Input);

                    db.Query<EntityPedido>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    int idUsuario = p.Get<int>("@ID");

                    if (idUsuario > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = new
                        {
                            id = idUsuario,
                            descripcion = pedido.descripcion
                        };
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

        public ResponseBase Actualizar(EntityPedido pedido)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Actualizar_Pedido";

                    var p = new DynamicParameters();
                    p.Add(name: "@ID", value: pedido.id, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    p.Add(name: "@IDUSUARIO", value: pedido.id_usuario, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@FECHACREACION", value: pedido.fecha_creacion, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@DESCRIPCION", value: pedido.descripcion, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@MONTOTOTAL", value: pedido.monto_total, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@ESTADO", value: pedido.estado, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@CALIFICACION", value: pedido.calificacion, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@HORALLEGADA", value: pedido.hora_llegada, dbType: DbType.Int32, direction: ParameterDirection.Input);

                    db.Query<EntityPedido>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    int idPedido = p.Get<int>("@ID");

                    if (idPedido > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = new
                        {
                            id = idPedido,
                            descripcion = pedido.descripcion
                        };
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

        public ResponseBase Calificar(EntityPedido pedido)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Calificar_Pedido";

                    var p = new DynamicParameters();
                    p.Add(name: "@ID", value: pedido.id, dbType: DbType.Int32, direction: ParameterDirection.Input);
                    p.Add(name: "@CALIFICACION", value: pedido.calificacion, dbType: DbType.Decimal, direction: ParameterDirection.Input);
                    p.Add(name: "@ESTADO", value: pedido.estado, dbType: DbType.Int32, direction: ParameterDirection.Input);

                    EntityPedido pedidoBD = db.Query<EntityPedido>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    if (pedidoBD != null)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = pedidoBD;
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

        public ResponseBase FinalizarPedido(int id)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Finalizar_Pedido";

                    var p = new DynamicParameters();
                    p.Add(name: "@ID", value: id, dbType: DbType.Int32, direction: ParameterDirection.InputOutput);

                    EntityPedido pedido = db.Query<EntityPedido>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    if (pedido != null)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = pedido;
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
