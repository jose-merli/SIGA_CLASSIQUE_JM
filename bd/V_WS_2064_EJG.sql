CREATE OR REPLACE VIEW V_WS_2064_EJG AS
SELECT EJGREMESA.IDREMESA
      , EJGREMESA.IDEJGREMESA
      , EJGREMESA.IDINSTITUCION
      , SUBSTR(R.NUMERO, LENGTH(R.NUMERO)-1) AS NUMERO_ENVIO
      , EJG.IDTIPOEJG
      , EJG.ANIO
      , EJG.NUMERO
      , EJG.NUMEJG
      , EJG.IDPERSONAJG AS IDPERSONASOLICITANTE
      , (SELECT SUBSTR(P.CODIGO, INSTR(P.CODIGO, '-')+1) FROM PCAJG_ICAS P, PCAJG_ICAS_CENINSTITUCION PS
                WHERE P.IDENTIFICADOR = PS.IDENTIFICADOR
                AND P.IDINSTITUCION = PS.IDINSTITUCION_PCAJG
                AND P.IDINSTITUCION = EJG.IDINSTITUCION
                AND PS.IDINSTITUCION_SIGA = EJG.IDINSTITUCION) AS NS_COLEXIO --OB
      , EJG.NUMEJG NS_NUMERO_EXPEDIENTE --OB
      , EJG.ANIO NS_ANO --OB
      , EJG.FECHAAPERTURA AS DATA_RECEPCION --OB TODO
      , (CASE WHEN ASIS.COMISARIA IS NOT NULL
              THEN 1--ADMINISTRATIVO
              WHEN ASIS.JUZGADO IS NOT NULL
              THEN 0--XUDICIAL
              WHEN D.IDJUZGADO IS NOT NULL
              THEN 0--XUDICIAL
              WHEN EJG.COMISARIA IS NOT NULL
              THEN 1--ADMINISTRATIVO
              WHEN EJG.JUZGADO IS NOT NULL
              THEN 0--XUDICIAL
              ELSE NULL
              END ) AS TIPO_PROCEDEMENTO_CHOICE
      /*, (CASE WHEN ASIS.IDTURNO IS NOT NULL
              THEN (SELECT T.CODIGOEXT FROM SCS_TURNO T WHERE T.IDINSTITUCION = ASIS.IDINSTITUCION AND T.IDTURNO = ASIS.IDTURNO)
              WHEN D.IDTURNO IS NOT NULL
              THEN (SELECT T.CODIGOEXT FROM SCS_TURNO T WHERE T.IDINSTITUCION = D.IDINSTITUCION AND T.IDTURNO = D.IDTURNO)
              WHEN EJG.GUARDIATURNO_IDTURNO IS NOT NULL
              THEN (SELECT T.CODIGOEXT FROM SCS_TURNO T WHERE T.IDINSTITUCION = EJG.IDINSTITUCION AND T.IDTURNO = EJG.GUARDIATURNO_IDTURNO)
              ELSE NULL
              END) AS P_XURISDICCION*/
      , (CASE WHEN D.IDPRETENSION IS NOT NULL
              THEN (SELECT PJ.CODIGO FROM PCAJG_JURISDICCION PJ, PCAJG_JURIS_SCS_JURIS PSJ, SCS_PRETENSION P
                           WHERE PJ.IDINSTITUCION = PSJ.IDINSTITUCION
                           AND PJ.IDENTIFICADOR = PSJ.IDENTIFICADOR
                           AND PSJ.IDJURISDICCION = P.IDJURISDICCION
                           AND PSJ.IDINSTITUCION = P.IDINSTITUCION
                           AND P.IDINSTITUCION = D.IDINSTITUCION
                           AND P.IDPRETENSION = D.IDPRETENSION)
              WHEN EJG.IDPRETENSION IS NOT NULL
              THEN (SELECT PJ.CODIGO FROM PCAJG_JURISDICCION PJ, PCAJG_JURIS_SCS_JURIS PSJ, SCS_PRETENSION P
                           WHERE PJ.IDINSTITUCION = PSJ.IDINSTITUCION
                           AND PJ.IDENTIFICADOR = PSJ.IDENTIFICADOR
                           AND PSJ.IDJURISDICCION = P.IDJURISDICCION
                           AND PSJ.IDINSTITUCION = P.IDINSTITUCION
                           AND P.IDINSTITUCION = EJG.IDINSTITUCION
                           AND P.IDPRETENSION = EJG.IDPRETENSION)
              ELSE NULL
              END) AS P_XURISDICCION
      , NULL AS PRO_FAMILIA
      , (CASE WHEN D.IDPRETENSION IS NOT NULL
              THEN (SELECT P.CODIGOEXT FROM SCS_PRETENSION P WHERE P.IDINSTITUCION = D.IDINSTITUCION AND P.IDPRETENSION = D.IDPRETENSION)
              WHEN EJG.IDPRETENSION IS NOT NULL
              THEN (SELECT P.CODIGOEXT FROM SCS_PRETENSION P WHERE P.IDINSTITUCION = EJG.IDINSTITUCION AND P.IDPRETENSION = EJG.IDPRETENSION)
              ELSE NULL
              END) AS P_TPX_TIPOPROCXUDICIAL
      , F_SIGA_GETNUMANIOPROC(D.NUMPROCEDIMIENTO, EJG.NUMEROPROCEDIMIENTO, 'A') AS P_TPX_ANIO
      /*, (CASE WHEN D.NUMPROCEDIMIENTO IS NOT NULL
              THEN (CASE INSTR(D.NUMPROCEDIMIENTO, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(D.NUMPROCEDIMIENTO, INSTR(D.NUMPROCEDIMIENTO, '/')+1, 4)
                   END)
              ELSE (CASE INSTR(EJG.NUMEROPROCEDIMIENTO, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(EJG.NUMEROPROCEDIMIENTO, INSTR(EJG.NUMEROPROCEDIMIENTO, '/')+1, 4)
                   END)
         END) AS P_TPX_ANIO*/
      , F_SIGA_GETNUMANIOPROC(D.NUMPROCEDIMIENTO, EJG.NUMEROPROCEDIMIENTO, 'N') AS P_TPX_NUMERO
      /*, (CASE WHEN D.NUMPROCEDIMIENTO IS NOT NULL
              THEN (CASE INSTR(D.NUMPROCEDIMIENTO, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(D.NUMPROCEDIMIENTO, 0, INSTR(D.NUMPROCEDIMIENTO, '/')-1)
                   END)
              ELSE (CASE INSTR(EJG.NUMEROPROCEDIMIENTO, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(EJG.NUMEROPROCEDIMIENTO, 0, INSTR(EJG.NUMEROPROCEDIMIENTO, '/')-1)
                   END)
         END) AS P_TPX_NUMERO*/
      , EJG.NIG AS P_TPX_NIG
      , NULL AS P_TPX_DESCRIPCION
      , (CASE WHEN ASIS.JUZGADO IS NOT NULL
--              THEN (SELECT J.CODIGOEXT FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = ASIS.JUZGADOIDINSTITUCION AND J.IDJUZGADO = ASIS.JUZGADO)
              THEN (SELECT SUBSTR(J.CODIGOEXT, 0, INSTR(J.CODIGOEXT, '-')-1) FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = ASIS.JUZGADOIDINSTITUCION AND J.IDJUZGADO = ASIS.JUZGADO)
              WHEN D.IDJUZGADO IS NOT NULL
              THEN (SELECT SUBSTR(J.CODIGOEXT, 0, INSTR(J.CODIGOEXT, '-')-1) FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = D.IDINSTITUCION_JUZG AND J.IDJUZGADO = D.IDJUZGADO)
              WHEN EJG.JUZGADO IS NOT NULL
              THEN (SELECT SUBSTR(J.CODIGOEXT, 0, INSTR(J.CODIGOEXT, '-')-1) FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = EJG.JUZGADOIDINSTITUCION AND J.IDJUZGADO = EJG.JUZGADO)
              ELSE NULL
              END) P_TPXO_ORGANO
      , (CASE WHEN ASIS.JUZGADO IS NOT NULL
              THEN (SELECT SUBSTR(J.CODIGOEXT, LENGTH(J.CODIGOEXT)-1) FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = ASIS.JUZGADOIDINSTITUCION AND J.IDJUZGADO = ASIS.JUZGADO)
              WHEN D.IDJUZGADO IS NOT NULL
              THEN (SELECT SUBSTR(J.CODIGOEXT, LENGTH(J.CODIGOEXT)-1) FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = D.IDINSTITUCION_JUZG AND J.IDJUZGADO = D.IDJUZGADO)
              WHEN EJG.JUZGADO IS NOT NULL
              THEN (SELECT SUBSTR(J.CODIGOEXT, LENGTH(J.CODIGOEXT)-1) FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = EJG.JUZGADOIDINSTITUCION AND J.IDJUZGADO = EJG.JUZGADO)
              ELSE NULL
              END) AS P_TPXO_NUMEROSALASECCION --OB TODO METEMOS UN 0. SANTIAGO DICE QUE NO LO USA SEGUN DANI
      , (CASE WHEN ASIS.JUZGADO IS NOT NULL
         THEN (SELECT SUBSTR(PJ.CODIGOEXT, 0, INSTR(PJ.CODIGOEXT, '-')-1) FROM CEN_PARTIDOJUDICIAL PJ, CEN_POBLACIONES POBPJ, SCS_JUZGADO J
                      WHERE PJ.IDPARTIDO = POBPJ.IDPARTIDO AND POBPJ.IDPOBLACION = J.IDPOBLACION AND J.IDINSTITUCION = ASIS.JUZGADOIDINSTITUCION AND J.IDJUZGADO = ASIS.JUZGADO)
               WHEN D.IDJUZGADO IS NOT NULL
               THEN (SELECT SUBSTR(PJ.CODIGOEXT, 0, INSTR(PJ.CODIGOEXT, '-')-1) FROM CEN_PARTIDOJUDICIAL PJ, CEN_POBLACIONES POBPJ, SCS_JUZGADO J
                      WHERE PJ.IDPARTIDO = POBPJ.IDPARTIDO AND POBPJ.IDPOBLACION = J.IDPOBLACION AND J.IDINSTITUCION = D.IDINSTITUCION_JUZG AND J.IDJUZGADO = D.IDJUZGADO)
                    WHEN EJG.JUZGADO IS NOT NULL
               THEN (SELECT SUBSTR(PJ.CODIGOEXT, 0, INSTR(PJ.CODIGOEXT, '-')-1) FROM CEN_PARTIDOJUDICIAL PJ, CEN_POBLACIONES POBPJ, SCS_JUZGADO J
                      WHERE PJ.IDPARTIDO = POBPJ.IDPARTIDO AND POBPJ.IDPOBLACION = J.IDPOBLACION AND J.IDINSTITUCION = EJG.JUZGADOIDINSTITUCION AND J.IDJUZGADO = EJG.JUZGADO)
               ELSE NULL
               END) P_TPXO_PARTIDO_XUDICIAL
       , (CASE WHEN ASIS.COMISARIA IS NOT NULL
              THEN (SELECT SUBSTR(C.CODIGOEXT, 1, 2) FROM SCS_COMISARIA C WHERE C.IDINSTITUCION = ASIS.COMISARIAIDINSTITUCION AND C.IDCOMISARIA = ASIS.COMISARIA)
              WHEN EJG.COMISARIA IS NOT NULL
              THEN (SELECT SUBSTR(C.CODIGOEXT, 1, 2) FROM SCS_COMISARIA C WHERE C.IDINSTITUCION = EJG.COMISARIAIDINSTITUCION AND C.IDCOMISARIA = EJG.COMISARIA)
              ELSE NULL
              END) AS P_TPXA_TIPOUNIDADE--COMISARIA
        , NULL AS P_TPXA_UNIDADE
        , (CASE WHEN ASIS.COMISARIA IS NOT NULL
          THEN (SELECT SUBSTR(PJ.CODIGOEXT, 0, INSTR(PJ.CODIGOEXT, '-')-1) FROM CEN_PARTIDOJUDICIAL PJ, CEN_POBLACIONES POBPJ, SCS_COMISARIA C
                      WHERE PJ.IDPARTIDO = POBPJ.IDPARTIDO AND POBPJ.IDPOBLACION = C.IDPOBLACION AND C.IDINSTITUCION = ASIS.COMISARIAIDINSTITUCION AND C.IDCOMISARIA = ASIS.COMISARIA)
          WHEN EJG.COMISARIA IS NOT NULL
          THEN (SELECT SUBSTR(PJ.CODIGOEXT, 0, INSTR(PJ.CODIGOEXT, '-')-1) FROM CEN_PARTIDOJUDICIAL PJ, CEN_POBLACIONES POBPJ, SCS_COMISARIA C
                      WHERE PJ.IDPARTIDO = POBPJ.IDPARTIDO AND POBPJ.IDPOBLACION = C.IDPOBLACION AND C.IDINSTITUCION = EJG.COMISARIAIDINSTITUCION AND C.IDCOMISARIA = EJG.COMISARIA)
          ELSE NULL
          END) AS P_TPXA_PARTIDO_XUDICIAL --PARTIDO JUDICIAL COMISARIA
      , F_SIGA_GETNUMANIOPROC(ASIS.NUMERODILIGENCIA, EJG.NUMERODILIGENCIA, 'N') AS P_TPA_NUMERO
      /*, (CASE WHEN ASIS.NUMERODILIGENCIA IS NOT NULL
              THEN (CASE INSTR(ASIS.NUMERODILIGENCIA, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(ASIS.NUMERODILIGENCIA, 0, INSTR(ASIS.NUMERODILIGENCIA, '/')-1)
                   END)
              ELSE (CASE INSTR(EJG.NUMERODILIGENCIA, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(EJG.NUMERODILIGENCIA, 0, INSTR(EJG.NUMERODILIGENCIA, '/')-1)
                   END)
         END) AS P_TPA_NUMERO*/
      , F_SIGA_GETNUMANIOPROC(ASIS.NUMERODILIGENCIA, EJG.NUMERODILIGENCIA, 'A') AS P_TPA_ANO
      /*, (CASE WHEN ASIS.NUMERODILIGENCIA IS NOT NULL
              THEN (CASE INSTR(ASIS.NUMERODILIGENCIA, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(ASIS.NUMERODILIGENCIA, INSTR(ASIS.NUMERODILIGENCIA, '/')+1)
                   END)
              ELSE (CASE INSTR(EJG.NUMERODILIGENCIA, '/')
                   WHEN 0 THEN NULL
                   ELSE SUBSTR(EJG.NUMERODILIGENCIA, INSTR(EJG.NUMERODILIGENCIA, '/')+1)
                   END)
         END) AS P_TPA_ANO*/
      , EJG.OBSERVACIONES AS P_PRETENSION
      , (CASE
             WHEN F_SIGA_GETDELITOS(EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO, 4) = ''
             THEN (SELECT CA.CODIGO FROM SCS_TIPOENCALIDAD CA WHERE CA.IDINSTITUCION = EJG.CALIDADIDINSTITUCION AND CA.IDTIPOENCALIDAD = EJG.IDTIPOENCALIDAD)
             END) AS P_CONDICION
      , (CASE
             WHEN F_SIGA_GETDELITOS(EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO, 4) = ''
             THEN (SELECT TEC.CODIGOEXT FROM SCS_TIPOEJGCOLEGIO TEC WHERE TEC.IDINSTITUCION = EJG.IDINSTITUCION AND TEC.IDTIPOEJGCOLEGIO = EJG.IDTIPOEJGCOLEGIO)
             END) AS P_TIPO_EXPEDIENTE
      , F_SIGA_GETDELITOS(EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO, 4) AS P_INFRACCION_IMPUTADA
      , NULL AS P_DATA_INICIO

      ,(SELECT CODIGOEXT FROM CEN_INSTITUCION P WHERE P.IDINSTITUCION = DECODE(COL_AS.IDPERSONA, NULL, COL_D.IDINSTITUCION, COL_AS.IDINSTITUCION)) AS A_COLEXIO
      , DECODE(COL_AS.IDPERSONA, NULL, COL_D.NCOLEGIADO, COL_AS.NCOLEGIADO) AS A_NUM_COLEXIADO
      , DECODE(COL_AS.IDPERSONA, NULL, P_D.NIFCIF, P_AS.NIFCIF) AS A_NIF
      , DECODE(COL_AS.IDPERSONA, NULL, P_D.NOMBRE, P_AS.NOMBRE) AS A_NOME
      , DECODE(COL_AS.IDPERSONA, NULL, P_D.APELLIDOS1, P_AS.APELLIDOS1) AS A_PRIMERAPELLIDO
      , DECODE(COL_AS.IDPERSONA, NULL, P_D.APELLIDOS2, P_AS.APELLIDOS2) AS A_SEGUNDOAPELLIDO
      , F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 1) AS A_ENDEREZO
      , SUBSTR(F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 3), 1, 25) AS A_LOCALIDADE
      , DECODE(F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 9)
                , '0', '99999', SUBSTR(F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 9), 1, 5)) AS A_MUNICIPIO
      , DECODE(F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 10), 00, '99'
				, F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 10)) AS A_PROVINCIA
      , F_SIGA_GETDIRECCIONDESPACHO(NVL(COL_AS.IDINSTITUCION, COL_D.IDINSTITUCION), NVL(COL_AS.IDPERSONA, COL_D.IDPERSONA), 4, 2) AS A_CODPOSTAL
      , DECODE (EJG.IDRENUNCIA, NULL, 0, 1) AS A_RENUNCIA_AVOGADO--RenHono o RenunDesig
      ,(SELECT CP.CODIGOEJIS FROM SCS_PROCURADOR P ,CEN_COLEGIOPROCURADOR CP
      WHERE P.IDCOLPROCURADOR = CP.IDCOLPROCURADOR AND P.IDPROCURADOR = DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.IDPROCURADOR, PROCD.IDPROCURADOR)
      AND P.IDINSTITUCION = DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.IDINSTITUCION, PROCD.IDINSTITUCION)) AS  PR_COLEXIO

