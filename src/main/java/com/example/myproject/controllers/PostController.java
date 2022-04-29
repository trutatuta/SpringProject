package com.example.myproject.controllers;

import com.example.myproject.dao.PostDao;
import com.example.myproject.dao.UserDao;
import com.example.myproject.entity.Post;
import com.example.myproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostDao dao;

    @Autowired
    private UserDao daoUser;

    @GetMapping("/")
    public String first(){
        return "redirect:/profile";
    }

    @GetMapping("/newpost")
    public String addPost(Model m){
        m.addAttribute("post", new Post());
        return "addpost";
    }

    @PostMapping("/newpost")
    public String addPost(@Valid Post post, BindingResult binding, @RequestParam("image") MultipartFile multipartFile, Principal principal) throws IOException {
        if(binding.hasErrors()){
            return "addpost";
        }

        User user = daoUser.findByUsername(principal.getName());

        post.setUser(user);
        post.setImagePath(multipartFile.getOriginalFilename().replaceAll(" ", ""));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Date now = new Date();
        post.setPub_date(now);

        dao.save(post);

        File upl = new File("images/" + multipartFile.getOriginalFilename().replaceAll(" ", ""));
        upl.createNewFile();
        FileOutputStream fout = new FileOutputStream(upl);
        fout.write(multipartFile.getBytes());
        fout.close();

        return "redirect:/profile";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model m, Principal principal){
        Optional<Post> postOptional = dao.findById(id);
        m.addAttribute("user", daoUser.findByUsername(principal.getName()));
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            m.addAttribute("post", post);
        }
        //m.addAttribute("post", post);
        return "details";
    }

    @GetMapping("/detailsup/{id}")
    public String detailsUp(@PathVariable("id") Integer id, Model m, Principal principal){
        Optional<Post> postOptional = dao.findById(id);
        m.addAttribute("user", daoUser.findByUsername(principal.getName()));
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            m.addAttribute("post", post);
        }
        //m.addAttribute("post", post);
        return "details_up";
    }


    @GetMapping("/home")
    public String allPosts(Model m){
        m.addAttribute("posts", dao.findAll());
        return "home";
    }

    @GetMapping("/deletepost/{id}")
    public String deletePost(@PathVariable("id") Integer id, Principal principal) throws IOException{
        User user = daoUser.findByUsername(principal.getName());
        Post post = dao.findById(id).get();
        if(user.getId_user().equals(post.getUser().getId_user())){
            Path path = FileSystems.getDefault().getPath("images/" + post.getImagePath());;
            Files.delete(path);
            dao.delete(post);
        }
        return "redirect:/profile";
    }

    @GetMapping("/editpost/{id}")
    public String editPost(@PathVariable("id") Integer id, Principal principal, Model m){
        User user = daoUser.findByUsername(principal.getName());
        Post post = dao.findById(id).get();
        if(user.getId_user().equals(post.getUser().getId_user())){
            m.addAttribute("post", post);
            return "editpost";
        }else{
            return "redirect:/profile";
        }
    }

    @PostMapping("/editpost/{id}")
    public String editPost(@PathVariable("id") Integer id, @Valid Post post, BindingResult binding, Principal principal){
        if(binding.hasErrors()){
            return "editpost";
        }
        Post post1 = dao.findById(post.getId()).get();

        post1.setCaption(post.getCaption());

        /*Date now = new Date();
        post1.setPub_date(now);*/
        dao.save(post1);

        return "redirect:/profile";
    }

}
