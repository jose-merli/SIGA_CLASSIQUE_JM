/*
 * VERSIONES:
 * 
 * daniel.casla - 04-11-2004 - Creacion
 * miguel.villegas - 05-12-2004 - Funciones de accesos a BBDDs y relacionadas
 * Luis Miguel Sánchez PIÑA - 02/03/2005 - Cambio un par de métodos dentro del hashTableToBean.
 *	
 */
package com.siga.beans;

import java.io.File;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.certificados.Certificado;
import com.siga.general.Articulo;
import com.siga.general.SIGAException;
/**
*
* Clase que gestiona la tabla PYS_PRODUCTOSINSTITUCION de la BBDD
* 
*/
public class PysProductosInstitucionAdm extends MasterBeanAdministrador 
{
	public static String TIPO_CERTIFICADO_CERTIFICADO      = "C";
	public static String TIPO_CERTIFICADO_COMUNICACION     = "M";
	public static String TIPO_CERTIFICADO_DILIGENCIA       = "D";
	public static String TIPO_CERTIFICADO_COMISIONBANCARIA = "B";
	public static String TIPO_CERTIFICADO_GRATUITO         = "G";
	
	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysProductosInstitucionAdm(UsrBean usu) {
		super(PysProductosInstitucionBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysProductosInstitucionBean.C_IDINSTITUCION,
							PysProductosInstitucionBean.C_IDTIPOPRODUCTO ,
							PysProductosInstitucionBean.C_IDPRODUCTO,
							PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,
							PysProductosInstitucionBean.C_DESCRIPCION,
							PysProductosInstitucionBean.C_CUENTACONTABLE,
							PysProductosInstitucionBean.C_VALOR,
							PysProductosInstitucionBean.C_PORCENTAJEIVA,
							PysProductosInstitucionBean.C_MOMENTOCARGO,
							PysProductosInstitucionBean.C_SOLICITARBAJA,
							PysProductosInstitucionBean.C_SOLICITARALTA,
							PysProductosInstitucionBean.C_IDIMPRESORA,
							PysProductosInstitucionBean.C_IDPLANTILLA,
							PysProductosInstitucionBean.C_IDCONTADOR,
							PysProductosInstitucionBean.C_TIPOCERTIFICADO,
							PysProductosInstitucionBean.C_FECHABAJA,
							PysProductosInstitucionBean.C_SUFIJO,
							PysProductosInstitucionBean.C_FECHAMODIFICACION,
							PysProductosInstitucionBean.C_NOFACTURABLE,
							PysProductosInstitucionBean.C_USUMODIFICACION};
		return campos;
	}
	
		public Vector getDiligenciasConsejo(String idinstitucion) throws ClsExceptions {
	    
	    Vector resultado = new Vector();
	    
	    Hashtable htCampos = new Hashtable();
	    htCampos.put(PysProductosInstitucionBean.C_IDINSTITUCION,idinstitucion);
	    htCampos.put(PysProductosInstitucionBean.C_TIPOCERTIFICADO,PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA);
	    htCampos.put(PysProductosInstitucionBean.C_FECHABAJA,"");
	    
	    try {
            resultado = this.select(htCampos);
        } catch (ClsExceptions e) {
            throw new ClsExceptions(e,"Error al obtener la informacion sobre diligencias de un consejo.");
        }
	    
	    return resultado;
	}
	
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysProductosInstitucionBean.C_IDINSTITUCION,
							PysProductosInstitucionBean.C_IDTIPOPRODUCTO ,
							PysProductosInstitucionBean.C_IDPRODUCTO,
							PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysProductosInstitucionBean bean = null;
		
