create or replace function F_SIGA_GET_ULTIMOESTADOPOREJG(P_INSTITUCION IN SCS_EJG.IDINSTITUCION%type,
                                                           P_IDTIPOEJG   IN SCS_EJG.IDTIPOEJG%type,
                                                           P_ANIO        IN SCS_EJG.ANIO%type,
                                                           P_NUMERO      IN SCS_EJG.NUMERO%type)
  return SCS_ESTADOEJG.IDESTADOPOREJG%type is
  /*
  * Descripcion:  Funcion para la obtencion del ultimo estado de un EJG
  *
  * Fecha Creacion: 26/07/2010
  * Autor:          Jacqueline chavez mendez
  */

  v_result SCS_ESTADOEJG.IDESTADOPOREJG%type;

begin

  SELECT IDESTADOPOREJG
    INTO v_result
    FROM (SELECT E.IDESTADOPOREJG
            FROM SCS_ESTADOEJG E
           WHERE E.IDINSTITUCION = P_INSTITUCION
             AND E.IDTIPOEJG = P_IDTIPOEJG
             AND E.ANIO = P_ANIO
             AND E.NUMERO = P_NUMERO
             AND E.FECHABAJA IS NULL
           ORDER BY TRUNC(E.FECHAINICIO) DESC, E.IDESTADOPOREJG DESC)
   WHERE ROWNUM = 1;

  return(v_result);
end F_SIGA_GET_ULTIMOESTADOPOREJG;
/
