package dev.basmathy.link_reducer;


public class Config {
    public static final long expirationTime = 86400; // время жизни ссылки, 24 часа
    public static final int clicksNumber = 10; // макс количество активации ссылки
    public static final byte shortLinkLength = 6; // длина короткой ссылки без clck.ru/
    public static final byte uuidIndex = 0;
    public static final byte longLinkIndex = 1;
    public static final byte expirationTimeIndex = 2;
    public static final byte lastClicksIndex = 3;
    public static final String elementsLine = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String fileName = "data.txt";
}