 /* Created on 22-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 /**
	 * @author pilar.duran
	 *
	 * TODO To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Style - Code Templates
	 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


/**
 * @author pilard
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
	public class GenerarImpreso190Adm extends MasterBeanAdministrador{

		public GenerarImpreso190Adm(UsrBean usuario) 
		{
			super(GenerarImpreso190Bean.T_NOMBRETABLA, usuario);
		}

		public String[] getCamposBean() {
			String [] campos = {GenerarImpreso190Bean.C_IDINSTITUCION,
					            GenerarImpreso190Bean.C_ANIO,
								GenerarImpreso190Bean.C_NOMBREFICHERO,
								GenerarImpreso190Bean.C_IDPROVINCIA,
								GenerarImpreso190Bean.C_TELEFONO,
								GenerarImpreso190Bean.C_NOMBRE,
								GenerarImpreso190Bean.C_APELLIDO1,
								GenerarImpreso190Bean.C_APELLIDO2,
								GenerarImpreso190Bean.C_FECHAMODIFICACION,
								GenerarImpreso190Bean.C_USUMODIFICACION
								};
			return campos;
		}

		public String[] getClavesBean() {
			String [] claves = {GenerarImpreso190Bean.C_IDINSTITUCION};
			return claves;
		}

		protected String[] getOrdenCampos() {
			return getClavesBean();
		}

		protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
			GenerarImpreso190Bean bean = null;
			
			try {
				bean = new GenerarImpreso190Bean();
				bean.setIdinstitucion(UtilidadesHash.getInteger(hash, GenerarImpreso190Bean.C_IDINSTITUCION));
				bean.setAnio(UtilidadesHash.getInteger(hash, GenerarImpreso190Bean.C_ANIO));
				bean.setNombreFichero(UtilidadesHash.getString(hash, GenerarImpreso190Bean.C_NOMBREFICHERO));
				bean.setIdprovincia(UtilidadesHash.getString(hash, GenerarImpreso190Bean.C_IDPROVINCIA));
				bean.setTelefono(UtilidadesHash.getString(hash, GenerarImpreso190Bean.C_TELEFONO));
				bean.setNombre(UtilidadesHash.getString(hash, GenerarImpreso190Bean.C_NOMBRE));
				bean.setApellido1(UtilidadesHash.getString(hash, GenerarImpreso190Bean.C_APELLIDO1));
				bean.setApellido2(UtilidadesHash.getString(hash, GenerarImpreso190Bean.C_APELLIDO2));
				bean.setFechaMod(UtilidadesHash.getString(hash, GenParametrosBean.C_FECHAMODIFICACION));
				bean.setUsuMod(UtilidadesHash.getInteger(hash, GenParametrosBean.C_USUMODIFICACION));
				
				
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
				GenerarImpreso190Bean b = (GenerarImpreso190Bean) bean;
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_IDINSTITUCION, 		b.getIdinstitucion());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_ANIO, 	b.getAnio());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_NOMBREFICHERO, 	b.getNombreFichero());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_IDPROVINCIA, 	b.getIdprovincia());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_TELEFONO, 	b.getTelefono());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_NOMBRE, 	b.getNombre());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_APELLIDO1, 	b.getApellido1());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_APELLIDO2, 	b.getApellido2());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_FECHAMODIFICACION, 	b.getFechaMod());
				UtilidadesHash.set(htData, GenerarImpreso190Bean.C_USUMODIFICACION, 	b.getUsuMod());
				
				
				
				
				
			}
			catch (Exception e) {
				htData = null;
				throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
			}
			return htData;	
		}
		

	
}	

