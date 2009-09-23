/*
 * Created on Apr 05, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;

public class EnvRemitentesAdm extends MasterBeanAdministrador {

    public EnvRemitentesAdm(UsrBean usuario)
	{
	    super(EnvRemitentesBean.T_NOMBRETABLA, usuario);
	}
    


	/* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvRemitentesBean.C_IDENVIO,
            	EnvRemitentesBean.C_IDPERSONA,
            	EnvRemitentesBean.C_IDINSTITUCION,
            	EnvRemitentesBean.C_DOMICILIO,
            	EnvRemitentesBean.C_CODIGOPOSTAL,
            	EnvRemitentesBean.C_FAX1,
            	EnvRemitentesBean.C_FAX2,
            	EnvRemitentesBean.C_MOVIL,
            	EnvRemitentesBean.C_CORREOELECTRONICO,
            	EnvRemitentesBean.C_IDPAIS,
            	EnvRemitentesBean.C_IDPROVINCIA,
            	EnvRemitentesBean.C_IDPOBLACION,
            	EnvRemitentesBean.C_POBLACIONEXTRANJERA,
            	EnvRemitentesBean.C_DESCRIPCION,
            	EnvRemitentesBean.C_FECHAMODIFICACION,
            	EnvRemitentesBean.C_USUMODIFICACION            	
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvRemitentesBean.C_IDINSTITUCION, 
                		   EnvRemitentesBean.C_IDENVIO,
                		   EnvRemitentesBean.C_IDPERSONA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvRemitentesBean.C_IDENVIO};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvRemitentesBean bean = null;

		try
		{
			bean = new EnvRemitentesBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvRemitentesBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvRemitentesBean.C_IDENVIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvRemitentesBean.C_IDPERSONA));
			bean.setDomicilio(UtilidadesHash.getString(hash, EnvRemitentesBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, EnvRemitentesBean.C_CODIGOPOSTAL));
			bean.setIdPais(UtilidadesHash.getString(hash, EnvRemitentesBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash, EnvRemitentesBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, EnvRemitentesBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash, EnvRemitentesBean.C_POBLACIONEXTRANJERA));
			bean.setFax1(UtilidadesHash.getString(hash, EnvRemitentesBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, EnvRemitentesBean.C_FAX2));
			bean.setFax2(UtilidadesHash.getString(hash, EnvRemitentesBean.C_MOVIL));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, EnvRemitentesBean.C_CORREOELECTRONICO));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvRemitentesBean.C_DESCRIPCION));
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

			EnvRemitentesBean b = (EnvRemitentesBean) bean;
			
			UtilidadesHash.set(htData, EnvRemitentesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData, EnvRemitentesBean.C_DESCRIPCION, b.getDescripcion());			
		}

		catch (Exception e)
		{
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
   /**
    * @author juan.grau
    *
    * Permite la búsqueda de datos de remitente.
    *
    * @param String idInstitucion
    * @param String idEnvio
    * @param String idPersona
    * 
    * @return Vector (Rows)
    * 
    * @throws ClsExceptions
    *  
    */    
       
   public Vector getDatosRemitente(String idEnvio, 
           							String idInstitucion, 
           							String idPersona)
   	throws ClsExceptions{
       
       Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_REMITENTES = EnvRemitentesBean.T_NOMBRETABLA + " REM";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " PER";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " COL";
		String T_CEN_PAIS = CenPaisBean.T_NOMBRETABLA + " PA";
		String T_CEN_PROVINCIA = CenProvinciaBean.T_NOMBRETABLA + " PR";
		String T_CEN_POBLACION = CenPoblacionesBean.T_NOMBRETABLA + " PO";
		
		String REM_IDINSTITUCION = "REM." + EnvRemitentesBean.C_IDINSTITUCION;
		String REM_IDENVIO = "REM." + EnvRemitentesBean.C_IDENVIO;
		String REM_IDPERSONA = "REM." + EnvRemitentesBean.C_IDPERSONA;
		String REM_IDPAIS = "REM." + EnvRemitentesBean.C_IDPAIS;
		String REM_IDPROVINCIA = "REM." + EnvRemitentesBean.C_IDPROVINCIA;
		String REM_POBLACIONENTRANJERA = "REM." + EnvRemitentesBean.C_POBLACIONEXTRANJERA;
		String REM_IDPOBLACION = "REM." + EnvRemitentesBean.C_IDPOBLACION;		
		String REM_DOMICILIO = "REM." + EnvRemitentesBean.C_DOMICILIO;		
		String REM_CODIGOPOSTAL = "REM." + EnvRemitentesBean.C_CODIGOPOSTAL;
		String REM_FAX1 = "REM." + EnvRemitentesBean.C_FAX1;
		String REM_FAX2 = "REM." + EnvRemitentesBean.C_FAX2;		
		String REM_MOVIL = "REM." + EnvRemitentesBean.C_MOVIL;
		String REM_CORREOELECTRONICO = "REM." + EnvRemitentesBean.C_CORREOELECTRONICO;		
		String REM_DESCRIPCION = "REM." + EnvRemitentesBean.C_DESCRIPCION;		
		
		String PER_IDPERSONA = "PER." + CenPersonaBean.C_IDPERSONA;
		String PER_NOMBRE = "PER." + CenPersonaBean.C_NOMBRE;
		String PER_APELLIDOS1 = "PER." + CenPersonaBean.C_APELLIDOS1;
		String PER_APELLIDOS2 = "PER." + CenPersonaBean.C_APELLIDOS2;
		String PER_NIFCIF = "PER." + CenPersonaBean.C_NIFCIF;
		
		String COL_IDINSTITUCION = "COL." + CenColegiadoBean.C_IDINSTITUCION + "(+)";
		String COL_IDPERSONA = "COL." + CenColegiadoBean.C_IDPERSONA + "(+)";
		String COL_NCOLEGIADO = "COL." + CenColegiadoBean.C_NCOLEGIADO;
		
		String PA_IDPAIS = "PA." + CenPaisBean.C_IDPAIS + "(+)";
		String PA_NOMBRE = UtilidadesMultidioma.getCampoMultidiomaSimple("PA." + CenPaisBean.C_NOMBRE, this.usrbean.getLanguage()) + " AS PAIS";
		
		String PR_IDPROVINCIA = "PR." + CenProvinciaBean.C_IDPROVINCIA + "(+)";
		String PR_NOMBRE = "PR." + CenProvinciaBean.C_NOMBRE + " AS PROVINCIA";
		
		String PO_IDPOBLACION = "PO." + CenPoblacionesBean.C_IDPOBLACION + "(+)";
		String PO_NOMBRE = "PO." + CenPoblacionesBean.C_NOMBRE + " AS POBLACION";
			
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String sql = "SELECT ";	        
		    
		    sql += REM_IDINSTITUCION + ", ";	
		    sql += REM_IDPERSONA + ", ";		    
		    sql += REM_POBLACIONENTRANJERA + ", ";		    
		    sql += REM_DOMICILIO + ", ";	
		    sql += REM_CODIGOPOSTAL + ", ";	
		    sql += REM_FAX1 + ", ";		    
		    sql += REM_FAX2 + ", ";	
		    sql += REM_MOVIL + ", ";
		    sql += REM_CORREOELECTRONICO + ", ";	
		    sql += REM_DESCRIPCION + ", ";	
		    
		    sql += PER_NOMBRE + ", ";	
		    sql += PER_APELLIDOS1 + ", ";		    
		    sql += PER_APELLIDOS2 + ", ";	
		    sql += PER_NIFCIF + ", ";	
		    
		    sql += COL_NCOLEGIADO + ", ";
		    
		    sql += PA_NOMBRE + ", ";	
		    
		    sql += PR_NOMBRE + ", ";	
		    
		    sql += PO_NOMBRE;	
		    
		    
			sql += " FROM ";
		    sql += T_ENV_REMITENTES + ", " + 
		    	   T_CEN_COLEGIADO + ", " +
		    	   T_CEN_PERSONA + ", " +
		    	   T_CEN_PAIS + ", " +
		    	   T_CEN_PROVINCIA + ", " +
		    	   T_CEN_POBLACION;
		    
		    sql += " WHERE ";
		    sql += REM_IDINSTITUCION + " = " + idInstitucion;
		    sql += " AND " + REM_IDENVIO + " = " + idEnvio;
		    sql += " AND " + REM_IDPERSONA + " = " + idPersona;
		    
			sql += " AND " + REM_IDPERSONA + " = " + PER_IDPERSONA;
			sql += " AND " + REM_IDINSTITUCION + " = " + COL_IDINSTITUCION;
			sql += " AND " + REM_IDPERSONA + " = " + COL_IDPERSONA;
			sql += " AND " + REM_IDPAIS + " = " + PA_IDPAIS;
			sql += " AND " + REM_IDPROVINCIA + " = " + PR_IDPROVINCIA;
			sql += " AND " + REM_IDPOBLACION + " = " + PO_IDPOBLACION;

			ClsLogging.writeFileLog("EnvRemitentesAdm -> QUERY: "+sql,3);
			
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
   
	/** 
	 * Comprueba si el remitente ya ha sido insertado en la tabla de remitentes
	 * @param  idEnvio id del envio
	 * @param  idInstitucion id de la institucion
	 * @param  idPersona id de la persona
	 * @return  boolean con el resultado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean existeRemitente(String idEnvio, 
									String idInstitucion, 
									String idPersona) 
		throws ClsExceptions, SIGAException{
		try {
			Vector v = this.select(" WHERE " + EnvRemitentesBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + EnvRemitentesBean.C_IDENVIO + " = " + idEnvio +
			        				 " AND " + EnvRemitentesBean.C_IDPERSONA + " = " + idPersona);
		    
			if ((v != null) && (v.size()>0)) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	
	public Vector getRemitentes(String idInstitucion, String idEnvio){
		String remitentes="", consulta="";
		Vector v = null;
		
		consulta = " SELECT persona."+CenPersonaBean.C_NOMBRE+"|| ' ' ||"+
							"persona."+CenPersonaBean.C_APELLIDOS1+"|| ' ' ||"+
							"persona."+CenPersonaBean.C_APELLIDOS2+" AS PERSONA"+
				   " FROM "+EnvRemitentesBean.T_NOMBRETABLA+ " remit "+
				   ","+CenPersonaBean.T_NOMBRETABLA+ " persona "+
				   " WHERE remit."+EnvRemitentesBean.C_IDINSTITUCION+" = "+idInstitucion+
				   " AND remit."+EnvRemitentesBean.C_IDENVIO+" = "+idEnvio+
				   " AND remit."+EnvRemitentesBean.C_IDPERSONA+" = persona."+CenPersonaBean.C_IDPERSONA;
		
		try {
			v = this.selectGenerico(consulta);					
		} catch(Exception e){
			remitentes = null;
		}		
		return v;
	}
    	
	public String getNombreRemitentes(Vector vRemitentes){
		String remitentes = "";
		try {	
			if (vRemitentes!= null && vRemitentes.size()>0){
				//Tomo el primero
				remitentes = (String)((Hashtable)vRemitentes.get(0)).get("PERSONA");
				//Concateno el resto de remitentes separandolos por "," :
				for (int i=1; i<vRemitentes.size(); i++){
					remitentes += ","+((Hashtable)vRemitentes.get(i)).get("PERSONA");
				}
			}			
		} catch(Exception e){
			remitentes = null;
		}
		return remitentes;
	}
	/**
	 * Inserta en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en EnvRemitentesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
}
