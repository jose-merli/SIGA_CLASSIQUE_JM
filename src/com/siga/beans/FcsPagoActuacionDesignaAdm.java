//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsPagoActuacionDesignaAdm extends MasterBeanAdministrador {

	
	public FcsPagoActuacionDesignaAdm(UsrBean usuario) {
		super(FcsPagoActuacionDesignaBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoActuacionDesignaBean.C_ANIO,
							FcsPagoActuacionDesignaBean.C_FECHAMODIFICACION,
							FcsPagoActuacionDesignaBean.C_IDFACTURACION,
							FcsPagoActuacionDesignaBean.C_IDINSTITUCION,
							FcsPagoActuacionDesignaBean.C_IDPAGOSJG,
							FcsPagoActuacionDesignaBean.C_IDPERSONA,
							FcsPagoActuacionDesignaBean.C_IDPERSONASOCIEDAD,
							FcsPagoActuacionDesignaBean.C_IDTURNO,
							FcsPagoActuacionDesignaBean.C_IMPORTEIRPF,
							FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO,
							FcsPagoActuacionDesignaBean.C_NUMERO,
							FcsPagoActuacionDesignaBean.C_NUMEROASUNTO,
							FcsPagoActuacionDesignaBean.C_PORCENTAJEIRPF,
							FcsPagoActuacionDesignaBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoActuacionDesignaBean.C_IDINSTITUCION,	FcsPagoActuacionDesignaBean.C_IDPAGOSJG,
							FcsPagoActuacionDesignaBean.C_NUMEROASUNTO,		FcsPagoActuacionDesignaBean.C_NUMERO,
							FcsPagoActuacionDesignaBean.C_ANIO,				FcsPagoActuacionDesignaBean.C_IDTURNO,
							FcsPagoActuacionDesignaBean.C_IDFACTURACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoActuacionDesignaBean bean = null;
		
		try {
			bean = new FcsPagoActuacionDesignaBean();
			bean.setAnio			 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_ANIO));
			bean.setFechaMod		 (UtilidadesHash.getString(hash,FcsPagoActuacionDesignaBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion	 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_IDFACTURACION));
			bean.setIdInstitucion	 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_IDINSTITUCION));
			bean.setIdPagosJG		 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_IDPAGOSJG));
			bean.setIdPersona		 (UtilidadesHash.getLong(hash,FcsPagoActuacionDesignaBean.C_IDPERSONA));
			bean.setIdPersonaSociedad(UtilidadesHash.getLong(hash,FcsPagoActuacionDesignaBean.C_IDPERSONASOCIEDAD));
			bean.setIdTurno			 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_IDTURNO));
			bean.setImporteIRPF		 (UtilidadesHash.getFloat(hash,FcsPagoActuacionDesignaBean.C_IMPORTEIRPF));
			bean.setImportePagado	 (UtilidadesHash.getFloat(hash,FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO));
			bean.setNumero			 (UtilidadesHash.getLong(hash,FcsPagoActuacionDesignaBean.C_NUMERO));
			bean.setNumeroAsunto	 (UtilidadesHash.getLong(hash,FcsPagoActuacionDesignaBean.C_NUMEROASUNTO));
			bean.setPorcentajeIRPF	 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_PORCENTAJEIRPF));
			bean.setUsuMod			 (UtilidadesHash.getInteger(hash,FcsPagoActuacionDesignaBean.C_USUMODIFICACION));
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
			FcsPagoActuacionDesignaBean b = (FcsPagoActuacionDesignaBean) bean;
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IDPAGOSJG, b.getIdPagosJG());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IDFACTURACION, b.getIdFacturacion());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IDPERSONASOCIEDAD, b.getIdPersonaSociedad());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IDTURNO, b.getIdTurno());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IMPORTEIRPF, b.getImporteIRPF());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO, b.getImportePagado());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_NUMEROASUNTO, b.getNumeroAsunto());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_PORCENTAJEIRPF, b.getPorcentajeIRPF());
			UtilidadesHash.set(htData, FcsPagoActuacionDesignaBean.C_USUMODIFICACION, b.getUsuMod());
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoActuacionDesignaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Devuelve el valor del importe que hay que pagar para un colegiado en una facturacion determinada y el IRPF
	 *   
	 * @param String idInstitucion 
	 * @param String idPago
	 * @param String idPersona
	 * 
	 * @return Hashtable resultado con el importe y el IRPF
	 */
	public Hashtable getImportePagado (String idInstitucion, String idPago, String idPersona)
	{
		Hashtable resultado = new Hashtable();
		String resultado1 = "", consulta = "", resultado2 ="";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO + ") AS IMPORTE, " +
					" SUM(" + FcsPagoActuacionDesignaBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsPagoActuacionDesignaBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsPagoActuacionDesignaBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsPagoActuacionDesignaBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsPagoActuacionDesignaBean.C_IDPERSONA + "=" + idPersona + " ";
		
		//Hashtable para recoger el resultado de la contulta
		Hashtable hash = new Hashtable();
		try{
			hash = (Hashtable)((Vector)this.selectGenerico(consulta)).get(0);
			//resogemos el resultado
			resultado1 = (String)hash.get("IMPORTE");
			if (resultado1.equals(""))resultado1="0";
			resultado2 = (String)hash.get("IRPF");
			if (resultado2.equals(""))resultado2="0";
		}catch(Exception e){
			//si no se ha obtenido resultado es porque no hay nada que facturar para el colegiado con ese idPersona
			resultado1 = "0";
			resultado2 = "0";
		}
		resultado.put("IMPORTE",resultado1);
		resultado.put("IRPF",resultado2);
		return resultado;
	}
	
	
	/**
	 * Devuelve un vector con los turnos de oficio que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getTurnosOficio (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", T." + ScsTurnoBean.C_IDINSTITUCION + 
							", T." + ScsTurnoBean.C_IDTURNO +
							", D." + ScsDesignaBean.C_RESUMENASUNTO +
							", A." + ScsActuacionDesignaBean.C_FECHA +
							", A." + ScsActuacionDesignaBean.C_ANIO +
							", A." + ScsActuacionDesignaBean.C_NUMERO +
							", F." + FcsPagoActuacionDesignaBean.C_IMPORTEPAGADO +
							", F." + FcsPagoActuacionDesignaBean.C_IMPORTEIRPF +
							", F." + FcsPagoActuacionDesignaBean.C_IDPAGOSJG +
							", F." + FcsPagoActuacionDesignaBean.C_NUMEROASUNTO +
							", F." + FcsPagoActuacionDesignaBean.C_PORCENTAJEIRPF +
							", F." + FcsPagoActuacionDesignaBean.C_IDPERSONA +
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsDesignaBean.T_NOMBRETABLA + " D" +
							" ," + ScsActuacionDesignaBean.T_NOMBRETABLA + " A" +
							" ," + FcsPagoActuacionDesignaBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= D." +ScsDesignaBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= D." +ScsDesignaBean.C_IDTURNO  +
							" AND D." + ScsDesignaBean.C_IDINSTITUCION + "= A." +ScsActuacionDesignaBean.C_IDINSTITUCION +
							" AND D." + ScsDesignaBean.C_IDTURNO + "= A." +ScsActuacionDesignaBean.C_IDTURNO +
							" AND D." + ScsDesignaBean.C_ANIO + "= A." +ScsActuacionDesignaBean.C_ANIO +
							" AND D." + ScsDesignaBean.C_NUMERO + "= A." +ScsActuacionDesignaBean.C_NUMERO +
							" AND A." + ScsActuacionDesignaBean.C_IDINSTITUCION + "= F." +FcsPagoActuacionDesignaBean.C_IDINSTITUCION +
							" AND A." + ScsActuacionDesignaBean.C_IDTURNO + "= F." +FcsPagoActuacionDesignaBean.C_IDTURNO +
							" AND A." + ScsActuacionDesignaBean.C_ANIO + "= F." +FcsPagoActuacionDesignaBean.C_ANIO +
							" AND A." + ScsActuacionDesignaBean.C_NUMERO + "= F." +FcsPagoActuacionDesignaBean.C_NUMERO +
							" AND A." + ScsActuacionDesignaBean.C_NUMEROASUNTO + "= F." +FcsPagoActuacionDesignaBean.C_NUMEROASUNTO +
							" AND F." + FcsPagoActuacionDesignaBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsPagoActuacionDesignaBean.C_IDPAGOSJG + "=" + idPago +
							" AND F." + FcsPagoActuacionDesignaBean.C_IDPERSONA + "=" + idPersona +" ";
							
		try {
			resultado = (Vector)this.selectGenerico(consulta);
		}
		catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsPagoActuacionDesignaAdm.getTurnosOficio"+consulta);
		}
		return resultado;
	}
}


