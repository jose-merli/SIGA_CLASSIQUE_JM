/*
 * Created on Mar 28, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;

public class EnvTempDestinatariosAdm extends MasterBeanAdministrador {

    public EnvTempDestinatariosAdm(UsrBean usuario)
	{
	    super(EnvTempDestinatariosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvTempDestinatariosBean.C_IDENVIO,
            	EnvTempDestinatariosBean.C_IDPERSONA,
            	EnvTempDestinatariosBean.C_IDINSTITUCION,
            	EnvTempDestinatariosBean.C_DOMICILIO,
            	EnvTempDestinatariosBean.C_CODIGOPOSTAL,
            	EnvTempDestinatariosBean.C_FAX1,
            	EnvTempDestinatariosBean.C_FAX2,
            	EnvTempDestinatariosBean.C_CORREOELECTRONICO,
            	EnvTempDestinatariosBean.C_IDPAIS,
            	EnvTempDestinatariosBean.C_IDPROVINCIA,
            	EnvTempDestinatariosBean.C_IDPOBLACION,
            	EnvTempDestinatariosBean.C_NOMBRE,
            	EnvTempDestinatariosBean.C_APELLIDOS1,
            	EnvTempDestinatariosBean.C_APELLIDOS2,
            	EnvTempDestinatariosBean.C_NIFCIF,
            	EnvTempDestinatariosBean.C_MENSAJE,
            	EnvTempDestinatariosBean.C_FECHAMODIFICACION,
            	EnvTempDestinatariosBean.C_USUMODIFICACION            	
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvTempDestinatariosBean.C_IDINSTITUCION, 
                		   EnvTempDestinatariosBean.C_IDENVIO,
                		   EnvTempDestinatariosBean.C_IDPERSONA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvTempDestinatariosBean.C_IDINSTITUCION, 
     		   				EnvTempDestinatariosBean.C_IDENVIO,
     		   				EnvTempDestinatariosBean.C_IDPERSONA};
		return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvTempDestinatariosBean bean = null;

		try
		{
			bean = new EnvTempDestinatariosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvTempDestinatariosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvTempDestinatariosBean.C_IDENVIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvTempDestinatariosBean.C_IDPERSONA));
			bean.setDomicilio(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_CODIGOPOSTAL));
			bean.setIdPais(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_IDPOBLACION));
			bean.setFax1(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_FAX2));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_CORREOELECTRONICO));
			bean.setNombre(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_NOMBRE));
			bean.setApellidos1(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_APELLIDOS1));
			bean.setApellidos2(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_APELLIDOS2));
			bean.setNifcif(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_NIFCIF));
			bean.setMensaje(UtilidadesHash.getString(hash, EnvTempDestinatariosBean.C_MENSAJE));			
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

			EnvTempDestinatariosBean b = (EnvTempDestinatariosBean) bean;
			
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_APELLIDOS1, b.getApellidos1());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_APELLIDOS2, b.getApellidos2());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_NIFCIF, b.getNifcif());
			UtilidadesHash.set(htData, EnvTempDestinatariosBean.C_MENSAJE, b.getMensaje());
			
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
    * Permite la búsqueda de datos de destinatario.
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
       
   public Vector getDatosDestinatario(String idEnvio, 
           							  String idInstitucion, 
           							  String idPersona)
   	throws ClsExceptions{
       
       Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_DESTINATARIOS = EnvTempDestinatariosBean.T_NOMBRETABLA + " DES";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " PER";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " COL";
		String T_CEN_PAIS = CenPaisBean.T_NOMBRETABLA + " PA";
		String T_CEN_PROVINCIA = CenProvinciaBean.T_NOMBRETABLA + " PR";
		String T_CEN_POBLACION = CenPoblacionesBean.T_NOMBRETABLA + " PO";
		
		String DES_IDINSTITUCION = "DES." + EnvTempDestinatariosBean.C_IDINSTITUCION;
		String DES_IDENVIO = "DES." + EnvTempDestinatariosBean.C_IDENVIO;
		String DES_IDPERSONA = "DES." + EnvTempDestinatariosBean.C_IDPERSONA;
		String DES_IDPAIS = "DES." + EnvTempDestinatariosBean.C_IDPAIS;
		String DES_IDPROVINCIA = "DES." + EnvTempDestinatariosBean.C_IDPROVINCIA;
		String DES_IDPOBLACION = "DES." + EnvTempDestinatariosBean.C_IDPOBLACION;		
		String DES_DOMICILIO = "DES." + EnvTempDestinatariosBean.C_DOMICILIO;		
		String DES_CODIGOPOSTAL = "DES." + EnvTempDestinatariosBean.C_CODIGOPOSTAL;
		String DES_FAX1 = "DES." + EnvTempDestinatariosBean.C_FAX1;
		String DES_FAX2 = "DES." + EnvTempDestinatariosBean.C_FAX2;		
		String DES_CORREOELECTRONICO = "DES." + EnvTempDestinatariosBean.C_CORREOELECTRONICO;		
		
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
		    
		    sql += DES_IDINSTITUCION + ", ";	
		    sql += DES_IDPERSONA + ", ";		    
		    sql += DES_DOMICILIO + ", ";	
		    sql += DES_CODIGOPOSTAL + ", ";	
		    sql += DES_FAX1 + ", ";		    
		    sql += DES_FAX2 + ", ";	
		    sql += DES_CORREOELECTRONICO + ", ";	
		    
		    sql += PER_NOMBRE + ", ";	
		    sql += PER_APELLIDOS1 + ", ";		    
		    sql += PER_APELLIDOS2 + ", ";	
		    sql += PER_NIFCIF + ", ";	
		    
		    sql += COL_NCOLEGIADO + ", ";
		    
		    sql += PA_NOMBRE + ", ";	
		    
		    sql += PR_NOMBRE + ", ";	
		    
		    sql += PO_NOMBRE;	
		    
		    
			sql += " FROM ";
		    sql += T_ENV_DESTINATARIOS + ", " + 
		    	   T_CEN_COLEGIADO + ", " +
		    	   T_CEN_PERSONA + ", " +
		    	   T_CEN_PAIS + ", " +
		    	   T_CEN_PROVINCIA + ", " +
		    	   T_CEN_POBLACION;
		    
		    sql += " WHERE ";
		    sql += DES_IDINSTITUCION + " = " + idInstitucion;
		    sql += " AND " + DES_IDENVIO + " = " + idEnvio;
		    sql += " AND " + DES_IDPERSONA + " = " + idPersona;
		    
			sql += " AND " + DES_IDPERSONA + " = " + PER_IDPERSONA;
			sql += " AND " + DES_IDINSTITUCION + " = " + COL_IDINSTITUCION;
			sql += " AND " + DES_IDPERSONA + " = " + COL_IDPERSONA;
			sql += " AND " + DES_IDPAIS + " = " + PA_IDPAIS;
			sql += " AND " + DES_IDPROVINCIA + " = " + PR_IDPROVINCIA;
			sql += " AND " + DES_IDPOBLACION + " = " + PO_IDPOBLACION;

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
	 * Comprueba si el destinatario ya ha sido insertado en la tabla de destinatarios
	 * @param  idEnvio id del envio
	 * @param  idInstitucion id de la institucion
	 * @param  idPersona id de la persona
	 * @return  boolean con el resultado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean existeDestinatario(String idEnvio, 
										String idInstitucion, 
										String idPersona) 
		throws ClsExceptions, SIGAException{
		try {
			Vector v = this.select(" WHERE " + EnvTempDestinatariosBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + EnvTempDestinatariosBean.C_IDENVIO + " = " + idEnvio +
			        				 " AND " + EnvTempDestinatariosBean.C_IDPERSONA + " = " + idPersona);
		    
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

	/** 
	 * Recoge informacion de la tabla para generar las etiquetas de los envios<br/>
	 * @param  idInstitucion - identificador de la institucion	
	 * @param  idEnvio - identificador del envio
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getDatosEtiquetas(String idInstitucion, String idEnvio) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			"("+EnvTempDestinatariosBean.T_NOMBRETABLA + "." + EnvTempDestinatariosBean.C_NOMBRE + " || ' ' || " +
	            			EnvTempDestinatariosBean.T_NOMBRETABLA + "." + EnvTempDestinatariosBean.C_APELLIDOS1 + " || ' ' || " +
	            			EnvTempDestinatariosBean.T_NOMBRETABLA + "." + EnvTempDestinatariosBean.C_APELLIDOS2 + ") AS NOMBRE_APELLIDOS," +
	            			EnvTempDestinatariosBean.T_NOMBRETABLA + "." + EnvTempDestinatariosBean.C_DOMICILIO + "," +
	            			EnvTempDestinatariosBean.T_NOMBRETABLA + "." + EnvTempDestinatariosBean.C_CODIGOPOSTAL + "," +
	            			" (SELECT " + CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + 
									 " FROM " + CenPoblacionesBean.T_NOMBRETABLA +
									 " WHERE " + 
									 EnvTempDestinatariosBean.T_NOMBRETABLA +"."+ EnvTempDestinatariosBean.C_IDPOBLACION + "=" + CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + ") AS POBLACION," +
							" (SELECT " + CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_NOMBRE + 
									 " FROM " + CenProvinciaBean.T_NOMBRETABLA +
									 " WHERE " + 
									 EnvTempDestinatariosBean.T_NOMBRETABLA +"."+ EnvTempDestinatariosBean.C_IDPROVINCIA + "=" + CenProvinciaBean.T_NOMBRETABLA +"."+ CenProvinciaBean.C_IDPROVINCIA + ") AS PROVINCIA" + 
							" FROM " + 
							EnvTempDestinatariosBean.T_NOMBRETABLA + 
							" WHERE " + 
							EnvTempDestinatariosBean.T_NOMBRETABLA +"."+ EnvTempDestinatariosBean.C_IDENVIO + "=" + idEnvio +
							" AND " +
							EnvTempDestinatariosBean.T_NOMBRETABLA +"."+ EnvTempDestinatariosBean.C_IDINSTITUCION + "=" + idInstitucion;
							
 																	
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable entrada=fila.getRow();
	                  datos.add(entrada);
	               }
	            }
	       }

	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener informacion para las etiquetas");
	       }
	       return datos;                        
	    }
	
}
