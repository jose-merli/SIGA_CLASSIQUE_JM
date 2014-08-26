//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsFactGuardiasColegiadoAdm extends MasterBeanAdministrador {

	
	public FcsFactGuardiasColegiadoAdm(UsrBean usuario) {
		super(FcsFactGuardiasColegiadoBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactGuardiasColegiadoBean.C_IDAPUNTE,	FcsFactGuardiasColegiadoBean.C_FECHAINICIO,
							FcsFactGuardiasColegiadoBean.C_FECHAMODIFICACION,	
							FcsFactGuardiasColegiadoBean.C_IDFACTURACION,	FcsFactGuardiasColegiadoBean.C_IDGUARDIA,
							FcsFactGuardiasColegiadoBean.C_IDINSTITUCION,	FcsFactGuardiasColegiadoBean.C_IDPERSONA,
							FcsFactGuardiasColegiadoBean.C_IDTURNO,	FcsFactGuardiasColegiadoBean.C_PRECIOAPLICADO,
							FcsFactGuardiasColegiadoBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactGuardiasColegiadoBean.C_IDINSTITUCION,		FcsFactGuardiasColegiadoBean.C_IDFACTURACION,
							FcsFactGuardiasColegiadoBean.C_IDTURNO,				FcsFactGuardiasColegiadoBean.C_IDGUARDIA,
							FcsFactGuardiasColegiadoBean.C_IDPERSONA,
							FcsFactGuardiasColegiadoBean.C_IDAPUNTE,				FcsFactGuardiasColegiadoBean.C_FECHAINICIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactGuardiasColegiadoBean bean = null;
		
		try {
			bean = new FcsFactGuardiasColegiadoBean();
			bean.setFechaInicio			(UtilidadesHash.getString(hash,FcsFactGuardiasColegiadoBean.C_FECHAINICIO));
			bean.setFechaMod			(UtilidadesHash.getString(hash,FcsFactGuardiasColegiadoBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion		(UtilidadesHash.getInteger(hash,FcsFactGuardiasColegiadoBean.C_IDFACTURACION));
			bean.setIdGuardia			(UtilidadesHash.getInteger(hash,FcsFactGuardiasColegiadoBean.C_IDGUARDIA));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash,FcsFactGuardiasColegiadoBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getLong(hash,FcsFactGuardiasColegiadoBean.C_IDPERSONA));
			bean.setIdTurno				(UtilidadesHash.getInteger(hash,FcsFactGuardiasColegiadoBean.C_IDTURNO));
			bean.setPrecioAplicado		(UtilidadesHash.getDouble(hash,FcsFactGuardiasColegiadoBean.C_PRECIOAPLICADO));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash,FcsFactGuardiasColegiadoBean.C_USUMODIFICACION));
			bean.setIdApunte			(UtilidadesHash.getLong(hash,FcsFactGuardiasColegiadoBean.C_IDAPUNTE));
			
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
			FcsFactGuardiasColegiadoBean b = (FcsFactGuardiasColegiadoBean) bean;
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_FECHAINICIO, b.getFechaInicio().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_IDGUARDIA, b.getIdGuardia().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_IDTURNO, b.getIdTurno().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactGuardiasColegiadoBean.C_IDAPUNTE, b.getIdApunte().toString());
			
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
			throw new ClsExceptions (e, "Excepcion en FcsFactGuardiasColegiadoAdm.selectGenerico(). Consulta SQL:"+select);
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
					" SUM(" + FcsFactGuardiasColegiadoBean.C_PRECIOAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsFactGuardiasColegiadoBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsFactGuardiasColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsFactGuardiasColegiadoBean.C_IDFACTURACION + "=" + idFacturacion + " " +
					" AND " + FcsFactGuardiasColegiadoBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las guardias que hay que facturar para una persona
	 *  
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPersona
	 * @return
	 */
	public Vector getGuardiasPresenciales (String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", G." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIAS " +
							", C." + ScsGuardiasColegiadoBean.C_FECHAINICIO +
							", C." + ScsGuardiasColegiadoBean.C_FECHAFIN +
							", F." + FcsFactGuardiasColegiadoBean.C_PRECIOAPLICADO +
							", C." + ScsGuardiasColegiadoBean.C_DIASACOBRAR +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " G" +
							" ," + ScsGuardiasColegiadoBean.T_NOMBRETABLA + " C" +
							" ," + FcsFactGuardiasColegiadoBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= G." +ScsGuardiasTurnoBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= G." +ScsGuardiasTurnoBean.C_IDTURNO  +
							" AND G." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "= C." +ScsGuardiasColegiadoBean.C_IDINSTITUCION +
							" AND G." + ScsGuardiasTurnoBean.C_IDTURNO + "= C." +ScsGuardiasColegiadoBean.C_IDTURNO +
							" AND G." + ScsGuardiasTurnoBean.C_IDGUARDIA + "= C." +ScsGuardiasColegiadoBean.C_IDGUARDIA +
							" AND C." + ScsGuardiasColegiadoBean.C_IDINSTITUCION + "= F." +FcsFactGuardiasColegiadoBean.C_IDINSTITUCION +
							" AND C." + ScsGuardiasColegiadoBean.C_IDTURNO + "= F." +FcsFactGuardiasColegiadoBean.C_IDTURNO +
							" AND C." + ScsGuardiasColegiadoBean.C_IDGUARDIA + "= F." +FcsFactGuardiasColegiadoBean.C_IDGUARDIA +
							" AND C." + ScsGuardiasColegiadoBean.C_IDPERSONA + "= F." +FcsFactGuardiasColegiadoBean.C_IDPERSONA +
							" AND C." + ScsGuardiasColegiadoBean.C_FECHAINICIO + "= F." +FcsFactGuardiasColegiadoBean.C_FECHAINICIO +
							" AND F." + FcsFactGuardiasColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsFactGuardiasColegiadoBean.C_IDFACTURACION + "=" + idFacturacion +
							" AND F." + FcsFactGuardiasColegiadoBean.C_IDPERSONA + "=" + idPersona +
							" ORDER BY C." + ScsGuardiasColegiadoBean.C_FECHAINICIO + " DESC ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactGuardiasColegiadoAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
	
}