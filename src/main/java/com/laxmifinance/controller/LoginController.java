package com.laxmifinance.controller;

import com.laxmifinance.form.LoginForm;
import com.laxmifinance.form.PasswordForm;
import com.laxmifinance.form.RegistrationForm;
import com.laxmifinance.model.AppUser;
import com.laxmifinance.repository.UserRepository;
import com.laxmifinance.util.BrowserParser;
import com.laxmifinance.util.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserRepository users;

    public LoginController(UserRepository users) {
        this.users = users;
    }

    @GetMapping({"/", "/index"})
    public String index(HttpSession session, Model model) {
        if (loggedIn(session)) {
            return "redirect:/successAction";
        }
        model.addAttribute("login", new LoginForm());
        return "index";
    }

    @GetMapping("/failAction")
    public String fail(HttpSession session, Model model) {
        if (loggedIn(session)) {
            return "redirect:/successAction";
        }
        model.addAttribute("login", new LoginForm());
        return "index";
    }

    @PostMapping("/userLogin")
    public String login(@ModelAttribute LoginForm form, HttpSession session,
                        HttpServletRequest request, RedirectAttributes redirect) {
        String userName = form.getUserName() == null ? "" : form.getUserName().trim();
        Optional<String> storedPassword = users.findPassword(userName);
        if (userName.isEmpty() || storedPassword.isEmpty()
                || !CryptoUtil.matches(form.getPassword(), storedPassword.get())) {
            redirect.addFlashAttribute("error", "Please Enter Correct User Name and password");
            return "redirect:/failAction";
        }
        Optional<AppUser> appUser = users.loadUser(userName);
        if (appUser.isEmpty()) {
            redirect.addFlashAttribute("error", "Please Enter Correct User Name and password");
            return "redirect:/failAction";
        }
        AppUser user = appUser.get();
        session.setAttribute("userDetails", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getUserName());
        users.recordLogin(user.getUserId(), BrowserParser.clientIp(request), BrowserParser.describe(request));
        return "redirect:/successAction";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping("/userRegistration")
    public String registrationForm(Model model) {
        model.addAttribute("registration", new RegistrationForm());
        return "registration";
    }

    @GetMapping("/regSuccess")
    public String registrationSuccess(Model model) {
        model.addAttribute("registration", new RegistrationForm());
        return "registration";
    }

    @PostMapping("/userRegistration")
    public String register(@ModelAttribute RegistrationForm form, RedirectAttributes redirect) {
        String userName = form.getUserName() == null ? "" : form.getUserName().trim();
        if (userName.isEmpty() || form.getPassword() == null || form.getPassword().isBlank()) {
            redirect.addFlashAttribute("message", "Please enter User Name and Password");
            return "redirect:/regSuccess";
        }
        if (!form.getPassword().equals(form.getCpassword())) {
            redirect.addFlashAttribute("message", "Password And Confirm Password Should be Same");
            return "redirect:/regSuccess";
        }
        if (users.userNameExists(userName)) {
            redirect.addFlashAttribute("message",
                    " User Name " + userName + " already exists Please choose another ");
            return "redirect:/regSuccess";
        }
        int inserted = users.insertUser(form, CryptoUtil.encrypt(form.getPassword()));
        if (inserted > 0) {
            redirect.addFlashAttribute("message", userName + " successfully registerd ");
        } else if (inserted == -1) {
            redirect.addFlashAttribute("message",
                    " User Name " + userName + " already exists Please choose another ");
        } else {
            redirect.addFlashAttribute("message", "Registration Failed");
        }
        return "redirect:/regSuccess";
    }

    @GetMapping({"/userDetails", "/userUpdate"})
    public String passwordPage(HttpSession session, Model model) {
        model.addAttribute("userDetails", session.getAttribute("userDetails"));
        return "userDetails";
    }

    @PostMapping("/userController")
    public String changePassword(@ModelAttribute PasswordForm form, HttpSession session,
                                 RedirectAttributes redirect) {
        String sessionUser = userName(session);
        String requestedUser = form.getUserName() == null ? "" : form.getUserName().trim();
        if (sessionUser.isEmpty() || !sessionUser.equals(requestedUser)) {
            redirect.addFlashAttribute("message", "You can only change your own password");
            return "redirect:/userUpdate";
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            redirect.addFlashAttribute("message", "Please enter Password");
            return "redirect:/userUpdate";
        }
        if (!form.getPassword().equals(form.getCnewPassword())) {
            redirect.addFlashAttribute("message", "Password And Confirm Password Should be Same");
            return "redirect:/userUpdate";
        }
        users.updatePassword(sessionUser, CryptoUtil.encrypt(form.getPassword()));
        redirect.addFlashAttribute("message", "Your Password Successfully Updated");
        return "redirect:/userUpdate";
    }

    private boolean loggedIn(HttpSession session) {
        return session.getAttribute("userName") != null;
    }

    private String userName(HttpSession session) {
        Object value = session.getAttribute("userName");
        return value == null ? "" : value.toString();
    }
}
