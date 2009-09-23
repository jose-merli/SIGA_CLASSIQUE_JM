//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsPagoGuardiasColegiadoAdm extends MasterBeanAdministrador {

	
	public FcsPagoGuardiasColegiadoAdm(UsrBean usuario) {
		super(FcsPagoGuardiasColegiadoBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoGuardiasColegiadoBean.C_FECHAMODIFICACION,	FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,
							FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG,			FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION,		
							FcsPagoGuardiasColegiadoBean.C_IDPERSONA,			FcsPagoGuardiasColegiadoBean.C_IDTURNO,				
							FcsPagoGuardiasColegiadoBean.C_FECHAINICIO,			FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF,			
							FcsPagoGuardiasColegiadoBean.C_PORCENTAJEIRPF,		FcsPagoGuardiasColegiadoBean.C_USUMODIFICACION,		
							FcsPagoGuardiasColegiadoBean.C_IDGUARDIA,			FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO,
							FcsPagoGuardiasColegiadoBean.C_IDPERSONASOCIEDAD};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION,		FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG,
							FcsPagoGuardiasColegiadoBean.C_IDTURNO,				FcsPagoGuardiasColegiadoBean.C_IDGUARDIA,
							FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,FcsPagoGuardiasColegiadoBean.C_IDPERSONA,
							FcsPagoGuardiasColegiadoBean.C_FECHAINICIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoGuardiasColegiadoBean bean = null;
		
		try {
			bean = new FcsPagoGuardiasColegiadoBean();
			bean.setFechaInicio			(UtilidadesHash.getString(hash,FcsPagoGuardiasColegiadoBean.C_FECHAINICIO));
			bean.setFechaMod			(UtilidadesHash.getString(hash,FcsPagoGuardiasColegiadoBean.C_FECHAMODIFICACION));
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdPagosJG			(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG));
			bean.setIdGuardia			(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_IDGUARDIA));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_IDPERSONA));
			bean.setIdTurno				(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_IDTURNO));
			bean.setImportePagado		(UtilidadesHash.getFloat(hash,FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO));
			bean.setImporteIRPF			(UtilidadesHash.getFloat(hash,FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF));
			bean.setPorcentajeIRPF		(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_PORCENTAJEIRPF));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash,FcsPagoGuardiasColegiadoBean.C_USUMODIFICACION));
			
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
			FcsPagoGuardiasColegiadoBean b = (FcsPagoGuardiasColegiadoBean) bean;
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_FECHAINICIO, b.getFechaInicio().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS, b.getIdCalendarioGuardias().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG, b.getIdPagosJG().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IDGUARDIA, b.getIdGuardia().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IDTURNO, b.getIdTurno().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF, b.getImporteIRPF().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO, b.getImportePagado().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_PORCENTAJEIRPF, b.getPorcentajeIRPF().toString());
			UtilidadesHash.set(htData, FcsPagoGuardiasColegiadoBean.C_USUMODIFICACION, b.getUsuMod().toString());
			
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoGuardiasColegiadoAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Devuelve el valor del importe que hay que pagar para un colegiado en una facturacion determinada
	 *   
	 * @param String idInstitucion 
	 * @param String idPago
	 * @param String idPersona
	 * 
	 * @return Hashtable resultado con el importe 
	 */
	public Hashtable getImportePagado (String idInstitucion, String idPago, String idPersona)
	{
		Hashtable resultado = new Hashtable();
		String resultado1 = "",resultado2 = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO + ") AS IMPORTE, " +
					" SUM(" + FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsPagoGuardiasColegiadoBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsPagoGuardiasColegiadoBean.C_IDPERSONA + "=" + idPersona + " ";
		
		//Hashtable para recoger el resultado de la contulta
		Hashtable hash = new Hashtable();
		try{
			hash = (Hashtable)((Vector)this.selectGenerico(consulta)).get(0);
			//recogemos el resultado
			resultado1 = (String)hash.get("IMPORTE");
			if (resultado1.equals(""))resultado1="0";
			resultado2 = (String)hash.get("IRPF");
			if (resultado2.equals(""))resultado2="0";
		}catch(Exception e){
			//si no se ha obtenido resultado es porque no hay nada que pagar para el colegiado con ese idPersona
			resultado1 = "0";
			resultado2 = "0";
		}
		resultado.put("IMPORTE",resultado1);
		resultado.put("IRPF",resultado2);
		return resultado;
	}
	
	/**
	 * Devuelve un vector con las guardias que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getGuardiasPresenciales (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", G." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIAS" +
							", G." + ScsGuardiasTurnoBean.C_IDINSTITUCION +
							", G." + ScsGuardiasTurnoBean.C_IDGUARDIA +
							", G." + ScsGuardiasTurnoBean.C_IDTURNO +
							", C." + ScsGuardiasColegiadoBean.C_FECHAINICIO +
							", C." + ScsGuardiasColegiadoBean.C_FECHAFIN +
							", C." + ScsGuardiasColegiadoBean.C_DIASACOBRAR +
							", F." + FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG +
							", F." + FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS +
							", F." + FcsPagoGuardiasColegiadoBean.C_IDPERSONA +
							", F." + FcsPagoGuardiasColegiadoBean.C_IMPORTEPAGADO +
							", F." + FcsPagoGuardiasColegiadoBean.C_IMPORTEIRPF +
							", F." + FcsPagoGuardiasColegiadoBean.C_PORCENTAJEIRPF +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " G" +
							" ," + ScsGuardiasColegiadoBean.T_NOMBRETABLA + " C" +
							" ," + FcsPagoGuardiasColegiadoBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= G." +ScsGuardiasTurnoBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= G." +ScsGuardiasTurnoBean.C_IDTURNO  +
							" AND G." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "= C." +ScsGuardiasColegiadoBean.C_IDINSTITUCION +
							" AND G." + ScsGuardiasTurnoBean.C_IDTURNO + "= C." +ScsGuardiasColegiadoBean.C_IDTURNO +
							" AND G." + ScsGuardiasTurnoBean.C_IDGUARDIA + "= C." +ScsGuardiasColegiadoBean.C_IDGUARDIA +
							" AND C." + ScsGuardiasColegiadoBean.C_IDINSTITUCION + "= F." +FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION +
							" AND C." + ScsGuardiasColegiadoBean.C_IDTURNO + "= F." +FcsPagoGuardiasColegiadoBean.C_IDTURNO +
							" AND C." + ScsGuardiasColegiadoBean.C_IDGUARDIA + "= F." +FcsPagoGuardiasColegiadoBean.C_IDGUARDIA +
							" AND C." + ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS + "= F." +FcsPagoGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS +
							" AND C." + ScsGuardiasColegiadoBean.C_IDPERSONA + "= F." +FcsPagoGuardiasColegiadoBean.C_IDPERSONA +
							" AND C." + ScsGuardiasColegiadoBean.C_FECHAINICIO + "= F." +FcsPagoGuardiasColegiadoBean.C_FECHAINICIO +
							" AND F." + FcsPagoGuardiasColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsPagoGuardiasColegiadoBean.C_IDPAGOSJG + "=" + idPago +
							" AND F." + FcsPagoGuardiasColegiadoBean.C_IDPERSONA + "=" + idPersona +" ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsPagoGuardiasColegiadoAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
	
}