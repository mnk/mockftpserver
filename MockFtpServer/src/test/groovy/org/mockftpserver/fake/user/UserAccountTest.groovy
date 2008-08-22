/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mockftpserver.fake.user

import org.mockftpserver.fake.filesystem.FileInfo
import org.mockftpserver.fake.filesystem.Permissions
import org.mockftpserver.test.AbstractGroovyTest

/**
 * Tests for UserAccount
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
class UserAccountTest extends AbstractGroovyTest {

    private static final USERNAME = "user123"
    private static final PASSWORD = "password123"
    private static final HOME_DIR = "/usr/user123"
    private static final GROUP = 'group'


    private UserAccount userAccount

    void testGetPrimaryGroup() {
        assert userAccount.primaryGroup == UserAccount.DEFAULT_GROUP

        userAccount.groups = ['abc']
        assert userAccount.primaryGroup == 'abc'

        userAccount.groups.add('def')
        assert userAccount.primaryGroup == 'abc'

        userAccount.groups = []
        assert userAccount.primaryGroup == UserAccount.DEFAULT_GROUP
    }

    void testIsValidPassword() {
        userAccount.username = USERNAME
        userAccount.password = PASSWORD
        assert userAccount.isValidPassword(PASSWORD)

        assert userAccount.isValidPassword("") == false
        assert userAccount.isValidPassword("wrong") == false
        assert userAccount.isValidPassword(null) == false
    }

    void testIsValidPassword_UsernameNullOrEmpty() {
        userAccount.password = PASSWORD
        shouldFail(AssertionError) { userAccount.isValidPassword(PASSWORD) }

        userAccount.username = ''
        shouldFail(AssertionError) { userAccount.isValidPassword(PASSWORD) }
    }

    void testIsValidPassword_OverrideComparePassword() {
        def customUserAccount = new CustomUserAccount()
        customUserAccount.username = USERNAME
        customUserAccount.password = PASSWORD
        println customUserAccount
        assert customUserAccount.isValidPassword(PASSWORD) == false
        assert customUserAccount.isValidPassword(PASSWORD + "123")
    }

    void testIsValidPassword_PasswordNotCheckedDuringValidation() {
        userAccount.username = USERNAME
        userAccount.password = PASSWORD
        userAccount.passwordCheckedDuringValidation = false
        assert userAccount.isValidPassword("wrong")
    }

    void testIsValid() {
        assert !userAccount.valid
        userAccount.homeDirectory = ""
        assert !userAccount.valid
        userAccount.homeDirectory = "/abc"
        assert userAccount.valid
    }

    void testCanRead() {
        userAccount.username = USERNAME
        userAccount.groups = [GROUP]

        testCanRead(USERNAME, GROUP, 'rwxrwxrwx', true)     // ALL
        testCanRead(USERNAME, GROUP, '---------', false)    // NONE

        testCanRead(USERNAME, null, 'r--------', true)      // User
        testCanRead(USERNAME, null, '-wxrwxrwx', false)

        testCanRead(null, GROUP, '---r-----', true)         // Group
        testCanRead(null, GROUP, 'rwx-wxrwx', false)

        testCanRead(null, null, '------r--', true)          // World
        testCanRead(null, null, 'rwxrwx-wx', false)
    }

    void testCanWrite() {
        userAccount.username = USERNAME
        userAccount.groups = [GROUP]

        testCanWrite(USERNAME, GROUP, 'rwxrwxrwx', true)     // ALL
        testCanWrite(USERNAME, GROUP, '---------', false)    // NONE

        testCanWrite(USERNAME, null, '-w-------', true)      // User
        testCanWrite(USERNAME, null, 'r-xrwxrwx', false)

        testCanWrite(null, GROUP, '----w----', true)         // Group
        testCanWrite(null, GROUP, 'rwxr-xrwx', false)

        testCanWrite(null, null, '-------w-', true)          // World
        testCanWrite(null, null, 'rwxrwxr-x', false)
    }

    void testCanExecute() {
        userAccount.username = USERNAME
        userAccount.groups = [GROUP]

        testCanExecute(USERNAME, GROUP, 'rwxrwxrwx', true)     // ALL
        testCanExecute(USERNAME, GROUP, '---------', false)    // NONE

        testCanExecute(USERNAME, null, '--x------', true)      // User
        testCanExecute(USERNAME, null, 'rw-rwxrwx', false)

        testCanExecute(null, GROUP, '-----x---', true)         // Group
        testCanExecute(null, GROUP, 'rwxrw-rwx', false)

        testCanExecute(null, null, '--------x', true)          // World
        testCanExecute(null, null, 'rwxrwxrw-', false)
    }

    //--------------------------------------------------------------------------
    // Helper Methods
    //--------------------------------------------------------------------------

    private void testCanRead(owner, group, permissionsString, expectedResult) {
        def file = createFileInfo(owner, permissionsString, group)
        assert userAccount.canRead(file) == expectedResult, file
    }

    private void testCanWrite(owner, group, permissionsString, expectedResult) {
        def file = createFileInfo(owner, permissionsString, group)
        assert userAccount.canWrite(file) == expectedResult, file
    }

    private void testCanExecute(owner, group, permissionsString, expectedResult) {
        def file = createFileInfo(owner, permissionsString, group)
        assert userAccount.canExecute(file) == expectedResult, file
    }

    private FileInfo createFileInfo(owner, permissionsString, group) {
        return FileInfo.forFile('', 0, null, owner, group, new Permissions(permissionsString))
    }

    void setUp() {
        super.setUp()
        userAccount = new UserAccount()
    }
}