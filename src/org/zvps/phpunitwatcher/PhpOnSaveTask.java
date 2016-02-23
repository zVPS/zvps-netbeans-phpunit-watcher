/**
 *   PHPUnit Watcher - run unit tests on save
 *   Copyright (C) 2016  nForced Website Hosting Limited
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
 * 
 *   Contributions List (please see github.com/zVPS/zvps-netbeans-phpunit-watcher)
 *   for an up-to-date list
 *   https://github.com/tmysik
 *   https://github.com/Caffe1neAdd1ct
 * 
 **/
package org.zvps.phpunitwatcher;

import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.filesystems.FileObject;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.modules.parsing.api.Source;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;

public final class PhpOnSaveTask implements OnSaveTask {
    
    private final static RequestProcessor RP = new RequestProcessor("phpunitwatchertasks", 1, true);
    
    private final FileObject fileObject;
    
    PhpOnSaveTask(FileObject fileObject) {
        assert fileObject != null;
        this.fileObject = fileObject;
    }

    @Override
    public void performTask() {
        
        PhpUnitTestRunnable UnitTestRunnable = new PhpUnitTestRunnable(fileObject);
        
        final RequestProcessor.Task theTask = RP.create(UnitTestRunnable);
        
        final ProgressHandle ph = ProgressHandle.createHandle("performing test", theTask);
        theTask.addTaskListener(new TaskListener() {
            @Override
            public void taskFinished(org.openide.util.Task task) {
                ph.finish();
            }
        });
        
        theTask.schedule(1000);
    }

    @Override
    public void runLocked(Runnable run) {
        run.run();
    }

    @Override
    public boolean cancel() {
        // noop
        return true;
    }
    
    //~ Factories
    @MimeRegistration(mimeType = "text/x-php5", service = OnSaveTask.Factory.class, position = 2000)
    public static final class Factory implements OnSaveTask.Factory {

        @Override
        public OnSaveTask createTask(OnSaveTask.Context context) {
            
            Document document = context.getDocument(); 
            if (document == null) { 
                return null; 
            } 
 
            Source source = Source.create(document); 
            FileObject srcFile = source.getFileObject(); 
            return srcFile != null ? new PhpOnSaveTask(srcFile) : null; 
        }

    }

}
