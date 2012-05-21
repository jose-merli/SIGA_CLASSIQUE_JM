//VERSIONES:
//ruben.fernandez 04-04-2005 creacion 

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;


public class FcsAplicaMovimientosVariosAdm extends MasterBeanAdministrador {

	
	public FcsAplicaMovimientosVariosAdm(UsrBean usuario) {
		super(FcsAplicaMovimientosVariosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsAplicaMovimientosVariosBean.C_FECHAMODIFICACION,	FcsAplicaMovimientosVariosBean.C_IDAPLICACION,
							FcsAplicaMovimientosVariosBean.C_IDINSTITUCION,		FcsAplicaMovimientosVariosBean.C_IDPAGOSJG,
							FcsAplicaMovimientosVariosBean.C_IDPERSONA,			FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO,
							FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO,   FcsAplicaMovimientosVariosBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsAplicaMovimientosVariosBean.C_IDINSTITUCION,		FcsAplicaMovimientosVariosBean.C_IDAPLICACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsAplicaMovimientosVariosBean bean = null;
		
		try {
			bean = new FcsAplicaMovimientosVariosBean();
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsAplicaMovimientosVariosBean.C_FECHAMODIFICACION));
			bean.setIdAplicacion	(UtilidadesHash.getInteger(hash,FcsAplicaMovimientosVariosBean.C_IDAPLICACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsAplicaMovimientosVariosBean.C_IDINSTITUCION));
			bean.setIdPagosJG		(UtilidadesHash.getInteger(hash,FcsAplicaMovimientosVariosBean.C_IDPAGOSJG));
			bean.setIdPersona		(UtilidadesHash.getInteger(hash,FcsAplicaMovimientosVariosBean.C_IDPERSONA));
			bean.setIdMovimiewnto	(UtilidadesHash.getInteger(hash,FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO));
			bean.setImporteAplicado	(UtilidadesHash.getDouble(hash,FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO));
			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsAplicaMovimientosVariosBean b = (FcsAplicaMovimientosVariosBean) bean;
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_IDAPLICACION, b.getIdAplicacion().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_IDPAGOSJG, b.getIdPagosJG().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO, b.getIdMovimiento().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO, b.getImporteAplicado().toString());
			UtilidadesHash.set(htData, FcsAplicaMovimientosVariosBean.C_USUMODIFICACION, b.getUsuMod().toString());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsCobrosRetencionJudicialAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}

	/**
	 * Calcula un nuevo idAplicacion para la tabla FCS_APLICA_MOVIMIENTOSVARIOS
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public String getNuevoId() throws ClsExceptions{
		String id = null;
		
		String consulta = " SELECT (MAX(" + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + ") + 1) AS "+ FcsAplicaMovimientosVariosBean.C_IDAPLICACION + 
						  " FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA;
		
		Vector v = this.selectGenerico(consulta);
		if (v==null || v.size()==0 || (((String)((Hashtable)v.get(0)).get(FcsAplicaMovimientosVariosBean.C_IDAPLICACION)).equals("")))
			id = "1";
		else
			id = (String)((Hashtable)v.get(0)).get(FcsAplicaMovimientosVariosBean.C_IDAPLICACION);
		return id;
	}	
	
	/**
	 * Devuelve el valor del importe que hay que pagar para un colegiado en un pago determinado
	 *   
	 * @param String idInstitucion 
	 * @param String idPago
	 * @param String idPersona
	 * 
	 * @return Hashtable resultado con el importe 
	 */
	public Hashtable getImporteAplicado (String idInstitucion, String idPago, String idPersona)
	{
		Hashtable resultado = new Hashtable();
		String resultado1 = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsAplicaMovimientosVariosBean.C_IDPERSONA + "=" + idPersona + " ";
		
		//Hashtable para recoger el resultado de la contulta
		Hashtable hash = new Hashtable();
		try{
			hash = (Hashtable)((Vector)this.selectGenerico(consulta)).get(0);
			//recogemos el resultado
			resultado1 = (String)hash.get("IMPORTE");
			if (resultado1.equals(""))resultado1="0";
		}catch(Exception e){
			//si no se ha obtenido resultado es porque no hay nada que pagar para el colegiado con ese idPersona
			resultado1 = "0";
		}
		resultado.put("IMPORTE",resultado1);
		return resultado;
	}
	
	/**
	 * Devuelve un vector con los movimientos que hay que pagar a una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getMovimientos (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT a." + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + " " + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO +
							", m." + FcsMovimientosVariosBean.C_CANTIDAD + " " + FcsMovimientosVariosBean.C_CANTIDAD + 
							", m." + FcsMovimientosVariosBean.C_IDINSTITUCION + " " + FcsMovimientosVariosBean.C_IDINSTITUCION + 
							", m." + FcsMovimientosVariosBean.C_IDPERSONA + " " + FcsMovimientosVariosBean.C_IDPERSONA + 
							", m." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + 
							" from " + 
							FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + " a,"+
							FcsMovimientosVariosBean.T_NOMBRETABLA + " m"+
							" where a."+FcsAplicaMovimientosVariosBean.C_IDINSTITUCION+" = "+ idInstitucion +
							" and a." + FcsAplicaMovimientosVariosBean.C_IDPERSONA + " = "+ idPersona +
							" and a." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = "+ idPago +
							//JOIN
							" and m." + FcsMovimientosVariosBean.C_IDINSTITUCION + " = a." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION +
							" and m." + FcsMovimientosVariosBean.C_IDPERSONA + " = a." + FcsAplicaMovimientosVariosBean.C_IDPERSONA +
							" and m." + FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = a." + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO;

							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsAplicaMovimientosVariosAdm.getMovimientos:"+consulta);
		}
		
		return resultado;
		
	}
	
	/**
	 * Devuelve los identificadores de aplicación de aquellos movimientos aplicados que tengan el identificador de institución pasado
	 * como parámetro y el identificador del pago también pasado como parámetro.
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getMovimientoAplicado(String idInstitucion, String idPago) throws ClsExceptions{

		Vector resultado = new Vector();
		
		String consulta = " SELECT " + FcsAplicaMovimientosVariosBean.C_IDAPLICACION + " AS "+ FcsAplicaMovimientosVariosBean.C_IDAPLICACION + 
						  " FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
						  " WHERE " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " = " + idInstitucion +
						  " AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + idPago; 
		
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsAplicaMovimientosVariosAdm.getMovimientoAplicado:"+consulta);
		}
		
		return resultado;
	}	
	
	/**
	 * Eliminar el movimiento aplicado definido por el identificador de institucion y el identificador de pago pasado por parámetro
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 * @throws ClsExceptions
	 */
	public void deleteMovimientoaplicado(String idInstitucion, String idPago) throws ClsExceptions{

		Vector resultado = new Vector();
		
		String consulta = " DELETE FROM " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +  
						  " WHERE " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDINSTITUCION + " = " + idInstitucion +
						  " AND " + FcsAplicaMovimientosVariosBean.T_NOMBRETABLA + "." + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + idPago; 
		
		try{
			ClsMngBBDD.executeUpdate (consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsAplicaMovimientosVariosAdm.deleteMovimientoAplicado:"+consulta);
		}
		
	}	
		
	
/*	private String getQueryRetencionesAplicadas(Integer idInstitucion, String letrado, String fechaDesde, 
			String fechaHasta, String idTipoRetencion, String idDestinatario, String pagoRelacionado,
			String abonoRelacionado,UsrBean usrBean,Hashtable codigos){
		StringBuffer sql = new StringBuffer();
		int contador = 0;
		
		sql.append(" SELECT RET.TIPORETENCION, ");
		sql.append(" f_siga_calculoncolegiado(COB.IDINSTITUCION,COB.IDPERSONA) NCOLEGIADO, ");
		sql.append(" PER.NOMBRE,PER.APELLIDOS1,PER.APELLIDOS2, ");
		sql.append(" RET.IDDESTINATARIO,dest.nombre NOMBREDESTINATARIO,RET.DESCDESTINATARIO,RET.FECHAINICIO, " );
		sql.append(" RET.FECHAFIN, ");
		sql.append(" COB.FECHARETENCION,COB.IMPORTERETENIDO,COB.IDPAGOSJG, ");
		sql.append(" COB.MES,COB.ANIO, ");
		sql.append(" PAGO.NOMBRE PAGORELACIONADO, ");
		sql.append(" ABONO.NUMEROABONO ABONORELACIONADO ");
		sql.append(" ,COB.IDCOBRO,COB.IDRETENCION, COB.IDINSTITUCION,    COB.IDPERSONA ");
		sql.append(" , TO_CHAR(PAGO.FECHADESDE,'dd/mm/yyyy') FECHADESDE,TO_CHAR(PAGO.FECHAHASTA,'dd/mm/yyyy') FECHAHASTA ");
		sql.append(" FROM FCS_COBROS_RETENCIONJUDICIAL COB, FCS_RETENCIONES_JUDICIALES RET, fcs_destinatarios_retenciones dest, ");
		sql.append(" Fcs_Pago_Colegiado PCOL, FAC_ABONO ABONO, FCS_PAGOSJG PAGO, CEN_PERSONA PER ");
		sql.append(" WHERE  ");
		sql.append(" COB.IDINSTITUCION = RET.IDINSTITUCION ");
		sql.append(" AND COB.IDRETENCION = RET.IDRETENCION ");
		sql.append(" AND RET.idinstitucion = DEST.idinstitucion ");
		sql.append(" AND RET.iddestinatario = DEST.iddestinatario ");
		sql.append(" AND COB.IDINSTITUCION = PCOL.Idinstitucion ");
		sql.append(" AND COB.Idpagosjg = PCOL.IDPAGOSJG  ");
		sql.append(" AND COB.Idpersona = PCOL.IDPERORIGEN ");
		sql.append(" AND PCOL.IDPERDESTINO = ABONO.IDPERSONA ");
		sql.append(" AND PCOL.IDINSTITUCION = ABONO.IDINSTITUCION ");
		sql.append(" AND PCOL.IDPAGOSJG = ABONO.IDPAGOSJG ");
		sql.append(" AND PCOL.IDINSTITUCION = PAGO.IDINSTITUCION ");
		sql.append(" AND PCOL.IDPAGOSJG = PAGO.IDPAGOSJG ");
		sql.append(" AND PCOL.IDPERORIGEN = PER.IDPERSONA ");
		
		sql.append(" AND COB.IDINSTITUCION = ");
		contador++;
		codigos.put(new Integer(contador),idInstitucion);
		sql.append(":"+contador);
		
		if(letrado!=null && !letrado.trim().equals("")){
			sql.append(" AND COB.IDPERSONA = ");
			contador++;
			codigos.put(new Integer(contador),letrado);
			sql.append(":"+contador);
		}
		if(fechaDesde!=null && !fechaDesde.trim().equals("")){
			sql.append(" AND RET.FECHAINICIO >= ");
			contador++;
			codigos.put(new Integer(contador),fechaDesde);
			sql.append(":"+contador);
		}
		
		if(fechaHasta!=null && !fechaHasta.trim().equals("")){
			sql.append(" AND RET.FECHAFIN <= ");
			contador++;
			codigos.put(new Integer(contador),fechaHasta);
			sql.append(":"+contador);
		}
		if(idTipoRetencion!=null && !idTipoRetencion.trim().equals("")){
			sql.append(" AND RET.TIPORETENCION = ");
			contador++;
			codigos.put(new Integer(contador),idTipoRetencion);
			sql.append(":"+contador);
		}
		if(idDestinatario!=null && !idDestinatario.trim().equals("")){
			sql.append(" AND RET.IDDESTINATARIO = ");
			contador++;
			codigos.put(new Integer(contador),idDestinatario);
			sql.append(":"+contador);
		}
		if(pagoRelacionado!=null && !pagoRelacionado.trim().equals("")){
			sql.append(" AND PAGO.IDPAGOSJG = ");
			contador++;
			codigos.put(new Integer(contador),pagoRelacionado);
			sql.append(":"+contador);
		}
		if(abonoRelacionado!=null && !abonoRelacionado.trim().equals("")){
			sql.append(" AND ABONO.NUMEROABONO = ");
			contador++;
			codigos.put(new Integer(contador),abonoRelacionado);
			sql.append(":"+contador);
		}
		sql.append(" ORDER BY PER.APELLIDOS1,PER.APELLIDOS2,PER.NOMBRE,RET.DESCDESTINATARIO,ANIO DESC,MES DESC");
		return sql.toString();
		
	}
	private Vector getRetencionesAplicadas(String sql, Hashtable codigos,UsrBean usrBean)throws ClsExceptions{
		RowsContainer rc = null;
		
		Vector datos = new Vector();
		
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(sql.toString(),codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					FcsAplicaMovimientosVariosBean retencionAplicada = new FcsAplicaMovimientosVariosBean();
					FcsRetencionesJudicialesBean retencionJudicial = new FcsRetencionesJudicialesBean();
					CenPersonaBean persona = new CenPersonaBean();
					StringBuffer nombre =  new StringBuffer();
					nombre.append((String)registro.get("NCOLEGIADO"));
					if((String)registro.get("NOMBRE")!=null){
						nombre.append(" ");
						nombre.append((String)registro.get("NOMBRE"));
						
					}
					if((String)registro.get("APELLIDOS1")!=null){
						nombre.append(" ");
						nombre.append((String)registro.get("APELLIDOS1"));
						
					}
					if((String)registro.get("APELLIDOS2")!=null){
						nombre.append(" ");
						nombre.append((String)registro.get("APELLIDOS2"));
						
					}
						
					persona.setNombre(nombre.toString());
					//persona.setApellido1((String)registro.get("APELLIDOS1"));
					//persona.setApellido2((String)registro.get("APELLIDOS2"));
					FcsDestinatariosRetencionesBean destinatarioRetencion = new FcsDestinatariosRetencionesBean();
					if((String)registro.get("IDDESTINATARIO")!=null)
						destinatarioRetencion.setIdDestinatario(new Integer((String)registro.get("IDDESTINATARIO")));
					if((String)registro.get("NOMBREDESTINATARIO")!=null)
						destinatarioRetencion.setNombre((String)registro.get("NOMBREDESTINATARIO"));
					else
						destinatarioRetencion.setNombre("");
					String tipoRetencion = (String)registro.get("TIPORETENCION");
			
					retencionJudicial.setDestinatarioRetencion(destinatarioRetencion);
					retencionJudicial.setFechaInicio(GstDate.getFormatedDateShort(usrBean.getLanguage(),(String)registro.get("FECHAINICIO")));
					retencionJudicial.setFechaFin(GstDate.getFormatedDateShort(usrBean.getLanguage(),(String)registro.get("FECHAFIN")));
//					retencionJudicial.setFechaInicio((String)registro.get("FECHAINICIO"));
//					retencionJudicial.setFechaFin((String)registro.get("FECHAFIN"));
					retencionJudicial.setTipoRetencion(tipoRetencion);
					
					retencionAplicada.setRetencionJudicial(retencionJudicial);
					
					if(retencionJudicial.getTipoRetencion().equals(ClsConstants.TIPO_RETENCION_PORCENTAJE))
						retencionJudicial.setDescTipoRetencion(UtilidadesString.getMensajeIdioma(usrBean, "FactSJCS.mantRetencionesJ.literal.porcentual"));
					
					else if(retencionJudicial.getTipoRetencion().equals(ClsConstants.TIPO_RETENCION_IMPORTEFIJO))
						retencionJudicial.setDescTipoRetencion(UtilidadesString.getMensajeIdioma(usrBean,"FactSJCS.mantRetencionesJ.literal.importeFijo"));
					else
						retencionJudicial.setDescTipoRetencion(UtilidadesString.getMensajeIdioma(usrBean,"FactSJCS.mantRetencionesJ.literal.tramosLEC"));
					retencionAplicada.setPersona(persona);
					retencionAplicada.setFechaRetencion((String)registro.get("FECHARETENCION"));
					if((String)registro.get("IMPORTERETENIDO")!=null)
						retencionAplicada.setImporteRetenido(new Double((String)registro.get("IMPORTERETENIDO")));
					
					retencionAplicada.setAbonoRelacionado((String)registro.get("ABONORELACIONADO"));
					retencionAplicada.setPagoRelacionado((String)registro.get("PAGORELACIONADO"));
					retencionAplicada.setAnio(UtilidadesHash.getString(registro, "ANIO") );
					retencionAplicada.setMes(UtilidadesHash.getString(registro, "MES") );
					retencionAplicada.setIdInstitucion(new Integer((String)registro.get("IDINSTITUCION")));
					retencionAplicada.setIdCobro(new Integer((String)registro.get("IDCOBRO")));
					retencionAplicada.setIdPersona(new Integer((String)registro.get("IDPERSONA")));
					retencionAplicada.setIdRetencion(new Integer((String)registro.get("IDRETENCION")));
					retencionAplicada.setFechaDesdePago(UtilidadesHash.getString(registro, "FECHADESDE") );
					retencionAplicada.setFechaHastaPago(UtilidadesHash.getString(registro, "FECHAHASTA") );
					
					datos.add(retencionAplicada);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsCobrosRetencionJudicialAdm.getRetencionesAplicadas(). Consulta SQL:"+sql.toString());
		}
		return datos;	
		
		
	}
	private Vector getRetencionesAplicadasAExportar(String sql, Hashtable codigos,UsrBean usrBean)throws ClsExceptions{
		RowsContainer rc = null;
		
		Vector datos = new Vector();
		
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(sql.toString(),codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					StringBuffer nombre =  new StringBuffer();
					nombre.append((String)registro.get("NCOLEGIADO"));
					if((String)registro.get("NOMBRE")!=null){
						nombre.append(" ");
						nombre.append((String)registro.get("NOMBRE"));
						
					}
					if((String)registro.get("APELLIDOS1")!=null){
						nombre.append(" ");
						nombre.append((String)registro.get("APELLIDOS1"));
						
					}
					if((String)registro.get("APELLIDOS2")!=null){
						nombre.append(" ");
						nombre.append((String)registro.get("APELLIDOS2"));
						
					}
					registro.put("NOMBRE", nombre);
					String tipoRetencion = (String)registro.get("TIPORETENCION");
			
					if(tipoRetencion.equals(ClsConstants.TIPO_RETENCION_PORCENTAJE))
						registro.put("DESCTIPORETENCION", UtilidadesString.getMensajeIdioma(usrBean, "FactSJCS.mantRetencionesJ.literal.porcentual"));
					else if(tipoRetencion.equals(ClsConstants.TIPO_RETENCION_IMPORTEFIJO))
						registro.put("DESCTIPORETENCION", UtilidadesString.getMensajeIdioma(usrBean,"FactSJCS.mantRetencionesJ.literal.importeFijo"));
					else
						registro.put("DESCTIPORETENCION", UtilidadesString.getMensajeIdioma(usrBean, "FactSJCS.mantRetencionesJ.literal.tramosLEC"));
						
					
					if((String)registro.get("IMPORTERETENIDO")!=null){
						registro.put("IMPORTERETENIDO",UtilidadesNumero.formatoCampo(UtilidadesHash.getString(registro, "IMPORTERETENIDO")));
					}
					registro.put("MES",UtilidadesHash.getString(registro, "MES") );
					registro.put("ANIO",UtilidadesHash.getString(registro, "ANIO") );
						
					
					datos.add(fila);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsCobrosRetencionJudicialAdm.getRetencionesAplicadasAExportar(). Consulta SQL:"+sql.toString());
		}
		return datos;	
		
		
	}
	public Vector getRetencionesAplicadas (Integer idInstitucion, String letrado, String fechaDesde, 
			String fechaHasta, String idTipoRetencion, String idDestinatario, String pagoRelacionado,
			String abonoRelacionado,UsrBean usrBean,boolean isGenerarExcel) throws ClsExceptions 
	{
		Hashtable codigos = new Hashtable();
		String sql = getQueryRetencionesAplicadas(idInstitucion, letrado, fechaDesde, 
				fechaHasta, idTipoRetencion, idDestinatario, pagoRelacionado,
				abonoRelacionado, usrBean,codigos);
		Vector datos = null;
		if(isGenerarExcel){
			datos = getRetencionesAplicadasAExportar(sql,codigos,usrBean);
			
		}else{
			datos = getRetencionesAplicadas(sql,codigos,usrBean);
			
		}
		return datos;
		

	}
	
	
	
	/**
	 * Devuelve la suma de los movimientos apliados al pago de un colegiado
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public double getSumaMovimientos (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT sum(" + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + ") as importe" +
							" from " + 	FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
							" where "+FcsAplicaMovimientosVariosBean.C_IDINSTITUCION+" = "+ idInstitucion +
							" and " + FcsAplicaMovimientosVariosBean.C_IDPERSONA + " = "+ idPersona +
							" and " + FcsAplicaMovimientosVariosBean.C_IDPAGOSJG + " = " + idPago ;
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
			if (resultado.isEmpty()){
				return 0;
			}
			else{
				String aux = ((Hashtable)resultado.get(0)).get("IMPORTE").toString();
				if (aux == null || "".equals(aux))
					return 0;
				else
					return Double.parseDouble(aux); 
			}
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsAplicaMovimientosVariosAdm.getSumaMovimientos:"+consulta);
		}
	}

	
	/**
	 * Devuelve la suma de los movimientos apliados al pago de un colegiado
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public double getSumaMovimientosAplicados (String idInstitucion, String idMovimiento, String idPersona) throws ClsExceptions 
	{
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT sum(" + FcsAplicaMovimientosVariosBean.C_IMPORTEAPLICADO + ") as importe" +
							" from " + 	FcsAplicaMovimientosVariosBean.T_NOMBRETABLA +
							" where "+FcsAplicaMovimientosVariosBean.C_IDINSTITUCION+" = "+ idInstitucion +
							" and " + FcsAplicaMovimientosVariosBean.C_IDPERSONA + " = "+ idPersona +
							" and " + FcsAplicaMovimientosVariosBean.C_IDMOVIMIENTO + " = " + idMovimiento ;
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
			if (resultado.isEmpty()){
				return 0;
			}
			else{
				String aux = ((Hashtable)resultado.get(0)).get("IMPORTE").toString();
				if (aux == null || "".equals(aux))
					return 0;
				else
					return Double.parseDouble(aux); 
			}
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsAplicaMovimientosVariosAdm.getSumaMovimientos:"+consulta);
		}
	}	
	
	
	/**
	 * Obtiene el listado de los registros de la tabla FCS_APLICA_MOVIMIENTOSVARIOS que tengan el valo de la institución
	 * y del identificador del pago que se les ha pasado por parámetro.
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */	
	
   	public Vector selectAplicaMovimientosVarios (String idinstitucion,
			  String idPago)
   		throws ClsExceptions {
   		
   		String consulta = "";
   		Vector resultado = new Vector();
   		
   		try {
   			consulta += " select * from "+FcsAplicaMovimientosVariosBean.T_NOMBRETABLA+" ";
   			consulta += " where ("+FcsAplicaMovimientosVariosBean.C_IDINSTITUCION+"="+idinstitucion+") ";
   			consulta += "   and ("+FcsAplicaMovimientosVariosBean.C_IDPAGOSJG+"="+idPago+") ";

   			resultado = (Vector)this.selectGenerico(consulta.toString());
   		} catch (Exception e) {
   			throw new ClsExceptions (e, "Excepcion en FcsAplicaMovimientosVariosAdm.selectAplicaMovimientosVarios(). Consulta SQL:" + consulta);
   		}
   		
   		return resultado;
   	} //selectAplicaMovimientosVarios ()		
	

	/**
	 * Elimina los registros de la tabla FCS_APLICA_MOVIMIENTOSvARIOS que tengan el valo de la institución
	 * y del identificador del pago que se les ha pasado por parámetro.
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */	
	
   	public void deleteAplicaMovimientosVarios (String idinstitucion,
			  String idPago)
   		throws ClsExceptions {
   		
   		String consulta = "";
   		try {
   			consulta += " delete from "+FcsAplicaMovimientosVariosBean.T_NOMBRETABLA+" ";
   			consulta += " where ("+FcsAplicaMovimientosVariosBean.C_IDINSTITUCION+"="+idinstitucion+") ";
   			consulta += "   and ("+FcsAplicaMovimientosVariosBean.C_IDPAGOSJG+"="+idPago+") ";

   			ClsMngBBDD.executeUpdate (consulta);
   		} catch (Exception e) {
   			throw new ClsExceptions (e, "Excepcion en FcsAplicaMovimientosVariosAdm.deleteAplicaMovimientosVarios(). Consulta SQL:" + consulta);
   		}
   	} //deleteAplicaMovimientosVarios ()	
	
}


