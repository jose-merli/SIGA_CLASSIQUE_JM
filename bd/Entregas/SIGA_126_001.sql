ALTER TABLE FCS_FACT_GUARDIASCOLEGIADO ADD (
	IDTIPOASISTENCIACOLEGIO NUMBER(3),
	IDTIPOACTUACION NUMBER(3),
	CONSTRAINT FK_ASTP_UG 
	FOREIGN KEY (IDINSTITUCION, IDTIPOASISTENCIACOLEGIO)
	REFERENCES SCS_TIPOASISTENCIACOLEGIO(IDINSTITUCION, IDTIPOASISTENCIACOLEGIO),
	CONSTRAINT FK_ACTP_UG 
	FOREIGN KEY (IDINSTITUCION, IDTIPOASISTENCIACOLEGIO, IDTIPOACTUACION)
	REFERENCES SCS_TIPOACTUACION(IDINSTITUCION, IDTIPOASISTENCIA, IDTIPOACTUACION)
);

ALTER TABLE GEN_DIASLETRA
ADD CONSTRAINT PK_GEN_DIASLETRA PRIMARY KEY (DIANUMERO);

Nuevo PKG_SIGA_FACT_SJCS_HITOS;
Modificado PKG_SIGA_FCS_HISTORICO;

-- Tratamiento Guardias inactivas
-- En el paquete antiguo se estan guardando importes de guardia inactiva para colegios NO catalanes
DELETE FCS_HISTORICO_HITOFACT
WHERE FCS_HISTORICO_HITOFACT.IDHITO IN (53, 54) -- 53:GAsMin; 54:GAcMin
AND EXISTS (
	SELECT 1        
	FROM CEN_INSTITUCION
	WHERE CEN_INSTITUCION.CEN_INST_IDINSTITUCION <> 3001 -- Consejo Catalan
		AND  FCS_HISTORICO_HITOFACT.IDINSTITUCION = CEN_INSTITUCION.IDINSTITUCION
);

----

-- Estaba indicado el hito 1:GAs, cuando deber�a ser 44:GAc
UPDATE SCS_HITOFACTURABLEGUARDIA
SET IDHITO = 44
WHERE (IDINSTITUCION, IDTURNO, IDGUARDIA, IDHITO) IN (
    SELECT DISTINCT H1.IDINSTITUCION, H1.IDTURNO, H1.IDGUARDIA, 1
    FROM SCS_HITOFACTURABLEGUARDIA H1
    WHERE (H1.DIASAPLICABLES IS NOT NULL OR H1.AGRUPAR IS NOT NULL)
    AND H1.IDHITO IN (4,46)
    AND NOT EXISTS (
        SELECT 1
        FROM SCS_HITOFACTURABLEGUARDIA H2
        WHERE H1.IDINSTITUCION = H2.IDINSTITUCION
        AND H1.IDTURNO = H2.IDTURNO
        AND H1.IDGUARDIA = H2.IDGUARDIA
        AND H2.IDHITO = 44
        AND NVL(H2.DIASAPLICABLES,-1) = NVL(H1.DIASAPLICABLES,-1)
        AND NVL(H2.AGRUPAR,-1) = NVL(H1.AGRUPAR,-1) 
    )
);

-- Estaba indicado el hito 1:GAs, cuando deber�a ser 44:GAc
UPDATE FCS_HISTORICO_HITOFACT
SET IDHITO = 44
WHERE (IDINSTITUCION, IDTURNO, IDGUARDIA, IDFACTURACION, IDHITO) IN (
    SELECT DISTINCT H1.IDINSTITUCION, H1.IDTURNO, H1.IDGUARDIA, IDFACTURACION, 1
    FROM FCS_HISTORICO_HITOFACT H1
    WHERE (H1.DIASAPLICABLES IS NOT NULL OR H1.AGRUPAR IS NOT NULL)
    AND H1.IDHITO IN (4,46)
    AND NOT EXISTS (
        SELECT 1
        FROM FCS_HISTORICO_HITOFACT H2
        WHERE H1.IDINSTITUCION = H2.IDINSTITUCION
        AND H1.IDTURNO = H2.IDTURNO
        AND H1.IDGUARDIA = H2.IDGUARDIA
        AND H2.IDHITO = 44
        AND NVL(H2.DIASAPLICABLES,-1) = NVL(H1.DIASAPLICABLES,-1)
        AND NVL(H2.AGRUPAR,-1) = NVL(H1.AGRUPAR,-1) 
    )
);

Modificado PKG_SIGA_FACTURACION_SJCS;

