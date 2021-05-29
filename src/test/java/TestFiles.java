import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class TestFiles {
    @Test
    public void testMkDir() {
        String testDirName = "testDirectory";
        if (FilesUtils.mkDir(testDirName)) {
            File f = new File(testDirName);
            Assert.assertTrue(f.isDirectory());
            f.delete();
        }
    }

    @Test
    public void testReadMapFileSL() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "jdsfkjdsfkj.txt";
        writeTextToFile (testFilename, "sometext:213\nsometext2:2133\n");
        HashMap<String, Long> testResult = FilesUtils.readMapFileSL(testFilename);
        File file = new File(testFilename);
        file.delete();
        Assert.assertTrue(testResult.get("sometext").equals(213L) && testResult.get("sometext2").equals(2133L));
    }

    @Test
    public void testReadMapFileSLDoesntExist() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "thisfiledoentexist_sasadsa";
        File file = new File(testFilename);
        file.delete();
        HashMap<String, Long> testResult = FilesUtils.readMapFileSL(testFilename);
        Assert.assertTrue(testResult.isEmpty());
    }

    @Test
    public void testReadMapFileSLRightsError() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "testfileRights.txt";
        writeTextToFile (testFilename, "sometext:213\nsometext2:2133\n");
        File file = new File(testFilename);
        file.setReadable(false);
        HashMap<String, Long> testResult = FilesUtils.readMapFileSL(testFilename);
        Assert.assertTrue(testResult.isEmpty());
        file.delete();
    }

    @Test
    public void testReadMapFileLL() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "jdsfkjdsfkjasdas.txt";
        writeTextToFile (testFilename, "111:222\n333:444\n");
        HashMap<Long, Long> testResult = FilesUtils.readMapFileLL(testFilename);
        Assert.assertTrue(testResult.get(111L).equals(222L) && testResult.get(333L).equals(444L));
        File file = new File(testFilename);
        file.delete();
    }

    @Test
    public void testReadMapFileLLDoesntExist() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "thisfiledoentexist_sfkjdsfkasdja";
        File file = new File(testFilename);
        file.delete();
        HashMap<Long, Long> testResult = FilesUtils.readMapFileLL(testFilename);
        Assert.assertTrue(testResult.isEmpty());
    }

    @Test
    public void testReadMapFileLLRightsError() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "testfileRights_jksaksa.txt";
        writeTextToFile (testFilename, "111:222\n111:222\n");
        File file = new File(testFilename);
        file.setReadable(false);
        HashMap<Long, Long> testResult = FilesUtils.readMapFileLL(testFilename);
        Assert.assertTrue(testResult.isEmpty());
        file.delete();
    }

    @Test
    public void testHumanReadableByteCountBin() {
        Assert.assertEquals("0 B", FilesUtils.humanReadableByteCountBin(0));
        Assert.assertEquals("1.0 KiB", FilesUtils.humanReadableByteCountBin(1024));
        Assert.assertEquals("1.0 MiB", FilesUtils.humanReadableByteCountBin(1024 * 1024));
        Assert.assertEquals("1.0 GiB", FilesUtils.humanReadableByteCountBin(1024 * 1024 * 1024));
        Assert.assertEquals("1.0 TiB", FilesUtils.humanReadableByteCountBin(1024 * 1024 * 1024 * 1024L));
    }

    @Test
    public void testFileSize() throws IOException {
        String testFilename = "testFileSize_12mifo5.txt";
        writeTextToFile (testFilename, "000");
        Assert.assertEquals(3, FilesUtils.getFileSize(testFilename));
        File file = new File(testFilename);
        file.delete();
    }

    @Test
    public void testReadNumberSetFile() throws FileNotFoundException, UnsupportedEncodingException {
        String testFilename = "testReadNumberSetFile_122dws5.txt";
        writeTextToFile (testFilename, "1\n2\n3\n123\n789\n");
        HashSet<Long> testNumbers = FilesUtils.readNumberSetFile(testFilename);
        Assert.assertTrue(testNumbers.contains(1L));
        Assert.assertTrue(testNumbers.contains(2L));
        Assert.assertTrue(testNumbers.contains(3L));
        Assert.assertTrue(testNumbers.contains(123L));
        Assert.assertTrue(testNumbers.contains(789L));
        File file = new File(testFilename);
        file.delete();
    }

    @Test
    public void testReadNumberSetFileErrorExist() {
        String testFilename = "testReadNumberSetFile_123432.txt";
        File file = new File(testFilename);
        file.delete();
        HashSet<Long> testNumbers = FilesUtils.readNumberSetFile(testFilename);
        Assert.assertTrue(testNumbers.isEmpty());
    }

    private void writeTextToFile(String filename, String text) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        writer.print(text);
        writer.close();
    }
}
