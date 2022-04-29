package com.example.myproject.controllers;

import com.example.myproject.dao.PostDao;
import com.example.myproject.dao.UserDao;
import com.example.myproject.entity.Post;
import com.example.myproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.ListIterator;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao dao;
    @Autowired
    private PostDao daoPost;

    @GetMapping("/login")
    public String loginPage(){
        return "loginPage";
    }

    @GetMapping("/signup")
    public String signUpPage(Model m){
        m.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpPagePost(@Valid User user, BindingResult binding){
        if(binding.hasErrors()){
            return "signup";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal){
        m.addAttribute("user", dao.findByUsername(principal.getName()));
        m.addAttribute("postOfUser", daoPost.findAllByUser(dao.findByUsername(principal.getName())));
        return "profile";
    }

    @GetMapping("/edit")
    public String editUser(Model m, Principal principal) {
        m.addAttribute("user", dao.findByUsername(principal.getName()));
        System.out.println("OKKKK");
        return "edituser";
    }

    @PostMapping("/edit")
    public String editUser(@Valid User user,BindingResult binding, Principal principal) {
        if(binding.hasErrors()){
            return "edituser";
        }

        User user1 = dao.findByUsername(principal.getName());
        user1.setSurname(user.getSurname());
        user1.setName(user.getName());
        if(user.getPassword() != ""){
            user1.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(user1.getUsername().equals(user.getUsername())){
            dao.save(user1);
            return "redirect:/profile";
        }else{
            user1.setUsername(user.getUsername());
            dao.save(user1);
            return "redirect:/logout";
        }

    }

    @GetMapping("/settings")
    public String settings(){
        return "settings";
    }

    @Transactional
    @GetMapping("/delete")
    public String delete(Principal principal) throws IOException {
        List<Post> posts = daoPost.findAllByUser(dao.findByUsername(principal.getName()));
        //List<String> images = new ArrayList<>();
        for(ListIterator<Post> iter = posts.listIterator(); iter.hasNext();){
            Post element = iter.next();
            Path path = FileSystems.getDefault().getPath("images/" + element.getImagePath());;
            Files.delete(path);
        }
        daoPost.deleteAllByUser(dao.findByUsername(principal.getName()));
        dao.delete(dao.findByUsername(principal.getName()));
        return "redirect:/logout";
    }
}
