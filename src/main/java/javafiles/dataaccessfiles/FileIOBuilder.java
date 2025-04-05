package javafiles.dataaccessfiles;

import javafiles.customexceptions.BadExtensionException;
import javafiles.customexceptions.PathNotFoundException;
import javafiles.customexceptions.ReadWriteException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An abstract class whose inheritors creates and returns versions of {@link FileIO}s.
 *
 * @author Dylan Browne
 */
public abstract class FileIOBuilder {
    /**
     * Creates and returns a new instance of a {@link FileIO} with the given path and mode.
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file that is created (read 'r' or write 'w').
     * @return the newly created {@link FileIO} instance.
     * @throws ReadWriteException when the {@link FileIO} is unable to be created.
     */
    protected abstract FileIO createFileIO(String path, char mode) throws ReadWriteException;

    /**
     * Returns a list o list of {@link String}s that correspond with the extensions of
     * files that can be created with the given mode. This means if a {@link FileIO} can
     * only read or only write, then it will only return the extensions when mode is the
     * correct value for reading or writing.
     *
     * @param mode A char representation of the mode associated with the extensions (read 'r' or write 'w').
     * @return an array of {@link String}s corresponding to the extensions that can be written with the mode.
     */
    protected abstract String[] getExtensions(char mode);

    protected final String[] EXTENSIONS;

    private static final List<FileIOBuilder> BUILDERS = new ArrayList<>();
    private static boolean instantiated = false;


    /**
     * Creates a new {@link FileIOBuilder} that creates {@link FileIO}s when the
     * extension on the path is in extensions.
     *
     * @param extensions The valid path extensions for creating a {@link FileIO} object.
     */
    protected FileIOBuilder(String[] extensions) {
        EXTENSIONS = extensions;
    }

    /**
     * Returns whether this {@link FileIO} can be created from the given extensions for
     * the given mode.
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file this is (read 'r' or write 'w').
     * @return Whether this {@link FileIO} ends with one of the valid extensions for this mode.
     */
    protected boolean buildable(String path, char mode) {
        for (String extension : getExtensions(mode)) {
            if (path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates and returns a new {@link FileIO} with parameters (path, mode).
     * Instead, returns null if the path does not have an appropriate extension.
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file being created (read 'r' or write 'w').
     * @return A new {@link FileIO} with parameters (path, mode).
     * @throws ReadWriteException When creating {@link FileIO}(path, mode) throws an exception.
     */
    private FileIO build(String path, char mode) throws ReadWriteException {
        if (buildable(path, mode)) {
            return createFileIO(path, mode);
        }
        return null;
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in extensions.
     *
     * @param extensions The available extensions of files that can be chosen.
     * @return The selected path to the file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     *
     * @author Christopher Engelhart
     */
    private static File selectFile(String[] extensions) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Choose File", extensions));
        int result = fileChooser.showOpenDialog(null);
        return result == 0 ? fileChooser.getSelectedFile() : null;
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in extensions.
     *
     * @param extensions The available extensions of files that can be chosen.
     * @return The selected path to the file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     */
    private static String selectFilePath(String[] extensions) {
        File file = selectFile(extensions);
        if (file == null) {return null;}
        return file.toString();
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in
     * that allows for the creation of a {@link FileIO} in the given mode
     * with one or more of the {@link FileIOBuilder}s in BUILDERS.
     *
     * @return The selected path to the file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     */
    public static String selectFilePath(char mode) {
        List<String> buildersExtensions = new ArrayList<>();
        for (FileIOBuilder builder : BUILDERS) {
            String[] extensions = builder.getExtensions(mode);
            buildersExtensions.addAll(Arrays.asList(extensions));
        }
        String[] extensions = new String[buildersExtensions.size()];
        for (int i = 0; i < extensions.length; i++) {extensions[i] = buildersExtensions.get(i);}
        return selectFilePath(extensions);
    }

    /**
     * Creates and returns the appropriate type of {@link FileIO} from the given path
     * and mode. If the creation of the {@link FileIO} is invalid, throws a new
     * {@link ReadWriteException} (such as through invalid path, mode, or overriding
     * Maven files).
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file that is created (read 'r' or write 'w').
     * @return The new instance of the {@link FileIO} created.
     * @throws ReadWriteException When the creation of the {@link FileIO} is invalid.
     */
    public static FileIO buildNewFileIO(String path, char mode) throws ReadWriteException {
        mode = FileIO.getLowercaseMode(mode);
        File file = new File(path);
        if (!file.exists() && mode == 'r') {
            String reason = "Path: \"" + path + "\" does not exist, so it can't be read.";
            PathNotFoundException cause = new PathNotFoundException(reason);
            throw new ReadWriteException(cause);
        }

        for (FileIOBuilder builder : BUILDERS) {
            FileIO fileIO = builder.build(path, mode);
            if (fileIO != null) {
                return fileIO;
            }
        }
        BadExtensionException cause = new BadExtensionException("Extension for \"" + path + " is invalid.");
        throw new ReadWriteException(cause);
    }

    /**
     * Creates and adds the {@link FileIOBuilder}s to a list of {@link FileIOBuilder}s
     * that will be used to build the appropriate type of {@link FileIO}. Function must
     * be called before calling selectFilePath as selectFilePath needs this as setup.
     */
    public static void setupFileIOBuilders() {
        if (!instantiated) {
            BUILDERS.add(new JSONIOBuilder(new String[]{"json"}));
            BUILDERS.add(new XMLIOBuilder(new String[]{"xml"}));
            instantiated = true;
        }
    }

}
