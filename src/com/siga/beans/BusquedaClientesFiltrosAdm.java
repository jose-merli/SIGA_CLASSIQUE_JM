package com.siga.beans;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

public class BusquedaClientesFiltrosAdm {
	
	private UsrBean usrbean = null;
	
	// Variables para implementar los filtros
	protected String nColegiado = null;
	protected String nif = null;
	protected String nombre = null;
	protected String apellido1 = null;
	protected String apellido2 = null;	
	
	
	//
	// Constructores
	//
	/**
	 * Constructor vacio, para las busquedas
	 */
	public BusquedaClientesFiltrosAdm() {
	}
	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public BusquedaClientesFiltrosAdm (UsrBean usuario) {
		this.usrbean=usuario;
	}	
	
	
	//
	// Setters
	//
	/**
	 * Establece los filtros que se aplican sobre la tabla persona
	 * @param nif
	 * @param nombre
	 * @param apellido1
	 * @param apellido2
	 */
	public void setFiltosPersona(String nif, String nombre, String apellido1, String apellido2){
		this.nif=nif;
		this.nombre=nombre;
		this.apellido1=apellido1;
		this.apellido2=apellido2;
	}
	/**
	 * Establece los filtros que se aplican sobre la tabla colegiado
	 * @param nColegiado
	 */
	public void setFiltroNColegiado(String nColegiado){
		this.nColegiado=nColegiado;
	}
	
	
	//
	// Metodos de busqueda
	//
	/**
	 * Busca los letrados de una misma guardia.<br>
	 * Los ordena por cola
	 */
	public Vector buscaLetradosColaGuardia(String idInstitucion, String idTurno, String idGuardia, String fecha, int difRow) throws ClsExceptions{
		List<LetradoGuardia> letradosColaGuardiaList = InscripcionGuardia.getColaGuardia(Integer.valueOf(idInstitucion), Integer.valueOf(idTurno),Integer.valueOf(idGuardia), fecha, fecha, usrbean);
		Vector letradosColaGuardiaVector = new Vector();
		
		for(LetradoGuardia letradoGuardia:letradosColaGuardiaList){
			Row row = new Row();
			Hashtable htRow = new Hashtable();
			htRow.put("N", difRow);
			htRow.put(CenPersonaBean.C_NIFCIF, letradoGuardia.getPersona().getNIFCIF());
			htRow.put(CenColegiadoBean.C_NCOLEGIADO, letradoGuardia.getPersona().getColegiado().getNColegiado());
			htRow.put(CenPersonaBean.C_APELLIDOS1, letradoGuardia.getPersona().getApellido1());
			htRow.put(CenPersonaBean.C_APELLIDOS2, letradoGuardia.getPersona().getApellido2());
			htRow.put(CenPersonaBean.C_NOMBRE, letradoGuardia.getPersona().getNombre());
			htRow.put(CenPersonaBean.C_IDPERSONA, letradoGuardia.getPersona().getIdPersona());
			Hashtable actualizaDatos = buscaInternaDatosInscripcionGuardiaPersona(letradoGuardia.getPersona().getIdPersona().toString(), idInstitucion, idTurno, idGuardia);
			htRow.put("TURNO", (String)actualizaDatos.get("TURNO"));
			htRow.put("GUARDIA", (String)actualizaDatos.get("GUARDIA"));
			htRow.put("POSICION", (String)actualizaDatos.get("POSICION"));
			htRow.put("SALTO", "");
			htRow.put("IDTU", "");
			htRow.put("IDGU", "");
			htRow.put("GUARDIA_SUSTITUCION", "");
			htRow.put("COMPENSACION", "");
			htRow.put("TELEFONO", (String)actualizaDatos.get("TELEFONO"));
			row.setRow(htRow);
			
			letradosColaGuardiaVector.add(row);
		}
		
		return letradosColaGuardiaVector;
	}

