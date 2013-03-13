package com.siga.beans;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.general.SIGAException;

public class HelperInformesAdm  {
	
	private String TABULADOR = "\t";
	private String SALTOLINEA = "\n";

	
	public Vector selectGenericoBindHashVacio(String select, Hashtable codigos) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.findBindHashVacio(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage()+" Consulta SQL:"+select);
		}
		return datos;	
	}
	public  Vector ejecutaConsultaBind(String sql, Hashtable h) throws ClsExceptions {
		Vector salida = new Vector();
		try {
			salida = this.selectGenericoBindHashVacio(sql, h);

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consutla para devolver vector incluso vacio");
		}
		return salida;
	}
	public Hashtable completarHashSalida(Hashtable htSalida, Vector vParcial){
		
		if (vParcial!=null && vParcial.size()>0) {
			Hashtable registro = (Hashtable) vParcial.get(0);
			htSalida.putAll(registro);
		}
	
		return htSalida;
		
		
	}
	
	public Vector ejecutaFuncionSalida (Hashtable codigos, 
			String nombreFuncion, String campoSalida) throws ClsExceptions  
	{
	    boolean salidaVacia = false;
		try {
			String sql = "select "+nombreFuncion+"(";
			for (int i=1; i<10; i++) {
				if (codigos.containsKey(new Integer(i))) {
				    if (codigos.get(new Integer(i))==null || ((String)codigos.get(new Integer(i))).trim().equals("")) {
				        salidaVacia = true;
				    }
					sql += ":"+new Integer(i).toString()+",";
				} else {
					break;
				}
			}
				
			//QUITAMOS LA ULTIMA , 
			sql = sql.substring(0,sql.length()-1);
			sql += ") AS "+campoSalida+" FROM DUAL"; 
			//System.out.println("sql:"+sql);
			
			if (salidaVacia) {
			    Hashtable sal = new Hashtable();
			    Vector vsal = new Vector();
			    sal.put(campoSalida," ");
			    vsal.add(sal);
			    return vsal;
			}
			
				
			return ejecutaConsultaBind(sql, codigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener HelperInformesAdm.ejecutaFuncionSalida ");
		}
	}
	/**
	 * 
	 * @param codigos
	 * @param nombreFuncion
	 * @param campoSalida
	 * @param modificadorSalida tochar...
	 * @return
	 * @throws ClsExceptions
	 */
	/*public Vector ejecutaFuncionSalida (Hashtable codigos, 
			String nombreFuncion, String campoSalida,String modificadorSalida) throws ClsExceptions  
	{
	    boolean salidaVacia = false;
		try {
			StringBuffer sql = new StringBuffer("select ");
			sql.append(" ");
			sql.append(modificadorSalida);
			//ABRIMOS E4L MODIFICADOR
			sql.append("(");
			sql.append(nombreFuncion);
			sql.append("(");
			for (int i=1; i<10; i++) {
				if (codigos.containsKey(new Integer(i))) {
				    if (codigos.get(new Integer(i))==null || ((String)codigos.get(new Integer(i))).trim().equals("")) {
				        salidaVacia = true;
				    }
				    sql.append(":");
				    sql.append(new Integer(i).toString());
				    sql.append(",");
				} else {
					break;
				}
			}
				
			//QUITAMOS LA ULTIMA , 
			sql = new StringBuffer(sql.substring(0,sql.length()-1));
			sql.append(") ");
			//CERRAMOS EL MODIFICADOR
			sql.append(")");
			sql.append(" AS ");
			sql.append(campoSalida);
			sql.append(" FROM DUAL"); 
			
			if (salidaVacia) {
			    Hashtable sal = new Hashtable();
			    Vector vsal = new Vector();
			    sal.put(campoSalida," ");
			    vsal.add(sal);
			    return vsal;
			}
			
				
			return ejecutaConsultaBind(sql.toString(), codigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener HelperInformesAdm.ejecutaFuncionSalida ");
		}
	}
	*/
	
	public Vector getNombrePoblacionSalida(String idPoblacion,String nombreCampoSalida) throws ClsExceptions  
	{
		Vector salida = new Vector();
		
		try {
			Hashtable h = new Hashtable();
			if (idPoblacion==null) {
				h.put(nombreCampoSalida, "");
				salida.add(h);
			} else {
			
				h.put(new Integer(1), idPoblacion);
				
				String sql = "SELECT "+
				" NOMBRE AS "+nombreCampoSalida+" FROM CEN_POBLACIONES"+  
				" WHERE IDPOBLACION = :1";
				
				
				salida = ejecutaConsultaBind(sql, h);
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las getNombrePoblacion");
		}
		return salida;
	}
	
	public Vector getNombreProvinciaSalida(String idPoblacion,String nombreCampoSalida) throws ClsExceptions  
	{
		Vector salida = new Vector();
		
		try {
			Hashtable h = new Hashtable();
			if (idPoblacion==null) {
				h.put(nombreCampoSalida, "");
				salida.add(h);
			} else {
				h.put(new Integer(1), idPoblacion);
				
				String sql = "SELECT "+
				" NOMBRE AS "+nombreCampoSalida+" FROM CEN_PROVINCIAS"+  
				" WHERE IDPROVINCIA = :1";
					
				
				salida = ejecutaConsultaBind(sql, h);
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las getNombreProvincia");
		}
		return salida;
	}
	public Vector getTurnoSalida (String idInstitucion, String idTurno) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idTurno);
			
			String sql = "select scs_turno.descripcion AS DESCRIPCION_TURNO ,"+
			" scs_turno.abreviatura AS ABREV_TURNO ,scs_turno.LETRADOACTUACIONES AS LETRADOACTUACIONES, scs_turno.validarjustificaciones as VALIDARJUSTIFICACIONES " +
			",scs_turno.ACTIVARRETRICCIONACREDIT "+
			" from scs_turno where idinstitucion = :1 and idturno = :2";
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTurnoSalida.");
		}
	}
	public Vector getTipoDesignaColegio (String idInstitucion, String idTipoDesigna,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idTipoDesigna);
			String sql = "SELECT f_siga_getrecurso(SCS_TIPODESIGNACOLEGIO.DESCRIPCION,"+idioma+") AS DESC_TIPODESIGNA "
				+" FROM SCS_TIPODESIGNACOLEGIO "
				+" WHERE " 
				+" SCS_TIPODESIGNACOLEGIO.IDINSTITUCION = :1 "
				+" AND SCS_TIPODESIGNACOLEGIO.IDTIPODESIGNACOLEGIO = :2";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoDesignaColegio.");
		}
	}
	
	
	public Vector getProcedimientoSalida (String idInstitucion, String idProc,String prolongacionCampo ) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idProc);
			String prolongarCampo ="";
			if(prolongacionCampo!=null && !prolongacionCampo.equals("")){
				prolongarCampo += "_";
				prolongarCampo += prolongacionCampo;
				
			}
						
			String sql = " select NOMBRE AS PROCEDIMIENTO";
			sql += prolongarCampo;
			sql +=",codigo AS CATEGORIA";
			sql += prolongarCampo;
			sql +=",idjurisdiccion AS IDJURISDICCION";
			sql += prolongarCampo;
			sql +=",COMPLEMENTO AS COMPLEMENTO";
			sql += prolongarCampo;
			sql +=",PERMITIRANIADIRLETRADO AS PERMITIRANIADIRLETRADO";
			sql += prolongarCampo;
			
			sql +=" from scs_procedimientos"+
			" WHERE IDINSTITUCION = :1 "+
			" AND IDPROCEDIMIENTO = :2 ";
			
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getProcedimientoSalida");
		}
	}
	public Vector getJuzgadoSalida (String idInstitucion, String idJuz, String prolongacionCampo) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idJuz);
			String prolongarCampo = "";
			if(prolongacionCampo!=null && !prolongacionCampo.equals("")){
				prolongarCampo += "_";
				prolongarCampo += prolongacionCampo;
				
			}
			
			String sql = " SELECT JUZ2.IDINSTITUCION IDINSTITUCION_JUZG" +
			prolongarCampo+
					","+
			   " JUZ2.IDJUZGADO AS IDJUZGADO" + prolongarCampo + "," +
		       " JUZ2.NOMBRE AS JUZGADO" + prolongarCampo + "," +
		       " JUZ2.DOMICILIO AS DIR_JUZGADO" + prolongarCampo + "," +
		       " JUZ2.CODIGOPOSTAL  AS CP_JUZGADO" + prolongarCampo + "," +
		       " JUZ2.IDPROVINCIA AS ID_PROVINCIA_JUZGADO" + prolongarCampo + "," +
		       " JUZ2.IDPOBLACION AS ID_POBLACION_JUZGADO" + prolongarCampo + "," +
		       " JUZ2.CODIGOEXT AS CODIGOJUZGADO" + prolongarCampo + "," +
		       " JUZ2.CODIGOEXT2 AS CODIGOJUZGADO2" + prolongarCampo +
		       " FROM SCS_JUZGADO JUZ2" +
		       " WHERE JUZ2.IDINSTITUCION= :1 AND  JUZ2.IDJUZGADO = :2 ";
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getJuzgadoSalida");
		}
	}
	public Vector getTipoResolucionAutomatico (String idTipoResolucion, String idioma) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idTipoResolucion);
			String sql = "SELECT f_siga_getrecurso(scs_tiporesolauto.DESCRIPCION,"+idioma+") AS DESC_TIPORESOLAUTO "
				+" FROM scs_tiporesolauto "
				+" WHERE " 
				
				+" scs_tiporesolauto.idtiporesolauto = :1";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoResolucionAutomatico.");
		}
	}
	public Vector getTipoSentidoAutomatico ( String idTipoSentido, String idioma) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idTipoSentido);
			String sql = "SELECT f_siga_getrecurso(scs_tiposentidoauto.DESCRIPCION,"+idioma+") AS DESC_TIPOSENTIDOAUTO "
				+" FROM scs_tiposentidoauto "
				+" WHERE " 
				
				+" scs_tiposentidoauto.idtiposentidoauto = :1";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoSentidoAutomatico.");
		}
	}
	
	public Vector getTipoDictamenEjg (String idInstitucion, String idTipoDictamen, String idioma) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idTipoDictamen);
			String sql = "SELECT f_siga_getrecurso(scs_tipodictamenejg.DESCRIPCION,"+idioma+") AS DESC_TIPODICTAMENEJG "
				+" FROM scs_tipodictamenejg "
				+" WHERE " 
				+" scs_tipodictamenejg.idinstitucion = :1 AND "
				+" scs_tipodictamenejg.idtipodictamenejg = :2";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoDictamenEjg.");
		}
	}
	public Vector getTipoRatificacionEjg (String idTipoRatificacion, String idioma) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
		
			h.put(new Integer(1), idTipoRatificacion);
			String sql = "SELECT f_siga_getrecurso(SCS_TIPORESOLUCION.DESCRIPCION,"+idioma+") AS DESC_TIPORATIFICACIONEJG "
				+" FROM SCS_TIPORESOLUCION "
				+" WHERE " 
				
				+" SCS_TIPORESOLUCION.IDTIPORESOLUCION = :1";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoRatificacionEjg.");
		}
	}
	
	public Vector getPersonaInstitucion (String idInstitucion, String idioma)throws SIGAException,ClsExceptions
	{	
		try { 
			 
			StringBuffer sql = new StringBuffer();
			sql.append(" select ins.nombre NOMBREINSTITUCION,per.nifcif AS NIFCIFINSTITUCION ");
			sql.append(" ,f_siga_getdireccioncliente(ins.idinstitucion,per.idpersona,1,3) AS INSTITUCIONPOBLACION ");
			sql.append(" ,TO_CHAR(SYSDATE, 'dd') AS DIA_HOY ");
			//sql.append(" ,TO_CHAR(SYSDATE, 'MONTH', 'NLS_DATE_LANGUAGE = SPANISH') AS MES_HOY ");
			sql.append(" ,pkg_siga_fecha_en_letra.f_siga_fechaenletra(to_char(sysdate),'M', "+idioma+") AS MES_HOY ");
			sql.append(" ,TO_CHAR(SYSDATE, 'yyyy') AS ANIO_HOY ");
			sql.append(" from cen_institucion ins,cen_persona per ");
			sql.append(" where ins.idpersona = per.idpersona ");
			sql.append(" and ins.idinstitucion =:1");
			Hashtable htCodigos = new Hashtable();
			htCodigos.put(new Integer("1"), idInstitucion);
			


            
			return ejecutaConsultaBind(sql.toString(), htCodigos);
		}
		
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.getPersonaInstitucion");
		}
		
	}
	
	
	public Vector getCamposConfigurablesExpediente(Vector datos, String idInstitucion, String idInstitucionTipoExp, String idTipoExp, String anio, String numero, String idPersona)throws SIGAException,ClsExceptions
	{	
		try { 
			 
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1), idInstitucion);
			codigos.put(new Integer(2), idInstitucionTipoExp);
			codigos.put(new Integer(3), idTipoExp);
			codigos.put(new Integer(4), anio);
			codigos.put(new Integer(5), numero);
			

			for (int j=0;j<datos.size();j++) {
				Hashtable dato = (Hashtable) datos.get(j);

				StringBuffer sql = new StringBuffer();
				sql.append(" select p.idinstitucion, p.idtipoexpediente, p.idcampo, p.idpestanaconf, c.idcampoconf, c.nombre, c.orden, v.VALOR "); 
				sql.append(" from exp_pestanaconf p, exp_campoconf c, exp_camposvalor v ");
				sql.append(" where p.idinstitucion=c.idinstitucion ");
				sql.append(" and   p.idtipoexpediente=c.idtipoexpediente ");
				sql.append(" and   p.idcampo = c.idcampo ");
				sql.append(" and   p.idpestanaconf=c.idpestanaconf ");
				sql.append(" and   c.idinstitucion = v.idinstitucion_tipoexpediente ");
				sql.append(" and   c.idtipoexpediente = v.idtipoexpediente ");
				sql.append(" and   c.idcampo = v.idcampo ");
				sql.append(" and   c.idpestanaconf = v.idpestanaconf ");
				sql.append(" and   c.idcampoconf = v.idcampoconf ");
				sql.append(" and   v.idinstitucion =:1  ");
				sql.append(" and   v.idinstitucion_tipoexpediente=:2 "); 
				sql.append(" and   v.idtipoexpediente=:3 ");
				sql.append(" and   v.anioexpediente=:4 ");
				sql.append(" and   v.numeroexpediente=:5 ");
				sql.append(" and   p.idpestanaconf=:6 ");
				sql.append(" order by c.orden");
				
				// pestaña uno
				codigos.put(new Integer(6), "1");
				Vector aux = ejecutaConsultaBind(sql.toString(), codigos);
				// recorrido de los 5 posibles campos
				for (int i=0;i<5;i++) {
					try {
						Object o = aux.get(i);
						Hashtable reg = (Hashtable) o;
						dato.put("CAMPOCONF1"+(i+1), (String)reg.get("VALOR"));
					} catch (ArrayIndexOutOfBoundsException a) {
						dato.put("CAMPOCONF1"+(i+1), " ");
					}
				}
				
				// pestaña dos
				codigos.put(new Integer(6), "2");
				Vector aux2 = ejecutaConsultaBind(sql.toString(), codigos);
				// recorrido de los 5 posibles campos
				for (int i=0;i<5;i++) {
					try {
						Object o = aux2.get(i);
						Hashtable reg = (Hashtable) o;
						dato.put("CAMPOCONF2"+(i+1), (String)reg.get("VALOR"));
					} catch (ArrayIndexOutOfBoundsException a) {
						dato.put("CAMPOCONF2"+(i+1), " ");
					}
				}
				
				//datos.add(j, dato);
				
			}
			return datos;
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getCamposConfigurablesExpediente");
		}
		
	}	
	
		
	public Vector getAnotacionesExpediente(Vector datos, String idInstitucion, String idInstitucionTipoExp, String idTipoExp, String anio, String numero, String idPersona)throws SIGAException,ClsExceptions
	{	
		try { 
			 
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1), idInstitucion);
			codigos.put(new Integer(2), idInstitucionTipoExp);
			codigos.put(new Integer(3), idTipoExp);
			codigos.put(new Integer(4), anio);
			codigos.put(new Integer(5), numero);
			

			for (int j=0;j<datos.size();j++) {
				Hashtable dato = (Hashtable) datos.get(j);

				StringBuffer sql = new StringBuffer();
				sql.append(" select a.fechaanotacion AS FECHAANOTACION, fa.nombre AS NOMBREFASE, es.nombre AS NOMBREESTADO, a.fechainicioestado AS FECHAINICIOESTADO, a.fechafinestado AS FECHAFINESTADO");
				sql.append(" from exp_anotacion a, exp_estado es, exp_fases fa ");
				sql.append(" where a.idinstitucion_tipoexpediente = es.idinstitucion ");
				sql.append(" and   a.idtipoexpediente = es.idtipoexpediente ");
				sql.append(" and   a.idfase = es.idfase ");
				sql.append(" and   a.idestado = es.idestado ");
				sql.append(" and   a.idinstitucion_tipoexpediente = fa.idinstitucion ");
				sql.append(" and   a.idtipoexpediente = fa.idtipoexpediente ");
				sql.append(" and   a.idfase = fa.idfase ");
				sql.append(" and   a.idinstitucion = :1 ");
				sql.append(" and   a.idinstitucion_tipoexpediente= :2"); 
				sql.append(" and   a.idtipoexpediente= :3 ");
				sql.append(" and   a.anioexpediente= :4 ");
				sql.append(" and   a.numeroexpediente= :5 ");
				sql.append(" order by a.fechaanotacion ");
				
				Vector aux = ejecutaConsultaBind(sql.toString(), codigos);
				String campoAnotaciones = "";
				// recorrido de los 5 posibles campos
				for (int i=0;i<aux.size();i++) {
					Hashtable reg = (Hashtable) aux.get(i);
					campoAnotaciones += GstDate.getFormatedDateShort("", (String) reg.get("FECHAANOTACION")) + TABULADOR + (String) reg.get("NOMBREFASE") + TABULADOR + (String) reg.get("NOMBREESTADO") + TABULADOR + GstDate.getFormatedDateShort("", ((reg.get("FECHAINICIOESTADO")!=null)?(String) reg.get("FECHAINICIOESTADO"):" "))+ TABULADOR + GstDate.getFormatedDateShort("", ((reg.get("FECHAFINESTADO")!=null)?(String) reg.get("FECHAFINESTADO"):" "))+ SALTOLINEA;
				}
				dato.put("HISTORICOESTADOS", campoAnotaciones);
				
				//datos.add(j, dato);
				
			}
			return datos;
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getAnotacionesExpediente");
		}
		
	}	
	
	
	public Vector getNombresImplicadosExpediente(Vector datos, String idInstitucion, String idInstitucionTipoExp, String idTipoExp, String anio, String numero, String idPersona)throws SIGAException,ClsExceptions
	{	
		try { 
			 
			Hashtable codigos = null;			

			for (int j=0;j<datos.size();j++) {
				Hashtable dato = (Hashtable) datos.get(j);
				
				codigos = new Hashtable();
				codigos.put(new Integer(1), idInstitucion);
				codigos.put(new Integer(2), idInstitucionTipoExp);
				codigos.put(new Integer(3), idTipoExp);
				codigos.put(new Integer(4), anio);
				codigos.put(new Integer(5), numero);
				/*
				codigos.put(new Integer(6), idInstitucion);
				codigos.put(new Integer(7), idInstitucionTipoExp);
				codigos.put(new Integer(8), idTipoExp);
				codigos.put(new Integer(9), anio);
				codigos.put(new Integer(10), numero);
				*/

				StringBuffer sql = new StringBuffer();
				sql.append(" select INITCAP(pe.nombre) || ' ' || INITCAP(pe.apellidos1) || ' ' || INITCAP(pe.apellidos2) as NOMBRE "); 
				sql.append(" from exp_denunciado d, cen_persona pe ");
				sql.append(" where d.idpersona = pe.idpersona ");
				sql.append(" and   d.idinstitucion =:1 ");
				sql.append(" and   d.idinstitucion_tipoexpediente=:2 "); 
				sql.append(" and   d.idtipoexpediente=:3 ");
				sql.append(" and   d.anioexpediente=:4 ");
				sql.append(" and   d.numeroexpediente=:5 ");
				/*
				sql.append(" union ");
				sql.append(" select INITCAP(pe.nombre) || ' ' || INITCAP(pe.apellidos1) || ' ' || INITCAP(pe.apellidos2) as NOMBRE "); 
				sql.append(" from exp_expediente ex, cen_persona pe ");
				sql.append(" where ex.idpersona = pe.idpersona ");
				sql.append(" and   ex.idinstitucion =:6 ");
				sql.append(" and   ex.idinstitucion_tipoexpediente=:7 "); 
				sql.append(" and   ex.idtipoexpediente=:8 ");
				sql.append(" and   ex.anioexpediente=:9 ");
				sql.append(" and   ex.numeroexpediente=:10 ");
				*/
				
				Vector aux = ejecutaConsultaBind(sql.toString(), codigos);
				String campoDenunciados = "";
				// recorrido de los 5 posibles campos
				for (int i=0;i<aux.size();i++) {
					Hashtable reg = (Hashtable) aux.get(i);
					campoDenunciados += (String) reg.get("NOMBRE") + ", ";
				}
				if (campoDenunciados.length()>0) campoDenunciados = campoDenunciados.substring(0,campoDenunciados.length()-2);
				dato.put("DENUNCIADOS", campoDenunciados);
				dato.put("DENUNCIADOS_MAYUS", campoDenunciados.toUpperCase());

				codigos = new Hashtable();
				codigos.put(new Integer(1), idInstitucion);
				codigos.put(new Integer(2), idInstitucionTipoExp);
				codigos.put(new Integer(3), idTipoExp);
				codigos.put(new Integer(4), anio);
				codigos.put(new Integer(5), numero);
				
				sql = new StringBuffer();
				sql.append(" select INITCAP(pe.nombre) || ' ' || INITCAP(pe.apellidos1) || ' ' || INITCAP(pe.apellidos2) as NOMBRE "); 
				sql.append(" from exp_denunciante d, cen_persona pe ");
				sql.append(" where d.idpersona = pe.idpersona ");
				sql.append(" and   d.idinstitucion =:1 ");
				sql.append(" and   d.idinstitucion_tipoexpediente=:2 "); 
				sql.append(" and   d.idtipoexpediente=:3 ");
				sql.append(" and   d.anioexpediente=:4 ");
				sql.append(" and   d.numeroexpediente=:5 ");
				
				
				Vector aux2 = ejecutaConsultaBind(sql.toString(), codigos);
				String campoDenunciantes = "";
				// recorrido de los 5 posibles campos
				for (int i=0;i<aux2.size();i++) {
					Hashtable reg = (Hashtable) aux2.get(i);
					campoDenunciantes += (String) reg.get("NOMBRE") + ", ";
				}
				if (campoDenunciantes.length()>0) campoDenunciantes = campoDenunciantes.substring(0,campoDenunciantes.length()-2);
				dato.put("DENUNCIANTES", campoDenunciantes);
				dato.put("DENUNCIANTES_MAYUS", campoDenunciantes.toUpperCase());

				//datos.add(j, dato);
				
			}
			return datos;
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getNombresImplicadosExpediente");
		}
		
	}	
	
	public Vector getNombresPartesExpediente(Vector datos, String idInstitucion, String idInstitucionTipoExp, String idTipoExp, String anio, String numero, String idPersona, String lenguaje)throws SIGAException,ClsExceptions
	{	
		try { 
			 
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1), lenguaje);
			codigos.put(new Integer(2), idInstitucion);
			codigos.put(new Integer(3), idInstitucionTipoExp);
			codigos.put(new Integer(4), idTipoExp);
			codigos.put(new Integer(5), anio);
			codigos.put(new Integer(6), numero);

			for (int j=0;j<datos.size();j++) {
				Hashtable dato = (Hashtable) datos.get(j);

				
				StringBuffer sql = new StringBuffer();
				sql.append(" select INITCAP(pe.nombre) || ' ' || INITCAP(pe.apellidos1) || ' ' || INITCAP(pe.apellidos2) as NOMBRE, f_siga_getrecurso(r.nombre,:1) as NOMBREROL "); 
				sql.append(" from exp_parte d, cen_persona pe, exp_rolparte r ");
				sql.append(" where d.idpersona = pe.idpersona ");
				sql.append(" and   d.idrol = r.idrol ");
				sql.append(" and   d.idinstitucion_tipoexpediente = r.idinstitucion ");
				sql.append(" and   d.idtipoexpediente = r.idtipoexpediente ");
				sql.append(" and   d.idinstitucion =:2 ");
				sql.append(" and   d.idinstitucion_tipoexpediente=:3 "); 
				sql.append(" and   d.idtipoexpediente=:4 ");
				sql.append(" and   d.anioexpediente=:5 ");
				sql.append(" and   d.numeroexpediente=:6 ");
				
				
				Vector aux2 = ejecutaConsultaBind(sql.toString(), codigos);
				String campoPartes = "";
				// recorrido de los 5 posibles campos
				for (int i=0;i<aux2.size();i++) {
					Hashtable reg = (Hashtable) aux2.get(i);
					campoPartes += (String) reg.get("NOMBRE")+ " ("+ (String) reg.get("NOMBREROL") + "), ";
				}
				if (campoPartes.length()>0) campoPartes = campoPartes.substring(0,campoPartes.length()-2);
				dato.put("PARTES", campoPartes);
				dato.put("PARTES_MAYUS", campoPartes.toUpperCase());

				//datos.add(j, dato);
				
			}
			return datos;
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getNombresPartesExpediente");
		}
		
	}	
	
	public Vector getImplicadosDireccionesExpediente(Vector datos,
			String idInstitucion,
			String idInstitucionTipoExp,
			String idTipoExp,
			String anio,
			String numero,
			String idPersona,
			String lenguaje,
			String destinatario,boolean isDesdoblar) throws SIGAException, ClsExceptions
			{
		Vector denunciadosVector = new Vector();
		Vector denunciantesVector = new Vector();
		Vector partesVector = new Vector();
		Hashtable datoActual, datoNuevo;

		// Variables para la ejecucion de las consultas
		StringBuffer sql; // sentencia
		Hashtable codigos; // codigos para select BIND
		Vector resultadoConsulta; // resultado de la sentencia
		Hashtable reg; // cada uno de los registros del resultado de la sentencia

		try {
			// Para cada registro, se consultan varios datos
			for (int j = 0; j < datos.size(); j++) {
				// Obteniendo el registro actual
				datoActual = (Hashtable) datos.get(j);

				// ///// NOMBRE Y DIRECCION DEl DENUNCIADO PRINCIPAL
				codigos = new Hashtable();
				codigos.put(new Integer(1), lenguaje);
				codigos.put(new Integer(2), idInstitucion);
				codigos.put(new Integer(3), idInstitucionTipoExp);
				codigos.put(new Integer(4), idTipoExp);
				codigos.put(new Integer(5), anio);
				codigos.put(new Integer(6), numero);

				sql = new StringBuffer();
				sql.append(" select pe.nifcif as NIFCIF, ");
				sql.append("        f_siga_getrecurso(tra.descripcion,1) TRATAMIENTO, ");
				sql.append("        dir.idpersona as IDPERSONA_DIR, ");
				sql.append("        dir.iddireccion as IDDIRECCION_DIR, ");
				sql.append("        dir.domicilio, ");
				sql.append("        dir.codigopostal, ");
				sql.append("        dir.telefono1, ");
				sql.append("        dir.telefono2, ");
				sql.append("        dir.movil, ");
				sql.append("        dir.fax1, ");
				sql.append("        dir.fax2, ");
				sql.append("        dir.correoelectronico, ");
				sql.append("        dir.paginaweb, ");
				sql.append("        dir.poblacionextranjera, ");
				sql.append("        (select po.nombre from cen_poblaciones po where po.idpoblacion = dir.idpoblacion) as NOMBRE_POBLACION, ");
				sql.append("        (select pr.nombre from cen_provincias pr where pr.idprovincia = dir.idprovincia) as NOMBRE_PROVINCIA, ");
				sql.append("        (select f_siga_getrecurso(pa.nombre,:1) from cen_pais pa where pa.idpais = dir.idpais) as NOMBRE_PAIS, ");
				sql.append("        INITCAP(pe.nombre) as NOMBRE, ");
				sql.append("        INITCAP(pe.apellidos1) as APELLIDO1, ");
				sql.append("        INITCAP(pe.apellidos2) as APELLIDO2, ");
				sql.append("        pe.sexo AS SEXO, ");
				sql.append("        DECODE(pe.SEXO, 'H','o','a') AS O_A, ");
				sql.append("        DECODE(pe.SEXO, 'H','el','la') AS EL_LA, ");
				sql.append("        den.idpersona AS IDINTERESADO ");
				sql.append(" from  exp_denunciado den, cen_persona pe, cen_direcciones dir, cen_cliente cli, cen_tratamiento tra");
				sql.append(" where den.idpersona = pe.idpersona ");
				sql.append(" and   den.idpersona = dir.idpersona(+) ");
				sql.append(" and   den.idinstitucion = dir.idinstitucion(+) ");
				sql.append(" and   den.iddireccion = dir.iddireccion(+) ");
				sql.append(" and   DEN.idinstitucion =:2 ");
				sql.append(" and   DEN.idinstitucion_tipoexpediente=:3 ");
				sql.append(" and   DEN.idtipoexpediente=:4 ");
				sql.append(" and   DEN.anioexpediente=:5 ");
				sql.append(" and   DEN.numeroexpediente=:6 ");
//				sql.append(" and   DEN.IDDENUNCIADO = "+ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL+"  ");
				sql.append(" and   pe.idpersona=cli.idpersona ");
				sql.append(" and   cli.idtratamiento= tra.idtratamiento ");
				sql.append(" and   cli.idinstitucion=DEN.idinstitucion ");

				resultadoConsulta = ejecutaConsultaBind(sql.toString(), codigos);
				String idPersonaDenunciadoPrincipal = null;
				for (int i = 0; i < resultadoConsulta.size(); i++) {
					reg = (Hashtable) resultadoConsulta.get(i);
					if (((String) reg.get("IDINTERESADO")).trim().equals("")) continue; // esto hay que hacerlo porque el metodo ejecutaConsultaBind devuelve un Hash con valores vacios si la consulta no obtiene resultados
					datoNuevo = new Hashtable();
					datoNuevo.putAll(datoActual);

					datoNuevo.put("NOMBRE_DEST", (String) reg.get("NOMBRE"));
					datoNuevo.put("NOMBRE_DEST_MAYUS", ((String) reg.get("NOMBRE")).toUpperCase());
					datoNuevo.put("APELLIDO1_DEST", (String) reg.get("APELLIDO1"));
					datoNuevo.put("APELLIDO1_DEST_MAYUS", ((String) reg.get("APELLIDO1")).toUpperCase());
					datoNuevo.put("APELLIDO2_DEST", (String) reg.get("APELLIDO2"));
					datoNuevo.put("APELLIDO2_DEST_MAYUS", ((String) reg.get("APELLIDO2")).toUpperCase());
					datoNuevo.put("SEXO_DEST", (String) reg.get("SEXO"));
					datoNuevo.put("O_A_DEST", (String) reg.get("O_A"));
					datoNuevo.put("EL_LA_DEST", (String) reg.get("EL_LA"));
					// .. resto de campos obtenidos.
					datoNuevo.put("TRATAMIENTO_DEST", (String) reg.get("TRATAMIENTO"));
					datoNuevo.put("NIFCIF_DEST", (String) reg.get("NIFCIF"));
					
					datoNuevo.put("IDPERSONA_DEST",reg.get("IDINTERESADO")!=null?(String)reg.get("IDINTERESADO"):"");
					
					datoNuevo.put("IDDIRECCION_DEST",reg.get("IDDIRECCION_DIR")!=null?(String)reg.get("IDDIRECCION_DIR"):"") ;
					datoNuevo.put("DOMICILIO_DEST",reg.get("DOMICILIO")!=null?(String)reg.get("DOMICILIO"):"");
					datoNuevo.put("CODIGOPOSTAL_DEST",reg.get("CODIGOPOSTAL")!=null?(String)reg.get("CODIGOPOSTAL"):"") ;
					datoNuevo.put("TELEFONO1_DEST",reg.get("TELEFONO1")!=null?(String)reg.get("TELEFONO1"):"") ;
					datoNuevo.put("TELEFONO2_DEST",reg.get("TELEFONO2")!=null?(String)reg.get("TELEFONO2"):"") ;
					datoNuevo.put("MOVIL_DEST",reg.get("MOVIL")!=null?(String)reg.get("MOVIL"):"") ;
					datoNuevo.put("FAX1_DEST",reg.get("FAX1")!=null?(String)reg.get("FAX1"):"") ;
					datoNuevo.put("FAX2_DEST",reg.get("FAX2")!=null?(String)reg.get("FAX2"):"") ;
					datoNuevo.put("CORREOELECTRONICO_DEST",reg.get("CORREOELECTRONICO")!=null?(String)reg.get("CORREOELECTRONICO"):"");
					datoNuevo.put("PAGINAWEB_DEST",reg.get("PAGINAWEB")!=null?(String)reg.get("PAGINAWEB"):"") ;
					datoNuevo.put("POBLACIONEXTRANJERA_DEST",reg.get("POBLACIONEXTRANJERA")!=null?(String)reg.get("POBLACIONEXTRANJERA"):"") ;
					datoNuevo.put("NOMBRE_POBLACION_DEST",reg.get("NOMBRE_POBLACION")!=null?(String)reg.get("NOMBRE_POBLACION"):"") ;
					datoNuevo.put("NOMBRE_PROVINCIA_DEST",reg.get("NOMBRE_PROVINCIA")!=null?(String)reg.get("NOMBRE_PROVINCIA"):"") ;
					datoNuevo.put("NOMBRE_PAIS_DEST",reg.get("NOMBRE_PAIS")!=null?(String)reg.get("NOMBRE_PAIS"):"") ;
					
					datoNuevo.put("IDINTERESADO", (String) reg.get("IDINTERESADO"));
					
//					idPersonaDenunciadoPrincipal = (String) reg.get("IDDENUNCIADO_PRINCIPAL");
//					if(idPersonaDenunciado==null || idPersonaDenunciado.equals(idPersonaDenunciadoPrincipal))
					
					denunciadosVector.add(datoNuevo);
					
				}


				

					// ////// NOMBRES Y DIRECCIONES DE DENUNCIANTES
					codigos = new Hashtable();
					codigos.put(new Integer(1), lenguaje);
					codigos.put(new Integer(2), idInstitucion);
					codigos.put(new Integer(3), idInstitucionTipoExp);
					codigos.put(new Integer(4), idTipoExp);
					codigos.put(new Integer(5), anio);
					codigos.put(new Integer(6), numero);

					sql = new StringBuffer();
					// NO OLVIDAR SACAR LOS NOMBRES DE POBLACION, PROVINCIA, PAIS, ETC...
					sql.append(" select pe.nifcif as NIFCIF, ");
					sql.append("        f_siga_getrecurso(tra.descripcion,1) TRATAMIENTO, ");
					sql.append("        dir.idpersona as IDPERSONA_DIR, ");
					sql.append("        dir.iddireccion as IDDIRECCION_DIR, ");
					sql.append("        dir.domicilio, ");
					sql.append("        dir.codigopostal, ");
					sql.append("        dir.telefono1, ");
					sql.append("        dir.telefono2, ");
					sql.append("        dir.movil, ");
					sql.append("        dir.fax1, ");
					sql.append("        dir.fax2, ");
					sql.append("        dir.correoelectronico, ");
					sql.append("        dir.paginaweb, ");
					sql.append("        dir.poblacionextranjera, ");
					sql.append("        (select po.nombre from cen_poblaciones po where po.idpoblacion = dir.idpoblacion) as NOMBRE_POBLACION, ");
					sql.append("        (select pr.nombre from cen_provincias pr where pr.idprovincia = dir.idprovincia) as NOMBRE_PROVINCIA, ");
					sql.append("        (select f_siga_getrecurso(pa.nombre,:1) from cen_pais pa where pa.idpais = dir.idpais) as NOMBRE_PAIS, ");
					sql.append("        INITCAP(pe.nombre) as NOMBRE, ");
					sql.append("        INITCAP(pe.apellidos1) as APELLIDO1, ");
					sql.append("        INITCAP(pe.apellidos2) as APELLIDO2, ");
					sql.append("        pe.sexo AS SEXO, ");
					sql.append("        DECODE(pe.SEXO, 'H','o','a') AS O_A, ");
					sql.append("        DECODE(pe.SEXO, 'H','el','la') AS EL_LA ");
					sql.append("        ,d.idpersona as IDINTERESADO ");
					sql.append(" from exp_denunciante d, cen_persona pe, cen_direcciones dir, cen_cliente cli, cen_tratamiento tra ");
					sql.append(" where d.idpersona = pe.idpersona ");
					sql.append(" and	d.idpersona = dir.idpersona(+) ");
					sql.append(" and   d.iddireccion = dir.iddireccion(+) ");
					sql.append(" and   d.idinstitucion = dir.idinstitucion(+) ");
					sql.append(" and   d.idinstitucion =:2 ");
					sql.append(" and   d.idinstitucion_tipoexpediente=:3 ");
					sql.append(" and   d.idtipoexpediente=:4 ");
					sql.append(" and   d.anioexpediente=:5 ");
					sql.append(" and   d.numeroexpediente=:6 ");
					sql.append(" and   pe.idpersona=cli.idpersona ");
					sql.append(" and   cli.idtratamiento= tra.idtratamiento ");
					sql.append(" and   cli.idinstitucion=d.idinstitucion ");

					resultadoConsulta = ejecutaConsultaBind(sql.toString(), codigos);
					for (int i = 0; i < resultadoConsulta.size(); i++) {
						reg = (Hashtable) resultadoConsulta.get(i);
						if (((String) reg.get("IDINTERESADO")).trim().equals("")) continue; // esto hay que hacerlo porque el metodo ejecutaConsultaBind devuelve un Hash con valores vacios si la consulta no obtiene resultados
						datoNuevo = new Hashtable();
						datoNuevo.putAll(datoActual);

						datoNuevo.put("NOMBRE_DEST", (String) reg.get("NOMBRE"));
						datoNuevo.put("NOMBRE_DEST_MAYUS", ((String) reg.get("NOMBRE")).toUpperCase());
						datoNuevo.put("APELLIDO1_DEST", (String) reg.get("APELLIDO1"));
						datoNuevo.put("APELLIDO1_DEST_MAYUS", ((String) reg.get("APELLIDO1")).toUpperCase());
						datoNuevo.put("APELLIDO2_DEST", (String) reg.get("APELLIDO2"));
						datoNuevo.put("APELLIDO2_DEST_MAYUS", ((String) reg.get("APELLIDO2")).toUpperCase());
						datoNuevo.put("TRATAMIENTO_DEST", (String) reg.get("TRATAMIENTO"));
						datoNuevo.put("NIFCIF_DEST", (String) reg.get("NIFCIF"));
						datoNuevo.put("SEXO_DEST", (String) reg.get("SEXO"));
						datoNuevo.put("O_A_DEST", (String) reg.get("O_A"));
						datoNuevo.put("EL_LA_DEST", (String) reg.get("EL_LA"));
						// .. resto de campos obtenidos.
						
						
						
						datoNuevo.put("IDPERSONA_DEST",reg.get("IDINTERESADO")!=null?(String)reg.get("IDINTERESADO"):"");
						datoNuevo.put("IDDIRECCION_DEST",reg.get("IDDIRECCION_DIR")!=null?(String)reg.get("IDDIRECCION_DIR"):"") ;
						datoNuevo.put("DOMICILIO_DEST",reg.get("DOMICILIO")!=null?(String)reg.get("DOMICILIO"):"");
						datoNuevo.put("CODIGOPOSTAL_DEST",reg.get("CODIGOPOSTAL")!=null?(String)reg.get("CODIGOPOSTAL"):"") ;
						datoNuevo.put("TELEFONO1_DEST",reg.get("TELEFONO1")!=null?(String)reg.get("TELEFONO1"):"") ;
						datoNuevo.put("TELEFONO2_DEST",reg.get("TELEFONO2")!=null?(String)reg.get("TELEFONO2"):"") ;
						datoNuevo.put("MOVIL_DEST",reg.get("MOVIL")!=null?(String)reg.get("MOVIL"):"") ;
						datoNuevo.put("FAX1_DEST",reg.get("FAX1")!=null?(String)reg.get("FAX1"):"") ;
						datoNuevo.put("FAX2_DEST",reg.get("FAX2")!=null?(String)reg.get("FAX2"):"") ;
						datoNuevo.put("CORREOELECTRONICO_DEST",reg.get("CORREOELECTRONICO")!=null?(String)reg.get("CORREOELECTRONICO"):"");
						datoNuevo.put("PAGINAWEB_DEST",reg.get("PAGINAWEB")!=null?(String)reg.get("PAGINAWEB"):"") ;
						datoNuevo.put("POBLACIONEXTRANJERA_DEST",reg.get("POBLACIONEXTRANJERA")!=null?(String)reg.get("POBLACIONEXTRANJERA"):"") ;
						datoNuevo.put("NOMBRE_POBLACION_DEST",reg.get("NOMBRE_POBLACION")!=null?(String)reg.get("NOMBRE_POBLACION"):"") ;
						datoNuevo.put("NOMBRE_PROVINCIA_DEST",reg.get("NOMBRE_PROVINCIA")!=null?(String)reg.get("NOMBRE_PROVINCIA"):"") ;
						datoNuevo.put("NOMBRE_PAIS_DEST",reg.get("NOMBRE_PAIS")!=null?(String)reg.get("NOMBRE_PAIS"):"") ;
						
						datoNuevo.put("IDINTERESADO", (String) reg.get("IDINTERESADO"));
						denunciantesVector.add(datoNuevo);
							
					}

					// ////// NOMBRES Y DIRECCIONES DE PARTES
					codigos = new Hashtable();
					codigos.put(new Integer(1), lenguaje);
					codigos.put(new Integer(2), lenguaje);
					codigos.put(new Integer(3), idInstitucion);
					codigos.put(new Integer(4), idInstitucionTipoExp);
					codigos.put(new Integer(5), idTipoExp);
					codigos.put(new Integer(6), anio);
					codigos.put(new Integer(7), numero);

					sql = new StringBuffer();
					// NO OLVIDAR SACAR LOS NOMBRES DE POBLACION, PROVINCIA, PAIS, ETC...
					sql.append(" select pe.nifcif as NIFCIF, ");
					sql.append("        f_siga_getrecurso(tra.descripcion,1) TRATAMIENTO, ");
					sql.append("        dir.idpersona as IDPERSONA_DIR, ");
					
					sql.append("        dir.iddireccion as IDDIRECCION_DIR, ");
					sql.append("        dir.domicilio, ");
					sql.append("        dir.codigopostal, ");
					sql.append("        dir.telefono1, ");
					sql.append("        dir.telefono2, ");
					sql.append("        dir.movil, ");
					sql.append("        dir.fax1, ");
					sql.append("        dir.fax2, ");
					sql.append("        dir.correoelectronico, ");
					sql.append("        dir.paginaweb, ");
					sql.append("        dir.poblacionextranjera, ");
					sql.append("        (select po.nombre from cen_poblaciones po where po.idpoblacion = dir.idpoblacion) as NOMBRE_POBLACION, ");
					sql.append("        (select pr.nombre from cen_provincias pr where pr.idprovincia = dir.idprovincia) as NOMBRE_PROVINCIA, ");
					sql.append("        (select f_siga_getrecurso(pa.nombre,:1) from cen_pais pa where pa.idpais = dir.idpais) as NOMBRE_PAIS, ");
					sql.append("        INITCAP(pe.nombre) as NOMBRE, ");
					sql.append("        INITCAP(pe.apellidos1) as APELLIDO1, ");
					sql.append("        INITCAP(pe.apellidos2) as APELLIDO2, ");
					sql.append("        f_siga_getrecurso(r.nombre, :2) as NOMBREROL, ");
					sql.append("        pe.sexo AS SEXO, ");
					sql.append("        DECODE(pe.SEXO, 'H','o','a') AS O_A, ");
					sql.append("        DECODE(pe.SEXO, 'H','el','la') AS EL_LA ");
					sql.append("        ,d.idpersona as IDINTERESADO ");
					sql.append(" from exp_parte d, cen_persona pe, exp_rolparte r , cen_direcciones dir, cen_cliente cli, cen_tratamiento tra ");
					sql.append(" where d.idpersona = pe.idpersona ");
					sql.append(" and   d.idrol = r.idrol  ");
					sql.append(" and   d.idinstitucion_tipoexpediente = r.idinstitucion ");
					sql.append(" and   d.idtipoexpediente = r.idtipoexpediente ");
					sql.append(" and   d.idpersona = dir.idpersona(+) ");
					sql.append(" and   d.iddireccion = dir.iddireccion(+) ");
					sql.append(" and   d.idinstitucion = dir.idinstitucion(+) ");
					sql.append(" and   d.idinstitucion =:3 ");
					sql.append(" and   d.idinstitucion_tipoexpediente=:4 ");
					sql.append(" and   d.idtipoexpediente=:5 ");
					sql.append(" and   d.anioexpediente=:6 ");
					sql.append(" and   d.numeroexpediente=:7 ");
					sql.append(" and   pe.idpersona=cli.idpersona ");
					sql.append(" and   cli.idtratamiento= tra.idtratamiento ");
					sql.append(" and   cli.idinstitucion=d.idinstitucion ");

					resultadoConsulta = ejecutaConsultaBind(sql.toString(), codigos);
					for (int i = 0; i < resultadoConsulta.size(); i++) {
						reg = (Hashtable) resultadoConsulta.get(i);
						if (((String) reg.get("IDINTERESADO")).trim().equals("")) continue; // esto hay que hacerlo porque el metodo ejecutaConsultaBind devuelve un Hash con valores vacios si la consulta no obtiene resultados
						datoNuevo = new Hashtable();
						datoNuevo.putAll(datoActual);

						datoNuevo.put("NOMBRE_DEST", (String) reg.get("NOMBRE"));
						datoNuevo.put("NOMBRE_DEST_MAYUS", ((String) reg.get("NOMBRE")).toUpperCase());
						datoNuevo.put("APELLIDO1_DEST", (String) reg.get("APELLIDO1"));
						datoNuevo.put("APELLIDO1_DEST_MAYUS", ((String) reg.get("APELLIDO1")).toUpperCase());
						datoNuevo.put("APELLIDO2_DEST", (String) reg.get("APELLIDO2"));
						datoNuevo.put("APELLIDO2_DEST_MAYUS", ((String) reg.get("APELLIDO2")).toUpperCase());
						datoNuevo.put("TRATAMIENTO_DEST", (String) reg.get("TRATAMIENTO"));
						datoNuevo.put("NIFCIF_DEST", (String) reg.get("NIFCIF"));
						datoNuevo.put("SEXO_DEST", (String) reg.get("SEXO"));
						datoNuevo.put("O_A_DEST", (String) reg.get("O_A"));
						datoNuevo.put("EL_LA_DEST", (String) reg.get("EL_LA"));
						datoNuevo.put("DESC_ROLPARTE", (String) reg.get("NOMBREROL"));
						// .. resto de campos obtenidos.
						datoNuevo.put("IDPERSONA_DEST",reg.get("IDINTERESADO")!=null?(String)reg.get("IDINTERESADO"):"");
						datoNuevo.put("IDDIRECCION_DEST",reg.get("IDDIRECCION_DIR")!=null?(String)reg.get("IDDIRECCION_DIR"):"") ;
						datoNuevo.put("DOMICILIO_DEST",reg.get("DOMICILIO")!=null?(String)reg.get("DOMICILIO"):"");
						datoNuevo.put("CODIGOPOSTAL_DEST",reg.get("CODIGOPOSTAL")!=null?(String)reg.get("CODIGOPOSTAL"):"") ;
						datoNuevo.put("TELEFONO1_DEST",reg.get("TELEFONO1")!=null?(String)reg.get("TELEFONO1"):"") ;
						datoNuevo.put("TELEFONO2_DEST",reg.get("TELEFONO2")!=null?(String)reg.get("TELEFONO2"):"") ;
						datoNuevo.put("MOVIL_DEST",reg.get("MOVIL")!=null?(String)reg.get("MOVIL"):"") ;
						datoNuevo.put("FAX1_DEST",reg.get("FAX1")!=null?(String)reg.get("FAX1"):"") ;
						datoNuevo.put("FAX2_DEST",reg.get("FAX2")!=null?(String)reg.get("FAX2"):"") ;
						datoNuevo.put("CORREOELECTRONICO_DEST",reg.get("CORREOELECTRONICO")!=null?(String)reg.get("CORREOELECTRONICO"):"");
						datoNuevo.put("PAGINAWEB_DEST",reg.get("PAGINAWEB")!=null?(String)reg.get("PAGINAWEB"):"") ;
						datoNuevo.put("POBLACIONEXTRANJERA_DEST",reg.get("POBLACIONEXTRANJERA")!=null?(String)reg.get("POBLACIONEXTRANJERA"):"") ;
						datoNuevo.put("NOMBRE_POBLACION_DEST",reg.get("NOMBRE_POBLACION")!=null?(String)reg.get("NOMBRE_POBLACION"):"") ;
						datoNuevo.put("NOMBRE_PROVINCIA_DEST",reg.get("NOMBRE_PROVINCIA")!=null?(String)reg.get("NOMBRE_PROVINCIA"):"") ;
						datoNuevo.put("NOMBRE_PAIS_DEST",reg.get("NOMBRE_PAIS")!=null?(String)reg.get("NOMBRE_PAIS"):"") ;
						
						datoNuevo.put("IDINTERESADO", (String) reg.get("IDINTERESADO"));
						partesVector.add(datoNuevo);
						
					}
				

			}
			
			
			// en caso de no haber datos por lo menos devuelvo lo que he recibido.
			
			
				

			if(destinatario!=null && destinatario.equals(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)){
				// A Lios denunciados le metemos regiones de partes y denunciantes
				for (int i = 0; i < denunciadosVector.size(); i++) {
					Hashtable datosHashtable = (Hashtable)denunciadosVector.get(i);
					if(denunciantesVector!=null && denunciantesVector.size()>0)
						datosHashtable.put("regiondenunciantes", denunciantesVector);
					else
						datosHashtable.put("regiondenunciantes", new Vector());
					
					if(partesVector!=null && partesVector.size()>0)
						datosHashtable.put("regionpartes", partesVector);
					else
						datosHashtable.put("regionpartes", new Vector());
					
					for (int j = 0; j < partesVector.size(); j++) {
						Hashtable datosPartes = (Hashtable)partesVector.get(j);
						String descripcionRolParte = (String) datosPartes.get("DESC_ROLPARTE");
						Iterator iteParte = datosPartes.keySet().iterator();
						while (iteParte.hasNext()) {
							String objParte = (String) iteParte.next();
							datosHashtable.put(objParte+"_"+descripcionRolParte.toUpperCase(), datosPartes.get(objParte));
							
						}
						
					}
					
					
					
				}
				//VAmo a meter las parte concatenando el nombre de su rol
				
				
				
				
				
				Vector denunciadosVectorClon =  (Vector) denunciadosVector.clone();
				for (int i = 0; i < denunciadosVector.size(); i++) {
					Hashtable datosHashtable = (Hashtable)denunciadosVector.get(i);
					datosHashtable.put("regiondenunciados", denunciadosVectorClon);
					
				}
				if(idPersona!=null){
					Iterator iterador = denunciadosVector.iterator();
					while (iterador.hasNext()) {
						Hashtable datosHashtable = (Hashtable) iterador.next();
						String idPersonaAux = (String) datosHashtable.get("IDINTERESADO");
						if(!idPersona.equals(idPersonaAux))
							iterador.remove();
						if(datosHashtable.get("IDPERSONA_DEST")==null){
							ClsLogging.writeFileLog("Envio informes generico de expedientes. En destinatario(denunciado) del envio no tiene configurada direccion de envio:"+datosHashtable.get("NOMBRE")+" "+ datosHashtable.get("APELLIDO1"),10);
						}
						
					}
				}
				
				
				
				
				
				if(denunciadosVector.size()>0)
					return denunciadosVector;
				else
					return datos;
				
			}
			else if(destinatario!=null && destinatario.equals(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
				//Para los denunciantes le metemos regiones de denunciados 
				for (int i = 0; i < denunciantesVector.size(); i++) {
					Hashtable datosHashtable = (Hashtable)denunciantesVector.get(i);
					if(denunciadosVector!=null && denunciadosVector.size()>0)
						datosHashtable.put("regiondenunciados", denunciadosVector);
					else//Esto es imposible ya que es obligatorio el denunciado con una direccion pero ...
						datosHashtable.put("regiondenunciados", new Vector());
					
					if(partesVector!=null && partesVector.size()>0)
						datosHashtable.put("regionpartes", partesVector);
					else
						datosHashtable.put("regionpartes", new Vector());
					
					for (int j = 0; j < partesVector.size(); j++) {
						Hashtable datosPartes = (Hashtable)partesVector.get(j);
						String descripcionRolParte = (String) datosPartes.get("DESC_ROLPARTE");
						Iterator iteParte = datosPartes.keySet().iterator();
						while (iteParte.hasNext()) {
							String objParte = (String) iteParte.next();
							datosHashtable.put(objParte+"_"+descripcionRolParte.toUpperCase(), datosPartes.get(objParte));
							
						}
						
					}
					
				}
				
				
				
				Vector denunciantesVectorClon =  (Vector) denunciantesVector.clone();
				for (int i = 0; i < denunciantesVector.size(); i++) {
					Hashtable datosHashtable = (Hashtable)denunciantesVector.get(i);
					datosHashtable.put("regiondenunciantes", denunciantesVectorClon);
					
				}
				if(idPersona!=null){
					Iterator iterador = denunciantesVector.iterator();
					while (iterador.hasNext()) {
						Hashtable datosHashtable = (Hashtable) iterador.next();
						String idPersonaAux = (String) datosHashtable.get("IDINTERESADO");
						if(!idPersona.equals(idPersonaAux))
							iterador.remove();
						if(datosHashtable.get("IDPERSONA_DEST")==null){
							ClsLogging.writeFileLog("Envio informes generico de expedientes. En destinatario(parte) del envio no tiene configurada direccion de envio:"+datosHashtable.get("NOMBRE")+" "+ datosHashtable.get("APELLIDO1"),10);
						}
						
					}
				}
				
				if(denunciantesVector.size()>0)
					return denunciantesVector;
				else
					return datos;
				
			}
			else if(destinatario!=null && destinatario.equals(AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)){
					//Para las partes le metemos regiones de denunciados y de denunciantes
					for (int i = 0; i < partesVector.size(); i++) {
						Hashtable datosHashtable = (Hashtable)partesVector.get(i);
						if(denunciadosVector!=null && denunciadosVector.size()>0)
							datosHashtable.put("regiondenunciados", denunciadosVector);
						//Esto es imposible ya que es obligatorio el denunciado con una direccion pero ...
						else
							datosHashtable.put("regiondenunciados", new Vector());
						if(denunciantesVector!=null && denunciantesVector.size()>0)
							datosHashtable.put("regiondenunciantes", denunciantesVector);
						else
							datosHashtable.put("regiondenunciantes", new Vector());
						
						
						
						Hashtable auxParteHashtable = new Hashtable();
						for (int j = 0; j < partesVector.size(); j++) {
							Hashtable datosPartes = (Hashtable)partesVector.get(j);
							String descripcionRolParte = (String) datosPartes.get("DESC_ROLPARTE");
							Iterator iteParte = datosPartes.keySet().iterator();
							while (iteParte.hasNext()) {
								String objParte = (String) iteParte.next();
								auxParteHashtable.put(objParte+"_"+descripcionRolParte.toUpperCase(), datosPartes.get(objParte));
								
							}
							
						}
						datosHashtable.putAll(auxParteHashtable);
						
						
						
					}
					
					if(idPersona!=null){
						Iterator iterador = partesVector.iterator();
						while (iterador.hasNext()) {
							Hashtable datosHashtable = (Hashtable) iterador.next();
							String idPersonaAux = (String) datosHashtable.get("IDINTERESADO");
							if(!idPersona.equals(idPersonaAux))
								iterador.remove();
							if(datosHashtable.get("IDPERSONA_DEST")==null){
								ClsLogging.writeFileLog("Envio informes generico de expedientes. En destinatario(denunciante) del envio no tiene configurada direccion de envio:"+datosHashtable.get("NOMBRE")+" "+ datosHashtable.get("APELLIDO1"),10);
							}
							
						}
					}
					
					if(partesVector.size()>0)
						return partesVector;
					else
						return datos;
					
				}
				 else{
					return datos;
				}
				
				
				
				
			
				
				
			

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar getImplicadosDireccionesExpediente");
		}

			} //getImplicadosDireccionesExpediente()
	

}
