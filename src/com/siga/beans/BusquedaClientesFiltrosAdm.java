package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;


public class BusquedaClientesFiltrosAdm {
	
	private UsrBean usrbean = null;
	
	//variables para implementar los filtros
	protected String tablaColegiado=
		"(select "+CenColegiadoBean.C_IDINSTITUCION+","+CenColegiadoBean.C_IDPERSONA+","+
		"F_SIGA_CALCULONCOLEGIADO("+CenColegiadoBean.C_IDINSTITUCION+","+CenColegiadoBean.C_IDPERSONA+") "+CenColegiadoBean.C_NCOLEGIADO+
		" from "+CenColegiadoBean.T_NOMBRETABLA+")";
	protected String tablaPersona=CenPersonaBean.T_NOMBRETABLA;
	protected String nColegiado=null;
	protected String nif=null;
	protected String nombre=null;
	protected String apellido1=null;
	protected String apellido2=null;
	
	
	//Constructor vacio, para las busquedas
	public BusquedaClientesFiltrosAdm() {
	}
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public BusquedaClientesFiltrosAdm (UsrBean usuario) {
		this.usrbean=usuario;
	}	
	
	
	/** 
	 * Realiza una consulta libre. 
	 * @param sql con la consulta en el formato adecuado
	 * @return Vector de Rows con el resultado 
	 */
	protected Vector find(String sql) throws ClsExceptions	{
		RowsContainer rc = new RowsContainer(); 
		rc.find(sql);
		return rc.getAll();
	}
	protected Vector findNLS(String sql) throws ClsExceptions	{
		RowsContainer rc = new RowsContainer(); 
		rc.findNLS(sql);
		return rc.getAll();
	}
	
	
	/**
	 * Establece los filtros que se aplican sobre la tabla persona
	 * @param nif filtro sobre el nif
	 * @param nombre filtro sobre el nombre
	 * @param apellido1 filtro sobre el primer apellido
	 * @param apellido2 filtro sobre el segundo apellido
	 */
	public void setFiltosPersona(String nif, String nombre, String apellido1, String apellido2){
		this.nif=nif;
		this.nombre=nombre;
		this.apellido1=apellido1;
		this.apellido2=apellido2;
	}
	
	
	/**
	 * Establece el filtros que se aplica sobre la tabla colegiado
	 * @param nColegiado filtro sobre el numero de colegiado
	 */
	public void setFiltroNColegiado(String nColegiado){
		this.nColegiado=nColegiado;
	}
	
	
	/**
	 * Busca los letrados de una misma guardia.<br>
	 * Los ordena por posicion
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */
	public Vector buscaLetradosMismaGuardia(String idInstitucion, String idTurno, String idGuardia, String fecha) throws ClsExceptions{
		return buscaInternaLetradosMismaGuardia(idInstitucion, idTurno, idGuardia, fecha, true, 0);
	}
	
	
	/**
	 * Busca los letrados de todas las guardias del turno.<br>
	 * Primero los de la misma guardia ordenados por posicion,<br>
	 * Luego los del resto de guardias por orden alfabetico.
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */	
	/*public Vector buscaLetradosTodasLasGuardias(String idInstitucion, String idTurno, String idGuardia, String fecha) throws ClsExceptions{
		//primero buscamos los de nuestra guardia
		Vector result=this.buscaInternaLetradosMismaGuardia(idInstitucion, idTurno, idGuardia, fecha, true, 0);
		if(result==null) result= new Vector();
		int difRow=result.size();
		
		//luego buscamos el resto de guardias
		String sqlGuardias=
			"select "+ScsGuardiasTurnoBean.C_IDGUARDIA+
			"  from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
			" where "+ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idInstitucion+
			"   and "+ScsGuardiasTurnoBean.C_IDTURNO+"="+idTurno+
			"   and "+ScsGuardiasTurnoBean.C_IDGUARDIA+"<>"+idGuardia+
			" order by "+ScsGuardiasTurnoBean.C_NOMBRE;
		
		Vector guardias=findNLS(sqlGuardias);
		if(guardias!=null && !guardias.isEmpty()){
			int nt=guardias.size();
			for(int i=0;i<nt;i++){
				//y buscamos los de cada uno
				Row auxR=(Row)guardias.get(i);
				String auxG=auxR.getString(ScsGuardiasTurnoBean.C_IDGUARDIA);
				Vector auxV=buscaInternaLetradosMismaGuardia(idInstitucion,idTurno,auxG,fecha, false, difRow);
				if(auxV!=null){
					while(!auxV.isEmpty()){
						result.addElement(auxV.elementAt(0));
						auxV.removeElementAt(0);
						difRow++;
					}
				}
			}
		}
		return result;
	}
	*/
	public Vector buscaLetradosTodasLasGuardiasDistintasAGuadria(String idInstitucion, String idTurno, String idGuardia, String fecha) throws ClsExceptions
	{
		//primero buscamos los de nuestra guardia
		Vector letradosTodasLasGuardiasDistintasAGuardiaVector = new Vector();
		
		//luego buscamos el resto de guardias
		String sqlGuardias=
			"select "+ScsGuardiasTurnoBean.C_IDGUARDIA+
			"  from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
			" where "+ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idInstitucion+
			"   and "+ScsGuardiasTurnoBean.C_IDTURNO+"="+idTurno;
		
			if (idGuardia != null && !idGuardia.equals(""))
				sqlGuardias += " and "+ScsGuardiasTurnoBean.C_IDGUARDIA+"<>"+idGuardia;
			
			sqlGuardias += " order by "+ScsGuardiasTurnoBean.C_NOMBRE;
		
		Vector guardias=findNLS(sqlGuardias);
		if(guardias!=null && !guardias.isEmpty()){
			int nt=guardias.size();
			for(int i=0;i<nt;i++){
				//y buscamos los de cada uno
				Row auxR=(Row)guardias.get(i);
				String auxG=auxR.getString(ScsGuardiasTurnoBean.C_IDGUARDIA);
				Vector auxV=buscaInternaLetradosMismaGuardia(idInstitucion,idTurno,auxG,fecha, false, letradosTodasLasGuardiasDistintasAGuardiaVector.size());
				if(auxV!=null){
					
						if(auxV!=null){
							letradosTodasLasGuardiasDistintasAGuardiaVector.addAll(auxV);

						}
					
				}
			}
		}
		return letradosTodasLasGuardiasDistintasAGuardiaVector;
	}
	
	
	/**
	 * Busca todos los letrados del turno:<br>
	 * Los de la misma guardia ordenados por posicion<br>
	 * Los del resto de guardias por orden alfabetico de guardias y nombre<br>
	 * Los Los del mismo turno que no tienen guardias por orden alfabetico de nombre<br>
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */
	protected Vector buscaLetradosDelTurno(String idInstitucion, String idTurno,  String fecha,int difRow)throws ClsExceptions{

		////////////////////////
		// Cambio ca-sjcs-004
		// Antes:
		//primero buscamos los de todas las guardias
//		Vector result=buscaLetradosTodasLasGuardias(idInstitucion, idTurno, idGuardia,fecha);
//		if(result==null) result= new Vector();
		// Ahora:
		////////////////////////
		
		
		//luego los que no tienen guardia
		String sqlResto=	
			"select "+difRow+" N, x.*	from ("+
			"select p."+CenPersonaBean.C_IDPERSONA+","+ 
			"p."+CenPersonaBean.C_NIFCIF+","+ 
			"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
			"p."+CenPersonaBean.C_NOMBRE+","+ 
			"p."+CenPersonaBean.C_APELLIDOS1+","+ 
			"p."+CenPersonaBean.C_APELLIDOS2+","+ 
			"t."+ScsTurnoBean.C_NOMBRE+" TURNO,"+
			" '' GUARDIA,"+
			" '' POSICION, "+
			" '0' SALTO "+
			
			", (select TELEFONO1 from cen_direccion_tipodireccion t, cen_direcciones pe "+
			" where pe."+CenDireccionesBean.C_IDINSTITUCION+"=t."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
			" and pe."+CenDireccionesBean.C_IDPERSONA+"=t."+CenDireccionTipoDireccionBean.C_IDPERSONA+
			" and pe."+CenDireccionesBean.C_IDDIRECCION+"=t."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
			" and pe."+CenDireccionesBean.C_IDINSTITUCION+"="+idInstitucion +
			" and pe."+CenDireccionesBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
			" and t."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+"=6" +
			" and pe."+CenDireccionesBean.C_FECHABAJA+" is null " +
		    " and rownum=1) TELEFONO "+
			
			"from "+
			ScsInscripcionTurnoBean.T_NOMBRETABLA+" it,"+
			this.getTablaColegiado()+" c,"+
			this.getTablaPersona()+" p,"+
			ScsTurnoBean.T_NOMBRETABLA+" t "+
			"where it."+ScsInscripcionTurnoBean.C_IDINSTITUCION+"="+idInstitucion+
			"  and it."+ScsInscripcionTurnoBean.C_IDTURNO+"="+idTurno+
			"  and it."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+" is not null"+
			"  and TRUNC(it."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+") <= to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD') "+
			"  and (it."+ScsInscripcionTurnoBean.C_FECHABAJA+" is null"+
			"  or TRUNC(it."+ScsInscripcionTurnoBean.C_FECHABAJA+") > to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD') )"+
			"  and it."+ScsInscripcionTurnoBean.C_IDINSTITUCION+"=c."+CenColegiadoBean.C_IDINSTITUCION+
			"  and it."+ScsInscripcionTurnoBean.C_IDINSTITUCION+"=t."+ScsTurnoBean.C_IDINSTITUCION+
			"  and it."+ScsInscripcionTurnoBean.C_IDTURNO+"=t."+ScsTurnoBean.C_IDTURNO+
			"  and it."+ScsInscripcionTurnoBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA+
			"  and it."+ScsInscripcionTurnoBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
//			" order by t."+ScsTurnoBean.C_NOMBRE+",p."+CenPersonaBean.C_APELLIDOS1+",p."+CenPersonaBean.C_APELLIDOS2+",p."+CenPersonaBean.C_NOMBRE+ 
			" order by p."+CenPersonaBean.C_APELLIDOS1+",p."+CenPersonaBean.C_APELLIDOS2+",p."+CenPersonaBean.C_NOMBRE+ 
			" ) x";
		
		
		
		return findNLS(sqlResto);
	}
	
	
	public Vector buscaLetradosDelTurno(String idInstitucion, String idTurno, String fecha) throws ClsExceptions {
		return buscaLetradosDelTurno(idInstitucion, idTurno, fecha, 0);
	}
	
