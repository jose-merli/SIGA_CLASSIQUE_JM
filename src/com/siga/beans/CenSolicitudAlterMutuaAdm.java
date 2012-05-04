
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.SolicitudIncorporacionForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
/**
 * 
 * @author jorgeta
 *
 */
public class CenSolicitudAlterMutuaAdm extends MasterBeanAdministrador {
	
	public CenSolicitudAlterMutuaAdm (UsrBean usu) {
		super(CenSolicitudAlterMutuaBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenSolicitudAlterMutuaBean.C_IDSOLICITUD,
				CenSolicitudAlterMutuaBean.C_IDSOLICITUDALTER,
				CenSolicitudAlterMutuaBean.C_NOMBRE,
				CenSolicitudAlterMutuaBean.C_APELLIDOS,
				CenSolicitudAlterMutuaBean.C_NUMEROIDENTIFICADOR,
				CenSolicitudAlterMutuaBean.C_DOMICILIO,
				CenSolicitudAlterMutuaBean.C_CODIGOPOSTAL,
				CenSolicitudAlterMutuaBean.C_TELEFONO1,
				CenSolicitudAlterMutuaBean.C_CORREOELECTRONICO,
				CenSolicitudAlterMutuaBean.C_IDINSTITUCION,
				CenSolicitudAlterMutuaBean.C_FECHAMODIFICACION,
				CenSolicitudAlterMutuaBean.C_USUMODIFICACION,
				CenSolicitudAlterMutuaBean.C_FECHANACIMIENTO,
				CenSolicitudAlterMutuaBean.C_IDTIPOIDENTIFICACION,
				CenSolicitudAlterMutuaBean.C_IDPROVINCIA,
				CenSolicitudAlterMutuaBean.C_TELEFONO2,
				CenSolicitudAlterMutuaBean.C_MOVIL,
				CenSolicitudAlterMutuaBean.C_FAX,
				CenSolicitudAlterMutuaBean.C_IDESTADOCIVIL,
				CenSolicitudAlterMutuaBean.C_IDPAIS,
				CenSolicitudAlterMutuaBean.C_IDSEXO,
				CenSolicitudAlterMutuaBean.C_CODIGOSUCURSAL,
				CenSolicitudAlterMutuaBean.C_CBO_CODIGO,
				CenSolicitudAlterMutuaBean.C_DIGITOCONTROL,
				CenSolicitudAlterMutuaBean.C_NUMEROCUENTA,
				CenSolicitudAlterMutuaBean.C_IBAN,
				CenSolicitudAlterMutuaBean.C_IDCOBERTURA,
				CenSolicitudAlterMutuaBean.C_OTROSBENEFICIARIOS,
				CenSolicitudAlterMutuaBean.C_PAIS,
				CenSolicitudAlterMutuaBean.C_PROVINCIA,
				CenSolicitudAlterMutuaBean.C_POBLACION,
				CenSolicitudAlterMutuaBean.C_PROPUESTA,
				CenSolicitudAlterMutuaBean.C_IDPAQUETE,
				CenSolicitudAlterMutuaBean.C_IDTIPOEJERCICIO,
				CenSolicitudAlterMutuaBean.C_ESTADO
		};
		
		
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenSolicitudAlterMutuaBean.C_IDSOLICITUD, CenSolicitudAlterMutuaBean.C_PROPUESTA};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenSolicitudAlterMutuaBean bean = null;
		
