package application.controller;

import application.dao.RepterDAO;
import application.model.Repter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import application.dao.UserDAO;
import application.model.User;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@Controller
public class UserController {

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private RepterDAO repterDAO;

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/register")
  public String register() {
    return "register.html";
  }
  @GetMapping("/login")
  public String login() {
    return "login.html";
  }


  @GetMapping("/error")
  public String error() {
    return "error.html";
  }






  @GetMapping("/arany_kartya")
  public String arany_kartya(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    User user = userDAO.findByUsername(currentUserName);
    model.addAttribute("user", user);
    return "arany_kartya.html";
  }

  @GetMapping("/rolunk")
  public String rolunk() {return "rolunk.html";}
  @GetMapping("/ut")
  public String ut() { return "ut.html";}
  @GetMapping("/repter")
  public String repter() {return "repter.html";}

  @PostMapping("/loginuser")
  public String loginUser(@RequestParam("felhasznalonev") String felhasznalonev,
                          @RequestParam("jelszo") String jelszo,
                          Model model) {
    User user = userDAO.findByUsername(felhasznalonev);
    if (user != null && passwordEncoder.matches(jelszo, user.getJelszo()) ) {
      authenticateUser(felhasznalonev, jelszo);
      return "redirect:/index";
    } else {
      model.addAttribute("loginError", "Érvénytelen felhasznalonév vagy jelszó.");
      return "login";
    }

  }
  @PostMapping(value = "/arany_kartya_vasarlas")
  public String arany_kartya_vasarlas(Model model) {
    // Lekérjük a bejelentkezett felhasználó authentikációját
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();

    // Ezt követően lekérhetjük a felhasználó adatait a felhasználónév alapján
    User user = userDAO.findByUsername(currentUserName);

    if (user != null) {
      user.setAranykartya(true);
      // Frissítjük a felhasználó adatait
      userDAO.updateUser(user);
      model.addAttribute("SikeresV", "Sikeres vásárlás!");


      return "index";
    }
    return "arany_kartya";
  }
  @PostMapping(value = "/registeruser")
  public String registerUser(@RequestParam("felhasznalonev") String felhasznalonev,
                             @RequestParam("jelszo") String jelszo,
                             @RequestParam("password-repeat") String jelszoUjra,
                             @RequestParam("vezetekNev") String vezetekNev,
                             @RequestParam("KeresztNev") String KeresztNev,
                             @RequestParam("email") String email,
                             @RequestParam("telefonSzam") String telefonSzam,
                             @RequestParam("bankkartyaszam") String bankkartyaszam,
                             Model model) {
    User existingUser = userDAO.findByUsername(felhasznalonev);
    if (existingUser != null) {
      model.addAttribute("usernameError", "Ez a felhasználónév már foglalt!");
      return "register";
    }
    //egyenlő e a két jelszó
    if (!jelszo.equals(jelszoUjra)) {

      model.addAttribute("passwordError", "A jelszavak nem egyeznek!");
      return "register";
    }
    //jelszó minimum 8 karakter
    if(jelszo.length()<8){
      model.addAttribute("passwordError", "A jelszó nem elég hosszú!");
      return "register";
    }
    //bankártya nem int
    if(String.valueOf(bankkartyaszam).isEmpty()){
      model.addAttribute("bankartyaError", "Add meg a bankártya számod!");
      return "register";
    }
    if(String.valueOf(telefonSzam).isEmpty()){
      model.addAttribute("phoneNumerError", "Add meg a telefonszámod számod!");
      return "register";
    }
    if(String.valueOf(felhasznalonev).isEmpty()){
      model.addAttribute("usernameError", "Add meg a felhasználóneved!");
      return "register";
    }

    //bankártya minimum 16 karakter
    if(String.valueOf(bankkartyaszam).length()<16){
      model.addAttribute("bankartyaError", "A bankatyaszam nem elég hosszú!");
      return "register";
    }

    //telefonszám minimum 11
    if(String.valueOf(telefonSzam).length()<11){
      model.addAttribute("phoneNumerError", "A telefonszám nem elég hosszú!");
      return "register";
    }




    User user = new User(felhasznalonev, jelszo,vezetekNev,KeresztNev , email, telefonSzam, bankkartyaszam);
    userDAO.insertUser(user);
    authenticateUser(felhasznalonev, jelszo);

    return "index";
  }
  private void authenticateUser(String username, String password) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    if (isAdmin){System.out.println(isAdmin);}
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return null;
  }

  @GetMapping("/deleteUser/{felhasznalonev}")
  public String deleteUser(@PathVariable("felhasznalonev") String felhasznalonev) {

    userDAO.deleteUser(felhasznalonev);
    return "redirect:/index";
  }

  //@GetMapping("/index")
  //public String showIndexPage() {
  //  return "index.html";
  //}
  @GetMapping("/logout")
  public String customLogout(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    return "redirect:/index";
  }

  @GetMapping("/fiokom")
  public String listUserData(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String felhasznalonev = authentication.getName();
    User currentUser = userDAO.findByUsername(felhasznalonev);
    model.addAttribute("user", currentUser);
    return "fiokom";
  }

  @PostMapping(value = "/updateUser")
  public String setUser(@RequestParam("vezetekNev") String vezetekNev,
                        @RequestParam("keresztNev") String keresztNev,
                        @RequestParam("email") String email,
                        @RequestParam("telefonSzam") String telefonSzam,
                        @RequestParam("bankkartyaSzam") String bankkartyaszam,
                        Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String felhasznalonev = authentication.getName();
    boolean vNev = true, kNev = true, mail = true, tSzam = true, bKartya = true;

    //Vezetéknév ellenőzés
    if (String.valueOf(vezetekNev).isEmpty()) {
      vNev = false;
    }

    //KeresztNév ellenőzés
    if (String.valueOf(keresztNev).isEmpty()) {
      kNev = false;
    }

    //Email ellenőzés
    if (!String.valueOf(email).isEmpty()) {
      Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
      Matcher mat = pattern.matcher(email);
      if (!mat.matches()) {
        model.addAttribute("emailError", "Add meg az email címed!");
        return "fiokom";
      }
    } else {
      mail = false;
    }

    //Bankártya ellenőrzés
    if(!String.valueOf(bankkartyaszam).isEmpty()){
      if(String.valueOf(bankkartyaszam).length()<16){
        model.addAttribute("bankartyaError", "A bankatyaszam nem elég hosszú!");
        return "fiokom";
      }
    } else {
      bKartya = false;
    }

    //Telefonszám ellenőrzés
    if(!String.valueOf(telefonSzam).isEmpty()){
      if(String.valueOf(telefonSzam).length()<11){
        model.addAttribute("phoneNumerError", "A telefonszám nem elég hosszú!");
        return "fiokom";
      }
    } else {
      tSzam = false;
    }

    //Adatok módosítása
    User user = userDAO.findByUsername(felhasznalonev);
    if (vNev) {user.setVezetekNev(vezetekNev);}
    if (kNev) {user.setKeresztNev(keresztNev);}
    if (mail) {user.setEmail(email);}
    if (tSzam) {user.setTelefonSzam(telefonSzam);}
    if (bKartya) {user.setBankkartyaszam(bankkartyaszam);}
    userDAO.updateUser(user);
    return "index";
  }
}