/*
 * Created on Mar 15, 2005
 * @author jmgrau
*/
package com.siga.beans;
 
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvProgramPagosAdm extends MasterBeanAdministrador {

  public final static int ESTADO_INICIAL = 1;
  public final static int ESTADO_PROCESADO = 2;
  public final static int ESTADO_PROCESADO_ERRORES= 3;
  public final static int ESTADO_PENDIENTE_AUTOMATICO = 4;

  public final static String NO_GENERAR = "N";
  public final static String FECHA_CREACION = "C";
  public final static String FECHA_PROGRAMADA = "P";

	public EnvProgramPagosAdm(UsrBean usuario)
	{
	    super(EnvProgramPagosBean.T_NOMBRETABLA, usuario);
	}

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvProgramPagosBean.C_IDINSTITUCION,
                EnvProgramPagosBean.C_IDENVIO,
                EnvProgramPagosBean.C_IDPROGRAM,
                EnvProgramPagosBean.C_IDPERSONA,
                EnvProgramPagosBean.C_ESTADO,
                EnvProgramPagosBean.C_IDPAGO,
                EnvProgramPagosBean.C_IDIOMA,
            	EnvProgramPagosBean.C_FECHAMODIFICACION,
            	EnvProgramPagosBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvProgramPagosBean.C_IDPROGRAM,EnvProgramPagosBean.C_IDINSTITUCION, EnvProgramPagosBean.C_IDENVIO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvProgramPagosBean.C_IDPROGRAM};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvProgramPagosBean bean = null;

		try
		{
			bean = new EnvProgramPagosBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvProgramPagosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvProgramPagosBean.C_IDENVIO));
			bean.setIdProgram(UtilidadesHash.getInteger(hash, EnvProgramPagosBean.C_IDPROGRAM));
			bean.setIdioma(UtilidadesHash.getInteger(hash, EnvProgramPagosBean.C_IDIOMA));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvProgramPagosBean.C_IDPERSONA));
			bean.setIdPago(UtilidadesHash.getInteger(hash, EnvProgramPagosBean.C_IDPAGO));
			bean.setEstado(UtilidadesHash.getString(hash, EnvProgramPagosBean.C_ESTADO));
			
			
			
			
			

		}

		catch (Exception e)
		{
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
     */
    public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			EnvProgramPagosBean b = (EnvProgramPagosBean) bean;

			UtilidadesHash.set(htData, EnvProgramPagosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvProgramPagosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvProgramPagosBean.C_IDPROGRAM, b.getIdProgram());
			UtilidadesHash.set(htData, EnvProgramPagosBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvProgramPagosBean.C_IDPAGO, b.getIdPago());
			UtilidadesHash.set(htData, EnvProgramPagosBean.C_IDIOMA, b.getIdioma());
			UtilidadesHash.set(htData, EnvProgramPagosBean.C_ESTADO, b.getEstado());
			

			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    /**
     * 
     * @param estado 1 ó 0. Sera uno cuando se quieran los pagos ya enviados.0 con los pagos pendienmtes de enviar
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */

    public Vector getPagosProgramados(String estado, String idInstitucion)
			throws ClsExceptions {

		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT EPP." + EnvProgramPagosBean.C_IDPROGRAM +",EPP.IDENVIO,EPP.IDINSTITUCION, ");
		sql.append(" EPP.IDIOMA,EPP.IDPERSONA,EPP.ESTADO,EPP.IDPAGO, ");
		sql.append(" EP.IDTIPOENVIOS,EP.IDPLANTILLAENVIOS,EP.IDPLANTILLA,EP.NOMBRE,EP.FECHAPROGRAMADA, ");
		sql.append(" L.CODIGOEXT ");

		sql.append(" FROM ENV_PROGRAMPAGOS EPP ,ENV_ENVIOPROGRAMADO EP, ADM_LENGUAJES L ");
		sql.append(" WHERE EPP.IDINSTITUCION = EP.IDINSTITUCION ");
		sql.append(" AND EPP.IDENVIO = EP.IDENVIO ");
		sql.append(" AND EPP.IDIOMA = L.IDLENGUAJE ");
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		keyContador++;
		htCodigos.put(new Integer(keyContador), estado);
		sql.append(" AND EPP.ESTADO = :");
		sql.append(keyContador);
		if(idInstitucion!=null){
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND EPP.IDINSTITUCION = :");
			sql.append(keyContador);
		}
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try {
			rc = new RowsContainer();
			if (rc.queryBind(sql.toString(),htCodigos)) {
				EnvProgramPagosBean programPagos = null;
				EnvEnvioProgramadoBean envioProgramado = null;
				for (int i = 0; i < rc.size(); i++) {
					
					Row row = (Row) rc.get(i);
					Hashtable htFila = row.getRow(); 
					programPagos = new EnvProgramPagosBean();
					envioProgramado = new EnvEnvioProgramadoBean();
					programPagos.setEnvioProgramado(envioProgramado);
					programPagos.setIdInstitucion(UtilidadesHash.getInteger(htFila, EnvProgramPagosBean.C_IDINSTITUCION));
					programPagos.setIdEnvio(UtilidadesHash.getInteger(htFila, EnvProgramPagosBean.C_IDENVIO));
					programPagos.setIdProgram(UtilidadesHash.getInteger(htFila, EnvProgramPagosBean.C_IDPROGRAM));
					programPagos.setIdioma(UtilidadesHash.getInteger(htFila, EnvProgramPagosBean.C_IDIOMA));
					programPagos.setIdiomaCodigoExt(UtilidadesHash.getString(htFila, AdmLenguajesBean.C_CODIGOEXT));
					programPagos.setIdPersona(UtilidadesHash.getLong(htFila, EnvProgramPagosBean.C_IDPERSONA));
					programPagos.setIdPago(UtilidadesHash.getInteger(htFila, EnvProgramPagosBean.C_IDPAGO));
					programPagos.setEstado(UtilidadesHash.getString(htFila, EnvProgramPagosBean.C_ESTADO));
					
					envioProgramado.setIdInstitucion(programPagos.getIdInstitucion());
					envioProgramado.setIdEnvio(programPagos.getIdEnvio());
					envioProgramado.setIdTipoEnvios(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_IDTIPOENVIOS));
					envioProgramado.setIdPlantillaEnvios(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_IDPLANTILLAENVIOS));
					envioProgramado.setIdPlantilla(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_IDPLANTILLA));
					envioProgramado.setNombre(UtilidadesHash.getString(htFila, EnvEnvioProgramadoBean.C_NOMBRE));
					envioProgramado.setFechaProgramada(UtilidadesHash.getString(htFila, EnvEnvioProgramadoBean.C_FECHAPROGRAMADA));
					
					datos.add(programPagos);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
		return datos;
	}
   


}