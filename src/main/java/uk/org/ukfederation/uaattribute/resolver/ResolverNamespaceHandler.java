/*
 * This file to You under the Apache  License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License.  You may obtain
 * a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.org.ukfederation.uaattribute.resolver;

import edu.internet2.middleware.shibboleth.common.config.BaseSpringNamespaceHandler;

/**
 * Spring namespace handler for the Shibboleth metadata namespace.
 */
public class ResolverNamespaceHandler extends BaseSpringNamespaceHandler {

    /** Namespace for this handler. */
    public static final String NAMESPACE = "http://ukfederation.org.uk/schemas/uaattribute/resolver";

    /** {@inheritDoc} */
    public void init() {
        registerBeanDefinitionParser(UserAgentAttributeMapDataConnectorBeanDefinitionParser.TYPE_NAME,
                new UserAgentAttributeMapDataConnectorBeanDefinitionParser());
    }
}