		try {
			bean = new CenSolicitudAlterMutuaBean();
			
			bean.setApellidos(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_APELLIDOS));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_CODIGOPOSTAL));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_CORREOELECTRONICO));
			bean.setDomicilio(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_DOMICILIO));
			bean.setFax(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_FAX));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_FECHAMODIFICACION));
			bean.setFechaNacimiento(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_FECHANACIMIENTO));
			bean.setIdEstadoCivil(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDESTADOCIVIL));
			bean.setIdInstitucion(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDINSTITUCION));
			bean.setIdPais(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDPAIS));
			bean.setIdProvincia(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDPROVINCIA));
			bean.setIdSolicitudAlter(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDSOLICITUDALTER));
			bean.setIdSolicitud(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDSOLICITUD));
			bean.setIdTipoIdentificacion(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDTIPOIDENTIFICACION));
			bean.setMovil(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_MOVIL));
			bean.setNombre(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_NOMBRE));
			bean.setIdentificador(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_NUMEROIDENTIFICADOR));
			bean.setTelefono1(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_TELEFONO2));
			bean.setIdSexo(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDSEXO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolicitudAlterMutuaBean.C_USUMODIFICACION));
			bean.setCboCodigo(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_CBO_CODIGO));
			bean.setCodigoSucursal(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_CODIGOSUCURSAL));
			bean.setDigitoControl(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_DIGITOCONTROL));
			bean.setNumeroCuenta(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_NUMEROCUENTA));
			bean.setIban(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IBAN));
			bean.setIdCobertura(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDCOBERTURA));
			bean.setOtrosBeneficiarios(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_OTROSBENEFICIARIOS));
			bean.setPais(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_PAIS));
			bean.setProvincia(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_PROVINCIA));
			bean.setPoblacion(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_POBLACION));
			bean.setPropuesta(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_PROPUESTA));
			bean.setIdPaquete(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDPAQUETE));
			bean.setIdTipoEjercicio(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDTIPOEJERCICIO));
			
			bean.setIdPersona(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_IDPERSONA));
			bean.setEstado(UtilidadesHash.getString(hash,CenSolicitudAlterMutuaBean.C_ESTADO));
			
			
			
		}
		catch (Exception e) { 
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {

		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenSolicitudAlterMutuaBean b = (CenSolicitudAlterMutuaBean) bean;
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_APELLIDOS, b.getApellidos());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_FAX, b.getFax());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_FECHANACIMIENTO, b.getFechaNacimiento());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDESTADOCIVIL, b.getIdEstadoCivil());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDSOLICITUD,b.getIdSolicitud());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDTIPOIDENTIFICACION, b.getIdTipoIdentificacion());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_NUMEROIDENTIFICADOR, b.getIdentificador());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_TELEFONO1, b.getTelefono1());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_TELEFONO2, b.getTelefono2());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDSEXO, b.getIdSexo());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IBAN, b.getIban());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_CBO_CODIGO, b.getCboCodigo());   
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_CODIGOSUCURSAL, b.getCodigoSucursal());   
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_DIGITOCONTROL, b.getDigitoControl());   
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_NUMEROCUENTA, b.getNumeroCuenta());   
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_SWIFT, b.getSwift());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDSOLICITUDALTER, b.getIdSolicitudAlter());		
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_PAIS, b.getPais());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_PROVINCIA, b.getProvincia());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_POBLACION, b.getPoblacion());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_PROPUESTA, b.getPropuesta());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDPAQUETE, b.getIdPaquete());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDTIPOEJERCICIO, b.getIdTipoEjercicio());
			UtilidadesHash.set(htData,CenSolicitudAlterMutuaBean.C_IDPERSONA, b.getIdPersona());
			

		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	public  List<CenSolicitudAlterMutuaBean> getSolicitudesAlterMutua(CenSolicitudIncorporacionBean solicitudIncorporacionBean) throws ClsExceptions{
		StringBuffer sql =  new StringBuffer();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		sql.append("SELECT SOL.IDSOLICITUD,SOL.IDESTADO,SOL.ESTADO,SOL.IDTIPOSOLICITUD,SOL.IDSOLICITUDACEPTADA,SOL.ESTADOMUTUALISTA ");
		sql.append("FROM CEN_SOLICITUDMUTUALIDAD SOL ");
		sql.append("WHERE SOL.IDSOLICITUDINCORPORACION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),solicitudIncorporacionBean.getIdSolicitud());

		List<CenSolicitudAlterMutuaBean> solicitudAlterMutuaBeans = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	solicitudAlterMutuaBeans = new ArrayList<CenSolicitudAlterMutuaBean>();
            	CenSolicitudAlterMutuaBean solicitudAlterMutuaBean = null;
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		solicitudAlterMutuaBean =  (CenSolicitudAlterMutuaBean)this.hashTableToBean(htFila);
            		solicitudAlterMutuaBeans.add(solicitudAlterMutuaBean);
            	}
            }else{
            	solicitudAlterMutuaBeans = new ArrayList<CenSolicitudAlterMutuaBean>();
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
	
	
		return solicitudAlterMutuaBeans;
	
	}
	public  CenSolicitudAlterMutuaBean getSolicitudAlterMutua(String idSolicitud) throws ClsExceptions{
		Hashtable pkHashtable = new Hashtable();
		pkHashtable.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
		Vector<CenSolicitudAlterMutuaBean> solicitudAlterMutuaBeans =selectByPK(pkHashtable);
		CenSolicitudAlterMutuaBean solicitudAlterMutuaBean= (CenSolicitudAlterMutuaBean)solicitudAlterMutuaBeans.get(0);
		return solicitudAlterMutuaBean;
	}
	
	public  CenSolicitudAlterMutuaBean getSolicitudAlterMutua(String idPersona,String idTipoSolicitudAlterMutua) throws ClsExceptions, SIGAException{
		StringBuffer sql =  new StringBuffer();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		sql.append("SELECT SOL.* ");
		sql.append(" FROM CEN_SOLICITUDMUTUALIDAD SOL,CEN_SOLICITUDINCORPORACION SI ");
		sql.append(" WHERE SOL.IDSOLICITUDINCORPORACION = SI.IDSOLICITUD ");
		sql.append(" AND SI.IDPERSONA = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idPersona);
		sql.append(" AND SOL.IDTIPOSOLICITUD = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTipoSolicitudAlterMutua);
		CenSolicitudAlterMutuaBean solicitudAlterMutuaBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 
											
            if (rc.findBind(sql.toString(),htCodigos)) {
            	
            	switch (rc.size()) {
				
            	case 0:
					
					break;
				case 1:
					Row fila = (Row) rc.get(0);
            		Hashtable<String, Object> htFila=fila.getRow();
            		solicitudAlterMutuaBean =  (CenSolicitudAlterMutuaBean)this.hashTableToBean(htFila);
					break;

				default:
					throw new SIGAException("Existen mas de una solucitud de mutualidad para un mismo idpersona");
				}
            	
            	
            	
    			
            }
       } catch (SIGAException e) {
       		throw e;
       }catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
	
	
		return solicitudAlterMutuaBean;
	
	}

	public Vector getSolicitudes(String identificador, String propuesta) throws ClsExceptions {
		StringBuffer sql =  new StringBuffer();
		RowsContainer rc = null;
		Vector result = new Vector();
		sql.append("SELECT SOL.* ");
		sql.append(" FROM CEN_SOLICITUDALTER SOL, CEN_SOLICITUDINCORPORACION SI");
		sql.append(" WHERE ((SOL.IDSOLICITUD = SI.IDSOLICITUD ");
		sql.append(" AND SI.IDPERSONA = " + identificador + ")");
		sql.append(" OR SI.IDSOLICITUD = " + identificador + ")");
		if(propuesta!=null){
			sql.append(" AND SOL.PROPUESTA = " + propuesta );
		}
		rc = new RowsContainer(); 
		if (rc.query(sql.toString())) {
			Vector rcv = rc.getAll();
			Hashtable hash ;
			for (int i = 0; i < rcv.size(); i++) {
				Row fila = (Row) rcv.get(0);
        		Hashtable htFila=fila.getRow();
        		result.add((CenSolicitudAlterMutuaBean)this.hashTableToBean(htFila));

			}
		}
		return result;
	}

}
