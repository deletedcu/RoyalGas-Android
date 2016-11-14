package dmg.com.rg.util;

import java.util.ArrayList;
import java.util.List;

import dmg.com.rg.model.MyMenu;

/**
 * Created by Star on 11/14/16.
 */

public class ShardData {

    private static ShardData instance = null;
    private List<MyMenu> menuList;

    private ShardData() {
        menuList = new ArrayList<>();
    }

    public static ShardData getInstance() {
        if (instance == null) {
            instance = new ShardData();
        }
        return instance;
    }

    public List<MyMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MyMenu> menuList) {
        this.menuList = menuList;
    }
}
