package ru.prusov.telegrambot.TB_HOH.settings.constants;

public enum ButtonName {
    CONSTRUCTION("Строительство домов"),
    ENGINEERING("Инженерные сети"),
    AUTOMATICS("Автоматика");

    private final String value;

    ButtonName(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
