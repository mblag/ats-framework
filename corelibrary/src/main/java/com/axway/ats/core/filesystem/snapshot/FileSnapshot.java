/*
 * Copyright 2017 Axway Software
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axway.ats.core.filesystem.snapshot;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.axway.ats.common.filesystem.Md5SumMode;
import com.axway.ats.common.filesystem.snapshot.equality.EqualityState;
import com.axway.ats.common.filesystem.snapshot.equality.FileTrace;
import com.axway.ats.core.filesystem.LocalFileSystemOperations;
import com.axway.ats.core.filesystem.exceptions.AttributeNotSupportedException;
import com.axway.ats.core.utils.IoUtils;

class FileSnapshot implements Serializable {

    private static final long     serialVersionUID = 1L;

    private static final Logger   log              = Logger.getLogger( FileSnapshot.class );

    // list of important file attributes
    private String                path;
    private long                  size             = -1;
    private long                  timeModified     = -1;
    private String                md5              = null;
    private String                permissions      = null;

    private SnapshotConfiguration configuration;

    /**
     * Constructor called when taking the actual snapshot.
     * All file attributes are read from the file system
     *
     * @param path
     * @param fileRule
     */
    FileSnapshot( SnapshotConfiguration configuration, String path, FindRules fileRule ) {

        this.configuration = configuration;

        LocalFileSystemOperations fileOps = new LocalFileSystemOperations();

        this.path = IoUtils.normalizeUnixFile( path );

        if( doWeCheckFileSize( fileRule ) ) {
            this.size = fileOps.getFileSize( path );
        }
        if( doWeCheckFileModificationTime( fileRule ) ) {
            this.timeModified = fileOps.getFileModificationTime( path );
        }
        if( doWeCheckFileMD5( fileRule ) ) {
            this.md5 = fileOps.computeMd5Sum( path, Md5SumMode.BINARY );
        }
        if( doWeCheckFilePermissions( fileRule ) ) {
            try {
                this.permissions = fileOps.getFilePermissions( path );
            } catch( AttributeNotSupportedException ante ) {
                this.permissions = null;
                log.debug( "We skip reading file permissions for " + this.path
                           + " as are not supported by this OS" );
            }
        }
    }

    /**
     * Constructor called when loading snapshot from backup file.
     * All file attributes are read from the backup file
     *
     * @param path
     * @param size
     * @param timeModified
     * @param md5
     */
    FileSnapshot( String path, long size, long timeModified, String md5, String permissions ) {

        this.path = IoUtils.normalizeUnixFile( path );

        this.size = size;
        this.timeModified = timeModified;
        this.md5 = md5;
        this.permissions = permissions;
    }

    /**
     * Create an instance from a file
     * @param fileNode
     */
    static FileSnapshot fromFile( Element fileNode ) {

        NamedNodeMap fileAttributes = fileNode.getAttributes();

        String pathAtt = fileAttributes.getNamedItem( "path" ).getNodeValue();
        long fileSize = -1;
        long fileTimeModified = -1;
        String fileMD5 = null;
        String filePermissions = null;

        Node sizeNode = fileAttributes.getNamedItem( "size" );
        if( sizeNode != null ) {
            fileSize = Long.parseLong( sizeNode.getNodeValue() );
        }
        Node timeModifiedNode = fileAttributes.getNamedItem( "modified" );
        if( timeModifiedNode != null ) {
            fileTimeModified = SnapshotUtils.stringToDate( timeModifiedNode.getNodeValue() );
        }
        Node md5Node = fileAttributes.getNamedItem( "md5" );
        if( md5Node != null ) {
            fileMD5 = md5Node.getNodeValue();
        }
        Node permissionsNode = fileAttributes.getNamedItem( "permissions" );
        if( permissionsNode != null ) {
            filePermissions = permissionsNode.getNodeValue();
        }
        FileSnapshot fileSnapshot = new FileSnapshot( pathAtt, fileSize, fileTimeModified, fileMD5,
                                                      filePermissions );

        log.debug( "Add " + fileSnapshot.toString() );

        return fileSnapshot;
    }

    String getPath() {

        return path;
    }

    void compare( String thisSnapshotName, String thatSnapshotName, FileSnapshot that,
                  EqualityState equality ) {

        // path is already checked, check the rest

        FileTrace fileTrace = new FileTrace( "[" + thisSnapshotName + "] file \"" + this.path,
                                             "[" + thatSnapshotName + "] file \"" + that.path );

        // check the file MD5
        if( ( this.md5 == null && that.md5 != null )
            || ( this.md5 != null && !this.md5.equals( that.md5 ) ) ) {
            fileTrace.addDifference( "MD5 checksum", String.valueOf( this.md5 ), String.valueOf( that.md5 ) );
        }

        // check the file size
        if( this.size != that.size ) {
            fileTrace.addDifference( "Size", String.valueOf( this.size ), String.valueOf( that.size ) );
        }

        // check the file modification time
        if( this.timeModified != that.timeModified ) {
            fileTrace.addDifference( "Modification time", String.valueOf( this.timeModified ),
                                     String.valueOf( that.timeModified ) );
        }

        if( ( this.permissions == null && that.permissions != null )
            || ( this.permissions != null && !this.permissions.equals( that.permissions ) ) ) {
            fileTrace.addDifference( "Permissions", String.valueOf( this.permissions ),
                                     String.valueOf( that.permissions ) );
        }

        if( fileTrace.getDifferencies().size() > 0 ) {
            // files are different
            equality.addDifference( fileTrace );
        } else {
            log.debug( "Same files: " + this.getPath() + " and " + that.getPath() );
        }
    }

    private boolean doWeCheckFileSize( FindRules fileRule ) {

        if( fileRule != null && fileRule.isCheckFileSize() ) {
            // user explicitly wanted to check this attribute on this file, ignore any global settings
            return true;
        }

        if( !configuration.isCheckSize() ) {
            // user globally disabled this check
            return false;
        }

        return fileRule == null || !fileRule.isSkipFileSize();
    }

    private boolean doWeCheckFileModificationTime( FindRules fileRule ) {

        if( fileRule != null && fileRule.isCheckFileModificationTime() ) {
            // user explicitly wanted to check this attribute on this file, ignore any global settings
            return true;
        }

        if( !configuration.isCheckModificationTime() ) {
            // user globally disabled this check
            return false;
        }

        return fileRule == null || !fileRule.isSkipFileModificationTime();
    }

    private boolean doWeCheckFileMD5( FindRules fileRule ) {

        if( fileRule != null && fileRule.isCheckFileMd5() ) {
            // user explicitly wanted to check this attribute on this file, ignore any global settings
            return true;
        }

        if( !configuration.isCheckMD5() ) {
            // user globally disabled this check
            return false;
        }

        return fileRule == null || !fileRule.isSkipFileMd5();
    }

    private boolean doWeCheckFilePermissions( FindRules fileRule ) {

        if( fileRule != null && fileRule.isCheckFilePermissions() ) {
            // user explicitly wanted to check this attribute on this file, ignore any global settings
            return true;
        }

        if( !configuration.isCheckPermissions() ) {
            // user globally disabled this check
            return false;
        }

        return fileRule == null || !fileRule.isSkipFilePermissions();
    }

    Element toFile( Document dom, Element fileSnapshotNode ) {

        fileSnapshotNode.setAttribute( "path", this.path );
        if( this.size > -1 ) {
            fileSnapshotNode.setAttribute( "size", String.valueOf( this.size ) );
        }
        if( this.timeModified > -1 ) {
            fileSnapshotNode.setAttribute( "modified", SnapshotUtils.dateToString( this.timeModified ) );
        }
        if( this.md5 != null ) {
            fileSnapshotNode.setAttribute( "md5", this.md5 );
        }
        return fileSnapshotNode;
    }

    /**
     * This method is good for debug purpose
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append( "file: " + this.path + ", " );
        if( this.size > -1 ) {
            sb.append( "size: " + this.size + ", " );
        }
        if( this.timeModified > -1 ) {
            sb.append( "modified: " + SnapshotUtils.dateToString( this.timeModified ) + ", " );
        }
        if( this.md5 != null ) {
            sb.append( "md5: " + this.md5 );
        }
        return sb.toString();
    }
}
