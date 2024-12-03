package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/register")
    public String registerMenu(Model model) {
        model.addAttribute("user", new Account());
        return "createaccount";
    }
    @PostMapping("/register")
    public String createUser(@ModelAttribute Account account) {
        accountRepository.save(account);
        return "redirect:/account/" + account.getId() + "/display";
    }

    @GetMapping("/login")
    public String loginMenu(Model model, @RequestParam(defaultValue = "") String message) {
        model.addAttribute("message", message);
        model.addAttribute("user", new Account());
        return "loginaccount";
    }
    @PostMapping("/login")
    public String loginUser(@ModelAttribute Account account) {
        Account account2 = accountRepository.findByUsername(account.getUsername()).orElse(null);
        if(account2 == null){
            return "redirect:/account/login?message=AccountNotFound";
        }
        if(account2.getHashedPassword().equals(account.getHashedPassword())){
            return "redirect:/account/" + account2.getId() + "/display";
        }
        return "redirect:/account/login?message=InvalidPassword";

    }

    @GetMapping("/{id}/display")
    public String displayUser(@PathVariable Long id, Model model){
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", account);
        return "displayaccount";
    }


}
