create or replace view v_siga_cat_justificacion as
Select IdInstitucion,
       IdInstitucion || Trim(AnyoExpediente) || SubStr(Trimestre, 1, 1) || Trim(To_Char(RowNum, '00000')) CodActuacion,
       ColegioExpediente,
       NumExpediente,
       AnyoExpediente,
       CodExpediente,
       Trimestre,
       ICAColegiado,
       NumColegiado,
       NombreAbogado,
       PrimerApellidoAbogado,
       SegundoApellidoAbogado,
       AsistenciaDefensa,
       Turno,
       AreaPartitJudicial,
       TipoIniciacion,
       NumDesignacionAbogado,
       FechaDesignacionAbogado,
       CodTipoIdentificacion,
       Identificacion,
       Nombre,
       PrimerApellido,
       SegundoApellido,
       Sexo,
       FechaActuacion,
       FechaJustificacionActuacion,
       Festivo,
       FueraPlazo,
       CausaNuevaDesigna,
       TipoDelito,
       TipoProcedimiento,
       CentroDetencionOrganJudicial,
       Modulo,
       Importe,
       CantidadAsistencias
 From (Select Fac.IdInstitucion, 
              Fac.IdInstitucion ColegioExpediente,
              '1' -- Tipo: Actuaciones de oficio
                 || Trim(To_Char(Fad.IdTurno, '0000')) -- Turno
                 || Trim(To_Char(Des.Codigo, '00000')) -- Número designación
                 || Trim(To_Char(Fad.NumeroAsunto, '00')) NumExpediente, 
              To_Char(Fad.Anio, '0000') AnyoExpediente,
              Decode(Ejg.Anio, Null, Null, Ins.IdComision -- Código comisión
                                              || '-'
                                              || SubStr(Ejg.NumEJG, 5, 5)
                                              || '/'
                                              || SubStr(Ejg.NumEJG, 1, 4)) CodExpediente,
              To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre, -- Trimestre
              Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColegiado, -- Institución
              Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
              Per.Nombre NombreAbogado,
              Per.Apellidos1 PrimerApellidoAbogado,
              Per.Apellidos2 SegundoApellidoAbogado,
              'D' AsistenciaDefensa,
              Decode(Pro.IdJurisdiccion,
                 1, 'PEN',
                 2, 'CIV',
                 4, 'SOC',
                 5, 'ADM',
                 7, 'CAS',
                 8, 'GEN',
                    'err') Turno,
              Upper(SubStr(Suz.Nombre, 1, 3)) AreaPartitJudicial,
              Decode(Nvl(Asi.Comisaria, 0), 0, 'O', 'A') TipoIniciacion,
              'O/0' 
                 || Des.Codigo
                 || '/'
                 || SubStr(Des.Anio, 3, 2)
                 || '/'
                 || Fac.IdInstitucion NumDesignacionAbogado,
              (Select Max(To_Char(FechaDesigna, 'YYYY-MM-DD'))
                 From Scs_DesignasLetrado
                Where IdInstitucion = Fad.IdInstitucion
                  And IdTurno       = Fad.IdTurno
                  And Anio          = Fad.Anio
                  And Numero        = Fad.Numero
                  And IdPersona     = Fad.IdPersona) FechaDesignacionAbogado,
              Decode(Pjg.IdTipoIdentificacion,
                 10, 'NIF',
                 20, 'CIF',
                 30, 'PAS',
                 40, 'NIE',
                 50, 'NID') CodTipoIdentificacion,
              Pjg.NIF Identificacion,
              Pjg.Nombre Nombre,
              Pjg.Apellido1 PrimerApellido,
              Pjg.Apellido2 SegundoApellido,
              Decode(Pjg.Sexo, 'H', 'H', 'M', 'M', Null) Sexo,
              To_Char(Fad.FechaActuacion, 'YYYY-MM-DD') FechaActuacion,
              To_Char(Fad.FechaJustificacion, 'YYYY-MM-DD') FechaJustificacionActuacion,
              Case When Cal.Fecha Is Null
                   Then Case To_Char(Fad.FechaActuacion, 'D')
                             When '6' Then 'DS'
                             When '7' Then 'DG'
                                      Else 'N'
                         End
                   Else Case When Cal.IdPartido Is Null
                             Then 'F'
                             Else Case When Cal.IdPartido In(Select IdPartido
                                                               From Scs_SubZonaPartido
                                                              Where IdInstitucion = Suz.IdInstitucion
                                                                And IdZona        = Suz.IdZona
                                                                And IdSubZona     = Suz.IdSubZona)
                                       Then 'F'
                                       Else 'N'
                                   End
                         End
                End Festivo,
              'N' FueraPlazo,
              Case When Fac.IdInstitucion In(Select IdInstitucion
                                               From Fcs_Fact_ActuacionDesigna
                                              Where IdInstitucion  = Fad.IdInstitucion
                                                And IdFacturacion <= Fad.IdFacturacion
                                                And IdTurno        = Fad.IdTurno
                                                And Anio           = Fad.Anio
                                                And Numero         = Fad.Numero
                                                And IdPersona     <> Fad.IdPersona)
                   Then 'NA'
                   Else Null
               End CausaNuevaDesigna,
              Del.CodigoExt TipoDelito,
              Pre.CodigoExt TipoProcedimiento,
              Decode(Juz.CodigoExt2, Null, Null, 'O' || Juz.CodigoExt2) CentroDetencionOrganJudicial,
              Decode(Fad.PorcentajeFacturado, 75, '22', Pro.CodigoExt) Modulo,
              To_char(Fad.ImporteFacturado, '9999990D00') Importe,
              0 CantidadAsistencias
         From Fcs_FacturacionJG Fac
              Inner Join Cen_Institucion Ins
                 On Ins.IdInstitucion = Fac.IdInstitucion
              Inner Join Fcs_Fact_ActuacionDesigna Fad
                 On Fad.IdInstitucion = Fac.IdInstitucion
                And Fad.IdFacturacion = Fac.IdFacturacion
                    Inner Join Scs_Designa Des
                       On Des.IdInstitucion = Fad.IdInstitucion
                      And Des.IdTurno       = Fad.IdTurno
                      And Des.Anio          = Fad.Anio
                      And Des.Numero        = Fad.Numero
                     Left Outer Join (Select Ejd.IdInstitucion,
                                             Ejd.IdTurno,
                                             Ejd.AnioDesigna Anio,
                                             Ejd.NumeroDesigna Numero,
                                             Min(To_Char(Ejg.Anio) || Trim(To_Char(Ejg.NumEJG, '00000'))) NumEJG
                                        From Scs_EJGDesigna Ejd
                                             Inner Join Scs_Ejg Ejg
                                                On Ejg.IdInstitucion = Ejd.IdInstitucion
                                               And Ejg.IdTipoEJG     = Ejd.IdTipoEJG
                                               And Ejg.Anio          = Ejd.AnioEJG
                                               And Ejg.Numero        = Ejd.NumeroEJG
                                       Group By Ejd.IdInstitucion,
                                                Ejd.IdTurno,
                                                Ejd.AnioDesigna,
                                                Ejd.NumeroDesigna) Ejg 
                       On Ejg.IdInstitucion = Fad.IdInstitucion
                      And Ejg.IdTurno       = Fad.IdTurno
                      And Ejg.Anio          = Fad.Anio
                      And Ejg.Numero        = Fad.Numero
                    Inner Join Cen_Colegiado Col
                       On Col.IdInstitucion = Fad.IdInstitucion
                      And Col.IdPersona     = Fad.IdPersona
                    Inner Join Cen_Persona Per
                       On Per.IdPersona = Fad.IdPersona
                    Inner Join Scs_Procedimientos Pro
                       On Pro.IdInstitucion   = Fad.IdInstitucion
                      And Pro.IdProcedimiento = Fad.CodigoProcedimiento 
                    Inner Join Scs_Turno Tur
                       On Tur.IdInstitucion = Fad.IdInstitucion
                      And Tur.IdTurno       = Fad.IdTurno
                          Inner Join Scs_SubZona Suz
                             On Suz.IdInstitucion = Tur.IdInstitucion
                            And Suz.IdZona        = Tur.IdZona
                            And Suz.IdSubZona     = Tur.IdSubZona
                     Left Outer Join (Select IdInstitucion,
                                             Designa_Turno,
                                             Designa_Anio,
                                             Designa_Numero,
                                             Max(Comisaria) Comisaria
                                        From Scs_Asistencia
                                       Group By IdInstitucion,
                                                Designa_Turno,
                                                Designa_Anio,
                                                Designa_Numero) Asi
                       On Asi.IdInstitucion  = Fad.IdInstitucion
                      And Asi.Designa_Turno  = Fad.IdTurno
                      And Asi.Designa_Anio   = Fad.Anio
                      And Asi.Designa_Numero = Fad.Numero
                     Left Outer Join (Select IdInstitucion,
                                             IdTurno,
                                             Anio,
                                             Numero,
                                             Min(IdPersona) IdPersona
                                        From Scs_DefendidosDesigna
                                       Group By IdInstitucion,
                                                IdTurno,
                                                Anio,
                                                Numero) Def
                       On Def.IdInstitucion = Fad.IdInstitucion
                      And Def.IdTurno       = Fad.IdTurno
                      And Def.Anio          = Fad.Anio
                      And Def.Numero        = Fad.Numero
                          Left Outer Join Scs_PersonaJG Pjg
                            On Pjg.IdInstitucion = Def.IdInstitucion
                           And Pjg.IdPersona     = Def.IdPersona
                     Left Outer Join (Select IdInstitucion,
                                             Fecha,
                                             Max(IdPartido) IdPartido
                                        From Scs_CalendarioLaboral
                                       Group By IdInstitucion,
                                                Fecha) Cal
                             On Cal.IdInstitucion = Fad.IdInstitucion
                            And Cal.Fecha         = Fad.FechaActuacion
                    Inner Join Scs_Procedimientos Pro
                       On Pro.IdInstitucion   = Fad.IdInstitucion
                      And Pro.IdProcedimiento = Fad.CodigoProcedimiento
                    Inner Join Scs_ActuacionDesigna Act
                       On Act.IdInstitucion = Fad.IdInstitucion
                      And Act.IdTurno       = Fad.IdTurno
                      And Act.Anio          = Fad.Anio
                      And Act.Numero        = Fad.Numero
                      And Act.NumeroAsunto  = Fad.NumeroAsunto
                           Left Outer Join Scs_Pretension Pre
                             On Pre.Idinstitucion = Act.IdInstitucion
                            And Pre.IdPretension  = Act.IdPretension
                           Left Outer Join Scs_Juzgado Juz
                             On Juz.IdInstitucion = Act.IdInstitucion_Juzg
                            And Juz.IdJuzgado     = Act.IdJuzgado
                     Left Outer Join (Select IdInstitucion,
                                             IdTurno,
                                             Anio,
                                             Numero,
                                             Min(IdDelito) IdDelito
                                        From Scs_DelitosDesigna
                                       Group By IdInstitucion,
                                                IdTurno,
                                                Anio,
                                                Numero) Mdd
                       On Mdd.IdInstitucion = Fad.IdInstitucion
                      And Mdd.IdTurno       = Fad.IdTurno
                      And Mdd.Anio          = Fad.Anio
                      And Mdd.Numero        = Fad.Numero
                          Left Outer Join Scs_Delito Del
                            On Del.IdInstitucion = Mdd.IdInstitucion
                           And Del.IdDelito      = Mdd.IdDelito
        Where (Fac.IdInstitucion,
               Fac.IdFacturacion) In(Select IdInstitucion,
                                            IdFacturacion
                                       From Fcs_Fact_GrupoFact_Hito
                                      Where IdGrupoFacturacion = 1)
    Union All
       Select Fac.IdInstitucion, 
              Faa.IdInstitucion ColegioExpediente,
              Decode(Faa.IdInstitucion,
                 Null, Null,
                       '2' -- Tipo: Actuaciones de Asistencia
                          || Trim(To_Char(Faa.IdTipo, '0000')) -- Tipo actuación
                          || Trim(To_Char(Faa.Numero, '00000')) -- Numero asistencia
                          || Trim(To_Char(Faa.IdActuacion, '00'))) NumExpediente, -- Numero actuacion
              Decode(Faa.IdInstitucion,
                 Null, Null,
                       To_Char(Fac.FechaDesde, 'YYYY')) AnyoExpediente,
              Decode(Faa.IdInstitucion,
                 Null, Null,
                       Ins.IdComision -- Código comisión
                          || '-'
                          || Trim(To_Char(Ejg.NumEJG, '00000'))
                          || '/'
                          || Ejg.Anio) CodExpediente,
              To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre, -- Trimestre
              Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColegiado, -- Institución
              Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
              Per.Nombre NombreAbogado,
              Per.Apellidos1 PrimerApellidoAbogado,
              Per.Apellidos2 SegundoApellidoAbogado,
              Decode(Faa.IdInstitucion, Null, Null, 'A') AsistenciaDefensa,
              Decode(Hfg.IdTipoGuardia,
                 1, 'VID',
                 5, 'MEN',
                 6, 'EST',
                    'GEN') Turno,
              Upper(SubStr(Suz.Nombre, 1, 3)) AreaPartitJudicial,
              Decode(Faa.IdInstitucion, Null, Null, Decode(Asi.Comisaria, Null, 'O', 'A')) TipoIniciacion,
              'A/'
                 || Trim(To_Char(Nvl(Faa.Numero, 999999), '000000'))
                 || '/'
                 || SubStr(Nvl(Faa.Anio, To_Char(Fac.FechaDesde, 'YYYY')), 3, 2)
                 || '/'
                 || Fac.IdInstitucion NumDesignacionAbogado,
              To_Char(Apu.FechaInicio, 'YYYY-MM-DD') FechaDesignacionAbogado,
              Decode(Pjg.IdTipoIdentificacion,
                 10, 'NIF',
                 20, 'CIF',
                 30, 'PAS',
                 40, 'NIE',
                 50, 'NID') CodTipoIdentificacion,
              Pjg.NIF Identificacion,
              Pjg.Nombre Nombre,
              Pjg.Apellido1 PrimerApellido,
              Pjg.Apellido2 SegundoApellido,
              Decode(Pjg.Sexo, 'H', 'H', 'M', 'M', Null) Sexo,
              To_Char(Nvl(Faa.FechaActuacion, Apu.FechaInicio), 'YYYY-MM-DD') FechaActuacion,
              To_Char(Nvl(Faa.FechaJustificacion, Apu.FechaInicio), 'YYYY-MM-DD') FechaJustificacionActuacion,
              Case When Cal.Fecha Is Null
                   Then Case To_Char(Apu.FechaInicio, 'D')
                             When '6' Then 'DS'
                             When '7' Then 'DG'
                                      Else 'N'
                         End
                   Else Case When Cal.IdPartido Is Null
                             Then 'F'
                             Else Case When Cal.IdPartido In(Select IdPartido
                                                               From Scs_SubZonaPartido
                                                              Where IdInstitucion = Suz.IdInstitucion
                                                                And IdZona        = Suz.IdZona
                                                                And IdSubZona     = Suz.IdSubZona)
                                       Then 'F'
                                       Else 'N'
                                   End
                         End
                End Festivo,
              'N' FueraPlazo,
              Null CausaNuevaDesigna,
              Del.CodigoExt TipoDelito,
              Null TipoProcedimiento,
              Decode(Act.IdComisaria, Null, Decode(Juz.CodigoExt2, Null, Null, 'O' || Juz.CodigoExt2), 'C' || Com.CodigoExt) CentroDetencionOrganJudicial,
              Case Apu.PrecioAplicado
                   When To_Number(SubStr(Hfg.Hitos, InStr(Hfg.Hitos, '01-') + 3, InStr(Hfg.Hitos, ' ;') - 4))
                        Then Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                             Then Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then '31a'
                                                 Else '31'
                                             End
                                       Else '40b'
                                   End
                             Else Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then 'D31a'
                                                 Else 'S31'
                                             End
                                       Else 'S40b'
                                   End
                         End
                   When To_Number(SubStr(Hfg.Hitos, InStr(Hfg.Hitos, '04-') + 3, InStr(Hfg.Hitos, ' ;', 1, 2) - 16))
                        Then Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                             Then Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then '32a'
                                                 Else '32'
                                             End
                                       Else '40c'
                                   End
                             Else Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then 'D32a'
                                                 Else 'D32'
                                             End
                                       Else 'S40b'
                                   End
                         End
                   Else Case When Nvl(Tot.Importe ,0) = 0
                             Then Case When Nvl(Tot.Cantidad, 0) = 0
                                       Then Case When Hfg.IdTipoGuardia <> 1
                                                 Then 'Guardia sin asistencias'
                                                 Else '40d'
                                             End
                                       Else Case When Cantidad > SubStr(Hfg.Hitos, InStr(Hfg.Hitos, '46') + 3, 1)
                                                 Then Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                                           Then Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Dob 1'
                                                                     Else '40c'
                                                                 End
                                                           Else Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Dob 2'
                                                                     Else 'D40c'
                                                                 End
                                                       End
                                                 Else Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                                           Then Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Sim 1'
                                                                     Else '40b'
                                                                 End
                                                           Else Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Sim 2'
                                                                     Else 'S40b'
                                                                 End
                                                       End
                                             End
                                   End
                             Else Case When Apu.PrecioAplicado = Tot.Importe
                                       Then Case When Hfg.IdTipoGuardia <> 1
                                                 Then Case When Apu.IdHito = 9
                                                           Then Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then '30cb'
                                                                     Else '30b'
                                                                 End
                                                           Else Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then '30c'
                                                                     Else '30'
                                                                 End
                                                       End
                                                 Else '40a'
                                             End
                                       Else Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                                 Then Case When Hfg.IdTipoGuardia <> 1
                                                           Then Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then '32ca'
                                                                     Else '32a'
                                                                 End
                                                           Else '40c'
                                                       End
                                                 Else Case When Hfg.IdTipoGuardia <> 1
                                                           Then Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then 'D32ca'
                                                                     Else 'D32a'
                                                                 End
                                                           Else 'D40c'
                                                       End
                                             End
                                   End
                         End
               End Modulo,
              To_Char(Case When Apu.PrecioAplicado = Tot.Importe
                           Then Tot.Importe / Tot.Cantidad
                           Else Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                     Then Apu.PrecioAplicado
                                     Else 0
                                 End
                       End, '9999990D00') Importe,
              0 CantidadAsistencias
         From Fcs_FacturacionJG Fac
              Inner Join Cen_Institucion Ins
                 On Ins.IdInstitucion = Fac.IdInstitucion
              Inner Join Fcs_Fact_Apunte Apu
                 On Apu.IdInstitucion = Fac.IdInstitucion
                And Apu.IdFacturacion = Fac.IdFacturacion
                    Inner Join Cen_Colegiado Col
                       On Col.IdInstitucion = Apu.IdInstitucion
                      And Col.IdPersona     = Apu.IdPersona
                    Inner Join Cen_Persona Per
                       On Per.IdPersona = Apu.IdPersona
                    Inner Join Scs_Turno Tur
                       On Tur.IdInstitucion = Apu.IdInstitucion
                      And Tur.IdTurno       = Apu.IdTurno
                          Inner Join Scs_SubZona Suz
                             On Suz.IdInstitucion = Tur.IdInstitucion
                            And Suz.IdZona        = Tur.IdZona
                            And Suz.IdSubZona     = Tur.IdSubZona
                    Inner Join (Select Gua.IdInstitucion,
                                       Gua.IdTurno,
                                       Gua.IdGuardia,
                                       Gua.Nombre,
                                       Gua.IdTipoGuardia,
                                       ListAgg(Trim(To_Char(Hit.Idhito, '00')) || '-' || Hit.PrecioHito, ' ; ') Within Group (Order By Hit.IdHito) Hitos
                                  From Scs_GuardiasTurno Gua
                                       Inner Join Scs_HitoFacturableGuardia Hit
                                          On Hit.IdInstitucion = Gua.IdInstitucion
                                         And Hit.IdTurno       = Gua.IdTurno
                                         And Hit.IdGuardia     = Gua.IdGuardia
                                 Where Hit.IdHito In(1, 2, 4, 7, 8, 9, 45, 46)
                                 Group By Gua.IdInstitucion,
                                          Gua.IdTurno,
                                          Gua.IdGuardia,
                                          Gua.Nombre,
                                          Gua.IdTipoGuardia) Hfg
                       On Hfg.IdInstitucion = Apu.IdInstitucion
                      And Hfg.IdTurno       = Apu.IdTurno
                      And Hfg.IdGuardia     = Apu.IdGuardia
                     Left Outer Join (Select IdInstitucion,
                                             Fecha,
                                             Max(IdPartido) IdPartido
                                        From Scs_CalendarioLaboral
                                       Group By IdInstitucion,
                                                Fecha) Cal
                             On Cal.IdInstitucion = Apu.IdInstitucion
                            And Cal.Fecha         = Apu.FechaInicio
                     Left Outer Join (Select IdInstitucion,
                                             IdFacturacion,
                                             IdApunte,
                                             Count(*) Cantidad,
                                             Sum(PrecioAplicado) Importe,
                                             Min(Anio * 10000000 + Numero * 100 + IdActuacion) Primera
                                        From Fcs_Fact_ActuacionAsistencia
                                       Group By IdInstitucion,
                                                IdFacturacion,
                                                IdApunte) Tot
                       On Tot.IdInstitucion = Apu.IdInstitucion
                      And Tot.IdFacturacion = Apu.IdFacturacion
                      And Tot.IdApunte      = Apu.IdApunte
                     Left Outer Join Fcs_Fact_ActuacionAsistencia Faa
                       On Faa.IdInstitucion = Apu.IdInstitucion
                      And Faa.IdFacturacion = Apu.IdFacturacion
                      And Faa.IdApunte      = Apu.IdApunte
                           Left Outer Join Scs_ActuacionAsistencia Act
                             On Act.IdInstitucion = Faa.IdInstitucion
                            And Act.Anio          = Faa.Anio
                            And Act.Numero        = Faa.Numero 
                            And Act.Idactuacion   = Faa.IdActuacion
                                 Left Outer Join Scs_Comisaria Com
                                   On Com.IdInstitucion = Act.IdInstitucion
                                  And Com.IdComisaria   = Act.IdComisaria
                                 Left Outer Join Scs_Juzgado Juz
                                   On Juz.IdInstitucion = Act.IdInstitucion
                                  And Juz.IdJuzgado     = Act.IdJuzgado
                           Left Outer Join Scs_Asistencia Asi
                             On Asi.IdInstitucion = Faa.IdInstitucion
                            And Asi.Anio          = Faa.Anio
                            And Asi.Numero        = Faa.Numero
                                 Left Outer Join Scs_PersonaJG Pjg
                                   On Pjg.IdInstitucion = Asi.IdInstitucion
                                  And Pjg.IdPersona     = Asi.IdPersonaJG
                                 Left Outer Join Scs_EJG Ejg
                                   On Ejg.IdInstitucion = Asi.IdInstitucion
                                  And Ejg.IdTipoEJG     = Asi.EJGIdTipoEJG
                                  And Ejg.Anio          = Asi.EJGAnio
                                  And Ejg.Numero        = Asi.EJGNumero
                                 Left Outer Join (Select IdInstitucion,
                                                         Anio,
                                                         Numero,
                                                         Min(IdDelito) IdDelito
                                                    From Scs_DelitosAsistencia
                                                   Group By IdInstitucion,
                                                            Anio,
                                                            Numero) Mda
                                   On Mda.IdInstitucion = Asi.IdInstitucion
                                  And Mda.Anio          = Asi.Anio
                                  And Mda.Numero        = Asi.Numero
                                      Left Outer Join Scs_Delito Del
                                        On Del.IdInstitucion = Mda.IdInstitucion
                                       And Del.IdDelito      = Mda.IdDelito
                    Where (Fac.IdInstitucion,
                           Fac.IdFacturacion) In(Select IdInstitucion,
                                                        IdFacturacion
                                                   From Fcs_Fact_GrupoFact_Hito
                                                  Where IdGrupoFacturacion = 1))
;
/
