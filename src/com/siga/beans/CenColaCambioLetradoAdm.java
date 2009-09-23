package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.EjecucionPLs;


/**
 * CenColaCambioLetradoAdm
 * 
 * @author Desconocido
 * @since 2007-03-19
 * @version 2008-05-30 adrianag: Limpieza general
 */
public class CenColaCambioLetradoAdm extends MasterBeanAdministrador
{
	//////////////////// CONSTRUCTORES ////////////////////
    public CenColaCambioLetradoAdm (UsrBean usu) {
		super (CenColaCambioLetradoBean.T_NOMBRETABLA, usu);
	}
	
    
    
	//////////////////// METODOS COMUNES DE ADMINISTRADOR ////////////////////
    protected String[] getCamposBean()
    {
		String [] campos = 
		{
				CenColaCambioLetradoBean.C_FECHACAMBIO,
				CenColaCambioLetradoBean.C_FECHAMODIFICACION, 
				CenColaCambioLetradoBean.C_IDCAMBIO,
				CenColaCambioLetradoBean.C_IDDIRECCION,
				CenColaCambioLetradoBean.C_IDINSTITUCION,
				CenColaCambioLetradoBean.C_IDPERSONA,
				CenColaCambioLetradoBean.C_IDTIPOCAMBIO,
				CenColaCambioLetradoBean.C_USUMODIFICACION
		};
		return campos;
    } //getCamposBean()
	
    protected String[] getClavesBean()
    {
		String [] campos = 
		{
				CenColaCambioLetradoBean.C_IDCAMBIO, 
    			CenColaCambioLetradoBean.C_IDINSTITUCION, 	
    			CenColaCambioLetradoBean.C_IDPERSONA
    	};
		return campos;
    } //getClavesBean()
	
    protected String[] getOrdenCampos()
    {
		String [] campos = 
		{
				CenColaCambioLetradoBean.C_FECHACAMBIO,
		        CenColaCambioLetradoBean.C_IDCAMBIO, 
				CenColaCambioLetradoBean.C_IDINSTITUCION, 	
				CenColaCambioLetradoBean.C_IDPERSONA
		};
        return campos;
    } //getOrdenCampos()

