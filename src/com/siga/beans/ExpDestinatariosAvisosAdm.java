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

public class ExpDestinatariosAvisosAdm extends MasterBeanAdministrador {

    public ExpDestinatariosAvisosAdm(UsrBean usuario)
	{
	    super(ExpDestinatariosAvisosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpDestinatariosAvisosBean.C_IDTIPOEXPEDIENTE,
            	ExpDestinatariosAvisosBean.C_IDPERSONA,
            	ExpDestinatariosAvisosBean.C_IDINSTITUCION,
            	ExpDestinatariosAvisosBean.C_DOMICILIO,
            	ExpDestinatariosAvisosBean.C_CODIGOPOSTAL,
            	ExpDestinatariosAvisosBean.C_FAX1,
            	ExpDestinatariosAvisosBean.C_FAX2,
            	ExpDestinatariosAvisosBean.C_CORREOELECTRONICO,
            	ExpDestinatariosAvisosBean.C_IDPAIS,
            	ExpDestinatariosAvisosBean.C_IDPROVINCIA,
            	ExpDestinatariosAvisosBean.C_IDPOBLACION,
            	ExpDestinatariosAvisosBean.C_POBLACIONEXTRANJERA,
            	ExpDestinatariosAvisosBean.C_NOMBRE,
            	ExpDestinatariosAvisosBean.C_APELLIDOS1,
            	ExpDestinatariosAvisosBean.C_APELLIDOS2,
            	ExpDestinatariosAvisosBean.C_NIFCIF,
            	ExpDestinatariosAvisosBean.C_MOVIL,
            	ExpDestinatariosAvisosBean.C_FECHAMODIFICACION,
            	ExpDestinatariosAvisosBean.C_USUMODIFICACION,
            	ExpDestinatariosAvisosBean.C_TIPODESTINATARIO,
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpDestinatariosAvisosBean.C_IDINSTITUCION, 
                		   ExpDestinatariosAvisosBean.C_IDTIPOEXPEDIENTE,
                		   ExpDestinatariosAvisosBean.C_IDPERSONA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {ExpDestinatariosAvisosBean.C_IDPERSONA};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpDestinatariosAvisosBean bean = null;

		try
		{
			bean = new ExpDestinatariosAvisosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpDestinatariosAvisosBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpDestinatariosAvisosBean.C_IDTIPOEXPEDIENTE));
			bean.setIdPersona(UtilidadesHash.getLong(hash, ExpDestinatariosAvisosBean.C_IDPERSONA));
			bean.setDomicilio(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_CODIGOPOSTAL));
			bean.setIdPais(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_POBLACIONEXTRANJERA));
			bean.setFax1(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_FAX2));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_CORREOELECTRONICO));
			bean.setNombre(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_NOMBRE));
			bean.setApellidos1(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_APELLIDOS1));
			bean.setApellidos2(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_APELLIDOS2));
			bean.setNifcif(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_NIFCIF));
			bean.setMovil(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_MOVIL));
			bean.setTipoDestinatario(UtilidadesHash.getString(hash, ExpDestinatariosAvisosBean.C_TIPODESTINATARIO));
			
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

			ExpDestinatariosAvisosBean b = (ExpDestinatariosAvisosBean) bean;
			
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_APELLIDOS1, b.getApellidos1());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_APELLIDOS2, b.getApellidos2());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_NIFCIF, b.getNifcif());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData, ExpDestinatariosAvisosBean.C_TIPODESTINATARIO, b.getTipoDestinatario());
			
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
       
   public Vector getDatosDestinatario(String idTipoExpediente, 
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
            sb.append(" FROM EXP_DESTINATARIOSAVISOS DES,   CEN_COLEGIADO COL, CEN_PERSONA PER, CEN_PAIS PA,CEN_PROVINCIAS PR,CEN_POBLACIONES   PO");
            sb.append(" WHERE DES.IDINSTITUCION ="+idInstitucion +" AND DES.IDTIPOEXPEDIENTE ="+idTipoExpediente+ " AND DES.IDPERSONA ="+idPersona+" AND DES.IDPERSONA = PER.IDPERSONA");
            sb.append(" AND DES.IDINSTITUCION = COL.IDINSTITUCION(+)  AND DES.IDPERSONA = COL.IDPERSONA(+) AND DES.IDPAIS = PA.IDPAIS(+) AND DES.IDPROVINCIA = PR.IDPROVINCIA(+)");
            sb.append(" AND DES.IDPOBLACION = PO.IDPOBLACION(+) and DES.TIPODESTINATARIO='CEN_PERSONA'");
            
             

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
	public boolean existeDestinatario(String idTipoExpediente, 
										String idInstitucion, 
										String idPersona) 
		throws ClsExceptions, SIGAException{
		try {
			Vector v = this.select(" WHERE " + ExpDestinatariosAvisosBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + ExpDestinatariosAvisosBean.C_IDTIPOEXPEDIENTE + " = " + idTipoExpediente +
			        				 " AND " + ExpDestinatariosAvisosBean.C_IDPERSONA + " = " + idPersona);
		    
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
	public Vector getDestinatariosManuales (String idInstitucion, String idTipoExpediente)
	throws ClsExceptions{

    Vector datos = new Vector();

	//NOMBRES TABLAS PARA LA JOIN
	String T_ENV_DESTINATARIOS = ExpDestinatariosAvisosBean.T_NOMBRETABLA + " D";
	String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " P";
	String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " C";

	//Tabla env_destinatarios
	String D_IDINSTITUCION = "D." + ExpDestinatariosAvisosBean.C_IDINSTITUCION;
	String D_IDTIPOEXPEDIENTES = "D." + ExpDestinatariosAvisosBean.C_IDTIPOEXPEDIENTE;
	String D_IDPERSONA = "D." + ExpDestinatariosAvisosBean.C_IDPERSONA;
	String D_NOMBRE = "D." + ExpDestinatariosAvisosBean.C_NOMBRE;
	String D_APELLIDO1 = "D." + ExpDestinatariosAvisosBean.C_APELLIDOS1;
	String D_APELLIDO2 = "D." + ExpDestinatariosAvisosBean.C_APELLIDOS2;
	String D_NIFCIF = "D." + ExpDestinatariosAvisosBean.C_NIFCIF;
	String D_TIPODESTINATARIO = "D." + ExpDestinatariosAvisosBean.C_TIPODESTINATARIO;

	//Tabla cen_persona
	String P_IDPERSONA = "P." + CenPersonaBean.C_IDPERSONA;
	String P_NOMBRE = "P." + CenPersonaBean.C_NOMBRE;
	String P_APELLIDO1 = "P." + CenPersonaBean.C_APELLIDOS1;
	String P_APELLIDO2 = "P." + CenPersonaBean.C_APELLIDOS2;
	String P_NIFCIF = "P." + CenPersonaBean.C_NIFCIF;

	//Tabla cen_colegiado
	String C_IDPERSONA = "C." + CenColegiadoBean.C_IDPERSONA;
	String C_NCOLEGIADO = "C." + CenColegiadoBean.C_NCOLEGIADO;
	String C_IDINSTITUCION = "C." + CenColegiadoBean.C_IDINSTITUCION;

	// Acceso a BBDD
	RowsContainer rc = null;
	try {
		rc = new RowsContainer();

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ");
		sql.append("DECODE("+P_IDPERSONA+",-1,");  
		sql.append(D_NOMBRE + "||' '|| " + D_APELLIDO1 + "||' '|| " + D_APELLIDO2+",");
		sql.append(P_NOMBRE + "||' '|| " + P_APELLIDO1 + "||' '|| " + P_APELLIDO2 + ") AS NOMBREYAPELLIDOS, ");
		sql.append(C_NCOLEGIADO + ", ");
		sql.append("DECODE("+P_IDPERSONA+",-1,"+  D_NIFCIF + ", "+  P_NIFCIF + ") NIFCIF, ");
		sql.append(P_IDPERSONA);
		sql.append(", " + D_TIPODESTINATARIO );
			
		sql.append(" FROM "+T_ENV_DESTINATARIOS + ", "+ T_CEN_PERSONA + ", "+ T_CEN_COLEGIADO);
			
		sql.append(" WHERE ");
		sql.append(D_IDINSTITUCION + " = " + idInstitucion);
		sql.append(" AND " + D_IDTIPOEXPEDIENTES + " = " + idTipoExpediente);
		sql.append(" AND " + D_IDPERSONA + " = " + P_IDPERSONA);
		sql.append(" AND " + D_IDINSTITUCION + " = " + C_IDINSTITUCION + "(+)");
		sql.append(" AND " + D_IDPERSONA + " = " + C_IDPERSONA + "(+) "); 
		sql.append(" AND D.Tipodestinatario ='");
		sql.append(ExpDestinatariosAvisosBean.TIPODESTINATARIO_CENPERSONA);
		sql.append("'  ");
		
		
		
		sql.append(" ORDER BY NOMBREYAPELLIDOS ");   
		
	

		//ClsLogging.writeFileLog("EnvEnviosAdm.getDestinatariosManuales -> QUERY: "+sql,3);
		
		if (rc.query(sql.toString())) {
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


    
}