--      ,  (select codigoext from cen_institucion p where p.idinstitucion = COL_AS.IDINSTITUCION) AS  PR_COLEXIO2
      , DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.NCOLEGIADO, PROCD.NCOLEGIADO) AS PR_NUM_COLEXIADO
      , DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.CODIGO, PROCD.CODIGO) AS PR_NIF
      , DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.NOMBRE, PROCD.NOMBRE) AS PR_NOME
      , DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.APELLIDOS1, PROCD.APELLIDOS1) AS PR_PRIMERAPELLIDO
      , DECODE(PROCD.IDPROCURADOR, NULL, PROCEJG.APELLIDOS2, PROCD.APELLIDOS2) AS PR_SEGUNDOAPELLIDO
      , (CASE
             WHEN EJG.IDRENUNCIA = 2
             THEN 1
             WHEN PROCD.IDPROCURADOR IS NOT NULL OR PROCEJG.IDPROCURADOR IS NOT NULL
             THEN 0
             ELSE
             NULL
             END) AS PR_RENUNCIA_PROCURADOR --Cuando renuncia designaci�n (Abo y Proc)
      , NULL AS OBSERVACIONES
      -- DATOS REPRESENTANTE
      , DECODE(REP.IDPERSONA, NULL, 0, 1) AS REP_TIENE_REPRESENTANTE
      , REP.NOMBRE REP_NOME
      , REP.APELLIDO1 REP_PRIMERAPELLIDO
      , REP.APELLIDO2 REP_SEGUNDOAPELLIDO
      , (CASE WHEN REP.EDAD IS NOT NULL
              THEN REP.EDAD
              ELSE TRUNC(MONTHS_BETWEEN(SYSDATE, REP.FECHANACIMIENTO)/12)
         END) AS REP_IDADE
      , 22 AS REP_PARENTESCO--SEGUN MAIL DEL 16/10/2012
      , (SELECT PI.CODIGO
               FROM PCAJG_TIPO_IDENTIFICACION PI, PCAJG_TIPO_IDENTIFICACION_CENT PS
               WHERE PI.IDENTIFICADOR = PS.IDENTIFICADOR
                AND PI.IDINSTITUCION = PS.IDINSTITUCION
                AND PS.IDTIPOIDENTIFICACION = REP.IDTIPOIDENTIFICACION
                AND PS.IDINSTITUCION = REP.IDINSTITUCION) AS REP_TIPOIDENTIFICADOR
      , REP.NIF REP_IDENTIFICADOR
      --AUTORIZACIONES
      , DECODE(EEJG.IDPETICION, NULL, 'N', 'S') AS A_AEAT_TGSS_CATASTRO
      , 'S' AS A_IDENTIDAD --TODO CAMPO OBLIGATORIO QUE HAY QUE METER NUEVO CHECK EN SIGA. PENDIENTE DE APROBAR LP.
