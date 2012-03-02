/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.context.passivating.validation;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Collections;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies that a decorator that is passivation capable while having non-passivation capable dependencies is allowed provided
 * it does not decorate a bean declaring passivation scope.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DecoratorWithNonPassivationCapableDependenciesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(DecoratorWithNonPassivationCapableDependenciesTest.class)
                .withClasses(Engine.class, Ferry.class, Vessel.class, VesselDecorator.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createDecorators().clazz(VesselDecorator.class.getName())
                                .up()).build();
    }

    @Test
    @SpecAssertion(section = "6.6.4", id = "l")
    public void testDeploymentValid() {
        // it is enough to verify that the deployment passes validation and deploys
        assertEquals(1, getCurrentManager().resolveDecorators(Collections.<Type> singleton(Ferry.class)).size());
    }
}