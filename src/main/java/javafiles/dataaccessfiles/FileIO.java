package javafiles.dataaccessfiles;

import javafiles.customexceptions.BadCharException;
import javafiles.customexceptions.ReadWriteException;

import java.io.File;

/**
 * An abstract class whose inheritors reads and writes to different file types.
 *
 * @author Dylan Browne
 */
public abstract class FileIO {
    protected File file;
    protected char mode;
    /**
     * Creates or opens a {@link File} with name path in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of {@link File}s, write mode allows for the
     * writing, but not reading of {@link File}s.
     *
     * @param path The path of the {@link File} to be opened or created.
     * @param mode A char representation of the type of {@link File} this is (read 'r' or write 'w').
     * @throws ReadWriteException Thrown if the mode is an invalid char.
     */
    public FileIO(String path, char mode) throws ReadWriteException {
        // File is assumed to have passed FileIO.buildable with the correct extensions.
        this.mode = getLowercaseMode(mode);
        file = new File(path);
    }

    /**
     * Takes a char and converts it to an appropriate lowercase version of the mode
     *
     * @param mode A char representation of the type of file this is to be converted to correct form
     * @return The correct form of the char representation (read 'r', write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    public static char getLowercaseMode(char mode)  throws ReadWriteException{
        mode = Character.toLowerCase(mode);
        char[] valid_modes = {'r', 'w'};
        for (char mode_lower : valid_modes) {
            if (mode == mode_lower) {
                return mode;
            }
        }

        String reason = "Mode '" + mode + "' is not in {'r', 'R', 'w', 'W'}, file not opened.";
        BadCharException cause = new BadCharException(reason);
        throw new ReadWriteException(cause);
    }

    @Override
    public String toString() {return "Opened file \"" + file.toString() + "\" in (" + mode + ") mode.";}
}
