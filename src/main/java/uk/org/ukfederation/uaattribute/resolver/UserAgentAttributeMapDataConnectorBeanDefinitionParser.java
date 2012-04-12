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

package uk.org.ukfederation.uaattribute.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.Pair;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.internet2.middleware.shibboleth.common.config.attribute.resolver.dataConnector.BaseDataConnectorBeanDefinitionParser;
import edu.internet2.middleware.shibboleth.idp.util.IPRange;

/** Spring bean definition parser that creates {@link UserAgentAttributeMapDataConnectorFactoryBean} beans. */
public class UserAgentAttributeMapDataConnectorBeanDefinitionParser extends BaseDataConnectorBeanDefinitionParser {

    /** LDAP data connector type name. */
    public static final QName TYPE_NAME = new QName(ResolverNamespaceHandler.NAMESPACE, "UserAgentMappedAttributes");

    /** Local name of attribute. */
    public static final QName MAPPING_ELEMENT_NAME = new QName(ResolverNamespaceHandler.NAMESPACE, "Mapping");

    /** Name of the attribute carrying the CIDR block. */
    public static final String CIDR_BLOCK_ATTR_NAME = "cidrBlock";

    /** Name of the attribute carrying the ID of the IdP attribute. */
    public static final String ATTRIBUTE_ID_ATTR_NAME = "attributeId";

    /** Name of the attribute carrying the value of the IdP attribute. */
    public static final String ATTRIBUTE_VALUE_ATTR_NAME = "attributeValue";

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(UserAgentAttributeMapDataConnectorBeanDefinitionParser.class);

    /** {@inheritDoc} */
    protected Class getBeanClass(Element element) {
        return UserAgentAttributeMapDataConnectorFactoryBean.class;
    }

    /** {@inheritDoc} */
    protected void doParse(String pluginId, Element pluginConfig, Map<QName, List<Element>> pluginConfigChildren,
            BeanDefinitionBuilder pluginBuilder, ParserContext parserContext) {
        super.doParse(pluginId, pluginConfig, pluginConfigChildren, pluginBuilder, parserContext);

        List<Element> mappings =
                XMLHelper.getChildElementsByTagNameNS(pluginConfig, MAPPING_ELEMENT_NAME.getNamespaceURI(),
                        MAPPING_ELEMENT_NAME.getLocalPart());

        pluginBuilder.addPropertyValue("attributeMappings", parseAttributeMappings(mappings));
    }

    /**
     * Parses a set of Mapping elements.
     * 
     * @param mappings the mapping elements
     * 
     * @return the parsed forms of the elements
     */
    private List<Pair<IPRange, Pair<String, String>>> parseAttributeMappings(List<Element> mappings) {
        ArrayList<Pair<IPRange, Pair<String, String>>> parsedMappings =
                new ArrayList<Pair<IPRange, Pair<String, String>>>();

        String ipRangeString;
        String attributeId;
        String attributeValue;
        for (Element mapping : mappings) {
            ipRangeString = DatatypeHelper.safeTrimOrNullString(mapping.getAttributeNS(null, CIDR_BLOCK_ATTR_NAME));
            if (ipRangeString == null) {
                log.debug("Ignoring mapping with missing or empty CIDR block");
            }

            attributeId = DatatypeHelper.safeTrimOrNullString(mapping.getAttributeNS(null, ATTRIBUTE_ID_ATTR_NAME));
            if (attributeId == null) {
                log.debug("Ignoring mapping with missing or empty attribute ID");
            }

            attributeValue =
                    DatatypeHelper.safeTrimOrNullString(mapping.getAttributeNS(null, ATTRIBUTE_VALUE_ATTR_NAME));
            if (attributeValue == null) {
                log.debug("Ignoring mapping with missing or empty attribute value");
            }

            parsedMappings.add(new Pair<IPRange, Pair<String, String>>(IPRange.parseCIDRBlock(ipRangeString),
                    new Pair<String, String>(attributeId, attributeValue)));
        }

        return parsedMappings;
    }
}