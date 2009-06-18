/* Copyright 2002-2008 CS Communication & Systèmes
 * Licensed to CS Communication & Systèmes (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.orekit.errors.OrekitException;


/** 
 * Helper class for loading data files.  
 * 
 * <p> Modified by J. Pritchard for a runtime independent strategy
 * employing URLs rather than Files.  Dropped the old
 * <code>orekit.data.path</code> for the current
 * <code>orekit.data.url</code> option. </p>
 * 
 * <p> This class accepts a data archive URL from a system property
 * named <code>orekit.data.url</code>.  In this case, the classpath is
 * ignored.  This usage improves the performance of data loading, and
 * permits data files to be excluded from the classpath archives. </p>
 * 
 *  <p> This class accepts classpath archives from a class loader in
 * the class {@link java.net.URLClassLoader} which is common to most
 * applications.  </p>
 * 
 * <p> If archives are not found, no data will be available to the
 * library (for example no pole corrections will be applied and only
 * predefined UTC steps will be taken into account). No errors will be
 * triggered.  </p>
 * 
 * <p> The organization of files in the directories is free. Files are
 * found by matching name patterns while crawling into all
 * sub-directories. If the date searched for is found in one path
 * component, the following path components will be ignored, thus
 * allowing users to overwrite system-wide data by prepending their
 * own components before system-wide ones.  </p>
 * 
 * <p> Gzip-compressed data files are supported transparently with non
 * gzip compressed data files. </p>
 *
 * <p> This is a simple application of the <code>visitor</code> design
 * pattern for directory hierarchy crawling.  </p>
 *
 * @author Luc Maisonobe
 * @version Revision:1665 Date:2008-06-11 12:12:59 +0200 (mer., 11 juin 2008) 
 * @author John Pritchard
 * @version Wed, 17 Jun 2009
 */
public class DataDirectoryCrawler {

    public static final String OREKIT_DATA_URL = "orekit.data.url";

    /** Pattern for gzip files. */
    private static final Pattern GZIP_FILE_PATTERN = Pattern.compile("(.*)\\.gz$");

    /** Pattern for zip archives. */
    private static final Pattern ZIP_ARCHIVE_PATTERN = Pattern.compile("(.*)(?:(?:\\.zip)|(?:\\.jar))$");

    /** Error message for unknown path entries. */
    private static final String NEITHER_DIRECTORY_NOR_ZIP_ARCHIVE =
        "{0} is neither a directory nor a zip archive file";

    private final List<URL> archives = new ArrayList<URL>();

    /** Build a data files crawler.
     * @exception OrekitException if path contains inexistent components
     */
    public DataDirectoryCrawler() throws OrekitException {
        super();

        String path = System.getProperty(OREKIT_DATA_URL);
        if (null != path){
            try {
                URL url = new URL(path);
                this.archives.add(url);
            }
            catch (IOException exc){
                throw new OrekitException(path,exc);
            }
        }
        else {
            ClassLoader cl = this.getClass().getClassLoader();
            if (cl instanceof URLClassLoader){
                URLClassLoader ucl = (URLClassLoader)cl;
                URL[] ulist = ucl.getURLs();
                for (URL url : ulist){

                    if (ZIP_ARCHIVE_PATTERN.matcher(url.getPath()).matches()){

                        this.archives.add(url);
                    }
                }
            }
        }
    }

    /** Crawl the contents of the archives with one visitor.
     * @param visitor data file visitor to use
     * @exception OrekitException if some data is missing, duplicated
     * or can't be read
     */
    public void crawl(final DataFileLoader visitor) throws OrekitException {
        if (null != visitor)
            this.crawl(new DataFileLoader[]{visitor});
    }
    /** Save time by crawling the contents of the archives with
     * multiple visitors.
     * @param visitors List of data file visitors
     * @exception OrekitException if some data is missing, duplicated
     * or can't be read
     */
    public void crawl(final DataFileLoader[] visitors) throws OrekitException {
        if (null != visitors && 0 < visitors.length){
            OrekitException delayedException = null;

            for (URL archive : archives) {
                try {
                    ZipInputStream zip = new ZipInputStream(archive.openStream());
                    try {
                        this.crawl(visitors, zip);
                    }
                    finally {
                        zip.close();
                    }
                }
                catch (ZipException ze) {
                    // this is an important configuration error, we report it immediately
                    throw new OrekitException(NEITHER_DIRECTORY_NOR_ZIP_ARCHIVE,
                                              new Object[] {
                                                  archive.toExternalForm()
                                              });
                }
                catch (IOException ioe) {
                    // maybe the next archive will be able to provide data
                    // wait until all archives have been tried
                    delayedException = new OrekitException(ioe.getMessage(), ioe);
                }
                catch (ParseException pe) {
                    // maybe the next archive will be able to provide data
                    // wait until all archives have been tried
                    delayedException = new OrekitException(pe.getMessage(), pe);
                }
                catch (OrekitException oe) {
                    // maybe the next archive will be able to provide data
                    // wait until all archives have been tried
                    delayedException = oe;
                }
            }

            if (delayedException != null) {
                throw delayedException;
            }
        }
    }

    /** Crawl the contents of an archive.
     * @param visitors Data file visitors
     * @param zip zip input stream
     * @exception OrekitException if some data is missing, duplicated
     * or can't be read
     * @exception IOException if data cannot be read
     * @exception ParseException if data cannot be read
     */
    private void crawl(final DataFileLoader[] visitors, final ZipInputStream zip)
        throws OrekitException, IOException, ParseException {

        // loop over all zip entries
        ZipEntry entry = zip.getNextEntry();
        while (entry != null) {

            if (!entry.isDirectory()) {

                if (ZIP_ARCHIVE_PATTERN.matcher(entry.getName()).matches())

                    this.crawl(visitors, new ZipInputStream(zip));

                else {
                    String name = entry.getName();
                    {
                        final int lastSlash = name.lastIndexOf('/');
                        if (lastSlash >= 0)
                            name = name.substring(lastSlash + 1);
                    }

                    final Matcher gzipMatcher = GZIP_FILE_PATTERN.matcher(name);
                    final boolean gzipped = gzipMatcher.matches();
                    final String baseName = ((gzipped)?(gzipMatcher.group(1)):(name));

                    for (DataFileLoader visitor: visitors){
                        if (visitor.fileIsSupported(baseName)) {

                            if (gzipped)
                                visitor.loadData(new GZIPInputStream(zip), name);
                            else 
                                visitor.loadData(zip, name);
                        }
                    }
                }
            }
            zip.closeEntry();
            entry = zip.getNextEntry();
        }
    }

}
