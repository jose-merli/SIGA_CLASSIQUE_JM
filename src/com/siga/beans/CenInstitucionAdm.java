
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

/**
* @author ruben.fernandez
*/

public class CenInstitucionAdm extends MasterBeanAdministrador {
    public static final String K_SIN_SIGA="2";

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
			bean.setIdPersona(UtilidadesHash.getInteger(hash,CenInstitucionBean.C_IDPERSONA));
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
	 * Obtiene el tipo de institución pasado como parámetro
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
//			throw new ClsExceptions (e, "Error al recuperar el tipo de institución");
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
	public String getDomicilioInstitucion(String idInstitucion)
			throws ClsExceptions,SIGAException {
		
		String nombreInstitucion="";
		RowsContainer rows=new RowsContainer();
        String sql="select DOMICILIO || ' (' || CODIGOPOSTAL || ')' AS DOMICILIO from cen_direcciones " +
           " where idpersona=(SELECT I.IDPERSONA FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION=" + idInstitucion + ") " +
           " and preferente like '%C%' " + 
           " and idinstitucion=" + idInstitucion;
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será null si no existe padre
            nombreInstitucion = (String)htRow.get("DOMICILIO");
        }
 
		return nombreInstitucion;
	}

	/** 
	 * Funcion que devuelve el Poblacion de la Institucion'
	 * @param  String idInstitucion 
	 * @return String nombreInstitucion
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getPoblacionInstitucion(String idInstitucion)
			throws ClsExceptions,SIGAException {
		
		String nombreInstitucion="";
		RowsContainer rows=new RowsContainer();
        String sql="select (SELECT P.NOMBRE FROM CEN_POBLACIONES P WHERE P.IDPOBLACION=cen_direcciones.IDPOBLACION) AS POBLACION  from cen_direcciones " +
           " where idpersona=(SELECT I.IDPERSONA FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION=" + idInstitucion + ") " +
           " and preferente like '%C%' " + 
           " and idinstitucion=" + idInstitucion;
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será null si no existe padre
            nombreInstitucion = (String)htRow.get("POBLACION");
        }
 
		return nombreInstitucion;
	}

	
	/** 
	 * Funcion que devuelve el identificador de la persona
	 * @param  String idInstitucion 
	 * @return String ident persona
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getIdPersona(String idInstitucion)
			throws ClsExceptions,SIGAException {
		
		String idPersona="";
		Hashtable hash = new Hashtable();
		try{
			hash.put(CenInstitucionBean.C_IDINSTITUCION,idInstitucion);
			Vector vInstitucion = this.select(hash);
			CenInstitucionBean instBean = (CenInstitucionBean)vInstitucion.elementAt(0);
			idPersona=instBean.getIdPersona().toString();
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}catch(Exception e){
			throw new ClsExceptions (e,"Elemento nulo");
		}
		return idPersona;
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
            // El valor devuelto será null si no existe padre
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
	
	public boolean tieneProductoComision(String idInstitucion) throws ClsExceptions, SIGAException {
		boolean salida = true;
		PysProductosInstitucionAdm pi = new PysProductosInstitucionAdm(this.usrbean); 
		Vector v = pi.getProductosComisiones(idInstitucion);
		if (v==null || v.size()==0) {
			salida = false;
		}
		return salida;
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
}