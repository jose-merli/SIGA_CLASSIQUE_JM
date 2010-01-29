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
		try{
			bean = new ScsEejgPeticionesBean();
			bean.setIdPeticion(UtilidadesHash.getLong(hash,ScsEejgPeticionesBean.C_IDPETICION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsEejgPeticionesBean.C_IDINSTITUCION));
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
		Hashtable<String, Object> hash = null;
		try{
			hash = new Hashtable<String, Object>();
			ScsEejgPeticionesBean b = (ScsEejgPeticionesBean) bean;
			
			hash.put(ScsEejgPeticionesBean.C_IDPETICION, b.getIdPeticion());
			hash.put(ScsEejgPeticionesBean.C_IDINSTITUCION, b.getIdInstitucion());
			hash.put(ScsEejgPeticionesBean.C_ANIO, b.getAnio());
			hash.put(ScsEejgPeticionesBean.C_ESTADO, b.getEstado());
			hash.put(ScsEejgPeticionesBean.C_FECHAPETICION, b.getFechaPeticion());
			hash.put(ScsEejgPeticionesBean.C_IDPERSONA, b.getIdPersona());
			hash.put(ScsEejgPeticionesBean.C_IDSOLICITUD, b.getIdSolicitud());
			hash.put(ScsEejgPeticionesBean.C_IDTIPOEJG, b.getIdTipoEjg());
			hash.put(ScsEejgPeticionesBean.C_IDXML, b.getIdXml());
			hash.put(ScsEejgPeticionesBean.C_NUMERO, b.getNumero());
			hash.put(ScsEejgPeticionesBean.C_NUMEROINTENTOSCONSULTA, b.getNumeroIntentosConsulta());
			hash.put(ScsEejgPeticionesBean.C_NUMEROINTENTOSSOLICITUD, b.getNumeroIntentosSolicitud());
			
			
			hash.put(ScsEejgPeticionesBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsEejgPeticionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			
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
		String[] vector = {ScsEejgPeticionesBean.C_IDPETICION};
		return vector;
	}
	

	

	public Integer getNuevoIdPeticion(String idInstitucion) throws ClsExceptions {
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsEejgPeticionesBean.C_IDPETICION+")+1 AS ID FROM "+ScsEejgPeticionesBean.T_NOMBRETABLA;
					  
			
			Vector<Hashtable<String, Object>> datos = this.selectGenerico(select);
			String id = (String)((Hashtable<String , Object>)datos.get(0)).get("ID");
			
			if ( (datos == null) || (id!= null && id.equals("")) )
				nuevoId = new Integer("0");
			else
				nuevoId = new Integer(id);

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}
/*
	public List<ScsEejgPeticionesBean> getComisarias(VolantesExpressVo volanteExpres)throws ClsExceptions{

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
		
		List<ScsEejgPeticionesBean> alComisarias = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alComisarias = new ArrayList<ScsEejgPeticionesBean>();
            	ScsEejgPeticionesBean comisariaBean = new ScsEejgPeticionesBean();
            	comisariaBean.setNombre(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(), "general.combo.seleccionar"));
            	comisariaBean.setIdComisaria(new Integer(-1));
    			alComisarias.add(comisariaBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		comisariaBean = new ScsEejgPeticionesBean();
            		comisariaBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsEejgPeticionesBean.C_IDINSTITUCION));
            		comisariaBean.setIdComisaria(UtilidadesHash.getInteger(htFila,ScsEejgPeticionesBean.C_IDCOMISARIA));
            		comisariaBean.setNombre(UtilidadesHash.getString(htFila,ScsEejgPeticionesBean.C_NOMBRE));
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