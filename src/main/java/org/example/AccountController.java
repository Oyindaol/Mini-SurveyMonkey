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
    public String registerMenu(Model model, @RequestParam(defaultValue = "") String message) {
        model.addAttribute("message", message);
        model.addAttribute("user", new Account());
        return "createaccount";
    }
    @PostMapping("/register")
    public String createUser(@ModelAttribute Account account, Model model) {
        //Check if username already exists
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            //If username exists, add an error message and return to the registration form
            return "redirect:/account/register?message=UsernameExists";

        }

        //Validate the raw password against the policy
        if(!Account.isValidPassword(account.getTempPassword())){
            return "redirect:/account/register?message=InvalidPassword";
        }

        //If the username doesn't exist, hash the password and save the new account
        account.setTempPassword(null);
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
        Account accountInDb = accountRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + account.getUsername()));   //.orElse(null);

        if(!accountInDb.passwordMatch(account.getHashedPassword())){
            throw new InvalidInputException("Invalid password. Please try again.");
        }
//        if(account2 == null){
//            return "redirect:/account/login?message=AccountNotFound";
//        }
//        if(account2.getHashedPassword().equals(account.getHashedPassword())){
//            return "redirect:/account/" + account2.getId() + "/display";
//        }
        return "redirect:/account/" + accountInDb.getId() + "/display";

    }

    @GetMapping("/{id}/display")
    public String displayUser(@PathVariable Long id, Model model){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        model.addAttribute("user", account);
        return "displayaccount";
    }


}
