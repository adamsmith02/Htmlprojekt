package application.controller;

import application.model.Repter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import application.dao.RepuloDAO;
import application.model.Repulo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RepuloController {

    @Autowired
    private RepuloDAO repuloDAO;

  /*  @GetMapping(value = "/repulok")
    public String listRepulo(Model model) {
        List<Repulo> repuloList = repuloDAO.listRepulok();
        model.addAttribute("repulok", repuloList);
        return "listRepulok"; // A nézet neve, ami megjeleníti a repülőgépeket
    }*/



    @PostMapping(value = "/deleteGep")
    public String deleteRepulo(@RequestParam("lajstromjel") String lajstromjel, Model model) {
        repuloDAO.deleteRepulo(lajstromjel);

        model.addAttribute("sikeresTorlesGep", "Sikeresen törölte a gépet!");
        List <Repulo>  repuloLista = repuloDAO.listRepulok() ;
        model.addAttribute("gepek", repuloLista);
        return "gepek_kezelese";
    }


    @GetMapping(value = "/editGep/{id}")
    public String editRepulo(@PathVariable("id") String lajstromjel, Model model) {
        Repulo repulo = repuloDAO.getRepuloByLajstromjel(lajstromjel);
        model.addAttribute("repulo", repulo);
        return "update-repulo";
    }



    @PostMapping(value = "/updateGep/{id}")
    public String updateRepulo(Model model,@PathVariable("id") String lajstromjel, @RequestParam("becenev") String becenev, @RequestParam("modell") String modell, @RequestParam("ulohely") int ulohely,
                               @RequestParam("rakter") int rakter, @RequestParam("info") String info, @RequestParam("repultU") int repultU) {
        try {
            validateRepuloInputs(lajstromjel, becenev, modell, ulohely, rakter, info, repultU,false);
            repuloDAO.updateRepulo(lajstromjel, becenev, modell, ulohely, rakter, info, repultU);
            model.addAttribute("successMessageRepuloup", "Sikerült módosítani a gép adatait!");
            return editRepulo(lajstromjel, model);
            //return "redirect:/gepek_kezelese";
        }catch (ValidationException e) {
            model.addAttribute("errorMessageRepuloup", e.getErrorMessages());
            return editRepulo(lajstromjel, model);
        }

    }

    @GetMapping(value = "/repcsik")
    public String repulok(Model model) {
        List <Repulo>  repuloLista = repuloDAO.listRepulok() ;
        model.addAttribute("repulok", repuloLista);
        return "repcsik";
    }
    @GetMapping(value = "/gepek_kezelese")
    public String gepek(Model model) {
        List <Repulo>  repuloLista = repuloDAO.listRepulok() ;
        model.addAttribute("gepek", repuloLista);
        return "gepek_kezelese";
    }

    /*@PostMapping(value = "/addRepulo")
    public String insertRepulo(@RequestParam("lajstromjel") String lajstromjel, @RequestParam("becenev") String becenev,
                               @RequestParam("modell") String modell, @RequestParam("ulohely") int ulohely,
                               @RequestParam("rakter") int rakter, @RequestParam("info") String info,@RequestParam("reput") int reput
    ) {
        Repulo ujRepulo = new Repulo(lajstromjel, becenev, modell, ulohely, rakter, info, reput);
        repuloDAO.insertRepulo(ujRepulo);
        return "redirect:/gepek_kezelese";
    }*/
    @PostMapping(value = "/addRepulo")
    public String insertRepulo(Model model, @RequestParam("lajstromjel") String lajstromjel, @RequestParam("becenev") String becenev,
                               @RequestParam("modell") String modell, @RequestParam("ulohely") int ulohely,
                               @RequestParam("rakter") int rakter, @RequestParam("info") String info, @RequestParam("reput") int reput
    ) {

        try {
            model.addAttribute("errorMessageRepulo", null);
            model.addAttribute("succsessMessageRepulo", null);

            validateRepuloInputs(lajstromjel, becenev, modell, ulohely, rakter, info, reput,true);

            Repulo ujRepulo = new Repulo(lajstromjel, becenev, modell, ulohely, rakter, info, reput);

            repuloDAO.insertRepulo(ujRepulo);
            model.addAttribute("succsessMessageRepulo", "Sikerült hozzáadni az új gépet!");
            return gepek(model);
        } catch (ValidationException e) {
            model.addAttribute("errorMessageRepulo", e.getErrorMessages());
            return gepek(model);
        }
    }

    private void validateRepuloInputs(String lajstromjel, String becenev, String modell, int ulohely, int rakter, String info, int reput,boolean add) throws ValidationException {
        List<String> errorMessages = new ArrayList<>();

        if (lajstromjel == null || lajstromjel.isEmpty()) {
            errorMessages.add("Lajstromjel mező nem lehet üres!");
        }
        if (becenev == null || becenev.isEmpty()) {
            errorMessages.add("becenév mező nem lehet üres!");
        }
        if (modell == null || modell.isEmpty()) {
            errorMessages.add("modell mező nem lehet üres!");
        }
        if (info == null || info.isEmpty()) {
            errorMessages.add("modell mező nem lehet üres!");
        }
        if (ulohely < 1) {
            errorMessages.add("Legalább egy ülőhelynek kelleneije a gépen!");
        }
        if (rakter < 0) {
            errorMessages.add("A raktér mérete nem lehet negatív!");
        }
        if (reput < 0) {
            errorMessages.add("A repült utak száma nem lehet negatív!");
        }

        if(lajstromjel.length() > 100){
            errorMessages.add("lajstromjel maximum 100 karakter hoszú lehet!");
        }
        if(becenev.length() > 100){
            errorMessages.add("becenév maximum 100 karakter hoszú lehet!");
        }
        if(modell.length() > 100){
            errorMessages.add("modell maximum 100 karakter hoszú lehet!");
        }
        if(add){
            if (repuloDAO.isLajstromjelExists(lajstromjel)) {
                errorMessages.add("Ez a lajstromjel már létezik az adatbázisban!");
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }


    public class ValidationException extends Exception {
        private List<String> errorMessages;

        public ValidationException(List<String> errorMessages) {
            this.errorMessages = errorMessages;
        }

        public List<String> getErrorMessages() {
            return errorMessages;
        }
    }






}

