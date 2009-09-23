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


public class EnvPlantillaRemitentesAdm extends MasterBeanAdministrador {

    public EnvPlantillaRemitentesAdm(UsrBean usuario)
	{
	    super(EnvPlantillaRemitentesBean.T_NOMBRETABLA, usuario);
	}
    
    protected String[] getCamposBean() 
    {
        String[] campos = {	EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS,		EnvPlantillaRemitentesBean.C_IDTIPOENVIOS,
        					EnvPlantillaRemitentesBean.C_IDPERSONA,      		EnvPlantillaRemitentesBean.C_IDINSTITUCION,
							EnvPlantillaRemitentesBean.C_DOMICILIO,        		EnvPlantillaRemitentesBean.C_CODIGOPOSTAL,
							EnvPlantillaRemitentesBean.C_FAX1,	         		EnvPlantillaRemitentesBean.C_FAX2,
							EnvPlantillaRemitentesBean.C_CORREOELECTRONICO,		EnvPlantillaRemitentesBean.C_IDPAIS,
							EnvPlantillaRemitentesBean.C_IDPROVINCIA,      		EnvPlantillaRemitentesBean.C_IDPOBLACION,
							EnvPlantillaRemitentesBean.C_POBLACIONEXTRANJERA, 	EnvPlantillaRemitentesBean.C_DESCRIPCION,
							EnvPlantillaRemitentesBean.C_FECHAMODIFICACION,    	EnvPlantillaRemitentesBean.C_USUMODIFICACION,
							EnvPlantillaRemitentesBean.C_MOVIL};
		return campos;
    }

    protected String[] getClavesBean() 
    {
        String[] claves = {EnvPlantillaRemitentesBean.C_IDINSTITUCION,		EnvPlantillaRemitentesBean.C_IDTIPOENVIOS,
                		   EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS,  EnvPlantillaRemitentesBean.C_IDPERSONA};
		return claves;
    }

