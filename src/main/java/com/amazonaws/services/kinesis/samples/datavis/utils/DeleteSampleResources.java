/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.services.kinesis.samples.datavis.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;

/**
 * Delete all resources used by the sample application.
 */
public class DeleteSampleResources {
    private static final Log LOG = LogFactory.getLog(DeleteSampleResources.class);

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: " + DeleteSampleResources.class.getSimpleName()
                    + " <application name> <stream name> <DynamoDB table name>");
            System.exit(1);
        }

        String applicationName = args[0];
        String streamName = args[1];
        String countsTableName = args[2];

        AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
        ClientConfiguration clientConfig = SampleUtils.configureUserAgentForSample(new ClientConfiguration());
        AmazonKinesis kinesis = new AmazonKinesisClient(credentialsProvider, clientConfig);
        AmazonDynamoDB dynamoDB = new AmazonDynamoDBClient(credentialsProvider, clientConfig);

        StreamUtils streamUtils = new StreamUtils(kinesis);
        DynamoDBUtils dynamoDBUtils = new DynamoDBUtils(dynamoDB);

        LOG.info("Removing Amazon Kinesis and DynamoDB resources used by the sample application...");

        streamUtils.deleteStream(streamName);
        // The Kinesis Client Library creates a table to manage shard leases and uses the application name for its name.
        dynamoDBUtils.deleteTable(applicationName);
        dynamoDBUtils.deleteTable(countsTableName);
    }
}
