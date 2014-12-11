//VERSIONES:
//ruben.fernandez 04-04-2005 creacion 

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class FcsPagoColegiadoAdm extends MasterBeanAdministrador {

	
	public FcsPagoColegiadoAdm(UsrBean usuario) {
		super(FcsPagoColegiadoBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagoColegiadoBean.C_IDINSTITUCION,		
							FcsPagoColegiadoBean.C_IDPAGOSJG,
							FcsPagoColegiadoBean.C_IDPERORIGEN,
							FcsPagoColegiadoBean.C_IDPERDESTINO,
							FcsPagoColegiadoBean.C_IMPOFICIO,
							FcsPagoColegiadoBean.C_IMPASISTENCIA,
							FcsPagoColegiadoBean.C_IMPEJG,
							FcsPagoColegiadoBean.C_IMPSOJ,
							FcsPagoColegiadoBean.C_IMPMOVVAR,
							FcsPagoColegiadoBean.C_IMPIRPF,
							FcsPagoColegiadoBean.C_PORCENTAJEIRPF,
							FcsPagoColegiadoBean.C_IMPRET,
							FcsPagoColegiadoBean.C_FECHAMODIFICACION,
							FcsPagoColegiadoBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagoColegiadoBean.C_IDINSTITUCION,
							FcsPagoColegiadoBean.C_IDPAGOSJG,
							FcsPagoColegiadoBean.C_IDPERORIGEN};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoColegiadoBean bean = null;
		
		try {
			bean = new FcsPagoColegiadoBean();
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash,FcsPagoColegiadoBean.C_IDINSTITUCION));
			bean.setIdPagosJG		(UtilidadesHash.getInteger(hash,FcsPagoColegiadoBean.C_IDPAGOSJG));
			bean.setIdPerOrigen		(UtilidadesHash.getInteger(hash,FcsPagoColegiadoBean.C_IDPERORIGEN));
			bean.setIdPerDestino	(UtilidadesHash.getInteger(hash,FcsPagoColegiadoBean.C_IDPERDESTINO));
			bean.setImpOficio		(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPOFICIO));
			bean.setImpAsistencia 	(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPASISTENCIA));
			bean.setImpSOJ			(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPSOJ));
			bean.setImpEJG			(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPEJG));
			bean.setImpMovVar		(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPMOVVAR));
			bean.setImpIRPF			(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPIRPF));
			bean.setPorcentajeIRPF	(UtilidadesHash.getInteger(hash,FcsPagoColegiadoBean.C_PORCENTAJEIRPF));
			bean.setImpRet			(UtilidadesHash.getDouble(hash,FcsPagoColegiadoBean.C_IMPRET));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsPagoColegiadoBean.C_USUMODIFICACION));
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsPagoColegiadoBean.C_FECHAMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean b) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsPagoColegiadoBean bean = (FcsPagoColegiadoBean) b;
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IDINSTITUCION, bean.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IDPAGOSJG, bean.getIdPagosJG());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IDPERORIGEN, bean.getIdPerOrigen());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IDPERDESTINO, bean.getIdPerDestino());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPOFICIO, bean.getImpOficio());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPASISTENCIA, bean.getImpAsistencia());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPSOJ, bean.getImpSOJ());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPEJG, bean.getImpEJG());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPMOVVAR, bean.getImpMovVar());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPIRPF, bean.getImpIRPF());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_PORCENTAJEIRPF, bean.getPorcentajeIRPF());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_IMPRET, bean.getImpRet());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_USUMODIFICACION, bean.getUsuMod());
			UtilidadesHash.set(htData, FcsPagoColegiadoBean.C_FECHAMODIFICACION, bean.getFechaMod());
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoColegiadoBean.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}

	/**
	 * Inserta un nuevo registro en la tabla a partir de un bean con la PK. 
	 * El resto de datos se inicializan con un valor por defecto.
	 * @param pcBean Contiene los campos de la PK inicializados
	 * @throws ClsExceptions 
	 */
	public boolean insertInicial(FcsPagoColegiadoBean bean) throws ClsExceptions {
		//inicializa el resto de datos del Bean
		bean.setIdPerDestino	(null);
		bean.setImpOficio		(new Double(0));
		bean.setImpAsistencia 	(new Double(0));
		bean.setImpSOJ			(new Double(0));
		bean.setImpEJG			(new Double(0));
		bean.setImpSOJ			(new Double(0));
		bean.setImpIRPF			(new Double(0));
		bean.setPorcentajeIRPF	(new Integer(0));
		bean.setImpRet			(new Double(0));
		bean.setUsuMod			(new Integer(0));
		bean.setFechaMod		("sysdate");
		//insertar el registro
		return insert(bean);
	}

	/**
	 * Actualizar el irpf, movimientos varios y retenciones en fcs_pago_colegiado
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @param irpf	porcentaje del irpf que se ha aplicado.
	 * @param movimientos	importe de los movimientos varios apalicados.
	 * @param retenciones	importe de las retenciones aplicadas.
	 * @throws ClsExceptions 
	 */
	public boolean updateCierrePago(String idInstitucion, String idPago, String idPersona, 
			double irpf, double porcentajeIRPF, double movimientos, double retenciones, boolean insertar) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		hash.put(FcsPagoColegiadoBean.C_IDINSTITUCION, idInstitucion);
		hash.put(FcsPagoColegiadoBean.C_IDPAGOSJG, idPago);
		hash.put(FcsPagoColegiadoBean.C_IDPERORIGEN, idPersona);
		hash.put(FcsPagoColegiadoBean.C_IMPIRPF, Double.toString(irpf));
		hash.put(FcsPagoColegiadoBean.C_PORCENTAJEIRPF, Double.toString(porcentajeIRPF));
		hash.put(FcsPagoColegiadoBean.C_IMPMOVVAR, Double.toString(movimientos));
		hash.put(FcsPagoColegiadoBean.C_IMPRET, Double.toString(retenciones));

		String aux[] = {FcsPagoColegiadoBean.C_IMPIRPF,
						FcsPagoColegiadoBean.C_PORCENTAJEIRPF,
						FcsPagoColegiadoBean.C_IMPMOVVAR,
						FcsPagoColegiadoBean.C_IMPRET};
		try {
			if (insertar){
				hash.put(FcsPagoColegiadoBean.C_IMPOFICIO, "0");				
				hash.put(FcsPagoColegiadoBean.C_IMPASISTENCIA, "0");				
				hash.put(FcsPagoColegiadoBean.C_IMPSOJ, "0");				
				hash.put(FcsPagoColegiadoBean.C_IMPEJG, "0");				
				return insert(hash);
			}
			else
				return updateDirect(hash, null, aux);
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al actualizar impMovVar en fcs_pago_colegiado");
		}
	}
	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 * @throws Exception 
	 */
	public String getIrpf(String idInstitucion, String idPago, String idPersona) throws Exception{
		Vector resultado;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl(PORCENTAJEIRPF,0) PORCENTAJEIRPF ");
		sql.append(" from fcs_pago_colegiado col ");
		sql.append(" WHERE col.IDPAGOSJG = " + idPago);
		sql.append(" and col.idinstitucion = " + idInstitucion);
		sql.append(" and col.idperorigen = " + idPersona);
		try {
			resultado = this.selectGenerico(sql.toString());
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al obtener el porcentaje IRPF");
		}
		if(resultado!=null && resultado.size()>0 && ((Hashtable)resultado.get(0)).get("PORCENTAJEIRPF")!=null)
			return ((Hashtable)resultado.get(0)).get("PORCENTAJEIRPF").toString();
		else 
			return "";
	}
	

	/**
	 * Generar una lista los registros de la tabla FCS_PAGO_COLEGIADO que tengan el valo de la instituci�n
	 * y del identificador del pago que se les ha pasado por par�metro.
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */	
	
   	public Vector selectPagoColegiado (String idinstitucion,
			  String idPago)
   		throws ClsExceptions {
   		
   		String consulta = "";
   		
   		Vector resultado = new Vector();
   		
   		try {
   			consulta += " select * from "+FcsPagoColegiadoBean.T_NOMBRETABLA+" ";
   			consulta += " where ("+FcsPagoColegiadoBean.C_IDINSTITUCION+"="+idinstitucion+") ";
   			consulta += "   and ("+FcsPagoColegiadoBean.C_IDPAGOSJG+"="+idPago+") ";

   			resultado = (Vector)this.selectGenerico(consulta.toString());
   		} catch (Exception e) {
   			throw new ClsExceptions (e, "Excepcion en FcsPagocolegiadoAdm.selectPagoColegiado(). Consulta SQL:" + consulta);
   		}
   		
   		return resultado;
   	} //selectPagoColegiado ()			
	
	/**
	 * Elimina los registros de la tabla FCS_PAGO_COLEGIADO que tengan el valo de la instituci�n
	 * y del identificador del pago que se les ha pasado por par�metro.
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */	
	
   	public void deletePagoColegiado (String idinstitucion,
			  String idPago)
   		throws ClsExceptions {
   		
   		String consulta = "";
   		try {
   			consulta += " delete from "+FcsPagoColegiadoBean.T_NOMBRETABLA+" ";
   			consulta += " where ("+FcsPagoColegiadoBean.C_IDINSTITUCION+"="+idinstitucion+") ";
   			consulta += "   and ("+FcsPagoColegiadoBean.C_IDPAGOSJG+"="+idPago+") ";

   			ClsMngBBDD.executeUpdate (consulta);
   		} catch (Exception e) {
   			throw new ClsExceptions (e, "Excepcion en FcsPagocolegiadoAdm.deletePagoColegiado(). Consulta SQL:" + consulta);
   		}
   	} //deletePagoColegiado ()		

}


