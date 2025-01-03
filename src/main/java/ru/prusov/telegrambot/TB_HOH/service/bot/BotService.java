package ru.prusov.telegrambot.TB_HOH.service.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.prusov.telegrambot.TB_HOH.model.User;
import ru.prusov.telegrambot.TB_HOH.repositories.UserRepository;
import ru.prusov.telegrambot.TB_HOH.service.callbackDataHandlers.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static ru.prusov.telegrambot.TB_HOH.settings.constants.ButtonName.*;

@Service
public class BotService {


    private final Map<String, CallbackHandler> handlers = new HashMap<>();

    @Autowired
    private ConstructionChapterHandler constructionChapterHandler;

    public void init(){
        handlers.put(CONSTRUCTION.toString(), constructionChapterHandler);
    }
    public SendMessage handleCallback(String callbackData){


        CallbackHandler handler = handlers.get(callbackData);
        if(handler != null){
            return handler.handle(callbackData);
        } else {
            SendMessage response = new SendMessage();
            response.setText("Unknown command");
            return response;
        }

    }
}
