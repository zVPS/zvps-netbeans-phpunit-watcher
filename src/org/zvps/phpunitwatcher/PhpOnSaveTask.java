/**
 *   PHPUnit Watcher - run unit tests on save
 *   Copyright (C) 2014  nForced Website Hosting Limited
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package org.zvps.phpunitwatcher;

import java.util.HashSet;
import java.util.Set;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.filesystems.FileObject;

@MimeRegistration(mimeType = "text/x-php5", service = OnSaveTask.Factory.class, position = 2000)
public final class PhpOnSaveTask implements OnSaveTask.Factory {
    
    private Document currentDocument;
    private FileObject currentFile;
    private final Set<FileObject> registeredFileList = new HashSet<>();

    /**
     * constructor
     */
    public PhpOnSaveTask() {
    }

    /** run on save tasks */
    @Override
    public OnSaveTask createTask(OnSaveTask.Context context) {
        
        /** Get the current document being saved from current context */
        this.currentDocument = context.getDocument();
        
        /** Get current file being saved */
        this.currentFile = NbEditorUtilities.getFileObject( currentDocument );
        
        /** Register a custom file change listener to the file */
        
        if(this.currentFile != null) {
            this.registerFileChangeListener();
        }
        
        /** Compulsory registration of SaveTask to current file. */
        OnSaveTask onSaveTask = new PhpOnSaveTaskRun();
        return onSaveTask;
    }
    
    private void registerFileChangeListener() {
        
        if(!(this.registeredFileList.contains(this.currentFile))) {
        
            /** Create a new PhpFileChangeListener to register to the File being saved */
            PhpFileChangeListener currentFileListener = new PhpFileChangeListener();

            /** Register our new PhpFileChangeListener to run custom code on file change */
            this.currentFile.addFileChangeListener( currentFileListener );

            this.registeredFileList.add(currentFile);
        }

    }

}