	public Hashtable buscaInternaDatosInscripcionGuardiaPersona(String idPersona,
			String idInstitucion,
			String idTurno,
			String idGuardia) throws ClsExceptions
	{
		Hashtable datos = new Hashtable();
		StringBuffer sql = new StringBuffer();
		sql.append("Select Distinct Tur.Nombre Turno, ");
		sql.append("                Gua.Nombre Guardia, ");
		sql.append("                (Select Count(*) ");
		sql.append("                   From Scs_Cabeceraguardias Cab ");
		sql.append("                  Where Cab.Idinstitucion = Ins.Idinstitucion ");
		sql.append("                    And Cab.Idpersona = Ins.Idpersona ");
		sql.append("                    And Cab.Idturno = Ins.Idturno ");
		sql.append("                    And Cab.Idguardia = Ins.Idguardia ");
		sql.append("                    And Cab.Fecha_Fin >= Sysdate) Posicion, ");
		sql.append("                (Select Dir.Telefono1 ");
		sql.append("                   From Cen_Direccion_Tipodireccion Tip, Cen_Direcciones Dir ");
		sql.append("                  Where Dir.Idinstitucion = Tip.Idinstitucion ");
		sql.append("                    And Dir.Idpersona = Tip.Idpersona ");
		sql.append("                    And Dir.Iddireccion = Tip.Iddireccion ");
		sql.append("                    And Dir.Idinstitucion = Ins.Idinstitucion ");
		sql.append("                    And Dir.Idpersona = Ins.Idpersona ");
		sql.append("                    And Tip.Idtipodireccion = " + ClsConstants.TIPO_DIRECCION_GUARDIA);
		sql.append("                    And Dir.Fechabaja Is Null ");
		sql.append("                    And Rownum = 1) Telefono ");
		sql.append("  From Scs_Inscripcionguardia Ins, Scs_Turno Tur, Scs_Guardiasturno Gua ");
		sql.append(" Where Ins.Idinstitucion = Gua.Idinstitucion ");
		sql.append("   And Ins.Idturno = Gua.Idturno ");
		sql.append("   And Ins.Idguardia = Gua.Idguardia ");
		sql.append("   And Gua.Idinstitucion = Tur.Idinstitucion ");
		sql.append("   And Gua.Idturno = Tur.Idturno ");
		sql.append("   And Ins.Idinstitucion = " + idInstitucion);
		sql.append("   And Ins.Idturno = " + idTurno);
		sql.append("   And Ins.Idguardia = " + idGuardia);
		sql.append("   And Ins.Idpersona = " + idPersona);

		try {
			Vector datosVector = this.find(sql.toString());
			if (datosVector != null && datosVector.size() > 0)
				datos = ((Row) datosVector.get(0)).getRow();
			else
				throw new ClsExceptions("Error en buscaInternaDatosInscripcionGuardiaPersona: ");

		} catch (ClsExceptions e1) {
			throw new ClsExceptions("Error en buscaInternaDatosInscripcionGuardiaPersona: " + e1.getLocalizedMessage());
		}
		return datos;
	}
	
	/**
	 * Busca los letrados de un mismo turno.<br>
	 * Los ordena por cola
	 */
	public Vector buscaLetradosColaTurno(String idInstitucion, String idTurno, String fecha, int idFiltro) throws ClsExceptions
	{
		int difRow = 0;
		List<LetradoGuardia> letradosColaTurnoList = InscripcionTurno.getColaTurno(Integer.valueOf(idInstitucion),
				Integer.valueOf(idTurno), fecha, false, usrbean);
		Vector letradosColaTurnoVector = new Vector();

		for (LetradoGuardia letradoTurno : letradosColaTurnoList) {
			Row row = new Row();
			Hashtable htRow = new Hashtable();
			htRow.put("N", difRow);
			htRow.put(CenPersonaBean.C_NIFCIF, letradoTurno.getPersona().getNIFCIF());
			htRow.put(CenColegiadoBean.C_NCOLEGIADO, letradoTurno.getPersona().getColegiado().getNColegiado());
			htRow.put(CenPersonaBean.C_APELLIDOS1, letradoTurno.getPersona().getApellido1());
			htRow.put(CenPersonaBean.C_APELLIDOS2, letradoTurno.getPersona().getApellido2());
			htRow.put(CenPersonaBean.C_NOMBRE, letradoTurno.getPersona().getNombre());
			htRow.put(CenPersonaBean.C_IDPERSONA, letradoTurno.getPersona().getIdPersona());
			if (idFiltro == 3) {
				Hashtable actualizaDatos = buscaInternaDatosInscripcionTurnoPersona(letradoTurno.getPersona().getIdPersona().toString(),
						idInstitucion, idTurno);
				htRow.put("TURNO", (String) actualizaDatos.get("TURNO"));
				htRow.put("TELEFONO", (String) actualizaDatos.get("TELEFONO"));
			} else {
				htRow.put("TURNO", "");
				htRow.put("TELEFONO", "");
			}
			htRow.put("GUARDIA", "");
			htRow.put("POSICION", "");
			htRow.put("SALTO", "");
			htRow.put("IDTU", "");
			htRow.put("IDGU", "");
			htRow.put("GUARDIA_SUSTITUCION", "");
			htRow.put("COMPENSACION", "");

			row.setRow(htRow);

			letradosColaTurnoVector.add(row);
		}

		return letradosColaTurnoVector;
	}

