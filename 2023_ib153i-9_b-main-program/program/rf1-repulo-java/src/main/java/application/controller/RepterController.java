package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.dao.RepuloDAO;
import application.model.Jarat;
import application.model.Repter;
import application.model.Repulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import application.dao.UserDAO;
import application.dao.RepterDAO;
import application.model.User;

@Controller
public class RepterController {

  @Autowired
  private RepterDAO repterDAO;

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private RepuloDAO repuloDAO;



  @PostMapping(value = "/addRepter")
  public String insertRepter(@RequestParam("varos") String varos, @RequestParam("nev") String nev) {
    Repter ujRepter = new Repter(varos, nev);
    repterDAO.insertRepter(ujRepter);
    return "redirect:/repterek_kezelese";
  }

  @PostMapping(value = "/deletevaros")
  public String deleteRepter(@RequestParam("varos") String varos) {
    repterDAO.deleteRepter(varos);

    return "redirect:/repterek_kezelese";
  }


  @GetMapping(value = "/editrepter/{id}")
  public String editRepter(@PathVariable("id") String varos, Model model) {
    Repter repter = repterDAO.getRepterbyVaros(varos);
    model.addAttribute("repter", repter);

    return "update-repter"; // Replace with the actual view name for updating flights
  }

  @PostMapping(value = "/update/{id}")
  public String updateRepter(@PathVariable("id") String varos, @RequestParam("nev2") String nev) {
    repterDAO.updateRepter(varos, nev);

    return "redirect:/repterek_kezelese";
  }


  @GetMapping(value = "/airports")
  public String repterek(Model model) {
    List <Repter>  repterekLista = repterDAO.listRepterek();
    model.addAttribute("repterek", repterekLista);
    return "airports";
  }

  @GetMapping(value = "/repterek_kezelese")
  public String repterek2(Model model) {
    List <Repter>  repterekLista2 = repterDAO.listRepterek();
    model.addAttribute("repterek2", repterekLista2);
    return "repterek_kezelese";
  }
  @GetMapping(value = "/jarat_kereso")
  public String repterek3(Model model) {
    List <Repter>  repterekLista2 = repterDAO.listRepterek();
    model.addAttribute("repterek3", repterekLista2);
    return "jarat_kereso";
  }

}