	/**
	 * Busca los letrados de todos los turnos:
	 * Los del turno ordenados por salto y posicion
	 * Los del resto de turnos ordenados por turno y nombre
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */
	public Vector buscaLetradosTodosLosTurnos(String idInstitucion, String idTurno,String fecha) throws ClsExceptions{
		//primero buscamos los de nuestro turno
		Vector letradoTodosLosTurnosTurnoVector = buscaLetradosDelTurno(idInstitucion, idTurno, fecha,0);
		if(letradoTodosLosTurnosTurnoVector==null) letradoTodosLosTurnosTurnoVector= new Vector();
		
		//luego buscamos el resto de turnos
		String sqlTurnos=
			"select "+ScsTurnoBean.C_IDTURNO+
			" from "+ScsTurnoBean.T_NOMBRETABLA+
			" where "+ScsTurnoBean.C_IDTURNO+"<>"+idTurno+
			" and "+ScsTurnoBean.C_IDINSTITUCION+"="+idInstitucion+
			" order by "+ScsTurnoBean.C_NOMBRE;
		
		Vector turnos=findNLS(sqlTurnos);
		if(turnos!=null && !turnos.isEmpty()){
			int nt=turnos.size();
			for(int i=0;i<nt;i++){
				//y buscamos los de cada uno
				Row auxR=(Row)turnos.get(i);
				String auxT=auxR.getString(ScsTurnoBean.C_IDTURNO);
				Vector auxV=buscaLetradosDelTurno(idInstitucion, auxT,fecha, letradoTodosLosTurnosTurnoVector.size());
				if(auxV!=null){
					letradoTodosLosTurnosTurnoVector.addAll(auxV);

				}
			}
		}
		
		return letradoTodosLosTurnosTurnoVector;
	}
	
	/**
	 * Busca todos los letrados del censo:<br>
	 * Los de la misma guardia ordenados por posicion<br>
	 * Los del resto de guardias por orden alfabetico de guardia y nombre<br>
	 * Los del mismo turno que no tienen guardias por orden alfabetico de nombre<br>
	 * Los de otros turnos por orden alfabetico de turnos y nombre<br>
	 * Los que no son de ningun turno por orden alfabetico de nombre<br>
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */
	public Vector buscaLetradosCensoCompleto(String idInstitucion,  String fecha)throws ClsExceptions{

		String sqlResto="select "+ 0 + " N, x.* "+ " from ("+	this.getSqlLetradosCompleto(idInstitucion)+") x ";
		////////////////////////
		
		Vector letradosCensoCompletoVector=findNLS(sqlResto);
		
		return letradosCensoCompletoVector;
	}
	
	/**
	 * Busca todos los letrados del censo:<br>
	 * Los del turno ordenados por salto y posicion
	 * Los del resto de turnos ordenados por turno y nombre
	 * Los que no son de ningun turno por orden alfabetico de nombre<br>
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */
	/*public Vector buscaLetradosCensoCompleto(String idInstitucion, String idTurno,String fecha)throws ClsExceptions{
		//Primero los que tienen turno
		Vector result=buscaLetradosTodosLosTurnos(idInstitucion, idTurno,fecha);
		if(result==null) result= new Vector();
		int difRow=result.size();
		
		//luego los que no tienen turno
		String sqlResto="select "+difRow+
		" N, x.* "+
		
	
		" from ("+this.getSqlLetradosSinTurno(idInstitucion)+") x ";

		Vector resto=findNLS(sqlResto);
		if(resto!=null && !resto.isEmpty()){
			while(!resto.isEmpty()){
				result.addElement(resto.elementAt(0));
				resto.removeElementAt(0);
			}
		}
		
		return result;
	}
	*/
			
