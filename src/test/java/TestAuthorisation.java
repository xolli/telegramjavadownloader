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
        TestScript testScript = new TestScript("simpleuserauth");
        Thread simpleTestUserAuthThread = new Thread(testScript);
        simpleTestUserAuthThread.start();
        DownloaderBot bot = TestScript.intiBot();
        Controller controller = new Controller();
        controller.setBot(bot);
        Authorisation authorisation = new Authorisation(1483105750L, controller);
        authorisation.authorisation();
        simpleTestUserAuthThread.join(10000L);
    }

    @Test
    public void testStart() throws TelegramApiException, InterruptedException {
        TestScript testScript = new TestScript("startchat");
        Thread startChatTestThread = new Thread(testScript);
        startChatTestThread.start();
        DownloaderBot bot = TestScript.intiBot();
        Controller controller = new Controller();
        controller.setBot(bot);
        Start start = new Start(1483105750L, controller);
        start.recv(new Update());
        startChatTestThread.join(10000L);
    }

}
