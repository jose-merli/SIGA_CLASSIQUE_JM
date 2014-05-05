package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.AnexosCuentasBancariasForm;

public class CenAnexosCuentasBancariasAdm extends MasterBeanAdministrador {
	
	private String sqlMandatosSelect = "SELECT MANDATOS." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + " AS " + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDPERSONA + " AS " + CenAnexosCuentasBancariasBean.C_IDPERSONA + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDCUENTA + " AS " + CenAnexosCuentasBancariasBean.C_IDCUENTA + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDMANDATO + " AS " + CenAnexosCuentasBancariasBean.C_IDMANDATO + ", " + 
										" NULL AS " + CenAnexosCuentasBancariasBean.C_IDANEXO + ", " + 										
										" NULL AS " + CenAnexosCuentasBancariasBean.C_ORIGEN + ", " + 
										" NULL AS " + CenAnexosCuentasBancariasBean.C_DESCRIPCION + ", " + 
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + ", 'DD/MM/YYYY') AS " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + ", 'HH24') AS " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_HORA + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + ", 'MI') AS " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_MINUTOS + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR + " AS " + CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA + " AS " + CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FECHACREACION + ", 'DD/MM/YYYY') AS " + CenAnexosCuentasBancariasBean.C_FECHACREACION + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_USUCREACION + " AS " + CenAnexosCuentasBancariasBean.C_USUCREACION + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION + ", 'DD/MM/YYYY') AS " + CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_USUMODIFICACION + " AS " + CenAnexosCuentasBancariasBean.C_USUMODIFICACION + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_FECHACREACION + " AS " + CenAnexosCuentasBancariasBean.C_FECHAORDEN;
	
	private String sqlMandatosFrom = " FROM " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + " MANDATOS ";
	
	private String sqlAnexosSelect = "SELECT ANEXOS." + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + ", " + 
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_IDPERSONA + ", " +  
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_IDCUENTA + ", " +  
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_IDMANDATO + "," +
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_IDANEXO + "," +										
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_ORIGEN + "," +
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_DESCRIPCION + "," +
										" TO_CHAR(ANEXOS." + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + ", 'DD/MM/YYYY') AS " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + ", " +
										" TO_CHAR(ANEXOS." + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + ", 'HH24') AS " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_HORA + ", " +
										" TO_CHAR(ANEXOS." + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + ", 'MI') AS " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_MINUTOS + ", " +
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR + "," +
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA + " AS " + CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA + ", " +
										" TO_CHAR(ANEXOS." + CenAnexosCuentasBancariasBean.C_FECHACREACION + ", 'DD/MM/YYYY') AS " + CenAnexosCuentasBancariasBean.C_FECHACREACION + ", " +
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_USUCREACION + ", " +
										" TO_CHAR(ANEXOS." + CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION + ", 'DD/MM/YYYY') AS " + CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION + ", " + 
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_USUMODIFICACION + ", " +
										" ANEXOS." + CenAnexosCuentasBancariasBean.C_FECHACREACION + " AS " + CenAnexosCuentasBancariasBean.C_FECHAORDEN;;
	
	private String sqlAnexosFrom = " FROM " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA + " ANEXOS ";								
			
