package ru.prusov.telegrambot.TB_HOH.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.prusov.telegrambot.TB_HOH.model.User;
import ru.prusov.telegrambot.TB_HOH.service.bot.BotService;
import ru.prusov.telegrambot.TB_HOH.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.prusov.telegrambot.TB_HOH.settings.BotConfig.BOT_NAME;
import static ru.prusov.telegrambot.TB_HOH.settings.BotConfig.BOT_TOKEN;
import static ru.prusov.telegrambot.TB_HOH.settings.constants.ButtonName.*;
import static ru.prusov.telegrambot.TB_HOH.settings.constants.MessageContent.INTRO;

@Component
public class TelegramBotController extends TelegramLongPollingBot {
    @Autowired
    private BotService botService;
    @Autowired
    private UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        }
    }

    public void init() {
        botService.init();
    }

    private void handleMessage(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (message.equals("/start")) {
            sendWelcomeMessage(chatId);
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setText(INTRO);
        message.setChatId(String.valueOf(chatId));
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardForWelcomeMessage();
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup createInlineKeyboardForWelcomeMessage() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton constructionButton = new InlineKeyboardButton();
        constructionButton.setText(CONSTRUCTION.getValue());
        constructionButton.setCallbackData(CONSTRUCTION.toString());

        InlineKeyboardButton engineeringButton = new InlineKeyboardButton();
        engineeringButton.setText(ENGINEERING.getValue());
        engineeringButton.setCallbackData(ENGINEERING.toString());

        InlineKeyboardButton automaticsButton = new InlineKeyboardButton();
        automaticsButton.setText(AUTOMATICS.getValue());
        automaticsButton.setCallbackData(AUTOMATICS.toString());

        row1.add(constructionButton);
        row2.add(engineeringButton);
        row3.add(automaticsButton);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createInlineKeyboardButton(String buttonName, String buttonCallbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallbackData);
        return button;
    }

    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        userService.saveUser(update);
        SendMessage response = botService.handleCallback(callbackData);
        response.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        try {
            execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
