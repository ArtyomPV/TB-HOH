package ru.prusov.telegrambot.TB_HOH.service.callbackDataHandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CallbackHandler {
    SendMessage handle(String callbackData);
}
