package ru.prusov.telegrambot.TB_HOH.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "user_name", nullable = true)
    private String name;



    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

//    @Column(name = "interaction_date",nullable = false)
//    private LocalDate interactionDate;

    @Column(name = "interaction_date_time",nullable = false)
    private LocalDateTime interactionDateTime;

    public User(Long telegramId, String name, LocalDateTime interactionDateTime, String firstName, String lastName ){
        this.telegramId = telegramId;
        this.name = name;
        this.interactionDateTime = interactionDateTime;
         this.lastName = lastName;
       this.firstName = firstName;
        }

}
