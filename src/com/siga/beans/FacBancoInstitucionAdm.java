/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 * jose.barrientos - 25-02-2009 - Añadida la función obtener bancos para recuperar todos los bancos de una institucion
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacBancoInstitucionAdm extends MasterBeanAdministrador {
	
	public FacBancoInstitucionAdm (UsrBean usu) {
		super (FacBancoInstitucionBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacBancoInstitucionBean.C_IDINSTITUCION, 		
						    FacBancoInstitucionBean.C_BANCOS_CODIGO,
							FacBancoInstitucionBean.C_COD_SUCURSAL,							
							FacBancoInstitucionBean.C_COD_BANCO,							
							FacBancoInstitucionBean.C_FECHABAJA,
							FacBancoInstitucionBean.C_NUMEROCUENTA,
							FacBancoInstitucionBean.C_DIGITOCONTROL,							
							FacBancoInstitucionBean.C_ASIENTOCONTABLE,
							FacBancoInstitucionBean.C_IBAN,
							FacBancoInstitucionBean.C_SJCS,
							FacBancoInstitucionBean.C_IDSUFIJOSJCS,			
							FacBancoInstitucionBean.C_COMISIONIMPORTE,
							FacBancoInstitucionBean.C_COMISIONIVA,
							FacBancoInstitucionBean.C_COMISIONDESCRIPCION,
							FacBancoInstitucionBean.C_COMISIONCUENTACONTABLE,
							FacBancoInstitucionBean.C_USUMODIFICACION,
							FacBancoInstitucionBean.C_FECHAMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacBancoInstitucionBean.C_IDINSTITUCION, FacBancoInstitucionBean.C_BANCOS_CODIGO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacBancoInstitucionBean bean = null;
		
		try {
			bean = new FacBancoInstitucionBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacBancoInstitucionBean.C_IDINSTITUCION));
			bean.setBancosCodigo				(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_BANCOS_CODIGO));
			bean.setCodBanco					(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_COD_BANCO));
			bean.setCodSucursal					(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_COD_SUCURSAL));
			bean.setFechaBaja					(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_FECHABAJA));
			bean.setNumeroCuenta				(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_NUMEROCUENTA));
			bean.setAsientoContable				(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_ASIENTOCONTABLE));
			bean.setDigitoControl				(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_DIGITOCONTROL));			
			bean.setIban						(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_IBAN));
			bean.setSJCS						(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_SJCS));
			bean.setIdsufijosjcs				(UtilidadesHash.getInteger(hash, FacBancoInstitucionBean.C_IDSUFIJOSJCS));
			bean.setComisionImporte				(UtilidadesHash.getDouble(hash, FacBancoInstitucionBean.C_COMISIONIMPORTE));
			bean.setComisionIVA					(UtilidadesHash.getDouble(hash, FacBancoInstitucionBean.C_COMISIONIVA));
			bean.setComisionDescripcion			(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_COMISIONDESCRIPCION));
			bean.setComisionCuentaContable		(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_COMISIONCUENTACONTABLE));
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacBancoInstitucionBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacBancoInstitucionBean.C_USUMODIFICACION));
			
		} catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacBancoInstitucionBean b = (FacBancoInstitucionBean) bean;
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_COD_BANCO, b.getCodBanco());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_COD_SUCURSAL, b.getCodSucursal());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_FECHABAJA, b.getFechaBaja());			
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_NUMEROCUENTA, b.getNumeroCuenta());			
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_ASIENTOCONTABLE, b.getAsientoContable());			
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_IBAN, b.getIban());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_DIGITOCONTROL, b.getDigitoControl());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_SJCS, b.getSJCS());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_IDSUFIJOSJCS, b.getIdsufijosjcs());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_COMISIONIMPORTE, b.getComisionImporte());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_COMISIONIVA, b.getComisionIVA());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_COMISIONDESCRIPCION, b.getComisionDescripcion());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_COMISIONCUENTACONTABLE, b.getComisionCuentaContable());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacBancoInstitucionBean.C_USUMODIFICACION, b.getUsuMod());
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/** 
	 * Recoge el banco de menor comision por abonos ajenos
	 * @param  institucion - identificador de la institucion
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getBancoMenorComision(String institucion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IBAN + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_SUCURSAL + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_NUMEROCUENTA + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_ASIENTOCONTABLE + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COMISIONIMPORTE + 
							" FROM " + 
							FacBancoInstitucionBean.T_NOMBRETABLA +
	            			" WHERE " + 
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_COMISIONIMPORTE + "=(" + 
								"SELECT MIN(" + FacBancoInstitucionBean.C_COMISIONIMPORTE + ")" + 
								" FROM " + 
								FacBancoInstitucionBean.T_NOMBRETABLA +
		            			" WHERE " + 
								FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + institucion +
								" AND " +
								FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_SJCS + "= 0 " +
								" AND " +
								FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_FECHABAJA + " IS NULL)" +
							" AND " +
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_SJCS + "= 0 " +
							" AND " +
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_FECHABAJA + " IS NULL" +
							" AND ROWNUM < 2";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener el banco de menor comision");
	       }
	       return datos;                        
	    }
	
	
	public Vector getBancosCodigoDesdeFichero(String institucion, String idFactura, String idRenegociacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="select b.cod_banco||b.cod_sucursal||'..'||b.numerocuenta as NUMEROCUENTA from fac_disquetecargos f, fac_facturaincluidaendisquete d, fac_bancoinstitucion b " +
	                		" where f.idinstitucion = d.idinstitucion " +
            				" and   f.iddisquetecargos = d.iddisquetecargos " +
            				" and   b.idinstitucion = f.idinstitucion " +
            				" and   b.bancos_codigo = f.bancos_codigo " +
            				" and   d.idinstitucion = "+institucion+
            				" and   d.idfactura =  " +idFactura +
            				" and   d.idrenegociacion " + ((idRenegociacion.equals("00"))?" IS NULL ":" = " + new Integer(Integer.parseInt(idRenegociacion)).toString());
            				    
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener el codigo de banco desde el fichero");
	       }
	       return datos;                        
	    }
		
	
	
	/** 
	 * Recoge los bancos restantes al de menor comision por abonos ajenos
	 * @param  institucion - identificador de la institucion
	 * @param  codigoBanco - identificador del banco que cobra menor comision
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getRestoBancosConComision(String institucion, String codigoBanco) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IBAN + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_SUCURSAL + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_NUMEROCUENTA + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_ASIENTOCONTABLE + "," +
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COMISIONIMPORTE + 
							" FROM " + 
							FacBancoInstitucionBean.T_NOMBRETABLA +
	            			" WHERE " + 
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_BANCOS_CODIGO + "<>" + codigoBanco +
							" AND " +
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_SJCS + "= 0 " +
							" AND " +
							FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_FECHABAJA + " IS NULL";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener el banco de menor comision");
	       }
	       return datos;                        
	    }
	
	public Vector obtenerBancosSerieFacturacion(String idInstitucion,String idSerieFacturacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							"BI." + FacBancoInstitucionBean.C_BANCOS_CODIGO + "," +
	            			"BI." + FacBancoInstitucionBean.C_IDINSTITUCION + "," +
	            			"BI." + FacBancoInstitucionBean.C_COD_BANCO + "," +
	            			"BI." + FacBancoInstitucionBean.C_COD_SUCURSAL + "," +
	            			"BI." + FacBancoInstitucionBean.C_NUMEROCUENTA + "," +
	            			"BI." + FacBancoInstitucionBean.C_ASIENTOCONTABLE + "," +
	            			"BI." + FacBancoInstitucionBean.C_IBAN + "," +
	            			"BI.COD_BANCO || '-' || BI.COD_SUCURSAL || '-' || BI.DIGITOCONTROL || '-' ||BI.NUMEROCUENTA AS CUENTACONTABLE, "+
	            			"BI." + FacBancoInstitucionBean.C_COMISIONIMPORTE + "," +
						    "BI.COD_BANCO, " +
						    "(SELECT NOMBRE FROM CEN_BANCOS WHERE CODIGO=BI.COD_BANCO) AS BANCO, "+
						    "(SELECT COUNT (1) FROM FAC_SERIEFACTURACION_BANCO WHERE IDINSTITUCION=BI.IDINSTITUCION AND BANCOS_CODIGO=BI.BANCOS_CODIGO AND IDSERIEFACTURACION="+idSerieFacturacion+" ) AS SELECCIONADO, "+ 
						    "(SELECT COUNT (*) FROM FAC_SERIEFACTURACION_BANCO WHERE IDINSTITUCION=BI.IDINSTITUCION AND BANCOS_CODIGO=BI.BANCOS_CODIGO) AS USO, "+ 
							"(SELECT NVL(IDSUFIJO,'') FROM FAC_SERIEFACTURACION_BANCO WHERE IDINSTITUCION=BI.IDINSTITUCION AND BANCOS_CODIGO=BI.BANCOS_CODIGO AND IDSERIEFACTURACION="+idSerieFacturacion+" ) AS IDSUFIJO "+ 
						    " FROM " + 
							FacBancoInstitucionBean.T_NOMBRETABLA + " BI " +
	            			" WHERE BI."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + idInstitucion +
	            			" AND BI."+ FacBancoInstitucionBean.C_FECHABAJA + " IS NULL";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error ");
	       }
	       return datos;                        
	    }

	public Vector obtenerSerieFacturacionBanco(String idInstitucion,String idSerieFacturacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT IDINSTITUCION, IDSERIEFACTURACION, BANCOS_CODIGO " +
							" FROM FAC_SERIEFACTURACION_BANCO" + 
	            			" WHERE IDINSTITUCION=" + idInstitucion +
	            			" AND IDSERIEFACTURACION=" + idSerieFacturacion;
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error ");
	       }
	       return datos;                        
	    }

	public void borrarBancosSerieFacturacion(String idInstitucion, String idSerieFacturacion) throws ClsExceptions {
		String sql = "DELETE FROM FAC_SERIEFACTURACION_BANCO WHERE IDINSTITUCION="+idInstitucion + " AND IDSERIEFACTURACION="+idSerieFacturacion;
		this.deleteSQL(sql);
	}
	
	public void insertaBancosSerieFacturacion(String idInstitucion, String idSerieFacturacion, String idBanco, String idSufijo) throws ClsExceptions {
		String sql = "INSERT INTO FAC_SERIEFACTURACION_BANCO (IDINSTITUCION,IDSERIEFACTURACION,BANCOS_CODIGO,USUMODIFICACION,IDSUFIJO,FECHAMODIFICACION) VALUES ("+idInstitucion + ","+idSerieFacturacion+",'"+idBanco+"',"+this.usuModificacion.toString()+","+idSufijo+",SYSDATE)";
		this.insertSQL(sql);
	}
	
	public Vector obtenerBancos(String idInstitucion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							"BI." + FacBancoInstitucionBean.C_BANCOS_CODIGO + "," +
	            			"BI." + FacBancoInstitucionBean.C_IDINSTITUCION + "," +
	            			"BI." + FacBancoInstitucionBean.C_IBAN + "," +
	            			"BI." + FacBancoInstitucionBean.C_COD_BANCO + "," +
	            			"BI." + FacBancoInstitucionBean.C_COD_SUCURSAL + "," +
	            			"BI." + FacBancoInstitucionBean.C_NUMEROCUENTA + "," +
	            			"BI." + FacBancoInstitucionBean.C_ASIENTOCONTABLE + "," +
	            			"BI." + FacBancoInstitucionBean.C_SJCS + "," +
	            			"BI." + FacBancoInstitucionBean.C_IDSUFIJOSJCS + "," +
	            			"BI.COD_BANCO || '-' || BI.COD_SUCURSAL || '-' || BI.DIGITOCONTROL || '-' ||BI.NUMEROCUENTA AS CUENTACONTABLE, "+
						    "BI.COD_BANCO, " +
						    "(SELECT NOMBRE FROM CEN_BANCOS WHERE CODIGO=BI.COD_BANCO) AS BANCO, "+
						    "(SELECT COUNT (1) FROM FAC_SERIEFACTURACION_BANCO WHERE IDINSTITUCION=BI.IDINSTITUCION AND BANCOS_CODIGO=BI.BANCOS_CODIGO ) AS SELECCIONADO "+ 

							" FROM " + 
							FacBancoInstitucionBean.T_NOMBRETABLA + " BI " +
	            			" WHERE BI."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + idInstitucion +
	            			" AND BI."+ FacBancoInstitucionBean.C_FECHABAJA + " IS NULL";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error ");
	       }
	       return datos;                        
	    }
	
	public Vector obtenerBancosConBaja(String idInstitucion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							"BI." + FacBancoInstitucionBean.C_BANCOS_CODIGO + "," +
							"DECODE(BI." + FacBancoInstitucionBean.C_FECHABAJA + ",NULL,1,0) ACTIVA,"+ //Si esta de baja ->0, si esta activa ->1
	            			"BI." + FacBancoInstitucionBean.C_IDINSTITUCION + "," +
	            			"BI." + FacBancoInstitucionBean.C_IBAN + "," +
	            			"BI." + FacBancoInstitucionBean.C_COD_BANCO + "," +
	            			"BI." + FacBancoInstitucionBean.C_COD_SUCURSAL + "," +
	            			"BI." + FacBancoInstitucionBean.C_NUMEROCUENTA + "," +
	            			"BI." + FacBancoInstitucionBean.C_ASIENTOCONTABLE + "," +
	            			"BI." + FacBancoInstitucionBean.C_SJCS + "," +
	            			"BI." + FacBancoInstitucionBean.C_IDSUFIJOSJCS + "," +
	            			"BI.COD_BANCO || '-' || BI.COD_SUCURSAL || '-' || BI.DIGITOCONTROL || '-' ||BI.NUMEROCUENTA AS CUENTACONTABLE, "+
						    "BI.COD_BANCO, " +
						    "(SELECT NOMBRE FROM CEN_BANCOS WHERE CODIGO=BI.COD_BANCO) AS BANCO, "+
						    "(SELECT COUNT (1) FROM FAC_SERIEFACTURACION_BANCO WHERE IDINSTITUCION=BI.IDINSTITUCION AND BANCOS_CODIGO=BI.BANCOS_CODIGO ) AS SELECCIONADO "+ 

							" FROM " + 
							FacBancoInstitucionBean.T_NOMBRETABLA + " BI " +
	            			" WHERE BI."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + idInstitucion;
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error ");
	       }
	       return datos;                        
	    }	
}
