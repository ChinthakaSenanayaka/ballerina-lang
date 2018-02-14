/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.wso2.ballerinalang.programfile;

import org.wso2.ballerinalang.compiler.semantics.model.types.BConnectorType;
import org.wso2.ballerinalang.compiler.semantics.model.types.BType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@code ConnectorInfo} contains metadata of a Ballerina connector entry in the program file.
 *
 * @since 0.87
 */
public class ConnectorInfo extends StructureTypeInfo {

    // Connector constructor signature
    public int signatureCPIndex;

    public BType[] paramTypes;

    public BConnectorType connectorType;

    public Map<String, ActionInfo> actionInfoMap = new HashMap<>();

    private boolean isFilterConnector = false;

    public ConnectorInfo(int pkgPathCPIndex, int nameCPIndex, int flags) {
        super(pkgPathCPIndex, nameCPIndex, flags);
    }

    public boolean isFilterConnector() {
        return isFilterConnector;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkgNameCPIndex, nameCPIndex);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ConnectorInfo && pkgNameCPIndex == (((ConnectorInfo) obj).pkgNameCPIndex)
                && nameCPIndex == (((ConnectorInfo) obj).nameCPIndex);
    }
}
