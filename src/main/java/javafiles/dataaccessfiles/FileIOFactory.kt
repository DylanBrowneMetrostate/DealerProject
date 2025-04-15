package javafiles.dataaccessfiles

import javafiles.customexceptions.BadExtensionException
import javafiles.customexceptions.PathNotFoundException
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.builderimplements.*
import javafiles.dataaccessfiles.builderimplements.JSONIOBuilder
import javafiles.dataaccessfiles.builderimplements.XMLIOBuilder
import javafiles.dataaccessfiles.fileioimplements.FileIOReader
import javafiles.dataaccessfiles.fileioimplements.FileIOWriter
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object FileIOFactory {
    @JvmStatic
    val instance: FileIOFactory = FileIOFactory

    private val READER_BUILDERS: MutableList<FileIOReaderBuilder> = ArrayList()
    private val WRITER_BUILDERS: MutableList<FileIOWriterBuilder> = ArrayList()

    init {
        val jsonIOBuilder: FileIOReaderBuilder = JSONIOBuilder(arrayOf("json"))
        val xmlIOBuilder: FileIOReaderBuilder = XMLIOBuilder(arrayOf("xml"))

        WRITER_BUILDERS.add(jsonIOBuilder as FileIOWriterBuilder)
        READER_BUILDERS.add(jsonIOBuilder)
        READER_BUILDERS.add(xmlIOBuilder)
    }

    /**
     * Returns whether this [FileIO] can be created from the given extensions for
     * the given mode.
     *
     * @param path The path of the file to be opened or created.
     * @param extensions An array of valid extensions for the object.
     * @return Whether this [FileIO] ends with one of the valid extensions for this mode.
     */
    private fun buildable(path: String, extensions: Array<String>): Boolean {
        for (extension in extensions) {
            if (path.endsWith(extension)) {
                return true
            }
        }
        return false
    }

    @Throws(ReadWriteException::class)
    internal fun build(builder: FileIOBuilder, path: String, mode: BuilderTag): FileIO? {
        if (buildable(path, builder.extensions)) {
            if (mode == BuilderTag.READER && builder is FileIOReaderBuilder) {
                return builder.createFileIOReader(path)
            }
            if (mode == BuilderTag.WRITER && builder is FileIOWriterBuilder) {
                return builder.createFileIOWriter(path)
            }
        }
        return null
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in extensions.
     *
     * @param extensions The available extensions of files that can be chosen.
     * @return The selected path to the file if the user selects a file and confirms
     * the dialog, or null if the user cancels or closes the dialog without
     * selecting a file.
     *
     * @author Christopher Engelhart
     */
    private fun selectFile(extensions: Array<String?>): File? {
        val fileChooser = JFileChooser()
        fileChooser.currentDirectory = File(System.getProperty("user.dir"))
        fileChooser.fileFilter = FileNameExtensionFilter("Choose File", *extensions)
        val result = fileChooser.showOpenDialog(null)
        return if (result == 0) fileChooser.selectedFile else null
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in extensions.
     *
     * @param extensions The available extensions of files that can be chosen.
     * @return The selected path to the file if the user selects a file and confirms
     * the dialog, or null if the user cancels or closes the dialog without
     * selecting a file.
     */
    private fun selectFilePath(extensions: Array<String?>): String? {
        val file = selectFile(extensions) ?: return null
        return file.toString()
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in
     * that allows for the creation of a [FileIO] in the given mode
     * with one or more of the [FileIOBuilder]s in BUILDERS.
     *
     * @return The selected path to the file if the user selects a file and confirms
     * the dialog, or null if the user cancels or closes the dialog without
     * selecting a file.
     */
    private fun selectFilePath(mode: BuilderTag): String? {
        val builders = getBuilderList(mode)

        val buildersExtensions: MutableList<String> = ArrayList()
        for (builder in builders) {
            val extensions = builder.extensions
            buildersExtensions.addAll(listOf(*extensions))
        }
        val extensions = arrayOfNulls<String>(buildersExtensions.size)
        for (i in extensions.indices) {
            extensions[i] = buildersExtensions[i]
        }
        return selectFilePath(extensions)
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in
     * that allows for the creation of a [FileIO] in reading mode
     * with one or more of the [FileIOBuilder]s in BUILDERS.
     *
     * @return The selected path to the file if the user selects a file and confirms
     * the dialog, or null if the user cancels or closes the dialog without
     * selecting a file.
     */
    fun selectFileReaderPath(): String? {
        return selectFilePath(BuilderTag.READER)
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in
     * that allows for the creation of a [FileIO] in writing mode
     * with one or more of the [FileIOBuilder]s in BUILDERS.
     *
     * @return The selected path to the file if the user selects a file and confirms
     * the dialog, or null if the user cancels or closes the dialog without
     * selecting a file.
     */
    fun selectFileWriterPath(): String? {
        return selectFilePath(BuilderTag.WRITER)
    }

    private fun getBuilderList(mode: BuilderTag): List<FileIOBuilder> {
        return ArrayList(if (mode == BuilderTag.WRITER) WRITER_BUILDERS else READER_BUILDERS)
    }

    /**
     * Creates and returns the appropriate type of [FileIO] from the given path
     * and mode. If the creation of the [FileIO] is invalid, throws a new
     * [ReadWriteException] (such as through invalid path, mode, or overriding
     * Maven files).
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file that is created (read 'r' or write 'w').
     * @return The new instance of the [FileIO] created.
     * @throws ReadWriteException When the creation of the [FileIO] is invalid.
     */
    @Throws(ReadWriteException::class)
    private fun buildNewFileIO(path: String, mode: BuilderTag): FileIO {
        val builders = getBuilderList(mode)

        for (builder in builders) {
            val fileIO = build(builder, path, mode)
            if (fileIO != null) {
                return fileIO
            }
        }
        val cause = BadExtensionException("Extension for \"$path is invalid.")
        throw ReadWriteException(cause)
    }

    @Throws(ReadWriteException::class)
    fun buildNewFileIOReader(path: String): FileIOReader {
        val file = File(path)
        if (!file.exists()) {
            val reason = "Path: \"$path\" does not exist, so it can't be read."
            val cause = PathNotFoundException(reason)
            throw ReadWriteException(cause)
        }
        try {
            return buildNewFileIO(path, BuilderTag.READER) as FileIOReader
        } catch (e: ClassCastException) {
            throw ReadWriteException(e)
        }
    }

    @Throws(ReadWriteException::class)
    fun buildNewFileIOWriter(path: String): FileIOWriter {
        try {
            return buildNewFileIO(path, BuilderTag.WRITER) as FileIOWriter
        } catch (e: ClassCastException) {
            throw ReadWriteException(e)
        }
    }

    internal enum class BuilderTag {
        READER,
        WRITER
    }
}
