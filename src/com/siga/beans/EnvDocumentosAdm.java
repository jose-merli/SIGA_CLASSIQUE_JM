/*
 * Created on Apr 11, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class EnvDocumentosAdm extends MasterBeanAdministrador {

	public EnvDocumentosAdm(UsrBean usuario)
	{
	    super(EnvDocumentosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvDocumentosBean.C_IDINSTITUCION,
            	EnvDocumentosBean.C_IDENVIO,
            	EnvDocumentosBean.C_IDDOCUMENTO,
            	EnvDocumentosBean.C_DESCRIPCION,
            	EnvDocumentosBean.C_PATHDOCUMENTO,
            	EnvDocumentosBean.C_FECHAMODIFICACION,
            	EnvDocumentosBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvDocumentosBean.C_IDINSTITUCION,
                		   EnvDocumentosBean.C_IDENVIO,
                		   EnvDocumentosBean.C_IDDOCUMENTO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {        
        
		return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvDocumentosBean bean = null;

		try
		{
			bean = new EnvDocumentosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvDocumentosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvDocumentosBean.C_IDENVIO));
			bean.setIdDocumento(UtilidadesHash.getInteger(hash, EnvDocumentosBean.C_IDDOCUMENTO));
			bean.setPathDocumento(UtilidadesHash.getString(hash, EnvDocumentosBean.C_PATHDOCUMENTO));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvDocumentosBean.C_DESCRIPCION));
			
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

			EnvDocumentosBean b = (EnvDocumentosBean) bean;
			
			UtilidadesHash.set(htData, EnvDocumentosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvDocumentosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvDocumentosBean.C_IDDOCUMENTO, b.getIdDocumento());
			UtilidadesHash.set(htData, EnvDocumentosBean.C_PATHDOCUMENTO, b.getPathDocumento());
			UtilidadesHash.set(htData, EnvDocumentosBean.C_DESCRIPCION, b.getDescripcion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdDocumento(String idInstitucion,
            						 String idEnvio) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvDocumentosBean.C_IDDOCUMENTO + 
        		") AS MAXVALOR FROM " + EnvDocumentosBean.T_NOMBRETABLA +
        		" WHERE " + EnvDocumentosBean.C_IDINSTITUCION + " = " +idInstitucion +
        		" AND " + EnvDocumentosBean.C_IDENVIO + " = " + idEnvio;
        int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("MAXVALOR")).equals("")) {
                Integer valorInt=Integer.valueOf((String)htRow.get("MAXVALOR"));
                valor=valorInt.intValue();
                valor++;
            }            
        }
        return new Integer(valor);        
    }
    
    /**
     * 
     * Clase para la obtención del objeto File de un documento.<br/>
     * @param idInstitucion
     * @param idEnvio
     * @param idDocumento
     * @return File
     *
     */
    
    public File getFile(String idInstitucion, 
			String idEnvio, 
			String idDocumento) throws ClsExceptions{

    	File fDocumento = null;

    	EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.usrbean);		

    	String pathDocumentosAdjuntos = "";
    	try {
    		pathDocumentosAdjuntos = envioAdm.getPathEnvio(idInstitucion,idEnvio);
    	} catch (Exception e) {
    		new ClsExceptions (e, "Error al recuperar el envio");
    	}
    	String pathCompleto = pathDocumentosAdjuntos + File.separator + 
    	idInstitucion + "_" + idEnvio + "_" + idDocumento;

    	fDocumento = new File(pathCompleto);
    	return fDocumento;
}

    public File getFile(EnvEnviosBean envBean, String idDocumento) throws ClsExceptions{

		File fDocumento = null;
		EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.usrbean);		
		
		String pathDocumentosAdjuntos = "";
		try {
			pathDocumentosAdjuntos = envioAdm.getPathEnvio(envBean);
		} catch (Exception e) {
			new ClsExceptions (e, "Error al recuperar el envio");
		}
		String pathCompleto = pathDocumentosAdjuntos + File.separator + 
						  envBean.getIdInstitucion().toString() + "_" + envBean.getIdEnvio().toString() + "_" + idDocumento;
		
		fDocumento = new File(pathCompleto);
		
		return fDocumento;
	}
	
    /**
     * 
     * Clase para la obtención del parámetro PATH_DOCUMENTOSADJUNTOS.<br/>
     * de la tabla GEN_PARAMETROS. 
     *
     */
		public String getPathDocumentosFromDB() throws ClsExceptions
		{
		    String sPath="";
		    
		    try
		    {
		        String sWhere = " WHERE " + GenParametrosBean.C_PARAMETRO + "='PATH_DOCUMENTOSADJUNTOS' AND "+
		                                    GenParametrosBean.C_MODULO + "='ENV' AND " + 
		                                    GenParametrosBean.C_IDINSTITUCION + "=0";
		        
		        GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);
		        Vector vParametros = admParametros.select(sWhere);	        

		        GenParametrosBean beanParametros = (GenParametrosBean)vParametros.elementAt(0);
		        sPath = beanParametros.getValor();
		    }
		    
		    catch(Exception e)
		    {
		        new ClsExceptions (e, "Error al recuperar el parámetro PATH_DOCUMENTOSADJUNTOS");
		    }
		    
		    return sPath;
		}
		public Vector getDocumentosFactura(String idInstitucion, String idFactura)  
			throws ClsExceptions,SIGAException {
			
		    Vector datos = new Vector();
		    
		    try {
			    
				StringBuffer select = new StringBuffer();
				 
				select.append("select doc.");
				select.append(EnvDocumentosBean.C_IDINSTITUCION);
				select.append(", doc.");
				select.append(EnvDocumentosBean.C_IDDOCUMENTO);
				select.append(",doc.");
				select.append(EnvDocumentosBean.C_PATHDOCUMENTO);
				select.append(",doc.");
				select.append(EnvDocumentosBean.C_DESCRIPCION);
				select.append(",moro.");
				select.append(EnvComunicacionMorososBean.C_IDENVIODOC);
				select.append(",moro.");
				select.append(EnvComunicacionMorososBean.C_IDENVIO);
				select.append(",moro.");
				select.append(EnvComunicacionMorososBean.C_FECHAENVIO);
 
				select.append(" from ");
				select.append(EnvDocumentosBean.T_NOMBRETABLA);
				select.append(" doc,"); 
				select.append(EnvComunicacionMorososBean.T_NOMBRETABLA);
				select.append(" moro ");
				select.append("where doc.");
				select.append(EnvDocumentosBean.C_IDENVIO);
				select.append(" = moro.");
				select.append(EnvComunicacionMorososBean.C_IDENVIODOC);
				select.append(" and doc.");
				select.append(EnvDocumentosBean.C_IDINSTITUCION);
				select.append(" = moro.");
				select.append(EnvComunicacionMorososBean.C_IDINSTITUCION); 
				
				select.append(" and doc.");
				select.append(EnvDocumentosBean.C_IDDOCUMENTO);
				select.append(" = moro.");
				select.append(EnvComunicacionMorososBean.C_IDDOCUMENTO);
				
				select.append(" and doc.");
				select.append(EnvDocumentosBean.C_IDINSTITUCION);
				select.append(" = ");
				select.append(idInstitucion);
				select.append(" and moro.");
				select.append(EnvComunicacionMorososBean.C_IDFACTURA);
				select.append(" = ");
				select.append(idFactura);
				
				select.append(" order by moro.");
				select.append(EnvComunicacionMorososBean.C_IDENVIO);
				select.append(" desc ");
				
				
				
				RowsContainer rc = new RowsContainer();			
			
				if (rc.query(select.toString())) {
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);	
						
						datos.add(fila.getRow());
						
						
						
					}
				}
			} 
			
		    catch (Exception e) {
		   		if (e instanceof SIGAException){
		   			throw (SIGAException)e;
		   		}
		   		else {
		   			if (e instanceof ClsExceptions){
		   				throw (ClsExceptions)e;
		   			}
		   			else {
		   				throw new ClsExceptions(e, "EnvDocuemntosAdm.getDocumentosMorosos");
		   			}
		   		}	
		    }
		    
		    return datos;
		}
		
		
		
}
