CREATE OR REPLACE package PKG_SIGA_FACTURACION_SJCS is

  -- Public variable declarations

  -- CODIGO DE ERROR EN CASO DE FACTURADO
  C_FACTURADO constant VARCHAR2(10) := TO_CHAR('F');

  V_CODRETORNO VARCHAR2(10) := TO_CHAR(0); /* Codigo de error Oracle */
  V_DATOSERROR VARCHAR2(200) := NULL; /* Mensaje de error Oracle */

  /* Declaracion de excepciones */
  E_ERROR EXCEPTION;
  E_ERROR_DATOS EXCEPTION;

  /***************************************************/
  /* ARRAYS GLOBALES PARA LA FACTURACION DE GUARDIAS */
  /***************************************************/

  /* Matriz de apuntes de CABECERA DE GUARDIA */
  TYPE MATRICE_CG IS RECORD(
    IDINSTITUCION        SCS_CABECERAGUARDIAS.IDINSTITUCION%TYPE,
    IDTURNO              SCS_CABECERAGUARDIAS.IDTURNO%TYPE,
    IDGUARDIA            SCS_CABECERAGUARDIAS.IDGUARDIA%TYPE,
    IDPERSONA            SCS_CABECERAGUARDIAS.IDPERSONA%TYPE,
    FECHAINICIO          SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE,
    FECHAFIN             SCS_CABECERAGUARDIAS.FECHA_FIN%TYPE,
    FACTURADO            SCS_GUARDIASCOLEGIADO.FACTURADO%TYPE,
    IDTIPOAPUNTE         FCS_FACT_APUNTE.IDTIPOAPUNTE%TYPE,
    MOTIVO               FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
    IMPORTE              FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
    CONTADOR             number,
    COSTEFIJO            SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE%TYPE);
  TYPE TAB_CG IS TABLE OF MATRICE_CG INDEX BY BINARY_INTEGER;

  /* Matriz de apuntes de UNIDAD DE GUARDIA */
  TYPE MATRICE_UG IS RECORD(
    IDINSTITUCION        SCS_GUARDIASCOLEGIADO.IDINSTITUCION%TYPE,
    IDTURNO              SCS_GUARDIASCOLEGIADO.IDTURNO%TYPE,
    IDGUARDIA            SCS_GUARDIASCOLEGIADO.IDGUARDIA%TYPE,
    IDPERSONA            SCS_GUARDIASCOLEGIADO.IDPERSONA%TYPE,
    FECHAINICIO          SCS_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
    FECHAFIN             SCS_GUARDIASCOLEGIADO.FECHAFIN%TYPE,
    FACTURADO            SCS_GUARDIASCOLEGIADO.FACTURADO%TYPE,
    MOTIVO               FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
    IMPORTE              FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
    CONTADOR             number,
    COSTEFIJO            SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE%TYPE);
  TYPE TAB_UG IS TABLE OF MATRICE_UG INDEX BY BINARY_INTEGER;

  /* Matriz de apuntes de ASISTENCIAS */
  TYPE MATRICE_AS IS RECORD(
    IDINSTITUCION SCS_ASISTENCIA.IDINSTITUCION%TYPE,
    ANIO          SCS_ASISTENCIA.ANIO%TYPE,
    NUMERO        SCS_ASISTENCIA.NUMERO%TYPE,
    FACTURADO     SCS_ASISTENCIA.FACTURADO%TYPE,
    MOTIVO        FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
    IMPORTE       FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
    CONTADOR      number);
  TYPE TAB_AS IS TABLE OF MATRICE_AS INDEX BY BINARY_INTEGER;

  /* Matriz de los GUARDIAS */
  TYPE MATRICE_AC IS RECORD(
    IDINSTITUCION SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
    ANIO          SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
    NUMERO        SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
    IDACTUACION   SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
    FACTURADO     SCS_ACTUACIONASISTENCIA.FACTURADO%TYPE,
    MOTIVO        FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
    IMPORTE       FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
    CONTADOR      number,
    COSTEFIJO     SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE%TYPE);
  TYPE TAB_AC IS TABLE OF MATRICE_AC INDEX BY BINARY_INTEGER;

  /* RECORD de FACTURACION */
  TYPE DATOS_FACTURACION IS RECORD(
    IDINSTITUCION Fcs_Facturacionjg.Idinstitucion%TYPE,
    IDFACTURACION Fcs_Facturacionjg.Idfacturacion%TYPE,
    FECHADESDE    Fcs_Facturacionjg.Fechadesde%TYPE,
    FECHAHASTA    Fcs_Facturacionjg.Fechahasta%TYPE);
  V_DATOS_FACTURACION DATOS_FACTURACION;

  TYPE TAB_HITOS IS TABLE OF SCS_HITOFACTURABLEGUARDIA.IDHITO%TYPE INDEX BY BINARY_INTEGER;

  /* RECORD de CONFIG_GUARDIA */
  TYPE CONFIG_GUARDIA IS RECORD(
    GUARDIA               NUMBER,
    IMPORTEGUARDIA        NUMBER,
    DOBLAASISTENCIA       NUMBER,
    IMPORTEGUARDIADOBLADA NUMBER,
    DOBLAACTUACION        NUMBER,
    ASISTENCIA            NUMBER,
    IMPORTEASISTENCIA     NUMBER,
    TIPOASISTENCIA        NUMBER,
    IMPORTEMAXASISTENCIA  NUMBER,
    IMPORTEMINASISTENCIA  NUMBER,
    ACTUACION             NUMBER,
    IMPORTEACTUACION      NUMBER,
    TIPOACTUACION         NUMBER,
    IMPORTETIPOACTUACION  NUMBER,
    IMPORTEMAXACTUACION   NUMBER,
    IMPORTEMINACTUACION   NUMBER,
    ACTUACIONFG           NUMBER,
    IMPORTEACTUACIONFG    NUMBER,
    TIPOACTUACIONFG       NUMBER,
    IMPORTEMAXACTUACIONFG NUMBER,
    IMPORTESOJ            NUMBER,
    IMPORTEEJG            NUMBER,
    NUMASISTENCIASDOBLA   NUMBER,
    NUMACTUACIONESDOBLA   NUMBER,
    DIASPAGAGUARDIA       SCS_HITOFACTURABLEGUARDIA.DIASAPLICABLES%TYPE,
    DIASNOPAGAGUARDIA     SCS_HITOFACTURABLEGUARDIA.DIASAPLICABLES%TYPE,
    AGRUPARPAGAGUARDIA    SCS_HITOFACTURABLEGUARDIA.AGRUPAR%TYPE,
    AGRUPARNOPAGAGUARDIA  SCS_HITOFACTURABLEGUARDIA.AGRUPAR%TYPE,
    NUMHITOS NUMBER,
    LISTAHITOS TAB_HITOS,    
    IDFACTURACION FCS_HISTORICO_HITOFACT.IDFACTURACION%TYPE,
    --INI: Cambio facturacion guardias inactivas catalanes de VG --
    CONSEJOINSTITUCION    CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE,
    ESGUARDIAVG           SCS_GUARDIASTURNO.IDTIPOGUARDIA%TYPE);
    --FIN: Cambio facturacion guardias inactivas catalanes de VG --
  V_CONFIG_GUARDIA CONFIG_GUARDIA;

  /* Matriz de los GUARDIAS */
  TYPE MATRICE_CG_FACTURABLE IS RECORD(
    IDINSTITUCION        SCS_CABECERAGUARDIAS.IDINSTITUCION%TYPE,
    IDTURNO              SCS_CABECERAGUARDIAS.IDTURNO%TYPE,
    IDGUARDIA            SCS_CABECERAGUARDIAS.IDGUARDIA%TYPE,
    IDPERSONA            SCS_CABECERAGUARDIAS.IDPERSONA%TYPE,
    FECHAINICIO          SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE,
    FECHAFIN             SCS_CABECERAGUARDIAS.FECHA_FIN%TYPE,
    FACTURADO            SCS_GUARDIASCOLEGIADO.FACTURADO%TYPE,
    IDFACTURACION FCS_HISTORICO_HITOFACT.IDFACTURACION%TYPE,
    FECHAACT SCS_ACTUACIONASISTENCIA.FECHA%TYPE,    
    IMPORTEFACTURADO     NUMBER,
    IMPORTEFACTURADOASIS NUMBER,
    --INI: Cambio para Asistencias que derivan en Designacion
    TIENE_ASISTQUEDERIVAN NUMBER);
    --FIN: Cambio para Asistencias que derivan en Designacion

  TYPE TAB_CG_FACTURABLE IS TABLE OF MATRICE_CG_FACTURABLE INDEX BY BINARY_INTEGER;

  --MATRICES:
  M_APUNTE_CG     TAB_CG;
  M_APUNTE_UG     TAB_UG;
  M_APUNTE_AS     TAB_AS;
  M_APUNTE_AC     TAB_AC;
  M_CG_FACTURABLE TAB_CG_FACTURABLE;

  --INDICES PARA RECORRER LAS MATRICES:
  IND_CG            BINARY_INTEGER := 0; /* Indicador de la matriz de Cabecera de Guardias */
  IND_UG            BINARY_INTEGER := 0; /* Indicador de la matriz de Unidad de Guardia */
  IND_AS            BINARY_INTEGER := 0; /* Indicador de la matriz de Asistencias */
  IND_AC            BINARY_INTEGER := 0; /* Indicador de la matriz de Actuaciones */
  IND_CG_FACTURABLE BINARY_INTEGER := 0; /* Indicador de la matriz de Cabecera de Guardias Facturable*/

  -- Function and procedure implementations

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_FACTURAR_TURNOS_OFICIO                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPARTIDAPRESUPUESTARIA       IN  Identificador de partida presupestaria                    NUMBER,
  /* P_FECHADESDE          IN       fecha desde                                                    DATE,
  /* P_FECHAHASTA          IN       fecha hasta                                                    DATE,
  /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
  /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*    07/03/2006          Pilar Duran Mu?oz                   Se obtiene ademas del precio total, el valor del  */
  /*                                                            Punto.
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_TURNOS_OFI(P_IDINSTITUCION   IN NUMBER,
                                         P_IDFACTURACION   IN NUMBER,
                                         P_USUMODIFICACION IN NUMBER,
                                         P_TOTAL           OUT VARCHAR2,
                                         P_CODRETORNO      OUT VARCHAR2,
                                         P_DATOSERROR      OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:  PROC_FCS_FACTURAR_EJG                                                                      */
  /* Descripcion:  Calcula los precios para la facturacion de los SOJ.
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /* P_USUMODIFICACION     IN       Usuario                                                        NUMBER,
  /* P_TOTAL               IN       Importe total                                                  VARCHAR2(10)
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/03/2006                                                                                   */
  /* Autor:          Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_EJG(P_IDINSTITUCION   IN NUMBER,
                                  P_IDFACTURACION   IN NUMBER,
                                  P_USUMODIFICACION IN NUMBER,
                                  P_TOTAL           OUT VARCHAR2,
                                  P_CODRETORNO      OUT VARCHAR2,
                                  P_DATOSERROR      OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:  PROC_FCS_FACTURAR_GUARDIAS                                                                      */
  /* Descripcion:  Calcula los precios para la facturacion de las guardias.
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /* P_USUMODIFICACION     IN       Usuario                                                        NUMBER,
  /* P_TOTAL               IN       Importe total                                                  VARCHAR2(10)
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/03/2006                                                                                   */
  /* Autor:          Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_GUARDIAS(P_IDINSTITUCION   IN NUMBER,
                                       P_IDFACTURACION   IN NUMBER,
                                       P_USUMODIFICACION IN NUMBER,
                                       P_TOTAL           OUT VARCHAR2,
                                       P_CODRETORNO      OUT VARCHAR2,
                                       P_DATOSERROR      OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:  PROC_FCS_FACTURAR_SOJ                                                                      */
  /* Descripcion:  Calcula los precios para la facturacion de los SOJ.
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /* P_USUMODIFICACION     IN       Usuario                                                        NUMBER,
  /* P_TOTAL               IN       Importe total                                                  VARCHAR2(10)
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/03/2006                                                                                   */
  /* Autor:          Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_SOJ(P_IDINSTITUCION   IN NUMBER,
                                  P_IDFACTURACION   IN NUMBER,
                                  P_USUMODIFICACION IN NUMBER,
                                  P_TOTAL           OUT VARCHAR2,
                                  P_CODRETORNO      OUT VARCHAR2,
                                  P_DATOSERROR      OUT VARCHAR2);


  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_BORRAR_FACTURACION                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_BORRAR_FACTURACION(P_IDINSTITUCION IN NUMBER,
                                        P_IDFACTURACION IN NUMBER,
                                        P_CODRETORNO    OUT VARCHAR2,
                                        P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_ACTUACIONDESIGNA                                                                                   */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_ACT_DES(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_ASISTENCIA                                                                                   */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_ASISTENCIA(P_IDINSTITUCION IN NUMBER,
                                         P_IDFACTURACION IN NUMBER,
                                         P_CODRETORNO    OUT VARCHAR2,
                                         P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_ACTUACIONASISTENCIA                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_ACT_ASIS(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_GUARDIASCOLEGIADO                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_GUAR_COL(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_APUNTES                                                                          */
  /* Descripcion: Pone a no facturado la cabecera de guardias                                                     */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Nuevo                                            */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_APUNTES(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_EXPEDIENTES_SOJ                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_EXP_SOJ(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_EXPEDIENTES_EJG                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_EXP_EJG(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_SOJ                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_SOJ(P_IDINSTITUCION IN NUMBER,
                                  P_IDFACTURACION IN NUMBER,
                                  P_IDPAGO        IN NUMBER,
                                  P_IDPERSONA     IN NUMBER,
                                  P_PATHFICHERO   IN VARCHAR2,
                                  P_FICHERO       IN VARCHAR2,
                                  P_CABECERAS     IN VARCHAR2,
                                  P_CODRETORNO    OUT VARCHAR2,
                                  P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_EJG                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_EJG(P_IDINSTITUCION IN NUMBER,
                                  P_IDFACTURACION IN NUMBER,
                                  P_IDPAGO        IN NUMBER,
                                  P_IDPERSONA     IN NUMBER,
                                  P_PATHFICHERO   IN VARCHAR2,
                                  P_FICHERO       IN VARCHAR2,
                                  P_CABECERAS     IN VARCHAR2,
                                  P_CODRETORNO    OUT VARCHAR2,
                                  P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_TURNOS_OFI                                                                       */
  /* Descripcion: Exporta los datos facturados de actuaciones de designaciones                                    */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_TURNOS_OFI(P_IDINSTITUCION  IN NUMBER,
                                         P_IDFACTURACION1 IN NUMBER,
                                         P_IDFACTURACION2 IN NUMBER,
                                         P_IDPERSONA      IN NUMBER,
                                         P_PATHFICHERO    IN VARCHAR2,
                                         P_FICHERO        IN VARCHAR2,
                                         P_IDIOMA         IN NUMBER,
                                         P_CODRETORNO     OUT VARCHAR2,
                                         P_DATOSERROR     OUT VARCHAR2);
  PROCEDURE PROC_EXPORTAR_TURNOS_OTRO(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_IDPERSONA     IN NUMBER,
                                      P_PATHFICHERO   IN VARCHAR2,
                                      P_FICHERO       IN VARCHAR2,
                                      P_IDIOMA        IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2);

  Function F_GET_LISTA_DEF_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                        p_Idturno        In Scs_Designa.Idturno%Type,
                                        p_Anio           In Scs_Designa.Anio%Type,
                                        p_Numero         In Scs_Designa.Numero%Type,
                                        p_Tipodevolucion In Number,
                                        p_Idioma         In Number)
    Return Varchar2;
  Function F_GET_LISTA_EJGS_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                         p_Idturno        In Scs_Designa.Idturno%Type,
                                         p_Anio           In Scs_Designa.Anio%Type,
                                         p_Numero         In Scs_Designa.Numero%Type)
    Return Varchar2;
  Function F_GET_LISTA_ASI_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                        p_Idturno        In Scs_Designa.Idturno%Type,
                                        p_Anio           In Scs_Designa.Anio%Type,
                                        p_Numero         In Scs_Designa.Numero%Type,
                                        p_Prefijo        In Varchar2)
    Return Varchar2;
  PROCEDURE PROC_EXPORTAR_TURNOS_3001(P_IDINSTITUCION  IN NUMBER,
                                      P_IDFACTURACION1 IN NUMBER,
                                      P_IDFACTURACION2 IN NUMBER,
                                      P_IDPERSONA      IN NUMBER,
                                      P_PATHFICHERO    IN VARCHAR2,
                                      P_FICHERO        IN VARCHAR2,
                                      P_IDIOMA         IN NUMBER,
                                      P_CODRETORNO     OUT VARCHAR2,
                                      P_DATOSERROR     OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_GUARDIAS                                                                         */
  /* Descripcion: Exporta los datos facturados de guardias                                                        */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 17/05/2006                                                                                   */
  /* Autor: David Sanchez Pina
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION  IN NUMBER,
                                       P_IDFACTURACION1 IN NUMBER,
                                       P_IDFACTURACION2 IN NUMBER,
                                       P_IDPERSONA      IN NUMBER,
                                       P_PATHFICHERO    IN VARCHAR2,
                                       P_FICHERO        IN VARCHAR2,
                                       P_IDIOMA         IN NUMBER,
                                       P_CODRETORNO     OUT VARCHAR2,
                                       P_DATOSERROR     OUT VARCHAR2);
  PROCEDURE PROC_EXPORTAR_GUARDIAS_OTRO(P_IDINSTITUCION IN NUMBER,
                                        P_IDFACTURACION IN NUMBER,
                                        P_IDPERSONA     IN NUMBER,
                                        P_PATHFICHERO   IN VARCHAR2,
                                        P_FICHERO       IN VARCHAR2,
                                        P_IDIOMA        IN NUMBER,
                                        P_CODRETORNO    OUT VARCHAR2,
                                        P_DATOSERROR    OUT VARCHAR2);
  PROCEDURE PROC_EXPORTAR_GUARDIAS_3001(P_IDINSTITUCION  IN NUMBER,
                                        P_IDFACTURACION1 IN NUMBER,
                                        P_IDFACTURACION2 IN NUMBER,
                                        P_IDPERSONA      IN NUMBER,
                                        P_PATHFICHERO    IN VARCHAR2,
                                        P_FICHERO        IN VARCHAR2,
                                        P_IDIOMA         IN NUMBER,
                                        P_CODRETORNO     OUT VARCHAR2,
                                        P_DATOSERROR     OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FCS_CARGA_FACTURACION                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de facturacion con el idinstitucion,
  /*                idfacturacion, fechadesde, fechahasta                               */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 19/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_CARGA_FACTURACION(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FCS_INI_CONFIG_GUARDIA                                                                           */
  /* Descripcion:   Este procedimiento se encarga de inicializar la matriz de configuracion de guardias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDTURNO             IN       Identificador del turno                                        NUMBER
  /* P_IDGUARDIA           IN       Identificador de la guardia                                    NUMBER         */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_INI_CONFIG_GUARDIA(P_CODRETORNO    OUT VARCHAR2,
                                        P_DATOSERROR    OUT VARCHAR2);

    /****************************************************************************************************************
        Nombre: PROC_FCS_CARGA_CONFIG_GUARDIA
        Descripcion: Este procedimiento se encarga de inicializar la matriz de configuracion de guardias

        Parametros:
        - P_IDINSTITUCION - IN - Identificador de la Institucion - NUMBER
        - P_IDTURNO - IN - Identificador del turno - NUMBER
        - P_IDGUARDIA- IN - Identificador de la guardia - NUMBER
        - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
            En caso de error devuelve el codigo de error Oracle correspondiente.
        - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
            En caso de error devuelve el mensaje de error Oracle correspondiente.

        Versiones:            
            - 1.0 - Fecha Creacion: 20/04/2006  - Autor: Pilar Duran Munoz
            - 2.0 - Fecha Modificacion: 26/02/2016  - Autor: Jorge Paez Trivino
                Cambios realizados para facturacion de colegios catalanes, de cabeceras de guardias facturadas, con la configuracion de la primera facturacion
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_CARGA_CONFIG_GUARDIA (
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDFACTURACION IN FCS_FACT_APUNTE.IDFACTURACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        FUNC_IMPORTEFACTURADO_CG                                                                      */
  /* Descripcion:   Funcion que nos devueve el importe facturado de la Cabecera de Guardia.
  /*                No se obtiene lo facturado por FG. Facturaciones + Regularizaciones                           */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha de inicio                                                  DATE        */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_IMPORTEFACTURADO_CG(P_IDINSTITUCION        NUMBER,
                                    P_IDTURNO              NUMBER,
                                    P_IDGUARDIA            NUMBER,
                                    P_IDPERSONA            NUMBER,
                                    P_FECHAINICIO          DATE)
    return NUMBER;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_IMPORTEFACTURADO_ASIS_CG                                                                      */
  /* Descripcion:   Funcion que nos devueve el importe facturado de la Cabecera de Guardia.
  /*                No se obtiene lo facturado por FG. Facturaciones + Regularizaciones                           */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha de inicio                                                  DATE        */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_IMPORTEFACTURADO_ASIS_CG(P_IDINSTITUCION        NUMBER,
                                    P_IDTURNO              NUMBER,
                                    P_IDGUARDIA            NUMBER,
                                    P_IDPERSONA            NUMBER,
                                    P_FECHAINICIO          DATE)
    return NUMBER;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGAR_CABECERA_GUARDIAS                                                                           */
  /* Descripcion:   Este procedimiento se encarga de Cargar la matriz de Cabecera de Guardias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDTURNO             IN       Identificador del turno                                        NUMBER
  /* P_IDGUARDIA           IN       Identificador de la guardia                                    NUMBER         */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGAR_CABECERA_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                          P_IDTURNO       IN NUMBER,
                                          P_IDGUARDIA     IN NUMBER,
                                          P_CODRETORNO    OUT VARCHAR2,
                                          P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        FUNC_ASISTENCIASFACTURADAS                                                                   */
  /* Descripcion:   Funcion que nos devuelve el total de asistencias factutadas de una Cabecera de Guardia            */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 21/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_ASISTENCIASFACTURADAS_CG(P_IDINSTITUCION NUMBER,
                                         P_IDTURNO       NUMBER,
                                         P_IDGUARDIA     NUMBER,
                                         P_IDPERSONA     NUMBER,
                                         P_FECHAINICIO   DATE,
                                         P_FECHAFIN      DATE) return number;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_ACTUACIONESFACTURADAS                                                                    */
  /* Descripcion:   Funcion que nos devuelve el total de actuaciones facturadas de una Cabecera de Guardia        */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     FECHA INICIO de la cabecera de Guardia                           DATE,
  /* P_FECHAFIN               IN     FECHA FIN de la cabecera de Guardia                           DATE,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 21/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_ACTUACIONESFACTURADAS_CG(P_IDINSTITUCION NUMBER,
                                         P_IDTURNO       NUMBER,
                                         P_IDGUARDIA     NUMBER,
                                         P_IDPERSONA     NUMBER,
                                         P_FECHAINICIO   DATE,
                                         P_FECHAFIN      DATE) return number;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_ACTUACIONESNUEVAS                                                                    */
  /* Descripcion:   Funcion que nos devuelve el total de actuaciones nuevas de una Cabecera de Guardia        */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     FECHA INICIO de la cabecera de Guardia                           DATE,
  /* P_FECHAFIN               IN     FECHA FIN de la cabecera de Guardia                           DATE,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 21/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_ACTUACIONESNUEVAS_CG(P_IDINSTITUCION NUMBER,
                                     P_IDTURNO       NUMBER,
                                     P_IDGUARDIA     NUMBER,
                                     P_IDPERSONA     NUMBER,
                                     P_FECHAINICIO   DATE,
                                     P_FECHAFIN      DATE) return number;

  /*
   *  Funciones que calculan los numeros de actuaciones y asistencias
   * para la exportacion del fichero de guardias en PROC_FCS_EXPORTAR_GUARDIAS
   *
   *  Si se pasa P_FECHAINICIO nulo, solo buscara en el dia exacto P_FECHAFIN
   *
   * INICIO
   */
  Function FUNC_NUM_ACTUAC_FACTURAC(p_Idinstitucion  Number,
                                    p_Idfacturacion1 Number,
                                    p_Idfacturacion2 Number,
                                    p_Idturno        Number,
                                    p_Idguardia      Number,
                                    p_Idpersona      Number,
                                    p_Fechainicio    Date,
                                    p_Fechafin       Date,
                                    p_Esfg           Varchar2) return number;
  Function FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion  Number,
                                   p_Idfacturacion1 Number,
                                   p_Idfacturacion2 Number,
                                   p_Idturno        Number,
                                   p_Idguardia      Number,
                                   p_Idpersona      Number,
                                   p_Fechainicio    Date,
                                   p_Fechafin       Date,
                                   p_Esfg           Varchar2) Return Number;
  function FUNC_NUM_ASIST_FACTURAC(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE) return number;
  function FUNC_NUM_ASIST_TOTALES(P_IDINSTITUCION NUMBER,
                                  P_IDFACTURACION NUMBER,
                                  P_IDTURNO       NUMBER,
                                  P_IDGUARDIA     NUMBER,
                                  P_IDPERSONA     NUMBER,
                                  P_FECHAINICIO   DATE,
                                  P_FECHAFIN      DATE) return number;

    /****************************************************************************************************************/
    /* Nombre: FUNC_EXISTE_ACTUACION_JUSTIF */
    /* Descripcion: Funcion que comprueba que exista una actuacion justificada */
    /* */
    /* P_IDINSTITUCION - IN - identificador de la institucion - NUMBER */
    /* P_ANIO - IN - identificador del anio - NUMBER */
    /* P_NUMERO - IN - identificador del numero de asistencia - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 21/04/2006 - Autor: Pilar Duran Munoz */
    /* Version: 2.0 - Fecha Modificacion: 22/01/2015 - Autor: Jorge Paez Trivino - Se indica si se admite fuera de guardia */
  /****************************************************************************************************************/
    FUNCTION FUNC_EXISTE_ACTUACION_JUSTIF(
        P_IDINSTITUCION NUMBER,
        P_ANIO NUMBER,
        P_NUMERO NUMBER) RETURN NUMBER;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_AC                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /*
  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_AC(P_IDINSTITUCION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE,
                                   CONTADOR        NUMBER,
                                   SELECT1         VARCHAR2,
                                   SELECT2         VARCHAR2,
                                   P_TIPODOBLA       VARCHAR2,
                                   V_costefijo     out number,
                                   P_TOTALACTUA    OUT NUMBER,
                                   V_CODRETORNO    OUT VARCHAR2,
                                   V_DATOSERROR    OUT VARCHAR2);

  PROCEDURE PROC_CARGA_M_APUNTE_ACNTIPO_CG(P_IDINSTITUCION NUMBER,
                                           P_IDTURNO       NUMBER,
                                           P_IDGUARDIA     NUMBER,
                                           P_IDPERSONA     NUMBER,
                                           P_FECHAINICIO   DATE,
                                           P_FECHAFIN      DATE,
                                           CONTADOR        NUMBER,
                                           SELECT1         VARCHAR2,
                                           SELECT2         VARCHAR2,
                                           MOTIVO          Number,
                                           V_costefijo     out number,
                                           V_CODRETORNO    OUT VARCHAR2,
                                           V_DATOSERROR    OUT VARCHAR2);
  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_ASTIPO_CG                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de asistencias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la actuacion          NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_ASTIPO_CG(P_IDINSTITUCION NUMBER,
                                          P_IDTURNO       NUMBER,
                                          P_IDGUARDIA     NUMBER,
                                          P_IDPERSONA     NUMBER,
                                          P_FECHAINICIO   DATE,
                                          P_FECHAFIN      DATE,
                                          CONTADOR        NUMBER,
                                          P_CODRETORNO    OUT VARCHAR2,
                                          P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_ACTIPO_CG                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la asistencia         NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_ACTIPO_CG(P_IDINSTITUCION NUMBER,
                                          P_IDTURNO       NUMBER,
                                          P_IDGUARDIA     NUMBER,
                                          P_IDPERSONA     NUMBER,
                                          P_FECHAINICIO   DATE,
                                          P_FECHAFIN      DATE,
                                          CONTADOR        NUMBER,
                                          SELECT1         VARCHAR2,
                                          SELECT2         VARCHAR2,
                                          IDTIPOASIST     number,
                                          MOTIVO          Number,
                                          V_COSTEFIJO     OUT NUMBER,
                                          V_CODRETORNO    OUT VARCHAR2,
                                          V_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_AS                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de asistencia.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_CODRETORNO           OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR           OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_AS(P_IDINSTITUCION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE,
                                   CONTADOR        NUMBER,
                                   SELECT1         VARCHAR2,
                                   SELECT2         VARCHAR2,
                                   P_TIPODOBLA       VARCHAR2,
                                   P_TOTALASIST    OUT NUMBER,
                                   P_CODRETORNO    OUT VARCHAR2,
                                   P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_GUARDIA_DOBLAACT                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Guardia
  /*                y se dobla por numero de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  PROCEDURE PROC_FACT_GUARDIA_DOBLAACT(indiceMatrizFacturable number,
                                       P_CODRETORNO           OUT VARCHAR2,
                                       P_DATOSERROR           OUT VARCHAR2);
  PROCEDURE PROC_FACT_GUARDIA_DOBLAAC_AGRU(indiceMatrizFacturable number);
  PROCEDURE PROC_FACT_GUARDIA_DOBLAAC_NOAG(indiceMatrizFacturable number);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_GUARDIA_DOBLAASIST                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Guardia
  /*                y se dobla por asistencia.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  PROCEDURE PROC_FACT_GUARDIA_DOBLAASIST(indiceMatrizFacturable number,
                                         P_CODRETORNO           OUT VARCHAR2,
                                         P_DATOSERROR           OUT VARCHAR2);
  PROCEDURE PROC_FACT_GUARDIA_DOBLAAS_AGRU(indiceMatrizFacturable number);
  PROCEDURE PROC_FACT_GUARDIA_DOBLAAS_NOAG(indiceMatrizFacturable number);

    /****************************************************************************************************************/
    /* Nombre: PROC_FACT_ASIST_NOAPLICATIPO */
    /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Asistencias y no aplica tipos de asistencias. */
    /* */
    /* P_INDICEMATRIZFACTURABLE - IN - Indica sobre que registro se obtiene la config. de facturacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(200) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* */
    /* Version: 1.0 - Fecha Creacion: 25/04/2006 - Autor: Pilar Duran Munoz */
    /* Version: 2.0 - Fecha Modificacion: 05/02/2014 - Autor: Jorge Paez Trivino */
    /****************************************************************************************************************/
    PROCEDURE PROC_FACT_ASIST_NOAPLICATIPO (
        P_INDICEMATRIZFACTURABLE IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2
    );

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ASIST_APLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Asistencias
  /*                y aplica tipos de asistencias.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  PROCEDURE PROC_FACT_ASIST_APLICATIPO(indiceMatrizFacturable in number,
                                       P_CODRETORNO           OUT VARCHAR2,
                                       P_DATOSERROR           OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ACT_NOAPLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Actuaciones
  /*                y no aplica tipos de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  Function f_Tiene_Asist_Derivadas(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                   p_Idturno       In Scs_Asistencia.Idturno%Type,
                                   p_Idguardia     In Scs_Asistencia.Idguardia%Type,
                                   p_Idpersona     In Scs_Asistencia.Idpersonacolegiado%Type,
                                   p_Fechaini      In Scs_Asistencia.Fechahora%Type,
                                   p_Fechafin      In Scs_Asistencia.Fechahora%Type,
                                   p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                   p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2;
  Function f_Tiene_Asist_Derivadas_Gijon(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                         p_Idturno       In Scs_Asistencia.Idturno%Type,
                                         p_Idguardia     In Scs_Asistencia.Idguardia%Type,
                                         p_Idpersona     In Scs_Asistencia.Idpersonacolegiado%Type,
                                         p_Fechaini      In Scs_Asistencia.Fechahora%Type,
                                         p_Fechafin      In Scs_Asistencia.Fechahora%Type,
                                         p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                         p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2;
  Function f_Tiene_Asist_Derivadas_Cr(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                      p_Idturno       In Scs_Asistencia.Idturno%Type,
                                      p_Idguardia     In Scs_Asistencia.Idguardia%Type,
                                      p_Idpersona     In Scs_Asistencia.Idpersonacolegiado%Type,
                                      p_Fechaini      In Scs_Asistencia.Fechahora%Type,
                                      p_Fechafin      In Scs_Asistencia.Fechahora%Type,
                                      p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2;
  Function f_Es_Asistencia_Derivada(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                    p_Anio          In Scs_Asistencia.Anio%Type,
                                    p_Numero        In Scs_Asistencia.Numero%Type,
                                    p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                    p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2;
  Function f_Es_Asistencia_Derivada_Gijon(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                          p_Anio          In Scs_Asistencia.Anio%Type,
                                          p_Numero        In Scs_Asistencia.Numero%Type,
                                          p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                          p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2;
  Function f_Es_Asistencia_Derivada_Cr(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                       p_Anio          In Scs_Asistencia.Anio%Type,
                                       p_Numero        In Scs_Asistencia.Numero%Type,
                                       p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2;
  Function f_Es_Tipo_Derivacion(p_Idinstitucion Scs_Actuacionasistencia.Idinstitucion%Type,
                                p_Anio          Scs_Actuacionasistencia.Anio%Type,
                                p_Numero        Scs_Actuacionasistencia.Numero%Type,
                                p_Idactuacion   Scs_Actuacionasistencia.Idactuacion%Type)
    Return Varchar2;
  PROCEDURE PROC_FACT_ACT_NOAPLICATIPO(indiceMatrizFacturable in number,
                                       P_CODRETORNO           OUT VARCHAR2,
                                       P_DATOSERROR           OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ACT_APLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Actuaciones
  /*                y aplica tipos de actuaciones.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  PROCEDURE PROC_FACT_ACT_APLICATIPO(indiceMatrizFacturable in number,
                                     P_CODRETORNO           OUT VARCHAR2,
                                     P_DATOSERROR           OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ACTFG_NOAPLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Actuaciones
  /*                fuera de guardia y no aplica tipos de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

    PROCEDURE PROC_FACT_ACTFG_NOAPLICATIPO(
        indiceMatrizFacturable IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ACTFG_APLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Actuaciones
  /*                fuera de guardia y aplica tipos de actuaciones.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  PROCEDURE PROC_FACT_ACTFG_APLICATIPO(indiceMatrizFacturable in number,
                                       P_CODRETORNO           OUT VARCHAR2,
                                       P_DATOSERROR           OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        FUNC_EXISTE_ACTFACTURADA                                                                             */
  /* Descripcion:   Funcion que comprueba si existen actuaciones facturadas para un dia concreto           */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO             IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA           IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA           IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO         IN     fecha inicio de CG                                               NUMBER,
  /* P_FECHAFIN            IN     fecha fin de CG                                                  NUMBER,
  /* P_FECHA               IN     dia                                                              NUMBER,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_EXISTE_ACTFACTURADA(P_IDINSTITUCION NUMBER,
                                    P_IDTURNO       NUMBER,
                                    P_IDGUARDIA     NUMBER,
                                    P_IDPERSONA     NUMBER,
                                    P_FECHAINICIO   DATE,
                                    P_FECHAFIN      DATE,
                                    P_FECHA         DATE) return number;

  -- RGG

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_DESCARGA_MATR_GUARDIA                                                                           */
  /* Descripcion:   Este procedimiento se encarga de descargar en las tablas de apuntes las matrices de guardias
  /*?               y vaciar las matrices
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 27/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                      */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

  Procedure PROC_FACT_DESC_MATR_GUARDIA(p_Idinstitucion   In Fcs_Fact_Apunte.Idinstitucion%Type,
                                        p_Idfacturacion   In Fcs_Fact_Apunte.Idfacturacion%Type,
                                        p_Usumodificacion In Fcs_Fact_Apunte.Usumodificacion%Type,
                                        p_Codretorno      Out Varchar2,
                                        p_Datoserror      Out Varchar2);

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_CG                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_CG(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER)
    return FCS_FACT_APUNTE.IDAPUNTE%TYPE;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_UG                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes UG                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_UG(P_IDINSTITUCION        NUMBER,
                                   P_IDFACTURACION        NUMBER,
                                   P_IDTURNO              NUMBER,
                                   P_IDGUARDIA            NUMBER,
                                   P_FECHAINICIO          FCS_FACT_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
                                   P_FECHAFIN             FCS_FACT_GUARDIASCOLEGIADO.FECHAFIN%TYPE,
                                   P_IDTIPO               NUMBER)
    return FCS_FACT_GUARDIASCOLEGIADO.IDAPUNTE%TYPE;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_AS                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes AS                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_AS(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_ANIO          NUMBER,
                                   P_NUMERO        NUMBER,
                                   P_IDTIPO        NUMBER)
    return FCS_FACT_ASISTENCIA.IDAPUNTE%TYPE;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_AC                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes AC                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_AC(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_IDACTUACION   NUMBER,
                                   P_ANIO          NUMBER,
                                   P_NUMERO        NUMBER,
                                   P_IDTIPO        NUMBER)
    return FCS_FACT_ACTUACIONASISTENCIA.IDAPUNTE%TYPE;

  /****************************************************************************************************************/
  /* Nombre:        FUN_DESCRIPCION_IDHITO                                                                           */
  /* Descripcion:   Funcion que obtiene la descripcion del hito                               */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_DESCRIPCION_IDHITO(P_IDHITO SCS_HITOFACTURABLE.IDHITO%TYPE)
    return SCS_HITOFACTURABLE.NOMBRE%TYPE;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_GUARDIAS                                                                     */
  /* Descripcion: Exporta los datos facturados de actuaciones de designaciones                                    */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_PATHFICHERO   IN VARCHAR2,
                                       P_FICHERO       IN VARCHAR2,
                                       P_IDIOMA        IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2);

  FUNCTION FUN_ESDIAAPLICABLE(P_FECHA DATE, P_DIASAPLICABLES VARCHAR2)
    return number;


  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURACIONES_INTERVALO                                                                  */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 22/06/2009                                                                                   */
  /* Autor:         Jose Barrientos Diaz                                                                          */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

 FUNCTION FUNC_FACTURACIONES_INTERVALO(P_IDINSTITUCION  NUMBER,
                                       P_IDFACTURACION1 NUMBER,
                                       P_IDFACTURACION2 NUMBER)
                                       return VARCHAR2;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURA_INTER_GRUPOS                                                                     */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG filtrado por grupos         */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/02/2012                                                                                   */
  /* Autor:         Susana Rubio Lopez                                                                            */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

 FUNCTION FUNC_FACTURA_INTER_GRUPOS(P_IDINSTITUCION  NUMBER,
                                       P_IDFACTURACION1 NUMBER,
                                       P_IDFACTURACION2 NUMBER,
                                       p_GrupoFact Number)
                                       return VARCHAR2;



  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_MARCAR_FACTURADOS                                                                         */
  /* Descripcion: Marca todos los registros de cabeceras guardias, asistencias y actuaciones utilizados en la     */
    /*              facturacion como facturados                                                                     */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* IDINSTITUCION         IN       Identificador de la Institucion                                NUMBER         */
  /* IDFACTURACION         IN       Identificador de la Facturacion                                NUMBER         */
  /* codigoRetorno         OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* error                 OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 19/01/2010                                                                                   */
  /* Autor:          Salvador Madrid-Salvador                                                                           */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
PROCEDURE PROC_FCS_MARCAR_FACTURADOS(
    idInstitucion IN VARCHAR2,
    idFacturacion in VARCHAR2,
    codigoRetorno OUT VARCHAR2,
    error OUT VARCHAR2);
                                                               
  /****************************************************************************************************************
    Nombre: FUNC_OBTENER_IDFACTURACION
    Descripcion: Funcion que busca el IDFACTURACION de una cabecera de guardia (incluye regularizaciones)

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER    
    - P_FECHA - IN - Fecha donde buscar la facturacion - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/    
    FUNCTION FUNC_OBTENER_IDFACTURACION(
        P_IDINSTITUCION IN FCS_HISTORICO_HITOFACT.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_HISTORICO_HITOFACT.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_HISTORICO_HITOFACT.IDGUARDIA%TYPE,
        P_FECHA IN FCS_FACTURACIONJG.FECHADESDE%TYPE) RETURN NUMBER;
        
  /****************************************************************************************************************
    Nombre: PROC_CARGAR_CABGUARDIASFG
    Descripcion: Procedimiento que obtiene las cabeceras de guardia de actuaciones fuera de guardia

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/  
    PROCEDURE PROC_CARGAR_CABGUARDIASFG(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);     
        
  /****************************************************************************************************************
    Nombre: FUNC_COSTEFIJO
    Descripcion: Funcion que nos devuelve el coste fijo de una actuacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER    
    - P_ANIO - IN - Ano de la actuacion - NUMBER
    - P_NUMERO - IN - Numero de la actuacion - NUMBER
    - P_IDACTUACION - IN - Identificador del tipo de actuacion - NUMBER
    - P_FECHA - IN - Fecha de la asistencia - DATE
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 24/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/
    FUNCTION FUNC_COSTEFIJO(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTURNO IN SCS_ASISTENCIA.IDTURNO%TYPE,
        P_IDGUARDIA IN SCS_ASISTENCIA.IDGUARDIA%TYPE,
        P_ANIO IN SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION IN SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        P_FECHA IN SCS_ASISTENCIA.FECHAHORA%TYPE,
        P_IDFACTURACION IN FCS_HISTO_TIPOACTCOSTEFIJO.IDFACTURACION%TYPE) RETURN NUMBER;
        
  /****************************************************************************************************************
    Nombre: FUNC_CALCULAR_IMPORTEASIST
    Descripcion: Funcion que nos devuelve el importe de un tipo de asistencia

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER    
    - P_ANIO - IN - Ano de la actuacion - NUMBER
    - P_NUMERO - IN - Numero de la actuacion - NUMBER
    - P_FECHA - IN - Fecha de la asistencia - DATE
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_TIPO - IN - 0:IMPORTE; 1:IMPORTEMAXIMO; - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 27/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/
    FUNCTION FUNC_CALCULAR_IMPORTEASIST(
        P_IDINSTITUCION IN SCS_ASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTURNO IN SCS_ASISTENCIA.IDTURNO%TYPE,
        P_IDGUARDIA IN SCS_ASISTENCIA.IDGUARDIA%TYPE,
        P_ANIO IN SCS_ASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ASISTENCIA.NUMERO%TYPE,
        P_FECHA IN SCS_ASISTENCIA.FECHAHORA%TYPE,
        P_IDFACTURACION IN FCS_HISTORICO_TIPOASISTCOLEGIO.IDFACTURACION%TYPE,
        P_TIPO IN NUMBER) RETURN NUMBER;
        
  /****************************************************************************************************************
    Nombre: FUNC_CALCULAR_IMPORTEACT
    Descripcion: Funcion que nos devuelve el importe de un tipo de actuacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER       
    - P_ANIO - IN - Ano de la actuacion - NUMBER
    - P_NUMERO - IN - Numero de la actuacion - NUMBER
    - P_IDACTUACION - IN - Identificador del tipo de actuacion - NUMBER
    - P_FECHA - IN - Fecha de la asistencia - DATE
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_TIPO - IN - 0:IMPORTE; 1:IMPORTEMAXIMO; - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 27/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/
    FUNCTION FUNC_CALCULAR_IMPORTEACT(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTURNO IN SCS_ASISTENCIA.IDTURNO%TYPE,
        P_IDGUARDIA IN SCS_ASISTENCIA.IDGUARDIA%TYPE,
        P_ANIO IN SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION IN SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        P_FECHA IN SCS_ASISTENCIA.FECHAHORA%TYPE,
        P_IDFACTURACION IN FCS_HISTORICO_TIPOACTUACION.IDFACTURACION%TYPE,
        P_TIPO IN NUMBER) RETURN NUMBER;        
                                                                      
END PKG_SIGA_FACTURACION_SJCS;
/
 
CCREATE OR REPLACE PACKAGE BODY USCGAE_DESA.PKG_SIGA_FACTURACION_SJCS IS

    -- Public variable declarations
    C_FACTURACION_CONTROLADA CONSTANT BOOLEAN := TRUE; -- CONSTANTE PARA ACTIVAR LA FACTURACION CONTROLADA
    HITO_SOJ CONSTANT NUMBER := 12;
    HITO_EJG CONSTANT NUMBER := 13;    

    V_CODRETORNO2 VARCHAR2(10) := TO_CHAR(0); /* Codigo de error Oracle */
    V_DATOSERROR2 VARCHAR2(4000) := NULL; /* Mensaje de error Oracle */
    V_COSTEFIJO NUMBER := 0;    
    V_IDINSTITUCION NUMBER;
    V_IDFACTURACION NUMBER;
    V_ANIO NUMBER;
    V_NUMERO NUMBER;
    V_IDACTUACION NUMBER;
    V_IDTURNO NUMBER;
    V_IDGUARDIA NUMBER;
    V_IDPERSONA NUMBER;
    V_FECHAINICIO_CG DATE;
    V_FECHAFIN_CG DATE;
    V_FECHA DATE;
    TOTAL_FACTURACION NUMBER := 0;

    /* Declaracion de excepciones */
    -- ERROR GENERAL
    E_ERROR2 EXCEPTION;
    -- ERROR CONTROLADO EN LA FUNCION DE CALCULAR PRECIO
    E_ERROR3 EXCEPTION;
    -- E_ERROR  EXCEPTION;

    -- ERRORES CONTROLADOS DE LA FUNCION BORRAR FACTURACION
    E_ERROR_BF_1 EXCEPTION;
    E_ERROR_BF_2 EXCEPTION;
    E_ERROR_BF_3 EXCEPTION;
    E_ERROR_BF_4 EXCEPTION;
    E_ERROR_BF_5 EXCEPTION;
    E_ERROR_BF_6 EXCEPTION;
    E_ERROR_BF_7 EXCEPTION;
      
    --INI: Cambio facturacion guardias inactivas catalanes de VG --
    C_CATALAN CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE := 3001;
    C_IMPORTE_GUARDIA_INACTIVA SCS_HITOFACTURABLEGUARDIA.PRECIOHITO%TYPE := 61.16;
    --FIN: Cambio facturacion guardias inactivas catalanes de VG --

  -- declaracion de cursores para la facturacion de guardias
    -- Cursor para cargar el RECORD de Facturacion
    CURSOR C_FACTURACION(V_IDINSTITUCION NUMBER, V_IDFACTURACION NUMBER) is
        SELECT IDINSTITUCION,
            IDFACTURACION,
            FECHADESDE,
            FECHAHASTA
        FROM FCS_FACTURACIONJG
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDFACTURACION = V_IDFACTURACION;

    -- Cursor para obtener los grupos de facturacion
    CURSOR C_GRUPOSFACTURACION(V_IDINSTITUCION NUMBER, V_IDFACTURACION NUMBER) IS
        SELECT IDGRUPOFACTURACION,
            IDINSTITUCION
        FROM FCS_FACT_GRUPOFACT_HITO
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDFACTURACION = V_IDFACTURACION
            AND IDHITOGENERAL = PKG_SIGA_CONSTANTES.HITO_GENERAL_GUARDIA;

    -- Cursor para obtener el idturno
    CURSOR C_TURNO(V_IDINSTITUCION NUMBER, V_IIDGRUPOFACTURACION NUMBER) IS
        SELECT IDTURNO, IDINSTITUCION
        FROM SCS_TURNO
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDGRUPOFACTURACION = V_IIDGRUPOFACTURACION;

    -- Cursor para obtener el idguardia
    CURSOR C_GUARDIASTURNO(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER) IS
        SELECT IDGUARDIA
        FROM SCS_GUARDIASTURNO
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDTURNO = V_IDTURNO;
            --AND scs_guardiasturno.idguardiasustitucion IS NULL  -- DCG para no facturar las guardias de sustitucion
            --AND scs_guardiasturno.idturnosustitucion IS NULL;   -- DCG              
                  
    c_importeFacturado_CG varchar2(4000) := 'select sum(nvl(precioaplicado,0)) v_importeTotalCG' ||
        ' from fcs_fact_apunte' ||
        ' where fcs_fact_apunte.idinstitucion=@P_IDINSTITUCION@' ||
        ' and fcs_fact_apunte.idturno=@P_IDTURNO@' ||
        ' and fcs_fact_apunte.idguardia=@P_IDGUARDIA@' ||
        ' and fcs_fact_apunte.idpersona=@P_IDPERSONA@' ||
        ' and trunc(fcs_fact_apunte.fechainicio)=''@P_FECHAINICIO@''' ||
        --' and fcs_fact_apunte.motivo like ''G%''' || => JPT (18-06-2015): Si quitas este comentario no funciona PROC_FACT_ASIST_APLICATIPO (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1')
        ' and fcs_fact_apunte.idtipoapunte<>''FG''';

    c_importeFacturado_asis_CG varchar2(4000) := 'select sum(nvl(precioaplicado,0)) v_importeTotalCG' ||
        ' from fcs_fact_apunte' ||
        ' where fcs_fact_apunte.idinstitucion=@P_IDINSTITUCION@' ||
        ' and fcs_fact_apunte.idturno=@P_IDTURNO@' ||
        ' and fcs_fact_apunte.idguardia=@P_IDGUARDIA@' ||
        ' and fcs_fact_apunte.idpersona=@P_IDPERSONA@' ||
        ' and trunc(fcs_fact_apunte.fechainicio)=''@P_FECHAINICIO@''' ||
        ' and fcs_fact_apunte.motivo not like ''G%''' ||
        ' and fcs_fact_apunte.idtipoapunte<>''FG''';

    c_asistenciasfacturadas_CG varchar2(4000) := 'select count(*)' ||
        ' from Scs_Asistencia' ||
        ' where Scs_Asistencia.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and Scs_Asistencia.Idturno=@P_IDTURNO@' ||
        ' and Scs_Asistencia.Idguardia=@P_IDGUARDIA@' ||
        ' and Scs_Asistencia.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(Scs_Asistencia.Fechahora) between ''@P_FECHAINICIO@'' AND ''@P_FECHAFIN@''' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(Scs_Asistencia.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and Scs_Asistencia.Facturado=''1'' ';

    c_actuacionesfacturadas_CG varchar2(4000) := 'select count(*)' ||
        ' from Scs_Asistencia A, scs_actuacionasistencia Ac' ||
        ' where A.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and A.Idturno=@P_IDTURNO@' ||
        ' and A.Idguardia=@P_IDGUARDIA@' ||
        ' and A.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(A.Fechahora) between ''@P_FECHAINICIO@'' AND ''@P_FECHAFIN@''' ||
        ' and A.IDINSTITUCION=Ac.Idinstitucion' ||
        ' and A.Anio=Ac.Anio' ||
        ' and A.Numero=Ac.Numero' ||
        --' and  Ac.Facturado=''1''' || -- se elimina y se cambia por lo de abajo.
        ' and exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = Ac.idinstitucion' ||
        '  and fac.anio = Ac.anio' ||
        '  and fac.numero = Ac.numero' ||
        '  and fac.idactuacion = Ac.idactuacion)' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(A.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and Ac.diadespues=''N''';

    c_Actuacionesnuevas_CG varchar2(4000) := 'select count(*)' ||
        ' from Scs_Asistencia A, scs_actuacionasistencia Ac' ||
        ' where A.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and A.Idturno=@P_IDTURNO@' ||
        ' and A.Idguardia=@P_IDGUARDIA@' ||
        ' and A.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(A.Fechahora) between ''@P_FECHAINICIO@'' AND ''@P_FECHAFIN@''' ||
        ' and A.IDINSTITUCION=Ac.Idinstitucion' ||
        ' and A.Anio=Ac.Anio' ||
        ' and A.Numero=Ac.Numero' ||
        --' and (Ac.Facturado is null or Ac.Facturado = ''0'')' || -- no facturadas
        -- se elimina y se cambia por lo de abajo
        ' and not exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = Ac.idinstitucion' ||
        '  and fac.anio = Ac.anio' ||
        '  and fac.numero = Ac.numero' ||
        '  and fac.idactuacion = Ac.idactuacion)' ||
        ' and (Ac.Anulacion is null or Ac.Anulacion=0)' ||  --no anuladas
        ' and Ac.Fechajustificacion is not null' || --justificadas
        ' and trunc(Ac.Fechajustificacion) between ''@P_FECHAINIFACT@'' and ''@P_FECHAFINFACT@''' ||
        ' and Ac.Validada=1' || -- en fecha
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(A.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and Ac.diadespues=''N'''; -- no del día después.

    /*Cursores que utilizaremos para cargar la matriz de memoria m_apunte_as de CG*/
    c_obtener_asistenciasFact varchar2(4000) := 'select idinstitucion, anio, numero,1 facturado' ||
        ' from Scs_Asistencia' ||
        ' where Scs_Asistencia.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and Scs_Asistencia.Idturno=@P_IDTURNO@' ||
        ' and Scs_Asistencia.Idguardia=@P_IDGUARDIA@' ||
        ' and Scs_Asistencia.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(Scs_Asistencia.Fechahora) between ''@P_FECHAINICIO@'' AND ''@P_FECHAFIN@''' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(Scs_Asistencia.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and Scs_Asistencia.Facturado=''1'''; -- facturadas

    c_obtener_asistenciasnuevas varchar2(4000) := 'select idinstitucion, anio, numero, 0 facturado' ||
        ' from Scs_Asistencia' ||
        ' where Scs_Asistencia.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and Scs_Asistencia.Idturno=@P_IDTURNO@' ||
        ' and Scs_Asistencia.Idguardia=@P_IDGUARDIA@' ||
        ' and Scs_Asistencia.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(Scs_Asistencia.Fechahora) between ''@P_FECHAINICIO@'' AND ''@P_FECHAFIN@''' ||
        ' and Scs_Asistencia.Fechaanulacion is null' || --no anulada
        ' and (Scs_Asistencia.Facturado is null or Scs_Asistencia.Facturado<>''1'')' || -- no facturadas
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(Scs_Asistencia.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and pkg_siga_facturacion_sjcs.FUNC_EXISTE_ACTUACION_JUSTIF(Scs_Asistencia.Idinstitucion,Scs_Asistencia.ANIO,Scs_Asistencia.NUMERO)>0'; -- con actuaciones justificadas.

    /*Cursores que utilizaremos para cargar la matriz de memoria m_apunte_ac de CG*/
    c_obtener_actuacionesFact varchar2(4000) := 'select ac.idinstitucion,' ||
        ' ac.anio,' ||
        ' ac.numero,' ||
        ' ac.idactuacion,' ||
        ' 1 facturado,' ||
        ' nvl(pkg_siga_facturacion_sjcs.func_costefijo(ac.idinstitucion,A.IDTURNO,A.IDGUARDIA,ac.anio,ac.numero,ac.idactuacion,TRUNC(A.FECHAHORA), NULL),0) costefijo' ||
        ' from SCS_ASISTENCIA A, SCS_ACTUACIONASISTENCIA AC' ||
        ' where a.idinstitucion = ac.idinstitucion' ||
        ' and a.anio = ac.anio' ||
        ' and a.numero = ac.numero' ||
        ' and a.idinstitucion = @P_IDINSTITUCION@' ||
        ' and a.idturno = @P_IDTURNO@' ||
        ' and a.idguardia = @P_IDGUARDIA@' ||
        ' and a.idpersonacolegiado = @P_IDPERSONA@' ||
        ' and trunc(a.fechahora) between ''@P_FECHAINICIO@'' and ''@P_FECHAFIN@''' ||
        ' and upper(ac.diadespues) = ''N''' ||
         -- Siguiente linea eliminada porque no funciona bien. Revisar si se puede eliminar el check tambien de otros sitios'
        --'      and nvl(ac.facturado, ''0'') = ''1''
        ' and exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = ac.idinstitucion' ||
        '  and fac.anio = ac.anio' ||
        '  and fac.numero = ac.numero' ||
        '  and fac.idactuacion = ac.idactuacion)' ||
        ' and pkg_siga_facturacion_sjcs.fun_esdiaaplicable(a.fechahora,''@P_DIASAPLICABLES@'') = 1';

    c_obtener_actuacionesnuevas varchar2(4000) := 'select ac.idinstitucion,' ||
        ' ac.anio,' ||
        ' ac.numero,' ||
        ' ac.idactuacion,' ||
        ' 0 facturado,' ||
        ' nvl(pkg_siga_facturacion_sjcs.func_costefijo(ac.idinstitucion,A.IDTURNO,A.IDGUARDIA,ac.anio,ac.numero,ac.idactuacion,TRUNC(A.FECHAHORA), NULL),0) costefijo' ||
        ' from SCS_ASISTENCIA A, SCS_ACTUACIONASISTENCIA AC' ||
        ' where a.idinstitucion = ac.idinstitucion' ||
        ' and a.anio = ac.anio' ||
        ' and a.numero = ac.numero' ||
        ' and a.idinstitucion = @P_IDINSTITUCION@' ||
        ' and a.idturno = @P_IDTURNO@' ||
        ' and a.idguardia = @P_IDGUARDIA@' ||
        ' and a.idpersonacolegiado = @P_IDPERSONA@' ||
        ' and trunc(a.fechahora) between ''@P_FECHAINICIO@'' and ''@P_FECHAFIN@''' ||
        ' and upper(ac.diadespues) = ''N''' ||
        -- Siguiente linea eliminada porque no funciona bien. Revisar si se puede eliminar el check tambien de otros sitios
        --'  and nvl(ac.facturado, ''0'') = ''0''
        ' and not exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = ac.idinstitucion' ||
        '  and fac.anio = ac.anio' ||
        '  and fac.numero = ac.numero' ||
        '  and fac.idactuacion = ac.idactuacion)' ||
        ' and nvl(ac.anulacion, ''0'') = ''0''' ||
        ' and ac.fechajustificacion is not null' ||
        ' and trunc(ac.fechajustificacion) between ''@P_FECHAINIFACT@'' and ''@P_FECHAFINFACT@''' ||
        ' and ac.validada = ''1''' ||
        ' and pkg_siga_facturacion_sjcs.fun_esdiaaplicable(a.fechahora,''@P_DIASAPLICABLES@'') = 1';

    /*Cursores que utilizaremos para cargar la matriz de memoria m_apunte_ac de UG*/
    c_obtener_actuacionesFactUG varchar2(4000) := 'select Ac.idinstitucion,' ||
        ' Ac.anio,' ||
        ' Ac.numero,' ||
        ' Ac.idactuacion,' ||
        ' 1 facturado,' ||
        ' nvl(pkg_siga_facturacion_sjcs.func_costefijo(Ac.idinstitucion,A.IDTURNO,A.IDGUARDIA,Ac.anio,Ac.numero,Ac.idactuacion,TRUNC(A.FECHAHORA), NULL),0) costefijo' ||
        ' from Scs_Asistencia A, scs_actuacionasistencia Ac' ||
        ' where A.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and A.Idturno=@P_IDTURNO@' ||
        ' and A.Idguardia=@P_IDGUARDIA@' ||
        ' and A.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(A.Fechahora) =''@P_FECHAFIN@''' ||
        ' and A.IDINSTITUCION=Ac.Idinstitucion' ||
        ' and A.Anio=Ac.Anio' ||
        ' and A.Numero=Ac.Numero' ||
        --' and Ac.Facturado=''1''' ||
        ' and exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = Ac.idinstitucion' ||
        '  and fac.anio = Ac.anio' ||
        '  and fac.numero = Ac.numero' ||
        '  and fac.idactuacion = Ac.idactuacion)' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(A.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and Ac.diadespues=''N''';

    c_obtener_actuacionesnuevasUG varchar2(4000) := ' select Ac.idinstitucion,' ||
        ' Ac.anio,' ||
        ' Ac.numero,' ||
        ' Ac.idactuacion, ' ||
        ' 0 facturado,' ||
        ' nvl(pkg_siga_facturacion_sjcs.func_costefijo(Ac.idinstitucion,A.IDTURNO,A.IDGUARDIA,Ac.anio,Ac.numero,Ac.idactuacion,TRUNC(A.FECHAHORA), NULL),0) costefijo' ||
        ' from Scs_Asistencia A, scs_actuacionasistencia Ac' ||
        ' where A.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and A.Idturno=@P_IDTURNO@' ||
        ' and A.Idguardia=@P_IDGUARDIA@' ||
        ' and A.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(A.Fechahora) =''@P_FECHAFIN@''' ||
        ' and A.IDINSTITUCION=Ac.Idinstitucion' ||
        ' and A.Anio=Ac.Anio' ||
        ' and A.Numero=Ac.Numero' ||
        --' and (Ac.Facturado is null or Ac.Facturado = ''0'')' ||
        ' and not exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = Ac.idinstitucion' ||
        '  and fac.anio = Ac.anio' ||
        '  and fac.numero = Ac.numero' ||
        '  and fac.idactuacion = Ac.idactuacion)' ||
        ' and (Ac.Anulacion is null or Ac.Anulacion=''0'')' ||
        ' and Ac.Fechajustificacion is not null ' ||
        ' and trunc(Ac.Fechajustificacion) between ''@P_FECHAINIFACT@'' and ''@P_FECHAFINFACT@''' ||
        ' and Ac.Validada=''1''' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(A.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and Ac.diadespues=''N''';

    c_obtener_actFactTipoUG varchar2(4000) := 'select Ac.idinstitucion,' ||
        ' Ac.anio,' ||
        ' Ac.numero,' ||
        ' Ac.idactuacion,' ||
        ' 1 facturado,' ||
        ' nvl(pkg_siga_facturacion_sjcs.func_costefijo(Ac.idinstitucion,A.IDTURNO,A.IDGUARDIA,Ac.anio,Ac.numero,Ac.idactuacion,TRUNC(A.FECHAHORA), NULL),0) costefijo' ||
        ' from Scs_Asistencia A, scs_actuacionasistencia Ac' ||
        ' where A.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and A.Idturno=@P_IDTURNO@' ||
        ' and A.Idguardia=@P_IDGUARDIA@' ||
        ' and A.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(A.Fechahora) =''@P_FECHAFIN@''' ||
        ' and A.IDINSTITUCION=Ac.Idinstitucion' ||
        ' and A.Anio=Ac.Anio' ||
        ' and A.Numero=Ac.Numero' ||
        --'  and  Ac.Facturado=''1''' ||
        ' and exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = Ac.idinstitucion' ||
        '  and fac.anio = Ac.anio' ||
        '  and fac.numero = Ac.numero' ||
        '  and fac.idactuacion = Ac.idactuacion)' ||
        ' and Ac.diadespues=''N''' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(A.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and A.idtipoasistenciacolegio=@P_IDTIPOASIST@';

    c_obtener_actnuevasTipoUG varchar2(4000) := 'select Ac.idinstitucion,' ||
        ' Ac.anio,' ||
        ' Ac.numero,' ||
        ' Ac.idactuacion,' ||
        ' 0 facturado,' ||
        ' nvl(pkg_siga_facturacion_sjcs.func_costefijo(Ac.idinstitucion,A.IDTURNO,A.IDGUARDIA,Ac.anio,Ac.numero,Ac.idactuacion,TRUNC(A.FECHAHORA), NULL),0) costefijo' ||
        ' from Scs_Asistencia A, scs_actuacionasistencia Ac ' ||
        ' where A.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and A.Idturno=@P_IDTURNO@' ||
        ' and A.Idguardia=@P_IDGUARDIA@' ||
        ' and A.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(A.Fechahora) =''@P_FECHAFIN@''' ||
        ' and A.IDINSTITUCION=Ac.Idinstitucion' ||
        ' and A.Anio=Ac.Anio' ||
        ' and A.Numero=Ac.Numero' ||
        --' and (Ac.Facturado is null or Ac.Facturado = ''0'')' ||
        ' and not exists(select 1' ||
        '  from FCS_FACT_ACTUACIONASISTENCIA FAC' ||
        '  where fac.idinstitucion = Ac.idinstitucion' ||
        '  and fac.anio = Ac.anio' ||
        '  and fac.numero = Ac.numero' ||
        '  and fac.idactuacion = Ac.idactuacion)' ||
        ' and (Ac.Anulacion is null or Ac.Anulacion=''0'')' ||
        ' and Ac.Fechajustificacion is not null ' ||
        ' and trunc(Ac.Fechajustificacion) between ''@P_FECHAINIFACT@'' and ''@P_FECHAFINFACT@''' ||
        ' and Ac.Validada=1' ||
        ' and Ac.diadespues=''N''' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(A.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and A.idtipoasistenciacolegio=@P_IDTIPOASIST@';

     /*Cursores que utilizaremos para cargar la matriz de memoria m_apunte_as de UG cuando facturamos por asistencia y no aplicamos tipos*/
    c_obtener_asistenciasUG varchar2(4000) := ' select idinstitucion, anio, numero, NVL(Scs_Asistencia.Facturado, ''0'') facturado' ||
        ' from Scs_Asistencia' ||
        ' where Scs_Asistencia.Idinstitucion=@P_IDINSTITUCION@' ||
        ' and Scs_Asistencia.Idturno=@P_IDTURNO@' ||
        ' and Scs_Asistencia.Idguardia=@P_IDGUARDIA@' ||
        ' and Scs_Asistencia.Idpersonacolegiado=@P_IDPERSONA@' ||
        ' and trunc(Scs_Asistencia.Fechahora) =''@P_FECHAFIN@''' ||
        ' and (Scs_Asistencia.Facturado=''1'' OR (NVL(Scs_Asistencia.Facturado, ''0'')=''0'' AND Scs_Asistencia.Fechaanulacion is null))' ||
        ' and pkg_siga_facturacion_sjcs.FUN_ESDIAAPLICABLE(Scs_Asistencia.Fechahora,''@P_DIASAPLICABLES@'')=1' ||
        ' and pkg_siga_facturacion_sjcs.FUNC_EXISTE_ACTUACION_JUSTIF(Scs_Asistencia.Idinstitucion,Scs_Asistencia.ANIO,Scs_Asistencia.NUMERO)>0';

    /***************************************************************************/
    --Obtenemos las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin buscando solo los apuntes que NO son TP ni FG
    CURSOR UNIDADES_GUARDIA(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAINICIO DATE, V_DIASAPLICABLES VARCHAR2) IS
        Select Gc.Idinstitucion,
            Gc.Idturno,
            Gc.Idguardia,
            Gc.Idpersona,
            Gc.Fechainicio,
            Gc.Fechafin,
            Gc.Facturado,
            Sum(Nvl(Fgc.Precioaplicado, 0)) Importefacturado,
            --INI: Cambio para Asistencias que derivan en Designacion
            f_Tiene_Asist_Derivadas(Gc.Idinstitucion, Gc.Idturno, Gc.Idguardia, Gc.Idpersona, Trunc(Gc.Fechafin), Trunc(Gc.Fechafin), v_Datos_Facturacion.Fechadesde, v_Datos_Facturacion.Fechahasta) As Tiene_Asistquederivan
            --FIN: Cambio para Asistencias que derivan en Designacion
        From Scs_Guardiascolegiado Gc,
            (
                Select *
                From Fcs_Fact_Guardiascolegiado Fgc2
                Where Upper(Fgc2.Motivo) Not Like '%TP%' -- ignorando los apuntes de tipo
                    And Upper(Fgc2.Motivo) Not Like '%FG%' -- ignorando los apuntes de FG
            ) Fgc
        Where Gc.Idinstitucion = Fgc.Idinstitucion(+)
            And Gc.Idturno = Fgc.Idturno(+)
            And Gc.Idguardia = Fgc.Idguardia(+)
            And Gc.Idpersona = Fgc.Idpersona(+)
            And Gc.Fechainicio = Fgc.Fechainicio(+)
            And Gc.Fechafin = Fgc.Fechafin(+)
            And Gc.Idinstitucion = v_Idinstitucion
            And Gc.Idturno = v_Idturno
            And Gc.Idguardia = v_Idguardia
            And Gc.Idpersona = v_Idpersona
            And Trunc(Gc.Fechainicio) = v_Fechainicio
            And Fun_Esdiaaplicable(Gc.Fechafin, v_Diasaplicables) = 1
        Group By Gc.Idinstitucion,
            Gc.Idturno,
            Gc.Idguardia,
            Gc.Idpersona,
            Gc.Fechainicio,
            Gc.Fechafin,
            Gc.Facturado
        Order By Gc.Fechafin;

    --Obtenemos las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin buscando solo los apuntes que son TP
    CURSOR UNIDADES_GUARDIA_TP(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAINICIO DATE, V_DIASAPLICABLES VARCHAR2) IS
        Select Gc.Idinstitucion,
            Gc.Idturno,
            Gc.Idguardia,
            Gc.Idpersona,
            Gc.Fechainicio,
            Gc.Fechafin,
            Gc.Facturado,
            -- RGG cambio max(NVL(fgc.precioaplicado, 0)) IMPORTEFACTURADO,
            Sum(Nvl(Fgc.Precioaplicado, 0)) Importefacturado,
            --INI: Cambio para Asistencias que derivan en Designacion
            f_Tiene_Asist_Derivadas(Gc.Idinstitucion, Gc.Idturno, Gc.Idguardia, Gc.Idpersona, Trunc(Gc.Fechafin), Trunc(Gc.Fechafin), v_Datos_Facturacion.Fechadesde, v_Datos_Facturacion.Fechahasta) As Tiene_Asistquederivan
            --FIN: Cambio para Asistencias que derivan en Designacion
        From Scs_Guardiascolegiado Gc,
            (
                Select *
                From Fcs_Fact_Guardiascolegiado Fgc2
                --           where upper(fgc2.motivo) like '%TP%' -- solo los apuntes de tipo
                Where Upper(Fgc2.Motivo) Not Like '%TP%' -- solo los apuntes de día
            ) Fgc
        Where Gc.Idinstitucion = Fgc.Idinstitucion(+)
            And Gc.Idturno = Fgc.Idturno(+)
            And Gc.Idguardia = Fgc.Idguardia(+)
            And Gc.Idpersona = Fgc.Idpersona(+)
            And Gc.Fechainicio = Fgc.Fechainicio(+)
            And Gc.Fechafin = Fgc.Fechafin(+)
            And Gc.Idinstitucion = v_Idinstitucion
            And Gc.Idturno = v_Idturno
            And Gc.Idguardia = v_Idguardia
            And Gc.Idpersona = v_Idpersona
            And Trunc(Gc.Fechainicio) = v_Fechainicio
            And Fun_Esdiaaplicable(Gc.Fechafin, v_Diasaplicables) = 1
        Group By Gc.Idinstitucion,
            Gc.Idturno,
            Gc.Idguardia,
            Gc.Idpersona,
            Gc.Fechainicio,
            Gc.Fechafin,
            Gc.Facturado
        Order By Gc.Fechafin;

    -- Obtenemos los distintos tipos de asistencias
    CURSOR C_TIPOASISTENCIAS_UG(V_IDINSTITUCION number, V_IDTURNO number, V_IDGUARDIA number, V_IDPERSONA number, V_FECHAINICIO date, V_FECHAFIN date, V_DIASAPLICABLES varchar2) IS (
        select distinct idtipoasistenciacolegio tipoasist
        from Scs_Asistencia
        where Scs_Asistencia.Idinstitucion = V_IDINSTITUCION
            and Scs_Asistencia.Idturno = V_IDTURNO
            and Scs_Asistencia.Idguardia = V_IDGUARDIA
            and Scs_Asistencia.Idpersonacolegiado = V_IDPERSONA
            and trunc(Scs_Asistencia.Fechahora) = V_FECHAFIN
            and Scs_Asistencia.Facturado = '1'
            and FUN_ESDIAAPLICABLE(Scs_Asistencia.Fechahora, V_DIASAPLICABLES) = 1
        UNION
        select distinct idtipoasistenciacolegio tipoasist
        from Scs_Asistencia
        where Scs_Asistencia.Idinstitucion = V_IDINSTITUCION
            and Scs_Asistencia.Idturno = V_IDTURNO
            and Scs_Asistencia.Idguardia = V_IDGUARDIA
            and Scs_Asistencia.Idpersonacolegiado = V_IDPERSONA
            and trunc(Scs_Asistencia.Fechahora) = V_FECHAFIN
            and Scs_Asistencia.Fechaanulacion is null --no anulada
            and (Scs_Asistencia.Facturado is null or Scs_Asistencia.Facturado = '0')
            and FUNC_EXISTE_ACTUACION_JUSTIF(Scs_Asistencia.Idinstitucion, Scs_Asistencia.ANIO, Scs_Asistencia.NUMERO) > 0
            and FUN_ESDIAAPLICABLE(Scs_Asistencia.Fechahora, V_DIASAPLICABLES) = 1)
        order by tipoasist;

    -- Obtenemos las asistencias realizadas y justificadas anteriormente a la fecha fin del ciclo de facturacion ordenadas por fecha de realizacion (Este cursor se utiliza para la facturacion por asistencias)
    CURSOR C_ASISTENCIAS_UG (V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAINICIO DATE, V_FECHAFIN DATE, V_DIASAPLICABLES VARCHAR2) IS (
        SELECT ASI.IDTIPOASISTENCIACOLEGIO,
            ASI.FECHAHORA,
            ASI.IDINSTITUCION,
            ASI.ANIO,
            ASI.NUMERO,
            1 AS FACTURADO,
            ( -- COMPRUEBO SI TIENE ACTUACIONES SIN FACTURAR
                SELECT COUNT(*)
                FROM SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = ASI.IDINSTITUCION
                    AND ACT.ANIO = ASI.ANIO
                    AND ACT.NUMERO = ASI.NUMERO
                    AND NOT EXISTS (
                        SELECT 1
                        FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                        WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION
                    )
            ) AS NUMACTNOFAC
        FROM SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND ASI.FACTURADO = '1'
            AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1
        UNION
        SELECT ASI.IDTIPOASISTENCIACOLEGIO,
           ASI.FECHAHORA,
           ASI.IDINSTITUCION,
           ASI.ANIO,
           ASI.NUMERO,
           0 AS FACTURADO,
            ( -- COMPRUEBO SI TIENE ACTUACIONES SIN FACTURAR
                SELECT COUNT(*)
                FROM SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = ASI.IDINSTITUCION
                    AND ACT.ANIO = ASI.ANIO
                    AND ACT.NUMERO = ASI.NUMERO
                    AND NOT EXISTS (
                        SELECT 1
                        FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                        WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION
                    )
            ) AS NUMACTNOFAC
        FROM SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND ASI.FECHAANULACION IS NULL --no anulada
            AND (ASI.FACTURADO IS NULL OR ASI.FACTURADO = '0')
            AND FUNC_EXISTE_ACTUACION_JUSTIF(ASI.IDINSTITUCION, ASI.ANIO, ASI.NUMERO) > 0
            AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1)
        ORDER BY IDTIPOASISTENCIACOLEGIO, FECHAHORA;

    -- Obtenemos los distintos tipos de actuaciones
    CURSOR C_TIPOACTUACIONES_UG(V_IDINSTITUCION number, V_IDTURNO number, V_IDGUARDIA number, V_IDPERSONA number, V_FECHAFIN date, V_DIASAPLICABLES varchar2) IS(
        select AC.IDTIPOACTUACION TIPOACTUACION
        from Scs_Asistencia A, scs_actuacionasistencia Ac
        where A.Idinstitucion = V_IDINSTITUCION
            and A.Idturno = V_IDTURNO
            and A.Idguardia = V_IDGUARDIA
            and A.Idpersonacolegiado = V_IDPERSONA
            and A.Fechahora = V_FECHAFIN
            and A.IDINSTITUCION = Ac.Idinstitucion
            and A.Anio = Ac.Anio
            and A.Numero = Ac.Numero
            and Ac.Facturado = '1'
            and Ac.diadespues = 'N'
            and FUN_ESDIAAPLICABLE(A.Fechahora, V_DIASAPLICABLES) = 1
        UNION
        select AC.IDTIPOACTUACION TIPOACTUACION
        from Scs_Asistencia A, scs_actuacionasistencia Ac
        where A.Idinstitucion = V_IDINSTITUCION
            and A.Idturno = V_IDTURNO
            and A.Idguardia = V_IDGUARDIA
            and A.Idpersonacolegiado = V_IDPERSONA
            and A.Fechahora = V_FECHAFIN
            and A.IDINSTITUCION = Ac.Idinstitucion
            and A.Anio = Ac.Anio
            and A.Numero = Ac.Numero
            --and (Ac.Facturado is null or Ac.Facturado = '0')
            and not exists( 
                select 1
                from FCS_FACT_ACTUACIONASISTENCIA FAC
                where fac.idinstitucion = Ac.idinstitucion
                and fac.anio = Ac.anio
                and fac.numero = Ac.numero
                and fac.idactuacion = Ac.idactuacion)
                and (Ac.Anulacion is null or Ac.Anulacion = '0')
                and Ac.Fechajustificacion is not null
                and trunc(Ac.Fechajustificacion) between trunc(V_DATOS_FACTURACION.FECHADESDE) and trunc(V_DATOS_FACTURACION.FECHAHASTA)
                and Ac.Validada = 1
                and Ac.diadespues = 'N'
                and FUN_ESDIAAPLICABLE(A.Fechahora, V_DIASAPLICABLES) = 1)
            ORDER BY TIPOACTUACION;

    -- CURSORES PARA ACFG (no aplica tipos)
    cursor C_GUARDIAS_ACFG (
        P_IDINSTITUCION SCS_CABECERAGUARDIAS.IDINSTITUCION%TYPE,
        P_IDTURNO SCS_CABECERAGUARDIAS.IDTURNO%TYPE,
        P_IDGUARDIA SCS_CABECERAGUARDIAS.IDGUARDIA%TYPE,
        P_FECHAINICIO SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE,
        P_IDPERSONA SCS_CABECERAGUARDIAS.IDPERSONA%TYPE) IS (
        select guacol.idinstitucion as IDINSTITUCION,
            guacol.idturno as IDTURNO,
            guacol.idguardia as IDGUARDIA,
            guacol.idpersona as IDPERSONA,
            guacol.fechainicio as FECHAINICIO,
            guacol.fechafin as FECHAFIN
        from SCS_GUARDIASCOLEGIADO GUACOL
        where guacol.idinstitucion = P_IDINSTITUCION
            and guacol.idturno = P_IDTURNO
            and guacol.idguardia = P_IDGUARDIA
            and guacol.fechainicio = P_FECHAINICIO
            and guacol.idpersona = P_IDPERSONA
            and exists(
                select 1
                from SCS_ASISTENCIA ASI, SCS_ACTUACIONASISTENCIA ACTASI
                where asi.idinstitucion = actasi.idinstitucion
                    and asi.anio = actasi.anio
                    and asi.numero = actasi.numero
                    and guacol.idinstitucion = asi.idinstitucion
                    and guacol.idturno = asi.idturno
                    and guacol.idguardia = asi.idguardia
                    and guacol.fechafin = trunc(asi.fechahora)
                    and guacol.idpersona = asi.idpersonacolegiado
                    and actasi.fechajustificacion between trunc(V_DATOS_FACTURACION.FECHADESDE) and trunc(V_DATOS_FACTURACION.FECHAHASTA)
                    and nvl(actasi.anulacion, '0') = '0'
                    and actasi.diadespues = 'S'
                    and not exists(
                        select 1
                        from FCS_FACT_ACTUACIONASISTENCIA FACTASI
                        where actasi.idinstitucion = factasi.idinstitucion
                            and actasi.anio = factasi.anio
                            and actasi.numero = factasi.numero
                            and actasi.idactuacion = factasi.idactuacion)));

    CURSOR C_ASISTENCIAS_ACFG (
        P_IDINSTITUCION SCS_GUARDIASCOLEGIADO.IDINSTITUCION%TYPE,
        P_IDTURNO SCS_GUARDIASCOLEGIADO.IDTURNO%TYPE,
        P_IDGUARDIA SCS_GUARDIASCOLEGIADO.IDGUARDIA%TYPE,
        P_FECHAFIN SCS_GUARDIASCOLEGIADO.FECHAFIN%TYPE,
        P_IDPERSONA SCS_GUARDIASCOLEGIADO.IDPERSONA%TYPE) IS (
        select asi.idinstitucion as IDINSTITUCION,
            asi.anio as ANIO,
            asi.numero as NUMERO
        from SCS_ASISTENCIA ASI
        where asi.idinstitucion = P_IDINSTITUCION
            and asi.idturno = P_IDTURNO
            and asi.idguardia = P_IDGUARDIA
            and trunc(asi.fechahora) = P_FECHAFIN
            and asi.idpersonacolegiado = P_IDPERSONA
            and exists(
                select 1
                from SCS_ACTUACIONASISTENCIA ACTASI
                where asi.idinstitucion = actasi.idinstitucion
                    and asi.anio = actasi.anio
                    and asi.numero = actasi.numero
                    and actasi.fechajustificacion between trunc(V_DATOS_FACTURACION.FECHADESDE) and trunc(V_DATOS_FACTURACION.FECHAHASTA)
                    and nvl(actasi.anulacion, '0') = '0'
                    and actasi.diadespues = 'S' -- solo las del día después.
                    and not exists( -- solo las NO FACTURADAS
                        select 1
                        from FCS_FACT_ACTUACIONASISTENCIA FACTASI
                        where actasi.idinstitucion = factasi.idinstitucion
                            and actasi.anio = factasi.anio
                            and actasi.numero = factasi.numero
                            and actasi.idactuacion = factasi.idactuacion)));

    -- Obtenemos los diferentes dias que se efectuan las actuaciones fuera de guardia y justificadas en el ciclo de facturacion y agrupadas por Tipo Actuacion y ordenadas por fecha de realizacin
    CURSOR C_DIAS_FG(V_IDINSTITUCION number, V_IDTURNO number, V_IDGUARDIA number, V_IDPERSONA number, V_FECHAINICIO_CG date, V_FECHAFIN_CG date) IS
        select Distinct Ac.fecha FECHA, '0' as facturado
        from Scs_Asistencia A, scs_actuacionasistencia Ac
        where A.Idinstitucion = V_IDINSTITUCION
            and A.Idturno = V_IDTURNO
            and A.Idguardia = V_IDGUARDIA
            and A.Idpersonacolegiado = V_IDPERSONA
            and trunc(A.Fechahora) BETWEEN trunc(V_FECHAINICIO_CG) AND trunc(V_FECHAFIN_CG)
            and A.IDINSTITUCION = Ac.Idinstitucion
            and A.Anio = Ac.Anio
            and A.Numero = Ac.Numero
            --and (Ac.Facturado is null or Ac.Facturado = '0')
            and not exists(
                select 1
                from FCS_FACT_ACTUACIONASISTENCIA FAC
                where fac.idinstitucion = Ac.idinstitucion
                and fac.anio = Ac.anio
                and fac.numero = Ac.numero
                and fac.idactuacion = Ac.idactuacion)
            and (Ac.Anulacion is null or Ac.Anulacion = '0')
            and Ac.Fechajustificacion is not null 
            and trunc(Ac.Fechajustificacion) between trunc(V_DATOS_FACTURACION.FECHADESDE) and trunc(V_DATOS_FACTURACION.FECHAHASTA) 
            and Ac.Validada = 1
            and Ac.diadespues = 'S' -- DIAS FUERA DE GUARDIA
        ORDER BY FECHA;

    -- Obtenemos los distintos tipos de actuaciones
    CURSOR C_TIPOACTUACIONES_FG(V_IDINSTITUCION number, V_IDTURNO number, V_IDGUARDIA number, V_IDPERSONA number, V_FECHAINICIO_CG date, V_FECHAFIN_CG date, V_FECHA DATE) IS
        select distinct AC.IDTIPOACTUACION TIPOACTUACION
        from Scs_Asistencia A, scs_actuacionasistencia Ac
        where A.Idinstitucion = V_IDINSTITUCION
            and A.Idturno = V_IDTURNO
            and A.Idguardia = V_IDGUARDIA
            and A.Idpersonacolegiado = V_IDPERSONA
            and trunc(A.Fechahora) BETWEEN trunc(V_FECHAINICIO_CG) AND trunc(V_FECHAFIN_CG)
            and A.IDINSTITUCION = Ac.Idinstitucion
            and A.Anio = Ac.Anio
            and A.Numero = Ac.Numero
            --and (Ac.Facturado is null or Ac.Facturado = '0')
            and not exists(
                select 1
                from FCS_FACT_ACTUACIONASISTENCIA FAC
                where fac.idinstitucion = Ac.idinstitucion
                    and fac.anio = Ac.anio
                    and fac.numero = Ac.numero
                    and fac.idactuacion = Ac.idactuacion)
            and (Ac.Anulacion is null or Ac.Anulacion = '0')
            and Ac.Fechajustificacion is not null
            and trunc(Ac.Fechajustificacion) between trunc(V_DATOS_FACTURACION.FECHADESDE) and trunc(V_DATOS_FACTURACION.FECHAHASTA) 
            and Ac.Validada = 1
            and Ac.diadespues = 'S'
            and Ac.fecha = V_FECHA
        ORDER BY TIPOACTUACION;

  /**********************************************************
  * JPT - INICIO - CODIGO PARA LA FACTURACION CONTROLADA EN CANTABRIA *
  **********************************************************/

    CURSOR UNIDADES_GUARDIA_CONTROLADO(
        V_IDINSTITUCION NUMBER,
        V_IDTURNO NUMBER,
        V_IDGUARDIA NUMBER,
        V_IDPERSONA NUMBER,
        V_FECHAINICIO DATE,
        V_DIASAPLICABLES VARCHAR2
    ) IS
        SELECT GC.IDINSTITUCION,
               GC.IDTURNO,
               GC.IDGUARDIA,
               GC.IDPERSONA,
               GC.FECHAINICIO,
               GC.FECHAFIN,
               SUM(NVL(FGC.PRECIOAPLICADO,0)) AS PRECIOAPLICADO,
               DECODE(FGC.IDFACTURACION, NULL, 0, 1) AS FACTURADO
        FROM SCS_GUARDIASCOLEGIADO GC,
            FCS_FACT_GUARDIASCOLEGIADO FGC
        WHERE GC.IDINSTITUCION = FGC.IDINSTITUCION(+)
            AND GC.IDTURNO = FGC.IDTURNO(+)
            AND GC.IDGUARDIA = FGC.IDGUARDIA(+)
            AND GC.IDPERSONA = FGC.IDPERSONA(+)
            AND GC.FECHAINICIO = FGC.FECHAINICIO(+)
            AND GC.FECHAFIN = FGC.FECHAFIN(+)
            AND 1 = FGC.IDTIPO(+) -- Solo toma los tipos que son sumatorios
            AND GC.IDINSTITUCION = V_IDINSTITUCION
            AND GC.IDTURNO = V_IDTURNO
            AND GC.IDGUARDIA = V_IDGUARDIA
            AND GC.IDPERSONA = V_IDPERSONA
            AND TRUNC(GC.FECHAINICIO) = V_FECHAINICIO
            AND FUN_ESDIAAPLICABLE(GC.FECHAFIN, V_DIASAPLICABLES) = 1
         GROUP BY GC.IDINSTITUCION,
                  GC.IDTURNO,
                  GC.IDGUARDIA,
                  GC.IDPERSONA,
                  GC.FECHAINICIO,
                  GC.FECHAFIN,
                  DECODE(FGC.IDFACTURACION, NULL, 0, 1)
         ORDER BY GC.FECHAFIN;

    /* Este cursor devuelve todas las asistencias que cumplen las siguientes condiciones:
        - No esta anulada
        - Se realiza en el dia de la guardia
        - Se realiza en un dia aplicable de los indicados en la configuracion de la guardia (LMXJVSD)
        - Tiene almenos una actuacion, sin facturar, justificada dentro del periodo de facturacion
    */
    CURSOR C_ASISTENCIAS_UG_CONTROLADO(
        V_IDINSTITUCION NUMBER,
        V_IDTURNO NUMBER,
        V_IDGUARDIA NUMBER,
        V_IDPERSONA NUMBER,
        V_FECHADIA DATE,
        V_FECHAFACTURACIONINICIO DATE,
        V_FECHAFACTURACIONFIN DATE,
        V_DIASAPLICABLES VARCHAR2,
        V_APLICATIPOS NUMBER
    ) IS
        SELECT ASI.IDINSTITUCION,
            ASI.ANIO,
            ASI.NUMERO,
            ASI.IDTIPOASISTENCIACOLEGIO,
            ( -- Compruebo si la asistencia esta facturada
                SELECT COUNT(*)
                FROM FCS_FACT_ASISTENCIA FAC_ASI
                WHERE FAC_ASI.IDINSTITUCION = ASI.IDINSTITUCION -- Relacion FCS_FACT_ASISTENCIA => SCS_ASISTENCIA
                    AND FAC_ASI.ANIO = ASI.ANIO -- Relacion FCS_FACT_ASISTENCIA => SCS_ASISTENCIA
                    AND FAC_ASI.NUMERO = ASI.NUMERO -- Relacion FCS_FACT_ASISTENCIA => SCS_ASISTENCIA
            ) AS NUMASIFAC,
            DECODE(V_APLICATIPOS, 0, NULL, FUNC_CALCULAR_IMPORTEASIST(ASI.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ASI.ANIO, ASI.NUMERO, V_FECHADIA, NULL,0)) AS IMPORTETIPO -- Obtiene el importe del tipo de la asistencia, en el caso de aplicar tipos
          FROM SCS_ASISTENCIA ASI
         WHERE ASI.IDINSTITUCION = V_IDINSTITUCION -- Utiliza indice SI_FK_ASISTENCIA_GUARDIASTURNO
            AND ASI.IDTURNO = V_IDTURNO -- Utiliza indice SI_FK_ASISTENCIA_GUARDIASTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA -- Utiliza indice SI_FK_ASISTENCIA_GUARDIASTURNO
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHADIA -- La asistencia se realiza en el dia de la guardia
            AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1 -- La asistencia se realiza en un dia aplicable en los indicados de la configuracion de la guardia (LMXJVSD)
            AND ASI.FECHAANULACION IS NULL -- Asistencia no anulada
            AND EXISTS ( -- Tiene almenos una actuacion, sin facturar, justificada dentro del periodo de facturacion
                SELECT 1
                FROM SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = ASI.IDINSTITUCION -- Relacion SCS_ASISTENCIA => SCS_ACTUACIONASISTENCIA
                    AND ACT.ANIO = ASI.ANIO -- Relacion SCS_ASISTENCIA => SCS_ACTUACIONASISTENCIA
                    AND ACT.NUMERO = ASI.NUMERO -- Relacion SCS_ASISTENCIA => SCS_ACTUACIONASISTENCIA
                    AND NVL(ACT.ANULACION, '0') = '0' -- Actuacion sin anular
                    AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN V_FECHAFACTURACIONINICIO AND V_FECHAFACTURACIONFIN -- Actuacion justificada dentro del periodo de facturacion
                    AND ACT.VALIDADA='1' -- Actuacion validada
                    AND ACT.DIADESPUES='N' -- No es dia despues
                    AND NOT EXISTS ( -- Actuacion sin facturar
                        SELECT 1
                        FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                        WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                            AND FAC_ACT.ANIO = ACT.ANIO -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                            AND FAC_ACT.NUMERO = ACT.NUMERO -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                            AND FAC_ACT.IDACTUACION = ACT.IDACTUACION -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                    )
            )
         ORDER BY IDTIPOASISTENCIACOLEGIO, FECHAHORA;

    /* Este cursor devuelve todas las actuaciones que cumplen las siguientes condiciones:
        - Sin anular
        - Justificada dentro del periodo de facturacion
        - Validada
        - No es dia despues
        - Sin facturar
    */
     CURSOR C_ACTUACIONES_ASIS_CONTROLADO(
        V_IDINSTITUCION NUMBER,
        V_ANIO NUMBER,
        V_NUMERO NUMBER,
        V_FECHAFACTURACIONINICIO DATE,
        V_FECHAFACTURACIONFIN DATE
    ) IS
        SELECT ACT.IDINSTITUCION,
            ACT.ANIO,
            ACT.NUMERO,
            ACT.IDACTUACION,
            NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ASI.FECHAHORA), NULL), 0) AS COSTEFIJO
        FROM SCS_ACTUACIONASISTENCIA ACT, SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = ACT.IDINSTITUCION
            AND ASI.ANIO = ACT.ANIO
            AND ASI.NUMERO = ACT.NUMERO
            AND ACT.IDINSTITUCION = V_IDINSTITUCION -- Utiliza el indice de la PK
            AND ACT.ANIO = V_ANIO -- Utiliza el indice de la PK
            AND ACT.NUMERO = V_NUMERO -- Utiliza el indice de la PK
            AND NVL(ACT.ANULACION, '0') = '0' -- Actuacion sin anular
            AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN V_FECHAFACTURACIONINICIO AND V_FECHAFACTURACIONFIN -- Actuacion justificada dentro del periodo de facturacion
            AND ACT.VALIDADA='1' -- Actuacion validada
            AND ACT.DIADESPUES='N' -- No es dia despues
            AND NOT EXISTS ( -- Actuacion sin facturar
                SELECT 1
                FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                    AND FAC_ACT.ANIO = ACT.ANIO -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                    AND FAC_ACT.NUMERO = ACT.NUMERO -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
                    AND FAC_ACT.IDACTUACION = ACT.IDACTUACION -- Relacion SCS_ACTUACIONASISTENCIA => FCS_FACT_ACTUACIONASISTENCIA
            );            
            
  /****************************************************************************************************************
    Nombre: FUNC_ES_COLEGIO_CATALAN
    Descripcion: Funcion que nos indica si es un colegio catalan

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/            
    FUNCTION FUNC_ES_COLEGIO_CATALAN (P_IDINSTITUCION IN CEN_INSTITUCION.IDINSTITUCION%TYPE) RETURN BOOLEAN IS
        V_CONSEJO CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE;
    BEGIN
    
        -- JPT_PRUEBAS
        RETURN TRUE;
    
        IF (V_CONFIG_GUARDIA.CONSEJOINSTITUCION = C_CATALAN) THEN
            RETURN TRUE;
        END IF;                            
    
        SELECT CEN_INST_IDINSTITUCION
            INTO V_CONSEJO
        FROM CEN_INSTITUCION
        WHERE IDINSTITUCION = P_IDINSTITUCION;
        
        IF (V_CONSEJO = C_CATALAN) THEN
            RETURN TRUE;
        ELSE
            RETURN FALSE;
        END IF;                        
            
        EXCEPTION
            WHEN OTHERS THEN
                RETURN FALSE;
    END FUNC_ES_COLEGIO_CATALAN;
    
  /****************************************************************************************************************
    Nombre: FUNC_OBTENER_IDFACTURACION
    Descripcion: Funcion que busca el IDFACTURACION de una cabecera de guardia (incluye regularizaciones)

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_FECHA - IN - Fecha donde buscar la facturacion - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/    
    FUNCTION FUNC_OBTENER_IDFACTURACION(
        P_IDINSTITUCION IN FCS_HISTORICO_HITOFACT.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_HISTORICO_HITOFACT.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_HISTORICO_HITOFACT.IDGUARDIA%TYPE,
        P_FECHA IN FCS_FACTURACIONJG.FECHADESDE%TYPE) RETURN NUMBER IS
    
        V_IDFACTURACION FCS_FACT_APUNTE.IDFACTURACION%TYPE := NULL;
  
    BEGIN    
        -- Comprueba si es un colegio catalan
        IF (FUNC_ES_COLEGIO_CATALAN(P_IDINSTITUCION) = TRUE) THEN        
            BEGIN
                -- JPT:  Obtiene el mayor de los IDFACTURACION que se han regularizado
                SELECT MAX(FAC.IDFACTURACION) 
                    INTO V_IDFACTURACION
                FROM  FCS_HISTORICO_HITOFACT HIST,
                    FCS_FACT_GRUPOFACT_HITO GF,
                    FCS_FACTURACIONJG FAC
                WHERE HIST.IDINSTITUCION = P_IDINSTITUCION
                    AND HIST.IDTURNO = P_IDTURNO
                    AND HIST.IDGUARDIA = P_IDGUARDIA
                    AND GF.IDINSTITUCION = HIST.IDINSTITUCION
                    AND GF.IDFACTURACION = HIST.IDFACTURACION
                    AND GF.IDHITOGENERAL = PKG_SIGA_CONSTANTES.HITO_GENERAL_GUARDIA
                    AND FAC.IDINSTITUCION = HIST.IDINSTITUCION
                    AND FAC.IDFACTURACION = HIST.IDFACTURACION
                    AND P_FECHA BETWEEN FAC.FECHADESDE AND FAC.FECHAHASTA        
                    AND FAC.REGULARIZACION = 1; -- JPT: Indica que es una facturacion de regularizacion                    
                EXCEPTION
                  WHEN OTHERS THEN
                    V_IDFACTURACION := NULL;
            END;

            IF (V_IDFACTURACION IS NULL) THEN
                BEGIN
                    /* JPT: En caso de no haber regularizaciones, obtiene el IDFACTURACION inicial 
                        - Puede no tener FCS_FACT_APUNTE, si se hizo una facturacion sin las cabeceras de guardias validadas ... y luego se validaron y se pusieron en una facturacion futura*/
                    SELECT MIN(FAC.IDFACTURACION) 
                        INTO V_IDFACTURACION
                    FROM FCS_HISTORICO_HITOFACT HIST,
                        FCS_FACT_GRUPOFACT_HITO GF,
                        FCS_FACTURACIONJG FAC
                    WHERE HIST.IDINSTITUCION = P_IDINSTITUCION
                        AND HIST.IDTURNO = P_IDTURNO
                        AND HIST.IDGUARDIA = P_IDGUARDIA
                        AND GF.IDINSTITUCION = HIST.IDINSTITUCION
                        AND GF.IDFACTURACION = HIST.IDFACTURACION
                        AND GF.IDHITOGENERAL = PKG_SIGA_CONSTANTES.HITO_GENERAL_GUARDIA
                        AND FAC.IDINSTITUCION = HIST.IDINSTITUCION
                        AND FAC.IDFACTURACION = HIST.IDFACTURACION
                        AND P_FECHA BETWEEN FAC.FECHADESDE AND FAC.FECHAHASTA;               
                    EXCEPTION
                      WHEN OTHERS THEN
                        V_IDFACTURACION := NULL;
                END;
            END IF;
        END IF;
        
        RETURN V_IDFACTURACION;

        EXCEPTION
            WHEN OTHERS THEN
                RETURN NULL;
    END FUNC_OBTENER_IDFACTURACION; 
    
  /****************************************************************************************************************
    Nombre: FUNC_COSTEFIJO
    Descripcion: Funcion que nos devuelve el coste fijo de una actuacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER    
    - P_ANIO - IN - Ano de la actuacion - NUMBER
    - P_NUMERO - IN - Numero de la actuacion - NUMBER
    - P_IDACTUACION - IN - Identificador del tipo de actuacion - NUMBER
    - P_FECHA - IN - Fecha de la asistencia - DATE
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 24/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/
    FUNCTION FUNC_COSTEFIJO(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTURNO IN SCS_ASISTENCIA.IDTURNO%TYPE,
        P_IDGUARDIA IN SCS_ASISTENCIA.IDGUARDIA%TYPE,
        P_ANIO IN SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION IN SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        P_FECHA IN SCS_ASISTENCIA.FECHAHORA%TYPE,
        P_IDFACTURACION IN FCS_HISTO_TIPOACTCOSTEFIJO.IDFACTURACION%TYPE) RETURN NUMBER IS
    
        V_IDFACTURACION FCS_HISTO_TIPOACTCOSTEFIJO.IDFACTURACION%TYPE := P_IDFACTURACION;
        V_COSTEFIJO FCS_HISTO_TIPOACTCOSTEFIJO.IMPORTE%TYPE := NULL;
        V_TIENEHISTORICO BOOLEAN := FALSE;
  
    BEGIN    
        IF (P_IDFACTURACION IS NULL) THEN
            -- Busca si tiene un IDFACTURACION previo (incluye tratamiento de regularizaciones)
            V_IDFACTURACION :=  FUNC_OBTENER_IDFACTURACION(P_IDINSTITUCION, P_IDTURNO, P_IDGUARDIA, P_FECHA);
        END IF;            
    
        -- Si obtiene un idFacturacion, hay que obtener el coste fijo del tipo de actuacion del historico
        IF (V_IDFACTURACION IS NOT NULL) THEN
            BEGIN
                SELECT HTACT.IMPORTE 
                    INTO V_COSTEFIJO
                FROM SCS_ACTUACIONASISTENCIA ACT,
                    SCS_ACTUACIONASISTCOSTEFIJO ACTCF,
                    FCS_HISTO_TIPOACTCOSTEFIJO  HTACT
                WHERE ACT.IDINSTITUCION = ACTCF.IDINSTITUCION
                    AND ACT.ANIO = ACTCF.ANIO
                    AND ACT.NUMERO = ACTCF.NUMERO
                    AND ACT.IDACTUACION = ACTCF.IDACTUACION
                    AND HTACT.IDINSTITUCION = ACTCF.IDINSTITUCION
                    AND HTACT.IDTIPOASISTENCIA = ACTCF.IDTIPOASISTENCIA
                    AND HTACT.IDTIPOACTUACION = ACTCF.IDTIPOACTUACION
                    AND HTACT.IDCOSTEFIJO = ACTCF.IDCOSTEFIJO
                    AND HTACT.IDFACTURACION = V_IDFACTURACION
                    AND ACT.IDINSTITUCION = P_IDINSTITUCION
                    AND ACT.ANIO = P_ANIO
                    AND ACT.NUMERO = P_NUMERO
                    AND ACT.IDACTUACION = P_IDACTUACION;
                    
                    V_TIENEHISTORICO := TRUE;                    
                    
                EXCEPTION
                    WHEN OTHERS THEN
                        V_TIENEHISTORICO := FALSE;
            END;                    
        END IF;
            
        -- Si no obtenemos un idFacturacion o no tenia historico, hay que obtener el coste fijo del tipo de actuacion actual
        IF (V_IDFACTURACION IS NULL OR V_TIENEHISTORICO = FALSE) THEN
            SELECT TACT.IMPORTE 
                INTO V_COSTEFIJO
            FROM SCS_ACTUACIONASISTENCIA ACT,
                SCS_ACTUACIONASISTCOSTEFIJO ACTCF,
                SCS_TIPOACTUACIONCOSTEFIJO  TACT
            WHERE ACT.IDINSTITUCION = ACTCF.IDINSTITUCION
                AND ACT.ANIO = ACTCF.ANIO
                AND ACT.NUMERO = ACTCF.NUMERO
                AND ACT.IDACTUACION = ACTCF.IDACTUACION
                AND TACT.IDINSTITUCION = ACTCF.IDINSTITUCION
                AND TACT.IDTIPOASISTENCIA = ACTCF.IDTIPOASISTENCIA
                AND TACT.IDTIPOACTUACION = ACTCF.IDTIPOACTUACION
                AND TACT.IDCOSTEFIJO = ACTCF.IDCOSTEFIJO
                AND ACT.IDINSTITUCION = P_IDINSTITUCION
                AND ACT.ANIO = P_ANIO
                AND ACT.NUMERO = P_NUMERO
                AND ACT.IDACTUACION = P_IDACTUACION;                               
        END IF;                
            
        RETURN V_COSTEFIJO;            

        EXCEPTION
            WHEN OTHERS THEN
                RETURN 0;
    END FUNC_COSTEFIJO;    
    
  /****************************************************************************************************************
    Nombre: FUNC_CALCULAR_IMPORTEASIST
    Descripcion: Funcion que nos devuelve el importe de un tipo de asistencia

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER    
    - P_ANIO - IN - Ano de la actuacion - NUMBER
    - P_NUMERO - IN - Numero de la actuacion - NUMBER
    - P_FECHA - IN - Fecha de la asistencia - DATE
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_TIPO - IN - 0:IMPORTE; 1:IMPORTEMAXIMO; - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 27/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/
    FUNCTION FUNC_CALCULAR_IMPORTEASIST(
        P_IDINSTITUCION IN SCS_ASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTURNO IN SCS_ASISTENCIA.IDTURNO%TYPE,
        P_IDGUARDIA IN SCS_ASISTENCIA.IDGUARDIA%TYPE,
        P_ANIO IN SCS_ASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ASISTENCIA.NUMERO%TYPE,
        P_FECHA IN SCS_ASISTENCIA.FECHAHORA%TYPE,
        P_IDFACTURACION IN FCS_HISTORICO_TIPOASISTCOLEGIO.IDFACTURACION%TYPE,
        P_TIPO IN NUMBER) RETURN NUMBER IS
    
        V_IDFACTURACION FCS_HISTORICO_TIPOASISTCOLEGIO.IDFACTURACION%TYPE := P_IDFACTURACION;
        V_IMPORTE FCS_HISTORICO_TIPOASISTCOLEGIO.IMPORTE%TYPE := NULL;
        V_IMPORTEMAXIMO FCS_HISTORICO_TIPOASISTCOLEGIO.IMPORTEMAXIMO%TYPE := NULL;
        V_TIENEHISTORICO BOOLEAN := FALSE;
  
    BEGIN
        IF (P_IDFACTURACION IS NULL) THEN
            -- Busca si tiene un IDFACTURACION previo (incluye tratamiento de regularizaciones)
            V_IDFACTURACION :=  FUNC_OBTENER_IDFACTURACION(P_IDINSTITUCION, P_IDTURNO, P_IDGUARDIA, P_FECHA);
        END IF;
            
        -- Si obtiene un idFacturacion, hay que obtener el tipo de asistencia del historico
        IF (V_IDFACTURACION IS NOT NULL) THEN
            BEGIN
                SELECT HTASI.IMPORTE, HTASI.IMPORTEMAXIMO
                    INTO V_IMPORTE, V_IMPORTEMAXIMO
                FROM FCS_HISTORICO_TIPOASISTCOLEGIO HTASI, SCS_ASISTENCIA ASI
                WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
                    AND ASI.ANIO = P_ANIO
                    AND ASI.NUMERO = P_NUMERO
                    AND HTASI.IDINSTITUCION = ASI.IDINSTITUCION
                    AND HTASI.IDTIPOASISTENCIACOLEGIO = ASI.IDTIPOASISTENCIACOLEGIO
                    AND HTASI.IDFACTURACION = V_IDFACTURACION;
                    
                    V_TIENEHISTORICO := TRUE;                    
                    
                EXCEPTION
                    WHEN OTHERS THEN
                        V_TIENEHISTORICO := FALSE;
            END;
        END IF;
            
        -- Si no obtenemos un idFacturacion o no tenia historico, hay que obtener el tipo de asistencia actual
        IF (V_IDFACTURACION IS NULL OR V_TIENEHISTORICO = FALSE) THEN
            SELECT TASI.IMPORTE, TASI.IMPORTEMAXIMO
                INTO V_IMPORTE, V_IMPORTEMAXIMO
            FROM SCS_TIPOASISTENCIACOLEGIO TASI, SCS_ASISTENCIA ASI
            WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
                AND ASI.ANIO = P_ANIO
                AND ASI.NUMERO = P_NUMERO
                AND TASI.IDINSTITUCION = ASI.IDINSTITUCION
                AND TASI.IDTIPOASISTENCIACOLEGIO = ASI.IDTIPOASISTENCIACOLEGIO;                    
        END IF;                
            
        IF (P_TIPO = 0) THEN
            RETURN V_IMPORTE;
        ELSE
            RETURN V_IMPORTEMAXIMO;
        END IF;

        EXCEPTION
            WHEN OTHERS THEN
                RETURN NULL;
    END FUNC_CALCULAR_IMPORTEASIST;

  /****************************************************************************************************************
    Nombre: FUNC_CALCULAR_IMPORTEACT
    Descripcion: Funcion que nos devuelve el importe de un tipo de actuacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER       
    - P_ANIO - IN - Ano de la actuacion - NUMBER
    - P_NUMERO - IN - Numero de la actuacion - NUMBER
    - P_IDACTUACION - IN - Identificador del tipo de actuacion - NUMBER
    - P_FECHA - IN - Fecha de la asistencia - DATE
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_TIPO - IN - 0:IMPORTE; 1:IMPORTEMAXIMO; - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 27/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/
    FUNCTION FUNC_CALCULAR_IMPORTEACT(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTURNO IN SCS_ASISTENCIA.IDTURNO%TYPE,
        P_IDGUARDIA IN SCS_ASISTENCIA.IDGUARDIA%TYPE,
        P_ANIO IN SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION IN SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        P_FECHA IN SCS_ASISTENCIA.FECHAHORA%TYPE,
        P_IDFACTURACION IN FCS_HISTORICO_TIPOACTUACION.IDFACTURACION%TYPE,
        P_TIPO IN NUMBER) RETURN NUMBER IS
    
        V_IDFACTURACION FCS_HISTORICO_TIPOACTUACION.IDFACTURACION%TYPE := P_IDFACTURACION;
        V_IMPORTE FCS_HISTORICO_TIPOACTUACION.IMPORTE%TYPE := NULL;
        V_IMPORTEMAXIMO FCS_HISTORICO_TIPOACTUACION.IMPORTEMAXIMO%TYPE := NULL;
        V_TIENEHISTORICO BOOLEAN := FALSE;
  
    BEGIN
        IF (P_IDFACTURACION IS NULL) THEN
            -- Busca si tiene un IDFACTURACION previo (incluye tratamiento de regularizaciones)
            V_IDFACTURACION :=  FUNC_OBTENER_IDFACTURACION(P_IDINSTITUCION, P_IDTURNO, P_IDGUARDIA, P_FECHA);
        END IF;
    
        -- Si obtiene un idFacturacion, hay que obtener el tipo de actuacion del historico
        IF (V_IDFACTURACION IS NOT NULL) THEN
            BEGIN
                SELECT HTACT.IMPORTE,  HTACT.IMPORTEMAXIMO
                    INTO V_IMPORTE, V_IMPORTEMAXIMO
                FROM FCS_HISTORICO_TIPOACTUACION HTACT, SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = P_IDINSTITUCION
                    AND ACT.ANIO = P_ANIO
                    AND ACT.NUMERO = P_NUMERO
                    AND ACT.IDACTUACION = P_IDACTUACION
                    AND HTACT.IDINSTITUCION = ACT.IDINSTITUCION
                    AND HTACT.IDTIPOASISTENCIA = ACT.IDTIPOASISTENCIA
                    AND HTACT.IDTIPOACTUACION = ACT.IDTIPOACTUACION
                    AND HTACT.IDFACTURACION = V_IDFACTURACION;
                    
                    V_TIENEHISTORICO := TRUE;                    
                    
                EXCEPTION
                    WHEN OTHERS THEN
                        V_TIENEHISTORICO := FALSE;
            END;                                
        END IF;
            
        -- Si no obtenemos un idFacturacion o no tenia historico, hay que obtener el tipo de actuacion actual
        IF (V_IDFACTURACION IS NULL OR V_TIENEHISTORICO = FALSE) THEN
            SELECT TACT.IMPORTE,  TACT.IMPORTEMAXIMO
                INTO V_IMPORTE, V_IMPORTEMAXIMO
            FROM SCS_TIPOACTUACION TACT, SCS_ACTUACIONASISTENCIA ACT
            WHERE ACT.IDINSTITUCION = P_IDINSTITUCION
                AND ACT.ANIO = P_ANIO
                AND ACT.NUMERO = P_NUMERO
                AND ACT.IDACTUACION = P_IDACTUACION
                AND TACT.IDINSTITUCION = ACT.IDINSTITUCION
                AND TACT.IDTIPOASISTENCIA = ACT.IDTIPOASISTENCIA
                AND TACT.IDTIPOACTUACION = ACT.IDTIPOACTUACION;
        END IF;                
            
        IF (P_TIPO = 0) THEN
            RETURN V_IMPORTE;
        ELSE
            RETURN V_IMPORTEMAXIMO;
        END IF;            

        EXCEPTION
            WHEN OTHERS THEN
                RETURN NULL;
    END FUNC_CALCULAR_IMPORTEACT;    
                    
  /****************************************************************************************************************
    Nombre: PROC_CARGA_CONFIG_GUARDIA
    Descripcion: Procedimiento para catalanes que carga V_CONFIG_GUARDIA 

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_CONFIGGUARDIAACTUAL - IN - RECORD que contiene la configuracion actual de la guardia - CONFIG_GUARDIA
    - P_IDFACTURACION - IN - Identificador de la facturacion de la que se quiere obtener la configuracion - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/        
    PROCEDURE PROC_CARGA_CONFIG_GUARDIA(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,        
        P_CONFIGGUARDIAACTUAL IN CONFIG_GUARDIA,
        P_IDFACTURACION IN NUMBER, -- DEBE TENER SIEMPRE VALOR
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
    BEGIN             
        IF (FUNC_ES_COLEGIO_CATALAN(P_IDINSTITUCION) = TRUE) THEN   
        
            IF (P_IDFACTURACION IS NULL OR V_CONFIG_GUARDIA.IDFACTURACION IS NULL OR P_CONFIGGUARDIAACTUAL.IDFACTURACION IS NULL) THEN
                P_DATOSERROR := 'PROC_CARGA_CONFIG_GUARDIA: No se encuentra el valor de IDFACTURACION';
                RAISE E_ERROR2;
            END IF;                
                                
            IF (P_IDFACTURACION =  V_CONFIG_GUARDIA.IDFACTURACION) THEN
                P_DATOSERROR := 'PROC_CARGA_CONFIG_GUARDIA: No ha cambiado el IDFACTURACION y por lo tanto, tenemos V_CONFIG_GUARDIA configurado correctamente';
                                
            ELSIF (P_IDFACTURACION = P_CONFIGGUARDIAACTUAL.IDFACTURACION) THEN -- JPT: Es una cabecera de guardia, configurado con la guardia actual
                    V_CONFIG_GUARDIA := P_CONFIGGUARDIAACTUAL; 
                                    
            ELSE -- JPT: Como es una cabecera de guardia ya facturada, hay que obtener la configuracion de la primera facturacion de esa cabecera de guardia
                /* JPT: Cargamos el RECORD de la Configuracion de Guardia FACTURADA (V_CONFIG_GUARDIA)
                    - Obtiene los hitos de la facturacion (tabla FCS_HISTORICO_HITOFACT) y carga los datos en V_CONFIG_GUARDIA
                    - Para los catalanes hay que consultar SCS_GUARDIASTURNO.ESGUARDIAVG para la facturacion por guardias*/
                P_DATOSERROR:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FCS_CARGA_CONFIG_GUARDIA';
                PROC_FCS_CARGA_CONFIG_GUARDIA(
                    P_IDINSTITUCION,
                    P_IDTURNO,
                    P_IDGUARDIA,
                    P_IDFACTURACION,
                    P_CODRETORNO,
                    P_DATOSERROR);            
                IF (P_CODRETORNO <> '0') THEN
                    RAISE E_ERROR2;
                END IF;
            END IF;
        END IF;
        
        P_DATOSERROR := 'PROC_FCS_FACTURAR_GUARDIAS: Ha finalizado correctamente';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;        
    END PROC_CARGA_CONFIG_GUARDIA;        

    /****************************************************************************************************************/
    /* Nombre: PROC_FAC_AS_CONTROLADO */
    /* Descripcion: Funcion que realiza la facturacion SJCS de guardias configuradas con una facturacion con asistencias (sin minimos, sin maximos, sin tipos) */
    /* */
    /* P_DIASGUARDIA - IN - Dias de la semana aplicables a la guardia - VARCHAR(7) */
    /* P_IMPORTEASISTENCIA - IN - Importe de la asistencia - NUMBER */
    /* P_FECHAFACTURACIONDESDE - IN - Fecha inicial de la facturadion - DATE */
    /* P_FECHAFACTURACIONHASTA - IN - Fecha final de la facturadion - DATE */
    /* P_REG_CG_FACTURABLE - IN - Registro de las cabeceras de guardia - MATRICE_CG_FACTURABLE */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/05/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    PROCEDURE PROC_FAC_AS_CONTROLADO (
        P_DIASGUARDIA IN SCS_HITOFACTURABLEGUARDIA.DIASAPLICABLES%TYPE, -- V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA
        P_IMPORTEASISTENCIA IN NUMBER, -- V_CONFIG_GUARDIA.IMPORTEASISTENCIA
        P_FECHAFACTURACIONDESDE IN DATE, -- V_DATOS_FACTURACION.FECHADESDE
        P_FECHAFACTURACIONHASTA IN DATE, -- V_DATOS_FACTURACION.FECHAHASTA
        P_REG_CG_FACTURABLE IN MATRICE_CG_FACTURABLE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2
    ) IS

        importeAsistUGSinFacturar NUMBER;
        costeFijoTotalActuacionesUG NUMBER;
        tieneNuevaFacturacionUG BOOLEAN;
        contadorUG NUMBER := 0;
        costeFijoTotalActuacionesCG NUMBER := 0;
        v_FacturadoCG NUMBER;
        C_TrazaInicial constant VARCHAR2(30) := 'PROC_FAC_AS_CONTROLADO: ';

    BEGIN
        IND_UG := 0;
        IND_AS := 0;
        IND_AC := 0;

        V_DATOSERROR2 := C_TrazaInicial || 'Bucle de unidades de guardia';
        FOR V_UNIDAD_GUARDIA IN UNIDADES_GUARDIA_CONTROLADO(
            P_REG_CG_FACTURABLE.IDINSTITUCION,
            P_REG_CG_FACTURABLE.IDTURNO,
            P_REG_CG_FACTURABLE.IDGUARDIA,
            P_REG_CG_FACTURABLE.IDPERSONA,
            P_REG_CG_FACTURABLE.FECHAINICIO,
            P_DIASGUARDIA
        ) LOOP

            V_DATOSERROR2 := C_TrazaInicial || 'Inicializa variables de unidades de guardia';
            contadorUG := contadorUG + 1;
            importeAsistUGSinFacturar := 0;
            costeFijoTotalActuacionesUG := 0;
            tieneNuevaFacturacionUG := FALSE;

            /* Este cursor devuelve todas las asistencias que cumplen las siguientes condiciones:
                - No esta anulada
                - Se realiza en el dia de la guardia
                - Se realiza en un dia aplicable de los indicados en la configuracion de la guardia (LMXJVSD)
                - Tiene almenos una actuacion, sin facturar, justificada dentro del periodo de facturacion
            */
            V_DATOSERROR2 := C_TrazaInicial || 'Bucle de unidades de asistencias';
            FOR V_ASISTENCIA IN C_ASISTENCIAS_UG_CONTROLADO(
                V_UNIDAD_GUARDIA.IDINSTITUCION,
                V_UNIDAD_GUARDIA.IDTURNO,
                V_UNIDAD_GUARDIA.IDGUARDIA,
                V_UNIDAD_GUARDIA.IDPERSONA,
                V_UNIDAD_GUARDIA.FECHAFIN,
                P_FECHAFACTURACIONDESDE,
                P_FECHAFACTURACIONHASTA,
                P_DIASGUARDIA,
                0 -- No aplica tipos
            ) LOOP

                IF (V_ASISTENCIA.NUMASIFAC = 0) THEN -- Asistencia sin facturar, que tiene almenos una actuacion (sin facturar y justificada dentro del periodo de facturacion)
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la asistencia con importe';
                    tieneNuevaFacturacionUG := TRUE;
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIA.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIA.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIA.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := 0; -- Sin facturar
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUG;
                    M_APUNTE_AS(IND_AS).MOTIVO := 5; -- As
                    M_APUNTE_AS(IND_AS).IMPORTE := P_IMPORTEASISTENCIA;

                    V_DATOSERROR2 := C_TrazaInicial || 'Acumula los importes de las asistencias sin facturar';
                    importeAsistUGSinFacturar := importeAsistUGSinFacturar + P_IMPORTEASISTENCIA;

                ELSE -- Asistencia ya facturada, que tiene almenos una actuacion (sin facturar y justificada dentro del periodo de facturacion)
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la asistencia sin importe';
                    tieneNuevaFacturacionUG := TRUE;
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIA.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIA.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIA.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := 1; -- Facturada
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUG;
                    M_APUNTE_AS(IND_AS).MOTIVO := 27; -- As+
                    M_APUNTE_AS(IND_AS).IMPORTE := 0; -- Hay que poner la asistencia, ya que tiene una actuacion
                END IF;

                /* Este cursor devuelve todas las actuaciones que cumplen las siguientes condiciones:
                    - Sin anular
                    - Justificada dentro del periodo de facturacion
                    - Validada
                    - Sin facturar
                */
                V_DATOSERROR2 := C_TrazaInicial || 'Bucle de actuaciones sin facturar';
                FOR V_ACTUACION IN C_ACTUACIONES_ASIS_CONTROLADO(
                    V_ASISTENCIA.IDINSTITUCION,
                    V_ASISTENCIA.ANIO,
                    V_ASISTENCIA.NUMERO,
                    P_FECHAFACTURACIONDESDE,
                    P_FECHAFACTURACIONHASTA
                ) LOOP
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la actuacion sin importe';
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).IDINSTITUCION := V_ACTUACION.IDINSTITUCION;
                    M_APUNTE_AC(IND_AC).ANIO := V_ACTUACION.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := V_ACTUACION.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := V_ACTUACION.IDACTUACION;
                    M_APUNTE_AC(IND_AC).FACTURADO := 0; -- Sin facturar
                    M_APUNTE_AC(IND_AC).MOTIVO := 5; -- As
                    M_APUNTE_AC(IND_AC).IMPORTE := 0;
                    M_APUNTE_AC(IND_AC).CONTADOR := contadorUG;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := V_ACTUACION.COSTEFIJO;

                    V_DATOSERROR2 := C_TrazaInicial || 'Acumula los costes fijos de las actuaciones sin facturar';
                    costeFijoTotalActuacionesUG := costeFijoTotalActuacionesUG + V_ACTUACION.COSTEFIJO;
                    costeFijoTotalActuacionesCG := costeFijoTotalActuacionesCG + V_ACTUACION.COSTEFIJO;
                END LOOP; -- Fin actuaciones
            END LOOP; -- Fin asistencias

            -- Si no se ha facturado o se ha facturado algo nuevo dentro de la unidad de guardia
            IF (V_UNIDAD_GUARDIA.FACTURADO = 0 OR tieneNuevaFacturacionUG) THEN
                V_DATOSERROR2 := C_TrazaInicial || 'Guarda la unidad de guardia';
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDAD_GUARDIA.IDINSTITUCION;
                M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDAD_GUARDIA.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDAD_GUARDIA.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDAD_GUARDIA.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDAD_GUARDIA.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDAD_GUARDIA.FECHAFIN;
                M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDAD_GUARDIA.FACTURADO;
                M_APUNTE_UG(IND_UG).COSTEFIJO := costeFijoTotalActuacionesUG;
                M_APUNTE_UG(IND_UG).CONTADOR := contadorUG;
                M_APUNTE_UG(IND_UG).IMPORTE := importeAsistUGSinFacturar;
                IF (V_UNIDAD_GUARDIA.FACTURADO>0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 27; -- As+
                ELSE
                    M_APUNTE_UG(IND_UG).MOTIVO := 5; -- As
                END IF;
            END IF;
        END LOOP; -- Fin unidades de guardia

        IF (IND_UG > 0) THEN -- Si ha insertado la unidad de guardia, se inserta la cabecera de guardia
            V_DATOSERROR2 := C_TrazaInicial || 'Guarda la cabecera de guardia';
            IND_CG := 1; -- Por cada recorrido de la matriz de cabecera facturable solo hay un registro en la matriz de apunte de CG por eso el indice siempre sera 1.
            M_APUNTE_CG(IND_CG).IDINSTITUCION := P_REG_CG_FACTURABLE.IDINSTITUCION;
            M_APUNTE_CG(IND_CG).IDTURNO := P_REG_CG_FACTURABLE.IDTURNO;
            M_APUNTE_CG(IND_CG).IDGUARDIA := P_REG_CG_FACTURABLE.IDGUARDIA;
            M_APUNTE_CG(IND_CG).IDPERSONA := P_REG_CG_FACTURABLE.IDPERSONA;
            M_APUNTE_CG(IND_CG).FECHAINICIO := P_REG_CG_FACTURABLE.FECHAINICIO;
            M_APUNTE_CG(IND_CG).FECHAFIN := P_REG_CG_FACTURABLE.FECHAFIN;
            M_APUNTE_CG(IND_CG).COSTEFIJO := costeFijoTotalActuacionesCG;
            M_APUNTE_CG(IND_CG).CONTADOR := 1;
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';

            V_DATOSERROR2 := C_TrazaInicial || 'Calculo la suma de los importes de las unidades de guardia';
            M_APUNTE_CG(IND_CG).IMPORTE := 0;
            FOR indiceUG IN 1..IND_UG LOOP
                M_APUNTE_CG(IND_CG).IMPORTE := M_APUNTE_CG(IND_CG).IMPORTE + M_APUNTE_UG(indiceUG).IMPORTE;
            END LOOP;

            V_DATOSERROR2 := C_TrazaInicial || 'Obtengo si la cabecera de guardia esta facturada';
            SELECT COUNT(*)
                INTO v_FacturadoCG
            FROM FCS_FACT_APUNTE
            WHERE IDINSTITUCION = P_REG_CG_FACTURABLE.IDINSTITUCION
                AND IDTURNO = P_REG_CG_FACTURABLE.IDTURNO
                AND IDGUARDIA = P_REG_CG_FACTURABLE.IDGUARDIA
                AND IDPERSONA = P_REG_CG_FACTURABLE.IDPERSONA
                AND FECHAINICIO = P_REG_CG_FACTURABLE.FECHAINICIO;

            M_APUNTE_CG(IND_CG).FACTURADO := v_FacturadoCG;

            IF (v_FacturadoCG>0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 27; -- As+
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := 5; -- As
            END IF;
        END IF;

        P_DATOSERROR := C_TrazaInicial || 'Ha finalizado correctamente';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || V_DATOSERROR2 || ' ' || SQLERRM;
    END PROC_FAC_AS_CONTROLADO;

    /****************************************************************************************************************/
    /* Nombre: PROC_FAC_ASMIN_CONTROLADO */
    /* Descripcion: Funcion que realiza la facturacion SJCS de guardias configuradas con una facturacion con asistencias configuradas con minimos y no agrupadas (sin maximos) */
    /* */
    /* P_DIASGUARDIA - IN - Dias de la semana aplicables a la guardia - VARCHAR(7) */
    /* P_IMPORTEASISTENCIA - IN - Importe de la asistencia - NUMBER */
    /* P_IMPORTEMINUG - IN - Importe minimo de la unidad de guardia - NUMBER */
    /* P_FECHAFACTURACIONDESDE - IN - Fecha inicial de la facturadion - DATE */
    /* P_FECHAFACTURACIONHASTA - IN - Fecha final de la facturadion - DATE */
    /* P_REG_CG_FACTURABLE - IN - Registro de las cabeceras de guardia - MATRICE_CG_FACTURABLE */
    /* P_MOTIVO_AS - IN - Indica el motivo As o AsTp */
    /* P_MOTIVO_ASMAS - IN - Indica el motivo As+ o AsTp+ */
    /* P_APLICATIPOS - IN - 0:NoAplicaTipos; 1:AplicaTipos */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 03/06/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    PROCEDURE PROC_FAC_ASMIN_CONTROLADO (
        P_DIASGUARDIA IN SCS_HITOFACTURABLEGUARDIA.DIASAPLICABLES%TYPE, -- V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA
        P_IMPORTEASISTENCIA IN NUMBER, -- V_CONFIG_GUARDIA.IMPORTEASISTENCIA
        P_IMPORTEMINUG IN NUMBER, -- V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA
        P_FECHAFACTURACIONDESDE IN DATE, -- V_DATOS_FACTURACION.FECHADESDE
        P_FECHAFACTURACIONHASTA IN DATE, -- V_DATOS_FACTURACION.FECHAHASTA
        P_REG_CG_FACTURABLE IN MATRICE_CG_FACTURABLE,
        P_MOTIVO_AS IN NUMBER,
        P_MOTIVO_ASMAS IN NUMBER,
        P_APLICATIPOS IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2
    ) IS

        importeAsistUGSinFacturar NUMBER;
        costeFijoTotalActuacionesUG NUMBER;
        tieneNuevaFacturacionUG BOOLEAN;
        contadorUG NUMBER := 0;
        v_ImporteAsistUGFacturado NUMBER;
        costeFijoTotalActuacionesCG NUMBER := 0;
        v_FacturadoCG NUMBER;
        v_ImporteAsistencia NUMBER;
        C_TrazaInicial constant VARCHAR2(30) := 'PROC_FAC_ASMIN_CONTROLADO: ';

    BEGIN
        IND_UG := 0;
        IND_AS := 0;
        IND_AC := 0;

        V_DATOSERROR2 := C_TrazaInicial || 'Bucle de unidades de guardia';
        FOR V_UNIDAD_GUARDIA IN UNIDADES_GUARDIA_CONTROLADO(
            P_REG_CG_FACTURABLE.IDINSTITUCION,
            P_REG_CG_FACTURABLE.IDTURNO,
            P_REG_CG_FACTURABLE.IDGUARDIA,
            P_REG_CG_FACTURABLE.IDPERSONA,
            P_REG_CG_FACTURABLE.FECHAINICIO,
            P_DIASGUARDIA
        ) LOOP

            V_DATOSERROR2 := C_TrazaInicial || 'Inicializa variables de unidades de guardia';
            contadorUG := contadorUG + 1;
            importeAsistUGSinFacturar := 0;
            costeFijoTotalActuacionesUG := 0;
            tieneNuevaFacturacionUG := FALSE;

            /* Este cursor devuelve todas las asistencias que cumplen las siguientes condiciones:
                - No esta anulada
                - Se realiza en el dia de la guardia
                - Se realiza en un dia aplicable de los indicados en la configuracion de la guardia (LMXJVSD)
                - Tiene almenos una actuacion, sin facturar, justificada dentro del periodo de facturacion
            */
            V_DATOSERROR2 := C_TrazaInicial || 'Bucle de unidades de asistencias';
            FOR V_ASISTENCIA IN C_ASISTENCIAS_UG_CONTROLADO(
                V_UNIDAD_GUARDIA.IDINSTITUCION,
                V_UNIDAD_GUARDIA.IDTURNO,
                V_UNIDAD_GUARDIA.IDGUARDIA,
                V_UNIDAD_GUARDIA.IDPERSONA,
                V_UNIDAD_GUARDIA.FECHAFIN,
                P_FECHAFACTURACIONDESDE,
                P_FECHAFACTURACIONHASTA,
                P_DIASGUARDIA,
                P_APLICATIPOS
            ) LOOP

                IF (V_ASISTENCIA.NUMASIFAC = 0) THEN -- Asistencia sin facturar, que tiene almenos una actuacion (sin facturar y justificada dentro del periodo de facturacion)
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la asistencia con importe';
                    tieneNuevaFacturacionUG := TRUE;
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIA.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIA.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIA.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := 0; -- Sin facturar
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUG;
                    M_APUNTE_AS(IND_AS).MOTIVO := P_MOTIVO_AS; -- As o AsTp

                    /*
                        Calculo del importe de la asistencia
                        1. Intenta obtener el importe del tipo de asistencia
                        2. En caso de no tener importe el tipo de asistencia, pone el importe de la asistencia
                        3. En caso de no tener ningun importe, caso muy raro, el valor es cero.
                    */
                    v_ImporteAsistencia := V_ASISTENCIA.IMPORTETIPO;
                    v_ImporteAsistencia := NVL(v_ImporteAsistencia, P_IMPORTEASISTENCIA);
                    v_ImporteAsistencia := NVL(v_ImporteAsistencia, 0);
                    M_APUNTE_AS(IND_AS).IMPORTE := v_ImporteAsistencia;

                    V_DATOSERROR2 := C_TrazaInicial || 'Acumula los importes de las asistencias sin facturar';
                    importeAsistUGSinFacturar := importeAsistUGSinFacturar + v_ImporteAsistencia;

                ELSE -- Asistencia ya facturada, que tiene almenos una actuacion (sin facturar y justificada dentro del periodo de facturacion)
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la asistencia sin importe';
                    tieneNuevaFacturacionUG := TRUE;
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIA.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIA.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIA.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := 1; -- Facturada
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUG;
                    M_APUNTE_AS(IND_AS).MOTIVO := P_MOTIVO_ASMAS; -- As+ o AsTp+
                    M_APUNTE_AS(IND_AS).IMPORTE := 0; -- Hay que poner la asistencia, ya que tiene una actuacion
                END IF;

                /* Este cursor devuelve todas las actuaciones que cumplen las siguientes condiciones:
                    - Sin anular
                    - Justificada dentro del periodo de facturacion
                    - Validada
                    - Sin facturar
                */
                V_DATOSERROR2 := C_TrazaInicial || 'Bucle de actuaciones sin facturar';
                FOR V_ACTUACION IN C_ACTUACIONES_ASIS_CONTROLADO(
                    V_ASISTENCIA.IDINSTITUCION,
                    V_ASISTENCIA.ANIO,
                    V_ASISTENCIA.NUMERO,
                    P_FECHAFACTURACIONDESDE,
                    P_FECHAFACTURACIONHASTA
                ) LOOP
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la actuacion sin importe';
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).IDINSTITUCION := V_ACTUACION.IDINSTITUCION;
                    M_APUNTE_AC(IND_AC).ANIO := V_ACTUACION.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := V_ACTUACION.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := V_ACTUACION.IDACTUACION;
                    M_APUNTE_AC(IND_AC).FACTURADO := 0; -- Sin facturar
                    M_APUNTE_AC(IND_AC).MOTIVO := P_MOTIVO_AS; -- As o AsTp
                    M_APUNTE_AC(IND_AC).IMPORTE := 0;
                    M_APUNTE_AC(IND_AC).CONTADOR := contadorUG;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := V_ACTUACION.COSTEFIJO;

                    V_DATOSERROR2 := C_TrazaInicial || 'Acumula los costes fijos de las actuaciones sin facturar';
                    costeFijoTotalActuacionesUG := costeFijoTotalActuacionesUG + V_ACTUACION.COSTEFIJO;
                    costeFijoTotalActuacionesCG := costeFijoTotalActuacionesCG + V_ACTUACION.COSTEFIJO;
                END LOOP; -- Fin actuaciones
            END LOOP; -- Fin asistencias

            -- Si no se ha facturado o se ha facturado algo nuevo dentro de la unidad de guardia
            IF (V_UNIDAD_GUARDIA.FACTURADO = 0 OR tieneNuevaFacturacionUG) THEN

                V_DATOSERROR2 := C_TrazaInicial || 'Guarda la unidad de guardia';
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDAD_GUARDIA.IDINSTITUCION;
                M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDAD_GUARDIA.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDAD_GUARDIA.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDAD_GUARDIA.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDAD_GUARDIA.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDAD_GUARDIA.FECHAFIN;
                M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDAD_GUARDIA.FACTURADO;
                M_APUNTE_UG(IND_UG).COSTEFIJO := costeFijoTotalActuacionesUG;
                M_APUNTE_UG(IND_UG).CONTADOR := contadorUG;

                IF (V_UNIDAD_GUARDIA.FACTURADO = 0) THEN -- Unidad de guardia sin facturar
                    IF (P_IMPORTEMINUG > importeAsistUGSinFacturar) THEN
                        M_APUNTE_UG(IND_UG).MOTIVO := 10; -- AsMin
                        M_APUNTE_UG(IND_UG).IMPORTE := P_IMPORTEMINUG;
                    ELSE
                        M_APUNTE_UG(IND_UG).MOTIVO := P_MOTIVO_AS; -- As o AsTp
                        M_APUNTE_UG(IND_UG).IMPORTE := importeAsistUGSinFacturar;
                    END IF;

                ELSE -- Unidad de guardia ya facturarada previamente
                    V_DATOSERROR2 := C_TrazaInicial || 'Obtengo el importe de las asistencias facturadas de la unidad de guardia';
                    SELECT NVL(SUM(FA.PRECIOAPLICADO), 0)
                        INTO v_ImporteAsistUGFacturado
                    FROM FCS_FACT_ASISTENCIA FA,
                        SCS_ASISTENCIA A
                    WHERE FA.IDINSTITUCION = A.IDINSTITUCION -- Relacion FCS_FACT_ASISTENCIA => SCS_ASISTENCIA
                        AND FA.ANIO = A.ANIO -- Relacion FCS_FACT_ASISTENCIA => SCS_ASISTENCIA
                        AND FA.NUMERO = A.NUMERO -- Relacion FCS_FACT_ASISTENCIA => SCS_ASISTENCIA
                        AND A.IDINSTITUCION = V_UNIDAD_GUARDIA.IDINSTITUCION -- Utiliza indice SI_FK_ASISTENCIA_GUARDIASTURNO
                        AND A.IDTURNO = V_UNIDAD_GUARDIA.IDTURNO -- Utiliza indice SI_FK_ASISTENCIA_GUARDIASTURNO
                        AND A.IDGUARDIA = V_UNIDAD_GUARDIA.IDGUARDIA -- Utiliza indice SI_FK_ASISTENCIA_GUARDIASTURNO
                        AND A.IDPERSONACOLEGIADO = V_UNIDAD_GUARDIA.IDPERSONA
                        AND TRUNC(A.FECHAHORA) = V_UNIDAD_GUARDIA.FECHAFIN -- La asistencia se realiza en el dia de la guardia
                        AND FUN_ESDIAAPLICABLE(A.FECHAHORA, P_DIASGUARDIA) = 1 -- La asistencia se realiza en un dia aplicable en los indicados de la configuracion de la guardia (LMXJVSD)
                        AND A.FECHAANULACION IS NULL; -- Asistencia no anulada

                    IF (P_IMPORTEMINUG > v_ImporteAsistUGFacturado + importeAsistUGSinFacturar) THEN
                        M_APUNTE_UG(IND_UG).MOTIVO := 33; -- AsMin+
                        M_APUNTE_UG(IND_UG).IMPORTE := P_IMPORTEMINUG - V_UNIDAD_GUARDIA.PRECIOAPLICADO; -- Casi siempre deberia ser 0;

                    ELSE
                        M_APUNTE_UG(IND_UG).MOTIVO := P_MOTIVO_ASMAS; -- As+ o AsTp+
                        M_APUNTE_UG(IND_UG).IMPORTE := importeAsistUGSinFacturar + v_ImporteAsistUGFacturado - V_UNIDAD_GUARDIA.PRECIOAPLICADO;
                    END IF;

                    IF (M_APUNTE_UG(IND_UG).IMPORTE < 0) THEN
                        M_APUNTE_UG(IND_UG).IMPORTE := 0;
                    END IF;
                END IF;
            END IF;
        END LOOP; -- Fin unidades de guardia

        IF (IND_UG > 0) THEN -- Si ha insertado la unidad de guardia, se inserta la cabecera de guardia
            V_DATOSERROR2 := C_TrazaInicial || 'Guarda la cabecera de guardia';
            IND_CG := 1; -- Por cada recorrido de la matriz de cabecera facturable solo hay un registro en la matriz de apunte de CG por eso el indice siempre sera 1.
            M_APUNTE_CG(IND_CG).IDINSTITUCION := P_REG_CG_FACTURABLE.IDINSTITUCION;
            M_APUNTE_CG(IND_CG).IDTURNO := P_REG_CG_FACTURABLE.IDTURNO;
            M_APUNTE_CG(IND_CG).IDGUARDIA := P_REG_CG_FACTURABLE.IDGUARDIA;
            M_APUNTE_CG(IND_CG).IDPERSONA := P_REG_CG_FACTURABLE.IDPERSONA;
            M_APUNTE_CG(IND_CG).FECHAINICIO := P_REG_CG_FACTURABLE.FECHAINICIO;
            M_APUNTE_CG(IND_CG).FECHAFIN := P_REG_CG_FACTURABLE.FECHAFIN;
            M_APUNTE_CG(IND_CG).COSTEFIJO := costeFijoTotalActuacionesCG;
            M_APUNTE_CG(IND_CG).CONTADOR := 1;
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';

            V_DATOSERROR2 := C_TrazaInicial || 'Calculo la suma de los importes de las unidades de guardia';
            M_APUNTE_CG(IND_CG).IMPORTE := 0;
            FOR indiceUG IN 1..IND_UG LOOP
                M_APUNTE_CG(IND_CG).IMPORTE := M_APUNTE_CG(IND_CG).IMPORTE + M_APUNTE_UG(indiceUG).IMPORTE;
            END LOOP;

            V_DATOSERROR2 := C_TrazaInicial || 'Obtengo si la cabecera de guardia esta facturada';
            SELECT COUNT(*)
                INTO v_FacturadoCG
            FROM FCS_FACT_APUNTE
            WHERE IDINSTITUCION = P_REG_CG_FACTURABLE.IDINSTITUCION
                AND IDTURNO = P_REG_CG_FACTURABLE.IDTURNO
                AND IDGUARDIA = P_REG_CG_FACTURABLE.IDGUARDIA
                AND IDPERSONA = P_REG_CG_FACTURABLE.IDPERSONA
                AND FECHAINICIO = P_REG_CG_FACTURABLE.FECHAINICIO;

            M_APUNTE_CG(IND_CG).FACTURADO := v_FacturadoCG;

            IF (v_FacturadoCG>0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := P_MOTIVO_ASMAS; -- As+ o AsTp+
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := P_MOTIVO_AS; -- As o AsTp
            END IF;
        END IF;

        P_DATOSERROR := C_TrazaInicial || 'Ha finalizado correctamente';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || V_DATOSERROR2 || ' ' || SQLERRM;
    END PROC_FAC_ASMIN_CONTROLADO;

   /****************************************************************************************************************/
    /* Nombre: PROC_FAC_GAS_CONTROLADO */
    /* Descripcion: Funcion que realiza la facturacion SJCS de guardias configuradas por guardias (sin minimos, sin maximos, sin tipos) */
    /* */
    /* P_DIASGUARDIA - IN - Dias de la semana aplicables a la guardia - VARCHAR(7) */
    /* P_IMPORTEGUARDIA - IN - Importe de la guardia - NUMBER */
    /* P_FECHAFACTURACIONDESDE - IN - Fecha inicial de la facturadion - DATE */
    /* P_FECHAFACTURACIONHASTA - IN - Fecha final de la facturadion - DATE */
    /* P_REG_CG_FACTURABLE - IN - Registro de las cabeceras de guardia - MATRICE_CG_FACTURABLE */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/05/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    PROCEDURE PROC_FAC_GAS_CONTROLADO (
        P_DIASGUARDIA IN SCS_HITOFACTURABLEGUARDIA.DIASAPLICABLES%TYPE, -- V_CONFIG_GUARDIA.DIASPAGAGUARDIA
        P_IMPORTEGUARDIA IN NUMBER, -- V_CONFIG_GUARDIA.IMPORTEGUARDIA
        P_FECHAFACTURACIONDESDE IN DATE, -- V_DATOS_FACTURACION.FECHADESDE
        P_FECHAFACTURACIONHASTA IN DATE, -- V_DATOS_FACTURACION.FECHAHASTA
        P_REG_CG_FACTURABLE IN MATRICE_CG_FACTURABLE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2
    ) IS

        costeFijoTotalActuacionesUG NUMBER;
        tieneNuevaFacturacionUG BOOLEAN;
        contadorUG NUMBER := 0;
        costeFijoTotalActuacionesCG NUMBER := 0;
        v_FacturadoCG NUMBER;
        C_TrazaInicial constant VARCHAR2(30) := 'PROC_FAC_GAS_CONTROLADO: ';

    BEGIN
        IND_UG := 0;
        IND_AS := 0;
        IND_AC := 0;

        V_DATOSERROR2 := C_TrazaInicial || 'Bucle de unidades de guardia';
        FOR V_UNIDAD_GUARDIA IN UNIDADES_GUARDIA_CONTROLADO(
            P_REG_CG_FACTURABLE.IDINSTITUCION,
            P_REG_CG_FACTURABLE.IDTURNO,
            P_REG_CG_FACTURABLE.IDGUARDIA,
            P_REG_CG_FACTURABLE.IDPERSONA,
            P_REG_CG_FACTURABLE.FECHAINICIO,
            P_DIASGUARDIA
        ) LOOP

            V_DATOSERROR2 := C_TrazaInicial || 'Inicializa variables de unidades de guardia';
            contadorUG := contadorUG + 1;
            costeFijoTotalActuacionesUG := 0;
            tieneNuevaFacturacionUG := FALSE;

            /* Este cursor devuelve todas las asistencias que cumplen las siguientes condiciones:
                - No esta anulada
                - Se realiza en el dia de la guardia
                - Se realiza en un dia aplicable de los indicados en la configuracion de la guardia (LMXJVSD)
                - Tiene almenos una actuacion, sin facturar, justificada dentro del periodo de facturacion
            */
            V_DATOSERROR2 := C_TrazaInicial || 'Bucle de unidades de asistencias';
            FOR V_ASISTENCIA IN C_ASISTENCIAS_UG_CONTROLADO(
                V_UNIDAD_GUARDIA.IDINSTITUCION,
                V_UNIDAD_GUARDIA.IDTURNO,
                V_UNIDAD_GUARDIA.IDGUARDIA,
                V_UNIDAD_GUARDIA.IDPERSONA,
                V_UNIDAD_GUARDIA.FECHAFIN,
                P_FECHAFACTURACIONDESDE,
                P_FECHAFACTURACIONHASTA,
                P_DIASGUARDIA,
                0 -- No aplica tipos
            ) LOOP

                IF (V_ASISTENCIA.NUMASIFAC = 0) THEN -- Asistencia sin facturar, que tiene almenos una actuacion (sin facturar y justificada dentro del periodo de facturacion)
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la asistencia con importe';
                    tieneNuevaFacturacionUG := TRUE;
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIA.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIA.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIA.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := 0; -- Sin facturar
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUG;
                    M_APUNTE_AS(IND_AS).MOTIVO := 1; -- GAs
                    M_APUNTE_AS(IND_AS).IMPORTE := 0;

                ELSE -- Asistencia ya facturada, que tiene almenos una actuacion (sin facturar y justificada dentro del periodo de facturacion)
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la asistencia sin importe';
                    tieneNuevaFacturacionUG := TRUE;
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIA.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIA.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIA.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := 1; -- Facturada
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUG;
                    M_APUNTE_AS(IND_AS).MOTIVO := 50; -- GAs+
                    M_APUNTE_AS(IND_AS).IMPORTE := 0; -- Hay que poner la asistencia, ya que tiene una actuacion
                END IF;

                /* Este cursor devuelve todas las actuaciones que cumplen las siguientes condiciones:
                    - Sin anular
                    - Justificada dentro del periodo de facturacion
                    - Validada
                    - Sin facturar
                */
                V_DATOSERROR2 := C_TrazaInicial || 'Bucle de actuaciones sin facturar';
                FOR V_ACTUACION IN C_ACTUACIONES_ASIS_CONTROLADO(
                    V_ASISTENCIA.IDINSTITUCION,
                    V_ASISTENCIA.ANIO,
                    V_ASISTENCIA.NUMERO,
                    P_FECHAFACTURACIONDESDE,
                    P_FECHAFACTURACIONHASTA
                ) LOOP
                    V_DATOSERROR2 := C_TrazaInicial || 'Guarda la actuacion sin importe';
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).IDINSTITUCION := V_ACTUACION.IDINSTITUCION;
                    M_APUNTE_AC(IND_AC).ANIO := V_ACTUACION.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := V_ACTUACION.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := V_ACTUACION.IDACTUACION;
                    M_APUNTE_AC(IND_AC).FACTURADO := 0; -- Sin facturar
                    M_APUNTE_AC(IND_AC).MOTIVO := 1; -- GAs
                    M_APUNTE_AC(IND_AC).IMPORTE := 0;
                    M_APUNTE_AC(IND_AC).CONTADOR := contadorUG;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := V_ACTUACION.COSTEFIJO;

                    V_DATOSERROR2 := C_TrazaInicial || 'Acumula los costes fijos de las actuaciones sin facturar';
                    costeFijoTotalActuacionesUG := costeFijoTotalActuacionesUG + V_ACTUACION.COSTEFIJO;
                    costeFijoTotalActuacionesCG := costeFijoTotalActuacionesCG + V_ACTUACION.COSTEFIJO;
                END LOOP; -- Fin actuaciones
            END LOOP; -- Fin asistencias

            -- Si no se ha facturado o se ha facturado algo nuevo dentro de la unidad de guardia
            IF (V_UNIDAD_GUARDIA.FACTURADO = 0 OR tieneNuevaFacturacionUG) THEN
                V_DATOSERROR2 := C_TrazaInicial || 'Guarda la unidad de guardia';
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDAD_GUARDIA.IDINSTITUCION;
                M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDAD_GUARDIA.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDAD_GUARDIA.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDAD_GUARDIA.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDAD_GUARDIA.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDAD_GUARDIA.FECHAFIN;
                M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDAD_GUARDIA.FACTURADO;
                M_APUNTE_UG(IND_UG).COSTEFIJO := costeFijoTotalActuacionesUG;
                M_APUNTE_UG(IND_UG).CONTADOR := contadorUG;
                IF (V_UNIDAD_GUARDIA.FACTURADO>0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 50; -- GAs+
                    M_APUNTE_UG(IND_UG).IMPORTE := 0;
                ELSE
                    M_APUNTE_UG(IND_UG).MOTIVO := 1; -- GAs
                    M_APUNTE_UG(IND_UG).IMPORTE := P_IMPORTEGUARDIA;
                END IF;
            END IF;
        END LOOP; -- Fin unidades de guardia

        IF (IND_UG > 0) THEN -- Si ha insertado la unidad de guardia, se inserta la cabecera de guardia
            V_DATOSERROR2 := C_TrazaInicial || 'Guarda la cabecera de guardia';
            IND_CG := 1; -- Por cada recorrido de la matriz de cabecera facturable solo hay un registro en la matriz de apunte de CG por eso el indice siempre sera 1.
            M_APUNTE_CG(IND_CG).IDINSTITUCION := P_REG_CG_FACTURABLE.IDINSTITUCION;
            M_APUNTE_CG(IND_CG).IDTURNO := P_REG_CG_FACTURABLE.IDTURNO;
            M_APUNTE_CG(IND_CG).IDGUARDIA := P_REG_CG_FACTURABLE.IDGUARDIA;
            M_APUNTE_CG(IND_CG).IDPERSONA := P_REG_CG_FACTURABLE.IDPERSONA;
            M_APUNTE_CG(IND_CG).FECHAINICIO := P_REG_CG_FACTURABLE.FECHAINICIO;
            M_APUNTE_CG(IND_CG).FECHAFIN := P_REG_CG_FACTURABLE.FECHAFIN;
            M_APUNTE_CG(IND_CG).COSTEFIJO := costeFijoTotalActuacionesCG;
            M_APUNTE_CG(IND_CG).CONTADOR := 1;
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';

            V_DATOSERROR2 := C_TrazaInicial || 'Calculo la suma de los importes de las unidades de guardia';
            M_APUNTE_CG(IND_CG).IMPORTE := 0;
            FOR indiceUG IN 1..IND_UG LOOP
                M_APUNTE_CG(IND_CG).IMPORTE := M_APUNTE_CG(IND_CG).IMPORTE + M_APUNTE_UG(indiceUG).IMPORTE;
            END LOOP;

            V_DATOSERROR2 := C_TrazaInicial || 'Obtengo si la cabecera de guardia esta facturada';
            SELECT COUNT(*)
                INTO v_FacturadoCG
            FROM FCS_FACT_APUNTE
            WHERE IDINSTITUCION = P_REG_CG_FACTURABLE.IDINSTITUCION
                AND IDTURNO = P_REG_CG_FACTURABLE.IDTURNO
                AND IDGUARDIA = P_REG_CG_FACTURABLE.IDGUARDIA
                AND IDPERSONA = P_REG_CG_FACTURABLE.IDPERSONA
                AND FECHAINICIO = P_REG_CG_FACTURABLE.FECHAINICIO;

            M_APUNTE_CG(IND_CG).FACTURADO := v_FacturadoCG;

            IF (v_FacturadoCG>0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 50; -- GAs+
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := 1; -- GAs
            END IF;
        END IF;

        P_DATOSERROR := C_TrazaInicial || 'Ha finalizado correctamente';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || V_DATOSERROR2 || ' ' || SQLERRM;
    END PROC_FAC_GAS_CONTROLADO;

  /********************************************************
  * JPT - FIN - CODIGO PARA LA FACTURACION CONTROLADA EN CANTABRIA *
  ********************************************************/

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_FACTURAR_TURNOS_OFI                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
  /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*    07/03/2006           Pilar Duran                      Se obtiene ademas del precio total                  */
  /*                                                          el valor del Punto.                                 */
  /*    01/10/2009           Juan antonio Saiz                Se elimina la facturacion por puntos                */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_TURNOS_OFI(P_IDINSTITUCION   IN NUMBER,
                                         P_IDFACTURACION   IN NUMBER,
                                         P_USUMODIFICACION IN NUMBER,
                                         P_TOTAL           OUT VARCHAR2,
                                         P_CODRETORNO      OUT VARCHAR2,
                                         P_DATOSERROR      OUT VARCHAR2) IS

    V_TOTAL      NUMBER := 0; /* variable para sumar */
    V_FECHADESDE DATE;
    V_FECHAHASTA DATE;
    V_PRECIO     NUMBER := 0;

  BEGIN

    DECLARE
      /* Declaracon de cursores */
      CURSOR C_GRUPOSFACTURACION IS
        SELECT FCS_FACT_GRUPOFACT_HITO.IDGRUPOFACTURACION,
               FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION
          FROM FCS_FACT_GRUPOFACT_HITO
         WHERE FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION = P_IDINSTITUCION
           AND FCS_FACT_GRUPOFACT_HITO.IDFACTURACION = P_IDFACTURACION
           AND FCS_FACT_GRUPOFACT_HITO.IDHITOGENERAL =
               PKG_SIGA_CONSTANTES.HITO_GENERAL_TURNO;

      CURSOR C_TURNO(P_IDGRUPOFACT number) IS
        SELECT SCS_TURNO.IDTURNO, SCS_TURNO.IDINSTITUCION
          FROM SCS_TURNO
         WHERE IDINSTITUCION = P_IDINSTITUCION
           AND IDGRUPOFACTURACION = P_IDGRUPOFACT;

      CURSOR C_ACTDESIGNA(P_IDTURNO number, P_FECHADESDE DATE, P_FECHAHASTA DATE) IS
        SELECT SCS_ACTUACIONDESIGNA.IDINSTITUCION,
               SCS_ACTUACIONDESIGNA.IDTURNO,
               SCS_ACTUACIONDESIGNA.ANIO,
               SCS_ACTUACIONDESIGNA.NUMERO,
               SCS_ACTUACIONDESIGNA.NUMEROASUNTO,
               SCS_ACTUACIONDESIGNA.IDPERSONACOLEGIADO,
               SCS_ACTUACIONDESIGNA.FECHA,
               SCS_ACTUACIONDESIGNA.IDACREDITACION,
               SCS_ACTUACIONDESIGNA.FECHAJUSTIFICACION,
               SCS_PROCEDIMIENTOS.PRECIO,
               SCS_PROCEDIMIENTOS.NOMBRE,
               SCS_PROCEDIMIENTOS.IDPROCEDIMIENTO,
               SCS_ACREDITACION.DESCRIPCION,
               SCS_ACREDITACIONPROCEDIMIENTO.PORCENTAJE
          FROM SCS_ACTUACIONDESIGNA,
               SCS_ACREDITACIONPROCEDIMIENTO,
               SCS_PROCEDIMIENTOS,
               SCS_ACREDITACION
         WHERE SCS_ACTUACIONDESIGNA.IDINSTITUCION_PROC =
               SCS_PROCEDIMIENTOS.IDINSTITUCION
           AND SCS_ACTUACIONDESIGNA.IDPROCEDIMIENTO =
               SCS_PROCEDIMIENTOS.IDPROCEDIMIENTO
           AND SCS_ACTUACIONDESIGNA.IDACREDITACION =
               SCS_ACREDITACIONPROCEDIMIENTO.IDACREDITACION
           AND SCS_ACTUACIONDESIGNA.IDINSTITUCION_PROC =
               SCS_ACREDITACIONPROCEDIMIENTO.IDINSTITUCION
           AND SCS_ACTUACIONDESIGNA.IDPROCEDIMIENTO =
               SCS_ACREDITACIONPROCEDIMIENTO.IDPROCEDIMIENTO
           AND SCS_ACREDITACIONPROCEDIMIENTO.IDACREDITACION =
               SCS_ACREDITACION.IDACREDITACION
           AND SCS_ACTUACIONDESIGNA.IDINSTITUCION = P_IDINSTITUCION
           AND SCS_ACTUACIONDESIGNA.IDTURNO = P_IDTURNO
           AND SCS_ACTUACIONDESIGNA.FECHAJUSTIFICACION IS NOT NULL
           AND (trunc(SCS_ACTUACIONDESIGNA.FECHAJUSTIFICACION) BETWEEN
               trunc(P_FECHADESDE) AND trunc(P_FECHAHASTA))
           AND NVL(SCS_ACTUACIONDESIGNA.FACTURADO, 0) <> 1
           AND NVL(SCS_ACTUACIONDESIGNA.VALIDADA, 0) = 1
           AND (SCS_ACTUACIONDESIGNA.ANULACION is null or
               SCS_ACTUACIONDESIGNA.ANULACION = '0')
           AND SCS_ACTUACIONDESIGNA.IDPERSONACOLEGIADO IS NOT NULL;

    BEGIN

      -- trazas
      V_CODRETORNO2 := 0;
      V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';
      V_TOTAL       := 0;
      V_PRECIO      := 0;

      --Almacenamos en los historicos
      PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_TURNOS(P_IDINSTITUCION,
                                          P_IDFACTURACION,
                                          V_CODRETORNO2,
                                          V_DATOSERROR2);
      -- 1? Hacemos una consulta sobre la tabla FCS_FACTURACIONJG para obtener
      -- la FECHADESDE y la FECHAHASTA del idinstitucion y del idfacturacion
      -- introducidos como parametros
      V_DATOSERROR2 := 'Antes de obtener las fechas desde y hasta de facturacion';
      SELECT fac.fechadesde, fac.fechahasta
        INTO V_FECHADESDE, V_FECHAHASTA
        FROM Fcs_Facturacionjg fac
       WHERE fac.idfacturacion = P_IDFACTURACION
         AND fac.idinstitucion = P_IDINSTITUCION;
      -- trazas

      V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';
      /* Grupos de facturacion */
      FOR V_GRUPOSFACTURACION IN C_GRUPOSFACTURACION LOOP

        -- trazas
        V_DATOSERROR2 := 'Antes de ejecutar turnos';

        /* Turnos */
        FOR V_TURNO IN C_TURNO(V_GRUPOSFACTURACION.IDGRUPOFACTURACION) LOOP
          /* Actuacion Designa*/
          V_DATOSERROR2 := 'Antes de ejecutar Actuacion Designa';

          FOR V_ACTDESIGNA IN C_ACTDESIGNA(V_TURNO.IDTURNO,
                                           V_FECHADESDE,
                                           V_FECHAHASTA) LOOP

            V_TOTAL  := V_TOTAL + round (V_ACTDESIGNA.PRECIO *
                        (V_ACTDESIGNA.PORCENTAJE / 100), 2);
            V_PRECIO := V_PRECIO + V_ACTDESIGNA.PRECIO;

            -- INSERTAMOS EL APUNTE: El IDPERSONA lo hemos sacado del SCS_ACTUACIONDESIGNA

            V_DATOSERROR2 := 'Antes de insertar en FCS_FACT_ACTUACIONDESIGNA';
            INSERT INTO FCS_FACT_ACTUACIONDESIGNA
              (IDINSTITUCION,
               IDFACTURACION,
               NUMEROASUNTO,
               NUMERO,
               ANIO,
               IDTURNO,
               IDPERSONA,
               PROCEDIMIENTO,
               ACREDITACION,
               FECHAACTUACION,
               FECHAJUSTIFICACION,
               PRECIOAPLICADO,
               PORCENTAJEFACTURADO,
               FECHAMODIFICACION,
               USUMODIFICACION,
               CODIGOPROCEDIMIENTO)
            VALUES
              (P_IDINSTITUCION,
               P_IDFACTURACION,
               V_ACTDESIGNA.NUMEROASUNTO,
               V_ACTDESIGNA.NUMERO,
               V_ACTDESIGNA.ANIO,
               V_ACTDESIGNA.IDTURNO,
               V_ACTDESIGNA.IDPERSONACOLEGIADO,
               V_ACTDESIGNA.NOMBRE,
               V_ACTDESIGNA.DESCRIPCION,
               V_ACTDESIGNA.FECHA,
               V_ACTDESIGNA.FECHAJUSTIFICACION,
               V_ACTDESIGNA.PRECIO,
               V_ACTDESIGNA.PORCENTAJE,
               SYSDATE,
               P_USUMODIFICACION,
               V_ACTDESIGNA.IDPROCEDIMIENTO);
            -- trazas
            V_DATOSERROR2 := 'Antes de actualizar en scs_actuaciondesigna';

            -- INDICAMOS QUE LO HEMOS CALCULADO YA
            UPDATE SCS_ACTUACIONDESIGNA
               SET FACTURADO     = PKG_SIGA_CONSTANTES.DB_TRUE_N,
                   IDFACTURACION = P_IDFACTURACION

             WHERE IDINSTITUCION = V_ACTDESIGNA.IDINSTITUCION
               AND IDTURNO = V_ACTDESIGNA.IDTURNO
               AND ANIO = V_ACTDESIGNA.ANIO
               AND NUMERO = V_ACTDESIGNA.NUMERO
               AND NUMEROASUNTO = V_ACTDESIGNA.NUMEROASUNTO;

          END LOOP; --/* Fin Actuacion Designa*/
        END LOOP; -- Fin Turnos
      END LOOP; -- Fin Grupos Facturacion

      -- trazas
      V_DATOSERROR2 := 'Fin correcto ';

      P_CODRETORNO := V_CODRETORNO2;
      P_DATOSERROR := V_DATOSERROR2;
      P_TOTAL      := TO_CHAR(V_TOTAL);

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';
      WHEN OTHERS THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';

    END;

  END; -- Procedure PROC_FCS_FACTURAR_TURNOS_OFI

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_FACTURAR_EJG                                                                      */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
  /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_EJG(P_IDINSTITUCION   IN NUMBER,
                                  P_IDFACTURACION   IN NUMBER,
                                  P_USUMODIFICACION IN NUMBER,
                                  P_TOTAL           OUT VARCHAR2,
                                  P_CODRETORNO      OUT VARCHAR2,
                                  P_DATOSERROR      OUT VARCHAR2) IS
    V_TOTAL      NUMBER := 0; /* variable para sumar */
    V_FECHADESDE DATE;
    V_FECHAHASTA DATE;
    V_PRECIO     NUMBER := 0;

  BEGIN

    -- trazas
    V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';

    DECLARE
      --Declaracon de cursores
      CURSOR C_GRUPOSFACTURACION IS
        SELECT FCS_FACT_GRUPOFACT_HITO.IDGRUPOFACTURACION,
               FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION
          FROM FCS_FACT_GRUPOFACT_HITO
         WHERE FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION = P_IDINSTITUCION
           AND FCS_FACT_GRUPOFACT_HITO.IDFACTURACION = P_IDFACTURACION
           AND FCS_FACT_GRUPOFACT_HITO.IDHITOGENERAL =
               PKG_SIGA_CONSTANTES.HITO_GENERAL_EJG;

      CURSOR C_TURNO(P_IDGRUPOFACT number) IS
        SELECT SCS_TURNO.IDTURNO, SCS_TURNO.IDINSTITUCION
          FROM SCS_TURNO
         WHERE IDINSTITUCION = P_IDINSTITUCION
           AND IDGRUPOFACTURACION = P_IDGRUPOFACT;

      CURSOR C_GUARDIASTURNO(P_IDTURNO number) IS
        SELECT scs_guardiasturno.idguardia
          FROM scs_guardiasturno
         WHERE scs_guardiasturno.idinstitucion = P_IDINSTITUCION
           AND scs_guardiasturno.idturno = P_IDTURNO;
      --AND scs_guardiasturno.idguardiasustitucion IS NULL  -- DCG para no facturar las guardias de sustitucion
      --AND scs_guardiasturno.idturnosustitucion IS NULL;   -- DCG

      CURSOR C_EJG(P_IDTURNO number, P_IDGUARDIA number, V_FECHADESDE DATE, V_FECHAHASTA DATE) IS
        SELECT scs_ejg.idtipoejg,
               scs_ejg.anio,
               scs_ejg.numero,
               scs_ejg.fechaapertura,
               scs_ejg.idpersona
          FROM scs_ejg
         WHERE scs_ejg.idinstitucion = P_IDINSTITUCION
           AND scs_ejg.guardiaturno_idturno = P_IDTURNO
           and scs_ejg.guardiaturno_idguardia = P_IDGUARDIA
              -- Se pueden dar de alta EJG sin letrado asociado
           and scs_ejg.idpersona is not null
              --
           and (trunc(scs_ejg.fechaapertura) between trunc(V_FECHADESDE) AND trunc(V_FECHAHASTA))
           and nvl(scs_ejg.facturado, 0) <> 1;

    BEGIN

      -- 1? Hacemos una consulta sobre la tabla FCS_FACTURACIONJG para obtener
      -- la FECHADESDE y la FECHAHASTA del idinstitucion y del idfacturacion
      -- introducidos como parametros

      V_DATOSERROR2 := 'Antes de obtener las fechas desde y hasta de facturacion';

      SELECT fac.fechadesde, fac.fechahasta
        INTO V_FECHADESDE, V_FECHAHASTA
        FROM Fcs_Facturacionjg fac
       WHERE fac.idfacturacion = P_IDFACTURACION
         AND fac.idinstitucion = P_IDINSTITUCION;
      -- trazas

      V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';
      -- Grupos de facturacion
      FOR V_GRUPOSFACTURACION IN C_GRUPOSFACTURACION LOOP

        -- trazas
        V_DATOSERROR2 := 'Antes de ejecutar turnos';

        -- Turnos
        FOR V_TURNO IN C_TURNO(V_GRUPOSFACTURACION.IDGRUPOFACTURACION) LOOP
          -- Actuacion Designa
          V_DATOSERROR2 := 'Antes de ejecutar Actuacion Designa';

          FOR V_GUARDIASTURNO IN C_GUARDIASTURNO(V_TURNO.IDTURNO) LOOP

            --Guardamos el historico de los hitos de la facturacion
            V_DATOSERROR2 := 'Antes de ejecutar el historico de los hitos';
            PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
                P_IDINSTITUCION,
                P_IDFACTURACION,
                V_TURNO.IDTURNO,
                V_GUARDIASTURNO.IDGUARDIA,
                HITO_EJG,
                NULL,
                NULL,
                V_CODRETORNO2,
                V_DATOSERROR2);

            -- Obtenemos el precio del EJG
            begin
              SELECT nvl(scs_hitofacturableguardia.preciohito, 0)
                into V_PRECIO
                FROM scs_hitofacturableguardia
               WHERE scs_hitofacturableguardia.idinstitucion =
                     P_IDINSTITUCION
                 AND scs_hitofacturableguardia.idturno = V_TURNO.IDTURNO
                 AND scs_hitofacturableguardia.idguardia =
                     V_GUARDIASTURNO.IDGUARDIA
                 AND scs_hitofacturableguardia.idhito = HITO_EJG;
            exception
              when no_data_found then
                V_PRECIO := 0;
            end;

            FOR V_EJG IN C_EJG(V_TURNO.IDTURNO,
                               V_GUARDIASTURNO.IDGUARDIA,
                               V_FECHADESDE,
                               V_FECHAHASTA) LOOP
              -- Actualizamos la variable V_TOTAL antes de hacer el apunte en la tabla de
              -- facturacion de EJG
              V_TOTAL := V_TOTAL + V_PRECIO;
              -- INSERTAMOS EL APUNTE EN FCS_FACT_EJG

              V_DATOSERROR2 := 'Antes de insertar en FCS_FACT_ACTUACIONDESIGNA';
              INSERT INTO FCS_FACT_EJG
                (IDINSTITUCION,
                 IDFACTURACION,
                 IDTIPOEJG,
                 ANIO,
                 NUMERO,
                 IDPERSONA,
                 IDTURNO,
                 IDGUARDIA,
                 FECHAAPERTURA,
                 PRECIOAPLICADO,
                 FECHAMODIFICACION,
                 USUMODIFICACION)
              VALUES
                (P_IDINSTITUCION,
                 P_IDFACTURACION,
                 V_EJG.IDTIPOEJG,
                 V_EJG.ANIO,
                 V_EJG.NUMERO,
                 V_EJG.IDPERSONA,
                 V_TURNO.IDTURNO,
                 V_GUARDIASTURNO.IDGUARDIA,
                 V_EJG.FECHAAPERTURA,
                 V_PRECIO,
                 SYSDATE,
                 P_USUMODIFICACION);

              -- trazas
              V_DATOSERROR2 := 'Antes de actualizar en SCS_EJG';

              -- INDICAMOS QUE LO HEMOS CALCULADO YA
              UPDATE SCS_EJG
                 SET FACTURADO     = PKG_SIGA_CONSTANTES.DB_TRUE_N,
                     IDFACTURACION = P_IDFACTURACION

               WHERE IDINSTITUCION = P_IDINSTITUCION
                 AND ANIO = V_EJG.ANIO
                 AND NUMERO = V_EJG.NUMERO
                 AND IDTIPOEJG = V_EJG.IDTIPOEJG;

            END LOOP; -- Fin Actuacion Designa*/
          END LOOP; -- Fin Turnos
        END LOOP; -- Fin Grupos Facturacion
      END LOOP;

      -- trazas
      V_DATOSERROR2 := 'Fin correcto';

      P_CODRETORNO := V_CODRETORNO2;
      P_DATOSERROR := V_DATOSERROR2;
      P_TOTAL      := TO_CHAR(V_TOTAL);

    EXCEPTION
      WHEN E_ERROR2 THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
      WHEN NO_DATA_FOUND THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';
      WHEN OTHERS THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';

    END;

  END; -- Procedure PROC_FCS_FACTURAR_EJG

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_FACTURAR_GUARDIAS                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
  /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

    PROCEDURE PROC_FCS_FACTURAR_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_TOTAL OUT VARCHAR2,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        V_ESFACTURACIONCONTROLADA BOOLEAN;
        V_CONFIGGUARDIAACTUAL CONFIG_GUARDIA;
        IND_CG_FACTURABLEFG NUMBER;

    BEGIN
        TOTAL_FACTURACION := 0;
        -- JPT: Obtiene los datos de la facturacion (tabla FCS_FACTURACIONJG) y carga los datos en V_DATOS_FACTURACION
        V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FCS_CARGA_FACTURACION';
        PROC_FCS_CARGA_FACTURACION(
            P_IDINSTITUCION, 
            P_IDFACTURACION, 
            V_CODRETORNO2, 
            V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR2;
        END IF;            

        /* JPT: Se almacenan los historicos a nivel de institucion   
            - FCS_HISTORICO_TIPOASISTCOLEGIO de SCS_TIPOASISTENCIACOLEGIO
            - FCS_HISTORICO_TIPOACTUACION de SCS_TIPOACTUACION
            - FCS_HISTO_TIPOACTCOSTEFIJO de SCS_TIPOACTUACIONCOSTEFIJO */
        V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_GUARDIAS';
        PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_GUARDIAS(
            P_IDINSTITUCION, 
            P_IDFACTURACION, 
            V_CODRETORNO2, 
            V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR2;
        END IF;

        -- JPT: Obtiene los grupos de la facturacion (tabla FCS_FACT_GRUPOFACT_HITO)
        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Cursor C_GRUPOSFACTURACION';
        FOR V_GRUPOSFACTURACION IN C_GRUPOSFACTURACION(P_IDINSTITUCION, P_IDFACTURACION) LOOP
        
            -- JPT: Guarda el historico de la configuracion de todas las guardias del grupo de facturacion para esa facturacion (FCS_HISTORICO_HITOFACT de SCS_HITOFACTURABLEGUARDIA)      
            V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT';
            PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
                P_IDINSTITUCION,
                P_IDFACTURACION,
                NULL,
                NULL,                
                0,
                V_GRUPOSFACTURACION.IDGRUPOFACTURACION,
                C_IMPORTE_GUARDIA_INACTIVA,
                V_CODRETORNO2,
                V_DATOSERROR2);       
            IF (V_CODRETORNO2 <> '0') THEN
                RAISE E_ERROR2;
            END IF;                  

            -- JPT: Obtiene los turnos asociados al grupo de facturacion (tabla SCS_TURNO)
            V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Cursor C_TURNO';
            FOR V_TURNO IN C_TURNO(P_IDINSTITUCION, V_GRUPOSFACTURACION.IDGRUPOFACTURACION) LOOP

                -- JPT: Obtiene las guardias asociadas al turno (tabla SCS_GUARDIASTURNO)
                V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Cursor C_GUARDIASTURNO';
                FOR V_GUARDIASTURNO IN C_GUARDIASTURNO(P_IDINSTITUCION, V_TURNO.IDTURNO) LOOP

                    /* JPT: Cargamos el RECORD de la Configuracion de Guardia ACTUAL (V_CONFIG_GUARDIA)
                        - Obtiene los hitos de la facturacion (tabla FCS_HISTORICO_HITOFACT) y carga los datos en V_CONFIG_GUARDIA
                        - Para los catalanes hay que consultar SCS_GUARDIASTURNO.ESGUARDIAVG para la facturacion por guardias*/
                    V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FCS_CARGA_CONFIG_GUARDIA';
                    PROC_FCS_CARGA_CONFIG_GUARDIA(
                        P_IDINSTITUCION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        P_IDFACTURACION, -- Obtenemos la configuracion de la guardia actual y la asociadamos a la facturacion actual
                        V_CODRETORNO2,
                        V_DATOSERROR2);
                    IF (V_CODRETORNO2 <> '0') THEN
                        RAISE E_ERROR2;
                    END IF;
                                              
                    -- JPT: Guardo una copia del RECORD de la Configuracion de Guardia ACTUAL (cambio facturacion guardias facturadas a precio facturado) 
                    IF (FUNC_ES_COLEGIO_CATALAN(P_IDINSTITUCION) = TRUE) THEN   
                        V_CONFIGGUARDIAACTUAL := V_CONFIG_GUARDIA;
                    END IF;                        

                    /* JPT: Cargamos la MATRIZ de la Cabecera de Guardias a facturar [M_CG_FACTURABLE(++IND_CG_FACTURABLE)]
                        - Obtiene las cabeceras de guardia validadas dentro del periodo de la facturacion y sin facturar 
                            (tabla SCS_CABECERAGUARDIAS + FCS_FACT_APUNTE ), 
                            y carga los datos en M_CG_FACTURABLE(++IND_CG_FACTURABLE)
                        - Obtiene las cabeceras de guardia validadas, que tengan por lo menos una actuacion que no sea fuera de guardia sin anular, justificada dentro del periodo de la facturacion y sin facturar 
                            (tabla SCS_CABECERAGUARDIAS + SCS_GUARDIASCOLEGIADO + SCS_ASISTENCIA +SCS_ACTUACIONASISTENCIA +FCS_FACT_ACTUACIONASISTENCIA), 
                            y carga los datos en M_CG_FACTURABLE(++IND_CG_FACTURABLE)*/
                    V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_CARGAR_CABECERA_GUARDIAS';
                    PROC_CARGAR_CABECERA_GUARDIAS(
                        P_IDINSTITUCION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        V_CODRETORNO2,
                        V_DATOSERROR2);
                    IF (V_CODRETORNO2 <> '0') THEN
                        RAISE E_ERROR2;
                    END IF;

                    -- JPT: Recorremos MATRIZ M_CG_FACTURABLE(1..IND_CG_FACTURABLE)
                    V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Recorremos MATRIZ M_CG_FACTURABLE(1..IND_CG_FACTURABLE)';
                    FOR I IN 1 .. IND_CG_FACTURABLE LOOP
                    
                        -- JPT: Procedimiento para catalanes que carga V_CONFIG_GUARDIA 
                        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Invocamos PROC_CARGA_CONFIG_GUARDIA';
                        PROC_CARGA_CONFIG_GUARDIA(
                            P_IDINSTITUCION,
                            V_TURNO.IDTURNO,
                            V_GUARDIASTURNO.IDGUARDIA,        
                            V_CONFIGGUARDIAACTUAL,
                            M_CG_FACTURABLE(I).IDFACTURACION,
                            V_CODRETORNO2,
                            V_DATOSERROR2);
                        IF (V_CODRETORNO2 <> '0') THEN
                            RAISE E_ERROR2;
                        END IF;                                                         

                        -- INICIALMENTE SOLO PARA EL COLEGIO DE CANTABRIA (2016)
                        V_ESFACTURACIONCONTROLADA := FALSE;
                        V_DATOSERROR2 := 'Consulta si es facturacion controlada';
                        IF (C_FACTURACION_CONTROLADA = TRUE AND P_IDINSTITUCION = 2016) THEN

                            V_DATOSERROR2 := 'Inicia facturacion controlada' ||
                                                            '; V_CONFIG_GUARDIA.NUMHITOS:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.NUMHITOS), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.DIASPAGAGUARDIA:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.DIASPAGAGUARDIA), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.IMPORTEGUARDIA:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.IMPORTEGUARDIA), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA:' || NVL(TO_CHAR(V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA), 'NULL') ||
                                                            '; V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA:' || NVL(V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA, 'NULL');
                            FOR AUX IN 1..V_CONFIG_GUARDIA.NUMHITOS LOOP
                                V_DATOSERROR2 := V_DATOSERROR2 || '; V_CONFIG_GUARDIA.LISTAHITOS(' || AUX || '):' || NVL(TO_CHAR(V_CONFIG_GUARDIA.LISTAHITOS(AUX)), 'NULL');
                            END LOOP;

                            IF (V_CONFIG_GUARDIA.NUMHITOS = 3
                                AND V_CONFIG_GUARDIA.LISTAHITOS(1) = 5
                                AND V_CONFIG_GUARDIA.LISTAHITOS(2) = 12
                                AND V_CONFIG_GUARDIA.LISTAHITOS(3) = 13
                                AND V_CONFIG_GUARDIA.IMPORTEASISTENCIA IS NOT NULL
                                AND V_CONFIG_GUARDIA.IMPORTEASISTENCIA <> 0.0) THEN

                                V_DATOSERROR2 := 'Facturacion por asistencias (sin minimos, sin maximos, sin tipos)';
                                PROC_FAC_AS_CONTROLADO (
                                    V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA,
                                    V_CONFIG_GUARDIA.IMPORTEASISTENCIA,
                                    V_DATOS_FACTURACION.FECHADESDE,
                                    V_DATOS_FACTURACION.FECHAHASTA,
                                    M_CG_FACTURABLE(I),
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                V_ESFACTURACIONCONTROLADA := TRUE;

                            ELSIF (V_CONFIG_GUARDIA.NUMHITOS = 4
                                AND V_CONFIG_GUARDIA.LISTAHITOS(1) = 5
                                AND V_CONFIG_GUARDIA.LISTAHITOS(2) = 10
                                AND V_CONFIG_GUARDIA.LISTAHITOS(3) = 12
                                AND V_CONFIG_GUARDIA.LISTAHITOS(4) = 13
                                AND V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA=0
                                AND V_CONFIG_GUARDIA.IMPORTEASISTENCIA IS NOT NULL
                                AND V_CONFIG_GUARDIA.IMPORTEASISTENCIA <> 0.0
                                AND V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA IS NOT NULL
                                AND V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA <> 0.0) THEN

                                V_DATOSERROR2 := 'Facturacion por asistencias configuradas con minimos y no agrupadas (sin maximos, ni tipos)';
                                PROC_FAC_ASMIN_CONTROLADO (
                                    V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA,
                                    V_CONFIG_GUARDIA.IMPORTEASISTENCIA,
                                    V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA,
                                    V_DATOS_FACTURACION.FECHADESDE,
                                    V_DATOS_FACTURACION.FECHAHASTA,
                                    M_CG_FACTURABLE(I),
                                    5,
                                    27,
                                    0,
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                V_ESFACTURACIONCONTROLADA := TRUE;

                            ELSIF (V_CONFIG_GUARDIA.NUMHITOS = 5
                                AND V_CONFIG_GUARDIA.LISTAHITOS(1) = 5
                                AND V_CONFIG_GUARDIA.LISTAHITOS(2) = 10
                                AND V_CONFIG_GUARDIA.LISTAHITOS(3) = 12
                                AND V_CONFIG_GUARDIA.LISTAHITOS(4) = 13
                                AND V_CONFIG_GUARDIA.LISTAHITOS(5) = 20
                                AND V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA=0
                                AND V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA IS NOT NULL
                                AND V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA <> 0.0) THEN

                                V_DATOSERROR2 := 'Facturacion por asistencias configuradas con tipos, con minimos y no agrupadas (sin maximos)';
                                PROC_FAC_ASMIN_CONTROLADO (
                                    V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA,
                                    V_CONFIG_GUARDIA.IMPORTEASISTENCIA,
                                    V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA,
                                    V_DATOS_FACTURACION.FECHADESDE,
                                    V_DATOS_FACTURACION.FECHAHASTA,
                                    M_CG_FACTURABLE(I),
                                    20,
                                    28,
                                    1,
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                V_ESFACTURACIONCONTROLADA := TRUE;

                            ELSIF (V_CONFIG_GUARDIA.NUMHITOS = 5
                                AND V_CONFIG_GUARDIA.LISTAHITOS(1) = 1
                                AND V_CONFIG_GUARDIA.LISTAHITOS(2) = 2
                                AND V_CONFIG_GUARDIA.LISTAHITOS(3) = 12
                                AND V_CONFIG_GUARDIA.LISTAHITOS(4) = 13
                                AND V_CONFIG_GUARDIA.LISTAHITOS(5) = 45
                                AND V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA=0
                                AND V_CONFIG_GUARDIA.IMPORTEGUARDIA IS NOT NULL
                                AND V_CONFIG_GUARDIA.IMPORTEGUARDIA <> 0.0
                                AND V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA IN (0.0, V_CONFIG_GUARDIA.IMPORTEGUARDIA)) THEN

                                V_DATOSERROR2 := 'Facturacion por guardias (sin minimos, sin maximos, sin tipos)';
                                PROC_FAC_GAS_CONTROLADO (
                                    V_CONFIG_GUARDIA.DIASPAGAGUARDIA,
                                    V_CONFIG_GUARDIA.IMPORTEGUARDIA,
                                    V_DATOS_FACTURACION.FECHADESDE,
                                    V_DATOS_FACTURACION.FECHAHASTA,
                                    M_CG_FACTURABLE(I),
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                V_ESFACTURACIONCONTROLADA := TRUE;
                            END IF;

                            IF (V_ESFACTURACIONCONTROLADA = TRUE) THEN
                                IF (V_CODRETORNO2 = '0') THEN
                                    -- Llamamos al procedimiento que realiza los apuntes en bb.dd y borra las matrices de memoria para la siguiente cabecera de guardia
                                    V_DATOSERROR2 := 'Apuntes en bb.dd y borrado de las matrices de memoria';
                                    PROC_FACT_DESC_MATR_GUARDIA(
                                        P_IDINSTITUCION,
                                        P_IDFACTURACION,
                                        P_USUMODIFICACION,
                                        V_CODRETORNO,
                                        V_DATOSERROR);
                                    IF (V_CODRETORNO <> '0') THEN
                                        V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
                                        RAISE E_ERROR2;
                                    END IF;

                                ELSE
                                    RAISE E_ERROR2;
                                END IF;
                            END IF;
                        END IF;

                        IF (V_ESFACTURACIONCONTROLADA = FALSE) THEN

                            -- Si se factura por Guardia
                            IF (V_CONFIG_GUARDIA.GUARDIA = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN

                                V_DATOSERROR2 := 'Facturacion por Guardias';
                                -- Si se dobla por numero de asistencias (A1)
                                IF (V_CONFIG_GUARDIA.DOBLAASISTENCIA = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN

                                    V_DATOSERROR2 := 'Facturacion por Guardias doblando asistencias';
                                    PROC_FACT_GUARDIA_DOBLAASIST(I, V_CODRETORNO2, V_DATOSERROR2);

                                ELSE
                                    -- Si se dobla por numero de actuaciones (A2)
                                    V_DATOSERROR2 := 'Facturacion por Guardias doblando actuaciones';
                                    PROC_FACT_GUARDIA_DOBLAACT(I, V_CODRETORNO2, V_DATOSERROR2);
                                END IF;
                                IF (V_CODRETORNO2 <> '0') THEN
                                    RAISE E_ERROR2;
                                END IF;       
                                                     
                                -- Llamamos al procedimiento que realiza los apuntes en bb.dd y borra las matrices de memoria para la siguiente cabecera de guardia
                                V_DATOSERROR2 := 'Apuntes en bb.dd y borrado de las matrices de memoria';
                                PROC_FACT_DESC_MATR_GUARDIA(
                                    P_IDINSTITUCION,
                                    P_IDFACTURACION,
                                    P_USUMODIFICACION,
                                    V_CODRETORNO,
                                    V_DATOSERROR);                                
                                IF (V_CODRETORNO <> '0') THEN
                                    V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
                                    RAISE E_ERROR2;
                                END IF;                  
                            END IF;

                            -- Si se factura por asistencias
                            IF (V_CONFIG_GUARDIA.ASISTENCIA = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN

                                --Si no se factura por Guardia (B2)
                                V_DATOSERROR2 := 'Facturacion por Asistencias';
                                -- Si se factura por Asistencia (A1 y B2) y no aplica tipos de Asistencia
                                IF (V_CONFIG_GUARDIA.TIPOASISTENCIA = PKG_SIGA_CONSTANTES.DB_FALSE_N) THEN
                                    V_DATOSERROR2 := 'Facturacion por Asistencias sin aplicar tipos';
                                    PROC_FACT_ASIST_NOAPLICATIPO(I, V_CODRETORNO2, V_DATOSERROR2);

                                --Si se factura por Asistencia (A1 y B2) y aplica tipos de Asistencia
                                ELSIF (V_CONFIG_GUARDIA.TIPOASISTENCIA = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN
                                    V_DATOSERROR2 := 'Facturacion por Asistencias aplicando tipos';
                                    PROC_FACT_ASIST_APLICATIPO(I, V_CODRETORNO2, V_DATOSERROR2);
                                END IF;
                                IF (V_CODRETORNO2 <> '0') THEN
                                    RAISE E_ERROR2;
                                END IF;       
                                                     
                                -- Llamamos al procedimiento que realiza los apuntes en bb.dd y borra las matrices de memoria para la siguiente cabecera de guardia
                                V_DATOSERROR2 := 'Apuntes en bb.dd y borrado de las matrices de memoria';
                                PROC_FACT_DESC_MATR_GUARDIA(
                                    P_IDINSTITUCION,
                                    P_IDFACTURACION,
                                    P_USUMODIFICACION,
                                    V_CODRETORNO,
                                    V_DATOSERROR);                                
                                IF (V_CODRETORNO <> '0') THEN
                                    V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
                                    RAISE E_ERROR2;
                                END IF;                          
                            
                            ELSIF (V_CONFIG_GUARDIA.ACTUACION = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN                            

                                --Si no se factura por Guardia (B2)
                                V_DATOSERROR2 := 'Facturacion por Actuacion';
                                -- Si se factura por Asistencia (A1 y B2) y no aplica tipos de Asistencia
                                IF (V_CONFIG_GUARDIA.TIPOACTUACION = PKG_SIGA_CONSTANTES.DB_FALSE_N) THEN
                                    V_DATOSERROR2 := 'Facturacion por Actuacion si aplicar tipos';
                                    PROC_FACT_ACT_NOAPLICATIPO(I, V_CODRETORNO2, V_DATOSERROR2);

                                --Si se factura por Asistencia (A1 y B2) y aplica tipos de Asistencia
                                ELSIF (V_CONFIG_GUARDIA.TIPOACTUACION = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN
                                    V_DATOSERROR2 := 'Facturacion por Actuacion aplicando tipos';
                                    PROC_FACT_ACT_APLICATIPO(I, V_CODRETORNO2, V_DATOSERROR2);
                                END IF;
                                IF (V_CODRETORNO2 <> '0') THEN
                                    RAISE E_ERROR2;
                                END IF;         
                                                   
                                -- Llamamos al procedimiento que realiza los apuntes en bb.dd y borra las matrices de memoria para la siguiente cabecera de guardia
                                V_DATOSERROR2 := 'Apuntes en bb.dd y borrado de las matrices de memoria';
                                PROC_FACT_DESC_MATR_GUARDIA(
                                    P_IDINSTITUCION,
                                    P_IDFACTURACION,
                                    P_USUMODIFICACION,
                                    V_CODRETORNO,
                                    V_DATOSERROR);                                
                                IF (V_CODRETORNO <> '0') THEN
                                    V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
                                    RAISE E_ERROR2;
                                END IF;
                            END IF;
                        END IF;
                    END LOOP; -- JPT: MATRIZ - M_CG_FACTURABLE(1..IND_CG_FACTURABLE)
                    
                    /******************* Tratamiento de FUERAS DE GUARDIA *******************/
                    IND_CG_FACTURABLEFG := IND_CG_FACTURABLE + 1;
                    IF (FUNC_ES_COLEGIO_CATALAN(P_IDINSTITUCION) = TRUE) THEN   
                        V_CONFIG_GUARDIA := V_CONFIGGUARDIAACTUAL; -- Carga por defecto la guardia actual
                    END IF;     
                                        
                    /* JPT: Cargamos la MATRIZ de la Cabecera de Guardias a facturar [M_CG_FACTURABLE(++IND_CG_FACTURABLE)]
                        - Obtiene las cabeceras de guardia validadas, que tengan por lo menos una actuacion fuera de guardia sin anular, justificada dentro del periodo de la facturacion y sin facturar 
                            (tabla SCS_CABECERAGUARDIAS + SCS_GUARDIASCOLEGIADO + SCS_ASISTENCIA + SCS_ACTUACIONASISTENCIA + FCS_FACT_ACTUACIONASISTENCIA), 
                            y carga los datos en M_CG_FACTURABLE(++IND_CG_FACTURABLE)*/
                    V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_CARGAR_CABGUARDIASFG';
                    PROC_CARGAR_CABGUARDIASFG(
                        P_IDINSTITUCION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        V_CODRETORNO2,
                        V_DATOSERROR2);
                    IF (V_CODRETORNO2 <> '0') THEN
                        RAISE E_ERROR2;
                    END IF;                        
                    
                    -- JPT: Recorremos MATRIZ M_CG_FACTURABLE(1..IND_CG_FACTURABLE)
                    V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Recorremos MATRIZ M_CG_FACTURABLE(1..IND_CG_FACTURABLE)';
                    FOR I IN IND_CG_FACTURABLEFG .. IND_CG_FACTURABLE LOOP
                    
                        -- JPT: Procedimiento para catalanes que carga V_CONFIG_GUARDIA 
                        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Invocamos PROC_CARGA_CONFIG_GUARDIA';
                        PROC_CARGA_CONFIG_GUARDIA(
                            P_IDINSTITUCION,
                            V_TURNO.IDTURNO,
                            V_GUARDIASTURNO.IDGUARDIA,        
                            V_CONFIGGUARDIAACTUAL,
                            M_CG_FACTURABLE(I).IDFACTURACION,
                            V_CODRETORNO2,
                            V_DATOSERROR2);
                        IF (V_CODRETORNO2 <> '0') THEN
                            RAISE E_ERROR2;
                        END IF;                          

                        IF (V_CONFIG_GUARDIA.ACTUACIONFG = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN
                            V_DATOSERROR2 := 'Facturacion por Actuacion Fuera de Guardia';
                            -- Si se factuara por Actuacion fuera de Guardia (C)
                            IF (V_CONFIG_GUARDIA.TIPOACTUACIONFG = PKG_SIGA_CONSTANTES.DB_FALSE_N) THEN
                                V_DATOSERROR2 := 'Facturacion por Actuacion Fuera de Guardia sin aplicar tipos';
                                PROC_FACT_ACTFG_NOAPLICATIPO(I, V_CODRETORNO2, V_DATOSERROR2);

                            ELSIF (V_CONFIG_GUARDIA.TIPOACTUACIONFG = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN
                                V_DATOSERROR2 := 'Facturacion por Actuacion Fuera de Guardia aplicando tipos';
                                PROC_FACT_ACTFG_APLICATIPO(I, V_CODRETORNO2, V_DATOSERROR2);
                            END IF;                            
                            IF (V_CODRETORNO2 <> '0') THEN
                                RAISE E_ERROR2;
                            END IF;                   
                                     
                            -- Llamamos al procedimiento que realiza los apuntes en bb.dd y borra las matrices de memoria para la siguiente cabecera de guardia
                            V_DATOSERROR2 := 'Apuntes en bb.dd y borrado de las matrices de memoria';
                            PROC_FACT_DESC_MATR_GUARDIA(
                                P_IDINSTITUCION,
                                P_IDFACTURACION,
                                P_USUMODIFICACION,
                                V_CODRETORNO,
                                V_DATOSERROR);                                
                            IF (V_CODRETORNO <> '0') THEN
                                V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
                                RAISE E_ERROR2;
                            END IF;
                        END IF;                                                                 
                    END LOOP; -- JPT: MATRIZ - M_CG_FACTURABLE(IND_CG_FACTURABLEFG..IND_CG_FACTURABLE)                    
                END LOOP; -- JPT: CURSOR - V_GUARDIASTURNO IN C_GUARDIASTURNO
            END LOOP; -- JPT: CURSOR - V_TURNO IN C_TURNO
        END LOOP; -- JPT: CURSOR - V_GRUPOSFACTURACION IN C_GRUPOSFACTURACION

        BEGIN
            IF TOTAL_FACTURACION IS NULL THEN
                P_TOTAL := '0';
            ELSE
                P_TOTAL := TO_CHAR(TOTAL_FACTURACION);
            END IF;

            EXCEPTION
                WHEN OTHERS THEN
                    P_TOTAL := '0';
        END;

        --Todos los elementos utilizados para la facturacion se marcan como facturados.
        V_DATOSERROR2 := 'Marcado de datos facturados.';
        PROC_FCS_MARCAR_FACTURADOS(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO, V_DATOSERROR);
        IF (V_CODRETORNO <> '0') THEN
            V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
            RAISE E_ERROR2;
        END IF;

        V_CODRETORNO2 := TO_CHAR(0);
        V_DATOSERROR2 := 'El proceso:PROC_FCS_FACTURAR_GUARDIAS ha finalizado correctamente';
        P_CODRETORNO  := V_CODRETORNO2;
        P_DATOSERROR  := V_DATOSERROR2;

        EXCEPTION
            WHEN E_ERROR2 THEN
                P_CODRETORNO := to_char(-1);
                P_DATOSERROR := V_DATOSERROR2;

            WHEN OTHERS THEN
                P_CODRETORNO := to_char(SQLCODE);
                P_DATOSERROR := 'Error en el proceso:PROC_FCS_FACTURAR_GUARDIAS. ' || SQLERRM || V_DATOSERROR2;
    END PROC_FCS_FACTURAR_GUARDIAS;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_FACTURAR_SOJ                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
  /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_FACTURAR_SOJ(P_IDINSTITUCION   IN NUMBER,
                                  P_IDFACTURACION   IN NUMBER,
                                  P_USUMODIFICACION IN NUMBER,
                                  P_TOTAL           OUT VARCHAR2,
                                  P_CODRETORNO      OUT VARCHAR2,
                                  P_DATOSERROR      OUT VARCHAR2) IS
    V_TOTAL      NUMBER := 0; /* variable para sumar */
    V_FECHADESDE DATE;
    V_FECHAHASTA DATE;
    V_PRECIO     NUMBER := 0;

  BEGIN
    /* V_DATOSERROR2 := 'Fin correcto';

    P_CODRETORNO := V_CODRETORNO2;
    P_DATOSERROR := V_DATOSERROR2;
    P_TOTAL      := TO_CHAR(0);*/

    -- trazas
    V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';

    DECLARE
      --Declaracon de cursores
      CURSOR C_GRUPOSFACTURACION IS
        SELECT FCS_FACT_GRUPOFACT_HITO.IDGRUPOFACTURACION,
               FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION
          FROM FCS_FACT_GRUPOFACT_HITO
         WHERE FCS_FACT_GRUPOFACT_HITO.IDINSTITUCION = P_IDINSTITUCION
           AND FCS_FACT_GRUPOFACT_HITO.IDFACTURACION = P_IDFACTURACION
           AND FCS_FACT_GRUPOFACT_HITO.IDHITOGENERAL =
               PKG_SIGA_CONSTANTES.HITO_GENERAL_SOJ;

      CURSOR C_TURNO(P_IDGRUPOFACT number) IS
        SELECT SCS_TURNO.IDTURNO, SCS_TURNO.IDINSTITUCION
          FROM SCS_TURNO
         WHERE IDINSTITUCION = P_IDINSTITUCION
           AND IDGRUPOFACTURACION = P_IDGRUPOFACT;

      CURSOR C_GUARDIASTURNO(P_IDTURNO number) IS
        SELECT scs_guardiasturno.idguardia
          FROM scs_guardiasturno
         WHERE scs_guardiasturno.idinstitucion = P_IDINSTITUCION
           AND scs_guardiasturno.idturno = P_IDTURNO;
      --         AND scs_guardiasturno.idguardiasustitucion IS NULL  -- DCG para no facturar las guardias de sustitucion
      --         AND scs_guardiasturno.idturnosustitucion IS NULL;   -- DCG

      CURSOR C_SOJ(P_IDTURNO number, P_IDGUARDIA number, V_FECHADESDE DATE, V_FECHAHASTA DATE) IS
        SELECT scs_soj.idtiposoj,
               scs_soj.anio,
               scs_soj.numero,
               scs_soj.fechaapertura,
               scs_soj.idpersona
          FROM scs_soj
         WHERE scs_soj.idinstitucion = P_IDINSTITUCION
           AND scs_soj.idguardia = P_IDGUARDIA
           and scs_soj.idturno = P_IDTURNO
              -- Se pueden haber creado SOJ sin letrado asociado
           and scs_soj.idpersona is not null
              ------------------------------------------
           and (trunc(scs_soj.fechaapertura) between V_FECHADESDE AND V_FECHAHASTA)
           and nvl(scs_soj.facturado, 0) <> 1;

    BEGIN

      -- 1? Hacemos una consulta sobre la tabla FCS_FACTURACIONJG para obtener
      -- la FECHADESDE y la FECHAHASTA del idinstitucion y del idfacturacion
      -- introducidos como parametros

      V_DATOSERROR2 := 'Antes de obtener las fechas desde y hasta de facturacion';

      SELECT fac.fechadesde, fac.fechahasta
        INTO V_FECHADESDE, V_FECHAHASTA
        FROM Fcs_Facturacionjg fac
       WHERE fac.idfacturacion = P_IDFACTURACION
         AND fac.idinstitucion = P_IDINSTITUCION;
      -- trazas

      V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';
      -- Grupos de facturacion
      FOR V_GRUPOSFACTURACION IN C_GRUPOSFACTURACION LOOP

        -- trazas
        V_DATOSERROR2 := 'Antes de ejecutar turnos';

        -- Turnos
        FOR V_TURNO IN C_TURNO(V_GRUPOSFACTURACION.IDGRUPOFACTURACION) LOOP
          -- Guardias turno
          V_DATOSERROR2 := 'Antes de ejecutar Actuacion Designa';

          FOR V_GUARDIASTURNO IN C_GUARDIASTURNO(V_TURNO.IDTURNO) LOOP

            --Guardamos el historico de los hitos de la facturacion
            V_DATOSERROR2 := 'Antes de ejecutar el historico de los hitos';
            PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
                P_IDINSTITUCION,
                P_IDFACTURACION,
                V_TURNO.IDTURNO,
                V_GUARDIASTURNO.IDGUARDIA,
                HITO_SOJ,
                NULL,
                NULL,
                V_CODRETORNO2,
                V_DATOSERROR2);

            -- Obtenemos el precio del EJG
            begin
              SELECT nvl(scs_hitofacturableguardia.preciohito, 0)
                into V_PRECIO
                FROM scs_hitofacturableguardia
               WHERE scs_hitofacturableguardia.idinstitucion =
                     P_IDINSTITUCION
                 AND scs_hitofacturableguardia.idturno = V_TURNO.IDTURNO
                 AND scs_hitofacturableguardia.idguardia =
                     V_GUARDIASTURNO.IDGUARDIA
                 AND scs_hitofacturableguardia.idhito = HITO_SOJ;
            exception
              when no_data_found then
                V_PRECIO := 0;
            end;

            FOR V_SOJ IN C_SOJ(V_TURNO.IDTURNO,
                               V_GUARDIASTURNO.IDGUARDIA,
                               V_FECHADESDE,
                               V_FECHAHASTA) LOOP

              -- Actualizamos la variable v_Total antes de hacer el apunte en
              -- la tabla de facturacion
              V_TOTAL := V_TOTAL + V_PRECIO;

              -- INSERTAMOS EL APUNTE EN FCS_FACT_EJG

              V_DATOSERROR2 := 'Antes de insertar en FCS_FACT_ACTUACIONDESIGNA';
              INSERT INTO FCS_FACT_SOJ
                (IDINSTITUCION,
                 IDFACTURACION,
                 IDTIPOSOJ,
                 ANIO,
                 NUMERO,
                 IDPERSONA,
                 IDTURNO,
                 IDGUARDIA,
                 PRECIOAPLICADO,
                 FECHAAPERTURA,
                 FECHAMODIFICACION,
                 USUMODIFICACION)
              VALUES
                (P_IDINSTITUCION,
                 P_IDFACTURACION,
                 V_SOJ.IDTIPOSOJ,
                 V_SOJ.ANIO,
                 V_SOJ.NUMERO,
                 V_SOJ.IDPERSONA,
                 V_TURNO.IDTURNO,
                 V_GUARDIASTURNO.IDGUARDIA,
                 V_PRECIO,
                 V_SOJ.FECHAAPERTURA,
                 SYSDATE,
                 P_USUMODIFICACION);

              -- trazas
              V_DATOSERROR2 := 'Antes de actualizar en SCS_EJG';

              -- INDICAMOS QUE LO HEMOS CALCULADO YA
              UPDATE SCS_SOJ
                 SET FACTURADO     = PKG_SIGA_CONSTANTES.DB_TRUE_N,
                     IDFACTURACION = P_IDFACTURACION

               WHERE IDINSTITUCION = P_IDINSTITUCION
                 AND ANIO = V_SOJ.ANIO
                 AND NUMERO = V_SOJ.NUMERO
                 AND IDTIPOSOJ = V_SOJ.IDTIPOSOJ;

            END LOOP; -- Fin Actuacion Designa*/
          END LOOP; -- Fin Turnos
        END LOOP; -- Fin Grupos Facturacion
      END LOOP;

      -- trazas
      V_DATOSERROR2 := 'Fin correcto';

      P_CODRETORNO := V_CODRETORNO2;
      P_DATOSERROR := V_DATOSERROR2;
      P_TOTAL      := TO_CHAR(V_TOTAL);

    EXCEPTION
      WHEN E_ERROR2 THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
      WHEN NO_DATA_FOUND THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';
      WHEN OTHERS THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';

    END;

  END; -- Procedure PROC_FCS_FACTURAR_SOJ


  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_BORRAR_FACTURACION                                                                        */
  /* Descripcion: Elimina una facturacion borrando la facturacion y sus apuntes (borrado en cascada) y actualizando*/
  /*              los conceptos de SJCS (facturado = null)                                                        */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* 07/03/2006           ACG                                 Adaptacion al nuevo modelo de datos                 */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_BORRAR_FACTURACION(P_IDINSTITUCION IN NUMBER,
                                        P_IDFACTURACION IN NUMBER,
                                        P_CODRETORNO    OUT VARCHAR2,
                                        P_DATOSERROR    OUT VARCHAR2) IS
    -- VARIABLES
    V_ESTADO    VARCHAR2(50) := TO_CHAR(0);
    V_PREVISION VARCHAR2(50) := TO_CHAR(0);

  BEGIN

    -- trazas
    V_DATOSERROR := TO_CHAR('Borrar: Antes de consultar estado');

    --Obtener el Estado de la Facturacion
    SELECT FCS_ESTADOSFACTURACION.IDESTADOFACTURACION
      INTO V_ESTADO
      from FCS_FACT_ESTADOSFACTURACION est, FCS_ESTADOSFACTURACION
     where est.FECHAESTADO =
           (select max(est2.FECHAESTADO)
              from FCS_FACT_ESTADOSFACTURACION est2
             where est2.IDFACTURACION = P_IDFACTURACION
               and est2.IDINSTITUCION = P_IDINSTITUCION)
       and est.IDFACTURACION = P_IDFACTURACION
       and est.IDINSTITUCION = P_IDINSTITUCION
       and est.IDESTADOFACTURACION =
           FCS_ESTADOSFACTURACION.IDESTADOFACTURACION;

    -- VER SI ES PREVISION
    SELECT Prevision
      INTO V_PREVISION
      FROM FCS_FACTURACIONJG
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDFACTURACION = P_IDFACTURACION;

    --trazas
    V_DATOSERROR2 := TO_CHAR(V_ESTADO);

    --SI ES PREVISION, BASTA CON BORRAR LA FACTURACION
/*    IF V_PREVISION = PKG_SIGA_CONSTANTES.DB_TRUE_N THEN

      --si es regularizacion, solo se borra la FacturacionJG
      DELETE FROM FCS_FACTURACIONJG
       WHERE IDFACTURACION = P_IDFACTURACION
         AND IDINSTITUCION = P_IDINSTITUCION;

      --SI LA FACTURACION ESTA LISTA PARA CONSEJO, LANZAR ERROR1
    ELSIF V_ESTADO = PKG_SIGA_CONSTANTES.ESTADO_FACTURA_LISTA_CONSEJO THEN

      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura en estado Lista Para Consejo');
      RAISE E_ERROR_BF_1;

      --SI LA FACTURACION ESTA ABIERTA
    ELSIF V_ESTADO = PKG_SIGA_CONSTANTES.ESTADO_FACTURA_ABIERTA THEN

      -- BORRAR LA FACTURACION. POR BORRADO EN CASCADA SE BORRA EL ESTADO Y LOS APUNTES
      DELETE FROM FCS_FACTURACIONJG
       WHERE IDFACTURACION = P_IDFACTURACION
         AND IDINSTITUCION = P_IDINSTITUCION;
      --trazas
      V_DATOSERROR := TO_CHAR('Borrar: Factura estado abierta, despues de borrar la Facturacion');

      --SI LA FACTURACION ESTA EJECUTADA
    ELSIF V_ESTADO = PKG_SIGA_CONSTANTES.ESTADO_FACTURA_EJECUTADA THEN
    */
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura antes de borrar en Actuacion Designa');

      --ELIMINAR DE ACTUACION DESIGNA
      PROC_FCS_ELIMINAR_ACT_DES(P_IDINSTITUCION,
                                P_IDFACTURACION,
                                V_CODRETORNO2,
                                V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_2;
      END IF;

      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Actuacion Designa');

      --ELIMINAR DE ACTUACION ASISTENCIA
      PROC_FCS_ELIMINAR_ACT_ASIS(P_IDINSTITUCION,
                                 P_IDFACTURACION,
                                 V_CODRETORNO2,
                                 V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_4;
      END IF;
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Actuacion Asistencia');

      --ELIMINAR DE ASISTENCIA
      PROC_FCS_ELIMINAR_ASISTENCIA(P_IDINSTITUCION,
                                   P_IDFACTURACION,
                                   V_CODRETORNO2,
                                   V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_3;
      END IF;

      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Asistencia');

      --ELIMINAR DE GUARDIAS COLEGIADO
      PROC_FCS_ELIMINAR_GUAR_COL(P_IDINSTITUCION,
                                 P_IDFACTURACION,
                                 V_CODRETORNO2,
                                 V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_5;
      END IF;
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Guardias Colegiado');

      --ELIMINAR DE APUNTES (CABECERA DE GUARDIAS)
      PROC_FCS_ELIMINAR_APUNTES(P_IDINSTITUCION,
                                P_IDFACTURACION,
                                V_CODRETORNO2,
                                V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_5;
      END IF;
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Apuntes');

      --ELIMINAR DE EXPEDIENTES SOJ
      PROC_FCS_ELIMINAR_EXP_SOJ(P_IDINSTITUCION,
                                P_IDFACTURACION,
                                V_CODRETORNO2,
                                V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_6;
      END IF;
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Expedientes SOJ');

      --ELIMINAR DE EXPEDIENTES EJG
      PROC_FCS_ELIMINAR_EXP_EJG(P_IDINSTITUCION,
                                P_IDFACTURACION,
                                V_CODRETORNO2,
                                V_DATOSERROR2);
      IF (V_CODRETORNO2 <> TO_CHAR(0)) THEN
        RAISE E_ERROR_BF_7;
      END IF;
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar en Expedientes EJG');

      --borrar los historicos
      PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTO_BORRAR_FACT(P_IDINSTITUCION,
                                          P_IDFACTURACION,
                                          V_CODRETORNO2,
                                          V_DATOSERROR2);

      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura despues de borrar historicos');

      -- BORRAR LA FACTURACION. POR BORRADO EN CASCADA SE BORRA EL ESTADO Y LOS APUNTES
      DELETE FROM FCS_FACTURACIONJG
       WHERE IDFACTURACION = P_IDFACTURACION
         AND IDINSTITUCION = P_IDINSTITUCION;
      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar: Factura estado ejecutada, despues de borrar la Facturacion');

      --EN OTRO CASO, SI LA FACTURACION ESTA ABIERTA
/*    ELSE

      --trazas
      V_DATOSERROR2 := TO_CHAR('Borrar Factura en estado Lista Para Consejo');
      RAISE E_ERROR_BF_1;

    END IF;
*/
    V_DATOSERROR2 := TO_CHAR('Fin correcto');

    P_CODRETORNO := V_CODRETORNO2;
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN E_ERROR_BF_1 THEN
      P_CODRETORNO := TO_CHAR('1');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.estadoNoCorrecto');
    WHEN E_ERROR_BF_2 THEN
      P_CODRETORNO := TO_CHAR('2');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.borrarActuacionDesignas');
    WHEN E_ERROR_BF_3 THEN
      P_CODRETORNO := TO_CHAR('3');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.borrarAsistencias');
    WHEN E_ERROR_BF_4 THEN
      P_CODRETORNO := TO_CHAR('4');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.borrarActuacionDesignas');
    WHEN E_ERROR_BF_5 THEN
      P_CODRETORNO := TO_CHAR('5');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.borrarGuardias');
    WHEN E_ERROR_BF_6 THEN
      P_CODRETORNO := TO_CHAR('6');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.borrarExpedientesSoj');
    WHEN E_ERROR_BF_7 THEN
      P_CODRETORNO := TO_CHAR('7');
      P_DATOSERROR := TO_CHAR('messages.factSJCS.error.borrarExpedientesEjg');
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --Procedure PROC_FCS_BORRAR_FACTURACION

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_ACTUACIONDESIGNA                                                                 */
  /* Descripcion:   Pone a no facturadas las actuaciones de designaciones                                         */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                        */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_ACT_DES(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2) IS
    --VARIABLES

  BEGIN
    --TRAZAS
    V_DATOSERROR2 := TO_CHAR('PROC_FCS_ELIMINAR_ACT_DES: Antes de empezar a recorrer el cursor');

    UPDATE SCS_ACTUACIONDESIGNA
       SET FACTURADO = NULL, IDFACTURACION = NULL
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

    --TRAZAS
    V_DATOSERROR2 := TO_CHAR('PROC_FCS_ELIMINAR_ACT_DES: Fin Correcto');

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --Procedure PROC_FCS_ELIMINAR_ASISTENCIA

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_ASISTENCIA                                                                       */
  /* Descripcion: Pone a no facturadas las asistencias                                                            */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                        */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_ASISTENCIA(P_IDINSTITUCION IN NUMBER,
                                         P_IDFACTURACION IN NUMBER,
                                         P_CODRETORNO    OUT VARCHAR2,
                                         P_DATOSERROR    OUT VARCHAR2) IS
    --VARIABLES

  BEGIN
    --TRAZAS
    V_DATOSERROR := TO_CHAR('Antes de empezar a modificar');

    --MODIFICAR EN SCS_ASISTENCIA
    UPDATE SCS_ASISTENCIA
       SET FACTURADO = NULL, IDFACTURACION = NULL
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

    --trazas
    V_DATOSERROR2 := TO_CHAR('Fin Correcto');

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --Procedure PROC_FCS_ELIMINAR_ASISTENCIA

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_ACTUACIONASISTENCIA                                                              */
  /* Descripcion: Pone a no facturado las actuaciones de Asistencia                                               */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                         */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_ACT_ASIS(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2)

   IS
    --variables

  BEGIN
    --TRAZAS
    V_DATOSERROR := TO_CHAR('Antes de empezar a modificar');

    --MODIFICAR EN SCS_ASISTENCIA
    UPDATE SCS_ACTUACIONASISTENCIA
       SET FACTURADO = NULL, IDFACTURACION = NULL
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

    --trazas
    V_DATOSERROR2 := TO_CHAR('Fin Correcto');

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --procedure PROC_FCS_ELIMINAR_ACTUACIONASISTENCIA

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_GUARDIASCOLEGIADO                                                                */
  /* Descripcion: Pone a no facturado los dias de guardia                                                         */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                       */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_GUAR_COL(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2) IS
    --VARIABLES

  BEGIN
    --TRAZAS
    V_DATOSERROR := TO_CHAR('Antes de empezar a modificar');

    --MODIFICAR EN SCS_GUARDIASCOLEGIADO
    UPDATE SCS_GUARDIASCOLEGIADO
       SET FACTURADO = NULL, IDFACTURACION = NULL
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

    --trazas
    V_DATOSERROR2 := TO_CHAR('Fin Correcto');

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --    PROCEDURE PROC_FCS_ELIMINAR_GUAR_COL

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_APUNTES                                                                          */
  /* Descripcion: Pone a no facturado la cabecera de guardias                                                     */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Nuevo                                            */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_APUNTES(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2) IS
    --VARIABLES

  BEGIN
    --TRAZAS
    V_DATOSERROR := TO_CHAR('Antes de empezar a modificar');

    --MODIFICAR EN SCS_GUARDIASCOLEGIADO
    UPDATE SCS_CABECERAGUARDIAS
       SET FACTURADO = NULL, IDFACTURACION = NULL
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

    --trazas
    V_DATOSERROR2 := TO_CHAR('Fin Correcto');

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --    PROCEDURE PROC_FCS_ELIMINAR_APUNTES

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_EXPEDIENTES_SOJ                                                                  */
  /* Descripcion: Pone a no facturado el expediente SOJ                                                           */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                       */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_EXP_SOJ(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2) IS
    --VARIABLES

  BEGIN

    DECLARE
      CURSOR C_SOJ IS

        SELECT IDINSTITUCION, IDTIPOSOJ, ANIO, NUMERO
          FROM FCS_FACT_SOJ
         WHERE IDINSTITUCION = P_IDINSTITUCION
           AND IDFACTURACION = P_IDFACTURACION;

    BEGIN
      --TRAZAS
      V_DATOSERROR2 := TO_CHAR('Antes de empezar a modificar');

      --PARA CADA FAC_SOJ
      FOR V_SOJ IN C_SOJ LOOP

        --MODIFICAR EN SCS_SOJ

        UPDATE SCS_SOJ
           SET FACTURADO = NULL, IDFACTURACION = NULL
         WHERE IDINSTITUCION = V_SOJ.IDINSTITUCION
           AND IDTIPOSOJ = V_SOJ.IDTIPOSOJ
           AND ANIO = V_SOJ.ANIO
           AND NUMERO = V_SOJ.NUMERO
           AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;
      END LOOP; --DEL LOOP

      --trazas
      V_DATOSERROR2 := TO_CHAR('Fin Correcto');

    END; -- DEL CURSOR

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --    PROCEDURE PROC_FCS_ELIMINAR_EXP_SOJ

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_ELIMINAR_EXPEDIENTES_EJG                                                                  */
  /* Descripcion: Pone a no facturado expedientes EJG                                                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                       */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_ELIMINAR_EXP_EJG(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2) IS
    --VARIABLES
  BEGIN

    DECLARE
      CURSOR C_EJG IS

        SELECT IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO
          FROM FCS_FACT_EJG
         WHERE IDINSTITUCION = P_IDINSTITUCION
           AND IDFACTURACION = P_IDFACTURACION;

    BEGIN
      --TRAZAS
      V_DATOSERROR2 := TO_CHAR('Antes de empezar a modificar');

      --PARA CADA FAC_EJG
      FOR V_EJG IN C_EJG LOOP

        --MODIFICAR EN SCS_EJG

        UPDATE SCS_EJG
           SET FACTURADO = NULL, IDFACTURACION = NULL
         WHERE IDINSTITUCION = V_EJG.IDINSTITUCION
           AND IDTIPOEJG = V_EJG.IDTIPOEJG
           AND ANIO = V_EJG.ANIO
           AND NUMERO = V_EJG.NUMERO
           AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;
      END LOOP; --DEL LOOP

      --trazas
      V_DATOSERROR2 := TO_CHAR('Fin Correcto');

    END; -- DEL CURSOR

    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := V_DATOSERROR2;

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; --    PROCEDURE PROC_FCS_ELIMINAR_EXP_EJG

  --
  -- Nombre:      F_NUM2CHAR
  -- Descripcion: Funcion que comprueba si la persona trabaja para una sociedad
  --
  -- Parametros  IN/OUT  Descripcion                            Tipo de Datos
  -- ----------  ------  -------------------------------------  -------------
  -- P_CADENA    IN      cadena para reemplazar el '.' por ','  VARCHAR2
  --
  -- Historico
  -- ---------
  -- Version: 18/04/2006 - Pilar Duran Munoz: Creacion
  -- Version: 10/02/2008 - Adrian Ayala Gomez: Limpiada y quitada de la
  --          Especificacion
  --
  FUNCTION F_NUM2CHAR(P_CADENA NUMBER) RETURN VARCHAR2 IS
    v_cadena varchar2(4000);
  BEGIN
    v_cadena := replace(p_cadena, '.', ',');
    return(v_cadena);
  END F_NUM2CHAR;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_SOJ                                                                       */
  /* Descripcion: Exporta los datos facturados, pagos o pagos por persona de los SOJ
  /*              segun esten rellenos el P_IDFACTURACION, P_IDPAGO, P_IDPERSONA
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_SOJ(P_IDINSTITUCION IN NUMBER,
                                  P_IDFACTURACION IN NUMBER,
                                  P_IDPAGO        IN NUMBER,
                                  P_IDPERSONA     IN NUMBER,
                                  P_PATHFICHERO   IN VARCHAR2,
                                  P_FICHERO       IN VARCHAR2,
                                  P_CABECERAS     IN VARCHAR2,
                                  P_CODRETORNO    OUT VARCHAR2,
                                  P_DATOSERROR    OUT VARCHAR2) IS

    -- VARIABLES

    V_PERSONA VARCHAR2(500) := '';
    V_TURNO   VARCHAR2(500) := '';

    F_SALIDA   UTL_FILE.FILE_TYPE;
    V_REGISTRO VARCHAR2(4000);
    ESPACIO CONSTANT VARCHAR2(6) := CHR(9);
    v_precio             number(10, 2) := 0;
    V_PERSONAOLD         varchar2(4000);
    V_PERSONANEW         varchar2(4000);
    V_POSICION           NUMBER := 0;
    v_cabletrado         varchar2(4000);
    v_cabturno           varchar2(4000);
    v_cabexped           varchar2(4000);
    v_cabfecha           varchar2(4000);
    v_cabbenef           varchar2(4000);
    v_cabimporte         varchar2(4000);
    v_cabtotal           varchar2(4000);
    v_cabeceras          varchar2(4000);
    v_cabtotalacum       varchar2(4000);
    v_cabImportePagReal  varchar2(4000);
    --v_cabImporteIRPFReal varchar2(4000);
    v_cabImportePagAct   varchar2(4000);
    --v_cabImporteIRPFAct  varchar2(4000);
    v_cabTotalPagoReal   varchar2(4000);
    --v_cabTotalIRPFReal   varchar2(4000);
    v_cabTotalPagoAct    varchar2(4000);
    --v_cabTotalIRPFAct    varchar2(4000);
    V_TOTALPAGOSREAL     NUMBER := 0; -- acumulado de v_cabTotalPagoReal
    V_TOTALIRPFREAL      NUMBER := 0; -- acumulado de v_cabTotalIRPFReal
    V_TOTALPAGOACTUAL    NUMBER := 0; -- acumulado de v_cabTotalPagoAct
    V_TOTALIRPFACTUAL    NUMBER := 0; -- acumulado de v_cabTotalIRPFAct
    v_cabguardia         varchar2(4000);
    CONT                 NUMBER := 0;

    /* Cursor Facturacion SOJ*/
    CURSOR C_FACTSOJ IS
      select fcs_fact_soj.idpersona,
             fcs_fact_soj.idinstitucion,
             ' ' || fcs_fact_soj.anio || '/' || scs_soj.numsoj nexpediente,
             to_char(fcs_fact_soj.fechaapertura, 'dd/mm/yyyy') FECHAAPERTURA,
             fcs_fact_soj.idturno,
             scs_personajg.nombre || ' ' || scs_personajg.apellido1 || ' ' ||
             scs_personajg.apellido2 beneficiario,
             fcs_fact_soj.Precioaplicado,
             cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 || ',' ||
             cen_persona.nombre V_PERSONA,
             scs_turno.abreviatura V_TURNO,
             scs_guardiasturno.nombre guardia

        from fcs_fact_soj,
             scs_soj,
             scs_guardiasturno,
             scs_turno,
             scs_personajg,
             cen_persona
       where fcs_fact_soj.idinstitucion = P_IDINSTITUCION
         and fcs_fact_soj.idfacturacion = P_IDFACTURACION
         and fcs_fact_soj.idinstitucion = scs_soj.idinstitucion
         and fcs_fact_soj.idtiposoj = scs_soj.idtiposoj
         and fcs_fact_soj.anio = scs_soj.anio
         and fcs_fact_soj.numero = scs_soj.numero
         and scs_soj.idturno = scs_turno.idturno
         and scs_soj.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idturno = scs_turno.idturno
         and scs_guardiasturno.idturno = scs_soj.idturno
         and scs_guardiasturno.idguardia = scs_soj.idguardia
         and scs_personajg.idpersona(+) = scs_soj.idpersonajg
         and scs_personajg.idinstitucion(+) = scs_soj.idinstitucion
         and cen_persona.idpersona = scs_soj.idpersona
       order by cen_persona.apellidos1 || cen_persona.apellidos2 ||
                cen_persona.nombre asc,
                fcs_fact_soj.fechaapertura asc;

    /* Cursor de Pagos SOJ*/
    CURSOR C_PAGOSOJ IS
      select fcs_fact_soj.idpersona,
             fcs_fact_soj.idinstitucion,
             ' ' || fcs_fact_soj.anio || '/' || scs_soj.numsoj nexpediente,
             to_char(fcs_fact_soj.fechaapertura, 'dd/mm/yyyy') FECHAAPERTURA,
             fcs_fact_soj.idturno,
             scs_personajg.nombre || ' ' || scs_personajg.apellido1 || ' ' ||
             scs_personajg.apellido2 beneficiario,
             fcs_fact_soj.Precioaplicado,
             cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 || ',' ||
             cen_persona.nombre V_PERSONA,
             scs_turno.abreviatura V_TURNO,
             scs_guardiasturno.nombre guardia,
             /* Diferencia con el cursor C_FACTSOJ*/
             nvl((select NVL(col.impsoj, 0) importeActualPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_soj.idinstitucion
                    and jg.idfacturacion = fcs_fact_soj.idfacturacion
                    and col.idperorigen = fcs_fact_soj.idpersona),
                 0) importeActualPagado,
             0 importeActualIRPF,
             nvl((select NVL(col.impsoj, 0) importeTotalPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_soj.idinstitucion
                    and jg.idfacturacion = fcs_fact_soj.idfacturacion
                    and col.idperorigen = fcs_fact_soj.idpersona),
                 0) importeTotalPagado,
             0 importeTotalIRPF
             /* FIN Diferencia con el cursor C_FACTSOJ*/
        from fcs_fact_soj,
             scs_soj,
             scs_guardiasturno,
             scs_turno,
             scs_personajg,
             cen_persona
       where fcs_fact_soj.idinstitucion = P_IDINSTITUCION
         and fcs_fact_soj.idfacturacion = P_IDFACTURACION
         and fcs_fact_soj.idinstitucion = scs_soj.idinstitucion
         and fcs_fact_soj.idfacturacion = scs_soj.idfacturacion
         and fcs_fact_soj.idinstitucion = scs_soj.idinstitucion
         and fcs_fact_soj.idfacturacion = scs_soj.idfacturacion
         and fcs_fact_soj.idtiposoj = scs_soj.idtiposoj
         and fcs_fact_soj.anio = scs_soj.anio
         and fcs_fact_soj.numero = scs_soj.numero
         and scs_soj.idturno = scs_turno.idturno
         and scs_soj.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idturno = scs_turno.idturno
         and scs_guardiasturno.idturno = scs_soj.idturno
         and scs_guardiasturno.idguardia = scs_soj.idguardia
         and scs_personajg.idpersona(+) = scs_soj.idpersonajg
         and scs_personajg.idinstitucion(+) = scs_soj.idinstitucion
         and cen_persona.idpersona = scs_soj.idpersona
       order by cen_persona.apellidos1 || cen_persona.apellidos2 ||
                cen_persona.nombre asc,
                fcs_fact_soj.fechaapertura asc;

    /* Cursor de Pagos realizados por persona SOJ*/
    CURSOR C_PAGOPERSONASOJ IS
      select fcs_fact_soj.idpersona,
             fcs_fact_soj.idinstitucion,
             ' ' || fcs_fact_soj.anio || '/' || scs_soj.numsoj nexpediente,
             to_char(fcs_fact_soj.fechaapertura, 'dd/mm/yyyy') FECHAAPERTURA,
             fcs_fact_soj.idturno,
             scs_personajg.nombre || ' ' || scs_personajg.apellido1 || ' ' ||
             scs_personajg.apellido2 beneficiario,
             fcs_fact_soj.Precioaplicado,
             cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 || ',' ||
             cen_persona.nombre V_PERSONA,
             scs_turno.abreviatura V_TURNO,
             scs_guardiasturno.nombre guardia,
             /* Diferencia con el cursor C_FACTSOJ*/
           nvl((select NVL(col.impsoj, 0) importeActualPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_soj.idinstitucion
                    and jg.idfacturacion = fcs_fact_soj.idfacturacion
                    and col.idperorigen = fcs_fact_soj.idpersona),
                 0) importeActualPagado,

               0 importeActualIRPF,
            nvl((select NVL(col.impsoj, 0) importeActualPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_soj.idinstitucion
                    and jg.idfacturacion = fcs_fact_soj.idfacturacion
                    and col.idperorigen = fcs_fact_soj.idpersona),
                 0) importeTotalPagado,
             0 importeTotalIRPF
      /***/
        from fcs_fact_soj,
             scs_soj,
             scs_guardiasturno,
             scs_turno,
             scs_personajg,
             cen_persona
       where fcs_fact_soj.idinstitucion = P_IDINSTITUCION
         and fcs_fact_soj.idfacturacion = P_IDFACTURACION
         and fcs_fact_soj.idpersona = P_IDPERSONA
         and -- Diferencia con el cursor C_PAGOSOJ
             fcs_fact_soj.idinstitucion = scs_soj.idinstitucion
         and fcs_fact_soj.idfacturacion = scs_soj.idfacturacion
         and fcs_fact_soj.idinstitucion = scs_soj.idinstitucion
         and fcs_fact_soj.idfacturacion = scs_soj.idfacturacion
         and fcs_fact_soj.idtiposoj = scs_soj.idtiposoj
         and fcs_fact_soj.anio = scs_soj.anio
         and fcs_fact_soj.numero = scs_soj.numero
         and scs_soj.idturno = scs_turno.idturno
         and scs_soj.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idturno = scs_turno.idturno
         and scs_guardiasturno.idturno = scs_soj.idturno
         and scs_guardiasturno.idguardia = scs_soj.idguardia
         and scs_personajg.idpersona(+) = scs_soj.idpersonajg
         and scs_personajg.idinstitucion(+) = scs_soj.idinstitucion
         and cen_persona.idpersona = scs_soj.idpersona
       order by cen_persona.apellidos1 || cen_persona.apellidos2 ||
                cen_persona.nombre asc,
                fcs_fact_soj.fechaapertura asc;

  Begin

    V_DATOSERROR := 'Comprobamos si el fichero esta abierto.';
    IF UTL_FILE.IS_OPEN(F_SALIDA) THEN
      UTL_FILE.FCLOSE(F_SALIDA);
    END IF;
    /* Borramos el fichero si existiera*/
    BEGIN
      UTL_FILE.fremove(P_PATHFICHERO, P_FICHERO);
    EXCEPTION
      WHEN OTHERS THEN
        NULL;
    END;
    begin
      v_cabeceras := p_cabeceras;

      V_POSICION := instr(v_cabeceras, '#');
      IF (V_POSICION = 0) THEN

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error en cabecera.' || sqlerrm;
        RAISE E_ERROR;
      END IF;
      v_cabletrado := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabturno   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabguardia := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabexped   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabfecha   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabbenef   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabimporte := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabtotal   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);

      IF (p_idpago IS null or p_idpago = 0) then
        v_cabtotalacum := v_cabeceras;
      ELSE
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabtotalacum       := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagReal  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFReal := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagAct   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFAct  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabTotalIRPFReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoAct    := substr(v_cabeceras, 1, v_posicion - 1);
        --v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        --v_cabTotalIRPFAct    := v_cabeceras;
      END IF;

    exception
      when others then

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error en cabecera.' || sqlerrm;
        RAISE E_ERROR;
    end;
    V_DATOSERROR := 'Abrimos fichero';

    begin
      V_REGISTRO := v_cabletrado || ESPACIO || v_cabturno || ESPACIO ||
                    v_cabguardia || ESPACIO || v_cabexped || ESPACIO ||
                    v_cabfecha || ESPACIO || v_cabbenef || ESPACIO ||
                    v_cabimporte || ESPACIO;

      if (p_idpago IS NOT null) then
        V_REGISTRO := V_REGISTRO || v_cabImportePagReal || ESPACIO ||
                     --v_cabImporteIRPFReal||ESPACIO||
                      v_cabImportePagAct || ESPACIO ||
                     --v_cabImporteIRPFAct||ESPACIO||
                      v_cabtotalacum || ESPACIO || v_cabTotalPagoReal ||
                      ESPACIO ||
                     --v_cabTotalIRPFReal||ESPACIO||
                      v_cabTotalPagoAct; --||ESPACIO||
        --v_cabTotalIRPFAct||ESPACIO;
      else
        V_REGISTRO := V_REGISTRO || v_cabtotalacum || ESPACIO;
      end if;
    exception
      when others then

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error al escribir la cabecera.' || sqlerrm;
        raise E_ERROR;
    end;
    V_DATOSERROR := 'Escribimos cabeceras';

    if (P_IDPAGO is null) then
      -- exportamos la facturacion de SOJ
      FOR V_FACTSOJ IN C_FACTSOJ LOOP
        IF (CONT = 0) THEN
          /* Antes de escribir el primer registro, escribimos la cabecera*/
          F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
        END IF;
        V_DATOSERROR := 'Escribimos el cuerpo del fichero';
        CONT         := CONT + 1;
        V_PERSONANEW := V_FACTSOJ.V_PERSONA;
        IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
          V_REGISTRO   := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || F_NUM2CHAR(v_precio);
          V_DATOSERROR := 'Se produce un cambio de persona';
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          v_precio := 0;
        end if;
        v_precio := v_precio + V_FACTSOJ.Precioaplicado;

        V_REGISTRO := V_FACTSOJ.V_PERSONA || ESPACIO || V_FACTSOJ.V_TURNO ||
                      ESPACIO || V_FACTSOJ.GUARDIA || ESPACIO ||
                      replace(V_FACTSOJ.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                      V_FACTSOJ.FECHAAPERTURA || ESPACIO ||
                      replace(V_FACTSOJ.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                      F_NUM2CHAR(V_FACTSOJ.Precioaplicado) || ESPACIO;

        /* Grabar registro en el fichero */

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);

        V_PERSONAOLD := V_FACTSOJ.V_PERSONA;
      END LOOP;
    else
      if (P_IDPERSONA is not null) then
        -- exportamos la los Pagos por Persona de SOJ
        FOR V_PAGOPERSONASOJ IN C_PAGOPERSONASOJ LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          V_DATOSERROR := 'Escribimos el cuerpo del fichero';
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOPERSONASOJ.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            V_DATOSERROR := 'Se produce un cambio de persona';
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOPERSONASOJ.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOPERSONASOJ.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOPERSONASOJ.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOPERSONASOJ.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOPERSONASOJ.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF

          V_REGISTRO := V_PAGOPERSONASOJ.V_PERSONA || ESPACIO ||
                        V_PAGOPERSONASOJ.V_TURNO || ESPACIO ||
                        V_PAGOPERSONASOJ.GUARDIA || ESPACIO ||
                        replace(V_PAGOPERSONASOJ.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                        V_PAGOPERSONASOJ.FECHAAPERTURA || ESPACIO ||
                        replace(V_PAGOPERSONASOJ.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                        F_NUM2CHAR(V_PAGOPERSONASOJ.Precioaplicado) ||
                        ESPACIO ||
                        F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTEACTUALPAGADO) ||
                        ESPACIO ||
                       --F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTEACTUALIRPF)||ESPACIO||
                        F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);

          V_PERSONAOLD := V_PAGOPERSONASOJ.V_PERSONA;
        END LOOP;
      else
        -- exportamos los Pagos de SOJ
        FOR V_PAGOSOJ IN C_PAGOSOJ LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          V_DATOSERROR := 'Escribimos el cuerpo del fichero';
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOSOJ.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            V_DATOSERROR := 'Se produce un cambio de persona';
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOSOJ.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOSOJ.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOSOJ.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOSOJ.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOSOJ.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF

          V_REGISTRO := V_PAGOSOJ.V_PERSONA || ESPACIO || V_PAGOSOJ.V_TURNO ||
                        ESPACIO || V_PAGOSOJ.GUARDIA || ESPACIO ||
                        replace(V_PAGOSOJ.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                        V_PAGOSOJ.FECHAAPERTURA || ESPACIO ||
                        replace(V_PAGOSOJ.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                        F_NUM2CHAR(V_PAGOSOJ.Precioaplicado) || ESPACIO ||
                        F_NUM2CHAR(V_PAGOSOJ.IMPORTEACTUALPAGADO) ||
                        ESPACIO ||
                       --F_NUM2CHAR(V_PAGOSOJ.IMPORTEACTUALIRPF)||ESPACIO||
                        F_NUM2CHAR(V_PAGOSOJ.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOSOJ.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);

          V_PERSONAOLD := V_PAGOSOJ.V_PERSONA;
        END LOOP;
      end if;
    end if;
    if (cont > 1) then
      V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO || ESPACIO ||
                    ESPACIO || ESPACIO || ESPACIO || ESPACIO || ESPACIO;
      if (p_idpago IS NOT null) then
        V_REGISTRO := V_REGISTRO || ESPACIO || ESPACIO ||
                     --ESPACIO||
                     --ESPACIO||
                      F_NUM2CHAR(v_precio) || ESPACIO ||
                      F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                     --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                      F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
        --F_NUM2CHAR(V_TOTALIRPFACTUAL);
      else
        V_REGISTRO := V_REGISTRO || F_NUM2CHAR(v_precio);
      end if;
      UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
      UTL_FILE.FFLUSH(F_SALIDA);
    end if;
    UTL_FILE.fclose(F_SALIDA);
    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := 'Fin correcto';

  EXCEPTION
    WHEN E_ERROR THEN
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := V_CODRETORNO;
      P_DATOSERROR := V_DATOSERROR;
    WHEN UTL_FILE.WRITE_ERROR THEN
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5398);
      P_DATOSERROR := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    WHEN UTL_FILE.INVALID_FILEHANDLE THEN
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5396);
      P_DATOSERROR := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    WHEN UTL_FILE.INVALID_PATH THEN
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5394);
      P_DATOSERROR := 'LA RUTA NO ES CORRECTA';
    WHEN UTL_FILE.INVALID_MODE THEN
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5395);
      P_DATOSERROR := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    WHEN UTL_FILE.INVALID_OPERATION THEN
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := sqlcode || ' ' || TO_CHAR(5397);
      P_DATOSERROR := sqlerrm || ' ' || 'OPERACION ERRONEA EN EL FICHERO';

    WHEN UTL_FILE.INTERNAL_ERROR THEN
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      /* Tratamiento de las matrices */
      P_CODRETORNO := TO_CHAR(5401);
      P_DATOSERROR := 'ERROR INTERNO NO ESPECIFICADO';

  END; --PROC_FCS_EXPORTAR_SOJ

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_EJG                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_EJG(P_IDINSTITUCION IN NUMBER,
                                  P_IDFACTURACION IN NUMBER,
                                  P_IDPAGO        IN NUMBER,
                                  P_IDPERSONA     IN NUMBER,
                                  P_PATHFICHERO   IN VARCHAR2,
                                  P_FICHERO       IN VARCHAR2,
                                  P_CABECERAS     IN VARCHAR2,
                                  P_CODRETORNO    OUT VARCHAR2,
                                  P_DATOSERROR    OUT VARCHAR2) IS

    -- VARIABLES

    V_PERSONA VARCHAR2(500) := '';
    V_TURNO   VARCHAR2(500) := '';

    F_SALIDA   UTL_FILE.FILE_TYPE;
    V_REGISTRO VARCHAR2(4000);
    ESPACIO CONSTANT VARCHAR2(6) := CHR(9);
    v_precio             number(10, 2) := 0;
    V_PERSONAOLD         varchar2(4000);
    V_PERSONANEW         varchar2(4000);
    CONT                 NUMBER := 0;
    V_POSICION           NUMBER := 0;
    v_cabletrado         varchar2(4000);
    v_cabturno           varchar2(4000);
    v_cabexped           varchar2(4000);
    v_cabfecha           varchar2(4000);
    v_cabbenef           varchar2(4000);
    v_cabimporte         varchar2(4000);
    v_cabtotal           varchar2(4000);
    v_cabeceras          varchar2(4000);
    v_cabtotalacum       varchar2(4000);
    v_cabguardia         varchar2(4000);
    v_cabCAJG            varchar2(4000);
    v_cabFechaCAJG       varchar2(4000);
    v_cabImportePagReal  varchar2(4000);
    --v_cabImporteIRPFReal varchar2(4000);
    v_cabImportePagAct   varchar2(4000);
    --v_cabImporteIRPFAct  varchar2(4000);
    v_cabTotalPagoReal   varchar2(4000);
    --v_cabTotalIRPFReal   varchar2(4000);
    v_cabTotalPagoAct    varchar2(4000);
    --v_cabTotalIRPFAct    varchar2(4000);
    V_TOTALPAGOSREAL     NUMBER := 0; -- acumulado de v_cabTotalPagoReal
    V_TOTALIRPFREAL      NUMBER := 0; -- acumulado de v_cabTotalIRPFReal
    V_TOTALPAGOACTUAL    NUMBER := 0; -- acumulado de v_cabTotalPagoAct
    V_TOTALIRPFACTUAL    NUMBER := 0; -- acumulado de v_cabTotalIRPFAct

    CURSOR C_FACTEJG IS
      select fcs_fact_ejg.idpersona,
             fcs_fact_ejg.idinstitucion,
             ' ' || fcs_fact_ejg.anio || '/' || scs_ejg.numejg nexpediente,
             to_char(fcs_fact_ejg.fechaapertura, 'dd/mm/yyyy') FECHAAPERTURA,
             fcs_fact_ejg.idturno,
             scs_personajg.nombre || ' ' || scs_personajg.apellido1 || ' ' ||
             scs_personajg.apellido2 beneficiario,
             fcs_fact_ejg.Precioaplicado,
             cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 || ',' ||
             cen_persona.nombre V_PERSONA,
             scs_turno.abreviatura V_TURNO,
             scs_guardiasturno.nombre guardia,
             scs_ejg.numero_cajg,
             scs_ejg.aniocajg

        from fcs_fact_ejg,
             scs_ejg,
             scs_guardiasturno,
             scs_turno,
             scs_personajg,
             cen_persona

       where fcs_fact_ejg.idinstitucion = P_IDINSTITUCION
         and fcs_fact_ejg.idfacturacion = P_IDFACTURACION
         and fcs_fact_ejg.idinstitucion = scs_ejg.idinstitucion
         and fcs_fact_ejg.idtipoejg = scs_ejg.idtipoejg
         and fcs_fact_ejg.anio = scs_ejg.anio
         and fcs_fact_ejg.numero = scs_ejg.numero
         and scs_ejg.guardiaturno_idturno = scs_turno.idturno
         and scs_ejg.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idturno = scs_turno.idturno
         and scs_guardiasturno.idturno = scs_ejg.guardiaturno_idturno
         and scs_guardiasturno.idguardia = scs_ejg.guardiaturno_idguardia
         and scs_personajg.idpersona(+) = scs_ejg.idpersonajg
         and scs_personajg.idinstitucion(+) = scs_ejg.idinstitucion
         and cen_persona.idpersona = scs_ejg.idpersona
       order by cen_persona.apellidos1 || cen_persona.apellidos2 ||
                cen_persona.nombre asc,
                fcs_fact_ejg.fechaapertura asc;

    CURSOR C_PAGOEJG IS
      select fcs_fact_ejg.idpersona,
             fcs_fact_ejg.idinstitucion,
             ' ' || fcs_fact_ejg.anio || '/' || scs_ejg.numejg nexpediente,
             to_char(fcs_fact_ejg.fechaapertura, 'dd/mm/yyyy') FECHAAPERTURA,
             fcs_fact_ejg.idturno,
             scs_personajg.nombre || ' ' || scs_personajg.apellido1 || ' ' ||
             scs_personajg.apellido2 beneficiario,
             fcs_fact_ejg.Precioaplicado,
             cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 || ',' ||
             cen_persona.nombre V_PERSONA,
             scs_turno.abreviatura V_TURNO,
             scs_guardiasturno.nombre guardia,
             scs_ejg.numero_cajg,
             scs_ejg.aniocajg,
             nvl((select NVL(col.impejg, 0) importeActualPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_ejg.idinstitucion
                    and jg.idfacturacion = fcs_fact_ejg.idfacturacion
                    and col.idperorigen = fcs_fact_ejg.idpersona),
                 0) importeActualPagado,

              0 importeActualIRPF,
            nvl((select NVL(col.impejg, 0) importeTotalPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_ejg.idinstitucion
                    and jg.idfacturacion = fcs_fact_ejg.idfacturacion
                    and col.idperorigen = fcs_fact_ejg.idpersona),
                 0) importeTotalPagado,
              0 importeTotalIRPF

        from fcs_fact_ejg,
             scs_ejg,
             scs_guardiasturno,
             scs_turno,
             scs_personajg,
             cen_persona

       where fcs_fact_ejg.idinstitucion = P_IDINSTITUCION
         and fcs_fact_ejg.idfacturacion = P_IDFACTURACION
         and fcs_fact_ejg.idinstitucion = scs_ejg.idinstitucion
         and fcs_fact_ejg.idfacturacion = scs_ejg.idfacturacion
         and fcs_fact_ejg.idtipoejg = scs_ejg.idtipoejg
         and fcs_fact_ejg.anio = scs_ejg.anio
         and fcs_fact_ejg.numero = scs_ejg.numero
         and scs_ejg.guardiaturno_idturno = scs_turno.idturno
         and scs_ejg.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idturno = scs_turno.idturno
         and scs_guardiasturno.idturno = scs_ejg.guardiaturno_idturno
         and scs_guardiasturno.idguardia = scs_ejg.guardiaturno_idguardia
         and scs_personajg.idpersona(+) = scs_ejg.idpersonajg
         and scs_personajg.idinstitucion(+) = scs_ejg.idinstitucion
         and cen_persona.idpersona = scs_ejg.idpersona
       order by cen_persona.apellidos1 || cen_persona.apellidos2 ||
                cen_persona.nombre asc,
                fcs_fact_ejg.fechaapertura asc;

    CURSOR C_PAGOPERSONAEJG IS
      select fcs_fact_ejg.idpersona,
             fcs_fact_ejg.idinstitucion,
             ' ' || fcs_fact_ejg.anio || '/' || scs_ejg.numejg nexpediente,
             to_char(fcs_fact_ejg.fechaapertura, 'dd/mm/yyyy') FECHAAPERTURA,
             fcs_fact_ejg.idturno,
             scs_personajg.nombre || ' ' || scs_personajg.apellido1 || ' ' ||
             scs_personajg.apellido2 beneficiario,
             fcs_fact_ejg.Precioaplicado,
             cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 || ',' ||
             cen_persona.nombre V_PERSONA,
             scs_turno.abreviatura V_TURNO,
             scs_guardiasturno.nombre guardia,
             scs_ejg.numero_cajg,
             scs_ejg.aniocajg,
             nvl((select NVL(col.impejg, 0) importeActualPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_ejg.idinstitucion
                    and jg.idfacturacion = fcs_fact_ejg.idfacturacion
                    and col.idperorigen = fcs_fact_ejg.idpersona),
                 0) importeActualPagado,
              0 importeActualIRPF,
              nvl((select NVL(col.impejg, 0) importeTotalPagado
                   from Fcs_Pago_colegiado col, fcs_pagosjg jg
                  where col.Idinstitucion = P_IDINSTITUCION
                    and col.Idpagosjg = P_IDPAGO
                    and jg.Idfacturacion = P_IDFACTURACION
                    and col.idinstitucion = jg.idinstitucion
                    and col.idpagosjg = jg.idpagosjg
                    and jg.idinstitucion = fcs_fact_ejg.idinstitucion
                    and jg.idfacturacion = fcs_fact_ejg.idfacturacion
                    and col.idperorigen = fcs_fact_ejg.idpersona),
                 0) importeTotalPagado,
              0 importeTotalIRPF

        from fcs_fact_ejg,
             scs_ejg,
             scs_guardiasturno,
             scs_turno,
             scs_personajg,
             cen_persona

       where fcs_fact_ejg.idinstitucion = P_IDINSTITUCION
         and fcs_fact_ejg.idfacturacion = P_IDFACTURACION
         and fcs_fact_ejg.idpersona = P_IDPERSONA
         and fcs_fact_ejg.idinstitucion = scs_ejg.idinstitucion
         and fcs_fact_ejg.idfacturacion = scs_ejg.idfacturacion
         and fcs_fact_ejg.idtipoejg = scs_ejg.idtipoejg
         and fcs_fact_ejg.anio = scs_ejg.anio
         and fcs_fact_ejg.numero = scs_ejg.numero
         and scs_ejg.guardiaturno_idturno = scs_turno.idturno
         and scs_ejg.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idinstitucion = scs_turno.idinstitucion
         and scs_guardiasturno.idturno = scs_turno.idturno
         and scs_guardiasturno.idturno = scs_ejg.guardiaturno_idturno
         and scs_guardiasturno.idguardia = scs_ejg.guardiaturno_idguardia
         and scs_personajg.idpersona(+) = scs_ejg.idpersonajg
         and scs_personajg.idinstitucion(+) = scs_ejg.idinstitucion
         and cen_persona.idpersona = scs_ejg.idpersona
       order by cen_persona.apellidos1 || cen_persona.apellidos2 ||
                cen_persona.nombre asc,
                fcs_fact_ejg.fechaapertura asc;

  Begin

    /* Borramos el fichero si existiera*/
    IF UTL_FILE.IS_OPEN(F_SALIDA) THEN
      UTL_FILE.FCLOSE(F_SALIDA);
    END IF;
    BEGIN
      UTL_FILE.fremove(P_PATHFICHERO, P_FICHERO);
    EXCEPTION
      WHEN OTHERS THEN
        NULL;
    END;

    BEGIN
      v_cabeceras := p_cabeceras;
      V_POSICION  := instr(v_cabeceras, '#');
      IF (V_POSICION = 0) THEN

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error en cabecera.';
        RAISE E_ERROR;
      END IF;

      v_cabletrado   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabturno     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabguardia   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabexped     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabfecha     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabCAJG      := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabFechaCAJG := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabbenef     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabimporte   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabtotal     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      IF (p_idpago IS null or p_idpago = 0) then
        v_cabtotalacum := v_cabeceras;
      ELSE
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabtotalacum       := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagReal  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFReal := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagAct   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFAct  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabTotalIRPFReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoAct    := substr(v_cabeceras, 1, v_posicion - 1);
        --v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        --v_cabTotalIRPFAct    := v_cabeceras;
      END IF;
    EXCEPTION
      WHEN OTHERS THEN

        V_CODRETORNO := SQLCODE;
        V_DATOSERROR := 'Error en la cabecera ' || sqlerrm;
        raise E_ERROR;
    end;

    V_REGISTRO := v_cabletrado || ESPACIO || v_cabturno || ESPACIO ||
                  v_cabguardia || ESPACIO || v_cabexped || ESPACIO ||
                  v_cabfecha || ESPACIO || v_cabCAJG || ESPACIO ||
                  v_cabFechaCAJG || ESPACIO || v_cabbenef || ESPACIO ||
                  v_cabimporte || ESPACIO;

    if (p_idpago IS NOT null) then
      V_REGISTRO := V_REGISTRO || v_cabImportePagReal || ESPACIO ||
                   --v_cabImporteIRPFReal||ESPACIO||
                    v_cabImportePagAct || ESPACIO ||
                   --v_cabImporteIRPFAct||ESPACIO||
                    v_cabtotalacum || ESPACIO || v_cabTotalPagoReal ||
                    ESPACIO ||
                   --v_cabTotalIRPFReal||ESPACIO||
                    v_cabTotalPagoAct; --||ESPACIO||
      --v_cabTotalIRPFAct||ESPACIO;
    else
      V_REGISTRO := V_REGISTRO || v_cabtotalacum || ESPACIO;
    end if;

    V_PERSONAOLD := '';
    IF (P_IDPAGO IS NULL OR P_IDPAGO = 0) THEN
      FOR V_FACTEJG IN C_FACTEJG LOOP

        IF (CONT = 0) THEN
          /* Antes de escribir el primer registro, escribimos la cabecer*/
          F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
        END IF;
        CONT         := CONT + 1;
        V_PERSONANEW := V_FACTEJG.V_PERSONA;
        IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
          V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                        ESPACIO || ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                        ESPACIO || ESPACIO || ESPACIO ||
                        F_NUM2CHAR(v_precio);
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          v_precio := 0;
        end if;
        v_precio     := v_precio + V_FACTEJG.Precioaplicado;
        V_DATOSERROR := 'Antes de escribir en el fichero';
        V_REGISTRO   := V_FACTEJG.V_PERSONA || ESPACIO || V_FACTEJG.V_TURNO ||
                        ESPACIO || V_FACTEJG.GUARDIA || ESPACIO ||
                        replace(V_FACTEJG.NEXPEDIENTE,chr(9),' ')  || ESPACIO ||
                        V_FACTEJG.FECHAAPERTURA || ESPACIO ||
                        replace(V_FACTEJG.NUMERO_CAJG,chr(9),' ')  || ESPACIO ||
                        V_FACTEJG.ANIOCAJG || ESPACIO ||
                        replace(V_FACTEJG.BENEFICIARIO,chr(9),' ')  || ESPACIO ||
                        F_NUM2CHAR(V_FACTEJG.Precioaplicado);

        /* Grabar registro en el fichero */

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);
        V_PERSONAOLD := V_FACTEJG.V_PERSONA;

      END LOOP;
    ELSE
      IF (P_IDPERSONA IS NOT NULL) THEN
        FOR V_PAGOPERSONAEJG IN C_PAGOPERSONAEJG LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOPERSONAEJG.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOPERSONAEJG.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOPERSONAEJG.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOPERSONAEJG.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOPERSONAEJG.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOPERSONAEJG.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF

          V_DATOSERROR := 'Antes de escribir en el fichero';
          V_REGISTRO   := V_PAGOPERSONAEJG.V_PERSONA || ESPACIO ||
                          V_PAGOPERSONAEJG.V_TURNO || ESPACIO ||
                          V_PAGOPERSONAEJG.GUARDIA || ESPACIO ||
                          replace(V_PAGOPERSONAEJG.NEXPEDIENTE,chr(9),' ')  || ESPACIO ||
                          V_PAGOPERSONAEJG.FECHAAPERTURA || ESPACIO ||
                          replace(V_PAGOPERSONAEJG.NUMERO_CAJG,chr(9),' ')  || ESPACIO ||
                          V_PAGOPERSONAEJG.ANIOCAJG || ESPACIO ||
                          replace(V_PAGOPERSONAEJG.BENEFICIARIO,chr(9),' ')  || ESPACIO ||
                          F_NUM2CHAR(V_PAGOPERSONAEJG.Precioaplicado) ||
                          ESPACIO ||
                          F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTEACTUALPAGADO) ||
                          ESPACIO ||
                         --F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTEACTUALIRPF)||ESPACIO||
                          F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          V_PERSONAOLD := V_PAGOPERSONAEJG.V_PERSONA;

        END LOOP;

      ELSE

        FOR V_PAGOEJG IN C_PAGOEJG LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOEJG.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOEJG.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOEJG.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOEJG.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOEJG.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOEJG.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF
          V_DATOSERROR      := 'Antes de escribir en el fichero';
          V_REGISTRO        := V_PAGOEJG.V_PERSONA || ESPACIO ||
                               V_PAGOEJG.V_TURNO || ESPACIO ||
                               V_PAGOEJG.GUARDIA || ESPACIO ||
                               replace(V_PAGOEJG.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                               V_PAGOEJG.FECHAAPERTURA || ESPACIO ||
                               replace(V_PAGOEJG.NUMERO_CAJG,chr(9),' ') || ESPACIO ||
                               V_PAGOEJG.ANIOCAJG || ESPACIO ||
                               replace(V_PAGOEJG.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                               F_NUM2CHAR(V_PAGOEJG.Precioaplicado) ||
                               ESPACIO ||
                               F_NUM2CHAR(V_PAGOEJG.IMPORTEACTUALPAGADO) ||
                               ESPACIO ||
                              --F_NUM2CHAR(V_PAGOEJG.IMPORTEACTUALIRPF)||ESPACIO||
                               F_NUM2CHAR(V_PAGOEJG.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOEJG.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          V_PERSONAOLD := V_PAGOEJG.V_PERSONA;

        END LOOP;

      END IF;
    END IF;
    if (cont > 0) then
      V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO || ESPACIO ||
                    ESPACIO || ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                    ESPACIO || ESPACIO;
      if (p_idpago IS NOT null) then
        V_REGISTRO := V_REGISTRO || ESPACIO || ESPACIO ||
                     --ESPACIO||
                     --ESPACIO||
                      F_NUM2CHAR(v_precio) || ESPACIO ||
                      F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                     --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                      F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
        --F_NUM2CHAR(V_TOTALIRPFACTUAL);
      else
        V_REGISTRO := V_REGISTRO || F_NUM2CHAR(v_precio);
      end if;
      UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
      UTL_FILE.FFLUSH(F_SALIDA);
    end if;
    UTL_FILE.fclose(F_SALIDA);
    P_CODRETORNO := TO_CHAR(0);
    P_DATOSERROR := 'Fin correcto';
  EXCEPTION
    when E_ERROR then
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(V_CODRETORNO);
      P_DATOSERROR := V_DATOSERROR;
    WHEN UTL_FILE.WRITE_ERROR THEN
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5398);
      P_DATOSERROR := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    WHEN UTL_FILE.INVALID_FILEHANDLE THEN
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5396);
      P_DATOSERROR := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    WHEN UTL_FILE.INVALID_PATH THEN
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5394);
      P_DATOSERROR := 'LA RUTA NO ES CORRECTA';
    WHEN UTL_FILE.INVALID_MODE THEN
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5395);
      P_DATOSERROR := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    WHEN UTL_FILE.INVALID_OPERATION THEN
      -- OPERACION ERRONEA EN EL FICHERO

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);

      P_CODRETORNO := sqlcode || ' ' || TO_CHAR(5397);
      P_DATOSERROR := sqlerrm || ' ' || 'OPERACION ERRONEA EN EL FICHERO';

    WHEN UTL_FILE.INTERNAL_ERROR THEN
      -- ERROR INTERNO NO ESPECIFICADO

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);

      /* Tratamiento de las matrices */
      P_CODRETORNO := TO_CHAR(5401);
      P_DATOSERROR := 'ERROR INTERNO NO ESPECIFICADO';

  END; --PROC_FCS_EXPORTAR_EJG

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_TURNOS_OFI                                                                      */
  /* Descripcion: Exporta los datos facturados, pagos o pagos por persona de actuaciones de designaciones
  /*              segun esten rellenos el P_IDFACTURACION, P_IDPAGO, P_IDPERSONA                                */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO            IN        Nombre del fichero                                             VARCHAR2,
  /* P_CABECERAS            IN      cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/
  Procedure PROC_FCS_EXPORTAR_TURNOS_OFI(p_Idinstitucion  In Number,
                                         p_Idfacturacion1 In Number,
                                         p_Idfacturacion2 In Number,
                                         p_Idpersona      In Number,
                                         p_Pathfichero    In Varchar2,
                                         p_Fichero        In Varchar2,
                                         p_Idioma         In Number,
                                         p_Codretorno     Out Varchar2,
                                         p_Datoserror     Out Varchar2) Is
    Escatalan Number(3);
  Begin
    Select Count(*)
      Into Escatalan
      From Cen_Institucion
     Where Idinstitucion = p_Idinstitucion
       And Cen_Inst_Idinstitucion = 3001;

    If Escatalan = '1' Then
      Proc_Exportar_Turnos_3001(p_Idinstitucion,
                                p_Idfacturacion1,
                                p_Idfacturacion2,
                                p_Idpersona,
                                p_Pathfichero,
                                p_Fichero,
                                p_Idioma,
                                p_Codretorno,
                                p_Datoserror);
    Else
      Proc_Exportar_Turnos_Otro(p_Idinstitucion,
                                p_Idfacturacion1,
                                p_Idpersona,
                                p_Pathfichero,
                                p_Fichero,
                                p_Idioma,
                                p_Codretorno,
                                p_Datoserror);
    End If;
  End PROC_FCS_EXPORTAR_TURNOS_OFI;

  PROCEDURE PROC_EXPORTAR_TURNOS_OTRO(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_IDPERSONA     IN NUMBER,
                                      P_PATHFICHERO   IN VARCHAR2,
                                      P_FICHERO       IN VARCHAR2,
                                      P_IDIOMA        IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2) IS

    --Variables generales
    F_SALIDA   UTL_FILE.FILE_TYPE;
    V_REGISTRO VARCHAR2(4000);
    TABULADOR CONSTANT VARCHAR2(6) := CHR(9);
    V_PERSONAOLD         varchar2(4000);
    V_PERSONANEW         varchar2(4000);
    CONT                 NUMBER := 0;

    --Variables de acumulados
    v_TOTAL              NUMBER := 0;

    /* Declaracion del cursor para la facturacion de turnos de oficio*/
    CURSOR C_TURNOSOFI (P_IDINSTITUCION FCS_FACT_ACTUACIONDESIGNA.IDINSTITUCION%type,
                        P_IDFACTURACION FCS_FACT_ACTUACIONDESIGNA.IDFACTURACION%type,
                        P_IDPERSONA FCS_FACT_ACTUACIONASISTENCIA.IDPERSONA%type) IS
      Select Fact.Idinstitucion As Id_Idinstitucion,
             Fact.Anio As Id_Anio,
             Fact.Idturno As Id_Idturno,
             Fact.Numero As Id_Numero,
             Fact.Numeroasunto As Id_Numeroasunto,
             Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) As Numcolletrado,
             Per.Apellidos1 ||
             Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
             Per.Nombre As Letrado,
             Tur.Abreviatura As Turno,
             To_Char(Des.Fechaentrada, 'dd/mm/yyyy') As Fechadesigna,
             To_Char(Act.Fecha, 'dd/mm/yyyy') As Fechaactuacion,
             To_Char(Act.Fechajustificacion, 'dd/mm/yyyy') As Fechajustificacion,
             Des.Anio || '/' || Lpad(Des.Codigo, 5, '0') As Numdesigna,
             Act.Numeroasunto As Numactuacion,
             (Select Pro.Codigo
                From Scs_Procedimientos Pro
               Where Pro.Idinstitucion = Act.Idinstitucion
                 And Pro.Idprocedimiento = Act.Idprocedimiento) As Codprocedim,
             Fact.Procedimiento As Procedimientoactuacion,
             (Select f_Siga_Getrecurso(Pre.Descripcion, p_Idioma)
                From Scs_Pretension Pre
               Where Pre.Idinstitucion = Act.Idinstitucion
                 And Pre.Idpretension = Act.Idpretension) As Pretensionactuacion,
             Fact.Precioaplicado As Precioprocedim,
             Fact.Porcentajefacturado || '% - ' || Fact.Acreditacion As Acreditacion,
             Round(Fact.Precioaplicado * Fact.Porcentajefacturado / 100, 2) As Importe,
             Nvl(Des.Numprocedimiento, Des.Resumenasunto) As Numprocedimoasunto,
             f_Siga_Nif_Litigante(Fact.Idturno,
                                  Fact.Idinstitucion,
                                  Fact.Anio,
                                  Fact.Numero) As Niflitigante,
             f_Siga_Litigante(Fact.Idturno,
                              Fact.Idinstitucion,
                              Fact.Anio,
                              Fact.Numero) As Litigante,
             (Select Juz.Nombre
                From Scs_Juzgado Juz
               Where Juz.Idinstitucion = Act.Idinstitucion
                 And Juz.Idjuzgado = Act.Idjuzgado) As Juzgado,
             (Select Pob.Nombre
                From Scs_Juzgado Juz, Cen_Poblaciones Pob
               Where Juz.Idinstitucion = Act.Idinstitucion
                 And Juz.Idjuzgado = Act.Idjuzgado
                 And Juz.Idpoblacion = Pob.Idpoblacion) As Poblacion,
             (Select Tur.Nombre
                From Scs_Asistencia Asi, Scs_Turno Tur
               Where Asi.Idinstitucion = Tur.Idinstitucion
                 And Asi.Idturno = Tur.Idturno
                 And Asi.Idinstitucion = Fact.Idinstitucion
                 And Asi.Designa_Turno = Fact.Idturno
                 And Asi.Designa_Anio = Fact.Anio
                 And Asi.Designa_Numero = Fact.Numero
                 And Rownum = 1) As Turnoasistencia,
             (Select Gua.Nombre
                From Scs_Asistencia Asi, Scs_Guardiasturno Gua
               Where Asi.Idinstitucion = Gua.Idinstitucion
                 And Asi.Idturno = Gua.Idturno
                 And Asi.Idguardia = Gua.Idguardia
                 And Asi.Idinstitucion = Fact.Idinstitucion
                 And Asi.Designa_Turno = Fact.Idturno
                 And Asi.Designa_Anio = Fact.Anio
                 And Asi.Designa_Numero = Fact.Numero
                 And Rownum = 1) As Guardiaasistencia,
             (Select Asi.Anio || '/' || Lpad(Asi.Numero, 5, '0')
                From Scs_Asistencia Asi
               Where Asi.Idinstitucion = Fact.Idinstitucion
                 And Asi.Designa_Turno = Fact.Idturno
                 And Asi.Designa_Anio = Fact.Anio
                 And Asi.Designa_Numero = Fact.Numero
                 And Rownum = 1) As Numasistencia,
             (Select Ejg.Anio || '/' || Lpad(Ejg.Numejg, 5, '0')
                From Scs_Ejg Ejg, Scs_Ejgdesigna Ejgdes
               Where Ejg.Idinstitucion = Ejgdes.Idinstitucion
                 And Ejg.Idtipoejg = Ejgdes.Idtipoejg
                 And Ejg.Anio = Ejgdes.Anioejg
                 And Ejg.Numero = Ejgdes.Numeroejg
                 And Ejgdes.Idinstitucion = Fact.Idinstitucion
                 And Ejgdes.Idturno = Fact.Idturno
                 And Ejgdes.Aniodesigna = Fact.Anio
                 And Ejgdes.Numerodesigna = Fact.Numero
                 And Rownum = 1) As Nejg,
             (Select Case
                       When (Ejg.Aniocajg Is Not Null And
                            Ejg.Numero_Cajg Is Not Null) Then
                        Ejg.Aniocajg || '/' || Lpad(Ejg.Numero_Cajg, 5, '0')
                       When (Ejg.Aniocajg Is Null And
                            Ejg.Numero_Cajg Is Not Null) Then
                        f_Siga_Getrecurso('fcs.ficheroFacturacion.cabecera.sinAnyo',
                                          p_Idioma) || ' /' ||
                        Lpad(Ejg.Numero_Cajg, 5, '0')
                       When (Ejg.Aniocajg Is Not Null And
                            Ejg.Numero_Cajg Is Null) Then
                        Ejg.Aniocajg || '/ ' ||
                        f_Siga_Getrecurso('fcs.ficheroFacturacion.cabecera.sinNumero',
                                          p_Idioma)
                       Else
                        ''
                     End Case
                From Scs_Ejg Ejg, Scs_Ejgdesigna Ejgdes
               Where Ejg.Idinstitucion = Ejgdes.Idinstitucion
                 And Ejg.Idtipoejg = Ejgdes.Idtipoejg
                 And Ejg.Anio = Ejgdes.Anioejg
                 And Ejg.Numero = Ejgdes.Numeroejg
                 And Ejgdes.Idinstitucion = Fact.Idinstitucion
                 And Ejgdes.Idturno = Fact.Idturno
                 And Ejgdes.Aniodesigna = Fact.Anio
                 And Ejgdes.Numerodesigna = Fact.Numero
                 And Rownum = 1) As Ncajg

        From Fcs_Fact_Actuaciondesigna Fact,
             Scs_Actuaciondesigna      Act,
             Scs_Designa               Des,
             Cen_Colegiado             Col,
             Cen_Persona               Per,
             Scs_Turno                 Tur
       Where Fact.Idinstitucion = Act.Idinstitucion
         And Fact.Idturno = Act.Idturno
         And Fact.Anio = Act.Anio
         And Fact.Numero = Act.Numero
         And Fact.Numeroasunto = Act.Numeroasunto
         And Fact.Idinstitucion = Des.Idinstitucion
         And Fact.Idturno = Des.Idturno
         And Fact.Anio = Des.Anio
         And Fact.Numero = Des.Numero
         And Fact.Idinstitucion = Col.Idinstitucion
         And Fact.Idpersona = Col.Idpersona
         And Fact.Idpersona = Per.Idpersona
         And Fact.Idinstitucion = Tur.Idinstitucion
         And Fact.Idturno = Tur.Idturno

         And Fact.Idinstitucion = p_Idinstitucion
         And Fact.Idfacturacion = p_Idfacturacion
         And Fact.Idpersona = Nvl(p_Idpersona, Fact.Idpersona)

       Order By Per.Apellidos1 ||
                Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
                Per.Nombre Asc,
                Des.Anio || '/' || Lpad(Des.Codigo, 5, '0'),
                Act.Numeroasunto;

  BEGIN

    --Recuperacion de cabeceras
    Select Replace(descripcion, '##', Tabulador) Into v_Registro
      From Gen_Recursos
     Where idrecurso = 'fcs.ficheroFacturacion.cabecera.turnos'
       And idlenguaje = p_idioma;

    --Comprobacion de fichero
    IF UTL_FILE.IS_OPEN(F_SALIDA) THEN
      UTL_FILE.FCLOSE(F_SALIDA);
    END IF;

    V_PERSONAOLD := '';
    FOR V_PAGO IN C_TURNOSOFI(P_IDINSTITUCION, P_IDFACTURACION, P_IDPERSONA) LOOP

      IF (CONT = 0) THEN
         /* Antes de escribir el primer registro, escribimos la cabecera*/
         F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
         UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
         UTL_FILE.FFLUSH(F_SALIDA);
      END IF;

      CONT         := CONT + 1;
      V_PERSONANEW := V_PAGO.LETRADO;
      IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
        BEGIN
          V_REGISTRO :=
                     'TOTAL:' || TABULADOR ||
                     V_PERSONAOLD || TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR || F_NUM2CHAR(V_TOTAL);

        EXCEPTION
          WHEN OTHERS THEN
            V_CODRETORNO := SQLCODE;
            V_DATOSERROR := SQLERRM || ' Error en la cabecera';
            raise E_ERROR;
        END;

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);
        V_TOTAL           := 0;
      end if;

      V_DATOSERROR := 'Antes de escribir el registro en el fichero';
      BEGIN
        V_REGISTRO :=
                   V_PAGO.NUMCOLLETRADO || TABULADOR ||
                   replace(V_PAGO.LETRADO,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.TURNO,chr(9),' ') || TABULADOR ||
                   V_PAGO.FECHADESIGNA || TABULADOR ||
                   V_PAGO.FECHAACTUACION || TABULADOR ||
                   V_PAGO.FECHAJUSTIFICACION || TABULADOR ||
                   V_PAGO.NUMDESIGNA || TABULADOR ||
                   V_PAGO.NUMACTUACION || TABULADOR ||
                   replace(V_PAGO.CODPROCEDIM,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.PROCEDIMIENTOACTUACION,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.PRETENSIONACTUACION,chr(9),' ') || TABULADOR ||
                   f_num2char(V_PAGO.PRECIOPROCEDIM) || TABULADOR ||
                   replace(V_PAGO.ACREDITACION,chr(9),' ') || TABULADOR ||
                   f_num2char(V_PAGO.IMPORTE) || TABULADOR ||
                   TABULADOR || --separador
                   replace(V_PAGO.NUMPROCEDIMOASUNTO,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.NIFLITIGANTE,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.LITIGANTE,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.JUZGADO,chr(9),' ') || TABULADOR ||
                   V_PAGO.POBLACION || TABULADOR ||
                   replace(V_PAGO.TURNOASISTENCIA,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.GUARDIAASISTENCIA,chr(9),' ') || TABULADOR ||
                   V_PAGO.NUMASISTENCIA || TABULADOR ||
                   replace(V_PAGO.NEJG,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.NCAJG,chr(9),' ') || TABULADOR;

      EXCEPTION
        WHEN OTHERS THEN
          V_CODRETORNO := SQLCODE;
          V_DATOSERROR := SQLERRM || ' ' || v_datoserror;
          raise E_ERROR;
      END;

      UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
      UTL_FILE.FFLUSH(F_SALIDA);
      BEGIN
        -- Para cada letrado calculamos el precio total aplicado
        V_DATOSERROR      := 'Antes de calcular el total';
        V_TOTAL           := V_TOTAL +
                             V_PAGO.IMPORTE;
      EXCEPTION
        WHEN OTHERS THEN
          V_CODRETORNO := SQLCODE;
          V_DATOSERROR := SQLERRM || ' Error en la cabecera';
          raise E_ERROR;
      END;

      V_PERSONAOLD := V_PAGO.LETRADO;
    END LOOP;

    begin --escribiendo ultimo registro
      V_DATOSERROR := 'El ultimo registro debe ser el total acumulado para el letrado';
      if (cont > 0) then
        V_REGISTRO :=
                     'TOTAL:' || TABULADOR ||
                     V_PERSONAOLD || TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR || F_NUM2CHAR(V_TOTAL);

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);
      end if;
    exception
      WHEN OTHERS THEN
        V_CODRETORNO := SQLCODE;
        V_DATOSERROR := SQLERRM || ' ' || v_datoserror;
        raise E_ERROR;
    end;

    begin --cerrando fichero
      UTL_FILE.fclose(F_SALIDA);
      P_CODRETORNO := TO_CHAR(0);
      P_DATOSERROR := 'Fin correcto';
    end;

  EXCEPTION
    when e_error then
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := to_char(v_codretorno);
      P_DATOSERROR := v_datoserror;
    WHEN UTL_FILE.WRITE_ERROR THEN
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5398);
      P_DATOSERROR := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    WHEN UTL_FILE.INVALID_FILEHANDLE THEN
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5396);
      P_DATOSERROR := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    WHEN UTL_FILE.INVALID_PATH THEN
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5394);
      P_DATOSERROR := 'LA RUTA NO ES CORRECTA';
    WHEN UTL_FILE.INVALID_MODE THEN
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(5395);
      P_DATOSERROR := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    WHEN UTL_FILE.INVALID_OPERATION THEN
      -- OPERACION ERRONEA EN EL FICHERO

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);

      P_CODRETORNO := sqlcode || ' ' || TO_CHAR(5397);
      P_DATOSERROR := sqlerrm || ' ' || 'OPERACION ERRONEA EN EL FICHERO';

    WHEN UTL_FILE.INTERNAL_ERROR THEN
      -- ERROR INTERNO NO ESPECIFICADO

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);

      /* Tratamiento de las matrices */
      P_CODRETORNO := TO_CHAR(5401);
      P_DATOSERROR := 'ERROR INTERNO NO ESPECIFICADO';
    when others then
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := to_Char(sqlcode);
      P_DATOSERROR := sqlerrm || ' ' ||
                      'ERROR EN LA EXPORTACION DE TURNOS DE OFICIO: ' ||
                      v_datoserror;

  End PROC_EXPORTAR_TURNOS_OTRO;

  Function F_GET_LISTA_DEF_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                        p_Idturno        In Scs_Designa.Idturno%Type,
                                        p_Anio           In Scs_Designa.Anio%Type,
                                        p_Numero         In Scs_Designa.Numero%Type,
                                        p_Tipodevolucion In Number,
                                        p_Idioma         In Number)
    Return Varchar2 Is

    Cursor c_Defendidos Is
      Select Pjg.Nif Nif,
             Nvl(Pjg.Nombre, '') ||
             Decode(Pjg.Apellido1, Null, '', ' ' || Pjg.Apellido1) ||
             Decode(Pjg.Apellido2, Null, '', ' ' || Pjg.Apellido2) Nombre,
             Decode(p_Idioma,
                    2,
                    Decode(Pjg.Sexo, 'H', 'H', 'M', 'D', Null),
                    Pjg.Sexo) Sexo
        From Scs_Defendidosdesigna Defdes, Scs_Personajg Pjg
       Where Defdes.Idinstitucion = Pjg.Idinstitucion
         And Defdes.Idpersona = Pjg.Idpersona
         And Defdes.Idinstitucion = p_Idinstitucion
         And Defdes.Idturno = p_Idturno
         And Defdes.Anio = p_Anio
         And Defdes.Numero = p_Numero
       Order By Pjg.Nif,
                Nvl(Pjg.Nombre, '') ||
                Decode(Pjg.Apellido1, Null, '', ' ' || Pjg.Apellido1) ||
                Decode(Pjg.Apellido2, Null, '', ' ' || Pjg.Apellido2),
                Decode(p_Idioma,
                       2,
                       Decode(Pjg.Sexo, 'H', 'H', 'M', 'D', Null),
                       Pjg.Sexo),
                Pjg.Idpersona;

    Resultado Varchar2(4000);

  Begin
    Resultado := '';

    For r_Defendido In c_Defendidos Loop
      If Resultado Is Not Null Then
        Resultado := Resultado || ', ';
      End If;

      Case p_Tipodevolucion
        When 1 Then
          --NIF
          Resultado := Resultado || r_Defendido.Nif;
        When 2 Then
          --Nombre
          Resultado := Resultado || r_Defendido.Nombre;
        When 3 Then
          --Nombre
          Resultado := Resultado || r_Defendido.Sexo;
        Else
          Resultado := Resultado || r_Defendido.Nif || ' - ' ||
                       r_Defendido.Nombre || ' - ' || r_Defendido.Sexo;
      End Case; End Loop;

    Return Resultado;
  End F_GET_LISTA_DEF_DESIGNA_3001;

  Function F_GET_LISTA_EJGS_DESIGNA_3001(p_Idinstitucion In Scs_Designa.Idinstitucion%Type,
                                         p_Idturno       In Scs_Designa.Idturno%Type,
                                         p_Anio          In Scs_Designa.Anio%Type,
                                         p_Numero        In Scs_Designa.Numero%Type)
    Return Varchar2 Is

    Cursor c_Ejgs Is
      Select Nvl(Tip.Codigoext, ' ') || '-' || Lpad(Ejg.Numejg, 5, '0') || '/' ||
             Substr(To_Char(Ejg.Anio), 3, 2) As Numejg
        From (Select Idinstitucion As Idinstitucion,
                     Ejgidtipoejg  As Idtipoejg,
                     Ejganio       As Anio,
                     Ejgnumero     As Numero
                From Scs_Asistencia Asi
               Where Asi.Idinstitucion = p_Idinstitucion
                 And Asi.Designa_Turno = p_Idturno
                 And Asi.Designa_Anio = p_Anio
                 And Asi.Designa_Numero = p_Numero
              Union
              Select Idinstitucion, Idtipoejg, Anioejg, Numeroejg
                From Scs_Ejgdesigna Ejgdes
               Where Ejgdes.Idinstitucion = p_Idinstitucion
                 And Ejgdes.Idturno = p_Idturno
                 And Ejgdes.Aniodesigna = p_Anio
                 And Ejgdes.Numerodesigna = p_Numero) Relacion,
             Scs_Ejg Ejg,
             Scs_Tipoejgcolegio Tip
       Where Relacion.Idinstitucion = Ejg.Idinstitucion
         And Relacion.Idtipoejg = Ejg.Idtipoejg
         And Relacion.Anio = Ejg.Anio
         And Relacion.Numero = Ejg.Numero
         And Ejg.Idinstitucion = Tip.Idinstitucion(+)
         And Ejg.Idtipoejgcolegio = Tip.Idtipoejgcolegio(+)
       Order By Nvl(Tip.Codigoext, ' ') || '-' || Lpad(Ejg.Numejg, 5, '0') || '/' ||
                Substr(To_Char(Ejg.Anio), 3, 2);

    Resultado Varchar2(4000);

  Begin
    Resultado := '';

    For r_Ejg In c_Ejgs Loop
      If Resultado Is Not Null Then
        Resultado := Resultado || ', ';
      End If;

      Resultado := Resultado || r_Ejg.Numejg;
    End Loop;

    Return Resultado;
  End F_GET_LISTA_EJGS_DESIGNA_3001;

  Function F_GET_LISTA_ASI_DESIGNA_3001(p_Idinstitucion In Scs_Designa.Idinstitucion%Type,
                                        p_Idturno       In Scs_Designa.Idturno%Type,
                                        p_Anio          In Scs_Designa.Anio%Type,
                                        p_Numero        In Scs_Designa.Numero%Type,
                                        p_Prefijo       In Varchar2)
    Return Varchar2 Is

    Cursor c_Asistencias Is
      Select p_Prefijo || Lpad(Asi.Numero, 5, '0') || '/' ||
             Substr(To_Char(Asi.Anio), 3, 2) As Numasi
        From Scs_Asistencia Asi
       Where Asi.Idinstitucion = p_Idinstitucion
         And Asi.Designa_Turno = p_Idturno
         And Asi.Designa_Anio = p_Anio
         And Asi.Designa_Numero = p_Numero
       Order By p_Prefijo || Lpad(Asi.Numero, 5, '0') || '/' ||
                Substr(To_Char(Asi.Anio), 3, 2);

    Resultado Varchar2(4000);

  Begin
    Resultado := '';

    For r_Asistencia In c_Asistencias Loop
      If Resultado Is Not Null Then
        Resultado := Resultado || ', ';
      End If;

      Resultado := Resultado || r_Asistencia.Numasi;
    End Loop;

    Return Resultado;
  End F_GET_LISTA_ASI_DESIGNA_3001;

  Procedure PROC_EXPORTAR_TURNOS_3001(p_Idinstitucion  In Number,
                                      p_Idfacturacion1 In Number,
                                      p_Idfacturacion2 In Number,
                                      p_Idpersona      In Number,
                                      p_Pathfichero    In Varchar2,
                                      p_Fichero        In Varchar2,
                                      p_Idioma         In Number,
                                      p_Codretorno     Out Varchar2,
                                      p_Datoserror     Out Varchar2) Is

    -- VARIABLES
    f_Salida   Utl_File.File_Type; --fichero para escribir
    v_Registro Varchar2(4000); --linea a escribir
    Tabulador Constant Varchar2(6) := Chr(9); --separador de los campos
    Cont Number; --contador de lineas

    Cursor c_Export(v_Idioma Adm_Lenguajes.Idlenguaje%Type, v_Idinstitucion Fcs_Fact_Apunte.Idinstitucion%Type, v_Idfacturacion1 Fcs_Facturacionjg.Idfacturacion%Type, v_Idfacturacion2 Fcs_Facturacionjg.Idfacturacion%Type, v_Idpersona Fcs_Fact_Apunte.Idpersona%Type) Is
      Select To_Number(Nvl(Col.Ncomunitario, Col.Ncolegiado)) As Numcol,
             Per.Apellidos1 ||
             Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
             Per.Nombre As Letrado,
             Tur.Abreviatura As Turno,
             nvl(f_Get_Lista_Asi_Designa_3001(Des.Idinstitucion,
                                              Des.Idturno,
                                              Des.Anio,
                                              Des.Numero,
                                              'A-'),
                 'O-' || Lpad(Des.Codigo, 5, '0') || '/' ||
                 Substr(Des.Anio, 3, 2)) As Expediente,
             f_Get_Lista_Ejgs_Designa_3001(Des.Idinstitucion,
                                           Des.Idturno,
                                           Des.Anio,
                                           Des.Numero) As Ejg,
             Fad.Fechaactuacion,
             f_Get_Lista_Def_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          1,
                                          v_idioma) As Litnif,
             f_Get_Lista_Def_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          2,
                                          v_idioma) As Litnombre,
             f_Get_Lista_Def_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          3,
                                          v_idioma) As Litsexo,
             Nvl(f_Siga_Getrecurso(Pra.Descripcion, 2),
                 f_Siga_Getrecurso(Prd.Descripcion, 2)) As Pretension,
             Pro.Nombre As Modulo,
             Round(Fad.Precioaplicado * Fad.Porcentajefacturado / 100, 2) As Importe,
             To_Char(Fad.Porcentajefacturado) || '%' As Acreditacion,
             Fad.Precioaplicado As Importemodulo,
             f_Get_Lista_Asi_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          Null) As Numasistencia,
             Lpad(Des.Codigo, 5, '0') || '/' || Substr(Des.Anio, 3, 2) As Numdesigna,
             Fad.Numeroasunto As Actuacion,
             Des.Fechaentrada As Fechadesigna,
             Fad.Fechajustificacion,
             Des.Numprocedimiento As Procedimiento,
             Juz.Nombre As Juzgado
        From Fcs_Facturacionjg         Fac,
             Fcs_Fact_Actuaciondesigna Fad,
             Cen_Colegiado             Col,
             Cen_Persona               Per,
             Scs_Turno                 Tur,
             Scs_Actuaciondesigna      Act,
             Scs_Procedimientos        Pro,
             Scs_Pretension            Pra,
             Scs_Juzgado               Juz,
             Scs_Designa               Des,
             Scs_Pretension            Prd
       Where Fac.Idinstitucion = Fad.Idinstitucion
         And Fac.Idfacturacion = Fad.Idfacturacion
         And Fad.Idinstitucion = Col.Idinstitucion
         And Fad.Idpersona = Col.Idpersona
         And Fad.Idpersona = Per.Idpersona
         And Fad.Idinstitucion = Tur.Idinstitucion
         And Fad.Idturno = Tur.Idturno
         And Fad.Idinstitucion = Act.Idinstitucion
         And Fad.Idturno = Act.Idturno
         And Fad.Anio = Act.Anio
         And Fad.Numero = Act.Numero
         And Fad.Numeroasunto = Act.Numeroasunto
         And Act.Idinstitucion = Pro.Idinstitucion
         And Act.Idprocedimiento = Pro.Idprocedimiento
         And Act.Idinstitucion = Pra.Idinstitucion(+)
         And Act.Idpretension = Pra.Idpretension(+)
         And Act.Idinstitucion_Juzg = Juz.Idinstitucion(+)
         And Act.Idjuzgado = Juz.Idjuzgado(+)
         And Fad.Idinstitucion = Des.Idinstitucion
         And Fad.Idturno = Des.Idturno
         And Fad.Anio = Des.Anio
         And Fad.Numero = Des.Numero
         And Des.Idinstitucion = Prd.Idinstitucion(+)
         And Des.Idpretension = Prd.Idpretension(+)

         And Fac.Idinstitucion = v_Idinstitucion

         --Si no hay 2a, se escoge una unica facturacion:
         -- es necesario hacerlo asi para que el Excel de 1 facturacion
         -- no obtenga otras facturaciones de las mismas fechas
         -- pero de otros grupos de facturacion
         And ((v_Idfacturacion2 Is Null And
               Fac.Idfacturacion = v_Idfacturacion1) Or

             (v_Idfacturacion2 Is Not Null And
             Fac.Fechadesde Between
             (Select Fac1.Fechadesde
                From Fcs_Facturacionjg Fac1
               Where Fac1.Idinstitucion = v_Idinstitucion
                 And Fac1.Idfacturacion = v_Idfacturacion1) And
             (Select Fac2.Fechahasta
                From Fcs_Facturacionjg Fac2
               Where Fac2.Idinstitucion = v_Idinstitucion
                 And Fac2.Idfacturacion = v_Idfacturacion2)))

         And Per.Idpersona = nvl(v_Idpersona, Per.Idpersona)

       Order By Numcol;

  Begin

    --Recuperacion de cabeceras
    Select Replace(descripcion, '##', Tabulador) Into v_Registro
      From Gen_Recursos
     Where idrecurso = 'fcs.ficheroFacturacion.cabecera.turnos.3001'
       And idlenguaje = p_idioma;

    --Comprobacion de fichero de salida
    If Utl_File.Is_Open(f_Salida) Then
      Utl_File.Fclose(f_Salida);
    End If;

    Cont := 0;

    For v_Linea In c_Export(p_Idioma,
                            p_Idinstitucion,
                            p_Idfacturacion1,
                            p_Idfacturacion2,
                            p_Idpersona) Loop

      If Cont = 0 Then
        /* Antes de escribir el primer registro, escribimos la cabecera*/
        f_Salida := Utl_File.Fopen(p_Pathfichero, p_Fichero, 'w');
        Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      End If;
      Cont := Cont + 1;

      v_Registro := v_Linea.Numcol || Tabulador ||
                    v_Linea.Letrado || Tabulador ||
                    v_Linea.Turno || Tabulador ||
                    v_Linea.Expediente || Tabulador ||
                    v_Linea.Ejg || Tabulador ||
                    v_Linea.Fechaactuacion || Tabulador ||
                    v_Linea.Litnif || Tabulador ||
                    v_Linea.Litnombre || Tabulador ||
                    v_Linea.Litsexo || Tabulador ||
                    v_Linea.Juzgado || Tabulador ||
                    v_Linea.Pretension || Tabulador ||
                    v_Linea.Modulo || Tabulador ||
                    f_num2char(v_Linea.Importe) || Tabulador ||
                    v_Linea.Acreditacion || Tabulador ||
                    f_num2char(v_Linea.Importemodulo) || Tabulador ||
                    v_Linea.Numasistencia || Tabulador ||
                    v_Linea.Numdesigna || Tabulador ||
                    v_Linea.Actuacion || Tabulador ||
                    v_Linea.Fechadesigna || Tabulador ||
                    v_Linea.Fechajustificacion || Tabulador ||
                    v_Linea.Procedimiento;

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);

    End Loop;

    --cerrando fichero
    Utl_File.Fclose(f_Salida);
    p_Codretorno := To_Char(0);
    p_Datoserror := 'Fin correcto';

  Exception
    When Utl_File.Write_Error Then
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(5398);
      p_Datoserror := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    When Utl_File.Invalid_Filehandle Then
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(5396);
      p_Datoserror := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    When Utl_File.Invalid_Path Then
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      p_Codretorno := To_Char(5394);
      p_Datoserror := 'LA RUTA NO ES CORRECTA';
    When Utl_File.Invalid_Mode Then
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      p_Codretorno := To_Char(5395);
      p_Datoserror := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    When Utl_File.Invalid_Operation Then
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := Sqlcode || ' ' || To_Char(5397);
      p_Datoserror := Sqlerrm || ' ' || 'OPERACION ERRONEA EN EL FICHERO';

    When Utl_File.Internal_Error Then
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      /* Tratamiento de las matrices */
      p_Codretorno := To_Char(5401);
      p_Datoserror := 'ERROR INTERNO NO ESPECIFICADO';
    When Others Then

      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm;
  End PROC_EXPORTAR_TURNOS_3001;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_GUARDIAS                                                                         */
  /* Descripcion: Exporta los datos facturados de guardias                                                        */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 17/05/2006                                                                                   */
  /* Autor: David Sanchez Pina
  /* Fecha Modificacion   Autor Mdoificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/
  Procedure PROC_FCS_EXPORTAR_GUARDIAS(p_Idinstitucion  In Number,
                                       p_Idfacturacion1 In Number,
                                       p_Idfacturacion2 In Number,
                                       p_Idpersona      In Number,
                                       p_Pathfichero    In Varchar2,
                                       p_Fichero        In Varchar2,
                                       p_Idioma         In Number,
                                       p_Codretorno     Out Varchar2,
                                       p_Datoserror     Out Varchar2) Is
    Escatalan Number(3);
  Begin
    Select Count(*)
      Into Escatalan
      From Cen_Institucion
     Where Idinstitucion = p_Idinstitucion
       And Cen_Inst_Idinstitucion = 3001;

    If Escatalan = '1' Then
      Proc_Exportar_Guardias_3001(p_Idinstitucion,
                                  p_Idfacturacion1,
                                  p_Idfacturacion2,
                                  p_Idpersona,
                                  p_Pathfichero,
                                  p_Fichero,
                                  p_Idioma,
                                  p_Codretorno,
                                  p_Datoserror);
    Else
      Proc_Exportar_Guardias_Otro(p_Idinstitucion,
                                  p_Idfacturacion1,
                                  p_Idpersona,
                                  p_Pathfichero,
                                  p_Fichero,
                                  p_Idioma,
                                  p_Codretorno,
                                  p_Datoserror);
    End If;
  End PROC_FCS_EXPORTAR_GUARDIAS;

  Procedure PROC_EXPORTAR_GUARDIAS_OTRO(p_Idinstitucion In Number,
                                        p_Idfacturacion In Number,
                                        p_Idpersona     In Number,
                                        p_Pathfichero   In Varchar2,
                                        p_Fichero       In Varchar2,
                                        p_Idioma        In Number,
                                        p_Codretorno    Out Varchar2,
                                        p_Datoserror    Out Varchar2) Is

    -- VARIABLES
    f_Salida                Utl_File.File_Type;
    v_Nombrepersonaanterior Varchar2(4000) := '';
    v_Nombreturnoanterior   Varchar2(4000) := '';
    v_Nombreguardiaanterior Varchar2(4000) := '';
    v_Registro              Varchar2(4000);
    v_Letradoactual         Fcs_Fact_Apunte.Idpersona%Type;
    v_Turnoactual           Fcs_Fact_Apunte.Idturno%Type;
    v_Guardiaactual         Fcs_Fact_Apunte.Idguardia%Type;
    v_Letradoanterior       Fcs_Fact_Apunte.Idpersona%Type;
    v_Turnoanterior         Fcs_Fact_Apunte.Idturno%Type;
    v_Guardiaanterior       Fcs_Fact_Apunte.Idguardia%Type;
    Totalpersona            Fcs_Fact_Apunte.Precioaplicado%Type;

    Tabulador Constant Varchar2(6) := Chr(9);
    Cont       Number;
    Fechaant   Date;
    Fechaact   Date;
    Tipoapunte Varchar2(100);

    Cursor c_Cg(p_Idioma Adm_Lenguajes.Idlenguaje%Type, p_Idinstitucion Fcs_Fact_Apunte.Idinstitucion%Type, p_Idfacturacion Fcs_Fact_Apunte.Idfacturacion%Type, p_Idpersona Fcs_Fact_Apunte.Idpersona%Type) Is
      Select Fcg.Idturno,
             Fcg.Idguardia,
             Fcg.Fechainicio,
             Fcg.Idinstitucion,

             --Datos de la Guardia
             Tt.Abreviatura Nomturno,
             Gt.Nombre      Nomguardia,
             Cg.Fechainicio Fechainiguardia,
             Cg.Fecha_Fin   Fechafinguardia,

             --Datos del Letrado
             p.Idpersona,
             (p.Apellidos1 || ' ' || p.Apellidos2 || ', ' || p.Nombre) Nompersona,
             Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) Numcol,

             --Datos de Facturacion
             (Select Descripcion
                From Gen_Recursos
               Where Idrecurso =
                     Decode(Fcg.Idtipoapunte,
                            'FGTp',
                            'fcs.ficheroFacturacion.tipoApunte.cabeceraFG',
                            'FG',
                            'fcs.ficheroFacturacion.tipoApunte.cabeceraFG',
                            'fcs.ficheroFacturacion.tipoApunte.cabecera')
                 And Idlenguaje = p_Idioma) Tipoapunte,
             f_Siga_Getrecurso(h.Nombre, p_Idioma) Nombrehito,
             Fcg.Precioaplicado,
             Fcg.Preciocostesfijos,
             (Fcg.Precioaplicado + Fcg.Preciocostesfijos) Total,
             h.Descripcion Deschito,
             Fcg.Idtipoapunte,
             Fcg.Idapunte

      --Tablas usadas
        From Fcs_Fact_Apunte      Fcg,
             Scs_Cabeceraguardias Cg,
             Cen_Persona          p,
             Cen_Colegiado        Col,
             Scs_Hitofacturable   h,
             Scs_Guardiasturno    Gt,
             Scs_Turno            Tt
       Where Fcg.Idpersona = p.Idpersona
         And Fcg.Idpersona = Col.Idpersona
         And Fcg.Idinstitucion = Col.Idinstitucion
         And Fcg.Idinstitucion = Cg.Idinstitucion
         And Fcg.Idturno = Cg.Idturno
         And Fcg.Idguardia = Cg.Idguardia
         And Fcg.Idpersona = Cg.Idpersona
         And Fcg.Fechainicio = Cg.Fechainicio
         And Fcg.Idhito = h.Idhito
         And Fcg.Idinstitucion = Gt.Idinstitucion
         And Fcg.Idturno = Gt.Idturno
         And Fcg.Idguardia = Gt.Idguardia
         And Fcg.Idinstitucion = Tt.Idinstitucion
         And Fcg.Idturno = Tt.Idturno

            --Filtros
         And Fcg.Idfacturacion = p_Idfacturacion
         And Fcg.Idinstitucion = p_Idinstitucion
         And p.Idpersona = Nvl(p_Idpersona, p.Idpersona)

      --Ordenacion
       Order By Fcg.Idinstitucion,
                Fcg.Idturno,
                Fcg.Idguardia,
                Fcg.Idpersona,
                Fcg.Fechainicio,
                Fcg.Idapunte,
                Fcg.Idtipoapunte;

    Cursor c_Ug(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fug.Fechafin Fechaini_Ug,
             Fug.Fechafin Fechafin_Ug,
             (Select '  ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso = 'fcs.ficheroFacturacion.tipoApunte.dia'
                 And Idlenguaje = p_Idioma) Tipoapuntedia,
             (Select '    ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso = 'fcs.ficheroFacturacion.tipoApunte.tipo'
                 And Idlenguaje = p_Idioma) /**/ Tipoapuntetipo,
             h.Nombre Nombrehito,
             Fug.Precioaplicado,
             Fug.Preciocostesfijos,
             (Fug.Precioaplicado + Fug.Preciocostesfijos) Total,
             h.Descripcion Deschito,
             Fug.Idtipo,
             Fug.Idapunte
        From Fcs_Fact_Guardiascolegiado Fug, Scs_Hitofacturable h
       Where Fug.Idhito = h.Idhito
         And Fug.Idfacturacion = p_Idfacturacion
         And Fug.Idinstitucion = p_Idinstitucion
         And Fug.Idapunte = Pp_Idapunte
       Order By Fug.Fechafin, Fug.Idtipo; -- ordenado por fecha y tipo (contador)

    Cursor c_Ugfg(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fug.Fechafin Fechaini_Ug,
             Fug.Fechafin Fechafin_Ug,
             (Select '    ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso = 'fcs.ficheroFacturacion.tipoApunte.tipoFG'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito,
             Fug.Precioaplicado,
             Fug.Preciocostesfijos,
             (Fug.Precioaplicado + Fug.Preciocostesfijos) Total,
             h.Descripcion Deschito,
             Fug.Idtipo,
             Fug.Idapunte
        From Fcs_Fact_Guardiascolegiado Fug, Scs_Hitofacturable h
       Where Fug.Idhito = h.Idhito
         And Fug.Idfacturacion = p_Idfacturacion
         And Fug.Idinstitucion = p_Idinstitucion
         And Fug.Idapunte = Pp_Idapunte
       Order By Fug.Fechafin, Fug.Idtipo; -- ordenado por fecha y tipo (contador)

    Cursor c_Ac(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type, Pp_Idtipo Fcs_Fact_Guardiascolegiado.Idtipo%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacion'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
         And Fac.Idtipo = Pp_Idtipo
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;
    Cursor c_Acfg(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type, Pp_Idtipo Fcs_Fact_Guardiascolegiado.Idtipo%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacionFG'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
         And Fac.Idtipo = Pp_Idtipo
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;

    Cursor c_Ac_Sintipo(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacion'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;
    Cursor c_Acfg_Sintipo(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacionFG'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;

    Cursor c_Ac_Sintipo_Ug(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type, Pp_Fecha Fcs_Fact_Guardiascolegiado.Fechafin%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacion'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
         And Trunc(a.Fechahora) = Trunc(Pp_Fecha)
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;

  Begin

    --Recuperacion de cabeceras
    Select Replace(Descripcion, '##', Tabulador)
      Into v_Registro
      From Gen_Recursos
     Where Idrecurso = 'fcs.ficheroFacturacion.cabecera.guardias'
       And Idlenguaje = p_Idioma;

    Begin
      --preparando variables para bucles de escritura de registros
      Totalpersona     := 0;

      v_Letradoanterior := -1;
      v_Turnoanterior   := -1;
      v_Guardiaanterior := -1;
      v_Letradoactual   := -1;
      v_Turnoactual     := -1;
      v_Guardiaactual   := -1;

      v_Nombrepersonaanterior := Null;
    End;

    --Comprobacion de fichero de salida
    If Utl_File.Is_Open(f_Salida) Then
      Utl_File.Fclose(f_Salida);
    End If;

    Cont := 0;

    For v_Cgpago In c_Cg(p_Idioma,
                         p_Idinstitucion,
                         p_Idfacturacion,
                         p_Idpersona) Loop

      If Cont = 0 Then
        /* Antes de escribir el primer registro, escribimos la cabecera*/
        f_Salida := Utl_File.Fopen(p_Pathfichero, p_Fichero, 'w');
        Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      End If;
      Cont := Cont + 1;

      -- CONTROL DE LETRADO
      v_Letradoanterior := v_Letradoactual;
      v_Turnoanterior   := v_Turnoactual;
      v_Guardiaanterior := v_Guardiaactual;
      v_Letradoactual   := v_Cgpago.Idpersona;
      v_Turnoactual     := v_Cgpago.Idturno;
      v_Guardiaactual   := v_Cgpago.Idguardia;

      If v_Letradoactual <> v_Letradoanterior Or
         v_Turnoactual <> v_Turnoanterior Or
         v_Guardiaactual <> v_Guardiaanterior Then

        -- SI CAMBIA LA PERSONA SE ACTUALIZA
        If v_Nombrepersonaanterior Is Null Then
          --INICIO DE LA PRIMERA PERSONA
          v_Nombrepersonaanterior := v_Cgpago.Nompersona;
          v_Nombreturnoanterior   := v_Cgpago.Nomturno;
          v_Nombreguardiaanterior := v_Cgpago.Nomguardia;
        Else
          -- FIN DE UNA PERSONA
          -- ESCRIBO EL TOTAL EN UNA LINEA PARA EL USUARIO ANTERIOR
          v_Registro := 'TOTAL ' || ' ' || Tabulador || -- Num Col Letrado
                        v_Nombrepersonaanterior || Tabulador || -- nombre persona
                        v_Nombreturnoanterior || Tabulador || -- turno
                        v_Nombreguardiaanterior || Tabulador || -- guardia
                        ' ' || Tabulador || -- tipo apunte
                        ' ' || Tabulador || -- fecha inicio
                        ' ' || Tabulador || -- fecha fin
                        ' ' || Tabulador || -- Fecha Actuacion
                        ' ' || Tabulador || -- fechajustificacion
                        ' ' || Tabulador || -- numero asistencia
                        ' ' || Tabulador || -- numero actuacion
                        ' ' || Tabulador || -- motivo
                        ' ' || Tabulador || -- Num Actuaciones Facturadas
                        ' ' || Tabulador || -- Num Actuac FG Facturadas
                        ' ' || Tabulador || -- Num Asistencias Facturadas
                        ' ' || Tabulador || -- Num Actuaciones Totales
                        ' ' || Tabulador || -- Num Actuac FG Totales
                        ' ' || Tabulador || -- Num Asistencias Totales
                        ' ' || Tabulador || -- importe
                        ' ' || Tabulador || -- precio costes
                        ' ' || Tabulador || -- total

                        ' ' || Tabulador || -- Separador
                        ' ' || Tabulador || -- Num Doc Asistido
                        ' ' || Tabulador || -- asistido
                        ' ' || Tabulador || -- delitos
                        ' ' || Tabulador || -- lugar
                        ' ' || Tabulador || -- localidad
                        f_Num2char(Totalpersona); -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          --INICIO DE PERSONA
          v_Nombrepersonaanterior := v_Cgpago.Nompersona;
          v_Nombreturnoanterior   := v_Cgpago.Nomturno;
          v_Nombreguardiaanterior := v_Cgpago.Nomguardia;
        End If;

        -- INICIALIZO
        Totalpersona     := 0;

      End If;

      Totalpersona     := Totalpersona + v_Cgpago.Total;

      -- ESCRIBO CABECERA DE GUARDIA

      v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                    || Tabulador || v_Cgpago.Nompersona -- nombre persona
                    || Tabulador || Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                    || Tabulador ||
                    Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                    || Tabulador || v_Cgpago.Tipoapunte --tipo apunte
                    || Tabulador || v_Cgpago.Fechainiguardia -- fecha inicio
                    || Tabulador || v_Cgpago.Fechafinguardia -- fecha fin
                    || Tabulador || ' ' -- Fecha Actuacion
                    || Tabulador || ' ' -- Fecha Justificacion
                    || Tabulador || ' ' -- numero asistencia
                    || Tabulador || ' ' -- numero actuacion
                    || Tabulador ||
                    Replace(v_Cgpago.Nombrehito, Chr(9), ' '); -- motivo

      If v_Cgpago.Idtipoapunte = 'FGTp' Or v_Cgpago.Idtipoapunte = 'FG' Then
        v_Registro := v_Registro || Tabulador || '' --V_CGPAGO.N_ACTUACIONESFACTURADAS -- Num Actuaciones Facturadas
                      || Tabulador ||
                      Func_Num_Actuac_Facturac(p_Idinstitucion,
                                               p_Idfacturacion,
                                               Null,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               v_Cgpago.Fechainiguardia,
                                               v_Cgpago.Fechafinguardia,
                                               'S') -- Num Actuac FG Facturadas
                      || Tabulador || '' --V_CGPAGO.N_ASISTENCIASFACTURADAS -- Num Asistencias Facturadas
                      || Tabulador || '' --V_CGPAGO.N_ACTUACIONESTOTAL -- Num Actuaciones Totales
                      || Tabulador ||
                      Func_Num_Actuac_Totales(p_Idinstitucion,
                                              p_Idfacturacion,
                                              Null,
                                              v_Cgpago.Idturno,
                                              v_Cgpago.Idguardia,
                                              v_Cgpago.Idpersona,
                                              v_Cgpago.Fechainiguardia,
                                              v_Cgpago.Fechafinguardia,
                                              'S') -- Num Actuac FG Totales
                      || Tabulador || ''; --V_CGPAGO.N_ASISTENCIASTOTAL -- Num Asistencias Totales
      Else
        v_Registro := v_Registro || Tabulador ||
                      Func_Num_Actuac_Facturac(p_Idinstitucion,
                                               p_Idfacturacion,
                                               Null,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               v_Cgpago.Fechainiguardia,
                                               v_Cgpago.Fechafinguardia,
                                               'N') -- Num Actuaciones Facturadas
                      || Tabulador || '' --V_CGPAGO.N_ACTUACFGFACTURADAS -- Num Actuac FG Facturadas
                      || Tabulador ||
                      Func_Num_Asist_Facturac(p_Idinstitucion,
                                              p_Idfacturacion,
                                              v_Cgpago.Idturno,
                                              v_Cgpago.Idguardia,
                                              v_Cgpago.Idpersona,
                                              v_Cgpago.Fechainiguardia,
                                              v_Cgpago.Fechafinguardia) -- Num Asistencias Facturadas
                      || Tabulador ||
                      Func_Num_Actuac_Totales(p_Idinstitucion,
                                              p_Idfacturacion,
                                              Null,
                                              v_Cgpago.Idturno,
                                              v_Cgpago.Idguardia,
                                              v_Cgpago.Idpersona,
                                              v_Cgpago.Fechainiguardia,
                                              v_Cgpago.Fechafinguardia,
                                              'N') -- Num Actuaciones Totales
                      || Tabulador || '' --V_CGPAGO.N_ACTUACFGTOTAL -- Num Actuac FG Totales
                      || Tabulador ||
                      Func_Num_Asist_Totales(p_Idinstitucion,
                                             p_Idfacturacion,
                                             v_Cgpago.Idturno,
                                             v_Cgpago.Idguardia,
                                             v_Cgpago.Idpersona,
                                             v_Cgpago.Fechainiguardia,
                                             v_Cgpago.Fechafinguardia); -- Num Asistencias Totales
      End If;

      v_Registro := v_Registro || Tabulador ||
                    f_Num2char(v_Cgpago.Precioaplicado) -- importe
                    || Tabulador || f_Num2char(v_Cgpago.Preciocostesfijos) -- precio costes
                    || Tabulador || f_Num2char(v_Cgpago.Total) -- total

                    || Tabulador || ' ' -- Separador
                    || Tabulador || ' ' -- Num Doc Asistido
                    || Tabulador || ' ' -- asistido
                    || Tabulador || ' ' -- delitos
                    || Tabulador || ' ' -- lugar
                    || Tabulador || ' ' -- localidad
                    || Tabulador || ' '; -- total letrado

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);

      If v_Cgpago.Idtipoapunte = 'CG' Then
        -- RECORRIDO CG-ACTUACIONES

        For v_Ug In c_Ug(v_Cgpago.Idapunte) Loop

          -- RGG 17/06/2008 ES UN REGISTRO DE UNIDAD DE GUARDIA GENERAL

          -- ESCRIBO UNIDAD DE GUARDIA
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ug.Tipoapuntedia --tipo apunte
                        || Tabulador || v_Ug.Fechaini_Ug -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || ' ' -- Fecha Actuacion
                        || Tabulador || ' ' -- fechajustificacion
                        || Tabulador || ' ' -- numero asistencia
                        || Tabulador || ' ' -- numero actuacion
                        || Tabulador ||
                        Replace(v_Ug.Nombrehito, Chr(9), ' ') -- motivo
                        || Tabulador ||
                        Func_Num_Actuac_Facturac(p_Idinstitucion,
                                                 p_Idfacturacion,
                                                 Null,
                                                 v_Cgpago.Idturno,
                                                 v_Cgpago.Idguardia,
                                                 v_Cgpago.Idpersona,
                                                 Null,
                                                 v_Ug.Fechafin_Ug,
                                                 'N') -- Num Actuaciones Facturadas
                        || Tabulador || '' -- Num Actuac FG Facturadas
                        || Tabulador ||
                        Func_Num_Asist_Facturac(p_Idinstitucion,
                                                p_Idfacturacion,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug) -- Num Asistencias Facturadas
                        || Tabulador ||
                        Func_Num_Actuac_Totales(p_Idinstitucion,
                                                p_Idfacturacion,
                                                Null,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug,
                                                'N') -- Num Actuaciones Totales
                        || Tabulador || '' --V_UG.N_ACTUACFGTOTAL -- Num Actuac FG Totales
                        || Tabulador ||
                        Func_Num_Asist_Totales(p_Idinstitucion,
                                               p_Idfacturacion,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               Null,
                                               v_Ug.Fechafin_Ug) -- Num Asistencias Totales
                        || Tabulador || f_Num2char(v_Ug.Precioaplicado) -- importe
                        || Tabulador || f_Num2char(v_Ug.Preciocostesfijos) -- precio costes
                        || Tabulador || f_Num2char(v_Ug.Total) -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador || ' ' -- Num Doc Asistido
                        || Tabulador || ' ' -- asistido
                        || Tabulador || ' ' -- delitos
                        || Tabulador || ' ' -- lugar
                        || Tabulador || ' ' -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          For v_Ac In c_Ac_Sintipo_Ug(v_Cgpago.Idapunte, v_Ug.Fechafin_Ug) Loop

            -- ESCRIBO ACTUACION
            v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                          || Tabulador || v_Cgpago.Nompersona -- nombre persona
                          || Tabulador ||
                          Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                          || Tabulador ||
                          Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                          || Tabulador || v_Ac.Tipoapunte --tipo apunte
                          || Tabulador || ' ' -- fecha inicio
                          || Tabulador || ' ' -- fecha fin
                          || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                          || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                          || Tabulador || v_Ac.Numasistencia -- numero asistencia
                          || Tabulador || v_Ac.Numactuacion -- numero actuacion
                          || Tabulador || v_Ac.Nombrehito -- motivo
                          || Tabulador || ' ' -- Num Actuaciones Facturadas
                          || Tabulador || ' ' -- Num Actuac FG Facturadas
                          || Tabulador || ' ' -- Num Asistencias Facturadas
                          || Tabulador || ' ' -- Num Actuaciones Totales
                          || Tabulador || ' ' -- Num Actuac FG Totales
                          || Tabulador || ' ' -- Num Asistencias Totales
                          || Tabulador || ' ' -- importe
                          || Tabulador || ' ' -- precio costes
                          || Tabulador || ' ' -- total

                          || Tabulador || ' ' -- Separador
                          || Tabulador ||
                          Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                          || Tabulador ||
                          Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                          || Tabulador ||
                          Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                          || Tabulador || v_Ac.Lugar -- lugar
                          || Tabulador || v_Ac.Localidad -- localidad
                          || Tabulador || ' '; -- total letrado

            Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);

          End Loop;

        End Loop;

      Elsif v_Cgpago.Idtipoapunte = 'CG-' Then
        -- RECORRIDO CG-ACTUACIONES

        For v_Ac In c_Ac_Sintipo(v_Cgpago.Idapunte) Loop

          -- ESCRIBO ACTUACION
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ac.Tipoapunte --tipo apunte
                        || Tabulador || ' ' -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                        || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                        || Tabulador || v_Ac.Numasistencia -- numero asistencia
                        || Tabulador || v_Ac.Numactuacion -- numero actuacion
                        || Tabulador || v_Ac.Nombrehito -- motivo
                        || Tabulador || ' ' -- Num Actuaciones Facturadas
                        || Tabulador || ' ' -- Num Actuac FG Facturadas
                        || Tabulador || ' ' -- Num Asistencias Facturadas
                        || Tabulador || ' ' -- Num Actuaciones Totales
                        || Tabulador || ' ' -- Num Actuac FG Totales
                        || Tabulador || ' ' -- Num Asistencias Totales
                        || Tabulador || ' ' -- importe
                        || Tabulador || ' ' -- precio costes
                        || Tabulador || ' ' -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador ||
                        Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                        || Tabulador ||
                        Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                        || Tabulador || Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                        || Tabulador || v_Ac.Lugar -- lugar
                        || Tabulador || v_Ac.Localidad -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

        End Loop;

      Elsif v_Cgpago.Idtipoapunte = 'CG+' Then
        -- RECORRIDO CG-UG-ACTUACIONES (CUANDO CAMBIA LA UG) (QUIZA VALE CUANDO CAMBIA EL CONTADOR TAMBIEN)
        Fechaant := To_Date('01/01/1990', 'DD/MM/YYYY');
        Fechaact := Null;

        For v_Ug In c_Ug(v_Cgpago.Idapunte) Loop

          Begin
            --controlando para escribir registros de Tipo
            Fechaact := v_Ug.Fechafin_Ug;

            -- RGG 17/06/2008
            If (Fechaact <> Fechaant) Then
              Tipoapunte := v_Ug.Tipoapuntedia;
            Else
              Tipoapunte := v_Ug.Tipoapuntetipo;
            End If;

            Fechaant := Fechaact;
          End;

          -- ESCRIBO UNIDAD DE GUARDIA
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || Tipoapunte --tipo apunte
                        || Tabulador || v_Ug.Fechaini_Ug -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || ' ' -- Fecha Actuacion
                        || Tabulador || ' ' -- fechajustificacion
                        || Tabulador || ' ' -- numero asistencia
                        || Tabulador || ' ' -- numero actuacion
                        || Tabulador ||
                        Replace(v_Ug.Nombrehito, Chr(9), ' ') -- motivo
                        || Tabulador ||
                        Func_Num_Actuac_Facturac(p_Idinstitucion,
                                                 p_Idfacturacion,
                                                 Null,
                                                 v_Cgpago.Idturno,
                                                 v_Cgpago.Idguardia,
                                                 v_Cgpago.Idpersona,
                                                 Null,
                                                 v_Ug.Fechafin_Ug,
                                                 'N') -- Num Actuaciones Facturadas
                        || Tabulador || '' --V_UG.N_ACTUACFGFACTURADAS -- Num Actuac FG Facturadas
                        || Tabulador ||
                        Func_Num_Asist_Facturac(p_Idinstitucion,
                                                p_Idfacturacion,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug) --- Num Asistencias Facturadas
                        || Tabulador ||
                        Func_Num_Actuac_Totales(p_Idinstitucion,
                                                p_Idfacturacion,
                                                Null,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug,
                                                'N') -- Num Actuaciones Totales
                        || Tabulador || '' --V_UG.N_ACTUACFGTOTAL -- Num Actuac FG Totales
                        || Tabulador ||
                        Func_Num_Asist_Totales(p_Idinstitucion,
                                               p_Idfacturacion,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               Null,
                                               v_Ug.Fechafin_Ug) -- Num Asistencias Totales
                        || Tabulador || f_Num2char(v_Ug.Precioaplicado) -- importe
                        || Tabulador || f_Num2char(v_Ug.Preciocostesfijos) -- precio costes
                        || Tabulador || f_Num2char(v_Ug.Total) -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador || ' ' -- Num Doc Asistido
                        || Tabulador || ' ' -- asistido
                        || Tabulador || ' ' -- delitos
                        || Tabulador || ' ' -- lugar
                        || Tabulador || ' ' -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          For v_Ac In c_Ac(v_Cgpago.Idapunte, v_Ug.Idtipo) Loop

            -- ESCRIBO ACTUACION
            v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                          || Tabulador || v_Cgpago.Nompersona -- nombre persona
                          || Tabulador ||
                          Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                          || Tabulador ||
                          Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                          || Tabulador || v_Ac.Tipoapunte --tipo apunte
                          || Tabulador || ' ' -- fecha inicio
                          || Tabulador || ' ' -- fecha fin
                          || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                          || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                          || Tabulador || v_Ac.Numasistencia -- numero asistencia
                          || Tabulador || v_Ac.Numactuacion -- numero actuacion
                          || Tabulador || v_Ac.Nombrehito -- motivo
                          || Tabulador || ' ' -- Num Actuaciones Facturadas
                          || Tabulador || ' ' -- Num Actuac FG Facturadas
                          || Tabulador || ' ' -- Num Asistencias Facturadas
                          || Tabulador || ' ' -- Num Actuaciones Totales
                          || Tabulador || ' ' -- Num Actuac FG Totales
                          || Tabulador || ' ' -- Num Asistencias Totales
                          || Tabulador || ' ' -- importe
                          || Tabulador || ' ' -- precio costes
                          || Tabulador || ' ' -- total

                          || Tabulador || ' ' -- Separador
                          || Tabulador ||
                          Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                          || Tabulador ||
                          Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                          || Tabulador ||
                          Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                          || Tabulador || v_Ac.Lugar -- lugar
                          || Tabulador || v_Ac.Localidad -- localidad
                          || Tabulador || ' '; -- total letrado

            Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);

          End Loop;

        End Loop;

      Elsif v_Cgpago.Idtipoapunte = 'FG' Then
        For v_Ac In c_Acfg_Sintipo(v_Cgpago.Idapunte) Loop

          -- ESCRIBO ACTUACION
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ac.Tipoapunte --tipo apunte
                        || Tabulador || ' ' -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                        || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                        || Tabulador || v_Ac.Numasistencia -- numero asistencia
                        || Tabulador || v_Ac.Numactuacion -- numero actuacion
                        || Tabulador || v_Ac.Nombrehito -- motivo
                        || Tabulador || ' ' -- Num Actuaciones Facturadas
                        || Tabulador || ' ' -- Num Actuac FG Facturadas
                        || Tabulador || ' ' -- Num Asistencias Facturadas
                        || Tabulador || ' ' -- Num Actuaciones Totales
                        || Tabulador || ' ' -- Num Actuac FG Totales
                        || Tabulador || ' ' -- Num Asistencias Totales
                        || Tabulador || ' ' -- importe
                        || Tabulador || ' ' -- precio costes
                        || Tabulador || ' ' -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador ||
                        Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                        || Tabulador ||
                        Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                        || Tabulador || Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                        || Tabulador || v_Ac.Lugar -- lugar
                        || Tabulador || v_Ac.Localidad -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

        End Loop;

        --                end loop; -- UG

      Elsif v_Cgpago.Idtipoapunte = 'FGTp' Then
        -- RECORRIDO CG-UG-ACTUACIONES (CUANDO CAMBIA LA UG) (QUIZA VALE CUANDO CAMBIA EL CONTADOR TAMBIEN)

        For v_Ug In c_Ugfg(v_Cgpago.Idapunte) Loop

          -- RGG 17/06/2008 ES UN REGISTRO DE UNIDAD DE GUARDIA POR TIPO

          -- ESCRIBO UNIDAD DE GUARDIA
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ug.Tipoapunte --tipo apunte
                        || Tabulador || v_Ug.Fechaini_Ug -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || ' ' -- Fecha Actuacion
                        || Tabulador || ' ' -- fechajustificacion
                        || Tabulador || ' ' -- numero asistencia
                        || Tabulador || ' ' -- numero actuacion
                        || Tabulador ||
                        Replace(v_Ug.Nombrehito, Chr(9), ' ') -- motivo
                        || Tabulador || '' --V_UG.N_ACTUACIONESFACTURADAS -- Num Actuaciones Facturadas
                        || Tabulador ||
                        Func_Num_Actuac_Facturac(p_Idinstitucion,
                                                 p_Idfacturacion,
                                                 Null,
                                                 v_Cgpago.Idturno,
                                                 v_Cgpago.Idguardia,
                                                 v_Cgpago.Idpersona,
                                                 Null,
                                                 v_Ug.Fechafin_Ug,
                                                 'S') -- Num Actuac FG Facturadas
                        || Tabulador || '' --V_UG.N_ASISTENCIASFACTURADAS -- Num Asistencias Facturadas
                        || Tabulador || '' --V_UG.N_ACTUACIONESTOTAL -- Num Actuaciones Totales
                        || Tabulador ||
                        Func_Num_Actuac_Totales(p_Idinstitucion,
                                                p_Idfacturacion,
                                                Null,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug,
                                                'S') -- Num Actuac FG Totales
                        || Tabulador || '' --V_UG.N_ASISTENCIASTOTAL -- Num Asistencias Totales
                        || Tabulador || f_Num2char(v_Ug.Precioaplicado) -- importe
                        || Tabulador || f_Num2char(v_Ug.Preciocostesfijos) -- precio costes
                        || Tabulador || f_Num2char(v_Ug.Total) -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador || ' ' -- Num Doc Asistido
                        || Tabulador || ' ' -- asistido
                        || Tabulador || ' ' -- delitos
                        || Tabulador || ' ' -- lugar
                        || Tabulador || ' ' -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          For v_Ac In c_Acfg(v_Cgpago.Idapunte, v_Ug.Idtipo) Loop

            -- ESCRIBO ACTUACION
            v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                          || Tabulador || v_Cgpago.Nompersona -- nombre persona
                          || Tabulador ||
                          Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                          || Tabulador ||
                          Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                          || Tabulador || v_Ac.Tipoapunte --tipo apunte
                          || Tabulador || ' ' -- fecha inicio
                          || Tabulador || ' ' -- fecha fin
                          || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                          || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                          || Tabulador || v_Ac.Numasistencia -- numero asistencia
                          || Tabulador || v_Ac.Numactuacion -- numero actuacion
                          || Tabulador || v_Ac.Nombrehito -- motivo
                          || Tabulador || ' ' -- Num Actuaciones Facturadas
                          || Tabulador || ' ' -- Num Actuac FG Facturadas
                          || Tabulador || ' ' -- Num Asistencias Facturadas
                          || Tabulador || ' ' -- Num Actuaciones Totales
                          || Tabulador || ' ' -- Num Actuac FG Totales
                          || Tabulador || ' ' -- Num Asistencias Totales
                          || Tabulador || ' ' -- importe
                          || Tabulador || ' ' -- precio costes
                          || Tabulador || ' ' -- total

                          || Tabulador || ' ' -- Separador
                          || Tabulador ||
                          Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                          || Tabulador ||
                          Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                          || Tabulador ||
                          Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                          || Tabulador || v_Ac.Lugar -- lugar
                          || Tabulador || v_Ac.Localidad -- localidad
                          || Tabulador || ' '; -- total letrado

            Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);

          End Loop;

        End Loop; -- UG

      End If;

    End Loop;

    -- ULTIMA PERSONA
    If v_Nombrepersonaanterior Is Not Null Then
      -- FIN DE LA ULTIMA PERSONA
      -- ESCRIBO EL TOTAL EN UNA LINEA PARA EL USUARIO ANTERIOR

      v_Registro := 'TOTAL ' || ' ' -- Num Col Letrado
                    || Tabulador || v_Nombrepersonaanterior -- nombre persona
                    || Tabulador || v_Nombreturnoanterior -- turno
                    || Tabulador || v_Nombreguardiaanterior -- guardia
                    || Tabulador || ' ' --tipo apunte
                    || Tabulador || ' ' -- fecha inicio
                    || Tabulador || ' ' -- fecha fin
                    || Tabulador || ' ' -- Fecha Actuacion
                    || Tabulador || ' ' -- fechajustificacion
                    || Tabulador || ' ' -- numero asistencia
                    || Tabulador || ' ' -- numero actuacion
                    || Tabulador || ' ' -- motivo
                    || Tabulador || ' ' -- Num Actuaciones Facturadas
                    || Tabulador || ' ' -- Num Actuac FG Facturadas
                    || Tabulador || ' ' -- Num Asistencias Facturadas
                    || Tabulador || ' ' -- Num Actuaciones Totales
                    || Tabulador || ' ' -- Num Actuac FG Totales
                    || Tabulador || ' ' -- Num Asistencias Totales
                    || Tabulador || ' ' -- importe
                    || Tabulador || ' ' -- precio costes
                    || Tabulador || ' ' -- total

                    || Tabulador || ' ' -- Separador
                    || Tabulador || ' ' -- Num Doc Asistido
                    || Tabulador || ' ' -- asistido
                    || Tabulador || ' ' -- delitos
                    || Tabulador || ' ' -- lugar
                    || Tabulador || ' ' -- localidad
                    || Tabulador || f_Num2char(Totalpersona); -- total letrado

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);
    End If;

    Begin
      --cerrando fichero
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(0);
      p_Datoserror := 'Fin correcto';
    End; --cerrando fichero

  Exception
    When Utl_File.Write_Error Then
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(5398);
      p_Datoserror := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    When Utl_File.Invalid_Filehandle Then
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(5396);
      p_Datoserror := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    When Utl_File.Invalid_Path Then
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      p_Codretorno := To_Char(5394);
      p_Datoserror := 'LA RUTA NO ES CORRECTA';
    When Utl_File.Invalid_Mode Then
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      p_Codretorno := To_Char(5395);
      p_Datoserror := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    When Utl_File.Invalid_Operation Then
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := Sqlcode || ' ' || To_Char(5397);
      p_Datoserror := Sqlerrm || ' ' || 'OPERACION ERRONEA EN EL FICHERO';

    When Utl_File.Internal_Error Then
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      /* Tratamiento de las matrices */
      p_Codretorno := To_Char(5401);
      p_Datoserror := 'ERROR INTERNO NO ESPECIFICADO';
    When Others Then

      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm;
  End PROC_EXPORTAR_GUARDIAS_OTRO;

  Procedure PROC_EXPORTAR_GUARDIAS_3001(p_Idinstitucion  In Number,
                                        p_Idfacturacion1 In Number,
                                        p_Idfacturacion2 In Number,
                                        p_Idpersona      In Number,
                                        p_Pathfichero    In Varchar2,
                                        p_Fichero        In Varchar2,
                                        p_Idioma         In Number,
                                        p_Codretorno     Out Varchar2,
                                        p_Datoserror     Out Varchar2) Is

    -- VARIABLES
    f_Salida   Utl_File.File_Type; --fichero para escribir
    v_Registro Varchar2(4000); --linea a escribir
    Tabulador Constant Varchar2(6) := Chr(9); --separador de los campos
    Cont Number; --contador de lineas

    Cursor c_Export(v_Idioma Adm_Lenguajes.Idlenguaje%Type, v_Idinstitucion Fcs_Fact_Apunte.Idinstitucion%Type, v_Idfacturacion1 Fcs_Facturacionjg.Idfacturacion%Type, v_Idfacturacion2 Fcs_Facturacionjg.Idfacturacion%Type, v_Idpersona Fcs_Fact_Apunte.Idpersona%Type) Is
      Select Numcol As Numcol,
             Idpersona As Idpersona,
             Initcap(Letrado) As Letrado,
             Idturno As Idturno,
             Decode(Turno, 'COMPL', Guardia, Turno) As Turno,
             Idguardia As Idguardia,
             Guardia As Guardia,
             Tipoapunte As Tipoapunte,
             To_Char(Fechainicio, 'dd/mm/yyyy') As Fechainicio,
             To_Char(Fechafin, 'dd/mm/yyyy') As Fechafin,
             To_Char(Fechaactuacion, 'dd/mm/yyyy') As Fechaactuacion,
             To_Char(Fechajustificacion, 'dd/mm/yyyy') As Fechajustificacion,
             Asistencia As Asistencia,
             Ejg As Ejg,
             Nif As Asistido_Nif,
             Initcap(Asistido) As Asistido_Nombre,
             Sexo As Asistido_Sexo,
             Delitos As Delitos,
             Lugar As Lugar,
             Sum(Actfacgua) As Actfacgua,
             Sum(Actfacfue) As Actfacfue,
             Acttotgua As Acttotgua,
             Acttotfue As Acttotfue,
             Decode(Max(Modulo),
                    'P7',  '32',
                    'P16', 'P16',
                    'P29', '32a',
                    'P44', Case
                              When Sum(Actfacgua) < 6 Then
                               '31'
                              Else
                               '32'
                            End,
                    'P49', Case
                              When Count(*) > 1 Then
                               '32'
                              Else
                               '31a'
                            End,
                    'V4',  '40c',
                    'V16', Case
                              When Sum(Actfacgua) < 4 Then
                               '40ba'
                              Else
                               '40c'
                            End,
                    'V44', Case
                              When Sum(Actfacgua) < 4 Then
                               '40b'
                              Else
                               '40c'
                            End,
                    'V49', Case
                              When Sum(Actfacgua) < 4 Then
                               '40ba'
                              Else
                               '40c'
                            End,
                    Max(Modulo)) As Modulo,
             Sum(Importe) As Importe
        From (

              --A. Guardias: se componen de 2 grupos de datos:
              Select Dat.Idinstitucion,
                      Dat.Idfacturacion,
                      Dat.Fechadesde,
                      To_Number(Nvl(Col.Ncolegiado, Col.Ncomunitario)) As Numcol,
                      Dat.Idpersona,
                      Per.Apellidos1 ||
                      Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
                      Per.Nombre As Letrado,
                      Dat.Idturno As Idturno,
                      Tur.Abreviatura As Turno,
                      Dat.Idguardia As Idguardia,
                      Gua.Nombre As Guardia,
                      1 As Ordapunte,
                      Dat.Tipoapunte,
                      Dat.Fechainicio,
                      Dat.Fechafin,
                      Fechaactuacion,
                      Null As Fechajustificacion,
                      Null As Asistencia,
                      0 As Idactuacion, --Se necesita para agrupar al final
                      Null As Ejg,
                      Null As Nif,
                      Null As Asistido,
                      Null As Sexo,
                      Null As Delitos,
                      Null As Lugar,
                      Sum(Dat.Actfacgua) As Actfacgua,
                      Sum(Dat.Actfacfue) As Actfacfue,
                      0 As Acttotgua,
                      0 As Acttotfue,
                      Decode(Max(Dat.Modulo),
                             Null,
                             Null,
                             Decode(Gua.idTipoGuardia,
                                    1,
                                    'V' || Max(Dat.Modulo),
                                    'P' || Max(Dat.Modulo))) As Modulo,
                      Sum(Dat.Importe) As Importe

              --A.1. Datos de cada apunte
                From (Select Fac.Idinstitucion,
                              Fac.Idfacturacion,
                              Min(Fac.Fechadesde) As Fechadesde,
                              Apu.Idapunte,
                              Apu.Idpersona,
                              Apu.Idturno,
                              Apu.Idguardia,
                              1 As Ordapunte,
                              'G. ' || f_siga_getrecurso_etiqueta('fcs.ficheroFacturacion.tipoApunte.' ||
                              Decode(Cgu.Fecha_Fin - Cgu.Fechainicio + 1,
                                     1,  'dia',
                                     7,  'semana',
                                     13, 'quincena',
                                     14, 'quincena',
                                     15, 'quincena',
                                     16, 'quincena',
                                     28, 'mes',
                                     29, 'mes',
                                     30, 'mes',
                                     31, 'mes',
                                     Cgu.Fecha_Fin - Cgu.Fechainicio + 1 || 'dias'),
                              v_Idioma) As Tipoapunte,
                              Cgu.Fechainicio,
                              Cgu.Fecha_Fin As Fechafin,
                              Cgu.Fechainicio As Fechaactuacion,
                              Sum(Decode(Acf.Idhito, Null, 0, 9, 0, 1)) As Actfacgua,
                              Sum(Decode(Acf.Idhito, Null, 0, 9, 1, 0)) As Actfacfue,
                              Decode(Apu.Precioaplicado,
                                     0,
                                     Null,
                                     Decode(Apu.Precioaplicado -
                                            Sum(Nvl(Acf.Precioaplicado, 0)),
                                            0,
                                            Null,
                                            Apu.Idhito)) As Modulo,
                              Case
                                When Apu.Precioaplicado -
                                     Sum(Nvl(Acf.Precioaplicado, 0)) < 0 Then
                                 Apu.Precioaplicado
                                Else
                                 Apu.Precioaplicado - Sum(Nvl(Acf.Precioaplicado, 0))
                              End As Importe
                         From Fcs_Facturacionjg            Fac,
                              Fcs_Fact_Apunte              Apu,
                              Scs_Cabeceraguardias         Cgu,
                              Fcs_Fact_Actuacionasistencia Acf
                        Where Fac.Idinstitucion = Apu.Idinstitucion
                          And Fac.Idfacturacion = Apu.Idfacturacion
                          And Apu.Idinstitucion = Cgu.Idinstitucion
                          And Apu.Idturno = Cgu.Idturno
                          And Apu.Idguardia = Cgu.Idguardia
                          And Apu.Idpersona = Cgu.Idpersona
                          And Apu.Fechainicio = Cgu.Fechainicio
                          And Apu.Idinstitucion = Acf.Idinstitucion(+)
                          And Apu.Idfacturacion = Acf.Idfacturacion(+)
                          And Apu.Idapunte = Acf.Idapunte(+)
                        Group By Fac.Idinstitucion,
                                 Fac.Idfacturacion,
                                 Apu.Idapunte,
                                 Apu.Idpersona,
                                 Apu.Idturno,
                                 Apu.Idguardia,
                                 Cgu.Fechainicio,
                                 Cgu.Fecha_Fin,
                                 Apu.Idhito,
                                 Apu.Precioaplicado) Dat,

                      --A.2. Tablas de otros datos
                      Cen_Colegiado     Col,
                      Cen_Persona       Per,
                      Scs_Turno         Tur,
                      Scs_Guardiasturno Gua

               Where Dat.Idinstitucion = Col.Idinstitucion
                 And Dat.Idpersona = Col.Idpersona
                 And Dat.Idpersona = Per.Idpersona
                 And Dat.Idinstitucion = Tur.Idinstitucion
                 And Dat.Idturno = Tur.Idturno
                 And Dat.Idinstitucion = Gua.Idinstitucion
                 And Dat.Idturno = Gua.Idturno
                 And Dat.Idguardia = Gua.Idguardia
               Group By Dat.Idinstitucion,
                         Dat.Idfacturacion,
                         Dat.Idturno,
                         Dat.Idguardia,
                         Dat.Idpersona,
                         Dat.Fechadesde,
                         Col.Ncolegiado,
                         Col.Ncomunitario,
                         Per.Apellidos1,
                         Per.Apellidos2,
                         Per.Nombre,
                         Tur.Abreviatura,
                         Gua.Nombre,
                         Dat.Tipoapunte,
                         Dat.Fechainicio,
                         Dat.Fechafin,
                         Dat.Fechaactuacion,
                         Gua.idTipoGuardia

              Union All

              --B. Actuaciones: que se compone de 2 grupos de datos:
              Select Fac.Idinstitucion,
                     Fac.Idfacturacion,
                     Fac.Fechadesde,
                     To_Number(Nvl(Col.Ncolegiado, Col.Ncomunitario)) As Numcol,
                     Per.Idpersona,
                     Per.Apellidos1 ||
                     Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
                     Per.Nombre As Letrado,
                     Tur.Idturno As Idturno,
                     Tur.Abreviatura As Turno,
                     Gua.Idguardia As Idguardia,
                     Gua.Nombre As Guardia,
                     Decode(Act.Idhito, 9, 3, 2) As Ordapunte,
                     '    ' || f_siga_getrecurso_etiqueta('fcs.ficheroFacturacion.tipoApunte.' ||
                     Decode(Act.Idhito, 9, 'actuacionFG', 'actuacion'), v_idioma) As Tipoapunte,
                     Cab.Fechainicio,
                     Cab.Fecha_Fin As Fechafin,
                     Trunc(Act.Fechaactuacion) As Fechaactuacion,
                     Trunc(Act.Fechajustificacion) As Fechajustificacion,
                     'A-' || lpad(Act.Numero, 5, '0') || '/' || Substr(Act.Anio, 3, 2) As Asistencia,
                     Idactuacion, --Se necesita para agrupar al final
                     f_Siga_Cat_Numejg(Asi.Idinstitucion, Asi.Ejgidtipoejg, Asi.Ejganio, Asi.Ejgnumero) As Ejg,
                     Pjg.Nif,
                     Nvl(Pjg.Nombre, '') ||
                     Decode(Pjg.Apellido1, Null, '', ' ' || Pjg.Apellido1) ||
                     Decode(Pjg.Apellido2, Null, '', ' ' || Pjg.Apellido2) As Asistido,
                     Decode(Pjg.Sexo, 'H', 'H', 'M', 'D', Null) As Sexo,
                     f_Siga_Getdelitos_Asistencia(Fac.Idinstitucion,
                                                  Act.Anio,
                                                  Act.Numero,
                                                  v_idioma) As Delitos,
                     f_Siga_Getlugar_Actasi(Fac.Idinstitucion,
                                            Act.Anio,
                                            Act.Numero,
                                            Act.Idactuacion) As Lugar,
                     Null As Actfacgua,
                     Null As Actfacfue,
                     Null As Acttotgua,
                     Null As Acttotfue,
                     Case
                       When Act.Precioaplicado = 0 Or
                            Apu.Precioaplicado - Nvl(Impact, 0) < 0 Then
                        Null
                       When Gua.idTipoGuardia = 1 Then
                        '40a'
                       When Act.Idhito = 9 And Upper(Tur.Abreviatura) Like '%COMPL%' Then
                        '30cb'
                       When Act.Idhito = 9 And
                            Upper(Tur.Abreviatura) Not Like '%COMPL%' Then
                        '30b'
                       When Act.Idhito <> 9 And
                            Upper(Tur.Abreviatura) Like '%COMPL%' Then
                        '30c'
                       When Act.Idhito <> 9 And
                            Upper(Tur.Abreviatura) Not Like '%COMPL%' Then
                        '30'
                     End As Modulo,
                     Case
                       When Apu.Precioaplicado - Nvl(Impact, 0) < 0 Then
                        0
                       Else
                        Act.Precioaplicado
                     End As Importe

              --B.1. Datos de las actuaciones
                From Fcs_Facturacionjg Fac,
                     Fcs_Fact_Apunte Apu,
                     Fcs_Fact_Actuacionasistencia Act,
                     (Select Idinstitucion,
                             Idfacturacion,
                             Idapunte,
                             Sum(Precioaplicado) As Impact
                        From Fcs_Fact_Actuacionasistencia
                       Group By Idinstitucion, Idfacturacion, Idapunte) Imp,

                     --B.2. Tablas de otros datos
                     Scs_Asistencia       Asi,
                     Scs_Personajg        Pjg,
                     Scs_Cabeceraguardias Cab,
                     Cen_Colegiado        Col,
                     Cen_Persona          Per,
                     Scs_Turno            Tur,
                     Scs_Guardiasturno    Gua
               Where Fac.Idinstitucion = Apu.Idinstitucion
                 And Fac.Idfacturacion = Apu.Idfacturacion
                 And Apu.Idinstitucion = Act.Idinstitucion
                 And Apu.Idfacturacion = Act.Idfacturacion
                 And Apu.Idapunte = Act.Idapunte
                 And Act.Idinstitucion = Asi.Idinstitucion
                 And Act.Anio = Asi.Anio
                 And Act.Numero = Asi.Numero
                 And Asi.Idinstitucion = Pjg.Idinstitucion(+)
                 And Asi.Idpersonajg = Pjg.Idpersona(+)
                 And Apu.Idinstitucion = Cab.Idinstitucion
                 And Apu.Idturno = Cab.Idturno
                 And Apu.Idguardia = Cab.Idguardia
                 And Apu.Idpersona = Cab.Idpersona
                 And Apu.Fechainicio = Cab.Fechainicio
                 And Apu.Idinstitucion = Col.Idinstitucion
                 And Apu.Idpersona = Col.Idpersona
                 And Apu.Idpersona = Per.Idpersona
                 And Apu.Idinstitucion = Tur.Idinstitucion
                 And Apu.Idturno = Tur.Idturno
                 And Apu.Idinstitucion = Gua.Idinstitucion
                 And Apu.Idturno = Gua.Idturno
                 And Apu.Idguardia = Gua.Idguardia
                 And Apu.Idinstitucion = Imp.Idinstitucion(+)
                 And Apu.Idfacturacion = Imp.Idfacturacion(+)
                 And Apu.Idapunte = Imp.Idapunte(+)

              )
       Where Idinstitucion = v_Idinstitucion

         --Si no hay 2a, se escoge una unica facturacion:
         -- es necesario hacerlo asi para que el Excel de 1 facturacion
         -- no obtenga otras facturaciones de las mismas fechas
         -- pero de otros grupos de facturacion
         And ((v_Idfacturacion2 Is Null And
               Idfacturacion = v_Idfacturacion1) Or

             (v_Idfacturacion2 Is Not Null And
             Fechadesde Between
             (Select Fac.Fechadesde
                From Fcs_Facturacionjg Fac
               Where Fac.Idinstitucion = v_Idinstitucion
                 And Fac.Idfacturacion = v_Idfacturacion1) And
             (Select Fac.Fechahasta
                From Fcs_Facturacionjg Fac
               Where Fac.Idinstitucion = v_Idinstitucion
                 And Fac.Idfacturacion = v_Idfacturacion2)))

         And Idpersona = nvl(v_Idpersona, Idpersona)

       Group By Numcol,
                Idpersona,
                Letrado,
                Idturno,
                Turno,
                Idguardia,
                Guardia,
                Ordapunte,
                Tipoapunte,
                Fechainicio,
                Fechafin,
                Fechaactuacion,
                Fechajustificacion,
                Asistencia,
                Idactuacion,
                Ejg,
                Nif,
                Asistido,
                Sexo,
                Delitos,
                Lugar,
                Acttotgua,
                Acttotfue
       Order By Numcol, Fechainicio, Ordapunte, Fechaactuacion;

  Begin

    --Recuperacion de cabeceras
    Select Replace(descripcion, '##', Tabulador) Into v_Registro
      From Gen_Recursos
     Where idrecurso = 'fcs.ficheroFacturacion.cabecera.guardias.3001'
       And idlenguaje = p_idioma;

    --Comprobacion de fichero de salida
    If Utl_File.Is_Open(f_Salida) Then
      Utl_File.Fclose(f_Salida);
    End If;

    Cont := 0;

    For v_Linea In c_Export(p_Idioma,
                            p_Idinstitucion,
                            p_Idfacturacion1,
                            p_Idfacturacion2,
                            p_Idpersona) Loop

      If Cont = 0 Then
        /* Antes de escribir el primer registro, escribimos la cabecera*/
        f_Salida := Utl_File.Fopen(p_Pathfichero, p_Fichero, 'w');
        Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      End If;
      Cont := Cont + 1;

      v_Registro := v_Linea.Numcol || Tabulador ||
                    v_Linea.Letrado || Tabulador ||
                    v_Linea.Turno || Tabulador ||
                    v_Linea.Guardia || Tabulador ||
                    v_Linea.Tipoapunte || Tabulador ||
                    v_Linea.Fechainicio || Tabulador ||
                    v_Linea.Fechafin || Tabulador ||
                    v_Linea.Fechaactuacion || Tabulador ||
                    v_Linea.Fechajustificacion || Tabulador ||
                    v_Linea.Asistencia || Tabulador ||
                    v_Linea.Ejg || Tabulador ||
                    v_Linea.Asistido_Nif || Tabulador ||
                    v_Linea.Asistido_Nombre || Tabulador ||
                    v_Linea.Asistido_Sexo || Tabulador ||
                    v_Linea.Delitos || Tabulador ||
                    v_Linea.Lugar || Tabulador ||
                    v_Linea.Actfacgua || Tabulador ||
                    v_Linea.Actfacfue || Tabulador;
      If v_linea.asistencia Is Null Then
        v_registro := v_registro ||
                    FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion,
                                            p_Idfacturacion1,
                                            p_Idfacturacion2,
                                            v_linea.idturno,
                                            v_linea.idguardia,
                                            v_linea.idpersona,
                                            v_linea.fechainicio,
                                            v_linea.fechafin, '0') || Tabulador ||
                    FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion,
                                            p_Idfacturacion1,
                                            p_Idfacturacion2,
                                            v_linea.idturno,
                                            v_linea.idguardia,
                                            v_linea.idpersona,
                                            v_linea.fechainicio,
                                            v_linea.fechafin, '1') || Tabulador;
      Else
        v_registro := v_registro ||
                    '' || Tabulador ||
                    '' || Tabulador;
      End If;
      v_registro := v_registro ||
                    v_Linea.Modulo || Tabulador ||
                    f_num2char(v_Linea.Importe);

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);

    End Loop;

    --cerrando fichero
    Utl_File.Fclose(f_Salida);
    p_Codretorno := To_Char(0);
    p_Datoserror := 'Fin correcto';

  Exception
    When Utl_File.Write_Error Then
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(5398);
      p_Datoserror := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    When Utl_File.Invalid_Filehandle Then
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := To_Char(5396);
      p_Datoserror := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    When Utl_File.Invalid_Path Then
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      p_Codretorno := To_Char(5394);
      p_Datoserror := 'LA RUTA NO ES CORRECTA';
    When Utl_File.Invalid_Mode Then
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      p_Codretorno := To_Char(5395);
      p_Datoserror := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    When Utl_File.Invalid_Operation Then
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := Sqlcode || ' ' || To_Char(5397);
      p_Datoserror := Sqlerrm || ' ' || 'OPERACION ERRONEA EN EL FICHERO';

    When Utl_File.Internal_Error Then
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      /* Tratamiento de las matrices */
      p_Codretorno := To_Char(5401);
      p_Datoserror := 'ERROR INTERNO NO ESPECIFICADO';
    When Others Then

      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm;
  End PROC_EXPORTAR_GUARDIAS_3001;

  FUNCTION FUN_ESDIAAPLICABLE(P_FECHA DATE, P_DIASAPLICABLES VARCHAR2)
    return NUMBER is
    V_RETORNO NUMBER;

  begin
    begin
      select 1 AS RESULTADO
        into V_RETORNO
        from dual
       WHERE to_char(P_FECHA, 'D') IN
             (SELECT DIANUMERO
                FROM GEN_DIASLETRA
               WHERE INSTR(P_DIASAPLICABLES, DIALETRA) <> 0);

    exception
      when no_data_found then
        V_RETORNO := 0;
      when others then
        V_RETORNO := 0;
    end;
    return V_RETORNO;

  end FUN_ESDIAAPLICABLE;

  /****************************************************************************************************************/
  /* Nombre:        PROC_FCS_CARGA_FACTURACION                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de facturacion con el idinstitucion,
  /*                idfacturacion, fechadesde, fechahasta                               */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 19/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
    PROCEDURE PROC_FCS_CARGA_FACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

        -- Hacemos la consulta para obtener la clave de la actuacion, el precio de la actuacion y el importe de los pagos anteriores por actuacion
    BEGIN
        --Para cada Registro de Facturacion cargamos el RECORD:
        FOR Facturacion IN C_FACTURACION(P_IDINSTITUCION, P_IDFACTURACION) LOOP
            V_DATOS_FACTURACION.IDINSTITUCION := Facturacion.IDINSTITUCION;
            V_DATOS_FACTURACION.IDFACTURACION := Facturacion.IDFACTURACION;
            V_DATOS_FACTURACION.FECHADESDE := Facturacion.FECHADESDE;
            V_DATOS_FACTURACION.FECHAHASTA := Facturacion.FECHAHASTA;
        END LOOP;

        --Actualizo para saber que el procedimiento ha finalizado correctamente:
        P_DATOSERROR := 'PROCEDURE PROC_FCS_CARGA_FACTURACION: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_CARGA_FACTURACION;

  /****************************************************************************************************************/
  /* Nombre:        PROC_FCS_INI_CONFIG_GUARDIA                                                                           */
  /* Descripcion:   Este procedimiento se encarga de inicializar la matriz de configuracion de guardias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDTURNO             IN       Identificador del turno                                        NUMBER
  /* P_IDGUARDIA           IN       Identificador de la guardia                                    NUMBER         */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
    PROCEDURE PROC_FCS_INI_CONFIG_GUARDIA (
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

    BEGIN
        /* Inicializamos los datos de la matriz de configuracion de Guardias*/
        V_CONFIG_GUARDIA.GUARDIA := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEGUARDIA := 0;
        V_CONFIG_GUARDIA.DOBLAASISTENCIA := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA := 0;
        V_CONFIG_GUARDIA.DOBLAACTUACION := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.ASISTENCIA := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEASISTENCIA := 0;
        V_CONFIG_GUARDIA.TIPOASISTENCIA := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA := 0;
        V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA := 0;
        V_CONFIG_GUARDIA.ACTUACION := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEACTUACION := 0;
        V_CONFIG_GUARDIA.TIPOACTUACION := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEMAXACTUACION := 0;
        V_CONFIG_GUARDIA.IMPORTEMINACTUACION := 0;
        V_CONFIG_GUARDIA.ACTUACIONFG := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEACTUACIONFG := 0;
        V_CONFIG_GUARDIA.TIPOACTUACIONFG := PKG_SIGA_CONSTANTES.DB_FALSE_N;
        V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG := 0;
        V_CONFIG_GUARDIA.IMPORTESOJ := 0;
        V_CONFIG_GUARDIA.IMPORTEEJG := 0;
        V_CONFIG_GUARDIA.DIASPAGAGUARDIA := '';
        V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA := '';
        V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA := '';
        V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA := '';
        V_CONFIG_GUARDIA.NUMASISTENCIASDOBLA := NULL;
        V_CONFIG_GUARDIA.NUMACTUACIONESDOBLA := NULL;
        V_CONFIG_GUARDIA.NUMHITOS := 0;
        V_CONFIG_GUARDIA.LISTAHITOS.DELETE;

        --Actualizo para saber que el procedimiento ha finalizado correctamente:
        P_DATOSERROR := 'PROCEDURE PROC_FCS_INI_CONFIG_GUARDIA: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_INI_CONFIG_GUARDIA;

    /****************************************************************************************************************
        Nombre: PROC_FCS_CARGA_CONFIG_GUARDIA
        Descripcion: Este procedimiento se encarga de inicializar la matriz de configuracion de guardias

        Parametros:
        - P_IDINSTITUCION - IN - Identificador de la Institucion - NUMBER
        - P_IDTURNO - IN - Identificador del turno - NUMBER
        - P_IDGUARDIA- IN - Identificador de la guardia - NUMBER
        - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
            En caso de error devuelve el codigo de error Oracle correspondiente.
        - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
            En caso de error devuelve el mensaje de error Oracle correspondiente.

        Versiones:            
            - 1.0 - Fecha Creacion: 20/04/2006  - Autor: Pilar Duran Munoz
            - 2.0 - Fecha Modificacion: 26/02/2016  - Autor: Jorge Paez Trivino
                Cambios realizados para facturacion de colegios catalanes, de cabeceras de guardias facturadas, con la configuracion de la primera facturacion
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_CARGA_CONFIG_GUARDIA (
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDFACTURACION IN FCS_FACT_APUNTE.IDFACTURACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        -- Cursor para obtener el idHito y el precio
        CURSOR C_IDHITO(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDFACTURACION NUMBER) IS
            SELECT HFG.IDHITO,
                NVL(HFG.PRECIOHITO, 0) AS PRECIO,
                HFG.DIASAPLICABLES,
                HFG.AGRUPAR
            FROM SCS_HITOFACTURABLEGUARDIA HFG
            WHERE HFG.IDINSTITUCION = V_IDINSTITUCION
                AND HFG.IDTURNO = V_IDTURNO
                AND HFG.IDGUARDIA = V_IDGUARDIA
                AND V_IDFACTURACION IS NULL
            UNION            
            SELECT HHTG.IDHITO,
                NVL(HHTG.PRECIOHITO, 0) AS PRECIO,
                HHTG.DIASAPLICABLES,
                HHTG.AGRUPAR
            FROM FCS_HISTORICO_HITOFACT HHTG
            WHERE HHTG.IDINSTITUCION = V_IDINSTITUCION
                AND HHTG.IDTURNO = V_IDTURNO
                AND HHTG.IDGUARDIA = V_IDGUARDIA
                AND HHTG.IDFACTURACION = V_IDFACTURACION                     
        ORDER BY IDHITO;

        -- RGG 02/03/2007 INC_2870
        b_actfg boolean := false;
        b_act boolean := false;
        b_asi boolean := false;
        b_actTpMax boolean := false;

    BEGIN

        -- Inicializamos el RECORD de Configuracion de Guardias
        PROC_FCS_INI_CONFIG_GUARDIA(P_CODRETORNO, P_DATOSERROR);

        -- Para cada Hito
        FOR V_IDHITO IN C_IDHITO(P_IDINSTITUCION, P_IDTURNO, P_IDGUARDIA, P_IDFACTURACION) LOOP

            V_CONFIG_GUARDIA.NUMHITOS := V_CONFIG_GUARDIA.NUMHITOS + 1;
            V_CONFIG_GUARDIA.LISTAHITOS(V_CONFIG_GUARDIA.NUMHITOS) := V_IDHITO.IDHITO;

            -- Actualizamos el RECORD de Configuracion de Guardias
            CASE (V_IDHITO.IDHITO)
                WHEN 1 THEN
                   V_CONFIG_GUARDIA.GUARDIA := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.IMPORTEGUARDIA := V_IDHITO.PRECIO;
                   V_CONFIG_GUARDIA.DIASPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA := V_IDHITO.AGRUPAR;

                WHEN 2 THEN
                   V_CONFIG_GUARDIA.DOBLAASISTENCIA := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA := V_IDHITO.PRECIO;

                WHEN 3 THEN
                   b_asi := true;
                   V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA := V_IDHITO.PRECIO;

                WHEN 4 THEN
                   V_CONFIG_GUARDIA.DOBLAACTUACION := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA := V_IDHITO.PRECIO;

                WHEN 5 THEN
                   V_CONFIG_GUARDIA.ASISTENCIA := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.IMPORTEASISTENCIA := V_IDHITO.PRECIO;
                   V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA := V_IDHITO.AGRUPAR;

                WHEN 6 THEN
                   b_actfg := true;
                   V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG := V_IDHITO.PRECIO;

                WHEN 7 THEN
                   V_CONFIG_GUARDIA.ACTUACION := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.IMPORTEACTUACION := V_IDHITO.PRECIO;
                   V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA := V_IDHITO.AGRUPAR;

                WHEN 8 THEN
                   b_act := true;
                   V_CONFIG_GUARDIA.IMPORTEMAXACTUACION := V_IDHITO.PRECIO;

                WHEN 9 THEN
                   V_CONFIG_GUARDIA.ACTUACIONFG := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.IMPORTEACTUACIONFG := V_IDHITO.PRECIO;

                WHEN 10 THEN
                   V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA := V_IDHITO.PRECIO;

                WHEN 12 THEN
                   V_CONFIG_GUARDIA.IMPORTESOJ := V_IDHITO.PRECIO;

                WHEN 13 THEN
                   V_CONFIG_GUARDIA.IMPORTEEJG := V_IDHITO.PRECIO;

               WHEN 19 THEN
                   V_CONFIG_GUARDIA.IMPORTEMINACTUACION := V_IDHITO.PRECIO;

                WHEN 20 THEN
                   V_CONFIG_GUARDIA.TIPOASISTENCIA := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA := V_IDHITO.AGRUPAR;

                WHEN 22 THEN
                   V_CONFIG_GUARDIA.TIPOACTUACION := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA := V_IDHITO.AGRUPAR;

                WHEN 23 THEN
                   b_actTpMax := true;
                   V_CONFIG_GUARDIA.TIPOACTUACION := PKG_SIGA_CONSTANTES.DB_TRUE_N;
                   V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA := V_IDHITO.AGRUPAR;

               WHEN 25 THEN
                   V_CONFIG_GUARDIA.TIPOACTUACIONFG := PKG_SIGA_CONSTANTES.DB_TRUE_N;

                WHEN 44 THEN
                   V_CONFIG_GUARDIA.DIASPAGAGUARDIA := V_IDHITO.DIASAPLICABLES;
                   V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA := V_IDHITO.AGRUPAR;

                WHEN 45 THEN
                   V_CONFIG_GUARDIA.NUMASISTENCIASDOBLA := V_IDHITO.PRECIO;

                WHEN 46 THEN
                   --(NDAc)
                   V_CONFIG_GUARDIA.NUMACTUACIONESDOBLA := V_IDHITO.PRECIO;
                   
                ELSE
                    P_DATOSERROR := 'No recupera el hito';                                       
            END CASE;
        END LOOP; -- Fin C_IDHITO

        -- RGG 02/03/2007 INC_2870
        IF B_ACTFG = FALSE THEN
            V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG := NULL;
        END IF;

        IF B_ACT = FALSE THEN
            V_CONFIG_GUARDIA.IMPORTEMAXACTUACION := NULL;
        END IF;

        IF B_ASI = FALSE THEN
            V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA := NULL;
        END IF;

        IF B_ACTTPMAX = FALSE THEN
            V_CONFIG_GUARDIA.IMPORTETIPOACTUACION := NULL;
        ELSE
            V_CONFIG_GUARDIA.IMPORTETIPOACTUACION := 1;
        END IF;

        --INI: Cambio facturacion guardias inactivas catalanes de VG --
            Select Cen_Inst_Idinstitucion
                Into V_CONFIG_GUARDIA.CONSEJOINSTITUCION
            From Cen_Institucion
            Where Idinstitucion = P_IDINSTITUCION;

            Select idTipoGuardia
                Into V_CONFIG_GUARDIA.ESGUARDIAVG
            From Scs_Guardiasturno
            Where Idinstitucion = P_IDINSTITUCION
                And idturno = P_IDTURNO
                And idguardia = P_IDGUARDIA;
        --FIN: Cambio facturacion guardias inactivas catalanes de VG --
        
        -- JPT: Indico que esta configuracion corresponde a un facturacion
        V_CONFIG_GUARDIA.IDFACTURACION := P_IDFACTURACION;

        --Actualizo para saber que el procedimiento ha finalizado correctamente:
        P_DATOSERROR := 'PROCEDURE PROC_FCS_CARGA_CONFIG_GUARDIA: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            when e_error2 then
                P_CODRETORNO := TO_CHAR(-1);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;

            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
            P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_CARGA_CONFIG_GUARDIA;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGAR_CABECERA_GUARDIAS                                                                           */
  /* Descripcion:   Este procedimiento se encarga de Cargar la matriz de Cabecera de Guardias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDTURNO             IN       Identificador del turno                                        NUMBER
  /* P_IDGUARDIA           IN       Identificador de la guardia                                    NUMBER         */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
    PROCEDURE PROC_CARGAR_CABECERA_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO       IN NUMBER,
        P_IDGUARDIA     IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS
        
        -- Cursor utilizado para cargar la matriz de cabecera de guardia
        CURSOR CABECERA_GUARDIAS(V_IDINSTITUCION NUMBER, V_IDTURNO number, V_IDGUARDIA NUMBER) IS
            SELECT IDINSTITUCION,
                   IDTURNO,
                   IDGUARDIA,
                   IDPERSONA,
                   FECHAINICIO,
                   FECHA_FIN,
                   MAX(FACTURADO) AS FACTURADO,
                   MAX(IDFACTURACION) AS IDFACTURACION,
                   SUM(IMPORTEFACTURADO_CG) AS IMPORTEFACTURADO_CG,
                   SUM(IMPORTEFACTURADO_ASIS_CG) AS IMPORTEFACTURADO_ASIS_CG
            FROM (
                --CG REALIZADAS EN EL RANGO DEL CICLO DE FACTURACION
                SELECT IDINSTITUCION,
                       IDTURNO,
                       IDGUARDIA,
                       IDPERSONA,
                       FECHAINICIO,
                       FECHA_FIN,
                       FACTURADO,
                       -- JPT: Puede tener IDFACTURACION aunque no haya facturado (porque quiero obtener la facturacion de la fecha de la cabecera de guardias)
                       FUNC_OBTENER_IDFACTURACION(IDINSTITUCION, IDTURNO, IDGUARDIA, FECHAINICIO) AS IDFACTURACION,
                       0 AS IMPORTEFACTURADO_CG,
                       0 AS IMPORTEFACTURADO_ASIS_CG
                FROM SCS_CABECERAGUARDIAS CG
                WHERE IDINSTITUCION = V_IDINSTITUCION
                    AND IDTURNO = V_IDTURNO
                    AND IDGUARDIA = V_IDGUARDIA 
                    AND NVL(VALIDADO, '0') = '1' -- validadas
                    AND TRUNC(NVL(FECHAVALIDACION, FECHAINICIO)) BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA) -- dentro del rango de fechas de la facturacion
                    AND NOT EXISTS ( -- sin facturar
                        SELECT 1
                        FROM FCS_FACT_APUNTE FAC_CG
                        WHERE CG.IDINSTITUCION = FAC_CG.IDINSTITUCION
                        AND CG.IDTURNO = FAC_CG.IDTURNO
                        AND CG.IDGUARDIA = FAC_CG.IDGUARDIA
                        AND CG.IDPERSONA = FAC_CG.IDPERSONA
                        AND CG.FECHAINICIO = FAC_CG.FECHAINICIO
                    )
                UNION
                -- CG con actuaciones (que no son de fuera de guardia) justificadas en el rango del ciclo de facturacion
                SELECT IDINSTITUCION,
                       IDTURNO,
                       IDGUARDIA,
                       IDPERSONA,
                       FECHAINICIO,
                       FECHA_FIN,
                       FACTURADO, 
                       FUNC_OBTENER_IDFACTURACION(IDINSTITUCION, IDTURNO, IDGUARDIA, FECHAINICIO) AS IDFACTURACION,
                       NVL(FUNC_IMPORTEFACTURADO_CG(IDINSTITUCION, IDTURNO, IDGUARDIA, IDPERSONA, FECHAINICIO), 0) AS IMPORTEFACTURADO_CG,
                       NVL(FUNC_IMPORTEFACTURADO_ASIS_CG(IDINSTITUCION, IDTURNO, IDGUARDIA, IDPERSONA, FECHAINICIO), 0) AS IMPORTEFACTURADO_ASIS_CG
                FROM SCS_CABECERAGUARDIAS CG
                WHERE IDINSTITUCION = V_IDINSTITUCION
                    AND IDTURNO = V_IDTURNO
                    AND IDGUARDIA = V_IDGUARDIA
                    AND NVL(VALIDADO, '0') = '1' -- validadas
                    AND EXISTS ( -- JPT: que tengan al menos una actuacion que no sea fuera de guardia...
                        SELECT 1
                        FROM SCS_GUARDIASCOLEGIADO UG,
                            SCS_ASISTENCIA ASI,
                            SCS_ACTUACIONASISTENCIA ACT
                        WHERE CG.IDINSTITUCION = UG.IDINSTITUCION
                           AND CG.IDTURNO = UG.IDTURNO
                           AND CG.IDGUARDIA = UG.IDGUARDIA
                           AND CG.FECHAINICIO = UG.FECHAINICIO
                           AND CG.IDPERSONA = UG.IDPERSONA
                           AND UG.IDINSTITUCION = ASI.IDINSTITUCION
                           AND UG.IDTURNO = ASI.IDTURNO
                           AND UG.IDGUARDIA = ASI.IDGUARDIA
                           AND UG.IDPERSONA = ASI.IDPERSONACOLEGIADO
                           AND UG.FECHAFIN = TRUNC(ASI.FECHAHORA)
                           AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                           AND ASI.ANIO = ACT.ANIO
                           AND ASI.NUMERO = ACT.NUMERO
                            AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                            AND TRUNC(ACT.FECHAJUSTIFICACION)  BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA) -- con fecha de justificacion en el rango de fechas de la facturacion
                            AND ACT.DIADESPUES = 'N' -- actuacion que NO es del dia despues (NO es fuera de guardia)
                            AND NOT EXISTS ( -- sin facturar
                                SELECT 1
                                FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                                WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                                    AND FAC.ANIO = ACT.ANIO
                                    AND FAC.NUMERO = ACT.NUMERO
                                    AND FAC.IDACTUACION = ACT.IDACTUACION)
                            )
                    )
                GROUP BY IDINSTITUCION,
                      IDTURNO,
                      IDGUARDIA,
                      IDPERSONA,
                      FECHAINICIO,
                      FECHA_FIN
              ORDER BY IDFACTURACION DESC; -- JPT: Se ordena DESC para que salgan primero los IDFACTURACION con valor NULL

    BEGIN
        --Inicializamos la matriz de cabecera de guardias
        M_CG_FACTURABLE.DELETE;
        IND_CG_FACTURABLE := 0;

        -- Cargamos en memoria la matriz de cabecera de guardias m_CG_facturables
        FOR V_CABECERAGUARDIAS IN CABECERA_GUARDIAS(P_IDINSTITUCION, P_IDTURNO, P_IDGUARDIA) loop
            IND_CG_FACTURABLE := IND_CG_FACTURABLE + 1;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDINSTITUCION := P_IDINSTITUCION;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDTURNO := P_IDTURNO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDGUARDIA := P_IDGUARDIA;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDPERSONA := V_CABECERAGUARDIAS.IDPERSONA;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FECHAINICIO := V_CABECERAGUARDIAS.FECHAINICIO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FECHAFIN := V_CABECERAGUARDIAS.FECHA_FIN;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FACTURADO := V_CABECERAGUARDIAS.FACTURADO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDFACTURACION := NVL(V_CABECERAGUARDIAS.IDFACTURACION, V_CONFIG_GUARDIA.IDFACTURACION); -- JPT: Si hay alguno nulo, le pongo el IDFACTURACION actual
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IMPORTEFACTURADO := V_CABECERAGUARDIAS.IMPORTEFACTURADO_CG;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IMPORTEFACTURADOASIS := V_CABECERAGUARDIAS.IMPORTEFACTURADO_ASIS_CG;

            --INI: Cambio para Asistencias que derivan en Designacion
                M_CG_FACTURABLE(IND_CG_FACTURABLE).TIENE_ASISTQUEDERIVAN := F_TIENE_ASIST_DERIVADAS(
                    P_IDINSTITUCION,
                    P_IDTURNO,
                    P_IDGUARDIA,
                    V_CABECERAGUARDIAS.IDPERSONA,
                    TRUNC(V_CABECERAGUARDIAS.FECHAINICIO),
                    TRUNC(V_CABECERAGUARDIAS.FECHA_FIN),
                    V_DATOS_FACTURACION.FECHADESDE,
                    V_DATOS_FACTURACION.FECHAHASTA);
            --FIN: Cambio para Asistencias que derivan en Designacion            
        END LOOP;

        P_DATOSERROR := 'PROCEDURE PROC_CARGAR_CABECERA_GUARDIAS: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);
  END PROC_CARGAR_CABECERA_GUARDIAS;  
  
  /****************************************************************************************************************
    Nombre: PROC_CARGAR_CABGUARDIASFG
    Descripcion: Procedimiento que obtiene las cabeceras de guardia de actuaciones fuera de guardia

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
  ****************************************************************************************************************/  
    PROCEDURE PROC_CARGAR_CABGUARDIASFG(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        -- Cursor utilizado para cargar la matriz de cabecera de guardia (de las actuaciones Fuera de Guardia)
        CURSOR CABECERA_GUARDIASFG(V_IDINSTITUCION NUMBER, V_IDTURNO number, V_IDGUARDIA NUMBER) IS
            -- CG con actuaciones validadas y de fuera de guardia, justificadas en el rango de fechas de la facturacion
            SELECT CG.IDINSTITUCION,
                   CG.IDTURNO,
                   CG.IDGUARDIA,
                   CG.IDPERSONA,
                   CG.FECHAINICIO,
                   CG.FECHA_FIN,
                   ACT.FECHA,
                   MAX(CG.FACTURADO) AS FACTURADO, 
                   FUNC_OBTENER_IDFACTURACION(CG.IDINSTITUCION, CG.IDTURNO, CG.IDGUARDIA, ACT.FECHA) AS IDFACTURACION
            FROM SCS_CABECERAGUARDIAS CG, 
                SCS_GUARDIASCOLEGIADO UG,
                SCS_ASISTENCIA ASI,
                SCS_ACTUACIONASISTENCIA ACT
            WHERE CG.IDINSTITUCION = V_IDINSTITUCION
                AND CG.IDTURNO = V_IDTURNO
                AND CG.IDGUARDIA = V_IDGUARDIA
                AND NVL(CG.VALIDADO, '0') = '1' -- validadas
                AND CG.IDINSTITUCION = UG.IDINSTITUCION
                AND CG.IDTURNO = UG.IDTURNO
                AND CG.IDGUARDIA = UG.IDGUARDIA
                AND CG.FECHAINICIO = UG.FECHAINICIO
                AND CG.IDPERSONA = UG.IDPERSONA
                AND UG.IDINSTITUCION = ASI.IDINSTITUCION
                AND UG.IDTURNO = ASI.IDTURNO
                AND UG.IDGUARDIA = ASI.IDGUARDIA
                AND UG.IDPERSONA = ASI.IDPERSONACOLEGIADO
                AND UG.FECHAFIN = TRUNC(ASI.FECHAHORA)
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO
                AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA) -- con fecha de justificacion en el rango de fechas de la facturacion
                AND ACT.DIADESPUES = 'S' -- actuacion del dia despues (fuera de guardia)             
                AND NOT EXISTS ( -- sin facturar
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                    WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION)
            GROUP BY CG.IDINSTITUCION,
                  CG.IDTURNO,
                  CG.IDGUARDIA,
                  CG.IDPERSONA,
                  CG.FECHAINICIO,
                  CG.FECHA_FIN,
                  ACT.FECHA
            ORDER BY IDFACTURACION DESC; -- JPT: Se ordena DESC para que salgan primero los IDFACTURACION con valor NULL        

    BEGIN
        -- Cargamos en memoria la matriz de cabecera de guardias m_CG_facturables
        FOR V_CABECERAGUARDIAS IN CABECERA_GUARDIASFG(P_IDINSTITUCION, P_IDTURNO, P_IDGUARDIA) loop
            IND_CG_FACTURABLE := IND_CG_FACTURABLE + 1;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDINSTITUCION := P_IDINSTITUCION;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDTURNO := P_IDTURNO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDGUARDIA := P_IDGUARDIA;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDPERSONA := v_cabeceraguardias.IDPERSONA;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FECHAINICIO := v_cabeceraguardias.FECHAINICIO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FACTURADO := v_cabeceraguardias.FACTURADO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FECHAACT := V_CABECERAGUARDIAS.FECHA; -- JPT: Necesito guardar la fecha de la actuacion porque ahora agrupamos por ese campo
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDFACTURACION := NVL(V_CABECERAGUARDIAS.IDFACTURACION, V_CONFIG_GUARDIA.IDFACTURACION); -- JPT: Si hay alguno nulo, le pongo el IDFACTURACION actual
        END LOOP;

        P_DATOSERROR := 'PROC_CARGAR_CABGUARDIASFG: Ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);
  END PROC_CARGAR_CABGUARDIASFG;  

  /****************************************************************************************************************/
  /* Nombre:        FUNC_IMPORTEFACTURADO_CG                                                                      */
  /* Descripcion:   Funcion que nos devueve el importe facturado de la Cabecera de Guardia.
  /*                No se obtiene lo facturado por FG. Facturaciones + Regularizaciones                           */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha de inicio                                                  DATE        */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_IMPORTEFACTURADO_ASIS_CG(P_IDINSTITUCION        NUMBER,
                                    P_IDTURNO              NUMBER,
                                    P_IDGUARDIA            NUMBER,
                                    P_IDPERSONA            NUMBER,
                                    P_FECHAINICIO          DATE)
    return NUMBER is
    importeTotalCG         number;
    V_CURSOR               INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
    V_DUMMY                INTEGER;
    c_importeFacturadoN_asis_CG c_importeFacturado_asis_CG%type;

  begin
    /*begin
       FOR V_importeFacturado_CG IN C_importeFacturado_CG(P_IDINSTITUCION,P_IDTURNO,P_IDGUARDIA,P_IDPERSONA,P_FECHAINICIO) LOOP
        importeTotalCG:=V_importeFacturado_CG.Importetotalcg;
       END LOOP;


    exception
    when others then
      importeTotalCG:=0;
    end;*/

    c_importeFacturadoN_asis_CG := replace(c_importeFacturado_asis_CG,
                                          '@P_IDINSTITUCION@',
                                          P_IDINSTITUCION);
    c_importeFacturadoN_asis_CG := replace(c_importeFacturadoN_asis_CG,
                                          '@P_IDTURNO@',
                                          P_IDTURNO);
    c_importeFacturadoN_asis_CG := replace(c_importeFacturadoN_asis_CG,
                                          '@P_IDGUARDIA@',
                                          P_IDGUARDIA);
    c_importeFacturadoN_asis_CG := replace(c_importeFacturadoN_asis_CG,
                                          '@P_IDPERSONA@',
                                          P_IDPERSONA);
    c_importeFacturadoN_asis_CG := replace(c_importeFacturadoN_asis_CG,
                                          '@P_FECHAINICIO@',
                                          P_FECHAINICIO);

    V_CURSOR := DBMS_SQL.OPEN_CURSOR;

    /* Se analiza la consulta */

    DBMS_SQL.PARSE(V_CURSOR, c_importeFacturadoN_asis_CG, DBMS_SQL.V7);

    /* Define las variables de salida */

    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, importeTotalCG);

    /* Ejecuta la orden. No nos preocupa el valor devuelto, */
    /* pero necesitamos declarar una variable para el       */

    V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);

    /* Bucle de extraccion de filas */

    LOOP
      /* Extrae las filas y las pone en el bufer, y tambien */
      /* comprueba la condicion de salida del bucle         */

      IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN
        EXIT;
      END IF;

      /* Recupera las filas del bufer en las variables PL/SQL */

      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, importeTotalCG);
    end loop;
    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    return importeTotalCG;

  end FUNC_IMPORTEFACTURADO_ASIS_CG;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_IMPORTEFACTURADO_CG                                                                      */
  /* Descripcion:   Funcion que nos devueve el importe facturado de la Cabecera de Guardia.
  /*                No se obtiene lo facturado por FG. Facturaciones + Regularizaciones                           */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha de inicio                                                  DATE        */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 20/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_IMPORTEFACTURADO_CG(P_IDINSTITUCION        NUMBER,
                                    P_IDTURNO              NUMBER,
                                    P_IDGUARDIA            NUMBER,
                                    P_IDPERSONA            NUMBER,
                                    P_FECHAINICIO          DATE)
    return NUMBER is
    importeTotalCG         number;
    V_CURSOR               INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
    V_DUMMY                INTEGER;
    c_importeFacturadoN_CG      c_importeFacturado_CG%type;

  begin
    /*begin
       FOR V_importeFacturado_CG IN C_importeFacturado_CG(P_IDINSTITUCION,P_IDTURNO,P_IDGUARDIA,P_IDPERSONA,P_FECHAINICIO) LOOP
        importeTotalCG:=V_importeFacturado_CG.Importetotalcg;
       END LOOP;


    exception
    when others then
      importeTotalCG:=0;
    end;*/

    c_importeFacturadoN_CG := replace(c_importeFacturado_CG,
                                          '@P_IDINSTITUCION@',
                                          P_IDINSTITUCION);
    c_importeFacturadoN_CG := replace(c_importeFacturadoN_CG,
                                          '@P_IDTURNO@',
                                          P_IDTURNO);
    c_importeFacturadoN_CG := replace(c_importeFacturadoN_CG,
                                          '@P_IDGUARDIA@',
                                          P_IDGUARDIA);
    c_importeFacturadoN_CG := replace(c_importeFacturadoN_CG,
                                          '@P_IDPERSONA@',
                                          P_IDPERSONA);
    c_importeFacturadoN_CG := replace(c_importeFacturadoN_CG,
                                          '@P_FECHAINICIO@',
                                          P_FECHAINICIO);

    V_CURSOR := DBMS_SQL.OPEN_CURSOR;

    /* Se analiza la consulta */

    DBMS_SQL.PARSE(V_CURSOR, c_importeFacturadoN_CG, DBMS_SQL.V7);

    /* Define las variables de salida */

    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, importeTotalCG);

    /* Ejecuta la orden. No nos preocupa el valor devuelto, */
    /* pero necesitamos declarar una variable para el       */

    V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);

    /* Bucle de extraccion de filas */

    LOOP
      /* Extrae las filas y las pone en el bufer, y tambien */
      /* comprueba la condicion de salida del bucle         */

      IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN
        EXIT;
      END IF;

      /* Recupera las filas del bufer en las variables PL/SQL */

      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, importeTotalCG);
    end loop;
    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    return importeTotalCG;

  end FUNC_IMPORTEFACTURADO_CG;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_ASISTENCIASFACTURADAS                                                                      */
  /* Descripcion:   Funcion que nos devuelve el total de asistencias ya facturadas anteriormente                  */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 21/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_ASISTENCIASFACTURADAS_CG(P_IDINSTITUCION NUMBER,
                                         P_IDTURNO       NUMBER,
                                         P_IDGUARDIA     NUMBER,
                                         P_IDPERSONA     NUMBER,
                                         P_FECHAINICIO   DATE,
                                         P_FECHAFIN      DATE) return number is
    V_CURSOR                    INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
    V_DUMMY                     INTEGER;
    v_asistenciafacturada       number := 0;
    c_asistenciasfacturadasN_CG c_asistenciasfacturadas_CG%type;

  begin
    c_asistenciasfacturadasN_CG := replace(c_asistenciasfacturadas_CG,
                                           '@P_IDINSTITUCION@',
                                           P_IDINSTITUCION);
    c_asistenciasfacturadasN_CG := replace(c_asistenciasfacturadasN_CG,
                                           '@P_IDTURNO@',
                                           P_IDTURNO);
    c_asistenciasfacturadasN_CG := replace(c_asistenciasfacturadasN_CG,
                                           '@P_IDGUARDIA@',
                                           P_IDGUARDIA);
    c_asistenciasfacturadasN_CG := replace(c_asistenciasfacturadasN_CG,
                                           '@P_IDPERSONA@',
                                           P_IDPERSONA);
    c_asistenciasfacturadasN_CG := replace(c_asistenciasfacturadasN_CG,
                                           '@P_FECHAINICIO@',
                                           P_FECHAINICIO);
    c_asistenciasfacturadasN_CG := replace(c_asistenciasfacturadasN_CG,
                                           '@P_FECHAFIN@',
                                           P_FECHAFIN);
    -- c_asistenciasfacturadasN_CG:=replace(c_asistenciasfacturadasN_CG,'@P_DIASAPLICABLES@',P_DIASAPLICABLES );
    BEGIN
      V_CURSOR := DBMS_SQL.OPEN_CURSOR;

      /* Se analiza la consulta */

      DBMS_SQL.PARSE(V_CURSOR, c_asistenciasfacturadasN_CG, DBMS_SQL.V7);

      /* Define las variables de salida */

      DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, v_asistenciafacturada);

      /* Ejecuta la orden. No nos preocupa el valor devuelto, */
      /* pero necesitamos declarar una variable para el       */

      V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);
      LOOP
        /* Extrae las filas y las pone en el bufer, y tambien */
        /* comprueba la condicion de salida del bucle         */

        V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
        IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

          EXIT;
        END IF;
        DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, v_asistenciafacturada);

      end loop;

    EXCEPTION
      when others then

        v_asistenciafacturada := 0;
    end;
    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    return v_asistenciafacturada;

  end FUNC_ASISTENCIASFACTURADAS_CG;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_ACTUACIONESFACTURADAS                                                                      */
  /* Descripcion:   Funcion que nos devuelve el total de actuaciones facturadas                                      */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 21/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_ACTUACIONESFACTURADAS_CG(P_IDINSTITUCION NUMBER,
                                         P_IDTURNO       NUMBER,
                                         P_IDGUARDIA     NUMBER,
                                         P_IDPERSONA     NUMBER,
                                         P_FECHAINICIO   DATE,
                                         P_FECHAFIN      DATE) return number is
    V_CURSOR                    INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
    V_DUMMY                     INTEGER;
    v_actuacionesfacturada      number := 0;
    c_actuacionesfacturadasN_CG c_actuacionesfacturadas_CG%type;

  begin
    c_actuacionesfacturadasN_CG := replace(c_actuacionesfacturadas_CG,
                                           '@P_IDINSTITUCION@',
                                           P_IDINSTITUCION);
    c_actuacionesfacturadasN_CG := replace(c_actuacionesfacturadasN_CG,
                                           '@P_IDTURNO@',
                                           P_IDTURNO);
    c_actuacionesfacturadasN_CG := replace(c_actuacionesfacturadasN_CG,
                                           '@P_IDGUARDIA@',
                                           P_IDGUARDIA);
    c_actuacionesfacturadasN_CG := replace(c_actuacionesfacturadasN_CG,
                                           '@P_IDPERSONA@',
                                           P_IDPERSONA);
    c_actuacionesfacturadasN_CG := replace(c_actuacionesfacturadasN_CG,
                                           '@P_FECHAINICIO@',
                                           P_FECHAINICIO);
    c_actuacionesfacturadasN_CG := replace(c_actuacionesfacturadasN_CG,
                                           '@P_FECHAFIN@',
                                           P_FECHAFIN);
    --c_actuacionesfacturadasN_CG:=replace(c_actuacionesfacturadasN_CG,'@P_DIASAPLICABLES@',P_DIASAPLICABLES );

    BEGIN
      V_CURSOR := DBMS_SQL.OPEN_CURSOR;

      /* Se analiza la consulta */

      DBMS_SQL.PARSE(V_CURSOR, c_actuacionesfacturadasN_CG, DBMS_SQL.V7);

      /* Define las variables de salida */

      DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, v_actuacionesfacturada);

      /* Ejecuta la orden. No nos preocupa el valor devuelto, */
      /* pero necesitamos declarar una variable para el       */

      V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);
      LOOP
        /* Extrae las filas y las pone en el bufer, y tambien */
        /* comprueba la condicion de salida del bucle         */

        V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
        IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

          EXIT;
        END IF;
        DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, v_actuacionesfacturada);

      end loop;

    EXCEPTION
      when others then
        v_actuacionesfacturada := 0;
    end;
    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    return v_actuacionesfacturada;

  end FUNC_ACTUACIONESFACTURADAS_CG;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_ACTUACIONESNUEVAS                                                                    */
  /* Descripcion:   Funcion que nos devuelve el total de actuaciones nuevas                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     FECHA INICIO de la cabecera de Guardia                           DATE,
  /* P_FECHAFIN               IN     FECHA FIN de la cabecera de Guardia                           DATE,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 21/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  FUNCTION FUNC_ACTUACIONESNUEVAS_CG(P_IDINSTITUCION NUMBER,
                                     P_IDTURNO       NUMBER,
                                     P_IDGUARDIA     NUMBER,
                                     P_IDPERSONA     NUMBER,
                                     P_FECHAINICIO   DATE,
                                     P_FECHAFIN      DATE) return number is

    V_CURSOR           INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
    V_DUMMY            INTEGER;
    v_actuacionesnueva number := 0;

    c_actuacionesnuevasN_CG c_actuacionesnuevas_CG%type;

  begin
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevas_CG,
                                       '@P_IDINSTITUCION@',
                                       P_IDINSTITUCION);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_IDTURNO@',
                                       P_IDTURNO);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_IDGUARDIA@',
                                       P_IDGUARDIA);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_IDPERSONA@',
                                       P_IDPERSONA);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_FECHAINICIO@',
                                       P_FECHAINICIO);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_FECHAFIN@',
                                       P_FECHAFIN);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_FECHAINIFACT@',
                                       V_DATOS_FACTURACION.FECHADESDE);
    c_actuacionesnuevasN_CG := replace(c_actuacionesnuevasN_CG,
                                       '@P_FECHAFINFACT@',
                                       V_DATOS_FACTURACION.FECHAHASTA);
    --c_actuacionesnuevasN_CG:=replace(c_actuacionesnuevasN_CG,'@P_DIASAPLICABLES@',P_DIASAPLICABLES );
    BEGIN
      V_CURSOR := DBMS_SQL.OPEN_CURSOR;

      /* Se analiza la consulta */

      DBMS_SQL.PARSE(V_CURSOR, c_actuacionesnuevasN_CG, DBMS_SQL.V7);

      /* Define las variables de salida */

      DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, v_actuacionesnueva);

      /* Ejecuta la orden. No nos preocupa el valor devuelto, */
      /* pero necesitamos declarar una variable para el       */

      V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);
      LOOP
        /* Extrae las filas y las pone en el bufer, y tambien */
        /* comprueba la condicion de salida del bucle         */

        V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
        IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

          EXIT;
        END IF;
        DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, v_actuacionesnueva);

      end loop;

    EXCEPTION
      when others then
        v_actuacionesnueva := 0;
    end;
    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    return v_actuacionesnueva;

  end FUNC_ACTUACIONESNUEVAS_CG;

  /*
   *  Funciones que calculan los numeros de actuaciones y asistencias
   * para la exportacion del fichero de guardias en PROC_FCS_EXPORTAR_GUARDIAS
   *
   *  Si se pasa P_FECHAINICIO nulo, solo buscara en el dia exacto P_FECHAFIN
   *
   * INICIO
   */
  Function FUNC_NUM_ACTUAC_FACTURAC(p_Idinstitucion  Number,
                                    p_Idfacturacion1 Number,
                                    p_Idfacturacion2 Number,
                                    p_Idturno        Number,
                                    p_Idguardia      Number,
                                    p_Idpersona      Number,
                                    p_Fechainicio    Date,
                                    p_Fechafin       Date,
                                    p_Esfg           Varchar2)
    Return Number Is
    Valor Number;

  Begin
    Select Count(*)
      Into Valor
      From Fcs_Facturacionjg            Fac,
           Fcs_Fact_Actuacionasistencia Fact,
           Scs_Actuacionasistencia      Act,
           Scs_Asistencia               Asi
     Where Fact.Idinstitucion = Act.Idinstitucion
       And Fact.Anio = Act.Anio
       And Fact.Numero = Act.Numero
       And Fact.Idactuacion = Act.Idactuacion
       And Act.Idinstitucion = Asi.Idinstitucion
       And Act.Anio = Asi.Anio
       And Act.Numero = Asi.Numero
       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Idturno = p_Idturno
       And Asi.Idguardia = p_Idguardia
       And Asi.Idpersonacolegiado = p_Idpersona
       And Trunc(Asi.Fechahora) Between
           Trunc(Nvl(p_Fechainicio, p_Fechafin)) And Trunc(p_Fechafin)
       And Act.Diadespues =
           Upper(Decode(p_Esfg, '0', 'N', '1', 'S', p_Esfg))

       And Fact.Idinstitucion = Fac.Idinstitucion
       And Fact.Idfacturacion = Fac.Idfacturacion
       And Fac.Fechadesde Between
           (Select Fechadesde
              From Fcs_Facturacionjg
             Where Idinstitucion = p_Idinstitucion
               And Idfacturacion = p_Idfacturacion1)
       And (Select Fechahasta
              From Fcs_Facturacionjg
             Where Idinstitucion = p_Idinstitucion
               And Idfacturacion = Nvl(p_Idfacturacion2, p_Idfacturacion1))
       And Fac.Prevision = 0;

    Return Valor;
  End FUNC_NUM_ACTUAC_FACTURAC;

  Function FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion  Number,
                                   p_Idfacturacion1 Number,
                                   p_Idfacturacion2 Number,
                                   p_Idturno        Number,
                                   p_Idguardia      Number,
                                   p_Idpersona      Number,
                                   p_Fechainicio    Date,
                                   p_Fechafin       Date,
                                   p_Esfg           Varchar2) Return Number Is
    Valor Number;

  Begin
    Select Count(*)
      Into Valor
      From Fcs_Facturacionjg            Fac,
           Fcs_Fact_Actuacionasistencia Fact,
           Scs_Actuacionasistencia      Act,
           Scs_Asistencia               Asi
     Where Fact.Idinstitucion = Act.Idinstitucion
       And Fact.Anio = Act.Anio
       And Fact.Numero = Act.Numero
       And Fact.Idactuacion = Act.Idactuacion
       And Act.Idinstitucion = Asi.Idinstitucion
       And Act.Anio = Asi.Anio
       And Act.Numero = Asi.Numero
       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Idturno = p_Idturno
       And Asi.Idguardia = p_Idguardia
       And Asi.Idpersonacolegiado = p_Idpersona
       And Trunc(Asi.Fechahora) Between
           Trunc(Nvl(p_Fechainicio, p_Fechafin)) And Trunc(p_Fechafin)
       And Act.Diadespues =
           Upper(Decode(p_Esfg, '0', 'N', '1', 'S', p_Esfg))

       And Fact.Idinstitucion = Fac.Idinstitucion
       And Fact.Idfacturacion = Fac.Idfacturacion
       And Fac.Fechadesde <=
           (Select Max(Fechadesde)
              From Fcs_Facturacionjg
             Where Idinstitucion = p_Idinstitucion
               And Idfacturacion In (nvl(p_Idfacturacion1, Idfacturacion),
                                     nvl(p_Idfacturacion2, Idfacturacion)))
       And Fac.Prevision = 0;

    Return Valor;
  End FUNC_NUM_ACTUAC_TOTALES;

  function FUNC_NUM_ASIST_FACTURAC(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE) return number is
    valor number;

  begin
    select count(*)
      into valor
      from FCS_FACT_ASISTENCIA FASI, SCS_ASISTENCIA ASI
     where fasi.idinstitucion = asi.idinstitucion
       and fasi.anio = asi.anio
       and fasi.numero = asi.numero
       and asi.idinstitucion = P_IDINSTITUCION
       and asi.idturno = P_IDTURNO
       and asi.idguardia = P_IDGUARDIA
       and asi.idpersonacolegiado = P_IDPERSONA
       and trunc(asi.fechahora) between
           trunc(nvl(P_FECHAINICIO, P_FECHAFIN)) and trunc(P_FECHAFIN)
       and fasi.idinstitucion = P_IDINSTITUCION
       and fasi.idfacturacion = P_IDFACTURACION;

    return valor;
  end FUNC_NUM_ASIST_FACTURAC;

  function FUNC_NUM_ASIST_TOTALES(P_IDINSTITUCION NUMBER,
                                  P_IDFACTURACION NUMBER,
                                  P_IDTURNO       NUMBER,
                                  P_IDGUARDIA     NUMBER,
                                  P_IDPERSONA     NUMBER,
                                  P_FECHAINICIO   DATE,
                                  P_FECHAFIN      DATE) return number is
    valor number;
  begin
    select count(*)
      into valor
      from FCS_FACT_ASISTENCIA FASI, SCS_ASISTENCIA ASI
     where fasi.idinstitucion = asi.idinstitucion
       and fasi.anio = asi.anio
       and fasi.numero = asi.numero
       and asi.idinstitucion = P_IDINSTITUCION
       and asi.idturno = P_IDTURNO
       and asi.idguardia = P_IDGUARDIA
       and asi.idpersonacolegiado = P_IDPERSONA
       and trunc(asi.fechahora) between
           trunc(nvl(P_FECHAINICIO, P_FECHAFIN)) and trunc(P_FECHAFIN)
       and fasi.idinstitucion = P_IDINSTITUCION
       and fasi.idfacturacion <= P_IDFACTURACION;

    return valor;
  end FUNC_NUM_ASIST_TOTALES;

    /****************************************************************************************************************/
    /* Nombre: FUNC_EXISTE_ACTUACION_JUSTIF */
    /* Descripcion: Funcion que comprueba que exista una actuacion justificada */
    /* */
    /* P_IDINSTITUCION - IN - identificador de la institucion - NUMBER */
    /* P_ANIO - IN - identificador del anio - NUMBER */
    /* P_NUMERO - IN - identificador del numero de asistencia - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 21/04/2006 - Autor: Pilar Duran Munoz */
    /* Version: 2.0 - Fecha Modificacion: 22/01/2015 - Autor: Jorge Paez Trivino - Se indica si se admite fuera de guardia */
  /****************************************************************************************************************/
    FUNCTION FUNC_EXISTE_ACTUACION_JUSTIF(
        P_IDINSTITUCION NUMBER,
        P_ANIO NUMBER,
        P_NUMERO NUMBER) RETURN NUMBER IS

        v_actuacion_justificada NUMBER;

    BEGIN
        BEGIN
            SELECT 1 -- Tiene alguna actuacion justificada en el rango del ciclo
                INTO v_actuacion_justificada
            FROM SCS_ACTUACIONASISTENCIA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND ANIO = P_ANIO
                AND NUMERO = P_NUMERO
                AND NVL(ANULACION,0) = 0 -- no anuladas
                AND FECHAJUSTIFICACION IS NOT NULL
                AND TRUNC(FECHAJUSTIFICACION) BETWEEN V_DATOS_FACTURACION.FECHADESDE AND V_DATOS_FACTURACION.FECHAHASTA
                AND DIADESPUES='N' -- No es dia despues (fuera de guardia)
                AND VALIDADA='1'
                AND ROWNUM=1;

            EXCEPTION
                WHEN OTHERS THEN
                    v_actuacion_justificada := 0;
        END;

        RETURN v_actuacion_justificada;
  END FUNC_EXISTE_ACTUACION_JUSTIF;    

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_AS                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de asistencias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la actuacion          NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_AS(P_IDINSTITUCION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE,
                                   CONTADOR        NUMBER,
                                   SELECT1         VARCHAR2,
                                   SELECT2         VARCHAR2,
                                   P_TIPODOBLA     VARCHAR2,
                                   P_TOTALASIST    OUT NUMBER,
                                   P_CODRETORNO    OUT VARCHAR2,
                                   P_DATOSERROR    OUT VARCHAR2) IS
    V_CURSOR             INTEGER; -- Cursor donde se ejecuta la sentencia SQL
    V_DUMMY              INTEGER;
    V_IDINSTITUCION      NUMBER;
    V_IDANIO             NUMBER;
    V_IDNUMERO           NUMBER;
    V_FACTURADO          NUMBER;
    c_asistenciastotales varchar2(4000);
    v_totalAsist         NUMBER;

  begin

    c_asistenciastotales := SELECT1 || ' UNION ' || SELECT2;

    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_IDINSTITUCION@',
                                    P_IDINSTITUCION);
    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_IDTURNO@',
                                    P_IDTURNO);
    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_IDGUARDIA@',
                                    P_IDGUARDIA);
    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_IDPERSONA@',
                                    P_IDPERSONA);
    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_FECHAINICIO@',
                                    P_FECHAINICIO);
    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_FECHAFIN@',
                                    P_FECHAFIN);
    -- RGG SUPERCAMBIO FACTURACION Estamos en un proceso de PAGAGUARDIA por lo que se usa ese valor
    c_asistenciastotales := replace(c_asistenciastotales,
                                    '@P_DIASAPLICABLES@',
                                    V_CONFIG_GUARDIA.DIASPAGAGUARDIA);

    V_CURSOR := DBMS_SQL.OPEN_CURSOR;

    /* Se analiza la consulta */

    DBMS_SQL.PARSE(V_CURSOR, c_asistenciastotales, DBMS_SQL.V7);

    /* Define las variables de salida */

    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDANIO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 3, V_IDNUMERO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 4, V_FACTURADO);

    /* Ejecuta la orden. No nos preocupa el valor devuelto, */
    /* pero necesitamos declarar una variable para el       */

    v_totalAsist := 0;
    V_DUMMY      := DBMS_SQL.EXECUTE(V_CURSOR);
    LOOP
      /* Extrae las filas y las pone en el bufer, y tambien */
      /* comprueba la condicion de salida del bucle         */

      V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
      IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

        EXIT;
      END IF;
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDANIO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 3, V_IDNUMERO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 4, V_FACTURADO);
      /* Insertamos los datos calculados en la matriz */

      -- RGG cambio para apuntar solo las NO facturadas, pero sumar todas
      if (V_FACTURADO <> Pkg_Siga_Constantes.DB_TRUE_A) then
        -- RGG solo aumento si voy a apuntar
        IND_AS := IND_AS + 1;

        M_APUNTE_AS(IND_AS).IDINSTITUCION := V_IDINSTITUCION;
        M_APUNTE_AS(IND_AS).ANIO := V_IDANIO;
        M_APUNTE_AS(IND_AS).NUMERO := V_IDNUMERO;
        M_APUNTE_AS(IND_AS).FACTURADO := V_FACTURADO;
        IF (P_TIPODOBLA='Ac') THEN
           M_APUNTE_AS(IND_AS).MOTIVO := 44; -- GAc
        ELSE
           M_APUNTE_AS(IND_AS).MOTIVO := 1; -- GAs
        END IF;
        M_APUNTE_AS(IND_AS).IMPORTE := 0;
        M_APUNTE_AS(IND_AS).CONTADOR := CONTADOR;
      end if;

      v_totalAsist := v_totalAsist + 1;
    end loop;

    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    P_TOTALASIST := v_totalAsist;
    P_DATOSERROR := 'PROCEDURE PROC_CARGA_M_APUNTE_AS: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);
/*
  EXCEPTION
    when others then
      DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
*/
  END PROC_CARGA_M_APUNTE_AS;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_ASTIPO_CG                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de asistencias
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la actuacion          NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
    PROCEDURE PROC_CARGA_M_APUNTE_ASTIPO_CG(
        P_IDINSTITUCION NUMBER,
        P_IDTURNO       NUMBER,
        P_IDGUARDIA     NUMBER,
        P_IDPERSONA     NUMBER,
        P_FECHAINICIO   DATE,
        P_FECHAFIN      DATE,
        CONTADOR        NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

        V_CURSOR             INTEGER; -- Cursor donde se ejecuta la sentencia SQL
        V_DUMMY              INTEGER;
        V_IDINSTITUCION      NUMBER;
        V_IDANIO             NUMBER;
        V_IDNUMERO           NUMBER;
        V_FACTURADO          NUMBER;
        c_asistenciastotales varchar2(4000);

    begin

        c_asistenciastotales := c_obtener_asistenciasUG;

        c_asistenciastotales := replace(c_asistenciastotales,'@P_IDINSTITUCION@',P_IDINSTITUCION);
        c_asistenciastotales := replace(c_asistenciastotales,'@P_IDTURNO@',P_IDTURNO);
        c_asistenciastotales := replace(c_asistenciastotales,'@P_IDGUARDIA@',P_IDGUARDIA);
        c_asistenciastotales := replace(c_asistenciastotales,'@P_IDPERSONA@',P_IDPERSONA);
        c_asistenciastotales := replace(c_asistenciastotales,'@P_FECHAINICIO@',P_FECHAINICIO);
        c_asistenciastotales := replace(c_asistenciastotales,'@P_FECHAFIN@',P_FECHAFIN);
        -- RGG SUPERCAMBIO FACTURACION Estamos en un proceso de NO PAGAGUARDIA por lo que se usa ese valor
        c_asistenciastotales := replace(c_asistenciastotales,'@P_DIASAPLICABLES@',V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA);

    V_CURSOR := DBMS_SQL.OPEN_CURSOR;

    /* Se analiza la consulta */

    DBMS_SQL.PARSE(V_CURSOR, c_asistenciastotales, DBMS_SQL.V7);

    /* Define las variables de salida */

    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDANIO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 3, V_IDNUMERO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 4, V_FACTURADO);

    /* Ejecuta la orden. No nos preocupa el valor devuelto, */
    /* pero necesitamos declarar una variable para el       */

    V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);
    LOOP
      /* Extrae las filas y las pone en el bufer, y tambien */
      /* comprueba la condicion de salida del bucle         */

      V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
      IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

        EXIT;
      END IF;
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDANIO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 3, V_IDNUMERO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 4, V_FACTURADO);
      IND_AS := IND_AS + 1;
      /* Insertamos los datos calculados en la matriz */
      M_APUNTE_AS(IND_AS).IDINSTITUCION := V_IDINSTITUCION;
      M_APUNTE_AS(IND_AS).ANIO := V_IDANIO;
      M_APUNTE_AS(IND_AS).NUMERO := V_IDNUMERO;
      M_APUNTE_AS(IND_AS).FACTURADO := V_FACTURADO;
-- RGG minicambio      M_APUNTE_AS(IND_AS).MOTIVO := 5; -- As
      M_APUNTE_AS(IND_AS).MOTIVO := 7; -- Ac
      M_APUNTE_AS(IND_AS).IMPORTE := 0;
      M_APUNTE_AS(IND_AS).CONTADOR := CONTADOR;

    end loop;

    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    P_DATOSERROR := 'PROCEDURE PROC_CARGA_M_APUNTE_AS: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);

  EXCEPTION
    when others then
      DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;

  END PROC_CARGA_M_APUNTE_ASTIPO_CG;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_AC                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la asistencia         NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_ACTIPO_CG(P_IDINSTITUCION NUMBER,
                                          P_IDTURNO       NUMBER,
                                          P_IDGUARDIA     NUMBER,
                                          P_IDPERSONA     NUMBER,
                                          P_FECHAINICIO   DATE,
                                          P_FECHAFIN      DATE,
                                          CONTADOR        NUMBER,
                                          SELECT1         VARCHAR2,
                                          SELECT2         VARCHAR2,
                                          IDTIPOASIST     number,
                                          MOTIVO          Number,
                                          V_COSTEFIJO     OUT NUMBER,
                                          V_CODRETORNO    OUT VARCHAR2,
                                          V_DATOSERROR    OUT VARCHAR2) IS
    V_CURSOR        INTEGER; -- Cursor donde se ejecuta la sentencia SQL
    V_DUMMY         INTEGER;
    V_IDINSTITUCION NUMBER;
    V_IDANIO        NUMBER;
    V_IDNUMERO      NUMBER;
    V_FACTURADO     NUMBER;

    c_actuacionestotales varchar2(4000);
    COSTEFIJO            NUMBER := 0;

  begin

    c_actuacionestotales := SELECT1 || ' UNION ' || SELECT2;

    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDINSTITUCION@',
                                    P_IDINSTITUCION);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDTURNO@',
                                    P_IDTURNO);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDGUARDIA@',
                                    P_IDGUARDIA);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDPERSONA@',
                                    P_IDPERSONA);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAINICIO@',
                                    P_FECHAINICIO);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAFIN@',
                                    P_FECHAFIN);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAINIFACT@',
                                    V_DATOS_FACTURACION.FECHADESDE);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAFINFACT@',
                                    V_DATOS_FACTURACION.FECHAHASTA);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDTIPOASIST@',
                                    IDTIPOASIST);
    -- RGG SUPERCAMBIO FACTURACION Estamos en un proceso de NO PAGAGUARDIA por lo que se usa ese valor
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_DIASAPLICABLES@',
                                    V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA);

    V_CURSOR := DBMS_SQL.OPEN_CURSOR;

    /* Se analiza la consulta */

    DBMS_SQL.PARSE(V_CURSOR, c_actuacionestotales, DBMS_SQL.V7);

    /* Define las variables de salida */

    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDANIO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 3, V_IDNUMERO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 4, V_IDACTUACION);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 5, V_FACTURADO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 6, V_COSTEFIJO);

    /* Ejecuta la orden. No nos preocupa el valor devuelto, */
    /* pero necesitamos declarar una variable para el       */

    V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);

    LOOP

      /* Extrae las filas y las pone en el bufer, y tambien */
      /* comprueba la condicion de salida del bucle         */

      V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
      IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

        EXIT;
      END IF;
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDANIO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 3, V_IDNUMERO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 4, V_IDACTUACION);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 5, V_FACTURADO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 6, V_COSTEFIJO);
      IF V_FACTURADO <> Pkg_Siga_Constantes.DB_TRUE_A THEN
        -- Si esta facturado no tenemos en cuenta los costes fijos
        COSTEFIJO := COSTEFIJO + V_COSTEFIJO;

        IND_AC := IND_AC + 1;
        /* Insertamos los datos calculados en la matriz */
        M_APUNTE_AC(IND_AC).IDINSTITUCION := V_IDINSTITUCION;
        M_APUNTE_AC(IND_AC).ANIO := V_IDANIO;
        M_APUNTE_AC(IND_AC).NUMERO := V_IDNUMERO;
        M_APUNTE_AC(IND_AC).IDACTUACION := V_IDACTUACION;
        M_APUNTE_AC(IND_AC).FACTURADO := V_FACTURADO;
        M_APUNTE_AC(IND_AC).MOTIVO := MOTIVO;
        M_APUNTE_AC(IND_AC).IMPORTE := 0;
        M_APUNTE_AC(IND_AC).CONTADOR := CONTADOR;
        M_APUNTE_AC(IND_AC).COSTEFIJO := V_COSTEFIJO;

      END IF;


    end loop;

    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    V_DATOSERROR := 'PROCEDURE PROC_CARGA_M_APUNTE_AC_CG: ha finalizado correctamente.';
    V_CODRETORNO := TO_CHAR(0);
    V_COSTEFIJO  := COSTEFIJO;

  EXCEPTION
    when others then
      DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
      V_CODRETORNO := TO_CHAR(SQLCODE);
      V_DATOSERROR := V_DATOSERROR || ' ' || SQLERRM;

  END PROC_CARGA_M_APUNTE_ACTIPO_CG;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_AC                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la asistencia         NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
  PROCEDURE PROC_CARGA_M_APUNTE_AC(P_IDINSTITUCION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE,
                                   CONTADOR        NUMBER,
                                   SELECT1         VARCHAR2,
                                   SELECT2         VARCHAR2,
                                   P_TIPODOBLA     VARCHAR2,
                                   V_COSTEFIJO     OUT NUMBER,
                                   P_TOTALACTUA    OUT NUMBER,
                                   V_CODRETORNO    OUT VARCHAR2,
                                   V_DATOSERROR    OUT VARCHAR2) IS
    V_CURSOR        INTEGER; -- Cursor donde se ejecuta la sentencia SQL
    V_DUMMY         INTEGER;
    V_IDINSTITUCION NUMBER;
    V_IDANIO        NUMBER;
    V_IDNUMERO      NUMBER;
    V_FACTURADO     NUMBER;

    c_actuacionestotales varchar2(4000);
    COSTEFIJO            NUMBER := 0;

    v_totalActua NUMBER;

  begin

    c_actuacionestotales := SELECT1 || ' UNION ' || SELECT2;

    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDINSTITUCION@',
                                    P_IDINSTITUCION);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDTURNO@',
                                    P_IDTURNO);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDGUARDIA@',
                                    P_IDGUARDIA);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_IDPERSONA@',
                                    P_IDPERSONA);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAINICIO@',
                                    P_FECHAINICIO);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAFIN@',
                                    P_FECHAFIN);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAINIFACT@',
                                    V_DATOS_FACTURACION.FECHADESDE);
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_FECHAFINFACT@',
                                    V_DATOS_FACTURACION.FECHAHASTA);
    -- RGG SUPERCAMBIO FACTURACION Estamos en un proceso de PAGAGUARDIA por lo que se usa ese valor
    c_actuacionestotales := replace(c_actuacionestotales,
                                    '@P_DIASAPLICABLES@',
                                    V_CONFIG_GUARDIA.DIASPAGAGUARDIA);

    V_CURSOR := DBMS_SQL.OPEN_CURSOR;

    /* Se analiza la consulta */

    DBMS_SQL.PARSE(V_CURSOR, c_actuacionestotales, DBMS_SQL.V7);

    /* Define las variables de salida */

    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDANIO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 3, V_IDNUMERO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 4, V_IDACTUACION);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 5, V_FACTURADO);
    DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 6, V_COSTEFIJO);

    /* Ejecuta la orden. No nos preocupa el valor devuelto, */
    /* pero necesitamos declarar una variable para el       */

    v_totalActua := 0;
    V_DUMMY      := DBMS_SQL.EXECUTE(V_CURSOR);

    LOOP

      /* Extrae las filas y las pone en el bufer, y tambien */
      /* comprueba la condicion de salida del bucle         */

      V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
      IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN

        EXIT;
      END IF;
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDANIO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 3, V_IDNUMERO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 4, V_IDACTUACION);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 5, V_FACTURADO);
      DBMS_SQL.COLUMN_VALUE(V_CURSOR, 6, V_COSTEFIJO);

      /* Insertamos los datos calculados en la matriz */

      -- RGG cambio para apuntar solo las NO facturadas, pero sumar todas
      if (V_FACTURADO <> Pkg_Siga_Constantes.DB_TRUE_A) then
        -- RGG solo aumento el contador si apunto
        IND_AC := IND_AC + 1;

        M_APUNTE_AC(IND_AC).IDINSTITUCION := V_IDINSTITUCION;
        M_APUNTE_AC(IND_AC).ANIO := V_IDANIO;
        M_APUNTE_AC(IND_AC).NUMERO := V_IDNUMERO;
        M_APUNTE_AC(IND_AC).IDACTUACION := V_IDACTUACION;
        M_APUNTE_AC(IND_AC).FACTURADO := V_FACTURADO;
        IF (P_TIPODOBLA='Ac') THEN
           M_APUNTE_AC(IND_AC).MOTIVO := 44; -- GAc
        ELSE
           M_APUNTE_AC(IND_AC).MOTIVO := 1; -- GAs
        END IF;
        M_APUNTE_AC(IND_AC).IMPORTE := 0;
        M_APUNTE_AC(IND_AC).CONTADOR := CONTADOR;
        M_APUNTE_AC(IND_AC).COSTEFIJO := V_COSTEFIJO;
        -- Si esta facturado no tenemos en cuenta los costes fijos
        COSTEFIJO := COSTEFIJO + V_COSTEFIJO;
      end if;

      v_totalActua := v_totalActua + 1;
    end loop;

    DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    P_TOTALACTUA := v_totalActua;
    V_DATOSERROR := 'PROCEDURE PROC_CARGA_M_APUNTE_AC_CG: ha finalizado correctamente.';
    V_CODRETORNO := TO_CHAR(0);
    V_COSTEFIJO  := COSTEFIJO;
/*
  EXCEPTION
    when others then
      DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
      V_CODRETORNO := TO_CHAR(SQLCODE);
      V_DATOSERROR := V_DATOSERROR || ' ' || SQLERRM;
*/
  END PROC_CARGA_M_APUNTE_AC;

  /****************************************************************************************************************/
  /* Nombre:        PROC_CARGA_M_APUNTE_ACNTIPO_CG                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar la matriz de apunte de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION          IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO                IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA              IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA              IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO            IN     Fecha Inicio  de la CG                                           DATE,
  /* P_FECHAFIN               IN     Fecha Fin  de la CG                                              DATE,
  /* CONTADOR                 IN     Indica a que cabecera de guardia pertenece la asistencia         NUMBER
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/
    PROCEDURE PROC_CARGA_M_APUNTE_ACNTIPO_CG(
        P_IDINSTITUCION NUMBER,
        P_IDTURNO       NUMBER,
        P_IDGUARDIA     NUMBER,
        P_IDPERSONA     NUMBER,
        P_FECHAINICIO   DATE,
        P_FECHAFIN      DATE,
        CONTADOR        NUMBER,
        SELECT1         VARCHAR2,
        SELECT2         VARCHAR2,
        MOTIVO          Number,
        V_COSTEFIJO     OUT NUMBER,
        V_CODRETORNO    OUT VARCHAR2,
        V_DATOSERROR    OUT VARCHAR2) IS

    V_CURSOR        INTEGER; -- Cursor donde se ejecuta la sentencia SQL
    V_DUMMY         INTEGER;
    V_IDINSTITUCION NUMBER;
    V_IDANIO        NUMBER;
    V_IDNUMERO      NUMBER;
    V_FACTURADO     NUMBER;
    c_actuacionestotales varchar2(4000);
    COSTEFIJO            NUMBER := 0;

    BEGIN

        c_actuacionestotales := SELECT1 || ' UNION ' || SELECT2;
        c_actuacionestotales := replace(c_actuacionestotales, '@P_IDINSTITUCION@', P_IDINSTITUCION);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_IDTURNO@', P_IDTURNO);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_IDGUARDIA@', P_IDGUARDIA);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_IDPERSONA@', P_IDPERSONA);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_FECHAINICIO@', P_FECHAINICIO);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_FECHAFIN@', P_FECHAFIN);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_FECHAINIFACT@', V_DATOS_FACTURACION.FECHADESDE);
        c_actuacionestotales := replace(c_actuacionestotales, '@P_FECHAFINFACT@', V_DATOS_FACTURACION.FECHAHASTA);
        -- RGG SUPERCAMBIO FACTURACION Estamos en un proceso de NO PAGAGUARDIA por lo que se usa ese valor
        c_actuacionestotales := replace(c_actuacionestotales, '@P_DIASAPLICABLES@', V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA);

        V_CURSOR := DBMS_SQL.OPEN_CURSOR;

        /* Se analiza la consulta */
        DBMS_SQL.PARSE(V_CURSOR, c_actuacionestotales, DBMS_SQL.V7);

        /* Define las variables de salida */
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDANIO);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 3, V_IDNUMERO);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 4, V_IDACTUACION);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 5, V_FACTURADO);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 6, V_COSTEFIJO);

        /* Ejecuta la orden. No nos preocupa el valor devuelto, pero necesitamos declarar una variable para el       */
        V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);

        LOOP

            /* Extrae las filas y las pone en el bufer, y tambien  comprueba la condicion de salida del bucle         */
            V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
            IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN
                EXIT;
            END IF;

            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDANIO);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 3, V_IDNUMERO);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 4, V_IDACTUACION);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 5, V_FACTURADO);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 6, V_COSTEFIJO);

            IF V_FACTURADO <> Pkg_Siga_Constantes.DB_TRUE_N THEN
                -- Si esta facturado no tenemos en cuenta los costes fijos
                COSTEFIJO := COSTEFIJO + V_COSTEFIJO;

                /* Insertamos los datos calculados en la matriz */
                IND_AC := IND_AC + 1;
                M_APUNTE_AC(IND_AC).IDINSTITUCION := V_IDINSTITUCION;
                M_APUNTE_AC(IND_AC).ANIO := V_IDANIO;
                M_APUNTE_AC(IND_AC).NUMERO := V_IDNUMERO;
                M_APUNTE_AC(IND_AC).IDACTUACION := V_IDACTUACION;
                M_APUNTE_AC(IND_AC).FACTURADO := V_FACTURADO;
                M_APUNTE_AC(IND_AC).MOTIVO := MOTIVO;
                M_APUNTE_AC(IND_AC).IMPORTE := 0;
                M_APUNTE_AC(IND_AC).CONTADOR := CONTADOR;
                M_APUNTE_AC(IND_AC).COSTEFIJO := V_COSTEFIJO;
            END IF;
        END LOOP;

        DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
        V_DATOSERROR := 'PROCEDURE PROC_CARGA_M_APUNTE_AC_CG: ha finalizado correctamente.';
        V_CODRETORNO := TO_CHAR(0);
        V_COSTEFIJO  := COSTEFIJO;

        EXCEPTION
            WHEN OTHERS THEN
            DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
            V_CODRETORNO := TO_CHAR(SQLCODE);
            V_DATOSERROR := V_DATOSERROR || ' ' || SQLERRM;
    END PROC_CARGA_M_APUNTE_ACNTIPO_CG;

  /***************************************************************************/
  /* Nombre:      PROC_FACT_GUARDIA_DOBLAASIST                               */
  /* Descripcion: Este procedimiento se encarga de cargar las tablas de      */
  /*              memoria cuando facturamos por Guardia y se dobla por       */
  /*              asistencia.                                                */
  /*                                                                         */
  /* Parametros             IN/OUT Descripcion                 Tipo de Datos */
  /* ---------------------- ------ --------------------------- ------------- */
  /* indiceMatrizFacturable  IN    Indica la posicion del      NUMBER        */
  /*                               registro actual en la                     */
  /*                               matriz facturable                         */
  /* P_CODRETORNO            OUT   Devuelve 0 en caso de que   VARCHAR2(10)  */
  /*                               la ejecucion haya sido OK.                */
  /*                               En caso de error devuelve el              */
  /*                               codigo de error Oracle                    */
  /*                               correspondiente.                          */
  /* P_DATOSERROR            OUT   Devuelve null en caso de    VARCHAR2(200) */
  /*                               que la ejecucion haya sido                */
  /*                               OK.                                       */
  /*                               En caso de error devuelve el              */
  /*                               mensaje de error Oracle                   */
  /*                               correspondiente.                          */
  /*                                                                         */
  /* Fecha       Autor Modificacion  Descripcion Modificacion                */
  /* ----------  ------------------  --------------------------------------- */
  /* 25/04/2006  Pilar Duran Munoz   Creacion                                */
  /* 23-07-2008  Adrian Ayala Gomez  Incorporacion de facturacion por Unidad */
  /*                                 de Guardia                              */
  /*                                                                         */
  /***************************************************************************/
  PROCEDURE PROC_FACT_GUARDIA_DOBLAASIST(indiceMatrizFacturable NUMBER,
                                         P_CODRETORNO           OUT VARCHAR2,
                                         P_DATOSERROR           OUT VARCHAR2) IS
  BEGIN
    if (V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA = '1') then
      proc_fact_guardia_doblaas_agru(indiceMatrizFacturable);
    else
      proc_fact_guardia_doblaas_noag(indiceMatrizFacturable);
    end if;

    P_DATOSERROR := 'PROCEDURE PROC_FACT_GUARDIA_DOBLAASIST: ha finalizado correctamente.';
    P_CODRETORNO := to_char(0);

  EXCEPTION
    when others then
      P_CODRETORNO := to_char(sqlcode);
      P_DATOSERROR := P_DATOSERROR || ' ' || sqlerrm;
  END PROC_FACT_GUARDIA_DOBLAASIST;

    PROCEDURE PROC_FACT_GUARDIA_DOBLAAS_AGRU(indiceMatrizFacturable NUMBER) IS    
        contador NUMBER;
        v_totalAsist NUMBER;
        v_totalActua NUMBER;

    BEGIN          
        contador := 0; --inicializando contador
    
        --POR CADA UG solo escribir en tabla (no se calcula nada por dias)
        FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA(
            M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
            V_CONFIG_GUARDIA.DIASPAGAGUARDIA
        ) LOOP
            contador := contador + 1;
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDINSTITUCION := M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION;
            M_APUNTE_UG(IND_UG).IDTURNO := M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
            M_APUNTE_UG(IND_UG).FACTURADO := M_CG_FACTURABLE(indiceMatrizFacturable).FACTURADO;
            M_APUNTE_UG(IND_UG).CONTADOR := contador;
            M_APUNTE_UG(IND_UG).COSTEFIJO := 0;
            M_APUNTE_UG(IND_UG).IMPORTE := 0;
            M_APUNTE_UG(IND_UG).MOTIVO := 1; --GAs
        END LOOP;

        --1. CARGAR EN MATRICES LAS ASISTENCIAS Y ACTUACIONES DE CG  
        contador := 1; --inicializando contador

        -- cargando la matriz de apunte M_APUNTE_AS
        PROC_CARGA_M_APUNTE_AS(
            M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
            contador,
            c_obtener_asistenciasFact,
            c_obtener_asistenciasnuevas,
            'As', -- apuntes GAs
            v_totalAsist,
            V_CODRETORNO2,
            V_DATOSERROR2);

        --cargando la matriz de apunte M_APUNTE_AC
        PROC_CARGA_M_APUNTE_AC(
            M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
            contador,
            c_obtener_actuacionesFact,
            c_obtener_actuacionesnuevas,
            'As', -- apuntes GAs
            V_COSTEFIJO,
            v_totalActua,
            V_CODRETORNO2,
            V_DATOSERROR2);

        --2. GENERAR APUNTE DE CG TENIENDO EN CUENTA LOS DIFERENTES MOTIVOS
        -- Por cada recorrido de la matriz de cabecera facturable solo habra un registro en la matriz de apunte de CG por eso el indice siempre sera 1
        IND_CG := 1;
        M_APUNTE_CG(IND_CG).IDINSTITUCION := M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION;
        M_APUNTE_CG(IND_CG).IDTURNO := M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO;
        M_APUNTE_CG(IND_CG).IDGUARDIA := M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA;
        M_APUNTE_CG(IND_CG).IDPERSONA := M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
        M_APUNTE_CG(IND_CG).FECHAINICIO := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO;
        M_APUNTE_CG(IND_CG).FECHAFIN := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN;
        M_APUNTE_CG(IND_CG).FACTURADO := M_CG_FACTURABLE(indiceMatrizFacturable).FACTURADO;
        M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG-';
        M_APUNTE_CG(IND_CG).CONTADOR := contador;
        M_APUNTE_CG(IND_CG).COSTEFIJO := V_COSTEFIJO;

        --INI: Cambio facturacion guardias inactivas catalanes de VG --
        IF (v_totalAsist = 0 
            AND V_CONFIG_GUARDIA.CONSEJOINSTITUCION = C_CATALAN 
            AND V_CONFIG_GUARDIA.ESGUARDIAVG = '1') THEN
            M_APUNTE_CG(IND_CG).IMPORTE := C_IMPORTE_GUARDIA_INACTIVA - M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO;

            IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO = 0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 53; --GAsMin
            ELSIF (M_APUNTE_CG(IND_CG).IMPORTE <= 0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 57; --GAsMinNoDev+
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := 59; --GAsMin+
            END IF;
            --FIN: Cambio facturacion guardias inactivas catalanes de VG --
        
        --3. COMPROBAR SI SE DOBLA O NO EN CG    
        ELSIF (v_totalAsist <= V_CONFIG_GUARDIA.NUMASISTENCIASDOBLA) THEN --No se dobla:      
            M_APUNTE_CG(IND_CG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIA - M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO;

            IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO = 0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 1; --GAs
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := 39; --GSNoDevAs+
            END IF;
    
        ELSE --Se dobla:
            M_APUNTE_CG(IND_CG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA - M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO;

            IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO = 0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 2; --GDAs
            ELSIF (M_APUNTE_CG(IND_CG).IMPORTE <= 0) THEN
                M_APUNTE_CG(IND_CG).MOTIVO := 14; --GDNoDevAs+
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := 15; --GDAs+
            END IF;
        END IF;

        -- RGG cambio para importes negativos
        IF (M_APUNTE_CG(IND_CG).IMPORTE < 0) THEN
            M_APUNTE_CG(IND_CG).IMPORTE := 0;
        END IF;
    END PROC_FACT_GUARDIA_DOBLAAS_AGRU;

    PROCEDURE PROC_FACT_GUARDIA_DOBLAAS_NOAG(indiceMatrizFacturable NUMBER) IS
        totalImporteUGs NUMBER;
        costeFijoAc_CG NUMBER;
        contador NUMBER;
        v_totalasist NUMBER;
        v_totalActua NUMBER;

    BEGIN
        --inicializando variables
        contador := 0;
        totalImporteUGs := 0;
        costeFijoAc_CG := 0;

        --1. POR CADA UG
        FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA(
            M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
            V_CONFIG_GUARDIA.DIASPAGAGUARDIA
        ) LOOP
            contador := contador + 1;

            --2. CARGAR EN MATRICES LAS ASISTENCIAS Y ACTUACIONES DE UG
            --cargando la matriz de apuntes de asistencias
            PROC_CARGA_M_APUNTE_AS(
                M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                V_UNIDADES_GUARDIA.FECHAFIN,
                V_UNIDADES_GUARDIA.FECHAFIN, --solo de la UG
                contador,
                c_obtener_asistenciasFact,
                c_obtener_asistenciasnuevas,
                'As', -- apuntes GAs
                v_totalasist,
                V_CODRETORNO2,
                V_DATOSERROR2);

            --cargando la matriz de apuntes de actuaciones
            PROC_CARGA_M_APUNTE_AC(
                M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                V_UNIDADES_GUARDIA.FECHAFIN,
                V_UNIDADES_GUARDIA.FECHAFIN, --solo de la UG
                contador,
                c_obtener_actuacionesFact,
                c_obtener_actuacionesnuevas,
                'As', -- apuntes GAs
                V_COSTEFIJO,
                v_totalActua,
                V_CODRETORNO2,
                V_DATOSERROR2);

            --3. GENERAR APUNTE DE UG TENIENDO EN CUENTA LOS DIFERENTES MOTIVOS
            --Acumulado de todos los costes fijos de actuaciones
            costeFijoAc_CG := costeFijoAc_CG + V_COSTEFIJO;

            --obteniendo la matriz donde se hacen los apuntes
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDINSTITUCION := M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION;
            M_APUNTE_UG(IND_UG).IDTURNO := M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
            M_APUNTE_UG(IND_UG).FACTURADO := M_CG_FACTURABLE(indiceMatrizFacturable).FACTURADO;
            M_APUNTE_UG(IND_UG).CONTADOR := contador;
            M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJO;

            --comprobando que importe apuntar y motivo
            --INI: Cambio facturacion guardias inactivas catalanes de VG --
            IF (v_totalasist = 0 
                And V_CONFIG_GUARDIA.CONSEJOINSTITUCION = C_CATALAN 
                And V_CONFIG_GUARDIA.ESGUARDIAVG = '1') THEN
                M_APUNTE_UG(IND_UG).IMPORTE := C_IMPORTE_GUARDIA_INACTIVA - V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

                IF (V_UNIDADES_GUARDIA.IMPORTEFACTURADO = 0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 53; --GAsMin
                ELSIF (M_APUNTE_UG(IND_UG).IMPORTE <= 0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 57; --GAsMinNoDev+
                ELSE
                    M_APUNTE_UG(IND_UG).MOTIVO := 59; --GAsMin+
                End If;
                --FIN: Cambio facturacion guardias inactivas catalanes de VG --
                    
            ELSIF (v_totalasist <= V_CONFIG_GUARDIA.NUMASISTENCIASDOBLA) THEN --No se dobla:
                M_APUNTE_UG(IND_UG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIA - V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

                IF (V_UNIDADES_GUARDIA.IMPORTEFACTURADO = 0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 1; --GAs
                ELSE
                    M_APUNTE_UG(IND_UG).MOTIVO := 39; --GSNoDevAs+
                END IF;
                    
            ELSE --Se dobla:
                M_APUNTE_UG(IND_UG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA - V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

                IF (V_UNIDADES_GUARDIA.IMPORTEFACTURADO = 0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 2; --GDAs
                ELSIF (M_APUNTE_UG(IND_UG).IMPORTE <= 0) THEN
                    M_APUNTE_UG(IND_UG).MOTIVO := 14; --GDNoDevAs+
                ELSE
                    M_APUNTE_UG(IND_UG).MOTIVO := 15; --GDAs+
                END IF;
            END IF;

            -- RGG cambio para importes negativos
            IF (M_APUNTE_UG(IND_UG).IMPORTE < 0) THEN
                M_APUNTE_UG(IND_UG).IMPORTE := 0;
            END IF;

            --total de importes para la CG
            totalImporteUGs := totalImporteUGs + M_APUNTE_UG(IND_UG).IMPORTE;
        END LOOP;

        --4. GENERAR APUNTE GENERICO DE CG TENIENDO EN CUENTA LOS DIFERENTES MOTIVOS    
        contador := 1; --inicializando contador

        --Por cada recorrido de la matriz de cabecera facturable solo habra un registro en la matriz de apunte de CG por eso el indice siempre sera 1
        IND_CG := 1;
        M_APUNTE_CG(IND_CG).IDINSTITUCION := M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION;
        M_APUNTE_CG(IND_CG).IDTURNO := M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO;
        M_APUNTE_CG(IND_CG).IDGUARDIA := M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA;
        M_APUNTE_CG(IND_CG).IDPERSONA := M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
        M_APUNTE_CG(IND_CG).FECHAINICIO := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO;
        M_APUNTE_CG(IND_CG).FECHAFIN := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN;
        M_APUNTE_CG(IND_CG).FACTURADO := M_CG_FACTURABLE(indiceMatrizFacturable).FACTURADO;
        M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG';
        M_APUNTE_CG(IND_CG).CONTADOR := contador;
        M_APUNTE_CG(IND_CG).COSTEFIJO := costeFijoAc_CG;    
        M_APUNTE_CG(IND_CG).MOTIVO := 1;  -- GAs (Apunte generico de pago por asistencias)

        -- RGG cambio para restarlo a lo ya facturado (con control de negativos)
        IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO>0) THEN
            M_APUNTE_CG(IND_CG).MOTIVO  := 50; -- GAs+
        END IF;

        M_APUNTE_CG(IND_CG).IMPORTE := totalImporteUGs;
    END PROC_FACT_GUARDIA_DOBLAAS_NOAG;

  /***************************************************************************/
  /* Nombre:      PROC_FACT_GUARDIA_DOBLAACT                                 */
  /* Descripcion: Este procedimiento se encarga de cargar las tablas de      */
  /*              memoria cuando facturamos por Guardia y se dobla por       */
  /*              numero de actuacion.                                       */
  /*                                                                         */
  /* Parametros             IN/OUT Descripcion                 Tipo de Datos */
  /* ---------------------- ------ --------------------------- ------------- */
  /* indiceMatrizFacturable   IN   Indicador de fila en la     NUMBER        */
  /*                               matriz para obtener la                    */
  /*                               cabecera correspondiente                  */
  /* P_CODRETORNO             OUT  Devuelve 0 en caso de que   VARCHAR2(10)  */
  /*                               la ejecucion haya sido OK.                */
  /*                               En caso de error devuelve                 */
  /*                               el codigo de error Oracle                 */
  /*                               correspondiente.                          */
  /* P_DATOSERROR             OUT  Devuelve null en caso de    VARCHAR2(200) */
  /*                               que la ejecucion haya sido                */
  /*                               OK.                                       */
  /*                               En caso de error devuelve                 */
  /*                               el mensaje de error Oracle                */
  /*                               correspondiente.                          */
  /*                                                                         */
  /* Fecha       Autor Modificacion  Descripcion                             */
  /* ----------  ------------------  --------------------------------------- */
  /* 25/04/2006  Pilar Duran Munoz   Creacion                                */
  /* 24/07/2008  Adrian Ayala Gomez  Cambio para incorporar la agrupacion    */
  /*                                 por unidad de guardia o cabecera        */
  /*                                                                         */
  /***************************************************************************/
  PROCEDURE PROC_FACT_GUARDIA_DOBLAACT(indiceMatrizFacturable NUMBER,
                                       P_CODRETORNO           OUT VARCHAR2,
                                       P_DATOSERROR           OUT VARCHAR2) IS
  BEGIN
    if (V_CONFIG_GUARDIA.AGRUPARPAGAGUARDIA = '1') then
      proc_fact_guardia_doblaac_agru(indiceMatrizFacturable);
    else
      proc_fact_guardia_doblaac_noag(indiceMatrizFacturable);
    end if;

    P_DATOSERROR := 'PROCEDURE PROC_FACT_GUARDIA_DOBLAACT: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);

  EXCEPTION
    when others then
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
  END PROC_FACT_GUARDIA_DOBLAACT;

  /**/
  PROCEDURE PROC_FACT_GUARDIA_DOBLAAC_AGRU(indiceMatrizFacturable NUMBER) IS
    contador       NUMBER;

    MATRIZFACTURABLE MATRICE_CG_FACTURABLE;
    MATRIZAPUNTES    MATRICE_CG;
    MATRIZAPUNTES_UG MATRICE_UG;

    costeFijoAc  Number;
    v_totalAsist NUMBER;
    v_totalActua NUMBER;

  BEGIN

    --inicializando variables
    contador    := 0;
    contador    := contador + 1;

    --obteniendo registro actual de CG de la matriz facturable
    MATRIZFACTURABLE := M_CG_FACTURABLE(indiceMatrizFacturable);

    --
    --POR CADA UG solo escribir en tabla (no se calcula nada por dias)
    --
    FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA(MATRIZFACTURABLE.IDINSTITUCION,
                                               MATRIZFACTURABLE.IDTURNO,
                                               MATRIZFACTURABLE.IDGUARDIA,
                                               MATRIZFACTURABLE.IDPERSONA,
                                               MATRIZFACTURABLE.FECHAINICIO,
                                               V_CONFIG_GUARDIA.DIASPAGAGUARDIA) LOOP
      contador := contador + 1;

      --obteniendo la matriz donde se hacen los apuntes
      IND_UG := IND_UG + 1;
      M_APUNTE_UG(IND_UG).IDINSTITUCION := MATRIZFACTURABLE.IDINSTITUCION;
      MATRIZAPUNTES_UG := M_APUNTE_UG(IND_UG);

      --haciendo apuntes
      MATRIZAPUNTES_UG.IDINSTITUCION        := MATRIZFACTURABLE.IDINSTITUCION;
      MATRIZAPUNTES_UG.IDTURNO              := MATRIZFACTURABLE.IDTURNO;
      MATRIZAPUNTES_UG.IDGUARDIA            := MATRIZFACTURABLE.IDGUARDIA;
      MATRIZAPUNTES_UG.IDPERSONA            := MATRIZFACTURABLE.IDPERSONA;
      MATRIZAPUNTES_UG.FECHAINICIO          := MATRIZFACTURABLE.FECHAINICIO;
      MATRIZAPUNTES_UG.FECHAFIN             := V_UNIDADES_GUARDIA.FECHAFIN;
      MATRIZAPUNTES_UG.FACTURADO            := MATRIZFACTURABLE.FACTURADO;
      MATRIZAPUNTES_UG.CONTADOR             := contador;
      MATRIZAPUNTES_UG.COSTEFIJO            := 0;
      MATRIZAPUNTES_UG.IMPORTE              := 0;
      MATRIZAPUNTES_UG.MOTIVO               := 44; --GAc

      --guardando los registros modificados
      M_APUNTE_UG(IND_UG) := MATRIZAPUNTES_UG;
    END LOOP;

    --inicializando contador
    contador := 1;

    --cargando matriz de apuntes de asistencias de CG
    proc_carga_m_apunte_as(MATRIZFACTURABLE.IDINSTITUCION,
                           MATRIZFACTURABLE.IDTURNO,
                           MATRIZFACTURABLE.IDGUARDIA,
                           MATRIZFACTURABLE.IDPERSONA,
                           MATRIZFACTURABLE.FECHAINICIO,
                           MATRIZFACTURABLE.FECHAFIN,
                           contador,
                           c_obtener_asistenciasFact,
                           c_obtener_asistenciasnuevas,
                           'Ac', -- apuntes GAc
                           v_totalAsist,
                           V_CODRETORNO2,
                           V_DATOSERROR2);

    --cargando matriz de apuntes de actuaciones de CG
    PROC_CARGA_M_APUNTE_AC(MATRIZFACTURABLE.IDINSTITUCION,
                           MATRIZFACTURABLE.IDTURNO,
                           MATRIZFACTURABLE.IDGUARDIA,
                           MATRIZFACTURABLE.IDPERSONA,
                           MATRIZFACTURABLE.FECHAINICIO,
                           MATRIZFACTURABLE.FECHAFIN,
                           contador,
                           c_obtener_actuacionesFact,
                           c_obtener_actuacionesnuevas,
                           'Ac', -- apuntes GAc
                           V_COSTEFIJO,
                           v_totalActua,
                           V_CODRETORNO2,
                           V_DATOSERROR2);

    --Acumulado de los costes fijos de actuaciones
    costeFijoAc := V_COSTEFIJO;

    --Por cada recorrido de la matriz de cabecera facturable solo habra un
    --registro en la matriz de apunte de CG por eso el indice siempre sera 1.
    IND_CG := 1;

    --obteniendo registro actual de CG de la matriz de apuntes
    M_APUNTE_CG(IND_CG).IDINSTITUCION := MATRIZFACTURABLE.IDINSTITUCION;
    MATRIZAPUNTES := M_APUNTE_CG(IND_CG);

    --haciendo apuntes
    MATRIZAPUNTES.IDINSTITUCION        := MATRIZFACTURABLE.IDINSTITUCION;
    MATRIZAPUNTES.IDTURNO              := MATRIZFACTURABLE.IDTURNO;
    MATRIZAPUNTES.IDGUARDIA            := MATRIZFACTURABLE.IDGUARDIA;
    MATRIZAPUNTES.IDPERSONA            := MATRIZFACTURABLE.IDPERSONA;
    MATRIZAPUNTES.FECHAINICIO          := MATRIZFACTURABLE.FECHAINICIO;
    MATRIZAPUNTES.FACTURADO            := MATRIZFACTURABLE.FACTURADO;
    MATRIZAPUNTES.CONTADOR             := contador;
    MATRIZAPUNTES.COSTEFIJO            := costeFijoAc;
    MATRIZAPUNTES.IDTIPOAPUNTE         := 'CG-';

    --comprobando que importe apuntar y motivo
    --INI: Cambio facturacion guardias inactivas catalanes de VG --
    If (v_totalActua = 0 And V_CONFIG_GUARDIA.CONSEJOINSTITUCION = C_CATALAN And V_CONFIG_GUARDIA.ESGUARDIAVG = '1') Then
      MATRIZAPUNTES.IMPORTE := C_IMPORTE_GUARDIA_INACTIVA - MATRIZFACTURABLE.IMPORTEFACTURADO;

      If (MATRIZFACTURABLE.IMPORTEFACTURADO = 0) Then
        MATRIZAPUNTES.MOTIVO := 54; --GAcMin
      Elsif (MATRIZAPUNTES.IMPORTE <= 0) Then
        MATRIZAPUNTES.MOTIVO := 58; --GAcMinNoDev+
      Else
        MATRIZAPUNTES.MOTIVO := 60; --GAcMin+
      End If;
    --FIN: Cambio facturacion guardias inactivas catalanes de VG --
    Elsif (v_totalActua <= V_CONFIG_GUARDIA.NUMACTUACIONESDOBLA) then
      --No se dobla:
      MATRIZAPUNTES.IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIA -
                               MATRIZFACTURABLE.IMPORTEFACTURADO;

      if (MATRIZFACTURABLE.IMPORTEFACTURADO = 0) then
        MATRIZAPUNTES.MOTIVO := 44; --GAc
      else
        MATRIZAPUNTES.MOTIVO := 38; --GSNoDev+
      end if;
    else
      --Se dobla:
      MATRIZAPUNTES.IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA -
                               MATRIZFACTURABLE.IMPORTEFACTURADO;

      if (MATRIZFACTURABLE.IMPORTEFACTURADO = 0) then
        MATRIZAPUNTES.MOTIVO := 4; --GDAc
      elsif (MATRIZAPUNTES.IMPORTE <= 0) then
        MATRIZAPUNTES.MOTIVO := 40; --GDNoDevAc+
      else
        MATRIZAPUNTES.MOTIVO := 16; --GDAc+
      end if;
    end if;

    -- RGG: cambio de importes negativos
    if (MATRIZAPUNTES.IMPORTE<0) then
       MATRIZAPUNTES.IMPORTE := 0;
    end if;

    --guardando los registros modificados
    M_APUNTE_CG(IND_CG) := MATRIZAPUNTES;

  END PROC_FACT_GUARDIA_DOBLAAC_AGRU;

  /**/
  PROCEDURE PROC_FACT_GUARDIA_DOBLAAC_NOAG(indiceMatrizFacturable NUMBER) IS
    totalImporteUGs NUMBER;
    costeFijoAc_CG  NUMBER;
    costeFijoAc_UG  NUMBER;
    contador        NUMBER;

    MATRIZFACTURABLE MATRICE_CG_FACTURABLE;
    MATRIZAPUNTES    MATRICE_CG;
    MATRIZAPUNTES_UG MATRICE_UG;

    V_TOTALASIST NUMBER;
    V_TOTALACTUA NUMBER;

  BEGIN

    --inicializando variables
    contador        := 0;
    totalImporteUGs := 0;
    costeFijoAc_CG  := 0;

    --obteniendo registro actual de CG de la matriz facturable
    MATRIZFACTURABLE := M_CG_FACTURABLE(indiceMatrizFacturable);

    --
    --1. POR CADA UG
    --
    FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA(MATRIZFACTURABLE.IDINSTITUCION,
                                               MATRIZFACTURABLE.IDTURNO,
                                               MATRIZFACTURABLE.IDGUARDIA,
                                               MATRIZFACTURABLE.IDPERSONA,
                                               MATRIZFACTURABLE.FECHAINICIO,
                                               V_CONFIG_GUARDIA.DIASPAGAGUARDIA) LOOP
      contador := contador + 1;

      --
      --2. CARGAR EN MATRICES LAS ASISTENCIAS Y ACTUACIONES DE UG
      --
      --cargando la matriz de apuntes de asistencias
      PROC_CARGA_M_APUNTE_AS(MATRIZFACTURABLE.IDINSTITUCION,
                             MATRIZFACTURABLE.IDTURNO,
                             MATRIZFACTURABLE.IDGUARDIA,
                             MATRIZFACTURABLE.IDPERSONA,
                             V_UNIDADES_GUARDIA.FECHAFIN,
                             V_UNIDADES_GUARDIA.FECHAFIN, --solo de la UG
                             contador,
                             c_obtener_asistenciasFact,
                             c_obtener_asistenciasnuevas,
                             'Ac', -- apuntes GAc
                             V_TOTALASIST,
                             V_CODRETORNO2,
                             V_DATOSERROR2);

      --cargando la matriz de apuntes de actuaciones
      PROC_CARGA_M_APUNTE_AC(MATRIZFACTURABLE.IDINSTITUCION,
                             MATRIZFACTURABLE.IDTURNO,
                             MATRIZFACTURABLE.IDGUARDIA,
                             MATRIZFACTURABLE.IDPERSONA,
                             V_UNIDADES_GUARDIA.FECHAFIN,
                             V_UNIDADES_GUARDIA.FECHAFIN, --solo de la UG
                             contador,
                             c_obtener_actuacionesFact,
                             c_obtener_actuacionesnuevas,
                             'Ac', -- apuntes GAc
                             V_COSTEFIJO,
                             V_TOTALACTUA,
                             V_CODRETORNO2,
                             V_DATOSERROR2);

      --
      --3. GENERAR APUNTE DE UG TENIENDO EN CUENTA LOS DIFERENTES MOTIVOS
      --
      --Acumulado de todos los costes fijos de actuaciones
      costeFijoAc_UG := V_COSTEFIJO;
      costeFijoAc_CG := costeFijoAc_CG + costeFijoAc_UG;

      --obteniendo la matriz donde se hacen los apuntes
      IND_UG := IND_UG + 1;
      M_APUNTE_UG(IND_UG).IDINSTITUCION := MATRIZFACTURABLE.IDINSTITUCION;
      MATRIZAPUNTES_UG := M_APUNTE_UG(IND_UG);

      --haciendo apuntes
      MATRIZAPUNTES_UG.IDINSTITUCION        := MATRIZFACTURABLE.IDINSTITUCION;
      MATRIZAPUNTES_UG.IDTURNO              := MATRIZFACTURABLE.IDTURNO;
      MATRIZAPUNTES_UG.IDGUARDIA            := MATRIZFACTURABLE.IDGUARDIA;
      MATRIZAPUNTES_UG.IDPERSONA            := MATRIZFACTURABLE.IDPERSONA;
      MATRIZAPUNTES_UG.FECHAINICIO          := MATRIZFACTURABLE.FECHAINICIO;
      MATRIZAPUNTES_UG.FECHAFIN             := V_UNIDADES_GUARDIA.FECHAFIN;
      MATRIZAPUNTES_UG.FACTURADO            := MATRIZFACTURABLE.FACTURADO;
      MATRIZAPUNTES_UG.CONTADOR             := contador;
      MATRIZAPUNTES_UG.COSTEFIJO            := costeFijoAc_UG;

      --comprobando que importe apuntar y motivo
      --INI: Cambio facturacion guardias inactivas catalanes de VG --
      If (v_totalactua = 0 And V_CONFIG_GUARDIA.CONSEJOINSTITUCION = C_CATALAN And V_CONFIG_GUARDIA.ESGUARDIAVG = '1') Then
        MATRIZAPUNTES_UG.IMPORTE := C_IMPORTE_GUARDIA_INACTIVA - V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

        If (V_UNIDADES_GUARDIA.IMPORTEFACTURADO = 0) Then
          MATRIZAPUNTES_UG.MOTIVO := 54; --GAcMin
        Elsif (MATRIZAPUNTES_UG.IMPORTE <= 0) Then
          MATRIZAPUNTES_UG.MOTIVO := 58; --GAcMinNoDev+
        Else
          MATRIZAPUNTES_UG.MOTIVO := 60; --GAcMin+
        End If;
      --FIN: Cambio facturacion guardias inactivas catalanes de VG --
      Elsif (v_totalactua <=
         V_CONFIG_GUARDIA.NUMACTUACIONESDOBLA) then
        --No se dobla:
        MATRIZAPUNTES_UG.IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIA -
                                    V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

        if (V_UNIDADES_GUARDIA.IMPORTEFACTURADO = 0) then
          MATRIZAPUNTES_UG.MOTIVO := 44; --GAc
        else
          MATRIZAPUNTES_UG.MOTIVO := 38; --GSNoDev+
        end if;
      else
        --Se dobla:
        MATRIZAPUNTES_UG.IMPORTE := V_CONFIG_GUARDIA.IMPORTEGUARDIADOBLADA -
                                    V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

        if (V_UNIDADES_GUARDIA.IMPORTEFACTURADO = 0) Then
          MATRIZAPUNTES_UG.MOTIVO := 4; --GDAc
        elsif (MATRIZAPUNTES_UG.IMPORTE <= 0) then
          MATRIZAPUNTES_UG.MOTIVO := 40; --GDNoDevAc+
        else
          MATRIZAPUNTES_UG.MOTIVO := 16; --GDAc+
        end if;
      end if;

      -- RGG cambio para importes negativos
      if (MATRIZAPUNTES_UG.IMPORTE < 0) then
         MATRIZAPUNTES_UG.IMPORTE := 0;
      end if;

      --total de importes para la CG
      totalImporteUGs := totalImporteUGs + MATRIZAPUNTES_UG.IMPORTE;

      --guardando los registros modificados
      M_APUNTE_UG(IND_UG) := MATRIZAPUNTES_UG;
    END LOOP;

    --
    --4. GENERAR APUNTE GENERICO DE CG TENIENDO EN CUENTA LOS DIFERENTES MOTIVOS
    --
    --inicializando contador
    contador := 1;

    --Por cada recorrido de la matriz de cabecera facturable solo habra un
    --registro en la matriz de apunte de CG por eso el indice siempre sera 1
    IND_CG := 1;

    --obteniendo la matriz donde se hacen los apuntes
    M_APUNTE_CG(IND_CG).IDINSTITUCION := MATRIZFACTURABLE.IDINSTITUCION;
    MATRIZAPUNTES := M_APUNTE_CG(IND_CG);

    --haciendo apuntes
    MATRIZAPUNTES.IDINSTITUCION        := MATRIZFACTURABLE.IDINSTITUCION;
    MATRIZAPUNTES.IDTURNO              := MATRIZFACTURABLE.IDTURNO;
    MATRIZAPUNTES.IDGUARDIA            := MATRIZFACTURABLE.IDGUARDIA;
    MATRIZAPUNTES.IDPERSONA            := MATRIZFACTURABLE.IDPERSONA;
    MATRIZAPUNTES.FECHAINICIO          := MATRIZFACTURABLE.FECHAINICIO;
    MATRIZAPUNTES.FECHAFIN             := MATRIZFACTURABLE.FECHAFIN;
    MATRIZAPUNTES.FACTURADO            := MATRIZFACTURABLE.FACTURADO;
    MATRIZAPUNTES.IDTIPOAPUNTE         := 'CG';
    MATRIZAPUNTES.CONTADOR             := contador;
    MATRIZAPUNTES.COSTEFIJO            := costeFijoAc_CG;
    --Apunte generico de pago por actuaciones
    MATRIZAPUNTES.MOTIVO  := 44; /* GAc */


    -- RGG cambio para restarlo a lo ya facturado (con control de negativos)
    IF (MATRIZFACTURABLE.IMPORTEFACTURADO>0) THEN
       MATRIZAPUNTES.MOTIVO  := 49; /* GAc+ */
    END IF;

    --totalImporteUGs := totalImporteUGs - MATRIZFACTURABLE.IMPORTEFACTURADO;
    --if (totalImporteUGs<0) then
    --   totalImporteUGs := 0;
    --end if;

    MATRIZAPUNTES.IMPORTE := totalImporteUGs;

    --guardando los registros modificados
    M_APUNTE_CG(IND_CG) := MATRIZAPUNTES;

  END PROC_FACT_GUARDIA_DOBLAAC_NOAG;

    /****************************************************************************************************************/
    /* Nombre: PROC_FACT_ASIST_NOAPLICATIPO */
    /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Asistencias y no aplica tipos de asistencias. */
    /* */
    /* P_INDICEMATRIZFACTURABLE - IN - Indica sobre que registro se obtiene la config. de facturacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(200) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* */
    /* Version: 1.0 - Fecha Creacion: 25/04/2006 - Autor: Pilar Duran Munoz */
    /* Version: 2.0 - Fecha Modificacion: 15/10/2014 - Autor: Jorge Paez Trivino */
    /****************************************************************************************************************/
    PROCEDURE PROC_FACT_ASIST_NOAPLICATIPO (
        P_INDICEMATRIZFACTURABLE IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2
    ) IS

        contadorAsistenciasFactUG number := 0;
        contadorAsistenciasFactCG number := 0;
        contadorAsistenciasUG number := 0;
        contadorAsistenciasCG number := 0;
        contadorUnidades number := 0;
        costeFijoUG number := 0;
        costeFijoCG number := 0;
        importeActualUG number := 0;
        importeTotalAsistenciaUG number := 0;
        importeTotalAsistenciaCG number := 0;
        importeTotalCG number := 0;
        importeFacturadoAsistAuxUG number := 0;
        importeFacturadoAsistAuxCG number := 0;

    BEGIN
        IND_UG := 0;
        IND_AS := 0;
        IND_AC := 0;

        -- Obtenemos las unidades de guardia realizadas en la cabecera de guardias, ordenadas por fecha_fin
        V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes del bucle por unidad de guardia';
        FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA(
            M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).IDINSTITUCION,
            M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).IDTURNO,
            M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).IDGUARDIA,
            M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).IDPERSONA,
            M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).FECHAINICIO,
            V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA
        ) LOOP
            contadorUnidades := contadorUnidades + 1;

            -- Para cada UG
            importeTotalAsistenciaUG := 0;
            costeFijoUG := 0;
            contadorAsistenciasFactUG := 0;
            contadorAsistenciasUG := 0;

            -- Obtenemos las asistencias realizadas y justificadas anteriormente a la fecha fin del ciclo de facturacion, ordenadas por fecha de realizacion
            V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes del bucle por asistencias';
            FOR V_ASISTENCIAS_UG IN C_ASISTENCIAS_UG(
                V_UNIDADES_GUARDIA.IDINSTITUCION,
                V_UNIDADES_GUARDIA.IDTURNO,
                V_UNIDADES_GUARDIA.IDGUARDIA,
                V_UNIDADES_GUARDIA.IDPERSONA,
                V_UNIDADES_GUARDIA.FECHAINICIO,
                V_UNIDADES_GUARDIA.FECHAFIN,
                V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA
            ) LOOP

                -- Para cada Asistencia
                contadorAsistenciasUG := contadorAsistenciasUG + 1;
                contadorAsistenciasCG := contadorAsistenciasCG + 1;

                IF (V_ASISTENCIAS_UG.FACTURADO<>PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN -- Asistencia no facturada
                    IND_AS := IND_AS + 1; -- Nuevo apunte M_APUNTE_AS
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIAS_UG.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIAS_UG.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIAS_UG.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := V_ASISTENCIAS_UG.FACTURADO;
                    M_APUNTE_AS(IND_AS).CONTADOR := contadorUnidades;
                    M_APUNTE_AS(IND_AS).MOTIVO := 5; --As
                    M_APUNTE_AS(IND_AS).IMPORTE := V_CONFIG_GUARDIA.IMPORTEASISTENCIA;

                ELSE -- Asistencia facturada
                    IF (V_UNIDADES_GUARDIA.facturado IS NOT NULL AND V_UNIDADES_GUARDIA.facturado <> '0') THEN -- UG facturada
                        contadorAsistenciasFactUG := contadorAsistenciasFactUG + 1;
                        contadorAsistenciasFactCG := contadorAsistenciasFactCG + 1;

                        IF (V_ASISTENCIAS_UG.NUMACTNOFAC>0) THEN -- Se comprueba que tenga almenos una actuacion no facturada
                            IND_AS := IND_AS + 1; -- Nuevo apunte M_APUNTE_AS
                            M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIAS_UG.IDINSTITUCION;
                            M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIAS_UG.ANIO;
                            M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIAS_UG.NUMERO;
                            M_APUNTE_AS(IND_AS).FACTURADO := V_ASISTENCIAS_UG.FACTURADO;
                            M_APUNTE_AS(IND_AS).CONTADOR := contadorUnidades;
                            M_APUNTE_AS(IND_AS).MOTIVO := 5; --As
                            M_APUNTE_AS(IND_AS).IMPORTE := 0;
                        END IF;
                    END IF;
                END IF;

                importeTotalAsistenciaUG := importeTotalAsistenciaUG + V_CONFIG_GUARDIA.IMPORTEASISTENCIA;
                importeTotalAsistenciaCG := importeTotalAsistenciaCG + V_CONFIG_GUARDIA.IMPORTEASISTENCIA;
            END LOOP; -- Fin asistencias

            -- CARGA LA MATRIZ M_APUNTE_AC (carga las actuaciones no facturadas)
            -- Utilizamos este procedimiento y no PROC_CARGA_M_APUNTE_AC_CG porque este no inicializa el contador de IND_AC a 0
            PROC_CARGA_M_APUNTE_ACNTIPO_CG(
                V_UNIDADES_GUARDIA.IDINSTITUCION,
                V_UNIDADES_GUARDIA.IDTURNO,
                V_UNIDADES_GUARDIA.IDGUARDIA,
                V_UNIDADES_GUARDIA.IDPERSONA,
                V_UNIDADES_GUARDIA.FECHAINICIO,
                V_UNIDADES_GUARDIA.FECHAFIN,
                contadorUnidades,
                c_obtener_actuacionesFactUG,
                c_obtener_actuacionesnuevasUG,
                5,
                V_COSTEFIJO,
                V_CODRETORNO2,
                V_DATOSERROR2);

            V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de sumar costes fijos';
            costeFijoUG := costeFijoUG + V_COSTEFIJO;
            costeFijoCG := costeFijoCG + costeFijoUG;

            importeActualUG := 0;

            V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de guardar las unidades de guardia';
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDADES_GUARDIA.IDINSTITUCION;
            M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDADES_GUARDIA.IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDADES_GUARDIA.IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDADES_GUARDIA.IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDADES_GUARDIA.FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
            M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDADES_GUARDIA.FACTURADO;
            M_APUNTE_UG(IND_UG).COSTEFIJO := COSTEFIJOUG;
            M_APUNTE_UG(IND_UG).CONTADOR := contadorUnidades;

            V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de calcular asistencia';
            IF (V_UNIDADES_GUARDIA.facturado IS NULL OR V_UNIDADES_GUARDIA.facturado = '0' OR contadorAsistenciasUG = 0 OR contadorAsistenciasFactUG<contadorAsistenciasUG) THEN -- UG no facturada OR sin asistencias OR asistencias no facturadas

                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de guardar importe auxiliar facturado de asistencias';
                importeFacturadoAsistAuxUG  := V_UNIDADES_GUARDIA.IMPORTEFACTURADO;

                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de calcular totales asistencia por dia si no agrupa';
                IF (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '0') THEN

                    -- El importe de las asistencias de la UG supera el maximo configurado
                    IF (V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA IS NOT NULL AND V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA <> 0.0 AND importeTotalAsistenciaUG >= V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA) THEN

                        IF (importeFacturadoAsistAuxUG>0) THEN -- UG facturada
                            V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Importe maximo de asistencia menos facturado';
                            M_APUNTE_UG(IND_UG).MOTIVO := 17; --AsMax+;
                            M_APUNTE_UG(IND_UG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA - importeFacturadoAsistAuxUG;

                            IF (M_APUNTE_UG(IND_UG).IMPORTE<0) THEN
                                M_APUNTE_UG(IND_UG).IMPORTE := 0;
                            END IF;

                        ELSE -- UG no facturada
                            V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Importe maximo de asistencia';
                            M_APUNTE_UG(IND_UG).MOTIVO := 3; --AsMax;
                            M_APUNTE_UG(IND_UG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA;
                        END IF;

                        importeActualUG := M_APUNTE_UG(IND_UG).IMPORTE;

                    ELSE -- El importe de las asistencias de la UG es menor que el maximo configurado

                        -- El importe de las asistencias de la UG es menor que el minimo configurado
                        IF (V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA IS NOT NULL AND V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA <> 0.0 AND importeTotalAsistenciaUG < V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) THEN

                            IF (importeFacturadoAsistAuxUG>0) THEN -- UG facturada
                                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Importe minimo de asistencia ya facturado';
                                M_APUNTE_UG(IND_UG).motivo := 33; --AsMin+
                                M_APUNTE_UG(IND_UG).importe := 0;

                            ELSE -- UG no facturada
                                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Importe minimo de asistencia no facturado';
                                M_APUNTE_UG(IND_UG).motivo := 10; --AsMin
                                M_APUNTE_UG(IND_UG).importe := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;
                            END IF;

                            importeActualUG := M_APUNTE_UG(IND_UG).importe;

                        ELSE -- El importe de las asistencias de la UG es menor que el maximo configurado y mayor que el minimo configurado
                            IF (importeFacturadoAsistAuxUG>0) THEN -- UG facturada
                                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Importe de asistencia ya facturado';
                                M_APUNTE_UG(IND_UG).MOTIVO := 27; --As+;
                                M_APUNTE_UG(IND_UG).IMPORTE := importeTotalAsistenciaUG - importeFacturadoAsistAuxUG;

                                IF (M_APUNTE_UG(IND_UG).IMPORTE<0) THEN
                                    M_APUNTE_UG(IND_UG).IMPORTE := 0;
                                END IF;

                            ELSE -- UG no facturada
                                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Importe de asistencia no facturado';
                                M_APUNTE_UG(IND_UG).MOTIVO := 5; --As;
                                M_APUNTE_UG(IND_UG).IMPORTE := importeTotalAsistenciaUG;
                            END IF;

                            importeActualUG := M_APUNTE_UG(IND_UG).IMPORTE;
                        END IF;
                    END IF;

                ELSE -- AGRUPARNOPAGAGUARDIA = '1'
                    V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de calcular totales asistencia por dia si agrupa';

                    -- -- El importe de las asistencias de la UG es mayor que el importe facturado
                    IF (importeFacturadoAsistAuxUG>0 and importeTotalAsistenciaUG <> importeFacturadoAsistAuxUG) THEN -- UG facturado
                        M_APUNTE_UG(IND_UG).MOTIVO := 27; --As+;
                    ELSE -- UG no facturado
                        M_APUNTE_UG(IND_UG).MOTIVO := 5; --As;
                    END IF;

                    M_APUNTE_UG(IND_UG).IMPORTE := importeTotalAsistenciaUG - importeFacturadoAsistAuxUG;

                    IF (M_APUNTE_UG(IND_UG).IMPORTE<0) THEN
                        M_APUNTE_UG(IND_UG).IMPORTE := 0;
                    END IF;

                    importeActualUG := M_APUNTE_UG(IND_UG).IMPORTE;
                END IF;

                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de sumar importes a la unidad de guardia';
                importeTotalCG := importeTotalCG + importeActualUG;

            ELSE -- UG facturada con asistencias y todas las asistencias facturadas
                V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de apuntar asistencia ya facturada';
                M_APUNTE_UG(IND_UG).MOTIVO := 27; --As+;
                M_APUNTE_UG(IND_UG).IMPORTE := 0;
            END IF;
        END LOOP; -- Fin UG

        V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de apuntar las cabeceras';
        -- Comenzamos el apunte en CG
        IND_CG := 1;
        -- Por cada recorrido de la matriz de cabecera facturable solo habra un registro en la matriz de apunte de CG por eso el indice siempre sera 1.
        M_APUNTE_CG(IND_CG).idinstitucion := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).idinstitucion;
        M_APUNTE_CG(IND_CG).idturno := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).idturno;
        M_APUNTE_CG(IND_CG).idguardia := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).idguardia;
        M_APUNTE_CG(IND_CG).idpersona := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).idpersona;
        M_APUNTE_CG(IND_CG).fechainicio := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).fechainicio;
        M_APUNTE_CG(IND_CG).fechafin := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).fechafin;
        M_APUNTE_CG(IND_CG).facturado := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).facturado;
        M_APUNTE_CG(IND_CG).costefijo := costeFijoCG;
        M_APUNTE_CG(IND_CG).contador := 1;

        if (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1') then
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG-';
        else
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';
        end if;

        importeFacturadoAsistAuxCG := M_CG_FACTURABLE(P_INDICEMATRIZFACTURABLE).IMPORTEFACTURADOASIS;

        V_DATOSERROR2 := 'PROC_FACT_ASIST_NOAPLICATIPO: Antes de calcular los importes por cabecera';
        if (contadorAsistenciasFactCG < contadorAsistenciasCG OR contadorAsistenciasCG = 0) then

            if (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1') then

                -- Los importes superan el maximo configurado
                if (V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA is not null and V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA <> 0.0 and importeTotalCG + importeFacturadoAsistAuxCG >= V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA) then

                    if (importeFacturadoAsistAuxCG > 0) then -- CG facturada
                        M_APUNTE_CG(IND_CG).MOTIVO := 17; --AsMax+;
                        M_APUNTE_CG(IND_CG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA - importeFacturadoAsistAuxCG;

                        if (M_APUNTE_CG(IND_CG).IMPORTE < 0) then
                            M_APUNTE_CG(IND_CG).IMPORTE := 0;
                        end if;

                    else -- CG no facturada
                        M_APUNTE_CG(IND_CG).MOTIVO := 3; --AsMax;
                        M_APUNTE_CG(IND_CG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA;
                    end if;

                else -- Los importes no superan el maximo configurado
                    if (V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA IS NOT NULL AND V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA <> 0.0) then

                        if (importeTotalCG < V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) then -- El importe total es menor que el minimo configurado

                            if (importeFacturadoAsistAuxCG > 0) then -- CG facturada

                                -- El importe facturado es mayor que el minimo configurado
                                if (importeFacturadoAsistAuxCG > V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) then
                                    M_APUNTE_CG(IND_CG).motivo := 5; --As;
                                    M_APUNTE_CG(IND_CG).importe := importeFacturadoAsistAuxCG - V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;

                                else -- El importe facturado es menor que el minimo configurado
                                    M_APUNTE_CG(IND_CG).motivo := 33; --AsMin+
                                    M_APUNTE_CG(IND_CG).importe := 0;
                                end if;

                            else -- CG no facturada
                                M_APUNTE_CG(IND_CG).motivo := 10; --AsMin
                                M_APUNTE_CG(IND_CG).importe := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;
                            end if;

                        else -- El importe total es mayor que el minimo configurado
                            if (importeFacturadoAsistAuxCG>0) then -- CG facturada

                                -- El importe total es mayor que el minimo configurado
                                if (importeTotalCG > V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) then
                                    M_APUNTE_CG(IND_CG).motivo := 27; --As+;

                                    -- El importe facturado es igual que el minimo configurado
                                    if (importeFacturadoAsistAuxCG = V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) then
                                        M_APUNTE_CG(IND_CG).importe := importeTotalCG - V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;

                                    else -- El importe facturado no es igual que el minimo configurado
                                        M_APUNTE_CG(IND_CG).importe := importeTotalCG;
                                    end if;

                                else -- El importe total es igual que el minimo configurado
                                    M_APUNTE_CG(IND_CG).motivo := 27; --As+;
                                    M_APUNTE_CG(IND_CG).importe := 0;
                                end if;

                            else -- CG no facturada
                                -- El importe total es mayor que el minimo configurado
                                if (importeTotalCG > V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) then
                                    M_APUNTE_CG(IND_CG).motivo := 5; --As;
                                    M_APUNTE_CG(IND_CG).importe := importeTotalCG;

                                else -- El importe total es igual que el minimo configurado
                                    M_APUNTE_CG(IND_CG).motivo := 10; --AsMin
                                    M_APUNTE_CG(IND_CG).importe := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;
                                end if;
                            end if;
                        end if;

                    else -- si no tiene minimo configurado
                        M_APUNTE_CG(IND_CG).IMPORTE := importeTotalCG;

                        if (importeFacturadoAsistAuxCG>0 and importeTotalCG > 0) then
                            M_APUNTE_CG(IND_CG).MOTIVO := 27; --As+;

                        else
                            M_APUNTE_CG(IND_CG).MOTIVO := 5; --As;
                        end if;
                    END IF;
                end if;

            else -- V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '0'
                if (importeFacturadoAsistAuxCG>0) then
                    M_APUNTE_CG(IND_CG).motivo := 27; --As+
                else
                    M_APUNTE_CG(IND_CG).motivo := 5; --As
                end if;
                M_APUNTE_CG(IND_CG).importe := importeTotalCG;
            end if;

        else
            M_APUNTE_CG(IND_CG).motivo := 11; --AsNoDev
            M_APUNTE_CG(IND_CG).importe := importeTotalCG;
        end if;

        P_DATOSERROR := 'PROCEDURE PROC_FACT_ASIST_NOAPLICATIPO: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            when others then
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || V_DATOSERROR2 || ' ' || SQLERRM;
  END PROC_FACT_ASIST_NOAPLICATIPO;

  /***************************************************************************/
  /* Nombre:      PROC_FACT_ASIST_APLICATIPO
  /* Descripcion: Se encarga de cargar las tablas de memoria cuando
  /*              facturamos por Asistencias y aplica tipos de asistencias.
  /*
  /* Parametros              IN/OUT   Descripcion              Tipo de Datos */
  /* ----------------------  ------   ----------------------   ------------- */
  /* indiceMatrizFacturable   IN      Indica sobre que         NUMBER
  /*                                  registro se obtiene la
  /*                                  config. de facturacion
  /* P_CODRETORNO             OUT     Devuelve 0 en caso de    VARCHAR2(10)
  /*                                  que todo termine OK
  /*                                  En caso de error
  /*                                  devuelve el codigo de
  /*                                  error Oracle corresp.
  /* P_DATOSERROR             OUT     Devuelve null en caso    VARCHAR2(200)
  /*                                  de que todo termine OK
  /*                                  En caso de error
  /*                                  devuelve el codigo de
  /*                                  error Oracle corresp.
  /*
  /* Fecha Modif.  Autor Modificacion   Descripcion Modificacion             */
  /* ------------  -------------------  ------------------------------------ */
  /* 25/04/2006    Pilar Duran Munoz    Creacion
  /* 11/08/2008    Adrian Ayala Gomez   Se incluye la diferencia entre
  /*                                    Agrupar o no y la seleccion de dias
  /*                                    Aunque se ha reconstruido TODO
  /***************************************************************************/

    PROCEDURE PROC_FACT_ASIST_APLICATIPO(
        indiceMatrizFacturable IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
    -- Obtenemos las asistencias realizadas y justificadas anteriormente a la fecha fin del ciclo de facturacion para cada idtipocolegiado ordenadas por fecha de realizacion (Este cursor se utiliza para la facturacion por asistencias)
    CURSOR C_ASISTENCIASTIPO_UG (V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAFIN DATE, V_IDTIPOASISTCOLEG NUMBER, V_DIASAPLICABLES VARCHAR2, V_IDFACTURACION NUMBER) IS (
        SELECT ASI.IDTIPOASISTENCIACOLEGIO,
           ASI.FECHAHORA,
           ASI.IDINSTITUCION,
           ASI.ANIO,
           ASI.NUMERO,
           1 AS FACTURADO,
           FUNC_CALCULAR_IMPORTEASIST(ASI.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ASI.ANIO, ASI.NUMERO, V_FECHAFIN, V_IDFACTURACION, 0) AS IMPORTE,
           FUNC_CALCULAR_IMPORTEASIST(ASI.IDINSTITUCION, ASI.IDTURNO, ASi.IDGUARDIA, ASI.ANIO, ASI.NUMERO, V_FECHAFIN, V_IDFACTURACION, 1) AS IMPORTEMAX
        FROM SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND ASI.FACTURADO = '1'
            AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1
            AND ASI.IDTIPOASISTENCIACOLEGIO = V_IDTIPOASISTCOLEG
        UNION
        SELECT ASI.IDTIPOASISTENCIACOLEGIO,
           ASI.FECHAHORA,
           ASI.IDINSTITUCION,
           ASI.ANIO,
           ASI.NUMERO,
           0 AS FACTURADO,
           FUNC_CALCULAR_IMPORTEASIST(ASI.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ASI.ANIO, ASI.NUMERO, V_FECHAFIN, V_IDFACTURACION, 0) AS IMPORTE,
           FUNC_CALCULAR_IMPORTEASIST(ASI.IDINSTITUCION, ASI.IDTURNO, ASi.IDGUARDIA, ASI.ANIO, ASI.NUMERO, V_FECHAFIN, V_IDFACTURACION, 1) AS IMPORTEMAX
        FROM SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND (ASI.FACTURADO IS NULL OR ASI.FACTURADO = '0')
            AND ASI.IDTIPOASISTENCIACOLEGIO = V_IDTIPOASISTCOLEG
            AND FUNC_EXISTE_ACTUACION_JUSTIF(ASI.IDINSTITUCION, ASI.ANIO, ASI.NUMERO) > 0
            AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1)
        ORDER BY IDTIPOASISTENCIACOLEGIO, FECHAHORA;        

        -- Contadores para los apuntes
        contador NUMBER;
        contadorDiaGuardia NUMBER;

        -- Importes cogidos del Tipo
        importeTipoAsistencia NUMBER;
        importeTipoAsistMax NUMBER;

        -- Costes Fijos
        costeFijoCG NUMBER;
        costeFijoUG NUMBER;
        costeFijoUGDiaGUardia NUMBER;

        -- Acumuladores
        totalImporteAsNuevas NUMBER;
        totalImporteAsFact NUMBER;
        totalImporteAsFactFinal NUMBER;
        auxImporteCalculado NUMBER;
        totalImporteTiposNuevos NUMBER;
        totalImporteTiposFacturados NUMBER;
        totalImporteUGs NUMBER;
        totalImporteUGsFacturado NUMBER;
        aplicaMaximoMas NUMBER;

    BEGIN
        -- Inicializo variables
        IND_UG := 0;
        IND_AS := 0;
        IND_AC := 0;        
        contador := 0;
        contadorDiaGuardia := 0;
        costeFijoCG := 0;
        totalImporteUGs := 0;  
        totalImporteUGsFacturado := 0;

        FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA_TP(
            M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
            M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
            V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA) LOOP

            -- Inicializo variables
            costeFijoUGDiaGuardia := 0;
            totalImporteTiposNuevos := 0;
            totalImporteTiposFacturados := 0;

            -- Actualizo contadores
            contador := contador + 1;
            contadorDiaGuardia := contador;

            FOR V_TIPOASISTENCIAS_UG IN C_TIPOASISTENCIAS_UG(
                V_UNIDADES_GUARDIA.IDINSTITUCION,
                V_UNIDADES_GUARDIA.IDTURNO,
                V_UNIDADES_GUARDIA.IDGUARDIA,
                V_UNIDADES_GUARDIA.IDPERSONA,
                V_UNIDADES_GUARDIA.FECHAINICIO,
                V_UNIDADES_GUARDIA.FECHAFIN,
                V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA) LOOP
                
                -- Inicializo variables
                totalImporteAsFact := 0;
                totalImporteAsNuevas := 0;

                -- Actualizo contadores
                contador := contador + 1;

                FOR V_ASISTENCIAS_UG IN C_ASISTENCIASTIPO_UG(
                    V_UNIDADES_GUARDIA.IDINSTITUCION,
                    V_UNIDADES_GUARDIA.IDTURNO,
                    V_UNIDADES_GUARDIA.IDGUARDIA,
                    V_UNIDADES_GUARDIA.IDPERSONA,
                    V_UNIDADES_GUARDIA.FECHAFIN,
                    V_TIPOASISTENCIAS_UG.tipoasist,
                    V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA,
                    M_CG_FACTURABLE(indiceMatrizFacturable).IDFACTURACION) LOOP

                    -- Obtenemos el importe de la asistencia, o bien de la guardia
                    importeTipoAsistencia := NVL(NVL(V_ASISTENCIAS_UG.importe, V_CONFIG_GUARDIA.IMPORTEASISTENCIA), 0);                    

                    -- Obtenemos el importe maximo de la asistencia
                    importeTipoAsistMax := NVL(NVL(V_ASISTENCIAS_UG.importemax, V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA), 0);

                    -- Compruebo que no ha facturado la asistencia
                    IF (V_ASISTENCIAS_UG.FACTURADO IS NULL OR V_ASISTENCIAS_UG.FACTURADO = PKG_SIGA_CONSTANTES.DB_FALSE_A) THEN

                        -- Creo apunte en la matriz de memoria: M_APUNTE_AS (APUNTE DE ASISTENCIA)
                        IND_AS := IND_AS + 1;
                        M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIAS_UG.IDINSTITUCION;
                        M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIAS_UG.ANIO;
                        M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIAS_UG.NUMERO;
                        M_APUNTE_AS(IND_AS).FACTURADO := V_ASISTENCIAS_UG.FACTURADO;
                        M_APUNTE_AS(IND_AS).CONTADOR := contador;
                        M_APUNTE_AS(IND_AS).MOTIVO := 20; --AsTp;
                        M_APUNTE_AS(IND_AS).IMPORTE := importeTipoAsistencia; 

                        totalImporteAsNuevas := totalImporteAsNuevas + importeTipoAsistencia;
                        
                    ELSE
                        totalImporteAsFact := totalImporteAsFact + importeTipoAsistencia;                        
                    END IF;
                END LOOP; -- Fin V_ASISTENCIAS_UG

                -- Carga de la matriz de apunte M_APUNTE_AC (APUNTE DE ACTUACION)
                PROC_CARGA_M_APUNTE_ACTIPO_CG(
                    V_UNIDADES_GUARDIA.IDINSTITUCION,
                    V_UNIDADES_GUARDIA.IDTURNO,
                    V_UNIDADES_GUARDIA.IDGUARDIA,
                    V_UNIDADES_GUARDIA.IDPERSONA,
                    V_UNIDADES_GUARDIA.FECHAINICIO,
                    V_UNIDADES_GUARDIA.FECHAFIN,
                    contador,
                    c_obtener_actFactTipoUG,
                    c_obtener_actnuevasTipoUG,
                    V_TIPOASISTENCIAS_UG.tipoasist,
                    20, -- AsTp
                    V_COSTEFIJO, -- Variable OUT
                    V_CODRETORNO2,
                    V_DATOSERROR2);

                -- Actualizo los costes fijos
                costeFijoUG := V_COSTEFIJO;
                costeFijoUGDiaGuardia := costeFijoUGDiaGuardia + costeFijoUG;
                costeFijoCG := costeFijoCG + costeFijoUG;
                
                /*** INICIO PROCESO CALCULO FACTURADO ***/                
                IF (NVL(importeTipoAsistMax, 0.0) > 0.0 -- Tiene importe maximo del tipo de asistencia
                    AND totalImporteAsFact > importeTipoAsistMax) THEN -- El importe de las asistencias facturadas (total - nuevas) supera el importe maximo del tipo de asistencia 
                    -- RGG: NO DEVENGO POR APUNTE DETIPO PORQUE YA SE DEVENGA POR DÍA                    
                    totalImporteAsFactFinal := importeTipoAsistMax;
                ELSE
                    totalImporteAsFactFinal := totalImporteAsFact;                                        
                END IF;                
                totalImporteTiposFacturados := totalImporteTiposFacturados + totalImporteAsFactFinal;            
                /*** INICIO PROCESO CALCULO FACTURADO ***/                
        
                IF (totalImporteAsNuevas > 0) THEN --  Existen nuevas asistencias sin facturar
                
                    -- Creo apunte en la matriz de memoria: M_APUNTE_UG (APUNTE DE TIPO)
                    IND_UG := IND_UG + 1;
                    M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDADES_GUARDIA.IDINSTITUCION;
                    M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDADES_GUARDIA.IDTURNO;
                    M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDADES_GUARDIA.IDGUARDIA;
                    M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDADES_GUARDIA.IDPERSONA;
                    M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDADES_GUARDIA.FECHAINICIO;
                    M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
                    M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDADES_GUARDIA.FACTURADO;            
                    M_APUNTE_UG(IND_UG).CONTADOR := contador;
                    M_APUNTE_UG(IND_UG).COSTEFIJO := costeFijoUG;                        
                
                    IF (V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA IS NOT NULL -- Tiene configurado maximo  
                        AND NVL(importeTipoAsistMax, 0.0) > 0.0 -- Tiene importe maximo del tipo de asistencia
                        AND totalImporteAsFact + totalImporteAsNuevas > importeTipoAsistMax) THEN -- El importe de todas las asistencias supera el importe maximo del tipo de asistencia 
                        -- RGG: NO DEVENGO POR APUNTE DETIPO PORQUE YA SE DEVENGA POR DÍA

                        IF (totalImporteAsFact >= importeTipoAsistMax) THEN -- Consulto si el importe de las asistencias facturadas supera o iguala el importe maximo del tipo de asistencia
                            auxImporteCalculado := 0; 
                            aplicaMaximoMas := 1; -- AsTpMax+
                       
                        ELSE              
                            auxImporteCalculado := importeTipoAsistMax - totalImporteAsFact; -- auxImporteCalculado = Maximo - Facturado
                            IF (auxImporteCalculado < 0) THEN -- Control negativo 
                                auxImporteCalculado := 0; 
                            END IF;
                            
                            IF (totalImporteAsFact > 0) THEN -- Consulta si tiene asistencias facturadas
                                aplicaMaximoMas := 1; -- AsTpMax+
                            ELSE
                                aplicaMaximoMas := 0; -- AsTpMax
                            END IF;                                                                                     
                        END IF;

                        IF (aplicaMaximoMas = 0) THEN 
                            M_APUNTE_UG(IND_UG).MOTIVO := 21; --AsTpMax;
                        ELSE 
                            M_APUNTE_UG(IND_UG).MOTIVO := 41; --AsTpMax+;
                        END IF;

                    ELSE  /* Se tiene que dar por lo menos una de las siguientes situaciones:
                            - No tiene configurado por maximo
                            - No tiene importe o es cero de maximo del tipo de asistencia
                            - El importe de todas las asistencias es inferior o igual al importe maximo del tipo de asistencia*/
                        -- RGG: NO DEVENGO POR APUNTE DETIPO PORQUE YA SE DEVENGA POR DÍA                            
                        
                        auxImporteCalculado := totalImporteAsNuevas;                            

                        IF (totalImporteAsFact > 0) THEN -- Consulta si tiene asistencias facturadas  (total - nuevas)
                            M_APUNTE_UG(IND_UG).MOTIVO := 28; --AsTp+;
                        ELSE
                            M_APUNTE_UG(IND_UG).MOTIVO := 20; --AsTp;
                        END IF;
                    END IF;
                    
                    totalImporteTiposNuevos := totalImporteTiposNuevos + auxImporteCalculado; -- totalImporteTiposNuevos += auxImporteCalculado
                    M_APUNTE_UG(IND_UG).IMPORTE := auxImporteCalculado;
                END IF;          
            END LOOP; -- Fin V_TIPOASISTENCIAS_UG

            -- Creo apunte en la matriz de memoria: M_APUNTE_UG (APUNTE DIA)
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDADES_GUARDIA.IDINSTITUCION;
            M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDADES_GUARDIA.IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDADES_GUARDIA.IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDADES_GUARDIA.IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDADES_GUARDIA.FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
            M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDADES_GUARDIA.FACTURADO;
            M_APUNTE_UG(IND_UG).CONTADOR := contadorDiaGuardia;
            M_APUNTE_UG(IND_UG).COSTEFIJO := costeFijoUGDiaGuardia;

            IF (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1') THEN -- Se agrupa, o sea que los Maximos y Minimos se aplican solo en la CG                
                IF (V_UNIDADES_GUARDIA.Facturado IN ('S', '1')) THEN -- Consulto si tiene facturado la UG                    
                    M_APUNTE_UG(IND_UG).MOTIVO := 27; --As+
                ELSE
                    M_APUNTE_UG(IND_UG).MOTIVO := 5; --As
                END IF;
                auxImporteCalculado := totalImporteTiposNuevos;
      
            ELSE -- No se agrupa, o sea que los Maximos y Minimos se aplican en la UG                
                IF (NVL(V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA, 0.0) > 0.0 -- Tiene configurado el maximo 
                    AND totalImporteTiposNuevos + totalImporteTiposFacturados > V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA) THEN -- El importe de lo nuevo + lo facturado supera el maximo configurado

                    IF (V_UNIDADES_GUARDIA.Facturado IN ('S', '1')) THEN -- Consulto si tiene facturado la UG
                        auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA - V_UNIDADES_GUARDIA.IMPORTEFACTURADO; -- auxImporteCalculado = Maximo - UG_Facturado;
                        IF (auxImporteCalculado<0) THEN -- Control negativo 
                            auxImporteCalculado := 0;
                        END IF;                                                                  
                        M_APUNTE_UG(IND_UG).MOTIVO := 17; --AsMax+                        

                    ELSE
                        auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA;
                        M_APUNTE_UG(IND_UG).MOTIVO := 3; --AsMax;
                    END IF;               

                ELSIF (NVL(V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA, 0.0) > 0.0 -- Tiene configurado el minimo  
                        AND totalImporteTiposNuevos + totalImporteTiposFacturados < V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) THEN -- El importe de lo nuevo + facturado es menor al minimo configurado                                     

                    IF (V_UNIDADES_GUARDIA.Facturado IN ('S', '1')) THEN -- Consulto si tiene facturado la UG
                        auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA - V_UNIDADES_GUARDIA.IMPORTEFACTURADO; -- auxImporteCalculado = Minimo - UG_Facturado;               
                        IF (auxImporteCalculado<0) THEN -- Control negativo 
                            auxImporteCalculado := 0;
                        END IF;                            
                        M_APUNTE_UG(IND_UG).MOTIVO := 33; --AsMin+
                        
                    ELSE
                        auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;
                        M_APUNTE_UG(IND_UG).MOTIVO := 10; --AsMin
                    END IF;

                ELSE -- No supera el maximo, ni es menor al minimo                       
                    IF (V_UNIDADES_GUARDIA.Facturado IN ('S', '1')) THEN -- Consulto si tiene facturado la UG
                        M_APUNTE_UG(IND_UG).MOTIVO := 27; --As+
                        auxImporteCalculado := totalImporteTiposNuevos + totalImporteTiposFacturados - V_UNIDADES_GUARDIA.IMPORTEFACTURADO; -- auxImporteCalculado += AsisNuevo + AsisFacturados - UG_Facturado;
                        IF (auxImporteCalculado<0) THEN -- Control negativo 
                            auxImporteCalculado := 0;
                        END IF;
                            
                    ELSE                        
                        M_APUNTE_UG(IND_UG).MOTIVO := 5; --As
                        auxImporteCalculado := totalImporteTiposNuevos; -- auxImporteCalculado += AsisNuevo;
                    END IF;
                END IF;
            END IF;
            
            totalImporteUGsFacturado := totalImporteUGsFacturado + totalImporteTiposFacturados;
            totalImporteUGs := totalImporteUGs + auxImporteCalculado; -- totalImporteUGs += auxImporteCalculado;
            M_APUNTE_UG(IND_UG).IMPORTE := auxImporteCalculado;
        END LOOP; -- Fin V_UNIDADES_GUARDIA

        -- Creo apunte en la matriz de memoria: M_APUNTE_CG (Cab. Guardia)
        IND_CG := 1; -- Por cada recorrido de la matriz de cabecera facturable solo habra un registro en la matriz de apunte de CG por eso el indice siempre sera 1
        M_APUNTE_CG(IND_CG).idinstitucion := M_CG_FACTURABLE(indiceMatrizFacturable).idinstitucion;
        M_APUNTE_CG(IND_CG).idturno := M_CG_FACTURABLE(indiceMatrizFacturable).idturno;
        M_APUNTE_CG(IND_CG).idguardia := M_CG_FACTURABLE(indiceMatrizFacturable).idguardia;
        M_APUNTE_CG(IND_CG).idpersona := M_CG_FACTURABLE(indiceMatrizFacturable).idpersona;
        M_APUNTE_CG(IND_CG).fechainicio := M_CG_FACTURABLE(indiceMatrizFacturable).fechainicio;
        M_APUNTE_CG(IND_CG).fechafin := M_CG_FACTURABLE(indiceMatrizFacturable).fechafin;
        M_APUNTE_CG(IND_CG).facturado := M_CG_FACTURABLE(indiceMatrizFacturable).facturado;
        M_APUNTE_CG(IND_CG).contador := 1;
        M_APUNTE_CG(IND_CG).costefijo := costeFijoCG;

        -- Indicando como se deberan mostrar los apuntes (agrupados o no)
        IF (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1') THEN
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG-';
        ELSE
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';
        END IF;

        IF (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '0') THEN -- No se agrupa, o sea que los Maximos y Minimos se aplican en las UGs        
            M_APUNTE_CG(IND_CG).IMPORTE := totalImporteUGs;

            IF (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado IN ('S', '1')) THEN -- Consulto si tiene facturado la CG
                M_APUNTE_CG(IND_CG).MOTIVO := 27; --As+
            ELSE
                M_APUNTE_CG(IND_CG).MOTIVO := 5; --As
            END IF;

        ELSE -- Se agrupa, o sea que los Maximos y Minimos se aplican solo en la CG   
            IF (NVL(V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA, 0.0) > 0.0 -- Tiene configurado el maximo 
                AND totalImporteUGs + totalImporteUGsFacturado > V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA) THEN  -- El importe de lo nuevo + lo facturado supera el maximo configurado

                IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO>0) THEN -- Consulto si tiene facturado la CG
                    auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA - M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO; -- auxImporteCalculado = Maximo - Facturado;
                    IF (auxImporteCalculado<0) THEN -- Control negativo 
                        auxImporteCalculado := 0;
                    END IF;                                                                  
                    M_APUNTE_CG(IND_CG).MOTIVO := 17; --AsMax+
                    
                ELSE
                    auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMAXASISTENCIA;
                    M_APUNTE_CG(IND_CG).MOTIVO := 3; --AsMax;                    
                END IF;

                 M_APUNTE_CG(IND_CG).IMPORTE := auxImporteCalculado; 

            ELSIF (NVL(V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA, 0.0) > 0.0 -- Tiene configurado el minimo   
                    AND totalImporteUGs + totalImporteUGsFacturado < V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA) THEN -- El importe de lo nuevo + facturado es menor al minimo configurado
                    
                    IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO>0) THEN -- Consulto si tiene facturado la CG
                        auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA - M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO; -- auxImporteCalculado = Minimo - Facturado;               
                        IF (auxImporteCalculado<0) THEN -- Control negativo 
                            auxImporteCalculado := 0;
                        END IF;                            
                        M_APUNTE_CG(IND_CG).MOTIVO := 33; --AsMin+
                        
                    ELSE
                        auxImporteCalculado := V_CONFIG_GUARDIA.IMPORTEMINASISTENCIA;
                        M_APUNTE_CG(IND_CG).MOTIVO := 10; --AsMin
                    END IF;
                      
                    M_APUNTE_CG(IND_CG).IMPORTE := auxImporteCalculado;    
                    
            ELSE -- No supera el maximo, ni es menor al minimo                       
                IF (M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO>0) THEN -- Consulto si tiene facturado la CG
                    M_APUNTE_CG(IND_CG).MOTIVO := 27; --As+
                ELSE                        
                    M_APUNTE_CG(IND_CG).MOTIVO := 5; --As
                END IF;
                
                auxImporteCalculado := totalImporteUGs + totalImporteUGsFacturado - M_CG_FACTURABLE(indiceMatrizFacturable).IMPORTEFACTURADO; -- auxImporteCalculado += Nuevo + Facturado - CG_Facturado;
                IF (auxImporteCalculado<0) THEN -- Control negativo 
                    auxImporteCalculado := 0;
                END IF;     
                M_APUNTE_CG(IND_CG).IMPORTE := auxImporteCalculado;
            END IF; 
        END IF; 

        P_DATOSERROR := 'PROCEDURE PROC_FACT_ASIST_APLICATIPO: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

    EXCEPTION
        WHEN OTHERS THEN
            P_CODRETORNO := TO_CHAR(SQLCODE);
            P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FACT_ASIST_APLICATIPO;

  --INI: Cambio para Asistencias que derivan en Designacion
  Function f_Tiene_Asist_Derivadas(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                   p_Idturno       In Scs_Asistencia.Idturno%Type,
                                   p_Idguardia     In Scs_Asistencia.Idguardia%Type,
                                   p_Idpersona     In Scs_Asistencia.Idpersonacolegiado%Type,
                                   p_Fechaini      In Scs_Asistencia.Fechahora%Type,
                                   p_Fechafin      In Scs_Asistencia.Fechahora%Type,
                                   p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                   p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2 Is

    c_Id_Gijon       Constant Scs_Asistencia.Idinstitucion%Type := 2027;
    c_Id_Albacete Constant Scs_Asistencia.Idinstitucion%Type := 2002;
    c_Id_Ciudad_Real Constant Scs_Asistencia.Idinstitucion%Type := 2020;
    c_Id_La_Rioja    Constant Scs_Asistencia.Idinstitucion%Type := 2058;

  Begin
    If p_Idinstitucion = c_Id_Gijon Then
      Return f_Tiene_Asist_Derivadas_Gijon(p_Idinstitucion,
                                           p_Idturno,
                                           p_Idguardia,
                                           p_Idpersona,
                                           p_Fechaini,
                                           p_Fechafin,
                                           p_Fechadesde,
                                           p_Fechahasta);
    Elsif p_Idinstitucion in (c_Id_Albacete, c_Id_Ciudad_Real, c_Id_La_Rioja) Then
      Return f_Tiene_Asist_Derivadas_Cr(p_Idinstitucion,
                                        p_Idturno,
                                        p_Idguardia,
                                        p_Idpersona,
                                        p_Fechaini,
                                        p_Fechafin,
                                        p_Fechahasta);
    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Tiene_Asist_Derivadas;

  Function f_Tiene_Asist_Derivadas_Gijon(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                         p_Idturno       In Scs_Asistencia.Idturno%Type,
                                         p_Idguardia     In Scs_Asistencia.Idguardia%Type,
                                         p_Idpersona     In Scs_Asistencia.Idpersonacolegiado%Type,
                                         p_Fechaini      In Scs_Asistencia.Fechahora%Type,
                                         p_Fechafin      In Scs_Asistencia.Fechahora%Type,
                                         p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                         p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2 Is

    c_Estado_Asistencia_Anulada  Constant Scs_Asistencia.Idestadoasistencia%Type := 2;
    c_Estado_Designacion_Anulada Constant Scs_Designa.Estado%Type := 'A';

    Numero Number;

  Begin
    --Numero de
    Select Count(*)
      Into Numero
    --Asistencias
      From Scs_Asistencia Asi, Scs_Actuacionasistencia Act
     Where Asi.Idinstitucion = Act.Idinstitucion
       And Asi.Anio = Act.Anio
       And Asi.Numero = Act.Numero

       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Idturno = p_Idturno
       And Asi.Idguardia = p_Idguardia
       And Asi.Idpersonacolegiado = p_Idpersona
          --del dia o cabecera pasados como parametro
       And Trunc(Asi.Fechahora) Between p_Fechaini And p_Fechafin

          --esta justificada,
       And Trunc(Act.Fechajustificacion) <= p_Fechahasta

          --no esta anulada,
       And Asi.Idestadoasistencia <> c_Estado_Asistencia_Anulada

          --que tienen:
       And (Exists (Select *
                    --a) una designacion directa
                      From Scs_Designa Des
                     Where Asi.Idinstitucion = Des.Idinstitucion
                       And Asi.Designa_Turno = Des.Idturno
                       And Asi.Designa_Anio = Des.Anio
                       And Asi.Designa_Numero = Des.Numero
                          --que no este anulada,
                       And Des.Estado <> c_Estado_Designacion_Anulada
                          --dentro del periodo de facturacion
                       And Trunc(Des.Fechaentrada) Between p_Fechadesde And
                           p_Fechahasta)
           --O
            Or Exists
           --b) una designacion indirecta (a traves de EJG)
            (Select *
               From Scs_Ejgdesigna Ejgdes, Scs_Designa Des
              Where Ejgdes.Idinstitucion = Des.Idinstitucion
                And Ejgdes.Idturno = Des.Idturno
                And Ejgdes.Aniodesigna = Des.Anio
                And Ejgdes.Numerodesigna = Des.Numero
                And Asi.Idinstitucion = Ejgdes.Idinstitucion
                And Asi.Ejgidtipoejg = Ejgdes.Idtipoejg
                And Asi.Ejganio = Ejgdes.Anioejg
                And Asi.Ejgnumero = Ejgdes.Numeroejg
                   --que no este anulada,
                And Des.Estado <> c_Estado_Designacion_Anulada
                   --dentro del periodo de facturacion
                And Trunc(Des.Fechaentrada) Between p_Fechadesde And
                    p_Fechahasta));

    If Numero > 0 Then
      Return Pkg_Siga_Constantes.Db_True_n;
    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Tiene_Asist_Derivadas_Gijon;

  Function f_Tiene_Asist_Derivadas_Cr(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                      p_Idturno       In Scs_Asistencia.Idturno%Type,
                                      p_Idguardia     In Scs_Asistencia.Idguardia%Type,
                                      p_Idpersona     In Scs_Asistencia.Idpersonacolegiado%Type,
                                      p_Fechaini      In Scs_Asistencia.Fechahora%Type,
                                      p_Fechafin      In Scs_Asistencia.Fechahora%Type,
                                      p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2 Is

    Numero Number;

  Begin
    --Numero de
    Select Count(*)
      Into Numero
    --Asistencias
      From Scs_Asistencia Asi
     Where Asi.Idinstitucion = p_Idinstitucion
       And Asi.Idturno = p_Idturno
       And Asi.Idguardia = p_Idguardia
       And Asi.Idpersonacolegiado = p_Idpersona
          --del dia o cabecera pasados como parametro
       And Trunc(Asi.Fechahora) Between p_Fechaini And p_Fechafin

          --que son de derivacion
       And f_Es_Asistencia_Derivada_Cr(Asi.Idinstitucion,
                                       Asi.Anio,
                                       Asi.Numero,
                                       p_Fechahasta) =
           Pkg_Siga_Constantes.Db_True_n;

    If Numero > 0 Then
      Return Pkg_Siga_Constantes.Db_True_n;
    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Tiene_Asist_Derivadas_Cr;

  Function f_Es_Asistencia_Derivada(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                    p_Anio          In Scs_Asistencia.Anio%Type,
                                    p_Numero        In Scs_Asistencia.Numero%Type,
                                    p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                    p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2 Is

    c_Id_Gijon       Constant Scs_Asistencia.Idinstitucion%Type := 2027;
    c_Id_Albacete Constant Scs_Asistencia.Idinstitucion%Type := 2002;
    c_Id_Ciudad_Real Constant Scs_Asistencia.Idinstitucion%Type := 2020;
    c_Id_La_Rioja    Constant Scs_Asistencia.Idinstitucion%Type := 2058;

  Begin
    If p_Idinstitucion = c_Id_Gijon Then
      Return f_Es_Asistencia_Derivada_Gijon(p_Idinstitucion,
                                            p_Anio,
                                            p_Numero,
                                            p_Fechadesde,
                                            p_Fechahasta);
    Elsif p_Idinstitucion in (c_Id_Albacete, c_Id_Ciudad_Real, c_Id_La_Rioja) Then
      Return f_Es_Asistencia_Derivada_Cr(p_Idinstitucion,
                                         p_Anio,
                                         p_Numero,
                                         p_Fechahasta);
    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Es_Asistencia_Derivada;

  Function f_Es_Asistencia_Derivada_Gijon(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                          p_Anio          In Scs_Asistencia.Anio%Type,
                                          p_Numero        In Scs_Asistencia.Numero%Type,
                                          p_Fechadesde    In Scs_Designa.Fechaentrada%Type,
                                          p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2 Is

    c_Estado_Asistencia_Anulada  Constant Scs_Asistencia.Idestadoasistencia%Type := 2;
    c_Estado_Designacion_Anulada Constant Scs_Designa.Estado%Type := 'A';

    v_Esderivada Number;

  Begin
    --averiguando si la asistencia
    Select Count(*)
      Into v_Esderivada
      From Scs_Asistencia Asi, Scs_Actuacionasistencia Act
     Where Asi.Idinstitucion = Act.Idinstitucion
       And Asi.Anio = Act.Anio
       And Asi.Numero = Act.Numero

       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Anio = p_Anio
       And Asi.Numero = p_Numero

          --esta justificada,
       And Trunc(Act.Fechajustificacion) <= p_Fechahasta

          --no esta anulada,
       And Asi.Idestadoasistencia <> c_Estado_Asistencia_Anulada

          --y que tiene:
       And (Exists
           --a) una designacion directa
            (Select *
               From Scs_Designa Des
              Where Asi.Idinstitucion = Des.Idinstitucion
                And Asi.Designa_Turno = Des.Idturno
                And Asi.Designa_Anio = Des.Anio
                And Asi.Designa_Numero = Des.Numero
                   --que no este anulada,
                And Des.Estado <> c_Estado_Designacion_Anulada
                   --dentro del periodo de facturacion
                And Trunc(Des.Fechaentrada) Between p_Fechadesde And
                    p_Fechahasta
                   --sin actuaciones de designacion
                And (Not Exists
                     (Select *
                        From Scs_Actuaciondesigna Actdes
                       Where Actdes.Idinstitucion = Des.Idinstitucion
                         And Actdes.Idturno = Des.Idturno
                         And Actdes.Anio = Des.Anio
                         And Actdes.Numero = Des.Numero
                         And Trunc(Actdes.Fechajustificacion) Between
                             p_Fechadesde And p_Fechahasta)
                    --o con actuaciones de designacion
                     Or Exists
                     (Select *
                        From Scs_Actuaciondesigna Actdes
                       Where Actdes.Idinstitucion = Des.Idinstitucion
                         And Actdes.Idturno = Des.Idturno
                         And Actdes.Anio = Des.Anio
                         And Actdes.Numero = Des.Numero
                            --que (la actuacion) no sea:
                            --ni "Asistencia a la comparecencia de la orden de proteccion"
                            --ni "Juicio de faltas"
                         And Actdes.Idprocedimiento Not In (20, 90, 21, 108)
                            --y (la actuacion) este justificada dentro del periodo de facturacion
                         And Trunc(Actdes.Fechajustificacion) Between
                             p_Fechadesde And p_Fechahasta)))
           --O
            Or Exists
           --b) una designacion indirecta (a traves de EJG)
            (Select *
               From Scs_Ejgdesigna Ejgdes, Scs_Designa Des
              Where Ejgdes.Idinstitucion = Des.Idinstitucion
                And Ejgdes.Idturno = Des.Idturno
                And Ejgdes.Aniodesigna = Des.Anio
                And Ejgdes.Numerodesigna = Des.Numero
                And Asi.Idinstitucion = Ejgdes.Idinstitucion
                And Asi.Ejgidtipoejg = Ejgdes.Idtipoejg
                And Asi.Ejganio = Ejgdes.Anioejg
                And Asi.Ejgnumero = Ejgdes.Numeroejg
                   --que no este anulada,
                And Des.Estado <> c_Estado_Designacion_Anulada
                   --dentro del periodo de facturacion
                And Trunc(Des.Fechaentrada) Between p_Fechadesde And
                    p_Fechahasta
                   --sin actuaciones de designacion
                And (Not Exists
                     (Select *
                        From Scs_Actuaciondesigna Actdes
                       Where Actdes.Idinstitucion = Des.Idinstitucion
                         And Actdes.Idturno = Des.Idturno
                         And Actdes.Anio = Des.Anio
                         And Actdes.Numero = Des.Numero
                         And Trunc(Actdes.Fechajustificacion) Between
                             p_Fechadesde And p_Fechahasta)
                    --o con actuaciones de designacion
                     Or Exists
                     (Select *
                        From Scs_Actuaciondesigna Actdes
                       Where Actdes.Idinstitucion = Des.Idinstitucion
                         And Actdes.Idturno = Des.Idturno
                         And Actdes.Anio = Des.Anio
                         And Actdes.Numero = Des.Numero
                            --que (la actuacion) no sea de "Asistencia a la comparecencia de la orden de proteccion"
                         And Actdes.Idprocedimiento Not In (20, 90, 21, 108)
                            --y (la actuacion) este justificada dentro del periodo de facturacion
                         And Trunc(Actdes.Fechajustificacion) Between
                             p_Fechadesde And p_Fechahasta))));

    If v_Esderivada > 0 Then
      Return Pkg_Siga_Constantes.Db_True_n;
    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Es_Asistencia_Derivada_Gijon;

  Function f_Es_Asistencia_Derivada_Cr(p_Idinstitucion In Scs_Asistencia.Idinstitucion%Type,
                                       p_Anio          In Scs_Asistencia.Anio%Type,
                                       p_Numero        In Scs_Asistencia.Numero%Type,
                                       p_Fechahasta    In Scs_Designa.Fechaentrada%Type)
    Return Varchar2 Is

    c_Estado_Asistencia_Anulada  Constant Scs_Asistencia.Idestadoasistencia%Type := 2;
    c_Estado_Designacion_Anulada Constant Scs_Designa.Estado%Type := 'A';

    v_Esderivada Number;

  Begin
    --averiguando si la asistencia
    Select Count(*)
      Into v_Esderivada
      From Scs_Asistencia Asi, Scs_Actuacionasistencia Act
     Where Asi.Idinstitucion = Act.Idinstitucion
       And Asi.Anio = Act.Anio
       And Asi.Numero = Act.Numero

       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Anio = p_Anio
       And Asi.Numero = p_Numero

          --esta justificada,
       And Trunc(Act.Fechajustificacion) <= p_Fechahasta

          --no esta anulada,
       And Asi.Idestadoasistencia <> c_Estado_Asistencia_Anulada

          --es de tipo derivacion
       And f_Es_Tipo_Derivacion(Asi.Idinstitucion,
                                Asi.Anio,
                                Asi.Numero,
                                Null) = Pkg_Siga_Constantes.Db_True_n

          --y que tiene:
       And (Exists
           --a) una designacion directa
            (Select *
               From Scs_Designa Des
              Where Asi.Idinstitucion = Des.Idinstitucion
                And Asi.Designa_Turno = Des.Idturno
                And Asi.Designa_Anio = Des.Anio
                And Asi.Designa_Numero = Des.Numero
                   --que no este anulada
                And Des.Estado <> c_Estado_Designacion_Anulada)
           --O
            Or Exists
           --b) una designacion indirecta (a traves de EJG)
            (Select *
               From Scs_Ejgdesigna Ejgdes, Scs_Designa Des
              Where Ejgdes.Idinstitucion = Des.Idinstitucion
                And Ejgdes.Idturno = Des.Idturno
                And Ejgdes.Aniodesigna = Des.Anio
                And Ejgdes.Numerodesigna = Des.Numero
                And Asi.Idinstitucion = Ejgdes.Idinstitucion
                And Asi.Ejgidtipoejg = Ejgdes.Idtipoejg
                And Asi.Ejganio = Ejgdes.Anioejg
                And Asi.Ejgnumero = Ejgdes.Numeroejg
                   --que no este anulada
                And Des.Estado <> c_Estado_Designacion_Anulada));

    If v_Esderivada > 0 Then
      Return Pkg_Siga_Constantes.Db_True_n;
    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Es_Asistencia_Derivada_Cr;

  Function f_Es_Tipo_Derivacion(p_Idinstitucion Scs_Actuacionasistencia.Idinstitucion%Type,
                                p_Anio          Scs_Actuacionasistencia.Anio%Type,
                                p_Numero        Scs_Actuacionasistencia.Numero%Type,
                                p_Idactuacion   Scs_Actuacionasistencia.Idactuacion%Type)
    Return Varchar2 Is

    c_Id_Gijon       Constant Scs_Asistencia.Idinstitucion%Type := 2027;
    c_Id_Albacete Constant Scs_Asistencia.Idinstitucion%Type := 2002;
    c_Id_Ciudad_Real Constant Scs_Asistencia.Idinstitucion%Type := 2020;
    c_Id_La_Rioja    Constant Scs_Asistencia.Idinstitucion%Type := 2058;

    c_Tipoactuacion_Judicial Constant Scs_Actuacionasistencia.Idtipoactuacion%Type := 2;
    c_Tipoasistencia_Jr      Constant Scs_Asistencia.Idtipoasistenciacolegio%Type := 3;
    v_Idtipo_Actuacion  Scs_Actuacionasistencia.Idtipoactuacion%Type;
    v_Idtipo_Asistencia Scs_Asistencia.Idtipoasistenciacolegio%Type;

  Begin
    If p_Idinstitucion = c_Id_Gijon Then
      Select Idtipoactuacion
        Into v_Idtipo_Actuacion
        From Scs_Actuacionasistencia
       Where Idinstitucion = p_Idinstitucion
         And Anio = p_Anio
         And Numero = p_Numero
         And Idactuacion = p_Idactuacion;

      If v_Idtipo_Actuacion = c_Tipoactuacion_Judicial Then
        Return Pkg_Siga_Constantes.Db_True_n;
      Else
        Return Pkg_Siga_Constantes.Db_False_n;
      End If;

    Elsif p_Idinstitucion in (c_Id_Albacete, c_Id_Ciudad_Real, c_Id_La_Rioja) Then
      Select Idtipoasistenciacolegio
        Into v_Idtipo_Asistencia
        From Scs_Asistencia
       Where Idinstitucion = p_Idinstitucion
         And Anio = p_Anio
         And Numero = p_Numero;

      If v_Idtipo_Asistencia = c_Tipoasistencia_Jr Then
        Return Pkg_Siga_Constantes.Db_True_n;
      Else
        Return Pkg_Siga_Constantes.Db_False_n;
      End If;

    Else
      Return Pkg_Siga_Constantes.Db_False_n;
    End If;
  End f_Es_Tipo_Derivacion;
  --FIN: Cambio para Asistencias que derivan en Designacion

  /*
   * Nombre: PROC_FACT_ACT_NOAPLICATIPO
   * Descripcion: Realiza la facturacion por actuaciones sin tipo
   * Notas:
   *   - Incluye la particularizacion de Derivacion en procedimiento para Gijon
   *
   * Algoritmo:
    --1.0 - inicializando acumuladores de Cabecera
    Para cada dia
      --2.0 - inicializando acumuladores de Dia
      Para cada actuacion
        --3.1+3.2 - calculando importe de Actuacion
        --3.3 - apuntando actuacion solo si no estaba ya facturada
        --2.1 - acumulando importes
      Fin para cada actuacion
      --2.2 - calculando importe de Dia
      --2.3 - apuntando dia
      --1.1 - acumulando importes
    Fin para cada dia
    --1.2 - calculando importe de Cabecera
    --1.3 - apuntando cabecera
   *
   * Historial:
   * - 2010-04-22: Adrian Ayala Gomez - rehecho completamente
   * - 2010-05-12: Adrian Ayala Gomez - no se aplicaba las derivaciones
   *   para Gijon porque no se habia dado valor al parametro
   *   usado en el metodo: arreglado
   * - 2010-07-15: Adrian Ayala Gomez - nuevo cambio en las funciones
   *   para incorporar la forma de Ciudad Real
   * - 2015-10-27: Jorge Paez - Se ha rehecho el codigo de UG y CG
   */
    Procedure PROC_FACT_ACT_NOAPLICATIPO(
        Indicematrizfacturable Number,
        p_Codretorno Out Varchar2,
        p_Datoserror Out Varchar2) Is
        
        -- Obtenemos las actuaciones realizadas y justificadas anteriormente a la fecha fin del ciclo de facturacion ordenadas por fecha de realizacion (Este cursor se utiliza para la facturacion por actuaciones)
        CURSOR C_ACTUACIONES_UG(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAFIN DATE, V_DIASAPLICABLES VARCHAR2, V_IDFACTURACION NUMBER) IS (
            SELECT ACT.IDTIPOACTUACION,
                ACT.FECHA,
                ACT.IDINSTITUCION,
                ACT.ANIO,
                ACT.NUMERO,
                ACT.IDACTUACION,
                1 AS FACTURADO,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION), 0) AS COSTEFIJO,
                --INI: Cambio para Asistencias que derivan en Designacion
                F_ES_ASISTENCIA_DERIVADA(ASI.IDINSTITUCION, ASI.ANIO, ASI.NUMERO, V_DATOS_FACTURACION.FECHADESDE, V_DATOS_FACTURACION.FECHAHASTA) AS DERIVA,
                F_ES_TIPO_DERIVACION(ACT.IDINSTITUCION, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION) AS ESJUDICIAL
                --FIN: Cambio para Asistencias que derivan en Designacion
            FROM SCS_ASISTENCIA ASI, 
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
                AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO            
                AND EXISTS ( --and ACT.Facturado = '1'
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                    WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                    AND FAC_ACT.ANIO = ACT.ANIO
                    AND FAC_ACT.NUMERO = ACT.NUMERO
                    AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                AND ACT.DIADESPUES = 'N'
                AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1
            UNION
            SELECT ACT.IDTIPOACTUACION,
                ACT.FECHA,
                ACT.IDINSTITUCION,
                ACT.ANIO,
                ACT.NUMERO,
                ACT.IDACTUACION,
                0 AS FACTURADO,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION), 0) AS COSTEFIJO,
                --INI: Cambio para Asistencias que derivan en Designacion
                F_ES_ASISTENCIA_DERIVADA(ASI.IDINSTITUCION, ASI.ANIO, ASI.NUMERO, V_DATOS_FACTURACION.FECHADESDE, V_DATOS_FACTURACION.FECHAHASTA) AS DERIVA,
                F_ES_TIPO_DERIVACION(ACT.IDINSTITUCION, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION) AS ESJUDICIAL
                --FIN: Cambio para Asistencias que derivan en Designacion
            FROM SCS_ASISTENCIA ASI, 
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
                AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO          
                AND NOT EXISTS ( --and (ACT.Facturado is null or ACT.Facturado = '0')
                    SELECT  1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                    WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC_ACT.ANIO = ACT.ANIO
                        AND FAC_ACT.NUMERO = ACT.NUMERO
                        AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                AND (ACT.ANULACION IS NULL OR ACT.ANULACION = '0')
                AND ACT.FECHAJUSTIFICACION IS NOT NULL 
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA) 
                AND ACT.VALIDADA = 1
                AND ACT.DIADESPUES = 'N'
                AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1)
            ORDER BY IDTIPOACTUACION, FECHA;

        --Constantes de Hitos
        Cons_Mot_Act CONSTANT NUMBER := 7;
        Cons_Mot_Actmas CONSTANT NUMBER := 29;
        Cons_Mot_Actmax CONSTANT NUMBER := 8;
        Cons_Mot_Actmaxmas CONSTANT NUMBER := 18;
        Cons_Mot_Actmin CONSTANT NUMBER := 19;
        Cons_Mot_Actminmas CONSTANT NUMBER := 34;
        Cons_Mot_Actnojudderiv CONSTANT NUMBER := 47;
        Cons_Mot_Actjudderiv CONSTANT NUMBER := 48;

        --Variables de configuracion de la guardia
        Conf_SeAgrupaPorCabecera VARCHAR2(1);
        Conf_Imp_Maximo NUMBER;
        Conf_Imp_MinimoIni NUMBER;
        Conf_Imp_Minimo NUMBER;
        Conf_Imp_Actuacion NUMBER;

        --Registros para iterar
        r_Dia Unidades_Guardia%Rowtype;
        r_Actuacion c_Actuaciones_Ug%Rowtype;

        --Importes, motivos y facturados
        Imp_Cab Number;
        Imp_Dia Number;
        Imp_Act Number;
        Mot_Cab Number;
        Mot_Dia Number;
        Mot_Act Number;
        Fac_Cab Number;
        Fac_Dia Number;
        Fac_Act Number;
        Fac_Act_Acumulado Number;
        Fac_Act_Acumulado_CG Number := 0;
        Fac_idapunte Number;
        Fac_Act_Pagada Number;

        --Contador para iterar sobre el dia
        Contador_Dia Number;

        --Costes fijos
        Costefijo_Cab Number;
        Costefijo_Dia Number;
        v_mensaje VARCHAR2(4000);

    Begin
        p_Datoserror := 'Antes de valores conf';

        --obteniendo valores de configuracion de la guardia
        Conf_SeAgrupaPorCabecera := V_CONFIG_GUARDIA.Agruparnopagaguardia;
        Conf_Imp_Maximo := NVL(V_CONFIG_GUARDIA.Importemaxactuacion, 0);
        Conf_Imp_MinimoIni := NVL(V_CONFIG_GUARDIA.Importeminactuacion, 0);
        Conf_Imp_Actuacion := NVL(V_CONFIG_GUARDIA.Importeactuacion, 0);

        --inicializando indices para avanzar por las matrices de salida
        Ind_Ug := 0;
        Ind_As := 0;
        Ind_Ac := 0;

        --1.0 - inicializando acumuladores de Cabecera
        Imp_Cab       := 0;
        Costefijo_Cab := 0;

        --inicializando contador de dia (solo sirve para las matrices de salida)
        Contador_Dia := 0;

        p_Datoserror := 'Antes del for de unidades';

        For r_Dia In Unidades_Guardia(
            m_Cg_Facturable(Indicematrizfacturable).Idinstitucion,
            m_Cg_Facturable(Indicematrizfacturable).Idturno,
            m_Cg_Facturable(Indicematrizfacturable).Idguardia,
            m_Cg_Facturable(Indicematrizfacturable).Idpersona,
            m_Cg_Facturable(Indicematrizfacturable).Fechainicio,
            V_CONFIG_GUARDIA.Diasnopagaguardia) Loop
            
            IF (r_Dia.Tiene_Asistquederivan <> Pkg_Siga_Constantes.Db_False_n) THEN -- Cuando un dia tenga asistencias derivadas no se aplica el importe minimo
                Conf_Imp_Minimo := 0;
            ELSE
                Conf_Imp_Minimo := Conf_Imp_MinimoIni;                            
            END IF;   

            --incrementando contador de dia
            Contador_Dia := Contador_Dia + 1;

            --2.0 - inicializando acumuladores de Dia
            Imp_Dia       := 0;
            Costefijo_Dia := 0;
            Fac_Act_Acumulado := 0;
            Fac_Act_Pagada := 0;

            p_Datoserror := 'Antes del for de actuaciones';
            For r_Actuacion In c_Actuaciones_Ug(
                r_Dia.Idinstitucion,
                r_Dia.Idturno,
                r_Dia.Idguardia,
                r_Dia.Idpersona,
                r_Dia.Fechafin,
                V_CONFIG_GUARDIA.Diasnopagaguardia,
                m_Cg_Facturable(Indicematrizfacturable).IDFACTURACION) Loop

                v_mensaje := r_Dia.Idinstitucion || ', ' ||
                    r_Dia.Idturno || ', ' ||
                    r_Dia.Idguardia || ', ' ||
                    r_Dia.Idpersona || ', ' ||
                    r_Dia.Fechafin || ', ' ||
                    r_actuacion.Anio || ', ' ||
                    r_actuacion.Numero || ', ' ||
                    r_actuacion.Idactuacion;

                p_Datoserror := 'Dia ' || v_mensaje;

                --3.1+3.2 - calculando importe de Actuacion
                If (r_Actuacion.Facturado <> Pkg_Siga_Constantes.Db_True_n) Then

                    --INI: Cambio para Asistencias que derivan en Designacion
                    If (r_Actuacion.Deriva = Pkg_Siga_Constantes.Db_True_a And r_Actuacion.Esjudicial = Pkg_Siga_Constantes.Db_False_a) Then
                        Imp_Act := Conf_Imp_Actuacion;
                        Mot_Act := Cons_Mot_Actnojudderiv;
                        Fac_Act := 0;

                    Elsif (r_Actuacion.Deriva = Pkg_Siga_Constantes.Db_True_a And r_Actuacion.Esjudicial = Pkg_Siga_Constantes.Db_True_a) Then
                        Imp_Act := 0;
                        Mot_Act := Cons_Mot_Actjudderiv;
                        Fac_Act := 0;

                    Else
                        --FIN: Cambio para Asistencias que derivan en Designacion
                        Imp_Act := Conf_Imp_Actuacion;
                        Mot_Act := Cons_Mot_Act;
                        Fac_Act := 0;
                        --INI: Cambio para Asistencias que derivan en Designacion
                    End If;
                     --FIN: Cambio para Asistencias que derivan en Designacion
                Else
                    Imp_Act := 0;
                    Mot_Act := Cons_Mot_Act;
                    Fac_Act := Conf_Imp_Actuacion;
                End If;

                p_Datoserror := v_mensaje || ', ' || 'Despues de calcular importe';

                --3.3 - apuntando actuacion solo si no estaba ya facturada
                If (r_Actuacion.Facturado <> Pkg_Siga_Constantes.Db_True_n) Then
                    Ind_Ac := Ind_Ac + 1;
                    m_Apunte_Ac(Ind_Ac).Idinstitucion := r_Actuacion.Idinstitucion;
                    m_Apunte_Ac(Ind_Ac).Anio := r_Actuacion.Anio;
                    m_Apunte_Ac(Ind_Ac).Numero := r_Actuacion.Numero;
                    m_Apunte_Ac(Ind_Ac).Idactuacion := r_Actuacion.Idactuacion;
                    m_Apunte_Ac(Ind_Ac).Facturado := r_Actuacion.Facturado;
                    m_Apunte_Ac(Ind_Ac).Costefijo := r_Actuacion.Costefijo;
                    m_Apunte_Ac(Ind_Ac).Contador := Contador_Dia;
                    m_Apunte_Ac(Ind_Ac).Importe := Imp_Act;
                    m_Apunte_Ac(Ind_Ac).Motivo := Mot_Act;

                else
                    p_Datoserror := v_mensaje || ', ' || 'Antes de primer select';
                    Begin
                        select sum(precioaplicado)
                            into Fac_Act_Acumulado
                        from fcs_fact_actuacionasistencia
                        where idinstitucion = r_Dia.Idinstitucion
                            and idpersona = r_Dia.Idpersona
                            and Anio = r_Actuacion.Anio
                            and trunc(fechaactuacion) = r_Dia.Fechafin;

                        Exception
                            When no_data_found Then
                                Fac_Act_Acumulado := 0;
                    End;                    

                    If (Conf_SeAgrupaPorCabecera = '1') Then
                        p_Datoserror := v_mensaje || ', ' || 'Antes de segundo select';
                        Begin
                            select idapunte
                                into Fac_idapunte
                            from fcs_fact_actuacionasistencia
                            where idinstitucion = r_Dia.Idinstitucion
                                and idpersona = r_Dia.Idpersona
                                and Anio = r_Actuacion.Anio
                                and trunc(fechaactuacion) = r_Dia.Fechafin
                                and rownum = 1;

                            Exception
                                When no_data_found Then
                                    Fac_idapunte := 0;
                        End;

                        p_Datoserror := v_mensaje || ', ' || 'Antes de tercer select';
                        Begin
                            select sum(precioaplicado)
                                into Fac_Act_Pagada
                            from fcs_fact_apunte
                            where idinstitucion = r_Dia.Idinstitucion
                                and idpersona = r_Dia.Idpersona
                                and idturno = r_Dia.Idturno
                                and idguardia = r_Dia.Idguardia
                                and idapunte =  Fac_idapunte;

                            Exception
                                When no_data_found Then
                                    Fac_Act_Pagada := 0;
                        End;
                    End if;
                End If;

                --2.1 - acumulando importes
                Imp_Dia := Imp_Dia + Imp_Act;
                If (r_Actuacion.Facturado <> Pkg_Siga_Constantes.Db_True_n) Then
                    Costefijo_Dia := Costefijo_Dia + r_Actuacion.Costefijo;
                End If;
            End Loop; -- Fin para cada Actuacion

            p_Datoserror := 'Antes de cargar las asistencias';

            --cargando asistencias (no interesan para importes)
            Proc_Carga_m_Apunte_Astipo_Cg(
                r_Dia.Idinstitucion,
                r_Dia.Idturno,
                r_Dia.Idguardia,
                r_Dia.Idpersona,
                r_Dia.Fechainicio,
                r_Dia.Fechafin,
                Contador_Dia,
                v_Codretorno2,
                v_Datoserror2);

            --2.2 - calculando importe de Dia
            Imp_Dia := Imp_Dia;
            Mot_Dia := Cons_Mot_Act;
            Fac_Dia := r_Dia.Importefacturado;
                
            p_Datoserror := 'Antes de calcular dia';                  

            IF (Conf_SeAgrupaPorCabecera <> '1') THEN -- Aplican maximos y minimos por dia
                IF (Fac_Dia <= 0.0) THEN -- Unidad de guardia sin facturar
                    
                    IF (Conf_Imp_Maximo > 0.0 And Conf_Imp_Maximo < Imp_Dia) THEN -- Supera el maximo
                        Mot_Dia := Cons_Mot_Actmax; -- AcMax                    
                        Imp_Dia := Conf_Imp_Maximo;                                        
                    
                    ELSIF (Conf_Imp_Minimo > 0.0 AND Conf_Imp_Minimo > Imp_Dia) THEN -- No llega al minimo
                        Mot_Dia := Cons_Mot_Actmin; -- AcMin
                        Imp_Dia := Conf_Imp_Minimo;        

                    ELSE
                        Mot_Dia := Cons_Mot_Act; -- Ac
                        Imp_Dia := Imp_Dia;
                    END IF;

                ELSIF (Conf_Imp_Maximo > 0.0 And Conf_Imp_Maximo < Imp_Dia + Fac_Act_Acumulado) THEN -- Supera el maximo (pero puede haber cambiado)
                    Mot_Dia := Cons_Mot_Actmaxmas; -- AcMax+
                    Imp_Dia := Conf_Imp_Maximo - Fac_Dia;
                            
                ELSIF (Conf_Imp_Minimo > 0.0 AND Conf_Imp_Minimo > Imp_Dia + Fac_Act_Acumulado) THEN -- No llega al minimo (pero puede haber cambiado)
                    Mot_Dia := Cons_Mot_Actminmas; -- AcMin+
                    Imp_Dia := Conf_Imp_Minimo - Fac_Dia;
                        
                ELSIF (Fac_Dia > Fac_Act_Acumulado) THEN -- Tiene un minimo previo, donde las actuaciones facturadas tienen un importe menor a lo facturado
                    Mot_Dia := Cons_Mot_Actmas; -- Ac+
                    Imp_Dia := Imp_Dia + Fac_Act_Acumulado - Fac_Dia;
                        
                ELSE
                     /* - Se ha facturado previamente 
                        - No supera el maximo
                        - Llega al minimo                        
                        - No tiene un minimo previo, donde las actuaciones facturadas tienen un importe menor a lo facturado
                     */
                     Mot_Dia := Cons_Mot_Actmas; -- Ac+ 
                    Imp_Dia := Imp_Dia;                
                END IF;

            ELSIF (Fac_Dia > 0) THEN
                Mot_Dia := Cons_Mot_Actmas;
            
            ELSE
                Mot_Dia := Cons_Mot_Act;
            END IF;                 

            --controlando que el importe nunca sea negativo
            If (Imp_Dia < 0) Then
                Imp_Dia := 0;
            End If;

            --2.3 - apuntando dia
            Ind_Ug := Ind_Ug + 1;
            m_Apunte_Ug(Ind_Ug).Idinstitucion := r_Dia.Idinstitucion;
            m_Apunte_Ug(Ind_Ug).Idturno := r_Dia.Idturno;
            m_Apunte_Ug(Ind_Ug).Idguardia := r_Dia.Idguardia;
            m_Apunte_Ug(Ind_Ug).Idpersona := r_Dia.Idpersona;
            m_Apunte_Ug(Ind_Ug).Fechainicio := r_Dia.Fechainicio;
            m_Apunte_Ug(Ind_Ug).Fechafin := r_Dia.Fechafin;
            m_Apunte_Ug(Ind_Ug).Facturado := r_Dia.Facturado;
            m_Apunte_Ug(Ind_Ug).Costefijo := Costefijo_Dia;
            m_Apunte_Ug(Ind_Ug).Contador := Contador_Dia;
            m_Apunte_Ug(Ind_Ug).Importe := Imp_Dia;
            m_Apunte_Ug(Ind_Ug).Motivo := Mot_Dia;

            --1.1 - acumulando importes
            Imp_Cab       := Imp_Cab + Imp_Dia;
            Costefijo_Cab := Costefijo_Cab + Costefijo_Dia;
            Fac_Act_Acumulado_CG := Fac_Act_Acumulado_CG + Fac_Act_Acumulado;
        End Loop; -- Fin para cada Dia

        p_Datoserror := 'Despues de cada dia';

        --1.2 - calculando importe de Cabecera
        Imp_Cab := Imp_Cab;
        Mot_Cab := Cons_Mot_Act;
        Fac_Cab := m_Cg_Facturable(Indicematrizfacturable).Importefacturadoasis;

        IF (Conf_SeAgrupaPorCabecera = '1') THEN -- Aplican maximos y minimos por Cabecera de Guardia
        
            IF (m_Cg_Facturable(Indicematrizfacturable).Tiene_Asistquederivan <> Pkg_Siga_Constantes.Db_False_n) THEN -- Cuando una cabecera tenga asistencias derivadas no se aplica el importe minimo
                Conf_Imp_Minimo := 0;
            ELSE
                Conf_Imp_Minimo := Conf_Imp_MinimoIni;                            
            END IF;   
            
            IF (Fac_Cab <= 0.0) THEN -- Cabecera de guardia sin facturar
                
                IF (Conf_Imp_Maximo > 0.0 And Conf_Imp_Maximo < Imp_Cab) THEN -- Supera el maximo
                    Mot_Cab := Cons_Mot_Actmax; -- AcMax
                    Imp_Cab := Conf_Imp_Maximo;
                
                ELSIF (Conf_Imp_Minimo > 0.0 AND Conf_Imp_Minimo > Imp_Cab) THEN -- No llega al minimo
                    Mot_Cab := Cons_Mot_Actmin; -- AcMin
                    Imp_Cab := Conf_Imp_Minimo;

                ELSE
                    Mot_Cab := Cons_Mot_Act; -- Ac
                    Imp_Cab := Imp_Cab;
                END IF;

            ELSIF (Conf_Imp_Maximo > 0.0 And Conf_Imp_Maximo < Imp_Cab + Fac_Act_Acumulado_CG) THEN -- Supera el maximo (pero puede haber cambiado)
                Mot_Cab := Cons_Mot_Actmaxmas; -- AcMax+
                Imp_Cab := Conf_Imp_Maximo - Fac_Cab;
                        
            ELSIF (Conf_Imp_Minimo > 0.0 AND Conf_Imp_Minimo > Imp_Cab + Fac_Act_Acumulado_CG) THEN -- No llega al minimo (pero puede haber cambiado)
                Mot_Cab := Cons_Mot_Actminmas; -- AcMin+
                Imp_Cab := Conf_Imp_Minimo - Fac_Cab;
                    
            ELSIF (Fac_Cab > Fac_Act_Acumulado_CG) THEN -- Tiene un minimo previo, donde las actuaciones facturadas tienen un importe menor a lo facturado
                Mot_Cab := Cons_Mot_Actmas; -- Ac+
                Imp_Cab := Imp_Cab + Fac_Act_Acumulado_CG - Fac_Cab;
                    
            ELSE
                 /* - Se ha facturado previamente 
                    - No supera el maximo
                    - Llega al minimo                        
                    - No tiene un minimo previo, donde las actuaciones facturadas tienen un importe menor a lo facturado
                 */
                 Mot_Cab := Cons_Mot_Actmas; -- Ac+
                 Imp_Cab := Imp_Cab;                            
            END IF;

        ELSIF (Fac_Cab > 0) THEN
            Mot_Cab := Cons_Mot_Actmas;
            
        ELSE
            Mot_Cab := Cons_Mot_Act;
        END IF;

        --controlando que el importe nunca sea negativo
        IF (Imp_Cab < 0) THEN
            Imp_Cab := 0;
        END IF;

        p_Datoserror := 'Antes de calcular cabecera';

        --1.3 - apuntando cabecera
        Ind_Cg := 1;
        m_Apunte_Cg(Ind_Cg).Idinstitucion := m_Cg_Facturable(Indicematrizfacturable).Idinstitucion;
        m_Apunte_Cg(Ind_Cg).Idturno := m_Cg_Facturable(Indicematrizfacturable).Idturno;
        m_Apunte_Cg(Ind_Cg).Idguardia := m_Cg_Facturable(Indicematrizfacturable).Idguardia;
        m_Apunte_Cg(Ind_Cg).Idpersona := m_Cg_Facturable(Indicematrizfacturable).Idpersona;
        m_Apunte_Cg(Ind_Cg).Fechainicio := m_Cg_Facturable(Indicematrizfacturable).Fechainicio;
        m_Apunte_Cg(Ind_Cg).Fechafin := m_Cg_Facturable(Indicematrizfacturable).Fechafin;
        m_Apunte_Cg(Ind_Cg).Facturado := m_Cg_Facturable(Indicematrizfacturable).Facturado;
        m_Apunte_Cg(Ind_Cg).Contador := 1;
        m_Apunte_Cg(Ind_Cg).Costefijo := Costefijo_Cab;

        If (Conf_SeAgrupaPorCabecera = '1') Then
            m_Apunte_Cg(Ind_Cg).Idtipoapunte := 'CG-';
        Else
            m_Apunte_Cg(Ind_Cg).Idtipoapunte := 'CG+';
        End If;

        m_Apunte_Cg(Ind_Cg).Importe := Imp_Cab;
        m_Apunte_Cg(Ind_Cg).Motivo := Mot_Cab;

        p_Datoserror := 'PROCEDURE PROC_FACT_ACT_NOAPLICATIPO: ha finalizado correctamente.';
        p_Codretorno := To_Char(0);

        Exception
            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := p_Datoserror || ' ' || Sqlerrm;
    End PROC_FACT_ACT_NOAPLICATIPO;

  /***************************************************************************/
  /* Nombre:      PROC_FACT_ACT_APLICATIPO
  /* Descripcion: Se encarga de cargar las tablas de memoria cuando
  /*              facturamos por Actuaciones y aplica tipos de actuaciones.
  /*
  /* Parametros              IN/OUT   Descripcion              Tipo de Datos */
  /* ----------------------  ------   ----------------------   ------------- */
  /* indiceMatrizFacturable   IN      Indica sobre que         NUMBER
  /*                                  registro se obtiene la
  /*                                  config. de facturacion
  /* P_CODRETORNO             OUT     Devuelve 0 en caso de    VARCHAR2(10)
  /*                                  que todo termine OK
  /*                                  En caso de error
  /*                                  devuelve el codigo de
  /*                                  error Oracle corresp.
  /* P_DATOSERROR             OUT     Devuelve null en caso    VARCHAR2(200)
  /*                                  de que todo termine OK
  /*                                  En caso de error
  /*                                  devuelve el codigo de
  /*                                  error Oracle corresp.
  /*
  /* Fecha Modif.  Autor Modificacion   Descripcion Modificacion             */
  /* ------------  -------------------  ------------------------------------ */
  /* 25/04/2006    Pilar Duran Munoz    Creacion
  /* 11/08/2008    Adrian Ayala Gomez   Se incluye la diferencia entre
  /*                                    Agrupar o no y la seleccion de dias
  /*                                    Aunque se ha reconstruido TODO
  /***************************************************************************/

    PROCEDURE PROC_FACT_ACT_APLICATIPO(
        indiceMatrizFacturable number,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        -- Obtenemos las actuaciones realizadas y justificadas anteriormente a la fecha fin del ciclo de facturacion para cada idtipoactuacion ordenadas por fecha de realizacion (Este cursor se utiliza para la facturacion por actuaciones)
        CURSOR C_ACTUACIONESTIPO_UG(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAFIN DATE, V_IDTIPOACTUACION NUMBER, V_ANIO NUMBER, V_NUMERO NUMBER, V_DIASAPLICABLES VARCHAR2, V_IDFACTURACION NUMBER) IS(
            SELECT ACT.IDTIPOACTUACION,
                ACT.FECHA,
                ACT.IDINSTITUCION,
                ACT.ANIO,
                ACT.NUMERO,
                ACT.IDACTUACION,
                1 AS FACTURADO,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION), 0) AS COSTEFIJO,
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION, 0) AS IMPORTE,
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION, 1) AS IMPORTEMAX
            FROM SCS_ASISTENCIA ASI, 
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
                AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO                
                AND EXISTS ( --and ACT.Facturado = '1'
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                    WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC_ACT.ANIO = ACT.ANIO
                        AND FAC_ACT.NUMERO = ACT.NUMERO
                        AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                AND ACT.IDTIPOACTUACION = V_IDTIPOACTUACION
                AND ASI.ANIO = V_ANIO
                AND ASI.NUMERO = V_NUMERO
                AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1
            UNION
            SELECT ACT.IDTIPOACTUACION,
                ACT.FECHA,
                ACT.IDINSTITUCION,
                ACT.ANIO,
                ACT.NUMERO,
                ACT.IDACTUACION,
                0 AS FACTURADO,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION), 0) AS COSTEFIJO,
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION, 0) AS IMPORTE,
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_FECHAFIN, V_IDFACTURACION, 1) AS IMPORTEMAX
            FROM SCS_ASISTENCIA ASI, 
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
                AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO                
                AND NOT EXISTS( --and (ACT.Facturado is null or ACT.Facturado = '0')
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                    WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC_ACT.ANIO = ACT.ANIO
                        AND FAC_ACT.NUMERO = ACT.NUMERO
                        AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                AND (ACT.ANULACION IS NULL OR ACT.ANULACION = '0')
                AND ACT.IDTIPOACTUACION = V_IDTIPOACTUACION
                AND ASI.ANIO = V_ANIO
                AND ASI.NUMERO = V_NUMERO
                AND ACT.FECHAJUSTIFICACION IS NOT NULL 
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA) 
                AND ACT.VALIDADA = 1
                AND ACT.DIADESPUES = 'N'
                AND FUN_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1)
            ORDER BY IDTIPOACTUACION, FECHA;        

        -- Matriz de donde se extrae la configuracion de la guardia
        MFACTURABLE MATRICE_CG_FACTURABLE;

        -- Contadores para los apuntes
        contador           number;
        contadorDiaGuardia number;

        -- Importes cogidos del Tipo
        importeTipoActuacion number;
        importeTipoActMax    number;

        -- Costes Fijos
        costeFijoCG           number;
        costeFijoUG           number;
        costeFijoUGDiaGUardia number;
        costeFijoAc           number;

        -- Acumuladores
        totalImporteActuaciones number;
        totalImporteTipos       number;
        totalImporteUGs         number;
        totalImporteNuevasActuacion number;

        -- Acumulador para el apunte de Asist (solo tramite)
        totalImporteActuacionesAsist number;

    BEGIN

        -- Matriz de donde se extrae la configuracion de la guardia
        MFACTURABLE := M_CG_FACTURABLE(indiceMatrizFacturable);

        -- Indices de matrices de apuntes
        IND_UG := 0;
        IND_AS := 0;
        IND_AC := 0;

        -- Contadores de UG
        contador := 0;
        contadorDiaGuardia := 0; -- RGG cambios

        -- Costes Fijos de UG
        costeFijoCG := 0;

        -- Acumulador de UGs
        totalImporteUGs := 0;

        FOR V_UNIDADES_GUARDIA IN UNIDADES_GUARDIA_TP(
            MFACTURABLE.IDINSTITUCION,
            MFACTURABLE.IDTURNO,
            MFACTURABLE.IDGUARDIA,
            MFACTURABLE.IDPERSONA,
            MFACTURABLE.FECHAINICIO,
            V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA) LOOP

            -- Contadores de UG
            contador := contador + 1;
            contadorDiaGuardia := contador;

            -- Costes Fijos de UG
            costeFijoUGDiaGuardia := 0;

            -- Acumulador de Tipos
            totalImporteTipos := 0;

            FOR V_TIPOACTUACIONES_UG IN C_TIPOACTUACIONES_UG(
                V_UNIDADES_GUARDIA.IDINSTITUCION,
                V_UNIDADES_GUARDIA.IDTURNO,
                V_UNIDADES_GUARDIA.IDGUARDIA,
                V_UNIDADES_GUARDIA.IDPERSONA,
                V_UNIDADES_GUARDIA.FECHAFIN,
                V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA) LOOP

                -- Contador de Tipo
                contador := contador + 1;

                -- Costes Fijos de Tipo
                costeFijoUG := 0;
                costeFijoAc := 0;

                -- Acumulador de Actuaciones
                totalImporteActuaciones := 0;

                FOR V_ASISTENCIAS_UG IN C_ASISTENCIAS_UG(
                    V_UNIDADES_GUARDIA.IDINSTITUCION,
                    V_UNIDADES_GUARDIA.IDTURNO,
                    V_UNIDADES_GUARDIA.IDGUARDIA,
                    V_UNIDADES_GUARDIA.IDPERSONA,
                    V_UNIDADES_GUARDIA.FECHAINICIO,
                    V_UNIDADES_GUARDIA.FECHAFIN,
                    V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA) LOOP

                    -- Acumulador de Asistencias (solo para el apunte de Asistencia)
                    totalImporteActuacionesAsist := 0;
                    totalImporteNuevasActuacion := 0;

                    FOR V_ACTUACIONES_UG IN C_ACTUACIONESTIPO_UG(
                        V_ASISTENCIAS_UG.IDINSTITUCION,
                        V_UNIDADES_GUARDIA.IDTURNO,
                        V_UNIDADES_GUARDIA.IDGUARDIA,
                        V_UNIDADES_GUARDIA.IDPERSONA,
                        V_UNIDADES_GUARDIA.FECHAFIN,
                        V_TIPOACTUACIONES_UG.tipoactuacion,
                        V_ASISTENCIAS_UG.anio,
                        V_ASISTENCIAS_UG.numero,
                        V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA,
                        MFACTURABLE.IDFACTURACION) LOOP

                        -- Obtenemos el importe de la asistencia, en el caso de que fuera nulo el importe de la asistencia seria el de la Guardia
                        importeTipoActuacion := NVL(NVL(NVL(V_ACTUACIONES_UG.IMPORTE, V_CONFIG_GUARDIA.IMPORTETIPOACTUACION), V_CONFIG_GUARDIA.IMPORTEACTUACION), 0);

                        -- El importe del Tipo solo se puede calcular en la propia asistencia
                        importeTipoActMax := NVL(NVL(V_ACTUACIONES_UG.IMPORTEMAX, V_CONFIG_GUARDIA.IMPORTEMAXACTUACION), 0);

                        -- Comenzando el apunte en la matriz de memoria: M_APUNTE_AC
                        if (V_ACTUACIONES_UG.facturado <> pkg_siga_constantes.DB_TRUE_A) then
                            costeFijoAc := costeFijoAc + V_ACTUACIONES_UG.costefijo;
                        end if;

                        IND_AC := IND_AC + 1;
                        M_APUNTE_AC(IND_AC).IDINSTITUCION := V_ACTUACIONES_UG.IDINSTITUCION;
                        M_APUNTE_AC(IND_AC).ANIO := V_ACTUACIONES_UG.ANIO;
                        M_APUNTE_AC(IND_AC).NUMERO := V_ACTUACIONES_UG.NUMERO;
                        M_APUNTE_AC(IND_AC).IDACTUACION := V_ACTUACIONES_UG.IDACTUACION;
                        M_APUNTE_AC(IND_AC).FACTURADO := V_ACTUACIONES_UG.FACTURADO;
                        M_APUNTE_AC(IND_AC).CONTADOR := CONTADOR;
                        M_APUNTE_AC(IND_AC).costefijo := V_ACTUACIONES_UG.costefijo;

                        -- Acumulador de Asistencias
                        totalImporteActuacionesAsist := totalImporteActuacionesAsist + importeTipoActuacion; -- este acumulador solo vale para el apunte de Asist
                        totalImporteActuaciones := totalImporteActuaciones + importeTipoActuacion;
                        M_APUNTE_AC(IND_AC).IMPORTE := importeTipoActuacion;

                        -- Comprobando si ESTA FACTURADO O NO
                        IF (V_ACTUACIONES_UG.FACTURADO is not null and V_ACTUACIONES_UG.FACTURADO = pkg_siga_constantes.DB_TRUE_A) THEN
                            M_APUNTE_AC(IND_AC).MOTIVO := 37; --AcNoDev+
                        ELSE
                            M_APUNTE_AC(IND_AC).MOTIVO := 22; --AcTp
                            M_APUNTE_AC(IND_AC).IMPORTE := importeTipoActuacion;
                            totalImporteNuevasActuacion := totalImporteNuevasActuacion + importeTipoActuacion;
                        END IF;
                    END LOOP; -- Fin para actuacion

                    -- Comenzando apunte en la matriz de memoria: M_APUNTE_AS
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).IDINSTITUCION := V_ASISTENCIAS_UG.IDINSTITUCION;
                    M_APUNTE_AS(IND_AS).ANIO := V_ASISTENCIAS_UG.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := V_ASISTENCIAS_UG.NUMERO;
                    M_APUNTE_AS(IND_AS).FACTURADO := V_ASISTENCIAS_UG.FACTURADO;
                    M_APUNTE_AS(IND_AS).CONTADOR := CONTADOR;

                    -- Acumulador de Asistencias (ya sin valor de acumulador)
                    M_APUNTE_AS(IND_AS).IMPORTE := totalImporteActuacionesAsist;

                    -- Comprobando si ESTA FACTURADO O NO
                    IF (V_ASISTENCIAS_UG.FACTURADO is not null and V_ASISTENCIAS_UG.FACTURADO = pkg_siga_constantes.DB_TRUE_A) THEN
                        M_APUNTE_AS(IND_AS).MOTIVO := 37; --AcNoDev+
                    ELSE
                        M_APUNTE_AS(IND_AS).MOTIVO := 22; --AcTp
                    END IF;
                END LOOP; -- Fin para asistencia

                -- Calculo de Costes Fijos
                costeFijoUG := costeFijoUG + costeFijoAc;
                costeFijoUGDiaGuardia := costeFijoUGDiaGuardia + costeFijoUG;
                costeFijoCG := costeFijoCG + costeFijoUG;

                -- Comenzando apunte en la matriz de memoria: M_APUNTE_UG
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDADES_GUARDIA.IDINSTITUCION;
                M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDADES_GUARDIA.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDADES_GUARDIA.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDADES_GUARDIA.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDADES_GUARDIA.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
                M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDADES_GUARDIA.FACTURADO;
                M_APUNTE_UG(IND_UG).CONTADOR := CONTADOR;
                M_APUNTE_UG(IND_UG).COSTEFIJO := COSTEFIJOUG;               

                -- Acumulador de Tipos
                if (importeTipoActMax is not null
                    and importeTipoActMax <> 0.0
                    and totalImporteActuaciones > importeTipoActMax
                    and V_CONFIG_GUARDIA.IMPORTETIPOACTUACION is not null) then

                    -- Supera el Maximo por Tipo
                    totalImporteTipos := totalImporteTipos + importeTipoActMax;
                    M_APUNTE_UG(IND_UG).IMPORTE := importeTipoActMax;

                    -- Comprobando si ESTA FACTURADO O NO
                    if (V_UNIDADES_GUARDIA.Facturado is not null and (V_UNIDADES_GUARDIA.Facturado = 'S' OR V_UNIDADES_GUARDIA.Facturado = '1')) then

                        M_APUNTE_UG(IND_UG).MOTIVO := 42; --AcTpMax+;

                        if (totalImporteActuaciones > totalImporteNuevasActuacion) then
                            totalImporteTipos := importeTipoActMax - (totalImporteActuaciones - totalImporteNuevasActuacion);
                        else
                            totalImporteTipos := importeTipoActMax - (totalImporteNuevasActuacion - totalImporteActuaciones);
                        end if;

                        if (totalImporteTipos < 0) then
                            totalImporteTipos := 0;
                        end if;

                    else
                        M_APUNTE_UG(IND_UG).MOTIVO := 23; --AcTpMax;
                    end if;

                else -- No supera el Maximo por Tipo
                    totalImporteTipos := totalImporteTipos + totalImporteActuaciones;
                    M_APUNTE_UG(IND_UG).IMPORTE := totalImporteActuaciones;

                    -- Comprobando si ESTA FACTURADO O NO
                    if (V_UNIDADES_GUARDIA.Facturado is not null and (V_UNIDADES_GUARDIA.Facturado = 'S' OR V_UNIDADES_GUARDIA.Facturado = '1')) then
                        M_APUNTE_UG(IND_UG).MOTIVO := 30; --AcTp+;
                    else
                        M_APUNTE_UG(IND_UG).MOTIVO := 22; --AcTp;
                    end if;
                end if;
            END LOOP; -- Fin tipo asistencias

            -- Comenzando apunte en la matriz de memoria: M_APUNTE_UG
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDINSTITUCION := V_UNIDADES_GUARDIA.IDINSTITUCION;
            M_APUNTE_UG(IND_UG).IDTURNO := V_UNIDADES_GUARDIA.IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := V_UNIDADES_GUARDIA.IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := V_UNIDADES_GUARDIA.IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := V_UNIDADES_GUARDIA.FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := V_UNIDADES_GUARDIA.FECHAFIN;
            M_APUNTE_UG(IND_UG).FACTURADO := V_UNIDADES_GUARDIA.FACTURADO;
            --identificador
            M_APUNTE_UG(IND_UG).CONTADOR := contadorDiaGuardia;
            M_APUNTE_UG(IND_UG).COSTEFIJO := costeFijoUGDiaGuardia;

            -- Acumulador de UGs
            if (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1') then

                -- Se agrupa, o sea que los Maximos y Minimos se aplican solo en la CG
                totalImporteUGs := totalImporteUGs + totalImporteTipos;
                M_APUNTE_UG(IND_UG).IMPORTE := totalImporteTipos;

                -- Comprobando si ESTA FACTURADO O NO
                if (V_UNIDADES_GUARDIA.Facturado is not null and (V_UNIDADES_GUARDIA.Facturado = 'S' OR V_UNIDADES_GUARDIA.Facturado = '1')) then
                    M_APUNTE_UG(IND_UG).MOTIVO := 29; --Ac+
                else
                    M_APUNTE_UG(IND_UG).MOTIVO := 7; --Ac
                end if;

            else
                -- NO Se agrupa, o sea que los Maximos y Minimos se aplican aqui, en la UG
                if (V_CONFIG_GUARDIA.IMPORTEMAXACTUACION is not null
                    and V_CONFIG_GUARDIA.IMPORTEMAXACTUACION <> 0.0
                    and totalImporteTipos > V_CONFIG_GUARDIA.IMPORTEMAXACTUACION) then

                    -- Supera el maximo
                    totalImporteUGs := totalImporteUGs + V_CONFIG_GUARDIA.IMPORTEMAXACTUACION;
                    M_APUNTE_UG(IND_UG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMAXACTUACION;

                    -- Comprobando si ESTA FACTURADO O NO
                    if (V_UNIDADES_GUARDIA.Facturado is not null and (V_UNIDADES_GUARDIA.Facturado = 'S' OR V_UNIDADES_GUARDIA.Facturado = '1')) then
                        M_APUNTE_UG(IND_UG).MOTIVO := 18; --AcMax+;
                    else
                        M_APUNTE_UG(IND_UG).MOTIVO := 8; --AcMax;
                    end if;

                elsif (V_CONFIG_GUARDIA.IMPORTEMINACTUACION is not null
                    and V_CONFIG_GUARDIA.IMPORTEMINACTUACION <> 0.0
                    and totalImporteTipos < V_CONFIG_GUARDIA.IMPORTEMINACTUACION) then

                    -- No llega al minimo
                    totalImporteUGs := totalImporteUGs + V_CONFIG_GUARDIA.IMPORTEMINACTUACION;
                    M_APUNTE_UG(IND_UG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMINACTUACION;

                    -- Comprobando si ESTA FACTURADO O NO
                    if (V_UNIDADES_GUARDIA.Facturado is not null and (V_UNIDADES_GUARDIA.Facturado = 'S' OR V_UNIDADES_GUARDIA.Facturado = '1')) then
                        M_APUNTE_UG(IND_UG).MOTIVO := 34; --AcMin+
                    else
                        M_APUNTE_UG(IND_UG).MOTIVO := 19; --AcMin
                    end if;

                else -- No supera maximo ni esta por debajo de minimo
                    totalImporteUGs := totalImporteUGs + totalImporteTipos;
                    M_APUNTE_UG(IND_UG).IMPORTE := totalImporteTipos;

                    -- Comprobando si ESTA FACTURADO O NO
                    if (V_UNIDADES_GUARDIA.Facturado is not null and (V_UNIDADES_GUARDIA.Facturado = 'S' OR V_UNIDADES_GUARDIA.Facturado = '1')) then
                        M_APUNTE_UG(IND_UG).MOTIVO := 29; --Ac+
                    else
                        M_APUNTE_UG(IND_UG).MOTIVO := 7; --Ac
                    end if;
                end if; -- comprobacion de maximo y minimo
            end if; -- if (agrupar)
        END LOOP; -- Fin para UG

        -- Comenzando apunte en la matriz de memoria: M_APUNTE_CG
        IND_CG := 1;
        -- Por cada recorrido de la matriz de cabecera facturable solo habra un registro en la matriz de apunte de CG por eso el indice siempre sera 1
        M_APUNTE_CG(IND_CG).idinstitucion := MFACTURABLE.idinstitucion;
        M_APUNTE_CG(IND_CG).idturno := MFACTURABLE.idturno;
        M_APUNTE_CG(IND_CG).idguardia := MFACTURABLE.idguardia;
        M_APUNTE_CG(IND_CG).idpersona := MFACTURABLE.idpersona;
        M_APUNTE_CG(IND_CG).fechainicio := MFACTURABLE.fechainicio;
        M_APUNTE_CG(IND_CG).fechafin := MFACTURABLE.fechafin;
        M_APUNTE_CG(IND_CG).facturado := MFACTURABLE.facturado;
        M_APUNTE_CG(IND_CG).contador := 1;
        M_APUNTE_CG(IND_CG).costefijo := costeFijoCG;

        -- Indicando como se deberan mostrar los apuntes (agrupados o no)
        if (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '1') then
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG-';
        else
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';
        end if;

        -- AAG: De Acumulador de CG
        if (V_CONFIG_GUARDIA.AGRUPARNOPAGAGUARDIA = '0') then

            -- NO Se agrupa, o sea que los Maximos y Minimos se aplicaron en las UGs
            M_APUNTE_CG(IND_CG).IMPORTE := totalImporteUGs;

            -- Comprobando si ESTA FACTURADO O NO
            if (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado is not null and (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = 'S' OR M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = '1')) then
                M_APUNTE_CG(IND_CG).MOTIVO := 29; --Ac+
            else
                M_APUNTE_CG(IND_CG).MOTIVO := 7; --Ac
            end if;

        else -- Se agrupa, o sea que los Maximos y Minimos se aplican aqui, en la CG
            if (V_CONFIG_GUARDIA.IMPORTEMAXACTUACION is not null
                and V_CONFIG_GUARDIA.IMPORTEMAXACTUACION <> 0.0
                and totalImporteUGs > V_CONFIG_GUARDIA.IMPORTEMAXACTUACION) then

                -- Supera el maximo
                M_APUNTE_CG(IND_CG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMAXACTUACION;

                -- Comprobando si ESTA FACTURADO O NO
                if (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado is not null and (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = 'S' OR M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = '1')) then
                    M_APUNTE_CG(IND_CG).MOTIVO := 18; --AcMax+;
                else
                    M_APUNTE_CG(IND_CG).MOTIVO := 8; --AcMax;
                end if;

            elsif (V_CONFIG_GUARDIA.IMPORTEMINACTUACION is not null
                and V_CONFIG_GUARDIA.IMPORTEMINACTUACION <> 0.0
                and totalImporteUGs < V_CONFIG_GUARDIA.IMPORTEMINACTUACION) then

                -- No llega al minimo
                M_APUNTE_CG(IND_CG).IMPORTE := V_CONFIG_GUARDIA.IMPORTEMINACTUACION;

                -- Comprobando si ESTA FACTURADO O NO
                if (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado is not null and (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = 'S' OR M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = '1')) then
                    M_APUNTE_CG(IND_CG).MOTIVO := 34; --AcMin+
                else
                    M_APUNTE_CG(IND_CG).MOTIVO := 19; --AcMin
                end if;

            else -- No supera maximo ni esta por debajo de minimo
                M_APUNTE_CG(IND_CG).IMPORTE := totalImporteUGs;

                -- Comprobando si ESTA FACTURADO O NO
                if (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado is not null and (M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = 'S' OR M_CG_FACTURABLE(indiceMatrizFacturable).Facturado = '1')) then
                    M_APUNTE_CG(IND_CG).MOTIVO := 29; --Ac+
                else
                    M_APUNTE_CG(IND_CG).MOTIVO := 7; --Ac
                end if;
            end if; -- comprobacion de maximo y minimo
        end if; -- if (agrupar)

        P_DATOSERROR := 'PROCEDURE PROC_FACT_ACT_APLICATIPO: ha finalizado correctamente.';
        P_CODRETORNO := TO_CHAR(0);

        EXCEPTION
            when others then
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FACT_ACT_APLICATIPO;

  PROCEDURE PROC_GENERA_APUNTE_ACT(P_IDINSTITUCION SCS_ACTUACIONASISTENCIA.IDINSTITUCION%type,
                                   P_ANIO          SCS_ACTUACIONASISTENCIA.ANIO%type,
                                   P_NUMERO        SCS_ACTUACIONASISTENCIA.NUMERO%type,
                                   P_IDACTUACION   SCS_ACTUACIONASISTENCIA.IDACTUACION%type,
                                   P_FACTURADO     SCS_ACTUACIONASISTENCIA.FACTURADO%type,
                                   P_CONTADOR      number,
                                   P_COSTEFIJO     FCS_FACT_ACTUACIONASISTENCIA.PRECIOCOSTESFIJOS%type,
                                   P_MOTIVO        FCS_FACT_ACTUACIONASISTENCIA.MOTIVO%type,
                                   P_IMPORTE       FCS_FACT_ACTUACIONASISTENCIA.PRECIOAPLICADO%type) IS
  BEGIN
    IND_AC := IND_AC + 1;

    M_APUNTE_AC(IND_AC).IDINSTITUCION := P_IDINSTITUCION;
    M_APUNTE_AC(IND_AC).ANIO := P_ANIO;
    M_APUNTE_AC(IND_AC).NUMERO := P_NUMERO;
    M_APUNTE_AC(IND_AC).IDACTUACION := P_IDACTUACION;
    M_APUNTE_AC(IND_AC).FACTURADO := P_FACTURADO;
    M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR;
    M_APUNTE_AC(IND_AC).COSTEFIJO := P_COSTEFIJO;
    M_APUNTE_AC(IND_AC).MOTIVO := P_MOTIVO;
    M_APUNTE_AC(IND_AC).IMPORTE := P_IMPORTE;
  END PROC_GENERA_APUNTE_ACT;

  PROCEDURE PROC_GENERA_APUNTE_ASIST(P_IDINSTITUCION SCS_ASISTENCIA.IDINSTITUCION%type,
                                     P_ANIO          SCS_ASISTENCIA.ANIO%type,
                                     P_NUMERO        SCS_ASISTENCIA.NUMERO%type,
                                     P_FACTURADO     SCS_ASISTENCIA.FACTURADO%type,
                                     P_CONTADOR      number,
                                     P_MOTIVO        FCS_FACT_ASISTENCIA.MOTIVO%type,
                                     P_IMPORTE       FCS_FACT_ASISTENCIA.PRECIOAPLICADO%type) IS
  BEGIN
    IND_AS := IND_AS + 1;

    M_APUNTE_AS(IND_AS).IDINSTITUCION := P_IDINSTITUCION;
    M_APUNTE_AS(IND_AS).ANIO := P_ANIO;
    M_APUNTE_AS(IND_AS).NUMERO := P_NUMERO;
    M_APUNTE_AS(IND_AS).FACTURADO := P_FACTURADO;
    M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR;
    M_APUNTE_AS(IND_AS).MOTIVO := P_MOTIVO;
    M_APUNTE_AS(IND_AS).IMPORTE := P_IMPORTE;
  END PROC_GENERA_APUNTE_ASIST;

  PROCEDURE PROC_GENERA_APUNTE_GUARDIA(P_IDINSTITUCION        SCS_GUARDIASCOLEGIADO.IDINSTITUCION%type,
                                       P_IDTURNO              SCS_GUARDIASCOLEGIADO.IDTURNO%type,
                                       P_IDGUARDIA            SCS_GUARDIASCOLEGIADO.IDGUARDIA%type,
                                       P_IDPERSONA            SCS_GUARDIASCOLEGIADO.IDPERSONA%type,
                                       P_FECHAINICIO          SCS_GUARDIASCOLEGIADO.FECHAINICIO%type,
                                       P_FECHAFIN             SCS_GUARDIASCOLEGIADO.FECHAFIN%type,
                                       P_FACTURADO            SCS_GUARDIASCOLEGIADO.FACTURADO%type,
                                       P_CONTADOR             number,
                                       P_COSTEFIJO            FCS_FACT_GUARDIASCOLEGIADO.PRECIOCOSTESFIJOS%type,
                                       P_MOTIVO               FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%type,
                                       P_IMPORTE              FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%type) IS
  BEGIN
    IND_UG := IND_UG + 1;

    M_APUNTE_UG(IND_UG).IDINSTITUCION := P_IDINSTITUCION;
    M_APUNTE_UG(IND_UG).IDTURNO := P_IDTURNO;
    M_APUNTE_UG(IND_UG).IDGUARDIA := P_IDGUARDIA;
    M_APUNTE_UG(IND_UG).IDPERSONA := P_IDPERSONA;
    M_APUNTE_UG(IND_UG).FECHAINICIO := P_FECHAINICIO;
    M_APUNTE_UG(IND_UG).FECHAFIN := P_FECHAFIN;
    M_APUNTE_UG(IND_UG).FACTURADO := P_FACTURADO;
    M_APUNTE_UG(IND_UG).CONTADOR := P_CONTADOR;
    M_APUNTE_UG(IND_UG).COSTEFIJO := P_COSTEFIJO;
    M_APUNTE_UG(IND_UG).MOTIVO := P_MOTIVO;
    M_APUNTE_UG(IND_UG).IMPORTE := P_IMPORTE;
  END PROC_GENERA_APUNTE_GUARDIA;

  PROCEDURE PROC_GENERA_APUNTE_CABECERA(P_IDINSTITUCION        SCS_CABECERAGUARDIAS.IDINSTITUCION%type,
                                        P_IDTURNO              SCS_CABECERAGUARDIAS.IDTURNO%type,
                                        P_IDGUARDIA            SCS_CABECERAGUARDIAS.IDGUARDIA%type,
                                        P_IDPERSONA            SCS_CABECERAGUARDIAS.IDPERSONA%type,
                                        P_FECHAINICIO          SCS_CABECERAGUARDIAS.FECHAINICIO%type,
                                        P_FECHAFIN             SCS_CABECERAGUARDIAS.FECHA_FIN%type,
                                        P_FACTURADO            SCS_CABECERAGUARDIAS.FACTURADO%type,
                                        P_COSTEFIJO            FCS_FACT_APUNTE.PRECIOCOSTESFIJOS%type,
                                        P_MOTIVO               FCS_FACT_APUNTE.MOTIVO%type,
                                        P_IMPORTE              FCS_FACT_APUNTE.PRECIOAPLICADO%type,
                                        P_IDTIPOAPUNTE         FCS_FACT_APUNTE.IDTIPOAPUNTE%type) IS
  BEGIN
    IND_CG := IND_CG + 1;

    M_APUNTE_CG(IND_CG).IDINSTITUCION := P_IDINSTITUCION;
    M_APUNTE_CG(IND_CG).IDTURNO := P_IDTURNO;
    M_APUNTE_CG(IND_CG).IDGUARDIA := P_IDGUARDIA;
    M_APUNTE_CG(IND_CG).IDPERSONA := P_IDPERSONA;
    M_APUNTE_CG(IND_CG).FECHAINICIO := P_FECHAINICIO;
    M_APUNTE_CG(IND_CG).FECHAFIN := P_FECHAFIN;
    M_APUNTE_CG(IND_CG).FACTURADO := P_FACTURADO;
    M_APUNTE_CG(IND_CG).CONTADOR := 1;
    M_APUNTE_CG(IND_CG).COSTEFIJO := P_COSTEFIJO;
    M_APUNTE_CG(IND_CG).MOTIVO := P_MOTIVO;
    M_APUNTE_CG(IND_CG).IMPORTE := P_IMPORTE;
    M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := P_IDTIPOAPUNTE;
  END PROC_GENERA_APUNTE_CABECERA;

  PROCEDURE PROC_APLICA_MAXIMOS(p_impNuevas             NUMBER,
                                p_impFacturadas         NUMBER,
                                p_impMaximo             NUMBER,
                                p_motivoNormal          NUMBER,
                                p_motivoMaximo          NUMBER,
                                p_motivoNormalDevengado NUMBER,
                                p_motivoMaximoDevengado NUMBER,
                                motivoFinal             OUT NUMBER,
                                impFinal                OUT NUMBER) IS
  BEGIN
    if p_impFacturadas = 0 then
      if p_impNuevas > p_impMaximo then
        impFinal    := p_impMaximo;
        motivoFinal := p_motivoMaximo;
      else
        impFinal    := p_impNuevas;
        motivoFinal := p_motivoNormal;
      end if;
    else
      if p_impFacturadas >= p_impMaximo then
        impFinal    := 0;
        motivoFinal := p_motivoMaximoDevengado;
      elsif p_impNuevas + p_impFacturadas > p_impMaximo then
        impFinal    := p_impMaximo - p_impFacturadas;
        -- RGG control de negativos
        if (impFinal<0) then
           impFinal:=0;
        end if;
        motivoFinal := p_motivoMaximoDevengado;
      else
        impFinal    := p_impNuevas;
        motivoFinal := p_motivoNormalDevengado;
      end if;
    end if;
  END PROC_APLICA_MAXIMOS;

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ACTFG_NOAPLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Actuaciones
  /*                fuera de guardia y no aplica tipos de actuacion.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
    PROCEDURE PROC_FACT_ACTFG_NOAPLICATIPO(
        indiceMatrizFacturable IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
    CURSOR C_ACTUACIONES_ACFG (P_IDINSTITUCION SCS_ASISTENCIA.IDINSTITUCION%TYPE, P_ANIO SCS_ASISTENCIA.ANIO%TYPE, P_NUMERO SCS_ASISTENCIA.NUMERO%TYPE, V_IDFACTURACION NUMBER) IS (
        SELECT ACT.IDINSTITUCION,
            ACT.ANIO,
            ACT.NUMERO,
            ACT.IDACTUACION,
            ASI.FECHAHORA,
            ACT.FECHA,
            ACT.FECHAJUSTIFICACION,
            '0' AS FACTURADA,
            0 AS IMPFACTURADA,
            NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA,  ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION), 0) AS IMPCOSTESFIJOS -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
        FROM SCS_ASISTENCIA ASI, 
            SCS_ACTUACIONASISTENCIA ACT
        WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
            AND ASI.ANIO = P_ANIO
            AND ASI.NUMERO = P_NUMERO
            AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
            AND ASI.ANIO = ACT.ANIO
            AND ASI.NUMERO = ACT.NUMERO
            AND ACT.FECHAJUSTIFICACION BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA)
            AND NVL(ACT.ANULACION, '0') = '0'
            AND ACT.DIADESPUES = 'S' -- solo las del día después
            AND NOT EXISTS ( -- solo las NO FACTURADAS
                SELECT 1
                FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                WHERE ACT.IDINSTITUCION = FAC.IDINSTITUCION
                    AND ACT.ANIO = FAC.ANIO
                    AND ACT.NUMERO = FAC.NUMERO
                    AND ACT.IDACTUACION = FAC.IDACTUACION)
        UNION
        SELECT ACT.IDINSTITUCION,
           ACT.ANIO,
           ACT.NUMERO,
           ACT.IDACTUACION,
           ASI.FECHAHORA,
           ACT.FECHA,
           ACT.FECHAJUSTIFICACION,
           '1' AS FACTURADA,
           SUM(FAC.PRECIOAPLICADO) AS IMPFACTURADA,
           SUM(FAC.PRECIOCOSTESFIJOS) AS IMPCOSTESFIJOS
        FROM SCS_ASISTENCIA ASI,
           SCS_ACTUACIONASISTENCIA ACT,
           FCS_FACT_ACTUACIONASISTENCIA FAC -- solo las facturadas
        WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
            AND ASI.ANIO = P_ANIO
            AND ASI.NUMERO = P_NUMERO
            AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
            AND ASI.ANIO = ACT.ANIO
            AND ASI.NUMERO = ACT.NUMERO
            AND ACT.IDINSTITUCION = FAC.IDINSTITUCION
            AND ACT.ANIO = FAC.ANIO
            AND ACT.NUMERO = FAC.NUMERO
            AND ACT.IDACTUACION = FAC.IDACTUACION
            AND NVL(ACT.ANULACION, '0') = '0'
            AND ACT.DIADESPUES = 'S' -- solo las del día después
        GROUP BY ACT.IDINSTITUCION,
              ACT.ANIO,
              ACT.NUMERO,
              ACT.IDACTUACION,
              ASI.FECHAHORA,
              ACT.FECHA,
              ACT.FECHAJUSTIFICACION);        
        
    --Acumuladores
    impActFacturadas_CABEC number;
    impActFacturadas_DIA   number;
    impActFacturadas_ASIST number;
    impActFacturadas_ACT   number;

    impActNuevas_CABEC number;
    impActNuevas_DIA   number;
    impActNuevas_ASIST number;
    impActNuevas_ACT   number;

    impCostesFijos_CABEC number;
    impCostesFijos_DIA   number;
    impCostesFijos_ASIST number;
    impCostesFijos_ACT   number;

    impActFacturadasPorDia_DIA     number;
    impActFacturadasPorCabec_DIA   number;
    impActNuevasPorDia_DIA         number;
    impActNuevasPorCabec_DIA       number;
    impActFacturadasPorDia_CABEC   number;
    impActFacturadasPorCabec_CABEC number;
    impActNuevasPorDia_CABEC       number;
    impActNuevasPorCabec_CABEC     number;

    --Indicador del dia actual en el bucle
    contadorUG number;

    --Motivos
    motivoNormal          number := 9; /*AcFG*/
    motivoMaximo          number := 6; /*AcFGMax*/
    motivoDevengado       number := 31; /*AcFG+*/
    motivoMaximoDevengado number := 26; /*AcFGMax+*/

    --Apuntes de dia y cabecera
    motivo  number;
    importe number;

  BEGIN

    --LOG de inicio
    P_DATOSERROR := 'PROCEDURE PROC_FACT_ACTFG_NOAPLICATIPO: antes que nada.' ||
                    M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION ||
                    M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO ||
                    M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA ||
                    M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO ||
                    M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
    P_CODRETORNO := TO_CHAR(0);

    --inicializando acumuladores de cabecera
    impActFacturadas_CABEC         := 0;
    impActNuevas_CABEC             := 0;
    impCostesFijos_CABEC           := 0;
    impActFacturadasPorDia_CABEC   := 0;
    impActFacturadasPorCabec_CABEC := 0;
    impActNuevasPorDia_CABEC       := 0;
    impActNuevasPorCabec_CABEC     := 0;

    contadorUG := 0;
    --bucle de dias para acumular en cabecera
    for R_GUARDIA in C_GUARDIAS_ACFG(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                     M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA) loop
      contadorUG := contadorUG + 1;

      --inicializando acumuladores de dia
      impActFacturadas_DIA := 0;
      impActNuevas_DIA     := 0;
      impCostesFijos_DIA   := 0;

      --bucle de asistencias para acumular en cada dia
      for R_ASISTENCIA in C_ASISTENCIAS_ACFG(R_GUARDIA.IDINSTITUCION,
                                             R_GUARDIA.IDTURNO,
                                             R_GUARDIA.IDGUARDIA,
                                             R_GUARDIA.FECHAFIN,
                                             R_GUARDIA.IDPERSONA) loop

        --inicializando acumuladores de asistencia
        impActFacturadas_ASIST := 0;
        impActNuevas_ASIST     := 0;
        impCostesFijos_ASIST   := 0;

        --bucle de actuaciones para acumular en cada asistencia
        for R_ACTUACION in C_ACTUACIONES_ACFG(R_ASISTENCIA.IDINSTITUCION, R_ASISTENCIA.ANIO, R_ASISTENCIA.NUMERO, M_CG_FACTURABLE(indiceMatrizFacturable).IDFACTURACION) loop

          --obteniendo importes para la actuacion
          if R_ACTUACION.FACTURADA = 1 then
            impActFacturadas_ACT := R_ACTUACION.IMPFACTURADA;
            impActNuevas_ACT     := 0;
            impCostesFijos_ACT   := 0;
          else
            impActFacturadas_ACT := 0;
            impActNuevas_ACT     := V_CONFIG_GUARDIA.IMPORTEACTUACIONFG;
            impCostesFijos_ACT   := R_ACTUACION.IMPCOSTESFIJOS;

            --generando registro apunte actuacion (SOLO PARA LAS NO FACTURADAS)
            proc_genera_apunte_act(R_ACTUACION.IDINSTITUCION,
                                   R_ACTUACION.ANIO,
                                   R_ACTUACION.NUMERO,
                                   R_ACTUACION.IDACTUACION,
                                   '0',
                                   contadorUG,
                                   impCostesFijos_ACT,
                                   motivoNormal,
                                   impActNuevas_ACT);
          end if;

          --acumulando importes de actuacion en asistencia
          impActFacturadas_ASIST := impActFacturadas_ASIST +
                                    impActFacturadas_ACT;
          impActNuevas_ASIST     := impActNuevas_ASIST + impActNuevas_ACT;
          impCostesFijos_ASIST   := impCostesFijos_ASIST +
                                    impCostesFijos_ACT;

        end loop;

        --generando registro apunte asistencia
        proc_genera_apunte_asist(R_ASISTENCIA.IDINSTITUCION,
                                 R_ASISTENCIA.ANIO,
                                 R_ASISTENCIA.NUMERO,
                                 '1',
                                 contadorUG,
                                 motivoNormal,
                                 impActNuevas_ASIST);

        --acumulando importes de asistencia en dia
        impActFacturadas_DIA := impActFacturadas_DIA +
                                impActFacturadas_ASIST;
        impActNuevas_DIA     := impActNuevas_DIA + impActNuevas_ASIST;
        impCostesFijos_DIA   := impCostesFijos_DIA + impCostesFijos_ASIST;

      end loop;

      --aplicando maximos si es necesario
      if V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG > 0 and
         (FUN_ESDIAAPLICABLE(R_GUARDIA.FECHAFIN,
                             V_CONFIG_GUARDIA.DIASPAGAGUARDIA) = 1 or
         FUN_ESDIAAPLICABLE(R_GUARDIA.FECHAFIN,
                             V_CONFIG_GUARDIA.DIASNOPAGAGUARDIA) = 1) then
        proc_aplica_maximos(impActNuevas_DIA,
                            impActFacturadas_DIA,
                            V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG,
                            motivoNormal,
                            motivoMaximo,
                            motivoDevengado,
                            motivoMaximoDevengado,
                            motivo,
                            importe);
        impActNuevas_DIA := importe;

        impActFacturadasPorDia_DIA := impActFacturadas_DIA;
        impActFacturadasPorCabec_DIA := 0;
        impActNuevasPorDia_DIA := importe;
        impActNuevasPorCabec_DIA := 0;
      else
        motivo  := motivoNormal;
        importe := impActNuevas_DIA;

        impActFacturadasPorDia_DIA   := 0;
        impActFacturadasPorCabec_DIA := impActFacturadas_DIA;
        impActNuevasPorDia_DIA       := 0;
        impActNuevasPorCabec_DIA     := importe;
      end if;

      --generando registro apunte dia
      proc_genera_apunte_guardia(R_GUARDIA.IDINSTITUCION,
                                 R_GUARDIA.IDTURNO,
                                 R_GUARDIA.IDGUARDIA,
                                 R_GUARDIA.IDPERSONA,
                                 R_GUARDIA.FECHAINICIO,
                                 R_GUARDIA.FECHAFIN,
                                 '1',
                                 contadorUG,
                                 impCostesFijos_DIA,
                                 motivo,
                                 importe);

      --acumulando importes de dia en cabecera
      impActFacturadas_CABEC         := impActFacturadas_CABEC +
                                        impActFacturadas_DIA;
      impActNuevas_CABEC             := impActNuevas_CABEC +
                                        impActNuevas_DIA;
      impCostesFijos_CABEC           := impCostesFijos_CABEC +
                                        impCostesFijos_DIA;
      impActFacturadasPorDia_CABEC   := impActFacturadasPorDia_CABEC +
                                        impActFacturadasPorDia_DIA;
      impActFacturadasPorCabec_CABEC := impActFacturadasPorCabec_CABEC +
                                        impActFacturadasPorCabec_DIA;
      impActNuevasPorDia_CABEC       := impActNuevasPorDia_CABEC +
                                        impActNuevasPorDia_DIA;
      impActNuevasPorCabec_CABEC     := impActNuevasPorCabec_CABEC +
                                        impActNuevasPorCabec_DIA;

    end loop;

    --aplicando maximos si es necesario
    if V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG > 0 then
      proc_aplica_maximos(impActNuevasPorCabec_CABEC,
                          impActFacturadasPorCabec_CABEC,
                          V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG,
                          motivoNormal,
                          motivoMaximo,
                          motivoDevengado,
                          motivoMaximoDevengado,
                          motivo,
                          importe);
      importe            := importe + impActNuevasPorDia_CABEC;
    else
      motivo  := motivoNormal;
      importe := impActNuevas_CABEC;
    end if;

    -- RGG pasa que no pinte la cabecera si no hay actuaciones FG
    if (contadorUG>0) then
      --generando registro apunte cabecera
      proc_genera_apunte_cabecera(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                  M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                  M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                  M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                                  M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                  M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
                                  '1',
                                  impCostesFijos_CABEC,
                                  motivo,
                                  importe,
                                  'FG');
    end if;

    --LOG de fin
    P_DATOSERROR := 'PROCEDURE PROC_FACT_ACTFG_NOAPLICATIPO: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);

  EXCEPTION
    when others then
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
  END PROC_FACT_ACTFG_NOAPLICATIPO;

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_ACTFG_APLICATIPO                                                                           */
  /* Descripcion:   Este procedimiento se encarga de cargar las tablas de memoria cuando facturamos por Actuaciones
  /*                fuera de guardia y aplica tipos de actuaciones.
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 25/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
    PROCEDURE PROC_FACT_ACTFG_APLICATIPO(
        indiceMatrizFacturable IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
                                       
        -- Obtenemos las actuaciones realizadas y justificadas anteriormente a la fecha fin del ciclo de facturacion fuera de la guardia y agrupadas por Tipo Actuacion y realizadas en Dia
        CURSOR C_ACTUACIONESTIPO_FG(V_IDINSTITUCION NUMBER, V_IDTURNO NUMBER, V_IDGUARDIA NUMBER, V_IDPERSONA NUMBER, V_FECHAINICIO_CG DATE, V_FECHAFIN_CG DATE, V_FECHA DATE, V_IDTIPOACT NUMBER, V_IDFACTURACION NUMBER) IS(
            SELECT ACT.IDTIPOACTUACION,
                ACT.FECHA,
                ACT.IDINSTITUCION,
                ACT.ANIO,
                ACT.NUMERO,
                ACT.IDACTUACION,
                1 AS FACTURADO,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION), 0) AS COSTEFIJO, -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION, 0) AS IMPORTE, -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION, 1) AS IMPORTEMAX -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
            FROM SCS_ASISTENCIA ASI, 
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
                AND TRUNC(ASI.FECHAHORA) BETWEEN TRUNC(V_FECHAINICIO_CG) AND TRUNC(V_FECHAFIN_CG)
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO                
                AND EXISTS ( --and ACT.Facturado = '1'
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                    WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION)
                AND ACT.DIADESPUES = 'S'
                AND ACT.FECHA = V_FECHA
                AND ACT.IDTIPOACTUACION = V_IDTIPOACT
            UNION
            SELECT ACT.IDTIPOACTUACION,
                ACT.FECHA,
                ACT.IDINSTITUCION,
                ACT.ANIO,
                ACT.NUMERO,
                ACT.IDACTUACION,
                0 AS FACTURADO,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION), 0) AS COSTEFIJO, -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION, 0) AS IMPORTE, -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
                FUNC_CALCULAR_IMPORTEACT(ACT.IDINSTITUCION, ASI.IDTURNO, ASI.IDGUARDIA, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, TRUNC(ACT.FECHA), V_IDFACTURACION, 1) AS IMPORTEMAX -- Los datos de Fuera de Guardia se calculan con la fecha de la actuacion
            FROM SCS_ASISTENCIA ASI, 
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
                AND TRUNC(ASI.FECHAHORA) BETWEEN TRUNC(V_FECHAINICIO_CG) AND TRUNC(V_FECHAFIN_CG)
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO                
                AND NOT EXISTS ( --and (ACT.Facturado is null or ACT.Facturado = '0')
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                    WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION)
                AND (ACT.ANULACION IS NULL OR ACT.ANULACION = '0')
                AND ACT.FECHAJUSTIFICACION IS NOT NULL 
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_DATOS_FACTURACION.FECHADESDE) AND TRUNC(V_DATOS_FACTURACION.FECHAHASTA) 
                AND ACT.VALIDADA = 1
                AND ACT.DIADESPUES = 'S'
                AND ACT.FECHA = V_FECHA
                AND ACT.IDTIPOACTUACION = V_IDTIPOACT)
            ORDER BY IDTIPOACTUACION, FECHA;

    importeActualDia          number;
    importeTotalActuacion     number;
    importeTotalActuacionCG   number;
    importeTotalTipoActuacion number;
    importeFacturadoActuacion number;
    importeFacturadoCG        number;
    importeFacturadoUG        number;
    importeTipoActuacion      number;
    importeTipoActMax         number;
    contador                  number;
    contadorUG                number;
    existe                    number;
    costeFijoCG               number := 0;
    costeFijoUG               number;
    costeFijoAc               number;
    contarActuaciones         number := 0;
    contarActuacionesCG       number := 0;

  BEGIN

    contador                := 0;
    contadorUG              := 0;
    importeTotalActuacionCG := 0;
    importeFacturadoCG      := 0;
    -- comienza en 1 porque como minimo ya existe un apunte en la matriz M_APUNTE_CG por haber hecho
    -- una facturacion por guardia dobla asist,... y ahora apuntariamos otra.
    -- Para cada Dia
    FOR V_DIAS IN C_DIAS_FG(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                            M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                            M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                            M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                            M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN) LOOP

      -- Para cada UG
      contador := contador + 1;

      -- Para cada Actuacion
      importeTotalActuacion := 0;
      costeFijoCG           := 0;
      costeFijoAc           := 0;
      contarActuacionesCG   := 0;
      importeFacturadoUG    := 0;

      FOR V_TIPOACTUACIONES_FG IN C_TIPOACTUACIONES_FG(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                                       M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                                       M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                                       M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                                                       M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                                       M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
                                                       V_DIAS.FECHA) LOOP

        contadorUG                := contadorUG + 1;
        importeTotalTipoActuacion := 0;
        importeFacturadoActuacion := 0;
        contarActuaciones         := 0;

        FOR V_ACTUACIONES_FG IN C_ACTUACIONESTIPO_FG(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                                                     M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                                     M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
                                                     V_DIAS.FECHA,
                                                     V_TIPOACTUACIONES_FG.TIPOACTUACION,
                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDFACTURACION) LOOP

          contarActuaciones   := contarActuaciones + 1;
          contarActuacionesCG := contarActuacionesCG + 1;

          -- Obtenemos el importe de la asistencia, en el caso de que fuera nulo
          -- el importe de la asistencia seria el de la Cabecera de Guardia
          importeTipoActuacion := V_ACTUACIONES_FG.importe;
          importeTipoActMax    := V_ACTUACIONES_FG.importemax;

          if (importeTipoActuacion is null) then

            importeTipoActuacion := V_CONFIG_GUARDIA.IMPORTEACTUACIONFG;

          end if;

          if (importeTipoActMax is null) then

            importeTipoActMax := V_CONFIG_GUARDIA.IMPORTEMAXACTUACIONFG;

          end if;

          -- Comenzamos el apunte en la matriz de memoria: M_APUNTE_AS
          if (V_ACTUACIONES_FG.facturado <> pkg_siga_constantes.DB_TRUE_A or
             V_ACTUACIONES_FG.facturado is null) then
             -- solo cuando NO ESTA FACTURADA
            costeFijoAc := costeFijoAc + V_ACTUACIONES_fG.costefijo;

              -- Comenzamos el apunte en la matriz de memoria: M_APUNTE_AC
              IND_AC := IND_AC + 1;
              M_APUNTE_AC(IND_AC).IDINSTITUCION := V_ACTUACIONES_FG.IDINSTITUCION;
              M_APUNTE_AC(IND_AC).ANIO := V_ACTUACIONES_FG.ANIO;
              M_APUNTE_AC(IND_AC).NUMERO := V_ACTUACIONES_FG.NUMERO;
              M_APUNTE_AC(IND_AC).IDACTUACION := V_ACTUACIONES_FG.IDACTUACION;
              M_APUNTE_AC(IND_AC).FACTURADO := V_ACTUACIONES_FG.FACTURADO;
              M_APUNTE_AC(IND_AC).CONTADOR := contadorUG;
              M_APUNTE_AC(IND_AC).costefijo := costeFijoAc;

          end if;


          if (importeTipoActMax is not null and importeTipoActMax <> 0.0 and
             importeTotalTipoActuacion >= importeTipoActMax) then

            -- RGG 19/06/2008 SUPERA EL MÁXIMO POR TIPO

            if (V_ACTUACIONES_FG.FACTURADO is not null and
               V_ACTUACIONES_FG.FACTURADO = 1) THEN

              -- RGG 19/06/2008 ESTA FACTURADO

              importeFacturadoActuacion := importeTipoActMax;

              -- RGG M_APUNTE_AC(IND_AC).MOTIVO := 37; --AcNoDev+
              -- RGG M_APUNTE_AC(IND_AC).IMPORTE := 0;

            else

              -- RGG 19/06/2008 NO ESTA FACTURADO

              M_APUNTE_AC(IND_AC).MOTIVO := 35; --AcNoDev
              M_APUNTE_AC(IND_AC).IMPORTE := 0;

            end if;

          ELSE
            -- RGG 19/06/2008 NO SUPERA EL MÁXIMO POR TIPO

            if (V_ACTUACIONES_FG.FACTURADO is not null and
               V_ACTUACIONES_FG.FACTURADO = 1) THEN

              -- RGG 19/06/2008 ESTA FACTURADO
              importeFacturadoActuacion := importeFacturadoActuacion +
                                           importeTipoActuacion;

              -- RGG M_APUNTE_AC(IND_AC).MOTIVO := 37; --AcNoDev+
              -- RGG M_APUNTE_AC(IND_AC).IMPORTE := importeTipoActuacion;

            else

              -- RGG 19/06/2008 NO ESTÁ FACTURADO
              M_APUNTE_AC(IND_AC).MOTIVO := 25; --AcFGTp
              M_APUNTE_AC(IND_AC).IMPORTE := importeTipoActuacion;

            end if;

          end if;

          importeTotalActuacion := importeTotalActuacion +
                                   importeTipoActuacion;

          importeTotalTipoActuacion := importeTotalTipoActuacion +
                                       importeTipoActuacion;

          importeFacturadoUG := importeFacturadoUG +
                                importeFacturadoActuacion;
          importeFacturadoCG := importeFacturadoCG +
                                importeFacturadoActuacion;

        END LOOP; -- Fin para actuaciones

        importeActualDia := 0;
        costeFijoUG      := costeFijoAc;
        costeFijoCG      := costeFijoCG + costeFijoAc;

        if (contarActuaciones > 0) then
          -- solo se hace apunte en la UG cuando existen actuaciones

          IND_UG := IND_UG + 1;
          M_APUNTE_UG(IND_UG).idinstitucion := M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION;
          M_APUNTE_UG(IND_UG).idturno := M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO;
          M_APUNTE_UG(IND_UG).idguardia := M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA;
          M_APUNTE_UG(IND_UG).idpersona := M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
          M_APUNTE_UG(IND_UG).fechainicio := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO;
          M_APUNTE_UG(IND_UG).fechafin := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN;
          M_APUNTE_UG(IND_UG).facturado := M_CG_FACTURABLE(indiceMatrizFacturable).FACTURADO;
          M_APUNTE_UG(IND_UG).contador := contadorUG;
          M_APUNTE_UG(IND_UG).costefijo := costeFijoUG;

          if (importeTipoActMax is not null and importeTipoActMax <> 0.0 and
             importeTotalActuacion >= importeTipoActMax) then

            existe := FUNC_EXISTE_ACTFACTURADA(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
                                                                         V_DIAS.FECHA); -- Exixten actuacions facturadas para el Dia

            if (existe = 1) then

              importeActualDia := importeTipoActMax;
              M_APUNTE_UG(IND_UG).MOTIVO := 43; --AcFGTpMax+;
              M_APUNTE_UG(IND_UG).IMPORTE := importeTipoActMax -
                                                                                                 importeFacturadoUG;

              if (M_APUNTE_UG(IND_UG).IMPORTE<0) then
                 M_APUNTE_UG(IND_UG).IMPORTE:=0;
              end if;


            else

              importeActualDia := importeTipoActMax;
              M_APUNTE_UG(IND_UG).MOTIVO := 24; --AcFGTpMax;
              M_APUNTE_UG(IND_UG).IMPORTE := importeTipoActMax;

            end if;

          else

            existe := FUNC_EXISTE_ACTFACTURADA(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                                                         M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
                                                                         V_DIAS.FECHA); -- Exixten actuacions facturadas para el Dia

            if (existe = 1) then

              importeActualDia := importeTotalActuacion;
              M_APUNTE_UG(IND_UG).MOTIVO := 32; --AcFGTp+;
              M_APUNTE_UG(IND_UG).IMPORTE := importeTotalActuacion -
                                                                                                 importeFacturadoUG;

              if (M_APUNTE_UG(IND_UG).IMPORTE<0) then
                 M_APUNTE_UG(IND_UG).IMPORTE:=0;
              end if;

            else

              importeActualDia := importeTotalActuacion;
              M_APUNTE_UG(IND_UG).MOTIVO := 25; --AcFGTp;
              M_APUNTE_UG(IND_UG).IMPORTE := importeTotalActuacion;

            end if;

          end if;

          importeTotalActuacionCG := importeTotalActuacionCG +
                                     importeActualDia;

        end if;

      END LOOP; -- Fin para cada Tipo

      if (contarActuacionesCG > 0) then
        -- solo se hace apunte en la CG cuando existen actuaciones

        IND_CG := IND_CG + 1;
        M_APUNTE_CG(IND_CG).idinstitucion := M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION;
        M_APUNTE_CG(IND_CG).idturno := M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO;
        M_APUNTE_CG(IND_CG).idguardia := M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA;
        M_APUNTE_CG(IND_CG).idpersona := M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA;
        M_APUNTE_CG(IND_CG).fechainicio := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO;
        M_APUNTE_CG(IND_CG).fechafin := M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN;
        M_APUNTE_CG(IND_CG).facturado := M_CG_FACTURABLE(indiceMatrizFacturable).FACTURADO;
        M_APUNTE_CG(IND_CG).contador := 2;
        M_APUNTE_CG(IND_CG).costefijo := costeFijoCG;
        M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'FGTp';

        existe := FUNC_EXISTE_ACTFACTURADA(M_CG_FACTURABLE(indiceMatrizFacturable).IDINSTITUCION,
                                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDTURNO,
                                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDGUARDIA,
                                                                     M_CG_FACTURABLE(indiceMatrizFacturable).IDPERSONA,
                                                                     M_CG_FACTURABLE(indiceMatrizFacturable).FECHAINICIO,
                                                                     M_CG_FACTURABLE(indiceMatrizFacturable).FECHAFIN,
                                                                     V_DIAS.FECHA); -- Exixten actuacions facturadas para el Dia

        if (existe = 1) then

          importeActualDia := importeTotalActuacionCG;
          M_APUNTE_CG(IND_CG).MOTIVO := 32; --AcFGTp+;
          M_APUNTE_CG(IND_CG).IMPORTE := importeTotalActuacionCG -
                                                                                             importeFacturadoCG;

          if (M_APUNTE_CG(IND_CG).IMPORTE<0) then
             M_APUNTE_CG(IND_CG).IMPORTE:=0;
          end if;

        else

          importeActualDia := importeTotalActuacionCG;
          M_APUNTE_CG(IND_CG).MOTIVO := 25; --AcFGTp;
          M_APUNTE_CG(IND_CG).IMPORTE := importeTotalActuacionCG;

        end if;

      end if;

    END LOOP; -- Fin para cada Dia

    P_DATOSERROR := 'PROCEDURE PROC_FACT_ACTFG_APLICATIPO: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);

  EXCEPTION
    when others then
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;

  END PROC_FACT_ACTFG_APLICATIPO;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_EXISTE_ACTFACTURADA                                                                             */
  /* Descripcion:   Funcion que comprueba si existen actuaciones facturadas para un dia concreto           */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN     identificador de la institucion                                  NUMBER,
  /* P_IDTURNO             IN     identificador del turno                                          NUMBER,
  /* P_IDGUARDIA           IN     identificador de la guardia                                      NUMBER,
  /* P_IDPERSONA           IN     identificador de la persona                                      NUMBER,
  /* P_FECHAINICIO         IN     fecha inicio de CG                                               NUMBER,
  /* P_FECHAFIN            IN     fecha fin de CG                                                  NUMBER,
  /* P_FECHA               IN     dia                                                              NUMBER,
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Pilar Duran Mu?oz                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  FUNCTION FUNC_EXISTE_ACTFACTURADA(P_IDINSTITUCION NUMBER,
                                    P_IDTURNO       NUMBER,
                                    P_IDGUARDIA     NUMBER,
                                    P_IDPERSONA     NUMBER,
                                    P_FECHAINICIO   DATE,
                                    P_FECHAFIN      DATE,
                                    P_FECHA         DATE) return number is
    CURSOR C_ACTFACT_FG IS
      select Ac.fecha FECHA, Ac.IDTIPOACTUACION TIPOACTUACION, Ac.facturado

        from Scs_Asistencia A, scs_actuacionasistencia Ac
       where A.Idinstitucion = P_IDINSTITUCION
         and A.Idturno = P_IDTURNO
         and A.Idguardia = P_IDGUARDIA
         and A.Idpersonacolegiado = P_IDPERSONA
         and A.Fechahora BETWEEN P_FECHAINICIO AND P_FECHAFIN
         and A.IDINSTITUCION = Ac.Idinstitucion
         and A.Anio = Ac.Anio
         and A.Numero = Ac.Numero
         --and Ac.Facturado = 1
         and exists
           (select *
              from FCS_FACT_ACTUACIONASISTENCIA FAC
             where fac.idinstitucion = Ac.idinstitucion
               and fac.anio = Ac.anio
               and fac.numero = Ac.numero
               and fac.idactuacion = Ac.idactuacion)
         and Ac.diadespues = 'S'
         AND AC.FECHA = P_FECHA;

    existe number := 0;

  begin

    FOR reg_actFact in C_ACTFACT_FG loop
      existe := 1;
    end loop;

    return existe;

  end FUNC_EXISTE_ACTFACTURADA;

  -- RGG

  /****************************************************************************************************************/
  /* Nombre:        PROC_FACT_DESCARGA_MATR_GUARDIA                                                                           */
  /* Descripcion:   Este procedimiento se encarga de descargar en las tablas de apuntes las matrices de guardias
  /*?               y vaciar las matrices
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 27/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                      */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */

  /****************************************************************************************************************/

    Procedure PROC_FACT_DESC_MATR_GUARDIA(
        p_Idinstitucion   In Fcs_Fact_Apunte.Idinstitucion%Type,
        p_Idfacturacion   In Fcs_Fact_Apunte.Idfacturacion%Type,
        p_Usumodificacion In Fcs_Fact_Apunte.Usumodificacion%Type,
        p_Codretorno      Out Varchar2,
        p_Datoserror      Out Varchar2) Is

        v_Idapunte Fcs_Fact_Apunte.Idapunte%Type;
        v_Idpersona_As Fcs_Fact_Asistencia.Idpersona%Type;
        v_Fechahora_As Fcs_Fact_Asistencia.Fechahora%Type;
        v_Idpersona_Ac Fcs_Fact_Actuacionasistencia.Idpersona%Type;
        v_Fechaactuacion_Ac Fcs_Fact_Actuacionasistencia.Fechaactuacion%Type;
        v_Fechajustificacion_Ac Fcs_Fact_Actuacionasistencia.Fechajustificacion%Type;
        v_Codretorno Varchar2(10);
        v_Datoserror Varchar2(200);

    Begin

        v_Datoserror := 'PROC_FACT_DESCARGA_MATRICES_GUARDIA: Tratando cabeceras de guardia';
        FOR I IN 1 .. IND_CG LOOP

            v_Idapunte := Fun_Obtener_Idapunte_Cg(p_Idinstitucion, p_Idfacturacion);

            INSERT INTO FCS_FACT_APUNTE (
                IDINSTITUCION,
                IDFACTURACION,
                IDAPUNTE,
                IDPERSONA,
                IDTURNO,
                IDGUARDIA,
                FECHAINICIO,
                IDHITO,
                MOTIVO,
                PRECIOAPLICADO,
                FECHAMODIFICACION,
                USUMODIFICACION,
                PRECIOCOSTESFIJOS,
                IDTIPOAPUNTE
            ) VALUES (
                p_Idinstitucion,
                p_Idfacturacion,
                v_Idapunte,
                m_Apunte_Cg(i).Idpersona,
                m_Apunte_Cg(i).Idturno,
                m_Apunte_Cg(i).Idguardia,
                m_Apunte_Cg(i).Fechainicio,
                m_Apunte_Cg(i).Motivo,
                Fun_Descripcion_Idhito(m_Apunte_Cg(i).Motivo),
                NVL(m_Apunte_Cg(i).Importe, 0.0),
                SYSDATE,
                p_Usumodificacion,
                NVL(m_Apunte_Cg(i).Costefijo, 0.0),
                m_Apunte_Cg(i).Idtipoapunte);

            Total_Facturacion := Total_Facturacion + NVL(m_Apunte_Cg(i).Importe,0) + NVL(m_Apunte_Cg(i).Costefijo,0);
        END LOOP;

        v_Datoserror := 'PROC_FACT_DESCARGA_MATRICES_GUARDIA: Tratando dias de guardia';
        FOR I IN 1 .. IND_UG LOOP

        INSERT INTO FCS_FACT_GUARDIASCOLEGIADO(
            IDINSTITUCION,
            IDFACTURACION,
            IDAPUNTE,
            IDTURNO,
            IDGUARDIA,
            FECHAINICIO,
            IDPERSONA,
            PRECIOAPLICADO,
            FECHAMODIFICACION,
            USUMODIFICACION,
            IDTIPO,
            PRECIOCOSTESFIJOS,
            IDHITO,
            MOTIVO,
            FECHAFIN
        ) VALUES (
            p_Idinstitucion,
            p_Idfacturacion,
            v_Idapunte,
            m_Apunte_Ug(i).Idturno,
            m_Apunte_Ug(i).Idguardia,
            m_Apunte_Ug(i).Fechainicio,
            m_Apunte_Ug(i).Idpersona,
            NVL(m_Apunte_Ug(i).Importe, 0.0),
            SYSDATE,
            p_Usumodificacion,
            m_Apunte_Ug(i).Contador,
            NVL(m_Apunte_Ug(i).Costefijo, 0.0),
            m_Apunte_Ug(i).Motivo,
            Fun_Descripcion_Idhito(m_Apunte_Ug(i).Motivo),
            m_Apunte_Ug(i).Fechafin);
        END LOOP;

        v_Datoserror := 'PROC_FACT_DESCARGA_MATRICES_GUARDIA: Tratando asistencias';
        FOR I IN 1 .. IND_AS LOOP

            SELECT IDPERSONACOLEGIADO, FECHAHORA
                INTO v_Idpersona_As, v_Fechahora_As
            FROM SCS_ASISTENCIA
            WHERE IDINSTITUCION = p_Idinstitucion
                AND ANIO = m_Apunte_As(i).Anio
                AND NUMERO = m_Apunte_As(i).Numero;

            INSERT INTO FCS_FACT_ASISTENCIA (
                IDINSTITUCION,
                IDFACTURACION,
                IDAPUNTE,
                ANIO,
                NUMERO,
                IDPERSONA,
                FECHAHORA,
                FECHAJUSTIFICACION,
                PRECIOAPLICADO,
                FECHAMODIFICACION,
                USUMODIFICACION,
                IDHITO,
                MOTIVO,
                IDTIPO
            ) VALUES (
                p_Idinstitucion,
                p_Idfacturacion,
                v_Idapunte,
                m_Apunte_As(i).Anio,
                m_Apunte_As(i).Numero,
                v_Idpersona_As,
                v_Fechahora_As,
                NULL,
                NVL(m_Apunte_As(i).Importe, 0.0),
                SYSDATE,
                p_Usumodificacion,
                m_Apunte_As(i).Motivo,
                Fun_Descripcion_Idhito(m_Apunte_As(i).Motivo),
                m_Apunte_As(i).Contador);
        END LOOP;

        v_Datoserror := 'PROC_FACT_DESCARGA_MATRICES_GUARDIA: Tratando actuaciones';
        FOR I IN 1 .. IND_AC LOOP

            v_Datoserror := 'PROC_FACT_DESCARGA_MATRICES_GUARDIA, ' || v_Idapunte || ', ' || m_Apunte_Ac(i).Idactuacion || ', ' || m_Apunte_Ac(i).Anio || ', ' || m_Apunte_Ac(i).Numero;

            SELECT ASI.IDPERSONACOLEGIADO, ACT.FECHA, ACT.FECHAJUSTIFICACION
                INTO v_Idpersona_Ac, v_Fechaactuacion_Ac, v_Fechajustificacion_Ac
            FROM SCS_ASISTENCIA ASI,
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO
                AND ACT.IDINSTITUCION = p_Idinstitucion
                AND ACT.ANIO = m_Apunte_Ac(i).Anio
                AND ACT.NUMERO = m_Apunte_Ac(i).Numero
                AND ACT.IDACTUACION = m_Apunte_Ac(i).Idactuacion;

            INSERT INTO FCS_FACT_ACTUACIONASISTENCIA(
                IDINSTITUCION,
                IDFACTURACION,
                FECHAMODIFICACION,
                IDAPUNTE,
                IDACTUACION,
                ANIO,
                NUMERO,
                IDPERSONA,
                PRECIOAPLICADO,
                FECHAACTUACION,
                FECHAJUSTIFICACION,
                USUMODIFICACION,
                PRECIOCOSTESFIJOS,
                IDHITO,
                MOTIVO,
                IDTIPO
            ) VALUES (
                p_Idinstitucion,
                p_Idfacturacion,
                SYSDATE,
                v_Idapunte,
                m_Apunte_Ac(i).Idactuacion,
                m_Apunte_Ac(i).Anio,
                m_Apunte_Ac(i).Numero,
                v_Idpersona_Ac,
                NVL(m_Apunte_Ac(i).Importe, 0.0),
                v_Fechaactuacion_Ac,
                v_Fechajustificacion_Ac,
                p_Usumodificacion,
                NVL(m_Apunte_Ac(i).Costefijo, 0.0),
                m_Apunte_Ac(i).Motivo,
                Fun_Descripcion_Idhito(m_Apunte_Ac(i).Motivo),
                m_Apunte_Ac(i).Contador);
        END LOOP;

        m_Apunte_Ac.Delete;
        m_Apunte_As.Delete;
        m_Apunte_Ug.Delete;
        m_Apunte_Cg.Delete;
        Ind_Ac := 0;
        Ind_As := 0;
        Ind_Ug := 0;
        Ind_Cg := 0;

        v_Datoserror := 'PROC_FACT_DESCARGA_MATRICES_GUARDIA: finalizado correctamente';
        v_Codretorno := To_Char(0);

        p_Codretorno := v_Codretorno;
        p_Datoserror := v_Datoserror;

        Exception
            When e_Error_Datos Then
                p_Codretorno := -1;
                p_Datoserror := v_Datoserror;

            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ' ' || Sqlerrm;
    End PROC_FACT_DESC_MATR_GUARDIA;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_CG                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_CG(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER)
    return FCS_FACT_APUNTE.IDAPUNTE%TYPE is

    V_SALIDA FCS_FACT_APUNTE.IDAPUNTE%TYPE;

  begin

    SELECT max(IDAPUNTE) + 1
      INTO V_SALIDA
      FROM FCS_FACT_APUNTE
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDFACTURACION = P_IDFACTURACION;

    IF (V_SALIDA IS NULL) THEN
      return(1);
    ELSE
      return(V_SALIDA);
    END IF;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      return(1);
    WHEN OTHERS THEN
      return(-1);

  end FUN_OBTENER_IDAPUNTE_CG;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_UG                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes UG                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_UG(P_IDINSTITUCION        NUMBER,
                                   P_IDFACTURACION        NUMBER,
                                   P_IDTURNO              NUMBER,
                                   P_IDGUARDIA            NUMBER,
                                   P_FECHAINICIO          FCS_FACT_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
                                   P_FECHAFIN             FCS_FACT_GUARDIASCOLEGIADO.FECHAFIN%TYPE,
                                   P_IDTIPO               NUMBER)
    return FCS_FACT_GUARDIASCOLEGIADO.IDAPUNTE%TYPE is

    V_SALIDA FCS_FACT_GUARDIASCOLEGIADO.IDAPUNTE%TYPE;

  begin

    SELECT max(IDAPUNTE) + 1
      INTO V_SALIDA
      FROM FCS_FACT_GUARDIASCOLEGIADO
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDFACTURACION = P_IDFACTURACION
       AND IDTURNO = P_IDTURNO
       AND IDGUARDIA = P_IDGUARDIA
       AND FECHAINICIO = P_FECHAINICIO
       AND FECHAFIN = P_FECHAFIN
       AND IDTIPO = P_IDTIPO;

    IF (V_SALIDA IS NULL) THEN
      return(1);
    ELSE
      return(V_SALIDA);
    END IF;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      return(1);
    WHEN OTHERS THEN
      return(-1);

  end FUN_OBTENER_IDAPUNTE_UG;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_AS                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes AS                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_AS(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_ANIO          NUMBER,
                                   P_NUMERO        NUMBER,
                                   P_IDTIPO        NUMBER)
    return FCS_FACT_ASISTENCIA.IDAPUNTE%TYPE is

    V_SALIDA FCS_FACT_ASISTENCIA.IDAPUNTE%TYPE;

  begin

    SELECT max(IDAPUNTE) + 1
      INTO V_SALIDA
      FROM FCS_FACT_ASISTENCIA
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDFACTURACION = P_IDFACTURACION
       AND ANIO = P_ANIO
       AND NUMERO = P_NUMERO
       AND IDTIPO = P_IDTIPO;

    IF (V_SALIDA IS NULL) THEN
      return(1);
    ELSE
      return(V_SALIDA);
    END IF;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      return(1);
    WHEN OTHERS THEN
      return(-1);

  end FUN_OBTENER_IDAPUNTE_AS;

  /****************************************************************************************************************/
  /* Nombre:        FUN_OBTENER_IDAPUNTE_AC                                                                           */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes AC                                */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_OBTENER_IDAPUNTE_AC(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_IDACTUACION   NUMBER,
                                   P_ANIO          NUMBER,
                                   P_NUMERO        NUMBER,
                                   P_IDTIPO        NUMBER)
    return FCS_FACT_ACTUACIONASISTENCIA.IDAPUNTE%TYPE is

    V_SALIDA FCS_FACT_ACTUACIONASISTENCIA.IDAPUNTE%TYPE;

  begin

    SELECT max(IDAPUNTE) + 1
      INTO V_SALIDA
      FROM FCS_FACT_ACTUACIONASISTENCIA
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDFACTURACION = P_IDFACTURACION
       AND IDACTUACION = P_IDACTUACION
       AND ANIO = P_ANIO
       AND NUMERO = P_NUMERO
       AND IDTIPO = P_IDTIPO;

    IF (V_SALIDA IS NULL) THEN
      return(1);
    ELSE
      return(V_SALIDA);
    END IF;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      return(1);
    WHEN OTHERS THEN
      return(-1);

  end FUN_OBTENER_IDAPUNTE_AC;

  /****************************************************************************************************************/
  /* Nombre:        FUN_DESCRIPCION_IDHITO                                                                           */
  /* Descripcion:   Funcion que obtiene la descripcion del hito                               */
  /*                                                                                                         */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                  */
  /* Version:        1.0                                                                               */
  /* Fecha Creacion: 28/04/2006                                                                                   */
  /* Autor:         Raul Gonzalez Gonzalez                                                                        */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION FUN_DESCRIPCION_IDHITO(P_IDHITO SCS_HITOFACTURABLE.IDHITO%TYPE)
    return SCS_HITOFACTURABLE.NOMBRE%TYPE is

    V_SALIDA SCS_HITOFACTURABLE.NOMBRE%TYPE;

  begin

    SELECT NOMBRE
      INTO V_SALIDA
      FROM SCS_HITOFACTURABLE
     WHERE IDHITO = P_IDHITO;

    IF (V_SALIDA IS NULL) THEN
      return('');
    ELSE
      return(V_SALIDA);
    END IF;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      return('');
    WHEN OTHERS THEN
      return('');

  end FUN_DESCRIPCION_IDHITO;

  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------
  -------------------------------------------------------------------------------------------------

  PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_PATHFICHERO   IN VARCHAR2,
                                       P_FICHERO       IN VARCHAR2,
                                       P_IDIOMA        IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2) IS
  BEGIN
    PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION,
                               P_IDFACTURACION,
                               null,
                               null,
                               P_PATHFICHERO,
                               P_FICHERO,
                               P_IDIOMA,
                               P_CODRETORNO,
                               P_DATOSERROR);
  END; --PROC_FCS_EXPORTAR_GUARDIAS


  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURACIONES_INTERVALO                                                                  */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 22/06/2009                                                                                   */
  /* Autor:         Jose Barrientos Diaz                                                                          */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Facturaciones_Intervalo(p_Idinstitucion  Number,
                                        p_Idfacturacion1 Number,
                                        p_Idfacturacion2 Number)
    Return Varchar2 Is

    Cursor c_Facturaciones(p_Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type, p_Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type) Is
      Select Fac.Idfacturacion
        From Fcs_Facturacionjg Fac
       Where Fac.Prevision = '0'
         And Fac.Regularizacion = '0'
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Fechadesde >= p_Fecha_Ini
         And Fac.Fechahasta <= p_Fecha_Fin
       Order By Fac.Fechadesde Asc;

    Fecha_1   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_2   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_3   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_4   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type;
    Rc        Varchar2(4000);

    v_Consejo cen_institucion.Cen_Inst_Idinstitucion%Type;
    v_Idgrupofacturacion Fcs_Fact_Grupofact_Hito.Idgrupofacturacion%Type;

    idfact_anterior Fcs_Facturacionjg.Idfacturacion%Type;

  Begin

    -- Obtenemos las fechas de la primera facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_1, Fecha_2
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion1;
    -- Obtenemos las fechas de la segunda facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_3, Fecha_4
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion2;

    -- La primera fechadesde marca el inicio del intervalo
    If (Fecha_1 < Fecha_3) Then
      Fecha_Ini := Fecha_1;
    Else
      Fecha_Ini := Fecha_3;
    End If;
    -- La ultima fechahasta marca el final del intervalo
    If (Fecha_2 > Fecha_4) Then
      Fecha_Fin := Fecha_2;
    Else
      Fecha_Fin := Fecha_4;
    End If;

    -- Sacando solo una facturacion si solo se selecciona una
    If (p_Idfacturacion2 Is Null) Then
      Return p_Idfacturacion1;
    Elsif (p_Idfacturacion1 = p_Idfacturacion2) Then
      Return p_Idfacturacion1;
    End If;

    Select Cen_Inst_Idinstitucion
      Into v_Consejo
      From Cen_Institucion
     Where Idinstitucion = p_Idinstitucion;

    idfact_anterior := -1;
    If (v_Consejo = 3001) Then
      For v_Fact In c_Facturaciones(Fecha_Ini, Fecha_Fin) Loop
        Select Idgrupofacturacion
          Into v_Idgrupofacturacion
          From Fcs_Fact_Grupofact_Hito
         Where Idinstitucion = p_Idinstitucion
           And Idfacturacion = v_Fact.Idfacturacion
         Group By Idgrupofacturacion;

        If (v_Idgrupofacturacion = 1) Then
          If (v_Fact.Idfacturacion <> idfact_anterior) Then
            -- Control para no duplicar el pago
            Rc := Rc || ', ' || v_Fact.Idfacturacion;
            idfact_anterior := v_Fact.Idfacturacion;
          END IF;
        End If;
      End Loop;
    Else
      For v_Fact In c_Facturaciones(Fecha_Ini, Fecha_Fin) Loop
        IF (True) THEN
          If (v_Fact.Idfacturacion <> idfact_anterior) Then
            -- Control para no duplicar el pago
            Rc := Rc || ', ' || v_Fact.Idfacturacion;
            idfact_anterior := v_Fact.Idfacturacion;
          END IF;
        END IF;
      End Loop;
    End If;

    Rc := Ltrim(Rc, ',');
    Return(Rc);

  Exception
    When No_Data_Found Then
      Return(1);
    When Others Then
      Return(-1);

  End Func_Facturaciones_Intervalo;


  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURACIONES_INTERVALO                                                                  */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 22/06/2009                                                                                   */
  /* Autor:         Jose Barrientos Diaz                                                                          */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Factura_Inter_Grupos(p_Idinstitucion  Number,
                                        p_Idfacturacion1 Number,
                                        p_Idfacturacion2 Number,
                                        p_GrupoFact Number)
    Return Varchar2 Is

    Cursor c_Facturaciones(p_Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type, p_Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type) Is
      Select Fac.Idfacturacion, gru.idgrupofacturacion
        From Fcs_Facturacionjg Fac, fcs_fact_grupofact_hito gru
       Where Fac.Prevision = '0'
         And Fac.Regularizacion = '0'
         And Fac.Idinstitucion = p_Idinstitucion
         and Fac.Idinstitucion = gru.Idinstitucion
         AND Fac.Idfacturacion = gru.Idfacturacion
         And Fac.Fechadesde >= p_Fecha_Ini
         And Fac.Fechahasta <= p_Fecha_Fin
       Order By Fac.Fechadesde Asc;

    Fecha_1   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_2   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_3   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_4   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type;
    Rc        Varchar2(4000);

    idfact_anterior Fcs_Facturacionjg.Idfacturacion%Type;

  Begin

    -- Obtenemos las fechas de la primera facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_1, Fecha_2
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion1;
    -- Obtenemos las fechas de la segunda facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_3, Fecha_4
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion2;

    -- La primera fechadesde marca el inicio del intervalo
    If (Fecha_1 < Fecha_3) Then
      Fecha_Ini := Fecha_1;
    Else
      Fecha_Ini := Fecha_3;
    End If;
    -- La ultima fechahasta marca el final del intervalo
    If (Fecha_2 > Fecha_4) Then
      Fecha_Fin := Fecha_2;
    Else
      Fecha_Fin := Fecha_4;
    End If;

    -- Sacando solo una facturacion si solo se selecciona una
    If (p_Idfacturacion2 Is Null) Then
      Return p_Idfacturacion1;
    Elsif (p_Idfacturacion1 = p_Idfacturacion2) Then
      Return p_Idfacturacion1;
    End If;

    idfact_anterior := -1;
    For v_Fact In c_Facturaciones(Fecha_Ini, Fecha_Fin) Loop
      IF (p_GrupoFact = -1 Or v_Fact.Idgrupofacturacion = p_GrupoFact) THEN
        If (v_Fact.Idfacturacion <> idfact_anterior) Then
          -- Control para no duplicar el pago
          Rc := Rc || ', ' || v_Fact.Idfacturacion;
          idfact_anterior := v_Fact.Idfacturacion;
        END IF;
      END IF;
    End Loop;

    Rc := Ltrim(Rc, ',');
    Return(Rc);

  Exception
    When No_Data_Found Then
      Return(1);
    When Others Then
      Return(-1);

  End Func_Factura_Inter_Grupos;


  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_MARCAR_FACTURADOS                                                                         */
  /* Descripcion: Marca todos los registros de cabeceras guardias, asistencias y actuaciones utilizados en la     */
    /*              facturacion como facturados                                                                     */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* IDINSTITUCION         IN       Identificador de la Institucion                                NUMBER         */
  /* IDFACTURACION         IN       Identificador de la Facturacion                                NUMBER         */
  /* codigoRetorno         OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* error                 OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 19/01/2010                                                                                   */
  /* Autor:          Salvador Madrid-Salvador                                                                           */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_MARCAR_FACTURADOS(idInstitucion IN VARCHAR2,
                                       idFacturacion in VARCHAR2,
                                       codigoRetorno OUT VARCHAR2,
                                       error         OUT VARCHAR2) is
    CURSOR cabeceras(pIdInstitucion IN VARCHAR2, pIdFacturacion IN VARCHAR2) IS
      select cab.idinstitucion,
             cab.idturno,
             cab.idguardia,
             cab.idpersona,
             cab.fechainicio,
             cab.fecha_fin,
             cab.facturado
        from FCS_FACT_APUNTE APU, SCS_CABECERAGUARDIAS CAB
       where cab.idinstitucion = apu.idinstitucion
         and cab.idturno = apu.idturno
         and cab.idguardia = apu.idguardia
         and cab.idpersona = apu.idpersona
         and cab.fechainicio = apu.fechainicio
         and apu.idinstitucion = pIdInstitucion
         AND apu.idfacturacion = pIdFacturacion;
    cabeceras_rec cabeceras%ROWTYPE;
    CURSOR guardias(pIdInstitucion IN VARCHAR2, pIdFacturacion IN VARCHAR2) IS
      select gua.idinstitucion,
             gua.idturno,
             gua.idguardia,
             gua.idpersona,
             gua.fechainicio,
             gua.fechafin,
             gua.facturado
        from FCS_FACT_GUARDIASCOLEGIADO FGUA, SCS_GUARDIASCOLEGIADO GUA
       where gua.idinstitucion = fgua.idinstitucion
         and gua.idturno = fgua.idturno
         and gua.idguardia = fgua.idguardia
         and gua.idpersona = fgua.idpersona
         and gua.fechainicio = fgua.fechainicio
         and gua.fechafin = fgua.fechafin
         and fgua.idinstitucion = pIdInstitucion
         AND fgua.idfacturacion = pIdFacturacion;
    guardias_rec guardias%ROWTYPE;
    CURSOR asistencias(pIdInstitucion IN VARCHAR2, pIdFacturacion IN VARCHAR2) IS
      select asi.idinstitucion,
             asi.anio,
             asi.numero,
             asi.idturno,
             asi.idguardia,
             asi.idpersonacolegiado,
             asi.facturado
        from FCS_FACT_ASISTENCIA FASI, SCS_ASISTENCIA ASI
       where fasi.idinstitucion = asi.idinstitucion
         and fasi.anio = asi.anio
         and fasi.numero = asi.numero
         AND fasi.idfacturacion = pIdFacturacion
         AND fasi.idinstitucion = pIdInstitucion;
    asistencias_rec asistencias%ROWTYPE;
    CURSOR actuaciones(pIdInstitucion IN VARCHAR2, pIdFacturacion IN VARCHAR2) IS
      select actasi.idinstitucion,
             actasi.anio,
             actasi.numero,
             actasi.idactuacion,
             actasi.facturado
        from FCS_FACT_ACTUACIONASISTENCIA FACTASI,
             SCS_ACTUACIONASISTENCIA      ACTASI
       where actasi.idinstitucion = factasi.idinstitucion
         and actasi.anio = factasi.anio
         and actasi.numero = factasi.numero
         and actasi.idactuacion = factasi.idactuacion
         AND factasi.idfacturacion = pIdFacturacion
         AND factasi.idinstitucion = pIdInstitucion;
    actuaciones_rec   actuaciones%ROWTYPE;
    updateCabeceras   VARCHAR2(3000);
    updateGuardias    VARCHAR2(3000);
    updateAsistencias VARCHAR2(3000);
    updateActuaciones VARCHAR2(3000);
  BEGIN
    updateCabeceras   := 'update SCS_CABECERAGUARDIAS
                             set facturado=1, idFacturacion= :idFacturacion
                           where idInstitucion= :idInstitucion
                             and idTurno= :idTurno
                             and idGuardia= :idGuardia
                             and idPersona= :idPersona
                             and fechaInicio= :fechaInicio
                             and idFacturacion is null';
    updateGuardias    := 'update SCS_GUARDIASCOLEGIADO
                             set facturado=1, idFacturacion= :idFacturacion
                           where idInstitucion= :idInstitucion
                             and idTurno= :idTurno
                             and idGuardia= :idGuardia
                             and idPersona= :idPersona
                             and fechaInicio= :fechaInicio
                             and fechaFin= :fechaFin
                             and idFacturacion is null';
    updateAsistencias := 'update SCS_ASISTENCIA
                             set facturado=1, idFacturacion= :idFacturacion
                           where idInstitucion= :idInstitucion
                             and anio= :anio
                             and numero= :numero
                             and idFacturacion is null';
    updateActuaciones := 'update SCS_ACTUACIONASISTENCIA
                             set facturado=1, idFacturacion= :idFacturacion
                           where idInstitucion= :idInstitucion
                             and anio= :anio
                             and numero= :numero
                             and idActuacion= :idActuacion
                             and idFacturacion is null';

    FOR cabeceras_rec IN cabeceras(idInstitucion, idFacturacion) LOOP
      EXECUTE IMMEDIATE updateCabeceras
        USING idFacturacion, cabeceras_rec.idInstitucion, cabeceras_rec.idturno, cabeceras_rec.idguardia, cabeceras_rec.idpersona, cabeceras_rec.fechainicio;
    END LOOP;

    FOR guardias_rec IN guardias(idInstitucion, idFacturacion) LOOP
      EXECUTE IMMEDIATE updateGuardias
        USING idFacturacion, guardias_rec.idInstitucion, guardias_rec.idturno, guardias_rec.idguardia, guardias_rec.idpersona, guardias_rec.fechainicio, guardias_rec.fechafin;
    END LOOP;

    FOR asistencias_rec IN asistencias(idInstitucion, idFacturacion) LOOP
      EXECUTE IMMEDIATE updateAsistencias
        USING idFacturacion, asistencias_rec.idInstitucion, asistencias_rec.anio, asistencias_rec.numero;
    END LOOP;

    FOR actuaciones_rec IN actuaciones(idInstitucion, idFacturacion) LOOP
      EXECUTE IMMEDIATE updateActuaciones
        USING idFacturacion, actuaciones_rec.idInstitucion, actuaciones_rec.anio, actuaciones_rec.numero, actuaciones_rec.idactuacion;
    END LOOP;

    codigoRetorno := TO_CHAR(0);
    error         := 'El proceso: PROC_FCS_MARCAR_FACTURADOS ha finalizado correctamente';

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      codigoRetorno := to_char(SQLCODE);
      error         := 'Error en el proceso: PROC_FCS_MARCAR_FACTURADOS. ' ||
                       SQLERRM;

  END PROC_FCS_MARCAR_FACTURADOS;
END PKG_SIGA_FACTURACION_SJCS;
/
