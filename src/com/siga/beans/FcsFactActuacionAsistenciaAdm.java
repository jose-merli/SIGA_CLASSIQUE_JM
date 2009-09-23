//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsFactActuacionAsistenciaAdm extends MasterBeanAdministrador {

	
	public FcsFactActuacionAsistenciaAdm(UsrBean usuario) {
		super(FcsFactActuacionAsistenciaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactActuacionAsistenciaBean.C_ANIO,					FcsFactActuacionAsistenciaBean.C_FECHA_ACTUACION,
							FcsFactActuacionAsistenciaBean.C_FECHA_JUSTIFICACION,	FcsFactActuacionAsistenciaBean.C_FECHAMODIFICACION,
							FcsFactActuacionAsistenciaBean.C_IDACTUACION,			FcsFactActuacionAsistenciaBean.C_IDAPUNTE,
							FcsFactActuacionAsistenciaBean.C_IDFACTURACION,			FcsFactActuacionAsistenciaBean.C_IDINSTITUCION,
							FcsFactActuacionAsistenciaBean.C_IDPERSONA,				FcsFactActuacionAsistenciaBean.C_NUMERO,
							FcsFactActuacionAsistenciaBean.C_PRECIOAPLICADO,		FcsFactActuacionAsistenciaBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactActuacionAsistenciaBean.C_IDINSTITUCION,	FcsFactActuacionAsistenciaBean.C_IDFACTURACION,
							FcsFactActuacionAsistenciaBean.C_NUMERO,		FcsFactActuacionAsistenciaBean.C_IDAPUNTE,
							FcsFactActuacionAsistenciaBean.C_IDACTUACION,   FcsFactActuacionAsistenciaBean.C_ANIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactActuacionAsistenciaBean bean = null;
		
		try {
			bean = new FcsFactActuacionAsistenciaBean();
			bean.setAnio						(UtilidadesHash.getInteger(hash,FcsFactActuacionAsistenciaBean.C_ANIO));
			bean.setFechaMod					(UtilidadesHash.getString(hash,FcsFactActuacionAsistenciaBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion				(UtilidadesHash.getInteger(hash,FcsFactActuacionAsistenciaBean.C_IDFACTURACION));
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash,FcsFactActuacionAsistenciaBean.C_IDINSTITUCION));
			bean.setIdPersona					(UtilidadesHash.getLong(hash,FcsFactActuacionAsistenciaBean.C_IDPERSONA));
			bean.setIdActuacion					(UtilidadesHash.getLong(hash,FcsFactActuacionAsistenciaBean.C_IDACTUACION));
			bean.setNumero						(UtilidadesHash.getLong(hash,FcsFactActuacionAsistenciaBean.C_NUMERO));
			bean.setPrecioAplicado				(UtilidadesHash.getDouble(hash,FcsFactActuacionAsistenciaBean.C_PRECIOAPLICADO));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash,FcsFactActuacionAsistenciaBean.C_USUMODIFICACION));
			bean.setFechaActuacion				(UtilidadesHash.getString(hash,FcsFactActuacionAsistenciaBean.C_FECHA_ACTUACION));
			bean.setFechaJustificacion			(UtilidadesHash.getString(hash,FcsFactActuacionAsistenciaBean.C_FECHA_JUSTIFICACION));
			bean.setIdApunte					(UtilidadesHash.getLong(hash,FcsFactActuacionAsistenciaBean.C_IDAPUNTE));
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
			FcsFactActuacionAsistenciaBean b = (FcsFactActuacionAsistenciaBean) bean;
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_IDACTUACION, b.getIdActuacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_NUMERO, b.getNumero().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_FECHA_ACTUACION, b.getFechaActuacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_FECHA_JUSTIFICACION, b.getFechaJustificacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionAsistenciaBean.C_IDAPUNTE, b.getIdApunte().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsFactActuacionAsistenciaAdm.selectGenerico(). Consulta SQL:"+select);
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
					" SUM(" + FcsFactActuacionAsistenciaBean.C_PRECIOAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsFactActuacionAsistenciaBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsFactActuacionAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsFactActuacionAsistenciaBean.C_IDFACTURACION + "=" + idFacturacion + " " +
					" AND " + FcsFactActuacionAsistenciaBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las actuaciones en asistencias que hay que facturar para una persona
	 *  
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPersona
	 * @return
	 */
	public Vector getActuacionAsistencias (String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", G." + ScsGuardiasTurnoBean.C_NOMBRE +  " AS GUARDIA " + 
							", A." + ScsActuacionAsistenciaBean.C_ANIO +
							", A." + ScsActuacionAsistenciaBean.C_ANIO +
							", A." + ScsActuacionAsistenciaBean.C_NUMERO +
							", A." + ScsAsistenciasBean.C_FECHAHORA +
							", AC." + ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE +
							", F." + FcsFactActuacionAsistenciaBean.C_PRECIOAPLICADO +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " G" +
							" ," + ScsAsistenciasBean.T_NOMBRETABLA + " A" +
							" ," + ScsActuacionAsistenciaBean.T_NOMBRETABLA + " AC" +
							" ," + FcsFactActuacionAsistenciaBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= G." +ScsGuardiasTurnoBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= G." +ScsGuardiasTurnoBean.C_IDTURNO  +
							" AND G." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "= A." +ScsAsistenciasBean.C_IDINSTITUCION +
							" AND G." + ScsGuardiasTurnoBean.C_IDTURNO + "= A." +ScsAsistenciasBean.C_IDTURNO +
							" AND G." + ScsGuardiasTurnoBean.C_IDGUARDIA + "= A." +ScsAsistenciasBean.C_IDGUARDIA +
							" AND A." + ScsAsistenciasBean.C_IDINSTITUCION + "= AC." +ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND A." + ScsAsistenciasBean.C_ANIO + "= AC." +ScsActuacionAsistenciaBean.C_ANIO +
							" AND A." + ScsAsistenciasBean.C_NUMERO + "= AC." +ScsActuacionAsistenciaBean.C_NUMERO +
							" AND F." + FcsFactActuacionAsistenciaBean.C_IDINSTITUCION + "= AC." +ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND F." + FcsFactActuacionAsistenciaBean.C_ANIO+ "= AC." +ScsActuacionAsistenciaBean.C_ANIO +
							" AND F." + FcsFactActuacionAsistenciaBean.C_NUMERO + "= AC." +ScsActuacionAsistenciaBean.C_NUMERO +
							" AND F." + FcsFactActuacionAsistenciaBean.C_IDACTUACION + "= AC." +ScsActuacionAsistenciaBean.C_IDACTUACION +
							" AND F." + FcsFactActuacionAsistenciaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsFactActuacionAsistenciaBean.C_IDFACTURACION + "=" + idFacturacion +
							" AND F." + FcsFactActuacionAsistenciaBean.C_IDPERSONA + "=" + idPersona +
							" ORDER BY A." + ScsAsistenciasBean.C_FECHAHORA + " DESC ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactActuacionAsistenciaAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
}


