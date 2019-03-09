package no.carlhen.kioskbot;

import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Menu extends GenericModel<Menu>{
    public String name;
    public String description;
    public List<Item> items;

    public Menu(String menuName, String menuDescription, List<Item> menuItems){
        id = RandomString.GenerateUniqueId(GetMenusList());
        name = menuName;
        description = menuDescription;
        items = menuItems;
    }

    // Getters and Setters

    public Item getItemByID(String itemID){
        for (Item item : items){
            if (item.id.equals(itemID)){
                return item;
            }
        }
        return null;
    }

    // Overrides

    @Override
    public boolean isSame(Menu object){
        boolean same = (
                id.equals(object.id) &&
                name.equals(object.name) &&
                description.equals(object.description)
        );
        if (same){
            for (int i = 0; i < items.size(); i++){
                if (!(items.get(i).sameItem(object.items.get(i)))){
                    return false;
                }
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public String toString(){
        return name;
    }

    // Static Methods

    private static final String fileName = "menus.json";
    private static final TypeToken<List<Menu>> TypeToken = new TypeToken<List<Menu>>(){};

    private static List<Menu> menus = GetMenusList();

    public static List<Menu> GetMenusList() {
        if (menus == null)
            return Storage.GetList(fileName, TypeToken);
        else
            return menus;
    }

    public static void AddNewMenu(Menu menu){
        menus.add(menu);
        Storage.SaveList(menus,fileName);
    }

    public static void DeleteMenu(Menu menu){
        for (Menu currMenu : menus)
            if (menu.isSame(currMenu)) {
                menus.remove(currMenu);
                Storage.SaveList(menus,fileName);
                return;
            }
    }

    public static void UpdateMenu(Menu menu){
        for (int i = 0; i < menus.size(); i++)
            if (menu.id.equals(menus.get(i).id)){
                menus.set(i,menu);
                Storage.SaveList(menus, fileName);
            }
    }

    // Current Menu Methods

    private static final String currMenuFileName = "currentMenu.txt";
    private static String MenuInUseID = LoadCurrentMenu();

    public static Menu GetCurrentMenu(){
        for (Menu menu : GetMenusList())
            if (menu.id.equals(MenuInUseID))
                return menu;
        return null;
    }

    private static String LoadCurrentMenu(){
        if (MenuInUseID == null)
            return Storage.ReadFile(currMenuFileName);
        else
            return MenuInUseID;
    }

    public static void SetCurrentMenu(Menu menu){
        MenuInUseID = menu.id;
        Storage.SaveFile(MenuInUseID, currMenuFileName);
    }
}
