//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsFactAsistenciaAdm extends MasterBeanAdministrador {

	
	public FcsFactAsistenciaAdm(UsrBean usuario) {
		super(FcsFactAsistenciaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactAsistenciaBean.C_ANIO,				FcsFactAsistenciaBean.C_FECHAHORA,
							FcsFactAsistenciaBean.C_FECHA_JUSTIFICACION,FcsFactAsistenciaBean.C_FECHAMODIFICACION,
							FcsFactAsistenciaBean.C_IDAPUNTE,				FcsFactAsistenciaBean.C_IDFACTURACION,
							FcsFactAsistenciaBean.C_IDINSTITUCION,				FcsFactAsistenciaBean.C_IDPERSONA,
							FcsFactAsistenciaBean.C_NUMERO,				FcsFactAsistenciaBean.C_PRECIOAPLICADO,
							FcsFactAsistenciaBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactAsistenciaBean.C_IDINSTITUCION,	FcsFactAsistenciaBean.C_IDFACTURACION,
							FcsFactAsistenciaBean.C_NUMERO,			FcsFactAsistenciaBean.C_ANIO,
							FcsFactAsistenciaBean.C_IDAPUNTE};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactAsistenciaBean bean = null;
		
		try {
			bean = new FcsFactAsistenciaBean();
			bean.setAnio				(UtilidadesHash.getInteger(hash,FcsFactAsistenciaBean.C_ANIO));
			bean.setFechaMod			(UtilidadesHash.getString(hash,FcsFactAsistenciaBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion		(UtilidadesHash.getInteger(hash,FcsFactAsistenciaBean.C_IDFACTURACION));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash,FcsFactAsistenciaBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getLong(hash,FcsFactAsistenciaBean.C_IDPERSONA));
			bean.setNumero				(UtilidadesHash.getLong(hash,FcsFactAsistenciaBean.C_NUMERO));
			bean.setPrecioAplicado		(UtilidadesHash.getDouble(hash,FcsFactAsistenciaBean.C_PRECIOAPLICADO));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash,FcsFactAsistenciaBean.C_USUMODIFICACION));
			bean.setFechaHora			(UtilidadesHash.getString(hash,FcsFactAsistenciaBean.C_FECHAHORA));
			bean.setFechaJustificacion	(UtilidadesHash.getString(hash,FcsFactAsistenciaBean.C_FECHA_JUSTIFICACION));
			bean.setIdApunte			(UtilidadesHash.getLong(hash,FcsFactAsistenciaBean.C_IDAPUNTE));
			
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
			FcsFactAsistenciaBean b = (FcsFactAsistenciaBean) bean;
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_NUMERO, b.getNumero().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_FECHA_JUSTIFICACION, b.getFechaJustificacion().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_FECHAHORA, b.getFechaHora().toString());
			UtilidadesHash.set(htData, FcsFactAsistenciaBean.C_IDAPUNTE, b.getIdApunte().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsFactAsistenciaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Devuelve el valor del importe que hay que facturar para un colegiado en una facturacion determinada
	 *   
	 * @param String idInstitucion 
	 * @param String idFacturacion
	 * @param String idPersona
	 * 
	 * @return String resultado con el importe 
	 */
	public String getImporteFacturado (String idInstitucion, String idFacturacion, String idPersona)
	{
		String resultado = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsFactAsistenciaBean.C_PRECIOAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsFactAsistenciaBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsFactAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsFactAsistenciaBean.C_IDFACTURACION + "=" + idFacturacion + " " +
					" AND " + FcsFactAsistenciaBean.C_IDPERSONA + "=" + idPersona + " ";
		
		//Hashtable para recoger el resultado de la contulta
		Hashtable hash = new Hashtable();
		try{
			hash = (Hashtable)((Vector)this.selectGenerico(consulta)).get(0);
			//resogemos el resultado
			resultado = (String)hash.get("IMPORTE");
			if (resultado.equals(""))resultado="0";
		}catch(Exception e){
			//si no se ha obtenido resultado es porque no hay nada que facturar para el colegiado con ese idPersona
			resultado = "0";
		}
		return resultado;
	}
	
	/**
	 * Devuelve un vector con las asistencias que hay que facturar para una persona
	 *  
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPersona
	 * @return
	 */
	public Vector getAsistencias (String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", G." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIA " + 
							", A." + ScsAsistenciasBean.C_ANIO +
							", A." + ScsAsistenciasBean.C_NUMERO +
							", A." + ScsAsistenciasBean.C_FECHAHORA +
							", F." + FcsFactActuacionDesignaBean.C_PRECIOAPLICADO +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " G" +
							" ," + ScsAsistenciasBean.T_NOMBRETABLA + " A" +
							" ," + FcsFactAsistenciaBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= G." +ScsGuardiasTurnoBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= G." +ScsGuardiasTurnoBean.C_IDTURNO  +
							" AND G." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "= A." +ScsAsistenciasBean.C_IDINSTITUCION +
							" AND G." + ScsGuardiasTurnoBean.C_IDTURNO + "= A." +ScsAsistenciasBean.C_IDTURNO +
							" AND G." + ScsGuardiasTurnoBean.C_IDGUARDIA + "= A." +ScsAsistenciasBean.C_IDGUARDIA +
							" AND F." + FcsFactAsistenciaBean.C_IDINSTITUCION + "= A." +ScsAsistenciasBean.C_IDINSTITUCION +
							" AND F." + FcsFactAsistenciaBean.C_ANIO + "= A." +ScsAsistenciasBean.C_ANIO +
							" AND F." + FcsFactAsistenciaBean.C_NUMERO + "= A." +ScsAsistenciasBean.C_NUMERO +
							" AND F." + FcsFactAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsFactAsistenciaBean.C_IDFACTURACION + "=" + idFacturacion +
							" AND F." + FcsFactAsistenciaBean.C_IDPERSONA + "=" + idPersona +
							" ORDER BY A." + ScsAsistenciasBean.C_FECHAHORA + " DESC ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactActuacionAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
	
	
}


