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
    
       RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			StringBuffer sb= new StringBuffer();
			sb.append(" SELECT DES.IDINSTITUCION,  DES.IDPERSONA,    DES.DOMICILIO,  DES.POBLACIONEXTRANJERA,   DES.CODIGOPOSTAL,   DES.FAX1,");
            sb.append(" DES.FAX2,  DES.MOVIL,  DES.CORREOELECTRONICO,  PER.NOMBRE,  PER.APELLIDOS1,  PER.APELLIDOS2,  PER.NIFCIF,  COL.NCOLEGIADO,");
            sb.append(" F_SIGA_GETRECURSO(PA.NOMBRE, 1) AS PAIS,  PR.NOMBRE AS PROVINCIA, PO.NOMBRE AS POBLACION");
            sb.append(" FROM ENV_DESTINATARIOS DES,   CEN_COLEGIADO COL, CEN_PERSONA PER, CEN_PAIS PA,CEN_PROVINCIAS PR,CEN_POBLACIONES   PO");
            sb.append(" WHERE DES.IDINSTITUCION ="+idInstitucion +" AND DES.IDENVIO ="+idEnvio+ " AND DES.IDPERSONA ="+idPersona+" AND DES.IDPERSONA = PER.IDPERSONA");
            sb.append(" AND DES.IDINSTITUCION = COL.IDINSTITUCION(+)  AND DES.IDPERSONA = COL.IDPERSONA(+) AND DES.IDPAIS = PA.IDPAIS(+) AND DES.IDPROVINCIA = PR.IDPROVINCIA(+)");
            sb.append(" AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='CEN_PERSONA'");
            sb.append(" UNION "); 
            sb.append(" SELECT DES.IDINSTITUCION, DES.IDPERSONA,  DES.DOMICILIO, DES.POBLACIONEXTRANJERA, DES.CODIGOPOSTAL, DES.FAX1, DES.FAX2, DES.MOVIL,");
            sb.append(" DES.CORREOELECTRONICO, PER.NOMBRE,  PER.APELLIDO1,  PER.APELLIDO2, PER.nif, COL.NCOLEGIADO, F_SIGA_GETRECURSO(PA.NOMBRE, 1) AS PAIS,");
            sb.append(" PR.NOMBRE AS PROVINCIA, PO.NOMBRE AS POBLACION FROM ENV_DESTINATARIOS DES, CEN_COLEGIADO     COL, SCS_PERSONAJG     PER,");
            sb.append(" CEN_PAIS  PA, CEN_PROVINCIAS  PR, CEN_POBLACIONES   PO  WHERE DES.IDINSTITUCION ="+idInstitucion+"  AND DES.IDENVIO ="+idEnvio+ " AND DES.IDPERSONA ="+idPersona);
            sb.append(" AND DES.IDPERSONA = PER.IDPERSONA  AND DES.IDINSTITUCION = COL.IDINSTITUCION(+) AND DES.IDPERSONA = COL.IDPERSONA(+) AND DES.IDPAIS = PA.IDPAIS(+)");
            sb.append(" AND DES.IDPROVINCIA = PR.IDPROVINCIA(+) AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='SCS_PERSONAJG'");
            sb.append(" AND PER.IDINSTITUCION="+idInstitucion);
  

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
