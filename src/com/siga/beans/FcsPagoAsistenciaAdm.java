//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsPagoAsistenciaAdm extends MasterBeanAdministrador {

	
	public FcsPagoAsistenciaAdm(UsrBean usuario) {
		super(FcsPagoAsistenciaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoAsistenciaBean.C_ANIO,				FcsPagoAsistenciaBean.C_FECHAMODIFICACION,
							FcsPagoAsistenciaBean.C_IDPAGOSJG,			FcsPagoAsistenciaBean.C_IDINSTITUCION,
							FcsPagoAsistenciaBean.C_IMPORTEIRPF,		FcsPagoAsistenciaBean.C_IMPORTEPAGADO,
							FcsPagoAsistenciaBean.C_NUMERO,				FcsPagoAsistenciaBean.C_PORCENTAJEIRPF,
							FcsPagoAsistenciaBean.C_USUMODIFICACION,    FcsPagoAsistenciaBean.C_IDPERSONASOCIEDAD};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoAsistenciaBean.C_IDINSTITUCION,	FcsPagoAsistenciaBean.C_IDPAGOSJG,
							FcsPagoAsistenciaBean.C_NUMERO,			FcsPagoAsistenciaBean.C_ANIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoAsistenciaBean bean = null;
		
		try {
			bean = new FcsPagoAsistenciaBean();
			bean.setAnio			(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_ANIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsPagoAsistenciaBean.C_FECHAMODIFICACION));
			bean.setIdPagosJG		(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_IDPAGOSJG));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_IDINSTITUCION));
			bean.setIdPersona		(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_IDPERSONA));
			bean.setImporteIRPF		(UtilidadesHash.getFloat(hash,FcsPagoAsistenciaBean.C_IMPORTEIRPF));
			bean.setImportePagado	(UtilidadesHash.getFloat(hash,FcsPagoAsistenciaBean.C_IMPORTEPAGADO));
			bean.setNumero			(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_NUMERO));
			bean.setPorcentajeIRPF	(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_PORCENTAJEIRPF));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsPagoAsistenciaBean.C_USUMODIFICACION));
			
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
			FcsPagoAsistenciaBean b = (FcsPagoAsistenciaBean) bean;
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_IDPAGOSJG, b.getIdPagosJG().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_IMPORTEPAGADO, b.getImportePagado().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_IMPORTEIRPF, b.getImporteIRPF().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_NUMERO, b.getNumero().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_PORCENTAJEIRPF, b.getPorcentajeIRPF().toString());
			UtilidadesHash.set(htData, FcsPagoAsistenciaBean.C_USUMODIFICACION, b.getUsuMod().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoAsistenciaAdm.selectGenerico(). Consulta SQL:"+select);
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
		String resultado1 = "", resultado2 = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsPagoAsistenciaBean.C_IMPORTEPAGADO + ") AS IMPORTE, " +
					" SUM(" + FcsPagoAsistenciaBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsPagoAsistenciaBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsPagoAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsPagoAsistenciaBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsPagoAsistenciaBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las asistencias que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getAsistencias (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE +  
							", G." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIAS " +
							", A." + ScsAsistenciasBean.C_IDINSTITUCION +
							", A." + ScsAsistenciasBean.C_ANIO +
							", A." + ScsAsistenciasBean.C_NUMERO +
							", A." + ScsAsistenciasBean.C_FECHAHORA +
							", F." + FcsPagoAsistenciaBean.C_IDPAGOSJG +
							", F." + FcsPagoAsistenciaBean.C_IMPORTEPAGADO +
							", F." + FcsPagoAsistenciaBean.C_IMPORTEIRPF +
							", F." + FcsPagoAsistenciaBean.C_PORCENTAJEIRPF +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " G" +
							" ," + ScsAsistenciasBean.T_NOMBRETABLA + " A" +
							" ," + FcsPagoAsistenciaBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= G." +ScsGuardiasTurnoBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= G." +ScsGuardiasTurnoBean.C_IDTURNO  +
							" AND G." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "= A." +ScsAsistenciasBean.C_IDINSTITUCION +
							" AND G." + ScsGuardiasTurnoBean.C_IDTURNO + "= A." +ScsAsistenciasBean.C_IDTURNO +
							" AND G." + ScsGuardiasTurnoBean.C_IDGUARDIA + "= A." +ScsAsistenciasBean.C_IDGUARDIA +
							" AND F." + FcsPagoAsistenciaBean.C_IDINSTITUCION + "= A." +ScsAsistenciasBean.C_IDINSTITUCION +
							" AND F." + FcsPagoAsistenciaBean.C_ANIO + "= A." +ScsAsistenciasBean.C_ANIO +
							" AND F." + FcsPagoAsistenciaBean.C_NUMERO + "= A." +ScsAsistenciasBean.C_NUMERO +
							" AND F." + FcsPagoAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsPagoAsistenciaBean.C_IDPAGOSJG + "=" + idPago +
							" AND F." + FcsPagoAsistenciaBean.C_IDPERSONA + "=" + idPersona +" ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsPagoActuacionAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
	
	
}


