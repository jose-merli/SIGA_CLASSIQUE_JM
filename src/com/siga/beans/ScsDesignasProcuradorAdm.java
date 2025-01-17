
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TIPODESIGNASCOLEGIADO
 */
public class ScsDesignasProcuradorAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDesignasProcuradorAdm (UsrBean usuario) {
		super( ScsDesignasProcuradorBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	
				ScsDesignasProcuradorBean.C_FECHAMODIFICACION,	ScsDesignasProcuradorBean.C_USUMODIFICACION,
				ScsDesignasProcuradorBean.C_ANIO,				ScsDesignasProcuradorBean.C_FECHADESIGNA,
				ScsDesignasProcuradorBean.C_FECHARENUNCIA,		ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA,
				ScsDesignasProcuradorBean.C_IDINSTITUCION,		ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC,
				ScsDesignasProcuradorBean.C_IDPROCURADOR,		ScsDesignasProcuradorBean.C_IDTIPOMOTIVO,
				ScsDesignasProcuradorBean.C_IDTURNO,			ScsDesignasProcuradorBean.C_NUMERO,			
				ScsDesignasProcuradorBean.C_NUMERODESIGNACION,	ScsDesignasProcuradorBean.C_OBSERVACIONES
		};
		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	
				ScsDesignasProcuradorBean.C_IDINSTITUCION,		ScsDesignasProcuradorBean.C_IDTURNO,
				ScsDesignasProcuradorBean.C_ANIO,				ScsDesignasProcuradorBean.C_NUMERO,
				ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC, ScsDesignasProcuradorBean.C_IDPROCURADOR,
				ScsDesignasProcuradorBean.C_FECHADESIGNA
		};
		return campos;
	}
	
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDesignasProcuradorBean bean = null;
		try{
			bean = new ScsDesignasProcuradorBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_ANIO));
			bean.setFechaDesigna(UtilidadesHash.getString(hash,ScsDesignasProcuradorBean.C_FECHADESIGNA));
			bean.setFechaRenuncia(UtilidadesHash.getString(hash,ScsDesignasProcuradorBean.C_FECHARENUNCIA));
			bean.setFechaRenunciaSolicita(UtilidadesHash.getString(hash,ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_IDINSTITUCION));
			bean.setIdInstitucionProc(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC));
			bean.setIdProcurador(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_IDPROCURADOR));
			bean.setIdTipoMotivo(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_IDTIPOMOTIVO));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_IDTURNO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_NUMERO));
			bean.setNumeroDesignacion(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_NUMERODESIGNACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDesignasProcuradorBean.C_USUMODIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDesignasProcuradorBean.C_FECHAMODIFICACION));
		}catch(Exception e){
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
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsDesignasProcuradorBean b = (ScsDesignasProcuradorBean) bean;
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_ANIO,b.getAnio());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_FECHADESIGNA,b.getFechaDesigna());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_FECHARENUNCIA,b.getFechaRenuncia());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_FECHARENUNCIASOLICITA,b.getFechaRenunciaSolicita());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_IDINSTITUCION,b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC,b.getIdInstitucionProc());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_IDPROCURADOR,b.getIdProcurador());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_IDTIPOMOTIVO,b.getIdTipoMotivo());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_IDTURNO,b.getIdTurno());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_NUMERO,b.getNumero());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_NUMERODESIGNACION,b.getNumeroDesignacion());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(hash, ScsDesignasProcuradorBean.C_FECHAMODIFICACION,b.getFechaMod());
		}catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
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
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	public boolean actualizarProcuradoresDesignas(Vector designaciones, String idProcurador, String idInstProcurador, String numdesignaProcurador) throws ClsExceptions {
		boolean ok = true;
		try {			
			for (int  j = 0; j < designaciones.size(); j++){				
				Hashtable designaProc = (Hashtable) designaciones.get(j);
				
				//CR - Si se trata del mismo procurador solo se actualizarán sus campos, no se insertará uno nuevo 
				if(idProcurador.equals((String) designaProc.get(ScsProcuradorBean.C_IDPROCURADOR)) && idInstProcurador.equals((String) designaProc.get("IDINSTITUCION_PROC"))){
					String[] campos = {	ScsDesignasProcuradorBean.C_NUMERODESIGNACION};
					String[] claves = {	ScsDesignasProcuradorBean.C_IDINSTITUCION,ScsDesignasProcuradorBean.C_IDTURNO,ScsDesignasProcuradorBean.C_ANIO,	
										ScsDesignasProcuradorBean.C_NUMERO,ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC, ScsDesignasProcuradorBean.C_IDPROCURADOR};	
					// HASH DE UPDATE
					Hashtable datos = new Hashtable();
					datos.put(ScsDesignasProcuradorBean.C_IDINSTITUCION,designaProc.get(ScsDesignasProcuradorBean.C_IDINSTITUCION));
					datos.put(ScsDesignasProcuradorBean.C_IDTURNO,designaProc.get(ScsDesignasProcuradorBean.C_IDTURNO));
					datos.put(ScsDesignasProcuradorBean.C_NUMERO,designaProc.get(ScsDesignasProcuradorBean.C_NUMERO));
					datos.put(ScsDesignasProcuradorBean.C_ANIO,designaProc.get(ScsDesignasProcuradorBean.C_ANIO));
					datos.put(ScsDesignasProcuradorBean.C_IDPROCURADOR,idProcurador);
					datos.put(ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC,idInstProcurador);
					datos.put(ScsDesignasProcuradorBean.C_NUMERODESIGNACION, numdesignaProcurador);
					ok = this.updateDirect(datos, claves, campos);
					if (!ok) 
						throw new ClsExceptions(this.getError());
						
						
				}else{
					// UPDATE DE MODIFICACION para el que actualmente estaba asignado
					String sql = "UPDATE SCS_DESIGNAPROCURADOR "+
								" SET FECHARENUNCIA = SYSDATE, MOTIVOSRENUNCIA = 'Cambio automatico registros relacionados' " +
								" WHERE   IDINSTITUCION = " + designaProc.get(ScsDesignasProcuradorBean.C_IDINSTITUCION) + 
									" AND IDTURNO = " + designaProc.get(ScsDesignasProcuradorBean.C_IDTURNO) +
									" AND ANIO = " + designaProc.get(ScsDesignasProcuradorBean.C_ANIO) + 
									" AND NUMERO = " + designaProc.get(ScsDesignasProcuradorBean.C_NUMERO) +
									" AND FECHARENUNCIA IS NULL";
					ok = this.updateSQL(sql);
					if (!ok) 
						throw new ClsExceptions(this.getError());
					
					// HASH DE INSERCION para el nuevo
					Hashtable designaNueva = new Hashtable();
					designaNueva.put(ScsDesignasProcuradorBean.C_IDINSTITUCION,designaProc.get(ScsDesignasProcuradorBean.C_IDINSTITUCION));
					designaNueva.put(ScsDesignasProcuradorBean.C_IDTURNO,designaProc.get(ScsDesignasProcuradorBean.C_IDTURNO));
					designaNueva.put(ScsDesignasProcuradorBean.C_NUMERO,designaProc.get(ScsDesignasProcuradorBean.C_NUMERO));
					designaNueva.put(ScsDesignasProcuradorBean.C_ANIO,designaProc.get(ScsDesignasProcuradorBean.C_ANIO));
					designaNueva.put(ScsDesignasProcuradorBean.C_IDPROCURADOR,idProcurador);
					designaNueva.put(ScsDesignasProcuradorBean.C_IDINSTITUCION_PROC,idInstProcurador);
					designaNueva.put(ScsDesignasProcuradorBean.C_FECHADESIGNA, "sysdate");		
					designaNueva.put(ScsDesignasProcuradorBean.C_NUMERODESIGNACION, numdesignaProcurador);
					designaNueva.put(ScsDesignasProcuradorBean.C_OBSERVACIONES,"");
					
					ok= this.insert(designaNueva);
					if (!ok) 
						throw new ClsExceptions(this.getError());		
				}
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return ok;
	}
		
}