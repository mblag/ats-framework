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
package com.axway.ats.log.autodb.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.axway.ats.log.autodb.entities.CheckpointSummary;
import com.axway.ats.log.autodb.entities.LoadQueue;
import com.axway.ats.log.autodb.entities.Machine;
import com.axway.ats.log.autodb.entities.Message;
import com.axway.ats.log.autodb.entities.Run;
import com.axway.ats.log.autodb.entities.Scenario;
import com.axway.ats.log.autodb.entities.Statistic;
import com.axway.ats.log.autodb.entities.StatisticDescription;
import com.axway.ats.log.autodb.entities.Suite;
import com.axway.ats.log.autodb.entities.Testcase;
import com.axway.ats.log.autodb.exceptions.DatabaseAccessException;

public interface IDbReadAccess {

    public List<Run> getRuns(
                              int startRecord,
                              int recordsCount,
                              String whereClause,
                              String sortColumn,
                              boolean ascending ) throws DatabaseAccessException;

    public int getRunsCount(
                             String whereClause ) throws DatabaseAccessException;

    public List<Suite> getSuites(
                                  int startRecord,
                                  int recordsCount,
                                  String whereClause,
                                  String sortColumn,
                                  boolean ascending,
                                  boolean dateFormatNoYear ) throws DatabaseAccessException;

    public int getSuitesCount(
                               String whereClause ) throws DatabaseAccessException;

    public List<Scenario> getScenarios(
                                        int startRecord,
                                        int recordsCount,
                                        String whereClause,
                                        String sortColumn,
                                        boolean ascending,
                                        boolean dateFormatNoYear ) throws DatabaseAccessException;

    public int getScenariosCount(
                                  String whereClause ) throws DatabaseAccessException;

    public List<Testcase> getTestcases(
                                        int startRecord,
                                        int recordsCount,
                                        String whereClause,
                                        String sortColumn,
                                        boolean ascending,
                                        boolean dateFormatNoYear ) throws DatabaseAccessException;

    public int getTestcasesCount(
                                  String whereClause ) throws DatabaseAccessException;

    public List<Machine> getMachines() throws DatabaseAccessException;

    public List<Message> getMessages(
                                      int startRecord,
                                      int recordsCount,
                                      String whereClause,
                                      String sortColumn,
                                      boolean ascending ) throws DatabaseAccessException;

    public List<Message> getRunMessages(
                                         int startRecord,
                                         int recordsCount,
                                         String whereClause,
                                         String sortColumn,
                                         boolean ascending ) throws DatabaseAccessException;

    public List<Message> getSuiteMessages(
                                           int startRecord,
                                           int recordsCount,
                                           String whereClause,
                                           String sortColumn,
                                           boolean ascending ) throws DatabaseAccessException;

    public int getMessagesCount(
                                 String whereClause ) throws DatabaseAccessException;

    public int getRunMessagesCount(
                                    String whereClause ) throws DatabaseAccessException;

    public int getSuiteMessagesCount(
                                      String whereClause ) throws DatabaseAccessException;

    public List<StatisticDescription> getSystemStatisticDescriptions(
                                                                      float timeOffset,
                                                                      String testcaseIds,
                                                                      Map<String, String> testcaseAliases ) throws DatabaseAccessException;

    public List<StatisticDescription> getCheckpointStatisticDescriptions(
                                                                          float timeOffset,
                                                                          String testcaseIds,
                                                                          Map<String, String> testcaseAliases ) throws DatabaseAccessException;

    public List<Statistic> getSystemStatistics(
                                                float timeOffset,
                                                String testcaseIds,
                                                String machineIds,
                                                String statsTypeIds,
                                                Set<String> expectedStatisticUIDs,
                                                Set<Integer> expectedSingleStatisticIDs,
                                                Set<Integer> expectedCombinedStatisticIDs ) throws DatabaseAccessException;

    public List<Statistic> getSystemAggregatedStatistics(
                                                          float timeOffset,
                                                          String testcaseIds,
                                                          String machineIds,
                                                          String statsTypeIds,
                                                          int interval,
                                                          int mode ) throws DatabaseAccessException;

    public List<Statistic> getCheckpointStatistics(
                                                    float timeOffset,
                                                    String testcaseIds,
                                                    String actionNames,
                                                    Set<String> expectedSingleActionUIDs,
                                                    Set<String> expectedCombinedActionUIDs ) throws DatabaseAccessException;

    public List<Statistic> getCheckpointAggregatedStatistics(
                                                              float timeOffset,
                                                              String testcaseIds,
                                                              String actionNames,
                                                              Set<String> expectedSingleActionUIDs,
                                                              Set<String> expectedCombinedActionUIDs,
                                                              int interval,
                                                              int mode ) throws DatabaseAccessException;

    public List<LoadQueue> getLoadQueues(
                                            String whereClause,
                                            String sortColumn,
                                            boolean ascending,
                                            boolean dateFormatNoYear ) throws DatabaseAccessException;

    public List<CheckpointSummary> getCheckpointsSummary(
                                                          String whereClause,
                                                          String sortColumn,
                                                          boolean ascending ) throws DatabaseAccessException;
}
