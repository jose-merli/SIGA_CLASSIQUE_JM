package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

// Clase: ScsRetencionesAdm 
// Autor: julio.vicente@atosorigin.com
// Ultima modificación: 20/12/2004
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...  
 */
public class ScsRetencionesAdm extends MasterBeanAdministrador {
		
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsRetencionesAdm(UsrBean usuario) {
		super(ScsRetencionesBean.T_NOMBRETABLA, usuario);		
	}
	
    /**
	 * Efectúa un SELECT en la tabla SCS_RETENCIONES con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector();		
				
		try { 			 		
			String where = " WHERE " + ScsRetencionesBean.C_IDRETENCION + " = " + hash.get(ScsRetencionesBean.C_IDRETENCION);		
			datos = this.select(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}
	public ScsRetencionesBean getRetencion(Hashtable htPkTabl,String lenguaje) throws ClsExceptions {
		ScsRetencionesBean beanRetencion = null;
		
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador = 0;
		String idRetencion = (String)htPkTabl.get(ScsRetencionesBean.C_IDRETENCION);
		sql.append(" SELECT ");
		sql.append(" F_SIGA_GETRECURSO(");
		sql.append(ScsRetencionesBean.C_DESCRIPCION);
		sql.append(",");
		sql.append(lenguaje);
		sql.append(") ");
		sql.append(ScsRetencionesBean.C_DESCRIPCION);
		sql.append(" , LETRANIFSOCIEDAD, RETENCION  "); 
		sql.append(" FROM SCS_MAESTRORETENCIONES  WHERE ");
		sql.append(ScsRetencionesBean.C_IDRETENCION);
		sql.append(" = :");
		contador++;
		htCodigos.put(new Integer(contador), idRetencion);
		sql.append(contador);
		
		   
		try {
						
			
			Vector datos = this.selectGenericoBind(sql.toString(),htCodigos);
			//como es por PK
			Hashtable row = (Hashtable)datos.get(0);
			String descripcion = (String)row.get(ScsRetencionesBean.C_DESCRIPCION);
			String letra = (String)row.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD);
			String retencion = (String)row.get(ScsRetencionesBean.C_RETENCION);
			beanRetencion = new ScsRetencionesBean();
			beanRetencion.setIdRetencion(new Integer(idRetencion));
			beanRetencion.setDescripcion(descripcion);
			beanRetencion.setLetraNifSociedad(letra);
			beanRetencion.setRetencion(new Float(retencion).floatValue());
			
			
			

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return beanRetencion;
	}

	public String getRetencionPorDefecto() throws ClsExceptions, SIGAException {
		ScsRetencionesBean beanRetencion = null;
		
		String sql = new String();
		String idRetencion = null;
		sql+=" SELECT ";
		sql+=" IDRETENCION "; 
		sql+=" FROM SCS_MAESTRORETENCIONES  WHERE ";
		sql+=" (letranifsociedad is null or letranifsociedad = '') AND ";
		sql+=ScsRetencionesBean.C_PORDEFECTO;
		sql+=" = '1'";
		
		   

			Vector datos;
			try {
				datos = this.selectGenerico(sql);
			} catch (SIGAException e) {
				throw new SIGAException ("Error BBDD");
			}
			//como es por PK
			if(datos.size()==1){
				Hashtable row = (Hashtable)datos.get(0);
				//String descripcion = (String)row.get(ScsRetencionesBean.C_DESCRIPCION);
				//String letra = (String)row.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD);
				idRetencion = (String)row.get(ScsRetencionesBean.C_IDRETENCION);
			}else{
				if(datos.size()==0)
					throw new SIGAException("messages.irpfxdef.error",new String[] {"modulo.censo"});
					//throw new SIGAException("Error al no existir un irpf por defecto");
				if(datos.size()>1)
					throw new SIGAException("messages.irpfxdef2.error",new String[] {"modulo.censo"});
					//throw new SIGAException("Error al existir mas de un irpf por defecto");
			
			}	
		
		return idRetencion;
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_MAESTRORETENCIONES con los datos de selección intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT 
	 */
	public Vector select(Hashtable miHash, String lenguaje) throws ClsExceptions {
	
		Vector datos = new Vector();
		String sql="SELECT " + ScsRetencionesBean.C_IDRETENCION + ", " +
				" F_SIGA_GETRECURSO(" + ScsRetencionesBean.C_DESCRIPCION + ", " + lenguaje + ") DESCRIPCION, " +
				ScsRetencionesBean.C_LETRANIFSOCIEDAD + ", " +
				ScsRetencionesBean.C_RETENCION + ", " +
				ScsRetencionesBean.C_FECHAMODIFICACION + ", " +
				ScsRetencionesBean.C_USUMODIFICACION +
		   " FROM " + ScsRetencionesBean.T_NOMBRETABLA;
		
		try { 
			// Se comprueba si se ha introducido algún criterio de búsqueda ya que sino es así no se creará el where			
			if ((((String)miHash.get(ScsRetencionesBean.C_IDRETENCION)) != null  && ((String)miHash.get(ScsRetencionesBean.C_IDRETENCION)).length() > 0) ||
			    (((String)miHash.get(ScsRetencionesBean.C_DESCRIPCION)) != null  && ((String)miHash.get(ScsRetencionesBean.C_DESCRIPCION)).length() > 0) ||
			    (((String)miHash.get(ScsRetencionesBean.C_RETENCION)) != null  && ((String)miHash.get(ScsRetencionesBean.C_RETENCION)).length() > 0) ||
			    (((String)miHash.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)) != null  && ((String)miHash.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)).length() > 0)) {
			
				sql += " WHERE ";
				
				// Por cada criterio de búsqueda, si se ha introducido, se va anhadiendo al string where esa restricción en 
				// la búsqueda.
				if ((String)miHash.get(ScsRetencionesBean.C_IDRETENCION) != null && ((String)miHash.get(ScsRetencionesBean.C_IDRETENCION)).length() > 0) {
					sql += ScsRetencionesBean.C_IDRETENCION + " = " + Integer.parseInt((String)miHash.get(ScsRetencionesBean.C_IDRETENCION));
					
				} else {
					boolean bwhere = false;
					if (((String)miHash.get(ScsRetencionesBean.C_DESCRIPCION) != null) && ((String)miHash.get(ScsRetencionesBean.C_DESCRIPCION)).length()>0) {
						
						sql +=  ComodinBusquedas.prepararSentenciaCompletaTranslateUpper(((String)miHash.get(ScsRetencionesBean.C_DESCRIPCION)).trim(), "F_SIGA_GETRECURSO(" + ScsRetencionesBean.C_DESCRIPCION + ", " + lenguaje + ")");
						bwhere = true;
					} 
					
					if (((String)miHash.get(ScsRetencionesBean.C_RETENCION)).length() > 0) 	{
						if (bwhere) {
							sql += " AND ";							
						} else {
							bwhere = true;
						}
						
						sql += ScsRetencionesBean.C_RETENCION + " = " + Float.parseFloat((String)miHash.get(ScsRetencionesBean.C_RETENCION));
					}
					
					if (((String)miHash.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)).length() > 0 ){
						if (bwhere){
							sql += " AND ";
						}
						sql += " UPPER (" + ScsRetencionesBean.C_LETRANIFSOCIEDAD + ") = '" + ((String)miHash.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)).toUpperCase() + "'";
					}
				}		
			}		
			
			sql += " ORDER BY PORDEFECTO  DESC, NLSSORT(DESCRIPCION, 'NLS_SORT=BINARY')";
			datos = this.selectGenerico(sql);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR");
		}	
		return datos;	
	}
	
	public Integer getNuevoIdRetencion() throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsRetencionesBean.C_IDRETENCION+")+1 AS ID FROM "+ScsRetencionesBean.T_NOMBRETABLA;
			
			datos = this.selectGenerico(select);
			String id = (String)((Hashtable)datos.get(0)).get("ID");
			
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
	
	 public List<ScsRetencionesBean> getRetenciones(String where,String idLenguaje)throws ClsExceptions{
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT IDRETENCION,  ");
			sql.append("  F_SIGA_GETRECURSO(DESCRIPCION, ");
			sql.append(idLenguaje);
			sql.append(") DESCRIPCION ");
			sql.append(" , LETRANIFSOCIEDAD, RETENCION, FECHAMODIFICACION, USUMODIFICACION   FROM SCS_MAESTRORETENCIONES "); 
			if(where!=null)
				sql.append(where);
			

			sql.append(" ORDER BY PORDEFECTO  DESC, DESCRIPCION ");
			
			List<ScsRetencionesBean> alRetenciones = null;
			try {
				RowsContainer rc = new RowsContainer(); 
													
	            if (rc.find(sql.toString())) {
	            	alRetenciones = new ArrayList<ScsRetencionesBean>();
	            	ScsRetencionesBean retencionBean = null;
      	
	            	
	    			for (int i = 0; i < rc.size(); i++){
	            		Row fila = (Row) rc.get(i);
	            		Hashtable<String, Object> htFila=fila.getRow();
	            		retencionBean = (ScsRetencionesBean) this.hashTableToBean(htFila);
	            		alRetenciones.add(retencionBean);
	            	}
	            } 
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return alRetenciones;
			
		} 
	 
	 
	
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la retención que se va a insertar. También se comprueba que no halla repeticiones en el campo
	 * letraNifSociedad
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
			
		RowsContainer rc =  new RowsContainer(); ;
		 
			
		/* Se consulta en la base de datos el número de registros existentes con la "letraNifSociedad" que se quiere insertar. Si existe algún registro ya
		 * con ese valor, no se puede realizar la inserción.
		 */		
		 String sql ="SELECT COUNT (*) AS NUMELEM FROM " + nombreTabla + " WHERE LETRANIFSOCIEDAD LIKE '" + ((String)entrada.get(ScsRetencionesBean.C_LETRANIFSOCIEDAD)).toUpperCase() +"'";	
		
		try {		
			rc.query(sql);			
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (Integer.parseInt(prueba.get("NUMELEM").toString()) > 0) entrada.put(ScsRetencionesBean.C_IDRETENCION,"-1");

		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE NÚMERO DE REGISTROS");
		};	
	
		return entrada;
	}
	
	/**
	 * Efectúa un INSERT en la tabla SCS_MAESTRORETENCIONES con los datos intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return boleano que indica si la inserción fue correcta o no. 
	 */
	public boolean insert(Hashtable miHash) throws ClsExceptions{	
		
		// Si el identificador de retención es menor que 0 no se puede realizar la inserción puesto que eso significa
		// que ya existe una retención con esa "Letra de NIF/Sociedad". En prepararInsert se almacena -1 en el valor
		// del identificador para indicar esta circunstancia.
		try {
			if (Integer.parseInt((String)miHash.get(ScsRetencionesBean.C_IDRETENCION)) >= 0) {	
				return super.insert(miHash);		
			}		
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN INSERCIÓN");
		}
		return false;
	}
	
	/**
	 * Efectúa un DELETE en la tabla SCS_MAESTRORETENCIONES del registro seleccionado 
	 * 
	 * @param hash Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return boleano que indica si la inserción fue correcta o no. 
	 */
	public boolean delete(Hashtable hash) throws ClsExceptions{
	
		try {
			Row row = new Row();	
			row.load(hash);
	
			String [] campos = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, campos) == 1) {
				return true;
			}		
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BORRADO");
		}
		return false;
	}	
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	public String[] getCamposBean() {
		String[] campos= {	ScsRetencionesBean.C_IDRETENCION, 
							ScsRetencionesBean.C_DESCRIPCION, 
							ScsRetencionesBean.C_LETRANIFSOCIEDAD, 
							ScsRetencionesBean.C_RETENCION,
							ScsRetencionesBean.C_FECHAMODIFICACION,
							ScsRetencionesBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	public String[] getClavesBean() {
		String[] campos= {ScsRetencionesBean.C_IDRETENCION};
		return campos;
	}
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsRetencionesBean bean = null;
		
		try {
			bean = new ScsRetencionesBean();		
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsRetencionesBean.C_DESCRIPCION));				
			bean.setIdRetencion(UtilidadesHash.getInteger(hash,ScsRetencionesBean.C_IDRETENCION));
			bean.setLetraNifSociedad(UtilidadesHash.getString(hash,ScsRetencionesBean.C_LETRANIFSOCIEDAD));		
			bean.setRetencion(Float.parseFloat((String)hash.get(ScsRetencionesBean.C_RETENCION)));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsRetencionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsRetencionesBean.C_USUMODIFICACION));		
		}		
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}		
		return bean;
	
	}
	
	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsRetencionesBean b = (ScsRetencionesBean) bean;
			htData.put(ScsRetencionesBean.C_IDRETENCION, String.valueOf(b.getIdRetencion()));
			htData.put(ScsRetencionesBean.C_RETENCION, String.valueOf(b.getRetencion()));
			htData.put(ScsRetencionesBean.C_LETRANIFSOCIEDAD, b.getLetraNifSociedad());
			htData.put(ScsRetencionesBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(ScsRetencionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(ScsRetencionesBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {
		
		//String[] campos= {	ScsRetencionesBean.C_RETENCION};	
		String[] campos= {	ScsRetencionesBean.C_DESCRIPCION};
		return campos;
	}
}