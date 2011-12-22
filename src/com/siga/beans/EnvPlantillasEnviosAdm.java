package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

public class EnvPlantillasEnviosAdm extends MasterBeanAdministrador
{
	public EnvPlantillasEnviosAdm(UsrBean usuario)
	{
	    super(EnvPlantillasEnviosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvPlantillasEnviosBean.C_IDINSTITUCION,
		        		   EnvPlantillasEnviosBean.C_IDTIPOENVIOS,
		        		   EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS,
		        		   EnvPlantillasEnviosBean.C_NOMBRE,
		        		   EnvPlantillasEnviosBean.C_ACUSERECIBO,
		        		   EnvPlantillasEnviosBean.C_FECHAMODIFICACION, 
						   EnvPlantillasEnviosBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvPlantillasEnviosBean.C_IDINSTITUCION, EnvPlantillasEnviosBean.C_IDTIPOENVIOS, EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvPlantillasEnviosBean bean = null;

		try
		{
			bean = new EnvPlantillasEnviosBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvPlantillasEnviosBean.C_IDINSTITUCION));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvPlantillasEnviosBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS));
			bean.setNombre(UtilidadesHash.getString(hash, EnvPlantillasEnviosBean.C_NOMBRE));
			bean.setAcuseRecibo(UtilidadesHash.getString(hash, EnvPlantillasEnviosBean.C_ACUSERECIBO));
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

			EnvPlantillasEnviosBean b = (EnvPlantillasEnviosBean) bean;

			UtilidadesHash.set(htData, EnvPlantillasEnviosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvPlantillasEnviosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvPlantillasEnviosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvPlantillasEnviosBean.C_ACUSERECIBO, b.getAcuseRecibo());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return null;
    }

    public Vector buscarPlantillas(String idInstitucion, 
            					   String idTipoEnvios, 
            					   String nombre)
    {
        Vector vDatos = null;
        
        try
        {
            RowsContainer rc = new RowsContainer();
            int contador=0;
            Hashtable codigos = new Hashtable();
            
            contador++;
            codigos.put(new Integer(contador),this.usrbean.getLanguage());
            contador++;
            codigos.put(new Integer(contador),idInstitucion);
            
            String sSQL = " SELECT P." + EnvPlantillasEnviosBean.C_IDINSTITUCION + ", " +
            					  "P." + EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS + ", " +
            					  "P." + EnvPlantillasEnviosBean.C_NOMBRE + " AS NOMBREPLANTILLA, " +
            					  "P." + EnvPlantillasEnviosBean.C_ACUSERECIBO + " , " +
                                  "T." + EnvTipoEnviosBean.C_IDTIPOENVIOS + ", " +
                                  " (select DESCRIPCION from GEN_RECURSOS_CATALOGOS WHERE IDRECURSO = T.NOMBRE AND IDLENGUAJE =:1) AS NOMBRETIPO " +
                                  // RGG 05/06/2009: SE QUITA LA FUNCION Y SE PONE LA SUBSELECT QUE EN ESTE CASO DA MEJOR RENDIMIENTO 
                                  // UtilidadesMultidioma.getCampoMultidiomaSimple(  "T." + EnvTipoEnviosBean.C_NOMBRE, this.usrbean.getLanguage()) + " AS NOMBRETIPO " +
                          " FROM " + EnvPlantillasEnviosBean.T_NOMBRETABLA + " P, " +
                                     EnvTipoEnviosBean.T_NOMBRETABLA + " T " +
                          " WHERE P." + EnvPlantillasEnviosBean.C_IDINSTITUCION + "=:2 AND " +
                          		 "P." + EnvPlantillasEnviosBean.C_IDTIPOENVIOS + "=T." + EnvTipoEnviosBean.C_IDTIPOENVIOS + " ";
            
            if (idTipoEnvios!=null && !idTipoEnvios.equals("")) {
                contador++;
                codigos.put(new Integer(contador),idTipoEnvios);
                sSQL += " AND P." + EnvPlantillasEnviosBean.C_IDTIPOENVIOS + "=:" + contador;
            }
            if (nombre!=null && !nombre.equals("")) {
                contador++;
                codigos.put(new Integer(contador),nombre.trim());
                sSQL += " AND "+ComodinBusquedas.prepararSentenciaCompletaBind(":"+contador,"P." + EnvPlantillasEnviosBean.C_NOMBRE );
            }

            ClsLogging.writeFileLogWithoutSession("SQL Envíos a Grupos / Definir plantillas: " + sSQL, 3);
            rc.findNLSBind(sSQL,codigos);
            
            vDatos = rc.getAll();
        }
        
        catch(Exception e)
        {
            vDatos = null;
        }
        
        return vDatos;
    }
    
    public boolean borrarPlantilla(String idInstitucion, String idPlantillaEnvios, String idTipoEnvios) throws ClsExceptions
    {
	    Hashtable hash = new Hashtable();
	    
	    hash.put(EnvPlantillasEnviosBean.C_IDINSTITUCION, idInstitucion);
	    hash.put(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS, idPlantillaEnvios);
	    hash.put(EnvPlantillasEnviosBean.C_IDTIPOENVIOS, idTipoEnvios);

	    if (delete(hash))
	    {
	        return true;
	    }
	    
	    else
	    {
	        return false;
	    }
    }
    
    public boolean insertarPlantilla(String idInstitucion, String descPlantilla, String idTipoEnvios) throws ClsExceptions
    {
	    Hashtable hash = new Hashtable();
	    
	    hash.put(EnvPlantillasEnviosBean.C_IDINSTITUCION, idInstitucion);
	    hash.put(EnvPlantillasEnviosBean.C_IDTIPOENVIOS, idTipoEnvios);
	    
	    String nuevoID = getNuevoID(hash);
	    
	    hash.put(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS, nuevoID);
	    
	    hash.put(EnvPlantillasEnviosBean.C_NOMBRE, descPlantilla);

	    if (insert(hash))
	    {
	        if (!idTipoEnvios.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))
	        {
	            return true;
	        }
	        
	        else
	        {
	            hash = new Hashtable();
	            
	            hash.put(EnvCamposPlantillaBean.C_IDINSTITUCION, idInstitucion);
	            hash.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS, idTipoEnvios);
	            hash.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, nuevoID);
	            hash.put(EnvCamposPlantillaBean.C_IDCAMPO, EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO);
	            hash.put(EnvCamposPlantillaBean.C_TIPOCAMPO, EnvCamposAdm.K_TIPOCAMPO_E);
	            hash.put(EnvCamposPlantillaBean.C_IDFORMATO, "");
	            hash.put(EnvCamposPlantillaBean.C_VALOR, "");
	            
	            EnvCamposPlantillaAdm admCampo = new EnvCamposPlantillaAdm(this.usrbean);
	            
	            if (admCampo.insert(hash))
	            {
	                hash.put(EnvCamposPlantillaBean.C_IDCAMPO, EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO);
	                
	                return admCampo.insert(hash);
	            }
	            
	            else
	            {
	                return false;
	            }
	        }
	    }
	    
	    else
	    {
	        return false;
	    }
    }
    
   	public String getNuevoID(Hashtable entrada) throws ClsExceptions 
	{
		String sID="0";
		
		try
		{
			RowsContainer rc = new RowsContainer();

			String sSQL="";
			
			sSQL = " SELECT MAX(" + EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS + ") AS " + EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS + 
				   " FROM " + nombreTabla +
				   " WHERE " + EnvPlantillasEnviosBean.C_IDINSTITUCION + "=" + entrada.get(EnvPlantillasEnviosBean.C_IDINSTITUCION) + " AND " +
				   			   EnvPlantillasEnviosBean.C_IDTIPOENVIOS + "=" + entrada.get(EnvPlantillasEnviosBean.C_IDTIPOENVIOS);
		
			if (rc.findForUpdate(sSQL))
			{
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				
				String sIDAux = (String)ht.get(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS);
				
				if (sIDAux!=null && !sIDAux.equals(""))
				{
					sID = "" + (Integer.parseInt(sIDAux)+1);
				}
			}
			
			return sID;
		}	

		catch (ClsExceptions e)
		{
			throw new ClsExceptions (e, "Error al obtener el MAX(IDPLANTILLA).");
		}
	}
   	
	/**
	 * Obtiene identificadores de plantillas validos para una institucion y un tipo de envio concreto
	 * @param institucion identificador de la institucion
	 * @param tipoEnvio identificador del tipo de envio
	 * @return Vector lista de plantillas posibles
	 * @throws ClsExceptions en caso de error
	 */
	public Vector getIdPlantillasValidos(String institucion, String tipoEnvio)  throws ClsExceptions {
		
	    Vector datos = new Vector();
	    
	    try {
		    
			String consulta = "SELECT " + 
							EnvPlantillasEnviosBean.T_NOMBRETABLA + "." + EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS + 
							" FROM " + 
							EnvPlantillasEnviosBean.T_NOMBRETABLA +
							" WHERE " + EnvPlantillasEnviosBean.T_NOMBRETABLA + "." + EnvPlantillasEnviosBean.C_IDINSTITUCION + " = " + institucion +
							" AND " + EnvPlantillasEnviosBean.T_NOMBRETABLA + "." + EnvPlantillasEnviosBean.C_IDTIPOENVIOS + " = " + tipoEnvio;
										
			RowsContainer rc = new RowsContainer();			
		
			ClsLogging.writeFileLog("EnvPlantillasEnvioAdm -> QUERY: " + consulta,3);
        
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add((String)fila.getRow().get(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS));					
				}
			}
		} 
		
	    catch (Exception e) {
	    	throw new ClsExceptions (e, "Error en la obtencion de plantillas de envio: "+e.getLocalizedMessage());	
	    }
	    
	    return datos;
	}
	
	
    public Vector getRemitentes (String idInstitucion, String idTipoEnvios, String idPlantillaEnvios) throws ClsExceptions
	{

    Vector datos = new Vector();

	//NOMBRES TABLAS PARA LA JOIN
	String T_ENV_REMITENTES = EnvPlantillaRemitentesBean.T_NOMBRETABLA + " R";
	String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " P";
	String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " C";

	//Tabla env_Remitentes
	String R_IDINSTITUCION     = "R." + EnvPlantillaRemitentesBean.C_IDINSTITUCION;
	String R_IDTIPOENVIOS      = "R." + EnvPlantillaRemitentesBean.C_IDTIPOENVIOS;
	String R_IDPLANTILLAENVIOS = "R." + EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS;
	String R_IDPERSONA         = "R." + EnvPlantillaRemitentesBean.C_IDPERSONA;
	String R_DESCRIPCION       = "R." + EnvPlantillaRemitentesBean.C_DESCRIPCION;

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

		String sql = "SELECT (";

	    sql += P_NOMBRE + "||' '|| " + P_APELLIDO1 + "||' '|| " + P_APELLIDO2 + ") AS NOMBREYAPELLIDOS, ";
	    sql += R_DESCRIPCION + ", ";
	    sql += C_NCOLEGIADO + ", ";
	    sql += P_NIFCIF + ", ";
	    sql += P_IDPERSONA;

		sql += " FROM ";
	    sql += T_ENV_REMITENTES + ", " +
	    	   T_CEN_PERSONA + ", " +
	    	   T_CEN_COLEGIADO;

	    sql += " WHERE ";
	    sql += R_IDINSTITUCION + " = " + idInstitucion;
	    sql += " AND " + R_IDTIPOENVIOS + " = " + idTipoEnvios;
	    sql += " AND " + R_IDPLANTILLAENVIOS + " = " + idPlantillaEnvios;
		sql += " AND " + R_IDPERSONA + " = " + P_IDPERSONA;
		sql += " AND " + R_IDINSTITUCION + " = " + C_IDINSTITUCION + "(+)";
		sql += " AND " + R_IDPERSONA + " = " + C_IDPERSONA + "(+)";

		sql += " ORDER BY NOMBREYAPELLIDOS";

		ClsLogging.writeFileLog("EnvEnviosAdm.getRemitentes -> QUERY: "+sql,3);

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


	
   	
}