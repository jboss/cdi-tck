/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.inheritance.initializer;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InitializerMethodInheritanceTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InitializerMethodInheritanceTest.class).build();
    }

    @Inject
    @Cheap
    BigDecimal cheap;

    @Inject
    @Expensive
    BigDecimal expensive;

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "dm")
    public void testManagedBeanDirectlyInheritsInitializer(@FirstLevel Citrus citrus) {
        assertEquals(citrus.getPrice(), cheap);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "do")
    public void testManagedBeanIndirectlyInheritsInitializer(@SecondLevel Orange orange, @SecondLevel Lemon lemon) {
        assertEquals(orange.getPrice(), cheap);
        assertEquals(lemon.getPrice(), expensive);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "dn")
    public void testSessionBeanDirectlyInheritsInitializer(@FirstLevel CitrusEjb citrus, @FirstLevel AppleEjb apple) {
        assertEquals(citrus.getPrice(), cheap);
        assertEquals(apple.getPrice(), expensive);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "dp")
    public void testSessionBeanIndirectlyInheritsInitializer(@SecondLevel OrangeEjb orange) {
        assertEquals(orange.getPrice(), cheap);
    }

}
