package org.techtown.letseat.menu;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class MenuData{

    ArrayList<Menu> items = new ArrayList<>();

    Menu menu[] = new Menu[100];


    int p = 0;

    public ArrayList<Menu> getItems(ArrayList list) {



        for(int i = 0; i < list.size()/3; i++){
            menu[i+1] = new Menu((Bitmap) list.get(p),
                    list.get(p+1).toString(), (String) list.get(p+2));
            items.add(menu[i+1]);
            p += 3;
        }




        return items;
    }


}