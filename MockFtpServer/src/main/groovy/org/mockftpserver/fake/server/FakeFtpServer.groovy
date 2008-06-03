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

import org.mockftpserver.core.command.CommandHandler
import org.mockftpserver.core.command.CommandNames
import org.mockftpserver.core.server.AbstractFtpServer
import org.mockftpserver.fake.ServerConfiguration
import org.mockftpserver.fake.ServerConfigurationAware
import org.mockftpserver.fake.command.ConnectCommandHandler
import org.mockftpserver.fake.command.PassCommandHandler
import org.mockftpserver.fake.command.PwdCommandHandler
import org.mockftpserver.fake.command.QuitCommandHandler
import org.mockftpserver.fake.command.UserCommandHandler
import org.mockftpserver.fake.filesystem.FileSystem
import org.mockftpserver.fake.user.UserAccount

/**
 * "Fake" implementation of an FTP server.
 *
 *      T B D
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
class FakeFtpServer extends AbstractFtpServer implements ServerConfiguration {

    FileSystem fileSystem
    Map userAccounts = [:]

    FakeFtpServer() {
        setCommandHandler(CommandNames.CONNECT, new ConnectCommandHandler());
        setCommandHandler(CommandNames.PASS, new PassCommandHandler());
        setCommandHandler(CommandNames.PWD, new PwdCommandHandler());
        setCommandHandler(CommandNames.QUIT, new QuitCommandHandler());
        setCommandHandler(CommandNames.USER, new UserCommandHandler());
    }

    /**
     * Initialize a CommandHandler that has been registered to this server. If the CommandHandler implements
     * the <code>ServerConfigurationAware</code> interface, then set its <code>ServerConfiguration</code>
     * property to <code>this</code>.
     *
     * @param commandHandler - the CommandHandler to initialize
     */
    protected void initializeCommandHandler(CommandHandler commandHandler) {
        if (commandHandler instanceof ServerConfigurationAware) {
            commandHandler.serverConfiguration = this
        }
    }

    public UserAccount getUserAccount(String username) {
        userAccounts[username]
    }

}