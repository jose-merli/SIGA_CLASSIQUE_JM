//VERSIONES:
//ruben.fernandez 04-04-2005 creacion 

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;


public class FcsCobrosRetencionJudicialAdm extends MasterBeanAdministrador {

	
	public FcsCobrosRetencionJudicialAdm(UsrBean usuario) {
		super(FcsCobrosRetencionJudicialBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsCobrosRetencionJudicialBean.C_FECHAMODIFICACION,	FcsCobrosRetencionJudicialBean.C_FECHARETENCION,
							FcsCobrosRetencionJudicialBean.C_IDCOBRO,			FcsCobrosRetencionJudicialBean.C_IDINSTITUCION,
							FcsCobrosRetencionJudicialBean.C_IDPAGOSJG,			FcsCobrosRetencionJudicialBean.C_IDPERSONA,
							FcsCobrosRetencionJudicialBean.C_IDRETENCION,		FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO,
							FcsCobrosRetencionJudicialBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsCobrosRetencionJudicialBean.C_IDINSTITUCION,		FcsCobrosRetencionJudicialBean.C_IDPERSONA,
							FcsCobrosRetencionJudicialBean.C_IDRETENCION,		FcsCobrosRetencionJudicialBean.C_IDCOBRO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsCobrosRetencionJudicialBean bean = null;
		
		try {
			bean = new FcsCobrosRetencionJudicialBean();
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsCobrosRetencionJudicialBean.C_FECHAMODIFICACION));
			bean.setFechaRetencion	(UtilidadesHash.getString(hash,FcsCobrosRetencionJudicialBean.C_FECHARETENCION));
			bean.setIdCobro			(UtilidadesHash.getInteger(hash,FcsCobrosRetencionJudicialBean.C_IDCOBRO));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsCobrosRetencionJudicialBean.C_IDINSTITUCION));
			bean.setIdPagosJG		(UtilidadesHash.getInteger(hash,FcsCobrosRetencionJudicialBean.C_IDPAGOSJG));
			bean.setIdPersona		(UtilidadesHash.getInteger(hash,FcsCobrosRetencionJudicialBean.C_IDPERSONA));
			bean.setIdRetencion		(UtilidadesHash.getInteger(hash,FcsCobrosRetencionJudicialBean.C_IDRETENCION));
			bean.setImporteRetenido	(UtilidadesHash.getDouble(hash,FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsCobrosRetencionJudicialBean.C_USUMODIFICACION));
			
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
			FcsCobrosRetencionJudicialBean b = (FcsCobrosRetencionJudicialBean) bean;
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_FECHARETENCION, b.getFechaRetencion().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_IDCOBRO, b.getIdCobro().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_IDPAGOSJG, b.getIdPagosJG().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_IDRETENCION, b.getIdRetencion().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO, b.getImporteRetenido().toString());
			UtilidadesHash.set(htData, FcsCobrosRetencionJudicialBean.C_USUMODIFICACION, b.getUsuMod().toString());
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
	 * Devuelve el valor del importe que hay que pagar para un colegiado en un pago determinado
	 *   
	 * @param String idInstitucion 
	 * @param String idPago
	 * @param String idPersona
	 * 
	 * @return Hashtable resultado con el importe 
	 */
	public Hashtable getImporteRetenido (String idInstitucion, String idPago, String idPersona)
	{
		Hashtable resultado = new Hashtable();
		String resultado1 = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO + ") AS IMPORTE " +
					" FROM " + FcsCobrosRetencionJudicialBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsCobrosRetencionJudicialBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsCobrosRetencionJudicialBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsCobrosRetencionJudicialBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las retenciones Judiciales que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getRetenciones (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT r." + FcsRetencionesJudicialesBean.C_TIPORETENCION + " "+ FcsRetencionesJudicialesBean.C_TIPORETENCION +
							", c." + FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO +
							", r." + FcsRetencionesJudicialesBean.C_IMPORTE + " " + FcsRetencionesJudicialesBean.C_IMPORTE + 
							", r." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " " + FcsRetencionesJudicialesBean.C_IDINSTITUCION + 
							", r." + FcsRetencionesJudicialesBean.C_IDPERSONA + " " + FcsRetencionesJudicialesBean.C_IDPERSONA + 
							", r." + FcsRetencionesJudicialesBean.C_IDRETENCION + " " + FcsRetencionesJudicialesBean.C_IDRETENCION + 
							", d." + FcsDestinatariosRetencionesBean.C_NOMBRE + " " + FcsDestinatariosRetencionesBean.C_NOMBRE + 
							" from " + 
							FcsCobrosRetencionJudicialBean.T_NOMBRETABLA + " c,"+
							FcsRetencionesJudicialesBean.T_NOMBRETABLA + " r,"+
							FcsDestinatariosRetencionesBean.T_NOMBRETABLA + " d"+
							" where c."+FcsCobrosRetencionJudicialBean.C_IDINSTITUCION+" = "+ idInstitucion +
							" and c." + FcsCobrosRetencionJudicialBean.C_IDPERSONA + " = "+ idPersona +
							" and c." + FcsCobrosRetencionJudicialBean.C_IDPAGOSJG + " = "+ idPago +
							//JOIN
							" and r." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = c." + FcsCobrosRetencionJudicialBean.C_IDINSTITUCION +
							" and r." + FcsRetencionesJudicialesBean.C_IDPERSONA + " = c." + FcsCobrosRetencionJudicialBean.C_IDPERSONA +
							" and r." + FcsRetencionesJudicialesBean.C_IDRETENCION + " = c." + FcsCobrosRetencionJudicialBean.C_IDRETENCION +
							" and d." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION + " = r." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + 
							" and d." + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO + " = r." + FcsRetencionesJudicialesBean.C_IDDESTINATARIO + " ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsCobrosRetencionJudicialAdm.getRetenciones:"+consulta);
		}
		
		return resultado;
		
	}
	private String getQueryRetencionesAplicadas(Integer idInstitucion, String letrado, String fechaDesde, 
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
		sql.append(" PAGO.NOMBRE PAGORELACIONADO, ");
		sql.append(" ABONO.NUMEROABONO ABONORELACIONADO");
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
		sql.append(" AND NVL(PCOL.IDPERDESTINO, PCOL.IDPERORIGEN) = ABONO.IDPERSONA ");
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
					FcsCobrosRetencionJudicialBean retencionAplicada = new FcsCobrosRetencionJudicialBean();
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
					retencionJudicial.setFechaInicio((String)registro.get("FECHAINICIO"));
					retencionJudicial.setFechaFin((String)registro.get("FECHAFIN"));
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
	 * Devuelve la suma de las retenciones judiciales aplicadas al pago de un colegiado
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public double getSumaRetenciones (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT sum(" + FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO + ") as importe" +
							" from " + 	FcsCobrosRetencionJudicialBean.T_NOMBRETABLA +
							" where "+FcsCobrosRetencionJudicialBean.C_IDINSTITUCION+" = "+ idInstitucion +
							" and " + FcsCobrosRetencionJudicialBean.C_IDPERSONA + " = "+ idPersona +
							" and " + FcsCobrosRetencionJudicialBean.C_IDPAGOSJG + " = " + idPago ;
							
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
			throw new ClsExceptions (e,"Error en FcsCobrosRetencionJudicialAdm.getRetenciones:"+consulta);
		}
	}
	
}


