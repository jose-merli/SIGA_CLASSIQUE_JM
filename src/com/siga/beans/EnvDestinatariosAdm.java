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

public class EnvDestinatariosAdm extends MasterBeanAdministrador {

    public EnvDestinatariosAdm(UsrBean usuario)
	{
	    super(EnvDestinatariosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvDestinatariosBean.C_IDENVIO,
            	EnvDestinatariosBean.C_IDPERSONA,
            	EnvDestinatariosBean.C_IDINSTITUCION,
            	EnvDestinatariosBean.C_DOMICILIO,
            	EnvDestinatariosBean.C_CODIGOPOSTAL,
            	EnvDestinatariosBean.C_FAX1,
            	EnvDestinatariosBean.C_FAX2,
            	EnvDestinatariosBean.C_CORREOELECTRONICO,
            	EnvDestinatariosBean.C_IDPAIS,
            	EnvDestinatariosBean.C_IDPROVINCIA,
            	EnvDestinatariosBean.C_IDPOBLACION,
            	EnvDestinatariosBean.C_POBLACIONEXTRANJERA,
            	EnvDestinatariosBean.C_NOMBRE,
            	EnvDestinatariosBean.C_APELLIDOS1,
            	EnvDestinatariosBean.C_APELLIDOS2,
            	EnvDestinatariosBean.C_NIFCIF,
            	EnvDestinatariosBean.C_MOVIL,
            	EnvDestinatariosBean.C_FECHAMODIFICACION,
            	EnvDestinatariosBean.C_USUMODIFICACION,
            	EnvDestinatariosBean.C_TIPODESTINATARIO,
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvDestinatariosBean.C_IDINSTITUCION, 
                		   EnvDestinatariosBean.C_IDENVIO,
                		   EnvDestinatariosBean.C_IDPERSONA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvDestinatariosBean.C_IDENVIO};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvDestinatariosBean bean = null;

		try
		{
			bean = new EnvDestinatariosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvDestinatariosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvDestinatariosBean.C_IDENVIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvDestinatariosBean.C_IDPERSONA));
			bean.setDomicilio(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_CODIGOPOSTAL));
			bean.setIdPais(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_POBLACIONEXTRANJERA));
			bean.setFax1(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_FAX2));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_CORREOELECTRONICO));
			bean.setNombre(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_NOMBRE));
			bean.setApellidos1(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_APELLIDOS1));
			bean.setApellidos2(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_APELLIDOS2));
			bean.setNifcif(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_NIFCIF));
			bean.setMovil(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_MOVIL));
			bean.setTipoDestinatario(UtilidadesHash.getString(hash, EnvDestinatariosBean.C_TIPODESTINATARIO));
			
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

			EnvDestinatariosBean b = (EnvDestinatariosBean) bean;
			
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_APELLIDOS1, b.getApellidos1());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_APELLIDOS2, b.getApellidos2());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_NIFCIF, b.getNifcif());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData, EnvDestinatariosBean.C_TIPODESTINATARIO, b.getTipoDestinatario());
			
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
		String T_ENV_DESTINATARIOS = EnvDestinatariosBean.T_NOMBRETABLA + " DES";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " PER";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " COL";
		String T_CEN_PAIS = CenPaisBean.T_NOMBRETABLA + " PA";
		String T_CEN_PROVINCIA = CenProvinciaBean.T_NOMBRETABLA + " PR";
		String T_CEN_POBLACION = CenPoblacionesBean.T_NOMBRETABLA + " PO";
		
		String DES_IDINSTITUCION = "DES." + EnvDestinatariosBean.C_IDINSTITUCION;
		String DES_IDENVIO = "DES." + EnvDestinatariosBean.C_IDENVIO;
		String DES_IDPERSONA = "DES." + EnvDestinatariosBean.C_IDPERSONA;
		String DES_IDPAIS = "DES." + EnvDestinatariosBean.C_IDPAIS;
		String DES_IDPROVINCIA = "DES." + EnvDestinatariosBean.C_IDPROVINCIA;
		String DES_IDPOBLACION = "DES." + EnvDestinatariosBean.C_IDPOBLACION;		
		String DES_POBLACIONEXTRANJERA = "DES." + EnvDestinatariosBean.C_POBLACIONEXTRANJERA;		
		String DES_DOMICILIO = "DES." + EnvDestinatariosBean.C_DOMICILIO;		
		String DES_CODIGOPOSTAL = "DES." + EnvDestinatariosBean.C_CODIGOPOSTAL;
		String DES_FAX1 = "DES." + EnvDestinatariosBean.C_FAX1;
		String DES_FAX2 = "DES." + EnvDestinatariosBean.C_FAX2;		
		String DES_CORREOELECTRONICO = "DES." + EnvDestinatariosBean.C_CORREOELECTRONICO;		
		String DES_NOMBRE = "DES." + EnvDestinatariosBean.C_NOMBRE;
		String DES_APELLIDOS1 = "DES." + EnvDestinatariosBean.C_APELLIDOS1;
		String DES_APELLIDOS2 = "DES." + EnvDestinatariosBean.C_APELLIDOS2;
		String DES_NIFCIF = "DES." + EnvDestinatariosBean.C_NIFCIF;
		String DES_MOVIL = "DES." + EnvDestinatariosBean.C_MOVIL;
		
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
		
		boolean fromPersona= !"-1".equals(idPersona);
			
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			StringBuffer sb= new StringBuffer();
			sb.append("SELECT ");	        
		    
			sb.append( DES_IDINSTITUCION + ", ");	
			sb.append(DES_IDPERSONA + ", ");		    
			sb.append( DES_DOMICILIO + ", ");	
			sb.append( DES_POBLACIONEXTRANJERA + ", ");	
			sb.append(DES_CODIGOPOSTAL + ", ");	
			sb.append( DES_FAX1 + ", ");		    
			sb.append( DES_FAX2 + ", ");	
			sb.append( DES_MOVIL + ", ");	
			sb.append( DES_CORREOELECTRONICO + ", ");	
		    if(fromPersona){
			    sb.append( PER_NOMBRE + ", ");	
			    sb.append(PER_APELLIDOS1 + ", ");		    
			    sb.append( PER_APELLIDOS2 + ", ");	
			    sb.append( PER_NIFCIF + ", ");	
			    sb.append(COL_NCOLEGIADO + ", ");
		    }else{
			    sb.append( DES_NOMBRE + ", ");	
			    sb.append( DES_APELLIDOS1 + ", ");		    
			    sb.append( DES_APELLIDOS2 + ", ");	
			    sb.append( DES_NIFCIF + ", ");	
		    }
		    
		    sb.append(PA_NOMBRE + ", ");	
		    sb.append(PR_NOMBRE + ", ");	
		    sb.append(PO_NOMBRE);	
		    
		    sb.append(" FROM ");
		    sb.append(T_ENV_DESTINATARIOS + ", " ); 
		    if(fromPersona){
		    	sb.append(T_CEN_COLEGIADO + ", " );
		    	sb.append(T_CEN_PERSONA + ", " );
		    }
		    sb.append(T_CEN_PAIS + ", " );
		    sb.append( T_CEN_PROVINCIA + ", " );
		    sb.append( T_CEN_POBLACION);
		    
		    sb.append(" WHERE ");
		    sb.append( DES_IDINSTITUCION + " = " + idInstitucion);
		    sb.append(" AND " + DES_IDENVIO + " = " + idEnvio);
		    sb.append(" AND " + DES_IDPERSONA + " = " + idPersona);
		    
		    if(fromPersona){
		    	sb.append(" AND " + DES_IDPERSONA + " = " + PER_IDPERSONA);
		    	sb.append(" AND " + DES_IDINSTITUCION + " = " + COL_IDINSTITUCION);
		    	sb.append(" AND " + DES_IDPERSONA + " = " + COL_IDPERSONA);
		    }
		    sb.append(" AND " + DES_IDPAIS + " = " + PA_IDPAIS);
		    sb.append(" AND " + DES_IDPROVINCIA + " = " + PR_IDPROVINCIA);
		    sb.append(" AND " + DES_IDPOBLACION + " = " + PO_IDPOBLACION);

			if (rc.query(sb.toString())) {
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
			Vector v = this.select(" WHERE " + EnvDestinatariosBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + EnvDestinatariosBean.C_IDENVIO + " = " + idEnvio +
			        				 " AND " + EnvDestinatariosBean.C_IDPERSONA + " = " + idPersona);
		    
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
    
}
