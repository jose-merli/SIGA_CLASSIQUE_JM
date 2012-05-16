package com.siga.beans;

import java.util.*;


import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;
/**
 * 
 * @author jorgeta
 *
 */
public class EnvComunicacionMorososAdm extends MasterBeanAdministrador
{
	public EnvComunicacionMorososAdm(UsrBean usuario)
	{
	    super(EnvComunicacionMorososBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvComunicacionMorososBean.C_IDINSTITUCION,
		        		   EnvComunicacionMorososBean.C_IDPERSONA,
		        		   EnvComunicacionMorososBean.C_IDFACTURA,
		        		   EnvComunicacionMorososBean.C_IDENVIO,
		        		   EnvComunicacionMorososBean.C_DESCRIPCION
		        		   ,EnvComunicacionMorososBean.C_PATHDOCUMENTO,
		        		   EnvComunicacionMorososBean.C_FECHAENVIO,
		        		   EnvComunicacionMorososBean.C_IDENVIODOC,
		        		   EnvComunicacionMorososBean.C_IDDOCUMENTO
		        		   };

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvComunicacionMorososBean.C_IDINSTITUCION,
     		   EnvComunicacionMorososBean.C_IDPERSONA,
    		   EnvComunicacionMorososBean.C_IDFACTURA,
    		   EnvComunicacionMorososBean.C_IDENVIO};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {EnvComunicacionMorososBean.C_IDINSTITUCION,
     		   EnvComunicacionMorososBean.C_IDPERSONA,
    		   EnvComunicacionMorososBean.C_IDFACTURA,
    		   EnvComunicacionMorososBean.C_IDENVIO,
    		   EnvComunicacionMorososBean.C_DESCRIPCION
    		   ,EnvComunicacionMorososBean.C_PATHDOCUMENTO,
    		   EnvComunicacionMorososBean.C_FECHAENVIO,
    		   EnvComunicacionMorososBean.C_IDENVIODOC,
    		   EnvComunicacionMorososBean.C_IDDOCUMENTO};
		
		return campos;

    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvComunicacionMorososBean bean = null;

