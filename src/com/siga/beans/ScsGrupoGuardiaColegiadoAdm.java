package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

/**
 * Implementa las operaciones sobre la base de datos a la tabla SCS_INSCRIPCIONGUARDIA
 */
public class ScsGrupoGuardiaColegiadoAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor 
	 */
	public ScsGrupoGuardiaColegiadoAdm (UsrBean usuario) {
		super(ScsGrupoGuardiaColegiadoBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean()
	{
		String[] campos =
		{
				ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO,
				ScsGrupoGuardiaColegiadoBean.C_IDPERSONA,
				ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION,
				ScsGrupoGuardiaColegiadoBean.C_IDTURNO,
				ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA,
				ScsGrupoGuardiaColegiadoBean.C_FECHASUSCRIPCION,
				ScsGrupoGuardiaColegiadoBean.C_IDGRUPO,
				ScsGrupoGuardiaColegiadoBean.C_ORDEN,
				ScsGrupoGuardiaColegiadoBean.C_FECHAMODIFICACION,
				ScsGrupoGuardiaColegiadoBean.C_USUMODIFICACION,
				ScsGrupoGuardiaColegiadoBean.C_FECHACREACION,
				ScsGrupoGuardiaColegiadoBean.C_USUCREACION
		};
		return campos;
	}
	
	/**
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos =
		{
				ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO
		};
		return campos;
	}
	
	/**
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsGrupoGuardiaColegiadoBean bean = null;
		try{
			bean = new ScsGrupoGuardiaColegiadoBean();
			bean.setIdGrupoGuardiaColegiado(Long.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO)));
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION)));
			bean.setIdPersona(Long.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA)));
			bean.setIdTurno(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDTURNO)));
			bean.setIdGuardia(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA)));
			bean.setFechaSuscripcion((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_FECHASUSCRIPCION));
			bean.setIdGrupo(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO)));
			bean.setOrden(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_ORDEN)));
			bean.setFechaCreacion((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_FECHACREACION));
			bean.setUsuCreacion(Integer.valueOf((String)hash.get(ScsGrupoGuardiaColegiadoBean.C_USUCREACION)));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	
	/**
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsGrupoGuardiaColegiadoBean b = (ScsGrupoGuardiaColegiadoBean) bean;
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO,String.valueOf(b.getIdGrupoGuardiaColegiado()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_FECHASUSCRIPCION, String.valueOf(b.getFechaSuscripcion()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO, String.valueOf(b.getIdGrupo()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_ORDEN, String.valueOf(b.getOrden()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_FECHACREACION, String.valueOf(b.getFechaCreacion()));
			hash.put(ScsGrupoGuardiaColegiadoBean.C_USUCREACION, String.valueOf(b.getUsuCreacion()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return null;
	}
	
	/**
	 * Funcion ejecutaSelect(String select)
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	// Ponemos a un letrado como ultimo del grupo
	public void setUltimoDeGrupo(String idGrupoGuardiaColegiado) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		String[] claves ={ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO}; 
		String[] campos ={ScsGrupoGuardiaColegiadoBean.C_ORDEN};
		// jbd // Para no aumentar el orden a lo tonto solo lo aumentamos cuando no sea el ultimo
		if(!esUltimoDeGrupo(idGrupoGuardiaColegiado)){
			hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO, idGrupoGuardiaColegiado);
			hash.put(ScsGrupoGuardiaColegiadoBean.C_ORDEN, this.getSiguientePosicion(idGrupoGuardiaColegiado));
			try {
				updateDirect(hash, claves, campos);
			} catch (Exception e) {
				throw new ClsExceptions (e, "Error al actualizar la posicion del letrado");
			}
		}
	}
	
	private String getSiguientePosicion(String idGrupoGuardiaColegiado) throws ClsExceptions{
	String posicion="";
		String sql = "select max(orden)+1 posicion from scs_grupoguardiacolegiado where idgrupoguardia=";
		sql += " (select idgrupoguardia from scs_grupoguardiacolegiado where idgrupoguardiacolegiado = "+idGrupoGuardiaColegiado+")";
		RowsContainer rc = new RowsContainer(); 
		try {
			if (rc.query(sql) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow(); 
				if (registro != null) 
					posicion = (String)registro.get("POSICION");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar la posicion del ultimo letrado");
		}
		return posicion;
	}
	
	private boolean esUltimoDeGrupo(String idGrupoGuardiaColegiado) throws ClsExceptions{
		String idGGCUltimo="";
	
		String sql = "select idgrupoguardiacolegiado from scs_grupoguardiacolegiado where idgrupoguardia = ";
        sql+= " (select idgrupoguardia from scs_grupoguardiacolegiado ";
        sql+= " where idgrupoguardiacolegiado = "+idGrupoGuardiaColegiado+")";
        sql+= " order by orden desc";
		RowsContainer rc = new RowsContainer(); 
		try {
			if (rc.query(sql) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow(); 
				if (registro != null) {
					idGGCUltimo =(String)registro.get(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO);
				}
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar la posicion del ultimo letrado");
		}
		return idGGCUltimo.equalsIgnoreCase(idGrupoGuardiaColegiado);
	}
	
	public Hashtable getGrupoGuardia(String idInstitucion, String idTurno, String idGuardia, String numeroGrupo) throws ClsExceptions{
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();
		sql.append(" select idgrupoguardia, idinstitucion, idturno, idguardia, ");
		sql.append(" numerogrupo, fechacreacion, usucreacion, fechamodificacion, usumodificacion ");
		sql.append(" from scs_grupoguardia ");
		sql.append(" where idinstitucion="+idInstitucion+" and idturno ="+idTurno+" and idguardia="+idGuardia+" and numerogrupo="+numeroGrupo);
		RowsContainer rc = new RowsContainer(); 
		try {
			if (rc.query(sql.toString()) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow(); 
				if (registro != null) 
					hash = (Hashtable) registro.clone();
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar la posicion del ultimo letrado");
		}
		return hash;
	}
	
	public void insertaGrupoGuardiaColegiado(Integer idInstitucion, Integer idTurno, Integer idGuardia,
			Long idPersona, String fechaValidacion, String numeroGrupo, String ordenGrupo, String idGrupoGuardiaColegiado) throws ClsExceptions{
		
			
		boolean nuevo=false;
		ScsGrupoGuardiaAdm admGrupo = new ScsGrupoGuardiaAdm(this.usrbean);
		// Buscamos el idGrupo que corresponde con el numero del grupo
		Hashtable grupoGuardia=admGrupo.getGrupoGuardia(idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), numeroGrupo);
		String idGrupo = null;
		if(grupoGuardia!=null && grupoGuardia.get(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA)!=null){
			idGrupo=(String)grupoGuardia.get(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
		}else{
			// Si el grupo no existe lo creamos y lo usamos para meter la guardia
			idGrupo = admGrupo.getSecuenciaNextVal(ScsGrupoGuardiaBean.S_SECUENCIA).toString();
			ScsGrupoGuardiaBean bean = new ScsGrupoGuardiaBean();
			bean.setIdGrupoGuardia(Long.valueOf(idGrupo));
			bean.setIdInstitucion(Integer.valueOf(idInstitucion));
			bean.setIdTurno(Integer.valueOf(idTurno));
			bean.setIdGuardia(Integer.valueOf(idGuardia));
			bean.setNumeroGrupo(numeroGrupo);
			bean.setFechaCreacion("sysdate");
			bean.setUsuCreacion(Integer.valueOf(this.usrbean.getUserName()));
			admGrupo.insert(bean);				
			
		}

		Hashtable grupoGuardiaHashtable=new Hashtable();
		grupoGuardiaHashtable.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO, idGrupo);
		grupoGuardiaHashtable.put(ScsGrupoGuardiaColegiadoBean.C_ORDEN, ordenGrupo);
		if(idGrupoGuardiaColegiado==null||(idGrupoGuardiaColegiado!=null&&idGrupoGuardiaColegiado.trim().equalsIgnoreCase(""))){
			nuevo=true;
			idGrupoGuardiaColegiado = getSecuenciaNextVal(ScsGrupoGuardiaColegiadoBean.S_SECUENCIA).toString();
		}
		ScsGrupoGuardiaColegiadoBean bean = new ScsGrupoGuardiaColegiadoBean();
		bean.setIdGrupoGuardiaColegiado (new Long(idGrupoGuardiaColegiado));
		bean.setIdPersona(Long.valueOf(idPersona));
		bean.setOrden(Integer.valueOf(ordenGrupo));
		bean.setIdGrupo(Integer.valueOf(idGrupo));
		bean.setIdInstitucion(Integer.valueOf(idInstitucion));
		bean.setIdTurno(Integer.valueOf(idTurno));
		bean.setIdGuardia(Integer.valueOf(idGuardia));
		bean.setFechaCreacion("sysdate");
		bean.setUsuCreacion(Integer.valueOf(this.usrbean.getUserName()));
		bean.setFechaSuscripcion(fechaValidacion);
		if(nuevo)
			this.insert(bean);
		else
			this.updateDirect(bean);
		//aalg: INC_09672_SIGA. Para actualizar el último idpersona de la cola si el grupo modificado es el último que ha trabajado
		actualizarColaGuardiaPorGrupos(idInstitucion, idTurno, idGuardia);
	}
	
	public void updateOrderGruposLetrados() {
		String idGuardia ="E";
		boolean repetido = false;
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();
		ArrayList<Hashtable<String, String>> grupo = new ArrayList<Hashtable<String,String>>();
		sql.append(" SELECT gg.idgrupoguardia, ggc.idgrupoguardiacolegiado, orden ");
		sql.append(" FROM scs_grupoguardiacolegiado ggc, scs_grupoguardia gg ");
		sql.append(" WHERE ggc.idgrupoguardia = gg.idgrupoguardia ");
		sql.append(" ORDER BY gg.idgrupoguardia, orden");
		RowsContainer rc = new RowsContainer();
		try {
			if (rc.query(sql.toString()) && rc.size() > 0) {				
				for (int i =0; i<rc.size(); i++){					
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					if (registro != null){						
						if(!idGuardia.equals((String)registro.get("IDGRUPOGUARDIA"))){
							if(grupo.size()>0){
								this.sortGrupoLetrado(grupo);
							}
							grupo = new ArrayList<Hashtable<String,String>>();
							idGuardia = (String)registro.get("IDGRUPOGUARDIA");
						}						
						grupo.add(registro);
											
					}						
				}
			}
		} catch (ClsExceptions e) {
			// throw new ClsExceptions (e,
			// "Error al recuperar la posicion del ultimo letrado");
		}
	}
	
	/**
	 * Modifica el orden de la cola de guardia de letrados incrementando su orden en una constante (3000).
	 * Este metodo es necesario para no producir un error de restricción unica de orden en bbdd
	 */
	public void modifyOrderGruposLetrados(int idGrupo) {
		boolean repetido = false;
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();
		int orden=0;		
		ArrayList<Hashtable<String, String>> grupo = new ArrayList<Hashtable<String,String>>();
		sql.append(" SELECT gg.idgrupoguardia, ggc.idgrupoguardiacolegiado, orden ");
		sql.append(" FROM scs_grupoguardiacolegiado ggc, scs_grupoguardia gg ");
		sql.append(" WHERE ggc.idgrupoguardia = gg.idgrupoguardia ");
		sql.append(" AND gg.idgrupoguardia ="+idGrupo+" ");
		sql.append(" ORDER BY gg.idgrupoguardia, orden");
		RowsContainer rc = new RowsContainer();
		try {
			if (rc.query(sql.toString()) && rc.size() > 0) {				
				for (int i =0; i<rc.size(); i++){					
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();										
					if (registro != null){
						orden = new Integer((String)registro.get("ORDEN"))+ new Integer(CalendarioSJCS.SUM_POSICION);
						updateOrderBBDD(new Integer((String)registro.get("IDGRUPOGUARDIA")),
								orden,
								new Integer((String)registro.get("IDGRUPOGUARDIACOLEGIADO")));											
					}
				}
			}
		} catch (ClsExceptions e) {
			// throw new ClsExceptions (e,
			// "Error al recuperar la posicion del ultimo letrado");
		}
	}
	
	/**
	 * Asigna un orden a los letrados inscritos en un grupo de guardia pero si apuntados en él,
	 * en orden consecutivo a partir del ultimo letrado apuntado en el grupo de guardia.
	*/
	public void reordenarRestoGrupoLetrados(int idGrupo, int sizeGrupo) {
		boolean repetido = false;
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();
		int orden=0;		
		ArrayList<Hashtable<String, String>> grupo = new ArrayList<Hashtable<String,String>>();
		sql.append(" SELECT gg.idgrupoguardia, ggc.idgrupoguardiacolegiado, orden ");
		sql.append(" FROM scs_grupoguardiacolegiado ggc, scs_grupoguardia gg ");
		sql.append(" WHERE ggc.idgrupoguardia = gg.idgrupoguardia ");
		sql.append(" AND gg.idgrupoguardia ="+idGrupo+" ");
		sql.append(" AND ggc.orden > "+CalendarioSJCS.SUM_POSICION+ " ");
		sql.append(" ORDER BY gg.idgrupoguardia, orden");
		RowsContainer rc = new RowsContainer();
		try {
			if (rc.query(sql.toString()) && rc.size() > 0) {				
				for (int i =0; i<rc.size(); i++){					
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();										
					if (registro != null){
						orden = sizeGrupo+i+1;
						updateOrderBBDD(new Integer((String)registro.get("IDGRUPOGUARDIA")),
								orden,
								new Integer((String)registro.get("IDGRUPOGUARDIACOLEGIADO")));											
					}
				}
			}
		} catch (ClsExceptions e) {
			// throw new ClsExceptions (e,
			// "Error al recuperar la posicion del ultimo letrado");
		}
	}
	
	/**
	 * Ordena el grupo de guardia en orden consecutivo iniciandose en 1.
	 * Nota: No se utiliza en ningún lado, se deja para un posible futuro uso.
	 */
	private void sortGrupoLetrado(ArrayList<Hashtable<String, String>> grupo) {

		// actualizar cada uno de los registros
		boolean esPrimero = true;
		int primero = 0, posicion = 0;

		for (int i = 0; i < grupo.size(); i++) {
			if (esPrimero) {
				primero = new Integer(grupo.get(i).get("ORDEN"));
				posicion = new Integer(grupo.get(i).get("ORDEN"));
				esPrimero = false;
			} else {
				posicion++;
				UtilidadesHash.set(grupo.get(i), "ORDEN", posicion);
				updateOrderBBDD(new Integer(grupo.get(i).get("IDGRUPOGUARDIA")),posicion, new Integer(grupo.get(i).get("IDGRUPOGUARDIACOLEGIADO")));
			}
		}

		int ultimo = posicion;
		int suma, resta;

		if (primero < CalendarioSJCS.INI_POSICION) {
			suma = CalendarioSJCS.INI_POSICION - ultimo + grupo.size() - 1;

			for (int i = (grupo.size() - 1); i >= 0; i--) {				
				posicion = new Integer(grupo.get(i).get("ORDEN")) + suma;
				updateOrderBBDD(new Integer(grupo.get(i).get("IDGRUPOGUARDIA")),posicion, new Integer(grupo.get(i).get("IDGRUPOGUARDIACOLEGIADO")));
			}
		} else {
			resta = primero - CalendarioSJCS.INI_POSICION;
			for (int i = 0; i < grupo.size(); i++) {
				posicion = new Integer(grupo.get(i).get("ORDEN")) - resta;
				updateOrderBBDD(new Integer(grupo.get(i).get("IDGRUPOGUARDIA")),posicion, new Integer(grupo.get(i).get("IDGRUPOGUARDIACOLEGIADO")));
			}
		}
	}
	
	/**
	 * Actualiza en bbdd el orden modificado necesario para el algoritmo de rotación de guardias
	 */
	public void updateOrderBBDD(int idGuardia, int orden, int idGrupoColegiado){
		Hashtable<String, String> hash = new Hashtable<String, String>();
		Hashtable<String, String> hashDataOld;
		try {
			UtilidadesHash.set(hash, ScsGrupoGuardiaColegiadoBean.C_IDGRUPO, idGuardia);
			UtilidadesHash.set(hash, ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO, idGrupoColegiado);
			UtilidadesHash.set(hash, ScsGrupoGuardiaColegiadoBean.C_ORDEN, orden);
			hashDataOld = this.beanToHashTable((ScsGrupoGuardiaColegiadoBean) this.selectByPK(hash).get(0));
			Hashtable<String, String> hashDataNew = (Hashtable<String, String>)hash.clone();
			UtilidadesHash.set(hashDataNew,ScsGrupoGuardiaColegiadoBean.C_ORDEN, orden);
			this.update(hashDataNew, hashDataOld);

		} catch (ClsExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	*/
	public void actualizarColaGuardia(Integer idInstitucion,Integer idTurno,Integer idGuardia) {
		boolean repetido = false;
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();
		//Guardia del Turno
		ScsGuardiasTurnoBean beanGuardiasTurno = new ScsGuardiasTurnoBean();
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(this.usrbean);
		Long idPersona, idGrupoGuardiaColegiado=null;
		String fechaSubs="";
		sql.append(" SELECT gru.idpersona, gru.Fechasuscripcion ,Gruult.IdgrupoguardiaColegiado ");
		sql.append(" FROM Scs_Grupoguardiacolegiado Gru,Scs_Inscripcionguardia Ins,Scs_Grupoguardiacolegiado Gruult,Scs_Guardiasturno Gua ");
		sql.append(" WHERE Gru.Idinstitucion = Ins.Idinstitucion ");
		sql.append(" AND  Gru.Idpersona = Ins.Idpersona");
		sql.append(" AND Gru.Idturno = Ins.Idturno");
		sql.append(" AND Gru.Idguardia = Ins.Idguardia");
		sql.append(" AND Gru.Fechasuscripcion = Ins.Fechasuscripcion");
		
		sql.append(" AND Gruult.Idpersona = Gru.Idpersona");
		sql.append(" AND Gruult.IdgrupoguardiaColegiado = Gru.IdgrupoguardiaColegiado");
		
		sql.append(" AND Gruult.Idinstitucion = Gua.Idinstitucion");
		sql.append(" AND Gruult.Idturno = Gua.Idturno");
		sql.append(" AND Gruult.Idguardia = Gua.Idguardia");

		sql.append(" AND Gruult.Idpersona = Gua.Idpersona_Ultimo");
		sql.append(" AND Gruult.Fechasuscripcion = Gua.Fechasuscripcion_Ultimo");
		sql.append(" AND Gruult.IdgrupoguardiaColegiado = Gua.Idgrupoguardia_Ultimo");

		sql.append(" AND Gru.Idgrupoguardia = Gruult.Idgrupoguardia");

		sql.append(" AND Gru.Orden >= Gruult.Orden");
		sql.append(" AND Gru.Idinstitucion = " + idInstitucion + " ");
		sql.append(" AND Gru.Idturno = " + idTurno + " ");
		sql.append(" AND Gru.Idguardia = " + idGuardia);
		sql.append(" ORDER BY Gru.Orden desc");
		RowsContainer rc = new RowsContainer();
		try {
			if (rc.query(sql.toString())) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow();
				if (registro != null) {
					idPersona = new Long((String) registro.get("IDPERSONA"));
					idGrupoGuardiaColegiado = new Long((String) registro.get("IDGRUPOGUARDIACOLEGIADO"));
					fechaSubs = (String) registro.get("FECHASUSCRIPCION");
					Hashtable<String, String> hashGuardiasTurno=new Hashtable<String, String>();
					hashGuardiasTurno.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
					hashGuardiasTurno.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
					hashGuardiasTurno.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
					beanGuardiasTurno = (ScsGuardiasTurnoBean)guardiaAdm.selectByPK(hashGuardiasTurno).get(0);
					beanGuardiasTurno.setIdPersona_Ultimo(idPersona);
					beanGuardiasTurno.setIdGrupoGuardiaColegiado_Ultimo(idGrupoGuardiaColegiado);
					beanGuardiasTurno.setFechaSuscripcion_Ultimo(fechaSubs);
					guardiaAdm.updateDirect(beanGuardiasTurno);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	//aalg: INC_09672
	public void actualizarColaGuardiaPorGrupos(Integer idInstitucion,Integer idTurno,Integer idGuardia) {
		boolean repetido = false;
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();
		//Guardia del Turno
		ScsGuardiasTurnoBean beanGuardiasTurno = new ScsGuardiasTurnoBean();
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(this.usrbean);
		Long idPersona, idGrupoGuardiaColegiado=null;
		String fechaSubs="";
		
		sql.append(" SELECT idpersona, Fechasuscripcion, IdgrupoguardiaColegiado ");
		sql.append(" FROM ( ");
		sql.append(" SELECT gru.idpersona, gru.Fechasuscripcion, Gru.IdgrupoguardiaColegiado, gru.orden, gru.idgrupoguardia ");
		sql.append("   FROM Scs_Grupoguardiacolegiado Gru, ");
		sql.append("        Scs_Guardiasturno         Gua ");
		sql.append("  WHERE Gru.Idinstitucion = Gua.Idinstitucion ");
		sql.append("   AND Gru.Idturno = Gua.Idturno ");
		sql.append("    AND Gru.Idguardia = Gua.Idguardia ");
		sql.append("    AND Gru.Idinstitucion = " + idInstitucion + " ");
		sql.append("   AND Gru.Idturno = " + idTurno + " ");
		sql.append("   AND Gru.Idguardia = " + idGuardia );
		sql.append("   and gru.idgrupoguardia = (select idgrupoguardia ");
		sql.append("                                 from Scs_Grupoguardiacolegiado  ");
		sql.append("                                 where Idinstitucion =  Gru.Idinstitucion ");
		sql.append("                                 AND Idturno = Gru.Idturno ");
		sql.append("                                 AND Idguardia = Gru.Idguardia and idgrupoguardiacolegiado = Gua.Idgrupoguardia_Ultimo) ");
		sql.append("  ORDER BY Gru.Orden desc) ");
		sql.append("  WHERE ROWNUM = 1 ");
		
		RowsContainer rc = new RowsContainer();
		try {
			if (rc.query(sql.toString())) {
				Row fila = (Row) rc.get(0);
				Hashtable registro = (Hashtable) fila.getRow();
				if (registro != null) {
					idPersona = new Long((String) registro.get("IDPERSONA"));
					idGrupoGuardiaColegiado = new Long((String) registro.get("IDGRUPOGUARDIACOLEGIADO"));
					fechaSubs = (String) registro.get("FECHASUSCRIPCION");
					Hashtable<String, String> hashGuardiasTurno=new Hashtable<String, String>();
					hashGuardiasTurno.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
					hashGuardiasTurno.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
					hashGuardiasTurno.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
					beanGuardiasTurno = (ScsGuardiasTurnoBean)guardiaAdm.selectByPK(hashGuardiasTurno).get(0);
					beanGuardiasTurno.setIdPersona_Ultimo(idPersona);
					beanGuardiasTurno.setIdGrupoGuardiaColegiado_Ultimo(idGrupoGuardiaColegiado);
					beanGuardiasTurno.setFechaSuscripcion_Ultimo(fechaSubs);
					guardiaAdm.updateDirect(beanGuardiasTurno);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	  
}