FROM CAJG_REMESA R,
     CAJG_EJGREMESA EJGREMESA,
     SCS_EJG EJG,
     SCS_EJGDESIGNA ED,
     SCS_DESIGNA D,
     SCS_ASISTENCIA ASIS,
     CEN_COLEGIADO COL_AS,
     SCS_DESIGNASLETRADO DL,
     CEN_COLEGIADO COL_D,
     CEN_PERSONA P_AS,
     CEN_PERSONA P_D,
     SCS_DESIGNAPROCURADOR DP,
     SCS_PROCURADOR PROCD,
     SCS_PROCURADOR PROCEJG,
     SCS_PERSONAJG SOL,
     SCS_PERSONAJG REP,
     SCS_EEJG_PETICIONES EEJG
WHERE R.IDINSTITUCION = EJGREMESA.IDINSTITUCION
   AND R.IDREMESA = EJGREMESA.IDREMESA
   AND EJG.IDINSTITUCION =  EJGREMESA.IDINSTITUCION
   AND EJG.ANIO = EJGREMESA.ANIO
   AND EJG.NUMERO = EJGREMESA.NUMERO
   AND EJG.IDTIPOEJG = EJGREMESA.IDTIPOEJG
   AND EJG.IDINSTITUCION = ED.IDINSTITUCION(+)
   AND EJG.IDTIPOEJG = ED.IDTIPOEJG(+)
   AND EJG.ANIO = ED.ANIOEJG(+)
   AND EJG.NUMERO = ED.NUMEROEJG(+)
   AND ED.IDINSTITUCION = D.IDINSTITUCION(+)
   AND ED.IDTURNO = D.IDTURNO(+)
   AND ED.ANIODESIGNA = D.ANIO(+)
   AND ED.NUMERODESIGNA = D.NUMERO(+)
   AND EJG.IDINSTITUCION = ASIS.IDINSTITUCION(+)
   AND EJG.IDTIPOEJG = ASIS.EJGIDTIPOEJG(+)
   AND EJG.ANIO = ASIS.EJGANIO(+)
   AND EJG.NUMERO = ASIS.EJGNUMERO(+)
   AND (ASIS.IDINSTITUCION IS NULL OR (ASIS.ANIO || LPAD(ASIS.NUMERO, 10, '0')) = --no tiene asistencia o es la �ltima
       (SELECT MIN(A.ANIO || LPAD(A.NUMERO, 10, '0'))
          FROM SCS_ASISTENCIA A
         WHERE EJG.IDINSTITUCION = A.IDINSTITUCION
           AND EJG.IDTIPOEJG = A.EJGIDTIPOEJG
           AND EJG.ANIO = A.EJGANIO
           AND EJG.NUMERO = A.EJGNUMERO))
   AND ASIS.IDINSTITUCION = COL_AS.IDINSTITUCION(+)
   AND ASIS.IDPERSONACOLEGIADO = COL_AS.IDPERSONA(+)
   AND D.IDINSTITUCION = DL.IDINSTITUCION(+)
   AND D.IDTURNO = DL.IDTURNO(+)
   AND D.ANIO = DL.ANIO(+)
   AND D.NUMERO = DL.NUMERO(+)
   AND DL.FECHARENUNCIA(+) IS NULL
   AND DL.IDINSTITUCION = COL_D.IDINSTITUCION(+)
   AND DL.IDPERSONA = COL_D.IDPERSONA(+)
   AND COL_AS.IDPERSONA = P_AS.IDPERSONA(+)
   AND COL_D.IDPERSONA = P_D.IDPERSONA(+)
   AND D.IDINSTITUCION = DP.IDINSTITUCION(+)
   AND D.IDTURNO = DP.IDTURNO(+)
   AND D.ANIO = DP.ANIO(+)
   AND D.NUMERO = DP.NUMERO(+)
   AND DP.IDINSTITUCION_PROC = PROCD.IDINSTITUCION(+)
   AND DP.IDPROCURADOR = PROCD.IDPROCURADOR(+)
   AND DP.FECHARENUNCIA(+) IS NULL
   AND EJG.IDINSTITUCION_PROC = PROCEJG.IDINSTITUCION(+)
   AND EJG.IDPROCURADOR = PROCEJG.IDPROCURADOR(+)
   --que no tenga ejgDesigna o si tiene sea la �ltima
   AND (ED.IDINSTITUCION IS NULL OR (ED.ANIODESIGNA || LPAD(ED.NUMERODESIGNA, 10, 0)) =
       (SELECT MIN(ED2.ANIODESIGNA || LPAD(ED2.NUMERODESIGNA, 10, 0))
          FROM SCS_EJGDESIGNA ED2
         WHERE ED2.IDINSTITUCION = EJG.IDINSTITUCION
           AND ED2.ANIOEJG = EJG.ANIO
           AND ED2.NUMEROEJG = EJG.NUMERO
           AND ED2.IDTIPOEJG = EJG.IDTIPOEJG))
   AND EJG.IDINSTITUCION = SOL.IDINSTITUCION(+)
   AND EJG.IDPERSONAJG = SOL.IDPERSONA(+)
   AND SOL.IDINSTITUCION = REP.IDINSTITUCION(+)
   AND SOL.IDREPRESENTANTEJG = REP.IDPERSONA(+)
   AND EJG.IDINSTITUCION = EEJG.IDINSTITUCION(+)
   AND EJG.IDTIPOEJG = EEJG.IDTIPOEJG(+)
   AND EJG.ANIO = EEJG.ANIO(+)
   AND EJG.NUMERO = EEJG.NUMERO(+)
   AND EJG.IDPERSONAJG = EEJG.IDPERSONA(+)
