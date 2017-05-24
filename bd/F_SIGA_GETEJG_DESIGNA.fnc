create or replace function F_SIGA_GETEJG_DESIGNA(P_INSTITUCION IN NUMBER,
                                                 P_IDTURNO     IN NUMBER,
                                                 P_ANIO        IN NUMBER,
                                                 P_NUMERO      IN NUMBER,
                                                 P_TAMANIOEJG  IN NUMBER DEFAULT 5
                                                 )
  return varchar2 is
  /****************************************************************************************************************/
  /* Nombre:       F_SIGA_GETEJG_DESIGNA                                                                 */
  /* Descripcion:   Funcion para la optencion de los delitos que tiene asociado una designa                      */
  /*                                                                                                          */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* INSTITUCION             IN             Codigo de institucion                                 NUMBER(5)      */
  /* NUMERO                  IN             Numero designa                                         NUMBER(5)      */
  /* IDTURNO                 IN             Codigo del turno                                       NUMBER(5)      */
  /* ANIO                    IN             Anio de la designa                                    NUMBER(5)      */
  /*                                                                                                  */
  /* Version:        1.0                                                                              */
  /* Fecha Creacion: 24/01/2008                                                                                   */
  /* Autor:         Ana Combarros                                                                                */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /*   11/05/2015          Jorge Torres                      Formateamos el nueejg                                */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  V_RESULT VARCHAR2(2000);



     cursor c_ejg is SELECT ejg.anio ANIO ,
     LPAD(ejg.numejg,P_TAMANIOEJG,0) NUMEJG,

       ejg.docresolucion DOCRESOLUCION
    ,
     ejg.Numero NUMERO,
     ejg.Idtipoejg IDTIPOEJG,

     ejg.idinstitucion IDINSTITUCION,
     EJG.IDTIPORATIFICACIONEJG IDTIPORATIFICACIONEJG,
    EJG.FECHARESOLUCIONCAJG FECHARESOLUCIONCAJG,
    EJG.IDTIPODICTAMENEJG
      FROM SCS_EJG ejg, SCS_EJGDESIGNA ejgd
     WHERE ejgd.idinstitucion = P_INSTITUCION
       AND ejgd.numerodesigna = P_NUMERO
       AND ejgd.idturno = P_IDTURNO
       AND ejgd.aniodesigna = P_ANIO
       AND ejgd.idinstitucion = ejg.idinstitucion
       AND ejgd.anioejg = EJG.ANIO
       AND ejgd.Numeroejg = EJG.NUMERO
       AND ejgd.idtipoejg = EJG.IDTIPOEJG;

begin

  for i in c_ejg loop
    V_RESULT := V_RESULT || ', ' || i.anio || '/' || i.numejg ||
                '##' ||nvl(to_char(i.docresolucion),' ') || '##' || i.Idinstitucion || '##' ||
                i.anio || '##' || i.idtipoejg || '##' || i.numero || '##' || nvl(to_char(i.Idtiporatificacionejg),' ') || '##' || nvl(to_char(i.fecharesolucioncajg),' ')|| '##'
                || nvl(to_char(i.idtipodictamenejg),' ')
                ;
  end loop;

  V_RESULT := lTrim(V_RESULT, ',');
  return(V_RESULT);
end F_SIGA_GETEJG_DESIGNA;
/