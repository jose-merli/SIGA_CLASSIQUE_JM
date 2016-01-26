create or replace function F_SIGA_GET_IDULTIMOESTADOCajg(P_INSTITUCION IN SCS_EJG.IDINSTITUCION%type,
                                                           P_IDTIPOEJG   IN SCS_EJG.IDTIPOEJG%type,
                                                           P_ANIO        IN SCS_EJG.ANIO%type,
                                                           P_NUMERO      IN SCS_EJG.NUMERO%type,
                                                           EXCEPCION     In Scs_Maestroestadosejg.Idestadoejg%Type)
  return SCS_ESTADOEJG.IDESTADOPOREJG%type is

  v_result SCS_ESTADOEJG.IDESTADOPOREJG%type;

begin

  SELECT IDESTADOEJG
    INTO v_result
    FROM (SELECT E.IDESTADOEJG
            FROM SCS_ESTADOEJG E, Scs_Maestroestadosejg MAESTRO
           WHERE E.IDINSTITUCION = P_INSTITUCION
             AND E.IDTIPOEJG = P_IDTIPOEJG
             AND E.ANIO = P_ANIO
             AND E.NUMERO = P_NUMERO
             And E.Idestadoejg = MAESTRO.Idestadoejg
             AND E.FECHABAJA IS Null
             And (MAESTRO.Visiblecomision = '1' Or e.Idestadoejg = EXCEPCION)
           ORDER BY E.FECHAINICIO DESC, E.IDESTADOPOREJG DESC)
   WHERE ROWNUM = 1;

  return(v_result);
end F_SIGA_GET_IDULTIMOESTADOCajg;
 
/
