package application.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class User implements UserDetails{


  private String felhasznalonev;
  private String jelszo;
  private String VezetekNev;
  private String KeresztNev;

  private String email;


  private String telefonSzam;
  private String bankkartyaszam;
  private boolean aranykartya;
  private String role;


  public User(String felhasznalonev, String jelszo, String VezetekNev,String KeresztNev, String email, String telefonSzam, String bankkartyaszam, boolean aranykartya, String role) {
    this.felhasznalonev = felhasznalonev;
    this.jelszo = jelszo;
   this.KeresztNev=KeresztNev;
   this.VezetekNev=VezetekNev;
    this.email = email;
    this.telefonSzam = telefonSzam;
    this.bankkartyaszam = bankkartyaszam;
    this.aranykartya = aranykartya;
    this.role = role;
  }

  public User(String felhasznalonev, String jelszo, String VezetekNev,String KeresztNev, String email, String telefonSzam, String bankkartyaszam) {
    this.felhasznalonev = felhasznalonev;
    this.jelszo = jelszo;
   this.VezetekNev=VezetekNev;
   this.KeresztNev=KeresztNev;
    this.email = email;
    this.telefonSzam = telefonSzam;
    this.bankkartyaszam = bankkartyaszam;
    this.aranykartya = false;
    this.role = "USER";
  }
  public User(){}

  // Getter és Setter metódusok az új adattagokhoz
  public String getFelhasznalonev() {
    return felhasznalonev;
  }

  public void setFelhasznalonev(String felhasznalonev) {
    this.felhasznalonev = felhasznalonev;
  }

  public String getJelszo() {
    return jelszo;
  }

  public void setJelszo(String jelszo) {
    this.jelszo = jelszo;
  }

  public String getVezetekNev() {
    return VezetekNev;
  }

  public void setVezetekNev(String vezetekNev) {
    VezetekNev = vezetekNev;
  }

  public String getKeresztNev() {
    return KeresztNev;
  }

  public void setKeresztNev(String keresztNev) {
    KeresztNev = keresztNev;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefonSzam() {
    return telefonSzam;
  }

  public void setTelefonSzam(String telefonSzam) {
    this.telefonSzam = telefonSzam;
  }

  public String getBankkartyaszam() {
    return bankkartyaszam;
  }

  public void setBankkartyaszam(String bankkartyaszam) {
    this.bankkartyaszam = bankkartyaszam;
  }

  public boolean isAranykartya() {
    return aranykartya;
  }

  public void setAranykartya(boolean aranykartya) {
    this.aranykartya = aranykartya;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Egyszerűsített példa, feltételezve, hogy a szerepkörök sztringként vannak tárolva
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
  }

  @Override
  public String getPassword() {
    return this.jelszo;
  }

  @Override
  public String getUsername() {
    return this.felhasznalonev;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
