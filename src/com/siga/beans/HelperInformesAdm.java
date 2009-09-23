package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.general.SIGAException;

public class HelperInformesAdm  {
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
			" scs_turno.abreviatura AS ABREV_TURNO ,scs_turno.LETRADOACTUACIONES AS LETRADOACTUACIONES"+
			" from scs_turno where idinstitucion = :1 and idturno = :2";
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTurnoSalida.");
		}
	}
	public Vector getTipoDesignaColegio (String idInstitucion, String idTipoDesigna) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idTipoDesigna);
			String sql = "SELECT f_siga_getrecurso(SCS_TIPODESIGNACOLEGIO.DESCRIPCION,1) AS DESC_TIPODESIGNA "
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
			   " JUZ2.IDJUZGADO AS IDJUZGADO" 
					+ prolongarCampo+
			   ","+
		       " JUZ2.NOMBRE AS JUZGADO"
		       + prolongarCampo+
		       ","+
		       " JUZ2.DOMICILIO AS DIR_JUZGADO"
		       + prolongarCampo+
		       ","+
		       " JUZ2.CODIGOPOSTAL  AS CP_JUZGADO"
		       + prolongarCampo+
		       ","+
		       " JUZ2.IDPROVINCIA AS ID_PROVINCIA_JUZGADO"
		       + prolongarCampo+
		       ","+
		       " JUZ2.IDPOBLACION AS ID_POBLACION_JUZGADO"
		       + prolongarCampo+
		       ""+
       
		       " FROM SCS_JUZGADO JUZ2"+
		       " WHERE JUZ2.FECHABAJA IS NULL"+
      		   " and JUZ2.IDINSTITUCION= :1 AND  JUZ2.IDJUZGADO = :2 ";
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getJuzgadoSalida");
		}
	}
	public Vector getTipoResolucionAutomatico (String idTipoResolucion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idTipoResolucion);
			String sql = "SELECT f_siga_getrecurso(scs_tiporesolauto.DESCRIPCION,1) AS DESC_TIPORESOLAUTO "
				+" FROM scs_tiporesolauto "
				+" WHERE " 
				
				+" scs_tiporesolauto.idtiporesolauto = :1";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoResolucionAutomatico.");
		}
	}
	public Vector getTipoSentidoAutomatico ( String idTipoSentido) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idTipoSentido);
			String sql = "SELECT f_siga_getrecurso(scs_tiposentidoauto.DESCRIPCION,1) AS DESC_TIPOSENTIDOAUTO "
				+" FROM scs_tiposentidoauto "
				+" WHERE " 
				
				+" scs_tiposentidoauto.idtiposentidoauto = :1";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoSentidoAutomatico.");
		}
	}
	
	public Vector getTipoDictamenEjg (String idInstitucion, String idTipoDictamen) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idTipoDictamen);
			String sql = "SELECT f_siga_getrecurso(scs_tipodictamenejg.DESCRIPCION,1) AS DESC_TIPODICTAMENEJG "
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
	public Vector getTipoRatificacionEjg (String idTipoRatificacion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
		
			h.put(new Integer(1), idTipoRatificacion);
			String sql = "SELECT f_siga_getrecurso(SCS_TIPORESOLUCION.DESCRIPCION,1) AS DESC_TIPORATIFICACIONEJG "
				+" FROM SCS_TIPORESOLUCION "
				+" WHERE " 
				
				+" SCS_TIPORESOLUCION.IDTIPORESOLUCION = :1";
			
				
			return ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoRatificacionEjg.");
		}
	}
	
	public Vector getPersonaInstitucion (String idInstitucion)throws SIGAException,ClsExceptions
	{	
		try { 
			 
			StringBuffer sql = new StringBuffer();
			sql.append(" select ins.nombre NOMBREINSTITUCION,per.nifcif AS NIFCIFINSTITUCION ");
			sql.append(" ,f_siga_getdireccioncliente(ins.idinstitucion,per.idpersona,1,3) AS INSTITUCIONPOBLACION ");
			sql.append(" ,TO_CHAR(SYSDATE, 'dd') AS DIA_HOY ");
			sql.append(" ,TO_CHAR(SYSDATE, 'MONTH', 'NLS_DATE_LANGUAGE = SPANISH') AS MES_HOY ");
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
	
	
	
}
