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


public class FcsPagoSojAdm extends MasterBeanAdministrador {

	
	public FcsPagoSojAdm(UsrBean usuario) {
		super(FcsPagoSojBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoSojBean.C_ANIO,
							FcsPagoSojBean.C_FECHAMODIFICACION,
							FcsPagoSojBean.C_IDFACTURACION,
							FcsPagoSojBean.C_IDINSTITUCION,
							FcsPagoSojBean.C_IDPAGOSJG,
							FcsPagoSojBean.C_IDPERSONA,
							FcsPagoSojBean.C_IDPERSONASOCIEDAD,
							FcsPagoSojBean.C_IDTIPOSOJ,
							FcsPagoSojBean.C_IMPORTEIRPF,
							FcsPagoSojBean.C_IMPORTEPAGADO,
							FcsPagoSojBean.C_NUMERO,
							FcsPagoSojBean.C_PORCENTAJEIRPF,
							FcsPagoSojBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoSojBean.C_IDINSTITUCION,	FcsPagoSojBean.C_IDPAGOSJG, FcsPagoSojBean.C_IDFACTURACION,
							FcsPagoSojBean.C_IDTIPOSOJ,		FcsPagoSojBean.C_NUMERO,	FcsPagoSojBean.C_ANIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoSojBean bean = null;
		
		try {
			bean = new FcsPagoSojBean();
			bean.setAnio			 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_ANIO));
			bean.setFechaMod		 (UtilidadesHash.getString(hash,FcsPagoSojBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion	 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_IDFACTURACION));
			bean.setIdInstitucion	 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_IDINSTITUCION));
			bean.setIdPagosJg		 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_IDPAGOSJG));
			bean.setIdPersona		 (UtilidadesHash.getLong(hash,FcsPagoSojBean.C_IDPERSONA));
			bean.setIdPersonaSociedad(UtilidadesHash.getLong(hash,FcsPagoSojBean.C_IDPERSONASOCIEDAD));
			bean.setIdTipoSoj		 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_IDTIPOSOJ));
			bean.setImporteIRPF		 (UtilidadesHash.getDouble(hash,FcsPagoSojBean.C_IMPORTEIRPF));
			bean.setImportePagado	 (UtilidadesHash.getDouble(hash,FcsPagoSojBean.C_IMPORTEPAGADO));
			bean.setNumero			 (UtilidadesHash.getLong(hash,FcsPagoSojBean.C_NUMERO));
			bean.setPorcentajeIRPF	 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_PORCENTAJEIRPF));
			bean.setUsuMod			 (UtilidadesHash.getInteger(hash,FcsPagoSojBean.C_USUMODIFICACION));
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
			FcsPagoSojBean b = (FcsPagoSojBean) bean;
			UtilidadesHash.set(htData, FcsPagoSojBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IDFACTURACION, b.getIdFacturacion());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IDPAGOSJG, b.getIdPagosJg());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IDPERSONASOCIEDAD, b.getIdPersonaSociedad());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IDTIPOSOJ, b.getIdTipoSoj());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IMPORTEIRPF, b.getImporteIRPF());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_IMPORTEPAGADO, b.getImportePagado());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_PORCENTAJEIRPF, b.getPorcentajeIRPF());
			UtilidadesHash.set(htData, FcsPagoSojBean.C_USUMODIFICACION, b.getUsuMod());
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoSojAdm.selectGenerico(). Consulta SQL:"+select);
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
					" SUM(" + FcsPagoSojBean.C_IMPORTEPAGADO + ") AS IMPORTE, " +
					" SUM(" + FcsPagoSojBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsPagoSojBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsPagoSojBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsPagoSojBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsPagoSojBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	public Vector getExpedientesSoj (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT "+UtilidadesMultidioma.getCampoMultidioma("T." + ScsTipoSOJ.C_DESCRIPCION, this.usrbean.getLanguage()) +
							", S." + ScsSOJBean.C_ANIO +
							", S." + ScsSOJBean.C_IDINSTITUCION +
							", S." + ScsSOJBean.C_NUMERO +
							", S." + ScsSOJBean.C_IDTIPOSOJ +
							", S." + ScsSOJBean.C_FECHAAPERTURA +
							", F." + FcsPagoSojBean.C_IDPAGOSJG +
							", F." + FcsPagoSojBean.C_IMPORTEPAGADO +
							", F." + FcsPagoSojBean.C_IMPORTEIRPF +
							", F." + FcsPagoSojBean.C_PORCENTAJEIRPF +
							", S." + ScsSOJBean.C_IDTURNO +
							" FROM " + ScsSOJBean.T_NOMBRETABLA + " S" +
							" ," + ScsTipoSOJ.T_NOMBRETABLA + " T" +
							" ," + FcsPagoSojBean.T_NOMBRETABLA + " F" +
							" WHERE S." + ScsSOJBean.C_IDTIPOSOJ + "= T." +ScsTipoSOJ.C_IDTIPOSOJ +
							" AND S." + ScsSOJBean.C_IDINSTITUCION + "= F." +FcsPagoSojBean.C_IDINSTITUCION +
							" AND S." + ScsSOJBean.C_IDTIPOSOJ + "= F." +FcsPagoSojBean.C_IDTIPOSOJ +
							" AND S." + ScsSOJBean.C_ANIO + "= F." +FcsPagoSojBean.C_ANIO +
							" AND S." + ScsSOJBean.C_NUMERO + "= F." +FcsPagoSojBean.C_NUMERO +
							" AND F." + FcsPagoSojBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsPagoSojBean.C_IDPAGOSJG + "=" + idPago +
							" AND F." + FcsPagoSojBean.C_IDPERSONA + "=" + idPersona +" ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}
		catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsFactActuacionDesignaAdm.getTurnosOficio"+consulta);
		}
		return resultado;
	}
}