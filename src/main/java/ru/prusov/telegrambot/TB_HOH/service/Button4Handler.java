package ru.prusov.telegrambot.TB_HOH.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class Button4Handler implements CallbackHandler{
    @Override
    public SendMessage handle(String callbackData) {
        SendMessage response = new SendMessage();
        response.setText("Вы нажали кнопку 4");
        return response;
    }
}
