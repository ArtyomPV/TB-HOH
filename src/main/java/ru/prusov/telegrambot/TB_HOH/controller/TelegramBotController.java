package ru.prusov.telegrambot.TB_HOH.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.prusov.telegrambot.TB_HOH.service.BotService;
import ru.prusov.telegrambot.TB_HOH.settings.BotConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.prusov.telegrambot.TB_HOH.settings.BotConfig.BOT_NAME;
import static ru.prusov.telegrambot.TB_HOH.settings.BotConfig.BOT_TOKEN;

@Component
public class TelegramBotController extends TelegramLongPollingBot {
    @Autowired
    private BotService botService;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            handleCallbackQuery(update);
        } else if (update.hasMessage() && update.getMessage().hasText()){
            handleMessage(update);
        }
    }

    private void handleMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Выберите кнопку");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton button1 = createInlineKeyboardButton("Button 1", "button1");
        InlineKeyboardButton button2 = createInlineKeyboardButton("Button 2", "button2");
        InlineKeyboardButton button3 = createInlineKeyboardButton("Button 3", "button3");
        InlineKeyboardButton button4 = createInlineKeyboardButton("Button 4", "button4");

        List<InlineKeyboardButton> row1 = Arrays.asList(
                button1,
                button2
        );
        List<InlineKeyboardButton> row2 = Arrays.asList(
                button3,
                button4
        );

        rows.add(row1);
        rows.add(row2);

        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private InlineKeyboardButton createInlineKeyboardButton(String buttonName, String buttonCallbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallbackData);
        return button;
    }


    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        SendMessage response = botService.handleCallback(callbackData);
        response.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        try{
            execute(response);
        } catch (Exception e){
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
