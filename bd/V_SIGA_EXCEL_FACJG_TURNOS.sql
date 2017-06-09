CREATE OR REPLACE VIEW V_SIGA_EXCEL_FACJG_TURNOS AS
(
       SELECT Per.Apellidos1 ||
                Decode(Per.Apellidos2, NULL, '', ' ' || Per.Apellidos2) || ', ' ||
                REPLACE(Per.Nombre, chr(9), ' ') AS Letrado,
                REPLACE(Tur.Abreviatura, chr(9), ' ') AS Turno,
                To_Char(Des.Fechaentrada, 'dd/mm/yyyy') AS FECHA1,
                To_Char(Act.Fecha, 'dd/mm/yyyy') AS Fechaactuacion,
                To_Char(Act.Fechajustificacion, 'dd/mm/yyyy') AS FECHAJUSTIFICACION,
                Des.Anio || '/' || Lpad(Des.Codigo, 6, '0') AS Numdesigna,
                Act.Numeroasunto AS N_ACTUACION,
                REPLACE((SELECT Pro.Codigo
                          FROM Scs_Procedimientos Pro
                         WHERE Pro.Idinstitucion = Act.Idinstitucion
                           AND Pro.Idprocedimiento = Act.Idprocedimiento),
                        chr(9),
                        ' ') AS CODIGO_PROCEDIMIENTO,
                REPLACE(Fact.Procedimiento, chr(9), ' ') AS Procedimiento,
                Fact.Precioaplicado AS IMPORTE_PROCEDIMIENTO,
                REPLACE(Fact.Porcentajefacturado || '% - ' ||
                        Fact.Acreditacion,
                        chr(9),
                        ' ') AS Acreditacion,
                Fact.Importefacturado AS Importe,
                REPLACE(Nvl(Des.Numprocedimiento, Des.Resumenasunto),
                        chr(9),
                        ' ') AS N_PROCEDIMIENTO,
                REPLACE(f_Siga_Litigante(Fact.Idturno,
                                         Fact.Idinstitucion,
                                         Fact.Anio,
                                         Fact.Numero),
                        chr(9),
                        ' ') AS Litigante,
                REPLACE((SELECT Juz.Nombre
                          FROM Scs_Juzgado Juz
                         WHERE Juz.Idinstitucion = Act.Idinstitucion
                           AND Juz.Idjuzgado = Act.Idjuzgado),
                        chr(9),
                        ' ') AS Juzgado,
                (SELECT Pob.Nombre
                   FROM Scs_Juzgado Juz, Cen_Poblaciones Pob
                  WHERE Juz.Idinstitucion = Act.Idinstitucion
                    AND Juz.Idjuzgado = Act.Idjuzgado
                    AND Juz.Idpoblacion = Pob.Idpoblacion) AS LOCALIDAD,
                REPLACE((SELECT Tur.Nombre
                          FROM Scs_Asistencia Asi, Scs_Turno Tur
                         WHERE Asi.Idinstitucion = Tur.Idinstitucion
                           AND Asi.Idturno = Tur.Idturno
                           AND Asi.Idinstitucion = Fact.Idinstitucion
                           AND Asi.Designa_Turno = Fact.Idturno
                           AND Asi.Designa_Anio = Fact.Anio
                           AND Asi.Designa_Numero = Fact.Numero
                           AND Rownum = 1),
                        chr(9),
                        ' ') AS TURNO_ASISTENCIA,
                REPLACE((SELECT Gua.Nombre
                          FROM Scs_Asistencia Asi, Scs_Guardiasturno Gua
                         WHERE Asi.Idinstitucion = Gua.Idinstitucion
                           AND Asi.Idturno = Gua.Idturno
                           AND Asi.Idguardia = Gua.Idguardia
                           AND Asi.Idinstitucion = Fact.Idinstitucion
                           AND Asi.Designa_Turno = Fact.Idturno
                           AND Asi.Designa_Anio = Fact.Anio
                           AND Asi.Designa_Numero = Fact.Numero
                           AND Rownum = 1),
                        chr(9),
                        ' ') AS GUARDIA_ASISTENCIA,
                (SELECT Asi.Anio || '/' || Lpad(Asi.Numero, 6, '0')
                   FROM Scs_Asistencia Asi
                  WHERE Asi.Idinstitucion = Fact.Idinstitucion
                    AND Asi.Designa_Turno = Fact.Idturno
                    AND Asi.Designa_Anio = Fact.Anio
                    AND Asi.Designa_Numero = Fact.Numero
                    AND Rownum = 1) AS N_ASISTENCIA,
                REPLACE((SELECT Ejg.Anio || '/' || Lpad(Ejg.Numejg, 5, '0')
                          FROM Scs_Ejg Ejg, Scs_Ejgdesigna Ejgdes
                         WHERE Ejg.Idinstitucion = Ejgdes.Idinstitucion
                           AND Ejg.Idtipoejg = Ejgdes.Idtipoejg
                           AND Ejg.Anio = Ejgdes.Anioejg
                           AND Ejg.Numero = Ejgdes.Numeroejg
                           AND Ejgdes.Idinstitucion = Fact.Idinstitucion
                           AND Ejgdes.Idturno = Fact.Idturno
                           AND Ejgdes.Aniodesigna = Fact.Anio
                           AND Ejgdes.Numerodesigna = Fact.Numero
                           AND Rownum = 1),
                        chr(9),
                        ' ') AS N_EXPEDIENTE,
                           NULL N_EXPEDIENTE_CAJG,
            --Campos para agrupar
            Fact.idinstitucion idinstitucion,
            Fact.idfacturacion idfacturacion,
            Fact.idpersona     idpersona,
            Fact.Idturno       idTurno,
            Fact.Anio          anio,
            Fact.Numero        numeroFact,
            NULL               IMPORTE_TOTAL
          FROM Fcs_Fact_Actuaciondesigna Fact,
                Scs_Actuaciondesigna      Act,
                Scs_Designa               Des,
                Cen_Colegiado             Col,
                Cen_Persona               Per,
                Scs_Turno                 Tur
         WHERE Fact.Idinstitucion = Act.Idinstitucion
           AND Fact.Idturno = Act.Idturno
           AND Fact.Anio = Act.Anio
           AND Fact.Numero = Act.Numero
           AND Fact.Numeroasunto = Act.Numeroasunto
           AND Fact.Idinstitucion = Des.Idinstitucion
           AND Fact.Idturno = Des.Idturno
           AND Fact.Anio = Des.Anio
           AND Fact.Numero = Des.Numero
           AND Fact.Idinstitucion = Col.Idinstitucion
           AND Fact.Idpersona = Col.Idpersona
           AND Fact.Idpersona = Per.Idpersona
           AND Fact.Idinstitucion = Tur.Idinstitucion
           AND Fact.Idturno = Tur.Idturno
        UNION ALL
        SELECT Per.Apellidos1 ||
               Decode(Per.Apellidos2, NULL, '', ' ' || Per.Apellidos2) || ', ' ||
               REPLACE(Per.Nombre, chr(9), ' ') AS Letrado,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
           --Campos para agrupar
           Fact.idinstitucion idinstitucion,
           Fact.idfacturacion idfacturacion,
           Fact.idpersona idpersona,
           NULL,
           NULL,
           NULL,
           SUM(Fact.Importefacturado) IMPORTE_TOTAL
          FROM Fcs_Fact_Actuaciondesigna Fact, Cen_Persona Per
         WHERE Fact.Idpersona = per.idpersona
         GROUP BY Fact.idinstitucion,
                  Fact.idfacturacion,
                  Fact.idpersona,
                  Per.Apellidos1 ||
                  Decode(Per.Apellidos2, NULL, '', ' ' || Per.Apellidos2) || ', ' ||
                  REPLACE(Per.Nombre, chr(9), ' ')
)
