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
package org.mockftpserver.fake.server

import org.mockftpserver.core.command.Command
import org.mockftpserver.core.command.CommandHandler
import org.mockftpserver.core.server.AbstractFtpServer
import org.mockftpserver.core.server.AbstractFtpServerTest
import org.mockftpserver.core.session.Session
import org.mockftpserver.fake.command.AbstractFakeCommandHandler
import org.mockftpserver.fake.user.UserAccount


/**
 * Tests for FakeFtpServer.
 *
 * @version $Revision: 54 $ - $Date: 2008-05-13 21:54:53 -0400 (Tue, 13 May 2008) $
 *
 * @author Chris Mair
 */
class FakeFtpServerTest extends AbstractFtpServerTest {

    def commandHandler
    def commandHandler_NotServerConfigurationAware

    //-------------------------------------------------------------------------
    // Extra tests  (Standard tests defined in superclass)
    //-------------------------------------------------------------------------

    /**
     * Test the setCommandHandler() method, for a CommandHandler that does not implement ResourceBundleAware
     */
    void testSetCommandHandler_NotServerConfigurationAware() {
        ftpServer.setCommandHandler("ZZZ", commandHandler_NotServerConfigurationAware);
        assert ftpServer.getCommandHandler("ZZZ") == commandHandler_NotServerConfigurationAware
    }

    /**
     * Test the setCommandHandler() method, for a CommandHandler that implements ReplyTextBundleAware,
     * and whose replyTextBundle attribute is null.
     */
    void testSetCommandHandler_ServerConfigurationAware() {
        ftpServer.setCommandHandler("ZZZ", commandHandler);
        assert ftpServer.getCommandHandler("ZZZ") == commandHandler
        assert ftpServer == commandHandler.serverConfiguration
    }

    void testUserAccounts() {
        def userAccount = new UserAccount()
        def userAccounts = ["abc": userAccount]
        ftpServer.userAccounts = userAccounts
        assert ftpServer.getUserAccount("abc") == userAccount
    }

    void testSystemName() {
        assert ftpServer.systemName == "WINDOWS"
        ftpServer.systemName = "abc"
        assert ftpServer.systemName == "abc"
    }

    void testReplyText() {
        ftpServer.replyTextBaseName = "SampleReplyText"

        ResourceBundle resourceBundle = ftpServer.replyTextBundle
        assert resourceBundle.getString("110") == "Testing123"
    }

    //-------------------------------------------------------------------------
    // Test set up
    //-------------------------------------------------------------------------

    void setUp() {
        super.setUp();
        commandHandler = new TestCommandHandler()
        commandHandler_NotServerConfigurationAware = new TestCommandHandlerNotServerConfigurationAware()
    }

    //-------------------------------------------------------------------------
    // Abstract method implementations
    //-------------------------------------------------------------------------

    protected AbstractFtpServer createFtpServer() {
        return new FakeFtpServer();
    }

    protected CommandHandler createCommandHandler() {
        return new TestCommandHandler();
    }

    protected void verifyCommandHandlerInitialized(CommandHandler commandHandler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

class TestCommandHandler extends AbstractFakeCommandHandler {

    protected void handle(Command command, Session session) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

class TestCommandHandlerNotServerConfigurationAware implements CommandHandler {

    public void handleCommand(Command command, Session session) {
    }

}