		try
		{
			bean = new EnvComunicacionMorososBean();
			
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvComunicacionMorososBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash, EnvComunicacionMorososBean.C_IDPERSONA));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvComunicacionMorososBean.C_IDENVIO));
			bean.setIdFactura(UtilidadesHash.getString(hash, EnvComunicacionMorososBean.C_IDFACTURA));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvComunicacionMorososBean.C_DESCRIPCION));
			bean.setPathDocumento(UtilidadesHash.getString(hash, EnvComunicacionMorososBean.C_PATHDOCUMENTO));
			bean.setFechaEnvio(UtilidadesHash.getString(hash, EnvComunicacionMorososBean.C_FECHAENVIO));
			bean.setFechaMod(UtilidadesHash.getString(hash, EnvComunicacionMorososBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, EnvComunicacionMorososBean.C_USUMODIFICACION));
			bean.setIdEnvioDoc(UtilidadesHash.getInteger(hash, EnvComunicacionMorososBean.C_IDENVIODOC));
			bean.setIdDocumento(UtilidadesHash.getInteger(hash, EnvComunicacionMorososBean.C_IDDOCUMENTO));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			EnvComunicacionMorososBean b = (EnvComunicacionMorososBean) bean;

			
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_IDFACTURA, b.getIdFactura());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_PATHDOCUMENTO, b.getPathDocumento());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_FECHAENVIO, b.getFechaEnvio());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_IDENVIODOC, b.getIdEnvioDoc());
			UtilidadesHash.set(htData, EnvComunicacionMorososBean.C_IDDOCUMENTO, b.getIdDocumento());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	public Integer getNewIdEnvio(EnvComunicacionMorososBean bean) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        StringBuffer sql= new StringBuffer();
        sql.append("SELECT MAX(");
        sql.append(EnvComunicacionMorososBean.C_IDENVIO);
        sql.append(") AS MAXVALOR FROM ");
        sql.append(EnvComunicacionMorososBean.T_NOMBRETABLA);
        sql.append(" WHERE ");
        sql.append(EnvComunicacionMorososBean.C_IDINSTITUCION);
        sql.append(" = ");
        sql.append(bean.getIdInstitucion());
        sql.append(" AND ");
        sql.append(EnvComunicacionMorososBean.C_IDFACTURA);
        sql.append(" = ");
        sql.append(bean.getIdFactura());
        sql.append(" AND  ");
        sql.append(EnvComunicacionMorososBean.C_IDPERSONA);
        sql.append(" = ");
        sql.append(bean.getIdPersona());
        
        	int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql.toString())){
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

    protected String[] getOrdenCampos()
    {
        String[] ordenCampos = {EnvComunicacionMorososBean.C_IDFACTURA};
        
        return ordenCampos;
    }
    /**
     * Devolvemos los datos de la comunicacion de morosos para el envio seleccionado
     * @param idInstitucion
     * @param idEnvioDoc El id del envio de la tabla ENV_ENVIOS. Este es registro unico(FK 1-->1).
     * @return
     * @throws ClsExceptions
     * @throws SIGAException
     */
    public Vector<Hashtable> getEnvioMorosos (String idInstitucion, String idEnvioDoc) throws ClsExceptions,SIGAException {
		
    	Vector<Hashtable> envioMorosoVector = new Vector<Hashtable>();
    	try {
            RowsContainer rc = new RowsContainer(); 
            
		    Hashtable codigos = new Hashtable();
		    int contador=0;
		    //IDINSTITUCION, IDPERSONA, IDFACTURA, IDENVIO
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ");
            sql.append(EnvComunicacionMorososBean.C_IDINSTITUCION);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDPERSONA);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDFACTURA);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDENVIO);
            sql.append(" FROM ");
            sql.append(EnvComunicacionMorososBean.T_NOMBRETABLA);
            sql.append(" WHERE ");
            sql.append(EnvComunicacionMorososBean.C_IDINSTITUCION);
            contador++;
            codigos.put(new Integer(contador),idInstitucion);
            sql.append(" =:");
            sql.append(contador);
            
            sql.append(" AND ");
            sql.append(EnvComunicacionMorososBean.C_IDENVIODOC);
            contador++;
            codigos.put(new Integer(contador),idEnvioDoc);
            sql.append(" =:");
            sql.append(contador);
            
            //solo vendar 
            if (rc.findBind(sql.toString(),codigos)) {
               //La FK es 1 a 1 luego solo ohabra un registro
            	for (int i = 0; i < rc.size(); i++){
                    Row fila = (Row) rc.get(i);
                    Hashtable htEnvioMoroso=fila.getRow();
                    envioMorosoVector.add(htEnvioMoroso);
            	}
               
               
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, this.getClass().getName()+".getEnvioMorosos");
       }
		return envioMorosoVector;
	}
    /**
     * Devolvemos el array de todas las comunicaciones factura/persona. Nos saldran tantas como comunicaciones se hayan hecho 
     * @param idInstitucion
     * @param idPersona
     * @param idFactura
     * @return
     * @throws ClsExceptions
     * @throws SIGAException
     */
    public TreeMap getComunicacionesMorosos (Hashtable htEnvioMoroso) throws ClsExceptions,SIGAException {
		
		TreeMap tmComunicacionMorosos = new TreeMap();
    	try {
            RowsContainer rc = new RowsContainer(); 
            
		    Hashtable codigos = new Hashtable();
		    int contador=0;
		    //IDINSTITUCION, IDPERSONA, IDFACTURA, IDENVIO
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ");
            sql.append(EnvComunicacionMorososBean.C_IDINSTITUCION);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDPERSONA);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDFACTURA);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDENVIO);
            sql.append(",");
            
            sql.append(EnvComunicacionMorososBean.C_DESCRIPCION);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_PATHDOCUMENTO);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_FECHAENVIO);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDENVIODOC);
            sql.append(",");
            sql.append(EnvComunicacionMorososBean.C_IDDOCUMENTO);
            
            sql.append(" FROM ");
            sql.append(EnvComunicacionMorososBean.T_NOMBRETABLA);
            sql.append(" WHERE ");
            sql.append(EnvComunicacionMorososBean.C_IDINSTITUCION);
            contador++;
            
            codigos.put(new Integer(contador),UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDINSTITUCION));
            sql.append(" =:");
            sql.append(contador);
            
            sql.append(" AND ");
            sql.append(EnvComunicacionMorososBean.C_IDPERSONA);
            contador++;
            codigos.put(new Integer(contador),UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDPERSONA));
            sql.append(" =:");
            sql.append(contador);
            
            sql.append(" AND ");
            sql.append(EnvComunicacionMorososBean.C_IDFACTURA);
            contador++;
            codigos.put(new Integer(contador),UtilidadesHash.getString(htEnvioMoroso, EnvComunicacionMorososBean.C_IDFACTURA));
            sql.append(" =:");
            sql.append(contador);
            sql.append(" ORDER BY ");
            sql.append(EnvComunicacionMorososBean.C_IDENVIO);
            
			 										
            if (rc.findBind(sql.toString(),codigos)) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  Hashtable resultado=fila.getRow();
                  StringBuffer key = new StringBuffer();
                  key.append(UtilidadesHash.getString(resultado, EnvComunicacionMorososBean.C_IDINSTITUCION));
                  key.append(UtilidadesHash.getString(resultado, EnvComunicacionMorososBean.C_IDPERSONA));
                  key.append(UtilidadesHash.getString(resultado, EnvComunicacionMorososBean.C_IDFACTURA));
                  key.append(UtilidadesHash.getString(resultado, EnvComunicacionMorososBean.C_IDENVIO));
                  tmComunicacionMorosos.put(new Long(key.toString()), resultado);
               }
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, this.getClass().getName()+".getComunicacionesMorosos");
       }
		return tmComunicacionMorosos;
	}
	

	
}