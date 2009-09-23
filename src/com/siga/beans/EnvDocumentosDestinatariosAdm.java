/*
 * Created on Apr 11, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class EnvDocumentosDestinatariosAdm extends MasterBeanAdministrador {

	public EnvDocumentosDestinatariosAdm(UsrBean usuario)
	{
	    super(EnvDocumentosDestinatariosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvDocumentosDestinatariosBean.C_IDINSTITUCION,
            	EnvDocumentosDestinatariosBean.C_IDENVIO,
            	EnvDocumentosDestinatariosBean.C_IDPERSONA,
            	EnvDocumentosDestinatariosBean.C_IDDOCUMENTO,
            	EnvDocumentosDestinatariosBean.C_DESCRIPCION,
            	EnvDocumentosDestinatariosBean.C_PATHDOCUMENTO,
            	EnvDocumentosDestinatariosBean.C_FECHAMODIFICACION,
            	EnvDocumentosDestinatariosBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvDocumentosDestinatariosBean.C_IDINSTITUCION,
                		   EnvDocumentosDestinatariosBean.C_IDENVIO,
                		   EnvDocumentosDestinatariosBean.C_IDPERSONA,
                		   EnvDocumentosDestinatariosBean.C_IDDOCUMENTO};
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
        EnvDocumentosDestinatariosBean bean = null;

		try
		{
			bean = new EnvDocumentosDestinatariosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvDocumentosDestinatariosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvDocumentosDestinatariosBean.C_IDENVIO));
			bean.setIdDocumento(UtilidadesHash.getInteger(hash, EnvDocumentosDestinatariosBean.C_IDDOCUMENTO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvDocumentosDestinatariosBean.C_IDPERSONA));
			bean.setPathDocumento(UtilidadesHash.getString(hash, EnvDocumentosDestinatariosBean.C_PATHDOCUMENTO));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvDocumentosDestinatariosBean.C_DESCRIPCION));
			
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

			EnvDocumentosDestinatariosBean b = (EnvDocumentosDestinatariosBean) bean;
			
			UtilidadesHash.set(htData, EnvDocumentosDestinatariosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvDocumentosDestinatariosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvDocumentosDestinatariosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvDocumentosDestinatariosBean.C_IDDOCUMENTO, b.getIdDocumento());
			UtilidadesHash.set(htData, EnvDocumentosDestinatariosBean.C_PATHDOCUMENTO, b.getPathDocumento());
			UtilidadesHash.set(htData, EnvDocumentosDestinatariosBean.C_DESCRIPCION, b.getDescripcion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdDocumentoDestinatario(String idInstitucion,
            						 			 String idEnvio,
            						 			 String idPersona) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvDocumentosDestinatariosBean.C_IDDOCUMENTO + 
        		") AS MAXVALOR FROM " + EnvDocumentosDestinatariosBean.T_NOMBRETABLA +
        		" WHERE " + EnvDocumentosDestinatariosBean.C_IDINSTITUCION + " = " +idInstitucion +
        		" AND " + EnvDocumentosDestinatariosBean.C_IDENVIO + " = " + idEnvio +
        		" AND " + EnvDocumentosDestinatariosBean.C_IDPERSONA + " = " + idPersona;
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
     * @param idPersona
     * @return File
     *
     */
    
    public File getFile(String idInstitucion, 
			String idEnvio, 
			String idDocumento,
			String idPersona) throws ClsExceptions{

		File fDocumento = null;
		
		//Recuperamos los datos del envio
		Hashtable htPk = new Hashtable();
		htPk.put(EnvDocumentosDestinatariosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvDocumentosDestinatariosBean.C_IDENVIO,idEnvio);
		
		Vector vEnvio = null;
		EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.usrbean);
		EnvEnviosBean envioBean = null;
		try {
			vEnvio = envioAdm.selectByPK(htPk);        
			envioBean = (EnvEnviosBean)vEnvio.firstElement();
		} catch (Exception e) {
			new ClsExceptions (e, "Error al recuperar el envio");
		}    
		
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		Calendar cal = Calendar.getInstance();
		Date d;
		try {
			d = sdf.parse(envioBean.getFechaCreacion());
			cal.setTime(d);    	        
		} catch (ParseException e1) {
			new ClsExceptions (e1, "Error al recuperar la fecha del envío");
		}
		String anio = String.valueOf(cal.get(Calendar.YEAR));            
		String mes = String.valueOf(cal.get(Calendar.MONTH)+1);             
		
		String pathDocumentosAdjuntos = this.getPathDocumentosFromDB();
		String pathCompleto = pathDocumentosAdjuntos + File.separator + 
						  idInstitucion + File.separator +
						  anio + File.separator + 
						  mes + File.separator + 
						  idEnvio + File.separator +         					  
						  idInstitucion + "_" + idEnvio  + "_" + idPersona + "_" + idDocumento;
		
		fDocumento = new File(pathCompleto);
		return fDocumento;
	}

    public File getFile(EnvEnviosBean envioBean,
			String idDocumento,
			String idPersona) throws ClsExceptions{

		File fDocumento = null;
		
		
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		Calendar cal = Calendar.getInstance();
		Date d;
		try {
		d = sdf.parse(envioBean.getFechaCreacion());
		cal.setTime(d);    	        
		} catch (ParseException e1) {
		new ClsExceptions (e1, "Error al recuperar la fecha del envío");
		}
		String anio = String.valueOf(cal.get(Calendar.YEAR));            
		String mes = String.valueOf(cal.get(Calendar.MONTH)+1);             
		
		String pathDocumentosAdjuntos = this.getPathDocumentosFromDB();
		String pathCompleto = pathDocumentosAdjuntos + File.separator + 
						  envioBean.getIdInstitucion().toString() + File.separator +
						  anio + File.separator + 
						  mes + File.separator + 
						  envioBean.getIdEnvio().toString() + File.separator +         					  
						  envioBean.getIdInstitucion().toString() + "_" + envioBean.getIdEnvio().toString()  + "_" + idPersona + "_" + idDocumento;
		
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
}
