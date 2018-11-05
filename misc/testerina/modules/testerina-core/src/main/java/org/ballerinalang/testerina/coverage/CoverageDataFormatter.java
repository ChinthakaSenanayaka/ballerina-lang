/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.ballerinalang.testerina.coverage;

import org.ballerinalang.bre.coverage.*;
import org.ballerinalang.config.ConfigRegistry;
import org.ballerinalang.testerina.core.entity.Test;
import org.ballerinalang.testerina.core.entity.TestSuite;
import org.ballerinalang.testerina.util.Constants;
import org.ballerinalang.util.codegen.LineNumberInfo;
import org.ballerinalang.util.debugger.LineNumberInfoHolder;
import org.ballerinalang.util.debugger.PackageLineNumberInfo;
import org.wso2.ballerinalang.compiler.util.Names;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoverageDataFormatter {

    private static PrintStream errStream = System.err;
    private static PrintStream outStream = System.out;

    private Map<String, LineNumberInfoHolder> lineNumberInfoHolderForProject;

    private String testName = null;

    private static final String COVERAGE_DATA_NAME = "coverage.data.name";

    private static final String COVERAGE_DATA_PATH = "coverage.data.path";

    private static final String COVERAGE_REPORT_NAME = "coverage.report.name";

    private static final String COVERAGE_REPORT_PATH = "coverage.report.path";

    private static final String COVERAGE_DATA_FOLDER = "coverage";

    private static final String COVERAGE_REPORT_FOLDER = Paths.get(COVERAGE_DATA_FOLDER, "report").toString();

    private static final String COVERAGE_DATA_FILE = "coverage.info";

    private static final String COVERAGE_REPORT_TARGET_PATH = "/target";

    private static final String LCOV_TN = "TN:";

    private static final String LCOV_SF = "SF:";

    private static final String LCOV_DA = "DA:";

    private static final String LCOV_EOR = "end_of_record";

    public CoverageDataFormatter() {
        CoverageManager coverageManager = CoverageManager.getInstance();
        lineNumberInfoHolderForProject = coverageManager.getLineNumberInfoHolderForProject();
    }

    public List<LCovData> getFormattedCoverageData(Map<String, List<ExecutedInstruction>> executedInstructionOrderMap,
                                                   Map<String, TestSuite> testSuiteForProject) {

        List<LCovData> packageCoverageList = new ArrayList<>();

        testSuiteForProject.forEach((testPkgPath, suite) -> {

            String entryPkgPath = suite.getProgramFile().getEntryPackage().pkgPath;
            LineNumberInfoHolder lineNumberInfoHolderForPkg = lineNumberInfoHolderForProject.get(entryPkgPath);
            PackageLineNumberInfo entryPkgLineNumberInfo = lineNumberInfoHolderForPkg.getPackageInfoMap().get(entryPkgPath);

            Map<String, Integer> fileLineCoverage = new HashMap<>();
            entryPkgLineNumberInfo.getLineNumbers().keySet().forEach(key -> {
                String fileNameWithPkg = entryPkgPath + File.separator + key.split(":")[0];
                fileLineCoverage.put(fileNameWithPkg,
                        fileLineCoverage.get(fileNameWithPkg) == null ? 1 : fileLineCoverage.get(fileNameWithPkg) + 1);
            });

            executedInstructionOrderMap.forEach((packagePath, executedInstructionOrder) -> {

                // get only current project's module
                if(packagePath.equals(entryPkgPath)) {

                    for (ExecutedInstruction executedInstruction : executedInstructionOrder) {

                        //TODO: init should be covered later. init has declarions and global configs for functions
                        //TODO: start should be covered later. start has declarions and global configs for services
                        if (!(executedInstruction.getPkgPath().matches("ballerina/.*"))) {

                            if (!(executedInstruction.getFunctionName().matches(".*\\." + Names.INIT_ACTION_SUFFIX) ||
                                    executedInstruction.getFunctionName().matches(".*\\." + Names.START_ACTION_SUFFIX))) {

                                // filter out the source code Ips
                                if (skipTestFunctionIps(executedInstruction, suite)) {
                                    continue;
                                }

                                boolean lCovDataFound = false;
                                for (LCovData lCovData : packageCoverageList) {

                                    if (lCovData.getTestName().equals(testName)) {
                                        lCovDataFound = true;

                                        boolean lCovSourceFileFound = false;
                                        for (LCovSourceFile lCovSourceFile : lCovData.getlCovSourceFileList()) {

                                            if (lCovSourceFile.getSourceFilePath().equals(packagePath + File.separator + executedInstruction.getFileName())) {
                                                lCovSourceFileFound = true;

                                                boolean lineNumFound = false;
                                                LineNumberInfo lineNumberInfo = entryPkgLineNumberInfo
                                                        .getLineNumberInfo(executedInstruction.getIp());
                                                for (LCovDA lCovDA : lCovSourceFile.getlCovDAList()) {
                                                    if (lineNumberInfo.getLineNumber() == lCovDA.getLineNumber()) {
                                                        lineNumFound = true;

                                                        lCovDA.setLineExecutionCount(lCovDA.getLineExecutionCount() + 1);

                                                        break;
                                                    }
                                                }
                                                if (!lineNumFound) {
                                                    LCovDA lCovDA = new LCovDA(lineNumberInfo.getLineNumber(), 1, 0);
                                                    lCovSourceFile.getlCovDAList().add(lCovDA);

                                                    lCovSourceFile.setNumOfLineExecuted(lCovSourceFile.getNumOfLineExecuted() + 1);
                                                }

                                                break;
                                            }
                                        }
                                        if (!lCovSourceFileFound) {

                                            LineNumberInfo lineNumberInfo = entryPkgLineNumberInfo
                                                    .getLineNumberInfo(executedInstruction.getIp());
                                            LCovDA lCovDA = new LCovDA(lineNumberInfo.getLineNumber(), 1, 0);

                                            LCovSourceFile lCovSourceFile = new LCovSourceFile(
                                                    //TODO: package path should be package folder path
                                                    packagePath + File.separator + executedInstruction.getFileName(), 1,
                                                    fileLineCoverage.get(packagePath + File.separator + executedInstruction.getFileName()));
                                            lCovSourceFile.getlCovDAList().add(lCovDA);

                                            lCovData.getlCovSourceFileList().add(lCovSourceFile);

                                        }

                                        break;
                                    }


                                }
                                if (!lCovDataFound) {

                                    LineNumberInfo lineNumberInfo = entryPkgLineNumberInfo
                                            .getLineNumberInfo(executedInstruction.getIp());
                                    LCovDA lCovDA = new LCovDA(lineNumberInfo.getLineNumber(), 1, 0);

                                    LCovSourceFile lCovSourceFile = new LCovSourceFile(
                                            //TODO: package path should be package folder path
                                            packagePath + File.separator + executedInstruction.getFileName(), 1,
                                            fileLineCoverage.get(packagePath + File.separator + executedInstruction.getFileName()));
                                    lCovSourceFile.getlCovDAList().add(lCovDA);

                                    LCovData lCovData = new LCovData(testName);
                                    lCovData.getlCovSourceFileList().add(lCovSourceFile);

                                    packageCoverageList.add(lCovData);
                                }

                            }

                        }

                    }

                }

            });

        });

        return packageCoverageList;

    }

    private boolean skipTestFunctionIps(ExecutedInstruction executedInstruction, TestSuite suite) {

        boolean skipTestFunctionIps = false;

        if(suite.getBeforeSuiteFunctionNames().contains(executedInstruction.getFunctionName())) {
            skipTestFunctionIps = true;
        }

        for(Test test : suite.getTests()) {
            if(executedInstruction.getFunctionName().equals(test.getBeforeTestFunction())) {
                skipTestFunctionIps = true;
                break;
            }

            if(executedInstruction.getFunctionName().equals(test.getTestName())) {
                testName = executedInstruction.getFunctionName();
                skipTestFunctionIps = true;
                break;
            }

            if(executedInstruction.getFunctionName().equals(test.getAfterTestFunction())) {
                skipTestFunctionIps = true;
                break;
            }
        }

        if(suite.getAfterSuiteFunctionNames().contains(executedInstruction.getFunctionName())) {
           skipTestFunctionIps = true;
        }

        return skipTestFunctionIps;
    }

    public void writeFormattedCovDataToFile(List<LCovData> packageLCovDataList, String sourceRoot) throws IOException {

        ConfigRegistry configRegistry = ConfigRegistry.getInstance();
        String customCovReportname = configRegistry.getAsString(COVERAGE_DATA_NAME);
        String customCovReportPath = configRegistry.getAsString(COVERAGE_DATA_PATH);

        String projectOutputPath = sourceRoot + COVERAGE_REPORT_TARGET_PATH;
        StringBuffer lcovOutputStrBuf = new StringBuffer();

        String covReportFilePath;
        if(customCovReportPath ==  null) {
            covReportFilePath = Paths.get(projectOutputPath, COVERAGE_DATA_FOLDER).toString();
        } else {
            covReportFilePath = Paths.get(customCovReportPath, COVERAGE_DATA_FOLDER).toString();
        }

        String covReportFileName;
        if(customCovReportname ==  null) {
            covReportFileName = COVERAGE_DATA_FILE;
        } else {
            covReportFileName = customCovReportname;
        }

        for(LCovData lCovData : packageLCovDataList) {
            lcovOutputStrBuf.append(LCOV_TN).append(lCovData.getTestName()).append(Constants.COMMA);

            for(LCovSourceFile lCovSourceFile : lCovData.getlCovSourceFileList()) {
                lcovOutputStrBuf.append(LCOV_SF).append(Paths.get(sourceRoot, lCovSourceFile.getSourceFilePath()))
                        .append(Constants.NEWLINE);
                for(LCovDA lCovDA : lCovSourceFile.getlCovDAList()) {
                    lcovOutputStrBuf.append(LCOV_DA).append(lCovDA.getLineNumber()).append(Constants.COMMA)
                            .append(lCovDA.getLineExecutionCount()).append(Constants.NEWLINE);
                }

                lcovOutputStrBuf.append(LCOV_EOR).append(Constants.NEWLINE);
                float fileCoveragePercentage = (((float) lCovSourceFile.getNumOfLineExecuted()) / ((float) lCovSourceFile.getNumOfInstrumentedLines())) * 100;
                outStream.println("Coverage for " + lCovSourceFile.getSourceFilePath() + " is " + fileCoveragePercentage + Constants.PERCENTAGE);
            }
        }

        BufferedWriter covReportFileBufWriter = null;
        try {

            File covReportDir = new File(covReportFilePath);
            if(covReportDir.exists()) {
                covReportDir.delete();
            }
            covReportDir.mkdirs();

            File covReportFile = new File(covReportDir, covReportFileName);
            FileWriter covReportFileWriter = new FileWriter(covReportFile);
            covReportFileBufWriter = new BufferedWriter(covReportFileWriter);

            covReportFileBufWriter.write(lcovOutputStrBuf.toString());

            outStream.println("Coverage report is written to " + covReportFilePath + File.separator + covReportFileName);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if(covReportFileBufWriter != null) {
                covReportFileBufWriter.close();
            }
        }
    }
}
