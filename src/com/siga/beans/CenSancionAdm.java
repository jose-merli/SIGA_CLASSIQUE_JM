package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
//import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.form.SancionesLetradoForm;
//import com.siga.general.SIGAException;
import com.siga.general.SIGAException;

/**
 * Implementa las operaciones sobre el bean de la tabla CEN_TIPOSANCION
 * 
 * @author RGG
 * @since 16/03/2007
 */
public class CenSancionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public CenSancionAdm (UsrBean usuario) {
		super( CenSancionBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	public String[] getCamposBean() {
		String[] campos = { CenSancionBean.C_IDSANCION,
				CenSancionBean.C_IDPERSONA, 
				CenSancionBean.C_IDTIPOSANCION,
				CenSancionBean.C_REFCOLEGIO, 
				CenSancionBean.C_REFCGAE,
				CenSancionBean.C_FECHARESOLUCION,
				CenSancionBean.C_FECHAIMPOSICION,
				CenSancionBean.C_FECHAACUERDO, 
				CenSancionBean.C_FECHAFIRMEZA,
				CenSancionBean.C_FECHAINICIO, 
				CenSancionBean.C_FECHAFIN,
				CenSancionBean.C_FECHAREHABILITADO, 
				CenSancionBean.C_TEXTO,
				CenSancionBean.C_OBSERVACIONES,
				CenSancionBean.C_IDINSTITUCIONSANCION,
				CenSancionBean.C_IDINSTITUCION,
				CenSancionBean.C_CHKREHABILITADO, 
				CenSancionBean.C_CHKFIRMEZA,
				CenSancionBean.C_USUMODIFICACION,
				CenSancionBean.C_FECHAMODIFICACION, 
				CenSancionBean.C_CHKARCHIVADA,
				CenSancionBean.C_FECHAARCHIVADA
				};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	public String[] getClavesBean() {
		String[] campos = {	CenSancionBean.C_IDSANCION,CenSancionBean.C_IDPERSONA};
		return campos;
	}

	
	public String[] getClavesBeans() {
		String[] campos = {	CenSancionBean.C_IDINSTITUCION,CenSancionBean.C_FECHAFIN, CenSancionBean.C_CHKREHABILITADO};
		return campos;
	}
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenSancionBean bean = null;
		try{
			bean = new CenSancionBean();
			bean.setIdSancion(UtilidadesHash.getInteger(hash,CenSancionBean.C_IDSANCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,CenSancionBean.C_IDPERSONA));
			bean.setIdTipoSancion(UtilidadesHash.getInteger(hash,CenSancionBean.C_IDTIPOSANCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenSancionBean.C_IDINSTITUCION));
			bean.setIdInstitucionSancion(UtilidadesHash.getInteger(hash,CenSancionBean.C_IDINSTITUCIONSANCION));
			bean.setRefColegio(UtilidadesHash.getString(hash,CenSancionBean.C_REFCOLEGIO));
			bean.setRefCGAE(UtilidadesHash.getString(hash,CenSancionBean.C_REFCGAE));
			bean.setFechaAcuerdo(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAACUERDO));
			bean.setFechaFin(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAFIN));
			bean.setFechaInicio(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAINICIO));
			bean.setFechaFirmeza(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAFIRMEZA));
			bean.setChkFirmeza(UtilidadesHash.getString(hash,CenSancionBean.C_CHKFIRMEZA));
			bean.setFechaRehabilitado(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAREHABILITADO));
			bean.setChkRehabilitado(UtilidadesHash.getString(hash,CenSancionBean.C_CHKREHABILITADO));
			bean.setFechaResolucion(UtilidadesHash.getString(hash,CenSancionBean.C_FECHARESOLUCION));
			bean.setFechaImposicion(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAIMPOSICION));
			bean.setTexto(UtilidadesHash.getString(hash,CenSancionBean.C_TEXTO));
			bean.setObservaciones(UtilidadesHash.getString(hash,CenSancionBean.C_OBSERVACIONES));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenSancionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSancionBean.C_USUMODIFICACION));
			bean.setChkArchivada(UtilidadesHash.getString(hash,CenSancionBean.C_CHKARCHIVADA));
			bean.setFechaArchivada(UtilidadesHash.getString(hash,CenSancionBean.C_FECHAARCHIVADA));
					
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
			CenSancionBean b = (CenSancionBean) bean;
			hash.put(CenSancionBean.C_IDSANCION, String.valueOf(b.getIdSancion()));
			hash.put(CenSancionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			hash.put(CenSancionBean.C_IDTIPOSANCION, String.valueOf(b.getIdTipoSancion()));
			hash.put(CenSancionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(CenSancionBean.C_IDINSTITUCIONSANCION, String.valueOf(b.getIdInstitucionSancion()));
			hash.put(CenSancionBean.C_REFCOLEGIO, b.getRefColegio());
			hash.put(CenSancionBean.C_REFCGAE, b.getRefCGAE());
			hash.put(CenSancionBean.C_FECHAACUERDO, b.getFechaAcuerdo());
			hash.put(CenSancionBean.C_FECHAFIN, b.getFechaFin());
			hash.put(CenSancionBean.C_FECHAFIRMEZA, b.getFechaFirmeza());
			hash.put(CenSancionBean.C_CHKFIRMEZA, b.getChkFirmeza());
			hash.put(CenSancionBean.C_FECHAINICIO, b.getFechaInicio());
			hash.put(CenSancionBean.C_FECHAREHABILITADO, b.getFechaRehabilitado());
			hash.put(CenSancionBean.C_CHKREHABILITADO, b.getChkRehabilitado());
			hash.put(CenSancionBean.C_FECHARESOLUCION, b.getFechaResolucion());
			hash.put(CenSancionBean.C_FECHAIMPOSICION, b.getFechaImposicion());
			hash.put(CenSancionBean.C_TEXTO, b.getTexto());
			hash.put(CenSancionBean.C_OBSERVACIONES, b.getObservaciones());
			hash.put(CenSancionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(CenSancionBean.C_FECHAMODIFICACION, b.getFechaMod());
			hash.put(CenSancionBean.C_FECHAARCHIVADA, b.getFechaArchivada());
			hash.put(CenSancionBean.C_CHKARCHIVADA, b.getChkArchivada());
			
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
		String[] vector = {CenSancionBean.C_IDPERSONA,CenSancionBean.C_IDSANCION};
		return vector;
	}

	/**
	 * Obtiene las sanciones para un letrado y una institucion que lo dio de alta
	 * @param idPersona
	 * @param idInstitucionAlta
	 * @return Vector de hashtables con los resultados
	 * @throws ClsExceptions
	 */
	public Vector getSancionesLetrado(String idPersona, String idInstitucionAlta) throws ClsExceptions {
		Vector salida = new Vector();
		Hashtable hash=null;
		RowsContainer rc = new RowsContainer(); 
		try{
			
            String sql ="SELECT " +
			" nvl( " +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+",' ') || ' ' || nvl( " +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+",' ') || ' ' || nvl( " +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+",' ') AS NOMBRE_LETRADO, " +
			" " +CenTipoSancionBean.T_NOMBRETABLA+"."+CenTipoSancionBean.C_DESCRIPCION+" AS NOMBRE_TIPOSANCION, " +
			" " +CenInstitucionBean.T_NOMBRETABLA+"."+CenInstitucionBean.C_ABREVIATURA+" AS ABREVIATURA_INSTI, " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDINSTITUCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDINSTITUCIONSANCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDPERSONA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDSANCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDTIPOSANCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAINICIO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAFIN+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAACUERDO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAFIRMEZA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_CHKFIRMEZA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_CHKREHABILITADO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAREHABILITADO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHARESOLUCION+",  " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAIMPOSICION+",  " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_REFCGAE+",  " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAARCHIVADA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_CHKARCHIVADA+" " +
            " FROM " + 
			CenSancionBean.T_NOMBRETABLA + ", " + 
			CenPersonaBean.T_NOMBRETABLA + ", " + 
			CenTipoSancionBean.T_NOMBRETABLA + ", " + 
			CenInstitucionBean.T_NOMBRETABLA + " " + 
			" WHERE " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDINSTITUCIONSANCION+ "=" + CenInstitucionBean.T_NOMBRETABLA +"."+ CenInstitucionBean.C_IDINSTITUCION +  						 										
			" AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDPERSONA+ "=" + CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA + 
			" AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDTIPOSANCION+ "=" + CenTipoSancionBean.T_NOMBRETABLA +"."+ CenTipoSancionBean.C_IDTIPOSANCION; 
			
            // filtro por institucion mia
			sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDINSTITUCION+ "=" + idInstitucionAlta;				 										
            // filtro por persona mia
			sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDPERSONA+ "=" + idPersona;						 										
			sql += " AND (nvl (" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+ ",0)=0";
			sql += " OR (nvl (" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+",0)=1";
			sql += " AND "+ CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+" IS NOT NULL";
			sql += " AND "+ CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+" > sysdate))";
			//sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+ "=0";
            
			
            rc = this.find(sql);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
	                Row fila = (Row) rc.get(i);
					salida.add((Hashtable)fila.getRow());				                
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener las sanciones del letrado");
		}
		return salida;

	}
	

	/**
	 * Obtiene las sanciones para unos criterios de busqueda y una institucion que lo dio de alta
	 * @param form Formulario con los criterios
	 * @param idInstitucionAlta
	 * @return Vector de Hashtables con los resultados
	 * @throws ClsExceptions
	 */
	public Paginador getSancionesBuscar(SancionesLetradoForm form, String idInstitucionAlta, String tipobusqueda) throws ClsExceptions{
		Vector salida = new Vector();
		Hashtable hash=null;
		String chkRehabilitado="";
		String tipofecha="";
		String fechainicioarchivada="";
		String fechafinarchivada="";
		RowsContainer rc = new RowsContainer(); 
		try{
			
            String sql ="SELECT " +
			" nvl( " +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_NOMBRE+",' ') || ' ' || nvl( " +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS1+",' ') || ' ' || nvl( " +CenPersonaBean.T_NOMBRETABLA+"."+CenPersonaBean.C_APELLIDOS2+",' ') AS NOMBRE_LETRADO, " +
			" " +CenTipoSancionBean.T_NOMBRETABLA+"."+CenTipoSancionBean.C_DESCRIPCION+" AS NOMBRE_TIPOSANCION, " +
			" " +CenInstitucionBean.T_NOMBRETABLA+"."+CenInstitucionBean.C_ABREVIATURA+" AS ABREVIATURA_INSTI, " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDINSTITUCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDINSTITUCIONSANCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDPERSONA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDSANCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDTIPOSANCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAINICIO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAFIN+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAACUERDO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAFIRMEZA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_CHKFIRMEZA+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_CHKREHABILITADO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAREHABILITADO+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHARESOLUCION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_FECHAIMPOSICION+", " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_REFCGAE+",  " +
			" " +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_CHKARCHIVADA+"  " +
            " FROM " + 
			CenSancionBean.T_NOMBRETABLA + ", " + 
			CenPersonaBean.T_NOMBRETABLA + ", " + 
			CenTipoSancionBean.T_NOMBRETABLA + ", " + 
			CenInstitucionBean.T_NOMBRETABLA + " " + 
			" WHERE " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDINSTITUCIONSANCION+ "=" + CenInstitucionBean.T_NOMBRETABLA +"."+ CenInstitucionBean.C_IDINSTITUCION +  						 										
			" AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDPERSONA+ "=" + CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA + 
			" AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDTIPOSANCION+ "=" + CenTipoSancionBean.T_NOMBRETABLA +"."+ CenTipoSancionBean.C_IDTIPOSANCION;
            
            if (tipobusqueda.equals("1")){//buscar sin archivar            
            // sql+= " AND (nvl(" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+ "=0" ;             
             	sql += " AND (nvl (" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+ ",0)=0";
             	sql += " OR (nvl (" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+",0)=1";
             	sql += " AND "+ CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+" IS NOT NULL";
             	sql += " AND "+ CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+" > sysdate))";            
            }
            
             if (tipobusqueda.equals("2")){//buscar archivados            
            	 sql+= " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKARCHIVADA+ "=1";          
              	
              	if ((form.getFechaInicioArchivada().trim().equals("")) && (form.getFechaFinArchivada().trim().equals("")) ) {              		
              		sql+= " AND (CEN_SANCION.FECHAARCHIVADA <=sysdate OR CEN_SANCION.FECHAARCHIVADA is null)";
              	}else{
              		if (!form.getFechaInicioArchivada().trim().equals("") && (!form.getFechaFinArchivada().trim().equals(""))){
              			sql += " AND TO_DATE(TO_CHAR(" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+ ",'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('" + form.getFechaInicioArchivada()+"','DD/MM/YYYY')"+
				       " AND TO_DATE(TO_CHAR(" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinArchivada()+"', 'DD/MM/YYYY')";			
              		}else if (form.getFechaInicioArchivada().trim().equals("") && (!form.getFechaFinArchivada().trim().equals(""))){
              					sql+= " AND TO_DATE(TO_CHAR(" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinArchivada()+"', 'DD/MM/YYYY')";              			
              				}else if (!form.getFechaInicioArchivada().trim().equals("") && (form.getFechaFinArchivada().trim().equals(""))){
              				  sql += " AND TO_DATE(TO_CHAR(" + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAARCHIVADA+ ",'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('" + form.getFechaInicioArchivada()+"','DD/MM/YYYY')";
              				}
              	}
              
             }

            // filtro por institucion mia
			sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDINSTITUCION+ "=" + idInstitucionAlta;				 										
            
			// filtros de busqueda
			if (!form.getNombreInstitucionBuscar().trim().equals("")) {
				sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDINSTITUCIONSANCION+ "=" + form.getNombreInstitucionBuscar();						 										
			} 
			if (!form.getColegiadoBuscar().trim().equals("")) {
				sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDPERSONA+ "=" + form.getColegiadoBuscar();						 										
			}
			
			if (!form.getTipoSancionBuscar().trim().equals("")) {
				sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDTIPOSANCION+ "=" + form.getTipoSancionBuscar();						 										
			}
			
			//definiendo los tipos de fechas.
			tipofecha=form.getMostrarTiposFechas();
			 
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_ACUERDO)){
				
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAACUERDO+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
					       " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAACUERDO+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAACUERDO+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAACUERDO+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}
				
			
			}
			
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_FIN)){				
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIN+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
					       " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIN+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIN+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIN+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}
					
			}
			
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_FIRMEZA)){
				
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIRMEZA+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
						   " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIRMEZA+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIRMEZA+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAFIRMEZA+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}
			
			}
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_IMPOSICION)){				
			
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAIMPOSICION+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
					       " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAIMPOSICION+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAIMPOSICION+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAIMPOSICION+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}			
			}
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_INICIO)){				
			
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAINICIO+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
					       " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAINICIO+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAINICIO+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAINICIO+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}			
			}
			
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_REHABILITADO)){				
			
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAREHABILITADO+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
							" AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAREHABILITADO+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAREHABILITADO+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHAREHABILITADO+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}			
			}
			
			if (tipofecha.equals(ClsConstants.COMBO_MOSTRAR_RESOLUCION)){				
			
				if (!form.getFechaInicioBuscar().trim().equals("") && (!form.getFechaFinBuscar().trim().equals(""))){
					sql += " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHARESOLUCION+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')"+
						  " AND TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHARESOLUCION+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(form.getFechaInicioBuscar().trim().equals("")&&  (!form.getFechaFinBuscar().trim().equals(""))){
					sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHARESOLUCION+", 'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE('"+form.getFechaFinBuscar()+"', 'DD/MM/YYYY')";
				}else if(!form.getFechaInicioBuscar().trim().equals("")&&  (form.getFechaFinBuscar().trim().equals(""))){
						sql+="And TO_DATE(TO_CHAR("+CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_FECHARESOLUCION+", 'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE('"+form.getFechaInicioBuscar()+"', 'DD/MM/YYYY')";
				}			
			}
			
			
			if (!form.getRefCGAE().trim().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(form.getRefCGAE().trim(),CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_REFCGAE) ;						 										
			}
			if (form.getChkRehabilitado()!=null){
				chkRehabilitado ="1";
				sql += " AND " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_CHKREHABILITADO+ "=" + chkRehabilitado;			}
		
			Paginador paginador = new Paginador(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			return paginador;
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener las sanciones en la busqueda");
		}
		
	}
	
	public String getNuevoId(String idInstitucionAlta) throws ClsExceptions{
		String nuevoId="";
		Hashtable hash=null;
		RowsContainer rc = new RowsContainer(); 
		try{
			
            String sql ="SELECT " +
			" nvl(MAX(" +CenSancionBean.T_NOMBRETABLA+"."+CenSancionBean.C_IDSANCION+"),0)+1  AS CODIGO " +
            " FROM " + 
			CenSancionBean.T_NOMBRETABLA + " " + 
			" WHERE " + CenSancionBean.T_NOMBRETABLA +"."+ CenSancionBean.C_IDINSTITUCION+ "=" + idInstitucionAlta;  						 										
			
			if (rc.findForUpdate(sql)) {
                Row fila = (Row) rc.get(0);
				hash = (Hashtable)fila.getRow();
				nuevoId=UtilidadesHash.getString(hash,"CODIGO");
            }
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener las sanciones en la busqueda");
		}
		return nuevoId;
	}
