//VERSIONES:
//ruben.fernandez 29-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsPagoActuacionAsistAdm extends MasterBeanAdministrador {

	
	public FcsPagoActuacionAsistAdm(UsrBean usuario) {
		super(FcsPagoActuacionAsistBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoActuacionAsistBean.C_ANIO,				FcsPagoActuacionAsistBean.C_FECHAMODIFICACION,
							FcsPagoActuacionAsistBean.C_IDPAGOSJG,			FcsPagoActuacionAsistBean.C_IDINSTITUCION,
							FcsPagoActuacionAsistBean.C_IMPORTEIRPF,		FcsPagoActuacionAsistBean.C_IDACTUACION,
							FcsPagoActuacionAsistBean.C_IMPORTEPAGADO,		FcsPagoActuacionAsistBean.C_NUMERO,
							FcsPagoActuacionAsistBean.C_USUMODIFICACION,	FcsPagoActuacionAsistBean.C_IDPERSONASOCIEDAD};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoActuacionAsistBean.C_IDINSTITUCION,	FcsPagoActuacionAsistBean.C_IDPAGOSJG,
							FcsPagoActuacionAsistBean.C_NUMERO,			FcsPagoActuacionAsistBean.C_ANIO,
							FcsPagoActuacionAsistBean.C_IDACTUACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoActuacionAsistBean bean = null;
		
		try {
			bean = new FcsPagoActuacionAsistBean();
			bean.setAnio			(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_ANIO));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsPagoActuacionAsistBean.C_FECHAMODIFICACION));
			bean.setIdPagosJG		(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_IDPAGOSJG));
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_IDINSTITUCION));
			bean.setIdPersona		(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_IDPERSONA));
			bean.setImporteIRPF		(UtilidadesHash.getFloat(hash,FcsPagoActuacionAsistBean.C_IMPORTEIRPF));
			bean.setImportePagado	(UtilidadesHash.getFloat(hash,FcsPagoActuacionAsistBean.C_IMPORTEPAGADO ));
			bean.setIdActuacion		(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_IDACTUACION));
			bean.setNumero			(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_NUMERO));			
			bean.setPorcentajeIRPF	(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_PORCENTAJEIRPF));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsPagoActuacionAsistBean.C_USUMODIFICACION));
			
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
			FcsPagoActuacionAsistBean b = (FcsPagoActuacionAsistBean) bean;
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_ANIO, b.getAnio().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_IDPAGOSJG, b.getIdPagosJG().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_IDINSTITUCION, b.getIdInstitucion().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_IDPERSONA, b.getIdPersona().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_IMPORTEIRPF, b.getImporteIRPF().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_IMPORTEPAGADO, b.getImportePagado().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_IDACTUACION, b.getIdActuacion().toString());
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_NUMERO, b.getNumero().toString());			
			UtilidadesHash.set(htData, FcsPagoActuacionAsistBean.C_USUMODIFICACION, b.getUsuMod().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoActuacionAsistAdm.selectGenerico(). Consulta SQL:"+select);
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
		Hashtable resultado =new Hashtable();
		String resultado1 = "",resultado2="", consulta = "";
		//query para consultar el importe 
		consulta = 	" SELECT " + 
					" SUM(" + FcsPagoActuacionAsistBean.C_IMPORTEPAGADO + ") AS IMPORTE, " +
					" SUM(" + FcsPagoActuacionAsistBean.C_IMPORTEIRPF + ") AS IRPF " +
					" FROM " + FcsPagoActuacionAsistBean.T_NOMBRETABLA + " " +
					" WHERE " + FcsPagoActuacionAsistBean.C_IDINSTITUCION + "=" + idInstitucion + " " +
					" AND " + FcsPagoActuacionAsistBean.C_IDPAGOSJG + "=" + idPago + " " +
					" AND " + FcsPagoActuacionAsistBean.C_IDPERSONA + "=" + idPersona + " ";
		
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
	 * Devuelve un vector con las actuaciones en asistencias que hay que pagar para una persona
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 */
	public Vector getActuacionAsistencias (String idInstitucion, String idPago, String idPersona) throws ClsExceptions 
	{
		//donde devolveremos el resultado
		Vector resultado = new Vector();
		//query con la select a ejecutar
		String consulta = " SELECT T." + ScsTurnoBean.C_NOMBRE + 
							", G." + ScsGuardiasTurnoBean.C_NOMBRE + " AS ACTUACIONES " +
							", A." + ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							", A." + ScsActuacionAsistenciaBean.C_ANIO +
							//", AC." + ScsActuacionAsistenciaBean.C_IDACTUACION +
							", A." + ScsActuacionAsistenciaBean.C_NUMERO +
							", A." + ScsAsistenciasBean.C_FECHAHORA +
							", AC." + ScsActuacionAsistenciaBean.C_DESCRIPCIONBREVE +

							//", P." + ScsPersonaJGBean.C_NOMBRE +
							//", P." + ScsPersonaJGBean.C_APELLIDO1 +
							//", P." + ScsPersonaJGBean.C_APELLIDO2 +

							", F." + FcsPagoActuacionAsistBean.C_IDPAGOSJG+
							", F." + FcsPagoActuacionAsistBean.C_IDACTUACION+
							", F." + FcsPagoActuacionAsistBean.C_IMPORTEPAGADO+
							", F." + FcsPagoActuacionAsistBean.C_IMPORTEIRPF+
							", F." + FcsPagoActuacionAsistBean.C_PORCENTAJEIRPF+
							" FROM " + ScsTurnoBean.T_NOMBRETABLA + " T" +
							" ," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " G" +
							" ," + ScsAsistenciasBean.T_NOMBRETABLA + " A" +
							" ," + ScsActuacionAsistenciaBean.T_NOMBRETABLA + " AC" +
							
							//" ," + ScsPersonaJGBean.T_NOMBRETABLA + " P" +
							
							" ," + FcsPagoActuacionAsistBean.T_NOMBRETABLA + " F" +
							" WHERE T." + ScsTurnoBean.C_IDINSTITUCION + "= G." +ScsGuardiasTurnoBean.C_IDINSTITUCION +
							" AND T." + ScsTurnoBean.C_IDTURNO +  "= G." +ScsGuardiasTurnoBean.C_IDTURNO  +
							" AND G." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "= A." +ScsAsistenciasBean.C_IDINSTITUCION +
							" AND G." + ScsGuardiasTurnoBean.C_IDTURNO + "= A." +ScsAsistenciasBean.C_IDTURNO +
							" AND G." + ScsGuardiasTurnoBean.C_IDGUARDIA + "= A." +ScsAsistenciasBean.C_IDGUARDIA +
							" AND A." + ScsAsistenciasBean.C_IDINSTITUCION + "= AC." +ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND A." + ScsAsistenciasBean.C_ANIO + "= AC." +ScsActuacionAsistenciaBean.C_ANIO +
							" AND A." + ScsAsistenciasBean.C_NUMERO + "= AC." +ScsActuacionAsistenciaBean.C_NUMERO +
							
							//" AND A." + ScsAsistenciasBean.C_IDPERSONAJG + "= P." +ScsPersonaJGBean.C_IDPERSONA +
							
							" AND F." + FcsPagoActuacionAsistBean.C_IDINSTITUCION + "= AC." +ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND F." + FcsPagoActuacionAsistBean.C_ANIO+ "= AC." +ScsActuacionAsistenciaBean.C_ANIO +
							" AND F." + FcsPagoActuacionAsistBean.C_NUMERO + "= AC." +ScsActuacionAsistenciaBean.C_NUMERO +
							" AND F." + FcsPagoActuacionAsistBean.C_IDACTUACION + "= AC." +ScsActuacionAsistenciaBean.C_IDACTUACION +
							" AND F." + FcsPagoActuacionAsistBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND F." + FcsPagoActuacionAsistBean.C_IDPAGOSJG + "=" + idPago +
							" AND F." + FcsPagoActuacionAsistBean.C_IDPERSONA + "=" + idPersona +" ";
							
		try{
			resultado = (Vector)this.selectGenerico(consulta);
		}catch(Exception e){
			throw new ClsExceptions (e,"Error en FcsPagoActuacionAsistAdm.getTurnosOficio"+consulta);
		}
		
		return resultado;
		
	}
}


