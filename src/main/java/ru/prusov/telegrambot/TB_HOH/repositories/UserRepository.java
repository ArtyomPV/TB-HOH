package ru.prusov.telegrambot.TB_HOH.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.telegrambot.TB_HOH.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
