/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package reciter.algorithm.evidence.cluster.clustersize;

import reciter.algorithm.cluster.model.ReCiterCluster;
import reciter.algorithm.evidence.cluster.RemoveClusterStrategy;
import reciter.algorithm.evidence.cluster.RemoveClusterStrategyContext;
import reciter.model.identity.Identity;

public class ClusterSizeStrategyContext implements RemoveClusterStrategyContext {

	private final RemoveClusterStrategy strategy;
	
	public ClusterSizeStrategyContext(RemoveClusterStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public double executeStrategy(ReCiterCluster reCiterCluster, Identity identity) {
		return strategy.executeStrategy(reCiterCluster, identity);
	}
}
