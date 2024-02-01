package application.controller;

import application.dao.JaratDAO;
import application.dao.JegyDAO;
import application.dao.RepuloDAO;
import application.model.Jarat;
import application.model.Jegy;
import application.model.Repulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class JegyController {

    @Autowired
    private JegyDAO jegyDAO;

    @Autowired
    private JaratDAO jaratDAO;

    @Autowired
    private RepuloDAO repuloDAO;

    @GetMapping("/jegyek")
    public String listJegyek(Model model) {
        model.addAttribute("jegyek", jegyDAO.findAll());
        return "jegyek"; // Nézet neve, ahol megjelenítjük a jegyeket
    }
    @GetMapping("/myJegyek")
    public String myJegyek(Model model) {
        return "myjegyek.html"; // Nézet neve, ahol megjelenítjük a jegyeket
    }

    @PostMapping("/createJegy")
    public String addJegy(@RequestParam("nev") String nev,
                          @RequestParam("jaratId") int jaratId,
                          @RequestParam(name = "pluszCsomag", defaultValue = "false") boolean pluszCsomag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String felhasznalonev = authentication.getName();
        Jarat jarat = jaratDAO.getJaratById(jaratId);
        Repulo repulo = repuloDAO.getRepuloByLajstromjel(jarat.getRepulo_lajstromjel());
        Random random = new Random();
        int hely = random.nextInt(repulo.getUlohely()-1)+1;
        Jegy newJegy = new Jegy(felhasznalonev, nev, hely, jaratId, pluszCsomag);
        jegyDAO.save(newJegy);
        return "redirect:/index"; // Visszairányítás a jegyek listázására
    }

        // Update (Módosítási űrlap megjelenítése)
        @GetMapping("/jegyek/edit/{id}")
        public String showUpdateForm(@PathVariable("id") int id, Model model) {
            Jegy jegy = jegyDAO.findJegyById(id);
            model.addAttribute("jegy", jegy);
            return "update-jegy"; // Nézet neve az űrlaphoz
        }

        // Update (Módosítás kezelése)
        @PostMapping("/jegyek/update/{id}")
        public String updateJegy(@PathVariable("id") int id, @ModelAttribute Jegy jegy, Model model) {
            jegy.setId(id);
            jegyDAO.updateJegy(jegy);
            return "redirect:/jegyek";
        }

    @PostMapping("/deleteJegy")
    public String deleteJegy(@RequestParam("id") int id, Model model) {
        jegyDAO.deleteJegy(id);
        return "redirect:/rendeles";
    }



    @GetMapping("/rendeles")
    public String rendeles(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        List<Map<String, Object>> jegyek;
        if (isAdmin) {
            // Admin esetén az összes jegy lekérdezése
            jegyek = jegyDAO.findAllWithUtInfo();
        } else {
            // Nem admin felhasználók esetén csak a saját jegyeik
            jegyek = jegyDAO.findJegyByFelhasznalonevWithUtInfo(currentUserName);
        }

        model.addAttribute("jegyek", jegyek);
        return "rendeles";
    }


    @GetMapping("/vasarlas/{id}")
    public String vasarlas(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "myJegyek";
    }
}


