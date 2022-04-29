package com.example.myproject.entity;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "Posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Pattern(regexp = "[a-zA-z0-9,.#%':;!?*@ ]{0,255}", message = "Enter valid caption")
    private String caption;
    private String imagePath;
    private Date pub_date;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;

    public Post (){

    }

    public Post (String caption, String imagePath, Date pub_date, User user){
        this.caption = caption;
        this.imagePath = imagePath;
        this.pub_date = pub_date;
        this.user = user;
    }

    public Post (String caption, String imagePath, Date pub_date){
        this.caption = caption;
        this.imagePath = imagePath;
        this.pub_date = pub_date;
    }

    public Post (String imagePath, Date pub_date){
        this.imagePath = imagePath;
        this.pub_date = pub_date;
    }

    public Post(Date pub_date, String caption){
        this.caption = caption;
        this.pub_date = pub_date;
    }

    public Post(Date pub_date){
        this.pub_date = pub_date;
    }

    public Post(String caption){
        this.caption = caption;
    }

    public Post(String imagePath, Date pub_date, User user){
        this.imagePath = imagePath;
        this.pub_date = pub_date;
        this.user = user;
    }
}
