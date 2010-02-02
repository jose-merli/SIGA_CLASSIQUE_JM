package com.siga.beans.eejg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.CajgRemesaBean;
import com.siga.beans.CajgRemesaBean;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
/**
 * 
 * @author jorgeta
 *
 */

public class ScsEejgPeticionesAdm extends MasterBeanAdministrador {
  

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsEejgPeticionesAdm (UsrBean usuario) {
		super( ScsEejgPeticionesBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {
				ScsEejgPeticionesBean.C_IDPETICION,
				ScsEejgPeticionesBean.C_IDUSUARIOPETICION,
				ScsEejgPeticionesBean.C_FECHAPETICION,
				ScsEejgPeticionesBean.C_ESTADO,
				ScsEejgPeticionesBean.C_IDSOLICITUD,
				ScsEejgPeticionesBean.C_IDINSTITUCION,
				ScsEejgPeticionesBean.C_IDTIPOEJG,
				ScsEejgPeticionesBean.C_ANIO,
				ScsEejgPeticionesBean.C_NUMERO,
				ScsEejgPeticionesBean.C_IDPERSONA,
				ScsEejgPeticionesBean.C_NUMEROINTENTOSSOLICITUD,
				ScsEejgPeticionesBean.C_NUMEROINTENTOSCONSULTA,
				ScsEejgPeticionesBean.C_IDXML,
				ScsEejgPeticionesBean.C_USUMODIFICACION,
				ScsEejgPeticionesBean.C_FECHAMODIFICACION
				};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsEejgPeticionesBean.C_IDPETICION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsEejgPeticionesBean bean = null;
//		ScsUnidadFamiliarEJGBean unidadFamiliar = null;
//		ScsPersonaJGBean personaJG;
		try{
			bean = new ScsEejgPeticionesBean();
//			unidadFamiliar =  new ScsUnidadFamiliarEJGBean();
//			personaJG = new ScsPersonaJGBean();
//			unidadFamiliar.setPersonaJG(personaJG);
//			bean.setUnidadFamiliar(unidadFamiliar);
			
			bean.setIdPeticion(UtilidadesHash.getLong(hash,ScsEejgPeticionesBean.C_IDPETICION));
			bean.setIdUsuarioPeticion(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_IDUSUARIOPETICION));			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_IDINSTITUCION));
//			unidadFamiliar.setIdInstitucion(valor);
//			unidadFamiliar.setAnio(valor);
//			unidadFamiliar.setNumero(valor);
//			unidadFamiliar.setIdPersona(valor);
		
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_ANIO));
			bean.setEstado(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_ESTADO));
			bean.setFechaPeticion(UtilidadesHash.getString(hash,ScsEejgPeticionesBean.C_FECHAPETICION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,ScsEejgPeticionesBean.C_IDPERSONA));
			bean.setIdSolicitud(UtilidadesHash.getString(hash,ScsEejgPeticionesBean.C_IDSOLICITUD));
			bean.setIdTipoEjg(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_IDTIPOEJG));
			bean.setIdXml(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_IDXML));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_NUMERO));
			bean.setNumeroIntentosConsulta(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_NUMEROINTENTOSCONSULTA));
			bean.setNumeroIntentosSolicitud(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_NUMEROINTENTOSSOLICITUD));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsEejgPeticionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_USUMODIFICACION));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable<String, Object> beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable<String, Object> htData = null;
		try{
			htData = new Hashtable<String, Object>();
			ScsEejgPeticionesBean b = (ScsEejgPeticionesBean) bean;
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_IDPETICION, b.getIdPeticion());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_ESTADO, b.getEstado());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_FECHAPETICION, b.getFechaPeticion());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_IDTIPOEJG, b.getIdTipoEjg());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_IDXML, b.getIdXml());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_NUMEROINTENTOSCONSULTA, b.getNumeroIntentosConsulta());
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_NUMEROINTENTOSSOLICITUD, b.getNumeroIntentosSolicitud());
			
			
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			UtilidadesHash.set(htData,ScsEejgPeticionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			
		}
		catch (Exception e){
			htData = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return htData;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	public String[] getOrdenCampos() {
		String[] vector = {ScsEejgPeticionesBean.C_IDPETICION};
		return vector;
	}
	
	public void insertarPeticionEejg(ScsEejgPeticionesBean peticionEejg) throws ClsExceptions, IllegalStateException, SecurityException, SystemException{
	
		UserTransaction tx = null;
		try {
			tx = this.usrbean.getTransaction();
			tx.begin();
			
			Long idPeticion = getNuevoPeticionEejg(peticionEejg.getIdInstitucion());
			peticionEejg.setIdPeticion(idPeticion);
			peticionEejg.setIdUsuarioPeticion(Integer.parseInt(this.usrbean.getUserName()));
			peticionEejg.setFechaPeticion("sysdate");
			peticionEejg.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL);
			insert(peticionEejg);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.insertarPeticionEejg");
		} 
		
	}

	

	public Long getNuevoPeticionEejg(Integer idInstitucion) throws ClsExceptions {
		String select = null;
		Long nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsEejgPeticionesBean.C_IDPETICION+")+1 AS ID FROM "+ScsEejgPeticionesBean.T_NOMBRETABLA;
					  
			
			Vector<Hashtable<String, Object>> datos = this.selectGenerico(select);
			String id = (String)((Hashtable<String , Object>)datos.get(0)).get("ID");
			
			if ( (datos == null) || (id!= null && id.equals("")) )
				nuevoId = new Long("0");
			else
				nuevoId = new Long(id);

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}

	public List<ScsEejgPeticionesBean> getPeticionesEejg(ScsEejgPeticionesBean peticionEejg)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
				
		sql.append(" SELECT PET.IDPETICION,  PET.IDUSUARIOPETICION,	PET.FECHAPETICION , ");
		sql.append(" PET.ESTADO,  PET.IDSOLICITUD, PET.IDINSTITUCION , PET.IDTIPOEJG, ");
		sql.append(" PET.ANIO,PET.NUMERO, PET.IDPERSONA, PET.NUMEROINTENTOSSOLICITUD, ");
		sql.append(" PET.NUMEROINTENTOSCONSULTA, PET.IDXML, PET.FECHAMODIFICACION,  PET.USUMODIFICACION, ");
		sql.append(" XML.ESTADO XML_ESTADO, XML.IDXML XML_IDXML, XML.ENVÍORESPUESTA XML_ENVÍORESPUESTA, ");
		sql.append(" XML.XML XML_XML ");
		sql.append(" FROM SCS_EEJG_PETICIONES PET, SCS_EEJG_XML XML ");
		sql.append(" WHERE PET.IDPETICION = XML.IDPETICION ");
		if(peticionEejg!=null){
			if(peticionEejg.getEstado()!=null){
				sql.append(" AND PET.ESTADO = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),peticionEejg.getEstado());
			}
			if(peticionEejg.getUnidadFamiliar()!=null){
				sql.append(" AND PET.IDINSTITUCION =:");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),peticionEejg.getUnidadFamiliar().getIdInstitucion());
				sql.append(" AND PET.IDTIPOEJG=:");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),peticionEejg.getUnidadFamiliar().getIdTipoEJG());
				sql.append(" AND PET.ANIO = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),peticionEejg.getUnidadFamiliar().getAnio());
				sql.append(" AND PET.NUMERO =:");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),peticionEejg.getUnidadFamiliar().getNumero());
				if(peticionEejg.getUnidadFamiliar().getPersonaJG()!=null){
					sql.append(" AND PET.IDPERSONA =:");
					contador ++;
					sql.append(contador);
					htCodigos.put(new Integer(contador),peticionEejg.getUnidadFamiliar().getPersonaJG().getIdPersona());
					
					
				}
						
				
			}
			
		}
		sql.append(" ORDER BY PET.FECHAPETICION ");
		List<ScsEejgPeticionesBean> lPeticionesEejg = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	lPeticionesEejg = new ArrayList<ScsEejgPeticionesBean>();
            	ScsEejgPeticionesBean peticionEejgOut = null;
            	ScsEejgXmlBean xmlPeticionEejg = null;
            	for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		peticionEejgOut = (ScsEejgPeticionesBean) this.hashTableToBean(htFila);
            		if(peticionEejg.getUnidadFamiliar()!=null)
            			peticionEejgOut.setUnidadFamiliar(peticionEejg.getUnidadFamiliar());
            		xmlPeticionEejg = new ScsEejgXmlBean();
            		peticionEejgOut.setXmlPeticionEejg(xmlPeticionEejg);
            		xmlPeticionEejg.setIdPeticion(peticionEejgOut.getIdPeticion());
            		xmlPeticionEejg.setEnvioRespuesta(UtilidadesHash.getString(htFila,"XML_"+ScsEejgXmlBean.C_ENVIORESPUESTA));
            		xmlPeticionEejg.setXml(UtilidadesHash.getString(htFila,"XML_"+ScsEejgXmlBean.C_XML));
            		xmlPeticionEejg.setEstado(UtilidadesHash.getInteger(htFila,"XML_"+ScsEejgXmlBean.C_ESTADO));
            		xmlPeticionEejg.setIdXml(UtilidadesHash.getInteger(htFila,"XML_"+ScsEejgXmlBean.C_IDXML));
            		lPeticionesEejg.add(peticionEejgOut);
            		
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return lPeticionesEejg;
		
	} 
	
	


	/**
	 * Método que obtiene una lista de peticines pendientes de solicitar o en estado inicial
	 */
	public List<ScsEejgPeticionesBean> getPeticionesIniciadas() throws ClsExceptions {
		Hashtable hash = new Hashtable();
		hash.put(ScsEejgPeticionesBean.C_ESTADO, ScsEejgPeticionesBean.EEJG_ESTADO_INICIAL);
		return select(hash);		
	}


	/**
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsEejgPeticionesBean> getSolicitudesPendientes() throws ClsExceptions {
		String horas = "1.25"; //1.25 = 30 horas
		String where = " WHERE " + ScsEejgPeticionesBean.C_ESTADO  + " = " + ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA;
		where += " AND (SYSDATE - " + horas + ") >= " + ScsEejgPeticionesBean.C_FECHAPETICION;		
		return select(where);
	}
	
}