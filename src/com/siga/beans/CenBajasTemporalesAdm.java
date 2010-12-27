/*
 * Created on Mar 15, 2005
 * @author jtacosta
*/
package com.siga.beans;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.BajasTemporalesForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

public class CenBajasTemporalesAdm extends MasterBeanAdministrador {


	public CenBajasTemporalesAdm(UsrBean usuario)
	{
	    super(CenBajasTemporalesBean.T_NOMBRETABLA, usuario);
	}

	
	
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                CenBajasTemporalesBean.C_IDINSTITUCION,
                CenBajasTemporalesBean.C_IDPERSONA,
                CenBajasTemporalesBean.C_FECHABT,
                CenBajasTemporalesBean.C_TIPO,
                CenBajasTemporalesBean.C_FECHADESDE,
                CenBajasTemporalesBean.C_FECHAHASTA,
                CenBajasTemporalesBean.C_FECHAALTA,
                CenBajasTemporalesBean.C_FECHAESTADO,
                CenBajasTemporalesBean.C_VALIDADO,
                CenBajasTemporalesBean.C_DESCRIPCION,
            	CenBajasTemporalesBean.C_FECHAMODIFICACION,
            	CenBajasTemporalesBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {
        		CenBajasTemporalesBean.C_IDINSTITUCION,
                CenBajasTemporalesBean.C_IDPERSONA,
                CenBajasTemporalesBean.C_FECHABT};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {CenBajasTemporalesBean.C_FECHADESDE};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        CenBajasTemporalesBean bean = null;