    protected String[] getOrdenCampos() 
    {
        String[] campos = {EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS, EnvPlantillaRemitentesBean.C_IDTIPOENVIOS};
		return campos;
    }

    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
        EnvPlantillaRemitentesBean bean = null;
		try
		{
			bean = new EnvPlantillaRemitentesBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvPlantillaRemitentesBean.C_IDINSTITUCION));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvPlantillaRemitentesBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvPlantillaRemitentesBean.C_IDPERSONA));
			bean.setDomicilio(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_DOMICILIO));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_CODIGOPOSTAL));
			bean.setIdPais(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_IDPROVINCIA));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_POBLACIONEXTRANJERA));
			bean.setFax1(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_FAX2));
			bean.setMovil(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_MOVIL));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_CORREOELECTRONICO));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash, EnvPlantillaRemitentesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, EnvPlantillaRemitentesBean.C_USUMODIFICACION));
		}
		catch (Exception e)	{
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
			EnvPlantillaRemitentesBean b = (EnvPlantillaRemitentesBean) bean;
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_DESCRIPCION, b.getDescripcion());			
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_FECHAMODIFICACION, b.getFechaMod());			
			UtilidadesHash.set(htData, EnvPlantillaRemitentesBean.C_USUMODIFICACION, b.getUsuMod());			
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
    * @throws ClsExceptions
    *  
    */    
       
   public Vector getDatosRemitente(String idTipoEnvios, String idPlantillaEnvios, String idInstitucion, String idPersona) throws ClsExceptions
   {
       Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_REMITENTES = EnvPlantillaRemitentesBean.T_NOMBRETABLA + " REM";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " PER";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " COL";
		String T_CEN_PAIS = CenPaisBean.T_NOMBRETABLA + " PA";
		String T_CEN_PROVINCIA = CenProvinciaBean.T_NOMBRETABLA + " PR";
		String T_CEN_POBLACION = CenPoblacionesBean.T_NOMBRETABLA + " PO";
		
		String REM_IDINSTITUCION = "REM." + EnvPlantillaRemitentesBean.C_IDINSTITUCION;
		String REM_IDTIPOENVIO = "REM." + EnvPlantillaRemitentesBean.C_IDTIPOENVIOS;
		String REM_IDPLANTILLAENVIOS = "REM." + EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS;
		String REM_IDPERSONA = "REM." + EnvPlantillaRemitentesBean.C_IDPERSONA;
		String REM_IDPAIS = "REM." + EnvPlantillaRemitentesBean.C_IDPAIS;
		String REM_IDPROVINCIA = "REM." + EnvPlantillaRemitentesBean.C_IDPROVINCIA;
		String REM_POBLACIONENTRANJERA = "REM." + EnvPlantillaRemitentesBean.C_POBLACIONEXTRANJERA;
		String REM_IDPOBLACION = "REM." + EnvPlantillaRemitentesBean.C_IDPOBLACION;		
		String REM_DOMICILIO = "REM." + EnvPlantillaRemitentesBean.C_DOMICILIO;		
		String REM_CODIGOPOSTAL = "REM." + EnvPlantillaRemitentesBean.C_CODIGOPOSTAL;
		String REM_FAX1 = "REM." + EnvPlantillaRemitentesBean.C_FAX1;
		String REM_FAX2 = "REM." + EnvPlantillaRemitentesBean.C_FAX2;		
		String REM_MOVIL = "REM." + EnvPlantillaRemitentesBean.C_MOVIL;
		String REM_CORREOELECTRONICO = "REM." + EnvPlantillaRemitentesBean.C_CORREOELECTRONICO;		
		String REM_DESCRIPCION = "REM." + EnvPlantillaRemitentesBean.C_DESCRIPCION;		
		
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
			
			String sql = " SELECT " + REM_IDINSTITUCION + ", " + REM_IDPERSONA + ", " + REM_POBLACIONENTRANJERA + ", " + REM_DOMICILIO + ", " + 
									 REM_CODIGOPOSTAL + ", "  + REM_FAX1 + ", " + REM_FAX2 + ", " + REM_CORREOELECTRONICO + ", " + REM_MOVIL + ", " +
									 REM_DESCRIPCION + ", " + PER_NOMBRE + ", " + PER_APELLIDOS1 + ", " + PER_APELLIDOS2 + ", " + 
									 PER_NIFCIF + ", " + COL_NCOLEGIADO + ", " + PA_NOMBRE + ", " + PR_NOMBRE + ", " + PO_NOMBRE +
									 
						   " FROM " + T_ENV_REMITENTES + ", " + 
						   			  T_CEN_COLEGIADO + ", " +
									  T_CEN_PERSONA + ", " +
									  T_CEN_PAIS + ", " +
									  T_CEN_PROVINCIA + ", " +
									  T_CEN_POBLACION +
		    
		    			   " WHERE " + REM_IDINSTITUCION + " = " + idInstitucion + 
						     " AND " + REM_IDTIPOENVIO + " = " + idTipoEnvios + 
							 " AND " + REM_IDPLANTILLAENVIOS + " = " + idPlantillaEnvios + 
							 " AND " + REM_IDPERSONA + " = " + idPersona + 
							 " AND " + REM_IDPERSONA + " = " + PER_IDPERSONA + 
							 " AND " + REM_IDINSTITUCION + " = " + COL_IDINSTITUCION +
							 " AND " + REM_IDPERSONA + " = " + COL_IDPERSONA +
							 " AND " + REM_IDPAIS + " = " + PA_IDPAIS +
							 " AND " + REM_IDPROVINCIA + " = " + PR_IDPROVINCIA + 
							 " AND " + REM_IDPOBLACION + " = " + PO_IDPOBLACION;

			ClsLogging.writeFileLog("EnvRemitentesAdm -> QUERY: " + sql,3);
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					datos.add (fila);					
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
	public boolean existeRemitente(String idTipoEnvios, String idPlantillaEnvios, String idInstitucion, String idPersona) throws ClsExceptions {
		try {
			Vector v = this.select(" WHERE " + EnvPlantillaRemitentesBean.C_IDINSTITUCION     + " = " + idInstitucion +
					   				 " AND " + EnvPlantillaRemitentesBean.C_IDTIPOENVIOS      + " = " + idTipoEnvios +
									 " AND " + EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS + " = " + idPlantillaEnvios +
			        				 " AND " + EnvPlantillaRemitentesBean.C_IDPERSONA         + " = " + idPersona);
		    
			if ((v != null) && (v.size()>0))
				return true;

			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
	
	/*
	public Vector getRemitentes(String idTipoEnvios, String idPlantillaEnvios, String idInstitucion)
	{
		String remitentes="", consulta="";
		Vector v = null;
		
		consulta = " SELECT persona."+CenPersonaBean.C_NOMBRE+"|| ' ' ||"+
							"persona."+CenPersonaBean.C_APELLIDOS1+"|| ' ' ||"+
							"persona."+CenPersonaBean.C_APELLIDOS2+" AS PERSONA"+
				   " FROM "+EnvPlantillaRemitentesBean.T_NOMBRETABLA+ " remit "+
				   ","+CenPersonaBean.T_NOMBRETABLA+ " persona "+
				   " WHERE remit."+EnvPlantillaRemitentesBean.C_IDINSTITUCION+" = "+idInstitucion+
				   " AND remit."+EnvPlantillaRemitentesBean.C_IDENVIO+" = "+idEnvio+
				   " AND remit."+EnvPlantillaRemitentesBean.C_IDPERSONA+" = persona."+CenPersonaBean.C_IDPERSONA;
		
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
	*/
}
