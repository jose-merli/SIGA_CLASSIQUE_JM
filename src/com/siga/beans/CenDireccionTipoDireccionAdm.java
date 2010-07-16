/*
 * Created on 19-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesHash;
import com.atos.utils.*;
import com.siga.general.*;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
// RGG cambio visibilidad public class CenDireccionTipoDireccionAdm extends MasterBeanAdministrador {
public class CenDireccionTipoDireccionAdm extends MasterBeanAdmVisible {

	/**
	 * @param usuario
	 */
	public CenDireccionTipoDireccionAdm(UsrBean usuario) {
		super(CenDireccionTipoDireccionBean.T_NOMBRETABLA, usuario);
	}


	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenDireccionTipoDireccionAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenDireccionTipoDireccionBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenDireccionTipoDireccionBean.C_FECHAMODIFICACION,
							CenDireccionTipoDireccionBean.C_IDDIRECCION,
							CenDireccionTipoDireccionBean.C_IDINSTITUCION,
							CenDireccionTipoDireccionBean.C_IDPERSONA,
							CenDireccionTipoDireccionBean.C_IDTIPODIRECCION,
							CenDireccionTipoDireccionBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	public String[] getClavesBean() {
		String[] claves = {	CenDireccionTipoDireccionBean.C_IDDIRECCION,
							CenDireccionTipoDireccionBean.C_IDINSTITUCION,
							CenDireccionTipoDireccionBean.C_IDPERSONA,
							CenDireccionTipoDireccionBean.C_IDTIPODIRECCION};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenDireccionTipoDireccionBean bean = null;
		try{
			bean = new CenDireccionTipoDireccionBean();
			bean.setIdDireccion(UtilidadesHash.getLong(hash,CenDireccionTipoDireccionBean.C_IDDIRECCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenDireccionTipoDireccionBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenDireccionTipoDireccionBean.C_IDPERSONA));
			bean.setIdTipoDireccion(UtilidadesHash.getInteger(hash,CenDireccionTipoDireccionBean.C_IDTIPODIRECCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDireccionTipoDireccionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDireccionTipoDireccionBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenDireccionTipoDireccionBean b = (CenDireccionTipoDireccionBean) bean;
			UtilidadesHash.set(hash, CenDireccionTipoDireccionBean.C_IDDIRECCION, b.getIdDireccion());
			UtilidadesHash.set(hash, CenDireccionTipoDireccionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenDireccionTipoDireccionBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenDireccionTipoDireccionBean.C_IDTIPODIRECCION, b.getIdTipoDireccion());
			UtilidadesHash.set(hash, CenDireccionTipoDireccionBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CenDireccionTipoDireccionBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * Borrar los datos de un C.V. y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param Hsah con las claves de los datos del tipo de direccion a borrar.
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico.
	 */
	public boolean deleteConHistorico (Hashtable clavesTipoDir, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			CenDireccionTipoDireccionBean beanTipoDir = (CenDireccionTipoDireccionBean) this.selectByPK(clavesTipoDir).get(0);
			if (delete(clavesTipoDir)) {
				// Si no hay mas registros en la tabla con esas claves debemos borrar el registro de CenDirecciones
				int numTipos = numTiposConLasMismasClaves(clavesTipoDir);
				if (numTipos < 0 ) {
					return false;
				}
				if (numTipos == 0 ) {
					CenDireccionesBean beanDir = new CenDireccionesBean ();
					beanDir.setIdInstitucion(UtilidadesHash.getInteger(clavesTipoDir, CenDireccionTipoDireccionBean.C_IDINSTITUCION));
					beanDir.setIdPersona(UtilidadesHash.getLong(clavesTipoDir, CenDireccionTipoDireccionBean.C_IDPERSONA));
					beanDir.setIdDireccion(UtilidadesHash.getLong(clavesTipoDir, CenDireccionTipoDireccionBean.C_IDDIRECCION));
					CenDireccionesAdm admDirecciones = new CenDireccionesAdm (this.usrbean);
					if(!admDirecciones.delete(beanDir)) {
						return false;
					}
				}
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanTipoDir, CenHistoricoAdm.ACCION_DELETE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	
	/**
	 * Busca el numero de tipos (registros) con las mismas claves que recibe en la tabla Cen_DireccionTipoDireccion 
	 * @author daniel.campos 22-01-05
	 * @version 1
	 * @param Hsah con las claves de los datos a buscar.
	 * @return el numero de registros
	 */
	public int numTiposConLasMismasClaves (Hashtable claves) throws ClsExceptions, SIGAException {
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT COUNT(*) AS NUMERO_REGISTROS"+ 
			  			 " FROM " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +
						 " WHERE " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +"." + CenDireccionTipoDireccionBean.C_IDPERSONA +"= " + UtilidadesHash.getLong(claves, CenDireccionTipoDireccionBean.C_IDPERSONA) +
						 " AND " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +"." + CenDireccionTipoDireccionBean.C_IDINSTITUCION +" = " + UtilidadesHash.getInteger(claves, CenDireccionTipoDireccionBean.C_IDINSTITUCION) + 
						 " AND " + CenDireccionTipoDireccionBean.T_NOMBRETABLA +"." + CenDireccionTipoDireccionBean.C_IDDIRECCION +" = " + UtilidadesHash.getLong(claves, CenDireccionTipoDireccionBean.C_IDDIRECCION);

			// RGG cambio visibilidad
			rc = this.findForUpdate(sql);
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer registros = UtilidadesHash.getInteger(prueba, "NUMERO_REGISTROS");
				if (registros != null) {
					return registros.intValue();
				}
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'select' para verificar si hay mas datos en B.D.");		
		}
		return -1;
	}
	
	public Hashtable selectDireccionGuardia( Integer idInstitucion, Long idPersona, Integer tipoDireccion) throws ClsExceptions, SIGAException {
		Hashtable registro = null;
		RowsContainer rc = null;
		String sql = null;

		try {
			sql="  SELECT "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_FECHAMODIFICACION+", "+
					CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDDIRECCION+", "+
					CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+", "+ 
					CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDPERSONA+", "+  
					CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+", "+
					CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_USUMODIFICACION+               
		       "  FROM "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+", "+CenDireccionesBean.T_NOMBRETABLA+
               "  WHERE "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+" = "+tipoDireccion+
               "  AND "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDPERSONA+" = "+idPersona+
               "  AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+"= "+idInstitucion+
               "  and "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDDIRECCION+"="+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDDIRECCION+
               "  and "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+"="+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDINSTITUCION+
               "  and "+CenDireccionTipoDireccionBean.T_NOMBRETABLA+"."+CenDireccionTipoDireccionBean.C_IDPERSONA+"="+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_IDPERSONA+
               "  and "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
               "  ORDER BY "+CenDireccionTipoDireccionBean.C_IDDIRECCION+", "+ CenDireccionTipoDireccionBean.C_IDINSTITUCION+", "+ 
                             CenDireccionTipoDireccionBean.C_IDPERSONA+", "+ CenDireccionTipoDireccionBean.C_IDTIPODIRECCION;		
			
		
			

			rc = new RowsContainer(); 
			//String sql = UtilidadesBDAdm.sqlSelect(this.getTablasDirecciones(), this.getCamposDirecciones());
			
			rc = this.find(sql);
			if (rc!=null) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					registro = (Hashtable)fila.getRow(); 					
				}
			}
			
		}
		catch(Exception e){
			throw new ClsExceptions (e, "Error en selectDirecciones");
		}
		return registro;
	}
}