	private Hashtable buscaInternaDatosInscripcionTurnoPersona(String idPersona, String idInstitucion, String idTurno) throws ClsExceptions
	{
		Hashtable datos = new Hashtable();
		StringBuffer sql = new StringBuffer();
		sql.append("Select Distinct Tur.Nombre Turno, ");
		sql.append("                (Select Dir.Telefono1 ");
		sql.append("                   From Cen_Direccion_Tipodireccion Tip, Cen_Direcciones Dir ");
		sql.append("                  Where Dir.Idinstitucion = Tip.Idinstitucion ");
		sql.append("                    And Dir.Idpersona = Tip.Idpersona ");
		sql.append("                    And Dir.Iddireccion = Tip.Iddireccion ");
		sql.append("                    And Dir.Idinstitucion = Ins.Idinstitucion ");
		sql.append("                    And Dir.Idpersona = Ins.Idpersona ");
		sql.append("                    And Tip.Idtipodireccion = " + ClsConstants.TIPO_DIRECCION_GUARDIA);
		sql.append("                    And Dir.Fechabaja Is Null ");
		sql.append("                    And Rownum = 1) Telefono ");
		sql.append("  From Scs_Inscripcionturno Ins, Scs_Turno Tur ");
		sql.append(" Where Ins.Idinstitucion = Tur.Idinstitucion ");
		sql.append("   And Ins.Idturno = Tur.Idturno ");
		sql.append("   And Ins.Idinstitucion = " + idInstitucion);
		sql.append("   And Ins.Idturno = " + idTurno);
		sql.append("   And Ins.Idpersona = " + idPersona);

		try {
			Vector datosVector = this.find(sql.toString());
			if (datosVector != null && datosVector.size() > 0)
				datos = ((Row) datosVector.get(0)).getRow();
			else
				throw new ClsExceptions("Error en buscaInternaDatosInscripcionTurnoPersona: ");

		} catch (ClsExceptions e1) {
			// throw new ClsExceptions (e, "Error al obtener la informacion de direcciones: "+e.getLocalizedMessage());
			throw new ClsExceptions("Error en buscaInternaDatosInscripcionTurnoPersona: "
					+ e1.getLocalizedMessage());
		}
		return datos;
	}
	
	/**
	 * Busca todos los letrados del censo que esten inscritos en guardia dada la fecha:<br>
	 * Ordenados por orden alfabetico
	 * Si se indica turno y/o guardia, entonces filtrara por ellos
	 */
	public Vector buscaLetradosInscritosGuardia(String idInstitucion, String idTurno, String idGuardia, String fecha) throws ClsExceptions
	{
		StringBuffer sql = new StringBuffer();
		sql.append("Select 0 n, x.* ");
		sql.append("  From (Select Per.Idpersona, ");
		sql.append("               Per.Nifcif, ");
		sql.append("               f_Siga_Calculoncolegiado(Col.Idinstitucion, Col.Idpersona) Ncolegiado, ");
		sql.append("               Per.Nombre, ");
		sql.append("               Per.Apellidos1, ");
		sql.append("               Per.Apellidos2, ");
		sql.append("               Tur.Nombre Turno, ");
		sql.append("               Gua.Nombre Guardia, ");
//		sql.append("               '' Posicion, ");		
		sql.append("               (SELECT COUNT(*)");
		sql.append("               	  FROM Scs_Cabeceraguardias Cab");
		sql.append("                  WHERE Cab.Idinstitucion = Ins.Idinstitucion");
		sql.append("           	   		AND Cab.Idpersona = Ins.Idpersona");
		sql.append("               		AND Cab.Idturno = Ins.Idturno");
		sql.append("          			AND Cab.Idguardia = Ins.Idguardia");
		sql.append("          			AND Cab.Fecha_Fin >= SYSDATE) Posicion,");
		sql.append("               '0' Salto, ");
		sql.append("               '' Compensacion, ");
		sql.append("               (Select Telefono1 ");
		sql.append("                  From Cen_Direccion_Tipodireccion Tip, Cen_Direcciones Dir ");
		sql.append("                 Where Dir.Idinstitucion = Tip.Idinstitucion ");
		sql.append("                   And Dir.Idpersona = Tip.Idpersona ");
		sql.append("                   And Dir.Iddireccion = Tip.Iddireccion ");
		sql.append("                   And Dir.Idinstitucion = Col.Idinstitucion ");
		sql.append("                   And Dir.Idpersona = Col.Idpersona ");
		sql.append("                   And Tip.Idtipodireccion = 6 ");
		sql.append("                   And Dir.Fechabaja Is Null ");
		sql.append("                   And Rownum = 1) Telefono ");
		sql.append("          From "+this.getTablaPersona()+"   Per, ");
		sql.append("               "+this.getTablaColegiado()+" Col, ");
		sql.append("               Cen_Datoscolegialesestado    Est, ");
		sql.append("               Scs_Inscripcionguardia       Ins, ");
		sql.append("               Scs_Turno                    Tur, ");
		sql.append("               Scs_Guardiasturno            Gua ");
		sql.append("         Where Per.Idpersona = Col.Idpersona ");
		sql.append("           And Col.Idpersona = Col.Idpersona ");
		sql.append("           And Col.Idinstitucion = "+idInstitucion+" ");
		
		sql.append("           And Col.Idinstitucion = Est.Idinstitucion ");
		sql.append("           And Col.Idpersona = Est.Idpersona ");
		sql.append("           And Est.Fechaestado = ");
		sql.append("               (Select Max(Est2.Fechaestado) ");
		sql.append("                  From Cen_Datoscolegialesestado Est2 ");
		sql.append("                 Where Est2.Idinstitucion = Est.Idinstitucion ");
		sql.append("                   And Est2.Idpersona = Est.Idpersona ");
		sql.append("                   And Trunc(Est2.Fechaestado) <= '"+fecha+"') ");
		sql.append("           And Est.Idestado = 20 ");
		
		sql.append("           And Ins.Idinstitucion = Col.Idinstitucion ");
		sql.append("           And Ins.Idpersona = Col.Idpersona ");
		sql.append("           And Ins.Idinstitucion = Gua.Idinstitucion ");
		sql.append("           And Ins.Idturno = Gua.Idturno ");
		sql.append("           And Ins.Idguardia = Gua.Idguardia ");
		sql.append("           And Gua.Idinstitucion = Tur.Idinstitucion ");
		sql.append("           And Gua.Idturno = Tur.Idturno ");
		sql.append("           And Ins.Fechavalidacion Is Not Null ");
		sql.append("           And Trunc(Ins.Fechavalidacion) <= '"+fecha+"' ");
		sql.append("           And (Ins.Fechabaja Is Null Or ");
		sql.append("               Trunc(Ins.Fechabaja) > '"+fecha+"') ");
		if (idTurno != null && !idTurno.equals(""))
			sql.append("           And Ins.Idturno = "+idTurno+" ");
		if (idGuardia != null && !idGuardia.equals(""))
			sql.append("           And Ins.Idguardia = "+idGuardia+" ");

		sql.append("         Order By Per.Apellidos1 || ' ' || Per.Apellidos2 || ', ' || ");
		sql.append("                  Per.Nombre) x ");

		Vector letradosCensoCompletoVector = findNLS(sql.toString());
		return letradosCensoCompletoVector;
	}
	
