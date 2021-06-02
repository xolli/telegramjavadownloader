import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;

public class TestScriptAdminUser implements Runnable {
    private final String mode;

    public TestScriptAdminUser(String mode) {
        this.mode = mode;
    }

    @Override
    public void run() {
        String line = "python3 testfiles/checktest.py " + mode + " adminuser.txt";
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        try {
            executor.execute(cmdLine);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
