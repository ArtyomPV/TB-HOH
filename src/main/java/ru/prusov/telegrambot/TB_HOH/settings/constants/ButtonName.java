package ru.prusov.telegrambot.TB_HOH.settings.constants;

public enum ButtonName {
    CONSTRUCTION("Строительство домов"),
    ENGINEERING("Инженерные сети"),
    AUTOMATICS("Автоматика"),
    CONSTRUCTION_CHAPTER_FINISHED_PROJECT("Реализованные объекты"),
    CONSTRUCTION_CHAPTER_ARTICLE("Полезные статьи"),
    CONSTRUCTION_CHAPTER_CONTACTS("Связаться со специалистом"),
    CONSTRUCTION_CHAPTER_BACK("Назад");

    private final String value;

    ButtonName(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