	/**
	 * Busca todos los letrados del censo que esten inscritos en turno dada la fecha:<br>
	 * Ordenados por orden alfabetico
	 * Si se indica turno y/o guardia, entonces filtrara por ellos
	 */
	public Vector buscaLetradosInscritosTurno(String idInstitucion, String idTurno, String fecha) throws ClsExceptions
	{
		StringBuffer sql = new StringBuffer();
		sql.append("Select 0 n, x.* ");
		sql.append("  From (Select Per.Idpersona, ");
		sql.append("               Per.Nifcif, ");
		sql.append("               f_Siga_Calculoncolegiado(Col.Idinstitucion, Col.Idpersona) Ncolegiado, ");
		sql.append("               Per.Nombre, ");
		sql.append("               Per.Apellidos1, ");
		sql.append("               Per.Apellidos2, ");
		sql.append("               Tur.Nombre Turno, ");
		sql.append("               '' Guardia, ");
		sql.append("               '' Posicion, ");
		sql.append("               '0' Salto, ");
		sql.append("               '' Compensacion, ");
		sql.append("               (Select Telefono1 ");
		sql.append("                  From Cen_Direccion_Tipodireccion Tip, Cen_Direcciones Dir ");
		sql.append("                 Where Dir.Idinstitucion = Tip.Idinstitucion ");
		sql.append("                   And Dir.Idpersona = Tip.Idpersona ");
		sql.append("                   And Dir.Iddireccion = Tip.Iddireccion ");
		sql.append("                   And Dir.Idinstitucion = Col.Idinstitucion ");
		sql.append("                   And Dir.Idpersona = Col.Idpersona ");
		sql.append("                   And Tip.Idtipodireccion = 6 ");
		sql.append("                   And Dir.Fechabaja Is Null ");
		sql.append("                   And Rownum = 1) Telefono ");
		sql.append("          From "+this.getTablaPersona()+"   Per, ");
		sql.append("               "+this.getTablaColegiado()+" Col, ");
		sql.append("               Cen_Datoscolegialesestado    Est, ");
		sql.append("               Scs_Inscripcionturno         Ins, ");
		sql.append("               Scs_Turno                    Tur ");
		sql.append("         Where Per.Idpersona = Col.Idpersona ");
		sql.append("           And Col.Idpersona = Col.Idpersona ");
		sql.append("           And Col.Idinstitucion = "+idInstitucion+" ");
		
		sql.append("           And Col.Idinstitucion = Est.Idinstitucion ");
		sql.append("           And Col.Idpersona = Est.Idpersona ");
		sql.append("           And Est.Fechaestado = ");
		sql.append("               (Select Max(Est2.Fechaestado) ");
		sql.append("                  From Cen_Datoscolegialesestado Est2 ");
		sql.append("                 Where Est2.Idinstitucion = Est.Idinstitucion ");
		sql.append("                   And Est2.Idpersona = Est.Idpersona ");
		sql.append("                   And Trunc(Est2.Fechaestado) <= '"+fecha+"') ");
		sql.append("           And Est.Idestado = 20 ");
		
		sql.append("           And Ins.Idinstitucion = Col.Idinstitucion ");
		sql.append("           And Ins.Idpersona = Col.Idpersona ");
		sql.append("           And Ins.Idinstitucion = Tur.Idinstitucion ");
		sql.append("           And Ins.Idturno = Tur.Idturno ");
		sql.append("           And Ins.Fechavalidacion Is Not Null ");
		sql.append("           And Trunc(Ins.Fechavalidacion) <= '"+fecha+"' ");
		sql.append("           And (Ins.Fechabaja Is Null Or ");
		sql.append("               Trunc(Ins.Fechabaja) > '"+fecha+"') ");
		if (idTurno != null && !idTurno.equals(""))
			sql.append("           And Ins.Idturno = "+idTurno+" ");

		sql.append("         Order By Per.Apellidos1 || ' ' || Per.Apellidos2 || ', ' || ");
		sql.append("                  Per.Nombre) x ");

		Vector letradosCensoCompletoVector = findNLS(sql.toString());
		return letradosCensoCompletoVector;
	}
	
