package com.driverapp.android.core;

import com.driverapp.android.R;
import com.driverapp.android.models.EventCategory;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class Core {
    public static ArrayList<EventCategory> categories() {
        ArrayList<EventCategory> categories = new ArrayList<>();
        categories.add(new EventCategory(){{
            id = 1;
            name = "Разговорчик";
            imgResId = R.drawable.ic_category_speak;
            pinResId = R.drawable.ic_category_pin_speak;
        }});
        categories.add(new EventCategory(){{
            id = 2;
            name = "Ремонт дороги";
            imgResId = R.drawable.ic_category_repair;
            pinResId = R.drawable.ic_category_pin_repair;
        }});
        categories.add(new EventCategory(){{
            id = 3;
            name = "Авария";
            imgResId = R.drawable.ic_category_disaster;
            pinResId = R.drawable.ic_category_pin_disaster;
        }});
        categories.add(new EventCategory(){{
            id = 4;
            name = "Пробка";
            imgResId = R.drawable.ic_category_traffic;
            pinResId = R.drawable.ic_category_pin_traffic;
        }});
        categories.add(new EventCategory(){{
            id = 5;
            name = "ДПС";
            imgResId = R.drawable.ic_category_police;
            pinResId = R.drawable.ic_category_pin_police;
        }});
        categories.add(new EventCategory(){{
            id = 6;
            name = "Видеокамера";
            imgResId = R.drawable.ic_category_camera;
            pinResId = R.drawable.ic_category_pin_camera;
        }});
        categories.add(new EventCategory(){{
            id = 7;
            name = "Нужна помощь";
            imgResId = R.drawable.ic_category_dangerous;
            pinResId = R.drawable.ic_category_pin_dangerous;
        }});
        categories.add(new EventCategory(){{
            id = 8;
            name = "Такси";
            imgResId = R.drawable.ic_category_taxi;
            pinResId = R.drawable.ic_category_pin_taxi;
        }});
        categories.add(new EventCategory(){{
            id = 9;
            name = "Автосервис";
            imgResId = R.drawable.ic_category_service;
            pinResId = R.drawable.ic_category_pin_service;
        }});
        categories.add(new EventCategory(){{
            id = 10;
            name = "Продаю авто";
            imgResId = R.drawable.ic_category_auto;
            pinResId = R.drawable.ic_category_pin_auto;
        }});
        return  categories;
    }
}
