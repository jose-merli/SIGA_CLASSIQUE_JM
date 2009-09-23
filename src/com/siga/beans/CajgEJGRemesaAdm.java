/*
 * Created on 17/09/2008
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;
import com.siga.gratuita.util.PCAJGConstantes;

/**
 * @author fernando.gomez
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgEJGRemesaAdm extends MasterBeanAdministrador {
	
	public CajgEJGRemesaAdm (UsrBean usu) {
		super (CajgEJGRemesaBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgEJGRemesaBean.C_IDINSTITUCION, 		CajgEJGRemesaBean.C_ANIO,
							CajgEJGRemesaBean.C_NUMERO, 			CajgEJGRemesaBean.C_IDTIPOEJG,
							CajgEJGRemesaBean.C_IDINSTITUCIONREMESA,CajgEJGRemesaBean.C_IDREMESA,
							CajgEJGRemesaBean.C_FECHAMODIFICACION,	CajgEJGRemesaBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgEJGRemesaBean.C_IDINSTITUCION,
							CajgEJGRemesaBean.C_ANIO,
							CajgEJGRemesaBean.C_NUMERO,
							CajgEJGRemesaBean.C_IDTIPOEJG,	
							CajgEJGRemesaBean.C_IDINSTITUCIONREMESA,
							CajgEJGRemesaBean.C_IDREMESA};
		return campos;
	}


	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgEJGRemesaBean bean = null;
		try{
			bean = new CajgEJGRemesaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_NUMERO));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDTIPOEJG));
			bean.setIdInstitucionRemesa(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDINSTITUCIONREMESA));
			bean.setIdRemesa(UtilidadesHash.getInteger(hash, CajgEJGRemesaBean.C_IDREMESA));
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgRemesaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgEJGRemesaBean b = (CajgEJGRemesaBean) bean;
			
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDTIPOEJG, b.getIdTipoEJG());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, b.getIdInstitucionRemesa());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDREMESA, b.getIdRemesa());	
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	public Vector busquedaEJGRemesa(String idInstitucion, String idRemesa) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		String consulta="";
		try {
			consulta = "select ejg." + ScsEJGBean.C_ANIO + ", ejg." + ScsEJGBean.C_IDINSTITUCION + ", ejg." + ScsEJGBean.C_IDTIPOEJG + ",ejg." + ScsEJGBean.C_NUMERO + "" +
					   " from " + ScsEJGBean.T_NOMBRETABLA + " ejg, CAJG_EJGREMESA ejgremesa" ;
			/* realizamos la join con de las tablas que necesitamos */
			consulta += " where ejg.idinstitucion=ejgremesa.idinstitucion"+
					    " and ejg.anio=ejgremesa.anio"+
					    " and ejg.numero=ejgremesa.numero"+
					    " and ejg.idtipoejg=ejgremesa.idtipoejg"+
					    " and ejgremesa.idremesa="+idRemesa+""+
					    " and ejgremesa.idinstitucion="+idInstitucion+"";	
			
			
			//Consulta:
			datos = this.selectGenerico(consulta);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	
	
	
	
	public Vector busquedaEJGRemesaParaRemitidoComision(String idInstitucion, String idRemesa) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		String consulta="";
		try {
			consulta = "select ejgremesa.ANIO, ejgremesa.IDINSTITUCION, ejgremesa.IDTIPOEJG, ejgremesa.NUMERO" +
					" from CAJG_EJGREMESA ejgremesa, scs_estadoejg estado" +
					" where ejgremesa.idinstitucion = estado.idinstitucion" +
					" and ejgremesa.anio = estado.anio" +
					" and ejgremesa.numero = estado.numero" +
					" and ejgremesa.idtipoejg = estado.idtipoejg" +
					" and ejgremesa.idremesa = " + idRemesa +
					" and ejgremesa.idinstitucion = " + idInstitucion +
					" and estado.idestadoporejg = (select max(estadoEJG.idestadoporejg)" +
					" FROM scs_estadoejg estadoEJG where estado.idinstitucion = estadoEJG.idinstitucion" +
					" and estado.anio = estadoEJG.anio" +
					" and estado.numero = estadoEJG.numero" +
					" and estado.idtipoejg = estadoEJG.idtipoejg)" +
					" and estado.idestadoporejg <> " + ClsConstants.REMITIDO_COMISION;
			
			
			//Consulta:
			datos = this.selectGenerico(consulta);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}


	/**
	 * Obtiene los datos de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */	
	public Vector getDatosEJGs(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {				
		String consulta = "SELECT *" +
					" FROM V_PCAJG_EJG" +
					" WHERE IDREMESA = " + idRemesa +
					" AND IDINSTITUCION = " + idInstitucion;				
			
		return getDatos(consulta);
	}
	
	
	/**
	 * Obtiene las marcas expediente de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDatosMarcasExpediente(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {
				
		String consulta = "SELECT *" +
				" FROM V_PCAJG_MARCASEXPEDIENTES" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDREMESA = " + idRemesa;
		
		return getDatos(consulta);
	}


	/**
	 * Obtiene los datos de los familiares de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getFamiliares(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {				
		String sql = "SELECT *" +
				" FROM V_PCAJG_FAMILIARES" +
				" WHERE IDREMESA = " + idRemesa +
				" AND IDINSTITUCION = " + idInstitucion;

		return getDatos(sql);
	}


	/**
	 * Obtiene los datos de los contrarios de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getContrarios(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_CONTRARIOS" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		
		return getDatos(sql);
	}
	
	/**
	 * Obtiene los abogados designados de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getAbogadosDesignados(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
			" FROM V_PCAJG_ABOGADOSDESIGNADOS" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDREMESA = " + idRemesa;
		
		return getDatos(sql);
	}

	/**
	 * Obtiene los datos de identificacion del intercambio
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDatosIdentificacionIntercambio(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {
		String consulta = "SELECT *" +
			" FROM V_PCAJG_INTERCAMBIO" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDREMESA = " + idRemesa;

		return getDatos(consulta);
	}

	/**
	 * Obtiene los datos de documentación de los expedientes
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDocumentacionExpedienteDS(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_DOCUMENTACIONEXP_DS" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return getDatos(sql);
	}
	
	/**
	 * Obtiene los datos de documentación de los expedientes
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDocumentacionExpedienteF(String idInstitucion, String idRemesa) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_DOCUMENTACIONEXP_F" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return getDatos(sql);
	}
	
	public Vector getDatosPrestacionesResolucion(String idInstitucion, String idRemesa, String idTipoEJG, String anio, String numero) {
		Vector datos = new Vector();		
		return datos;
	}
	
	
	/**
	 * Metodo que comprueba si los cambos de la query estan recogidos en java, así
	 * si cambia una columna lanzará un error. Este método es solo de debug y luego puede
	 * comentarse su contenido si la versión está estable
	 * @param vector
	 * @throws SIGAException
	 */
	private void compruebaCampos(Vector vector) throws SIGAException {
		if (vector != null && vector.size() > 0) {
			if (vector.get(0) instanceof Hashtable) {
				 
				Hashtable ht = (Hashtable)vector.get(0);
				Enumeration enumeration = ht.keys();
				while (enumeration.hasMoreElements()) {
					String key = (String) enumeration.nextElement();
					try {
						PCAJGConstantes.class.getField(key);
					} catch (Exception e) {
						throw new SIGAException("No se encuentra la columna " + key);
					}					
				}
			}
		}
	}
	
	/**
	 * Obtiene un vector de hastables con los datos de la query que se pase por parametro
	 * @param sql
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Vector getDatos(String sql) throws ClsExceptions, SIGAException {
		Vector datos = this.selectGenerico(sql);
		//llamada al metodo compruebaCampos es solo para debug.
		compruebaCampos(datos);
		return datos;
	}
	
}