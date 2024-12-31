package ru.prusov.telegrambot.TB_HOH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.prusov.telegrambot.TB_HOH.service.callbackDataHandlers.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotService {
    private final Map<String, CallbackHandler> handlers = new HashMap<>();
    @Autowired
    private Button1Handler button1Handler;
    @Autowired
    private Button2Handler button2Handler;
    @Autowired
    private Button3Handler button3Handler;
    @Autowired
    private Button4Handler button4Handler;


    public void init(){
        handlers.put("button1", button1Handler);
        handlers.put("button2", button2Handler);
        handlers.put("button3", button3Handler);
        handlers.put("button4", button4Handler);
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
