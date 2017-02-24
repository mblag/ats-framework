/*
 * Copyright 2017 Axway Software
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axway.ats.uiengine.elements.swing;

import javax.swing.JMenuItem;

import org.fest.swing.fixture.JMenuItemFixture;

import com.axway.ats.common.PublicAtsApi;
import com.axway.ats.uiengine.UiDriver;
import com.axway.ats.uiengine.elements.UiButton;
import com.axway.ats.uiengine.elements.UiElementProperties;
import com.axway.ats.uiengine.exceptions.VerificationException;
import com.axway.ats.uiengine.utilities.swing.SwingElementState;

/**
 * A Swing Menu Item
 * <p>
 * Can be identified by:
 * <li>name
 * <li>text
 * <li>path
 * <li>label
 * </p>
 */
@PublicAtsApi
public class SwingMenuItem extends UiButton {

    private static final String[] RULES = { "label,visible",
            "label",
            "name,text,visible",
            "name,text",
            "name,visible",
            "name",
            "text,visible",
            "text",
            "path",
            "index"                    };

    static {
        SwingElementLocator.componentsMap.put( SwingMenuItem.class, JMenuItem.class );
    }

    public SwingMenuItem( UiDriver uiDriver,
                          UiElementProperties properties ) {

        super( uiDriver, properties );
        checkTypeAndRules( "Swing", RULES );
    }

    /**
     * Click menu item
     * @throws VerificationException if the element doesn't exist
     */
    @Override
    @PublicAtsApi
    public void click() {

        new SwingElementState( this ).waitToBecomeExisting();

        ( ( JMenuItemFixture ) SwingElementLocator.findFixture( this ) ).click();
    }

}
