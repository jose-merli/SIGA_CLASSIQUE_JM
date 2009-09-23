//VERSIONES:
//ruben.fernandez 04-04-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;


public class FcsPagoEjgAdm extends MasterBeanAdministrador {

	
	public FcsPagoEjgAdm(UsrBean usuario) {
		super(FcsPagoEjgBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoEjgBean.C_ANIO,
							FcsPagoEjgBean.C_FECHAMODIFICACION,		
							FcsPagoEjgBean.C_IDFACTURACION,
							FcsPagoEjgBean.C_IDINSTITUCION,		
							FcsPagoEjgBean.C_IDPAGOSJG,
							FcsPagoEjgBean.C_IDPERSONA,
							FcsPagoEjgBean.C_IDPERSONASOCIEDAD,
							FcsPagoEjgBean.C_IDTIPOEJG,
							FcsPagoEjgBean.C_IMPORTEIRPF,
							FcsPagoEjgBean.C_IMPORTEPAGADO,
							FcsPagoEjgBean.C_NUMERO,
							FcsPagoEjgBean.C_PORCENTAJEIRPF,
							FcsPagoEjgBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoEjgBean.C_IDINSTITUCION,	FcsPagoEjgBean.C_IDPAGOSJG, FcsPagoEjgBean.C_IDFACTURACION,
							FcsPagoEjgBean.C_IDTIPOEJG,		FcsPagoEjgBean.C_NUMERO,	FcsPagoEjgBean.C_ANIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoEjgBean bean = null;
		
		try {
			bean = new FcsPagoEjgBean();
			bean.setAnio			(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_ANIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsPagoEjgBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion	(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_IDFACTURACION));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_IDINSTITUCION));
			bean.setIdPagosJG		(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_IDPAGOSJG));
			bean.setIdPersona		(UtilidadesHash.getLong(hash,FcsPagoEjgBean.C_IDPERSONA));
			bean.setIdPersonaSociedad(UtilidadesHash.getLong(hash,FcsPagoEjgBean.C_IDPERSONASOCIEDAD));
			bean.setImporteIRPF		(UtilidadesHash.getDouble(hash,FcsPagoEjgBean.C_IMPORTEIRPF));
			bean.setImportePagado	(UtilidadesHash.getDouble(hash,FcsPagoEjgBean.C_IMPORTEPAGADO));
			bean.setIdTipoEjg		(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_IDTIPOEJG));
			bean.setNumero			(UtilidadesHash.getLong(hash,FcsPagoEjgBean.C_NUMERO));
			bean.setPorcentajeIRPF	(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_PORCENTAJEIRPF));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsPagoEjgBean.C_USUMODIFICACION));
			
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
			FcsPagoEjgBean b = (FcsPagoEjgBean) bean;
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IDPAGOSJG, b.getIdPagosJG());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IDFACTURACION, b.getIdFacturacion());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IDPERSONASOCIEDAD, b.getIdPersonaSociedad());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IMPORTEIRPF, b.getImporteIRPF());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IMPORTEPAGADO, b.getImportePagado());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_IDTIPOEJG, b.getIdTipoEjg());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_PORCENTAJEIRPF, b.getPorcentajeIRPF());
			UtilidadesHash.set(htData, FcsPagoEjgBean.C_USUMODIFICACION, b.getUsuMod());
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoEjgAdm.selectGenerico(). Consulta SQL:"+select);
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
	public Hashtable getImportePagado (String idInstitucion, String idPago, String idPersona)
	{
		Hashtable resultado = new Hashtable();
		String resultado1 = "", resultado2 = "", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsPagoEjgBean.C_IMPORTEPAGADO + ") AS IMPORTE, " +
					" SUM(" + FcsPagoEjgBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsPagoEjgBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsPagoEjgBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsPagoEjgBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsPagoEjgBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las expedientes SOJ que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getExpedientesEjg (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT  " + UtilidadesMultidioma.getCampoMultidioma("T." + ScsTipoEJGBean.C_DESCRIPCION,this.usrbean.getLanguage()) +  
							", S." + ScsEJGBean.C_ANIO +
							", S." + ScsEJGBean.C_NUMERO +
							", S." + ScsEJGBean.C_IDINSTITUCION +
							", S." + ScsEJGBean.C_IDTIPOEJG +
							", S." + ScsEJGBean.C_FECHAAPERTURA +
							", F." + FcsPagoEjgBean.C_IDPAGOSJG +
							", F." + FcsPagoEjgBean.C_IMPORTEPAGADO +
							", F." + FcsPagoEjgBean.C_IMPORTEIRPF +
							", F." + FcsPagoEjgBean.C_PORCENTAJEIRPF +
							", D." + ScsEJGDESIGNABean.C_IDTURNO +
							" FROM " + ScsEJGBean.T_NOMBRETABLA + " S" +
							" ," + ScsTipoEJGBean.T_NOMBRETABLA + " T" +
							" ," + FcsPagoEjgBean.T_NOMBRETABLA + " F" +
							" ," + ScsEJGDESIGNABean.T_NOMBRETABLA + " D" +
							" WHERE S." + ScsEJGBean.C_IDTIPOEJG + "= T." +ScsTipoEJGBean.C_IDTIPOEJG +
							" AND S." + ScsEJGBean.C_IDINSTITUCION + "= F." +FcsPagoEjgBean.C_IDINSTITUCION +
							" AND S." + ScsEJGBean.C_IDTIPOEJG + "= F." +FcsPagoEjgBean.C_IDTIPOEJG +
							" AND S." + ScsEJGBean.C_ANIO + "= F." +FcsPagoEjgBean.C_ANIO +
							" AND S." + ScsEJGBean.C_NUMERO + "= F." +FcsPagoEjgBean.C_NUMERO +
							" AND F." + FcsPagoEjgBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsPagoEjgBean.C_IDPAGOSJG + "=" + idPago +
							" AND S." + ScsEJGBean.C_IDTIPOEJG + "= D." +ScsEJGDESIGNABean.C_IDTIPOEJG +
							" AND S." + ScsEJGBean.C_ANIO + "= D." +ScsEJGDESIGNABean.C_ANIOEJG +
							" AND S." + ScsEJGBean.C_NUMERO + "= D." +ScsEJGDESIGNABean.C_NUMEROEJG +
							" AND S." + ScsEJGBean.C_IDINSTITUCION + "= D." +ScsEJGDESIGNABean.C_IDINSTITUCION +
							" AND F." + FcsPagoEjgBean.C_IDPERSONA + "=" + idPersona +" ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactEjgAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
}


