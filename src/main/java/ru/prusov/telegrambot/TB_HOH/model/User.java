package ru.prusov.telegrambot.TB_HOH.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "interaction_date",nullable = false)
    private LocalDate interactionDate;

    public User(Long telegramId, String name, LocalDate interactionDate){
        this.telegramId = telegramId;
        this.name = name;
        this.interactionDate = interactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public LocalDate getInteractionDate() {
        return interactionDate;
    }

    public void setInteractionDate(LocalDate interactionDate) {
        this.interactionDate = interactionDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
