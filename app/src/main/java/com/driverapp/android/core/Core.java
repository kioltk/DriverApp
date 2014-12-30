package com.driverapp.android.core;

import com.driverapp.android.R;
import com.driverapp.android.models.Category;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class Core {
    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(){{
            id = 1;
            name = "Разговорчик";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 2;
            name = "Ремонт дороги";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 3;
            name = "Авария";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 4;
            name = "Пробка";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 5;
            name = "ДПС";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 6;
            name = "Видеокамера";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 7;
            name = "Опасность";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 8;
            name = "Такси";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 9;
            name = "Автосервис";
            imgResId = R.drawable.category_placeholder;
        }});
        categories.add(new Category(){{
            id = 10;
            name = "Продаю авто";
            imgResId = R.drawable.category_placeholder;
        }});
        return  categories;
    }
}
