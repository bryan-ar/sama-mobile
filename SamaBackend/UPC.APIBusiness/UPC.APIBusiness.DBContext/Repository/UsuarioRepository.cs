using Dapper;
using DBEntity;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;

namespace DBContext
{
    public class UsuarioRepository : BaseRepository, IUsuarioRepository
    {
        public ResponseBase ObtenerUsuario(int IdUsuario)
        {
            var returnEntity = new ResponseBase();
            var usuario = new EntityUsuario();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Obtener_Usuario";
                    var p = new DynamicParameters();
                    p.Add(name: "@ID", value: IdUsuario, dbType: DbType.String, direction: ParameterDirection.Input);
                    usuario = db.Query<EntityUsuario>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    if (usuario != null)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = usuario;
                    }
                    else
                    {
                        returnEntity.isSuccess = false;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = "No puede logear";
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

        public ResponseBase Login(EntityLogin login)
        {
            var returnEntity = new ResponseBase();
            var usuario = new EntityUsuario();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Login";
                    var p = new DynamicParameters();
                    p.Add(name: "@EMAIL", value: login.Username, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@PASSWORD", value: login.Password, dbType: DbType.String, direction: ParameterDirection.Input);
                    usuario = db.Query<EntityUsuario>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    if (usuario != null)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = usuario;
                    }
                    else
                    {
                        returnEntity.isSuccess = false;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = "Credenciales incorrectas";
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

        public ResponseBase Registrar(EntityUsuario user)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Registrar_Usuario";

                    var p = new DynamicParameters();
                    p.Add(name: "@IDUSUARIO", dbType: DbType.Int32, direction: ParameterDirection.Output);
                    p.Add(name: "@EMAIL", value: user.email, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@PASSWORDUSUARIO", value: user.password, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@NOMBRES", value: user.nombres, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@APELLIDOPATERNO", value: user.apellido_paterno, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@APELLIDOMATERNO", value: user.apellido_materno, dbType: DbType.String, direction: ParameterDirection.Input);
                    p.Add(name: "@DOCUMENTOIDENTIDAD", value: user.documento_identidad, dbType: DbType.String, direction: ParameterDirection.Input);

                    db.Query<EntityUsuario>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    int idUsuario = p.Get<int>("@IDUSUARIO");

                    if(idUsuario > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = new
                        {
                            id = idUsuario,
                            nombre = user.nombres
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
            catch(Exception ex)
            {
                returnEntity.isSuccess = false;
                returnEntity.errorCode = "0001";
                returnEntity.errorMessage = ex.Message;
                returnEntity.data = null;
            }

            return returnEntity;
        }

        public ResponseBase ActualizarToken(EntityUsuario user)
        {
            var returnEntity = new ResponseBase();

            try
            {
                using (var db = GetSqlConnection())
                {
                    const string sql = @"usp_Actualizar_Token";

                    var p = new DynamicParameters();
                    p.Add(name: "@IDUSUARIO", value:user.id, dbType: DbType.Int32, direction: ParameterDirection.InputOutput);
                    p.Add(name: "@TOKEN", value: user.id_firebase, dbType: DbType.String, direction: ParameterDirection.Input);

                    db.Query<EntityUsuario>(sql: sql, param: p, commandType: CommandType.StoredProcedure).FirstOrDefault();

                    int idUsuario = p.Get<int>("@IDUSUARIO");

                    if (idUsuario > 0)
                    {
                        returnEntity.isSuccess = true;
                        returnEntity.errorCode = "0000";
                        returnEntity.errorMessage = string.Empty;
                        returnEntity.data = new
                        {
                            id = idUsuario
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
    }
}