import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.telegramdownloader.condition.Authorisation;
import ru.nsu.fit.telegramdownloader.utils.AuthorisationUtils;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;

import java.net.MalformedURLException;

public class TestUtils {
    @Test
    public void testGetName() throws MalformedURLException {
        Assert.assertEquals("name", UrlHandler.getFileName("https://example.com/name"));
        Assert.assertEquals("", UrlHandler.getFileName("https://example.com/"));
        Assert.assertEquals("", UrlHandler.getFileName("https://example.com"));
        Assert.assertEquals("", UrlHandler.getFileName("https://example.com"));
        Assert.assertEquals("name", UrlHandler.getFileName("https://example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com:8080/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com:80/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://login@example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://login:password@example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com/path/name?key=val"));
    }

    @Test
    public void TestAuthorisationIsTrustedUser() {
        AuthorisationUtils authorisationUtils = AuthorisationUtils.getInstance();
        authorisationUtils.addToken("123",(long)455);
        Assert.assertTrue(authorisationUtils.isTrustedUser((long) 455));
        Assert.assertFalse(authorisationUtils.isTrustedUser((long) 4));
    }
    @Test
    public void TestAuthorisationIsToken() {
        AuthorisationUtils authorisationUtils = AuthorisationUtils.getInstance();
        authorisationUtils.addToken("123",(long)455);
        Assert.assertTrue(authorisationUtils.isToken("123"));
        Assert.assertFalse(authorisationUtils.isToken("1"));
    }
    @Test
    public void TestAuthorisationUnusedToken() {
        AuthorisationUtils authorisationUtils = AuthorisationUtils.getInstance();
        authorisationUtils.generateToken("321");
        Assert.assertTrue(authorisationUtils.unusedToken("321"));
        Assert.assertFalse(authorisationUtils.unusedToken("1"));
        Assert.assertFalse(authorisationUtils.unusedToken("123"));
    }


}
