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

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.php.project.PhpProject;
import org.netbeans.modules.php.project.ui.actions.RunTestCommand;
import org.openide.filesystems.FileObject;

public class PhpFileChanged {
    
    public static final String TEST_IDENTIFIER = "PhpUnit";
    
    private final FileObject changedFileObject;

    public PhpFileChanged(FileObject currentFileObject) {
        changedFileObject = currentFileObject;
    }
    
    public void runChangedFile() {
        
        Project currentProject = FileOwnerQuery.getOwner(changedFileObject);
        PhpProject currentPhpProject = (PhpProject) currentProject;
        
        RunTestCommand currentTestCommand = new RunTestCommand(currentPhpProject);
        currentTestCommand.invokeActionInternal(changedFileObject.getLookup());
        
    }
   
}
