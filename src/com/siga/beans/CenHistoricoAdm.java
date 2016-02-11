/*
 * VERSIONES:
 * 
 * miguel.villegas - 20-12-2004 - Funciones de accesos a BBDDs y relacionadas con el historico
 * daniel.campos - 12-01-2005 - Incorpora metodos insert y getNuevoId
 *	
 */
package com.siga.beans;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.redabogacia.sigaservices.app.exceptions.BusinessException;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.AuditoriaUsuariosForm;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class CenHistoricoAdm extends MasterBeanAdministrador {
	
	public static final int  ACCION_INSERT = 1,
							 ACCION_UPDATE = 2,
						     ACCION_DELETE = 3,
						     MAX_NUM_CARACTERES_DESCRIPCION=4000;

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public CenHistoricoAdm(UsrBean usu) {
		super(CenHistoricoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenHistoricoBean.C_IDPERSONA,
							CenHistoricoBean.C_IDINSTITUCION,
							CenHistoricoBean.C_IDHISTORICO,
							CenHistoricoBean.C_FECHAENTRADA,
							CenHistoricoBean.C_FECHAEFECTIVA,
							CenHistoricoBean.C_MOTIVO,
							CenHistoricoBean.C_IDTIPOCAMBIO,
							CenHistoricoBean.C_DESCRIPCION,
							CenHistoricoBean.C_OBSERVACIONES,
							CenHistoricoBean.C_FECHAMODIFICACION,
							CenHistoricoBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenHistoricoBean.C_IDPERSONA,
							CenHistoricoBean.C_IDINSTITUCION,
							CenHistoricoBean.C_IDHISTORICO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenHistoricoBean bean = null;
		try {
			bean = new CenHistoricoBean();
			bean.setIdPersona (UtilidadesHash.getLong(hash,CenHistoricoBean.C_IDPERSONA));			
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,CenHistoricoBean.C_IDINSTITUCION));
			bean.setIdHistorico (UtilidadesHash.getInteger(hash,CenHistoricoBean.C_IDHISTORICO));
			bean.setFechaEntrada (UtilidadesHash.getString(hash,CenHistoricoBean.C_FECHAENTRADA));
			bean.setFechaEfectiva (UtilidadesHash.getString(hash,CenHistoricoBean.C_FECHAEFECTIVA));
			bean.setMotivo (UtilidadesHash.getString(hash,CenHistoricoBean.C_MOTIVO));
			bean.setIdTipoCambio (UtilidadesHash.getInteger(hash,CenHistoricoBean.C_IDTIPOCAMBIO));			
			bean.setDescripcion(UtilidadesHash.getString(hash,CenHistoricoBean.C_DESCRIPCION));	
			bean.setObservaciones(UtilidadesHash.getString(hash,CenHistoricoBean.C_OBSERVACIONES));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenHistoricoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenHistoricoBean.C_USUMODIFICACION));
			
		} catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable<String,Object> htData = null;
		try {
			htData = new Hashtable<String,Object>();
			CenHistoricoBean b = (CenHistoricoBean) bean;
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDPERSONA,b.getIdPersona ());
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDINSTITUCION,b.getIdInstitucion ());
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDINSTITUCIONCARGO,b.getIdInstitucionCargo()); 			
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDHISTORICO, b.getIdHistorico());
			UtilidadesHash.set(htData,CenHistoricoBean.C_FECHAENTRADA,b.getFechaEntrada());
			UtilidadesHash.set(htData,CenHistoricoBean.C_FECHAEFECTIVA,b.getFechaEfectiva());
			UtilidadesHash.set(htData,CenHistoricoBean.C_MOTIVO,b.getMotivo());
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDTIPOCAMBIO ,b.getIdTipoCambio());
			UtilidadesHash.set(htData,CenHistoricoBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenHistoricoBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(htData,CenHistoricoBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,CenHistoricoBean.C_USUMODIFICACION,b.getUsuMod());
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		
		return htData;	
	}

	/** 
	 * Obtiene el valor IDHISTORICO, <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Integer - Valor del historico  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Integer getNuevoID (Hashtable<String,Object> entrada) throws ClsExceptions, SIGAException {
		try {		
			RowsContainer rc = new RowsContainer();
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT NVL(MAX(IDHISTORICO), 0) + 1 AS IDHISTORICO FROM ");
			sql.append(nombreTabla);
			sql.append(" WHERE IDPERSONA = ");
			sql.append(entrada.get("IDPERSONA"));
			sql.append(" AND IDINSTITUCION = ");
			sql.append(entrada.get("IDINSTITUCION"));
		
			if (rc.findForUpdate(sql.toString())) {
				Row fila = (Row) rc.get(0);
				Hashtable<String,Object> prueba = fila.getRow();			
				if (prueba.get("IDHISTORICO").equals("")) {
					return new Integer(1);
				} else 
					return UtilidadesHash.getInteger(prueba, "IDHISTORICO");								
			}
			
		} catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		
		return null;
	}

	/** 
	 * Devuelve el valor de IDHISTORICO, <br/>
	 * @param  bean con los valores del formulario 
	 * @return  Integer - valor del historico  
	 * @exception  ClsExceptions En cualquier caso de error
	 */	
	public Integer getNuevoID (CenHistoricoBean bean) throws ClsExceptions, SIGAException {
		try {		
			Hashtable<String,Object> hash = beanToHashTable (bean);
			Integer idHistorico = getNuevoID(hash);
			return idHistorico;
			
		} catch (SIGAException e) {
			throw e;
			
		} catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert(bean)' en B.D.");		
		}
	}

	/** 
	 * Adecua los formatos de las fechas para la insercion en BBDD. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable<String,Object> prepararFormatosFechas (Hashtable<String,Object> entrada)throws ClsExceptions, SIGAException {
		try {		
			String fechaEntrada = GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAENTRADA"));
			String fechaEfectiva = GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAEFECTIVA"));
			entrada.put("FECHAENTRADA",fechaEntrada);
			entrada.put("FECHAEFECTIVA",fechaEfectiva);			
		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al adecuar los formatos delas fechas.");		
		}
		return entrada;
	}
	
	/** 
	 * Recoge informacion de la tabla a partir de cierta informacion del historial<br/>
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @param  tipoCambio - tipo de cambio en el historial 
	 * @param  fechaInicio - inicio de rango de la fecha efectiva
	 * @param  fechaFin - fin de rango de la fecha efectiva
	 * @param motivo 
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector<Row> getHistorico(String idPersona, String idInstitucion, String tipoCambio, String fechaInicio, String fechaFin, String motivo) throws ClsExceptions,SIGAException {
		   Vector<Row> datos = new Vector<Row>();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDPERSONA + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDHISTORICO + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAENTRADA + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_MOTIVO + "," +	            			
	            			UtilidadesMultidioma.getCampoMultidioma(CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_DESCRIPCION, this.usrbean.getLanguage()) + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_OBSERVACIONES + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + 
							" FROM " + 
							CenHistoricoBean.T_NOMBRETABLA +", "+ CenTipoCambioBean.T_NOMBRETABLA + 
							" WHERE " + 
							CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDTIPOCAMBIO + "=" + CenTipoCambioBean.T_NOMBRETABLA +"."+ CenTipoCambioBean.C_IDTIPOCAMBIO +
							" AND " +
							CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDINSTITUCION + "=" + idInstitucion;
							
				if (!tipoCambio.trim().equals("")){								 
					sql +=" AND " +
						  CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDTIPOCAMBIO + " IN (" + tipoCambio + ") ";									 
				}
				if (motivo!=null &&  !motivo.trim().equals("")){								 
					sql +=" AND LOWER(" +
						  CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_MOTIVO + ") like '%" + motivo.trim().toLowerCase()+"%'";									 
				}
				
				String auxFechaInicio = "";
				String auxFechaFin = "";
				if (!fechaInicio.trim().equals("")) 
	 				auxFechaInicio = GstDate.getApplicationFormatDate("",fechaInicio);
				if (!fechaFin.trim().equals("")) 
	 				auxFechaFin = GstDate.getApplicationFormatDate("",fechaFin);
				
				if ((fechaInicio!=null && !fechaInicio.trim().equals("")) || (fechaFin!=null && !fechaFin.trim().equals(""))) {
					sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_FECHAEFECTIVA,auxFechaInicio,auxFechaFin);
				}
				
				sql += " ORDER BY " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAENTRADA + " DESC, " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO ; 										
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       } catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener productos del historial");
	       }
	       return datos;                        
	    }
		
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idPers - identificador de la persona 
	 * @param  idInst - identificador de la institucion
	 * @param  idHistor - identificador del historico	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerEntradaHistorico (String idPers, String idInst, String idHistor) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDPERSONA  + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDHISTORICO  + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAENTRADA  + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_MOTIVO + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_DESCRIPCION  + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_OBSERVACIONES + "," +
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + ", " +
	            			" (SELECT " + AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_DESCRIPCION + " FROM " + AdmUsuariosBean.T_NOMBRETABLA +
	            			" WHERE "+ AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_IDUSUARIO + " = " + CenHistoricoBean.T_NOMBRETABLA  + "." + CenHistoricoBean.C_USUMODIFICACION+
							" AND "+CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDINSTITUCION+" = "+AdmUsuariosBean.T_NOMBRETABLA+"."+AdmUsuariosBean.C_IDINSTITUCION + ") " + 
	            			" AS NOMBRE_USU_MOD"+
							" FROM " + CenHistoricoBean.T_NOMBRETABLA + 
							" WHERE " +
							CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDPERSONA + "=" + idPers +
							" AND " +
							CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDINSTITUCION + "=" + idInst +
							" AND " +							
							CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDHISTORICO + "=" + idHistor; 
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada del historial.");
	       }
	       return datos;                        
	    }

	/** 
	 *  Funcion que inserta un registro en Cen_Historico
	 * @param  registro - Registro a introducir
	 * @param  usuario - UsrBean en ejecucion	 
	 * @return  Boolean - Resultado de la operacion   
	 */	
	public boolean insertarRegistroHistorico(CenHistoricoBean registro, UsrBean usuario)throws ClsExceptions, SIGAException {
		try{ 
			Hashtable<String,Object> hash = this.beanToHashTable(registro);
			// Adecuo formatos
			this.prepararFormatosFechas(hash);

			// Inserto en CEN_HISTORICO 
			return this.insert(hash); 			
		} catch (SIGAException e) {
			throw e;
		} catch (Exception ee){
			throw new ClsExceptions(ee,"Ha fallado el proceso de insercion en el historico");				
		}
	}	
	
	/** Funcion insert (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el insert 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insert(Hashtable hash) throws ClsExceptions{
		try {
			if(UtilidadesHash.getInteger(hash, CenHistoricoBean.C_IDHISTORICO)==null)
				UtilidadesHash.set(hash, CenHistoricoBean.C_IDHISTORICO, getNuevoID(hash));
			return super.insert(hash);
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el 'insert' en B.D");
		}
	}
	
	public Paginador getAuditoriaUsuariosConPaginador (Integer idInstitucion, AuditoriaUsuariosForm datos) throws ClsExceptions,SIGAException 
	{
       try {
            String sql =" SELECT " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDPERSONA  + "," +
									 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + ", " +
									 AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_DESCRIPCION + " AS NOMBRE_USUARIO, " + 
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDHISTORICO  + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAENTRADA  + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_MOTIVO + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + ", " +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_OBSERVACIONES + "," +
			            			 CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_DESCRIPCION + " AS DESCRIPCION_TIPO_ACCESO" + 
								 
						 " FROM " + CenHistoricoBean.T_NOMBRETABLA + ", " + AdmUsuariosBean.T_NOMBRETABLA + ", " + CenTipoCambioBean.T_NOMBRETABLA +
						 
						" WHERE " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + " = " + idInstitucion +
						  " AND " +	CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + " = " + AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_IDINSTITUCION  + " (+) " +
						  " AND " +	CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + " = " + AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_IDUSUARIO + " (+) " +
						  " AND " +	CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + " = " + CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_IDTIPOCAMBIO;

            if (datos.getTipoAccion() != null && !datos.getTipoAccion().equals("")) {
            	sql += " AND " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + " = " + datos.getTipoAccion();
            }

            if (UtilidadesString.stringToBoolean(datos.getUsuarioAutomatico())) {
			   	sql += " AND " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + " = " + ClsConstants.USUMODIFICACION_AUTOMATICO;
            }
            else { 
	            if (datos.getNombre() != null && !datos.getNombre().equals("")) {
				   	sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta (datos.getNombre(), AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_DESCRIPCION);
	            }
            }
		
			String fDesde = datos.getFechaDesde(); 
			String fHasta = datos.getFechaHasta();
			if ((fDesde != null && !fDesde.trim().equals("")) || (fHasta != null && !fHasta.trim().equals(""))) {
				if (!fDesde.equals(""))
					fDesde = GstDate.getApplicationFormatDate("", fDesde); 
				if (!fHasta.equals(""))
					fHasta = GstDate.getApplicationFormatDate("", fHasta);
				sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA, fDesde, fHasta);
			}
			
			sql += " ORDER BY " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA;

			Paginador paginador = new Paginador(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			 return paginador;  
       }
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada del historial.");
		}
    }
	
	public Vector getAuditoriaUsuarios (String idInstitucion, String idPersona, String idHistorico) throws ClsExceptions,SIGAException 
	{
       try {
            String sql =" SELECT " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDPERSONA  + "," +
									 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + ", " +
									 AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_DESCRIPCION + " AS NOMBRE_USUARIO, " + 
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDHISTORICO  + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAENTRADA  + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_MOTIVO + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + ", " +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_DESCRIPCION + ", " +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_OBSERVACIONES + "," +
			            			 UtilidadesMultidioma.getCampoMultidiomaSimple(CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS DESCRIPCION_TIPO_ACCESO" + 
								 
						 " FROM " + CenHistoricoBean.T_NOMBRETABLA + ", " + AdmUsuariosBean.T_NOMBRETABLA + ", " + CenTipoCambioBean.T_NOMBRETABLA +
						 
						" WHERE " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + " = " + idInstitucion +
						  " AND " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDPERSONA +     " = " + idPersona +
						  " AND " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDHISTORICO +   " = " + idHistorico +
						  " AND " +	CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + " = " + AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_IDINSTITUCION  + " (+) " +
						  " AND " +	CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + " = " + AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_IDUSUARIO + " (+) " +
						  " AND " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + " = " + CenTipoCambioBean.T_NOMBRETABLA + "." + CenTipoCambioBean.C_IDTIPOCAMBIO;
            
            return this.selectGenerico(sql);
       }
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada del historial.");
		}
    }
	
	/**
	 * Gestiona CEN_HISTORICO
	 * @param hHistorico
	 * @param hBeanAsociado
	 * @param hBeanAsociadoOriginal
	 * @param nombreClaseBean
	 * @param accion
	 * @param idioma
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean insertCompleto(Hashtable hHistorico, Hashtable hBeanAsociado, Hashtable hBeanAsociadoOriginal, String nombreClaseBean, int accion, String idioma) throws ClsExceptions {
		try {
			MasterBeanAdministrador adm = null;
			
			if (nombreClaseBean.equalsIgnoreCase("CenClienteBean")) {
				adm = new CenClienteAdm(this.usrbean);
				
			} else if (nombreClaseBean.equalsIgnoreCase("CenPersonaBean")) {
				adm = new CenPersonaAdm(this.usrbean);
				
			} else if (nombreClaseBean.equalsIgnoreCase("PysServiciosSolicitadosBean")) {
				adm = new PysServiciosSolicitadosAdm(this.usrbean);
			
			} else if (nombreClaseBean.equalsIgnoreCase("PysSuscripcionBean")) {
				adm = new PysSuscripcionAdm(this.usrbean);
				
			} else if (nombreClaseBean.equalsIgnoreCase("PysCompraBean")) {
				adm = new PysCompraAdm(this.usrbean);
				
			} else if (nombreClaseBean.equalsIgnoreCase("ScsDesignaBean")) {
				adm = new ScsDesignaAdm(this.usrbean);
				
			}  else {
				return false;
			}

			MasterBean beanAsociado = adm.hashTableToBean(hBeanAsociado);
			beanAsociado.setOriginalHash(hBeanAsociadoOriginal);
			
			return this.insertCompleto(hHistorico, beanAsociado, accion, idioma);
			
		} catch (Exception e) {
			return false;
		}
	}

	public boolean insertCompleto(Hashtable hHistorico, MasterBean beanAsociado, int accion, String idioma) throws ClsExceptions
	{
		return this.insertCompleto((CenHistoricoBean)this.hashTableToBean(hHistorico), beanAsociado, accion, idioma, false);
	}
	
	public boolean insertCompleto(Hashtable hHistorico, MasterBean beanAsociado, int accion, String idioma, boolean bDesdeCGAE) throws ClsExceptions
	{
		return this.insertCompleto((CenHistoricoBean)this.hashTableToBean(hHistorico), beanAsociado, accion, idioma, bDesdeCGAE);
	}

	public boolean insertCompleto(CenHistoricoBean beanHistorico, MasterBean beanAsociado, int accion, String idioma) throws ClsExceptions{
		return this.insertCompleto(beanHistorico, beanAsociado, accion, idioma, false);
	}
	public boolean insertCompleto(CenHistoricoBean beanHistorico, MasterBean beanAsociado, int accion, String idioma, boolean bDesdeCGAE) throws ClsExceptions
	{
		try {
			Hashtable hBeanAsociado  = null, hBeanAsociadoAnterior = null;

			if (beanHistorico == null)
				beanHistorico = new CenHistoricoBean();
			
			do {
				
				if (beanAsociado instanceof CenPersonaBean) {
					CenPersonaBean beanCliente = (CenPersonaBean) beanAsociado;
					CenPersonaAdm adm = new CenPersonaAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCliente);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCliente.getOriginalHash()));
					
					beanHistorico.setIdInstitucion(Integer.parseInt(this.usrbean.getLocation()));
					beanHistorico.setIdPersona(beanCliente.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_GENERALES));
					break;
					
				} else if (beanAsociado instanceof CenClienteBean) {
					CenClienteBean beanCliente = (CenClienteBean) beanAsociado;
					CenClienteAdm adm = new CenClienteAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCliente);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCliente.getOriginalHash()));
					
					beanHistorico.setIdInstitucion(beanCliente.getIdInstitucion());
					beanHistorico.setIdPersona(beanCliente.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_GENERALES));
					break;
					
				} else if (beanAsociado instanceof CenColegiadoBean) {
					CenColegiadoBean beanCliente = (CenColegiadoBean) beanAsociado;
					CenColegiadoAdm adm = new CenColegiadoAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCliente);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCliente.getOriginalHash()));

					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES));			
					break;
					
				} else if (beanAsociado instanceof CenComponentesBean) {
					CenComponentesBean beanComponentes = (CenComponentesBean) beanAsociado;
					CenComponentesAdm adm = new CenComponentesAdm(this.usrbean);
					
					beanHistorico.setIdInstitucion(beanComponentes.getIdInstitucion());
					beanHistorico.setIdPersona(beanComponentes.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COMPONENTES));
					
					//Datos de los campos a mostrar 
					try {

						hBeanAsociado = adm.beanToHashTable(beanComponentes);
						
						//Persona
						if(beanComponentes.getCen_Cliente_IdPersona()!=null){
							CenPersonaAdm pAdm = new CenPersonaAdm(this.usrbean);
							Hashtable hClaveP=new Hashtable();
							hClaveP.put(CenPersonaBean.C_IDPERSONA, beanComponentes.getCen_Cliente_IdPersona());
							CenPersonaBean pBean =(CenPersonaBean)pAdm.selectByPK(hClaveP).get(0);
							UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, pBean.getApellido1() + " "+ pBean.getApellido2() + ", "+ pBean.getNombre()+" NIF/CIF "+pBean.getNIFCIF());
						}else{
							UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA,"");
						}
						//Cuenta
						if((beanComponentes.getIdCuenta()!=null)&&(beanComponentes.getIdCuenta()>0)&&(beanComponentes.getCen_Cliente_IdPersona()!=null)&&(beanComponentes.getCen_Cliente_IdInstitucion()!=null))
						{
							CenCuentasBancariasAdm cAdm = new CenCuentasBancariasAdm(this.usrbean);
							Hashtable hClaveC=new Hashtable();
							hClaveC.put(CenCuentasBancariasBean.C_IDPERSONA, beanComponentes.getCen_Cliente_IdPersona());	
							hClaveC.put(CenCuentasBancariasBean.C_IDCUENTA, beanComponentes.getIdCuenta());
							hClaveC.put(CenCuentasBancariasBean.C_IDINSTITUCION, beanComponentes.getCen_Cliente_IdInstitucion());	
							
							CenCuentasBancariasBean cBean =(CenCuentasBancariasBean)cAdm.selectByPK(hClaveC).get(0);
							UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDCUENTA,UtilidadesString.mostrarIBANConAsteriscos(cBean.getIban()));
							
						}else{
							UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDCUENTA,"");
						}
						
						//Tipo de Colegio
						if((beanComponentes.getIdTipoColegio()!=null)&&(!beanComponentes.getIdTipoColegio().isEmpty()))
						{
																				
							CenActividadProfesionalAdm actpAdm = new CenActividadProfesionalAdm(this.usrbean);
																						
							String sql= "SELECT "+UtilidadesMultidioma.getCampoMultidiomaSimple(CenActividadProfesionalBean.C_DESCRIPCION,this.usrbean.getLanguage())+ " AS DESCRIPCION " +
							            "  FROM "+CenActividadProfesionalBean.T_NOMBRETABLA+
							            " WHERE "+CenActividadProfesionalBean.C_IDACTIVIDADPROFESIONAL+"="+beanComponentes.getIdTipoColegio();

							Vector vDescTipoColegio=actpAdm.selectSQL(sql);
								
													
							if((vDescTipoColegio!=null)&&(vDescTipoColegio.size()>0)){
								CenActividadProfesionalBean actBean =(CenActividadProfesionalBean)vDescTipoColegio.get(0);
								UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDTIPOCOLEGIO, actBean.getDescripcion());
							}
							
						}else{
							UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDTIPOCOLEGIO,"");
						}	
						
						//Cargo
						if((beanComponentes.getIdCargo()!=null)&&(!beanComponentes.getIdCargo().isEmpty()))
						{
																				
							CenCargoAdm cargoAdm=new CenCargoAdm(this.usrbean);
														
							String sql= "SELECT "+UtilidadesMultidioma.getCampoMultidiomaSimple(CenCargoBean.C_DESCRIPCION,this.usrbean.getLanguage())+ " AS DESCRIPCION " +
							            "  FROM "+CenCargoBean.T_NOMBRETABLA+
							            " WHERE "+CenCargoBean.C_IDCARGO+"="+beanComponentes.getIdCargo();

							Vector vDescCargo=cargoAdm.selectSQL(sql);
														
							if((vDescCargo!=null)&&(vDescCargo.size()>0)){
								CenCargoBean cargoBean=(CenCargoBean)vDescCargo.get(0);
								UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_CARGO, cargoBean.getDescripcion());
							}
							
						}else{
							UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_CARGO,"");
						}		
						
						//Provincia
						if((beanComponentes.getIdProvincia()!=null)&&(!beanComponentes.getIdProvincia().isEmpty()))
						{
					
							CenProvinciaAdm provAdm=new CenProvinciaAdm(this.usrbean);
							Hashtable hClaveProv=new Hashtable();
							hClaveProv.put(CenProvinciaBean.C_IDPROVINCIA, beanComponentes.getIdProvincia());
							
							Vector vDescProv=provAdm.selectByPK(hClaveProv);
							
							if((vDescProv!=null)&&(vDescProv.size()>0)){
								CenProvinciaBean provBean=(CenProvinciaBean) vDescProv.get(0);
								UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDPROVINCIA, provBean.getNombre());
							}else{
								UtilidadesHash.set(hBeanAsociado,CenComponentesBean.C_IDPROVINCIA,"");
							}
							
						//Si la Provincia está vacía se muestra la de la persona
						}else{
																					
							if(beanComponentes.getCen_Cliente_IdInstitucion()!=null)
							{
								
								CenInstitucionAdm insAdm=new CenInstitucionAdm(this.usrbean);
								Hashtable hClaveIns=new Hashtable();
								hClaveIns.put(CenInstitucionBean.C_IDINSTITUCION, beanComponentes.getCen_Cliente_IdInstitucion());
							
								Vector vDescProv=insAdm.selectByPK(hClaveIns);

								if((vDescProv!=null)&&(vDescProv.size()>0)){
									CenInstitucionBean insBean=(CenInstitucionBean)vDescProv.get(0);
									UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDPROVINCIA, insBean.getAbreviatura());
								
								}else{
									UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDPROVINCIA,"");
							    }	

							 }else{
								UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_IDPROVINCIA,"");
							 }	
	
						}	
						
						//Sino está informado el colegiado mostramos el de la persona
						if((beanComponentes.getNumColegiado()==null)||(beanComponentes.getNumColegiado().isEmpty())){
							
							if((beanComponentes.getCen_Cliente_IdInstitucion()!=null)&&(beanComponentes.getCen_Cliente_IdPersona()!=null))
							{
								
								CenColegiadoAdm colAdm=new CenColegiadoAdm(this.usrbean);
									
								String numCol=colAdm.getNumColegiado(beanComponentes.getCen_Cliente_IdInstitucion(),beanComponentes.getCen_Cliente_IdPersona());
								
								if((numCol!=null)&&(!numCol.isEmpty())){
									UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_NUMCOLEGIADO, numCol);
								}else{
									UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_NUMCOLEGIADO,"");
								}

							}else{
								UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_NUMCOLEGIADO,"");
							}
						}	

						//Si se han actualizado los datos del elemento hay que insertar otro registro con los datos modificados
						CenComponentesBean beanAsociadoAnterior = (CenComponentesBean) adm.hashTableToBean(beanComponentes.getOriginalHash());
						hBeanAsociadoAnterior = adm.beanToHashTable(beanAsociadoAnterior);
						//Si es null es nuevo componente y no hay registro anterior
						if(beanComponentes.getOriginalHash() != null){

							//Persona
							if(beanAsociadoAnterior.getCen_Cliente_IdPersona()!=null){
								
								Hashtable hClavePU=new Hashtable();
								hClavePU.put(CenPersonaBean.C_IDPERSONA, beanAsociadoAnterior.getCen_Cliente_IdPersona());
								CenPersonaAdm pAdm = new CenPersonaAdm(this.usrbean);
								CenPersonaBean pUBean =(CenPersonaBean)pAdm.selectByPK(hClavePU).get(0);
								UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, pUBean.getApellido1() + " "+ pUBean.getApellido2() + ", "+ pUBean.getNombre()+" NIF/CIF "+pUBean.getNIFCIF());
							}else{
								UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA,"");
							}
							//Cuenta
							if((beanAsociadoAnterior.getIdCuenta()!=null)&&(beanAsociadoAnterior.getIdCuenta()>0)&&(beanAsociadoAnterior.getCen_Cliente_IdPersona()!=null)&&(beanAsociadoAnterior.getCen_Cliente_IdInstitucion()!=null))
							{
								CenCuentasBancariasAdm cUAdm = new CenCuentasBancariasAdm(this.usrbean);
								Hashtable hClaveCU=new Hashtable();
								hClaveCU.put(CenCuentasBancariasBean.C_IDPERSONA, beanAsociadoAnterior.getCen_Cliente_IdPersona());	
								hClaveCU.put(CenCuentasBancariasBean.C_IDCUENTA, beanAsociadoAnterior.getIdCuenta());
								hClaveCU.put(CenCuentasBancariasBean.C_IDINSTITUCION, beanAsociadoAnterior.getCen_Cliente_IdInstitucion());	
								
								CenCuentasBancariasBean cUBean =(CenCuentasBancariasBean)cUAdm.selectByPK(hClaveCU).get(0);
								UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDCUENTA,UtilidadesString.mostrarIBANConAsteriscos(cUBean.getIban()));
							
							}else{
								UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDCUENTA,"");
							}	
							
							//Tipo de Colegio
							if((beanAsociadoAnterior.getIdTipoColegio()!=null)&&(!beanAsociadoAnterior.getIdTipoColegio().isEmpty()))
							{
								CenActividadProfesionalAdm actpAdm = new CenActividadProfesionalAdm(this.usrbean);
															
								String sql= "SELECT "+UtilidadesMultidioma.getCampoMultidiomaSimple(CenActividadProfesionalBean.C_DESCRIPCION,this.usrbean.getLanguage())+ " AS DESCRIPCION " +
								            "  FROM "+CenActividadProfesionalBean.T_NOMBRETABLA+
								            " WHERE "+CenActividadProfesionalBean.C_IDACTIVIDADPROFESIONAL+"="+beanAsociadoAnterior.getIdTipoColegio();

								Vector vDescTipoColegio=actpAdm.selectSQL(sql);
								
								if((vDescTipoColegio!=null)&&(vDescTipoColegio.size()>0)){
									CenActividadProfesionalBean actBean =(CenActividadProfesionalBean)vDescTipoColegio.get(0);
									UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDTIPOCOLEGIO, actBean.getDescripcion());
								}
								
							}else{
								UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDTIPOCOLEGIO,"");
							}	
							
							//Cargo
							if((beanAsociadoAnterior.getIdCargo()!=null)&&(!beanAsociadoAnterior.getIdCargo().isEmpty()))
							{
																					
								CenCargoAdm cargoAdm=new CenCargoAdm(this.usrbean);
														
								String sql= "SELECT "+UtilidadesMultidioma.getCampoMultidiomaSimple(CenCargoBean.C_DESCRIPCION,this.usrbean.getLanguage())+ " AS DESCRIPCION " +
								            "  FROM "+CenCargoBean.T_NOMBRETABLA+
								            " WHERE "+CenCargoBean.C_IDCARGO+"="+beanAsociadoAnterior.getIdCargo();
	
								Vector vDescCargo=cargoAdm.selectSQL(sql);
															
								if((vDescCargo!=null)&&(vDescCargo.size()>0)){
									CenCargoBean cargoBean=(CenCargoBean)vDescCargo.get(0);
									UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_CARGO, cargoBean.getDescripcion());
								}
								
							}else{
								UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_CARGO,"");
							}	
							
							//Provincia
							if((beanAsociadoAnterior.getIdProvincia()!=null)&&(!beanAsociadoAnterior.getIdProvincia().isEmpty()))
							{
																					
								CenProvinciaAdm provAdm=new CenProvinciaAdm(this.usrbean);
								Hashtable hClaveProv=new Hashtable();
								hClaveProv.put(CenProvinciaBean.C_IDPROVINCIA, beanAsociadoAnterior.getIdProvincia());
								
								Vector vDescProv=provAdm.selectByPK(hClaveProv);
								
								if((vDescProv!=null)&&(vDescProv.size()>0)){
									CenProvinciaBean provBean=(CenProvinciaBean)vDescProv.get(0);
									UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDPROVINCIA, provBean.getNombre());
								}else{
									UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDPROVINCIA, "");
								}
								
							
							//Si la Provincia está vacía se muestra la de la persona
							}else{
																					
								if(beanAsociadoAnterior.getCen_Cliente_IdInstitucion()!=null){

									CenInstitucionAdm insAdm=new CenInstitucionAdm(this.usrbean);
									Hashtable hClaveIns=new Hashtable();
									hClaveIns.put(CenInstitucionBean.C_IDINSTITUCION, beanAsociadoAnterior.getCen_Cliente_IdInstitucion());
							
									Vector vDescProv=insAdm.selectByPK(hClaveIns);
																		
									if((vDescProv!=null)&&(vDescProv.size()>0)){
										CenInstitucionBean insBean=(CenInstitucionBean)vDescProv.get(0);
										UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDPROVINCIA, insBean.getAbreviatura());
									}else{
										UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDPROVINCIA,"");
									}
								
								}else{
									UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_IDPROVINCIA,"");
								}
								
							}
							
							//Sino está informado el colegiado mostramos el de la persona
							if((beanAsociadoAnterior.getNumColegiado()==null)||(beanAsociadoAnterior.getNumColegiado().isEmpty())){
								
								if((beanAsociadoAnterior.getCen_Cliente_IdInstitucion()!=null)&&(beanAsociadoAnterior.getCen_Cliente_IdPersona()!=null)){
									
									CenColegiadoAdm colAdm=new CenColegiadoAdm(this.usrbean);
									
									String numCol=colAdm.getNumColegiado(beanAsociadoAnterior.getCen_Cliente_IdInstitucion(),beanAsociadoAnterior.getCen_Cliente_IdPersona());
									
									if((numCol!=null)&&(!numCol.isEmpty())){
										UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_NUMCOLEGIADO, numCol);
									}else{
										UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_NUMCOLEGIADO,"");
									}
								}else{
									UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_NUMCOLEGIADO,"");
								}
							}
						}
					} 
					catch (Exception e) {}
					
					break;
					
				} else if (beanAsociado instanceof CenCuentasBancariasBean) {
					CenCuentasBancariasBean beanCuentas = (CenCuentasBancariasBean) beanAsociado;
					CenCuentasBancariasAdm adm = new CenCuentasBancariasAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCuentas);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCuentas.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanCuentas.getIdInstitucion());
					beanHistorico.setIdPersona(beanCuentas.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_CUENTAS_BANCARIAS));			

					// Sustituimos id's por descripciones
					try {
						String fin = "", p = UtilidadesHash.getString(hBeanAsociado, CenCuentasBancariasBean.C_ABONOCARGO);
						if (p.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO))       fin = UtilidadesString.getMensajeIdioma(idioma, "censo.tipoCuenta.abono"); 
						if (p.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_CARGO)) 	  fin = UtilidadesString.getMensajeIdioma(idioma, "censo.tipoCuenta.cargo"); 
						if (p.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO_CARGO)) fin = UtilidadesString.getMensajeIdioma(idioma, "censo.tipoCuenta.abonoCargo");
						UtilidadesHash.set(hBeanAsociado, CenDireccionesBean.C_PREFERENTE, fin);

						p = UtilidadesHash.getString(hBeanAsociadoAnterior, CenCuentasBancariasBean.C_ABONOCARGO);
						if (p.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO))       fin = UtilidadesString.getMensajeIdioma(idioma, "censo.tipoCuenta.abono"); 
						if (p.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_CARGO)) 	  fin = UtilidadesString.getMensajeIdioma(idioma, "censo.tipoCuenta.cargo"); 
						if (p.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO_CARGO)) fin = UtilidadesString.getMensajeIdioma(idioma, "censo.tipoCuenta.abonoCargo");
						UtilidadesHash.set(hBeanAsociadoAnterior, CenDireccionesBean.C_PREFERENTE, fin);
					} 
					catch (Exception e) {}
					try {
						UtilidadesHash.set(hBeanAsociado, CenCuentasBancariasBean.C_NUMEROCUENTA, UtilidadesString.mostrarNumeroCuentaConAsteriscos(UtilidadesHash.getString(hBeanAsociado, CenCuentasBancariasBean.C_NUMEROCUENTA)));
						UtilidadesHash.set(hBeanAsociadoAnterior, CenCuentasBancariasBean.C_NUMEROCUENTA, UtilidadesString.mostrarNumeroCuentaConAsteriscos(UtilidadesHash.getString(hBeanAsociadoAnterior, CenCuentasBancariasBean.C_NUMEROCUENTA)));
						
						UtilidadesHash.set(hBeanAsociado, CenCuentasBancariasBean.C_IBAN, UtilidadesString.mostrarIBANConAsteriscos(UtilidadesHash.getString(hBeanAsociado, CenCuentasBancariasBean.C_IBAN)));
						UtilidadesHash.set(hBeanAsociadoAnterior, CenCuentasBancariasBean.C_IBAN, UtilidadesString.mostrarIBANConAsteriscos(UtilidadesHash.getString(hBeanAsociadoAnterior, CenCuentasBancariasBean.C_IBAN)));
						
					} 
					catch (Exception e) {}

					break;
					
				} else if (beanAsociado instanceof CenDatosColegialesEstadoBean) {
					CenDatosColegialesEstadoBean beanDatosColegiales = (CenDatosColegialesEstadoBean) beanAsociado;
					CenDatosColegialesEstadoAdm adm = new CenDatosColegialesEstadoAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanDatosColegiales);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanDatosColegiales.getOriginalHash()));
					
					if(beanDatosColegiales.getFechaEstado()!=null && !beanDatosColegiales.getFechaEstado().equals(""))
						beanHistorico.setFechaEfectiva(beanDatosColegiales.getFechaEstado());
					
					beanHistorico.setIdPersona(beanDatosColegiales.getIdPersona());			
					beanHistorico.setIdInstitucion(beanDatosColegiales.getIdInstitucion());
					
					if (beanHistorico.getIdTipoCambio()!=null && !beanHistorico.getIdTipoCambio().equals("")){
					  beanHistorico.setIdTipoCambio(beanHistorico.getIdTipoCambio());
					}else{
						beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_INHABILITACION));	
					}
					
					
					// Sustituimos id's por descripciones
					try {
						CenEstadoColegialAdm eAdm = new CenEstadoColegialAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, CenDatosColegialesEstadoBean.C_IDESTADO, UtilidadesString.getMensajeIdioma(idioma, ((CenEstadoColegialBean)eAdm.selectByPK(hBeanAsociado).get(0)).getDescripcion()));
						UtilidadesHash.set(hBeanAsociadoAnterior,CenDatosColegialesEstadoBean.C_IDESTADO,UtilidadesString.getMensajeIdioma(idioma, ((CenEstadoColegialBean)eAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getDescripcion()));
					} 
					catch (Exception e) {}
					
					break;
					
				} else if (beanAsociado instanceof CenDatosCVBean) {
					CenDatosCVBean beanCV = (CenDatosCVBean) beanAsociado;
					CenDatosCVAdm adm = new CenDatosCVAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCV);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCV.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanCV.getIdInstitucion());
					beanHistorico.setIdInstitucionCargo(beanCV.getIdInstitucionCargo());
					beanHistorico.setIdPersona(beanCV.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_CV));			

					// Sustituimos id's por descripciones
					try {
						CenTiposCVAdm cAdm = new CenTiposCVAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, CenDatosCVBean.C_IDTIPOCV, UtilidadesString.getMensajeIdioma(idioma, ((CenTiposCVBean)cAdm.selectByPK(hBeanAsociado).get(0)).getDescripcion()));
						UtilidadesHash.set(hBeanAsociadoAnterior, CenDatosCVBean.C_IDTIPOCV, UtilidadesString.getMensajeIdioma(idioma, ((CenTiposCVBean)cAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getDescripcion()));
					} 
					catch (Exception e) {}

					break;
					
				} else if (beanAsociado instanceof CenDireccionesBean) {
					CenDireccionesBean beanDir = (CenDireccionesBean) beanAsociado;
					CenDireccionesAdm adm = new CenDireccionesAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanDir);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanDir.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanDir.getIdInstitucion());
					beanHistorico.setIdPersona(beanDir.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DIRECCIONES));

					// Sustituimos id's por descripciones
					try {
						CenPaisAdm paisAdm = new CenPaisAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, CenDireccionesBean.C_IDPAIS, UtilidadesString.getMensajeIdioma(idioma, ((CenPaisBean)paisAdm.selectByPK(hBeanAsociado).get(0)).getNombre()));
						UtilidadesHash.set(hBeanAsociadoAnterior, CenDireccionesBean.C_IDPAIS, UtilidadesString.getMensajeIdioma(idioma, ((CenPaisBean)paisAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getNombre()));
					}
					catch (Exception e) {}
					try {
						CenPoblacionesAdm pobAdm = new CenPoblacionesAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, CenDireccionesBean.C_IDPOBLACION, UtilidadesString.getMensajeIdioma(idioma, ((CenPoblacionesBean)pobAdm.selectByPK(hBeanAsociado).get(0)).getNombre()));
						UtilidadesHash.set(hBeanAsociadoAnterior, CenDireccionesBean.C_IDPOBLACION, UtilidadesString.getMensajeIdioma(idioma, ((CenPoblacionesBean)pobAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getNombre()));
					}
					catch (Exception e) {}
					try {
						CenProvinciaAdm provAdm = new CenProvinciaAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, CenDireccionesBean.C_IDPROVINCIA, UtilidadesString.getMensajeIdioma(idioma, ((CenProvinciaBean)provAdm.selectByPK(hBeanAsociado).get(0)).getNombre()));
						UtilidadesHash.set(hBeanAsociadoAnterior, CenDireccionesBean.C_IDPROVINCIA, UtilidadesString.getMensajeIdioma(idioma, ((CenProvinciaBean)provAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getNombre()));
					} 
					catch (Exception e) {}
					try {
						String p = UtilidadesHash.getString(hBeanAsociado, CenDireccionesBean.C_PREFERENTE);
						if (p != null && !p.equals("")) {
							String fin = ""; 
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_CORREO) >= 0)            fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.correo"); 
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO) >= 0) fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.mail"); 
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_FAX) >= 0)               fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.fax");
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_SMS) >= 0)               fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.sms");
							if (fin.length() > 2)
								fin = fin.substring(2);
							UtilidadesHash.set(hBeanAsociado, CenDireccionesBean.C_PREFERENTE, fin);
						}

						p = UtilidadesHash.getString(hBeanAsociadoAnterior, CenDireccionesBean.C_PREFERENTE);
						if (p != null && !p.equals("")) {
							String fin = ""; 
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_CORREO) >= 0)            fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.correo"); 
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_CORREOELECTRONICO) >= 0) fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.mail"); 
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_FAX) >= 0)               fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.fax");
							if (p.indexOf(ClsConstants.TIPO_PREFERENTE_SMS) >= 0)               fin += ", " + UtilidadesString.getMensajeIdioma(idioma, "censo.preferente.sms");
							if (fin.length() > 2)
								fin = fin.substring(2);
							UtilidadesHash.set(hBeanAsociadoAnterior, CenDireccionesBean.C_PREFERENTE, fin);
						}
					} 
					catch (Exception e) {}
					try {
					    Hashtable codigos = new Hashtable();
					    codigos.put(new Integer(1),beanDir.getIdInstitucion());
					    codigos.put(new Integer(2),beanDir.getIdPersona());
					    codigos.put(new Integer(3),beanDir.getIdDireccion());
					    codigos.put(new Integer(4),this.usrbean.getLanguage());

						String sql = " SELECT f_siga_gettiposdireccion(:1, " + 
																	  ":2, " + 
																	  ":3, " +
																	  ":4) DESCRIPCION FROM DUAL";
						Vector v = this.selectGenericoBind(sql, codigos);
						String sDirecciones = "";
						for (int k = 0; k < v.size(); k++) {
							sDirecciones += ", " + UtilidadesHash.getString((Hashtable)v.get(k), "DESCRIPCION");
						}
						UtilidadesHash.set(hBeanAsociado, "TIPO DIRECCIONES", (sDirecciones.length() > 2)?sDirecciones.substring(2):sDirecciones);
						UtilidadesHash.set(hBeanAsociadoAnterior, "TIPO DIRECCIONES", (String)(beanDir.getOriginalHash()).get(CenTipoDireccionBean.T_NOMBRETABLA+"."+CenTipoDireccionBean.C_DESCRIPCION));
					} 
					catch (Exception e) {}

					break;
					
				} else if (beanAsociado instanceof CenDireccionTipoDireccionBean) {
					CenDireccionTipoDireccionBean beanTipoDir = (CenDireccionTipoDireccionBean) beanAsociado;
					CenDireccionTipoDireccionAdm adm = new CenDireccionTipoDireccionAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanTipoDir);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanTipoDir.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanTipoDir.getIdInstitucion());
					beanHistorico.setIdPersona(beanTipoDir.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DIRECCIONES));
					break;
					
				} else if (beanAsociado instanceof CenGruposClienteClienteBean) {
					CenGruposClienteClienteBean beanGrupo = (CenGruposClienteClienteBean) beanAsociado;

					beanHistorico.setIdInstitucion(beanGrupo.getIdInstitucion());
					beanHistorico.setIdPersona(beanGrupo.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_GENERALES));

					// Sustituimos id's por descripciones
					try {
//						CenGruposClienteClienteAdm adm = new CenGruposClienteClienteAdm(this.usrbean);
//						Vector v = adm.busquedaGruposFijoPersona(""+beanGrupo.getIdInstitucion(),""+beanGrupo.getIdPersona());
//						String sGrupos = "";
//						for (int k = 0; k < v.size(); k++) {
//							sGrupos += ", " + UtilidadesHash.getString((Hashtable)v.get(k), CenGruposClienteBean.C_NOMBRE);
//						}
//						Hashtable h = new Hashtable();
//						UtilidadesHash.set (h, "GRUPOS FIJOS", sGrupos.substring(2));
						
						CenGruposClienteAdm adm = new CenGruposClienteAdm(this.usrbean);
						Hashtable h = new Hashtable();
						UtilidadesHash.set(h , CenGruposClienteBean.C_IDGRUPO, beanGrupo.getIdGrupo());
						UtilidadesHash.set(h , CenGruposClienteBean.C_IDINSTITUCION, beanGrupo.getIdInstitucionGrupo());

						Hashtable hDatos = new Hashtable();
						//aalg: se añade la utilidadMultidioma para obtener el nombre del grupo no el identificador
						String grupo = ((CenGruposClienteBean)adm.selectByPK(h).get(0)).getNombre();
						UtilidadesHash.set (hDatos, "GRUPOS FIJOS", UtilidadesMultidioma.getDatoMaestroIdioma(grupo,usrbean));
						hBeanAsociado = (Hashtable)hDatos.clone();
					}
					catch (Exception e) {}
					
					break;
					
				} else if (beanAsociado instanceof CenNoColegiadoActividadBean ) {
					CenNoColegiadoActividadBean beanGrupo = (CenNoColegiadoActividadBean) beanAsociado;

					beanHistorico.setIdInstitucion(beanGrupo.getIdInstitucion());
					beanHistorico.setIdPersona(beanGrupo.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_GENERALES));

					// Sustituimos id's por descripciones
					try {
//						
						
						CenActividadProfesionalAdm adm = new CenActividadProfesionalAdm(this.usrbean);
						Hashtable h = new Hashtable();
						UtilidadesHash.set(h , CenActividadProfesionalBean.C_IDACTIVIDADPROFESIONAL, beanGrupo.getIdActividadProfesional());

						Hashtable hDatos = new Hashtable();
						UtilidadesHash.set (hDatos, "GRUPOS FIJOS", ((CenActividadProfesionalBean)adm.selectByPK(h).get(0)).getDescripcion());
						hBeanAsociado = (Hashtable)hDatos.clone();
					}
					catch (Exception e) {}
					
					break;
					
				} else if (beanAsociado instanceof CenPersonaBean) {
					CenPersonaBean beanPersona = (CenPersonaBean) beanAsociado;
					CenPersonaAdm adm = new CenPersonaAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanPersona);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanPersona.getOriginalHash()));

					//beanHis.setIdInstitucion(beanPersona.getIdInstitucion());
					beanHistorico.setIdPersona(beanPersona.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_COLEGIAL));			
					break;
					
				} else if (beanAsociado instanceof ExpExpedienteBean) {
					ExpExpedienteBean beanExp = (ExpExpedienteBean) beanAsociado;
					ExpExpedienteAdm adm = new ExpExpedienteAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanExp);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanExp.getOriginalHash()));
					
					beanHistorico.setIdInstitucion(beanExp.getIdInstitucion());										
					beanHistorico.setIdPersona(beanExp.getIdPersonaDenunciado());											
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_EXPEDIENTES));

					// Sustituimos id's por descripciones
					try {
						ExpClasificacionesAdm cAdm = new ExpClasificacionesAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, ExpExpedienteBean.C_IDCLASIFICACION, UtilidadesString.getMensajeIdioma(idioma, ((ExpClasificacionesBean)cAdm.selectByPK(hBeanAsociado).get(0)).getNombre()));
						UtilidadesHash.set(hBeanAsociadoAnterior, ExpExpedienteBean.C_IDCLASIFICACION, UtilidadesString.getMensajeIdioma(idioma, ((ExpClasificacionesBean)cAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getNombre()));
					} 
					catch (Exception e) {}
					try {
						ExpEstadosAdm eAdm = new ExpEstadosAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, ExpExpedienteBean.C_IDESTADO, UtilidadesString.getMensajeIdioma(idioma, ((ExpEstadosBean)eAdm.selectByPK(hBeanAsociado).get(0)).getNombre()));
						UtilidadesHash.set(hBeanAsociadoAnterior, ExpExpedienteBean.C_IDESTADO, UtilidadesString.getMensajeIdioma(idioma, ((ExpEstadosBean)eAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getNombre()));
					} 
					catch (Exception e) {}
					try {
						ExpFasesAdm fAdm = new ExpFasesAdm(this.usrbean);
						UtilidadesHash.set(hBeanAsociado, ExpExpedienteBean.C_IDFASE, UtilidadesString.getMensajeIdioma(idioma, ((ExpFasesBean)fAdm.selectByPK(hBeanAsociado).get(0)).getNombre()));
						UtilidadesHash.set(hBeanAsociadoAnterior, ExpExpedienteBean.C_IDFASE, UtilidadesString.getMensajeIdioma(idioma, ((ExpFasesBean)fAdm.selectByPK(hBeanAsociadoAnterior).get(0)).getNombre()));
					} 
					catch (Exception e) {}
					try {
						ExpTipoExpedienteAdm tAdm = new ExpTipoExpedienteAdm(this.usrbean);
						
						Hashtable h = new Hashtable ();
						UtilidadesHash.set (h, ExpTipoExpedienteBean.C_IDINSTITUCION,   UtilidadesHash.getInteger(hBeanAsociado, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
						UtilidadesHash.set (h, ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,UtilidadesHash.getInteger(hBeanAsociado, ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
						UtilidadesHash.set(hBeanAsociado, ExpExpedienteBean.C_IDTIPOEXPEDIENTE, UtilidadesString.getMensajeIdioma(idioma, ((ExpTipoExpedienteBean)tAdm.selectByPK(h).get(0)).getNombre()));
						
						h.clear();
						UtilidadesHash.set (h, ExpTipoExpedienteBean.C_IDINSTITUCION,   UtilidadesHash.getInteger(hBeanAsociadoAnterior, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
						UtilidadesHash.set (h, ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,UtilidadesHash.getInteger(hBeanAsociadoAnterior, ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
						UtilidadesHash.set(hBeanAsociadoAnterior, ExpExpedienteBean.C_IDTIPOEXPEDIENTE, UtilidadesString.getMensajeIdioma(idioma, ((ExpTipoExpedienteBean)tAdm.selectByPK(h).get(0)).getNombre()));
					} 
					catch (Exception e) {}

					break;
					
				} else if (beanAsociado instanceof PysServiciosSolicitadosBean) {
					PysServiciosSolicitadosBean beanFact = (PysServiciosSolicitadosBean) beanAsociado;
					PysServiciosSolicitadosAdm adm = new PysServiciosSolicitadosAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanFact);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanFact.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanFact.getIdInstitucion());
					beanHistorico.setIdPersona(beanFact.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_FACTURACION));			
					break;

				} else if (beanAsociado instanceof PysSuscripcionBean) {
					PysSuscripcionBean beanPysSuscripcion = (PysSuscripcionBean) beanAsociado;
					PysSuscripcionAdm admPysSuscripcion = new PysSuscripcionAdm(this.usrbean);
					hBeanAsociado = admPysSuscripcion.beanToHashTable(beanPysSuscripcion);
					hBeanAsociadoAnterior = admPysSuscripcion.beanToHashTable(admPysSuscripcion.hashTableToBean(beanPysSuscripcion.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanPysSuscripcion.getIdInstitucion());
					beanHistorico.setIdPersona(beanPysSuscripcion.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_FACTURACION));			
					break;
					
				} else if (beanAsociado instanceof PysCompraBean) {
					PysCompraBean beanPysCompra = (PysCompraBean) beanAsociado;
					PysCompraAdm admPysCompra = new PysCompraAdm(this.usrbean);
					hBeanAsociado = admPysCompra.beanToHashTable(beanPysCompra);
					hBeanAsociadoAnterior = admPysCompra.beanToHashTable(admPysCompra.hashTableToBean(beanPysCompra.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanPysCompra.getIdInstitucion());
					beanHistorico.setIdPersona(beanPysCompra.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_FACTURACION));			
					break;					
				}


			} while (false);

			String descripcion = "";
			switch (accion) {
				case ACCION_INSERT:	descripcion = UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroNuevo")     + "\n";	break;
				case ACCION_DELETE:	descripcion = UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroEliminado") + "\n";	break;
				case ACCION_UPDATE:	descripcion = UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroActual")    + "\n";	break;
			}
			descripcion += this.getDescripcionCalveValor(hBeanAsociado);
			
			if (accion == ACCION_UPDATE) {
				descripcion += "\n" + UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroAnterior") + "\n";
				descripcion += this.getDescripcionCalveValor(hBeanAsociadoAnterior);
			}
			
			//Hay que comprobar la longitud de la descripción que vamos a insertar
			//porque con la petición R1411_0046 se amplió el campo de la descripción de los datos del cv a 4000.
			int numCaract=descripcion.length();
			
			//Si estamos insertando más de 4000 caracteres hay que truncar el valor de "DESCRIPCION"
			if(numCaract > MAX_NUM_CARACTERES_DESCRIPCION)
			{
				descripcion="";
				
				descripcion=this.getDescripcionCorta(accion,hBeanAsociado,hBeanAsociadoAnterior,idioma,numCaract);
				
			}
			
			beanHistorico.setDescripcion(descripcion);
				
			if ((beanHistorico.getFechaEfectiva() == null) || (beanHistorico.getFechaEfectiva().equals(""))) 
				beanHistorico.setFechaEfectiva("SYSDATE");
			if ((beanHistorico.getFechaEntrada()  == null) || (beanHistorico.getFechaEntrada().equals(""))) 
				beanHistorico.setFechaEntrada ("SYSDATE");
			if (bDesdeCGAE)
				beanHistorico.setIdInstitucion(2000);
			//beanHistorico.setIdInstitucionCargo("");
			// Insertamos el historico
			if (this.insert(beanHistorico)) {
				return true;
			}
			return false;
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el \"insert\" en B.D.");
		}
	}
	
	public boolean auditoriaColegiados(Long idPersona, String motivo, int tipoCambio,Hashtable objectHashtable, Hashtable originalObjectHashtable,
			String [] claves, List<String> ocultarClaveList,Hashtable<String, String> cambiarNombreSalidaHashtable,int accion, String idioma, boolean isCGAE) throws ClsExceptions
	{
		try {
			
			
			CenHistoricoBean beanHistorico = new CenHistoricoBean();
			beanHistorico.setIdInstitucion(Integer.parseInt(this.usrbean.getLocation()));
			beanHistorico.setIdPersona(idPersona);
			beanHistorico.setMotivo(motivo);
			beanHistorico.setIdTipoCambio(tipoCambio);
			beanHistorico.setUsuMod(Integer.valueOf(this.usrbean.getUserName())); 
			beanHistorico.setDescripcion(getDescripcion(objectHashtable, originalObjectHashtable, claves, ocultarClaveList,cambiarNombreSalidaHashtable,accion, idioma));
			if ((beanHistorico.getFechaEfectiva() == null) || (beanHistorico.getFechaEfectiva().equals(""))) 
				beanHistorico.setFechaEfectiva("SYSDATE");
			if ((beanHistorico.getFechaEntrada()  == null) || (beanHistorico.getFechaEntrada().equals(""))) 
				beanHistorico.setFechaEntrada ("SYSDATE");
			if (isCGAE)
				beanHistorico.setIdInstitucion(2000);
//			beanHistorico.setIdInstitucionCargo("");
			// Insertamos el historico
			if (this.insert(beanHistorico)) {
				return true;
			}
			return false;
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el \"insert\" en B.D.");
		}
	}
	
	private String getDescripcion(Hashtable objectHashtable, Hashtable originalObjectHashtable, String[] claves, List<String> ocultarClaveList,Hashtable<String, String> cambiarNombreSalidaHashtable,int accion, String idioma) throws SIGAException{
		StringBuffer descripcion = new StringBuffer();
		switch (accion) {
			case ACCION_INSERT:	
				descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroNuevo"));
				descripcion.append("\n");
				break;
			case ACCION_DELETE:
				descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroEliminado"));
				descripcion.append("\n");
				break;
			case ACCION_UPDATE:	
				descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroActual"));
				descripcion.append("\n");
				break;
		}
		if(accion!=ACCION_DELETE)
			descripcion.append(getDescripcionClaveValor(objectHashtable, claves,ocultarClaveList,cambiarNombreSalidaHashtable,idioma));
		else
			descripcion.append(getDescripcionBorrado(objectHashtable,ocultarClaveList,cambiarNombreSalidaHashtable,idioma));
		if(accion!=ACCION_UPDATE && descripcion.length()>4000)
			throw new SIGAException("lA DESCRIPCION ES DEMASIADO LARGA. NO PUEDE LLEGAR HASTA AQUI");
		
		
		if (accion == ACCION_UPDATE) {
			descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroAnterior"));
			descripcion.append("\n");
			descripcion.append(getDescripcionClaveValor(originalObjectHashtable, claves,ocultarClaveList,cambiarNombreSalidaHashtable,idioma));
		}
		if(descripcion.length()>4000){
			return getDescripcionCorta(objectHashtable,originalObjectHashtable, claves,ocultarClaveList,cambiarNombreSalidaHashtable, accion, idioma, descripcion.length()-MAX_NUM_CARACTERES_DESCRIPCION);
		}else{
			return descripcion.toString();	
		}
		
		
		
	}
	/**
	 * @param objectHashtable
	 * @return
	 */
	private String getDescripcionBorrado(Hashtable objectHashtable, List<String> ocultarClaveList, Hashtable<String, String> cambiarNombreSalidaHashtable, String idioma) {
		StringBuffer descripcion = new StringBuffer();
		Map<String, Hashtable<String, Object>> fksDesignaMap = (Map<String, Hashtable<String, Object>>) objectHashtable.get("fks");
		if (fksDesignaMap != null)
			objectHashtable.remove("fks");

		Iterator ite = objectHashtable.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			if (!ocultarClaveList.contains(key)) {
				descripcion.append("  - ");
				if (cambiarNombreSalidaHashtable != null && cambiarNombreSalidaHashtable.containsKey(key))
					descripcion.append(UtilidadesString.getPrimeraMayuscula(cambiarNombreSalidaHashtable.get(key)));
				else
					descripcion.append(UtilidadesString.getPrimeraMayuscula(key));
				descripcion.append(": ");
				if (objectHashtable.get(key) instanceof String) {
					String elemento = (String) objectHashtable.get(key);

					if (fksDesignaMap != null && fksDesignaMap.containsKey(key)) {

						try {
							Vector vFK = this.getHashSQL(getQueryFK(fksDesignaMap.get(key), idioma));
							if (vFK != null && vFK.size() == 1) {
								Hashtable htFK = (Hashtable) vFK.get(0);
								Object claveAux = fksDesignaMap.get(key).get(key);
								if (claveAux != null) {
									descripcion.append(claveAux.toString());
									descripcion.append(" (");
									descripcion.append(htFK.get("SALIDA_FK").toString());
									descripcion.append(")");
									descripcion.append("\n");
								} else {
									descripcion.append(elemento);
									descripcion.append(" (");
									descripcion.append(htFK.get("SALIDA_FK").toString());
									descripcion.append(")");
									descripcion.append("\n");

								}
							} else {
								throw new ClsExceptions("Hay clave primaria de la FK mal configurada");
							}
						} catch (ClsExceptions e) {
							e.printStackTrace();
							descripcion.append(elemento);
							descripcion.append("\n");
						}
					} else {
						descripcion.append(elemento);
						descripcion.append("\n");
					}

				}

			}

		}

		return descripcion.toString();
	}

	private void recortarrCampoLargo(Hashtable objectHashtable, Hashtable originalObjectHashtable,String clave,int numCaracteresCortar){
		//Vamos a ver cuanto tenemos que acortar proporcianalmente al nuevo o al viejo
		String observaciones = (String)objectHashtable.get(clave);
		String observacionesOriginal = (String)originalObjectHashtable.get(clave);
		int observacionesLength = observaciones.length();
		int observacionesOriginalesLength = observacionesOriginal.length();
		int observacionesTotalLength = observacionesLength+observacionesOriginalesLength;
		//Miramos si hay alguno que sea mucho mas grande que el otro. en este caso solo recortaremos a este
		double  porcentaje = observacionesLength*100/observacionesTotalLength;
		double  porOriginal = 0;
//		si es mayor que 75 solo cortamos a este
		if(porcentaje>75){
			porcentaje = 100;
			
		}else if(porcentaje<=25){
			porOriginal = 100;
			porcentaje = 0;
			
		}else{
			porOriginal = 100-porcentaje;
		}
		if(porcentaje==0){
			StringBuffer observacionesCortadas = new StringBuffer();
			observacionesCortadas.append("...");
			observacionesCortadas.append(((String)originalObjectHashtable.get(clave)).substring(numCaracteresCortar+3));
			originalObjectHashtable.put(clave,observacionesCortadas);
		}else if(porOriginal==0){
			StringBuffer observacionesCortadas = new StringBuffer();
			observacionesCortadas.append("...");
			observacionesCortadas.append(((String)objectHashtable.get(clave)).substring(numCaracteresCortar+3));
			objectHashtable.put(clave,observacionesCortadas);
			
		}else{
			double caracteresRecortar = (numCaracteresCortar-6)*porOriginal/100;
			int enteroOriginal = (int)caracteresRecortar;
			int entero = numCaracteresCortar-enteroOriginal;
			StringBuffer observacionesCortadas = new StringBuffer();
			observacionesCortadas.append("...");
			observacionesCortadas.append(((String)originalObjectHashtable.get(clave)).substring(enteroOriginal+3));
			originalObjectHashtable.put(clave,observacionesCortadas);
			
			observacionesCortadas = new StringBuffer();
			observacionesCortadas.append("...");
			observacionesCortadas.append(((String)objectHashtable.get(clave)).substring(entero+3));
			objectHashtable.put(clave,observacionesCortadas);
			
		}
		
	}
	private String getDescripcionCorta(Hashtable objectHashtable, Hashtable originalObjectHashtable, String[] claves, List<String> ocultarClaveList,Hashtable<String, String> cambiarNombreSalidaHashtable, int accion, String idioma,int numCaracteresCortar){
		if(!objectHashtable.containsKey("DESCRIPCION") && !objectHashtable.containsKey("OBSERVACIONES"))
			throw new BusinessException("No tiene campo descripcion ni observaciones por lo que no se puede acortar");
		//Asumimos que solo tendra una
		List<String> clavesList = Arrays.asList (claves);
		if(clavesList.contains("OBSERVACIONES")){
			recortarrCampoLargo(objectHashtable, originalObjectHashtable, "OBSERVACIONES", numCaracteresCortar);
		}else if(clavesList.contains("DESCRIPCION")){
			recortarrCampoLargo(objectHashtable, originalObjectHashtable, "DESCRIPCION", numCaracteresCortar);
			
		}
		StringBuffer descripcion = new StringBuffer();
		switch (accion) {
			case ACCION_INSERT:	
				descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroNuevo"));
				descripcion.append("\n");
				break;
			case ACCION_DELETE:
				descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroEliminado"));
				descripcion.append("\n");
				break;
			case ACCION_UPDATE:	
				descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroActual"));
				descripcion.append("\n");
				break;
		}
		descripcion.append(getDescripcionClaveValor(objectHashtable, claves,ocultarClaveList,cambiarNombreSalidaHashtable, idioma)); 
		if (accion == ACCION_UPDATE) {
			descripcion.append(UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroAnterior"));
			descripcion.append("\n");
			descripcion.append(getDescripcionClaveValor(originalObjectHashtable, claves,ocultarClaveList,cambiarNombreSalidaHashtable, idioma));
		}
		return descripcion.toString();
	}
	
	private String getDescripcionCorta(int accion, Hashtable hBeanAsociado, Hashtable hBeanAsociadoAnterior,String idioma, int numCaract) throws Exception {
		try{
			
			String descripcion="";
			
			//Obtenemos el número de caracteres a acortar
			int numCaractElim=numCaract-MAX_NUM_CARACTERES_DESCRIPCION;
			int numCaractElimUpdate=0;
			
			switch (accion) {
				case ACCION_INSERT:	descripcion = UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroNuevo")     + "\n";	break;
				case ACCION_DELETE:	descripcion = UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroEliminado") + "\n";	break;
				case ACCION_UPDATE:	descripcion = UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroActual")    + "\n";	break;
			}

			Vector v = new Vector();
			Enumeration e = hBeanAsociado.keys();
			while (e.hasMoreElements()) {
				String clave = (String) e.nextElement();
				
				if (this.omitirClave(clave))
					continue;
				
				String valor = "";
				try {
					// Si es fecha, la presentamos en español
					valor = GstDate.getFormatedDateShort("", (String)hBeanAsociado.get(clave));
				}
				catch (Exception exp) {
					valor = (String)hBeanAsociado.get(clave);
					if (valor.trim().equalsIgnoreCase("SYSDATE")) {
						valor = UtilidadesBDAdm.getFechaBD("");
					}
				}
				
				clave = this.reemplazarClave(clave);				
				String t="";
				//Acortamos el valor de la clave "DESCRIPCION" 
				if((clave.equals(CenDatosCVBean.C_DESCRIPCION))||(clave.equals(ExpExpedienteBean.C_OBSERVACIONES))){
				
					//Si estamos haciendo una actualización hay que calcular el número de caracteres a recortar 
					//de la DESCRIPCION de cada hash (actual y anterior)
					if (accion == ACCION_UPDATE) {
						
						if ((numCaractElim%2) == 0) {
							numCaractElimUpdate=(numCaractElim/2)+3;
						}else{
							numCaractElimUpdate=((numCaractElim-1)/2)+3;
						}
						
						t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": ..." + valor.substring(numCaractElimUpdate) + "\n";
						
					}else{
						t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": ..." + valor.substring(numCaractElim+3) + "\n";
					}
					
					
				
				}else{
					if(valor.equals("null"))
						valor="";
					
					t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": " + valor + "\n";
				}

				int i;
				for (i = 0; i < v.size() && ((String)v.get(i)).compareToIgnoreCase(t) < 0; i++);
					v.add(i,t);
			}
			String descripcion_aux = "";
			for (int i = 0; i < v.size(); i++) 
				descripcion_aux += v.get(i);

			descripcion +=descripcion_aux;

			if (accion == ACCION_UPDATE) {
				descripcion += "\n" + UtilidadesString.getMensajeIdioma(idioma, "historico.literal.registroAnterior") + "\n";
	
				Vector vu = new Vector();
				Enumeration eu = hBeanAsociadoAnterior.keys();
				while (eu.hasMoreElements()) {
					String clave = (String) eu.nextElement();
					
					if (this.omitirClave(clave))
						continue;
					
					String valor = "";
					try {
						// Si es fecha, la presentamos en español
						valor = GstDate.getFormatedDateShort("", (String)hBeanAsociadoAnterior.get(clave));
					}
					catch (Exception exp) {
						valor = (String)hBeanAsociadoAnterior.get(clave);
						if (valor.trim().equalsIgnoreCase("SYSDATE")) {
							valor = UtilidadesBDAdm.getFechaBD("");
						}
					}
					
					clave = this.reemplazarClave(clave);				
					String t="";
					//Acortamos el valor de la clave "DESCRIPCION" 
					if((clave.equals(CenDatosCVBean.C_DESCRIPCION))||(clave.equals(ExpExpedienteBean.C_OBSERVACIONES))){

						if ((numCaractElim%2) == 0) {
							t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": ..." + valor.substring(numCaractElimUpdate) + "\n";
						}else{
							//Le sumo un caracter más porque en la descripción antigua le resté
							t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": ..." + valor.substring(numCaractElimUpdate+1) + "\n";
						}

					}else{
						if(valor.equals("null"))
							valor="";
						
						t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": " + valor + "\n";
					}
					
					int i;
					for (i = 0; i < vu.size() && ((String)vu.get(i)).compareToIgnoreCase(t) < 0; i++);
						vu.add(i,t);
				}
				String descripcion_aux2 = "";
				for (int i = 0; i < vu.size(); i++) 
					descripcion_aux2 += vu.get(i);
	
				descripcion +=descripcion_aux2;

			}
			
			return descripcion;
		
		}catch (Exception e)	{
			throw new Exception (e);
		}
	}

	private String getDescripcionCalveValor (Hashtable h) 
	{
		try {
			Vector v = new Vector();
			Enumeration e = h.keys();
			while (e.hasMoreElements()) {
				String clave = (String) e.nextElement();
				
				if (this.omitirClave(clave))
					continue;
				
				String valor = "";
				try {
					// Si es fecha, la presentamos en español
					valor = GstDate.getFormatedDateShort("", (String)h.get(clave));
				}
				catch (Exception exp) {
					valor = (String)h.get(clave);
					if (valor.trim().equalsIgnoreCase("SYSDATE")) {
						valor = UtilidadesBDAdm.getFechaBD("");
					}
				}
				
				clave = this.reemplazarClave(clave);				
				String t = "  - " + clave.substring(0,1).toUpperCase() +  clave.substring(1).toLowerCase() + ": " + valor + "\n";
				
				int i;
				for (i = 0; i < v.size() && ((String)v.get(i)).compareToIgnoreCase(t) < 0; i++);
				v.add(i,t);
			}
			String descripcion = "";
			for (int i = 0; i < v.size(); i++) 
				descripcion += v.get(i);
				
			return descripcion;
		}
		catch (Exception e) {
			return "";
		}
	}
	
	
	private String getDescripcionClaveValor (Hashtable hashtable,String[] claves,List<String> ocultarClaveList ,Hashtable<String,String> cambiarNombreSalidaHashtable,String idioma) 
	{
		StringBuffer descripcion =  new StringBuffer();
		Map<String,Hashtable<String, Object>> fksDesignaMap = (Map<String, Hashtable<String, Object>>) hashtable.get("fks");
		for (int i = 0; i < claves.length  ; i++) {
			
			if(!ocultarClaveList.contains(claves[i])){
				String clave = claves[i];
				descripcion.append("  - ");
				if(cambiarNombreSalidaHashtable!=null && cambiarNombreSalidaHashtable.containsKey(clave))
					descripcion.append(UtilidadesString.getPrimeraMayuscula(cambiarNombreSalidaHashtable.get(clave)));
				else
					descripcion.append(UtilidadesString.getPrimeraMayuscula(clave));
				descripcion.append(": ");
				Object valor = hashtable.get(clave); 
				if(valor!=null){
					if(valor instanceof Date){
						try {
							descripcion.append(GstDate.getFormatedDateShort((Date)hashtable.get(clave)));
						} catch (ClsExceptions e) {
							e.printStackTrace();
							descripcion.append("");
						}
						
					}else{
						if(fksDesignaMap!=null && fksDesignaMap.containsKey(clave)){
							
							try {
								Vector vFK =  this.getHashSQL(getQueryFK(fksDesignaMap.get(clave), idioma));
								if(vFK!=null && vFK.size()==1){
									Hashtable htFK = (Hashtable)vFK.get(0);
									Object claveAux = fksDesignaMap.get(clave).get(clave);
									if(claveAux!=null){
										descripcion.append(claveAux.toString());
										descripcion.append(" (");
										descripcion.append(htFK.get("SALIDA_FK").toString());
										descripcion.append(")");
									}else{
										descripcion.append(valor.toString());
										descripcion.append(" (");
										descripcion.append(htFK.get("SALIDA_FK").toString());
										descripcion.append(")");
										
									}
								}else{
									throw new ClsExceptions("Hay clave primaria de la FK mal configurada");
								}
							} catch (ClsExceptions e) {
								e.printStackTrace();
								descripcion.append(hashtable.get(clave).toString());
							}	
						}else{
							descripcion.append(hashtable.get(clave).toString());	
						}
						
					}
				}else
					descripcion.append("");
				descripcion.append("\n");
			}
			
		}
		return descripcion.toString();
		
	}
	private String getQueryFK(Hashtable<String, Object> hashtable,String idioma){
		String tabla = (String)hashtable.get("TABLA_FK");
		String salida = (String)hashtable.get("SALIDA_FK");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("F_SIGA_GETRECURSO(");
		sql.append(salida);
		sql.append(",");
		sql.append(idioma);
		sql.append(") SALIDA_FK ");
		sql.append(" FROM ");
		sql.append(tabla);
		sql.append(" WHERE ");
		Iterator<String> iteratorWhere = hashtable.keySet().iterator();
		while (iteratorWhere.hasNext()) {
			String key = (String) iteratorWhere.next();
			if(!key.equals("TABLA_FK") && !key.equals("SALIDA_FK")){
				sql.append(key);
				sql.append("=");
				sql.append(hashtable.get(key));
				sql.append(" AND ");
			}
			
		}
		return sql.substring(0,sql.lastIndexOf("AND"));
		
//		return sql.toString();
		
	}
	
	private boolean omitirClave (String s) 
	{
		String [] camposOmitir = {"IDPERSONA", "IDINSTITUCION", "FECHABAJA", MasterBean.C_FECHAMODIFICACION, MasterBean.C_USUMODIFICACION, 
								  CenDireccionesBean.C_IDDIRECCION,  CenCuentasBancariasBean.C_IDCUENTA, CenDatosCVBean.C_IDCV, 
								  ExpExpedienteBean.C_IDINSTITUCION_JUZGADO, ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
								  CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION,
								  CenComponentesBean.C_IDCARGO};

		for (int k = 0; k < camposOmitir.length; k++) {
			if (s.equalsIgnoreCase(camposOmitir[k])) {
				return true;

			}
		}
		return false;
	}
	
	private String reemplazarClave (String s) 
	{
		// Pareja: original --> final
		String [][] camposReemplazar = { { CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, "PERSONA" },
										 { CenComponentesBean.C_IDTIPOCOLEGIO, "TIPO COLEGIO" },
										 { CenDireccionesBean.C_IDPAIS, "PAIS" },
										 { CenDireccionesBean.C_IDPOBLACION, "POBLACION" },
										 { CenDireccionesBean.C_IDPROVINCIA, "PROVINCIA" },
									   };  

		for (int k = 0; k < camposReemplazar.length; k++) {
			if (s.equalsIgnoreCase(camposReemplazar[k][0])) {
				return camposReemplazar[k][1];
			}
		}
		return s;
	}
}
