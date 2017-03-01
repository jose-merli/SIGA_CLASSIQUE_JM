create or replace function f_comunicaciones_ejg_2003_CAB(P_INSTITUCION IN SCS_EJG.IDINSTITUCION%type,
                                                         P_IDREMESA    IN CAJG_REMESA.IDREMESA%type)
  return clob is
  salida      clob;
  sentencia   varchar(30000);
  condiciones   clob;
  cuentaCondicion integer(2);

  TYPE tipo_cod_desc IS RECORD(
    condicion   VARCHAR2(250),
    descripcion CAJG_RESPUESTA_EJGREMESA.DESCRIPCION%TYPE);


  TYPE tipo_array_codDesc IS TABLE OF tipo_cod_desc index by binary_integer;
  v_arrayCondiciones tipo_array_codDesc;

begin
  condiciones := '';

  salida := '''CAB'' AS TIPO_REGISTRO_CAB
          , ''001'' AS CAB1_TIPO_INTERCAMBIO --Traslado de expediente
          , LPAD(ER.NUMEROINTERCAMBIO, 7, ''0'') AS CAB2_NUMERO_INTERCAMBIO --Numero secuencial
          , LPAD(R.PREFIJO, 4, ''0'') AS CAB3_ANIO_INTERCAMBIO --En el contador meteremos como prefijo el año
          , ''$$'' AS GRUPO_1
     , ''EXP'' AS TIPO_REGISTRO_EXP
          , LPAD(EJG.NUMEJG, 8, ''0'') AS EXP1_NUM_EXPEDIENTE --OB Numero de EJG
          , LPAD(EJG.ANIO, 4, ''0'') AS EXP2_ANIO_EXPEDIENTE --NOB Año del EJG
          , (CASE
                  WHEN EJG.IDTIPODICTAMENEJG = 5 --ARCHIVADO CON DESIGNACION
                  THEN ''ARC''
                  WHEN EJG.IDPRECEPTIVO = 2-- NO PRECEPTIVO
                  THEN ''EXT''
                  WHEN EJG.IDRENUNCIA IN (1, 2) --RENUNCIA HONORARIOS O DESIGNACION
                  THEN ''EXT''
                  WHEN (SELECT COUNT(1) FROM SCS_TIPOFUNDAMENTOCALIF FC
                       WHERE FC.IDINSTITUCION = EJG.IDINSTITUCION
                       AND FC.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF
                       AND FC.CODIGO IN (''EXP'', ''CSO'')) > 0
                  THEN ''EXT''
                  ELSE ''ORD''
                  END) AS EXP3_TIPO_EXPEDIENTE
          /*, (CASE
                  WHEN EJG.IDTIPODICTAMENEJG = 5 --ARCHIVADO CON DESIGNACION
                  THEN ''   ''
                  WHEN EJG.IDPRECEPTIVO = 2-- NO PRECEPTIVO
                  THEN ''NPL''--CM: No preceptivo letrado
                  WHEN EJG.IDRENUNCIA = 1 --RENUNCIA HONORARIOS
                  THEN ''RAB''--CM: Renuncia letrado
                  WHEN EJG.IDRENUNCIA = 2 --RENUNCIA DESIGNACION
                  THEN ''RPR''--CM: Renuncia profesionales
                  WHEN (SELECT COUNT(1) FROM SCS_TIPOFUNDAMENTOCALIF FC
                       WHERE FC.IDINSTITUCION = EJG.IDINSTITUCION
                       AND FC.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF
                       AND FC.CODIGO IN (''EXP'', ''CSO'')) > 0 -- Excepcional o Circunstancias sobrevenida
                  THEN (SELECT FC.CODIGO FROM SCS_TIPOFUNDAMENTOCALIF FC
                       WHERE FC.IDINSTITUCION = EJG.IDINSTITUCION
                       AND FC.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF)
                  ELSE ''   ''
                  END) AS EXP4_TIPO_EXPEDIENTE_EXTRAORD*/
          , RPAD(SUBSTR((CASE
                 WHEN EJG.IDTIPODICTAMENEJG = 5 --ARCHIVADO CON DESIGNACION
                 THEN '', ''--PQ CON SUBSTR QUITAMOS LA PRIMERA COMA
                 ELSE NVL(DECODE(EJG.IDPRECEPTIVO, 2, '',NPL'', '''')-- NO PRECEPTIVO
                        || DECODE(EJG.IDRENUNCIA, 1, '',RAB'', 2, '',RPR'', '''')--1 RENUNCIA HONORARIOS 2 RENUNCIA DESIGNACION
                        || DECODE((SELECT FC.CODIGO FROM SCS_TIPOFUNDAMENTOCALIF FC--Excepcional o Circunstancias sobrevenida
                                    WHERE FC.IDINSTITUCION = EJG.IDINSTITUCION
                                    AND FC.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF), ''EXP'', '',EXP'', ''CSO'', '',CSO'', ''''), '', '')
                 END), 2), 27, '' '') AS EXP4_TIPO_EXPEDIENTE_EXTRAORD
          , RPAD(NVL(TO_CHAR(EJG.FECHAAPERTURA, ''YYYYMMDD''), '' ''), 8, '' '') AS EXP5_FECHA_SOLICITUD --NO PUEDES SER NULO EN SIGA Confirmado E
          , RPAD(NVL((SELECT CODIGO FROM PCAJG_ICAS P, PCAJG_ICAS_CENINSTITUCION PS
                    WHERE P.IDENTIFICADOR = PS.IDENTIFICADOR
                    AND P.IDINSTITUCION = PS.IDINSTITUCION_PCAJG
                    AND PS.IDINSTITUCION_PCAJG = ER.IDINSTITUCION
                    AND PS.IDINSTITUCION_SIGA = ER.IDINSTITUCION), '' ''), 5, '' '') AS EXP6_COLEGIO_ABOGADOS --ICA
          , RPAD((CASE WHEN (SELECT COUNT(1) FROM SCS_TIPOFUNDAMENTOCALIF FC
                       WHERE FC.IDINSTITUCION = EJG.IDINSTITUCION
                       AND FC.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF
                       AND FC.CODIGO IN (''EXP'', ''CSO'')) > 0
                  THEN '' ''
                  ELSE NVL((SELECT TD.CODIGOEXT
                    FROM SCS_TIPODICTAMENEJG TD
                    WHERE TD.IDINSTITUCION = EJG.IDINSTITUCION
                    AND TD.IDTIPODICTAMENEJG = EJG.IDTIPODICTAMENEJG), '' '') END), 3, '' '') AS EXP7_CALIFICACION --Atención codigo id 4 no confirmado
          , RPAD(NVL(TO_CHAR(EJG.FECHADICTAMEN, ''YYYYMMDD''), '' ''), 8, '' '') AS EXP8_FECHA_DICTAMEN --Fecha del dictamen
          , RPAD((CASE WHEN (SELECT COUNT(1) FROM SCS_TIPOFUNDAMENTOCALIF FC
                       WHERE FC.IDINSTITUCION = EJG.IDINSTITUCION
                       AND FC.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF
                       AND FC.CODIGO IN (''EXP'', ''CSO'')) > 0
                  THEN '' ''
                  ELSE NVL((SELECT P.CODIGO
                    FROM PCAJG_MOTIVO_DICTAMEN P, PCAJG_MOTIVO_DICTAMEN_SCSTIPOF PS
                    WHERE P.IDENTIFICADOR = PS.IDENTIFICADOR
                    AND P.IDINSTITUCION = PS.IDINSTITUCION
                    AND P.IDINSTITUCION = ER.IDINSTITUCION
                    AND PS.IDFUNDAMENTOCALIF = EJG.IDFUNDAMENTOCALIF), '' '')END), 3, '' '') AS EXP9_FUNDAMENTO_CALIFICACION
          , (CASE WHEN EJG.IDPRECEPTIVO IN (1,3)
                  THEN ''S''
                  ELSE ''N''
                  END) AS EXP10_PRECISA_ABOGADO
          , (CASE WHEN EJG.IDPRECEPTIVO = 1
                  THEN ''S''
                  ELSE ''N''
                  END) AS EXP11_PRECISA_PROCURADOR
          , RPAD('' '', 100, '' '') AS EXP12_PROCEDENCIA--no obligatorio
          , RPAD('' '', 1000, '' '') AS EXP13_OBSERVACIONES--no obligatorio
          , (SELECT DECODE(COUNT(1), 0, ''  '', LPAD(COUNT(1), 2, '' ''))
                    FROM SCS_EEJG_PETICIONES P
                    WHERE P.IDINSTITUCION = EJG.IDINSTITUCION
                    AND P.ANIO = EJG.ANIO
                    AND P.NUMERO = EJG.NUMERO
                    AND P.IDTIPOEJG = EJG.IDTIPOEJG
                    AND P.ESTADO = 30)--FINALIZADO
                    AS EXP14_EEJG
          , ''##'' AS SALTO_LINEA_2
     , ''PRD'' AS TIPO_REGISTRO_PRD
          , LPAD(DECODE(DL.IDPERSONA, NULL, ''      '', NVL(F_SIGA_CALCULONCOLEGIADO(DL.IDINSTITUCION, DL.IDPERSONA), ''      '')), 6, ''0'') AS PRD1_NCOLEGIADOABOGADO
          , RPAD(NVL(TO_CHAR(DL.FECHADESIGNA, ''YYYYMMDD''), '' ''), 8, '' '') AS PRD2_FECHA_DESIGNA
          , LPAD(NVL(TO_CHAR(D.CODIGO), ''      ''), 6, ''0'') AS PRD3_NUMERO_DESIGNA
          , LPAD(NVL(TO_CHAR(D.ANIO), ''    ''), 4, ''0'') AS PRD4_ANIO_DESIGNA
          , LPAD(NVL((CASE
                  WHEN DP.IDPROCURADOR IS NOT NULL
                  THEN (SELECT P.NCOLEGIADO
                       FROM SCS_PROCURADOR P
                       WHERE P.IDINSTITUCION = DP.IDINSTITUCION_PROC
                       AND P.IDPROCURADOR = DP.IDPROCURADOR)
                   WHEN EJG.IDPROCURADOR IS NOT NULL
                   THEN (SELECT P.NCOLEGIADO
                        FROM SCS_PROCURADOR P
                        WHERE P.IDINSTITUCION = EJG.IDINSTITUCION_PROC
                        AND P.IDPROCURADOR = EJG.IDPROCURADOR)
                   ELSE NULL
                   END), ''      ''), 6, ''0'') AS PRD5_NCOLEGIADO_PROCURADOR
          , (CASE
             WHEN DP.FECHADESIGNA IS NOT NULL
             THEN TO_CHAR(DP.FECHADESIGNA, ''YYYY'')
             WHEN EJG.FECHA_DES_PROC IS NOT NULL
             THEN TO_CHAR(EJG.FECHA_DES_PROC, ''YYYY'')
             ELSE LPAD('' '' , 4, '' '') END) AS PRD6_ANIO_DESIGNA_PROCURADOR
          , (CASE
             WHEN DP.FECHADESIGNA IS NOT NULL
             THEN TO_CHAR(DP.FECHADESIGNA, ''YYYYMMDD'')
             WHEN EJG.FECHA_DES_PROC IS NOT NULL
             THEN TO_CHAR(EJG.FECHA_DES_PROC, ''YYYYMMDD'')
             ELSE LPAD('' '' , 8, '' '') END) AS PRD7_FECHA_DESIGNA_PROCURADOR
          , LPAD((CASE
             WHEN DP.NUMERODESIGNACION IS NOT NULL
             THEN DP.NUMERODESIGNACION
             WHEN EJG.NUMERODESIGNAPROC IS NOT NULL
             THEN EJG.NUMERODESIGNAPROC
             ELSE ''      '' END), 6, ''0'') AS PRD8_NUMERO_DESIGNA_PROCURADOR
          --, LPAD(NVL(TO_CHAR(DP.FECHADESIGNA, ''YYYY''), ''    ''), 4, ''0'') AS PRD6_ANIO_DESIGNA_PROCURADOR
          --, LPAD(NVL(TO_CHAR(DP.FECHADESIGNA, ''YYYYMMDD''), '' ''), 8, '' '') AS PRD7_FECHA_DESIGNA_PROCURADOR
          --, RPAD(NVL(DP.NUMERODESIGNACION, '' ''), 6, '' '') AS PRD8_NUMERO_DESIGNA_PROCURADOR
          , ''##'' AS SALTO_LINEA_3
     , ''PRJ'' AS TIPO_REGISTRO_PRJ
          , (CASE DECODE (D.IDPRETENSION, NULL, (SELECT P.IDJURISDICCION
                    FROM SCS_PRETENSION P
                    WHERE P.IDINSTITUCION = EJG.IDINSTITUCION
                    AND  P.IDPRETENSION = EJG.IDPRETENSION),
                    (SELECT P.IDJURISDICCION
                    FROM SCS_PRETENSION P
                    WHERE P.IDINSTITUCION = D.IDINSTITUCION
                    AND P.IDPRETENSION = D.IDPRETENSION))
              WHEN 1 THEN ''1''--PENAL
              WHEN 2 THEN ''2''--CIVIL
              WHEN 4 THEN ''4''--SOCIAL
              WHEN 5 THEN ''3''--CONTENCIOSO-ADMINISTRATIVO
              ELSE '' '' END) AS PRJ1_ORDEN_JURISDICCIONAL
          , LPAD((CASE DECODE (D.IDPRETENSION, NULL, (SELECT P.IDJURISDICCION
                    FROM SCS_PRETENSION P
                    WHERE P.IDINSTITUCION = EJG.IDINSTITUCION
                    AND  P.IDPRETENSION = EJG.IDPRETENSION),
                    (SELECT P.IDJURISDICCION
                    FROM SCS_PRETENSION P
                    WHERE P.IDINSTITUCION = D.IDINSTITUCION
                    AND P.IDPRETENSION = D.IDPRETENSION))
                  WHEN 1 --PENAL
                  THEN NVL((SELECT TO_CHAR(D.IDTIPODOCUMENTO)
                     FROM SCS_DOCUMENTACIONEJG D
                     WHERE D.IDINSTITUCION = EJG.IDINSTITUCION
                     AND D.IDTIPOEJG = EJG.IDTIPOEJG
                     AND D.ANIO = EJG.ANIO
                     AND D.NUMERO = EJG.NUMERO
                     AND ROWNUM <= 1
                     AND D.IDTIPODOCUMENTO IN (1, 2, 3)), ''1'')
                  WHEN 2 --CIVIL
                  THEN NVL((SELECT TO_CHAR(D.IDTIPODOCUMENTO)
                     FROM SCS_DOCUMENTACIONEJG D
                     WHERE D.IDINSTITUCION = EJG.IDINSTITUCION
                     AND D.IDTIPOEJG = EJG.IDTIPOEJG
                     AND D.ANIO = EJG.ANIO
                     AND D.NUMERO = EJG.NUMERO
                     AND ROWNUM <= 1
                     AND D.IDTIPODOCUMENTO IN (4, 5, 6, 7)), ''4'')
                  WHEN 5 --CONTENCIOSO-ADMINISTRATIVO
                  THEN NVL((SELECT TO_CHAR(D.IDTIPODOCUMENTO)
                     FROM SCS_DOCUMENTACIONEJG D
                     WHERE D.IDINSTITUCION = EJG.IDINSTITUCION
                     AND D.IDTIPOEJG = EJG.IDTIPOEJG
                     AND D.ANIO = EJG.ANIO
                     AND D.NUMERO = EJG.NUMERO
                     AND ROWNUM <= 1
                     AND D.IDTIPODOCUMENTO IN (8, 9, 10, 11, 12)), ''8'')
                  ELSE '' ''
                  END), 3, ''0'') AS PRJ2_CLASE_ASUNTO_TIPO_ORDEN
          , RPAD(NVL((CASE WHEN D.IDJUZGADO IS NOT NULL
               THEN (SELECT JUZ.CODIGOEXT
               FROM SCS_JUZGADO JUZ, CEN_POBLACIONES POB
                WHERE JUZ.IDPOBLACION = POB.IDPOBLACION
                AND JUZ.IDJUZGADO = D.IDJUZGADO
                AND JUZ.IDINSTITUCION = D.IDINSTITUCION_JUZG)
              WHEN EJG.JUZGADO IS NOT NULL
               THEN (SELECT JUZ.CODIGOEXT
                FROM SCS_JUZGADO JUZ, CEN_POBLACIONES POB
                WHERE JUZ.IDPOBLACION = POB.IDPOBLACION
                AND JUZ.IDJUZGADO = EJG.JUZGADO
                AND JUZ.IDINSTITUCION = EJG.JUZGADOIDINSTITUCION)
               ELSE '' ''
               END), '' ''), 10, '' '') AS PRJ3_ORGANO_JUDICIAL
          , RPAD((CASE WHEN D.IDPRETENSION IS NOT NULL
                  THEN (SELECT NVL(PRE.CODIGOEXT, '' '')
                               FROM SCS_PRETENSION PRE
                               WHERE PRE.IDINSTITUCION = D.IDINSTITUCION AND PRE.IDPRETENSION = D.IDPRETENSION)
                  ELSE (SELECT NVL(PRE.CODIGOEXT, '' '')
                               FROM SCS_PRETENSION PRE
                               WHERE PRE.IDINSTITUCION = EJG.IDINSTITUCION AND PRE.IDPRETENSION = EJG.IDPRETENSION)
                  END), 3, '' '') AS PRJ4_TIPO_PROCED_JUDICIAL
          , LPAD(NVL((CASE
                WHEN D.NUMPROCEDIMIENTO IS NOT NULL
                THEN (CASE INSTR(D.NUMPROCEDIMIENTO, ''/'')
                        WHEN 0 THEN NULL
                        ELSE SUBSTR(D.NUMPROCEDIMIENTO, 0, INSTR(D.NUMPROCEDIMIENTO, ''/'')-1)
                      END)
                WHEN INSTR(EJG.NUMEROPROCEDIMIENTO, ''/'') > 0
                THEN SUBSTR(EJG.NUMEROPROCEDIMIENTO, 0, INSTR(EJG.NUMEROPROCEDIMIENTO, ''/'')-1)
                ELSE NULL
               END), ''       ''), 7, ''0'') AS PRJ5_NUM_PROCEDIMIENTO
           , LPAD(NVL((CASE
                WHEN INSTR(D.NUMPROCEDIMIENTO, ''/20'') > 0
                THEN RPAD(SUBSTR(D.NUMPROCEDIMIENTO, INSTR(D.NUMPROCEDIMIENTO, ''/'')+1, 4), 4, ''0'')
                WHEN INSTR(D.NUMPROCEDIMIENTO, ''/'') > 0
                THEN RPAD(''20'' || SUBSTR(D.NUMPROCEDIMIENTO, INSTR(D.NUMPROCEDIMIENTO, ''/'')+1, 2), 4, ''0'')
                WHEN INSTR(EJG.NUMEROPROCEDIMIENTO, ''/20'') > 0
                THEN RPAD(SUBSTR(EJG.NUMEROPROCEDIMIENTO, INSTR(EJG.NUMEROPROCEDIMIENTO, ''/'')+1, 4), 4, ''0'')
                WHEN INSTR(EJG.NUMEROPROCEDIMIENTO, ''/'') > 0
                THEN RPAD(''20'' || SUBSTR(EJG.NUMEROPROCEDIMIENTO, INSTR(EJG.NUMEROPROCEDIMIENTO, ''/'')+1, 2), 4, ''0'')
                ELSE NULL
                END), ''    ''), 4, ''0'') AS PRJ6_ANIO_PROCEDIMIENTO
          , RPAD(NVL((SELECT P.CODIGO FROM PCAJG_TIPO_EJG P, PCAJG_TIPO_EJG_SCSTIPOEJG PS
                    WHERE P.IDENTIFICADOR = PS.IDENTIFICADOR
                    AND P.IDINSTITUCION = PS.IDINSTITUCION
                    AND P.IDINSTITUCION = ER.IDINSTITUCION
                    AND PS.IDTIPOEJG = EJG.IDTIPOEJG), '' ''), 3, '' '') AS PRJ7_NATURALEZA_PROCEDIMIENTO
          --, DECODE(EJG.IDTIPOENCALIDAD, NULL, ''   '', ''1'', ''001'', ''0'', ''002'') AS PRJ8_ESTADO_PROCEDIMIENTO
          , NVL((SELECT TC.CODIGO FROM SCS_TIPOENCALIDAD TC WHERE TC.IDTIPOENCALIDAD = EJG.IDTIPOENCALIDAD AND TC.IDINSTITUCION = EJG.CALIDADIDINSTITUCION), ''   '') AS PRJ8_ESTADO_PROCEDIMIENTO
          , RPAD('' '', 50, '' '') AS PRJ9_DESCRIPCION_PROCEDIMIEN --no obligatorio
          , RPAD('' '', 1, '' '') AS PRJ10_ACUSACION_PARTICULAR --no obligatorio
          , RPAD('' '', 1, '' '') AS PRJ11_PETICION_PARTICULAR --no obligatorio
          , RPAD('' '', 100, '' '') AS PRJ12_PARTE_CONTRARIA --no obligatorio
          , ''$$'' AS GRUPO_2
     , ''SOL'' AS TIPO_REGISTRO_SOL
          , RPAD(NVL((CASE --TIPOIDENTIFICACION = 50 OTROS
                WHEN SOL.IDTIPOIDENTIFICACION IS NULL AND SOL.TIPOPERSONAJG = ''F'' AND SOL.NIF IS NULL THEN ''I''
                WHEN SOL.IDTIPOIDENTIFICACION = 50 AND SOL.TIPOPERSONAJG = ''F'' AND SOL.NIF IS NOT NULL THEN ''Z''
                WHEN SOL.IDTIPOIDENTIFICACION IS NULL AND SOL.TIPOPERSONAJG = ''J'' AND SOL.NIF IS NULL THEN ''J''
                WHEN SOL.IDTIPOIDENTIFICACION = 50 AND SOL.TIPOPERSONAJG = ''J'' AND SOL.NIF IS NOT NULL THEN ''Y''
                ELSE (SELECT P.CODIGO FROM PCAJG_TIPO_IDENTIFICACION P, PCAJG_TIPO_IDENTIFICACION_CENT S
                                 WHERE P.IDENTIFICADOR = S.IDENTIFICADOR
                                  AND P.IDINSTITUCION = S.IDINSTITUCION
                                  AND S.IDINSTITUCION = R.IDINSTITUCION
                                  AND S.IDTIPOIDENTIFICACION = SOL.IDTIPOIDENTIFICACION)
                END), '' ''), 1, '' '') AS SOL1_TIPO_IDENTIFICACION
          , RPAD(NVL(SOL.NIF, '' ''), 11, '' '') AS SOL2_IDENTIFICACION_SOLICIT
          , RPAD(NVL((SELECT CODIGO FROM PCAJG_TIPO_PERSONA P, PCAJG_TIPO_PERSONA_TIPOPER S
                    WHERE P.IDENTIFICADOR = S.IDENTIFICADOR
                    AND P.IDINSTITUCION = S.IDINSTITUCION
                    AND P.IDINSTITUCION = R.IDINSTITUCION
                    AND S.IDTIPOPERSONA = SOL.TIPOPERSONAJG), '' ''), 3, ''0'') AS SOL3_TIPOPERSONA
          , RPAD(DECODE(SOL.TIPOPERSONAJG, ''F'', NVL(SOL.NOMBRE, '' ''), '' ''), 50, '' '') AS SOL4_NOMBRE_SOLICITANTE
          , RPAD(DECODE(SOL.TIPOPERSONAJG, ''F'', NVL(SOL.APELLIDO1, '' ''), '' ''), 50, '' '') AS SOL5_APELLIDO1_SOLICITANTE
          , RPAD(DECODE(SOL.TIPOPERSONAJG, ''F'', NVL(SOL.APELLIDO2, '' ''), '' ''), 50, '' '') AS SOL6_APELLIDO2_SOLICITANTE
          , '' '' AS SOL7_SEXO_SOLICITANTE --no obligatorio
          , RPAD(NVL((SELECT CODIGO FROM PCAJG_PAIS P, PCAJG_PAIS_CENPAIS S
                    WHERE P.IDENTIFICADOR = S.IDENTIFICADOR
                    AND P.IDINSTITUCION = S.IDINSTITUCION
                    AND P.IDINSTITUCION = R.IDINSTITUCION
                    AND S.IDPAIS = SOL.IDPAIS), '' ''), 3, '' '') AS SOL8_NACIONALIDAD
          , '' '' AS SOL9_SITUACION_PERSONAL --no obligatorio
          , RPAD(NVL(TO_CHAR(SOL.FECHANACIMIENTO, ''YYYYMMDD''), '' ''), 8, '' '') AS SOL10_FECHA_NACIMIENTO --no obligatorio
          , RPAD('' '', 50, '' '') AS SOL11_PROFESION --no obligatorio
          , RPAD('' '', 1, '' '') AS SOL12_REGIMEN_ECONOMICO --no obligatorio
          , RPAD(NVL(DECODE(SOL.TIPOPERSONAJG, ''J'', SOL.NOMBRE || SOL.APELLIDO1, NULL), '' ''), 100, '' '') AS SOL13_NOMBRE_ENTIDAD_JURIDICA
          , RPAD('' '', 3, '' '') AS SOL14_USO_DOMICILIO --no obligatorio
          , RPAD('' '', 1, '' '') AS SOL15_TIPO_TELEFONO --no obligatorio
          , RPAD('' '', 15, '' '') AS SOL16_NUMERO_TELEFONO --no obligatorio
          , RPAD('' '', 1, '' '') AS SOL17_PRESO --obligatorio??
          , RPAD('' '', 10, '' '') AS SOL18_CENTRO_PENITENCIARIO --obligatorio??
          , ''##'' AS SALTO_LINEA_5
     , ''DOM'' AS TIPO_REGISTRO_DOM
          , RPAD(''2'', 1, '' '') AS DOM1_TIPO_DOMICILIO
          , RPAD(NVL((SELECT CODIGO FROM PCAJG_PAIS P, PCAJG_PAIS_CENPAIS S
                    WHERE P.IDENTIFICADOR = S.IDENTIFICADOR
                    AND P.IDINSTITUCION = S.IDINSTITUCION
                    AND P.IDINSTITUCION = R.IDINSTITUCION
                    AND S.IDPAIS = PROV.IDPAIS), '' ''), 3, '' '') AS DOM2_PAIS_DOMICILIO
          , RPAD(DECODE(PROV.CODIGOEXT, NULL, ''88'', ''0'', ''88'', PROV.CODIGOEXT), 2, '' '') AS DOM3_PROVINCIA_DOMICILIO
          , RPAD(NVL(PROV.NOMBRE, '' ''), 50, '' '') AS DOM4_DESCRIP_PROVIN_DOMICI_EXT
          , LPAD(NVL(SUBSTR(POBL.CODIGOEXT, 3, 3), '' ''), 3, '' '') AS DOM5_MUNICIPIO_DOMICILIO_NORM
          , RPAD(NVL(POBL.NOMBRE, '' ''), 50, '' '') AS DOM6_DESC_MUNIC_DOM_SINNORM
          , RPAD(''P'', 1, '' '') AS DOM7_TIPO_NUMERACION --PORTAL: VISTO EN LOS EJEMPLOS
          , RPAD(NVL(SOL.ESCALERADIR, '' '') || ''/'' || NVL(SOL.PISODIR, '' '') || ''/'' || NVL(SOL.PUERTADIR, '' ''), 50, '' '') AS DOM8_PISO --no obligatorio
          , RPAD(NVL((SELECT TV.CODIGO FROM CEN_TIPOVIA TV
                                     WHERE TV.IDTIPOVIA = SOL.IDTIPOVIA
                                     AND TV.IDINSTITUCION = SOL.IDINSTITUCION), ''DESC''), 5, '' '') AS DOM9_TIPO_VIA --DESCONOCIDO
          , RPAD(NVL((SELECT F_SIGA_GETRECURSO(TV.DESCRIPCION, 1) FROM CEN_TIPOVIA TV
                                     WHERE TV.IDTIPOVIA = SOL.IDTIPOVIA
                                     AND TV.IDINSTITUCION = SOL.IDINSTITUCION), ''DESCONOCIDO''), 15, '' '') AS DOM10_DESCRIPCION_TIPO_VIA
          , RPAD(NVL(SOL.NUMERODIR, ''0''), 5, '' '') AS DOM11_NUMERO_PORTAL
          , RPAD('' '', 10, '' '') AS DOM12_DESCRIPCION_VIA_EXTRANJ
          , RPAD(DECODE(PROV.IDPAIS, 191, NVL(SOL.DIRECCION, ''SIN DESCRIPCION DE LA VIA''), '' ''), 125, '' '') AS DOM13_DESCRIPCION_VIA --HAY QUE HACER UN DECODE SI NO ES EXTRANJERO?

