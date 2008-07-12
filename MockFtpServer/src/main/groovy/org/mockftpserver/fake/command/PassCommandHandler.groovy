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
package org.mockftpserver.fake.command

import org.mockftpserver.core.command.Command
import org.mockftpserver.core.command.ReplyCodes
import org.mockftpserver.core.session.Session
import org.mockftpserver.core.session.SessionKeys
import org.mockftpserver.fake.command.AbstractFakeCommandHandler



/**
 * CommandHandler for the PASS command. Handler logic:
 * <ol>
 *  <li>If the required password parameter is missing, then reply with 501</li>
 *  <li>If this command was not preceded by a valid USER command, then reply with 503</li>
 *  <li>If the user account configured for the named user does not exist or is not valid, then reply with 530</li>
 *  <li>If the specified password is not correct, then reply with 530</li>
 *  <li>Otherwise, reply with 250</li>
 * </ol>
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
class PassCommandHandler extends AbstractFakeCommandHandler {

    protected void handle(Command command, Session session) {
        def password = command.getRequiredParameter(0)
        def username = getRequiredSessionAttribute(session, SessionKeys.USERNAME)

        if (validateUserAccount(username, session)) {
            def userAccount = serverConfiguration.getUserAccount(username)
            if (userAccount.isValidPassword(password)) {
                login(userAccount, session)
            }
            else {
                sendReply(session, ReplyCodes.PASS_LOG_IN_FAILED)
            }
        }
    }

}