	public CenAnexosCuentasBancariasAdm(UsrBean usuario) {
	    super(CenAnexosCuentasBancariasBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {
			CenAnexosCuentasBancariasBean.C_IDINSTITUCION, 
			CenAnexosCuentasBancariasBean.C_IDPERSONA, 
			CenAnexosCuentasBancariasBean.C_IDCUENTA, 
			CenAnexosCuentasBancariasBean.C_IDMANDATO,
			CenAnexosCuentasBancariasBean.C_IDANEXO,
			CenAnexosCuentasBancariasBean.C_FECHACREACION,
			CenAnexosCuentasBancariasBean.C_USUCREACION,
			CenAnexosCuentasBancariasBean.C_ORIGEN,
			CenAnexosCuentasBancariasBean.C_DESCRIPCION, 
			CenAnexosCuentasBancariasBean.C_FIRMA_FECHA,
			CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR, 
			CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA,
			CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION,
			CenAnexosCuentasBancariasBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = {
			CenAnexosCuentasBancariasBean.C_IDINSTITUCION, 
			CenAnexosCuentasBancariasBean.C_IDPERSONA, 
			CenAnexosCuentasBancariasBean.C_IDCUENTA, 
			CenAnexosCuentasBancariasBean.C_IDMANDATO,
			CenAnexosCuentasBancariasBean.C_IDANEXO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenAnexosCuentasBancariasBean bean = new CenAnexosCuentasBancariasBean();;

		try	{
			bean.setIdInstitucion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDINSTITUCION)); 
			bean.setIdPersona(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDPERSONA)); 
			bean.setIdCuenta(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDCUENTA)); 
			bean.setIdMandato(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDMANDATO));
			bean.setIdAnexo(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDANEXO));
			bean.setFechaCreacion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FECHACREACION));
			bean.setUsuCreacion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_USUCREACION));
			bean.setOrigen(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_ORIGEN)); 
			bean.setDescripcion(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_DESCRIPCION));
			bean.setFirmaFecha(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA));
			bean.setFirmaFechaHora(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_HORA)); 
			bean.setFirmaFechaMinutos(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_MINUTOS)); 
			bean.setFirmaLugar(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR)); 
			bean.setIdFicheroFirma(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenAnexosCuentasBancariasBean.C_USUMODIFICACION));			
			
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htAnexo = new Hashtable();

		try {			
			CenAnexosCuentasBancariasBean beanAnexo = (CenAnexosCuentasBancariasBean) bean;
			
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDINSTITUCION, beanAnexo.getIdInstitucion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDPERSONA, beanAnexo.getIdPersona());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDCUENTA, beanAnexo.getIdCuenta());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDMANDATO, beanAnexo.getIdMandato());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDANEXO, beanAnexo.getIdAnexo());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FECHACREACION, beanAnexo.getFechaCreacion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_USUCREACION, beanAnexo.getUsuCreacion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_ORIGEN, beanAnexo.getOrigen());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_DESCRIPCION, beanAnexo.getDescripcion());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA, beanAnexo.getFirmaFecha());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_HORA, beanAnexo.getFirmaFechaHora());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FIRMA_FECHA_MINUTOS, beanAnexo.getFirmaFechaMinutos());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR, beanAnexo.getFirmaLugar());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_IDFICHEROFIRMA, beanAnexo.getIdFicheroFirma());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION, beanAnexo.getFechaMod());
			UtilidadesHash.set(htAnexo, CenAnexosCuentasBancariasBean.C_USUMODIFICACION, beanAnexo.getUsuMod());
			
		} catch (Exception e) {
			htAnexo = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htAnexo;
	}

    protected String[] getOrdenCampos() {
        return null;
    } 
    
    /**
     * Obtengo el mandato y anexos de un mandato
     * @param beanAnexo
     * @return
     * @throws ClsExceptions
     */
	public Vector<CenAnexosCuentasBancariasBean> obtenerAnexos(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS  UNION  CEN_ANEXOS_CUENTASBANCARIAS
			String sql = this.sqlMandatosSelect + 
						this.sqlMandatosFrom + 
						" WHERE MANDATOS." + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanMandato.getIdInstitucion() +
							" AND MANDATOS." + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + beanMandato.getIdPersona() + 
							" AND MANDATOS." + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + beanMandato.getIdCuenta() +
							" AND MANDATOS." + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + beanMandato.getIdMandato() +
					" UNION ALL " +	
						this.sqlAnexosSelect + 
						this.sqlAnexosFrom + 
						" WHERE ANEXOS." + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanMandato.getIdInstitucion() +
							" AND ANEXOS." + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + beanMandato.getIdPersona() + 
							" AND ANEXOS." + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + beanMandato.getIdCuenta() +
							" AND ANEXOS." + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + beanMandato.getIdMandato() +
					" ORDER BY " + CenAnexosCuentasBancariasBean.C_FECHAORDEN + " ASC";
			
			Vector<CenAnexosCuentasBancariasBean> vAnexos = null;
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql)) {        	   
				vAnexos = new Vector<CenAnexosCuentasBancariasBean>();
    			for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					CenAnexosCuentasBancariasBean anexoBean = (CenAnexosCuentasBancariasBean) this.hashTableToBean(fila.getRow());					
					vAnexos.add(anexoBean);
				}
            }		
			
			return vAnexos;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}      		
	
    /**
     * Obtengo los datos de un anexo
     * @param beanAnexo
     * @return
     * @throws ClsExceptions
     */
	public CenAnexosCuentasBancariasBean obtenerAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {
			String sql = this.sqlAnexosSelect + 
						this.sqlAnexosFrom + 
						" WHERE " + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanAnexo.getIdInstitucion() +
							" AND " + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + beanAnexo.getIdPersona() + 
							" AND " + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + beanAnexo.getIdCuenta() + 
							" AND " + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + beanAnexo.getIdMandato() +
							" AND " + CenAnexosCuentasBancariasBean.C_IDANEXO + " = " + beanAnexo.getIdAnexo();
			
			RowsContainer rc = new RowsContainer(); 												
		
			if (rc.find(sql) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				beanAnexo = (CenAnexosCuentasBancariasBean) this.hashTableToBean(fila.getRow());
            }		
			
			return beanAnexo;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}
	private String getSqlObtenerAnexo(CenAnexosCuentasBancariasBean beanAnexo){
		StringBuffer sql = new StringBuffer();
		sql.append(this.sqlAnexosSelect);
		sql.append(this.sqlAnexosFrom);
		sql.append(" WHERE ");
		sql.append(CenAnexosCuentasBancariasBean.C_IDINSTITUCION);
		sql.append(" = ");
		sql.append(beanAnexo.getIdInstitucion());
		sql.append(" AND ");
		sql.append(CenAnexosCuentasBancariasBean.C_IDPERSONA);
		sql.append(" = ");
		sql.append(beanAnexo.getIdPersona());
		
		if(beanAnexo.getIdCuenta()!=null){
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanAnexo.getIdCuenta());
		}
		if(beanAnexo.getIdMandato()!=null){
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdMandato());
		}
		if(beanAnexo.getIdAnexo()!=null){
			sql.append(" AND ");
			sql.append(CenAnexosCuentasBancariasBean.C_IDANEXO);
			sql.append(" = ");
			sql.append(beanAnexo.getIdAnexo());
		}
		return sql.toString();
	} 
	
	public Hashtable getAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		Hashtable anexoHashtable = null;
		try {
		
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(getSqlObtenerAnexo(beanAnexo)) && rc.size()>0) {
				 
				Row fila = (Row) rc.get(0);
				anexoHashtable = fila.getRow();
            }		
			
			return anexoHashtable;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}
	
	/**
	 * Actualizo los campos que se pueden modificar en CEN_ANEXOS_CUENTASBANCARIAS
	 * @param beanAnexo
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean modificarFirmaAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {
			// Calculo la fecha de la firma
			String sFirmaFecha = "NULL";
			if (beanAnexo.getFirmaFecha()!=null && !beanAnexo.getFirmaFecha().equals("") && 
					beanAnexo.getFirmaFechaHora()!=null && !beanAnexo.getFirmaFechaHora().equals("") &&
					beanAnexo.getFirmaFechaMinutos()!=null && !beanAnexo.getFirmaFechaMinutos().equals("")) {
				sFirmaFecha = "TO_DATE('" + beanAnexo.getFirmaFecha() + " " + beanAnexo.getFirmaFechaHora() + ":" + beanAnexo.getFirmaFechaMinutos() + ":00', 'DD/MM/YYYY HH24:MI:SS')";
			}
			
			// Calculo el lugar de la firma
			String sFirmaLugar = "NULL";
			if (beanAnexo.getFirmaLugar()!=null && !beanAnexo.getFirmaLugar().equals("")) {
				sFirmaLugar = "'" + beanAnexo.getFirmaLugar() + "'";
			}
			
			// Calculo el origen de la firma
			String sFirmaOrigen = "NULL";
			if (beanAnexo.getOrigen()!=null && !beanAnexo.getOrigen().equals("")) {
				sFirmaOrigen = "'" + beanAnexo.getOrigen() + "'";
			}
			
			// Calculo la descripcion de la firma
			String sFirmaDescripcion = "NULL";
			if (beanAnexo.getDescripcion()!=null && !beanAnexo.getDescripcion().equals("")) {
				sFirmaDescripcion = "'" + beanAnexo.getDescripcion() + "'";
			}		
			
			String sql = "UPDATE " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA +
						" SET " + CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + " = " + sFirmaFecha + ", " +
							CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR + " = " + sFirmaLugar +  ", " +
							CenAnexosCuentasBancariasBean.C_ORIGEN + " = " + sFirmaOrigen +  ", " +
							CenAnexosCuentasBancariasBean.C_DESCRIPCION + " = " + sFirmaDescripcion +  ", " +
							CenAnexosCuentasBancariasBean.C_USUMODIFICACION + " = " + this.usrbean.getUserName() + ", " +
							CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION + " = SYSDATE " +							
						" WHERE " + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanAnexo.getIdInstitucion() +
							" AND " + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + beanAnexo.getIdPersona() + 
							" AND " + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + beanAnexo.getIdCuenta() + 
							" AND " + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + beanAnexo.getIdMandato() +
							" AND " + CenAnexosCuentasBancariasBean.C_IDANEXO + " = " + beanAnexo.getIdAnexo();
			
			Row row = new Row();									
			return (row.updateSQL(sql)>0);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}		
	}		
	
	/**
	 * Insertar un nuevo anexos en CEN_ANEXOS_CUENTASBANCARIAS
	 * @param beanAnexo
	 * @return
	 * @throws ClsExceptions
	 */
	public void insertarFirmaAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {
			// Calculo la fecha de la firma
			String sFirmaFecha = "NULL";
			if (beanAnexo.getFirmaFecha()!=null && !beanAnexo.getFirmaFecha().equals("") && 
					beanAnexo.getFirmaFechaHora()!=null && !beanAnexo.getFirmaFechaHora().equals("") &&
					beanAnexo.getFirmaFechaMinutos()!=null && !beanAnexo.getFirmaFechaMinutos().equals("")) {
				sFirmaFecha = "TO_DATE('" + beanAnexo.getFirmaFecha() + " " + beanAnexo.getFirmaFechaHora() + ":" + beanAnexo.getFirmaFechaMinutos() + ":00', 'DD/MM/YYYY HH24:MI:SS')";
			}
			
			// Calculo el lugar de la firma
			String sFirmaLugar = "NULL";
			if (beanAnexo.getFirmaLugar()!=null && !beanAnexo.getFirmaLugar().equals("")) {
				sFirmaLugar = "'" + beanAnexo.getFirmaLugar() + "'";
			}
			
			// Calculo el origen de la firma
			String sFirmaOrigen = "NULL";
			if (beanAnexo.getOrigen()!=null && !beanAnexo.getOrigen().equals("")) {
				sFirmaOrigen = "'" + beanAnexo.getOrigen() + "'";
			}
			
			// Calculo la descripcion de la firma
			String sFirmaDescripcion = "NULL";
			if (beanAnexo.getDescripcion()!=null && !beanAnexo.getDescripcion().equals("")) {
				sFirmaDescripcion = "'" + beanAnexo.getDescripcion() + "'";
			}
			
			// Calculo nuevo identificador de anexo
			Integer idAnexo = this.getNuevoID(beanAnexo);	
			
			// Guardo el identificador del anexo en el bean
			beanAnexo.setIdAnexo(idAnexo.toString());
			
			String sql = "INSERT INTO " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA + " (" +
								CenAnexosCuentasBancariasBean.C_IDINSTITUCION + ", " +
								CenAnexosCuentasBancariasBean.C_IDPERSONA + ", " +
								CenAnexosCuentasBancariasBean.C_IDCUENTA + ", " +
								CenAnexosCuentasBancariasBean.C_IDMANDATO + ", " +
								CenAnexosCuentasBancariasBean.C_IDANEXO + ", " +								
								CenAnexosCuentasBancariasBean.C_FIRMA_FECHA + ", " +
								CenAnexosCuentasBancariasBean.C_FIRMA_LUGAR + ", " +
								CenAnexosCuentasBancariasBean.C_ORIGEN + ", " +
								CenAnexosCuentasBancariasBean.C_DESCRIPCION + ", " +
								CenAnexosCuentasBancariasBean.C_USUCREACION + ", " +
								CenAnexosCuentasBancariasBean.C_FECHACREACION + ", " +								
								CenAnexosCuentasBancariasBean.C_USUMODIFICACION + ", " +
								CenAnexosCuentasBancariasBean.C_FECHAMODIFICACION + 
							") VALUES (" +
								beanAnexo.getIdInstitucion() + ", " +
								beanAnexo.getIdPersona() + ", " +
								beanAnexo.getIdCuenta() + ", " +
								beanAnexo.getIdMandato() + ", " +
								idAnexo + ", " +								
								sFirmaFecha + ", " +
								sFirmaLugar + ", " +
								sFirmaOrigen + ", " +
								sFirmaDescripcion + ", " +
								this.usrbean.getUserName() + ", " +
								"SYSDATE, " +
								this.usrbean.getUserName() + ", " +
								"SYSDATE)";
			
			Row row = new Row();									
			row.insertSQL(sql);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al insertar un nuevo anexo en B.D.");
		}		
	}		
	
	/**
	 * Obtiene un nuevo ID del curriculum de una persona e institucion determinada
	 * @author daniel.campos 12-01-05
	 * @version 1	 
	 * @param Bean datos de la cuenta.
	 * @return nuevo ID.
	 */
	public Integer getNuevoID (CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		RowsContainer rc = null;
		
		try { 
			rc = new RowsContainer(); 
		} catch(Exception e) { 
			e.printStackTrace(); 
		}
		
		try {		
			String sql = " SELECT NVL(MAX(" + CenAnexosCuentasBancariasBean.C_IDANEXO + "),0) + 1 AS " + CenAnexosCuentasBancariasBean.C_IDANEXO + 
			  			 " FROM " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA +
						 " WHERE " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA + "." + CenAnexosCuentasBancariasBean.C_IDINSTITUCION +" = " + beanAnexo.getIdInstitucion() +
						 	" AND " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA + "." + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + beanAnexo.getIdPersona() +
						 	" AND " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA + "." + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + beanAnexo.getIdCuenta() +
						 	" AND " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA + "." + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + beanAnexo.getIdMandato();

			rc = this.find(sql);
			if (rc!=null && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Integer idAnexo = UtilidadesHash.getInteger(fila.getRow(), CenAnexosCuentasBancariasBean.C_IDANEXO);
				return idAnexo;
			}
			
		} catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		
		return null;
	}
	
	/**
	 * Elimino un anexos de CEN_ANEXOS_CUENTASBANCARIAS
	 * @param beanAnexo
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean borrarFirmaAnexo(CenAnexosCuentasBancariasBean beanAnexo) throws ClsExceptions {
		try {			
			String sql = "DELETE " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA +
						" WHERE " + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanAnexo.getIdInstitucion() +
							" AND " + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + beanAnexo.getIdPersona() + 
							" AND " + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + beanAnexo.getIdCuenta() + 
							" AND " + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + beanAnexo.getIdMandato() +
							" AND " + CenAnexosCuentasBancariasBean.C_IDANEXO + " = " + beanAnexo.getIdAnexo();
			
			Row row = new Row();									
			return (row.deleteSQL(sql) == 1);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al insertar un nuevo anexo en B.D.");
		}		
	}		
	public boolean asociarFichero(AnexosCuentasBancariasForm anexoMandatosCuentasBancariasForm) throws ClsExceptions {
		try {
			// Calculo la fecha de la firma
			String sql = "UPDATE " + CenAnexosCuentasBancariasBean.T_NOMBRETABLA ;
						if(anexoMandatosCuentasBancariasForm.getIdFichero()!=null && !anexoMandatosCuentasBancariasForm.getIdFichero().equals(""))
							sql += " SET IDFICHEROFIRMA = " + anexoMandatosCuentasBancariasForm.getIdFichero();
						else
							sql += " SET IDFICHEROFIRMA = null";
						
						sql +=" WHERE " + CenAnexosCuentasBancariasBean.C_IDINSTITUCION + " = " + anexoMandatosCuentasBancariasForm.getIdInstitucion() +
						" AND " + CenAnexosCuentasBancariasBean.C_IDPERSONA + " = " + anexoMandatosCuentasBancariasForm.getIdPersona() + 
						" AND " + CenAnexosCuentasBancariasBean.C_IDCUENTA + " = " + anexoMandatosCuentasBancariasForm.getIdCuenta() + 
						" AND " + CenAnexosCuentasBancariasBean.C_IDMANDATO + " = " + anexoMandatosCuentasBancariasForm.getIdMandato() +
						" AND " + CenAnexosCuentasBancariasBean.C_IDANEXO + " = " + anexoMandatosCuentasBancariasForm.getIdAnexo();
			
			Row row = new Row();									
			return (row.updateSQL(sql)>0);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}		
	}	
}