    protected MasterBean hashTableToBean(Hashtable hash)
    		throws ClsExceptions 
    {
		CenColaCambioLetradoBean bean = null;
		
		try {
			bean = new CenColaCambioLetradoBean();
			bean.setFechaCambio(UtilidadesHash.getString(hash, CenColaCambioLetradoBean.C_FECHACAMBIO));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenColaCambioLetradoBean.C_FECHAMODIFICACION));
			bean.setIdCambio(UtilidadesHash.getLong(hash, CenColaCambioLetradoBean.C_IDCAMBIO));
			bean.setIdDireccion(UtilidadesHash.getLong(hash, CenColaCambioLetradoBean.C_IDDIRECCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenColaCambioLetradoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CenColaCambioLetradoBean.C_IDPERSONA));
			bean.setIdTipoCambio(UtilidadesHash.getInteger(hash, CenColaCambioLetradoBean.C_IDTIPOCAMBIO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenColaCambioLetradoBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;    
	} //hashTableToBean()
	
    protected Hashtable beanToHashTable(MasterBean bean)
    		throws ClsExceptions 
    {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenColaCambioLetradoBean b = (CenColaCambioLetradoBean) bean;
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_FECHACAMBIO, 			b.getFechaCambio());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDCAMBIO, 			b.getIdCambio());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDDIRECCION, 			b.getIdDireccion());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDPERSONA, 			b.getIdPersona());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDTIPOCAMBIO, 		b.getIdTipoCambio());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_USUMODIFICACION, 		b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
    } //beanToHashTable()
	
    
    
	//////////////////// OTROS METODOS ////////////////////
    public boolean insertarCambioEnCola (int idTipoCambio,
    									 Integer idInstitucion,
    									 Long idPersona,
    									 Long idDireccion) 
    {
        try
        {
			CenColaCambioLetradoBean bean = new CenColaCambioLetradoBean ();
			bean.setFechaCambio ("SYSDATE");
			bean.setIdCambio (this.getNuevoId (idInstitucion, idPersona));
			if (idDireccion != null) bean.setIdDireccion (idDireccion);
			bean.setIdInstitucion (idInstitucion);
			bean.setIdPersona (idPersona);
			bean.setIdTipoCambio (new Integer (idTipoCambio));
			return this.insert (bean);
        }
        catch (Exception e) {
            e.printStackTrace ();
            this.setError (e.getMessage () + " (" + 
            		CenColaCambioLetradoBean.T_NOMBRETABLA + ")");
            return false;
        }
    } //insertarCambioEnCola()
	
	public Long getNuevoId(Integer idInstitucion,
						   Long idPersona)
			throws ClsExceptions 
	{
		try {
			String select = 
				"select max(" +CenColaCambioLetradoBean.C_IDCAMBIO+") + 1 AS ID " +
				"  from "+CenColaCambioLetradoBean.T_NOMBRETABLA+"" + 
				" where "+CenColaCambioLetradoBean.C_IDINSTITUCION+" = " + idInstitucion + 
				"   and "+CenColaCambioLetradoBean.C_IDPERSONA+" = " + idPersona;					  			
			
			RowsContainer rc = new RowsContainer();
			if (rc.findForUpdate(select)) {
				Hashtable prueba = ((Row) rc.get(0)).getRow();
				Long id = UtilidadesHash.getLong(prueba, "ID");
				if (id == null) 
					return new Long(1);
				else 
				    return id;								
			}
			throw new ClsExceptions ("Error al obtener el id del cambio en B.D."); 
		}
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al obtener el id del cambio en B.D."); 
		}
	} //getNuevoId()
	
	public void chequearCola() 
	{
	    try {
		    ReadProperties rp = new ReadProperties("SIGA.properties");			       
			String pathFicheroLog = rp.returnProperty("LogAdmin.archivo");
			String nombreFichero = rp.returnProperty("LogAdmin.archivo.gestionColas");
			SIGALogging log = new SIGALogging(pathFicheroLog + nombreFichero);
	        
       		UserTransaction t = this.usrbean.getTransaction();
			
       		Vector vCola = this.select();
	        for (int i = 0; i < vCola.size(); i++) {
	            
	            CenColaCambioLetradoBean bean = (CenColaCambioLetradoBean)vCola.get(i);
	            UsrBean us2= UsrBean.UsrBeanAutomatico(bean.getIdInstitucion().toString());
	            CenPersonaAdm personaAdm2 = new CenPersonaAdm (us2);
	            
	            try {
	                String nombreCliente = personaAdm2.obtenerNombreApellidos
	                		("" + bean.getIdPersona());

		            t.begin();
		            
		            String rc[] = EjecucionPLs.ejecutarPL_ActualizarDatosLetrado
							(bean.getIdInstitucion(), bean.getIdPersona(), 
							bean.getIdTipoCambio(), bean.getIdDireccion(), 
							this.usuModificacion);
					
		            if (!this.delete(bean)) {
		                log.writeLogGestorColaSincronizarDatos(SIGALogging.ERROR, 
		                		bean.getIdInstitucion(), bean.getIdPersona(), 
		                		nombreCliente, "Error borrando elemento de la cola");
	                    t.rollback();
	                	continue;
                	}
	                else {
		                if ((new Integer(rc[0])).intValue() < 0) { 
			                log.writeLogGestorColaSincronizarDatos
			                		(SIGALogging.ERROR, bean.getIdInstitucion(), 
			                		bean.getIdPersona(), nombreCliente, rc[1]);
		                }else{
		                	 log.writeLogGestorColaSincronizarDatos
		                	 		(SIGALogging.INFO, bean.getIdInstitucion(), 
		                	 		bean.getIdPersona(), nombreCliente, rc[1]); 	
		                }
                	}
	                
	                t.commit();

	            }
	            catch (Exception e) {
		            log.write ("Error: " + e.getMessage());
		            t.rollback();
                }
	        } 
	    }
	    catch (Exception e) {  }
	} //chequearCola()
}
