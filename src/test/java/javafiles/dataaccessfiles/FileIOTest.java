package javafiles.dataaccessfiles;

import javafiles.customexceptions.BadCharException;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileIOTest {
//    // Expected: FileIO.getLowercaseMode() throws a ReadWriteException.
//    @Test
//    void getLowercaseModeBadChar() {
//        char ch = 'x';
//        String reason = "Mode '" + ch + "' is not in {'r', 'R', 'w', 'W'}, file not opened.";
//        try {
//            FileIO.getLowercaseMode(ch);
//            fail("No ReadWriteException");
//        } catch (ReadWriteException e) {
//            BadCharException cause = new BadCharException(reason);
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//
//    }
//
//    // Expected: FileIO.getLowercaseMode() returns lowercase 'r'.
//    @Test
//    void getLowercaseModeGoodLowerR() {
//        char ch = 'r';
//        char ans;
//        try {
//            ans = FileIO.getLowercaseMode(ch);
//            assertEquals(ch, ans);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//    }
//
//    // Expected: FileIO.getLowercaseMode() returns lowercase 'w'.
//    @Test
//    void getLowercaseModeGoodLowerW() {
//        char ch = 'w';
//        char ans;
//        try {
//            ans = FileIO.getLowercaseMode(ch);
//            assertEquals(ch, ans);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//    }
//
//    // Expected: FileIO.getLowercaseMode() returns lowercase 'r'.
//    @Test
//    void getLowercaseModeGoodUpperR() {
//        char ch = 'R';
//        char ans;
//        try {
//            ans = FileIO.getLowercaseMode(ch);
//            assertEquals(Character.toLowerCase(ch), ans);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//    }
//
//    // Expected: FileIO.getLowercaseMode() returns lowercase 'w'.
//    @Test
//    void getLowercaseModeGoodUpperW() {
//        char ch = 'W';
//        char ans;
//        try {
//            ans = FileIO.getLowercaseMode(ch);
//            assertEquals(Character.toLowerCase(ch), ans);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//    }
//
}