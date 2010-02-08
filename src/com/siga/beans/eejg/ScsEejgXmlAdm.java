package com.siga.beans.eejg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.MasterBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.beans.ScsTurnoBean;
/**
 * 
 * @author jorgeta
 *
 */

public class ScsEejgXmlAdm extends MasterBeanAdministrador {
  

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsEejgXmlAdm (UsrBean usuario) {
		super( ScsEejgXmlBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {
				ScsEejgXmlBean.C_IDPETICION,
				ScsEejgXmlBean.C_ESTADO,
				ScsEejgXmlBean.C_IDXML,
				ScsEejgXmlBean.C_ENVIORESPUESTA,
				ScsEejgXmlBean.C_XML,
				
				ScsEejgXmlBean.C_USUMODIFICACION,
				ScsEejgXmlBean.C_FECHAMODIFICACION
				};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsEejgXmlBean.C_IDXML};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsEejgXmlBean bean = null;
		try{
			bean = new ScsEejgXmlBean();
			bean.setIdPeticion(UtilidadesHash.getLong(hash,ScsEejgXmlBean.C_IDPETICION));
			bean.setEnvioRespuesta(UtilidadesHash.getString(hash,ScsEejgXmlBean.C_ENVIORESPUESTA));
			bean.setXml(UtilidadesHash.getString(hash,ScsEejgXmlBean.C_XML));
			bean.setEstado(UtilidadesHash.getInteger(hash,ScsEejgXmlBean.C_ESTADO));
			bean.setIdXml(UtilidadesHash.getInteger(hash,ScsEejgXmlBean.C_IDXML));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsEejgXmlBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsEejgXmlBean.C_USUMODIFICACION));
			
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
		Hashtable<String, Object> hash = null;
		try{
			hash = new Hashtable<String, Object>();
			ScsEejgXmlBean b = (ScsEejgXmlBean) bean;
			
			hash.put(ScsEejgXmlBean.C_IDPETICION, b.getIdPeticion());
			hash.put(ScsEejgXmlBean.C_ENVIORESPUESTA, b.getEnvioRespuesta());
			hash.put(ScsEejgXmlBean.C_XML, b.getXml());
			hash.put(ScsEejgXmlBean.C_ESTADO, b.getEstado());
			hash.put(ScsEejgXmlBean.C_IDXML, b.getIdXml());
			hash.put(ScsEejgXmlBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsEejgXmlBean.C_FECHAMODIFICACION, b.getFechaMod());
			
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	public String[] getOrdenCampos() {
		String[] vector = {ScsEejgXmlBean.C_IDPETICION};
		return vector;
	}
	

	/**
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	public Integer getNuevoIdXml() throws ClsExceptions {
		Integer nuevoId = null;		
		try {
			String select  = "SELECT NVL(MAX("+ScsEejgXmlBean.C_IDXML+"), 0) + 1 AS ID FROM "+ScsEejgXmlBean.T_NOMBRETABLA;
			Vector datos = this.selectGenerico(select);
			String id = (String)((Hashtable)datos.get(0)).get("ID");
			
			nuevoId = Integer.valueOf(id);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}
	

/*
	public List<ScsEejgXmlBean> getComisarias(VolantesExpressVo volanteExpres)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT C.IDCOMISARIA , C.IDINSTITUCION, ");
		sql.append(" c.NOMBRE || ' (' || po.nombre || ')' AS NOMBRE ");
		sql.append(" FROM SCS_COMISARIA       c, ");
		sql.append(" cen_poblaciones     po, ");
		sql.append(" cen_partidojudicial par, ");
		sql.append(" scs_subzonapartido  spar, ");
		sql.append(" scs_subzona         szo, ");
		sql.append(" scs_zona            zo, ");
		sql.append(" scs_turno           tu ");
		sql.append(" where tu.idzona = zo.idzona ");
		sql.append(" AND tu.idinstitucion = zo.idinstitucion ");
		sql.append(" AND zo.idzona = szo.idzona ");
		sql.append(" AND zo.idinstitucion = szo.idinstitucion ");
		sql.append(" AND szo.idinstitucion = spar.idinstitucion ");
		sql.append(" AND szo.idsubzona = spar.idsubzona ");
		sql.append(" AND szo.idzona = spar.idzona ");
		sql.append(" AND spar.idpartido = par.idpartido ");
		sql.append(" AND par.idpartido = po.idpartido ");
		sql.append(" AND c.idpoblacion = po.idpoblacion ");
		sql.append(" and TU.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
		sql.append(" and tu.idinstitucion = c.idinstitucion ");
		sql.append(" AND TU.IDTURNO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdTurno());
		sql.append(" ORDER BY DESCRIPCION ");
		
		List<ScsEejgXmlBean> alComisarias = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alComisarias = new ArrayList<ScsEejgXmlBean>();
            	ScsEejgXmlBean comisariaBean = new ScsEejgXmlBean();
            	comisariaBean.setNombre(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(), "general.combo.seleccionar"));
            	comisariaBean.setIdComisaria(new Integer(-1));
    			alComisarias.add(comisariaBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		comisariaBean = new ScsEejgXmlBean();
            		comisariaBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsEejgXmlBean.C_IDINSTITUCION));
            		comisariaBean.setIdComisaria(UtilidadesHash.getInteger(htFila,ScsEejgXmlBean.C_IDCOMISARIA));
            		comisariaBean.setNombre(UtilidadesHash.getString(htFila,ScsEejgXmlBean.C_NOMBRE));
            		alComisarias.add(comisariaBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alComisarias;
		
	} 
	*/
	
}