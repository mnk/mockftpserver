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

/**
 * Represents a single user account on the server, including the username, password and home
 * directory. It also includes several configuration flags, described below.
 * <p>
 * The <code>isValidPassword()</code> method returns true if the specified password matches
 * the password value configured for this user account. This implementation uses the
 * <code>isEquals()</code> method to compare passwords. 
 * <p>
 * If you want to provide a custom comparison, for instance using encrypted passwords, you can
 * subclass this class and override the <code>comparePassword()</code> method to provide your own
 * custom implementation.
 * <p>
 * If the <code>passwordCheckedDuringValidation</code> property is set to false, then the password
 * value is ignored, and the <code>isValidPassword()</code> method just returns <code<true</code>. 
 * <p>
 * The <code>accountRequiredForLogin</code> property defaults to false. If it is set to true, then
 * it is expected that the login for this account will require an ACCOUNT (ACCT) command after the
 * PASSWORD (PASS) command is completed.
 */
class UserAccount {

    public static final DEFAULT_USER = 'system'
    public static final DEFAULT_GROUP = 'users'

    String username
    String password
    String homeDirectory
    List groups
    boolean passwordRequiredForLogin = true
    boolean passwordCheckedDuringValidation = true
    boolean accountRequiredForLogin = false

    /**
     * Return the name of the primary group to which this user belongs. If this account has no associated
     * groups set, then this method returns the <code>DEFAULT_GROUP</code>. Otherwise, this method
     * returns the first group name in the <code>groups</code> list.
     * @return the name of the primary group for this user 
     */
    String getPrimaryGroup() {
        groups ? groups.first() : DEFAULT_GROUP
    }

    /**
     * Return true if the specified password is the correct, valid password for this user account.
     * This implementation uses standard (case-sensitive) String comparison. Subclasses can provide
     * custom comparison behavior, for instance using encrypted password values, by overriding this
     * method.
     *
     * @param password - the password to compare against the configured value
     * @return true if the password is correct and valid
     *
     * @throws AssertionError - if the username property is null
     */
    boolean isValidPassword(String password) {
        assert username
        return passwordCheckedDuringValidation ? comparePassword(password) : true
    }

    /**
     * @return true if this UserAccount object is valid; i.e. if the homeDirectory is non-null and non-empty.
     */
    boolean isValid() {
        return homeDirectory
    }

    /**
     * @return the String representation of this object
     */
    String toString() {
        "UserAccount[username=$username; password=$password; homeDirectory=$homeDirectory; " +
                "passwordRequiredForLogin=$passwordRequiredForLogin]"
    }

    /**
     * Return true if the specified password matches the password configured for this user account.
     * This implementation uses standard (case-sensitive) String comparison. Subclasses can provide
     * custom comparison behavior, for instance using encrypted password values, by overriding this
     * method.
     *
     * @param password - the password to compare against the configured value
     * @return true if the passwords match
     */
    protected boolean comparePassword(String password) {
        return password == this.password
    }
}