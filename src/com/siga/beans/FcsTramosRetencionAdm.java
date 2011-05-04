
package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.facturacionSJCS.form.TramosRetencionForm;
import com.siga.general.SIGAException;

/**
* 
* 
* @author jtacosta
* @since 29/04/2011 
*/
public class FcsTramosRetencionAdm extends MasterBeanAdministrador {

	
	public FcsTramosRetencionAdm(UsrBean usuario) {
		super(FcsTramosRetencionBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsTramosRetencionBean.C_FECHAMODIFICACION,		FcsTramosRetencionBean.C_USUMODIFICACION,
					FcsTramosRetencionBean.C_IDINSTITUCION,FcsTramosRetencionBean.C_NSMI,	FcsTramosRetencionBean.C_NTRAMO,
					FcsTramosRetencionBean.C_PORCENTAJE,FcsTramosRetencionBean.C_DESCRIPCION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsTramosRetencionBean.C_IDINSTITUCION,FcsTramosRetencionBean.C_NTRAMO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String[] campos = {FcsTramosRetencionBean.C_NTRAMO};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsTramosRetencionBean bean = null;
		
		try {
			bean = new FcsTramosRetencionBean();
			bean.setFechaMod		(UtilidadesHash.getString(hash,FcsTramosRetencionBean.C_FECHAMODIFICACION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash,FcsTramosRetencionBean.C_USUMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsTramosRetencionBean.C_IDINSTITUCION));
			bean.setNumeroTramo(UtilidadesHash.getInteger(hash,FcsTramosRetencionBean.C_NTRAMO));
			bean.setNumerosSmi(		UtilidadesHash.getInteger(hash,FcsTramosRetencionBean.C_NSMI));
			bean.setDescripcion(		UtilidadesHash.getString(hash,FcsTramosRetencionBean.C_DESCRIPCION));
			bean.setPorcentaje(		UtilidadesHash.getDouble(hash,FcsTramosRetencionBean.C_PORCENTAJE));
			
			
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
			FcsTramosRetencionBean b = (FcsTramosRetencionBean) bean;
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_NTRAMO, b.getNumeroTramo());
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_NSMI, b.getNumerosSmi());
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_PORCENTAJE, b.getPorcentaje());
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_FECHAMODIFICACION, b.getFechaMod().toString());
			UtilidadesHash.set(htData, FcsTramosRetencionBean.C_USUMODIFICACION, b.getUsuMod().toString());
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
			throw new ClsExceptions (e, "Excepcion en FcsTramosRetencionAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	public List<TramosRetencionForm> getTramosRetencion(String idInstitucion,String smi,String numeroMeses)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		
		
		sql.append(" SELECT IDINSTITUCION, NTRAMO, DESCRIPCION, PORCENTAJE, NSMI ");
		sql.append(" FROM FCS_TRAMOSRETENCION  ");
		sql.append(" WHERE IDINSTITUCION = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" ORDER BY NTRAMO ");
		
		List<TramosRetencionForm> alTramosRetencion = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (!rc.findBind(sql.toString(),htCodigos)) {
            	htCodigos.put(new Integer(contador),"0");
        		if (!rc.findBind(sql.toString(),htCodigos)) {
        			return alTramosRetencion;
        		}
            	
            	
    			
            } 
            alTramosRetencion = new ArrayList<TramosRetencionForm>();
            TramosRetencionForm tramosRetencionForm = null;
            for (int i = 0; i < rc.size(); i++){
        		Row fila = (Row) rc.get(i);
        		Hashtable<String, Object> htFila=fila.getRow();
        		
        		
        		tramosRetencionForm = new TramosRetencionForm();
        		tramosRetencionForm.setIdInstitucion(UtilidadesHash.getString(htFila,FcsTramosRetencionBean.C_IDINSTITUCION));
        		tramosRetencionForm.setNumeroTramo(UtilidadesHash.getString(htFila,FcsTramosRetencionBean.C_NTRAMO));
        		if(!UtilidadesHash.getString(htFila,FcsTramosRetencionBean.C_NSMI).equals(""))
        			tramosRetencionForm.setNumerosSmi(UtilidadesHash.getString(htFila,FcsTramosRetencionBean.C_NSMI));
        		else
        			tramosRetencionForm.setNumerosSmi("-");
        			
        		tramosRetencionForm.setDescripcion(UtilidadesHash.getString(htFila,FcsTramosRetencionBean.C_DESCRIPCION));
        		tramosRetencionForm.setPorcentaje(UtilidadesNumero.formato(UtilidadesHash.getDouble(htFila,FcsTramosRetencionBean.C_PORCENTAJE)) );
        		tramosRetencionForm.setNumeroMeses(numeroMeses);
        		tramosRetencionForm.setSmi(smi);
        		alTramosRetencion.add(tramosRetencionForm);
        	}
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTramosRetencion;
		
		
		
	}
	
	public Double getSmi(String anio)throws ClsExceptions, SIGAException{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		
		
		sql.append(" select * FROM FCS_SMI ");
		sql.append(" WHERE ANIO = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),anio);
	
		Double smi = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	if(rc.size()==0)
            		throw new SIGAException("FactSJCS.mantRetencionesJ.plAplicarRetencionesJudiciales.error.tramosLEC");
            	Row fila = (Row) rc.get(0);
        		Hashtable<String, Object> htFila=fila.getRow();
        		smi = UtilidadesHash.getDouble(htFila,"VALOR");
            	
            	
    			
            }else 
            	throw new SIGAException("FactSJCS.mantRetencionesJ.plAplicarRetencionesJudiciales.error.tramosLEC");
            
       } catch (SIGAException e) {
       		throw e ;
       }catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return smi;
		
		
		
	}
	public Double getImporteRetenido(String importe,String anio,String idInstitucion,String numMeses)throws ClsExceptions, SIGAException{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		
		
		sql.append(" select F_SIGA_RETENCION_LEC(:");
		contador ++;
		sql.append(contador);
		sql.append(",:");
		htCodigos.put(new Integer(contador),importe);
		contador ++;
		sql.append(contador);
		sql.append(",:");
		htCodigos.put(new Integer(contador),anio);
		contador ++;
		sql.append(contador);
		sql.append(",:");
		htCodigos.put(new Integer(contador),idInstitucion);
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),numMeses);
				
		sql.append(") AS IMPORTE_RETENCION FROM DUAL ");
		
	    
	
		Double importeRetencion = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	if(rc.size()==0)
            		throw new SIGAException("FactSJCS.mantRetencionesJ.plAplicarRetencionesJudiciales.error.tramosLEC");
            	Row fila = (Row) rc.get(0);
        		Hashtable<String, Object> htFila=fila.getRow();
        		importeRetencion = UtilidadesHash.getDouble(htFila,"IMPORTE_RETENCION");
            	
            	
    			
            }else 
            	throw new SIGAException("FactSJCS.mantRetencionesJ.plAplicarRetencionesJudiciales.error.tramosLEC");
            
       } catch (SIGAException e) {
       		throw e ;
       }catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return importeRetencion;
		
		
		
	}
	
	
}


