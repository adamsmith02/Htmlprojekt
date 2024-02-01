package application.controller;

import application.dao.RepterDAO;
import application.model.Repter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import application.dao.UtDAO;
import application.model.Ut;

import java.util.List;

@Controller
public class UtController {

    @Autowired
    private UtDAO utDAO;
    private RepterDAO repterDAO;
    @GetMapping("/createUt")
    public String showCreateUtForm(Model model) {

        List<Repter> availableRepterek = repterDAO.listRepterek();
        model.addAttribute("availableRepterek", availableRepterek);
        return "createUt";
    }

    @PostMapping("/createUt")
    public String addUt(@RequestParam("hossz") int hossz,
                        @RequestParam("ar") int ar,
                        @RequestParam(value = "megy", defaultValue = "false") boolean megy,
                        @RequestParam("repterVaros") String repterVaros,
                        Model model) {
        Ut newUt = new Ut(); // Tegyük fel, hogy van egy konstruktor, amely beállítja ezeket az értékeket
        newUt.setHossz(hossz);
        newUt.setAr(ar);
        newUt.setMegy(megy);
        newUt.setRepterVaros(repterVaros);
        utDAO.addUt(newUt);


        return "redirect:/uts"; // Változtasd meg az útvonalat a megfelelő nézethez
    }
    // ... meglévő UtController kód ...

    @GetMapping("/editUt/{id}")
    public String showUpdateUtForm(@PathVariable int id, Model model) {
        Ut ut = utDAO.getUtById(id);
        model.addAttribute("ut", ut);
        model.addAttribute("availableRepterek", repterDAO.listRepterek());
        return "editUt";
    }

    @PostMapping("/updateUt/{id}")
    public String updateUt(@PathVariable int id,
                           @RequestParam("hossz") int hossz,
                           @RequestParam("ar") int ar,
                           @RequestParam(value = "megy", defaultValue = "false") boolean megy,
                           @RequestParam("repterVaros") String repterVaros) {
        Ut ut = new Ut(hossz, ar, megy, repterVaros);
        ut.setId(id); // Feltételezve, hogy a Ut osztálynak van setId metódusa
        utDAO.updateUt(ut);
        return "redirect:/uts";
    }

    @GetMapping("/deleteUt/{id}")
    public String deleteUt(@PathVariable int id) {
        utDAO.deleteUt(id);
        return "redirect:/uts";
    }

}