		try
		{
			bean = new CenBajasTemporalesBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenBajasTemporalesBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CenBajasTemporalesBean.C_IDPERSONA));
			bean.setFechaBT(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHABT));
			bean.setTipo(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_TIPO));
			bean.setFechaDesde(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHADESDE));
			bean.setFechaHasta(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHAHASTA));
			bean.setFechaAlta(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHAALTA));
			bean.setFechaEstado(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_FECHAESTADO));
			bean.setValidado(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_VALIDADO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CenBajasTemporalesBean.C_DESCRIPCION));

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

			CenBajasTemporalesBean b = (CenBajasTemporalesBean) bean;

			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHABT, b.getFechaBT());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_TIPO, b.getTipo());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHADESDE, b.getFechaDesde());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHAHASTA, b.getFechaHasta());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHAALTA, b.getFechaAlta());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_FECHAESTADO, b.getFechaEstado());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_VALIDADO, b.getValidado());
			UtilidadesHash.set(htData, CenBajasTemporalesBean.C_DESCRIPCION, b.getDescripcion());
			

			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    /**
     * Devuelve un mapa de todos los colegiados inscritos a una guardia, donde cada registro
     * corresponde a cada dia en que cada colegiado tiene baja temporal
     * 
     * @param idColegiado
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */
	public HashMap<Long, TreeMap<String, CenBajasTemporalesBean>> getLetradosDiasBajaTemporal(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fechaDesde,
			String fechaHasta) throws ClsExceptions
	{
		CenBajasTemporalesBean bajasBean;
		Long idPersona;
		String fechaBT;
		TreeMap<String, CenBajasTemporalesBean> bajasDePersona;
		
		HashMap<Long, TreeMap<String, CenBajasTemporalesBean>> mSalida = null;
		
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer select = new StringBuffer();
			select.append(" SELECT BAJAS.* ");
			select.append("   FROM CEN_BAJASTEMPORALES BAJAS, SCS_INSCRIPCIONGUARDIA INS ");
			select.append("  WHERE BAJAS.IDINSTITUCION = INS.IDINSTITUCION ");
			select.append("    AND BAJAS.IDPERSONA = INS.IDPERSONA ");
			select.append("    AND INS.IDINSTITUCION = :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idInstitucion);
			select.append("    AND INS.IDTURNO = :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idTurno);
			select.append("    AND INS.IDGUARDIA = :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idGuardia);
			select.append("    AND TRUNC(BAJAS.FECHABT) BETWEEN :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), fechaDesde);
			select.append("                             AND :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), fechaHasta);

			Vector datos = this.selectGenericoBind(select.toString(), htCodigos);

			mSalida = new HashMap<Long, TreeMap<String, CenBajasTemporalesBean>>();
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable) datos.get(i);
				
				bajasBean = (CenBajasTemporalesBean) this.hashTableToBean(ht);
				idPersona = bajasBean.getIdPersona();
				fechaBT = bajasBean.getFechaBT();
				
				bajasDePersona = (TreeMap<String, CenBajasTemporalesBean>) mSalida.get(idPersona);
				if (bajasDePersona != null)
					bajasDePersona.put(GstDate.getFormatedDateShort("", fechaBT), bajasBean);
				else {
					bajasDePersona = new TreeMap<String, CenBajasTemporalesBean>();
					bajasDePersona.put(GstDate.getFormatedDateShort("", fechaBT), bajasBean);
					mSalida.put(idPersona, bajasDePersona);
				}
			}

		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener los getDiasBajaTemporal: " + e.toString());
		}
		
		return mSalida;
	}    

    /**
     * Devuelve un mapa de los dias en que el colegiado tiene baja temporal
     * 
     * @param idColegiado
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */
	public Map<String, CenBajasTemporalesBean> getDiasBajaTemporal(Long idColegiado, Integer idInstitucion) throws ClsExceptions
	{
		Map<String, CenBajasTemporalesBean> mSalida = null;
		
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer select = new StringBuffer();
			select.append(" SELECT * FROM CEN_BAJASTEMPORALES T ");
			select.append("  WHERE T.IDINSTITUCION = :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idInstitucion);
			select.append("    AND T.IDPERSONA = :");
			keyContador++;
			select.append(keyContador);
			htCodigos.put(new Integer(keyContador), idColegiado);

			Vector datos = this.selectGenericoBind(select.toString(), htCodigos);

			mSalida = new TreeMap<String, CenBajasTemporalesBean>();
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable) datos.get(i);
				mSalida.put(GstDate.getFormatedDateShort("", (String) ht.get(CenBajasTemporalesBean.C_FECHABT)),
						(CenBajasTemporalesBean) this.hashTableToBean(ht));
			}

		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener los getDiasBajaTemporal: " + e.toString());
		}
		
		return mSalida;
	}    
    
    /**
     * eSTE METODO NOS DEVUELVE UN MAP DE LOS LETRADOS CON BAJA TEMPORAL DE LA LISTA DE LETRADOS
     * @param alLetrados
     * @return
     * @throws ClsExceptions
     */
    public int getNumLetradosConBajaTemporal(ArrayList alLetrados,ArrayList alPeriodosDiasGuardia )
	throws ClsExceptions {

    	int numLetradosConBajaTemporal = 0;		
    	try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer select = new StringBuffer();
			select.append(" SELECT count(*) numLetradosConBajaTemporal FROM CEN_BAJASTEMPORALES T ");
			select.append(" WHERE ");
			select.append(" (T.IDINSTITUCION,T.IDPERSONA) IN ( ");
			for (int i = 0; i < alLetrados.size(); i++) {
				LetradoGuardia letrado = (LetradoGuardia)alLetrados.get(i);
				if(i!=0)
					select.append(" ,");
	
				select.append(" (:");
				keyContador++;
				select.append(keyContador);
				htCodigos.put(new Integer(keyContador), letrado.getIdInstitucion());
				select.append(" , :");
				keyContador++;
				select.append(keyContador);
				htCodigos.put(new Integer(keyContador), letrado.getIdPersona());
				select.append(") ");
				}
			select.append(") ");
			select.append(" AND T.FECHABT IN (");
			
			for (int i = 0; i < alPeriodosDiasGuardia.size(); i++) {
				String fechaPeriodo = (String)alPeriodosDiasGuardia.get(i);
				if(i==0)
				select.append(" :");
				else
					select.append(" , :");
				keyContador++;
				select.append(keyContador);
				htCodigos.put(new Integer(keyContador), fechaPeriodo);
			    
			}
			select.append(")");
			
			Vector datos = this.selectGenericoBind(select.toString(), htCodigos);
		
			if(datos !=null && datos.size()>0) {
				Hashtable ht = (Hashtable) datos.get(0);
				numLetradosConBajaTemporal = Integer.parseInt((String)ht.get("NUMLETRADOSCONBAJATEMPORAL"));
				
			}
				
			
		}
		catch (ClsExceptions e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener los getNumLetradosConBajaTemporal: "+ e.toString());
		}
		return numLetradosConBajaTemporal;
		
		}
    	
    public List<CenBajasTemporalesBean> getBajasTemporales(BajasTemporalesForm bajaTemporalForm)throws ClsExceptions{
    	StringBuffer sql = new StringBuffer();

    	
    	StringBuffer sqlWhereBT = new StringBuffer();
    	
    	sqlWhereBT.append(" AND BT.IDINSTITUCION=");
    	sqlWhereBT.append(bajaTemporalForm.getIdInstitucion());
		if(bajaTemporalForm.getEstadoBaja()!=null && !bajaTemporalForm.getEstadoBaja().equals("-1")){
			if(bajaTemporalForm.getEstadoBaja().equals("P")){
				sqlWhereBT.append(" AND BT.VALIDADO IS NULL ");
			}else if(bajaTemporalForm.getEstadoBaja().equals("V")){
				sqlWhereBT.append(" AND BT.VALIDADO='1' ");
			}else if(bajaTemporalForm.getEstadoBaja().equals("D")){
				sqlWhereBT.append(" AND BT.VALIDADO='0' ");
			}
		}
		if(bajaTemporalForm.getTipo()!=null && !bajaTemporalForm.getTipo().equals("")){
			sqlWhereBT.append(" AND BT.TIPO='");
			sqlWhereBT.append(bajaTemporalForm.getTipo());
			sqlWhereBT.append("'");
			
		}
		StringBuffer sqlFechas = new StringBuffer();
		if(bajaTemporalForm.getFechaDesde()!=null && !bajaTemporalForm.getFechaDesde().equals("")){
			sqlFechas.append(" AND TRUNC(BT.FECHABT)>='");
			sqlFechas.append(bajaTemporalForm.getFechaDesde());
			sqlFechas.append("'");
			
		}else{
			sqlFechas.append(" AND TRUNC(BT.FECHABT)>=TRUNC(SYSDATE)");
			bajaTemporalForm.setFechaDesde(GstDate.getHoyJsp());
			
		}
		if(bajaTemporalForm.getFechaHasta()!=null && !bajaTemporalForm.getFechaHasta().equals("")){
			sqlFechas.append(" AND TRUNC(BT.FECHABT)<='");
			sqlFechas.append(bajaTemporalForm.getFechaHasta());
			sqlFechas.append("'");
			
		}
		sqlWhereBT.append(sqlFechas);
		StringBuffer sqlWhereTurno = new StringBuffer();
		sqlWhereTurno.append(" AND IT.IDINSTITUCION=");
		sqlWhereTurno.append(bajaTemporalForm.getIdInstitucion());
		StringBuffer sqlWhereGuardia = new StringBuffer();
		if(bajaTemporalForm.getIdTurno()!=null && !bajaTemporalForm.getIdTurno().equals("-1")&& !bajaTemporalForm.getIdTurno().equals("")){
			sqlWhereTurno.append(" AND IT.IDTURNO=");
			sqlWhereTurno.append(bajaTemporalForm.getIdTurno());
			sqlWhereTurno.append(" ");
			
			
			
		}
		sqlWhereGuardia.append(" AND IT.IDINSTITUCION=");
		sqlWhereGuardia.append(bajaTemporalForm.getIdInstitucion());
		if(bajaTemporalForm.getIdGuardia()!=null && !bajaTemporalForm.getIdGuardia().equals("-1")&& !bajaTemporalForm.getIdGuardia().equals("")){
			sqlWhereGuardia.append(" AND IT.IDGUARDIA=");
			sqlWhereGuardia.append(bajaTemporalForm.getIdGuardia());
			sqlWhereGuardia.append(" ");
			
			
			
		}
		StringBuffer sqlBajas = new StringBuffer();
		sqlBajas.append(" SELECT DISTINCT BT.IDINSTITUCION,  BT.IDPERSONA,    ");
		//Sera null cuando venga dela ficha colegial
		if(bajaTemporalForm.getSituacion()==null || bajaTemporalForm.getSituacion().equals("B")){
			sqlBajas.append(" BT.TIPO,BT.FECHADESDE,  BT.FECHAHASTA,  BT.FECHAALTA,  BT.DESCRIPCION,BT.VALIDADO,TRUNC(BT.FECHAESTADO), ");	
		}else{
			sqlBajas.append(" 'Baja' TIPO, NULL, NULL,  NULL, ");
			sqlBajas.append(" NULL, NULL, NULL, ");	
		}
		
		sqlBajas.append(" DECODE(COL.COMUNITARIO,'1',COL.NCOMUNITARIO, COL.NCOLEGIADO) NCOLEGIADO, ");
		sqlBajas.append(" PER.NOMBRE,PER.APELLIDOS1,PER.APELLIDOS2 "); 
		sqlBajas.append(" FROM CEN_BAJASTEMPORALES BT,CEN_COLEGIADO COL,CEN_PERSONA PER ");
		sqlBajas.append(" WHERE BT.IDINSTITUCION = COL.IDINSTITUCION  AND BT.IDPERSONA = COL.IDPERSONA ");
		sqlBajas.append(" AND COL.IDPERSONA = PER.IDPERSONA ");
    	if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
    		sqlBajas.append(" AND BT.IDPERSONA=");
    		sqlBajas.append(bajaTemporalForm.getIdPersona());
    		sqlBajas.append(" ");
			
			
		}
    	sqlBajas.append(" AND TO_CHAR( BT.FECHABT,'YYYY' )>=TO_CHAR( BT.FECHABT,'YYYY' )-2 ");
    	sqlBajas.append(sqlWhereBT);
    	
    	if(bajaTemporalForm.getIdTurno()!=null && !bajaTemporalForm.getIdTurno().equals("-1")){
    		sqlBajas.append(" AND ( ");
    		sqlBajas.append(" EXISTS (SELECT IT.IDPERSONA ");
    		sqlBajas.append(" FROM SCS_INSCRIPCIONTURNO IT ");
    		sqlBajas.append(" WHERE IT.IDPERSONA = COL.IDPERSONA ");
        	if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
        		sqlBajas.append(" AND IT.IDPERSONA=");
        		sqlBajas.append(bajaTemporalForm.getIdPersona());
        		sqlBajas.append(" ");
				
			}
        	sqlBajas.append(" AND IT.IDINSTITUCION = COL.IDINSTITUCION ");
        	sqlBajas.append(" AND IT.IDINSTITUCION=");
        	sqlBajas.append(bajaTemporalForm.getIdInstitucion());
        	
        	sqlBajas.append(sqlWhereTurno);
        	
        	sqlBajas.append(" AND (IT.FECHABAJA IS NULL OR TRUNC(IT.FECHABAJA)>='");
        	sqlBajas.append(bajaTemporalForm.getFechaDesde());
        	sqlBajas.append("')");

        	sqlBajas.append("AND (IT.FECHAVALIDACION IS NOT NULL AND TRUNC(IT.FECHAVALIDACION)<='");
        	sqlBajas.append(bajaTemporalForm.getFechaDesde());
        	sqlBajas.append("')");
        	
        	sqlBajas.append(" ) ");
        	sqlBajas.append(" OR  ");
        	sqlBajas.append(" EXISTS (SELECT IT.IDPERSONA ");
        	sqlBajas.append(" FROM SCS_INSCRIPCIONGUARDIA IT ");
        	sqlBajas.append(" WHERE IT.IDPERSONA = COL.IDPERSONA ");
        	if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
        		sqlBajas.append(" AND IT.IDPERSONA=");
        		sqlBajas.append(bajaTemporalForm.getIdPersona());
        		sqlBajas.append(" ");
				
				
			}
        	sqlBajas.append(" AND IT.IDINSTITUCION = COL.IDINSTITUCION ");
        	sqlBajas.append(" AND IT.IDINSTITUCION=");
        	sqlBajas.append(bajaTemporalForm.getIdInstitucion());
        	sqlBajas.append(sqlWhereTurno);
        	sqlBajas.append(sqlWhereGuardia);
        	sqlBajas.append(" AND (IT.FECHABAJA IS NULL OR TRUNC(IT.FECHABAJA)>='");
        	sqlBajas.append(bajaTemporalForm.getFechaDesde());
        	sqlBajas.append("')");

        	sqlBajas.append("AND (IT.FECHAVALIDACION IS NOT NULL AND TRUNC(IT.FECHAVALIDACION)<='");
        	sqlBajas.append(bajaTemporalForm.getFechaDesde());
        	sqlBajas.append("')");
        	
        	sqlBajas.append(") ");
        	
        	
        	sqlBajas.append(" ) ");
			
			
		}
    	
    	StringBuffer sqlActivos = new StringBuffer();
    	sqlActivos.append(" SELECT DISTINCT COL.IDINSTITUCION,  COL.IDPERSONA, ");
		sqlActivos.append(" NULL, NULL, NULL,  NULL, ");
		sqlActivos.append(" NULL, NULL, NULL, ");
		sqlActivos.append(" DECODE(COL.COMUNITARIO, '1', COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO, ");
		sqlActivos.append(" PER.NOMBRE,  PER.APELLIDOS1,  PER.APELLIDOS2 ");
		sqlActivos.append(" FROM CEN_COLEGIADO COL, CEN_PERSONA PER ");
		sqlActivos.append(" WHERE COL.IDPERSONA = PER.IDPERSONA ");
		sqlActivos.append(" AND COL.IDINSTITUCION=");
		sqlActivos.append(bajaTemporalForm.getIdInstitucion());
		if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
			sqlActivos.append(" AND PER.IDPERSONA=");
			sqlActivos.append(bajaTemporalForm.getIdPersona());
			sqlActivos.append(" ");
		}
		sqlActivos.append(" AND EXISTS (SELECT IT.IDPERSONA ");
		sqlActivos.append(" FROM SCS_INSCRIPCIONTURNO IT ");
		sqlActivos.append(" WHERE IT.IDPERSONA = COL.IDPERSONA ");
		if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
			sqlActivos.append(" AND IT.IDPERSONA=");
			sqlActivos.append(bajaTemporalForm.getIdPersona());
			sqlActivos.append(" ");
			
			
		}
		sqlActivos.append(" AND IT.IDINSTITUCION = COL.IDINSTITUCION ");
		sqlActivos.append(sqlWhereTurno);
		
		sqlActivos.append(" AND (IT.FECHABAJA IS NULL OR TRUNC(IT.FECHABAJA)>='");
		sqlActivos.append(bajaTemporalForm.getFechaDesde());
		sqlActivos.append("')");

		sqlActivos.append("AND (IT.FECHAVALIDACION IS NOT NULL AND TRUNC(IT.FECHAVALIDACION)<='");
		sqlActivos.append(bajaTemporalForm.getFechaDesde());
		sqlActivos.append("')");
    	
		
		sqlActivos.append(" ) ");
		sqlActivos.append(" AND NOT EXISTS (SELECT * ");
		sqlActivos.append(" FROM CEN_BAJASTEMPORALES BT ");
		sqlActivos.append(" WHERE BT.IDINSTITUCION = COL.IDINSTITUCION ");
		if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
			sqlActivos.append(" AND BT.IDPERSONA=");
			sqlActivos.append(bajaTemporalForm.getIdPersona());
			sqlActivos.append(" ");
			
			
		}
		sqlActivos.append(sqlFechas);
		sqlActivos.append(" AND BT.VALIDADO='1'");
		sqlActivos.append(" AND BT.IDINSTITUCION=");
		sqlActivos.append(bajaTemporalForm.getIdInstitucion());
		sqlActivos.append(" AND BT.IDPERSONA = COL.IDPERSONA) ");
		

		sqlActivos.append(" UNION ");

		sqlActivos.append(" SELECT DISTINCT COL.IDINSTITUCION,  COL.IDPERSONA, ");
		sqlActivos.append(" NULL, NULL, NULL,  NULL, ");
		sqlActivos.append(" NULL, NULL, NULL, ");
		sqlActivos.append(" DECODE(COL.COMUNITARIO, '1', COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO, ");
		sqlActivos.append(" PER.NOMBRE,  PER.APELLIDOS1,  PER.APELLIDOS2 ");
		sqlActivos.append(" FROM CEN_COLEGIADO COL, CEN_PERSONA PER ");
		sqlActivos.append(" WHERE COL.IDPERSONA = PER.IDPERSONA ");
		sqlActivos.append(" AND COL.IDINSTITUCION=");
		sqlActivos.append(bajaTemporalForm.getIdInstitucion());
		if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
			sqlActivos.append(" AND PER.IDPERSONA=");
			sqlActivos.append(bajaTemporalForm.getIdPersona());
			sqlActivos.append(" ");
			
			
		}

		sqlActivos.append(" AND EXISTS (SELECT IT.IDPERSONA ");
		sqlActivos.append(" FROM SCS_INSCRIPCIONGUARDIA IT ");
		sqlActivos.append(" WHERE IT.IDPERSONA = COL.IDPERSONA ");
		if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
			sqlActivos.append(" AND IT.IDPERSONA=");
			sqlActivos.append(bajaTemporalForm.getIdPersona());
			sqlActivos.append(" ");
			
			
		}
		sqlActivos.append(" AND IT.IDINSTITUCION = COL.IDINSTITUCION ");
		sqlActivos.append(sqlWhereTurno);
		sqlActivos.append(sqlWhereGuardia);
		sqlActivos.append(" AND (IT.FECHABAJA IS NULL OR TRUNC(IT.FECHABAJA)>='");
		sqlActivos.append(bajaTemporalForm.getFechaDesde());
		sqlActivos.append("')");

		sqlActivos.append("AND (IT.FECHAVALIDACION IS NOT NULL AND TRUNC(IT.FECHAVALIDACION)<='");
		sqlActivos.append(bajaTemporalForm.getFechaDesde());
		sqlActivos.append("')");
		sqlActivos.append(" ) ");
		sqlActivos.append(" AND NOT EXISTS (SELECT * ");
		sqlActivos.append(" FROM CEN_BAJASTEMPORALES BT ");
		sqlActivos.append(" WHERE BT.IDINSTITUCION = COL.IDINSTITUCION ");
		if(bajaTemporalForm.getIdPersona()!=null && !bajaTemporalForm.getIdPersona().equals("")){
			sqlActivos.append(" AND BT.IDPERSONA=");
			sqlActivos.append(bajaTemporalForm.getIdPersona());
			sqlActivos.append(" ");
		}
		sqlActivos.append(sqlFechas);
		sqlActivos.append(" AND BT.VALIDADO='1'");
		sqlActivos.append(" AND BT.IDINSTITUCION=");
		sqlActivos.append(bajaTemporalForm.getIdInstitucion());
		sqlActivos.append(" AND BT.IDPERSONA = COL.IDPERSONA) ");
		
		if(bajaTemporalForm.getSituacion()==null || bajaTemporalForm.getSituacion().equals("B")){
        	sql.append(sqlBajas);
        	sql.append(" ORDER BY  BT.FECHADESDE,PER.APELLIDOS1 ");
		}else if(bajaTemporalForm.getSituacion().equals("T")){
			sql.append(sqlBajas);
			sql.append(" UNION ");
			sql.append(sqlActivos);

			sql.append(" ORDER BY APELLIDOS1 ");
		}else if(bajaTemporalForm.getSituacion().equals("A")){
			sql.append(sqlActivos);
				sql.append(" ORDER BY APELLIDOS1 ");
			
		}
    	
    	List<CenBajasTemporalesBean> alBajasTemporales = null;
    	try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql.toString())) {
				alBajasTemporales = new ArrayList<CenBajasTemporalesBean>();
				
				CenPersonaBean persona = null;
				CenColegiadoBean colegiado = null;
				CenBajasTemporalesBean bajaTemporal = null;

				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					bajaTemporal =  (CenBajasTemporalesBean)this.hashTableToBean(htFila);
					bajaTemporal.setUsrBean(this.usrbean);
					
					
					persona = new CenPersonaBean();
					colegiado = new CenColegiadoBean();
					persona.setColegiado(colegiado);
					bajaTemporal.setPersona(persona);
					colegiado.setIdInstitucion(UtilidadesHash.getInteger(htFila,CenColegiadoBean.C_IDINSTITUCION));
					colegiado.setNColegiado(UtilidadesHash.getString(htFila,CenColegiadoBean.C_NCOLEGIADO));
					persona.setIdPersona(UtilidadesHash.getLong(htFila,CenPersonaBean.C_IDPERSONA));
					persona.setNombre(UtilidadesHash.getString(htFila,CenPersonaBean.C_NOMBRE));
					persona.setApellido1(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS1));
					persona.setApellido2(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS2));
					alBajasTemporales.add(bajaTemporal);
				}
			}else{
				alBajasTemporales = new ArrayList<CenBajasTemporalesBean>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return alBajasTemporales;
    }
    public void validarBajaTemporal(CenBajasTemporalesBean bajaTemporal) throws ClsExceptions{
    	String[] claves ={CenBajasTemporalesBean.C_IDINSTITUCION,CenBajasTemporalesBean.C_FECHAALTA,CenBajasTemporalesBean.C_IDPERSONA};
    	String[] campos ={CenBajasTemporalesBean.C_VALIDADO,CenBajasTemporalesBean.C_FECHAESTADO};
    	try {
			updateDirect(bajaTemporal, claves, campos);
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al modificar Baja temporal.");
		}
    	
    	
    	
    }
    public void borrarBajaTemporal(CenBajasTemporalesBean bajaTemporal) throws ClsExceptions{
    	String[] claves ={CenBajasTemporalesBean.C_IDINSTITUCION,CenBajasTemporalesBean.C_FECHAALTA,CenBajasTemporalesBean.C_IDPERSONA};
    	try {
			deleteDirect(this.beanToHashTable(bajaTemporal), claves);
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al borrar baja Temporal.");
		}
    	
    	
    	
    }
    public void modificarBajaTemporal(CenBajasTemporalesBean bajaTemporal) throws ClsExceptions{
    	String[] claves ={CenBajasTemporalesBean.C_IDINSTITUCION,CenBajasTemporalesBean.C_FECHAALTA,CenBajasTemporalesBean.C_IDPERSONA};
    	String[] campos ={CenBajasTemporalesBean.C_TIPO,CenBajasTemporalesBean.C_DESCRIPCION};
    	try {
    		updateDirect(bajaTemporal, claves, campos);
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al modificar Baja temporal.");
		}
    	
    }
    public void setBajaTemporal(BajasTemporalesForm bajaTemporalForm)throws ClsExceptions {
    	CenBajasTemporalesBean bajaTemporalBean = bajaTemporalForm.getBajaTemporalBean();
    	Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM CEN_BAJASTEMPORALES WHERE IDINSTITUCION=:");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),bajaTemporalBean.getIdInstitucion());
		sql.append(" AND IDPERSONA=:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),bajaTemporalBean.getIdPersona());
    	sql.append(" AND FECHAALTA=");
    	sql.append("TO_DATE('");
    	sql.append(bajaTemporalBean.getFechaAlta());
    	sql.append("', 'YYYY/MM/DD HH24:MI:SS')");
		
		RowsContainer rc = new RowsContainer(); 
		
        if (rc.findBind(sql.toString(),htCodigos)) {

        	if(rc.size()>1){
        		Row fila = (Row) rc.get(0);
        		Hashtable<String, Object> htFila=fila.getRow();
        		bajaTemporalBean.setFechaBT((String)htFila.get(CenBajasTemporalesBean.C_FECHABT));
        		bajaTemporalBean.setFechaDesde((String)htFila.get(CenBajasTemporalesBean.C_FECHADESDE));
        		bajaTemporalBean.setFechaHasta((String)htFila.get(CenBajasTemporalesBean.C_FECHAHASTA));
        		bajaTemporalBean.setFechaAlta((String)htFila.get(CenBajasTemporalesBean.C_FECHAALTA));
        		bajaTemporalBean.setDescripcion((String)htFila.get(CenBajasTemporalesBean.C_DESCRIPCION));
        		bajaTemporalBean.setTipo((String)htFila.get(CenBajasTemporalesBean.C_TIPO));
        		bajaTemporalBean.setUsrBean(this.usrbean);
        		
        	}else
    			throw new ClsExceptions("Error fatal al obtener la baja temporal");
        }
		
	}
    public CenBajasTemporalesBean getBajaTemporal(BajasTemporalesForm bajaTemporalForm)throws ClsExceptions {
    	Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM CEN_BAJASTEMPORALES WHERE IDINSTITUCION=:");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),bajaTemporalForm.getBajaTemporalBean().getIdInstitucion());
		sql.append(" AND IDPERSONA=:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),bajaTemporalForm.getBajaTemporalBean().getIdPersona());
    	sql.append(" AND FECHAALTA=");
    	sql.append("TO_DATE('");
    	sql.append(bajaTemporalForm.getBajaTemporalBean().getFechaAlta());
    	sql.append("', 'YYYY/MM/DD HH24:MI:SS')");
		
		RowsContainer rc = new RowsContainer(); 
		CenBajasTemporalesBean bajaTemporalBean = null;
        if (rc.findBind(sql.toString(),htCodigos)) {

        	if(rc.size()>0){
        		Row fila = (Row) rc.get(0);
        		Hashtable<String, Object> htFila=fila.getRow();
        		bajaTemporalBean = (CenBajasTemporalesBean)hashTableToBean(htFila);
        		bajaTemporalBean.setUsrBean(this.usrbean);
        	}else
    			throw new ClsExceptions("Error fatal al obtener la baja temporal");
        }
        return bajaTemporalBean;
		
	}
    
    public Map<Long,CenPersonaBean> getColegiadosBajaTemporal(Integer idInstitucion,String fechaDesde,String fechaHasta)
	throws ClsExceptions {

    	List<Long> alColegiados = null;
		
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT BT.IDPERSONA,");
		sql.append(" DECODE(COL.COMUNITARIO, '1', COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO, ");
		sql.append(" PER.NOMBRE,  PER.APELLIDOS1,  PER.APELLIDOS2 ");
		sql.append(" FROM CEN_BAJASTEMPORALES BT,CEN_COLEGIADO COL, CEN_PERSONA PER ");
		sql.append(" WHERE COL.IDPERSONA = PER.IDPERSONA ");
		sql.append(" AND BT.IDINSTITUCION = COL.IDINSTITUCION ");
		sql.append(" AND BT.IDPERSONA = PER.IDPERSONA ");
		sql.append(" AND BT.IDINSTITUCION =:");
		keyContador++;
		sql.append(keyContador);
		htCodigos.put(new Integer(keyContador), idInstitucion);
		sql.append(" AND BT.FECHABT  BETWEEN :");
		keyContador++;
		sql.append(keyContador);
		htCodigos.put(new Integer(keyContador),GstDate.getFormatedDateShort("",fechaDesde));
		sql.append(" AND :");
		keyContador++;
		sql.append(keyContador);
		htCodigos.put(new Integer(keyContador),GstDate.getFormatedDateShort("",fechaHasta));	
		Map<Long,CenPersonaBean> tmPersonas = null;
		CenPersonaBean persona = null;
		CenColegiadoBean colegiado = null;
		try {
			Vector datos = this.selectGenericoBind(sql.toString(), htCodigos);
			
			tmPersonas = new  TreeMap<Long, CenPersonaBean>();
			for (int i = 0; i < datos.size(); i++) {
				Hashtable htFila = (Hashtable) datos.get(i);
				persona = new CenPersonaBean();
				colegiado = new CenColegiadoBean();
				persona.setColegiado(colegiado);
				colegiado.setIdInstitucion(UtilidadesHash.getInteger(htFila,CenColegiadoBean.C_IDINSTITUCION));
				colegiado.setNColegiado(UtilidadesHash.getString(htFila,CenColegiadoBean.C_NCOLEGIADO));
				persona.setIdPersona(UtilidadesHash.getLong(htFila,CenPersonaBean.C_IDPERSONA));
				persona.setNombre(UtilidadesHash.getString(htFila,CenPersonaBean.C_NOMBRE));
				persona.setApellido1(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS1));
				persona.setApellido2(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS2));
				
				tmPersonas.put(persona.getIdPersona(),persona);
			}
 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }finally{
    	   if(tmPersonas==null)
    		   tmPersonas = new  TreeMap<Long, CenPersonaBean>();
       }
       return tmPersonas;
    }

}