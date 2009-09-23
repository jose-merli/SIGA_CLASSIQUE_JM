//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;


public class FcsFactSojAdm extends MasterBeanAdministrador {

	
	public FcsFactSojAdm(UsrBean usuario) {
		super(FcsFactSojBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactSojBean.C_ANIO,					FcsFactSojBean.C_FECHA_APERTURA,
							FcsFactSojBean.C_FECHAMODIFICACION,		FcsFactSojBean.C_IDFACTURACION,
							FcsFactSojBean.C_IDGUARDIA,				FcsFactSojBean.C_IDINSTITUCION,
							FcsFactSojBean.C_IDPERSONA,				FcsFactSojBean.C_IDTIPOSOJ,
							FcsFactSojBean.C_IDTURNO,				FcsFactSojBean.C_NUMERO,
							FcsFactSojBean.C_PRECIOAPLICADO,		FcsFactSojBean.C_USUMODIFICACION,};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactSojBean.C_IDINSTITUCION,	FcsFactSojBean.C_IDFACTURACION,
							FcsFactSojBean.C_IDTIPOSOJ,		FcsFactSojBean.C_NUMERO,
							FcsFactSojBean.C_ANIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactSojBean bean = null;
		
		try {
			bean = new FcsFactSojBean();
			bean.setAnio			(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_ANIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsFactSojBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion	(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_IDFACTURACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_IDINSTITUCION));
			bean.setIdPersona		(UtilidadesHash.getLong(hash,FcsFactSojBean.C_IDPERSONA));
			bean.setIdTipoSoj		(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_IDTIPOSOJ));
			bean.setNumero			(UtilidadesHash.getLong(hash,FcsFactSojBean.C_NUMERO));
			bean.setPrecioAplicado	(UtilidadesHash.getDouble(hash,FcsFactSojBean.C_PRECIOAPLICADO));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_USUMODIFICACION));
			bean.setIdGuardia		(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_IDGUARDIA));
			bean.setFechaApertura	(UtilidadesHash.getString(hash,FcsFactSojBean.C_FECHA_APERTURA));
			bean.setIdTurno			(UtilidadesHash.getInteger(hash,FcsFactSojBean.C_IDTURNO));
			
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
			FcsFactSojBean b = (FcsFactSojBean) bean;
			UtilidadesHash.set(htData, FcsFactSojBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_IDTIPOSOJ, b.getIdTipoSoj().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_NUMERO, b.getNumero().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_FECHA_APERTURA, b.getFechaApertura().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_IDGUARDIA, b.getIdGuardia().toString());
			UtilidadesHash.set(htData, FcsFactSojBean.C_IDTURNO, b.getIdTurno().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsFactSojAdm.selectGenerico(). Consulta SQL:"+select);
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
					" SUM(" + FcsFactSojBean.C_PRECIOAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsFactSojBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsFactSojBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsFactSojBean.C_IDFACTURACION + "=" + idFacturacion + " " +
					" AND " + FcsFactSojBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las expedientes SOJ que hay que facturar para una persona
	 *  
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPersona
	 * @return
	 */
	public Vector getExpedientesSoj (String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT "+UtilidadesMultidioma.getCampoMultidioma("T." + ScsTipoSOJ.C_DESCRIPCION, this.usrbean.getLanguage()) + 
							", S." + ScsSOJBean.C_ANIO +
							", S." + ScsSOJBean.C_NUMERO +
							", S." + ScsSOJBean.C_IDTIPOSOJ +
							", F." + FcsFactSojBean.C_PRECIOAPLICADO +
							", S." + ScsSOJBean.C_FECHAAPERTURA +
							" FROM " + ScsSOJBean.T_NOMBRETABLA + " S" +
							" ," + ScsTipoSOJ.T_NOMBRETABLA + " T" +
							" ," + FcsFactSojBean.T_NOMBRETABLA + " F" +
							" WHERE S." + ScsSOJBean.C_IDTIPOSOJ + "= T." +ScsTipoSOJ.C_IDTIPOSOJ +
							" AND S." + ScsSOJBean.C_IDINSTITUCION + "= F." +FcsFactSojBean.C_IDINSTITUCION +
							" AND S." + ScsSOJBean.C_IDTIPOSOJ + "= F." +FcsFactSojBean.C_IDTIPOSOJ +
							" AND S." + ScsSOJBean.C_ANIO + "= F." +FcsFactSojBean.C_ANIO +
							" AND S." + ScsSOJBean.C_NUMERO + "= F." +FcsFactSojBean.C_NUMERO +
							" AND F." + FcsFactSojBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsFactSojBean.C_IDFACTURACION + "=" + idFacturacion +
							" AND F." + FcsFactSojBean.C_IDPERSONA + "=" + idPersona +
							" ORDER BY S." + ScsSOJBean.C_FECHAAPERTURA + " DESC ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactActuacionDesignaAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}

}


