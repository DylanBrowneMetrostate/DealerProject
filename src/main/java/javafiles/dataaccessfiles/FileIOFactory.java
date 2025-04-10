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

public class FileIOFactory {
    private static final List<FileIOReaderBuilder> READER_BUILDERS = new ArrayList<>();
    private static final List<FileIOWriterBuilder> WRITER_BUILDERS = new ArrayList<>();
    private static boolean instantiated = false;

    /**
     * Returns whether this {@link FileIO} can be created from the given extensions for
     * the given mode.
     *
     * @param path The path of the file to be opened or created.
     * @param extensions An array of valid extensions for the object.
     * @return Whether this {@link FileIO} ends with one of the valid extensions for this mode.
     */
    protected static boolean buildable(String path, String[] extensions) {
        for (String extension : extensions) {
            if (path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    protected static FileIO build(FileIOBuilder builder, String path, char mode) throws ReadWriteException{
        if (buildable(path, builder.getExtensions())) {
            if (mode == 'r' && builder instanceof FileIOReaderBuilder) {
                return ((FileIOReaderBuilder) builder).createFileIOReader(path);
            }
            if (mode == 'w' && builder instanceof FileIOWriterBuilder) {
                return ((FileIOWriterBuilder) builder).createFileIOWriter(path);
            }
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
        List<FileIOBuilder> builders = getBuilderList(mode);

        List<String> buildersExtensions = new ArrayList<>();
        for (FileIOBuilder builder : builders) {
            String[] extensions = builder.getExtensions();
            buildersExtensions.addAll(Arrays.asList(extensions));
        }
        String[] extensions = new String[buildersExtensions.size()];
        for (int i = 0; i < extensions.length; i++) {extensions[i] = buildersExtensions.get(i);}
        return selectFilePath(extensions);
    }

    private static List<FileIOBuilder> getBuilderList(char mode) {
        return new ArrayList<>(mode == 'w' ? WRITER_BUILDERS : READER_BUILDERS);
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
    private static FileIO buildNewFileIO(String path, char mode) throws ReadWriteException {
        mode = FileIO.getLowercaseMode(mode);

        List<FileIOBuilder> builders = getBuilderList(mode);

        for (FileIOBuilder builder : builders) {
            FileIO fileIO = build(builder, path, mode);
            if (fileIO != null) {
                return fileIO;
            }
        }
        BadExtensionException cause = new BadExtensionException("Extension for \"" + path + " is invalid.");
        throw new ReadWriteException(cause);
    }

    public static FileIOReader buildNewFileIOReader(String path) throws ReadWriteException {
        char mode = 'r';
        File file = new File(path);
        if (!file.exists()) {
            String reason = "Path: \"" + path + "\" does not exist, so it can't be read.";
            PathNotFoundException cause = new PathNotFoundException(reason);
            throw new ReadWriteException(cause);
        }
        try {
            return (FileIOReader) buildNewFileIO(path, mode);
        } catch (ClassCastException e) {
            throw new ReadWriteException(e);
        }
    }

    public static FileIOWriter buildNewFileIOWriter(String path)  throws ReadWriteException {
        try {
            return (FileIOWriter) buildNewFileIO(path, 'w');
        } catch (ClassCastException e) {
            throw new ReadWriteException(e);
        }
    }



    /**
     * Creates and adds the {@link FileIOBuilder}s to a list of {@link FileIOBuilder}s
     * that will be used to build the appropriate type of {@link FileIO}. Function must
     * be called before calling selectFilePath as selectFilePath needs this as setup.
     */
    public static void setupFileIOBuilders() {
        if (!instantiated) {
            FileIOReaderBuilder jsonIOBuilder = new JSONIOBuilder(new String[]{"json"});
            FileIOReaderBuilder xmlIOBuilder = new XMLIOBuilder(new String[]{"xml"});

            WRITER_BUILDERS.add((FileIOWriterBuilder) jsonIOBuilder);
            READER_BUILDERS.add(jsonIOBuilder);
            READER_BUILDERS.add(xmlIOBuilder);

            instantiated = true;
        }
    }
}