	public synchronized LetradoGuardia gestionaDesignacionesAutomaticas(String idInstitucion, String idTurno, String fecha)throws ClsExceptions, SIGAException{
		
		String saltoocompensacion = null;
		String idSaltosTurno;
		ScsSaltosCompensacionesAdm saltocompAdm = new ScsSaltosCompensacionesAdm(usrbean);
		try{
			//busco las compensaciones
			Vector<LetradoGuardia> letradosCompensacionesVector = saltocompAdm.getLetradosSaltosCompensacionesTurno(idInstitucion, idTurno);
			
			if (letradosCompensacionesVector != null && letradosCompensacionesVector.size()>0) {
				for (LetradoGuardia letradoTurno :letradosCompensacionesVector){
					//letradoGuardia = (Row)compensacionesSaltos.get(i);
					saltoocompensacion = letradoTurno.getSaltoCompensacion();					

					if (ClsConstants.COMPENSACIONES.equals(saltoocompensacion)) {
						idSaltosTurno = letradoTurno.getIdSaltoCompensacion();
						
						CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usrbean);
						//comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
						Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(letradoTurno.getIdPersona(), new Integer(idInstitucion));
						if(mBajasTemporales.containsKey(fecha)){
							ScsSaltosCompensacionesBean salto = new ScsSaltosCompensacionesBean(
								new Integer(idInstitucion),new Integer(idTurno),letradoTurno.getIdPersona(),ClsConstants.SALTOS,"sysdate");
							ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrbean);
							CenBajasTemporalesBean bajaTemporal= (CenBajasTemporalesBean)mBajasTemporales.get(fecha);
							//bajaTemporal.setDescripcion(bajaTemporal.getDescripcion()+" al crear designa para el "+fecha+" ");
							scsSaltosCompensacionesAdm.insertarSaltoPorBajaTemporal(bajaTemporal,salto);
							continue;
							
						}
						
						

						updateCompensacion(idInstitucion, idTurno, idSaltosTurno);
						
						return letradoTurno;
					}
				}
			}

