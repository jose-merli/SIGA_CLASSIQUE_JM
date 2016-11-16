CREATE OR REPLACE VIEW V_PCAJG_MARCASEXPEDIENTES AS
SELECT ER.IDINSTITUCION
       , ER.IDTIPOEJG
       , ER.ANIO
       , ER.NUMERO
       , ER.IDREMESA
       , (CASE
               WHEN M.CONTENIDO = 'Men' AND (0 = (SELECT COUNT(1)
                                  FROM SCS_DESIGNA D, SCS_PRETENSION PRE
                                  WHERE D.IDINSTITUCION = PRE.IDINSTITUCION
                                  AND D.IDPRETENSION = PRE.IDPRETENSION
                                  AND D.IDINSTITUCION = ED.IDINSTITUCION
                                  AND D.IDTURNO = ED.IDTURNO
                                  AND D.ANIO = ED.ANIODESIGNA
                                  AND D.NUMERO = ED.NUMERODESIGNA
                                  AND PRE.CODIGOEXT = '2101401000')) --Expedient (Fiscalia de Menors)
               THEN '002##Menor'
               WHEN M.CONTENIDO = 'Vdm'
               THEN '001##VIDO'
               WHEN M.CONTENIDO = 'VIDO'
               THEN '001##VIDO'
               WHEN ER.IDTIPOEJG = 5
               THEN '010##DesignacionesOrganoJudicial'
               ELSE NULL END) AS DE_ME_ME_MARCAEXPEDIENTE_CDA
        , (CASE
               WHEN M.CONTENIDO = 'Men' AND (0 = (SELECT COUNT(1)
                                  FROM SCS_DESIGNA D, SCS_PRETENSION PRE
                                  WHERE D.IDINSTITUCION = PRE.IDINSTITUCION
                                  AND D.IDPRETENSION = PRE.IDPRETENSION
                                  AND D.IDINSTITUCION = ED.IDINSTITUCION
                                  AND D.IDTURNO = ED.IDTURNO
                                  AND D.ANIO = ED.ANIODESIGNA
                                  AND D.NUMERO = ED.NUMERODESIGNA
                                  AND PRE.CODIGOEXT = '2101401000')) --Expedient (Fiscalia de Menors)
               THEN 1
               WHEN M.CONTENIDO = 'Vdm'
               THEN 1
               WHEN M.CONTENIDO = 'VIDO'
               THEN 1
               WHEN ER.IDTIPOEJG = 5
               THEN 1
               ELSE NULL END) AS DE_ME_ME_VALORMARCAEXPEDIENTE
FROM CAJG_EJGREMESA ER, SCS_EJGDESIGNA ED, SCS_TURNO T, SCS_MATERIA M
WHERE ER.IDINSTITUCION = ED.IDINSTITUCION
AND ER.ANIO = ED.ANIOEJG
AND ER.NUMERO = ED.NUMEROEJG
AND ER.IDTIPOEJG = ED.IDTIPOEJG
AND ED.IDINSTITUCION = T.IDINSTITUCION
AND ED.IDTURNO = T.IDTURNO
AND ER.IDINSTITUCION IN (SELECT I.IDINSTITUCION
                     FROM CEN_INSTITUCION I
                     WHERE I.CEN_INST_IDINSTITUCION = 3001)--CATALANES
AND T.IDINSTITUCION = M.IDINSTITUCION
AND T.IDAREA = M.IDAREA
AND T.IDMATERIA = M.IDMATERIA
AND M.CONTENIDO IN ('Men', 'Vdm', 'VIDO')
UNION
SELECT ER.IDINSTITUCION
       , ER.IDTIPOEJG
       , ER.ANIO
       , ER.NUMERO
       , ER.IDREMESA
       , (CASE
               WHEN M.CONTENIDO = 'Men' AND (0 = (SELECT COUNT(1)
                                  FROM SCS_ASISTENCIA AS2, SCS_PRETENSION PRE
                                  WHERE AS2.IDINSTITUCION = PRE.IDINSTITUCION
                                  AND AS2.IDPRETENSION = PRE.IDPRETENSION
                                  AND AS2.IDINSTITUCION = ASIS.IDINSTITUCION
                                  AND AS2.ANIO = ASIS.ANIO
                                  AND AS2.NUMERO = ASIS.NUMERO
                                  AND PRE.CODIGOEXT = '2101401000')) --Expedient (Fiscalia de Menors)
               THEN '002##Menor'
               WHEN M.CONTENIDO = 'Vdm'
               THEN '001##VIDO'
               WHEN M.CONTENIDO = 'VIDO'
               THEN '001##VIDO'
               WHEN ER.IDTIPOEJG = 5
               THEN '010##DesignacionesOrganoJudicial'
               ELSE NULL END) AS DE_ME_ME_MARCAEXPEDIENTE_CDA
        , (CASE
               WHEN M.CONTENIDO = 'Men' AND (0 = (SELECT COUNT(1)
                                  FROM SCS_ASISTENCIA AS2, SCS_PRETENSION PRE
                                  WHERE AS2.IDINSTITUCION = PRE.IDINSTITUCION
                                  AND AS2.IDPRETENSION = PRE.IDPRETENSION
                                  AND AS2.IDINSTITUCION = ASIS.IDINSTITUCION
                                  AND AS2.ANIO = ASIS.ANIO
                                  AND AS2.NUMERO = ASIS.NUMERO
                                  AND PRE.CODIGOEXT = '2101401000')) --Expedient (Fiscalia de Menors)
               THEN 1
               WHEN M.CONTENIDO = 'Vdm'
               THEN 1
               WHEN M.CONTENIDO = 'VIDO'
               THEN 1
               WHEN ER.IDTIPOEJG = 5
               THEN 1
               ELSE NULL END) AS DE_ME_ME_VALORMARCAEXPEDIENTE