public String getTienePermisoArchivación(String idInstitucion,
			String usuario) throws ClsExceptions {
		String permiso = "";
		RowsContainer rc = new RowsContainer();
		try {
			String sql =
				"Select Count(*) As Tienepermiso " +
				"  From Adm_Tiposacceso Tip, Adm_Usuarios_Efectivos_Perfil Usu " +
				" Where Tip.Idperfil = Usu.Idperfil " +
				"   And Tip.Idinstitucion = Usu.Idinstitucion " +
				"    " +
				"   And Usu.Idusuario = "+usuario+" " +
				"   And Usu.Idinstitucion = "+idInstitucion+" " +
				"   And Tip.Idproceso ="+ "'"+ClsConstants.PERMISO_ARCHIVARSANCIONES+ "'"+
				"   And Tip.Derechoacceso = '3' " +
				"   And Not Exists " +
				" (Select * " +
				"          From Adm_Tiposacceso Tip2, Adm_Usuarios_Efectivos_Perfil Usu2 " +
				"         Where Tip2.Idperfil = Usu2.Idperfil " +
				"           And Tip2.Idinstitucion = Usu2.Idinstitucion " +
				"            " +
				"           And Usu2.Idinstitucion = Usu.Idinstitucion " +
				"           And Usu2.Idusuario = Usu.Idusuario " +
				"           And Tip2.Derechoacceso = '1') ";

			if (rc.find(sql)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable resultado = fila.getRow();
					permiso = (String) resultado.get("TIENEPERMISO");
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return permiso;
	} // getTienePermisoArchivación()
	
	




 public int getArchivar (String idInstitucion, String fechaArchivada) throws ClsExceptions, SIGAException {
		  Hashtable h = new Hashtable();
		  h.put(new Integer(1), idInstitucion);
		   CenSancionAdm sancionesAdm = new CenSancionAdm(this.usrbean);
		   CenSancionBean sancionesBean = new CenSancionBean();
	       try {
	            
	            String whereSancion =" where CEN_SANCION.CHKREHABILITADO = 1";
                       whereSancion +=" and CEN_SANCION.Fechafin <= '"+fechaArchivada+"' AND CEN_SANCION.IDINSTITUCION ="+idInstitucion;
                       whereSancion += " and cen_sancion.fechaarchivada is null and cen_sancion.chkarchivada=0";

               Vector sanciones= sancionesAdm.select(whereSancion);
               System.out.println(sanciones.size());
               int nSanciones=sanciones.size();
               for(int i=0;i<sanciones.size();i++)
               {
            	   sancionesBean = (CenSancionBean)sanciones.elementAt(i);
			
            	   sancionesBean.setChkArchivada("1");          	 
            	   sancionesBean.setFechaArchivada(GstDate.getApplicationFormatDate ("",fechaArchivada)); 
            	   String datosCambiar[] = new String[2];
				   datosCambiar[0]=CenSancionBean.C_CHKARCHIVADA;			
				   datosCambiar[1]=CenSancionBean.C_FECHAARCHIVADA;
					
					if(!sancionesAdm.updateDirect(sancionesBean,sancionesAdm.getClavesBeans(),datosCambiar))
						throw new ClsExceptions(sancionesAdm.getError());
				}
            return nSanciones;
	     } catch(ClsExceptions e){
			throw e; 
		} catch(Exception e){
			throw new ClsExceptions(e,e.toString()); 
		}
		
		
	} //getArchivar()
}