/*          , RPAD(DECODE(PROV.IDPAIS, 191, NVL((SELECT F_SIGA_GETRECURSO(TV.DESCRIPCION, 1) || '' '' FROM CEN_TIPOVIA TV
                                     WHERE TV.IDTIPOVIA = SOL.IDTIPOVIA AND TV.IDINSTITUCION = SOL.IDINSTITUCION) ||
                                     SOL.DIRECCION ||
                                     DECODE(SOL.NUMERODIR, NULL, '''', '' '' || SOL.NUMERODIR) ||
                                     DECODE(SOL.ESCALERADIR, NULL, '''', '' '' || SOL.ESCALERADIR) ||
                                     DECODE(SOL.PISODIR, NULL, '''', '' '' || SOL.PISODIR) ||
                                     DECODE(SOL.PUERTADIR, NULL, '''', '' '' || SOL.PUERTADIR),
                          ''SIN DESCRIPCION DE LA VIA''), '' ''), 125, '' '') AS DOM13_DESCRIPCION_VIA --HAY QUE HACER UN DECODE SI NO ES EXTRANJERO?*/
          , RPAD('' '', 100, '' '') AS DOM14_DESCRIPCION_VIA_COMPLETA --NO OBLIGATORIO
          , RPAD(NVL(SOL.CODIGOPOSTAL, '' ''), 10, '' '') AS DOM15_CODIGO_POSTAL
          , ''##'' AS SALTO_LINEA_6
     , ''ECO'' AS TIPO_REGISTRO_ECO
          , RPAD(''002'', 3, '' '') AS ECO1_CONCEPTO_INGRESO_ANUAL
          , DECODE(UF.IMPORTEINGRESOSANUALES, NULL, LPAD('' '', 8, '' ''), LPAD(TRUNC(UF.IMPORTEINGRESOSANUALES), 8, ''0'')) AS ECO2_IMPORTE_INGRESO_ANUAL
          --INGRESO ANUAL ES LA SUMA DE LA UNIDAD FAMILIAR DE LOS INGRESOS O SOLO DEL SOLICITANTE???
          , RPAD('' '', 3, '' '') AS ECO3_CONCEPTO_INGRESO_MENSUAL
          , LPAD('' '', 8, '' '') AS ECO4_IMPORTE_INGRESO_MENSUAL
          , RPAD('' '', 3, '' '') AS ECO5_CONCEPTO_OTROS_INGRESOS --no obligatorio
          , DECODE(UF.IMPORTEOTROSBIENES, NULL, LPAD('' '', 8, '' ''), LPAD(TRUNC(UF.IMPORTEOTROSBIENES), 8, ''0'')) AS ECO6_IMPORTE_OTROS_INGRESOS --no obligatorio
          , RPAD('' '', 3, '' '') AS ECO7_PROPIEDADES_INMUEBLES --no obligatorio
          , DECODE(UF.IMPORTEBIENESINMUEBLES, NULL, LPAD('' '', 8, '' ''), LPAD(TRUNC(UF.IMPORTEBIENESINMUEBLES), 8, ''0'')) AS ECO8_IMPORTE_PROP_INMUEBLES --no obligatorio
          , RPAD('' '', 3, '' '') AS ECO9_OBLIGACIONES_ECONOMICAS --no obligatorio
          , LPAD('' '', 8, '' '') AS ECO10_IMPORTE_OBLIGAC_ECONOMIC --no obligatorio
          , RPAD('' '', 1000, '' '') AS ECO11_DESCRIP_INF_ECONOMICA --no obligatorio
          , ''##'' AS SALTO_LINEA_7
     , ''DOC'' AS TIPO_REGISTRO_DOC
          , '''' AS DOC1_DOCUMENTACION_APORTADA --no obligatorio

          FROM CAJG_REMESA R, CAJG_EJGREMESA ER, SCS_EJG EJG, SCS_EJGDESIGNA ED
               , SCS_DESIGNASLETRADO DL, SCS_PERSONAJG SOL
               , CEN_PROVINCIAS PROV, CEN_POBLACIONES POBL, SCS_UNIDADFAMILIAREJG UF
               , SCS_DESIGNAPROCURADOR DP, SCS_DESIGNA D

          WHERE R.IDREMESA = ER.IDREMESA
          AND R.IDINSTITUCION = ER.IDINSTITUCION
          AND ER.IDINSTITUCION = EJG.IDINSTITUCION
          AND ER.ANIO = EJG.ANIO
          AND ER.IDTIPOEJG = EJG.IDTIPOEJG
          AND ER.NUMERO = EJG.NUMERO
          AND EJG.IDINSTITUCION = ED.IDINSTITUCION(+)
          AND EJG.ANIO = ED.ANIOEJG(+)
          AND EJG.NUMERO = ED.NUMEROEJG(+)
          AND EJG.IDTIPOEJG = ED.IDTIPOEJG(+)
          AND ED.IDINSTITUCION = DL.IDINSTITUCION(+)
          AND ED.IDTURNO = DL.IDTURNO(+)
          AND ED.ANIODESIGNA = DL.ANIO(+)
          AND ED.NUMERODESIGNA = DL.NUMERO(+)
          AND ED.IDINSTITUCION = D.IDINSTITUCION(+)
          AND ED.IDTURNO = D.IDTURNO(+)
          AND ED.ANIODESIGNA = D.ANIO(+)
          AND ED.NUMERODESIGNA = D.NUMERO(+)
          AND (DL.FECHADESIGNA IS NULL
              OR DL.FECHADESIGNA = (SELECT MAX(D.FECHADESIGNA)
                             from SCS_DESIGNASLETRADO D
                            where D.IDINSTITUCION = DL.IDINSTITUCION
                              and D.IDTURNO = DL.IDTURNO
                              and D.ANIO = DL.ANIO
                              and D.NUMERO = DL.NUMERO
                              and TRUNC(D.FECHADESIGNA) <= TRUNC(SYSDATE)))

          AND EJG.IDINSTITUCION = UF.IDINSTITUCION(+)
          AND EJG.NUMERO = UF.NUMERO(+)
          AND EJG.ANIO = UF.ANIO(+)
          AND EJG.IDTIPOEJG = UF.IDTIPOEJG(+)
          AND UF.IDINSTITUCION = SOL.IDINSTITUCION(+)
          AND UF.IDPERSONA = SOL.IDPERSONA(+)
          AND UF.SOLICITANTE(+) = 1

          AND SOL.IDPROVINCIA = PROV.IDPROVINCIA(+)
          AND SOL.IDPOBLACION = POBL.IDPOBLACION(+)
          --AND POBL.IDPOBLACIONMUNICIPIO(+) IS NOT NULL --20101013 debe sacar también los municipios de las poblaciones

          AND ED.IDINSTITUCION = DP.IDINSTITUCION(+)
          AND ED.IDTURNO = DP.IDTURNO(+)
          AND ED.ANIODESIGNA = DP.ANIO(+)
          AND ED.NUMERODESIGNA = DP.NUMERO(+)
          AND DP.FECHARENUNCIA IS NULL

          AND (ED.ANIODESIGNA IS NULL OR ((ED.ANIODESIGNA || LPAD(ED.NUMERODESIGNA, 10, 0)) =
              (SELECT MAX(ED2.ANIODESIGNA || LPAD(ED2.NUMERODESIGNA, 10, 0))
              FROM SCS_EJGDESIGNA ED2
              WHERE ED2.IDINSTITUCION = EJG.IDINSTITUCION
              AND ED2.ANIOEJG = EJG.ANIO
              AND ED2.NUMEROEJG = EJG.NUMERO
              AND ED2.IDTIPOEJG = EJG.IDTIPOEJG)))



          AND R.IDREMESA = ' || P_IDREMESA || ' AND R.IDINSTITUCION = ' || P_INSTITUCION || '
          ORDER BY PRD4_ANIO_DESIGNA, TO_NUMBER(TRIM(PRD3_NUMERO_DESIGNA)), EXP2_ANIO_EXPEDIENTE, EXP1_NUM_EXPEDIENTE';

  cuentaCondicion := 0;
 --ELIMINADA LA OBLIGATORIEDAD DE LOS CAMPOS EXP7 Y EXP8 SEGÚN CORREO DE CARMEN DEL 23/05/2011
-- AÑADIMOS OBLIGATORIEDAD POR CORREO DE ENRIQUE DE 21-02-2017
  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(EXP7_CALIFICACION) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el tipo de dictamen';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(EXP8_FECHA_DICTAMEN) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar la fecha del dictamen';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := '(TRIM(PRD6_ANIO_DESIGNA_PROCURADOR) IS NOT NULL AND TRIM(PRD8_NUMERO_DESIGNA_PROCURADOR) IS NOT NULL OR TRIM(PRD6_ANIO_DESIGNA_PROCURADOR) IS NULL AND TRIM(PRD8_NUMERO_DESIGNA_PROCURADOR) IS NULL)';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Revise el año y número de designación del procurador';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(PRJ1_ORDEN_JURISDICCIONAL) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el campo procedimientos (orden jurisdiccional).';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(PRJ2_CLASE_ASUNTO_TIPO_ORDEN) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Se debe rellenar la documentación requerida del EJG correspondiente al procedimiento (orden jurisdiccional).';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(PRJ3_ORGANO_JUDICIAL) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el juzgado en la designa o en el expediente.';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(PRJ7_NATURALEZA_PROCEDIMIENTO) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el campo tipo EJG';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(PRJ8_ESTADO_PROCEDIMIENTO) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el campo en calidad de demandante o demandado';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(SOL1_TIPO_IDENTIFICACION) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'El código de identificación de los solicitantes no puede estar vacío';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(SOL3_TIPOPERSONA) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el tipo de persona de los solicitantes';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(SOL8_NACIONALIDAD) IS NOT NULL';--TODO METER EN SIGA
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar la nacionalidad de los solicitantes';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(DOM2_PAIS_DOMICILIO) IS NOT NULL';--TODO METER EN SIGA
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el país del domicilio de los solicitantes';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(DOM5_MUNICIPIO_DOMICILIO_NORM) IS NOT NULL';
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el municipio del domicilio de los solicitantes';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(DOM15_CODIGO_POSTAL) IS NOT NULL';--TODO METER EN SIGA
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el código postal del domicilio de los solicitantes';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := '(TRIM(PRJ5_NUM_PROCEDIMIENTO) IS NULL OR IS_NUMBER(PRJ5_NUM_PROCEDIMIENTO) = 1)';--TODO METER EN SIGA
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el número de procedimiento correctamente';

  cuentaCondicion := cuentaCondicion + 1;
  v_arrayCondiciones(cuentaCondicion).condicion := '(TRIM(PRJ6_ANIO_PROCEDIMIENTO) IS NULL OR IS_NUMBER(PRJ6_ANIO_PROCEDIMIENTO) = 1)';--TODO METER EN SIGA
  v_arrayCondiciones(cuentaCondicion).descripcion := 'Debe rellenar el año del procedimiento correctamente';

--  cuentaCondicion := cuentaCondicion + 1;
--  v_arrayCondiciones(cuentaCondicion).condicion := 'TRIM(PRJ4_TIPO_PROCED_JUDICIAL) IS NOT NULL';
--  v_arrayCondiciones(cuentaCondicion).descripcion := 'Compruebe que ha rellenado el módulo en la designa y que ese módulo tiene un código asociado';


  --Eliminamos primero los registros de respuesta
  --No debe haber, pero si se ha borrado manualmente por pruebas...
  DELETE FROM CAJG_RESPUESTA_EJGREMESA
        WHERE IDEJGREMESA IN (SELECT IDEJGREMESA
        FROM CAJG_EJGREMESA WHERE IDINSTITUCION = P_INSTITUCION
        AND IDREMESA = P_IDREMESA);

  for i in v_arrayCondiciones.first .. v_arrayCondiciones.last loop
    --INSERTA EL ERROR
    sentencia := 'insert into cajg_respuesta_ejgremesa(idrespuesta, idejgremesa, codigo, descripcion, abreviatura, fecha, fechamodificacion, usumodificacion, idTipoRespuesta)
          SELECT SEQ_CAJG_RESPUESTA_EJGREMESA.NEXTVAL, IDEJGREMESA,
          '|| i ||', ''' || v_arrayCondiciones(i).descripcion || ''', NULL, SYSDATE, SYSDATE, 0, 1
          FROM CAJG_EJGREMESA RE
          WHERE NOT EXISTS (SELECT 1 FROM SCS_ESTADOEJG E, CAJG_EJGREMESA ER
                  WHERE ER.IDEJGREMESA = RE.IDEJGREMESA
                  AND E.IDINSTITUCION = ER.IDINSTITUCION
                  AND E.ANIO = ER.ANIO
                  AND E.NUMERO = ER.NUMERO
                  AND E.IDTIPOEJG = ER.IDTIPOEJG
				  AND E.FECHABAJA IS NULL
                  AND E.IDESTADOEJG = 17)/*Listo remitir comisión actualización designación*/
          /*AND RE.IDEJGREMESA NOT IN (SELECT IDEJGREMESA FROM CAJG_RESPUESTA_EJGREMESA)*/
          AND RE.IDEJGREMESA IN (SELECT ER.IDEJGREMESA
            FROM CAJG_EJGREMESA ER WHERE ER.IDINSTITUCION = ' || P_INSTITUCION ||
                 ' AND ER.IDREMESA = ' || P_IDREMESA || ')
            AND RE.IDEJGREMESA NOT IN (SELECT T.IDEJGREMESA FROM (SELECT ER.IDEJGREMESA, ' ||
                 SALIDA || ') T
            WHERE ' || v_arrayCondiciones(i).condicion || ')';
    EXECUTE IMMEDIATE to_char(sentencia);
    condiciones := condiciones || ' AND ' || v_arrayCondiciones(i).condicion;
  end loop;

  salida := 'SELECT T.* FROM (SELECT ' || salida || ') T WHERE 1=1' || condiciones;
  return salida;

end f_comunicaciones_ejg_2003_CAB;
/
