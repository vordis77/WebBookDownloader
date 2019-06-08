package app.file;

import org.junit.Test;

import app.settings.Settings;
import app.settings.Settings.Book;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;

/**
 * Unit test for app.file.Saver.
 */
public class SaverTest {

    private final static String FILE_NAME = "test";

    /**
     * Chapters in format [0] = title, [1] = content.
     */
    private final static String[][] DUMMY_CHAPTERS = new String[][] { { "testTitle",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." },
            { "testTitle2",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." },
            { "testTitle3",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." }, };

    private final static String ENCODING = "UTF-8";

    private Saver saver;
    private File file;

    /**
     * Prepare for saver test.
     */
    @Before
    public void before() {
        saver = new Saver(FILE_NAME, ENCODING);
    }

    /**
     * Test txt saving.
     * @throws Throwable if any error or exception occured in test.
     */
    @Test
    public void testTxtSaver() throws Throwable {
        // set bookType in settings // TODO: {Vordis 2019-05-23 21:53:42} this needs to be rewritten, but it's on low priority
        Settings.Fields.bookType = Book.TXT;
        saver.createFile();
        // save all dummy data into file
        for (String[] chapter : DUMMY_CHAPTERS) {
            saver.saveToFile(chapter);
        }
        saver.closeFile();
        // now, we need to check that file exists and at least is not empty
        // best would be to compare saved data (read file) with the one in memory
        file = new File(FILE_NAME);
        assertTrue(file.exists());
        assertTrue(file.length() > 100); // it surely should have 100 chars
    }

    /**
     * Test epub saving.
     * @throws Throwable if any error or exception occured in test.
     */
    @Test
    public void testEpubSaver() throws Throwable {
        // set bookType in settings // TODO: {Vordis 2019-05-23 21:53:42} this needs to be rewritten, but it's on low priority
        Settings.Fields.bookType = Book.EPUB;
        saver.createFile();
        // save all dummy data into file
        for (String[] chapter : DUMMY_CHAPTERS) {
            saver.saveToFile(chapter);
        }
        saver.closeFile();
        // now, we need to check that file exists and at least is not empty
        // best would be to compare saved data (read file) with the one in memory
        file = new File(FILE_NAME);
        assertTrue(file.exists());
        assertTrue(file.length() > 100); // it surely should have 100 chars
    }

    /**
     * Test pdf saving.
     * @throws Throwable if any error or exception occured in test.
     */
    @Test
    public void testPDFSaver() throws Throwable {
        // set bookType in settings // TODO: {Vordis 2019-05-23 21:53:42} this needs to be rewritten, but it's on low priority
        Settings.Fields.bookType = Book.PDF;
        saver.createFile();
        // save all dummy data into file
        for (String[] chapter : DUMMY_CHAPTERS) {
            saver.saveToFile(chapter);
        }
        saver.closeFile();
        // now, we need to check that file exists and at least is not empty
        // best would be to compare saved data (read file) with the one in memory
        file = new File(FILE_NAME);
        assertTrue(file.exists());
        assertTrue(file.length() > 100); // it surely should have 100 chars
    }

    /**
     * Clean after saver test.
     * @throws Throwable if any error or exception occured in test.
     */
    @After
    public void after() throws Throwable {
        // remove created file.
        assertTrue(file.delete());
    }
}
