/*
 * Created on Mar 15, 2005
 * @author jmgrau
*/
package com.siga.beans;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvProgramIRPFAdm extends MasterBeanAdministrador {

  public final static int ESTADO_INICIAL = 1;
  public final static int ESTADO_PROCESADO = 2;
  public final static int ESTADO_PROCESADO_ERRORES= 3;
  public final static int ESTADO_PENDIENTE_AUTOMATICO = 4;

  public final static String NO_GENERAR = "N";
  public final static String FECHA_CREACION = "C";
  public final static String FECHA_PROGRAMADA = "P";

	public EnvProgramIRPFAdm(UsrBean usuario)
	{
	    super(EnvProgramIRPFBean.T_NOMBRETABLA, usuario);
	}

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvProgramIRPFBean.C_IDINSTITUCION,
                EnvProgramIRPFBean.C_IDENVIO,
                EnvProgramIRPFBean.C_IDPROGRAM,
                EnvProgramIRPFBean.C_IDPERSONA,
                EnvProgramIRPFBean.C_ESTADO,
                EnvProgramIRPFBean.C_PERIODO,
                EnvProgramIRPFBean.C_ANIOIRPF,
                EnvProgramIRPFBean.C_IDIOMA,
                EnvProgramIRPFBean.C_PLANTILLAS,
            	EnvProgramIRPFBean.C_FECHAMODIFICACION,
            	EnvProgramIRPFBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvProgramIRPFBean.C_IDPROGRAM,EnvProgramIRPFBean.C_IDINSTITUCION, EnvProgramIRPFBean.C_IDENVIO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvProgramIRPFBean.C_IDPROGRAM};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvProgramIRPFBean bean = null;

		try
		{
			bean = new EnvProgramIRPFBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvProgramIRPFBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvProgramIRPFBean.C_IDENVIO));
			bean.setIdProgram(UtilidadesHash.getInteger(hash, EnvProgramIRPFBean.C_IDPROGRAM));
			bean.setIdioma(UtilidadesHash.getInteger(hash, EnvProgramIRPFBean.C_IDIOMA));
			bean.setPlantillas(UtilidadesHash.getString(hash, EnvProgramIRPFBean.C_PLANTILLAS));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvProgramIRPFBean.C_IDPERSONA));
			bean.setPeriodo(UtilidadesHash.getInteger(hash, EnvProgramIRPFBean.C_PERIODO));
			bean.setAnyoIRPF(UtilidadesHash.getInteger(hash, EnvProgramIRPFBean.C_ANIOIRPF));
			bean.setEstado(UtilidadesHash.getString(hash, EnvProgramIRPFBean.C_ESTADO));
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

			EnvProgramIRPFBean b = (EnvProgramIRPFBean) bean;

			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_IDPROGRAM, b.getIdProgram());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_PERIODO, b.getPeriodo());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_ANIOIRPF, b.getAnyoIRPF());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_IDIOMA, b.getIdioma());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_PLANTILLAS, b.getPlantillas());
			UtilidadesHash.set(htData, EnvProgramIRPFBean.C_ESTADO, b.getEstado());
		}

		catch (Exception e)
		{
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    public Integer getNewIdProgramIrpf(String idInstitucion) throws ClsExceptions{
    	
        Long idEnvio = getSecuenciaNextVal(EnvProgramInformesBean.SEQ_ENV_PROGRAMINFORMES);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        return new Integer(formato.format(new Date())+idEnvio);
    }

    public Integer getNewIdProgramIrpf(UsrBean usrBean) throws ClsExceptions{
    	return getNewIdProgramIrpf(usrBean.getLocation());
    }
    
    /**
     * 
     * @param estado 1 ó 0. Sera uno cuando se quieran los pagos ya enviados.0 con los pagos pendienmtes de enviar
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */

    public Vector getCertificadosIRPFProgramados(String estado, String idInstitucion)
			throws ClsExceptions {

		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT EPI.IDPROGRAM,EPI.IDENVIO,EPI.IDINSTITUCION, ");
		sql.append(" EPI.IDIOMA,EPI.IDPERSONA,EPI.ESTADO,EPI.ANIOIRPF,EPI.PLANTILLAS, ");
		sql.append(" EP.IDTIPOENVIOS,EP.IDPLANTILLAENVIOS,EP.IDPLANTILLA,EP.NOMBRE,EP.FECHAPROGRAMADA, ");
		sql.append(" L.CODIGOEXT ");

		sql.append(" FROM ENV_PROGRAMIRPF EPI ,ENV_ENVIOPROGRAMADO EP, ADM_LENGUAJES L ");
		sql.append(" WHERE EPI.IDINSTITUCION = EP.IDINSTITUCION ");
		sql.append(" AND EPI.IDENVIO = EP.IDENVIO ");
		sql.append(" AND EPI.IDIOMA = L.IDLENGUAJE ");
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		keyContador++;
		htCodigos.put(new Integer(keyContador), estado);
		sql.append(" AND EPI.ESTADO = :");
		sql.append(keyContador);
		if(idInstitucion!=null){
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND EPI.IDINSTITUCION = :");
			sql.append(keyContador);
		}
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try {
			rc = new RowsContainer();
			if (rc.queryBind(sql.toString(),htCodigos)) {
				EnvProgramIRPFBean programPagos = null;
				EnvEnvioProgramadoBean envioProgramado = null;
				for (int i = 0; i < rc.size(); i++) {
					
					Row row = (Row) rc.get(i);
					Hashtable htFila = row.getRow(); 
					programPagos = new EnvProgramIRPFBean();
					envioProgramado = new EnvEnvioProgramadoBean();
					programPagos.setEnvioProgramado(envioProgramado);
					programPagos.setIdInstitucion(UtilidadesHash.getInteger(htFila, EnvProgramIRPFBean.C_IDINSTITUCION));
					programPagos.setIdEnvio(UtilidadesHash.getInteger(htFila, EnvProgramIRPFBean.C_IDENVIO));
					programPagos.setIdProgram(UtilidadesHash.getInteger(htFila, EnvProgramIRPFBean.C_IDPROGRAM));
					programPagos.setIdioma(UtilidadesHash.getInteger(htFila, EnvProgramIRPFBean.C_IDIOMA));
					programPagos.setPlantillas(UtilidadesHash.getString(htFila, EnvProgramIRPFBean.C_PLANTILLAS));
					programPagos.setIdiomaCodigoExt(UtilidadesHash.getString(htFila, AdmLenguajesBean.C_CODIGOEXT));
					programPagos.setIdPersona(UtilidadesHash.getLong(htFila, EnvProgramIRPFBean.C_IDPERSONA));
					programPagos.setPeriodo(UtilidadesHash.getInteger(htFila, EnvProgramIRPFBean.C_PERIODO));
					programPagos.setAnyoIRPF(UtilidadesHash.getInteger(htFila, EnvProgramIRPFBean.C_ANIOIRPF));
					programPagos.setEstado(UtilidadesHash.getString(htFila, EnvProgramIRPFBean.C_ESTADO));
					
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