	/**
	 * Busca todos los letrados del censo que sean ejercientes dada la fecha:<br>
	 * Ordenados por orden alfabetico
	 */
	public Vector buscaLetradosCensoCompleto(String idInstitucion, String fecha) throws ClsExceptions
	{
		StringBuffer sql = new StringBuffer();
		sql.append("Select 0 n, x.* ");
		sql.append("  From (Select Per.Idpersona, ");
		sql.append("               Per.Nifcif, ");
		sql.append("               f_Siga_Calculoncolegiado(Col.Idinstitucion, Col.Idpersona) Ncolegiado, ");
		sql.append("               Per.Nombre, ");
		sql.append("               Per.Apellidos1, ");
		sql.append("               Per.Apellidos2, ");
		sql.append("               '' Turno, ");
		sql.append("               '' Guardia, ");
		sql.append("               '' Posicion, ");
		sql.append("               '0' Salto, ");
		sql.append("               '' Compensacion, ");
		sql.append("               (Select Telefono1 ");
		sql.append("                  From Cen_Direccion_Tipodireccion Tip, Cen_Direcciones Dir ");
		sql.append("                 Where Dir.Idinstitucion = Tip.Idinstitucion ");
		sql.append("                   And Dir.Idpersona = Tip.Idpersona ");
		sql.append("                   And Dir.Iddireccion = Tip.Iddireccion ");
		sql.append("                   And Dir.Idinstitucion = Col.Idinstitucion ");
		sql.append("                   And Dir.Idpersona = Col.Idpersona ");
		sql.append("                   And Tip.Idtipodireccion = 6 ");
		sql.append("                   And Dir.Fechabaja Is Null ");
		sql.append("                   And Rownum = 1) Telefono ");
		sql.append("          From "+this.getTablaPersona()+"   Per, ");
		sql.append("               "+this.getTablaColegiado()+" Col, ");
		sql.append("               Cen_Datoscolegialesestado    Est ");
		sql.append("         Where Per.Idpersona = Col.Idpersona ");
		sql.append("           And Col.Idpersona = Col.Idpersona ");
		sql.append("           And Col.Idinstitucion = "+idInstitucion+" ");
		sql.append("           And Col.Idinstitucion = Est.Idinstitucion ");
		sql.append("           And Col.Idpersona = Est.Idpersona ");
		sql.append("           And Est.Fechaestado = ");
		sql.append("               (Select Max(Est2.Fechaestado) ");
		sql.append("                  From Cen_Datoscolegialesestado Est2 ");
		sql.append("                 Where Est2.Idinstitucion = Est.Idinstitucion ");
		sql.append("                   And Est2.Idpersona = Est.Idpersona ");
		sql.append("                   And Trunc(Est2.Fechaestado) <= '"+fecha+"') ");
		sql.append("           And Est.Idestado = 20 ");
		sql.append("         Order By Per.Apellidos1 || ' ' || Per.Apellidos2 || ', ' || ");
		sql.append("                  Per.Nombre) x ");

		Vector letradosCensoCompletoVector = findNLS(sql.toString());
		return letradosCensoCompletoVector;
	}
	
	/**
	 * Crea una sentencia sql para ser utilizada como tabla persona dentro de una join.<br>
	 * Tiene en cuenta los filtros que pudieron ser insertados previamente en el metodo setFiltosPersona
	 * 
	 * @return sentencia sql
	 */
	protected String getTablaPersona()
	{
		String filtro = "";
		String tablaPersona;

		if (nombre != null && !nombre.trim().equals("")) {
			filtro += (filtro.equals("") ? "" : " and ");
			filtro += "(" + ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(), CenPersonaBean.C_NOMBRE) + ")";
		}

