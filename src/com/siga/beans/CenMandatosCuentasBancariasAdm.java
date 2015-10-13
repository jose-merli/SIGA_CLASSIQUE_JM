package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.MandatosCuentasBancariasForm;
import com.siga.general.EjecucionPLs;

public class CenMandatosCuentasBancariasAdm extends MasterBeanAdministrador {
	
	private StringBuilder sqlMandatosSelect = new StringBuilder();
	private StringBuilder sqlMandatosFrom = new StringBuilder();
	private StringBuilder sqlMandatosWhere = new StringBuilder();	
						
	public CenMandatosCuentasBancariasAdm(UsrBean usuario) {
	    super(CenMandatosCuentasBancariasBean.T_NOMBRETABLA, usuario);
	    
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDMANDATO);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_TIPOMANDATO);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_TIPOPAGO);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ESQUEMA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_AUTORIZACIONB2B);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_TIPOID);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_ID);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_NOMBRE);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_DOMICILIO);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_CODIGOPOSTAL);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPAIS);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_PAIS);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPROVINCIA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_PROVINCIA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPOBLACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_ACREEDOR_POBLACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_TIPOID);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_ID);
	    sql.append(", CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_FECHABAJA);
	    sql.append(", CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_TITULAR);
	    sql.append(" AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_NOMBRE);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_DOMICILIO);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_CODIGOPOSTAL);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_IDPAIS);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_PAIS);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_IDPROVINCIA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_PROVINCIA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_IDPOBLACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_DEUDOR_POBLACION);
	    sql.append(", F_SIGA_GETNCOL_NCOM(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
	    sql.append(") AS DEUDOR_NCOLEGIADO, TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA);
	    sql.append(", TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAUSO);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAUSO);
	    sql.append(", CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_IBAN);
	    sql.append(" AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_IBAN);
	    sql.append(", BANCOS.");
	    sql.append(CenBancosBean.C_BIC);
	    sql.append(" AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_BIC);
	    sql.append(", BANCOS.");
	    sql.append(CenBancosBean.C_NOMBRE);
	    sql.append(" AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_BANCO);
	    sql.append(", TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHACREACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_USUCREACION);
	    sql.append(", TO_CHAR(MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION);
	    sql.append(", 'DD/MM/YYYY') AS ");
	    sql.append(CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION);
	    sql.append(", MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_USUMODIFICACION);	    
	    this.sqlMandatosSelect = sql;
	    
	    sql = new StringBuilder();
	    sql.append(" FROM ");
	    sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
	    sql.append(" MANDATOS, ");
	    sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
	    sql.append(" CUENTAS, ");
	    sql.append(CenBancosBean.T_NOMBRETABLA);
	    sql.append(" BANCOS ");
	    this.sqlMandatosFrom = sql;
	    
	    sql = new StringBuilder();
	    sql.append(" WHERE CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(" = MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
	    sql.append(" AND CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_IDPERSONA);
	    sql.append(" = MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
	    sql.append(" AND CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_IDCUENTA);
	    sql.append(" = MANDATOS.");
	    sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
	    sql.append(" AND BANCOS.");
	    sql.append(CenBancosBean.C_CODIGO);
	    sql.append(" = CUENTAS.");
	    sql.append(CenCuentasBancariasBean.C_CBO_CODIGO);
	    this.sqlMandatosWhere = sql;
	}

	protected String[] getCamposBean() {
		String[] campos = {
			CenMandatosCuentasBancariasBean.C_IDINSTITUCION, 
			CenMandatosCuentasBancariasBean.C_IDPERSONA, 
			CenMandatosCuentasBancariasBean.C_IDCUENTA, 
			CenMandatosCuentasBancariasBean.C_IDMANDATO,
			CenMandatosCuentasBancariasBean.C_TIPOMANDATO,
			CenMandatosCuentasBancariasBean.C_FECHACREACION,
			CenMandatosCuentasBancariasBean.C_USUCREACION,
			CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA,
			CenMandatosCuentasBancariasBean.C_TIPOPAGO, 
			CenMandatosCuentasBancariasBean.C_ESQUEMA,
			CenMandatosCuentasBancariasBean.C_AUTORIZACIONB2B,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_TIPOID,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_ID,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_NOMBRE, 
			CenMandatosCuentasBancariasBean.C_ACREEDOR_DOMICILIO, 
			CenMandatosCuentasBancariasBean.C_ACREEDOR_CODIGOPOSTAL, 
			CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPAIS,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_PAIS, 
			CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPROVINCIA,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_PROVINCIA,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPOBLACION,
			CenMandatosCuentasBancariasBean.C_ACREEDOR_POBLACION,
			CenMandatosCuentasBancariasBean.C_DEUDOR_TIPOID,
			CenMandatosCuentasBancariasBean.C_DEUDOR_ID,
//			CenMandatosCuentasBancariasBean.C_DEUDOR_NOMBRE,
			CenMandatosCuentasBancariasBean.C_DEUDOR_DOMICILIO, 
			CenMandatosCuentasBancariasBean.C_DEUDOR_CODIGOPOSTAL, 
			CenMandatosCuentasBancariasBean.C_DEUDOR_IDPAIS,
			CenMandatosCuentasBancariasBean.C_DEUDOR_PAIS, 
			CenMandatosCuentasBancariasBean.C_DEUDOR_IDPROVINCIA,
			CenMandatosCuentasBancariasBean.C_DEUDOR_PROVINCIA,
			CenMandatosCuentasBancariasBean.C_DEUDOR_IDPOBLACION,
			CenMandatosCuentasBancariasBean.C_DEUDOR_POBLACION,
			CenMandatosCuentasBancariasBean.C_FIRMA_FECHA, 
			CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR, 
			CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA,
			CenMandatosCuentasBancariasBean.C_FECHAUSO, 			
//			CenMandatosCuentasBancariasBean.C_IBAN, 
//			CenMandatosCuentasBancariasBean.C_BIC,
//			CenMandatosCuentasBancariasBean.C_BANCO,
			CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION,
			CenMandatosCuentasBancariasBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean() {
		String[] claves = {
			CenMandatosCuentasBancariasBean.C_IDINSTITUCION, 
			CenMandatosCuentasBancariasBean.C_IDPERSONA, 
			CenMandatosCuentasBancariasBean.C_IDCUENTA, 
			CenMandatosCuentasBancariasBean.C_IDMANDATO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenMandatosCuentasBancariasBean bean = new CenMandatosCuentasBancariasBean();;

		try	{
			bean.setIdInstitucion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_IDINSTITUCION)); 
			bean.setIdPersona(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_IDPERSONA)); 
			bean.setIdCuenta(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_IDCUENTA)); 
			bean.setIdMandato(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_IDMANDATO));
			bean.setTipoMandato(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_TIPOMANDATO));
			bean.setFechaCreacion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_FECHACREACION));
			bean.setUsuCreacion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_USUCREACION));
			bean.setRefMandatoSepa(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA));
			bean.setTipoPago(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_TIPOPAGO)); 
			bean.setEsquema(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ESQUEMA));
			bean.setAutorizacionB2B(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_AUTORIZACIONB2B));
			bean.setAcreedorTipoId(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_TIPOID));
			bean.setAcreedorId(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_ID));
			bean.setAcreedorNombre(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_NOMBRE)); 
			bean.setAcreedorDomicilio(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_DOMICILIO)); 
			bean.setAcreedorCodigoPostal(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_CODIGOPOSTAL)); 
			bean.setAcreedorIdPais(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPAIS));
			bean.setAcreedorPais(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_PAIS));
			bean.setAcreedorIdProvincia(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPROVINCIA));
			bean.setAcreedorProvincia(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_PROVINCIA));
			bean.setAcreedorIdPoblacion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPOBLACION));
			bean.setAcreedorPoblacion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_ACREEDOR_POBLACION));
			bean.setDeudorTipoId(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_TIPOID));
			bean.setDeudorId(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_ID));
			bean.setDeudorNombre(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_NOMBRE));
			bean.setDeudorDomicilio(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_DOMICILIO)); 
			bean.setDeudorCodigoPostal(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_CODIGOPOSTAL)); 
			bean.setDeudorIdPais(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_IDPAIS));
			bean.setDeudorPais(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_PAIS)); 
			bean.setDeudorIdProvincia(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_IDPROVINCIA));
			bean.setDeudorProvincia(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_PROVINCIA));
			bean.setDeudorIdPoblacion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_IDPOBLACION));
			bean.setDeudorPoblacion(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_DEUDOR_POBLACION));
			bean.setFirmaFecha(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_FIRMA_FECHA));
			bean.setIdFicheroFirma(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA));
			bean.setFirmaLugar(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR)); 
			bean.setFechaUso(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_FECHAUSO)); 			
			bean.setIban(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_IBAN)); 
			bean.setBic(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_BIC));
			bean.setBanco(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_BANCO));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenMandatosCuentasBancariasBean.C_USUMODIFICACION));			
			
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htMandato = null;

		try {
			
			htMandato = new Hashtable();
			CenMandatosCuentasBancariasBean beanMandato = (CenMandatosCuentasBancariasBean) bean;
			
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_IDINSTITUCION, beanMandato.getIdInstitucion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_IDPERSONA, beanMandato.getIdPersona());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_IDCUENTA, beanMandato.getIdCuenta());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_IDMANDATO, beanMandato.getIdMandato());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_TIPOMANDATO, beanMandato.getTipoMandato());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_FECHACREACION, beanMandato.getFechaCreacion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_USUCREACION, beanMandato.getUsuCreacion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA, beanMandato.getRefMandatoSepa());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_TIPOPAGO, beanMandato.getTipoPago());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ESQUEMA, beanMandato.getEsquema());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_AUTORIZACIONB2B, beanMandato.getAutorizacionB2B());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_TIPOID, beanMandato.getAcreedorTipoId());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_ID, beanMandato.getAcreedorId());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_NOMBRE, beanMandato.getAcreedorNombre());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_DOMICILIO, beanMandato.getAcreedorDomicilio());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_CODIGOPOSTAL, beanMandato.getAcreedorCodigoPostal());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPAIS, beanMandato.getAcreedorIdPais());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_PAIS, beanMandato.getAcreedorPais());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPROVINCIA, beanMandato.getAcreedorIdProvincia());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_PROVINCIA, beanMandato.getAcreedorProvincia());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPOBLACION, beanMandato.getAcreedorIdPoblacion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_ACREEDOR_POBLACION, beanMandato.getAcreedorPoblacion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_TIPOID, beanMandato.getDeudorTipoId());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_ID, beanMandato.getDeudorId());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_NOMBRE, beanMandato.getDeudorNombre());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_DOMICILIO, beanMandato.getDeudorDomicilio());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_CODIGOPOSTAL, beanMandato.getDeudorCodigoPostal());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_IDPAIS, beanMandato.getDeudorIdPais());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_PAIS, beanMandato.getDeudorPais());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_IDPROVINCIA, beanMandato.getDeudorIdProvincia());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_PROVINCIA, beanMandato.getDeudorProvincia());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_IDPOBLACION, beanMandato.getDeudorIdPoblacion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_DEUDOR_POBLACION, beanMandato.getDeudorPoblacion());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_FIRMA_FECHA, beanMandato.getFirmaFecha());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA, beanMandato.getIdFicheroFirma());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR, beanMandato.getFirmaLugar());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_FECHAUSO, beanMandato.getFechaUso());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_IBAN, beanMandato.getIban());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_BIC, beanMandato.getBic());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_BANCO, beanMandato.getBanco());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION, beanMandato.getFechaMod());
			UtilidadesHash.set(htMandato, CenMandatosCuentasBancariasBean.C_USUMODIFICACION, beanMandato.getUsuMod());
			
		} catch (Exception e) {
			htMandato = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htMandato;
	}

    protected String[] getOrdenCampos() {
        return null;
    } 
    
    /**
     * Obtengo los mandatos de una cuenta
     * @param idInstitucion
     * @param idPersona
     * @param idCuenta
     * @return
     * @throws ClsExceptions
     */
	public Vector<CenMandatosCuentasBancariasBean> obtenerMandatos(Integer idInstitucion, Long idPersona, Integer idCuenta) throws ClsExceptions {
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS UNION CEN_CUENTASBANCARIAS UNION CEN_BANCOS
			StringBuffer sql = new StringBuffer();
			sql.append(this.sqlMandatosSelect);
			sql.append(this.sqlMandatosFrom);
			sql.append(this.sqlMandatosWhere);
			sql.append(" AND MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(idInstitucion);
			sql.append(" AND MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(idPersona); 
			sql.append(" AND MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(idCuenta);
			
			Vector<CenMandatosCuentasBancariasBean> vMandatos = null;
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql.toString())) {        	   
				vMandatos = new Vector<CenMandatosCuentasBancariasBean>();
    			for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					CenMandatosCuentasBancariasBean mandatoBean = (CenMandatosCuentasBancariasBean) this.hashTableToBean(fila.getRow());					
					vMandatos.add(mandatoBean);
				}
            }		
			
			return vMandatos;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}      		 
	
	
	private String getSqlObtenerMandato(CenMandatosCuentasBancariasBean beanMandato, Boolean isFirmado){
		StringBuffer sql = new StringBuffer();
		sql.append(this.sqlMandatosSelect);
		sql.append(this.sqlMandatosFrom);
		sql.append(this.sqlMandatosWhere);
		sql.append(" AND  MANDATOS.");
		sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
		sql.append(" = ");
		sql.append(beanMandato.getIdInstitucion());
		if(beanMandato.getIdPersona()!=null&& !beanMandato.getIdPersona().equals("")){
			sql.append(" AND  MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(beanMandato.getIdPersona());
		}
		if(beanMandato.getRefMandatoSepa()!=null && !beanMandato.getRefMandatoSepa().equals("")){
			sql.append(" AND  MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA);
			sql.append(" = '");
			sql.append(beanMandato.getRefMandatoSepa());
			sql.append("'");
		}
		
		if(beanMandato.getIdCuenta()!=null&& !beanMandato.getIdCuenta().equals("")){
			sql.append(" AND  MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanMandato.getIdCuenta());
		}
		if(beanMandato.getIdMandato()!=null&& !beanMandato.getIdMandato().equals("")){
			sql.append(" AND  MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanMandato.getIdMandato());
		}else{
			if(isFirmado!=null){
				if(isFirmado){
					sql.append(" AND  MANDATOS.");
					sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
					sql.append(" IS NOT NULL ");
				}else{
					sql.append(" AND  MANDATOS.");
					sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
					sql.append(" IS  NULL ");
					
					
				}
			}	
		}
		
		
		
		return sql.toString();
	} 
	
	
    /**
     * Obtengo los datos de un mandato
     * @param beanMandato
     * @return
     * @throws ClsExceptions
     */
	public CenMandatosCuentasBancariasBean obtenerMandato(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS UNION CEN_CUENTASBANCARIAS UNION CEN_BANCOS
			
			RowsContainer rc = new RowsContainer(); 												
		
			if (rc.find(getSqlObtenerMandato(beanMandato,null)) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				beanMandato = (CenMandatosCuentasBancariasBean) this.hashTableToBean(fila.getRow());
            }		
			
			return beanMandato;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}
	public Hashtable<String,Object> getMandato(CenMandatosCuentasBancariasBean beanMandato,Boolean isFirmado) throws ClsExceptions {
		Hashtable<String,Object> mandatoHashtable = null;
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS UNION CEN_CUENTASBANCARIAS UNION CEN_BANCOS
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(getSqlObtenerMandato(beanMandato,isFirmado)) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				mandatoHashtable = fila.getRow();
            }		
			
			return mandatoHashtable;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}     	
	/**
	 * 
	 * @param beanMandato
	 * @param isFirmado
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String,Object>> getMandatos(CenMandatosCuentasBancariasBean beanMandato,Boolean isFirmado) throws ClsExceptions {
		List<Hashtable<String,Object>> mandatosList = null;
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS UNION CEN_CUENTASBANCARIAS UNION CEN_BANCOS
			RowsContainer rc = new RowsContainer(); 												
			mandatosList = new ArrayList<Hashtable<String,Object>>();
			if (rc.find(getSqlObtenerMandato(beanMandato,isFirmado)) && rc.size()>0) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String,Object> hMandato = fila.getRow();
					String sFechaBaja = (String) UtilidadesHash.getString(hMandato, CenCuentasBancariasBean.C_FECHABAJA);
					if (sFechaBaja==null || sFechaBaja.equals(""))
						mandatosList.add(hMandato);
				}
            }		
			
			return mandatosList;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}     	
	
	/**
	 * Actualizo los campos que se pueden modificar en CEN_MANDATOS_CUENTASBANCARIAS
	 * @param beanMandato
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean modificarMandato(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		try {
			String sql = "UPDATE " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA +
						" SET " + CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA + " = '" + beanMandato.getRefMandatoSepa() +  "', " +
							CenMandatosCuentasBancariasBean.C_ESQUEMA + " = " + beanMandato.getEsquema() +  ", " +							
							CenMandatosCuentasBancariasBean.C_AUTORIZACIONB2B + " = " + beanMandato.getAutorizacionB2B() +  ", " +
							CenMandatosCuentasBancariasBean.C_DEUDOR_TIPOID + " = '" + beanMandato.getDeudorTipoId() +  "', " +
							CenMandatosCuentasBancariasBean.C_DEUDOR_ID + " = '" + beanMandato.getDeudorId() +  "', " +
							CenMandatosCuentasBancariasBean.C_USUMODIFICACION + " = " + this.usrbean.getUserName() + ", " +
							CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION + " = SYSDATE " +							
						" WHERE " + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanMandato.getIdInstitucion() +
							" AND " + CenMandatosCuentasBancariasBean.C_IDPERSONA + " = " + beanMandato.getIdPersona() + 
							" AND " + CenMandatosCuentasBancariasBean.C_IDCUENTA + " = " + beanMandato.getIdCuenta() +
							" AND " + CenMandatosCuentasBancariasBean.C_IDMANDATO + " = " + beanMandato.getIdMandato();
			
			Row row = new Row();									
			return (row.updateSQL(sql)>0);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}		
	}
	
	
	/**
	 * Actualizo los campos que se pueden modificar en CEN_MANDATOS_CUENTASBANCARIAS
	 * @param beanMandato
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean modificarFirmaMandato(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		try {
			// Calculo la fecha de la firma
			CenMandatosCuentasBancariasBean beanMandatoOld = (CenMandatosCuentasBancariasBean) (selectByPK(beanToHashTable(beanMandato))).get(0);
			
			
			if (beanMandato.getFirmaFecha()!=null && !beanMandato.getFirmaFecha().equals("")) {
				beanMandato.setFirmaFecha(GstDate.getApplicationFormatDate(this.usrbean.getLanguage(), beanMandato.getFirmaFecha()));
			}
			
											
			return update(beanMandato,beanMandatoOld);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}		
	}
	
	public boolean asociarFichero(MandatosCuentasBancariasForm mandatosCuentasBancariasForm) throws ClsExceptions {
		try {
			// Calculo la fecha de la firma
			String sql = "UPDATE " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA ;
						if(mandatosCuentasBancariasForm.getIdFichero()!=null && !mandatosCuentasBancariasForm.getIdFichero().equals(""))
							sql += " SET IDFICHEROFIRMA = " + mandatosCuentasBancariasForm.getIdFichero();
						else
							sql += " SET IDFICHEROFIRMA = null";
						
						sql +=" WHERE " + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + " = " + mandatosCuentasBancariasForm.getIdInstitucion() +
						" AND " + CenMandatosCuentasBancariasBean.C_IDPERSONA + " = " + mandatosCuentasBancariasForm.getIdPersona() + 
						" AND " + CenMandatosCuentasBancariasBean.C_IDCUENTA + " = " + mandatosCuentasBancariasForm.getIdCuenta() + 
						" AND " + CenMandatosCuentasBancariasBean.C_IDMANDATO + " = " + mandatosCuentasBancariasForm.getIdMandato();
			
			Row row = new Row();									
			return (row.updateSQL(sql)>0);
							
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}		
	}	
	
	/**
	 * Actualiza la referencia del mandato para SEPA
	 * @param beanMandato
	 * @return
	 * @throws ClsExceptions
	 */
	public void actualizarReferenciaMandatoSEPA(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		String sReferenciaMandato = EjecucionPLs.ejecutarRevisarCaracteresSEPA(beanMandato.getRefMandatoSepa());
		beanMandato.setRefMandatoSepa(sReferenciaMandato);
	}
	
	
    /**
     * Consultar la existencia de un mandato con un identificador de mandato para SEPA (que no sea el actual)
	 * @param beanMandato
	 * @return
	 * @throws ClsExceptions
     */
	public boolean consultarMandatoSEPA(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		try {
			String sql = "SELECT 1 " +  
							" FROM " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + " MANDATOS " +
							" WHERE MANDATOS." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + " = " + beanMandato.getIdInstitucion() + 
								" AND MANDATOS." + CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA + " = '" + beanMandato.getRefMandatoSepa() + "' " +
								" AND NOT (" +
									" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDPERSONA + " = " + beanMandato.getIdPersona() + 
									" AND MANDATOS." + CenMandatosCuentasBancariasBean.C_IDCUENTA + " = " + beanMandato.getIdCuenta() +
									" AND MANDATOS." + CenMandatosCuentasBancariasBean.C_IDMANDATO + " = " + beanMandato.getIdMandato() + 
								" ) ";
			
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql)) {
				return (rc.size() > 0);
            }		
			
			return false;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}   	
}