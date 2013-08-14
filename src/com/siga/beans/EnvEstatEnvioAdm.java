/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import iaik.security.cipher.IDEA;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class EnvEstatEnvioAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public EnvEstatEnvioAdm(UsrBean usu) {
		super(EnvEstatEnvioBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {EnvEstatEnvioBean.C_IDINSTITUCION,
				EnvEstatEnvioBean.C_IDENVIO,
				EnvEstatEnvioBean.C_IDTIPOENVIO,
				EnvEstatEnvioBean.C_IDPERSONA,
				EnvEstatEnvioBean.C_USUMODIFICACION,
				EnvEstatEnvioBean.C_FECHAMODIFICACION,
				EnvEstatEnvioBean.C_IDESTATENVIO};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {EnvEstatEnvioBean.C_IDINSTITUCION,EnvEstatEnvioBean.C_IDENVIO,EnvEstatEnvioBean.C_IDTIPOENVIO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] orden = {};
		return orden;
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		EnvEstatEnvioBean bean = null;
		
		try {
			bean = new EnvEstatEnvioBean();
			bean.setIdEstatEnvio(UtilidadesHash.getLong(hash,EnvEstatEnvioBean.C_IDESTATENVIO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,EnvEstatEnvioBean.C_IDINSTITUCION));			
			bean.setIdEnvio(UtilidadesHash.getInteger(hash,EnvEstatEnvioBean.C_IDENVIO));
			bean.setIdTipoEnvio(UtilidadesHash.getInteger(hash,EnvEstatEnvioBean.C_IDTIPOENVIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash,EnvEstatEnvioBean.C_IDPERSONA));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,EnvEstatEnvioBean.C_USUMODIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,EnvEstatEnvioBean.C_FECHAMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			EnvEstatEnvioBean b = (EnvEstatEnvioBean) bean;
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_IDESTATENVIO,b.getIdEstatEnvio());
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_IDINSTITUCION,b.getIdInstitucion());
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_IDTIPOENVIO,b.getIdTipoEnvio());
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_IDPERSONA,b.getIdPersona());
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(htData,EnvEstatEnvioBean.C_FECHAMODIFICACION,b.getFechaMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

    public Vector getDatosInforme(DefinirEnviosForm form)
	throws ClsExceptions{

        Vector salida = new Vector();
        Hashtable codigos = new Hashtable();
        int contador = 0;
        
        try {
	        String fechaDesde = form.getFechaDesde();
	        String fechaHasta = form.getFechaHasta();
	        String idEstado = form.getIdEstado();
	        String nombre = form.getNombre();
	        String idTipoEnvio = form.getIdTipoEnvio();        
	        String idEnvio = form.getIdEnvioBuscar();        
	        String idInstitucion = this.usrbean.getLocation();
	        String tipoFecha = form.getTipoFecha();
	        
	        // Formateo de fechas
			String dFechaDesde = null, dFechaHasta = null;
			if (fechaDesde!=null && !fechaDesde.trim().equals("")) dFechaDesde = GstDate.getApplicationFormatDate("",fechaDesde);
			if (fechaHasta!=null && !fechaHasta.trim().equals("")) dFechaHasta = GstDate.getApplicationFormatDate("",fechaHasta);
			
			String fecha,fechaIsNull;
			if (tipoFecha.equalsIgnoreCase(EnvEnviosAdm.FECHA_CREACION)){
			    fecha = "env.fecha";
			    fechaIsNull = "";
			} else {
			    fecha = "env.fechaprogramada";
			    fechaIsNull = " AND env.fechaprogramada is not null";
			}			
		
			String sql="select * from ((select e.idinstitucion, e.idpersona,p.nombre,p.apellidos1,p.apellidos2  , "+
			" (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 1) as NUM_ELECTRONICO, " +
		    " (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 2) as NUM_ORDINARIO, " +
			" (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 3) as NUM_FAX, "+
			" (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 4) as NUM_SMS, "+
			" (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 5) as NUM_BUROSMS "+
			" from env_estat_envio e, cen_persona p,env_envios env, env_destinatarios envdest "+
			" where e.idpersona = p.idpersona "+			
            " and e.idinstitucion = env.idinstitucion "+
            " and e.idenvio = env.idenvio "+
            " and env.idinstitucion = "+idInstitucion;
                     
			// condiciones de filtro del form
	    	contador++;
	    //	codigos.put(new Integer(contador),idInstitucion);
	    	//sql += " and env.idinstitucion=:"+contador;
	    
	    	sql += (nombre!=null && !nombre.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"env.descripcion" ) : "";
			if (dFechaDesde!=null || dFechaHasta!=null) {
			    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(fecha,dFechaDesde,dFechaHasta);
			}
			sql += (idEstado!=null && !idEstado.equals("")) ? " AND env.idestado = "+ idEstado : "";
			
			sql += (idTipoEnvio!=null && !idTipoEnvio.equals("")) ? " AND env.idtipoenvios = "+ idTipoEnvio : "";
			
			sql += (idEnvio!=null && !idEnvio.equals("")) ? " AND env.idenvio = "+ idEnvio : "";

			sql += fechaIsNull;	  
			
			sql += " and envdest.idinstitucion=env.idinstitucion "+
			       " and envdest.idenvio=env.idenvio "+
			       " and envdest.tipodestinatario="+ "'"+EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA +"'"+
			       " group by e.idinstitucion, "+
                   " e.idpersona, "+
                   " p.nombre, "+
                   " p.apellidos1, "+
                   " p.apellidos2) "+  
			       " UNION "+
			       " (select e.idinstitucion,e.idpersona,p.nombre,p.apellido1 as apellidos1, p.apellido2 as apellidos2,"+
			       " (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 1) as NUM_ELECTRONICO, "+
			       " (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 2) as NUM_ORDINARIO, "+			       
			       " (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 3) as NUM_FAX, "+ 
			       " (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 4) as NUM_SMS, "+
			       " (select count(*) from env_estat_envio s where s.idinstitucion = e.idinstitucion and s.idpersona = e.idpersona and s.idtipoenvios = 5) as NUM_BUROSMS "+
			       " from env_estat_envio e, scs_personajg p,env_envios env, env_destinatarios envdest "+
			       " where e.idpersona = p.idpersona "+         
                   " and e.idinstitucion=p.idinstitucion "+
                   " and e.idinstitucion = env.idinstitucion "+
                   " and e.idenvio = env.idenvio "+
                   " and env.idinstitucion = "+idInstitucion;
			
            //       codigos.put(new Integer(contador),idInstitucion);
	    	//sql += " and env.idinstitucion=:"+contador;
	    
	    	sql += (nombre!=null && !nombre.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"env.descripcion" ) : "";
			if (dFechaDesde!=null || dFechaHasta!=null) {
			    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(fecha,dFechaDesde,dFechaHasta);
			}
			sql += (idEstado!=null && !idEstado.equals("")) ? " AND env.idestado = "+ idEstado : "";
			
			sql += (idTipoEnvio!=null && !idTipoEnvio.equals("")) ? " AND env.idtipoenvios = "+ idTipoEnvio : "";
			
			sql += (idEnvio!=null && !idEnvio.equals("")) ? " AND env.idenvio = "+ idEnvio : "";

			sql += fechaIsNull;	  
			
			sql += " and envdest.idinstitucion=env.idinstitucion "+
                   " and envdest.idenvio=env.idenvio "+
                   " and envdest.tipodestinatario="+ "'" +EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG+ "'" + 
                   " group by e.idinstitucion, "+
                   " e.idpersona, "+
                   " p.nombre,"+
                   " p.apellido1,"+
                   " p.apellido2)) "+       
                   " order by  apellidos1, apellidos2, nombre";
			       
	        //sql +=" group by  e.idinstitucion, e.idpersona, p.nombre, p.apellidos1, p.apellidos2 " +
	        //" order by  p.apellidos1, p.apellidos2, p.nombre";
	
	        salida.add(sql);
	        salida.add(codigos);
	        
//	        Vector vCampo = this.selectBind(sql,codigos);
//	        if (vCampo!=null && vCampo.size()>0){
//	            salida = vCampo;
//	        }
	    } catch (ClsExceptions e) {
	        throw new ClsExceptions(e,"Error al obtener datos de informe de envíos.");
	    }
	    return salida;
	}

	public void insertarApunte(Integer idInstitucion, Integer idEnvio, Integer idTipoEnvio, Long idPersona)
	{
		insertarApunteExtra(idInstitucion, idEnvio, idTipoEnvio, idPersona, null);
	}

	public void insertarApunteExtra(Integer idInstitucion,
			Integer idEnvio,
			Integer idTipoEnvio,
			Long idPersona,
			String extra)
	{
		// escribiendo log de envio
		try {
			String pathDoc = new EnvDocumentosDestinatariosAdm(this.usrbean).getPathDocumentosFromDB();
			
			File auxDirectorios = new File(pathDoc + File.separator + idInstitucion.toString());
			auxDirectorios.mkdirs();
			
			String sFicheroLog = pathDoc + File.separator + idInstitucion.toString() + File.separator
					+ "logEnvios_" + idInstitucion.toString() + ".log.xls";
			SIGALogging logEnvio = new SIGALogging(sFicheroLog);
			logEnvio.write(idInstitucion.toString() + ClsConstants.SEPARADOR + idEnvio.toString()
					+ ClsConstants.SEPARADOR + idTipoEnvio.toString() + ClsConstants.SEPARADOR
					+ idPersona.toString() + ((extra == null) ? "" : ClsConstants.SEPARADOR + extra));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		// insertando apunte estadistico de envio
		try {
			EnvEstatEnvioBean bean = new EnvEstatEnvioBean();
			Long idEstatEnvio = getSecuenciaNextVal(EnvEstatEnvioBean.SEQ_ENV_ESTAT_ENVIO);
			bean.setIdEstatEnvio(idEstatEnvio);
			bean.setIdInstitucion(idInstitucion);
			bean.setIdEnvio(idEnvio);
			bean.setIdTipoEnvio(idTipoEnvio);
			bean.setIdPersona(idPersona);
			
			if (!this.insert(bean)) {
				throw new ClsExceptions("Error al insertar apunte estadístico de envíos: " + this.getError());
			}
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogWithoutSession(e.getMessage(), 10);
		}
	} // insertarApunteExtra ()

	public void borrarEnvio(String idInstitucion, String idEnvio, String idTipoEnvio) throws SIGAException, ClsExceptions {

		Hashtable htEnvio = new Hashtable();
		htEnvio.put(EnvEstatEnvioBean.C_IDINSTITUCION, idInstitucion);
		htEnvio.put(EnvEstatEnvioBean.C_IDENVIO, idEnvio);
		htEnvio.put(EnvEstatEnvioBean.C_IDTIPOENVIO, idTipoEnvio);

		try {
			this.delete(htEnvio);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e);
		}
	}

}
