
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.general.SIGAException;

/**
 * Implementa las operaciones sobre la base de datos a la tabla SCS_INSCRIPCIONGUARDIA
 */
public class ScsInscripcionGuardiaAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsInscripcionGuardiaAdm (UsrBean usuario) {
		super( ScsInscripcionGuardiaBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsInscripcionGuardiaBean.C_IDPERSONA,					ScsInscripcionGuardiaBean.C_IDINSTITUCION,
							ScsInscripcionGuardiaBean.C_IDTURNO,					ScsInscripcionGuardiaBean.C_IDGUARDIA,
							ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,			ScsInscripcionGuardiaBean.C_FECHABAJA,
							ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,	ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,
							ScsInscripcionGuardiaBean.C_USUMODIFICACION,			ScsInscripcionGuardiaBean.C_FECHAMODIFICACION};
		return campos;
	}
	
	/**
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsInscripcionGuardiaBean.C_IDINSTITUCION,		ScsInscripcionGuardiaBean.C_IDPERSONA,
							ScsInscripcionGuardiaBean.C_IDTURNO,			ScsInscripcionGuardiaBean.C_IDGUARDIA,
							ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
		return campos;
	}
	
	/**
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsInscripcionGuardiaBean bean = null;
		try{
			bean = new ScsInscripcionGuardiaBean();
			bean.setIdInstitucion(Integer.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDINSTITUCION)));
			bean.setIdPersona(Long.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDPERSONA)));
			bean.setIdTurno(Integer.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDTURNO)));
			bean.setIdGuardia(Integer.valueOf((String)hash.get(ScsInscripcionGuardiaBean.C_IDGUARDIA)));
			bean.setFechaSuscripcion((String)hash.get(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION));
			bean.setFechaBaja((String)hash.get(ScsInscripcionTurnoBean.C_FECHABAJA));
			bean.setObservacionesSuscripcion((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION));
			bean.setObservacionesBaja((String)hash.get(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	
	/** Esta funcion habria que borrarla de aqui:
	 * pero hay que tener en cuenta que se usa desde una JSP
	 */
	public Hashtable obtenerNumCuenta(Long idpersona,Integer idInstitucion) throws SIGAException, ClsExceptions {
		try{
			String consulta="select count(C.IDCUENTA)AS NUM from cen_cuentasbancarias C where C.IDPERSONA = "+ idpersona+" AND C.IDINSTITUCION = "+idInstitucion+" AND (c.ABONOCARGO = 'C' OR c.ABONOCARGO = 'T') and c.fechabaja IS NULL";
			
			Vector vector=null;
			vector=(Vector)ejecutaSelect(consulta);
			String numcuentas=(String)((Hashtable)vector.get(0)).get("NUM");
			Hashtable cuentaelegida=new Hashtable();
			
			if(numcuentas.equals("1")){
				
				consulta="select (C.CBO_CODIGO || '-' ||C.CODIGOSUCURSAL || '-' || C.DIGITOCONTROL || '-' ||LPAD(SUBSTR(C.NUMEROCUENTA, 7), 10, '*')) as DESCRIPCION, C.IDCUENTA A from cen_cuentasbancarias C where C.IDPERSONA = "+idpersona+" AND C.IDINSTITUCION = "+idInstitucion+" AND (c.ABONOCARGO = 'C' OR c.ABONOCARGO = 'T') and c.fechabaja IS NULL";
				vector=(Vector)ejecutaSelect(consulta);
				cuentaelegida.put("NUMCUENTA",(String)((Hashtable)vector.get(0)).get("DESCRIPCION"));
				cuentaelegida.put("IDCUENTA",(String)((Hashtable)vector.get(0)).get("A"));
			}
			else{
				cuentaelegida.put("NUMCUENTA","");
				cuentaelegida.put("IDCUENTA","");
			}
			
			return (cuentaelegida);
		}catch(Exception e){
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
	}
	
	/**
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsInscripcionGuardiaBean b = (ScsInscripcionGuardiaBean) bean;
			hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsInscripcionGuardiaBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, b.getFechaSuscripcion());
			hash.put(ScsInscripcionGuardiaBean.C_FECHABAJA, b.getFechaBaja());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION, b.getObservacionesSuscripcion());
			hash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA, b.getObservacionesBaja());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return null;
	}
	
	/**
	 * Funcion ejecutaSelect(String select)
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
}