----
-- Tratamiento par�metro para indicar paquete de facturaci�n SJCS
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.gratuita.hitos.ayuda', 'Tipo de facturaci�n SJCS (N:Antigua; S:Hitos)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.gratuita.hitos.ayuda', 'Tipo de facturaci�n SJCS (N:Antigua; S:Hitos)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.gratuita.hitos.ayuda', 'Tipo de facturaci�n SJCS (N:Antigua; S:Hitos)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.gratuita.hitos.ayuda', 'Tipo de facturaci�n SJCS (N:Antigua; S:Hitos)#EU', 0, '3', sysdate, 0, '19');


INSERT INTO GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO) 
VALUES ('FCS', 'FACTURACION_HITOS', 'N', SYSDATE, 0, 0, 'facturacion.gratuita.hitos.ayuda');

INSERT INTO GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO)
SELECT 'FCS', 'FACTURACION_HITOS', 'S', SYSDATE, 0, IDINSTITUCION, 'facturacion.gratuita.hitos.ayuda'
FROM CEN_INSTITUCION
WHERE CEN_INST_IDINSTITUCION = 3004; -- Consejo Valenciano

----
-- Tratamiento para nuevos valores de configuraci�n
INSERT INTO SCS_HITOFACTURABLE (
    IDHITO, NOMBRE, APLICABLEA, 
    FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, 
    IDHITOCONFIGURACION) 
VALUES (
    61 /* IDHITO */,
    'FacConfig' /* NOMBRE */,
    'DIAOGUARDIA'/* APLICABLEA */,
    SYSDATE/* FECHAMODIFICACION */,
    0/* USUMODIFICACION */,
    'Obtener hitos de fact.= 0:Siempre actual; 1:Del hist�rico siempre que sea posible' /* DESCRIPCION */,
    NULL/* IDHITOCONFIGURACION */ );
	
INSERT INTO SCS_HITOFACTURABLE (
    IDHITO, NOMBRE, APLICABLEA, 
    FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, 
    IDHITOCONFIGURACION) 
VALUES (
    62 /* IDHITO */,
    'FacConfigFG' /* NOMBRE */,
    'ACTUACION'/* APLICABLEA */,
    SYSDATE/* FECHAMODIFICACION */,
    0/* USUMODIFICACION */,
    'Obtener hitos de fact. FG= 0:Siempre actual; 1:De actuaci�n moderna; 2:De actuaci�n antigua' /* DESCRIPCION */,
    NULL/* IDHITOCONFIGURACION */ );
	
Declare
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  Update Gen_Catalogos_Multiidioma
     Set Migrado = 'N'
   Where Nombretabla = 'SCS_HITOFACTURABLE'; --Marcar la tabla como No traducida
  Proc_Act_Recursos(p_Codretorno, p_Datoserror);
  Dbms_Output.Put_Line(p_Codretorno || ': ' || p_Datoserror);
End;

INSERT INTO SCS_HITOFACTURABLEGUARDIA (
   IDINSTITUCION, IDTURNO, IDGUARDIA, 
   IDHITO, PRECIOHITO, FECHAMODIFICACION, 
   USUMODIFICACION, DIASAPLICABLES, AGRUPAR) 
    SELECT DISTINCT H1.IDINSTITUCION, 
        H1.IDTURNO, 
        H1.IDGUARDIA, 
        61, -- IDHITO=FacConfig
        1, -- PRECIOHITO=Utiliza la configuracion de la facturacion antigua
        SYSDATE, -- FECHAMODIFICACION
        0, -- USUMODIFICACION
        NULL, -- DIASAPLICABLES
        NULL -- AGRUPAR
    FROM SCS_HITOFACTURABLEGUARDIA H1
    WHERE H1.IDINSTITUCION in ( -- Comprueba que es un colegio catalan
            SELECT COLEGIO.IDINSTITUCION
            FROM CEN_INSTITUCION COLEGIO
            WHERE COLEGIO.CEN_INST_IDINSTITUCION = 3001); -- Consejo Catalan
			
