package com.driverapp.android.models;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventCategory {
    public long id;
    public String name;
    public int imgResId;
    public int pinResId;

    public static enum EventCategoryType{
        SPEAK,      // разговорчик
        REPAIR,     // ремонт
        DISASTER,   // авария
        TRAFFIC,    // пробка
        POLICE,     // ДПС
        CAMERA,     // видеокамера
        DANGEROUS,  // нужна помощь
        TAXI,       // такси
        SERVICE,    // автосервис
        AUTO        // продажа авто
    }

}
