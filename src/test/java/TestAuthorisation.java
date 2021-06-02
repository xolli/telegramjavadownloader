import org.junit.Assert;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.condition.Authorisation;
import ru.nsu.fit.telegramdownloader.condition.Start;
import ru.nsu.fit.telegramdownloader.utils.AuthorisationUtils;
import ru.nsu.fit.telegramdownloader.utils.TokenGenerator;

public class TestAuthorisation {
    @Test
    public void testSimpleUser() throws TelegramApiException, InterruptedException {
        AuthorisationUtils authorisationUtils = AuthorisationUtils.getInstance();
        if (!authorisationUtils.isTrustedUser(1483105750L)) {
            authorisationUtils.addToken(TokenGenerator.generateToken(), 1483105750L);
        }
        TestScriptSimpleUser testScriptSimpleUser = new TestScriptSimpleUser("simpleuserauth");
        Thread simpleTestUserAuthThread = new Thread(testScriptSimpleUser);
        simpleTestUserAuthThread.start();
        DownloaderBot bot = TestScriptSimpleUser.intiBot();
        Controller controller = new Controller();
        controller.setBot(bot);
        Authorisation authorisation = new Authorisation(1483105750L, controller);
        authorisation.authorisation();
        simpleTestUserAuthThread.join(10000L);
        if (simpleTestUserAuthThread.isAlive()) {
            Assert.fail("The client did not receive a message");
        }
    }

    @Test
    public void testStart() throws TelegramApiException, InterruptedException {
        TestScriptSimpleUser testScriptSimpleUser = new TestScriptSimpleUser("startchat");
        Thread startChatTestThread = new Thread(testScriptSimpleUser);
        startChatTestThread.start();
        DownloaderBot bot = TestScriptSimpleUser.intiBot();
        Controller controller = new Controller();
        controller.setBot(bot);
        Start start = new Start(1483105750L, controller);
        start.recv(new Update());
        startChatTestThread.join(10000L);
        if (startChatTestThread.isAlive()) {
            Assert.fail("The client did not receive a message");
        }
    }

    @Test
    public void testAdmin() throws TelegramApiException, InterruptedException {
        AuthorisationUtils authorisationUtils = AuthorisationUtils.getInstance();
        if (!authorisationUtils.isTrustedUser(1803832607L) || !authorisationUtils.isAdmin(1803832607L)) {
            return;
        }
        TestScriptSimpleUser testScriptSimpleUser = new TestScriptSimpleUser("adminuserauth");
        Thread simpleTestUserAuthThread = new Thread(testScriptSimpleUser);
        simpleTestUserAuthThread.start();
        DownloaderBot bot = TestScriptSimpleUser.intiBot();
        Controller controller = new Controller();
        controller.setBot(bot);
        Authorisation authorisation = new Authorisation(1483105750L, controller);
        authorisation.authorisation();
        simpleTestUserAuthThread.join(10000L);
        if (simpleTestUserAuthThread.isAlive()) {
            Assert.fail("The client did not receive a message");
        }
    }

}
