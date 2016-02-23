/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zvps.phpunitwatcher;

import java.util.Arrays;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.spi.project.ActionProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author kandrews
 */
public class PhpUnitTestRunnable implements Runnable {
    
    private final FileObject fileObject;
    
    PhpUnitTestRunnable(FileObject fileObject) {
        this.fileObject = fileObject;
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

        Lookup lookup = Lookups.fixed(fileObject);
        if (Arrays.asList(actionProvider.getSupportedActions()).contains(ActionProvider.COMMAND_TEST_SINGLE)
                && actionProvider.isActionEnabled(ActionProvider.COMMAND_TEST_SINGLE, lookup)) {
            actionProvider.invokeAction(ActionProvider.COMMAND_TEST_SINGLE, lookup);
        }
    }

    /**
     * Avoiding impl dep on php.api.phpmodule
     * @param fileObject
     * @param sourceGroups
     * @return 
     */
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
    
}
