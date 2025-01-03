package ru.prusov.telegrambot.TB_HOH.service.callbackDataHandlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static ru.prusov.telegrambot.TB_HOH.settings.constants.ButtonName.*;
import static ru.prusov.telegrambot.TB_HOH.settings.constants.MessageContent.CONSTRUCTION_MENU;

@Component
public class ConstructionChapterHandler implements CallbackHandler{
    @Override
    public SendMessage handle(String callbackData) {
        SendMessage response = new SendMessage();
        response.setText(CONSTRUCTION_MENU);
        InlineKeyboardMarkup keyboardMarkup = createInlineKeyBoard();
        response.setReplyMarkup(keyboardMarkup);
        return response;
    }

    private InlineKeyboardMarkup createInlineKeyBoard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        InlineKeyboardButton constructionChapterFinishedProjectsButton = new InlineKeyboardButton(CONSTRUCTION_CHAPTER_FINISHED_PROJECT.getValue());
        constructionChapterFinishedProjectsButton.setCallbackData(CONSTRUCTION_CHAPTER_FINISHED_PROJECT.toString());

        System.out.println(constructionChapterFinishedProjectsButton.getCallbackData());

        InlineKeyboardButton constructionChapterArticleButton = new InlineKeyboardButton(CONSTRUCTION_CHAPTER_ARTICLE.getValue());
        constructionChapterArticleButton.setCallbackData(CONSTRUCTION_CHAPTER_ARTICLE.toString());

        InlineKeyboardButton constructionChapterContactUsButton = new InlineKeyboardButton(CONSTRUCTION_CHAPTER_CONTACTS.getValue());
        constructionChapterContactUsButton.setCallbackData(CONSTRUCTION_CHAPTER_CONTACTS.toString());

        InlineKeyboardButton constructionChapterBackButton = new InlineKeyboardButton(CONSTRUCTION_CHAPTER_BACK.getValue());
        constructionChapterBackButton.setCallbackData(CONSTRUCTION_CHAPTER_BACK.name());

        row1.add(constructionChapterFinishedProjectsButton);
        row2.add(constructionChapterArticleButton);
        row3.add(constructionChapterContactUsButton);
        row4.add(constructionChapterBackButton);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
}
