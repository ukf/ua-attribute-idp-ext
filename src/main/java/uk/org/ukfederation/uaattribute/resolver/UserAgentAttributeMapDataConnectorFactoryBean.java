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

import java.util.List;

import org.opensaml.xml.util.Pair;

import edu.internet2.middleware.shibboleth.common.config.attribute.resolver.dataConnector.BaseDataConnectorFactoryBean;
import edu.internet2.middleware.shibboleth.idp.util.IPRange;

/** Spring bean factory that produces {@link UserAgentAttributeMapDataConnector} data connectors. */
public class UserAgentAttributeMapDataConnectorFactoryBean extends BaseDataConnectorFactoryBean {

    /** Map from IP ranges to the attribute name/value pairs that they trigger. */
    private List<Pair<IPRange, Pair<String, String>>> attributeMappings;
    
    /**
     * Sets the mappings from IP ranges to attributes/values.
     * 
     * @param mappings mappings from IP ranges to attributes/values
     */
    public void setAttributeMappings(List<Pair<IPRange, Pair<String, String>>> mappings){
        attributeMappings = mappings;
    }
    
    /** {@inheritDoc} */
    public Class getObjectType() {
        return UserAgentAttributeMapDataConnector.class;
    }

    /** {@inheritDoc} */
    protected Object createInstance() throws Exception {
        UserAgentAttributeMapDataConnector connector = new UserAgentAttributeMapDataConnector();
        populateDataConnector(connector);

        connector.setAttributeMappings(attributeMappings);

        return connector;
    }
}
