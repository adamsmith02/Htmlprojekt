package application.controller;

import java.time.LocalDateTime;
import java.util.*;

import application.dao.*;
import application.model.*;
//import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class JaratController {

    @Autowired
    private JaratDAO jaratDAO;

    @Autowired
    private RepterDAO repterDAO;

    @Autowired
    private UtDAO utDAO;

    @Autowired
    private RepuloDAO repuloDAO;


    @GetMapping(value = "/akciok")
    public String akciok(Model model) {
        Map<Jarat, Ut> jaratLista = jaratDAO.listJaratok();
        model.addAttribute("jaratLista", jaratLista);
        return "akciok";
    }

    @GetMapping(value = "/index")
    public String topAkciok(Model model) {
        Map<Jarat, Ut> jaratLista = jaratDAO.listJaratok();
        model.addAttribute("topAkciok", jaratLista);

        List<Repter> repterekLista3 = repterDAO.listRepterek();
        model.addAttribute("repterek3", repterekLista3);

        return "index";
    }

    @PostMapping(value = "/addJarat")
    public String insertJarat(@RequestParam("akcio") int akcio,
                              @RequestParam("utId") int utId,
                              @RequestParam("repLajstrom") String repLajstrom,
                              Model model) {
        Jarat ujJarat = new Jarat(akcio, utId, repLajstrom);
        jaratDAO.insertJarat(ujJarat);
        return "redirect:/jaratok_kezelese";
    }

    @GetMapping("/jaratok_kezelese")
    public String jaratok_kezelese(Model model) {
        List<JaratDAO.JaratInfo> jaratList = jaratDAO.listJaratok2();
        model.addAttribute("jaratok_kezelese", jaratList);

        List<Ut> utak = utDAO.listUt();
        model.addAttribute("utak", utak);

        List <Repulo> repulok = repuloDAO.listRepulok();
        model.addAttribute("repulok", repulok);

        return "jaratok_kezelese";

    }
//    @GetMapping("/jarat_kereso") //todo ideiglenes
//    public String jarat_kereso(Model model) {
//        List<JaratDAO.JaratInfo> jaratList = jaratDAO.listJaratok2();
//        model.addAttribute("jaratok_kezelese", jaratList);
//        return "jarat_kereso.html";
//
//    }
    @PostMapping(value = "/jaratKereses")
    public String jaratKereses(@RequestParam("repterek")  String repter,
                                @RequestParam("allapot") String allapot,
                                Model model){

        boolean megy = false;
        if(allapot.equals("jon")) megy = true;
        //if(allapot.equals("megy")) megy = false;
        Map<Jarat,Ut> jaratok = jaratDAO.listJaratok();
        Map<Jarat,Ut> megoldas = new HashMap<>();

        for (Map.Entry<Jarat,Ut> entry : jaratok.entrySet()){
            if(Objects.equals(entry.getValue().getRepterVaros(), repter) && entry.getValue().isMegy() == megy ){
                megoldas.put(entry.getKey(),entry.getValue());
            }
        }

        model.addAttribute("jaratKeresesKiiras",megoldas);

        List <Repter>  repterekLista2 = repterDAO.listRepterek();
        model.addAttribute("repterek3", repterekLista2);

        return "jarat_kereso";
    }

    @PostMapping(value = "/deletejarat")
    public String deleteJarat(@RequestParam("id") int jaratId,
                              Model model) {

        jaratDAO.deleteJarat(jaratId);
        return "redirect:/jaratok_kezelese";
    }

    @GetMapping(value = "/editjarat/{id}")
    public String editJarat(@PathVariable("id") int id, Model model) {
        Jarat jarat = jaratDAO.getJaratById(id);
        model.addAttribute("jarat", jarat);
        List<Ut> utak = utDAO.listUt();
        model.addAttribute("utak", utak);

        List <Repulo> repulok = repuloDAO.listRepulok();
        model.addAttribute("repulok", repulok);

        return "update-jarat";
    }

    @PostMapping(value = "/updateJarat/{id}")
    public String updateJarat(@PathVariable("id") int id,
                              @RequestParam("akcio") int akcio,
                              @RequestParam("utId") int utId,
                              @RequestParam("repLajstrom") String repLajstrom) {
        jaratDAO.updateJarat(id, akcio, utId, repLajstrom);

        return "redirect:/jaratok_kezelese";
    }
}
