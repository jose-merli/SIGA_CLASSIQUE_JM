/*
 * VERSIONES:
 * 
 * miguel.villegas - 20-12-2004 - Funciones de accesos a BBDDs y relacionadas con el historico
 * daniel.campos - 12-01-2005 - Incorpora metodos insert y getNuevoId
 *	
 */
package com.siga.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.administracion.form.AuditoriaUsuariosForm;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class CenHistoricoAdm extends MasterBeanAdministrador 
{
	
	public static final int  ACCION_INSERT = 1,
							 ACCION_UPDATE = 2,
						     ACCION_DELETE = 3;

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
			bean.setFechaMod(UtilidadesHash.getString(hash,CenHistoricoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenHistoricoBean.C_USUMODIFICACION));			
		}
		catch (Exception e) { 
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
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenHistoricoBean b = (CenHistoricoBean) bean;
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDPERSONA,b.getIdPersona ());
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDHISTORICO, b.getIdHistorico());
			UtilidadesHash.set(htData,CenHistoricoBean.C_FECHAENTRADA,b.getFechaEntrada());
			UtilidadesHash.set(htData,CenHistoricoBean.C_FECHAEFECTIVA,b.getFechaEfectiva());
			UtilidadesHash.set(htData,CenHistoricoBean.C_MOTIVO,b.getMotivo());
			UtilidadesHash.set(htData,CenHistoricoBean.C_IDTIPOCAMBIO ,b.getIdTipoCambio());
			UtilidadesHash.set(htData,CenHistoricoBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenHistoricoBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,CenHistoricoBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
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
	public Integer getNuevoID (Hashtable entrada) throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		

			String sql;
			sql ="SELECT (MAX(IDHISTORICO) + 1) AS IDHISTORICO FROM " + nombreTabla +
				" WHERE IDPERSONA =" + (String)entrada.get("IDPERSONA") +
				" AND IDINSTITUCION =" + (String)entrada.get("IDINSTITUCION");
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDHISTORICO").equals("")) {
					return new Integer(1);
				}
				else return UtilidadesHash.getInteger(prueba, "IDHISTORICO");								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
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
	public Integer getNuevoID (CenHistoricoBean bean) throws ClsExceptions,SIGAException {
		try {		
			Hashtable hash = beanToHashTable (bean);
			Integer idHistorico = getNuevoID(hash);
			return idHistorico;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert(bean)' en B.D.");		
		}
	}

	
	/** 
	 * Adecua los formatos de las fechas para la insercion en BBDD. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable prepararFormatosFechas (Hashtable entrada)throws ClsExceptions,SIGAException 
	{
		String fechaEntrada,fechaEfectiva;
		String sql;
		RowsContainer rc = null;
		int contador = 0;
				
		try {		
			fechaEntrada=GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAENTRADA"));
			fechaEfectiva=GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAEFECTIVA"));
			entrada.put("FECHAENTRADA",fechaEntrada);
			entrada.put("FECHAEFECTIVA",fechaEfectiva);			
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al adecuar los formatos delas fechas.");		
		}
		return entrada;
	}
	
	/** 
	 * Adecua los formatos para la insercion en BBDD. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable prepararFormatos (Hashtable entrada)throws ClsExceptions,SIGAException
	{
		String fechaEntrada,fechaEfectiva;
		String idPersona,idInstitucion,idTipoCambio;		
		String sql;
		RowsContainer rc = null;
		int contador = 0;
				
		try {		
			fechaEntrada=GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAENTRADA"));
			fechaEfectiva=GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAEFECTIVA"));
			entrada.put("FECHAENTRADA",fechaEntrada);
			entrada.put("FECHAEFECTIVA",fechaEfectiva);			
//			idPersona=((Long)entrada.get("IDPERSONA")).toString();
//			idInstitucion=((Integer)entrada.get("IDINSTITUCION")).toString();			
//			idTipoCambio=((Integer)entrada.get("IDTIPOCAMBIO")).toString();			
//			idPersona=((String)entrada.get("IDPERSONA"));
//			idInstitucion=((String)entrada.get("IDINSTITUCION"));			
//			idTipoCambio=((String)entrada.get("IDTIPOCAMBIO"));			
//			entrada.put("IDPERSONA",idPersona);
//			entrada.put("IDINSTITUCION",idInstitucion);			
//			entrada.put("IDTIPOCAMBIO",idTipoCambio);			
		}	
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al adecuar los formatos.");		
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
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getHistorico(String idPersona, String idInstitucion, String tipoCambio, String fechaInicio, String fechaFin) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
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
						  CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDTIPOCAMBIO + "=" + tipoCambio;									 
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
				
				/* RGG cambio de funcion para fechas desde hasta
				if (!fechaInicio.trim().equals("")){								 
					sql +=" AND " +
						  CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_FECHAEFECTIVA + ">= TO_DATE ('" + fechaInicio + "', 'DD/MM/YYYY')";
				}							

				if (!fechaFin.trim().equals("")){								 
					sql +=" AND (" +
					  	  CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_FECHAEFECTIVA + "<= TO_DATE ('" + fechaFin + "', 'DD/MM/YYYY')" +									 
						  " OR " +
						  GstDate.dateBetween0and24h(CenHistoricoBean.C_FECHAEFECTIVA,fechaFin)+")";					
				}							
				*/
				
				sql += " ORDER BY " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA + " DESC, " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO ; 										
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
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
	            			CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + ", " +
	            			" (SELECT " + AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_DESCRIPCION + " FROM " + AdmUsuariosBean.T_NOMBRETABLA +
	            			" WHERE "+ AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_IDUSUARIO + " = " + CenHistoricoBean.T_NOMBRETABLA  + "." + CenHistoricoBean.C_USUMODIFICACION+
							" AND "+CenHistoricoBean.T_NOMBRETABLA +"."+ CenHistoricoBean.C_IDINSTITUCION+" = "+AdmUsuariosBean.T_NOMBRETABLA+"."+AdmUsuariosBean.C_IDINSTITUCION + ") " + 
	            			" AS NOMBRE_USU_MOD"+
							" FROM " + CenHistoricoBean.T_NOMBRETABLA + ", " + AdmUsuariosBean.T_NOMBRETABLA + 
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
//			catch (SIGAException e) {
//				throw e;
//			}
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
	public boolean insertarRegistroHistorico(CenHistoricoBean registro, UsrBean usuario)throws ClsExceptions,SIGAException
	{
		try{ 
			Hashtable hash=null;
			hash=this.beanToHashTable(registro);
			// Adecuo formatos
			this.prepararFormatos(hash);

			// Inserto en CEN_HISTORICO 
			return this.insert(hash); 			
		}	
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception ee){
			throw new ClsExceptions(ee,"Ha fallado el proceso de insercion en el historico");				
		}
	}	
	
	/** Funcion insert (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el insert 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insert(Hashtable hash) throws ClsExceptions{
		try
		{
			UtilidadesHash.set(hash, CenHistoricoBean.C_IDHISTORICO, getNuevoID(hash));
			return super.insert(hash);
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el 'insert' en B.D");
		}
	}
	
	public Paginador getAuditoriaUsuariosConPaginador (Integer idInstitucion, AuditoriaUsuariosForm datos) throws ClsExceptions,SIGAException 
	{
	   Vector out = new Vector();
       try {
            RowsContainer rc = new RowsContainer(); 
            String sql =" SELECT " + CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDPERSONA  + "," +
									 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_USUMODIFICACION + ", " +
									 AdmUsuariosBean.T_NOMBRETABLA + "." + AdmUsuariosBean.C_DESCRIPCION + " AS NOMBRE_USUARIO, " + 
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDINSTITUCION + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDHISTORICO  + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAENTRADA  + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_FECHAEFECTIVA + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_MOTIVO + "," +
			            			 CenHistoricoBean.T_NOMBRETABLA + "." + CenHistoricoBean.C_IDTIPOCAMBIO + ", " +
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
	   Vector out = new Vector();
       try {
            RowsContainer rc = new RowsContainer(); 
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
	
	public boolean insertCompleto(Hashtable hHistorico, Hashtable hBeanAsociado, Hashtable hBeanAsociadoOriginal, String nombreClaseBean, int accion, String idioma) throws ClsExceptions
	{
		try {
			MasterBean beanAsociado = null;
			MasterBeanAdministrador adm = null;
			
			do {
				if (nombreClaseBean.equalsIgnoreCase("CenClienteBean")) {
					adm = new CenClienteAdm(this.usrbean);
					break;
				}
				
				if (nombreClaseBean.equalsIgnoreCase("CenPersonaBean")) {
					adm = new CenPersonaAdm(this.usrbean);
					break;
				}
				
				if (nombreClaseBean.equalsIgnoreCase("PysServiciosSolicitadosBean")) {
					adm = new PysServiciosSolicitadosAdm(this.usrbean);
					break;
				}

				return false;
				
			} while (false);

			beanAsociado = adm.hashTableToBean(hBeanAsociado);
			beanAsociado.setOriginalHash(hBeanAsociadoOriginal);
			
			return this.insertCompleto(hHistorico, beanAsociado, accion, idioma);
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean insertCompleto(Hashtable hHistorico, MasterBean beanAsociado, int accion, String idioma) throws ClsExceptions
	{
		return this.insertCompleto((CenHistoricoBean)this.hashTableToBean(hHistorico), beanAsociado, accion, idioma);
	}

	public boolean insertCompleto(CenHistoricoBean beanHistorico, MasterBean beanAsociado, int accion, String idioma) throws ClsExceptions
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
				}				
				
				if (beanAsociado instanceof CenClienteBean) {
					CenClienteBean beanCliente = (CenClienteBean) beanAsociado;
					CenClienteAdm adm = new CenClienteAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCliente);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCliente.getOriginalHash()));
					
					beanHistorico.setIdInstitucion(beanCliente.getIdInstitucion());
					beanHistorico.setIdPersona(beanCliente.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_GENERALES));
					break;
				}

				if (beanAsociado instanceof CenColegiadoBean) {
					CenColegiadoBean beanCliente = (CenColegiadoBean) beanAsociado;
					CenColegiadoAdm adm = new CenColegiadoAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCliente);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCliente.getOriginalHash()));

					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES));			
					break;
				}
			
				if (beanAsociado instanceof CenComponentesBean) {
					CenComponentesBean beanComponentes = (CenComponentesBean) beanAsociado;
					CenComponentesAdm adm = new CenComponentesAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanComponentes);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanComponentes.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanComponentes.getIdInstitucion());
					beanHistorico.setIdPersona(beanComponentes.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COMPONENTES));
					
					// Sustituimos id's por descripciones
					try {
						CenPersonaAdm pAdm = new CenPersonaAdm(this.usrbean);
						CenPersonaBean pBean = (CenPersonaBean)pAdm.selectByPK(hBeanAsociado).get(0);
						UtilidadesHash.set(hBeanAsociado, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, pBean.getApellido1() + " "+ pBean.getApellido2() + ", "+ pBean.getNombre());
						pBean = (CenPersonaBean)pAdm.selectByPK(hBeanAsociadoAnterior).get(0);
						UtilidadesHash.set(hBeanAsociadoAnterior, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, pBean.getApellido1() + " "+ pBean.getApellido2() + ", "+ pBean.getNombre());
					} 
					catch (Exception e) {}
					
					break;
				}
				
				if (beanAsociado instanceof CenCuentasBancariasBean) {
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
					} 
					catch (Exception e) {}

					break;
				}
				
				if (beanAsociado instanceof CenDatosColegialesEstadoBean) {
					CenDatosColegialesEstadoBean beanDatosColegiales = (CenDatosColegialesEstadoBean) beanAsociado;
					CenDatosColegialesEstadoAdm adm = new CenDatosColegialesEstadoAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanDatosColegiales);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanDatosColegiales.getOriginalHash()));
					
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
				}

				if (beanAsociado instanceof CenDatosCVBean) {
					CenDatosCVBean beanCV = (CenDatosCVBean) beanAsociado;
					CenDatosCVAdm adm = new CenDatosCVAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanCV);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanCV.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanCV.getIdInstitucion());
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
				}

				if (beanAsociado instanceof CenDireccionesBean) {
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
				}
				
				if (beanAsociado instanceof CenDireccionTipoDireccionBean) {
					CenDireccionTipoDireccionBean beanTipoDir = (CenDireccionTipoDireccionBean) beanAsociado;
					CenDireccionTipoDireccionAdm adm = new CenDireccionTipoDireccionAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanTipoDir);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanTipoDir.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanTipoDir.getIdInstitucion());
					beanHistorico.setIdPersona(beanTipoDir.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DIRECCIONES));
					break;
				}
				
				if (beanAsociado instanceof CenGruposClienteClienteBean) {
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
						UtilidadesHash.set (hDatos, "GRUPOS FIJOS", ((CenGruposClienteBean)adm.selectByPK(h).get(0)).getNombre());
						hBeanAsociado = (Hashtable)hDatos.clone();
					}
					catch (Exception e) {}
					
					break;
				}
				if (beanAsociado instanceof CenNoColegiadoActividadBean ) {
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
						UtilidadesHash.set(h , CenActividadProfesionalBean.C_IDINSTITUCION, beanGrupo.getIdInstitucionActividad());

						Hashtable hDatos = new Hashtable();
						UtilidadesHash.set (hDatos, "GRUPOS FIJOS", ((CenActividadProfesionalBean)adm.selectByPK(h).get(0)).getDescripcion());
						hBeanAsociado = (Hashtable)hDatos.clone();
					}
					catch (Exception e) {}
					
					break;
				}


				if (beanAsociado instanceof CenPersonaBean) {
					CenPersonaBean beanPersona = (CenPersonaBean) beanAsociado;
					CenPersonaAdm adm = new CenPersonaAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanPersona);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanPersona.getOriginalHash()));

					//beanHis.setIdInstitucion(beanPersona.getIdInstitucion());
					beanHistorico.setIdPersona(beanPersona.getIdPersona());
					beanHistorico.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_COLEGIAL));			
					break;
				}

				if (beanAsociado instanceof ExpExpedienteBean) {
					ExpExpedienteBean beanExp = (ExpExpedienteBean) beanAsociado;
					ExpExpedienteAdm adm = new ExpExpedienteAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanExp);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanExp.getOriginalHash()));
					
					beanHistorico.setIdInstitucion(beanExp.getIdInstitucion());										
					beanHistorico.setIdPersona(beanExp.getIdPersona());												
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
				}

				if (beanAsociado instanceof PysServiciosSolicitadosBean) {
					PysServiciosSolicitadosBean beanFact = (PysServiciosSolicitadosBean) beanAsociado;
					PysServiciosSolicitadosAdm adm = new PysServiciosSolicitadosAdm(this.usrbean);
					hBeanAsociado = adm.beanToHashTable(beanFact);
					hBeanAsociadoAnterior = adm.beanToHashTable(adm.hashTableToBean(beanFact.getOriginalHash()));

					beanHistorico.setIdInstitucion(beanFact.getIdInstitucion());
					beanHistorico.setIdPersona(beanFact.getIdPersona());
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
			
			beanHistorico.setDescripcion(descripcion);
			if ((beanHistorico.getFechaEfectiva() == null) || (beanHistorico.getFechaEfectiva().equals(""))) 
				beanHistorico.setFechaEfectiva("SYSDATE");
			if ((beanHistorico.getFechaEntrada()  == null) || (beanHistorico.getFechaEntrada().equals(""))) 
				beanHistorico.setFechaEntrada ("SYSDATE");
		
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
	
	private boolean omitirClave (String s) 
	{
		String [] camposOmitir = {"IDPERSONA", "IDINSTITUCION", "FECHABAJA", MasterBean.C_FECHAMODIFICACION, MasterBean.C_USUMODIFICACION, 
								  CenDireccionesBean.C_IDDIRECCION,  CenCuentasBancariasBean.C_IDCUENTA, CenDatosCVBean.C_IDCV, 
								  ExpExpedienteBean.C_IDINSTITUCION_JUZGADO, ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO, ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
								  CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION, CenComponentesBean.C_IDCOMPONENTE};

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
