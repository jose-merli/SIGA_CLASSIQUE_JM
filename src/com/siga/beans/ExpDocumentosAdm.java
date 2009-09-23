/*
 * Created on Jan 27, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de Documentos de un expediente
 */
public class ExpDocumentosAdm extends MasterBeanAdministrador {

	public ExpDocumentosAdm(UsrBean usuario)
	{
	    super(ExpDocumentosBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpDocumentosBean.C_IDINSTITUCION,
				ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpDocumentosBean.C_IDTIPOEXPEDIENTE,
				ExpDocumentosBean.C_NUMEROEXPEDIENTE,
				ExpDocumentosBean.C_ANIOEXPEDIENTE,
				ExpDocumentosBean.C_IDDOCUMENTO,
				ExpDocumentosBean.C_DESCRIPCION,
				ExpDocumentosBean.C_RUTA,
				ExpDocumentosBean.C_REGENTRADA,
				ExpDocumentosBean.C_REGSALIDA,
				ExpDocumentosBean.C_IDFASE,
				ExpDocumentosBean.C_IDESTADO,
				ExpDocumentosBean.C_FECHAMODIFICACION,
				ExpDocumentosBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpDocumentosBean.C_IDINSTITUCION,
				ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpDocumentosBean.C_IDTIPOEXPEDIENTE,
				ExpDocumentosBean.C_NUMEROEXPEDIENTE,
				ExpDocumentosBean.C_ANIOEXPEDIENTE,
				ExpDocumentosBean.C_IDDOCUMENTO};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ExpDocumentosBean bean = null;

		try
		{
			bean = new ExpDocumentosBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_IDINSTITUCION));
			bean.setIdInstitucion_TipoExpediente(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_IDTIPOEXPEDIENTE));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_ANIOEXPEDIENTE));
			bean.setIdDocumento(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_IDDOCUMENTO));
			bean.setDescripcion(UtilidadesHash.getString(hash, ExpDocumentosBean.C_DESCRIPCION));
			bean.setRuta(UtilidadesHash.getString(hash, ExpDocumentosBean.C_RUTA));
			bean.setRegEntrada(UtilidadesHash.getString(hash, ExpDocumentosBean.C_REGENTRADA));
			bean.setRegSalida(UtilidadesHash.getString(hash, ExpDocumentosBean.C_REGSALIDA));
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_IDFASE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_IDESTADO));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpDocumentosBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpDocumentosBean.C_USUMODIFICACION));
		
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;

	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpDocumentosBean b = (ExpDocumentosBean) bean;

			UtilidadesHash.set(htData, ExpDocumentosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_TipoExpediente());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_IDDOCUMENTO, b.getIdDocumento());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_RUTA, b.getRuta());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_REGENTRADA, b.getRegEntrada());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_REGSALIDA, b.getRegSalida());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpDocumentosBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	/** Funcion selectDocFaseEstado (String where)
	 * Consulta sobre la tabla de documentos,fases y estados, para búsqueda de datos de documentos
	 * @param criteros para filtrar el select, campo where 
	 * @return vector con los datos de un expediente  
	 * */
	public Vector selectDocFaseEstado(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String [] fields = this.getCamposBean();
	        
	        String sql = "SELECT ";
	        //todos los campos de la tabla exp_documento separados por coma
		    for(int i=0;i<fields.length; i++){
			  sql += "D."+fields[i]+", ";
			}
		    		    
		    sql += "F."+ExpFasesBean.C_NOMBRE+" AS FASE, ";
		    sql += "E."+ExpEstadosBean.C_NOMBRE+" AS ESTADO ";
		    
			sql += " FROM ";
		    sql += ExpDocumentosBean.T_NOMBRETABLA+" D, "+ExpEstadosBean.T_NOMBRETABLA+" E, "+ExpFasesBean.T_NOMBRETABLA+" F";
		    		    		
			sql += " " + where;
			sql += " ORDER BY F."+ExpFasesBean.C_NOMBRE+", E."+ExpEstadosBean.C_NOMBRE+", D."+ExpDocumentosBean.C_IDDOCUMENTO;
						
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	/** Funcion getNewIdDocumento (Hashtable hash)
	 * Genera el id de un nuevo Documento
	 * @param hash con la clave primaria sin el idDocumento
	 * @return nuevo idDocumento  
	 * */
	public Integer getNewIdDocumento(Hashtable hash) throws ClsExceptions 
	{		
		int nuevoIdDocumento = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpDocumentosBean.C_IDDOCUMENTO+") AS ULTIMODOCUMENTO ";
	        //todos los campos de la tabla exp_expediente separados por coma
		    
		    
			sql += " FROM "+ExpDocumentosBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpDocumentosBean.C_IDINSTITUCION+" = "+hash.get(ExpDocumentosBean.C_IDINSTITUCION)+" AND ";
			sql += ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+hash.get(ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+" AND ";
			sql += ExpDocumentosBean.C_IDTIPOEXPEDIENTE+" = "+hash.get(ExpDocumentosBean.C_IDTIPOEXPEDIENTE)+" AND ";
			sql += ExpDocumentosBean.C_NUMEROEXPEDIENTE+" = "+hash.get(ExpDocumentosBean.C_NUMEROEXPEDIENTE)+" AND ";
			sql += ExpDocumentosBean.C_ANIOEXPEDIENTE+" = "+hash.get(ExpDocumentosBean.C_ANIOEXPEDIENTE);
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMODOCUMENTO")).equals("")) {
					Integer ultimoDocumentoInt = Integer.valueOf((String)htRow.get("ULTIMODOCUMENTO"));
					int ultimoDocumento = ultimoDocumentoInt.intValue();
					ultimoDocumento++;
					nuevoIdDocumento = ultimoDocumento;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdDocumento);
	}
}
