/*
 * Created on Mar 15, 2005
 * @author jtacosta
*/
package com.siga.beans;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvProgramInformesAdm extends MasterBeanAdministrador {

  public final static int ESTADO_INICIAL = 1;
  public final static int ESTADO_PROCESADO = 2;
  public final static int ESTADO_PROCESADO_ERRORES= 3;
  public final static int ESTADO_PENDIENTE_AUTOMATICO = 4;

  public final static String NO_GENERAR = "N";
  public final static String FECHA_CREACION = "C";
  public final static String FECHA_PROGRAMADA = "P";

	public EnvProgramInformesAdm(UsrBean usuario)
	{
	    super(EnvProgramInformesBean.T_NOMBRETABLA, usuario);
	}

	
	
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvProgramInformesBean.C_IDINSTITUCION,
                EnvProgramInformesBean.C_IDENVIO,
                EnvProgramInformesBean.C_IDPROGRAM,
                EnvProgramInformesBean.C_ESTADO,
                EnvProgramInformesBean.C_IDTIPOINFORME,
                EnvProgramInformesBean.C_IDIOMA,
                EnvProgramInformesBean.C_PLANTILLAS,
                EnvProgramInformesBean.C_CLAVES,
            	EnvProgramInformesBean.C_FECHAMODIFICACION,
            	EnvProgramInformesBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvProgramInformesBean.C_IDPROGRAM,EnvProgramInformesBean.C_IDINSTITUCION, EnvProgramInformesBean.C_IDENVIO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvProgramInformesBean.C_IDPROGRAM};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvProgramInformesBean bean = null;

		try
		{
			bean = new EnvProgramInformesBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvProgramInformesBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvProgramInformesBean.C_IDENVIO));
			bean.setIdProgram(UtilidadesHash.getInteger(hash, EnvProgramInformesBean.C_IDPROGRAM));
			bean.setIdioma(UtilidadesHash.getInteger(hash, EnvProgramInformesBean.C_IDIOMA));
			bean.setPlantillas(UtilidadesHash.getString(hash, EnvProgramInformesBean.C_PLANTILLAS));
			bean.setIdTipoInforme(UtilidadesHash.getString(hash, EnvProgramInformesBean.C_IDTIPOINFORME));
			bean.setClaves(UtilidadesHash.getString(hash, EnvProgramInformesBean.C_CLAVES));
			bean.setEstado(UtilidadesHash.getString(hash, EnvProgramInformesBean.C_ESTADO));
			
			
			
			
			

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

			EnvProgramInformesBean b = (EnvProgramInformesBean) bean;

			UtilidadesHash.set(htData, EnvProgramInformesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_IDPROGRAM, b.getIdProgram());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_IDTIPOINFORME, b.getIdTipoInforme());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_CLAVES, b.getClaves());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_IDIOMA, b.getIdioma());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_PLANTILLAS, b.getPlantillas());
			UtilidadesHash.set(htData, EnvProgramInformesBean.C_ESTADO, b.getEstado());
			

			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    public Integer getNewIdProgramInformes(String idInstitucion) throws ClsExceptions{
    	
        Long idEnvio = getSecuenciaNextVal(EnvProgramInformesBean.SEQ_ENV_PROGRAMINFORMES);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        return new Integer(formato.format(new Date())+idEnvio);
    }

    public Integer getNewIdProgramInformes(UsrBean usrBean) throws ClsExceptions{
    	return getNewIdProgramInformes(usrBean.getLocation());
    }
    
    
    /**
     * 
     * @param estado 1 � 0. Sera uno cuando se quieran los pagos ya enviados.0 con los pagos pendienmtes de enviar
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */

    public Vector getInformesGenericosProgramados(String estado,List<AdmTipoInformeBean> tiposInformeExcluir)
			throws ClsExceptions {
    	StringBuffer sqlGlobal = new StringBuffer();
    	StringBuffer sqlExcluidos = new StringBuffer();
    	StringBuffer sqlUnionExcluidos = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT EPI.IDPROGRAM,EPI.IDENVIO,EPI.IDINSTITUCION, ");
		sql.append(" EPI.IDIOMA,EPI.ESTADO,EPI.PLANTILLAS, ");
		sql.append(" EPI.IDTIPOINFORME,EPI.CLAVES, ");
		sql.append(" EP.IDTIPOENVIOS,EP.IDPLANTILLAENVIOS,EP.IDPLANTILLA,EP.NOMBRE,EP.FECHAPROGRAMADA, EP.ACUSERECIBO, ");
		sql.append(" L.CODIGOEXT,EP.COMISIONAJG ");
		// jbd // Nos traemos el usumodificacion para luego pasarselo al hitorico y saber quien ha hecho el envio
		sql.append(" , EP.USUMODIFICACION "); 
		

		sql.append(" FROM ENV_PROGRAMINFORMES EPI ,ENV_ENVIOPROGRAMADO EP, ADM_LENGUAJES L ");
		sql.append(" WHERE EPI.IDINSTITUCION = EP.IDINSTITUCION ");
		sql.append(" AND EPI.IDENVIO = EP.IDENVIO ");
		sql.append(" AND EPI.IDIOMA = L.IDLENGUAJE ");
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		
		
//		if(tiposInformeIncluir!=null && tiposInformeIncluir.size()>0) {
//			sql.append(" AND EPI.IDTIPOINFORME  in (");
//			for (String tipoInformeIncluir : tiposInformeIncluir) {
//				keyContador++;
//				htCodigos.put(new Integer(keyContador), tipoInformeIncluir);
//				sql.append(":");
//				sql.append(keyContador);
//				sql.append(",");
//			}
//			sql.deleteCharAt(sql.length()-1);
//			sql.append(" ) ");
//			
//		}
		
		sqlGlobal.append(sql);
		keyContador++;
		htCodigos.put(new Integer(keyContador), estado);
		sqlGlobal.append(" AND EPI.ESTADO = :");
		sqlGlobal.append(keyContador);
		
		
		if(tiposInformeExcluir!=null && tiposInformeExcluir.size()>0) {
			
			sqlUnionExcluidos.append(sql);
			
			
			sqlExcluidos.append(" AND EPI.IDTIPOINFORME  not in (");
			
			for (AdmTipoInformeBean tipoInformeExcluir : tiposInformeExcluir) {
				keyContador++;
				htCodigos.put(new Integer(keyContador), tipoInformeExcluir.getIdTipoInforme());
				sqlExcluidos.append(":");
				sqlExcluidos.append(keyContador);
				sqlExcluidos.append(",");
				
				
			}
			sqlExcluidos.deleteCharAt(sqlExcluidos.length()-1);
			sqlExcluidos.append(" ) ");
			
			sqlUnionExcluidos.append(" AND EPI.IDTIPOINFORME  in (");
			for (AdmTipoInformeBean tipoInformeExcluir : tiposInformeExcluir) {
				
				keyContador++;
				htCodigos.put(new Integer(keyContador), tipoInformeExcluir.getIdTipoInforme());
				sqlUnionExcluidos.append(":");
				sqlUnionExcluidos.append(keyContador);
				sqlUnionExcluidos.append(",");
				
			}
			
			sqlUnionExcluidos.deleteCharAt(sqlUnionExcluidos.length()-1);
			sqlUnionExcluidos.append(" ) ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), estado);
			sqlUnionExcluidos.append(" AND EPI.ESTADO = :");
			sqlUnionExcluidos.append(keyContador);
			
			sqlUnionExcluidos.append(" AND EP.FECHAPROGRAMADA < SYSDATE");
			
			sqlGlobal.append(sqlExcluidos);
			
			sqlGlobal.append(" UNION ");
//			sqlGlobal.append(sql);
			sqlGlobal.append(sqlUnionExcluidos);
			
		}
		
		// Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try {
			rc = new RowsContainer();
			if (rc.queryBind(sqlGlobal.toString(),htCodigos)) {
				EnvProgramInformesBean programPagos = null;
				EnvEnvioProgramadoBean envioProgramado = null;
				for (int i = 0; i < rc.size(); i++) {
					
					Row row = (Row) rc.get(i);
					Hashtable htFila = row.getRow(); 
					programPagos = new EnvProgramInformesBean();
					envioProgramado = new EnvEnvioProgramadoBean();
					programPagos.setEnvioProgramado(envioProgramado);
					programPagos.setIdInstitucion(UtilidadesHash.getInteger(htFila, EnvProgramInformesBean.C_IDINSTITUCION));
					programPagos.setIdEnvio(UtilidadesHash.getInteger(htFila, EnvProgramInformesBean.C_IDENVIO));
					programPagos.setIdProgram(UtilidadesHash.getInteger(htFila, EnvProgramInformesBean.C_IDPROGRAM));
					programPagos.setIdioma(UtilidadesHash.getInteger(htFila, EnvProgramInformesBean.C_IDIOMA));
					programPagos.setPlantillas(UtilidadesHash.getString(htFila, EnvProgramInformesBean.C_PLANTILLAS));
					programPagos.setIdiomaCodigoExt(UtilidadesHash.getString(htFila, AdmLenguajesBean.C_CODIGOEXT));
					//programPagos.setIdPersona(UtilidadesHash.getLong(htFila, EnvProgramInformesBean.C_IDPERSONA));
					programPagos.setIdTipoInforme(UtilidadesHash.getString(htFila, EnvProgramInformesBean.C_IDTIPOINFORME));
					programPagos.setClaves(UtilidadesHash.getString(htFila, EnvProgramInformesBean.C_CLAVES));
					programPagos.setEstado(UtilidadesHash.getString(htFila, EnvProgramInformesBean.C_ESTADO));
					
					envioProgramado.setIdInstitucion(programPagos.getIdInstitucion());
					envioProgramado.setIdEnvio(programPagos.getIdEnvio());
					envioProgramado.setIdTipoEnvios(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_IDTIPOENVIOS));
					envioProgramado.setIdPlantillaEnvios(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_IDPLANTILLAENVIOS));
					envioProgramado.setIdPlantilla(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_IDPLANTILLA));
					envioProgramado.setNombre(UtilidadesHash.getString(htFila, EnvEnvioProgramadoBean.C_NOMBRE));
					envioProgramado.setFechaProgramada(UtilidadesHash.getString(htFila, EnvEnvioProgramadoBean.C_FECHAPROGRAMADA));
					envioProgramado.setAcuseRecibo(UtilidadesHash.getString(htFila, EnvEnvioProgramadoBean.C_ACUSERECIBO));
					envioProgramado.setComisionAJG(UtilidadesHash.getShort(htFila, EnvEnvioProgramadoBean.C_COMISIONAJG));
					// jbd // A�adimos el usumodificacion del creador
					envioProgramado.setUsuMod(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_USUMODIFICACION));
					programPagos.setUsuMod(UtilidadesHash.getInteger(htFila, EnvEnvioProgramadoBean.C_USUMODIFICACION));
					datos.add(programPagos);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.getInformesGenericosProgramados");
		}
		return datos;
	}
   


}