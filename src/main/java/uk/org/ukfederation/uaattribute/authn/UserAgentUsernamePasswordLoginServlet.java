/*
 * Licensed to the University Corporation for Advanced Internet Development, Inc.
 * under one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.org.ukfederation.uaattribute.authn;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import edu.internet2.middleware.shibboleth.idp.authn.LoginHandler;
import edu.internet2.middleware.shibboleth.idp.authn.provider.UsernamePasswordLoginServlet;

/**
 * An extension to the standard {@link UsernamePasswordLoginServlet} that also captures the user agent's IP address and
 * stores it in the ....
 */
public class UserAgentUsernamePasswordLoginServlet extends UsernamePasswordLoginServlet {

    /** {@inheritDoc} */
    protected void authenticateUser(HttpServletRequest request, String username, String password) throws LoginException {
        super.authenticateUser(request, username, password);

        Subject userSubject = (Subject) request.getAttribute(LoginHandler.SUBJECT_KEY);
        if (userSubject == null) {
            // TODO error
        }

        // Remove any that may have accumulated from past logins
        userSubject.getPrincipals().removeAll(userSubject.getPrincipals(UserAgentPrincipal.class));

        try {
            InetAddress uaAddress = InetAddress.getByName(request.getRemoteAddr());
            UserAgentPrincipal uaPrincipal = new UserAgentPrincipal(uaAddress.getAddress());
            userSubject.getPrincipals().add(uaPrincipal);
        } catch (UnknownHostException e) {
            // TODO error
        }
    }
}