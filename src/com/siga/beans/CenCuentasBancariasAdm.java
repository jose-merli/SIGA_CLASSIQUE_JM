/*
 * Created on Dec 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
// RGG CAMBIO VISIBILIDAD public class CenCuentasBancariasAdm extends MasterBeanAdministrador {
public class CenCuentasBancariasAdm extends MasterBeanAdmVisible {
 
	/**
	 * @param tabla
	 * @param usuario
	 */
	public CenCuentasBancariasAdm(UsrBean usuario) {
		super( CenCuentasBancariasBean.T_NOMBRETABLA, usuario);
	}	
	
	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenCuentasBancariasAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenCuentasBancariasBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	

	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_CUENTASBANCARIAS 
	 * @author nuria.rgonzalez 14-12-04	 
	 */
	protected String[] getCamposBean() {
		String[] campos = {CenCuentasBancariasBean.C_IDINSTITUCION,	
				CenCuentasBancariasBean.C_IDPERSONA,			CenCuentasBancariasBean.C_IDCUENTA,
				CenCuentasBancariasBean.C_ABONOCARGO,			CenCuentasBancariasBean.C_ABONOSJCS,	
				CenCuentasBancariasBean.C_CBO_CODIGO,			CenCuentasBancariasBean.C_CODIGOSUCURSAL,	
				CenCuentasBancariasBean.C_DIGITOCONTROL,		CenCuentasBancariasBean.C_NUMEROCUENTA,		
				CenCuentasBancariasBean.C_TITULAR,				CenCuentasBancariasBean.C_FECHABAJA,	
				CenCuentasBancariasBean.C_CUENTACONTABLE,		CenCuentasBancariasBean.C_FECHAMODIFICACION,
				CenCuentasBancariasBean.C_USUMODIFICACION,		CenCuentasBancariasBean.C_IBAN};
		return campos;
	}

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_CUENTASBANCARIAS 
	 * @author nuria.rgonzalez 14-12-04	 
	 */
	protected String[] getClavesBean() {
		String[] campos = {CenCuentasBancariasBean.C_IDINSTITUCION,
				CenCuentasBancariasBean.C_IDPERSONA,	CenCuentasBancariasBean.C_IDCUENTA};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/**
	 * Devuelve un CenCuentasBancariasBean con los campos de la tabla CEN_CUENTASBANCARIAS 
	 * @author nuria.rgonzalez 14-12-04	 
	 * @param Hashtable 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenCuentasBancariasBean bean = null;
		try{
			bean = new CenCuentasBancariasBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenCuentasBancariasBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenCuentasBancariasBean.C_IDPERSONA));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,CenCuentasBancariasBean.C_IDCUENTA));
			bean.setAbonoCargo(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_ABONOCARGO));
			bean.setAbonoSJCS(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_ABONOSJCS));
			bean.setCbo_Codigo(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_CBO_CODIGO));
			bean.setDigitoControl(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_DIGITOCONTROL));
			bean.setCodigoSucursal(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_CODIGOSUCURSAL));
			bean.setNumeroCuenta(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_NUMEROCUENTA));
			bean.setTitular(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_TITULAR));			
			bean.setFechaBaja(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_FECHABAJA));
			bean.setCuentaContable(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_CUENTACONTABLE));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenCuentasBancariasBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenCuentasBancariasBean.C_USUMODIFICACION));
			bean.setIban(UtilidadesHash.getString(hash, CenCuentasBancariasBean.C_IBAN));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_CUENTASBANCARIAS 
	 * @author nuria.rgonzalez 14-12-04	 
	 * @param CenCuentasBancariasBean 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenCuentasBancariasBean  b = (CenCuentasBancariasBean ) bean;
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_IDCUENTA, b.getIdCuenta());			
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_ABONOCARGO, b.getAbonoCargo());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_ABONOSJCS, b.getAbonoSJCS());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_CBO_CODIGO, b.getCbo_Codigo());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_CODIGOSUCURSAL, b.getCodigoSucursal());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_DIGITOCONTROL, b.getDigitoControl());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_NUMEROCUENTA, b.getNumeroCuenta());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_TITULAR, b.getTitular());			
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_FECHABAJA, b.getFechaBaja());
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_CUENTACONTABLE, b.getCuentaContable());	
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CenCuentasBancariasBean .C_USUMODIFICACION, b.getUsuMod());	
			UtilidadesHash.set(hash, CenCuentasBancariasBean.C_IBAN, String.valueOf(b.getIban()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	protected String[] getCamposCuentas() {
		String[] campos = {	
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION,	
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA,
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA,
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOCARGO,				
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOSJCS,	
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO,	
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL,		
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL,	
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA,			
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_TITULAR + " as TITULAR ",
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA,
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CUENTACONTABLE,
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN};
		return campos;
	}
	
	protected String getTablasCuentas() {		
		StringBuffer tablas = new StringBuffer();
		tablas.append(CenComponentesBean.T_NOMBRETABLA); 
		tablas.append(" RIGHT JOIN "); 
		tablas.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		tablas.append(" ON ");
		tablas.append(CenComponentesBean.T_NOMBRETABLA);
		tablas.append(".");
		tablas.append(CenComponentesBean.C_IDINSTITUCION);
		tablas.append(" = ");
		tablas.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		tablas.append(".");
		tablas.append(CenCuentasBancariasBean.C_IDINSTITUCION);
		tablas.append(" AND ");		
		tablas.append(CenComponentesBean.T_NOMBRETABLA);
		tablas.append(".");
		tablas.append(CenComponentesBean.C_IDPERSONA);
		tablas.append(" = ");			 	
		tablas.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		tablas.append(".");
		tablas.append(CenCuentasBancariasBean.C_IDPERSONA);
		tablas.append(" AND ");		
		tablas.append(CenComponentesBean.T_NOMBRETABLA);
		tablas.append(".");
		tablas.append(CenComponentesBean.C_IDCUENTA);
		tablas.append(" = ");			 	
		tablas.append(CenCuentasBancariasBean.T_NOMBRETABLA);
		tablas.append(".");
		tablas.append(CenCuentasBancariasBean.C_IDCUENTA);	
		return tablas.toString();
	}
	
	protected String[] getOrdenCuentas(){
		String[] campos = {"TITULAR"};
		return campos;
	}
		
	/**
	 * Devuelve un vector con los datos de las cuentas bancarias del cliente pasado como par�metro 
	 * y adem�s obtenemos todas las cuentas bancarias de las sociedades de las que el cliente es componente. 
	 * @author nuria.rgonzalez 18-01-05
	 * @version 2	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public Vector<Hashtable<String, Object>> selectCuentas(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica) throws ClsExceptions {
		Vector<Hashtable<String, Object>> vCuentas = null;
		try {			
			StringBuffer where = new StringBuffer();
			where.append(" WHERE ");
			where.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			where.append(".");
			where.append(CenCuentasBancariasBean.C_IDPERSONA);
			where.append(" = ");
			where.append(idPersona);
			where.append(" AND ");
			where.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			where.append(".");
			where.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			where.append(" = ");
			where.append(idInstitucion);
			
			if (!bIncluirRegistrosConBajaLogica) {
				where.append(" AND (");
				where.append(CenCuentasBancariasBean.T_NOMBRETABLA);
				where.append("." + CenCuentasBancariasBean.C_FECHABAJA);
				where.append(" IS NULL OR ");
				where.append(CenCuentasBancariasBean.T_NOMBRETABLA);
				where.append(".");
				where.append(CenCuentasBancariasBean.C_FECHABAJA);
				where.append(" > SYSDATE) ");
			}
			
			StringBuffer where2 = new StringBuffer();
			where2.append(" WHERE ");
			where2.append(CenComponentesBean.T_NOMBRETABLA);
			where2.append(".");
			where2.append(CenComponentesBean.C_CEN_CLIENTE_IDPERSONA);
			where2.append(" = ");
			where2.append(idPersona);
			where2.append(" AND ");
			where2.append(CenComponentesBean.T_NOMBRETABLA);
			where2.append(".");
			where2.append(CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION);
			where2.append(" = ");
			where2.append(idInstitucion);
			where2.append(" AND ");
			where2.append(CenComponentesBean.T_NOMBRETABLA);
			where2.append(".");
			where2.append(CenComponentesBean.C_FECHABAJA);
			where2.append(" IS NULL ");
			where2.append(" AND ");
			where2.append(CenComponentesBean.T_NOMBRETABLA);
			where2.append(".");
			where2.append(CenComponentesBean.C_SOCIEDAD);
			where2.append(" = '1' ");
			
			if (!bIncluirRegistrosConBajaLogica) {
				where2.append(" AND (");
				where2.append(CenCuentasBancariasBean.T_NOMBRETABLA);
				where2.append("." + CenCuentasBancariasBean.C_FECHABAJA);
				where2.append(" IS NULL OR ");
				where2.append(CenCuentasBancariasBean.T_NOMBRETABLA);
				where2.append(".");
				where2.append(CenCuentasBancariasBean.C_FECHABAJA);
				where2.append(" > SYSDATE) ");
			}
			
			StringBuffer sql = new StringBuffer();
			sql.append(UtilidadesBDAdm.sqlSelect(CenCuentasBancariasBean.T_NOMBRETABLA, this.getCamposCuentas()));
			sql.append(where);
			sql.append(" UNION ");
			sql.append(UtilidadesBDAdm.sqlSelect(this.getTablasCuentas(), this.getCamposCuentas()));
			sql.append(where2);
			sql.append(UtilidadesBDAdm.sqlOrderBy(this.getOrdenCuentas()));
			
			RowsContainer rc = this.find(sql.toString());
			if (rc!=null) {
				vCuentas = new Vector();
				for (int i=0; i<rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					if (htFila != null) 
						vCuentas.add(htFila);
				}
			}
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar selectCuentas");
		}
		
		return vCuentas;
	}
	
	/**
	 * Devuelve un bean de cuentas bancarias por defecto para utilizar como de cargo  
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public CenCuentasBancariasBean selectCuentaCargo(Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException{
		CenCuentasBancariasBean salida = null;
		try{			
			String where = " WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion + 
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOCARGO + " in ('T','C')"+
			" AND  " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA + " IS NULL";
		
			Vector v = this.select(where);
			if (v!=null && v.size()>0) {
				salida = (CenCuentasBancariasBean) v.get(0);
			} else {
				throw new SIGAException("certificados.boton.mensaje.noCuenta");
			}
		}
		catch(SIGAException e){			
			throw e;
		}
		catch(Exception e){			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return salida;
	}
	
	/**
	 * Devuelve un bean de cuentas bancarias por defecto para utilizar como de cargo  
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de al que vamos a obtener los datos. 
	 */	
	public CenCuentasBancariasBean selectPrimeraCuentaCargo(Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException{
		CenCuentasBancariasBean salida = null;
		try{			
			String where = " WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion + 
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA+ " IS NULL " + 
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOCARGO + " in ('T','C')" +
			" AND ROWNUM<2 ";
		
			Vector v = this.select(where);
			if (v!=null && v.size()>0) {
				salida = (CenCuentasBancariasBean) v.get(0);
			}
		}
		catch(Exception e){			
			throw new ClsExceptions (e, e.toString());
		}
		return salida;
	}
	
	public ArrayList getCuentasAbono(Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException{
		ArrayList alCuentasAbono = null;
		try{			
			String where = " WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion + 
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA+ " IS NULL " + 
			" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOCARGO + " in ('T','A')" ;
			//" AND ROWNUM<2 ";
		
			Vector vCuentasAbono = this.select(where);
			alCuentasAbono = new ArrayList();
			for (int i = 0; i < vCuentasAbono.size(); i++) {
				CenCuentasBancariasBean salida = (CenCuentasBancariasBean) vCuentasAbono.get(i);
				alCuentasAbono.add(salida.getIdCuenta());
			}
			
		}
		catch(Exception e){			
			throw new ClsExceptions (e, e.toString());
		}
		return alCuentasAbono;
	}
	
	/**
	 * Consulta las cuentas bancarias activas de cargos de la persona
	 * @param idPersona
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public ArrayList getCuentasCargo (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException {
		ArrayList alCuentasCargo = null;
		try{			
			String where = " WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
					" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion + 
					" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA + " IS NULL " + 
					" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOCARGO + " in ('T','C')" ;
		
			Vector vCuentasCargo = this.select(where);
			alCuentasCargo = new ArrayList();
			for (int i = 0; i < vCuentasCargo.size(); i++) {
				CenCuentasBancariasBean salida = (CenCuentasBancariasBean) vCuentasCargo.get(i);
				alCuentasCargo.add(salida);
			}
			
		} catch(Exception e){			
			throw new ClsExceptions (e, e.toString());
		}
		
		return alCuentasCargo;
	}
	
	/**
	 * Devuelve un Hastable con los datos de la cuenta bancaria del cliente
	 * @param idPersona
	 * @param idInstitucion
	 * @param idCuenta
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable selectCuentas (Long idPersona, Integer idInstitucion, Integer idCuenta) throws ClsExceptions, SIGAException{
		Hashtable registro = null;
		try{			
			String sql = UtilidadesBDAdm.sqlSelect(CenCuentasBancariasBean.T_NOMBRETABLA, this.getCamposCuentas());
			
			sql += " WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
					" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
					" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + idCuenta;	
			
			RowsContainer rc = this.find(sql);
			if (rc!=null && rc.size() == 1) {
				Row fila = (Row) rc.get(0);
				registro = (Hashtable)fila.getRow(); 					
			}
			
		} catch(Exception e) {			
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		
		return registro;
	}
	
	/**
	 * Funcion que inserta un mandato nuevo
	 * @param beanCuentas
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean insert(CenCuentasBancariasBean beanCuentas) throws ClsExceptions, SIGAException {
		try {
			// Obtiene el nuevo idCuenta
			if (beanCuentas.getIdCuenta()==null) {
				beanCuentas.setIdCuenta(this.getNuevoID(beanCuentas));
			}
			
			// Inserta la cuenta
			if (this.insert(this.beanToHashTable(beanCuentas))) {
				
				// Tratamiento de mandatos cuando tenga el abono del cargo
				if (beanCuentas.getAbonoCargo() != null && (beanCuentas.getAbonoCargo().equals("C") || beanCuentas.getAbonoCargo().equals("T"))) {
					
					// Compruebo que no tiene mandatos asociados la cuenta
					CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(this.usrbean);
					Vector<CenMandatosCuentasBancariasBean> vMandatos = mandatosAdm.obtenerMandatos(beanCuentas.getIdInstitucion(), beanCuentas.getIdPersona(), beanCuentas.getIdCuenta());			
					if (vMandatos==null || vMandatos.size()==0) {
						
						// Se insertan dos mandatos nuevos a la cuenta, uno para productos y otro para servicios
						Object[] paramMandatos = new Object[4];
						paramMandatos[0] = beanCuentas.getIdInstitucion().toString();
						paramMandatos[1] = beanCuentas.getIdPersona().toString();
						paramMandatos[2] = beanCuentas.getIdCuenta().toString();
						paramMandatos[3] = this.usrbean.getUserName().toString();
						
						String resultado[] = new String[2];
						resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.InsertarMandatos(?,?,?,?,?,?)}", 2, paramMandatos);
						if (resultado == null) {
							throw new ClsExceptions ("Error al insertar los mandatos de las cuentas");
							
						} else {
							if (resultado[0].equals("1")) {
								throw new SIGAException ("messages.censo.direcciones.facturacion");
								
							} else if (resultado[0].equals("2")) {
								throw new SIGAException ("messages.censo.direcciones.facturacion");
								
							} else if (!resultado[0].equals("0")) {
								throw new ClsExceptions ("Error al insertar los mandatos de las cuentas");
							}
						}
					}
				}	
				
				return true;
			}
			
			return false;
			
		} catch (SIGAException e) {
			throw e;			
					
		} catch (Exception e)	{
			throw new ClsExceptions (e,  e.getMessage());
		}
	}	

	/**
	 * Inserta los datos de una cuenta bancaria y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 21-01-05
	 * @version 1	 
	 * @param beanDir datos de la cuenta
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean insertarConHistorico (CenCuentasBancariasBean beanCuentas, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			// Insertamos la direccion
			beanCuentas.setIdCuenta(this.getNuevoID(beanCuentas));
			if (insert(beanCuentas)) {
				// Insertamos el historico
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCuentas, CenHistoricoAdm.ACCION_INSERT, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	
	/**
	 * Obtiene un nuevo ID del curriculum de una persona e institucion determinada
	 * @author daniel.campos 12-01-05
	 * @version 1	 
	 * @param Bean datos de la cuenta.
	 * @return nuevo ID.
	 */
	public Integer getNuevoID (CenCuentasBancariasBean bean)throws SIGAException, ClsExceptions 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + CenCuentasBancariasBean.C_IDCUENTA + ") + 1) AS " + CenCuentasBancariasBean.C_IDCUENTA + 
			  			 " FROM " + CenCuentasBancariasBean.T_NOMBRETABLA +
						 " WHERE "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDPERSONA +" = " + bean.getIdPersona() +
						 " AND "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDINSTITUCION +" = " + bean.getIdInstitucion();

			// RGG cambio visibilidad
			rc = this.findForUpdate(sql);
			if (rc!=null && rc.size()>0) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer idCuenta = UtilidadesHash.getInteger(prueba, CenCuentasBancariasBean.C_IDCUENTA);
				if (idCuenta == null) {
					return new Integer(1);
				}
				else return idCuenta;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;
	}	

	/**
	 * Inserta los datos de una cuenta manteniendo el registro anterior con fecha de baja.
	 * Adem�s incluye en la tabla de hist�ricos el alta y la baja que se han producido.
	 * @author BNS 11-12-12
	 * @version 1	 
	 * @param beanCuentas datos de la cuenta
	 * @param beanHis con el motivo y el tipo, para almacenar en el Historico
	 * @param userName
	 * @param usrBean 
	 * @param abonoCargoOrig
	 * @param idioma idioma actual
	 * @param bProcesoAltaCuentaCargos 
	 * @return int indicando el resultado de la operacion:
	 * 				-1: Error
	 * 				 0: OK
	 * 				 1: OK messages.updated.borrarCuenta
	 * 				 2: OK messages.updated.actualizarCuenta
	 */
	public int updateConHistoricoYfecBaj(CenCuentasBancariasBean beanCuentas, CenHistoricoBean beanHis, Integer userName, UsrBean usrBean, String abonoCargoOrig, String idioma, boolean bProcesoAltaCuentaCargos) throws ClsExceptions, SIGAException {
		int iResult = 0;
		Hashtable clavesCuenta = new Hashtable();
		try{
			UtilidadesHash.set (clavesCuenta, CenCuentasBancariasBean.C_IDINSTITUCION, beanCuentas.getIdInstitucion());
			UtilidadesHash.set (clavesCuenta, CenCuentasBancariasBean.C_IDPERSONA, beanCuentas.getIdPersona());
			UtilidadesHash.set (clavesCuenta, CenCuentasBancariasBean.C_IDCUENTA, beanCuentas.getIdCuenta());
			
			// Insertamos un nuevo registro con los datos actualizados
			if (!insertarConHistorico(beanCuentas, beanHis, idioma)){
				throw new ClsExceptions("Error al insertar el nuevo registro");
			}
			// Damos de baja el registro anterior
			if (!deleteConHistorico(clavesCuenta, beanHis, idioma)){
				throw new ClsExceptions("Error al borrar el registro anterior");
			}
			
			iResult = this.revisionesCuentas(beanCuentas, userName, usrBean, true, bProcesoAltaCuentaCargos);						
			
		} catch (SIGAException e) {
			iResult = -1;
			throw e;
			
		} catch (ClsExceptions e) {
			iResult = -1;
			throw e;
			
		} catch (Exception e){
			iResult = -1;
			throw new ClsExceptions (e, "Se ha producido un error al ejecutar la acci�n");
		}
		
		return iResult;
	}
	
	/**
	 * Este m�todo deber�a ser llamado al modificar cualquier cuenta porque realiza las revisiones generales de las cuentas
	 * @param beanCuentas
	 * @param userName
	 * @param usrBean
	 * @param comprobarSJCS
	 * @param bProcesoAltaCuentaCargos
	 * @return 0:OperacionRealizada; 
	 * @throws ClsExceptions
	 * @throws SIGAException 
	 */
	public int revisionesCuentas(CenCuentasBancariasBean beanCuentas, Integer userName, UsrBean usrBean, boolean comprobarSJCS, boolean bProcesoAltaCuentaCargos) throws ClsExceptions, SIGAException {
		
	Integer iResult = -1;
	
	try{

		//Si se ha marcado el check abono SJCS se comprueba si existe otra cuenta que ya es abono SJCS
		if((comprobarSJCS)&&(beanCuentas.getAbonoSJCS().equals(ClsConstants.DB_TRUE))){
			
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (usrBean);
			
			if (cuentasAdm.existeCuentaAbonoSJCS(beanCuentas.getIdPersona(), beanCuentas.getIdInstitucion(), beanCuentas.getIdCuenta())) {

				throw new SIGAException ("messages.censo.existeAbonoSJCS");
			}
		}
		
		// Tratamiento de mandatos cuando tenga el abono del cargo
		if (beanCuentas.getAbonoCargo() != null && (beanCuentas.getAbonoCargo().equals("C") || beanCuentas.getAbonoCargo().equals("T"))) {
			
			// Compruebo que no tiene mandatos asociados la cuenta
			CenMandatosCuentasBancariasAdm mandatosAdm = new CenMandatosCuentasBancariasAdm(usrBean);
			Vector<CenMandatosCuentasBancariasBean> vMandatos = mandatosAdm.obtenerMandatos(beanCuentas.getIdInstitucion(), beanCuentas.getIdPersona(), beanCuentas.getIdCuenta());			
			if (vMandatos==null || vMandatos.size()==0) {
				
				// Se insertan dos mandatos nuevos a la cuenta, uno para productos y otro para servicios
				Object[] paramMandatos = new Object[4];
				paramMandatos[0] = beanCuentas.getIdInstitucion().toString();
				paramMandatos[1] = beanCuentas.getIdPersona().toString();
				paramMandatos[2] = beanCuentas.getIdCuenta().toString();
				paramMandatos[3] = userName.toString();
				
				String resultado[] = new String[2];
				resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.InsertarMandatos(?,?,?,?,?,?)}", 2, paramMandatos);
				if (resultado == null) {
					throw new ClsExceptions ("Error al insertar los mandatos de las cuentas");
					
				} else {
					if (resultado[0].equals("1")) {
						throw new SIGAException ("messages.censo.direcciones.facturacion");
						
					} else if (resultado[0].equals("2")) {
						throw new SIGAException ("messages.censo.direcciones.facturacion");
						
					} else if (!resultado[0].equals("0")) {
						throw new ClsExceptions ("Error al insertar los mandatos de las cuentas");
					}
				}
			}
		}
		
		// Lanzamos el proceso de revision de suscripciones del letrado 
		String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(""+beanCuentas.getIdInstitucion().toString(),
																				  ""+beanCuentas.getIdPersona().toString(),
																				  "",
																				  ""+userName);
		if ((resultado == null) || (!resultado[0].equals("0"))){
			throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO"+resultado[1]);
		}
		
		// Este proceso se encarga de actualizar las cosas pendientes asociadas a la cuenta de la persona 
		String[] resultado1 = EjecucionPLs.ejecutarPL_Revision_Cuenta(
			""+beanCuentas.getIdInstitucion().toString(),
			  ""+beanCuentas.getIdPersona().toString(),
			  ""+beanCuentas.getIdCuenta().toString(),
			  ""+userName);
		if (resultado1 == null || !resultado1[0].equals("0")) {
			throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_CUENTA" + resultado[1]);
		}
		
		// Comprueba si va a lanzar el proceso que asocia las suscripciones activas con forma de pago en metalico a la nueva cuenta bancaria
		if (bProcesoAltaCuentaCargos) { 
			// Este proceso asocia las suscripciones activas con forma de pago en metalico a la nueva cuenta bancaria 
			resultado1 = EjecucionPLs.ejecutarPL_AltaCuentaCargos(
				""+beanCuentas.getIdInstitucion().toString(),
				  ""+beanCuentas.getIdPersona().toString(),
				  ""+beanCuentas.getIdCuenta().toString(),
				  ""+userName);
			if (resultado1 == null || !resultado1[0].equals("0")) {
				throw new ClsExceptions ("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_ALTA_CUENTA_CARGOS" + resultado[1]);
			}
		}		
		iResult = 0;
		
	} catch (SIGAException e) {
			throw e;
			
	} catch (Exception e) {			
		throw new ClsExceptions("Error en CenCuentasBancariasAdm.revisionesCuentas");		
	}
	
	return iResult;	
	
	}
	
	/**
	 * Actualiza los datos de una cuenta y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 21-01-05
	 * @version 1	 
	 * @param BeanDir datos de la cuenta
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (CenCuentasBancariasBean beanCuenta, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanCuenta), beanCuenta.getOriginalHash())) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCuenta, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al actualizar los datos en B.D.");
		}
	}
	
	/**
	 * Borrar los datos de una cuenta y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param Hash con las claves de la cuenta a borrar
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico.
	 */
	public boolean deleteConHistorico (Hashtable clavesCuenta, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			CenCuentasBancariasBean beanCuenta = (CenCuentasBancariasBean) this.selectByPK(clavesCuenta).get(0);
			if (delete(clavesCuenta)) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCuenta, CenHistoricoAdm.ACCION_DELETE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	
	/**
	 * Comprueba si existe una cuenta de tipo SJCS asociada a una idpersona e idIntitucion
	 * @author daniel.campos 25-01-05
	 * @version 1	 
	 * @param idPersona
	 * @param idInstitucion
	 * @return false si no exite, true si exite
	 */
	public boolean existeCuentaAbonoSJCS (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException {
		try {
			/*Hashtable claves = new Hashtable();
			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_ABONOSJCS, ClsConstants.DB_TRUE);
			Vector v = select(claves);
			if ((v != null) && (v.size() > 0)) {
				return true;
			}*/
			String where = " WHERE " + CenCuentasBancariasBean.C_IDINSTITUCION +  " = " + idInstitucion +
			 " AND " + CenCuentasBancariasBean.C_IDPERSONA +  " = " + idPersona +
			 " AND " + CenCuentasBancariasBean.C_ABONOSJCS +  " = " + ClsConstants.DB_TRUE+
			 " AND " +CenCuentasBancariasBean.C_FECHABAJA+" IS NULL";
			Vector v = this.select(where);
			if ((v != null) && (v.size() > 0))
			{
			return true;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar la funcion existeCuentaAbonoSJCS");
		}
		return false;
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idPersona
	 * @param alTiposCuenta Array de cadenas con los tipos de cuenta que queramos buscar
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getCuentaBancaria(String idInstitucion, String idPersona, ArrayList alTiposCuenta) throws ClsExceptions, SIGAException {
		String idCuentaPresentador = null;
		try {
			StringBuffer sql = new StringBuffer();
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			sql.append("SELECT CUENTA.");
			sql.append(CenCuentasBancariasBean.C_IDCUENTA);
			sql.append("  FROM ");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(" CUENTA, ");
			sql.append(CenBancosBean.T_NOMBRETABLA);
			sql.append(" BANCO WHERE CUENTA.");
			sql.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			sql.append(" = BANCO.");
			sql.append(CenBancosBean.C_CODIGO);
			sql.append(" AND CUENTA.");
			sql.append(CenCuentasBancariasBean.C_FECHABAJA);
			sql.append(" IS NULL");
			sql.append(" AND CUENTA.");
			sql.append(CenCuentasBancariasBean.C_IDPERSONA);
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" = :");
			sql.append(keyContador);
			sql.append(" AND CUENTA.");
			sql.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" = :");
			sql.append(keyContador);

			sql.append(" AND (");
			int size = alTiposCuenta.size();
			for (int i = 0; i < size; i++) {
				String tipoCuenta = (String)alTiposCuenta.get(i);
				if(i!=0)
					sql.append(" OR ");
				sql.append(" CUENTA.");
				sql.append(CenCuentasBancariasBean.C_ABONOCARGO);
				keyContador++;
				htCodigos.put(new Integer(keyContador), tipoCuenta);
				sql.append(" = :");
				sql.append(keyContador);
			}
			sql.append(" )");
			sql.append(" AND ROWNUM=1");
			RowsContainer rc = new RowsContainer();
			if(rc.findBind(sql.toString(),htCodigos)){
				if (rc.size()>0){	
					Row fila = (Row) rc.get(0);
					idCuentaPresentador = fila.getString(CenCuentasBancariasBean.C_IDCUENTA);


				}else{// Si no existe cuenta lanzamos la excepcion

					throw new SIGAException("certificados.boton.mensaje.noCuenta");
				}


			}else{
				throw new SIGAException("certificados.boton.mensaje.noCuenta");
				
			}



		}catch (SIGAException e) {
			throw new SIGAException ("certificados.boton.mensaje.noCuenta");
		}
		catch (Exception e) {
			throw new ClsExceptions("Error en BBDD.CenCuentasBancariasAdm.getCuentaBancaria");
		}
		return idCuentaPresentador;




	}

	/**
	 * Comprueba si existe una cuenta de tipo SJCS asociada a una idpersona e idIntitucion sin contar la pasada como par�metro.
	 * @author Luis Miguel S�nchez PI�A 06/09/2006
	 * @version 1	 
	 * @param idPersona
	 * @param idInstitucion
	 * @return false si no exite, true si exite
	 */
//	public boolean existeCuentaAbonoSJCS (Long idPersona, Integer idInstitucion, String sEntidad, String sSucursal, String sDigitoControl, String sNumeroCuenta) throws ClsExceptions, SIGAException
	public boolean existeCuentaAbonoSJCS (Long idPersona, Integer idInstitucion, Integer idCuenta) throws ClsExceptions, SIGAException
	{
		try
		{
//			Hashtable claves = new Hashtable();
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_IDINSTITUCION, idInstitucion);
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_IDPERSONA, idPersona);
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_ABONOSJCS, ClsConstants.DB_TRUE);
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_CBO_CODIGO, sEntidad);
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_CODIGOSUCURSAL, sSucursal);
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_DIGITOCONTROL, sDigitoControl);
//			UtilidadesHash.set (claves, CenCuentasBancariasBean.C_NUMEROCUENTA, sNumeroCuenta);
//			Vector v = select(claves);
	
			String where = " WHERE " + CenCuentasBancariasBean.C_IDINSTITUCION +  " = " + idInstitucion +
							 " AND " + CenCuentasBancariasBean.C_IDPERSONA +  " = " + idPersona +
							 " AND " + CenCuentasBancariasBean.C_IDCUENTA +  " <> " + idCuenta +
							 " AND " + CenCuentasBancariasBean.C_ABONOSJCS +  " = " + ClsConstants.DB_TRUE+
							 " AND " +CenCuentasBancariasBean.C_FECHABAJA+" IS NULL";
			Vector v = this.select(where);
			if ((v != null) && (v.size() > 0))
			{
				return true;
			}
		}

		catch (Exception e)
		{
			throw new ClsExceptions (e, "Error al ejecutar la funcion existeCuentaAbonoSJCS");
		}

		return false;
	}

	/** 
	 * Recoge la informacion del registro seleccionado a partir de sus claves para <br/>
	 * transacciones (for update)
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaCuenta (String idPersona, String idInstitucion, String idCuenta) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOCARGO + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_ABONOSJCS + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_TITULAR + "," +							
			    			CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA + "," +							
			    			CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CUENTACONTABLE + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHAMODIFICACION + "," +
	            			CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_USUMODIFICACION +
							" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + 
							" WHERE " +
							CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " +
							CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDCUENTA + "=" + idCuenta;							
							
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cuentas bancarias.");
	       }
	       return datos;                        
	}
	
	/* DELETE LOGICO: no se borra fisicamente */
	public boolean delete(Hashtable hash) throws ClsExceptions 
	{
		try {
			Hashtable h = new Hashtable();
			String [] claves = this.getClavesBean();
			for (int i = 0; i < claves.length; i++) {
				h.put (claves[i], hash.get(claves[i]));
			}

			String [] campos = {CenCuentasBancariasBean.C_FECHABAJA, CenCuentasBancariasBean.C_USUMODIFICACION, CenCuentasBancariasBean.C_FECHAMODIFICACION};
			UtilidadesHash.set(h, CenCuentasBancariasBean.C_FECHABAJA, "SYSDATE");

			if (this.updateDirect(h, claves, campos)) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ClsExceptions(e, "Error al realizar el \"delete\" en B.D.");
		}
		return false;
	}
	
	public CenCuentasBancariasBean getCuenta(CenCuentasBancariasBean cuentaBancaria)
	throws ClsExceptions,SIGAException
{
		CenCuentasBancariasBean cuentaBean = null;
	try {
		Hashtable codigosHashtable = new Hashtable();
		int contador = 0;
		RowsContainer rc = new RowsContainer();
		StringBuffer sql = new StringBuffer();
		
		
		sql.append(" SELECT CB.IDCUENTA,substr(B.NOMBRE,0,25) || ' ' ||'n� ' || CB.IBAN as DESCRIPCION ");
		
		sql.append(" FROM  ");
		sql.append(" CEN_CUENTASBANCARIAS CB, CEN_BANCOS B ");
		sql.append(" WHERE  ");


		sql.append(" CB.CBO_CODIGO = B.CODIGO ");
		sql.append(" AND CB.IDCUENTA =:");
		contador ++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),cuentaBancaria.getIdCuenta());
		sql.append(" AND CB.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),cuentaBancaria.getIdInstitucion());
		sql.append(" AND CB.IDPERSONA =  :");
		contador ++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),cuentaBancaria.getIdPersona());
		
		
		
		
						
		if (rc.findBind(sql.toString(),codigosHashtable)) {
			
			if(rc.size()>0){
			
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila=fila.getRow();
				cuentaBean = new CenCuentasBancariasBean();
				cuentaBean.setIdCuenta(UtilidadesHash.getInteger(htFila, CenCuentasBancariasBean.C_IDCUENTA));
				cuentaBean.setNumeroCuenta(UtilidadesHash.getString(htFila,"DESCRIPCION"));
				
				
			}
		}
	}
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al obtener la informacion sobre getCuentaUnicaServiciosFactura.");
	}
	
	return cuentaBean;                        
}
	
	public String getNumeroCuentaCompra(String idInstitucion, String idPersona, String idCuenta) throws ClsExceptions{
		String cuentaBancaria = "";
				
		try {
			
			RowsContainer rc = new RowsContainer();
			String sql = new String();
			
			sql = "  SELECT C.IBAN AS CUENTABANCARIA";
			sql += " FROM CEN_CUENTASBANCARIAS C, CEN_BANCOS B ";
			sql += " WHERE C.CBO_CODIGO = B.CODIGO ";
			sql += " 	AND C.IDCUENTA = "+idCuenta;
			sql += " 	AND C.IDINSTITUCION = "+idInstitucion;
			sql += " 	AND C.IDPERSONA =  "+idPersona;
			sql += " 	AND (C.ABONOCARGO = 'C' OR C.ABONOCARGO = 'T')";
			sql += " 	AND c.fechabaja IS NULL";   
	
	        // RGG cambio visibilidad
	        rc = this.find(sql);
	        if (rc!=null) {
	           for (int i = 0; i < rc.size(); i++){
	              Row fila = (Row) rc.get(i);
	              cuentaBancaria = (String) fila.getString("CUENTABANCARIA");
	           }
	        } 
		 
		}catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cuentas bancarias.");
	    }
	
		return cuentaBancaria;
	}
	

	public Vector getCuentaCorrienteAbono (String idInstitucion, String idPersona)throws ClsExceptions{
		RowsContainer rc = null;		
		Vector datos=new Vector();
		
		try { 
			rc = new RowsContainer(); 
			String sql = "";
			                      
            sql = "SELECT ' n� ' || cuen.iban as CUENTABANCARIA_ABONO, "
            	+ " ' n� ' || cuen.iban as CUENTABANCARIA_ABONO_ABIERTA "
            	+ " FROM cen_cuentasbancarias cuen"
            	+ " WHERE cuen.abonocargo IN ('A', 'T')"
            	+ " AND cuen.idinstitucion = " + idInstitucion
            	+ " AND cuen.idpersona = " + idPersona
            	+ " AND (cuen.FECHABAJA is null or cuen.fechabaja > sysdate)"
            	+ " order by fechamodificacion desc";       
            
         // RGG cambio visibilidad
			rc = this.find(sql);
			if (rc!=null) 
			{ 				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos de getCuentaCorrienteAbono()");
		}		
		return datos;
	}
	
	public Vector getCuentaCorrienteCargo (String idInstitucion, String idPersona)throws ClsExceptions{
		RowsContainer rc = null;		
		Vector datos=new Vector();
		
		try { 
			rc = new RowsContainer(); 
			String sql = "";
			                      
            sql = "SELECT ' n� ' || cuen.iban as CUENTABANCARIA_CARGO, "
            	+ " ' n� ' || cuen.iban as CUENTABANCARIA_CARGO_ABIERTA "
            	+ " FROM cen_cuentasbancarias cuen"
            	+ " WHERE cuen.abonocargo IN ('C', 'T')"
            	+ " AND cuen.idinstitucion = " + idInstitucion
            	+ " AND cuen.idpersona = " + idPersona
            	+ " AND (cuen.FECHABAJA is null or cuen.fechabaja > sysdate)"
            	+ " order by fechamodificacion desc";       
            
         // RGG cambio visibilidad
			rc = this.find(sql);
			if (rc!=null) 
			{ 				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos de getCuentaCorrienteCargo()");
		}		
		return datos;
	}
	
	/**
	 * Se ha cambiado "( cuen.abonosjcs = 1 OR (cuen.abonosjcs = 0 AND cuen.abonocargo IN ('A', 'T')))" por "cuen.abonosjcs = '1'"", ya que el campo ABONOCARGO ya no tiene relacion con ABONOSJCS
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getCuentaCorrienteSJCS (String idInstitucion, String idPersona) throws ClsExceptions {
		Vector datos = new Vector();
		try { 
			RowsContainer rc = new RowsContainer(); 
			StringBuilder sql = new StringBuilder();
			                      
            sql.append("SELECT ' n� ' || cuen.iban as CUENTABANCARIA_SJCS, ");
            sql.append(" ' n� ' || cuen.iban as CUENTABANCARIA_SJCS_ABIERTA ");	
            sql.append(" FROM cen_cuentasbancarias cuen ");
            sql.append(" WHERE cuen.abonosjcs = '1' ");
            sql.append(" AND cuen.idinstitucion = ");
            sql.append(idInstitucion);
            sql.append(" AND cuen.idpersona = ");
            sql.append(idPersona);
            sql.append(" AND (cuen.FECHABAJA is null or cuen.fechabaja > sysdate) ");
            sql.append(" ORDER BY abonosjcs desc, fechamodificacion desc ");       
            
         // RGG cambio visibilidad
			rc = this.find(sql.toString());
			if (rc!=null) { 				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos de getCuentaCorrienteSJCS()");
		}	
		
		return datos;
	}	
	
	
	public String getNumeroCuentaFactura(String idInstitucion, String idPersona, String idCuenta) throws ClsExceptions{
		String cuentaBancaria = "";
				
		try {
			
			RowsContainer rc = new RowsContainer();
			String sql = new String();
			
			sql = "  SELECT C.iban AS CUENTABANCARIA";
			sql += " FROM CEN_CUENTASBANCARIAS C, CEN_BANCOS B ";
			sql += " WHERE C.CBO_CODIGO = B.CODIGO ";
			sql += " 	AND C.IDCUENTA = "+idCuenta;
			sql += " 	AND C.IDINSTITUCION = "+idInstitucion;
			sql += " 	AND C.IDPERSONA =  "+idPersona;
			sql += " 	AND (C.ABONOCARGO = 'C' OR C.ABONOCARGO = 'T')";
			sql += " 	AND (c.fechabaja IS NULL or c.fechabaja > sysdate)";   
	
	        // RGG cambio visibilidad
	        rc = this.find(sql);
	        if (rc!=null) {
	           for (int i = 0; i < rc.size(); i++){
	              Row fila = (Row) rc.get(i);
	              cuentaBancaria = (String) fila.getString("CUENTABANCARIA");
	           }
	        } 
		 
		}catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cuentas bancarias.");
	    }
	
		return cuentaBancaria;
	}	
	
	/**
	 * Obtiene la cuenta bancaria activa de cargo de la persona, que tiene la mayoria de suscripciones activas
	 * @param idInstitucion
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public String getCuentaActivaServiciosActivos (Integer idInstitucion, Long idPersona) throws ClsExceptions {
		String idCuenta = null;
		
		try {
			String sql = "SELECT TABLA." + CenCuentasBancariasBean.C_IDCUENTA + ", " +
							" TABLA.NUM_SERVICIOS_ASOCIADOS " +
						" FROM ( " +
							"SELECT " + CenCuentasBancariasBean.C_IDCUENTA + ", " +
								CenCuentasBancariasBean.C_FECHAMODIFICACION + ", " +
								" ( " + // Obtiene el numero de servicios en los que esta actualmente asociada la cuenta
									" SELECT COUNT(*) " +
									" FROM " + PysSuscripcionBean.T_NOMBRETABLA +
									" WHERE " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDINSTITUCION + " = " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION +
										" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDPERSONA + " = " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA +
										" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_IDCUENTA + " = " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA +
										" AND " + PysSuscripcionBean.T_NOMBRETABLA + "." + PysSuscripcionBean.C_FECHABAJA + " IS NULL " +
								" ) AS NUM_SERVICIOS_ASOCIADOS " +						
							" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA +
							" WHERE " + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
								" AND " + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
								" AND " + CenCuentasBancariasBean.C_FECHABAJA + " IS NULL " + 
								" AND " + CenCuentasBancariasBean.C_ABONOCARGO + " in ('T','C')" +							
						" ) TABLA " +
						" WHERE TABLA.NUM_SERVICIOS_ASOCIADOS > 0 " + // Solo obtengo las cuentas con servicios asociados
						" ORDER BY TABLA.NUM_SERVICIOS_ASOCIADOS DESC"; // Ordeno por el numero de servicios para obtener la cuenta con mas servicios asociados
			
			RowsContainer rc = this.find(sql);
	        if (rc!=null && rc.size()>0) {
	        	if (rc.size()>1) { // Almenos tiene dos cuentas asociadas a un numero de suscripciones activas
	        		Row fila1 = (Row) rc.get(0);
	        		String sNumSuscrCuenta1 = fila1.getString("NUM_SERVICIOS_ASOCIADOS");
	        		
	        		Row fila2 = (Row) rc.get(1);
	        		String sNumSuscrCuenta2 = fila2.getString("NUM_SERVICIOS_ASOCIADOS");

	        		// Si son diferentes, ya que la sentencia esta ordenada, es que la fila1 tiene la mayoria de suscripciones activas
	        		if (!sNumSuscrCuenta1.equals(sNumSuscrCuenta2)) {
	        			idCuenta = (String) fila1.getString(CenCuentasBancariasBean.C_IDCUENTA);
	        		}
	        		
	        	} else { // Solo tiene una cuenta asociada a suscripciones activas
	        		Row fila = (Row) rc.get(0);
	        		idCuenta = (String) fila.getString(CenCuentasBancariasBean.C_IDCUENTA);
	        	}
	        } 
			
		} catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la cuenta bancaria activa de cargo de la persona mas utilizada para los servicios.");
	    }
	
		return idCuenta;
	}	
	
	/**
	 * Obtiene las lineas de devoluciones
	 * @param institucion
	 * @param idPersona
	 * @param bPagosJG
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerCuentasAbonos (String institucion, String idPersona, boolean bPagosJG) throws ClsExceptions {
		Vector<Hashtable<String,Object>> vCuentas = new Vector<Hashtable<String,Object>>();
       try {
            String sql = "SELECT " + CenCuentasBancariasBean.C_IDCUENTA + " AS ID, " +
					" F_SIGA_FORMATOIBAN(" + CenCuentasBancariasBean.C_IBAN + ") as DESCRIPCION " +
				" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + 
				" WHERE " + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + institucion +
					" AND " + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +					
					" AND " + CenCuentasBancariasBean.C_FECHABAJA + " IS NULL" +
					(bPagosJG ? " AND " + CenCuentasBancariasBean.C_ABONOSJCS + " = '1'" : " AND " + CenCuentasBancariasBean.C_ABONOCARGO + " IN ('A','T')") +
				" ORDER BY 2";
            
			vCuentas = this.selectGenerico(sql);
            
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al obtener las cuentas de abonos.");
       }
       
       return vCuentas;                        
    }		
}