//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsFactActuacionDesignaAdm extends MasterBeanAdministrador {

	
	public FcsFactActuacionDesignaAdm(UsrBean usuario) {
		super(FcsFactActuacionDesignaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsFactActuacionDesignaBean.C_ACREDITACION,				FcsFactActuacionDesignaBean.C_ANIO,
							FcsFactActuacionDesignaBean.C_CODIGOPROCEDIMIENTO,		FcsFactActuacionDesignaBean.C_FECHA_ACTUACION,
							FcsFactActuacionDesignaBean.C_FECHA_JUSTIFICACION,		FcsFactActuacionDesignaBean.C_FECHAMODIFICACION,
							FcsFactActuacionDesignaBean.C_IDFACTURACION,			FcsFactActuacionDesignaBean.C_IDINSTITUCION,
							FcsFactActuacionDesignaBean.C_IDPERSONA,				FcsFactActuacionDesignaBean.C_IDTURNO,
							FcsFactActuacionDesignaBean.C_PORCENTAJEFACTURADO,		FcsFactActuacionDesignaBean.C_NUMERO,
							FcsFactActuacionDesignaBean.C_USUMODIFICACION,			FcsFactActuacionDesignaBean.C_PRECIOAPLICADO,			
							FcsFactActuacionDesignaBean.C_PROCEDIMIENTO,			FcsFactActuacionDesignaBean.C_NUMEROASUNTO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactActuacionDesignaBean.C_IDINSTITUCION,	FcsFactActuacionDesignaBean.C_IDFACTURACION,
							FcsFactActuacionDesignaBean.C_NUMEROASUNTO,		FcsFactActuacionDesignaBean.C_NUMERO,
							FcsFactActuacionDesignaBean.C_ANIO,				FcsFactActuacionDesignaBean.C_IDTURNO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactActuacionDesignaBean bean = null;
		
		try {
			bean = new FcsFactActuacionDesignaBean();
			bean.setAnio			(UtilidadesHash.getInteger(hash,FcsFactActuacionDesignaBean.C_ANIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsFactActuacionDesignaBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion	(UtilidadesHash.getInteger(hash,FcsFactActuacionDesignaBean.C_IDFACTURACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsFactActuacionDesignaBean.C_IDINSTITUCION));
			bean.setIdPersona		(UtilidadesHash.getLong(hash,FcsFactActuacionDesignaBean.C_IDPERSONA));
			bean.setProcedimiento	(UtilidadesHash.getString(hash,FcsFactActuacionDesignaBean.C_PROCEDIMIENTO));
			bean.setIdTurno			(UtilidadesHash.getInteger(hash,FcsFactActuacionDesignaBean.C_IDTURNO));
			bean.setNumero			(UtilidadesHash.getLong(hash,FcsFactActuacionDesignaBean.C_NUMERO));
			bean.setNumeroAsunto	(UtilidadesHash.getLong(hash,FcsFactActuacionDesignaBean.C_NUMEROASUNTO));
			bean.setPorcentajeFacturado(UtilidadesHash.getInteger(hash,FcsFactActuacionDesignaBean.C_PORCENTAJEFACTURADO));
			bean.setPrecioAplicado	(UtilidadesHash.getDouble(hash,FcsFactActuacionDesignaBean.C_PRECIOAPLICADO));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsFactActuacionDesignaBean.C_USUMODIFICACION));
			bean.setAcreditacion	(UtilidadesHash.getString(hash,FcsFactActuacionDesignaBean.C_ACREDITACION));
			bean.setCodigoProcedimiento	(UtilidadesHash.getString(hash,FcsFactActuacionDesignaBean.C_CODIGOPROCEDIMIENTO));
			bean.setFechaActuacion	(UtilidadesHash.getString(hash,FcsFactActuacionDesignaBean.C_FECHA_ACTUACION));
			bean.setFechaJustificacion	(UtilidadesHash.getString(hash,FcsFactActuacionDesignaBean.C_FECHA_JUSTIFICACION));
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
			FcsFactActuacionDesignaBean b = (FcsFactActuacionDesignaBean) bean;
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_IDFACTURACION, b.getIdFacturacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_PROCEDIMIENTO, b.getProcedimiento().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_IDTURNO, b.getIdTurno().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_NUMERO, b.getNumero().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_NUMEROASUNTO, b.getNumeroAsunto().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_PORCENTAJEFACTURADO, b.getPorcentajeFacturado().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_PRECIOAPLICADO, b.getPrecioAplicado().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_USUMODIFICACION, b.getUsuMod().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_CODIGOPROCEDIMIENTO, b.getCodigoProcedimiento().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_FECHA_ACTUACION, b.getFechaActuacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_FECHA_JUSTIFICACION, b.getFechaJustificacion().toString());
			UtilidadesHash.set(htData, FcsFactActuacionDesignaBean.C_CODIGOPROCEDIMIENTO, b.getCodigoProcedimiento().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsFactActuacionDesignaAdm.selectGenerico(). Consulta SQL:"+select);
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
					" SUM(" + FcsFactActuacionDesignaBean.C_PRECIOAPLICADO + ") AS IMPORTE " +
					" FROM " + FcsFactActuacionDesignaBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsFactActuacionDesignaBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsFactActuacionDesignaBean.C_IDFACTURACION + "=" + idFacturacion + " " +
					" AND " + FcsFactActuacionDesignaBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con los turnos de oficio que hay que facturar para una persona
	 *  
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPersona
	 * @return
	 */
	public Vector getTurnosOficio (String idInstitucion, String idFacturacion, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", D." + ScsDesignaBean.C_RESUMENASUNTO +
							", A." + ScsActuacionDesignaBean.C_FECHA +
							", A." + ScsActuacionDesignaBean.C_ANIO +
							", A." + ScsActuacionDesignaBean.C_NUMERO +
							", F." + FcsFactActuacionDesignaBean.C_PRECIOAPLICADO +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsDesignaBean.T_NOMBRETABLA + " D" +
							" ," + ScsActuacionDesignaBean.T_NOMBRETABLA + " A" +
							" ," + FcsFactActuacionDesignaBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= D." +ScsDesignaBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= D." +ScsDesignaBean.C_IDTURNO  +
							" AND D." + ScsDesignaBean.C_IDINSTITUCION + "= A." +ScsActuacionDesignaBean.C_IDINSTITUCION +
							" AND D." + ScsDesignaBean.C_IDTURNO + "= A." +ScsActuacionDesignaBean.C_IDTURNO +
							" AND D." + ScsDesignaBean.C_ANIO + "= A." +ScsActuacionDesignaBean.C_ANIO +
							" AND D." + ScsDesignaBean.C_NUMERO + "= A." +ScsActuacionDesignaBean.C_NUMERO +
							" AND A." + ScsActuacionDesignaBean.C_IDINSTITUCION + "= F." +FcsFactActuacionDesignaBean.C_IDINSTITUCION +
							" AND A." + ScsActuacionDesignaBean.C_IDTURNO + "= F." +FcsFactActuacionDesignaBean.C_IDTURNO +
							" AND A." + ScsActuacionDesignaBean.C_ANIO + "= F." +FcsFactActuacionDesignaBean.C_ANIO +
							" AND A." + ScsActuacionDesignaBean.C_NUMERO + "= F." +FcsFactActuacionDesignaBean.C_NUMERO +
							" AND A." + ScsActuacionDesignaBean.C_NUMEROASUNTO + "= F." +FcsFactActuacionDesignaBean.C_NUMEROASUNTO +
							" AND F." + FcsFactActuacionDesignaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsFactActuacionDesignaBean.C_IDFACTURACION + "=" + idFacturacion +
							" AND F." + FcsFactActuacionDesignaBean.C_IDPERSONA + "=" + idPersona +
							" ORDER BY A." + ScsActuacionDesignaBean.C_FECHA + " DESC ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactActuacionDesignaAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
	
}


