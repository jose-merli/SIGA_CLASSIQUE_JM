package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.MandatosCuentasBancariasForm;
import com.siga.general.EjecucionPLs;

public class CenMandatosCuentasBancariasAdm extends MasterBeanAdministrador {
	
	private String sqlMandatosSelect = "SELECT MANDATOS." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDPERSONA + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDCUENTA + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDMANDATO + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_TIPOMANDATO + ", " +										
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_TIPOPAGO + ", " +										
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ESQUEMA + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_AUTORIZACIONB2B + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_TIPOID + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_ID + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_NOMBRE + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_DOMICILIO + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_CODIGOPOSTAL + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPAIS + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_PAIS + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPROVINCIA + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_PROVINCIA + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_IDPOBLACION + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_ACREEDOR_POBLACION + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_TIPOID + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_ID + ", " + 
										" CUENTAS." + CenCuentasBancariasBean.C_TITULAR + " AS " + CenMandatosCuentasBancariasBean.C_DEUDOR_NOMBRE + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_DOMICILIO + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_CODIGOPOSTAL + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_IDPAIS + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_PAIS + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_IDPROVINCIA + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_PROVINCIA + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_IDPOBLACION + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_DEUDOR_POBLACION + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + ", 'DD/MM/YYYY') AS " + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR + ", " +  
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_IDFICHEROFIRMA + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FECHAUSO + ", 'DD/MM/YYYY') AS " + CenMandatosCuentasBancariasBean.C_FECHAUSO + ", " +
										" CUENTAS." + CenCuentasBancariasBean.C_IBAN + " AS " + CenMandatosCuentasBancariasBean.C_IBAN + ", " +
										" BANCOS." + CenBancosBean.C_BIC + " AS " + CenMandatosCuentasBancariasBean.C_BIC + ", " + 
										" BANCOS." + CenBancosBean.C_NOMBRE + " AS " + CenMandatosCuentasBancariasBean.C_BANCO + ", " + 
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FECHACREACION + ", 'DD/MM/YYYY') AS " + CenMandatosCuentasBancariasBean.C_FECHACREACION + ", " +
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_USUCREACION + ", " +
										" TO_CHAR(MANDATOS." + CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION + ", 'DD/MM/YYYY') AS " + CenMandatosCuentasBancariasBean.C_FECHAMODIFICACION + ", " + 
										" MANDATOS." + CenMandatosCuentasBancariasBean.C_USUMODIFICACION;
	
	
	private String sqlMandatosFrom = " FROM " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + " MANDATOS, " +
										CenCuentasBancariasBean.T_NOMBRETABLA + " CUENTAS, " +
										CenBancosBean.T_NOMBRETABLA + " BANCOS ";
	
	private String sqlMandatosWhere = " WHERE CUENTAS." + CenCuentasBancariasBean.C_IDINSTITUCION + " = MANDATOS." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION +
										" AND CUENTAS." + CenCuentasBancariasBean.C_IDPERSONA + " = MANDATOS." + CenMandatosCuentasBancariasBean.C_IDPERSONA +
										" AND CUENTAS." + CenCuentasBancariasBean.C_IDCUENTA + " = MANDATOS." + CenMandatosCuentasBancariasBean.C_IDCUENTA +
										" AND BANCOS." + CenBancosBean.C_CODIGO + " = CUENTAS." + CenCuentasBancariasBean.C_CBO_CODIGO;	
						
	public CenMandatosCuentasBancariasAdm(UsrBean usuario) {
	    super(CenMandatosCuentasBancariasBean.T_NOMBRETABLA, usuario);
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
			CenMandatosCuentasBancariasBean.C_DEUDOR_NOMBRE,
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
			CenMandatosCuentasBancariasBean.C_IBAN, 
			CenMandatosCuentasBancariasBean.C_BIC,
			CenMandatosCuentasBancariasBean.C_BANCO,
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
			String sql = this.sqlMandatosSelect + 
						this.sqlMandatosFrom + 
						this.sqlMandatosWhere +
							" AND MANDATOS." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND MANDATOS." + CenMandatosCuentasBancariasBean.C_IDPERSONA + " = " + idPersona + 
							" AND MANDATOS." + CenMandatosCuentasBancariasBean.C_IDCUENTA + " = " + idCuenta;
			
			Vector<CenMandatosCuentasBancariasBean> vMandatos = null;
			RowsContainer rc = new RowsContainer(); 												
			if (rc.find(sql)) {        	   
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
	
	
	private String getSqlObtenerMandato(CenMandatosCuentasBancariasBean beanMandato){
		StringBuffer sql = new StringBuffer();
		sql.append(this.sqlMandatosSelect);
		sql.append(this.sqlMandatosFrom);
		sql.append(this.sqlMandatosWhere);
		sql.append(" AND  MANDATOS.");
		sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
		sql.append(" = ");
		sql.append(beanMandato.getIdInstitucion());
		sql.append(" AND  MANDATOS.");
		sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
		sql.append(" = ");
		sql.append(beanMandato.getIdPersona());
		
		if(beanMandato.getIdCuenta()!=null){
			sql.append(" AND  MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(beanMandato.getIdCuenta());
		}
		if(beanMandato.getIdMandato()!=null){
			sql.append(" AND  MANDATOS.");
			sql.append(CenMandatosCuentasBancariasBean.C_IDMANDATO);
			sql.append(" = ");
			sql.append(beanMandato.getIdMandato());
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
		
			if (rc.find(getSqlObtenerMandato(beanMandato)) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				beanMandato = (CenMandatosCuentasBancariasBean) this.hashTableToBean(fila.getRow());
            }		
			
			return beanMandato;
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	}
	public Hashtable getMandato(CenMandatosCuentasBancariasBean beanMandato) throws ClsExceptions {
		Hashtable mandatoHashtable = null;
		try {
			// JPT: CEN_MANDATOS_CUENTASBANCARIAS UNION CEN_CUENTASBANCARIAS UNION CEN_BANCOS
			
			
			RowsContainer rc = new RowsContainer(); 												
		
			if (rc.find(getSqlObtenerMandato(beanMandato)) && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				mandatoHashtable = fila.getRow();
            }		
			
			return mandatoHashtable;
			
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
			String sFirmaFecha = "NULL";
			if (beanMandato.getFirmaFecha()!=null && !beanMandato.getFirmaFecha().equals("")) {
				sFirmaFecha = "TO_DATE('" + beanMandato.getFirmaFecha() + "', 'DD/MM/YYYY')";
			}
			
			// Calculo el lugar de la firma
			String sFirmaLugar = "NULL";
			if (beanMandato.getFirmaLugar()!=null && !beanMandato.getFirmaLugar().equals("")) {
				sFirmaLugar = "'" + beanMandato.getFirmaLugar() + "'";
			}
			
			String sql = "UPDATE " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA +
						" SET " + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + " = " + sFirmaFecha + ", " +
							CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR + " = " + sFirmaLugar +  ", " +
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