package ru.prusov.telegrambot.TB_HOH.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class BotService {
    public SendMessage handleCallback(String callbackData){
        SendMessage response = new SendMessage();
        switch (callbackData){
            case "button1":
                response.setText("Pressed btn 1");
                break;
            case "button2":
                response.setText("Pressed btn 2");
                break;
            case "button3":
                response.setText("Pressed btn 3");
                break;
            case "button4":
                response.setText("Pressed btn 4");
                break;
            default:
                response.setText("Unknown command");
        }
        return response;
    }
}
