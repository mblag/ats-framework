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
package com.axway.ats.rbv.db;

import java.util.ArrayList;

import com.axway.ats.common.PublicAtsApi;
import com.axway.ats.common.dbaccess.DbQuery;
import com.axway.ats.rbv.storage.SearchTerm;

@PublicAtsApi
public class DbSearchTerm implements SearchTerm {

    private DbQuery dbQuery;

    @PublicAtsApi
    public DbSearchTerm( DbQuery selectQuery ) {

        this.dbQuery = selectQuery;
    }

    @PublicAtsApi
    public DbSearchTerm( String selectSQL ) {

        this.dbQuery = new DbQuery( selectSQL, new ArrayList<Object>() );
    }

    public DbQuery getDbQuery() {

        return dbQuery;
    }
}
