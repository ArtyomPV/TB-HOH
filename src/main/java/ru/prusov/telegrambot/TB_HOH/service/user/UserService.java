package ru.prusov.telegrambot.TB_HOH.service.user;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.prusov.telegrambot.TB_HOH.model.User;


public interface UserService {
    void saveUser(Update update);


}
