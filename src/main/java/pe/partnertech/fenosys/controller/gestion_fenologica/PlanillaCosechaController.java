/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.gestion_fenologica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.FaseFenologica;
import pe.partnertech.fenosys.model.PlanillaCosecha;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IFaseFenologicaService;
import pe.partnertech.fenosys.service.IPlanillaCosechaService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PlanillaCosechaController {

    @Autowired
    IPlanillaCosechaService planillaCosechaService;

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IFaseFenologicaService faseFenologicaService;

    @PostMapping("/agricultor/{id}/planilla/register")
    @PreAuthorize("hasRole('ROLE_AGRICULTOR')")
    public ResponseEntity<?> RegistrarPlanillaCosecha(@PathVariable("id") Long id,
                                                      @RequestBody PlanillaCosecha planillacosecha) {

        Optional<Usuario> agricultor_data = usuarioService.BuscarUsuario_ID(id);

        if (agricultor_data.isPresent()) {
            Optional<PlanillaCosecha> planillacosecha_data = planillaCosechaService.BuscarPlanillaCosecha_CosechaYIDAgricultor(planillacosecha.getNombreCosecha(), id);

            if (planillacosecha_data.isPresent()) {
                return new ResponseEntity<>(new MessageResponse("Ya se encuentra registrado esa planilla."), HttpStatus.BAD_REQUEST);
            } else {
                PlanillaCosecha planillacosecha_agricultor = new PlanillaCosecha();

                planillacosecha_agricultor.setNombreCosecha(planillacosecha.getNombreCosecha());

                planillaCosechaService.GuardarPlanillaCosecha(planillacosecha_agricultor);

                Usuario agricultor = agricultor_data.get();

                Set<PlanillaCosecha> planillacosecha_list;
                if (agricultor.getPlanillacosechaUsuario() == null) {
                    planillacosecha_list = new HashSet<>();
                } else {
                    planillacosecha_list = agricultor.getPlanillacosechaUsuario();
                }
                planillacosecha_list.add(planillacosecha_agricultor);
                agricultor.setPlanillacosechaUsuario(planillacosecha_list);

                usuarioService.GuardarUsuarioSemiFull(agricultor);

                Optional<PlanillaCosecha> current_planillacosecha_data = planillaCosechaService.BuscarPlanillaCosecha_CosechaYIDAgricultor(planillacosecha.getNombreCosecha(), id);

                if (current_planillacosecha_data.isPresent()) {
                    PlanillaCosecha config_planillacosecha = current_planillacosecha_data.get();

                    switch (planillacosecha_agricultor.getNombreCosecha()) {
                        case "Avena":
                            config_planillacosecha.setTipoCosecha("Anual");
                            GuardarConfiguracionAvena(config_planillacosecha);
                            break;
                        case "Caña de Azúcar":
                            config_planillacosecha.setTipoCosecha("Permanente");
                            GuardarConfiguracionCañaAzucar(config_planillacosecha);
                            break;
                        case "Kiwicha":
                            config_planillacosecha.setTipoCosecha("Anual");
                            GuardarConfiguracionKiwicha(config_planillacosecha);
                            break;
                        case "Mango":
                            config_planillacosecha.setTipoCosecha("Permanente");
                            GuardarConfiguracionMango(config_planillacosecha);
                            break;
                        case "Limón":
                            config_planillacosecha.setTipoCosecha("Permanente");
                            GuardarConfiguracionLimón(config_planillacosecha);
                            break;
                        default:
                            config_planillacosecha.setTipoCosecha(null);
                    }

                    planillaCosechaService.GuardarPlanillaCosecha(config_planillacosecha);

                    return new ResponseEntity<>(new MessageResponse("Se ha guardado la planilla satisfactoriamente."), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error durante la configuración de la Planilla."), HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encontró información del usuario."), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/planilla/{id}/delete")
    @PreAuthorize("hasRole('ROLE_AGRICULTOR')")
    public ResponseEntity<?> EliminarPlanillaCosecha(@PathVariable("id") Long id) {

        Optional<PlanillaCosecha> planillacosecha_data = planillaCosechaService.BuscarPlanillaCosecha_ID(id);

        if (planillacosecha_data.isPresent()) {

            EliminarFasesFenologicas(id);

            planillaCosechaService.EliminarPlanillaCosecha_From_PCU_MiddleTable(id);
            planillaCosechaService.EliminarPlanillaCosecha_This(id);

            return new ResponseEntity<>(new MessageResponse("Se ha eliminado la planilla satisfactoriamente."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al eliminar la planilla."), HttpStatus.NOT_FOUND);
        }
    }

    //Avena Config
    public void GuardarConfiguracionAvena(PlanillaCosecha planillaCosecha) {
        FaseFenologica ff_01 = new FaseFenologica("Emergencia", 1);

        FaseFenologica ff_02 = new FaseFenologica("Tercera Hoja", 2);

        FaseFenologica ff_03 = new FaseFenologica("Macollaje", 3);

        FaseFenologica ff_04 = new FaseFenologica("Encañado", 4);

        FaseFenologica ff_05 = new FaseFenologica("Panoja", 5);

        FaseFenologica ff_06 = new FaseFenologica("Floración", 6);

        FaseFenologica ff_07 = new FaseFenologica("Maduración lechosa", 7);

        FaseFenologica ff_08 = new FaseFenologica("Maduración pastosa", 8);

        FaseFenologica ff_09 = new FaseFenologica("Maduración córnea", 9);

        List<FaseFenologica> aux_lista_fasesfenologicas = Arrays.asList(ff_01, ff_02, ff_03, ff_04, ff_05, ff_06, ff_07, ff_08, ff_09);
        faseFenologicaService.GuardarFasesFenologicas_Config(aux_lista_fasesfenologicas);

        Set<FaseFenologica> lista_fasesfenologicas = new HashSet<>(aux_lista_fasesfenologicas);
        PlanillaCosecha current_planillacosecha = planillaCosecha;
        current_planillacosecha.setFasesfenologicasPlanillaCosecha(lista_fasesfenologicas);
        planillaCosechaService.GuardarPlanillaCosecha(current_planillacosecha);
    }

    //Kiwicha Config
    public void GuardarConfiguracionKiwicha(PlanillaCosecha planillaCosecha) {
        FaseFenologica ff_01 = new FaseFenologica("Emergencia", 1);

        FaseFenologica ff_02 = new FaseFenologica("Dos hojas verdaderas", 2);

        FaseFenologica ff_03 = new FaseFenologica("Seis hojas verdaderas", 3);

        FaseFenologica ff_04 = new FaseFenologica("Ramificación", 4);

        FaseFenologica ff_05 = new FaseFenologica("Panoja", 5);

        FaseFenologica ff_06 = new FaseFenologica("Floración", 6);

        FaseFenologica ff_07 = new FaseFenologica("Grano lechoso", 7);

        FaseFenologica ff_08 = new FaseFenologica("Grano pastoso", 8);

        FaseFenologica ff_09 = new FaseFenologica("Maduración", 9);

        List<FaseFenologica> aux_lista_fasesfenologicas = Arrays.asList(ff_01, ff_02, ff_03, ff_04, ff_05, ff_06, ff_07, ff_08, ff_09);
        faseFenologicaService.GuardarFasesFenologicas_Config(aux_lista_fasesfenologicas);

        Set<FaseFenologica> lista_fasesfenologicas = new HashSet<>(aux_lista_fasesfenologicas);
        PlanillaCosecha current_planillacosecha = planillaCosecha;
        current_planillacosecha.setFasesfenologicasPlanillaCosecha(lista_fasesfenologicas);
        planillaCosechaService.GuardarPlanillaCosecha(current_planillacosecha);
    }

    //Limón Config
    public void GuardarConfiguracionLimón(PlanillaCosecha planillaCosecha) {
        FaseFenologica ff_01 = new FaseFenologica("Hinchazón de botón floral", 1);

        FaseFenologica ff_02 = new FaseFenologica("Apertura de botón floral", 2);

        FaseFenologica ff_03 = new FaseFenologica("Floración", 3);

        FaseFenologica ff_04 = new FaseFenologica("Fructificación", 4);

        FaseFenologica ff_05 = new FaseFenologica("Maduración", 5);

        List<FaseFenologica> aux_lista_fasesfenologicas = Arrays.asList(ff_01, ff_02, ff_03, ff_04, ff_05);
        faseFenologicaService.GuardarFasesFenologicas_Config(aux_lista_fasesfenologicas);

        Set<FaseFenologica> lista_fasesfenologicas = new HashSet<>(aux_lista_fasesfenologicas);
        PlanillaCosecha current_planillacosecha = planillaCosecha;
        current_planillacosecha.setFasesfenologicasPlanillaCosecha(lista_fasesfenologicas);
        planillaCosechaService.GuardarPlanillaCosecha(current_planillacosecha);
    }

    //Caña de Azúcar Config
    public void GuardarConfiguracionCañaAzucar(PlanillaCosecha planillaCosecha) {
        FaseFenologica ff_01 = new FaseFenologica("Emergencia", 1);

        FaseFenologica ff_02 = new FaseFenologica("Primer banderín", 2);

        FaseFenologica ff_03 = new FaseFenologica("Macollaje", 3);

        FaseFenologica ff_04 = new FaseFenologica("Crecimiento de tallos", 4);

        FaseFenologica ff_05 = new FaseFenologica("Inflorescencia", 5);

        FaseFenologica ff_06 = new FaseFenologica("Floración", 6);

        FaseFenologica ff_07 = new FaseFenologica("Maduración", 7);

        List<FaseFenologica> aux_lista_fasesfenologicas = Arrays.asList(ff_01, ff_02, ff_03, ff_04, ff_05, ff_06, ff_07);
        faseFenologicaService.GuardarFasesFenologicas_Config(aux_lista_fasesfenologicas);

        Set<FaseFenologica> lista_fasesfenologicas = new HashSet<>(aux_lista_fasesfenologicas);
        PlanillaCosecha current_planillacosecha = planillaCosecha;
        current_planillacosecha.setFasesfenologicasPlanillaCosecha(lista_fasesfenologicas);
        planillaCosechaService.GuardarPlanillaCosecha(current_planillacosecha);
    }

    //Mango Config
    public void GuardarConfiguracionMango(PlanillaCosecha planillaCosecha) {
        FaseFenologica ff_01 = new FaseFenologica("Brotamiento", 1);

        FaseFenologica ff_02 = new FaseFenologica("Floración", 2);

        FaseFenologica ff_03 = new FaseFenologica("Cuajado", 3);

        FaseFenologica ff_04 = new FaseFenologica("Fructificación", 4);

        FaseFenologica ff_05 = new FaseFenologica("Maduración", 5);

        List<FaseFenologica> aux_lista_fasesfenologicas = Arrays.asList(ff_01, ff_02, ff_03, ff_04, ff_05);
        faseFenologicaService.GuardarFasesFenologicas_Config(aux_lista_fasesfenologicas);

        Set<FaseFenologica> lista_fasesfenologicas = new HashSet<>(aux_lista_fasesfenologicas);
        PlanillaCosecha current_planillacosecha = planillaCosecha;
        current_planillacosecha.setFasesfenologicasPlanillaCosecha(lista_fasesfenologicas);
        planillaCosechaService.GuardarPlanillaCosecha(current_planillacosecha);
    }

    //Eliminar las Fases Fenologicas de la Planilla de Cosecha
    void EliminarFasesFenologicas(Long idPlanillaCosecha) {

        Set<FaseFenologica> lista_fasesfenologicas =
                faseFenologicaService.BuscarFasesFenologicas_From_FFPC_MiddleTable_By_IDPlanillaCosecha(idPlanillaCosecha);

        List<FaseFenologica> listadelete_fasesfenologicas = new ArrayList<>();
        listadelete_fasesfenologicas.addAll(lista_fasesfenologicas);

        for (FaseFenologica delete_fasefenologica : lista_fasesfenologicas) {
            faseFenologicaService.EliminarFaseFenologica_From_FFPC_MiddleTable_By_IDPlanillaCosecha(idPlanillaCosecha);
            faseFenologicaService.EliminarFaseFenologica(delete_fasefenologica.getIdFaseFenologica());
        }
    }
}

