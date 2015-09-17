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

import java.awt.EventQueue;
import java.util.Arrays;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.netbeans.spi.project.ActionProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;

public final class PhpOnSaveTask implements OnSaveTask, Runnable {

    private static final RequestProcessor RP = new RequestProcessor(PhpOnSaveTask.class);

    private final FileObject fileObject;


    PhpOnSaveTask(FileObject fileObject) {
        assert fileObject != null;
        this.fileObject = fileObject;
    }

    @Override
    public void performTask() {
        // noop
    }

    @Override
    public void runLocked(Runnable run) {
        RP.post(this);
    }

    @Override
    public boolean cancel() {
        // noop
        return false;
    }

    @Override
    public void run() {
        Project project = FileOwnerQuery.getOwner(fileObject);
        if (project == null) {
            return;
        }
        SourceGroup[] sourceGroups = ProjectUtils.getSources(project).getSourceGroups("PHPSOURCE"); // NOI18N
        if (sourceGroups.length < 1) {
            return;
        }
        if (!isSourceFile(fileObject, sourceGroups)) {
            return;
        }
        final ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
        if (actionProvider == null) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Lookup lookup = Lookups.fixed(fileObject);
                if (Arrays.asList(actionProvider.getSupportedActions()).contains(ActionProvider.COMMAND_TEST_SINGLE)
                        && actionProvider.isActionEnabled(ActionProvider.COMMAND_TEST_SINGLE, lookup)) {
                    actionProvider.invokeAction(ActionProvider.COMMAND_TEST_SINGLE, lookup);
                }
            }
        });
    }

    // a it hack to avoid impl dep on php.api.phpmodule
    private static boolean isSourceFile(FileObject fileObject, SourceGroup[] sourceGroups) {
        if (!FileUtil.isParentOf(sourceGroups[0].getRootFolder(), fileObject)) {
            // not a source file
            return false;
        }
        for (int i = 1; i < sourceGroups.length; ++i) {
            if (FileUtil.isParentOf(sourceGroups[i].getRootFolder(), fileObject)) {
                // some test file
                return false;
            }
        }
        return true;
    }

    //~ Factories

    @MimeRegistration(mimeType = "text/x-php5", service = OnSaveTask.Factory.class, position = 2000)
    public static final class Factory implements OnSaveTask.Factory {

        @Override
        public OnSaveTask createTask(OnSaveTask.Context context) {
            return new PhpOnSaveTask(NbEditorUtilities.getFileObject(context.getDocument()));
        }

    }

}
