package cz.muni.fi.mathml.mathml2text.input;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for unzipping files.
 * 
 * @author Maros Kucbel
 * @date 2013-05-12T20:58:03+0100
 */
public final class Unzipper {

    private static final Logger logger = LoggerFactory.getLogger(Unzipper.class);
    
    /**
     * Unzips a single file.
     * 
     * @param zipFile Input zipped file.
     * @return {@link InputStream} containing unzipped file or {@code null} if there
     * was an error during unzipping.
     */
    public static InputStream unzip(final File zipFile) {
        Validate.isTrue(zipFile.getName().endsWith("zip"), "Input file is not zipped.");
        int bufferSize = 2048;

        try {
            ZipFile zip = new ZipFile(zipFile);
            Enumeration zipFileEntries = zip.entries();
            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                if (!entry.isDirectory()) {
                    final BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[bufferSize];

                    // write the current file to disk
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, bufferSize)) != -1) {
                        out.write(data, 0, currentByte);
                    }
                    is.close();
                    return new ByteArrayInputStream(out.toByteArray());
                }
            }
        } catch (final ZipException ex) {
            logger.warn("Error while unzipping file.", ex);
        } catch (final IOException ex) {
            logger.warn("Error while unzipping file.", ex);
        }
        return null;
    }
}