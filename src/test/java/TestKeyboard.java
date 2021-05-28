import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.telegramdownloader.buttons.KeyboardAdminMenu;
import ru.nsu.fit.telegramdownloader.buttons.KeyboardUserMenu;

public class TestKeyboard {

    @Test
    public void UserKeyboard(){
        KeyboardUserMenu keyboardUserMenu = new KeyboardUserMenu();
        Assert.assertEquals(3,keyboardUserMenu.getKeyboard().getKeyboard().size());

        KeyboardAdminMenu keyboardAdminMenu = new KeyboardAdminMenu();
        Assert.assertEquals(5,keyboardAdminMenu.getKeyboard().getKeyboard().size());
    }
}
