package ru.prusov.telegrambot.TB_HOH.settings;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.prusov.telegrambot.TB_HOH.controller.TelegramBotController;

@Component
public class BotInitializer {

    private final TelegramBotController bot;

    public BotInitializer(TelegramBotController bot) {
        this.bot = bot;
    }
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot(bot);
            bot.init();
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