		if (apellido1 != null && !apellido1.trim().equals("")) {
			filtro += (filtro.equals("") ? "" : " and ");
			filtro += "(" + ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(), CenPersonaBean.C_APELLIDOS1) + ")";
		}

		if (apellido2 != null && !apellido2.trim().equals("")) {
			filtro += (filtro.equals("") ? "" : " and ");
			filtro += "(" + ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(), CenPersonaBean.C_APELLIDOS2) + ")";
		}
		
		// Consulta sobre el campo NIF/CIF:
		// Si el usuario mete comodines la busqueda es como se hacia siempre.
		// En el caso de no meter comodines, se ha creado un nuevo metodo (ComodinBusquedas.prepararSentenciaNIF) para
		// que monte la consulta adecuada.
		if (nif != null && !nif.trim().equals("")) {
			filtro += (filtro.equals("") ? "" : " and ");
			filtro += (ComodinBusquedas.hasComodin(nif) ? 
					"" + ComodinBusquedas.prepararSentenciaCompleta(nif.trim(), CenPersonaBean.C_NIFCIF)
					: ComodinBusquedas.prepararSentenciaNIF(nif, "upper(" + CenPersonaBean.C_NIFCIF + ")"));
		}

		if (filtro.equals("")) {
			tablaPersona = CenPersonaBean.T_NOMBRETABLA;
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("(");
			sql.append("select " + CenPersonaBean.C_IDPERSONA + ", ");
			sql.append("       " + CenPersonaBean.C_NOMBRE + ", ");
			sql.append("       " + CenPersonaBean.C_APELLIDOS1 + ", ");
			sql.append("       " + CenPersonaBean.C_APELLIDOS2 + ", ");
			sql.append("       " + CenPersonaBean.C_NIFCIF + " ");
			sql.append("  from " + CenPersonaBean.T_NOMBRETABLA + " ");
			sql.append(" where " + filtro + " ");
			sql.append(")");
			tablaPersona = sql.toString();
		}
		return tablaPersona;
	}
	
	/**
	 * Crea una sentencia sql para ser utilizada como tabla colegiado dentro de una join.<br>
	 * Tiene en cuenta el filtro que pudo ser insertado previamente en el metodo setFiltoColegiado
	 * 
	 * @return sentencia sql
	 */
	protected String getTablaColegiado()
	{
		String filtro = "";
		String tablaColegiado;

		if (this.nColegiado != null && !this.nColegiado.trim().equals("")) {
			filtro = ComodinBusquedas.tratarNumeroColegiado(this.nColegiado, "F_SIGA_CALCULONCOLEGIADO("
					+ CenColegiadoBean.C_IDINSTITUCION + "," + CenColegiadoBean.C_IDPERSONA + ") ");
		}

		if (filtro.equals("")) {
			tablaColegiado = CenColegiadoBean.T_NOMBRETABLA;
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("(");
			sql.append("select " + CenColegiadoBean.C_IDINSTITUCION + ", ");
			sql.append("       " + CenColegiadoBean.C_IDPERSONA + ", ");
			sql.append("       " + CenColegiadoBean.C_SITUACIONEJERCICIO + ", ");
			sql.append("       F_SIGA_CALCULONCOLEGIADO(" + CenColegiadoBean.C_IDINSTITUCION + ", "
					+ CenColegiadoBean.C_IDPERSONA + ") " + ", ");
			sql.append("       " + CenColegiadoBean.C_NCOLEGIADO + " ");
			sql.append("  from " + CenColegiadoBean.T_NOMBRETABLA + " ");
			sql.append(" where " + filtro + " ");
			sql.append(")");
			tablaColegiado = sql.toString();
		}
		return tablaColegiado;
	}	
	
	
	//
	// Otros Metodos
	//
	public synchronized LetradoGuardia gestionaDesignacionesAutomaticas(String idInstitucion,
			String idTurno,
			String fecha) throws ClsExceptions, SIGAException
	{

		String saltoocompensacion = null;
		String idSaltosTurno;
		ScsSaltosCompensacionesAdm saltocompAdm = new ScsSaltosCompensacionesAdm(usrbean);
		try {
			// busco las compensaciones
			Vector<LetradoGuardia> letradosCompensacionesVector = saltocompAdm.getLetradosSaltosCompensacionesTurno(
					idInstitucion, idTurno, fecha);

			if (letradosCompensacionesVector != null && letradosCompensacionesVector.size() > 0) {
				for (LetradoGuardia letradoTurno : letradosCompensacionesVector) {
					// letradoGuardia = (Row)compensacionesSaltos.get(i);
					saltoocompensacion = letradoTurno.getSaltoCompensacion();

					if (ClsConstants.COMPENSACIONES.equals(saltoocompensacion)) {
						idSaltosTurno = letradoTurno.getIdSaltoCompensacion();

						CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usrbean);
						// comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
						Map<String, CenBajasTemporalesBean> mBajasTemporales = bajasTemporalescioneAdm
								.getDiasBajaTemporal(letradoTurno.getIdPersona(), new Integer(idInstitucion));
						if (mBajasTemporales.containsKey(fecha)) {
							ScsSaltosCompensacionesBean salto = new ScsSaltosCompensacionesBean(new Integer(
									idInstitucion), new Integer(idTurno), letradoTurno.getIdPersona(),
									ClsConstants.SALTOS, "sysdate");
							ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(
									this.usrbean);
							CenBajasTemporalesBean bajaTemporal = (CenBajasTemporalesBean) mBajasTemporales.get(fecha);
							// bajaTemporal.setDescripcion(bajaTemporal.getDescripcion()+" al crear designa para el "+fecha+" ");
							scsSaltosCompensacionesAdm.insertarSaltoPorBajaTemporal(bajaTemporal, salto);
							continue;

						}

						updateCompensacion(idInstitucion, idTurno, idSaltosTurno);

						return letradoTurno;
					}
				}
			}

			List<LetradoGuardia> colaLetradosTurnoList = InscripcionTurno.getColaTurno(Integer.valueOf(idInstitucion),
					Integer.valueOf(idTurno), fecha, false, usrbean);
			if (colaLetradosTurnoList != null && colaLetradosTurnoList.size() > 0) {
				int i = 0;
				for (LetradoGuardia letradoTurno : colaLetradosTurnoList) {
					Long idPersona = letradoTurno.getIdPersona();

					CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usrbean);
					// comprobamos que el letrado no esta de vacaciones
					Map<String, CenBajasTemporalesBean> mBajasTemporales = bajasTemporalescioneAdm.getDiasBajaTemporal(
							new Long(idPersona), new Integer(idInstitucion));
					if (mBajasTemporales.containsKey(fecha)) {
						ScsSaltosCompensacionesBean salto = new ScsSaltosCompensacionesBean(new Integer(idInstitucion),
								new Integer(idTurno), letradoTurno.getIdPersona(), ClsConstants.SALTOS, "sysdate");
						ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(
								this.usrbean);
						CenBajasTemporalesBean bajaTemporal = (CenBajasTemporalesBean) mBajasTemporales.get(fecha);
						// bajaTemporal.setDescripcion(bajaTemporal.getDescripcion()+" al crear designa para el "+fecha+" ");
						scsSaltosCompensacionesAdm.insertarSaltoPorBajaTemporal(bajaTemporal, salto);
						continue;
					}

					idSaltosTurno = getIdSaltoTurno(letradosCompensacionesVector, idPersona.toString());
					// se buscan los saltos
					if (idSaltosTurno != null) {
						updateCompensacion(idInstitucion, idTurno, idSaltosTurno);
						continue;
					} else {
						return letradoTurno;
					}

				}
			}

			throw new SIGAException("messages.designa.colaVacia");

		} catch (ClsExceptions ex) {
			throw ex;
		}

	}	
	
	private String getIdSaltoTurno(Vector compensacionesSaltos, String idPersona)
	{
		String idSaltoTurno = null;
		if (compensacionesSaltos != null) {
			for (int i = 0; i < compensacionesSaltos.size(); i++) {
				LetradoGuardia letradoTurno = (LetradoGuardia) compensacionesSaltos.get(i);
				Long idPersonaSC = letradoTurno.getIdPersona();
				String salto = letradoTurno.getSaltoCompensacion();
				if (idPersona.toString().equals(idPersonaSC.toString())) {
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
	 * Actualiza el valor de "manual" en la designación según los parámetros obtenidos en la busqueda SJCS
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param anio
	 * @param numero
	 * @param flagSalto
	 * @param flagCompensacion
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void actualizaManualDesigna(String idInstitucion,
			String idTurno,
			String idPersona,
			String anio,
			String numero,
			String flagSalto,
			String flagCompensacion) throws ClsExceptions, SIGAException
	{
		if (flagSalto != null && flagCompensacion != null && flagSalto.equalsIgnoreCase("O")
				&& (flagCompensacion.equalsIgnoreCase("C") || flagCompensacion.equalsIgnoreCase("N"))) {
			ScsDesignasLetradoAdm adm = new ScsDesignasLetradoAdm(this.usrbean);
			Hashtable hash = new Hashtable();
			hash.put(ScsDesignasLetradoBean.C_IDINSTITUCION, idInstitucion);
			hash.put(ScsDesignasLetradoBean.C_IDTURNO, idTurno);
			hash.put(ScsDesignasLetradoBean.C_ANIO, anio);
			hash.put(ScsDesignasLetradoBean.C_NUMERO, numero);
			hash.put(ScsDesignasLetradoBean.C_IDPERSONA, idPersona);
			Vector v = adm.select(hash);
			if (v != null && v.size() > 0) {
				ScsDesignasLetradoBean bean = (ScsDesignasLetradoBean) v.get(0);
				bean.setManual(new Integer(ClsConstants.DB_FALSE));
				if (!adm.updateDirect(bean)) {
					throw new ClsExceptions("Error actualizando campo 'manual' en designa: " + adm.getError());
				}
			}
		}
	}

	/**
	 * Crea salto según los parámetros obtenidos en la busqueda SJCS
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idPersona
	 * @param checkSalto
	 * @param motivo
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void crearSalto(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idPersona,
			String checkSalto,
			String motivo) throws ClsExceptions, SIGAException
	{

		if (checkSalto != null) {
			if (checkSalto.equals("on") || checkSalto.equals("1")) {
				// cuando hay que insertar salto
				ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrbean);
				Hashtable hash = new Hashtable();
				hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, idInstitucion);
				hash.put(ScsSaltosCompensacionesBean.C_IDTURNO, idTurno);
				if (idGuardia != null) {
					hash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA, idGuardia);
				}
				hash.put(ScsSaltosCompensacionesBean.C_MOTIVOS, motivo);
				hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA, idPersona);
				hash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, ClsConstants.SALTOS);
				hash
						.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, adm.getNuevoIdSaltosTurno(idInstitucion,
								idTurno));
				hash.put(ScsSaltosCompensacionesBean.C_FECHA, "SYSDATE");
				if (!adm.insert(hash)) {
					throw new ClsExceptions("Error insertando salto: " + adm.getError());
				}
			}
		}
	}

	/**
	 * Crea compensación según los parámetros obtenidos en la busqueda SJCS
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idPersona
	 * @param checkCompensacion
	 * @param motivo
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void crearCompensacion(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idPersona,
			String checkCompensacion,
			String motivo) throws ClsExceptions, SIGAException
	{
		if (checkCompensacion != null) {
			if (checkCompensacion.equals("on") || checkCompensacion.equals("1")) {
				// cuando hay que insertar salto
				ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrbean);
				Hashtable hash = new Hashtable();
				hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, idInstitucion);
				hash.put(ScsSaltosCompensacionesBean.C_IDTURNO, idTurno);
				if (idGuardia != null) {
					hash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA, idGuardia);
				}
				hash.put(ScsSaltosCompensacionesBean.C_MOTIVOS, motivo);
				hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA, idPersona);
				hash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, ClsConstants.COMPENSACIONES);
				hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, adm.getNuevoIdSaltosTurno(idInstitucion, idTurno));
				hash.put(ScsSaltosCompensacionesBean.C_FECHA, "SYSDATE");
				if (!adm.insert(hash))
					throw new ClsExceptions("Error insertando compensacion: " + adm.getError());
			}
		}
	}	
	
	/**
	 * Tratamiento del último del turno según los parámetros obtenidos en la busqueda SJCS
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param flagSalto
	 * @param flagCompensacion
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void tratamientoUltimo(String idInstitucion,
			String idTurno,
			String idPersona,
			String flagSalto,
			String flagCompensacion) throws ClsExceptions, SIGAException
	{

		if (flagSalto != null && (flagSalto.equals("") || flagSalto.trim().equalsIgnoreCase("0"))) {

			ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrbean);

			if (flagCompensacion == null) {
				// no hace nada

			} else

			if (flagCompensacion.equalsIgnoreCase("C")) {

				// Cumplimento la compensacion más antigua sin compensar para el turno y letrado
				String consulta = "where " + ScsSaltosCompensacionesBean.C_IDINSTITUCION + "=" + idInstitucion
						+ " and " + ScsSaltosCompensacionesBean.C_IDTURNO + "=" + idTurno + " and "
						+ ScsSaltosCompensacionesBean.C_IDPERSONA + "=" + idPersona + " and "
						+ ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + "='" + ClsConstants.COMPENSACIONES + "'"
						+ " and " + ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " IS NULL ";

				Vector v = adm.select(consulta);
				if (v != null && v.size() > 0) {
					ScsSaltosCompensacionesBean bean = (ScsSaltosCompensacionesBean) v.get(0);
					bean.setFechaCumplimiento("SYSDATE");
					if (!adm.update(bean)) {
						throw new ClsExceptions("Error cumplimentando compensacion: " + adm.getError());
					}
				}

			} else if (flagCompensacion.equalsIgnoreCase("N") || flagCompensacion.equalsIgnoreCase("")) {

				// Le pongo el ultimo del turno
				ScsTurnoAdm admT = new ScsTurnoAdm(this.usrbean);
				Hashtable hash = new Hashtable();
				hash.put(ScsTurnoBean.C_IDINSTITUCION, idInstitucion);
				hash.put(ScsTurnoBean.C_IDTURNO, idTurno);

				Vector v = admT.select(hash);
				if (v != null && v.size() > 0) {
					ScsTurnoBean bean = (ScsTurnoBean) v.get(0);
					bean.setIdPersonaUltimo(new Long(idPersona));

					if (!admT.updateDirect(bean)) {

						throw new ClsExceptions("Error actualizando campo 'ultimo' en turno: " + admT.getError());
					}

				}
			} else if (flagCompensacion.equalsIgnoreCase("S")) {

				// Pongo la fecha de cumplimento al salto mas antiguo sin cumplimentar para elturno y guardia
				String consulta = " where " + ScsSaltosCompensacionesBean.C_IDINSTITUCION + "=" + idInstitucion
						+ " and " + ScsSaltosCompensacionesBean.C_IDTURNO + "=" + idTurno + " and "
						+ ScsSaltosCompensacionesBean.C_IDPERSONA + "=" + idPersona + " and "
						+ ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + "='" + ClsConstants.SALTOS + "'" + " and "
						+ ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " IS NULL ";// +
				// " order by " + ScsSaltosCompensacionesBean.C_FECHA + " desc ";

				Vector v = adm.select(consulta);
				if (v != null && v.size() > 0) {
					ScsSaltosCompensacionesBean bean = (ScsSaltosCompensacionesBean) v.get(0);
					bean.setFechaCumplimiento("sysdate");
					if (!adm.updateDirect(bean)) {
						throw new ClsExceptions("Error cumplimentando salto: " + adm.getError());
					}
				}
			}
		}

	}
	
	
	//
	// Metodos de ejecucion de consultas genericas
	//
	protected Vector find(String sql) throws ClsExceptions
	{
		RowsContainer rc = new RowsContainer();
		rc.find(sql);
		return rc.getAll();
	}
	protected Vector findNLS(String sql) throws ClsExceptions
	{
		RowsContainer rc = new RowsContainer();
		rc.findNLS(sql);
		return rc.getAll();
	}
	
}