FROM CAJG_EJGREMESA ER, SCS_ASISTENCIA ASIS, SCS_TURNO T, SCS_MATERIA M
WHERE ER.IDINSTITUCION = ASIS.IDINSTITUCION
AND ER.ANIO = ASIS.EJGANIO
AND ER.NUMERO = ASIS.EJGNUMERO
AND ER.IDTIPOEJG = ASIS.EJGIDTIPOEJG
AND ASIS.IDINSTITUCION = T.IDINSTITUCION
AND ASIS.IDTURNO = T.IDTURNO
AND ER.IDINSTITUCION IN (SELECT I.IDINSTITUCION
                     FROM CEN_INSTITUCION I
                     WHERE I.CEN_INST_IDINSTITUCION = 3001)--CATALANES
AND T.IDINSTITUCION = M.IDINSTITUCION
AND T.IDAREA = M.IDAREA
AND T.IDMATERIA = M.IDMATERIA
AND M.CONTENIDO IN ('Men', 'Vdm', 'VIDO')
AND NOT EXISTS (SELECT 1 FROM SCS_EJGDESIGNA ED WHERE ER.IDINSTITUCION = ED.IDINSTITUCION
AND ER.ANIO = ED.ANIOEJG
AND ER.NUMERO = ED.NUMEROEJG
AND ER.IDTIPOEJG = ED.IDTIPOEJG)
UNION
SELECT ER.IDINSTITUCION
       , ER.IDTIPOEJG
       , ER.ANIO
       , ER.NUMERO
       , ER.IDREMESA
       , '003##Incapacidad' AS DE_ME_ME_MARCAEXPEDIENTE_CDA
       , 1 AS DE_ME_ME_VALORMARCAEXPEDIENTE
FROM CAJG_EJGREMESA ER, SCS_EJG EJG, SCS_UNIDADFAMILIAREJG UF
WHERE ER.IDINSTITUCION = EJG.IDINSTITUCION
AND ER.ANIO = EJG.ANIO
AND ER.NUMERO = EJG.NUMERO
AND ER.IDTIPOEJG = EJG.IDTIPOEJG
AND EJG.IDINSTITUCION = UF.IDINSTITUCION
AND EJG.ANIO = UF.ANIO
AND EJG.IDTIPOEJG = UF.IDTIPOEJG
AND EJG.NUMERO = UF.NUMERO
AND EJG.IDPERSONAJG = UF.IDPERSONA
AND ER.IDINSTITUCION IN (SELECT I.IDINSTITUCION
                     FROM CEN_INSTITUCION I
                     WHERE I.CEN_INST_IDINSTITUCION = 3001)--CATALANES
AND UF.INCAPACITADO = 1
UNION
SELECT ER.IDINSTITUCION
       , ER.IDTIPOEJG
       , ER.ANIO
       , ER.NUMERO
       , ER.IDREMESA
       , '011##Circumst�ncies excepcionals' AS DE_ME_ME_MARCAEXPEDIENTE_CDA
       , 1 AS DE_ME_ME_VALORMARCAEXPEDIENTE
FROM CAJG_EJGREMESA ER, SCS_EJG EJG, SCS_UNIDADFAMILIAREJG UF
WHERE ER.IDINSTITUCION = EJG.IDINSTITUCION
AND ER.ANIO = EJG.ANIO
AND ER.NUMERO = EJG.NUMERO
AND ER.IDTIPOEJG = EJG.IDTIPOEJG
AND EJG.IDINSTITUCION = UF.IDINSTITUCION
AND EJG.ANIO = UF.ANIO
AND EJG.IDTIPOEJG = UF.IDTIPOEJG
AND EJG.NUMERO = UF.NUMERO
AND EJG.IDPERSONAJG = UF.IDPERSONA
AND ER.IDINSTITUCION IN (SELECT I.IDINSTITUCION
                     FROM CEN_INSTITUCION I
                     WHERE I.CEN_INST_IDINSTITUCION = 3001)--CATALANES
AND UF.CIRCUNSTANCIAS_EXCEPCIONALES = 1