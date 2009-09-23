/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacLineaAbonoAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacLineaAbonoAdm(UsrBean usu) {
		super(FacLineaAbonoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacLineaAbonoBean.C_IDINSTITUCION,
							FacLineaAbonoBean.C_IDABONO,
							FacLineaAbonoBean.C_NUMEROLINEA,
							FacLineaAbonoBean.C_DESCRIPCIONLINEA,
							FacLineaAbonoBean.C_CANTIDAD,
							FacLineaAbonoBean.C_PRECIOUNITARIO,
							FacLineaAbonoBean.C_IVA,
							FacLineaAbonoBean.C_IDFACTURA,
							FacLineaAbonoBean.C_LINEAFACTURA,
							FacLineaAbonoBean.C_FECHAMODIFICACION,
							FacLineaAbonoBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacLineaAbonoBean.C_IDABONO,
							FacLineaAbonoBean.C_IDINSTITUCION,
							FacLineaAbonoBean.C_NUMEROLINEA};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacLineaAbonoBean bean = null;
		
		try {
			bean = new FacLineaAbonoBean();			
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,FacLineaAbonoBean.C_IDINSTITUCION));
			bean.setIdAbono (UtilidadesHash.getLong(hash,FacLineaAbonoBean.C_IDABONO));
			bean.setNumeroLinea (UtilidadesHash.getLong(hash,FacLineaAbonoBean.C_NUMEROLINEA));
			bean.setDescripcionLinea (UtilidadesHash.getString(hash,FacLineaAbonoBean.C_DESCRIPCIONLINEA));
			bean.setCantidad (UtilidadesHash.getInteger(hash,FacLineaAbonoBean.C_CANTIDAD));
			bean.setPrecioUnitario (UtilidadesHash.getDouble(hash,FacLineaAbonoBean.C_PRECIOUNITARIO));
			bean.setIva (UtilidadesHash.getFloat(hash,FacLineaAbonoBean.C_IVA));
			bean.setIdFactura(UtilidadesHash.getString(hash,FacLineaAbonoBean.C_IDFACTURA));
			bean.setLineaFactura(UtilidadesHash.getLong(hash,FacLineaAbonoBean.C_LINEAFACTURA));
			bean.setFechaMod(UtilidadesHash.getString(hash,FacLineaAbonoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacLineaAbonoBean.C_USUMODIFICACION));			
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
			FacLineaAbonoBean b = (FacLineaAbonoBean) bean;
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_IDABONO, b.getIdAbono());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_NUMEROLINEA,b.getNumeroLinea());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_DESCRIPCIONLINEA,b.getDescripcionLinea());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_CANTIDAD,b.getCantidad());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_PRECIOUNITARIO ,b.getPrecioUnitario());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_IVA ,b.getIva());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_IDFACTURA ,b.getIdFactura());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_LINEAFACTURA ,b.getLineaFactura());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,FacLineaAbonoBean.C_USUMODIFICACION,b.getUsuMod());			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Obtiene el valor NUMEROLINEA, <br/>
	 * @param  institucion - identificador del abono
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Siguiente numero de linea  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion, String abono) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		int contador = 0;	            
        Hashtable codigos = new Hashtable();
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(NUMEROLINEA) + 1) AS NUMEROLINEA FROM " + nombreTabla;
				contador++;
				codigos.put(new Integer(contador),institucion);
				sql += " WHERE IDINSTITUCION =:" + contador;
				contador++;
				codigos.put(new Integer(contador),abono);
				sql += " AND IDABONO =:" + contador;
		
			if (rc.findBind(sql,codigos)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("NUMEROLINEA").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "NUMEROLINEA");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en B.D.");		
		}
		return resultado;
	}
			
	/** 
	 * Recoge el desglose completo (las diferentes lineas) de un determinado abono<br/>
	 * @param  abono - identificador del abono
	 * @param  institucion - identificador de la institucion
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getDesglose(String abono, String institucion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDABONO + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDINSTITUCION + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_NUMEROLINEA + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_DESCRIPCIONLINEA + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_CANTIDAD + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDFACTURA + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_LINEAFACTURA + "," +
	            			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_PRECIOUNITARIO + "," +
							FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IVA + 
							" FROM " + 
							FacLineaAbonoBean.T_NOMBRETABLA +
	            			" WHERE " + 
							FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_IDABONO + "=" + abono;
											
				sql += " ORDER BY " + FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_NUMEROLINEA; 										
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
//	       catch (SIGAException e) {
//	       	throw e;
//	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener el desglose");
	       }
	       return datos;                        
	    }
		
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  abono - identificador del abono	 	  
	 * @param  linea - numero de la linea
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getLineaAbono (String institucion, String abono, String linea) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
				FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDABONO + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDINSTITUCION + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_NUMEROLINEA + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_DESCRIPCIONLINEA + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_CANTIDAD + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDFACTURA + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_LINEAFACTURA + "," +
    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_PRECIOUNITARIO + "," +
				FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IVA + 
				" FROM " + 
				FacLineaAbonoBean.T_NOMBRETABLA +
    			" WHERE " + 
				FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_IDINSTITUCION + "=" + institucion +
				" AND " +
				FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_IDABONO + "=" + abono +
				" AND " +
				FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_NUMEROLINEA + "=" + linea;
																						
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge informacion sobre las lineas asociadas a un abono para el informe MasterRepor 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  abono - identificador del abono
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getLineasImpresionInforme (String institucion, String abono) throws ClsExceptions {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDINSTITUCION + "," +
			    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IDABONO + "," +
			    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_NUMEROLINEA + "," +
			    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_CANTIDAD + " CANTIDAD_LINEA, " +
			    			"("+FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_PRECIOUNITARIO + ")*-1 PRECIO_LINEA," +
			    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_IVA + " IVA_LINEA," +
			    			FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_DESCRIPCIONLINEA + " DESCRIPCION_LINEA " +
							" FROM " + FacLineaAbonoBean.T_NOMBRETABLA + 
							" WHERE " +
							FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacLineaAbonoBean.T_NOMBRETABLA +"."+ FacLineaAbonoBean.C_IDABONO + "=" + abono +
							" ORDER BY " + FacLineaAbonoBean.T_NOMBRETABLA + "." + FacLineaAbonoBean.C_NUMEROLINEA + " ASC ";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  double importeNeto=UtilidadesNumero.redondea(new Double((String)resultado.get("CANTIDAD_LINEA")).doubleValue() * new Double((String)resultado.get("PRECIO_LINEA")).doubleValue(),2);
	                  double importeIva=UtilidadesNumero.redondea((importeNeto * new Double((String)resultado.get("IVA_LINEA")).doubleValue())/100,2);
	                  double importeTotal=UtilidadesNumero.redondea(importeNeto+importeIva,2);
	                  
	                  resultado.put("NETO_LINEA",        UtilidadesNumero.formato(new Double(importeNeto).toString()));
	                  resultado.put("IMPORTE_IVA_LINEA", UtilidadesNumero.formato(new Double(importeIva).toString()));
	                  resultado.put("TOTAL_LINEA",       UtilidadesNumero.formato(new Double(importeTotal).toString()));

	                  UtilidadesHash.set(resultado, "IVA_LINEA_AUX",UtilidadesHash.getFloat(resultado, "IVA_LINEA"));
	                  UtilidadesHash.set(resultado, "IVA_LINEA",    UtilidadesNumero.formato(UtilidadesHash.getFloat(resultado, "IVA_LINEA").floatValue()));
	                  UtilidadesHash.set(resultado, "PRECIO_LINEA", UtilidadesNumero.formato(UtilidadesHash.getFloat(resultado, "PRECIO_LINEA").floatValue()));

	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las lineas de un abono.");
	       }
	       return datos;                        
	    }	
		
}
