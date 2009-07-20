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
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/

package org.apache.wink.common;

/**
 * This interface replaces DispatchedPath annotation to declare a resource. It's
 * impossible to declare resource using both DynamicResource interface and
 * DispatchedPath annotation.
 * 
 * @see org.apache.wink.common.AbstractDynamicResource
 */
public interface DynamicResource {

    /**
     * returns the name of the bean The bean name must be unique and usually
     * should come from Spring If your bean is not generated by Spring, return
     * null and the unique bean name will be generated. If method bean name was
     * set using the setBeanName method, the method must return the same name
     * that was set.
     * 
     * @return
     */
    String getBeanName();

    /**
     * sets bean name. After this method was invoked, the getBeanName() must
     * return the same method that was set.
     * 
     * @param beanName
     */
    void setBeanName(String beanName);

    /**
     * Returns array of URI templates. Like value() of javax.ws.rs.Path
     * annotation.
     * 
     * @return array of URI templates
     */
    String[] getDispatchedPath();

    /**
     * <p>
     * A parent of this dispatched URI so the resulting dispatched URI is a
     * composition of the parent dispatched URI and this one. There can be more
     * than one parent.
     * <p>
     * Note that in comparison to the DispatchedPath annotation, the parent here
     * must be reference to the resource and not class.
     * <p>
     * In case there are no parents, return empty list.
     * <p>
     * If returns null, the default parent (RootResource) is used.
     * <p>
     * If method setParent was invoked, this method must return the same parent
     * that was set.
     * 
     * @return Resource instance of the parent dispatched URIs
     */
    Object[] getParents();

    /**
     * <p>
     * Sets parents. See getParents() for full description.
     * 
     * @param parents
     */
    void setParents(Object[] parents);

    /**
     * Returns the workspace title. If the title was already defined using
     * Workspace, the annotation data is overridden.
     * 
     * @return workspace title
     * @see org.apache.wink.common.annotations.Workspace
     */
    String getWorkspaceTitle();

    /**
     * Returns the collection title. If the title was already defined using
     * Workspace, the annotation data is overridden.
     * 
     * @return collection title
     * @see org.apache.wink.common.annotations.Workspace
     */
    String getCollectionTitle();
}