			List<LetradoGuardia> colaLetradosTurnoList = InscripcionTurno.getColaTurno(Integer.valueOf(idInstitucion), Integer.valueOf(idTurno), fecha,true,usrbean);
			if(colaLetradosTurnoList != null && colaLetradosTurnoList.size() > 0) {	
				int i = 0;
				for (LetradoGuardia letradoTurno :colaLetradosTurnoList){
					Long idPersona = letradoTurno.getIdPersona();
					
					CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usrbean);
					//comprobamos que el letrado no esta de vacaciones 
					Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersona), new Integer(idInstitucion));
					if(mBajasTemporales.containsKey(fecha)){
						ScsSaltosCompensacionesBean salto = new ScsSaltosCompensacionesBean(
							new Integer(idInstitucion),new Integer(idTurno),letradoTurno.getIdPersona(),ClsConstants.SALTOS,"sysdate");
						ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrbean);
						CenBajasTemporalesBean bajaTemporal= (CenBajasTemporalesBean)mBajasTemporales.get(fecha);
						//bajaTemporal.setDescripcion(bajaTemporal.getDescripcion()+" al crear designa para el "+fecha+" ");
						scsSaltosCompensacionesAdm.insertarSaltoPorBajaTemporal(bajaTemporal,salto);
						continue;
					}
					
					idSaltosTurno = getIdSaltoTurno(letradosCompensacionesVector, idPersona.toString());
					//se buscan los saltos
					if (idSaltosTurno != null) {
						updateCompensacion(idInstitucion, idTurno, idSaltosTurno);
						continue;
					} else {
						return letradoTurno;
					}
					
					
				}
			}
			
			throw new SIGAException("messages.designa.colaVacia");

			
		}catch(ClsExceptions ex){
			throw ex;
		}
		
	}
	
	
	private String getIdSaltoTurno(Vector compensacionesSaltos, String idPersona) {
		String idSaltoTurno = null;
		if (compensacionesSaltos != null){
			for (int i = 0; i < compensacionesSaltos.size();i++){
				LetradoGuardia letradoTurno = (LetradoGuardia)compensacionesSaltos.get(i);
				Long idPersonaSC = letradoTurno.getIdPersona();
				String salto = letradoTurno.getSaltoCompensacion();
				if (idPersona.toString().equals(idPersonaSC)){
					if (salto != null && salto.trim().equals(ClsConstants.SALTOS)) {
						idSaltoTurno = letradoTurno.getIdSaltoCompensacion();
						compensacionesSaltos.remove(i);
						break;
					}
				}
			}
		}
		return idSaltoTurno;
	}


	private void updateCompensacion(String idInstitucion, String idTurno, String idSaltosTurno) throws ClsExceptions {
		ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrbean);
		Hashtable hash = new Hashtable();
		hash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, "SYSDATE");
		hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ScsSaltosCompensacionesBean.C_IDTURNO, idTurno);
		hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, idSaltosTurno);
		String[] claves = new String[]{ScsSaltosCompensacionesBean.C_IDINSTITUCION, ScsSaltosCompensacionesBean.C_IDTURNO, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO};
		String[] campos = new String[]{ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO};
		
		if (!scsSaltosCompensacionesAdm.updateDirect(hash, claves, campos)) {
			throw new ClsExceptions("No se ha podido actualizar el salto o compensación");
		}
	}


	


	
	//este método ya no se usa. Usar el método gestionaDesignacionesAutomaticas
	/**
	 * Busca el primero de la cola
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @return Row con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */	
	/*public Row buscaAutomaticaDesignaciones(String idInstitucion, String idTurno)throws ClsExceptions{
		Row result=null;
		String contador = null;
		try{
			contador = cargaTemporal(idInstitucion,idTurno,"1");
			
			//Consulta en la tabla temporal la posicion para el letrado
			String consultaTemp =
				"select p."+CenPersonaBean.C_IDPERSONA+","+ 
				"p."+CenPersonaBean.C_NIFCIF+","+ 
				"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
				"p."+CenPersonaBean.C_NOMBRE+","+ 
				"p."+CenPersonaBean.C_APELLIDOS1+","+ 
				"p."+CenPersonaBean.C_APELLIDOS2+","+ 
				"'0' SALTO, "+
				"ct."+GenClientesTemporalBean.C_SALTO+" COMPENSACION "+
				"from "+
				"(select "+GenClientesTemporalBean.C_IDPERSONA+","+
				GenClientesTemporalBean.C_SALTO+","+
				GenClientesTemporalBean.C_POSICION+
				"   from "+GenClientesTemporalBean.T_NOMBRETABLA+
				"  where "+GenClientesTemporalBean.C_CONTADOR+"="+contador+
				"    and "+GenClientesTemporalBean.C_SALTO+" <> 'S'"+
				"    and "+GenClientesTemporalBean.C_POSICION+"="+
				"   	 (select min("+GenClientesTemporalBean.C_POSICION+")"+
				"    	    from "+GenClientesTemporalBean.T_NOMBRETABLA+
				"   	   where "+GenClientesTemporalBean.C_CONTADOR+"="+contador+
				"    		 and "+GenClientesTemporalBean.C_SALTO+" <> 'S')"+
				"  order by "+GenClientesTemporalBean.C_POSICION+") ct,"+
				this.getTablaColegiado()+" c,"+
				CenPersonaBean.T_NOMBRETABLA+" p"+
				" where c."+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
				"   and ct."+GenClientesTemporalBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
				"   and ct."+GenClientesTemporalBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA+
				" order by ct."+GenClientesTemporalBean.C_SALTO+",ct."+GenClientesTemporalBean.C_POSICION;
			System.out.println("Consulta de la busqueda automatica de designas "+consultaTemp);
			Vector vResult = find(consultaTemp);
			if(vResult!=null && vResult.size()==1){
				result=(Row) vResult.elementAt(0);
				Hashtable traza = result.getRow();
				
				ClsLogging.writeFileLog("buscaAutomaticaDesignaciones",10);
				ClsLogging.writeFileLog("IDPERSONA = " + traza.get(CenPersonaBean.C_IDPERSONA),10);
				ClsLogging.writeFileLog("COMPENSACION = " + traza.get("COMPENSACION"),10);
				ClsLogging.writeFileLog("SALTO = " + traza.get("SALTO"),10);
				
			}
			
		}catch(ClsExceptions ex){
			throw ex;
		}finally{
			borraTemporal(contador);
		}
		
		return result;
	}*/
	
	
	/**
	 * Busca el que menos asistencias ha hecho ese dia
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Row con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */	
	public Row buscaAutomaticaAsistencias(String idInstitucion, String idTurno, String idGuardia, String fecha)throws ClsExceptions{
		Row result=null;
		//Consulta en la tabla temporal la posicion para el letrado
		String consultaTemp =
			"select p."+CenPersonaBean.C_IDPERSONA+","+ 
			"p."+CenPersonaBean.C_NIFCIF+","+ 
			"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
			"p."+CenPersonaBean.C_NOMBRE+","+ 
			"p."+CenPersonaBean.C_APELLIDOS1+","+ 
			"p."+CenPersonaBean.C_APELLIDOS2+","+ 
			"'0' SALTO, "+
			"'' COMPENSACION "+
			"from "+
			"(select "+ScsGuardiasColegiadoBean.C_IDPERSONA+" from"+
			"  (select "+ScsGuardiasColegiadoBean.C_IDPERSONA+", numeroActuacionesDia from"+ 
			"     (select gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+","+
			"        (select count(*)"+
			"           from "+ScsActuacionAsistenciaBean.T_NOMBRETABLA+" a,"+ScsAsistenciasBean.T_NOMBRETABLA+" b"+
			"          where b."+ScsAsistenciasBean.C_IDINSTITUCION+"=a."+ScsActuacionAsistenciaBean.C_IDINSTITUCION+
			"            and b."+ScsAsistenciasBean.C_ANIO+"=a."+ScsActuacionAsistenciaBean.C_ANIO+
			"            and b."+ScsAsistenciasBean.C_NUMERO+"=a."+ScsActuacionAsistenciaBean.C_NUMERO+
			"            and b."+ScsAsistenciasBean.C_IDINSTITUCION+"="+idInstitucion+
			"            and b."+ScsAsistenciasBean.C_IDTURNO+"="+idTurno+
			"            and b."+ScsAsistenciasBean.C_IDGUARDIA+"="+idGuardia+
			"            and b."+ScsAsistenciasBean.C_IDPERSONACOLEGIADO+"=gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+
			"            and trunc(a."+ScsActuacionAsistenciaBean.C_FECHA+") = to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')"+
			"		 ) numeroActuacionesDia"+
			"        from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" gc"+
			"       where gc."+ScsGuardiasColegiadoBean.C_IDINSTITUCION +"="+idInstitucion+
			"         and gc."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idTurno+
			"         and gc."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idGuardia+
//			"         and trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+") = to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')"+
			"         and trunc(to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')) between trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAINICIO+") and  trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+") "+
			"     )order by numeroActuacionesDia"+
			"  )where rownum < 2"+
			") a,"+
			this.getTablaColegiado()+" c,"+
			CenPersonaBean.T_NOMBRETABLA+" p"+
			" where c."+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
			"   and a."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
			"   and a."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA;
		
		Vector vResult = find(consultaTemp);
		if(vResult!=null && vResult.size()==1){
			result=(Row) vResult.elementAt(0);
		}
		
		return result;
	}
	
	
	/**
	 * Busca el que menos EJG ha hecho ese dia
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Row con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */	
	public Row buscaAutomaticaEJG(String idInstitucion, String idTurno, String idGuardia, String fecha)throws ClsExceptions{
		Row result=null;
		
		//Consulta en la tabla temporal la posicion para el letrado
		String consultaTemp =
			"select p."+CenPersonaBean.C_IDPERSONA+","+ 
			"p."+CenPersonaBean.C_NIFCIF+","+ 
			"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
			"p."+CenPersonaBean.C_NOMBRE+","+ 
			"p."+CenPersonaBean.C_APELLIDOS1+","+ 
			"p."+CenPersonaBean.C_APELLIDOS2+","+ 
			"'0' SALTO, "+
			"'' COMPENSACION "+
			"from "+
			"(select "+ScsGuardiasColegiadoBean.C_IDPERSONA+" from"+
			"  (select "+ScsGuardiasColegiadoBean.C_IDPERSONA+", numeroSojsDia from"+ 
			"     (select gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+","+
			"        (select count(*)"+
			"           from "+ScsSOJBean.T_NOMBRETABLA+
			"          where "+ScsSOJBean.C_IDINSTITUCION+"="+idInstitucion+
			"            and "+ScsSOJBean.C_IDTURNO+"="+idTurno+
			"            and "+ScsSOJBean.C_IDGUARDIA+"="+idGuardia+
			"            and "+ScsSOJBean.C_IDPERSONA+"=gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+
			"            and trunc("+ScsSOJBean.C_FECHAAPERTURA+") = to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')"+
			"		 ) numeroSojsDia"+
			"        from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" gc"+
			"       where gc."+ScsGuardiasColegiadoBean.C_IDINSTITUCION +"="+idInstitucion+
			"         and gc."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idTurno+
			"         and gc."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idGuardia+
//			"         and trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+") = to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')"+
			"         and trunc(to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')) between trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAINICIO+") and  trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+") "+
			"     )order by numeroSojsDia"+
			"  )where rownum < 2"+
			") a,"+
			this.getTablaColegiado()+" c,"+
			CenPersonaBean.T_NOMBRETABLA+" p"+
			" where c."+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
			"   and a."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
			"   and a."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA;
		
		Vector vResult = find(consultaTemp);
		if(vResult!=null && vResult.size()==1){
			result=(Row) vResult.elementAt(0);
		}
		
		return result;
	}
	
	
	/**
	 * Busca el que menos SOJ ha hecho ese dia
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @return Row con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */	
	public Row buscaAutomaticaSOJ(String idInstitucion, String idTurno, String idGuardia, String fecha)throws ClsExceptions{
		Row result=null;
		
		//Consulta en la tabla temporal la posicion para el letrado
		String consultaTemp =
			"select p."+CenPersonaBean.C_IDPERSONA+","+ 
			"p."+CenPersonaBean.C_NIFCIF+","+ 
			"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
			"p."+CenPersonaBean.C_NOMBRE+","+ 
			"p."+CenPersonaBean.C_APELLIDOS1+","+ 
			"p."+CenPersonaBean.C_APELLIDOS2+","+ 
			"'0' SALTO, "+
			"'' COMPENSACION "+
			"from "+
			"(select "+ScsGuardiasColegiadoBean.C_IDPERSONA+" from"+
			"  (select "+ScsGuardiasColegiadoBean.C_IDPERSONA+", numeroEjgsDia from"+ 
			"     (select gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+","+
			"        (select count(*)"+
			"           from "+ScsEJGBean.T_NOMBRETABLA+
			"          where "+ScsEJGBean.C_IDINSTITUCION+"="+idInstitucion+
			"            and "+ScsEJGBean.C_GUARDIATURNO_IDTURNO+"="+idTurno+
			"            and "+ScsEJGBean.C_GUARDIATURNO_IDGUARDIA+"="+idGuardia+
			"            and "+ScsEJGBean.C_IDPERSONA+"=gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+
			"            and trunc("+ScsEJGBean.C_FECHAAPERTURA+") = to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')"+
			"		 ) numeroEjgsDia"+
			"        from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" gc"+
			"       where gc."+ScsGuardiasColegiadoBean.C_IDINSTITUCION +"="+idInstitucion+
			"         and gc."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idTurno+
			"         and gc."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idGuardia+
			"         and trunc(to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')) between trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAINICIO+") and  trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+") "+
			"     )order by numeroEjgsDia"+
			"  )where rownum < 2"+
			") a,"+
			this.getTablaColegiado()+" c,"+
			CenPersonaBean.T_NOMBRETABLA+" p"+
			" where c."+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
			"   and a."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
			"   and a."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA;
		
		Vector vResult = find(consultaTemp);
		if(vResult!=null && vResult.size()==1){
			result=(Row) vResult.elementAt(0);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Busca los letrados de una misma guardia.<br>
	 * Los ordena por posicion
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param fecha Fecha sobre la que se realiza la busqueda
	 * @param saltos Indicador para cargar o no el campo salto
	 * @param difRow Posicion de la fila anterior
	 * @return Vector de Rows con los resultados
	 * @throws ClsExceptions Excepcion interna
	 */
	protected Vector buscaInternaLetradosMismaGuardia(String idInstitucion, String idTurno, String idGuardia, String fecha, boolean saltos, int difRow) throws ClsExceptions
	{
		String sql=
			"select "+difRow+" N, x.*	from ("+
			"select p."+CenPersonaBean.C_IDPERSONA+","+ 
			"p."+CenPersonaBean.C_NIFCIF+","+ 
			"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
			"p."+CenPersonaBean.C_NOMBRE+","+ 
			"p."+CenPersonaBean.C_APELLIDOS1+","+ 
			"p."+CenPersonaBean.C_APELLIDOS2+","+ 
			"t."+ScsTurnoBean.C_NOMBRE+" TURNO,"+
			"gt."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
			" gt."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTU,               " +
			" gt."+ScsGuardiasTurnoBean.C_IDGUARDIA +" IDGU," +
			"decode (nvl(gt.idguardiasustitucion,0),0,'NO','SI') as GUARDIA_SUSTITUCION,"+
			"(select count(*) "+
			" from "+ ScsCabeceraGuardiasBean.T_NOMBRETABLA+" cg"+
			" where cg."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"=g."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+
			"   and cg."+ScsCabeceraGuardiasBean.C_IDPERSONA+"=g."+ScsInscripcionGuardiaBean.C_IDPERSONA+
			"   and cg."+ScsCabeceraGuardiasBean.C_IDTURNO+"=g."+ScsInscripcionGuardiaBean.C_IDTURNO+
			"   and cg."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"=g."+ScsInscripcionGuardiaBean.C_IDGUARDIA+
			"   and cg."+ScsCabeceraGuardiasBean.C_FECHA_FIN+">=SYSDATE "+
			") POSICION, "+
			(saltos?//Si esta de guardia el dia que nos pasan entonces 0 y si no 1 -> decode(estaGuardia,0,1)
					(" decode((select count(*) from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" gc"+
							" where gc."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"=g."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+
							"   and gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+"=g."+ScsInscripcionGuardiaBean.C_IDPERSONA+
							"   and gc."+ScsGuardiasColegiadoBean.C_IDTURNO+"=g."+ScsInscripcionGuardiaBean.C_IDTURNO+
							"   and gc."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"=g."+ScsInscripcionGuardiaBean.C_IDGUARDIA+
							"   and trunc(gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+")=to_date('"+fecha.substring(0,10)+"','YYYY/MM/DD')"+
					"),0,'1','0')"):
						("'0'"))+" SALTO, "+
						"'N' COMPENSACION, "+
						
						" (select TELEFONO1 from cen_direccion_tipodireccion t, cen_direcciones pe "+
						" where pe."+CenDireccionesBean.C_IDINSTITUCION+"=t."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
						" and pe."+CenDireccionesBean.C_IDPERSONA+"=t."+CenDireccionTipoDireccionBean.C_IDPERSONA+
						" and pe."+CenDireccionesBean.C_IDDIRECCION+"=t."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
						" and pe."+CenDireccionesBean.C_IDINSTITUCION+"="+idInstitucion +
						" and pe."+CenDireccionesBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
						" and t."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+"=6"+
						" and pe."+CenDireccionesBean.C_FECHABAJA+" is null " +
						" and rownum=1) TELEFONO "+
						
				"from "+
						ScsInscripcionGuardiaBean.T_NOMBRETABLA+" g,"+
						this.getTablaColegiado()+" c,"+
						this.getTablaPersona()+" p,"+
						ScsTurnoBean.T_NOMBRETABLA+" t,"+
						ScsGuardiasTurnoBean.T_NOMBRETABLA+" gt "+
						
				"where g."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+"="+idInstitucion+
						"  and g."+ScsInscripcionGuardiaBean.C_IDTURNO+"="+idTurno+
						"  and g."+ScsInscripcionGuardiaBean.C_IDGUARDIA+"="+idGuardia+
						" and g.fechavalidacion is not null "+
						" and TRUNC(g.fechavalidacion) <= "+
						" to_date('"+fecha.substring(0,10)+"', 'YYYY/MM/DD') "+
						" and (g.fechabaja is null or "+
						" TRUNC(g.fechabaja) > to_date('"+fecha.substring(0,10)+"', 'YYYY/MM/DD')) "+
						"  and g."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+"=c."+CenColegiadoBean.C_IDINSTITUCION+
						"  and g."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+"=gt."+ScsGuardiasTurnoBean.C_IDINSTITUCION+
						"  and g."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+"=t."+ScsTurnoBean.C_IDINSTITUCION+
						"  and g."+ScsInscripcionGuardiaBean.C_IDTURNO+"=t."+ScsTurnoBean.C_IDTURNO+
						"  and g."+ScsInscripcionGuardiaBean.C_IDTURNO+"=gt."+ScsGuardiasTurnoBean.C_IDTURNO+
						"  and g."+ScsInscripcionGuardiaBean.C_IDGUARDIA+"=gt."+ScsGuardiasTurnoBean.C_IDGUARDIA+
						"  and g."+ScsInscripcionGuardiaBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA+
						"  and g."+ScsInscripcionGuardiaBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
//						" order by POSICION"+

						" ORDER BY GUARDIA, p." + CenPersonaBean.C_APELLIDOS1 + ", p." + CenPersonaBean.C_APELLIDOS2 + ", p." + CenPersonaBean.C_NOMBRE + 

						") x";
		
		return	findNLS(sql);
	}
	
	
	
	/*protected Vector buscaInternaLetradosDelTurnoNew(String idInstitucion, String idTurno, String fecha, int difRow)throws ClsExceptions{
		List<LetradoGuardia> letradosColaTurnoList = InscripcionTurno.getColaTurno(Integer.valueOf(idInstitucion), Integer.valueOf(idTurno), fecha, false, usrbean);
		Vector letradosColaTurnoVector = new Vector();
		for(LetradoGuardia letradoTurno:letradosColaTurnoList){
			Row row = new Row();
			Hashtable htRow = new Hashtable();
			htRow.put("N", difRow);
			htRow.put("letradoTurno", letradoTurno);
			letradosColaTurnoVector.add(htRow);
		}
		
		return letradosColaTurnoVector;
	}	*/
	
	
	/**
	 * Crea una sentencia sql para ser utilizada como tabla persona dentro de una join.<br>
	 * Tiene en cuenta los filtros que pudieron ser insertados previamente en el metodo setFiltosPersona
	 * @return sentencia sql
	 */
	protected String getTablaPersona() {
		boolean hayFiltro=false;
		String filtro="";
		String tablaPersona=null;
		
		if (nombre!=null && !nombre.trim().equals("")) {
			filtro+=(hayFiltro?" and ":" where "); 
			hayFiltro=true; 
			filtro +="("+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),CenPersonaBean.C_NOMBRE)+")";
		}
		
		if (apellido1!=null && !apellido1.trim().equals("")) {
			filtro+=(hayFiltro?" and ":" where "); 
			hayFiltro=true; 
			
			filtro += "("+ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(),CenPersonaBean.C_APELLIDOS1)+")";
		}
		
		if (apellido2!=null && !apellido2.trim().equals("")) {
			filtro+=(hayFiltro?" and ":" where "); 
			hayFiltro=true; 
			
			filtro += "("+ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(),CenPersonaBean.C_APELLIDOS2)+")";
		}
