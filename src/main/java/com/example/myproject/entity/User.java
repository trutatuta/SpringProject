package com.example.myproject.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_user;
    @NotNull
    @Pattern(regexp = "[a-zA-z]{2,30}", message = "Enter valid name")
    private String name;
    @NotNull
    @Pattern(regexp = "[a-zA-z]{2,30}",
            message = "Enter valid surname")
    private String surname;
    @NotNull
    @Pattern(regexp = "[a-zA-z0-9]{2,30}",
            message = "Enter valid username")
    private String username;


    private String password;

    public User(){

    }

    public User(String name, String surname, String username, String password){
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }
}
