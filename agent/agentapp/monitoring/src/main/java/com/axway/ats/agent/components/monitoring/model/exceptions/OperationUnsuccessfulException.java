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
package com.axway.ats.agent.components.monitoring.model.exceptions;

/**
 * Thrown whenever the operation that is called has terminated abnormally
 */
@SuppressWarnings("serial")
public class OperationUnsuccessfulException extends Exception {

    /**
     * Constructor
     */
    public OperationUnsuccessfulException() {

        super();
    }

    /**
     * Constructor
     *
     * @param message
     * @param throwable
     */
    public OperationUnsuccessfulException( String message,
                                           Throwable throwable ) {

        super( message +" Cause message and trace: " + throwable.getMessage(), throwable );
    }

    /**
     * Constructor
     *
     * @param arg0
     */
    public OperationUnsuccessfulException( String arg0 ) {

        super( arg0 );
    }
}
