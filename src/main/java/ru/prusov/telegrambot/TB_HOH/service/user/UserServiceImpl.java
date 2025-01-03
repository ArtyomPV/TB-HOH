package ru.prusov.telegrambot.TB_HOH.service.user;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.prusov.telegrambot.TB_HOH.model.User;
import ru.prusov.telegrambot.TB_HOH.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void saveUser(Update update) {
        Long telegramId = update.getCallbackQuery().getFrom().getId();
        String userName = update.getCallbackQuery().getFrom().getUserName();
        String firstName = update.getCallbackQuery().getFrom().getFirstName();
        String lastName = update.getCallbackQuery().getFrom().getLastName();
        User user = new User(telegramId, userName, LocalDateTime.now(), firstName, lastName);
        userRepository.save(user);
    }
}
