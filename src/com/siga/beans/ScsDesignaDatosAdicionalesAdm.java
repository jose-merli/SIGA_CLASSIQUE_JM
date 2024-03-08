
package com.siga.beans;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;
import org.codehaus.jackson.map.ObjectMapper;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MaestroDesignasForm;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_DESIGNA_DATOSADICIONALES
 * 
 * @since 07/03/2024 
 */
public class ScsDesignaDatosAdicionalesAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDesignaDatosAdicionalesAdm (UsrBean usuario) {
		super( ScsDesignaDatosAdicionalesBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {
				ScsDesignaDatosAdicionalesBean.C_IDINSTITUCION 						 ,
				ScsDesignaDatosAdicionalesBean.C_IDTURNO                             ,
				ScsDesignaDatosAdicionalesBean.C_ANIO                                ,
				ScsDesignaDatosAdicionalesBean.C_NUMERO                              ,
				ScsDesignaDatosAdicionalesBean.C_NUMEROASUNTO                        ,
				ScsDesignaDatosAdicionalesBean.C_FECHAMODIFICACION                   ,
				ScsDesignaDatosAdicionalesBean.C_USUMODIFICACION                     ,
				ScsDesignaDatosAdicionalesBean.C_TIPO_AUTO                           ,
				ScsDesignaDatosAdicionalesBean.C_FECHA_RESOLUCION_JUDICIAL_OPOSICION ,
				ScsDesignaDatosAdicionalesBean.C_FECHA_RESOLUCION_SENTENCIA_FIRME    ,
				ScsDesignaDatosAdicionalesBean.C_NUMERO_VISTAS_ADICIONALES           ,
				ScsDesignaDatosAdicionalesBean.C_FECHA_VISTA                         ,
				ScsDesignaDatosAdicionalesBean.C_NUMERO_PERSONADOS_MACROCAUSA        ,
				ScsDesignaDatosAdicionalesBean.C_ESVICTIMA                           ,
				ScsDesignaDatosAdicionalesBean.C_ESSUSTITUCION                       
		};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	
				ScsDesignaDatosAdicionalesBean.C_IDINSTITUCION,			
				ScsDesignaDatosAdicionalesBean.C_IDTURNO,
				ScsDesignaDatosAdicionalesBean.C_ANIO,					
				ScsDesignaDatosAdicionalesBean.C_NUMERO,
				ScsDesignaDatosAdicionalesBean.C_NUMEROASUNTO
		};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDesignaDatosAdicionalesBean bean = null;
		try{
			bean = new ScsDesignaDatosAdicionalesBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_ANIO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_IDTURNO));
			bean.setNumero(UtilidadesHash.getLong(hash,ScsDesignaDatosAdicionalesBean.C_NUMERO));
			bean.setNumeroAsunto(UtilidadesHash.getLong(hash,ScsDesignaDatosAdicionalesBean.C_NUMEROASUNTO));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDesignaDatosAdicionalesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_USUMODIFICACION));	
			bean.setTipoAuto(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_TIPO_AUTO                           ));
			bean.setFechaResolucionJudicialOposicion(UtilidadesHash.getString(hash,ScsDesignaDatosAdicionalesBean.C_FECHA_RESOLUCION_JUDICIAL_OPOSICION ));
			bean.setFechaResolucionSentenciaFirme(UtilidadesHash.getString(hash,ScsDesignaDatosAdicionalesBean.C_FECHA_RESOLUCION_SENTENCIA_FIRME    ));
			bean.setNumeroVistasAdicionales(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_NUMERO_VISTAS_ADICIONALES           ));
			bean.setFechaVista(UtilidadesHash.getString(hash,ScsDesignaDatosAdicionalesBean.C_FECHA_VISTA                         ));
			bean.setNumeroPersonadosMacrocausa(UtilidadesHash.getInteger(hash,ScsDesignaDatosAdicionalesBean.C_NUMERO_PERSONADOS_MACROCAUSA        ));
			bean.setEsVictima(UtilidadesHash.getString(hash,ScsDesignaDatosAdicionalesBean.C_ESVICTIMA                           ));
			bean.setEsSustitucion(UtilidadesHash.getString(hash,ScsDesignaDatosAdicionalesBean.C_ESSUSTITUCION                       ));
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
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsDesignaDatosAdicionalesBean b = (ScsDesignaDatosAdicionalesBean) bean;
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_IDINSTITUCION,b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_IDTURNO,b.getIdTurno());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_ANIO,b.getAnio());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_NUMERO,b.getNumero());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_NUMEROASUNTO,b.getNumeroAsunto());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_TIPO_AUTO,b.getTipoAuto());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_FECHA_RESOLUCION_JUDICIAL_OPOSICION,b.getFechaResolucionJudicialOposicion());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_FECHA_RESOLUCION_SENTENCIA_FIRME,b.getFechaResolucionSentenciaFirme());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_NUMERO_VISTAS_ADICIONALES,b.getNumeroVistasAdicionales());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_FECHA_VISTA,b.getFechaVista());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_NUMERO_PERSONADOS_MACROCAUSA,b.getNumeroPersonadosMacrocausa());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_ESVICTIMA,b.getEsVictima());
			UtilidadesHash.set(hash, ScsDesignaDatosAdicionalesBean.C_ESSUSTITUCION,b.getEsSustitucion());
			return hash;
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	
	/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.query(consulta)) {
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
	
}