INSERT INTO SCS_HITOFACTURABLEGUARDIA (
   IDINSTITUCION, IDTURNO, IDGUARDIA, 
   IDHITO, PRECIOHITO, FECHAMODIFICACION, 
   USUMODIFICACION, DIASAPLICABLES, AGRUPAR) 
    SELECT DISTINCT H1.IDINSTITUCION, 
        H1.IDTURNO, 
        H1.IDGUARDIA, 
        61, -- IDHITO=FacConfig
        0, -- PRECIOHITO=Utiliza la configuracion de la facturacion actual
        SYSDATE, -- FECHAMODIFICACION
        0, -- USUMODIFICACION
        NULL, -- DIASAPLICABLES
        NULL -- AGRUPAR
    FROM SCS_HITOFACTURABLEGUARDIA H1
    WHERE H1.IDINSTITUCION in ( -- Comprueba que NO es un colegio catalan
            SELECT COLEGIO.IDINSTITUCION
            FROM CEN_INSTITUCION COLEGIO
            WHERE COLEGIO.CEN_INST_IDINSTITUCION <> 3001); -- Consejo Catalan
			
INSERT INTO SCS_HITOFACTURABLEGUARDIA (
   IDINSTITUCION, IDTURNO, IDGUARDIA, 
   IDHITO, PRECIOHITO, FECHAMODIFICACION, 
   USUMODIFICACION, DIASAPLICABLES, AGRUPAR) 
    SELECT DISTINCT H1.IDINSTITUCION, 
        H1.IDTURNO, 
        H1.IDGUARDIA, 
        62, -- IDHITO=FacConfigFG
        1, -- PRECIOHITO=Utiliza la configuracion de la facturacion de la actuacion mas moderna
        SYSDATE, -- FECHAMODIFICACION
        0, -- USUMODIFICACION
        NULL, -- DIASAPLICABLES
        NULL -- AGRUPAR
    FROM SCS_HITOFACTURABLEGUARDIA H1
    WHERE H1.IDINSTITUCION in ( -- Comprueba que es un colegio catalan
            SELECT COLEGIO.IDINSTITUCION
            FROM CEN_INSTITUCION COLEGIO
            WHERE COLEGIO.CEN_INST_IDINSTITUCION = 3001); -- Consejo Catalan

INSERT INTO SCS_HITOFACTURABLEGUARDIA (
   IDINSTITUCION, IDTURNO, IDGUARDIA, 
   IDHITO, PRECIOHITO, FECHAMODIFICACION, 
   USUMODIFICACION, DIASAPLICABLES, AGRUPAR) 
    SELECT DISTINCT H1.IDINSTITUCION, 
        H1.IDTURNO, 
        H1.IDGUARDIA, 
        62, -- IDHITO=FacConfigFG
        0, -- PRECIOHITO=Utiliza la configuracion de la facturacion actual
        SYSDATE, -- FECHAMODIFICACION
        0, -- USUMODIFICACION
        NULL, -- DIASAPLICABLES
        NULL -- AGRUPAR
    FROM SCS_HITOFACTURABLEGUARDIA H1
    WHERE H1.IDINSTITUCION in ( -- Comprueba que NO es un colegio catalan
            SELECT COLEGIO.IDINSTITUCION
            FROM CEN_INSTITUCION COLEGIO
            WHERE COLEGIO.CEN_INST_IDINSTITUCION <> 3001); -- Consejo Catalan		

