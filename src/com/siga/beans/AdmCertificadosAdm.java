package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.EjecucionPLs;

public class AdmCertificadosAdm extends MasterBeanAdministrador
{
	public AdmCertificadosAdm(UsrBean usuario)
	{
	    super(AdmCertificadosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmCertificadosBean.C_IDUSUARIO,
						   AdmCertificadosBean.C_IDINSTITUCION,
		        		   AdmCertificadosBean.C_NUMSERIE,
		        		   AdmCertificadosBean.C_REVOCACION,
		        		   AdmCertificadosBean.C_FECHACAD,
		        		   AdmCertificadosBean.C_NIF,
		        		   AdmCertificadosBean.C_ROL,
						   AdmCertificadosBean.C_FECHAMODIFICACION, 
						   AdmCertificadosBean.C_USUMODIFICACION,
						   AdmCertificadosBean.F_ROLES};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmCertificadosBean.C_IDUSUARIO, AdmCertificadosBean.C_NUMSERIE, AdmCertificadosBean.C_IDINSTITUCION};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmCertificadosBean.C_IDUSUARIO,
						   AdmCertificadosBean.C_IDINSTITUCION,
     		   			   AdmCertificadosBean.C_NUMSERIE,
     		   			   AdmCertificadosBean.C_REVOCACION,
     		   			   AdmCertificadosBean.C_FECHACAD,
     		   			   AdmCertificadosBean.C_NIF,
     		   			   AdmCertificadosBean.C_ROL,
     		   			   AdmCertificadosBean.C_FECHAMODIFICACION, 
     		   			   AdmCertificadosBean.C_USUMODIFICACION};
		
		return campos;

    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    AdmCertificadosBean bean = null;

		try
		{
			bean = new AdmCertificadosBean();
			
			bean.setIdUsuario(UtilidadesHash.getInteger(hash, AdmCertificadosBean.C_IDUSUARIO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmCertificadosBean.C_IDINSTITUCION));
			bean.setNumSerie(UtilidadesHash.getString(hash, AdmCertificadosBean.C_NUMSERIE));
			bean.setRevocacion(UtilidadesHash.getString(hash, AdmCertificadosBean.C_REVOCACION));
			bean.setFechaCad(UtilidadesHash.getString(hash, AdmCertificadosBean.C_FECHACAD));
			bean.setNIF(UtilidadesHash.getString(hash, AdmCertificadosBean.C_NIF));
			bean.setRol(UtilidadesHash.getString(hash, AdmCertificadosBean.C_ROL));
			bean.setRoles(UtilidadesHash.getString(hash, AdmCertificadosBean.C_IDS_ROLES));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmCertificadosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmCertificadosBean.C_USUMODIFICACION));
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

			AdmCertificadosBean b = (AdmCertificadosBean) bean;

			UtilidadesHash.set(htData, AdmCertificadosBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_NUMSERIE, b.getNumSerie());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_REVOCACION, b.getRevocacion());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_FECHACAD, b.getFechaCad());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_NIF, b.getNIF());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_ROL, b.getRol());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_IDS_ROLES, b.getRoles());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmCertificadosBean.C_USUMODIFICACION, b.getUsuMod());
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
        String[] ordenCampos = {AdmCertificadosBean.C_NUMSERIE};
        
        return ordenCampos;
    }
    
	public Paginador selectConDescripcionUsuario(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();
			String funcionRolesParseada = "";
			
            String cadena = AdmCertificadosBean.F_ROLES.substring(AdmCertificadosBean.F_ROLES.indexOf("(")+1, AdmCertificadosBean.F_ROLES.indexOf(")")); 
            StringTokenizer st = new StringTokenizer(cadena, ",");
            String tokens = "";
             
            while (st.hasMoreTokens())
            {
                String token = st.nextToken();
                token = ", C." + token.trim();
                tokens += token;
            }
            
            tokens = tokens.substring(1);
            
            funcionRolesParseada += AdmCertificadosBean.F_ROLES.substring(0, AdmCertificadosBean.F_ROLES.indexOf("(")+1);
            funcionRolesParseada += tokens;
            funcionRolesParseada += AdmCertificadosBean.F_ROLES.substring(AdmCertificadosBean.F_ROLES.indexOf(")"));

			String sql = "SELECT " + "C." + AdmCertificadosBean.C_IDUSUARIO + ", " +
									 "C." + AdmCertificadosBean.C_IDINSTITUCION + ", " +
									 "U." + AdmUsuariosBean.C_DESCRIPCION + ", " +
									 "C." + AdmCertificadosBean.C_NUMSERIE + ", " +
									 "C." + AdmCertificadosBean.C_FECHACAD + ", " +
									 "C." + AdmCertificadosBean.C_NIF + ", " +
									 "C." + AdmCertificadosBean.C_ROL + ", " +
									 "C." + AdmCertificadosBean.C_REVOCACION + ", " +
									        funcionRolesParseada + ", " +
									 "C." + AdmCertificadosBean.C_FECHAMODIFICACION + ", " +
									 "C." + AdmCertificadosBean.C_USUMODIFICACION + " " +
			             " FROM " + AdmCertificadosBean.T_NOMBRETABLA + " C, " +
			                        AdmUsuariosBean.T_NOMBRETABLA + " U " +
			             " WHERE " + where;

			Paginador paginador = new Paginador(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
	}

	public Vector getCertificadosUsuario(String NIF) throws ClsExceptions 
	{
		return select(" WHERE " + AdmCertificadosBean.C_NIF + " = '" + NIF + "'");
	}
	
	public void revocarCertificados(Integer idInstitucion, String nif) throws ClsExceptions
	{
		String resultado[] = EjecucionPLs.ejecutarPL_RevocarCertificados(idInstitucion, nif);
		if (! resultado[0].equalsIgnoreCase("0"))
			throw new ClsExceptions("Error al ejecutar el PL de Revocacion de Certificados");
	}
}