//		Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
		   // de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
		   // la consulta adecuada.		
		if (nif!=null && !nif.trim().equals("")) {
			filtro+=(hayFiltro?" and ":" where "); 
			hayFiltro=true; 
		  if (ComodinBusquedas.hasComodin(nif)){		
			
		  	filtro +=""+ComodinBusquedas.prepararSentenciaCompleta(nif.trim(),CenPersonaBean.C_NIFCIF);
		  }else{
		  	filtro +=ComodinBusquedas.prepararSentenciaNIF(nif,"upper("+CenPersonaBean.C_NIFCIF+")");
		  }
		}
		
		if( hayFiltro){
			tablaPersona=
				"(select "+CenPersonaBean.C_IDPERSONA+","+CenPersonaBean.C_NOMBRE+","+
				CenPersonaBean.C_APELLIDOS1+","+CenPersonaBean.C_APELLIDOS2+","+CenPersonaBean.C_NIFCIF+
				" from "+CenPersonaBean.T_NOMBRETABLA + filtro + ")";
		}else{
			tablaPersona=CenPersonaBean.T_NOMBRETABLA;
		}
		return tablaPersona;
	}
	
	
	/**
	 * Crea una sentencia sql para ser utilizada como tabla colegiado dentro de una join.<br>
	 * Tiene en cuenta el filtro que pudo ser insertado previamente en el metodo setFiltoColegiado
	 * @return sentencia sql
	 */
	protected String getTablaColegiado(){
		String filtro="";
		if (nColegiado!=null && !nColegiado.trim().equals("")) {
			filtro=" where "+ComodinBusquedas.tratarNumeroColegiado(nColegiado,"F_SIGA_CALCULONCOLEGIADO("+CenColegiadoBean.C_IDINSTITUCION+","+CenColegiadoBean.C_IDPERSONA+") " );
		}
		String tablaColegiado=
			"(select "+CenColegiadoBean.C_IDINSTITUCION+","+CenColegiadoBean.C_IDPERSONA+","+
			"F_SIGA_CALCULONCOLEGIADO("+CenColegiadoBean.C_IDINSTITUCION+","+CenColegiadoBean.C_IDPERSONA+") "+CenColegiadoBean.C_NCOLEGIADO+
			" from "+CenColegiadoBean.T_NOMBRETABLA + filtro + ")";
		return tablaColegiado;
	}
	
	
	/**
	 * Monta la sentencia sql que busca todos los letrados del censo que no pertenecen a ningun turno
	 * @param idInstitucion Identificador de la institucion
	 * @return Sentencia sql
	 * @see buscaLetradosCensoCompleto
	 */
	protected String getSqlLetradosSinTurno(String idInstitucion){
		String sql=	
			"select p."+CenPersonaBean.C_IDPERSONA+","+ 
			"p."+CenPersonaBean.C_NIFCIF+","+ 
			"c."+CenColegiadoBean.C_NCOLEGIADO+","+ 
			"p."+CenPersonaBean.C_NOMBRE+","+ 
			"p."+CenPersonaBean.C_APELLIDOS1+","+ 
			"p."+CenPersonaBean.C_APELLIDOS2+","+ 
			"'' TURNO,"+
			"'' GUARDIA,"+
			"'' POSICION, "+
			"'0' SALTO, "+
			"'' COMPENSACION, "+
			
			" (select TELEFONO1 from cen_direccion_tipodireccion t, cen_direcciones pe "+
			" where pe."+CenDireccionesBean.C_IDINSTITUCION+"=t."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
			" and pe."+CenDireccionesBean.C_IDPERSONA+"=t."+CenDireccionTipoDireccionBean.C_IDPERSONA+
			" and pe."+CenDireccionesBean.C_IDDIRECCION+"=t."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
			" and pe."+CenDireccionesBean.C_IDINSTITUCION+"="+idInstitucion +
			" and pe."+CenDireccionesBean.C_IDPERSONA+"=p."+CenPersonaBean.C_IDPERSONA+
			" and t."+CenDireccionTipoDireccionBean.C_IDTIPODIRECCION+"=6 "+
			" and pe."+CenDireccionesBean.C_FECHABAJA+" is null " +
			" and rownum=1) TELEFONO "+
			
			
			"from "+
			this.getTablaColegiado()+" c,"+
			this.getTablaPersona()+" p,"+
			"(select "+CenDatosColegialesEstadoBean.C_IDPERSONA+
			"   from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+" a"+
			"  where "+CenDatosColegialesEstadoBean.C_IDINSTITUCION+"="+idInstitucion+
			"    and "+CenDatosColegialesEstadoBean.C_IDESTADO+"=20"+      
			"    and "+CenDatosColegialesEstadoBean.C_FECHAESTADO+"="+
			"		 (select max("+CenDatosColegialesEstadoBean.C_FECHAESTADO+")"+
			"		    from "+CenDatosColegialesEstadoBean.T_NOMBRETABLA+
			"		   where "+CenDatosColegialesEstadoBean.C_IDPERSONA+"= a."+CenDatosColegialesEstadoBean.C_IDPERSONA+
			"			 and "+CenDatosColegialesEstadoBean.C_IDINSTITUCION+"="+idInstitucion+" AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE)"+          
			" minus  "+
			" select "+ScsInscripcionTurnoBean.C_IDPERSONA+
			"   from "+ScsInscripcionTurnoBean.T_NOMBRETABLA+
			"  where "+ScsInscripcionTurnoBean.C_IDINSTITUCION+"="+idInstitucion+
			"    and "+ScsInscripcionTurnoBean.C_FECHAVALIDACION+" is not null"+
			"    and "+ScsInscripcionTurnoBean.C_FECHABAJA+" is null"+
			") r "+
			" where c."+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
			"   and c."+ScsInscripcionTurnoBean.C_IDPERSONA+"=p."+CenColegiadoBean.C_IDPERSONA+
			"   and c."+ScsInscripcionTurnoBean.C_IDPERSONA+"=r."+CenPersonaBean.C_IDPERSONA+
			" order by p."+CenPersonaBean.C_APELLIDOS1+",p."+CenPersonaBean.C_APELLIDOS2+",p."+CenPersonaBean.C_NOMBRE;
		return sql;	
	}

	
	protected String getSqlLetradosCompleto(String idInstitucion){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" ");
		sql.append("SELECT CEN_PERSONA.IDPERSONA, ");
		sql.append("CEN_PERSONA.NIFCIF, ");
		sql.append("F_SIGA_CALCULONCOLEGIADO(CEN_COLEGIADO.IDINSTITUCION, CEN_COLEGIADO.IDPERSONA) NCOLEGIADO, ");
		sql.append("CEN_PERSONA.NOMBRE, CEN_PERSONA.APELLIDOS1, CEN_PERSONA.APELLIDOS2, ");
		sql.append("'' TURNO,'' GUARDIA,'' POSICION,'0' SALTO,'' COMPENSACION, ");
		sql.append("(select TELEFONO1 ");
		sql.append("from cen_direccion_tipodireccion t, cen_direcciones pe ");
		sql.append("where pe.IDINSTITUCION = t.IDINSTITUCION ");
		sql.append("and pe.IDPERSONA = t.IDPERSONA ");
		sql.append("and pe.IDDIRECCION = t.IDDIRECCION ");
		sql.append("and pe.IDINSTITUCION = ");
		sql.append(idInstitucion);
		sql.append("and pe.IDPERSONA = CEN_PERSONA.IDPERSONA ");
		sql.append("and t.IDTIPODIRECCION = 6 ");
		sql.append("and pe.FECHABAJA is null ");
		sql.append("and rownum = 1) TELEFONO ");
		        
		sql.append("FROM CEN_PERSONA, CEN_CLIENTE, CEN_COLEGIADO ");
		sql.append("WHERE CEN_PERSONA.IDPERSONA = CEN_CLIENTE.IDPERSONA ");
		sql.append("AND CEN_CLIENTE.IDPERSONA = CEN_COLEGIADO.IDPERSONA ");
		sql.append("AND CEN_CLIENTE.IDINSTITUCION = CEN_COLEGIADO.IDINSTITUCION ");
		sql.append("AND (CEN_CLIENTE.IDINSTITUCION = ");
		sql.append(idInstitucion);
		sql.append(") AND CEN_CLIENTE.IDINSTITUCION = ");
		sql.append(idInstitucion);
		sql.append("AND (CEN_COLEGIADO.SITUACIONEJERCICIO = 1) ");
		sql.append("AND 1 in (select 1 ");
		sql.append("from CEN_DATOSCOLEGIALESESTADO ");
		sql.append("where CEN_DATOSCOLEGIALESESTADO.IDINSTITUCION = ");
		sql.append("CEN_CLIENTE.IDINSTITUCION ");
		sql.append("and IDPERSONA = CEN_CLIENTE.IDPERSONA ");
		sql.append("and FECHAESTADO = ");
		sql.append("(select max(FECHAESTADO) ");
		sql.append("from CEN_DATOSCOLEGIALESESTADO ");
		sql.append("where IDINSTITUCION = CEN_CLIENTE.IDINSTITUCION ");
		sql.append("and IDPERSONA = CEN_CLIENTE.IDPERSONA ");
		sql.append("and FECHAESTADO <= sysdate) ");
		sql.append("and IDESTADO in (20, 10)) ");
		        
		sql.append("ORDER BY CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2, ");
		sql.append("CEN_PERSONA.NOMBRE ");
		
		return sql.toString();	
	}

	/**
	 * Crea salto según los parámetros obtenidos en la busqueda SJCS
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param idPersona
	 * @param checkSalto
	 * @param motivo
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void crearSalto(String idInstitucion, String idTurno, String idGuardia, String idPersona, String checkSalto, String motivo ) throws ClsExceptions, SIGAException {
		
		if (checkSalto!=null){
			if(checkSalto.equals("on") || checkSalto.equals("1")) {
				// cuando hay que insertar salto
				ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrbean);
				Hashtable hash = new Hashtable();
				hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,idInstitucion);
				hash.put(ScsSaltosCompensacionesBean.C_IDTURNO,idTurno);
				if (idGuardia!=null) {
					hash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,idGuardia);
				}
				hash.put(ScsSaltosCompensacionesBean.C_MOTIVOS,motivo);
				hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA,idPersona);
				hash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,ClsConstants.SALTOS);
				hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,adm.getNuevoIdSaltosTurno(idInstitucion,idTurno));
				hash.put(ScsSaltosCompensacionesBean.C_FECHA,"SYSDATE");
				if (!adm.insert(hash)) {
					throw new ClsExceptions("Error insertando salto: "+ adm.getError());
				}
			}
		}
	}
	
	
	/**
	 * Actualiza el valor de "manual" en la designación según los parámetros obtenidos en la busqueda SJCS
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param anio
	 * @param numero
	 * @param flagSalto
	 * @param flagCompensacion
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void actualizaManualDesigna(String idInstitucion, String idTurno, String idPersona, String anio, String numero, String flagSalto, String flagCompensacion) throws ClsExceptions, SIGAException {
		if (flagSalto!=null && flagCompensacion!=null && flagSalto.equalsIgnoreCase("O") && (flagCompensacion.equalsIgnoreCase("C") || flagCompensacion.equalsIgnoreCase("N"))) {
			ScsDesignasLetradoAdm adm = new ScsDesignasLetradoAdm(this.usrbean);
			Hashtable hash = new Hashtable();
			hash.put(ScsDesignasLetradoBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ScsDesignasLetradoBean.C_IDTURNO,idTurno);
			hash.put(ScsDesignasLetradoBean.C_ANIO,anio);
			hash.put(ScsDesignasLetradoBean.C_NUMERO,numero);
			hash.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersona);
			Vector v = adm.select(hash);
			if (v!=null && v.size()>0) {
				ScsDesignasLetradoBean bean = (ScsDesignasLetradoBean) v.get(0);
				bean.setManual(new Integer(ClsConstants.DB_FALSE));
				if (!adm.updateDirect(bean)) {
					throw new ClsExceptions("Error actualizando campo 'manual' en designa: "+ adm.getError());
				}
			}
		}
	}
	
	
	/**
	 * Crea compensación según los parámetros obtenidos en la busqueda SJCS
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param idPersona
	 * @param checkCompensacion
	 * @param motivo
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void crearCompensacion(String idInstitucion, String idTurno, String idGuardia, String idPersona, String checkCompensacion, String motivo ) throws ClsExceptions, SIGAException {
		if (checkCompensacion!=null) {
			if (checkCompensacion.equals("on") || checkCompensacion.equals("1")) {
				// cuando hay que insertar salto
				ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrbean);
				Hashtable hash = new Hashtable();
				hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,idInstitucion);
				hash.put(ScsSaltosCompensacionesBean.C_IDTURNO,idTurno);
				if (idGuardia!=null) {
					hash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,idGuardia);
				}
				hash.put(ScsSaltosCompensacionesBean.C_MOTIVOS,motivo);
				hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA,idPersona);
				hash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,ClsConstants.COMPENSACIONES);
				hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,adm.getNuevoIdSaltosTurno(idInstitucion,idTurno));
				hash.put(ScsSaltosCompensacionesBean.C_FECHA,"SYSDATE");
				if (!adm.insert(hash)) {
					throw new ClsExceptions("Error insertando compensacion: "+ adm.getError());
				}
			}
		}
	}
	
	
	/**
	 * Tratamiento del último del turno según los parámetros obtenidos en la busqueda SJCS
	 * @param idInstitucion Identificador de la institucion
	 * @param idTurno Identificador del turno
	 * @param idGuardia Identificador de la guardia
	 * @param flagSalto
	 * @param flagCompensacion
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void tratamientoUltimo(String idInstitucion, String idTurno, String idPersona, String flagSalto, String flagCompensacion) throws ClsExceptions, SIGAException {
		
		
		
		
		if (flagSalto!=null && (flagSalto.equals("")||flagSalto.trim().equalsIgnoreCase("0"))) {
			
			ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrbean);
			
			if (flagCompensacion==null) {
				// no hace nada
				
			} else
				
				if (flagCompensacion.equalsIgnoreCase("C")) {
					
					// Cumplimento la compensacion más antigua sin compensar para el turno y letrado
					String consulta="where " + ScsSaltosCompensacionesBean.C_IDINSTITUCION + "=" + idInstitucion +
					" and " + ScsSaltosCompensacionesBean.C_IDTURNO + "=" + idTurno + 
					" and " + ScsSaltosCompensacionesBean.C_IDPERSONA + "=" + idPersona + 
					" and " + ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + "='" + ClsConstants.COMPENSACIONES + "'" + 
					" and " + ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " IS NULL "; 
					
					Vector v = adm.select(consulta);
					if (v!=null && v.size()>0) {
						ScsSaltosCompensacionesBean bean = (ScsSaltosCompensacionesBean) v.get(0);
						bean.setFechaCumplimiento("SYSDATE");
						if (!adm.update(bean)) {
							throw new ClsExceptions("Error cumplimentando compensacion: "+ adm.getError());
						}
					}
					
				} else
					if (flagCompensacion.equalsIgnoreCase("N")||flagCompensacion.equalsIgnoreCase("")) {
						
						// Le pongo el ultimo del turno
						ScsTurnoAdm admT = new ScsTurnoAdm(this.usrbean);
						Hashtable hash = new Hashtable();
						hash.put(ScsTurnoBean.C_IDINSTITUCION,idInstitucion);
						hash.put(ScsTurnoBean.C_IDTURNO,idTurno);
						
						Vector v = admT.select(hash);
						if (v!=null && v.size()>0) {
							ScsTurnoBean bean = (ScsTurnoBean) v.get(0);
							bean.setIdPersonaUltimo(new Long(idPersona));
							
							if (!admT.updateDirect(bean)) {
								
								throw new ClsExceptions("Error actualizando campo 'ultimo' en turno: "+ admT.getError());
							}
							
						}				
					} else
						if (flagCompensacion.equalsIgnoreCase("S")) {
							
							// Pongo la fecha de cumplimento al salto mas antiguo sin cumplimentar para elturno y guardia
							String consulta=" where " + ScsSaltosCompensacionesBean.C_IDINSTITUCION + "=" + idInstitucion +
							" and " + ScsSaltosCompensacionesBean.C_IDTURNO + "=" + idTurno + 
							" and " + ScsSaltosCompensacionesBean.C_IDPERSONA + "=" + idPersona + 
							" and " + ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + "='" + ClsConstants.SALTOS + "'" + 
							" and " + ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " IS NULL ";// + 
							//" order by " + ScsSaltosCompensacionesBean.C_FECHA + " desc "; 
							
							Vector v = adm.select(consulta);
							if (v!=null && v.size()>0) {
								ScsSaltosCompensacionesBean bean = (ScsSaltosCompensacionesBean) v.get(0);
								bean.setFechaCumplimiento("sysdate");
								if (!adm.updateDirect(bean)) {
									throw new ClsExceptions("Error cumplimentando salto: "+ adm.getError());
								}
							}				
						}
		}else{
			;	
		}
			
	}


	
	
}