		try {
			bean = new PysProductosInstitucionBean();
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,PysProductosInstitucionBean.C_IDINSTITUCION));
			bean.setIdTipoProducto (UtilidadesHash.getInteger(hash,PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto (UtilidadesHash.getLong(hash,PysProductosInstitucionBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion (UtilidadesHash.getLong(hash,PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
			bean.setDescripcion (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_DESCRIPCION ));
			bean.setCuentacontable (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_CUENTACONTABLE ));
			// Esto no ha podido funcionar nunca, pero bueno, lo comento por si acaso.
			//bean.setValor ((Double)hash.get(PysProductosInstitucionBean.C_VALOR));
			bean.setValor (UtilidadesHash.getDouble(hash,PysProductosInstitucionBean.C_VALOR));
			// Esto no ha podido funcionar nunca, pero bueno, lo comento por si acaso.
			//bean.setPorcentajeIva ((Float)hash.get(PysProductosInstitucionBean.C_PORCENTAJEIVA ));
			bean.setPorcentajeIva (UtilidadesHash.getFloat(hash,PysProductosInstitucionBean.C_PORCENTAJEIVA));
			bean.setMomentoCargo (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_MOMENTOCARGO ));
			bean.setSolicitarBaja (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_SOLICITARBAJA ));
			bean.setSolicitarAlta (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_SOLICITARALTA));
			bean.setIdImpresora (UtilidadesHash.getLong(hash,PysProductosInstitucionBean.C_IDIMPRESORA ));
			bean.setIdPlantilla (UtilidadesHash.getLong(hash,PysProductosInstitucionBean.C_IDPLANTILLA ));
			bean.setTipoCertificado (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_TIPOCERTIFICADO ));
			bean.setFechaBaja(UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_FECHABAJA));
			bean.setSufijo(UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_SUFIJO));
			bean.setIdContador(UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_IDCONTADOR));
			bean.setFechaMod(UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysProductosInstitucionBean.C_USUMODIFICACION));			
			bean.setnoFacturable (UtilidadesHash.getString(hash,PysProductosInstitucionBean.C_NOFACTURABLE ));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			PysProductosInstitucionBean b = (PysProductosInstitucionBean) bean;
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDINSTITUCION,b.getIdInstitucion ());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDPRODUCTO,b.getIdProducto());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,b.getIdProductoInstitucion());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_DESCRIPCION ,b.getDescripcion());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_CUENTACONTABLE ,b.getCuentacontable());
			htData.put(PysProductosInstitucionBean.C_VALOR,b.getValor());
			htData.put(PysProductosInstitucionBean.C_PORCENTAJEIVA ,b.getPorcentajeIva());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_MOMENTOCARGO ,b.getMomentoCargo());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_SOLICITARBAJA ,b.getSolicitarBaja());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_SOLICITARALTA,b.getSolicitarAlta());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDIMPRESORA ,b.getIdImpresora ());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDPLANTILLA ,b.getIdPlantilla());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_TIPOCERTIFICADO,b.getTipoCertificado());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_SUFIJO, b.getSufijo());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_IDCONTADOR, b.getIdContador());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData,PysProductosInstitucionBean.C_NOFACTURABLE, b.getnoFacturable());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Prepara una tabla para insertarla en la tabla. <br/>
	 * Obtieneel campos IDPRODUCTOIINSTITUCION y los campos SOLICITARBAJA, <br/>
	 * SOLICITARALTA en caso de que no estuviesen "marcados" en el formulario
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions, SIGAException 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDPRODUCTOINSTITUCION) + 1) AS IDPRODUCTOINSTITUCION FROM " + nombreTabla +
					" WHERE " +
					PysProductosInstitucionBean.C_IDINSTITUCION + "=" + (String)entrada.get(PysProductosInstitucionBean.C_IDINSTITUCION) + 
					" AND " +
					PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + (String)entrada.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO) + 					
					" AND " +
					PysProductosInstitucionBean.C_IDPRODUCTO + "=" + (String)entrada.get(PysProductosInstitucionBean.C_IDPRODUCTO);
										
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPRODUCTOINSTITUCION").equals("")) {
					entrada.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,"1");
				}
				else entrada.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,(String)prueba.get("IDPRODUCTOINSTITUCION"));								
			}
			if (!entrada.containsKey("SOLICITARBAJA")) {
				entrada.put(PysProductosInstitucionBean.C_SOLICITARBAJA,"0");
			}
			if (!entrada.containsKey("SOLICITARALTA")) {
				entrada.put(PysProductosInstitucionBean.C_SOLICITARALTA,"0");
			}
			if (!entrada.containsKey("NOFACTURABLE")) {
				entrada.put(PysProductosInstitucionBean.C_NOFACTURABLE,ClsConstants.DB_FALSE);
			}else {
				entrada.put(PysProductosInstitucionBean.C_NOFACTURABLE,ClsConstants.DB_TRUE);
			}
		}	
		catch (ClsExceptions e) {		
			throw e;		
		}
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
       			throw new ClsExceptions(e,"Error al ejecutar el 'prepararInsert' en B.D.");
       		}	
	    }
		
		return entrada;
	}

	/** 
	 * Recoge informacion de la tabla a partirde la descripcion del producto<br/>
	 * y la forma de pago de este <br/>
	 * @param  desProducto - descripcion del producto 
	 * @param  idForma de pago - descripcion del producto	 * 
	 * @return  Vector - Filas de tabla seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public Vector obtenerProductosInstitucion (String tipo, String categoria, String descripcion, String institucion, String pago, String estado) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDPRODUCTO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDCONTADOR  + "," +
				            PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_DESCRIPCION  + "," +
	            			"F_siga_formatonumero("+PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_VALOR+",2)"  + " VALOR," +
	            			//PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_NOFACTURABLE  + " FACTURABLE," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_PORCENTAJEIVA  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_FECHABAJA  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_TIPOCERTIFICADO  + "," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.T_NOMBRETABLA +"." + PysTiposProductosBean.C_DESCRIPCION,this.usrbean.getLanguage())  + " AS TIPO," +
	            			PysProductosBean.T_NOMBRETABLA +"." + PysProductosBean.C_DESCRIPCION  + " AS CATEGORIA," +
	            			PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_DESCRIPCION + " AS IVA" + 
							" FROM " + 
							PysProductosInstitucionBean.T_NOMBRETABLA + 
							" INNER JOIN "+ 
							PysTiposProductosBean.T_NOMBRETABLA +
							" ON "+
								PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" +
								PysTiposProductosBean.T_NOMBRETABLA +"."+ PysTiposProductosBean.C_IDTIPOPRODUCTO +
							" INNER JOIN "+ 
							PysProductosBean.T_NOMBRETABLA +
							" ON " +
								PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" +
								PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDTIPOPRODUCTO +						
								" AND " +
								PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" +
								PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDPRODUCTO +
								
								" AND " +
								PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" +
								PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDINSTITUCION +
								
							" INNER JOIN "+ 
							PysTipoIvaBean.T_NOMBRETABLA +
							" ON "+
								PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_PORCENTAJEIVA + "=" +
								PysTipoIvaBean.T_NOMBRETABLA +"."+ PysTipoIvaBean.C_IDTIPOIVA;
	            
	    			// Si se empleo la forma de pago como parametro de busqueda
					if (!pago.trim().equalsIgnoreCase("")){
						sql +=" INNER JOIN "+ 
									PysFormaPagoProductoBean.T_NOMBRETABLA +
									" ON " +
										PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" +
										PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDTIPOPRODUCTO +						
										" AND " +
										PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" +
										PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTO +
										" AND " +
										PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" +
										PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDINSTITUCION +
										" AND " +
										PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + "=" +
										PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION;
					}	            
	            
					sql +=  " WHERE " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + institucion;
					
							if ((estado != null ) && (!estado.trim().equalsIgnoreCase(""))) {
								if (estado.equalsIgnoreCase("ALTA")) {
									sql += " AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_FECHABAJA + " IS NULL ";									
								}
								if (estado.equalsIgnoreCase("BAJA")) {
									sql += " AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_FECHABAJA + " IS NOT NULL ";									
								}
							}
	            			
	            			// Si se empleo el tipo como parametro de busqueda
							if (!tipo.trim().equalsIgnoreCase("")){
								sql +=" AND " + PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + tipo;
							}
							
	            			// Si se empleo la categoria como parametro de busqueda
							if (!categoria.trim().equalsIgnoreCase("")){
								sql +=" AND " + PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" + categoria;
							}						

	            			// Si se empleo el nombre como parametro de busqueda
							if (!descripcion.trim().equals("")){
								sql +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(),PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION); 
								
							}
							
	            			// Si se empleo la forma de pago como parametro de busqueda
							if (!pago.trim().equalsIgnoreCase("")){
								sql +=" AND " + PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDFORMAPAGO + "=" + pago;
							}							
							
	            			// Ordenado por...						
	            			sql += " ORDER BY " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION; 
	            
	            if (rc.findNLS(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  //ClsLogging.writeFileLogWithoutSession("Precio devuelto de la consulta de productos "+fila.getString("VALOR"),10);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener productos de la institucion.");
	       		}	
		   }
	       return datos;                        
	    }

	/** 
	 * Recoge toda informacion del del registro seleccionado a partir de sus claves<br/>
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoProd - identificador del tipo de producto
	 * @param  idProd - identificador del producto	 
	 * @param  idProdInst - identificador del producto institucion	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector obtenerInfoProducto (String idInst, String idTipoProd, String idProd, String idProdInst) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDCONTADOR  + "," +
							PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_FECHAMODIFICACION + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_CUENTACONTABLE + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_VALOR + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_PORCENTAJEIVA + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_MOMENTOCARGO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_FECHABAJA + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_SOLICITARBAJA + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_SOLICITARALTA + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_TIPOCERTIFICADO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_NOFACTURABLE + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_SUFIJO + "," +
	            			PysProductosBean.T_NOMBRETABLA + "." + PysProductosBean.C_DESCRIPCION + " AS CATEGORIA," +
	            			PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_VALOR + " AS VALORIVA," +
	            			UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.T_NOMBRETABLA + "." + PysTiposProductosBean.C_DESCRIPCION,this.usrbean.getLanguage()) + " AS TIPO, " +
							/*PDM: INC-2763, no se recuperaba el tipo de certificado (comisión bancaria ...)*/
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_TIPOCERTIFICADO +
							/**/
							" FROM " + PysProductosInstitucionBean.T_NOMBRETABLA + "," + PysProductosBean.T_NOMBRETABLA + "," +PysTiposProductosBean.T_NOMBRETABLA +  "," +PysTipoIvaBean.T_NOMBRETABLA +
							" WHERE " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDTIPOPRODUCTO +
							" AND " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" + PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDPRODUCTO +
							" AND " +							
							PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDTIPOPRODUCTO + "=" + PysTiposProductosBean.T_NOMBRETABLA +"."+ PysTiposProductosBean.C_IDTIPOPRODUCTO +

							" AND " +							
							PysProductosBean.T_NOMBRETABLA +"."+ PysProductosBean.C_IDINSTITUCION + "=" + PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION+
							
							" AND " +							
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + idInst +
							" AND " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + idTipoProd +
	            			" AND " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" + idProd +
	            			" AND " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + "=" + idProdInst
	            			+" AND "+PysTipoIvaBean.T_NOMBRETABLA+".IDTIPOIVA = "+ PysProductosInstitucionBean.T_NOMBRETABLA + "." +PysProductosInstitucionBean.C_PORCENTAJEIVA;
							// Ordenado por...						
							sql += " ORDER BY " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION; 
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener la informacion sobre un producto.");
	       		}	
		   }
	       return datos;                        
	    }

	/** 
	 * Recoge las formas de pago relacionadas con un determinado registro por sus claves <br/>
	 * y su atributo Internet
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoProd - identificador del tipo de producto
	 * @param  idProd - identificador del producto	 
	 * @param  idProdInst - identificador del producto institucion	  
	 * @param  internet - descripcion de la forma de pago 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector obtenerFormasPago (String idInst, String idTipoProd, String idProd, String idProdInst, String internet) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION  + "," +
	            			PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDFORMAPAGO + "," +
							PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_FECHAMODIFICACION + 							
							" FROM " + PysProductosInstitucionBean.T_NOMBRETABLA + ", " + PysFormaPagoProductoBean.T_NOMBRETABLA + 
							" WHERE " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + idInst +
							" AND " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + idTipoProd +
	            			" AND " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" + idProd +
	            			" AND " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + "=" + idProdInst +
	            			" AND " +							
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDINSTITUCION +
							" AND " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDTIPOPRODUCTO  +
	            			" AND " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTO + "=" + PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDPRODUCTO  +
	            			" AND " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + "=" + PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION +
	            			" AND " +
	            			PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_INTERNET + "=" + "'" + internet + "'";							
							// Ordenado por...						
							sql += " ORDER BY " + PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_INTERNET; 
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener las formas de pago relacionadas.");
	       		}	
		   }
	       return datos;                        
	    }	
	
	/** 
	 * Recoge informacion de la tabla a partirde la descripcion del producto<br/>
	 * la forma de pago de este y la descripcion del modo de pago <br/>
	 * @param  desProducto - descripcion del producto
	 * @param  idForma de pago - descripcion del producto	
	 * @param  internet - descripcion del modo de pago 
	 * @return  Vector - Filas de tabla seleccionadas  
	 * @exception  ClsExceptions, SIGAException
	 */	
	public Vector getProductos(Integer desInstitucion, String desProducto, String idFormPago) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDPRODUCTO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION  + "," +
							PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_FECHAMODIFICACION + "," +
							PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_FECHABAJA + "," +
							PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_IDCONTADOR + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION + 
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_TIPOCERTIFICADO + 
							" FROM " + PysProductosInstitucionBean.T_NOMBRETABLA + ", " +
							PysFormaPagoProductoBean.T_NOMBRETABLA + 
							" WHERE " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDINSTITUCION +
							" AND " +							
	            			PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + desInstitucion;
							if (!desProducto.trim().equals("")){
								sql +=" AND UPPER(" +
									PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION + ") like'" + UtilidadesString.parseLike(desProducto) + "' ";
							}
							if (!idFormPago.trim().equals("")){
								sql+=" AND  " +
									PysFormaPagoProductoBean.T_NOMBRETABLA + "." + PysFormaPagoProductoBean.C_IDFORMAPAGO + "=" + idFormPago;
							}
							
							sql += " ORDER BY " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION; 										
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener productos de la institucion.");
	       		}	
		   }
	       return datos;                        
	    }
	
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		//return this.getClavesBean();
		return new String[] {PysProductosInstitucionBean.C_DESCRIPCION};
	}
	

	/**
	 * Obtiene los datos de un producto
	 * @param idInstitucion
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param idTipo
	 * @return un hashtable con los datos
	 * @throws ClsExceptions, SIGAException
	 */
	public Hashtable getProducto (Integer idInstitucion, Long idProducto, Long idProductoInstitucion, Integer idTipo) throws ClsExceptions, SIGAException
	{
		try{
			String select = "SELECT " +	
						UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.T_NOMBRETABLA + "." + PysTiposProductosBean.C_DESCRIPCION,this.usrbean.getLanguage()) + " AS DESCRIPCION_TIPO, " + 
						PysProductosBean.T_NOMBRETABLA + "." + PysProductosBean.C_DESCRIPCION + " AS DESCRIPCION_PRODUCTO, " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION + " AS DESCRIPCION_P_INSTITUCION, " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_VALOR + ", " +
						//PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_NOFACTURABLE + ", " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDCONTADOR + ", " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_PORCENTAJEIVA + ", " +
						PysTipoIvaBean.T_NOMBRETABLA + "." + PysTipoIvaBean.C_VALOR + " AS VALORIVA," +
						
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_MOMENTOCARGO + ", " +
						PysProductosInstitucionBean.T_NOMBRETABLA +"." + PysProductosInstitucionBean.C_FECHABAJA + ", " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_TIPOCERTIFICADO;

			String from = " FROM " + 
						PysProductosInstitucionBean.T_NOMBRETABLA + ", " + PysProductosBean.T_NOMBRETABLA + ", " + PysTiposProductosBean.T_NOMBRETABLA +  "," +PysTipoIvaBean.T_NOMBRETABLA ;
		
			String where = " WHERE " + 
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + idInstitucion + " AND " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + " = " + idProducto + " AND " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO + " = " + idTipo + " AND " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion + " AND " +
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + " = " + PysProductosBean.T_NOMBRETABLA + "." + PysProductosBean.C_IDPRODUCTO + " AND " + 
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO + " = " + PysProductosBean.T_NOMBRETABLA + "." + PysProductosBean.C_IDTIPOPRODUCTO + " AND " +
						
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + PysProductosBean.T_NOMBRETABLA + "." + PysProductosBean.C_IDINSTITUCION + " AND " +
						
						PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO + " = " + PysTiposProductosBean.T_NOMBRETABLA + "." +  PysTiposProductosBean.C_IDTIPOPRODUCTO
						+" AND "+PysTipoIvaBean.T_NOMBRETABLA+".IDTIPOIVA = "+ PysProductosInstitucionBean.T_NOMBRETABLA + "." +PysProductosInstitucionBean.C_PORCENTAJEIVA;
			
		
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			String sql = select + from + where;
			if (rc.query(sql)) {
				if (rc.size() ==  1)  
					return (Hashtable)(((Row) rc.get(0)).getRow()); 
			}
		}
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
       			throw new ClsExceptions(e,"Error al obtener los datos de un producto.");
       		}	
	    }
		return null;
	}
	
	/** 
	 * Recoge toda informacion del del registro seleccionado a partir de sus claves<br/>
	 * @param  idInst - identificador de la institucion 
	 * @param  idTipoProd - identificador del tipo de producto
	 * @param  idProd - identificador del producto	 
	 * @param  idProdInst - identificador del producto institucion	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector getProductosComisiones (String institucion) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDCONTADOR  + "," +
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_PORCENTAJEIVA +", "+ 	
	            			PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_NOFACTURABLE + 	
							" FROM " + PysProductosInstitucionBean.T_NOMBRETABLA +  
							" WHERE " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							PysProductosInstitucionBean.T_NOMBRETABLA +"."+ PysProductosInstitucionBean.C_TIPOCERTIFICADO + "=" + "'B'" +
							" AND ROWNUM < 2 "; 
							
	            if (rc.findForUpdate(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener la informacion sobre productos con comisiones.");
	       		}	
		   }
	       return datos;                        
	    }
	
	public Vector getDiligenciasCGAE() throws ClsExceptions {
	    
	    Vector resultado = new Vector();
	    
	    Hashtable htCampos = new Hashtable();
	    htCampos.put(PysProductosInstitucionBean.C_IDINSTITUCION,CerSolicitudCertificadosAdm.IDCGAE);
	    htCampos.put(PysProductosInstitucionBean.C_TIPOCERTIFICADO,PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA);
	    htCampos.put(PysProductosInstitucionBean.C_FECHABAJA,"");
	    
	    try {
            resultado = this.select(htCampos);
        } catch (ClsExceptions e) {
            throw new ClsExceptions(e,"Error al obtener la informacion sobre diligencias del CGAE.");
        }
	    
	    return resultado;
	}
	
	public Vector getComunicacionesInst(Integer idInstitucion) throws ClsExceptions {
	    
	    Vector resultado = new Vector();
	    
	    Hashtable htCampos = new Hashtable();
	    htCampos.put(PysProductosInstitucionBean.C_IDINSTITUCION,idInstitucion);
	    htCampos.put(PysProductosInstitucionBean.C_TIPOCERTIFICADO,PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION);
	    htCampos.put(PysProductosInstitucionBean.C_FECHABAJA,"");
	    
	    try {
            resultado = this.select(htCampos);
        } catch (ClsExceptions e) {
            throw new ClsExceptions(e,"Error al obtener la informacion sobre comunicaciones del CGAE.");
        }
	    
	    return resultado;
	}
	
	
	public Articulo realizarCompraPredefinida(Integer idInstitucion, String idInstitucionPresentador, Integer idTipoProducto, Long idProducto, Long idProductoInstitucion, Long idPersona, String idFormaPago, String idTipoEnvio,boolean isColegio, String fechaSolicitud, String metodoSolicitud) throws SIGAException, ClsExceptions {
	    
	    try {
			PysPeticionCompraSuscripcionAdm  peticionAdm = new PysPeticionCompraSuscripcionAdm(this.usrbean);
			PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm(this.usrbean);

			//Obtengo el numero de operacion y lo inserto en el carro. El codigo 1 es usado para Facturacion de Productos y Servicios:
			String idInstitucionAux = rellenarConCeros(idInstitucion.toString(),4); 
			String idPersonaAux = rellenarConCeros(idPersona.toString(),10);
			String fechaActualAux = String.valueOf(Calendar.getInstance().getTimeInMillis());;
			String numOperacion = "1"+idInstitucionAux+idPersonaAux+fechaActualAux;			
			
			// Petición de alta
			Long idPeticion = peticionAdm.getNuevoID(idInstitucion);
			if(!peticionAdm.insertPeticionAlta(idPersona, idInstitucion, idPeticion, numOperacion)){
				throw new ClsExceptions("Error al insertar la peticion del carro de la compra.");
			}	
					
			//Insertamos los articulos (productos y servicios), cada uno en su tabla:
			Articulo articulo = new Articulo(Articulo.CLASE_PRODUCTO,idProducto, idInstitucion, idProductoInstitucion, idTipoProducto);

			articulo.setIdPeticion(idPeticion);
			if(idFormaPago!=null){
			// ponemos la forma de pago
			articulo.setIdFormaPago(new Integer(idFormaPago));
			// comprobamos que tiene cuentas bancarias si es forma de pago banco
			if (new Integer(idFormaPago).intValue() == ClsConstants.TIPO_FORMAPAGO_TARJETA) {
				throw new SIGAException("certificados.boton.mensaje.tarjetaNO");
			} else{
				if(isColegio){
					//Solo de be venir configurado como banco o metalico. en caso de que venga como banco se buscara la cuenta bancaria
					if (new Integer(idFormaPago).intValue() == ClsConstants.TIPO_FORMAPAGO_FACTURA) {
						
						
						try {
							// Obtenemos su cuenta de pago.
							// En el caso de que no tenga cuenta de pago lo ponemos como metalico 
							CenCuentasBancariasAdm admBanc=new CenCuentasBancariasAdm(this.usrbean);
							
							CenCuentasBancariasBean beanBanc = admBanc.selectCuentaCargo(idPersona,idInstitucion);
							if(beanBanc!=null && beanBanc.getIdCuenta()!=null)
								articulo.setIdCuenta(beanBanc.getIdCuenta());
							else{
								//lanzamos la exception
								throw new Exception("Excepcion controlada. Cambiamos la forma de pago a metálico");
								
							}
								
							
						} catch (Exception e) {
							articulo.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_METALICO));
							
						}
						
					}
					
				}else{
				
					if (new Integer(idFormaPago).intValue() == ClsConstants.TIPO_FORMAPAGO_TRANSFERENCIA) {
						// obtenemos su cuenta de pago 
						CenCuentasBancariasAdm admBanc=new CenCuentasBancariasAdm(this.usrbean);
						CenCuentasBancariasBean beanBanc = admBanc.selectCuentaCargo(idPersona,idInstitucion);
						articulo.setIdCuenta(beanBanc.getIdCuenta());
					} 
				}
			}
			}
			// ponemos el tipo de envio
			articulo.setIdTipoEnvios(new Integer(idTipoEnvio));
			// comprobamos que tiene dirección según el tipo de envío.
			CenDireccionesAdm admDir = new CenDireccionesAdm(this.usrbean);
			CenDireccionesBean beanDir = null; 
			if (idTipoEnvio==null || idTipoEnvio.equals("") || (!idTipoEnvio.equals("1") && !idTipoEnvio.equals("2") && !idTipoEnvio.equals("3"))) {
				throw new SIGAException("certificados.boton.mensaje.preferenciasMal");
			} else {
				beanDir=admDir.obtenerDireccionPorTipo(idPersona.toString(),idInstitucion.toString(),idTipoEnvio);
				if (beanDir==null) {
					// RGG 18/04/2007 Peticion CGAE: Obtenemos cualquier direccion porque es probable que la carga de datos no cargara el campo preferente
					beanDir=admDir.obtenerDireccionPrimera(idPersona.toString(),idInstitucion.toString());
					if (beanDir==null) {
						// si no tiene direccion simplemente no enviamos.
						// throw new SIGAException("certificados.boton.mensaje.noDireccion");
					} else {
						articulo.setIdDireccion(beanDir.getIdDireccion());
					}
				} else {
					articulo.setIdDireccion(beanDir.getIdDireccion());
				}
			}
			// jbd 17/02/2010 inc-6361
			articulo.setMetodoSolicitud(new Integer(metodoSolicitud));
			articulo.setFechaSolicitud(fechaSolicitud);
			//articulo.getNoFacturable()
			productosAdm.insertProducto(articulo, idPeticion, (idInstitucionPresentador.trim().equals(""))?null:new Integer(idInstitucionPresentador), idPersona);

			return articulo;
			
        } catch (SIGAException e) {
            throw e;
	    } catch (ClsExceptions e) {
	        throw e;
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al realizar la compra predefinida");
	    }

	}


	public void aprobarGenerarPredefinido(CerSolicitudCertificadosBean beanSolicitud, PysProductosInstitucionBean beanProd, boolean usarIdInstitucion, String idPlantilla,HttpServletRequest request) throws SIGAException, ClsExceptions {
	    
	    try {

	        GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);

            String sRutaDB = admParametros.getValor(beanSolicitud.getIdInstitucion().toString(), "CER" ,"PATH_CERTIFICADOS", "");

	        if (sRutaDB==null || sRutaDB.equals(""))
	        {
	            throw new SIGAException("certificados.boton.mensaje.pathCertitificadosMAL");
	        }
	        
	        String sRutaPlantillas = admParametros.getValor(beanSolicitud.getIdInstitucion().toString(), "CER" ,"PATH_PLANTILLAS", "");
	        
	        if (sRutaPlantillas==null || sRutaPlantillas.equals(""))
	        {
	            throw new SIGAException("certificados.boton.mensaje.pathPlantillasMAL");
	        }
	        
	        sRutaPlantillas += File.separator + beanSolicitud.getIdInstitucion().toString();

	        String sAux = beanSolicitud.getIdInstitucion().toString() + "_" + beanSolicitud.getIdSolicitud().toString();
	        
	        sRutaDB += File.separator + "tmp";
	        
	        File fDirTemp = new File(sRutaDB);
	        fDirTemp.mkdirs();

	        File fOut = new File(fDirTemp.getPath() + File.separator + sAux + "_" + System.currentTimeMillis() + ".pdf");
	        File fIn = new File(fOut.getPath() + ".tmp");
	        
	        fOut.deleteOnExit();
	        fIn.deleteOnExit();

        	// HACEMOS UNO NUEVO PARA ACTUALIZAR EL ESTADO
	        //Hashtable htOld = beanSolicitud.getOriginalHash();
		    Hashtable htNew = beanSolicitud.getOriginalHash();
		    
		    /// RGG 05/03/2007 CAMBIO DE CONTADORES /////////////////////
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.usrbean);
		    boolean tieneContador=admSolicitud.tieneContador(beanSolicitud.getIdInstitucion().toString(),beanSolicitud.getIdSolicitud().toString());
			GestorContadores gc = new GestorContadores(this.usrbean); 
			//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
			String idContador="";
			idContador=beanProd.getIdContador();
			Hashtable contadorTablaHash=gc.getContador(beanSolicitud.getIdInstitucion(),idContador);
				
			
			if (!tieneContador && contadorTablaHash.get("MODO").toString().equals("0")){
				// MODO REGISTRO. Se suponen siempre asi estos
				
				Integer longitud= new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));
				int longitudContador=longitud.intValue();
				
				int numContador= new Integer((contadorTablaHash.get("CONTADOR").toString())).intValue();
				gc.validarLogitudContador(numContador,contadorTablaHash);
				
				//Comprobamos la unicidad de este contador junto con el prefijo y sufijo guardado en la hash contador
				while(gc.comprobarUnicidadContadorProdCertif(numContador,contadorTablaHash, beanProd.getIdTipoProducto().toString(), beanProd.getIdProducto().toString(), beanProd.getIdProductoInstitucion().toString())){
					
					numContador++;
					gc.validarLogitudContador(numContador,contadorTablaHash);
				}
				
			  	Integer contadorSugerido=new Integer(numContador);
			  	String contadorFinalSugerido=UtilidadesString.formatea(contadorSugerido,longitudContador,true);
			  	
			  	htNew.put(CerSolicitudCertificadosBean.C_PREFIJO_CER,(String)contadorTablaHash.get("PREFIJO"));
			  	htNew.put(CerSolicitudCertificadosBean.C_SUFIJO_CER,(String)contadorTablaHash.get("SUFIJO"));
			  	htNew.put(CerSolicitudCertificadosBean.C_CONTADOR_CER,contadorFinalSugerido);
			  
			  	gc.setContador(contadorTablaHash,contadorFinalSugerido);
				
			}

		    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBADO);
        	
			
			///////////////////////////////////////////////

		    String[] claves = {CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD};
	        String[] campos = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO,CerSolicitudCertificadosBean.C_PREFIJO_CER,CerSolicitudCertificadosBean.C_SUFIJO_CER, CerSolicitudCertificadosBean.C_CONTADOR_CER};

		    if (!admSolicitud.updateDirect(htNew,claves,campos))
		    {
		        throw new ClsExceptions("Error al APROBAR el/los PDF/s");
		    }

		    // GENERAR CERTIFICADO
        	Certificado.generarCertificadoPDF(beanProd.getIdTipoProducto().toString(), beanProd.getIdProducto().toString(), beanProd.getIdProductoInstitucion().toString(), 
                    						  beanSolicitud.getIdInstitucion().toString(), idPlantilla, beanSolicitud.getIdPersona_Des().toString(), fIn, fOut, 
                    						  sRutaPlantillas, beanSolicitud.getIdSolicitud().toString(), (beanSolicitud.getIdInstitucionOrigen()==null)?null:beanSolicitud.getIdInstitucionOrigen().toString(),usarIdInstitucion, this.usrbean,request);
        	
        	
        	admSolicitud.guardarCertificado(beanSolicitud, fOut);
	        fOut.delete();

		    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO);


	        // FIRMAR CERTIFICADO
	        if (!admSolicitud.firmarPDF(beanSolicitud.getIdSolicitud().toString(), beanSolicitud.getIdInstitucion().toString())) {
		        ClsLogging.writeFileLog("Error al FIRMAR el PDF de la Solicitud: " + beanSolicitud.getIdSolicitud().toString(), 3);
		    } else {
		        htNew.put(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO);
		    }
		    
	        String[] campos2 = {CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO};
		    
	        if (!admSolicitud.updateDirect(htNew, claves, campos2 ))
		    {
		        throw new ClsExceptions("Error al GENERAR el/los PDF/s:" + admSolicitud.getError());
		    }
		    
	        fIn.delete();
	    	
        } catch (SIGAException e) {
            throw e;
	    } catch (ClsExceptions e) {
	        throw e;
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al aprobar y generar certificado predefinido");
	    }

	}

	private String rellenarConCeros(String arg, int longitud){
		String salida = null;
		
		salida = arg;
		while (salida.length() < longitud) {
			salida = "0"+salida;
		}
		return salida; 
	}
	
	
	public boolean cargarFicheroCompras(PysProductosInstitucionBean producto, CenClienteBean cliente, String unidades)throws SIGAException, ClsExceptions{
		
		boolean cargaOk = false;
		
		PysPeticionCompraSuscripcionAdm petCompraSuscripAdm = new PysPeticionCompraSuscripcionAdm(this.usrbean);
		PysProductosSolicitadosAdm prodSolAdm = new PysProductosSolicitadosAdm(this.usrbean);
		PysCompraAdm compraAdm = new PysCompraAdm (this.usrbean);
		CenCuentasBancariasAdm cuentasBanAdm = new CenCuentasBancariasAdm(this.usrbean);
		
		PysPeticionCompraSuscripcionBean petCompraSuscripBean = new PysPeticionCompraSuscripcionBean();
		PysProductosSolicitadosBean prodSolBean = new PysProductosSolicitadosBean();
		PysCompraBean compraBean = new PysCompraBean ();
		CenCuentasBancariasBean cuentasBanBean = new CenCuentasBancariasBean();
		
		
		cuentasBanBean = cuentasBanAdm.selectPrimeraCuentaCargo(cliente.getIdPersona(), new Integer(this.usrbean.getLocation()));
		
		Long nuevoID = petCompraSuscripAdm.getNuevoID(new Integer(this.usrbean.getLocation()));
		
		
		// seteamos los beans necesarios para hacer los insert en dichas tablas.
		
		petCompraSuscripBean.setIdInstitucion(new Integer(this.usrbean.getLocation()));
		petCompraSuscripBean.setIdPeticion(nuevoID);
		petCompraSuscripBean.setTipoPeticion("A");
		petCompraSuscripBean.setIdPersona(cliente.getIdPersona());
		petCompraSuscripBean.setFecha("sysdate");
		petCompraSuscripBean.setIdEstadoPeticion(new Integer(20));
		petCompraSuscripBean.setFechaMod("sysdate");
		petCompraSuscripBean.setIdPeticionAlta(null);
		petCompraSuscripBean.setNumAut(null);
		petCompraSuscripBean.setNumOperacion(null);
		petCompraSuscripBean.setUsuMod(new Integer(this.usrbean.getUserName()));
		petCompraSuscripBean.setReferencia(null);

		prodSolBean.setIdInstitucion(new Integer(this.usrbean.getLocation()));
		prodSolBean.setIdPeticion(nuevoID);
		prodSolBean.setIdTipoProducto(producto.getIdTipoProducto());
		prodSolBean.setIdProducto(producto.getIdProducto());
		prodSolBean.setIdProductoInstitucion(producto.getIdProductoInstitucion());
		prodSolBean.setIdPersona(cliente.getIdPersona());
		
		prodSolBean.setCantidad(new Integer(unidades));
		prodSolBean.setAceptado("A");
		prodSolBean.setValor(producto.getValor());
		prodSolBean.setPorcentajeIVA(producto.getPorcentajeIva());
		if (!(cuentasBanBean==null)){
			prodSolBean.setIdCuenta(cuentasBanBean.getIdCuenta());
			prodSolBean.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA));//Forma de pago por Banco
		}else{
			prodSolBean.setIdCuenta(null);
			prodSolBean.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_METALICO));//Forma de pago por Metalico
		}
		prodSolBean.setIdTipoEnvios(null);
		prodSolBean.setIdDireccion(null);
		prodSolBean.setIdInstitucionOrigen(new Integer(this.usrbean.getLocation()));
		prodSolBean.setNoFacturable("0");
		prodSolBean.setUsuMod(new Integer(this.usrbean.getUserName()));
		prodSolBean.setFechaSolicitud("sysdate");
		prodSolBean.setFechaMod("sysdate");
		

		compraBean.setIdInstitucion(new Integer(this.usrbean.getLocation()));
		compraBean.setIdPeticion(nuevoID);
		compraBean.setIdTipoProducto(producto.getIdTipoProducto());
		compraBean.setIdProducto(producto.getIdProducto());
		compraBean.setIdProductoInstitucion(producto.getIdProductoInstitucion());
		compraBean.setFecha("sysdate");
		compraBean.setCantidad(new Integer(unidades));
		compraBean.setImporteUnitario(producto.getValor());
		compraBean.setIva(producto.getPorcentajeIva());
		compraBean.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA));//Forma de pago por Banco
		compraBean.setFechaMod("sysdate");
		compraBean.setUsuMod(new Integer(this.usrbean.getUserName()));
		compraBean.setIdPersona(cliente.getIdPersona());
		compraBean.setDescripcion(producto.getDescripcion());
		compraBean.setImporteAnticipado(new Double(0));
		compraBean.setAceptado(null);
		compraBean.setNumeroLinea(null);
		compraBean.setIdFactura(null);
		compraBean.setFechaBaja(null);
		if (!(cuentasBanBean==null)){
			compraBean.setIdCuenta(cuentasBanBean.getIdCuenta());
		}else{
			compraBean.setIdCuenta(null);
		}
		compraBean.setIdPersonaDeudor(null);
		compraBean.setIdCuentaDeudor(null);
		compraBean.setNoFacturable("0");
		
		//hacemos los insert relacionados con el proceso de compra
		
		try{
			cargaOk = petCompraSuscripAdm.insert(petCompraSuscripBean) && prodSolAdm.insert(prodSolBean)&&compraAdm.insert(compraBean);
		}catch (Exception e){
			throw new ClsExceptions("No se ha realizado correctamente la compra :" +e); 
		}
		return cargaOk;
		
	}
}