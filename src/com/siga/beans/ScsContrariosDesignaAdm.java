
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_DESIGNA
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsContrariosDesignaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsContrariosDesignaAdm (UsrBean usuario) {
		super( ScsContrariosDesignaBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsContrariosDesignaBean.C_IDINSTITUCION,			ScsContrariosDesignaBean.C_IDTURNO,
							ScsContrariosDesignaBean.C_ANIO,					ScsContrariosDesignaBean.C_NUMERO,
							ScsContrariosDesignaBean.C_FECHAMODIFICACION,		ScsContrariosDesignaBean.C_USUMODIFICACION,
							ScsContrariosDesignaBean.C_IDPERSONA,				ScsContrariosDesignaBean.C_OBSERVACIONES,
							ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR,	ScsContrariosDesignaBean.C_IDPROCURADOR,
							ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE,     ScsContrariosDesignaBean.C_IDREPRESENTANTELEGAL,
							ScsContrariosDesignaBean.C_NOMBREABOGADOCONTRARIO,  ScsContrariosDesignaBean.C_IDABOGADOCONTRARIO};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsContrariosDesignaBean.C_IDINSTITUCION,			ScsContrariosDesignaBean.C_IDTURNO,
							ScsContrariosDesignaBean.C_ANIO,					ScsContrariosDesignaBean.C_NUMERO,
							ScsContrariosDesignaBean.C_IDPERSONA};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsContrariosDesignaBean bean = null;
		try{
			bean = new ScsContrariosDesignaBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_ANIO));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsContrariosDesignaBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_IDINSTITUCION));
			bean.setIdProcurador(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_IDPROCURADOR));
			bean.setIdInstitucionProcurador(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_IDPERSONA));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_IDTURNO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_NUMERO));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsContrariosDesignaBean.C_OBSERVACIONES));
			bean.setIdRepresentanteLegal(UtilidadesHash.getString(hash,ScsContrariosDesignaBean.C_IDREPRESENTANTELEGAL));
			bean.setNombreRepresentante(UtilidadesHash.getString(hash,ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsContrariosDesignaBean.C_USUMODIFICACION));
			bean.setnombreAbogadoContrario(UtilidadesHash.getString(hash,ScsContrariosDesignaBean.C_NOMBREABOGADOCONTRARIO));
			bean.setIdAbogadoContrario(UtilidadesHash.getString(hash,ScsContrariosDesignaBean.C_IDABOGADOCONTRARIO));
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsContrariosDesignaBean b = (ScsContrariosDesignaBean) bean;
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_ANIO,b.getAnio());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_NOMBREREPRESENTANTE,b.getNombreRepresentante());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDREPRESENTANTELEGAL,b.getIdRepresentanteLegal());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDINSTITUCION,b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDTURNO,b.getIdTurno());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_NUMERO,b.getNumero());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDPERSONA,b.getIdPersona());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDINSTITUCIONPROCURADOR,b.getIdInstitucionProcurador());
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDPROCURADOR,b.getIdProcurador());	
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_NOMBREABOGADOCONTRARIO,b.getnombreAbogadoContrario());	
			UtilidadesHash.set(hash, ScsContrariosDesignaBean.C_IDABOGADOCONTRARIO,b.getIdAbogadoContrario());	
			
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
	protected String[] getOrdenCampos() {
		return null;
	}
	
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
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

		
	/** 
	 * Recoge los identificadores de las personas de la parte contraria 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno
	 * @param  anio - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getListaContrarios(Integer institucion, Integer anio, Integer numero, Integer turno) throws ClsExceptions,SIGAException {

		Vector datos=new Vector();
	       try {
	            	            
	            String sql =
	       		 "select pc.apellido1||' '||pc.apellido2||', '||pc.nombre NOMBRE_CONTRARIO,"+
	    		 "nvl(cd.nombrerepresentante,"+
	    		 "pl.apellidos1||' '||pl.apellidos2||', '||pl.nombre) NOMBRE_REPRESENTANTE,"+
	    		 "pr.apellidos1||' '||pr.apellidos2||', '||pr.nombre NOMBRE_PROCURADOR "+
	    		 "from scs_contrariosdesigna cd, scs_personajg pc, scs_procurador pr,cen_persona pl "+
	    		 "where cd.idpersona=pc.idpersona(+)"+
	    		 "  and cd.idinstitucion=pc.idinstitucion(+)"+
	    		 "  and cd.idrepresentantelegal=pl.idpersona(+)"+
	    		 "  and cd.idinstitucion_procu=pr.idinstitucion(+)"+
	    		 "  and cd.idprocurador=pr.idprocurador(+)"+
	    		 "  and cd.idinstitucion="+institucion+
	    		 "  and cd.anio="+anio+
	    		 "  and cd.idturno="+turno+
	    		 "  and cd.numero="+numero+
	    		 " order by NOMBRE_CONTRARIO";
	            	
	            RowsContainer rc = new RowsContainer(); 
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre la parte contraria de una designa.");
	       }
	       return datos;                        
	    }
	
	public String getNcolegiadoRepresentante( String idPersona) throws ClsExceptions {
		String consulta = "";
		
			try {
				consulta ="select ncolegiado as NCOLEGIADOABOGADO from  CEN_COLEGIADO where idpersona="+idPersona;
		   
	         }
			catch (Exception e){
				//throw new ClsExceptions(e,"Excepcion en ScsContrariosEJGAdm.getNcolegiadoAbogado(). Consulta SQL:"+consulta);
				return consulta;
			}
			
			return consulta;
	
	}
		
}