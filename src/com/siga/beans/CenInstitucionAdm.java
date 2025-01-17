
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;

/**
* @author ruben.fernandez
*/

public class CenInstitucionAdm extends MasterBeanAdministrador {
    public static final String K_SIN_SIGA="2";
    public static final int C_SDOMICILIO = 0;
    public static final int C_SCODIGOPOSTAL = 1;
    public static final int C_SPOBLACION = 2;
    public static final int C_SPROVINCIA = 3;

	/**
	 * @param tabla
	 * @param request
	 */
	public CenInstitucionAdm (UsrBean usuario) {
		super( CenInstitucionBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	CenInstitucionBean.C_IDINSTITUCION,		CenInstitucionBean.C_NOMBRE,		
							CenInstitucionBean.C_CUENTACONTABLECAJA,CenInstitucionBean.C_BBDDCPD,		
							CenInstitucionBean.C_IDLENGUAJE,		CenInstitucionBean.C_USUMODIFICACION,
							CenInstitucionBean.C_FECHAMODIFICACION,	CenInstitucionBean.C_IDPERSONA,
							CenInstitucionBean.C_CEN_INST_IDINSTITUCION, CenInstitucionBean.C_ABREVIATURA,
							CenInstitucionBean.C_FECHAENPRODUCCION};
		return campos;
	}
	protected String[] getClavesBean() {
		String[] campos = {	CenInstitucionBean.C_IDINSTITUCION};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenInstitucionBean bean = null;
		try{
			bean = new CenInstitucionBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenInstitucionBean.C_IDINSTITUCION));
			bean.setNombre(UtilidadesHash.getString(hash,CenInstitucionBean.C_NOMBRE));
			bean.setCuentaContableCaja(UtilidadesHash.getInteger(hash,CenInstitucionBean.C_CUENTACONTABLECAJA));
			bean.setBbddcpd(UtilidadesHash.getInteger(hash,CenInstitucionBean.C_BBDDCPD));
			bean.setIdLenguaje(UtilidadesHash.getString(hash,CenInstitucionBean.C_IDLENGUAJE));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenInstitucionBean.C_IDPERSONA));
			bean.setCen_inst_idInstitucion(UtilidadesHash.getInteger(hash,CenInstitucionBean.C_CEN_INST_IDINSTITUCION));
			bean.setAbreviatura(UtilidadesHash.getString(hash,CenInstitucionBean.C_ABREVIATURA));
			bean.setFechaEnProduccion(UtilidadesHash.getString(hash,CenInstitucionBean.C_FECHAENPRODUCCION));
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
			CenInstitucionBean b = (CenInstitucionBean) bean;
			hash.put(CenInstitucionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(CenInstitucionBean.C_NOMBRE, b.getNombre());
			hash.put(CenInstitucionBean.C_CUENTACONTABLECAJA, String.valueOf(b.getCuentaContableCaja()));
			hash.put(CenInstitucionBean.C_BBDDCPD, String.valueOf(b.getBbddcpd()));
			hash.put(CenInstitucionBean.C_IDLENGUAJE, String.valueOf(b.getIdLenguaje()));
			hash.put(CenInstitucionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			hash.put(CenInstitucionBean.C_CEN_INST_IDINSTITUCION, String.valueOf(b.getCen_inst_idInstitucion()));
			hash.put(CenInstitucionBean.C_ABREVIATURA, b.getAbreviatura());
			hash.put(CenInstitucionBean.C_FECHAENPRODUCCION, b.getFechaEnProduccion());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}
	
	public Vector getDocumentacionAPresentar (Integer idInstitucion, Integer tipoSolicitud, Integer tipoColegiacion, Integer tipoModalidadDocumentacion) throws ClsExceptions,SIGAException { 
		try {
			CenDocumentacionSolicitudInstituAdm adm = new CenDocumentacionSolicitudInstituAdm(this.usrbean);
			Hashtable hash = new Hashtable ();
			UtilidadesHash.set(hash, CenDocumentacionSolicitudInstituBean.C_IDINSTITUCION, idInstitucion);  
			UtilidadesHash.set(hash, CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD, tipoSolicitud);  
			UtilidadesHash.set(hash, CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION, tipoColegiacion);
			UtilidadesHash.set(hash, CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD, tipoModalidadDocumentacion);
			Vector v = adm.select(hash);
			if (v != null) {
				return v;
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
		return null;
	}

	/**
	 * Obtiene el tipo de instituci�n pasado como par�metro
	 * un formulario de datos (BusquedaClientesForm)
	 * 
	 * @param idInstitucion 
	 * @return java.util.Vector Vector de tablas hash  
	 */
//	public Vector getTipoInstitucion (String idInstitucion) throws ClsExceptions { 
//		try {
//			return null;
//		} 
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Error al recuperar el tipo de instituci�n");
//		}
//	}
	
	/** 
	 * Funcion que devuelve el nombre de la Institucion'
	 * @param  String idInstitucion 
	 * @return String nombreInstitucion
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getNombreInstitucion(String idInstitucion)
			throws ClsExceptions,SIGAException {
		
		String nombreInstitucion="";
		Hashtable hash = new Hashtable();
		try{
			hash.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
			Vector vInstitucion = this.select(hash);
			CenInstitucionBean instBean = (CenInstitucionBean)vInstitucion.elementAt(0);
			nombreInstitucion=instBean.getNombre();
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}catch(Exception e){
			throw new ClsExceptions (e,"Elemento nulo");
		}
		return nombreInstitucion;
	}
	
	/** 
	 * Funcion que devuelve la abreviatura de la Institucion'
	 * @param  String idInstitucion 
	 * @return String nombreInstitucion
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getAbreviaturaInstitucion(String idInstitucion)
			throws ClsExceptions,SIGAException {
		
		String abrevInstitucion="";
		Hashtable hash = new Hashtable();
		try{
			hash.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
			Vector vInstitucion = this.select(hash);
			CenInstitucionBean instBean = (CenInstitucionBean)vInstitucion.elementAt(0);
			abrevInstitucion=instBean.getAbreviatura();
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}catch(Exception e){
			throw new ClsExceptions (e,"Elemento nulo");
		}
		return abrevInstitucion;
	}

	
	/** 
	 * Funcion que devuelve el domicilio de la Institucion'
	 * @param  String idInstitucion 
	 * @return String nombreInstitucion
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String[] getDomicilioInstitucion(String idInstitucion)
			throws ClsExceptions,SIGAException {
		
		String[] direcciones = new String[4];
		RowsContainer rows=new RowsContainer();
        String sql = "";
        sql += "select "+CenDireccionesBean.C_DOMICILIO+" AS DOMICILIO, ";
        sql += "       "+CenDireccionesBean.C_CODIGOPOSTAL+" AS CODIGOPOSTAL, ";
        sql += "       (SELECT P."+CenPoblacionesBean.C_NOMBRE+" FROM "+CenPoblacionesBean.T_NOMBRETABLA+" P WHERE P."+CenPoblacionesBean.C_IDPOBLACION+"=cen_direcciones."+CenDireccionesBean.C_IDPOBLACION+") AS POBLACION, ";
        sql += "       (SELECT P."+CenProvinciaBean.C_NOMBRE+" FROM "+CenProvinciaBean.T_NOMBRETABLA+" P WHERE P."+CenProvinciaBean.C_IDPROVINCIA+"=cen_direcciones."+CenDireccionesBean.C_IDPROVINCIA+") AS PROVINCIA ";
        
        sql += this.getSqlDireccioInstitucion(idInstitucion);
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto ser� null si no existe padre
            direcciones[C_SDOMICILIO] = (String)htRow.get("DOMICILIO");
            direcciones[C_SCODIGOPOSTAL] = (String)htRow.get("CODIGOPOSTAL");
            direcciones[C_SPOBLACION] = (String)htRow.get("POBLACION");
            direcciones[C_SPROVINCIA] = (String)htRow.get("PROVINCIA");
        } else{
        	throw new SIGAException("facturacionsjcs.ficheroBancario.msgerror.noexistedireccion");
        }
 
		return direcciones;
	}

	public String getSqlDireccioInstitucion (String idInstitucion) throws ClsExceptions,SIGAException {
		
		RowsContainer rows=new RowsContainer();
        String sql=" from cen_direcciones " +
           " WHERE idpersona=(SELECT I.IDPERSONA FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION=" + idInstitucion + ") " +
           " AND fechabaja is null " +           
           " AND idinstitucion=" + idInstitucion +
           //CR7 - INC_12027_SIGA. Ya no buscamos la direcci�n preferente de correo, buscamos la que tenga tipo direcci�n facturaci�n. Si no existe esta, que devuelva la ultima modificada.
           " AND " + CenDireccionesBean.C_IDDIRECCION + "= F_SIGA_GETIDDIRECCION_TIPOPRE2("+ idInstitucion +", idPersona ,"+ Integer.toString(ClsConstants.TIPO_DIRECCION_FACTURACION) +", null)";
 
		return sql;
	}
	
	/**
	 * Funcion que devuelve el identificador de la persona
	 * @param	String idInstitucion
	 * @return	String ident persona
	 * @exception ClsExceptions  En cualquier caso de error
	 */
	public String getIdPersona(String idInstitucion) throws ClsExceptions, SIGAException
	{
		String idPersona = "";
		Hashtable hash = new Hashtable();

		try {
			hash.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucion);
			Vector vInstitucion = this.select(hash);
			CenInstitucionBean instBean = (CenInstitucionBean) vInstitucion.elementAt(0);
			idPersona = instBean.getIdPersona().toString();
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		} catch (Exception e) {
			throw new ClsExceptions(e, "Elemento nulo");
		}
		return idPersona;
	}
	
	/** 
	 * Funcion que devuelve el identificador de la persona
	 * @param  String idInstitucion 
	 * @return String ident persona
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getIdInstitucion(String idPersona) throws ClsExceptions, SIGAException
	{
		String idInstitucion = "";
		Hashtable hash = new Hashtable();

		try {
			hash.put(CenInstitucionBean.C_IDPERSONA, idPersona);
			Vector vInstitucion = this.select(hash);
			CenInstitucionBean instBean = (CenInstitucionBean) vInstitucion.elementAt(0);
			idInstitucion = instBean.getIdInstitucion().toString();
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		} catch (Exception e) {
			throw new ClsExceptions(e, "Elemento nulo");
		}
		return idInstitucion;
	}
	
	public boolean tieneSIGA(Integer idInstitucion) throws ClsExceptions,SIGAException {
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
	    CenInstitucionBean instBean;
        try {
            instBean = (CenInstitucionBean)this.selectByPK(htPk).firstElement();
        } catch (ClsExceptions e) {
            throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
        }
        boolean resultado = !instBean.getBbddcpd().equals(Integer.valueOf(CenInstitucionAdm.K_SIN_SIGA));
	    return resultado;
	}
	
	public Integer getInstitucionPadre(Integer idInstitucion) throws ClsExceptions,SIGAException {
        RowsContainer rows=new RowsContainer();
        String sql="SELECT " + CenInstitucionBean.C_CEN_INST_IDINSTITUCION + 
			" FROM " + CenInstitucionBean.T_NOMBRETABLA + 
			" WHERE " + CenInstitucionBean.C_IDINSTITUCION + "="+ idInstitucion;
        Integer valorInt = null;
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto ser� null si no existe padre
            String padre = (String)htRow.get(CenInstitucionBean.C_CEN_INST_IDINSTITUCION);
            valorInt = padre.equals("")?null:Integer.valueOf((String)htRow.get(CenInstitucionBean.C_CEN_INST_IDINSTITUCION));                          
        }
        return valorInt;        
    }
	
	public Vector obtenerInstitucionesAlta () throws Exception 
	{
		
	  	Vector salida = new Vector();
		String query = " WHERE " + CenInstitucionBean.C_FECHAENPRODUCCION + " is not null"; 
		salida = this.select(query);
		return salida;
	}

	public Vector getInstitucionesActAutomatica ()
	{
		Vector datos = new Vector();
		try { 
			RowsContainer rc = new RowsContainer(); 
			String sql = " SELECT i." + CenInstitucionBean.C_IDINSTITUCION + " INSTITUCION, " +
								" i." + CenInstitucionBean.C_IDLENGUAJE + " LENGUAJE " +
					       " FROM " + CenInstitucionBean.T_NOMBRETABLA + " i " +
					      " WHERE i." + CenInstitucionBean.C_FECHAENPRODUCCION +" IS NOT NULL " +
					        " AND NVL ( (SELECT p." + GenParametrosBean.C_VALOR +
					                     " FROM " + GenParametrosBean.T_NOMBRETABLA + " p " +
					                    " WHERE (p." + GenParametrosBean.C_IDINSTITUCION + " = i." + CenInstitucionBean.C_IDINSTITUCION + " OR p." + GenParametrosBean.C_IDINSTITUCION + " = 0) " +
					                      " AND rownum = 1 " +
					                      " AND p." + GenParametrosBean.C_MODULO + " = 'CEN' " +
					                      " AND p." + GenParametrosBean.C_PARAMETRO + " = 'SOLICITUDES_MODIF_CENSO' " +
					                      " AND p." + GenParametrosBean.C_VALOR + " = 'S'), 0) = 'S' "; 
				
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
//					Integer id = UtilidadesHash.getInteger((Hashtable)fila.getRow(), "INSTITUCION");
// 					if (id != null) 
//						datos.add(id);
					
					if ((Hashtable)fila.getRow() != null)
						datos.add((Hashtable)fila.getRow());
				}
			}
		} 
		catch (Exception e) {
			return new Vector();
		}
		return datos;
	}
	
	public Hashtable getDatosInformeFacturasEmitidas (String idInstitucion)
	{
		try { 
			RowsContainer rc = new RowsContainer(); 
			String sql = " select " + CenInstitucionBean.C_NOMBRE + " as EMPRESA, " +
								  " to_char (sysdate,'DD/MM/RRRR') as FECHA_GENERACION " +
					       " from " + CenInstitucionBean.T_NOMBRETABLA +
					      " where idinstitucion = " + idInstitucion ;
            
			if (rc.query(sql)) {
				if (rc.size() == 1)
					return ((Row) rc.get(0)).getRow();
			}
		} 
		catch (Exception e) {
			return null;
		}
		return null;
	}
	public List<CenInstitucionBean> getInstitucionesInformes(Integer idInstitucion,boolean isCombo)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();

			sql.append(" select * from cen_institucion ");
			sql.append(" WHERE IDINSTITUCION  IN ( :");
			/*contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),"2000"); 
			sql.append(" ,:");*/
			contador ++;
			sql.append(contador);
			sql.append(")");
			htCodigos.put(new Integer(contador),idInstitucion);
		//	 sql.append(" ORDER BY abreviatura ");
			
			
			
		
		List<CenInstitucionBean> alInstituciones = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alInstituciones = new ArrayList<CenInstitucionBean>();
            	CenInstitucionBean institucionBean = null;
            	if(isCombo){
	            	if(rc.size()>0){
	            		institucionBean = new CenInstitucionBean();
	            		institucionBean.setIdInstitucion(new Integer(-1));
	            		institucionBean.setAbreviatura("");
	            		alInstituciones.add(institucionBean);
	            	}
            	}
            	institucionBean = new CenInstitucionBean();
        		institucionBean.setIdInstitucion(new Integer("0"));
        		institucionBean.setAbreviatura("POR DEFECTO");
        		alInstituciones.add(institucionBean);
            	for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		institucionBean = new CenInstitucionBean();
            		institucionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,CenInstitucionBean.C_IDINSTITUCION));
            		institucionBean.setAbreviatura(UtilidadesHash.getString(htFila,CenInstitucionBean.C_ABREVIATURA));
            		institucionBean.setNombre(UtilidadesHash.getString(htFila,CenInstitucionBean.C_NOMBRE));
            		alInstituciones.add(institucionBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alInstituciones;
		
		
	} 
	
	
	public String getFechaEnProduccion (String idInstitucion){
		String fechaProduccion="";
		try { 
			RowsContainer rc = new RowsContainer(); 
			String sql = " select fechaenproduccion " +
					       " from " + CenInstitucionBean.T_NOMBRETABLA +
					      " where idinstitucion = " + idInstitucion ;
            
			if (rc.query(sql)) {
				if (rc.size() == 1){
					Row fila = (Row) rc.get(0);
            		Hashtable ht =fila.getRow();
            		
					if (ht.get("FECHAENPRODUCCION")!=null){
						fechaProduccion = (String) ht.get("FECHAENPRODUCCION");
					}
				}
			}
		}catch (Exception e) {
			return "";
		}
		return fechaProduccion;
	}
	
	public boolean estaEnProduccion(String idInstitucion) throws ClsExceptions
	{
		boolean enProduccion = false;

		RowsContainer rc = new RowsContainer();
		String sql = " select fechaenproduccion " + " from " + CenInstitucionBean.T_NOMBRETABLA + " where idinstitucion = " + idInstitucion;

		if (rc.query(sql)) {
			if (rc.size() == 1) {
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();

				if (ht.get("FECHAENPRODUCCION") != null && !ht.get("FECHAENPRODUCCION").equals("")) {
					enProduccion = true;
				}
			}
		}

		return enProduccion;
	} // estaEnProduccion()
	
	public static boolean esConsejoGeneral(Object idInstitucion){
		boolean esConsejoGeneral = false;	  
		String strInstitucion = idInstitucion.toString();
		if(strInstitucion.length()==6){
			strInstitucion = strInstitucion.substring(2);
		}
		int institucionNumber = Integer.parseInt(strInstitucion); 
		if (institucionNumber == ClsConstants.INSTITUCION_CONSEJOGENERAL){ // General
			esConsejoGeneral = true;
		}
		
		return esConsejoGeneral;
	}
	
	public static  boolean esConsejoColegio(Object idInstitucion){
		boolean esConsejoColegio = false;
		String strInstitucion = idInstitucion.toString().substring(2);
		int institucionNumber = Integer.parseInt(strInstitucion); 
		
		if (institucionNumber > ClsConstants.INSTITUCION_CONSEJO){ // Consejo de Colegio
			esConsejoColegio = true;
		}
		
		return esConsejoColegio;
	}
	
	public static  boolean esColegio(Object idInstitucion){
		boolean esColegio = false;
		String strInstitucion = idInstitucion.toString().substring(2);
		int institucionNumber = Integer.parseInt(strInstitucion); 
		
		if (institucionNumber > ClsConstants.INSTITUCION_CONSEJOGENERAL && institucionNumber < ClsConstants.INSTITUCION_CONSEJO){ 
			esColegio = true;
		}
		
		return esColegio;
	}
	
	public static String getIdInstitucionGeneral(String idInstitucion){
		String profesion = idInstitucion.substring(0,2);
		return profesion + ClsConstants.INSTITUCION_CONSEJOGENERAL;
	}

	public List<CenInstitucionBean> getInstitucionesConsejo (String sIdConsejo) throws ClsExceptions {
		String sql = "SELECT " + CenInstitucionBean.C_IDINSTITUCION + ", " +
				CenInstitucionBean.C_ABREVIATURA +
			" FROM " + CenInstitucionBean.T_NOMBRETABLA + 
			" WHERE " +  CenInstitucionBean.C_IDINSTITUCION + " <> " + ClsConstants.INSTITUCION_CONSEJOGENERAL +
				" AND " +  CenInstitucionBean.C_IDINSTITUCION + " < " + ClsConstants.INSTITUCION_CONSEJO +
				" AND " +  CenInstitucionBean.C_FECHAENPRODUCCION + " IS NOT NULL " +
				" CONNECT BY PRIOR " +  CenInstitucionBean.C_IDINSTITUCION + " = " +  CenInstitucionBean.C_CEN_INST_IDINSTITUCION + 
				" START WITH " +  CenInstitucionBean.C_IDINSTITUCION + " = " + sIdConsejo +
			" ORDER BY 2";
		
		List<CenInstitucionBean> aInstituciones = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.query(sql)) {
            	aInstituciones = new ArrayList<CenInstitucionBean>();       
            	
            	CenInstitucionBean institucionBean = new CenInstitucionBean();
            	institucionBean.setIdInstitucion(-1);
        		institucionBean.setAbreviatura(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));
        		aInstituciones.add(institucionBean);
    			
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		institucionBean = new CenInstitucionBean();            		
            		institucionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, CenInstitucionBean.C_IDINSTITUCION));
            		institucionBean.setAbreviatura(UtilidadesHash.getString(htFila, CenInstitucionBean.C_ABREVIATURA));
            		aInstituciones.add(institucionBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return aInstituciones;
	}
	
	public CenInstitucionBean getInstitucion (String idInstitucion) throws Exception
	{
		CenInstitucionBean b= null;
		try {
			
			Hashtable datos = new Hashtable();
			datos.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
			
			Vector v = this.selectByPK(datos);
			if (v!=null && v.size()>0) {
				b = (CenInstitucionBean) v.get(0);
				
			}
			return b;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public List<CenInstitucionBean> getNombreColegiosTodos (String sIdColegio) throws ClsExceptions {		
		String sql = "select " + CenInstitucionBean.C_IDINSTITUCION +","+CenInstitucionBean.C_ABREVIATURA +
				" FROM "+ CenInstitucionBean.T_NOMBRETABLA +  
				" WHERE " +  CenInstitucionBean.C_IDINSTITUCION + " <> " + ClsConstants.INSTITUCION_CONSEJOGENERAL +
				" connect by prior idinstitucion=cen_inst_idinstitucion start with idinstitucion= "+sIdColegio+ "ORDER BY 2";
		List<CenInstitucionBean> aInstituciones = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.query(sql)) {
            	aInstituciones = new ArrayList<CenInstitucionBean>();       
            	
            	CenInstitucionBean institucionBean = new CenInstitucionBean();
            	institucionBean.setIdInstitucion(-1);	
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		institucionBean = new CenInstitucionBean();            		
            		institucionBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, CenInstitucionBean.C_IDINSTITUCION));
            		institucionBean.setAbreviatura(UtilidadesHash.getString(htFila, CenInstitucionBean.C_ABREVIATURA));
            		aInstituciones.add(institucionBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return aInstituciones;
	}
	
}