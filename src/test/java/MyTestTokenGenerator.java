import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.telegramdownloader.utils.TokenGenerator;

public class MyTestTokenGenerator {
    @Test
    public void testGenerateTokens() {
        Assert.assertEquals(TokenGenerator.generateToken().length(), 16);
    }
}
