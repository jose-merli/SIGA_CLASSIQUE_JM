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


public class FcsFactEjgAdm extends MasterBeanAdministrador {

	
	public FcsFactEjgAdm(UsrBean usuario) {
		super(FcsFactEjgBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactEjgBean.C_ANIO,				FcsFactEjgBean.C_FECHA_APERTURA,
							FcsFactEjgBean.C_FECHAMODIFICACION,	FcsFactEjgBean.C_IDFACTURACION,
							FcsFactEjgBean.C_IDGUARDIA,			FcsFactEjgBean.C_IDINSTITUCION,
							FcsFactEjgBean.C_IDPERSONA,			FcsFactEjgBean.C_IDTIPOEJG,
							FcsFactEjgBean.C_IDTURNO,			FcsFactEjgBean.C_NUMERO,
							FcsFactEjgBean.C_PRECIOAPLICADO,	FcsFactEjgBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactEjgBean.C_IDINSTITUCION,	FcsFactEjgBean.C_IDFACTURACION,
							FcsFactEjgBean.C_IDTIPOEJG,		FcsFactEjgBean.C_NUMERO,
							FcsFactEjgBean.C_ANIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactEjgBean bean = null;
		
		try {
			bean = new FcsFactEjgBean();
			bean.setAnio			(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_ANIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsFactEjgBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion	(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_IDFACTURACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_IDINSTITUCION));
			bean.setIdPersona		(UtilidadesHash.getLong(hash,FcsFactEjgBean.C_IDPERSONA));
			bean.setIdTipoEjg		(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_IDTIPOEJG));
			bean.setNumero			(UtilidadesHash.getLong(hash,FcsFactEjgBean.C_NUMERO));
			bean.setPrecioAplicado	(UtilidadesHash.getDouble(hash,FcsFactEjgBean.C_PRECIOAPLICADO));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_USUMODIFICACION));
			bean.setFechaApertura	(UtilidadesHash.getString(hash,FcsFactEjgBean.C_FECHA_APERTURA));
			bean.setIdGuardia		(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_IDGUARDIA));
			bean.setFechaApertura	(UtilidadesHash.getString(hash,FcsFactEjgBean.C_FECHA_APERTURA));
			bean.setIdTurno			(UtilidadesHash.getInteger(hash,FcsFactEjgBean.C_IDTURNO));
			
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
			FcsFactEjgBean b = (FcsFactEjgBean) bean;
			UtilidadesHash.set(htData, FcsFactEjgBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_IDTIPOEJG, b.getIdTipoEjg().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_NUMERO, b.getNumero().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_FECHA_APERTURA, b.getFechaApertura().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_IDTURNO, b.getIdTurno().toString());
			UtilidadesHash.set(htData, FcsFactEjgBean.C_IDGUARDIA, b.getIdGuardia().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsFactEjgAdm.selectGenerico(). Consulta SQL:"+select);
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
					" SUM(" + FcsFactEjgBean.C_PRECIOAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsFactEjgBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsFactEjgBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsFactEjgBean.C_IDFACTURACION + "=" + idFacturacion + " " +
					" AND " + FcsFactEjgBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	public Vector getExpedientesEjg (String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT  " + UtilidadesMultidioma.getCampoMultidioma("T." + ScsTipoEJGBean.C_DESCRIPCION,this.usrbean.getLanguage()) + 
							", S." + ScsEJGBean.C_ANIO +
							", S." + ScsEJGBean.C_NUMERO +
							", S." + ScsEJGBean.C_IDTIPOEJG +
							", F." + FcsFactEjgBean.C_PRECIOAPLICADO +
							", S." + ScsEJGBean.C_FECHAAPERTURA +
							" FROM " + ScsEJGBean.T_NOMBRETABLA + " S" +
							" ," + ScsTipoEJGBean.T_NOMBRETABLA + " T" +
							" ," + FcsFactEjgBean.T_NOMBRETABLA + " F" +
							" WHERE S." + ScsEJGBean.C_IDTIPOEJG + "= T." +ScsTipoEJGBean.C_IDTIPOEJG +
							" AND S." + ScsEJGBean.C_IDINSTITUCION + "= F." +FcsFactEjgBean.C_IDINSTITUCION +
							" AND S." + ScsEJGBean.C_IDTIPOEJG + "= F." +FcsFactEjgBean.C_IDTIPOEJG +
							" AND S." + ScsEJGBean.C_ANIO + "= F." +FcsFactEjgBean.C_ANIO +
							" AND S." + ScsEJGBean.C_NUMERO + "= F." +FcsFactEjgBean.C_NUMERO +
							" AND F." + FcsFactEjgBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsFactEjgBean.C_IDFACTURACION + "=" + idFacturacion +
							" AND F." + FcsFactEjgBean.C_IDPERSONA + "=" + idPersona +
							" ORDER BY S." + ScsEJGBean.C_FECHAAPERTURA + " DESC ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactEjgAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
}