----

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMinimo', 'M�nimo Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMinimo', 'M�nimo Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMinimo', 'M�nimo Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMinimo', 'M�nimo Ac#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s antigua (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s antigua (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s antigua (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s antigua (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.literal', 'Facturaci�n Actual', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.literal', 'Facturaci�n Actual#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.literal', 'Facturaci�n Actual#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.literal', 'Facturaci�n Actual#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.literal', 'Antigua', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.literal', 'Antigua#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.literal', 'Antigua#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.literal', 'Antigua#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importe', 'Importe Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importe', 'Importe Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importe', 'Importe Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importe', 'Importe Ac#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.info', 'Configuraci�n de precios de la facturaci�n donde se incluy� el d�a de la guardia la primera vez. En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.info', 'Configuraci�n de precios de la facturaci�n donde se incluy� el d�a de la guardia la primera vez. En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.info', 'Configuraci�n de precios de la facturaci�n donde se incluy� el d�a de la guardia la primera vez. En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.antigua.info', 'Configuraci�n de precios de la facturaci�n donde se incluy� el d�a de la guardia la primera vez. En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importeMaximo', 'M�ximo AcFg', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importeMaximo', 'M�ximo AcFg#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importeMaximo', 'M�ximo AcFg#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importeMaximo', 'M�ximo AcFg#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMaximo', 'M�ximo As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMaximo', 'M�ximo As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMaximo', 'M�ximo As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMaximo', 'M�ximo As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s moderna (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s moderna (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s moderna (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.info', 'Configuraci�n de precios de la fecha de actuaci�n m�s moderna (de la cabecera de guardia). En caso de que hubiera renegociaciones, se obtiene la configuraci�n de precios de la mayor#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos de fuera de guardia (ya incluidos en facturaciones anteriores)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos de fuera de guardia (ya incluidos en facturaciones anteriores)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos de fuera de guardia (ya incluidos en facturaciones anteriores)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos de fuera de guardia (ya incluidos en facturaciones anteriores)#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.importe', 'Importe Doblada As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.importe', 'Importe Doblada As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.importe', 'Importe Doblada As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.importe', 'Importe Doblada As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeInactiva', 'Importe Inactiva As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeInactiva', 'Importe Inactiva As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeInactiva', 'Importe Inactiva As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeInactiva', 'Importe Inactiva As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importe', 'Importe AcFg', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importe', 'Importe AcFg#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importe', 'Importe AcFg#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacionFueraGuardia.importe', 'Importe AcFg#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.numero', 'N�mero Doblada Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.numero', 'N�mero Doblada Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.numero', 'N�mero Doblada Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.numero', 'N�mero Doblada Ac#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.importe', 'Importe Doblada Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.importe', 'Importe Doblada Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.importe', 'Importe Doblada Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.doblada.importe', 'Importe Doblada Ac#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeInactiva', 'Importe Inactiva Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeInactiva', 'Importe Inactiva Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeInactiva', 'Importe Inactiva Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeInactiva', 'Importe Inactiva Ac#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.literal', 'Facturaci�n Moderna', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.literal', 'Facturaci�n Moderna#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.literal', 'Facturaci�n Moderna#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionModerna.literal', 'Facturaci�n Moderna#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeGuardia', 'Importe Guardia As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeGuardia', 'Importe Guardia As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeGuardia', 'Importe Guardia As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeGuardia', 'Importe Guardia As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.info', 'Configuraci�n de precios actual', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.info', 'Configuraci�n de precios actual#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.info', 'Configuraci�n de precios actual#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.info', 'Configuraci�n de precios actual#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.literal', 'Facturaci�n Antigua', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.literal', 'Facturaci�n Antigua#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.literal', 'Facturaci�n Antigua#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionAntigua.literal', 'Facturaci�n Antigua#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.info', 'Configuraci�n de precios actual', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.info', 'Configuraci�n de precios actual#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.info', 'Configuraci�n de precios actual#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.facturacionActual.info', 'Configuraci�n de precios actual#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMaximo', 'M�ximo Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMaximo', 'M�ximo Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMaximo', 'M�ximo Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeMaximo', 'M�ximo Ac#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos (ya incluidos en facturaciones anteriores)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos (ya incluidos en facturaciones anteriores)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos (ya incluidos en facturaciones anteriores)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.titulo.info', 'Configuraci�n a utilizar para asuntos antiguos (ya incluidos en facturaciones anteriores)#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.literal', 'Actual', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.literal', 'Actual#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.literal', 'Actual#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosGuardia.actual.literal', 'Actual#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMinimo', 'M�nimo As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMinimo', 'M�nimo As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMinimo', 'M�nimo As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importeMinimo', 'M�nimo As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importe', 'Importe As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importe', 'Importe As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importe', 'Importe As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.importe', 'Importe As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asuntosAntiguosFueraGuardia.titulo.literal', 'Configuraci�n Asuntos Antiguos#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.numero', 'N�mero Doblada As', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.numero', 'N�mero Doblada As#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.numero', 'N�mero Doblada As#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.asistencia.doblada.numero', 'N�mero Doblada As#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeGuardia', 'Importe Guardia Ac', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeGuardia', 'Importe Guardia Ac#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeGuardia', 'Importe Guardia Ac#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.confGuardia.actuacion.importeGuardia', 'Importe Guardia Ac#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad ps�quica v�ctima de abuso o maltrato', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad ps�quica v�ctima de abuso o maltrato#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad ps�quica v�ctima de abuso o maltrato#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad ps�quica v�ctima de abuso o maltrato#EU', 0, '3', sysdate, 0, '19');

alter table SCS_CARACTASISTENCIA add PERSONACONDISCAPACIDAD VARCHAR2(1) DEFAULT 0;


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', '�ltimo acceso', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', '�ltimo acceso#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', '�ltimo acceso#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', '�ltimo acceso#GL', 0, '4', sysdate, 0, '19');

       
       
insert into gen_procesos 
(IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION,TRANSACCION,IDPARENT,NIVEL) 
            values ('84V','GEN',1,'Y',sysdate,0,'HIDDEN_InformacionUsuario','GEN_InformacionUsuario','0',10);
            
            
alter table adm_certificados add email VARCHAR2(320);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema#EU', 0, '3', sysdate, 0, '19');