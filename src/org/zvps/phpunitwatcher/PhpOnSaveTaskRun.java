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

import org.netbeans.spi.editor.document.OnSaveTask;

public class PhpOnSaveTaskRun implements OnSaveTask {

    public void PhpOnSaveTaskRun() {
    }

    @Override
    public void performTask() {
    }

    @Override
    public void runLocked( Runnable run ) {
        performTask();
    }

    @Override
    public boolean cancel() {
        return true;
    }
}
